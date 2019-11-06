package com.tokopedia.product.detail.view.fragment.partialview

import android.app.Activity
import android.view.View
import android.widget.TextView
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.product.detail.view.adapter.RecommendationProductAdapter
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.partial_product_recom_3.view.*

class PartialRecommendationThirdView private constructor(private val view: View,
                                                         private val userActiveListener: RecommendationProductAdapter.UserActiveListener,
                                                         productDetailTracking: ProductDetailTracking,
                                                         activity: Activity?)
    :BaseRecommendationView(view.context, productDetailTracking, activity){

    companion object {
        fun build(_view:View,_userActiveListener: RecommendationProductAdapter.UserActiveListener, productDetailTracking: ProductDetailTracking, activity: Activity) =
                PartialRecommendationThirdView(_view,_userActiveListener, productDetailTracking, activity)
    }

    override fun getListener(): RecommendationProductAdapter.UserActiveListener = userActiveListener

    override fun getLayoutTitle(): TextView = view.title_recom_3

    override fun getView(): View = view.base_recom_3

    override fun getRecyclerView(): CarouselProductCardView = view.product_recom_3

    override fun getLayoutProgress(): View = view.loading_recom_3

    override fun getSeeMore(): Typography = view.findViewById(R.id.see_more_recom_3)

}