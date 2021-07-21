package com.tokopedia.play.broadcaster.view.custom.interactive

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ViewPlayInteractiveLiveBinding
import com.tokopedia.play_common.view.RoundedConstraintLayout
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import java.util.*
import com.tokopedia.play_common.R as commonR
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created by jegul on 07/07/21
 */
class InteractiveLiveView : RoundedConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = ViewPlayInteractiveLiveBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
    )

    init {
        binding.root.setCornerRadius(
                resources.getDimension(R.dimen.play_interactive_live_radius)
        )
    }

    fun setMode(mode: Mode) {
        when (mode) {
            is Mode.Scheduled -> setScheduledMode(mode)
            is Mode.Live -> setLiveMode(mode)
        }
    }

    private fun setScheduledMode(mode: Mode.Scheduled) {
        binding.timerInteractiveSetup.timerVariant = TimerUnifySingle.VARIANT_ALTERNATE
        binding.timerInteractiveSetup.timerFormat = TimerUnifySingle.FORMAT_MINUTE
        setTimer(mode.timeToStartInMs, mode.onFinished)

        binding.tvInteractiveTitle.text = mode.title
        binding.tvInteractiveTimerInfo.text = context.getString(R.string.play_interactive_setup_start_in)

        binding.root.setBackgroundResource(commonR.drawable.bg_play_interactive)
        binding.tvInteractiveTitle.setTextColor(
                MethodChecker.getColor(context, unifyR.color.Unify_Static_White)
        )
        binding.clTimerContainer.setBackgroundColor(
                MethodChecker.getColor(context, unifyR.color.Neutral_N75)
        )
    }

    private fun setLiveMode(mode: Mode.Live) {
        binding.timerInteractiveSetup.timerVariant = TimerUnifySingle.VARIANT_MAIN
        binding.timerInteractiveSetup.timerFormat = TimerUnifySingle.FORMAT_SECOND
        setTimer(mode.remainingTimeInMs, mode.onFinished)

        binding.tvInteractiveTitle.text = context.getString(R.string.play_interactive_live)
        binding.tvInteractiveTimerInfo.text = context.getString(R.string.play_interactive_live_end_in)

        binding.root.setBackgroundColor(
                MethodChecker.getColor(context, unifyR.color.Unify_Static_White)
        )
        binding.tvInteractiveTitle.setTextColor(
                MethodChecker.getColor(context, R.color.play_dms_broadcast_interactive_live_title)
        )
        binding.clTimerContainer.setBackgroundColor(
                MethodChecker.getColor(context, unifyR.color.Neutral_N75)
        )
    }

    private fun setTimer(durationInMs: Long, onFinished: () -> Unit) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MILLISECOND, durationInMs.toInt())

        binding.timerInteractiveSetup.pause()
        binding.timerInteractiveSetup.targetDate = calendar
        binding.timerInteractiveSetup.onFinish = onFinished
        binding.timerInteractiveSetup.resume()
    }

    sealed class Mode {

        data class Scheduled(val title: String, val timeToStartInMs: Long, val onFinished: () -> Unit) : Mode()
        data class Live(val remainingTimeInMs: Long, val onFinished: () -> Unit) : Mode()
    }
}