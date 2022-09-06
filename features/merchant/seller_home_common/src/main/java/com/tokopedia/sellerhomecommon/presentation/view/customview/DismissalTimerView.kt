package com.tokopedia.sellerhomecommon.presentation.view.customview

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcDismissalTimerViewBinding

/**
 * Created by @ilhamsuaib on 02/09/22.
 */

class DismissalTimerView : ConstraintLayout {

    companion object {
        private const val DURATION = 8000L
        private const val INTERVAL = 1000L
    }

    private var binding: ShcDismissalTimerViewBinding? = null
    private var listener: Listener? = null
    private val timer: CountDownTimer = object : CountDownTimer(DURATION, INTERVAL) {

        override fun onTick(millisUntilFinished: Long) {
            onTimerTicked(millisUntilFinished)
        }

        override fun onFinish() {
            listener?.onFinished()
        }
    }

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

    fun startTimer(title: String, listener: Listener) {
        this.listener = listener
        binding?.tvShcDismissalTitle?.text = title
        timer.start()
    }

    private fun initView(context: Context) {
        binding = ShcDismissalTimerViewBinding.inflate(
            LayoutInflater.from(context), this, true
        )

        binding?.run {
            tvShcDismissalTimer.setOnClickListener {
                timer.cancel()
                listener?.onCancelTimer()
            }
        }
    }

    private fun onTimerTicked(millisUntilFinished: Long) {
        val second = (millisUntilFinished / INTERVAL).toInt()
        binding?.tvShcDismissalTimer?.text = context.getString(R.string.shc_cancel_timer, second)
    }

    interface Listener {
        fun onFinished()
        fun onCancelTimer()
    }
}