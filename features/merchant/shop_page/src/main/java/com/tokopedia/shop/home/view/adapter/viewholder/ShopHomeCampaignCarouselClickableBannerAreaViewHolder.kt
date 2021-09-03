package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.widget.Toast

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.shop.R
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.listener.ShopHomeCampaignNplWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeCampaignCarouselClickableBannerAreaUiModel
import com.tokopedia.shop.home.view.model.ShopHomeNewProductLaunchCampaignUiModel

/**
 * @author by alvarisi on 12/12/17.
 */

open class ShopHomeCampaignCarouselClickableBannerAreaViewHolder(
        itemView: View,
        private val shopHomeNewProductLaunchCampaignUiModel: ShopHomeNewProductLaunchCampaignUiModel,
        private val shopHomeCampaignNplWidgetListener: ShopHomeCampaignNplWidgetListener
) : AbstractViewHolder<ShopHomeCampaignCarouselClickableBannerAreaUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_campaign_carousel_clickable_banner_area
    }

    private val viewClickableArea: View? = itemView.findViewById(R.id.view_clickable_area)

    override fun bind(model: ShopHomeCampaignCarouselClickableBannerAreaUiModel) {
        viewClickableArea?.apply {
            layoutParams.width = model.width
            setOnClickListener {
                shopHomeCampaignNplWidgetListener.onClickCampaignBannerAreaNplWidget(shopHomeNewProductLaunchCampaignUiModel)
            }
        }
    }

}