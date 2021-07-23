package com.tokopedia.play.view.custom.interactive

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.R
import com.tokopedia.play_common.view.RoundedFrameLayout
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import java.util.*

/**
 * Created by jegul on 05/07/21
 */
class InteractiveTapView : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val flInteractiveTap: FrameLayout
    private val timerTap: TimerUnifySingle
    private val iconTap: IconUnify
    private val tvTapAction: TextView
    private val flTapBackground: RoundedFrameLayout

    private var mListener: Listener? = null

    init {
        val view = View.inflate(context, R.layout.view_interactive_tap, this)

        flInteractiveTap = view.findViewById(R.id.fl_interactive_tap)
        timerTap = view.findViewById(R.id.timer_tap)
        iconTap = view.findViewById(R.id.icon_tap)
        tvTapAction = view.findViewById(R.id.tv_tap_action)
        flTapBackground = view.findViewById(R.id.fl_tap_background)

        flTapBackground.setCornerRadius(
                resources.getDimension(com.tokopedia.play_common.R.dimen.play_interactive_common_radius)
        )
    }

    fun setTimer(durationInMs: Long, onFinished: () -> Unit) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MILLISECOND, durationInMs.toInt())

        timerTap.pause()
        timerTap.targetDate = calendar
        timerTap.onFinish = onFinished
        timerTap.resume()
    }

    fun cancelTimer() {
        timerTap.pause()
        timerTap.timer?.cancel()
    }

    fun showFollowMode(shouldShow: Boolean) {
        changeMode(
                if (!shouldShow) Mode.Tap
                else Mode.Follow
        )

        flInteractiveTap.setOnClickListener {
            if (!shouldShow) mListener?.onTapClicked(this)
            else mListener?.onFollowClicked(this)
        }
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    private fun changeMode(mode: Mode) {
        when (mode) {
            Mode.Follow -> {
                iconTap.setImage(IconUnify.USER_ADD)
                tvTapAction.text = context.getString(R.string.play_interactive_tap_action_follow_text)
            }
            Mode.Tap -> {
                iconTap.setImage(IconUnify.GIFT)
                tvTapAction.text = context.getString(R.string.play_interactive_tap_action_tap_text)
            }
        }
    }

    private enum class Mode {
        Follow,
        Tap
    }

    interface Listener {

        fun onTapClicked(view: InteractiveTapView)
        fun onFollowClicked(view: InteractiveTapView)
    }
}