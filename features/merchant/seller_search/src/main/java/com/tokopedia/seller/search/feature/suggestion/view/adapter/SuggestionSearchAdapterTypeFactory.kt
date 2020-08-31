package com.tokopedia.seller.search.feature.suggestion.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.FAQ
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.NAVIGATION
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.ORDER
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.PRODUCT
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.*
import com.tokopedia.seller.search.feature.suggestion.view.model.LoadingSearchModel
import com.tokopedia.seller.search.feature.suggestion.view.model.SellerSearchNoResultUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.SellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.viewholder.faq.FaqSearchViewHolder
import com.tokopedia.seller.search.feature.suggestion.view.viewholder.navigation.NavigationSearchViewHolder
import com.tokopedia.seller.search.feature.suggestion.view.viewholder.order.OrderSearchViewHolder
import com.tokopedia.seller.search.feature.suggestion.view.viewholder.product.ProductSearchViewHolder

class SuggestionSearchAdapterTypeFactory(private val orderSearchListener: OrderSearchListener,
                                         private val productSearchListener: ProductSearchListener,
                                         private val navigationSearchListener: NavigationSearchListener,
                                         private val faqSearchListener: FaqSearchListener) : BaseAdapterTypeFactory(), TypeFactorySuggestionSearchAdapter {

    override fun type(sellerSearchUiModel: SellerSearchUiModel): Int {
        return when (sellerSearchUiModel.id) {
            ORDER -> OrderSearchViewHolder.LAYOUT_RES
            PRODUCT -> ProductSearchViewHolder.LAYOUT_RES
            NAVIGATION -> NavigationSearchViewHolder.LAYOUT_RES
            FAQ -> FaqSearchViewHolder.LAYOUT_RES
            else -> -1
        }
    }

    override fun type(loadingModel: LoadingSearchModel): Int {
        return ShimmerLoadingViewHolder.LAYOUT_RES
    }

    override fun type(sellerSearchNoResultUiModel: SellerSearchNoResultUiModel): Int {
        return SellerSearchNoResultViewHolder.LAYOUT_RES
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            OrderSearchViewHolder.LAYOUT_RES -> OrderSearchViewHolder(parent, orderSearchListener)
            ProductSearchViewHolder.LAYOUT_RES -> ProductSearchViewHolder(parent, productSearchListener)
            NavigationSearchViewHolder.LAYOUT_RES -> NavigationSearchViewHolder(parent, navigationSearchListener)
            FaqSearchViewHolder.LAYOUT_RES -> FaqSearchViewHolder(parent, faqSearchListener)
            ShimmerLoadingViewHolder.LAYOUT_RES -> ShimmerLoadingViewHolder(parent)
            SellerSearchNoResultViewHolder.LAYOUT_RES -> SellerSearchNoResultViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

}