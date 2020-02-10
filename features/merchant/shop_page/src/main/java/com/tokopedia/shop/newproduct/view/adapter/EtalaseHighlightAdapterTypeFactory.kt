package com.tokopedia.shop.newproduct.view.adapter

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
import com.tokopedia.shop.newproduct.view.datamodel.EtalaseHighlightCarouselViewModel
import com.tokopedia.shop.newproduct.view.listener.ShopCarouselSeeAllClickedListener
import com.tokopedia.shop.newproduct.view.listener.ShopProductClickedListener
import com.tokopedia.shop.product.view.adapter.viewholder.ErrorNetworkWrapViewHolder
import com.tokopedia.shop.newproduct.view.viewholder.ShopProductCarouselViewHolder
import com.tokopedia.shop.product.view.model.HideViewModel

class EtalaseHighlightAdapterTypeFactory(private val shopProductClickedListener: ShopProductClickedListener?,
                                         private val shopCarouselSeeAllClickedListener: ShopCarouselSeeAllClickedListener?,
                                         private val deviceWidth: Int) : BaseAdapterTypeFactory() {

    override fun type(viewModel: LoadingModel): Int {
        return LoadingShimmeringGridViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyModel): Int {
        return EmptyResultViewHolder.LAYOUT
    }

    fun type(etalaseHighlightCarouselViewModel: EtalaseHighlightCarouselViewModel): Int {
        return ShopProductCarouselViewHolder.LAYOUT
    }

    override fun type(errorNetworkModel: ErrorNetworkModel): Int {
        return ErrorNetworkWrapViewHolder.LAYOUT
    }

    fun type(viewModel: HideViewModel): Int {
        return HideViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == LoadingShimmeringGridViewHolder.LAYOUT) {
            LoadingShimmeringGridViewHolder(parent)
        } else if (type == ErrorNetworkWrapViewHolder.LAYOUT) {
            ErrorNetworkWrapViewHolder(parent)
        } else if (type == ShopProductCarouselViewHolder.LAYOUT) {
            ShopProductCarouselViewHolder(parent, deviceWidth, shopProductClickedListener,
                    "", ShopTrackProductTypeDef.ETALASE_HIGHLIGHT, shopCarouselSeeAllClickedListener)
        } else if (type == HideViewHolder.LAYOUT) {
            HideViewHolder(parent)
        } else {
            super.createViewHolder(parent, type)
        }
    }
}