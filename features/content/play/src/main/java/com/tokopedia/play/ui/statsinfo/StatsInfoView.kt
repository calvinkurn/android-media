package com.tokopedia.play.ui.statsinfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.view.uimodel.TotalViewUiModel
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 26/02/20
 */
class StatsInfoView(container: ViewGroup) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_stats_info, container, true)
                    .findViewById(R.id.cl_stats_info)

    override val containerId: Int = view.id

    private val tvTotalView = view.findViewById<Typography>(com.tokopedia.play_common.R.id.tv_total_views)
    private val liveBadge = view.findViewById<View>(R.id.live_badge)

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    internal fun setTotalViews(totalView: TotalViewUiModel) {
        tvTotalView.text = totalView.totalView
    }

    internal fun setLiveBadgeVisibility(isLive: Boolean) {
        if (isLive) liveBadge.show() else liveBadge.hide()
    }
}