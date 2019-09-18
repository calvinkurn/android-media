package com.tokopedia.product.detail.view.fragment.partialview

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.product.detail.view.adapter.RecommendationProductAdapter
import kotlinx.android.synthetic.main.partial_product_recom_2.view.*

class PartialRecommendationSecondView private constructor(private val view: View,
                                                          private val userActiveListener: RecommendationProductAdapter.UserActiveListener,
                                                          productDetailTracking: ProductDetailTracking)
    :BaseRecommendationView(view.context, productDetailTracking){

    companion object {
        fun build(_view: View, _userActiveListener: RecommendationProductAdapter.UserActiveListener, productDetailTracking: ProductDetailTracking) =
                PartialRecommendationSecondView(_view, _userActiveListener, productDetailTracking)
    }

    override fun getLayoutProgress(): View = view.loading_recom_2

    override fun getListener(): RecommendationProductAdapter.UserActiveListener = userActiveListener

    override fun getLayoutTitle(): TextView = view.title_recom_2

    override fun getView(): View = view.base_recom_2

    override fun getRecyclerView(): RecyclerView = view.product_recom_2
}