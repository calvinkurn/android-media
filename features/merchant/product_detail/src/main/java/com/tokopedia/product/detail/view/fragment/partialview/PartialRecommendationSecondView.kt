package com.tokopedia.product.detail.view.fragment.partialview

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.tokopedia.product.detail.view.adapter.RecommendationProductAdapter
import kotlinx.android.synthetic.main.partial_product_recom_2.view.*

class PartialRecommendationSecondView private constructor(private val view: View,
                                                          private val userActiveListener: RecommendationProductAdapter.UserActiveListener)
    :BaseRecommendationView(view.context){

    companion object {
        fun build(_view: View, _userActiveListener: RecommendationProductAdapter.UserActiveListener) =
                PartialRecommendationSecondView(_view, _userActiveListener)
    }

    override fun getLayoutProgress(): ProgressBar = view.loading_recom_2

    override fun getListener(): RecommendationProductAdapter.UserActiveListener = userActiveListener

    override fun getLayoutTitle(): TextView = view.title_recom_2

    override fun getView(): View = view.base_recom_2

    override fun getRecyclerView(): RecyclerView = view.product_recom_2
}