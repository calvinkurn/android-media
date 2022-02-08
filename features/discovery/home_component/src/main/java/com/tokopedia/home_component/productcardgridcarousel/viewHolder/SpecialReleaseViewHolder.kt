package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselCampaignCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSpecialReleaseDataModel
import com.tokopedia.media.loader.loadImage

/**
 * Created by devarafikry on 08/02/22.
 */
class SpecialReleaseViewHolder(
    private val view: View,
    private val channels: ChannelModel
) : AbstractViewHolder<CarouselSpecialReleaseDataModel>(view) {

    override fun bind(element: CarouselSpecialReleaseDataModel?) {
        initView()
    }

    private fun initView() {
    }

    companion object {
        val LAYOUT = R.layout.home_component_special_release_item
    }
}