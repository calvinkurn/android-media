package com.tokopedia.play.view.viewcomponent

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.recom.PlayTotalViewUiModel
import com.tokopedia.play.view.uimodel.recom.totalViewFmt
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 03/08/20
 */
class StatsInfoViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int
) : ViewComponent(container, idRes) {

    private val tvTotalView = findViewById<Typography>(com.tokopedia.play_common.R.id.tv_total_views)
    private val liveBadge = findViewById<View>(R.id.live_badge)

    fun setTotalViews(totalView: PlayTotalViewUiModel) {
        tvTotalView.text = totalView.totalViewFmt
    }

    fun setLiveBadgeVisibility(isLive: Boolean) {
        if (isLive) liveBadge.show() else liveBadge.hide()
    }
}