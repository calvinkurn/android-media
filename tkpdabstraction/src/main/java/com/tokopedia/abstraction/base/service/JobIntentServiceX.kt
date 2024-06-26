package com.tokopedia.abstraction.base.service

import android.annotation.TargetApi
import android.app.job.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import android.util.SparseArray
import java.util.concurrent.Executor
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.atomic.AtomicBoolean

abstract class JobIntentServiceX(val mExecutor: Executor) : JobService() {

    // Default constructor uses deprecated but still useful executor
    @Suppress("DEPRECATION")
    constructor() : this(AsyncTask.SERIAL_EXECUTOR) {
    }

    abstract fun onHandleWork(intent: Intent)

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT < ANDROID_O_SDK) {
            val app = applicationContext
            val pm = app.getSystemService(Context.POWER_SERVICE) as PowerManager
            val tag = javaClass.name + ":running"
            mRunningWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, tag).apply {
                setReferenceCounted(false)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mRunningWakeLock?.also {
            if (it.isHeld) {
                it.release()
            }
        }
        mRunningWakeLock = null
    }

    // Old implementation (before 8.0)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand $intent, $flags, $startId")

        if (Build.VERSION.SDK_INT < ANDROID_O_SDK) {
            // Release the starting wake lock
            synchronized(mStartingWakeLockLock) {
                mStartingWakeLock?.also {
                    if (it.isHeld) {
                        try {
                            it.release()
                        } catch (ignore: Exception) {
                        }
                    }
                }
            }

            if (intent != null) {
                mRunningIntentCount += 1
                mRunningWakeLock?.acquire(TIMEOUT_1)

                try {
                    mExecutor.execute(OldIntentRunnable(this, mHandler, intent, startId))
                } catch (x: RejectedExecutionException) {
                    // Could not enqueue, ignore is the only thing we can do
                    mRunningIntentCount -= 1
                    if (mRunningIntentCount == 0) {
                        mRunningWakeLock?.also {
                            try {
                                it.release()
                            } catch (ignore: Exception) {
                            }
                        }
                        return START_NOT_STICKY
                    }
                }

                return START_REDELIVER_INTENT
            }
        }

        return START_NOT_STICKY
    }

    // New implementation (8.0 or newer)
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.i(TAG, "onStartJob $params")

        if (params != null) {
            val jobId = params.jobId

            val jobOld = mRunningJobList.get(jobId)
            jobOld?.stopRequested?.set(true)

            val jobNew =
                if (Build.VERSION.SDK_INT >= ANDROID_O_SDK) NewJobRunnable(this, mHandler, params)
                else null

            if (jobNew != null) {
                mRunningJobList.put(jobId, jobNew)
                try {
                    mExecutor.execute(jobNew)
                } catch (x: RejectedExecutionException) {
                    // Could not enqueue, ask Android to retry
                    return false
                }
            }

            return true
        }

        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        if (params != null) {
            val jobId = params.jobId
            val jobOld = mRunningJobList.get(jobId)
            if (jobOld != null) {
                jobOld.stopRequested.set(true)
                mRunningJobList.remove(jobId)
                return true
            }
        }

        return false
    }

    private fun onHandlerMessage(msg: Message): Boolean {
        when (msg.what) {
            WHAT_OLD_INTENT_RUNNABLE_DONE -> {
                val r = msg.obj as OldIntentRunnable

                stopSelf(r.startId)

                mRunningIntentCount -= 1
                if (mRunningIntentCount == 0) {
                    mRunningWakeLock?.also {
                        if (it.isHeld) {
                            try {
                                it.release()
                            } catch (ignore: Exception) {
                            }
                        }
                    }
                }
            }
            WHAT_NEW_JOB_RUNNABLE_DONE -> {
                val r = msg.obj as NewJobRunnable
                val jobId = r.params.jobId

                if (mRunningJobList.get(jobId) == r) {
                    mRunningJobList.remove(jobId)
                }
            }
            else -> return false
        }

        return true
    }

    // Intent runnable - "old" implementation
    private class OldIntentRunnable(val service: JobIntentServiceX,
                                    val handler: Handler,
                                    val intent: Intent,
                                    val startId: Int) : Runnable {
        override fun run() {
            service.onHandleWork(intent)

            handler.obtainMessage(WHAT_OLD_INTENT_RUNNABLE_DONE, this).sendToTarget()
        }
    }

    // Job runnable - "new" implementation
    private open class JobRunnable() {
        val stopRequested = AtomicBoolean(false)
    }

    @TargetApi(ANDROID_O_SDK)
    private class NewJobRunnable(val service: JobIntentServiceX,
                                 val handler: Handler,
                                 val params: JobParameters) : JobRunnable(), Runnable {

        override fun run() {
            while (!stopRequested.get()) {
                val work = next() ?: break
                service.onHandleWork(work.intent)
                try {
                    params.completeWork(work)
                } catch (ignore: Exception) {
                }
            }

            handler.obtainMessage(WHAT_NEW_JOB_RUNNABLE_DONE, this).sendToTarget()
        }

        fun next(): JobWorkItem? {
            return try {
                return params.dequeueWork()
            } catch (ignore: Exception) {
                null
            }
        }
    }

    // Enqueue compat and implementations
    private abstract class EnqueueCompat {
        abstract fun enqueueWork(context: Context, cn: ComponentName,
                                 jobId: Int, intent: Intent)
    }

    private class EnqueueCompatOld : EnqueueCompat() {
        override fun enqueueWork(context: Context, cn: ComponentName, jobId: Int, intent: Intent) {
            // Acquire the starting wake lock
            synchronized(mStartingWakeLockLock) {
                var wl = mStartingWakeLock
                if (wl == null) {
                    val app = context.applicationContext
                    val pm = app.getSystemService(Context.POWER_SERVICE) as PowerManager
                    val tag = JobIntentServiceX::class.java.name + ":start"
                    wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, tag)
                    mStartingWakeLock = wl
                }

                wl?.acquire(TIMEOUT_2)
            }

            // Start the service
            val service = Intent(intent).apply {
                component = cn
            }
            context.startService(service)
        }
    }

    @TargetApi(ANDROID_O_SDK)
    private class EnqueueCompatNew : EnqueueCompat() {
        override fun enqueueWork(context: Context, cn: ComponentName, jobId: Int, intent: Intent) {
            val app = context.applicationContext
            val scheduler = app.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val job = JobInfo.Builder(jobId, cn).apply {
                setOverrideDeadline(0)
            }.build()
            val work = JobWorkItem(intent)
            scheduler.enqueue(job, work)
        }
    }

    companion object {
        fun enqueueWork(context: Context, cn: ComponentName,
                        jobId: Int, intent: Intent) {
            val compat = makeEnqueueCompat()
            compat.enqueueWork(context, cn, jobId, intent)
        }

        fun <T : JobIntentServiceX> enqueueWork(context: Context, cls: Class<T>,
                                                jobId: Int, intent: Intent) {
            enqueueWork(context, ComponentName(context, cls), jobId, intent)
        }

        private fun makeEnqueueCompat(): EnqueueCompat {
            return if (Build.VERSION.SDK_INT >= ANDROID_O_SDK) EnqueueCompatNew()
            else EnqueueCompatOld()
        }

        private const val TAG = "JobIntentServiceX"

        private const val WHAT_OLD_INTENT_RUNNABLE_DONE = 0
        private const val WHAT_NEW_JOB_RUNNABLE_DONE = 1
        private const val ANDROID_O_SDK = 26
        private const val TIMEOUT_1 = 60 * 1000L
        private const val TIMEOUT_2 = 15 * 1000L
        private val mStartingWakeLockLock = Any()
        private var mStartingWakeLock: PowerManager.WakeLock? = null
    }

    private val mHandler = Handler(Looper.getMainLooper(), this::onHandlerMessage)
    private var mRunningIntentCount = 0
    private var mRunningWakeLock: PowerManager.WakeLock? = null
    private val mRunningJobList = SparseArray<JobRunnable>()
}