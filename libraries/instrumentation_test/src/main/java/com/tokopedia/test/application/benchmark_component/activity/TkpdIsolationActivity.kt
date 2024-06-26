package com.tokopedia.test.application.benchmark_component.activity

/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.os.Process
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.AnyThread
import androidx.annotation.RestrictTo
import androidx.annotation.WorkerThread
import androidx.benchmark.IsolationActivity
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.instrumentation.test.R
import com.tokopedia.test.application.benchmark_component.CpuInfo
import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.thread

/**
 * Simple opaque activity used to reduce benchmark interference from other windows.
 *
 * For example, sources of potential interference:
 * - live wallpaper rendering
 * - homescreen widget updates
 * - hotword detection
 * - status bar repaints
 * - running in background (some cores may be foreground-app only)
 *
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class TkpdIsolationActivity : android.app.Activity() {
    private var destroyed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        themeRes?.let { setTheme(it) }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tkpd_isolation_activity)

        // disable launch animation
        overridePendingTransition(0, 0)

        if (firstInit) {
            if (!CpuInfo.locked && isSustainedPerformanceModeSupported()) {
                sustainedPerformanceModeInUse = true
            }
            application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)

            // trigger the one missed lifecycle event, from registering the callbacks late
            activityLifecycleCallbacks.onActivityCreated(this, savedInstanceState)

            if (sustainedPerformanceModeInUse) {
                // Keep at least one core busy. Together with a single threaded benchmark, this
                // makes the process get multi-threaded setSustainedPerformanceMode.
                //
                // We want to keep to the relatively lower clocks of the multi-threaded benchmark
                // mode to avoid any benchmarks running at higher clocks than any others.
                //
                // Note, thread names have 15 char max in Systrace
                thread(name = "BenchSpinThread") {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST)
                    while (true) {
                    }
                }
            }
            firstInit = false
        }

        val old = singleton.getAndSet(this)
        if (old != null && !old.destroyed && !old.isFinishing) {
            throw IllegalStateException("Only one IsolationActivity should exist")
        }

        findViewById<TextView>(R.id.clock_state).text = when {
            CpuInfo.locked -> "Locked Clocks"
            sustainedPerformanceModeInUse -> "Sustained Performance Mode"
            else -> ""
        }
    }

    override fun onResume() {
        super.onResume()
        resumed = true
    }

    override fun onPause() {
        super.onPause()
        resumed = false
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyed = true
    }

    /** finish is ignored! we defer until [actuallyFinish] is called. */
    override fun finish() {
    }

    fun actuallyFinish() {
        // disable close animation
        overridePendingTransition(0, 0)
        super.finish()
    }

    companion object {
        private const val TAG = "Benchmark"
        internal val singleton = AtomicReference<TkpdIsolationActivity>()
        private var firstInit = true
        internal var sustainedPerformanceModeInUse = false
            private set
        var resumed = false
            private set
        private var themeRes: Int? = null

        @WorkerThread
        fun launchSingleton(theme: Int? = null) {
            val intent = Intent(Intent.ACTION_MAIN).apply {
                Log.d(TAG, "launching Benchmark IsolationActivity")
                theme?.let {
                    themeRes = it
                }
                setClassName(
                        InstrumentationRegistry.getInstrumentation().targetContext.packageName,
                        TkpdIsolationActivity::class.java.name
                )
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            InstrumentationRegistry.getInstrumentation().startActivitySync(intent)
        }

        @AnyThread
        fun finishSingleton() {
            Log.d(TAG, "Benchmark runner being destroyed, tearing down activities")
            singleton.getAndSet(null)?.apply {
                runOnUiThread {
                    actuallyFinish()
                }
            }
        }

        internal fun isSustainedPerformanceModeSupported(): Boolean =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val context = InstrumentationRegistry.getInstrumentation().targetContext
                    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
                    powerManager.isSustainedPerformanceModeSupported
                } else {
                    false
                }

        private val activityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
            @SuppressLint("NewApi") // window API guarded by [isSustainedPerformanceModeSupported]
            override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
                if (sustainedPerformanceModeInUse) {
                    activity.window.setSustainedPerformanceMode(true)
                }

                // Forcibly wake the device, and keep the screen on to prevent benchmark failures.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                    val keyguardManager =
                            activity.getSystemService(KEYGUARD_SERVICE) as KeyguardManager
                    keyguardManager.requestDismissKeyguard(activity, null)
                    activity.setShowWhenLocked(true)
                    activity.setTurnScreenOn(true)
                    activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                } else {
                    @Suppress("DEPRECATION")
                    activity.window.addFlags(
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                                    or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                                    or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    )
                }
            }

            override fun onActivityDestroyed(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
        }
    }
}