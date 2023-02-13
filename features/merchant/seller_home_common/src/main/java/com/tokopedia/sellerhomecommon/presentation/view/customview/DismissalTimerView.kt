package com.tokopedia.sellerhomecommon.presentation.view.customview

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcDismissalTimerViewBinding

/**
 * Created by @ilhamsuaib on 02/09/22.
 */

class DismissalTimerView : ConstraintLayout {

    companion object {
        private const val INTERVAL = 1000L
        const val DEFAULT_DURATION = 8000L
    }

    private var isTimerRunning = false
    private var binding: ShcDismissalTimerViewBinding? = null
    private var listener: Listener? = null
    private var timer: CountDownTimer? = null

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    fun startTimer(title: String, duration: Long = DEFAULT_DURATION, listener: Listener) {
        if (!isTimerRunning) {
            this.listener = listener
            binding?.tvShcDismissalTitle?.text = title
            timer = object : CountDownTimer(duration, INTERVAL) {

                override fun onTick(millisUntilFinished: Long) {
                    isTimerRunning = true
                    onTimerTicked(millisUntilFinished)
                    this@DismissalTimerView.listener?.onTicked(millisUntilFinished)
                }

                override fun onFinish() {
                    isTimerRunning = false
                    this@DismissalTimerView.listener?.onFinished()
                }
            }
            timer?.start()
        }
    }

    private fun initView(context: Context) {
        binding = ShcDismissalTimerViewBinding.inflate(
            LayoutInflater.from(context), this, true
        )

        binding?.run {
            tvShcDismissalTimer.setOnClickListener {
                timer?.cancel()
                isTimerRunning = false
                listener?.onCancelTimer()
            }
        }
    }

    private fun onTimerTicked(millisUntilFinished: Long) {
        val second: Int = (millisUntilFinished / INTERVAL).toInt().plus(Int.ONE)
        binding?.tvShcDismissalTimer?.text = context.getString(R.string.shc_cancel_timer, second)
    }

    interface Listener {
        fun onFinished()
        fun onTicked(millisUntilFinished: Long)
        fun onCancelTimer()
    }
}