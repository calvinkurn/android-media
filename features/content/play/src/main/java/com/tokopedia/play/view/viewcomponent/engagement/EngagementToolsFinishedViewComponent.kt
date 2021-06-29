package com.tokopedia.play.view.viewcomponent.engagement

import android.view.ViewGroup
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 29/06/21
 */
class EngagementToolsFinishedViewComponent(
        container: ViewGroup
) : ViewComponent(container, R.id.view_engagement_finish) {

    private val tvEngagementFinishInfo = findViewById<Typography>(R.id.tv_engagement_finish_info)

    fun setInfo(info: String) {
        tvEngagementFinishInfo.text = info
    }
}