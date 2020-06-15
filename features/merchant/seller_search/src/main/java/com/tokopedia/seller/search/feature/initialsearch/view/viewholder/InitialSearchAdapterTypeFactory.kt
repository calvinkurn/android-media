package com.tokopedia.seller.search.feature.initialsearch.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.HISTORY
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.NAVIGATION
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.ORDER
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.PRODUCT
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchMinCharUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchNoHistoryUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchNoResultUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.SellerSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.history.HistorySearchViewHolder
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.navigation.NavigationSearchViewHolder
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.order.OrderSearchViewHolder
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.product.ProductSearchViewHolder

class InitialSearchAdapterTypeFactory: BaseAdapterTypeFactory, TypeFactoryInitialSearchViewHolder {

    private var historySearchListener: HistorySearchListener? = null
    private var orderSearchListener: OrderSearchListener? = null
    private var productSearchListener: ProductSearchListener? = null
    private var navigationSearchListener: NavigationSearchListener? = null

    constructor(orderSearchListener: OrderSearchListener,
                productSearchListener: ProductSearchListener, navigationSearchListener: NavigationSearchListener) {
        this.orderSearchListener = orderSearchListener
        this.productSearchListener = productSearchListener
        this.navigationSearchListener = navigationSearchListener
    }

    constructor(historySearchListener: HistorySearchListener) {
        this.historySearchListener = historySearchListener
    }

    override fun type(sellerSearchUiModel: SellerSearchUiModel): Int {
        return when(sellerSearchUiModel.id) {
            HISTORY -> HistorySearchViewHolder.LAYOUT_RES
            ORDER -> OrderSearchViewHolder.LAYOUT_RES
            PRODUCT -> ProductSearchViewHolder.LAYOUT_RES
            NAVIGATION -> NavigationSearchViewHolder.LAYOUT_RES
            else -> -1
        }
    }

    override fun type(sellerSearchMinCharUiModel: SellerSearchMinCharUiModel): Int {
        return SellerSearchMinCharViewHolder.LAYOUT_RES
    }

    override fun type(sellerSearchNoHistoryUiModel: SellerSearchNoHistoryUiModel): Int {
        return SellerSearchNoHistoryViewHolder.LAYOUT_RES
    }

    override fun type(sellerSearchNoResultUiModel: SellerSearchNoResultUiModel): Int {
        return SellerSearchNoResultViewHolder.LAYOUT_RES
    }

    override fun type(viewModel: LoadingModel): Int {
        return ShimmerLoadingViewHolder.LAYOUT_RES
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when(type) {
            HistorySearchViewHolder.LAYOUT_RES -> HistorySearchViewHolder(parent, historySearchListener)
            OrderSearchViewHolder.LAYOUT_RES -> OrderSearchViewHolder(parent, orderSearchListener)
            ProductSearchViewHolder.LAYOUT_RES -> ProductSearchViewHolder(parent, productSearchListener)
            NavigationSearchViewHolder.LAYOUT_RES -> NavigationSearchViewHolder(parent, navigationSearchListener)
            ShimmerLoadingViewHolder.LAYOUT_RES -> ShimmerLoadingViewHolder(parent)
            SellerSearchMinCharViewHolder.LAYOUT_RES -> SellerSearchMinCharViewHolder(parent)
            SellerSearchNoHistoryViewHolder.LAYOUT_RES -> SellerSearchNoHistoryViewHolder(parent)
            SellerSearchNoResultViewHolder.LAYOUT_RES -> SellerSearchNoResultViewHolder(parent)
            else ->  super.createViewHolder(parent, type)
        }
    }

}