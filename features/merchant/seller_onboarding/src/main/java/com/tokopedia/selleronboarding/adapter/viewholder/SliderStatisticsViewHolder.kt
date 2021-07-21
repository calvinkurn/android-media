package com.tokopedia.selleronboarding.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.model.SobSliderHomeUiModel
import com.tokopedia.selleronboarding.model.SobSliderStatisticsUiModel
import kotlinx.android.synthetic.main.sob_slider_statistics_view_holder.view.*

/**
 * Created By @ilhamsuaib on 20/07/21
 */

class SliderStatisticsViewHolder(itemView: View) : AbstractViewHolder<SobSliderStatisticsUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.sob_slider_statistics_view_holder
    }

    override fun bind(element: SobSliderStatisticsUiModel) {
        with(itemView) {
            imgSobStatistic.loadImage(R.drawable.onboarding_05)
        }
    }
}