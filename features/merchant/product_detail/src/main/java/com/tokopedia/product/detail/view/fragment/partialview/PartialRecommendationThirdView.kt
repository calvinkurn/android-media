package com.tokopedia.product.detail.view.fragment.partialview

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.product.detail.view.adapter.RecommendationProductAdapter
import kotlinx.android.synthetic.main.partial_product_recom_3.view.*

class PartialRecommendationThirdView private constructor(private val view: View,
                                                         private val userActiveListener: RecommendationProductAdapter.UserActiveListener,
                                                         productDetailTracking: ProductDetailTracking)
    :BaseRecommendationView(view.context, productDetailTracking){

    companion object{
        fun build(_view:View,_userActiveListener: RecommendationProductAdapter.UserActiveListener, productDetailTracking: ProductDetailTracking) =
                PartialRecommendationThirdView(_view,_userActiveListener, productDetailTracking)
    }

    override fun getListener(): RecommendationProductAdapter.UserActiveListener = userActiveListener

    override fun getLayoutTitle(): TextView = view.title_recom_3

    override fun getView(): View = view.base_recom_3

    override fun getRecyclerView(): RecyclerView = view.product_recom_3

    override fun getLayoutProgress(): View = view.loading_recom_3

}