package com.tokopedia.product.detail.view.fragment.partialview

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.view.adapter.RecommendationProductAdapter
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import kotlinx.android.synthetic.main.partial_product_recommendation.view.*

abstract class BaseRecommendationView() {


    fun hideView() {
        getView().gone()
    }

    fun renderData(product: RecommendationWidget) {
        with(getView()) {
            getLayoutProgress().gone()
            if (product.recommendationItemList.isEmpty())
                gone()
            else {
                getLayoutTitle().text = product.title
                getRecyclerView().layoutManager = LinearLayoutManager(context,
                        LinearLayoutManager.HORIZONTAL, false)
                getRecyclerView().adapter = RecommendationProductAdapter(product, getListener())
                getRecyclerView().visible()
                visible()
            }
        }
    }

    fun startLoading() {
        with(getView()) {
            visible()
            getLayoutProgress().visible()
            getRecyclerView().gone()
        }
    }

    abstract fun getListener(): RecommendationProductAdapter.UserActiveListener
    abstract fun getLayoutTitle(): TextView
    abstract fun getView(): View
    abstract fun getRecyclerView(): RecyclerView
    abstract fun getLayoutProgress(): ProgressBar


}