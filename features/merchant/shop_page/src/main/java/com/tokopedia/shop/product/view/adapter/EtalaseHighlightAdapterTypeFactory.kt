package com.tokopedia.shop.product.view.adapter

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef
import com.tokopedia.shop.product.view.datamodel.EtalaseHighlightCarouselUiModel
import com.tokopedia.shop.product.view.listener.ShopCarouselSeeAllClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductImpressionListener
import com.tokopedia.shop.product.view.viewholder.ErrorNetworkWrapViewHolder
import com.tokopedia.shop.product.view.viewholder.ShopProductCarouselViewHolder

class EtalaseHighlightAdapterTypeFactory(
        private val shopProductClickedListener: ShopProductClickedListener?,
        private val shopProductImpressionListener: ShopProductImpressionListener?,
        private val shopCarouselSeeAllClickedListener: ShopCarouselSeeAllClickedListener?,
        private val deviceWidth: Int
) : BaseAdapterTypeFactory() {

    override fun type(viewModel: LoadingModel): Int {
        return LoadingShimmeringGridViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyModel): Int {
        return EmptyResultViewHolder.LAYOUT
    }

    fun type(etalaseHighlightCarouselUiModel: EtalaseHighlightCarouselUiModel): Int {
        return ShopProductCarouselViewHolder.LAYOUT
    }

    override fun type(errorNetworkModel: ErrorNetworkModel): Int {
        return ErrorNetworkWrapViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == LoadingShimmeringGridViewHolder.LAYOUT) {
            LoadingShimmeringGridViewHolder(parent)
        } else if (type == ErrorNetworkWrapViewHolder.LAYOUT) {
            ErrorNetworkWrapViewHolder(parent)
        } else if (type == ShopProductCarouselViewHolder.LAYOUT) {
            ShopProductCarouselViewHolder(parent, deviceWidth, shopProductClickedListener,
                    shopProductImpressionListener,"", ShopTrackProductTypeDef.ETALASE_HIGHLIGHT, shopCarouselSeeAllClickedListener)
        } else if (type == HideViewHolder.LAYOUT) {
            HideViewHolder(parent)
        } else {
            super.createViewHolder(parent, type)
        }
    }
}