package com.tokopedia.product.detail.view.fragment.partialview

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.view.adapter.RecommendationProductAdapter
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import kotlinx.android.synthetic.main.partial_product_recommendation.view.*

class PartialRecommendationProductView private constructor(private val view: View) {


    companion object {
        fun build(_view: View) = PartialRecommendationProductView(_view)
    }

    init {
        with(view) {
            recommendation_product.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    fun renderData(products: RecommendationWidget) {
        with(view) {
            loading_recommen_product.gone()
            if (products.recommendationItemList.isEmpty())
                gone()
            else {
                title_recommen_product.text = products.title
                recommendation_product.adapter = RecommendationProductAdapter(products)
                recommendation_product.visible()
                visible()
            }
        }

    }

    fun hideView() {
        with(view) {
            gone()
        }
    }

    fun startLoading() {
        with(view) {
            visible()
            loading_recommen_product.visible()
            recommendation_product.gone()
        }
    }

}