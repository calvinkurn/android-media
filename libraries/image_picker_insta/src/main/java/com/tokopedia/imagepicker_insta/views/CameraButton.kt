package com.tokopedia.imagepicker_insta.views

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.util.VideoUtil
import com.tokopedia.unifycomponents.toPx

@SuppressLint("ClickableViewAccessibility")
class CameraButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    lateinit var progressBar: ProgressBar
    lateinit var imageCapture: AppCompatImageView
    var countDownTimer: CountDownTimer? = null
    val longPressHandler = android.os.Handler(Looper.getMainLooper())
    var longTimeMillis = 0L
    val ANIMATION_DURATION = 300L
    var cameraButtonListener: CameraButtonListener?=null
    var imageCaptureAction = MotionEvent.ACTION_CANCEL
    val ON_CLICK_TIME = 500L
    var maxDurationToRecord = VideoUtil.DEFAULT_DURATION_MAX_LIMIT

    private val cameraButtonTouchListener = OnTouchListener { _:View, event:MotionEvent ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                imageCaptureAction = event.action
                longTimeMillis = System.currentTimeMillis()
                longPressHandler.postDelayed({
                    if (imageCaptureAction == MotionEvent.ACTION_DOWN){
                        onLongPressStart()
                    }
                }, ON_CLICK_TIME)

                return@OnTouchListener true
            }
            MotionEvent.ACTION_UP -> {
                imageCaptureAction = event.action
                val currentTime = System.currentTimeMillis()
                val longTimeForDebugMillis = longTimeMillis
                if (currentTime - longTimeForDebugMillis < ON_CLICK_TIME) {
                    onClick()
                } else {
                    onLongPressEnd()
                }
                imageCapture.setOnTouchListener(null)
                longTimeMillis = 0L
                return@OnTouchListener true
            }
        }
        return@OnTouchListener false
    }

    /**
    * These are the values of progress bar sizes taken from design
    * */
    val PROGRESS_BAR_VALUES_PAIR = Pair(64.toPx(), 80.toPx())

    fun getLayout() = R.layout.imagepicker_insta_camer_button

    init {
        LayoutInflater.from(context).inflate(getLayout(), this, true)
        initViews()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews() {
        progressBar = findViewById(R.id.progress_bar_timer)
        imageCapture = findViewById(R.id.image_capture)
        addTouchListener()
    }

    fun addTouchListener(){
        imageCapture.setOnTouchListener(cameraButtonTouchListener)
    }

    private fun onLongPressStart() {
        val greenCircleScaleFactor = 0.8f

        progressBarAnimation(PROGRESS_BAR_VALUES_PAIR.first, PROGRESS_BAR_VALUES_PAIR.second)

        imageCapture.animate()
            .scaleX(greenCircleScaleFactor)
            .scaleY(greenCircleScaleFactor)
            .setDuration(ANIMATION_DURATION)
            .start()

        startCountDown()

        cameraButtonListener?.onLongClickStart()
    }


    private fun onLongPressEnd() {
        val scaleFactor = 1f

        progressBarAnimation(PROGRESS_BAR_VALUES_PAIR.second, PROGRESS_BAR_VALUES_PAIR.first)

        imageCapture.animate()
            .scaleX(scaleFactor)
            .scaleY(scaleFactor)
            .setDuration(ANIMATION_DURATION)
            .start()

        stopCountDown()
        resetProgressBar()
        cameraButtonListener?.onLongClickEnd()
    }

    private fun progressBarAnimation(initialValue: Int, finalValue: Int) {
        updateProgressBarLayoutParams(initialValue)
        val valueAnim = ValueAnimator.ofInt(initialValue, finalValue)
        valueAnim.addUpdateListener {
            val v = (it.animatedValue as Int)
            updateProgressBarLayoutParams(v)
        }
        valueAnim.duration = ANIMATION_DURATION
        valueAnim.start()
    }

    private fun updateProgressBarLayoutParams(size: Int) {
        val lp = progressBar.layoutParams
        lp.width = size
        lp.height = size
        progressBar.layoutParams = lp
    }

    /**
     * Start this timer when video recording is started
     * */
    private fun startCountDown() {
        val totalSecondsMillis = maxDurationToRecord * 1000L
        val interval = 1000L
        countDownTimer = object : CountDownTimer(totalSecondsMillis, interval) {

            override fun onTick(millisUntilFinished: Long) {
                val curSecond = (totalSecondsMillis - millisUntilFinished).toFloat()
                val progressCount = (curSecond / totalSecondsMillis * 100).toInt()
                updateProgressBar(progressCount)
            }

            override fun onFinish() {
                updateProgressBar(100)
            }
        }
        countDownTimer?.start()
    }

    fun stopCountDown(){
        countDownTimer?.cancel()
    }

    fun updateProgressBar(count:Int){
        progressBar.progress = count
    }
    fun resetProgressBar(){
        updateProgressBar(0)
    }

    private fun onClick() {
        cameraButtonListener?.onClick()
    }
}

interface CameraButtonListener {
    fun onClick()
    fun onLongClickStart()
    fun onLongClickEnd()
}