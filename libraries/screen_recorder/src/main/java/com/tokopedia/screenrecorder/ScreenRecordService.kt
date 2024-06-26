package com.tokopedia.screenrecorder

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.media.MediaScannerConnection
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import com.tokopedia.utils.file.PublicFolderUtil
import kotlinx.coroutines.*
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext
import com.tokopedia.unifyprinciples.Typography

const val ANDROID_LOLLIPOP = 21

@TargetApi(ANDROID_LOLLIPOP)
class ScreenRecordService : Service(), CoroutineScope {

    companion object {
        const val ACTION_INIT = "ACTION_INIT"
        const val EXTRA_MEDIA_PROEJECTION_RESULT_CODE = "EXTRA_MEDIA_PROEJECTION_RESULT_CODE"
        const val EXTRA_MEDIA_PROEJECTION_RESULT_DATA = "EXTRA_MEDIA_PROEJECTION_RESULT_DATA"
        const val EXTRA_RECORD_MIC = "EXTRA_RECORD_MIC"

        private const val ONE_SECOND_MS = 1000L
        private const val HALF_SECOND_MS = 500L

        private const val MAX_RECORD_DURATION_SECOND = 60
        private const val PRE_RECORD_COUNTDOWN_SECOND = 3

        private const val PRE_RECORD_COUNTDOWN_TEXT_SIZE_SP = 32f

        private const val VIDEO_WIDTH = 480
        private const val VIDEO_HEIGHT = 720

        private const val VIDEO_BITRATE = 1500000

        private const val FILENAME_RESULT = "result.mp4"

        private const val ACTION_FINISH = "ACTION_FINISH"
        private const val ACTION_START_RECORD = "ACTION_START_RECORD"
        private const val ACTION_STOP_RECORD = "ACTION_STOP_RECORD"

        private const val VIRTUAL_DISPLAY_NAME = "Tokopedia Screen Recorder"

        private const val LOW_PRIO_CHANNEL_ID = "LowPrioTkpdScreenRecord"
        private const val LOW_PRIO_CHANNEL_NAME = "Low Priority Tokopedia Screen Record Notif Channel"

        private const val HIGH_PRIO_CHANNEL_ID = "HiPrioTkpdScreenRecord"
        private const val HIGH_PRIO_CHANNEL_NAME = "High Priority Tokopedia Screen Record Notif Channel"
        private const val NOTIF_ID = 1
    }

    protected val masterJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + masterJob

    val backgroundCoroutineContext: CoroutineContext
        get() = Dispatchers.IO + masterJob

    private var mediaRecorder: MediaRecorder? = null
    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null

    var notificationManager: NotificationManager? = null

    lateinit var ongoingNotifBuilder: NotificationCompat.Builder
    lateinit var preparationNotifBuilder: NotificationCompat.Builder

    private lateinit var internalStoragePath: String
    private lateinit var resultVideoPath: String

    lateinit var preRecordCountDownText: Typography
    lateinit var mWindowManager: WindowManager

    override fun onCreate() {
        super.onCreate()
        internalStoragePath = getExternalFilesDir(Environment.DIRECTORY_MOVIES).toString() + "/"
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val action = intent.getAction()
        val projectionResultCode = intent.getIntExtra(EXTRA_MEDIA_PROEJECTION_RESULT_CODE, 0)
        val projectionResultData = intent.getParcelableExtra<Intent>(EXTRA_MEDIA_PROEJECTION_RESULT_DATA)
        val isRecordMic = intent.getBooleanExtra(EXTRA_RECORD_MIC, false)

        when (action) {
            ACTION_INIT -> init(projectionResultCode, projectionResultData!!, isRecordMic)
            ACTION_START_RECORD -> startPreRecordCountDown()
            ACTION_STOP_RECORD -> {
                masterJob.cancel()
                stopRecord()
            }
            ACTION_FINISH -> finish()
        }

        return START_NOT_STICKY
    }

    private fun init(projectionResultCode: Int, projectionResultData: Intent, isRecordMic: Boolean) {
        try {

            createNotificationChannel()

            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            ongoingNotifBuilder = NotificationCompat.Builder(applicationContext, LOW_PRIO_CHANNEL_ID)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setContentTitle(getString(R.string.screen_record_notif_title_recording_screen))
                    .addAction(R.drawable.screen_recorder_ic_stop_black_24dp,
                            getString(R.string.screen_recorder_notif_stop), buildPendingIntent(ACTION_STOP_RECORD))
                    .setSmallIcon(R.drawable.screen_recorder_ic_notify_white)
            preparationNotifBuilder = NotificationCompat.Builder(applicationContext, LOW_PRIO_CHANNEL_ID)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setContentTitle(getString(R.string.screen_record_notif_title_preparation))
                    .setSmallIcon(R.drawable.screen_recorder_ic_notify_white)

            val startServiceNotif = NotificationCompat.Builder(applicationContext, HIGH_PRIO_CHANNEL_ID)
                    .setContentTitle(getString(R.string.screen_record_notif_title_preparing))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setSmallIcon(R.drawable.screen_recorder_ic_notify_white).build()

            val recorderReadyNotif = NotificationCompat.Builder(applicationContext, HIGH_PRIO_CHANNEL_ID)
                    .setContentTitle(getString(R.string.screen_record_notif_title_ready_to_record))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentText(getString(R.string.screen_record_notif_text_ready_to_record))
                    .addAction(R.drawable.screen_recorder_ic_videocam_black_24dp,
                            getString(R.string.screen_recorder_notif_record), buildPendingIntent(ACTION_START_RECORD))
                    .addAction(R.drawable.screen_recorder_ic_close_black_24dp,
                            getString(R.string.screen_recorder_notif_finish), buildPendingIntent(ACTION_FINISH))
                    .setSmallIcon(R.drawable.screen_recorder_ic_notify_white).build()

            startForeground(NOTIF_ID, startServiceNotif)

            mediaRecorder = MediaRecorder()

            if (isRecordMic) {
                mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            }

            mediaRecorder?.setVideoSource(MediaRecorder.VideoSource.SURFACE)

            lateinit var profile: CamcorderProfile
            if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_480P)) {
                profile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P)
            } else {
                profile = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW)
            }

            profile.videoFrameWidth = VIDEO_WIDTH
            profile.videoFrameHeight = VIDEO_HEIGHT

            mediaRecorder?.setOutputFormat(profile.fileFormat)

            if (isRecordMic) {
                mediaRecorder?.setAudioEncoder(profile.audioCodec)
                mediaRecorder?.setAudioChannels(profile.audioChannels)
                mediaRecorder?.setAudioEncodingBitRate(profile.audioBitRate)
                mediaRecorder?.setAudioSamplingRate(profile.audioSampleRate)
            }

            mediaRecorder?.setVideoFrameRate(profile.videoFrameRate)
            mediaRecorder?.setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight)
            mediaRecorder?.setVideoEncodingBitRate(VIDEO_BITRATE)
            mediaRecorder?.setVideoEncoder(profile.videoCodec)

            mediaRecorder?.setOutputFile(internalStoragePath + FILENAME_RESULT)

            mediaRecorder?.prepare()

            //short delay to give time after start service and before request media projection
            //to avoid "Media projections require a foreground service of type ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION" issue
            Thread.sleep(HALF_SECOND_MS)

            val projectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
            mediaProjection = projectionManager
                    .getMediaProjection(projectionResultCode, projectionResultData) as MediaProjection

            virtualDisplay = mediaProjection?.createVirtualDisplay(
                    VIRTUAL_DISPLAY_NAME,
                    profile.videoFrameWidth, profile.videoFrameHeight, resources.displayMetrics.densityDpi,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    mediaRecorder?.surface, null, null)

            notificationManager?.notify(NOTIF_ID, recorderReadyNotif)
        } catch (e: Exception) {
            handleError()
        }
    }

    private fun handleError() {
        infoErrorToUser()
        finish()
    }

    private fun infoErrorToUser() {
        notificationManager?.notify(NOTIF_ID, buildErrorNotification())
    }

    private fun buildErrorNotification(): Notification {
        return NotificationCompat.Builder(applicationContext, HIGH_PRIO_CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(getString(R.string.screen_record_notif_title_error))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.screen_recorder_ic_notify_white).build()
    }

    private fun buildPendingIntent(action: String): PendingIntent? {
        val i = Intent(applicationContext, javaClass)
        i.action = action
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getService(applicationContext, 0, i,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getService(applicationContext, 0, i, 0)
        }
    }

    private fun startRecord() {
        mediaRecorder?.start()
        startDurationCountDown()
    }

    private fun startPreRecordCountDown() {
        try {
            showPreparationNotif()
            showPreRecordCountDown()
            var remainingDurationSecond = PRE_RECORD_COUNTDOWN_SECOND
            launch {
                while (remainingDurationSecond > 0) {
                    preRecordCountDownText.text = remainingDurationSecond.toString()
                    withContext(backgroundCoroutineContext) {
                        Thread.sleep(ONE_SECOND_MS)
                    }
                    remainingDurationSecond--
                }
                hidePreRecordCountDown()
                startRecord()
            }
        } catch (e: Exception) {
            hidePreRecordCountDown()
            startRecord()
        }
    }

    private fun showPreparationNotif() {
        val notif = preparationNotifBuilder.build()
        notificationManager?.notify(NOTIF_ID, notif)
    }

    private fun startDurationCountDown() {
        var remainingDurationSecond = MAX_RECORD_DURATION_SECOND
        launch {
            while (remainingDurationSecond > 0) {
                val notif =
                        ongoingNotifBuilder.setContentText(formatRemainingTime(remainingDurationSecond)).build()
                notificationManager?.notify(NOTIF_ID, notif)
                withContext(backgroundCoroutineContext) {
                    Thread.sleep(ONE_SECOND_MS)
                }
                remainingDurationSecond--
            }
            stopRecord()
        }
    }

    private fun formatRemainingTime(second: Int): String {
        val formatDate: DateFormat = SimpleDateFormat(
                "- mm:ss", Locale("in", "ID")
        )
        return formatDate.format(Date(second * ONE_SECOND_MS))
    }

    private fun stopRecord() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                mediaRecorder?.stop()

                releaseResources()

                withContext(Dispatchers.IO) {
                    resultVideoPath = writeResultToMovies()
                    cleanUnusedFiles()
                }

                stopForeground(true)
                infoFinishToUser(resultVideoPath)
                stopSelf()
            } catch (e: Exception) {}
        }
    }

    private fun finish() {
        releaseResources()
        stopForeground(true)
        stopSelf()
    }

    private fun releaseResources() {
        mediaProjection?.stop()
        mediaRecorder?.release()
        virtualDisplay?.release()
    }

    private fun writeResultToMovies(): String {
        val srcFile = File(internalStoragePath + FILENAME_RESULT)
        val result = PublicFolderUtil.putFileToPublicFolder(applicationContext,
                srcFile,
                "${getOutputFileName()}_${getTimestamp()}.mp4",
                "video/mp4")
        val outputFile = result.first
        if (outputFile != null) {
            MediaScannerConnection.scanFile(applicationContext, arrayOf(outputFile.absolutePath), null, null)
            return outputFile.absolutePath
        } else {
            return ""
        }
    }

    private fun getTimestamp(): String {
        val formatDate: DateFormat = SimpleDateFormat(
                "yyyy-MM-dd_HH:mm:ss", Locale("in", "ID")
        )
        return formatDate.format(Date())
    }

    private fun getOutputFileName(): String {
        return "Tokopedia_Screen_Record"
    }

    private fun cleanUnusedFiles() {
        var file = File(internalStoragePath + FILENAME_RESULT)
        file.delete()
    }

    private fun infoFinishToUser(resultPath: String) {
        val pendingIntent = getOpenVideoResultPendingIntent(resultPath)
        notificationManager?.notify(NOTIF_ID, buildFinishNotification(pendingIntent))
    }

    private fun buildFinishNotification(pendingIntent: PendingIntent): Notification {
        return NotificationCompat.Builder(applicationContext, HIGH_PRIO_CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(getString(R.string.screen_record_notif_title_video_create_success))
                .setContentText(getString(R.string.screen_record_notif_text_video_create_success))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.screen_recorder_ic_notify_white).build()
    }

    private fun getOpenVideoResultPendingIntent(resultPath: String): PendingIntent {
        val uri = FileProvider.getUriForFile(applicationContext,
                applicationContext.packageName + ".provider", File(resultPath))

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "video/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(applicationContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(applicationContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val normalChannel = NotificationChannel(
                    LOW_PRIO_CHANNEL_ID,
                    LOW_PRIO_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
            )
            normalChannel.setSound(null, null)
            normalChannel.enableLights(false)
            normalChannel.enableVibration(false)

            val highPrioChannel = NotificationChannel(
                    HIGH_PRIO_CHANNEL_ID,
                    HIGH_PRIO_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            )

            val manager = getSystemService(
                    NotificationManager::class.java
            )
            manager?.createNotificationChannel(normalChannel)
            manager?.createNotificationChannel(highPrioChannel)
        }
    }

    private fun showPreRecordCountDown() {
        try {
            preRecordCountDownText = Typography(this)
            preRecordCountDownText.textSize = PRE_RECORD_COUNTDOWN_TEXT_SIZE_SP
            preRecordCountDownText.setTextColor(resources.getColor(R.color.pre_record_countdown_dms_text_color))
            preRecordCountDownText.setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            preRecordCountDownText.setGravity(Gravity.CENTER)

            mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

            preRecordCountDownText.setText(PRE_RECORD_COUNTDOWN_SECOND.toString())

            val params = WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT)

            params.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL

            params.x = 0
            params.y = 0

            mWindowManager.addView(preRecordCountDownText, params)
        } catch (e: Exception) {}
    }

    fun hidePreRecordCountDown() {
        try {
            mWindowManager.removeView(preRecordCountDownText)
        } catch (e: Exception) {}
    }
}
