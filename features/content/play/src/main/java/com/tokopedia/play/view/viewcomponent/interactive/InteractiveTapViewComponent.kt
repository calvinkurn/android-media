package com.tokopedia.play.view.viewcomponent.interactive

import android.view.ViewGroup
import android.widget.FrameLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.*

/**
 * Created by jegul on 28/06/21
 */
class InteractiveTapViewComponent(
        container: ViewGroup,
        private val listener: Listener
) : ViewComponent(container, R.id.view_interactive_tap) {

    private val flInteractiveTap = findViewById<FrameLayout>(R.id.fl_interactive_tap)
    private val timerTap = findViewById<TimerUnifySingle>(R.id.timer_tap)
    private val iconTap = findViewById<IconUnify>(R.id.icon_tap)
    private val tvTapAction = findViewById<Typography>(R.id.tv_tap_action)

    override fun hide() {
        super.hide()
        timerTap.pause()
    }

    fun setTimer(durationInMs: Long, onFinished: () -> Unit) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MILLISECOND, durationInMs.toInt())

        timerTap.pause()
        timerTap.targetDate = calendar
        timerTap.onFinish = onFinished
        timerTap.resume()
    }

    fun showFollowMode(shouldShow: Boolean) {
        changeMode(
                if (!shouldShow) Mode.Tap
                else Mode.Follow
        )

        flInteractiveTap.setOnClickListener {
            if (!shouldShow) listener.onTapClicked(this)
            else listener.onFollowClicked(this)
        }
    }

    private fun changeMode(mode: Mode) {
        when (mode) {
            Mode.Follow -> {
                iconTap.setImage(IconUnify.USER_ADD)
                tvTapAction.text = getString(R.string.play_interactive_tap_action_follow_text)
            }
            Mode.Tap -> {
                iconTap.setImage(IconUnify.GIFT)
                tvTapAction.text = getString(R.string.play_interactive_tap_action_tap_text)
            }
        }
    }

    interface Listener {

        fun onTapClicked(view: InteractiveTapViewComponent)
        fun onFollowClicked(view: InteractiveTapViewComponent)
    }

    private enum class Mode {
        Follow,
        Tap
    }
}