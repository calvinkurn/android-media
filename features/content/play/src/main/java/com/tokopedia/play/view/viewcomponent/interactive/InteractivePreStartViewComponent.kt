package com.tokopedia.play.view.viewcomponent.interactive

import android.os.CountDownTimer
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.*

/**
 * Created by jegul on 28/06/21
 */
class InteractivePreStartViewComponent(
        container: ViewGroup,
        listener: Listener
) : ViewComponent(container, R.id.view_interactive_prestart) {

    private val btnInteractiveFollow = findViewById<UnifyButton>(R.id.btn_interactive_follow)
    private val tvInteractiveTitle = findViewById<Typography>(R.id.tv_interactive_title)
    private val timerPreStart = findViewById<TimerUnifySingle>(R.id.timer_prestart)

    init {
        btnInteractiveFollow.setOnClickListener {
            listener.onFollowButtonClicked(this)
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

    fun showFollowButton(shouldShow: Boolean) {
        if (shouldShow) {
            btnInteractiveFollow.show()
        } else {
            btnInteractiveFollow.hide()
        }
    }

    override fun hide() {
        super.hide()
        timerPreStart.resume()
    }

    interface Listener {

        fun onFollowButtonClicked(view: InteractivePreStartViewComponent)
    }
}