package com.tokopedia.play.view.custom.interactive

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play_common.view.RoundedConstraintLayout
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import java.util.*

/**
 * Created by jegul on 05/07/21
 */
class InteractivePreStartView : RoundedConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val tvInteractiveTitle: TextView
    private val btnInteractiveFollow: UnifyButton
    private val timerPreStart: TimerUnifySingle

    private var mListener: Listener? = null

    init {
        val view = View.inflate(context, R.layout.view_interactive_prestart, this)

        tvInteractiveTitle = view.findViewById(R.id.tv_interactive_title)
        btnInteractiveFollow = view.findViewById(R.id.btn_interactive_follow)
        timerPreStart = view.findViewById(R.id.timer_prestart)

        setupView(view)
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.GONE) {
            timerPreStart.pause()
        }
    }

    fun setTitle(title: String) {
        tvInteractiveTitle.text = title
    }

    fun setTimer(
            durationInMs: Long,
            onFinished: () -> Unit
    ) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MILLISECOND, durationInMs.toInt())

        timerPreStart.pause()
        timerPreStart.targetDate = calendar
        timerPreStart.onFinish = onFinished
        timerPreStart.resume()
    }

    fun cancelTimer() {
        timerPreStart.pause()
        timerPreStart.timer?.cancel()
    }

    fun showFollowButton(shouldShow: Boolean) {
        if (shouldShow) {
            btnInteractiveFollow.show()
        } else {
            btnInteractiveFollow.hide()
        }
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    private fun setupView(view: View) {
        setCornerRadius(
                resources.getDimension(com.tokopedia.play_common.R.dimen.play_interactive_common_radius)
        )

        btnInteractiveFollow.setOnClickListener {
            mListener?.onFollowButtonClicked(this)
        }
    }

    interface Listener {

        fun onFollowButtonClicked(view: InteractivePreStartView)
    }
}