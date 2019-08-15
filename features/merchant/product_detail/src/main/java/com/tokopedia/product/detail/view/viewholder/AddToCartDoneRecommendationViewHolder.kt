package com.tokopedia.product.detail.view.viewholder

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationViewModel
import com.tokopedia.product.detail.view.adapter.RecommendationProductAdapter
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.android.synthetic.main.add_to_cart_done_recommendation_layout.view.*

class AddToCartDoneRecommendationViewHolder(
        itemView: View,
        val listener : RecommendationProductAdapter.UserActiveListener,
        val trackingQueue: TrackingQueue
) : AbstractViewHolder<AddToCartDoneRecommendationViewModel>(itemView) {
    override fun bind(element: AddToCartDoneRecommendationViewModel) {
        with(itemView){
            title_recom.text = element.recommendationWidget.title
            product_recom.layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
            )
            val adapter = RecommendationProductAdapter(
                    element.recommendationWidget,
                    listener,
                    element.recommendationWidget.pageName,
                    trackingQueue
            )
            product_recom.adapter = adapter
            product_recom.visible()
            visible()
//            adapter.notifyDataSetChanged()
        }
    }

    companion object {
        val LAYOUT_RES = R.layout.add_to_cart_done_recommendation_layout
    }
}