package com.tokopedia.product.detail.view.fragment.partialview

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.tokopedia.product.detail.view.adapter.RecommendationProductAdapter
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.android.synthetic.main.partial_product_recom_3.view.*

class PartialRecommendationThirdView private constructor(private val view: View,
                                                         private val userActiveListener: RecommendationProductAdapter.UserActiveListener,
                                                         trackingQueue: TrackingQueue)
    :BaseRecommendationView(view.context, trackingQueue){

    companion object{
        fun build(_view:View,_userActiveListener: RecommendationProductAdapter.UserActiveListener, trackingQueue: TrackingQueue) =
                PartialRecommendationThirdView(_view,_userActiveListener, trackingQueue)
    }

    override fun getListener(): RecommendationProductAdapter.UserActiveListener = userActiveListener

    override fun getLayoutTitle(): TextView = view.title_recom_3

    override fun getView(): View = view.base_recom_3

    override fun getRecyclerView(): RecyclerView = view.product_recom_3

    override fun getLayoutProgress(): ProgressBar = view.loading_recom_3

}