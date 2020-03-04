package com.tokopedia.shop.home.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.home.WidgetName.DISPLAY_DOUBLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_SINGLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_TRIPLE_COLUMN
import com.tokopedia.shop.home.WidgetName.PRODUCT
import com.tokopedia.shop.home.WidgetName.SLIDER_BANNER
import com.tokopedia.shop.home.WidgetName.SLIDER_SQUARE_BANNER
import com.tokopedia.shop.home.WidgetName.VIDEO
import com.tokopedia.shop.home.WidgetName.VOUCHER
import com.tokopedia.shop.home.view.adapter.viewholder.*
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeCarousellProductViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductEtalaseTitleViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductViewHolder
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductEtalaseTitleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel

class ShopHomeAdapterTypeFactory : BaseAdapterTypeFactory(), TypeFactoryShopHome {

    override fun type(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        when(baseShopHomeWidgetUiModel.name) {
            DISPLAY_SINGLE_COLUMN, DISPLAY_DOUBLE_COLUMN, DISPLAY_TRIPLE_COLUMN -> return ShopHomeMultipleImageColumnViewHolder.LAYOUT_RES
            SLIDER_SQUARE_BANNER -> return ShopHomeSliderSquareViewHolder.LAYOUT_RES
            SLIDER_BANNER -> return ShopHomeSliderBannerViewHolder.LAYOUT_RES
            VIDEO -> return ShopHomeVideoViewHolder.LAYOUT_RES
            PRODUCT -> return ShopHomeCarousellProductViewHolder.LAYOUT
//            VOUCHER -> return ShopHomeVoucherViewHolder.LAYOUT
        }
        return -1
    }

    override fun type(shopHomeProductEtalaseTitleUiModel: ShopHomeProductEtalaseTitleUiModel): Int {
        return ShopHomeProductEtalaseTitleViewHolder.LAYOUT
    }

    override fun type(shopHomeProductViewModel: ShopHomeProductViewModel): Int {
        return ShopHomeProductViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ShopHomeMultipleImageColumnViewHolder.LAYOUT_RES -> ShopHomeMultipleImageColumnViewHolder(
                    parent
            )
            ShopHomeSliderSquareViewHolder.LAYOUT_RES -> ShopHomeSliderSquareViewHolder(
                    parent
            )
            ShopHomeSliderBannerViewHolder.LAYOUT_RES -> ShopHomeSliderBannerViewHolder(
                    parent
            )
            ShopHomeVideoViewHolder.LAYOUT_RES -> ShopHomeVideoViewHolder(
                    parent
            )
            ShopHomeProductViewHolder.LAYOUT -> {
                ShopHomeProductViewHolder(parent, null)
            }
            ShopHomeProductEtalaseTitleViewHolder.LAYOUT -> {
                ShopHomeProductEtalaseTitleViewHolder(parent)
            }
            ShopHomeCarousellProductViewHolder.LAYOUT -> {
                ShopHomeCarousellProductViewHolder(parent)
            }
            else -> return super.createViewHolder(parent, type)
        }
    }
}