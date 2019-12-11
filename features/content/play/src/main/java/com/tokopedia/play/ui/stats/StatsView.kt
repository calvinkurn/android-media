package com.tokopedia.play.ui.stats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.view.model.TotalLikes
import com.tokopedia.play.view.model.TotalView
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 03/12/19
 */
class StatsView(container: ViewGroup) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_stats, container, true)
                    .findViewById(R.id.ll_stats)

    override val containerId: Int = view.id

    private val tvTotalLikes = view.findViewById<Typography>(R.id.tv_total_likes)
    private val tvTotalView = view.findViewById<Typography>(R.id.tv_total_views)

    fun setTotalLikes(totalLikes: TotalLikes) {
        tvTotalLikes.text = "${totalLikes.number} Likes"
    }

    fun setTotalViews(totalView: TotalView) {
        tvTotalView.text = totalView.number
    }

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }
}