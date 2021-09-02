package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselViewAllCardDataModel

/**
 * created by Dhaba
 */
class CarouselViewAllCardViewHolder(view: View,
                                    private val channels: ChannelModel
)
    : AbstractViewHolder<CarouselViewAllCardDataModel>(view){

    private val container: View by lazy { view.findViewById<ConstraintLayout>(R.id.container_banner_view_all) }
    private val ivHeadingViewAll: ImageView by lazy { view.findViewById<ImageView>(R.id.iv_chevron_down_view_all) }
    private val tvDcMixViewAll: TextView by lazy { view.findViewById<TextView>(R.id.tv_dc_mix_view_all) }
    private val tvTitleViewAll: TextView by lazy { view.findViewById<TextView>(R.id.tv_title_view_all) }
    private val tvDescriptionViewAll: TextView by lazy { view.findViewById<TextView>(R.id.tv_description_view_all) }

    override fun bind(element: CarouselViewAllCardDataModel) {
        container.setOnClickListener {
            element.listener.onSeeMoreCardClicked(applink = element.applink, channel = channels)
        }
    }

    companion object{
        val LAYOUT = R.layout.home_banner_item_carousel_view_all
    }
}