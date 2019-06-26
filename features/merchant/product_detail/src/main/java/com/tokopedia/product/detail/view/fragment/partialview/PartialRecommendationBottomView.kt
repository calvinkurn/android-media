package com.tokopedia.product.detail.view.fragment.partialview

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.tokopedia.product.detail.view.adapter.RecommendationProductAdapter
import kotlinx.android.synthetic.main.partial_product_recom_bottom.view.*

class PartialRecommendationBottomView private constructor(private val view: View,
                                                          private val userActiveListener: RecommendationProductAdapter.UserActiveListener)
    :BaseRecommendationView(){


    companion object{
        fun build(_view:View,_userActiveListener: RecommendationProductAdapter.UserActiveListener) =
                PartialRecommendationBottomView(_view,_userActiveListener)
    }


    override fun getListener(): RecommendationProductAdapter.UserActiveListener = userActiveListener

    override fun getLayoutTitle(): TextView = with(view) {
        title_recom_3
    }

    override fun getView(): View = view

    override fun getRecyclerView(): RecyclerView = with(view) {
        product_recom_3
    }

    override fun getLayoutProgress(): ProgressBar =with(view) {
        loading_recom_3
    }

}