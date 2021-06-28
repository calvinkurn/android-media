package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by jegul on 28/06/21
 */
class EngagementToolsPreStartViewComponent(
        container: ViewGroup,
        listener: Listener
) : ViewComponent(container, R.id.view_engagement_prestart) {

    private val btnEngagementFollow = findViewById<UnifyButton>(R.id.btn_engagement_follow)

    init {
        btnEngagementFollow.setOnClickListener {
            listener.onFollowButtonClicked(this)
        }
    }

    fun showFollowButton(shouldShow: Boolean) {
        if (shouldShow) {
            btnEngagementFollow.show()
        } else {
            btnEngagementFollow.hide()
        }
    }

    interface Listener {

        fun onFollowButtonClicked(view: EngagementToolsPreStartViewComponent)
    }
}