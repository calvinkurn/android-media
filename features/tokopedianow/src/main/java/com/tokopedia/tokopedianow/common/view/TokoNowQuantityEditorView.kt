package com.tokopedia.tokopedianow.common.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.motion.widget.MotionLayout
import com.tokopedia.home_component.util.toSp
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowQuantityEditorCustomViewBinding
import com.tokopedia.unifycomponents.BaseCustomView
import java.util.*


@SuppressLint("ClickableViewAccessibility")
class TokoNowQuantityEditorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private var binding: LayoutTokopedianowQuantityEditorCustomViewBinding? = null

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    var delay = 1000L

    fun startTimer() {
        timer = Timer()
        initializeTimerTask()
        timer?.schedule(timerTask, delay)
    }

    fun cancelTask() {
        if (timer != null) {
            timer?.cancel()
            timerTask?.cancel()
            timerTask?.scheduledExecutionTime()
            timerTask =  null
            timer = null
        }
    }

    fun initializeTimerTask() {
        timerTask = object : TimerTask() {
            override fun run() {
                if (binding?.editText?.text?.isBlank() == true) {
                    binding?.root?.transitionToStart()
                } else {
                    binding?.editText?.clearFocus()
                    binding?.root?.setTransition(R.id.end, R.id.startWithValue)
                    binding?.root?.transitionToEnd()
                }
            }
        }
    }

    init {
        binding = LayoutTokopedianowQuantityEditorCustomViewBinding.inflate(LayoutInflater.from(context),this, true)

        setOnTouchListener { p0, p1 ->
            cancelTask()
            binding?.editText?.requestFocus()
            binding?.root?.transitionToEnd()
            true
        }
        binding?.editText?.focusChangedListener = {
            if (!it) {
                startTimer()
            } else {
                binding?.root?.transitionToEnd()
            }
        }
        binding?.root?.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout?, p1: Int, p2: Int) {
                if (motionLayout?.currentState == R.id.startWithValue) {
                    binding?.editText?.setPadding(16, 0, 16, 0)
                }
            }

            override fun onTransitionChange(motionLayout: MotionLayout?, p1: Int, p2: Int, p3: Float) {

            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, p1: Int) {
                if (motionLayout?.currentState == R.id.startWithValue) {
                    binding?.editText?.textSize = pixelsToSp(resources.getDimension(R.dimen.tokopedianow_test))
                    binding?.editText?.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                    cancelTask()
                } else if (motionLayout?.currentState == R.id.end) {
                    binding?.editText?.requestFocus()
                } else {
                    cancelTask()
                }
            }

            override fun onTransitionTrigger(motionLayout: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

            }

        })
    }

    fun pixelsToSp(px: Float): Float {
        val scaledDensity: Float = resources.getDisplayMetrics().scaledDensity
        return px / scaledDensity
    }
}
