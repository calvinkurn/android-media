package com.tokopedia.shop.home.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.home.WidgetMultipleImageColumn
import com.tokopedia.shop.home.WidgetSliderBanner
import com.tokopedia.shop.home.WidgetSliderSquareBanner
import com.tokopedia.shop.home.view.adapter.viewholder.*
import com.tokopedia.shop.home.view.model.*
import com.tokopedia.shop.home.view.model.WidgetModel
import com.tokopedia.shop.newproduct.view.datamodel.ShopProductViewModel

class ShopHomeAdapterTypeFactory: BaseAdapterTypeFactory(), TypeFactoryShopHome{

    override fun type(widgetModel: WidgetModel): Int {
        when(widgetModel.name) {
            WidgetMultipleImageColumn -> return ShopHomeMultipleImageColumnViewHolder.LAYOUT_RES
            WidgetSliderSquareBanner -> return ShopHomeSliderSquareViewHolder.LAYOUT_RES
            WidgetSliderBanner -> return ShopHomeSliderBannerViewHolder.LAYOUT_RES
        }
        return -1
    }

    fun type(bannerUIModel: BannerUIModel): Int {
        return -1
    }

    fun type(shopProductViewModel: ShopProductViewModel): Int {
        return -1
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when(type) {
            ShopHomeMultipleImageColumnViewHolder.LAYOUT_RES -> ShopHomeMultipleImageColumnViewHolder(
                    parent
            )
            ShopHomeSliderSquareViewHolder.LAYOUT_RES -> ShopHomeSliderSquareViewHolder(
                    parent
            )
            ShopHomeSliderBannerViewHolder.LAYOUT_RES -> ShopHomeSliderBannerViewHolder(
                    parent
            )
            else -> return super.createViewHolder(parent, type)
        }
    }
}