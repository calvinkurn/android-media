package com.tokopedia.officialstore.official.presentation.adapter.viewmodel

import android.util.Log
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapterTypeFactory
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem


class ProductRecommendationViewModel(
        val productItem: RecommendationItem,
        val listener: RecommendationListener
) : Visitable<OfficialHomeAdapterTypeFactory> {

    private val adapter: OfficialHomeAdapter by lazy {
        OfficialHomeAdapter(fact)
    }

    private val fact: OfficialHomeAdapterTypeFactory by lazy {
        OfficialHomeAdapterTypeFactory(listener)
    }

    override fun type(adapterTypeFactory: OfficialHomeAdapterTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }

    fun getProductRecommendationList() {
        Log.d("products: ", productItem.toString())
    }

//    fun updateWishlist(position: Int, isAddWishlist: Boolean) {
//        if (adapter.getDataByPosition(position) is ProductRecommendationViewModel) {
//            (adapter.getDataByPosition(position) as ProductRecommendationViewModel).productItem.isWishlist = isAddWishlist
//            adapter.notifyItemChanged(position)
//        }
//    }

    companion object {
        val LAYOUT = R.layout.viewmodel_product_recommendation_item
    }
}