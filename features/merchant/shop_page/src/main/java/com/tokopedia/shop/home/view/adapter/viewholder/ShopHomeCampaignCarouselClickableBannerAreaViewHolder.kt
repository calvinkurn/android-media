package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopCampaignCarouselClickableBannerAreaBinding
import com.tokopedia.shop.home.view.listener.ShopHomeCampaignNplWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeCampaignCarouselClickableBannerAreaUiModel
import com.tokopedia.shop.home.view.model.ShopHomeNewProductLaunchCampaignUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * @author by alvarisi on 12/12/17.
 */

open class ShopHomeCampaignCarouselClickableBannerAreaViewHolder(
    itemView: View,
    private val parentPosition: Int,
    private val shopHomeNewProductLaunchCampaignUiModel: ShopHomeNewProductLaunchCampaignUiModel,
    private val shopHomeCampaignNplWidgetListener: ShopHomeCampaignNplWidgetListener
) : AbstractViewHolder<ShopHomeCampaignCarouselClickableBannerAreaUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_campaign_carousel_clickable_banner_area
    }

    private val viewBinding: ItemShopCampaignCarouselClickableBannerAreaBinding? by viewBinding()
    private val viewClickableArea: View? = viewBinding?.viewClickableArea

    override fun bind(model: ShopHomeCampaignCarouselClickableBannerAreaUiModel) {
        viewClickableArea?.apply {
            layoutParams.width = model.width
            setOnClickListener {
                shopHomeCampaignNplWidgetListener.onClickCampaignBannerAreaNplWidget(
                    shopHomeNewProductLaunchCampaignUiModel,
                    parentPosition
                )
            }
        }
    }
}
