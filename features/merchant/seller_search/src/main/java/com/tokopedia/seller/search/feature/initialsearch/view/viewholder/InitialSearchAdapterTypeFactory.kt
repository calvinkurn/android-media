package com.tokopedia.seller.search.feature.initialsearch.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.HISTORY
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.NAVIGATION
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.ORDER
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.PRODUCT
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.SellerSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.history.HistorySearchViewHolder
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.navigation.NavigationSearchViewHolder
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.order.OrderSearchViewHolder
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.product.ProductSearchViewHolder

class InitialSearchAdapterTypeFactory: BaseAdapterTypeFactory(), TypeFactoryInitialSearchViewHolder {

    override fun type(sellerSearchUiModel: SellerSearchUiModel): Int {
        return when(sellerSearchUiModel.id) {
            HISTORY -> HistorySearchViewHolder.LAYOUT_RES
            ORDER -> OrderSearchViewHolder.LAYOUT_RES
            PRODUCT -> ProductSearchViewHolder.LAYOUT_RES
            NAVIGATION -> NavigationSearchViewHolder.LAYOUT_RES
            else -> -1
        }
    }

    override fun type(viewModel: LoadingModel): Int {
        return ShimmerLoadingViewHolder.LAYOUT_RES
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when(type) {
            HistorySearchViewHolder.LAYOUT_RES -> HistorySearchViewHolder(parent)
            OrderSearchViewHolder.LAYOUT_RES -> OrderSearchViewHolder(parent)
            ProductSearchViewHolder.LAYOUT_RES -> ProductSearchViewHolder(parent)
            NavigationSearchViewHolder.LAYOUT_RES -> NavigationSearchViewHolder(parent)
            else ->  super.createViewHolder(parent, type)
        }
    }

}