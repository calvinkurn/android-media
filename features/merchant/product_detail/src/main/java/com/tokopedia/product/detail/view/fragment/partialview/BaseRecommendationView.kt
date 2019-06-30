package com.tokopedia.product.detail.view.fragment.partialview

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.data.util.ProductTrackingConstant.PageNameRecommendation.PDP_1
import com.tokopedia.product.detail.data.util.ProductTrackingConstant.PageNameRecommendation.PDP_2
import com.tokopedia.product.detail.data.util.ProductTrackingConstant.PageNameRecommendation.PDP_3
import com.tokopedia.product.detail.data.util.ProductTrackingConstant.PageNameRecommendation.PDP_4
import com.tokopedia.product.detail.view.adapter.RecommendationProductAdapter
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import kotlinx.android.synthetic.main.partial_product_recom_1.view.*
import kotlinx.android.synthetic.main.partial_product_recom_2.view.*
import kotlinx.android.synthetic.main.partial_product_recom_3.view.*
import kotlinx.android.synthetic.main.partial_product_recom_4.view.*

abstract class BaseRecommendationView(context: Context) : View(context) {

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
                initAdapter(product)
                getRecyclerView().visible()
                visible()
            }
        }
    }

    private fun initAdapter(product: RecommendationWidget) {
        var pageName = ""
        getRecyclerView().layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)

        when (getView()) {
            base_recom_1 -> pageName = PDP_1
            base_recom_2 -> pageName = PDP_2
            base_recom_3 -> pageName = PDP_3
            base_recom_4 -> pageName = PDP_4
        }
        getRecyclerView().adapter = RecommendationProductAdapter(product, getListener(), pageName)

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