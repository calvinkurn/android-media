package com.tokopedia.product.detail.view.fragment.partialview

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.product.detail.view.adapter.RecommendationProductAdapter
import kotlinx.android.synthetic.main.partial_product_recom_4.view.*

class PartialRecommendationFourthView private constructor(private val view: View,
                                                          private val userActiveListener: RecommendationProductAdapter.UserActiveListener,
                                                          productDetailTracking: ProductDetailTracking,
                                                          private val activity: Activity?)
    : BaseRecommendationView(view.context, productDetailTracking, activity) {

    companion object {
        fun build(_view:View,_userActiveListener: RecommendationProductAdapter.UserActiveListener, productDetailTracking: ProductDetailTracking, activity: Activity) =
                PartialRecommendationFourthView(_view,_userActiveListener, productDetailTracking, activity)
    }

    override fun getListener(): RecommendationProductAdapter.UserActiveListener = userActiveListener

    override fun getLayoutTitle(): TextView = view.title_recom_4

    override fun getView(): View = view.base_recom_4

    override fun getRecyclerView(): CarouselProductCardView = view.product_recom_4

    override fun getLayoutProgress(): View = view.loading_recom_4

}
