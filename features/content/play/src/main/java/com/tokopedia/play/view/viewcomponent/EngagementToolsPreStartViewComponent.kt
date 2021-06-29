package com.tokopedia.play.view.viewcomponent

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
class EngagementToolsPreStartViewComponent(
        container: ViewGroup,
        listener: Listener
) : ViewComponent(container, R.id.view_engagement_prestart) {

    private val btnEngagementFollow = findViewById<UnifyButton>(R.id.btn_engagement_follow)
    private val tvEngagementTitle = findViewById<Typography>(R.id.tv_engagement_title)
    private val timerPreStart = findViewById<TimerUnifySingle>(R.id.timer_prestart)

    init {
        btnEngagementFollow.setOnClickListener {
            listener.onFollowButtonClicked(this)
        }
    }

    fun setTitle(title: String) {
        tvEngagementTitle.text = title
    }

    fun setTimer(durationInMs: Long, onFinished: () -> Unit) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MILLISECOND, durationInMs.toInt())

        timerPreStart.pause()
        timerPreStart.targetDate = calendar
        timerPreStart.onFinish = onFinished
        timerPreStart.resume()
    }

    fun showFollowButton(shouldShow: Boolean) {
        if (shouldShow) {
            btnEngagementFollow.show()
        } else {
            btnEngagementFollow.hide()
        }
    }

    override fun hide() {
        super.hide()
        timerPreStart.resume()
    }

    interface Listener {

        fun onFollowButtonClicked(view: EngagementToolsPreStartViewComponent)
    }
}