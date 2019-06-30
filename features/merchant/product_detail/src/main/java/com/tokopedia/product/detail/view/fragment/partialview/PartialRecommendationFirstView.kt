package com.tokopedia.product.detail.view.fragment.partialview

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.view.adapter.RecommendationProductAdapter
import kotlinx.android.synthetic.main.partial_product_recom_1.view.*


class PartialRecommendationFirstView private constructor(private val view: View,
                                                         private val userActiveListener: RecommendationProductAdapter.UserActiveListener)
    : BaseRecommendationView(view.context) {


    companion object {
        fun build(_view: View, _userActiveListener: RecommendationProductAdapter.UserActiveListener) =
                PartialRecommendationFirstView(_view, _userActiveListener)
    }

    override fun getLayoutTitle(): TextView = view.findViewById(R.id.title_recom_1)

    override fun getListener(): RecommendationProductAdapter.UserActiveListener = userActiveListener

    override fun getView(): View = view.base_recom_1

    override fun getRecyclerView(): RecyclerView = view.findViewById(R.id.product_recom_1)

    override fun getLayoutProgress(): ProgressBar = view.findViewById(R.id.loading_recom_1)

}