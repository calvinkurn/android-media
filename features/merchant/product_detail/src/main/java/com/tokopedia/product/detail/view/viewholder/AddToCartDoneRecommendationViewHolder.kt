package com.tokopedia.product.detail.view.viewholder

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationViewModel
import com.tokopedia.product.detail.data.model.addtocartrecommendation.RecommendationProductViewModel
import com.tokopedia.product.detail.view.adapter.AddToCartRecommendationProductAdapter
import com.tokopedia.product.detail.view.adapter.RecommendationProductTypeFactory
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.android.synthetic.main.add_to_cart_done_recommendation_layout.view.*

class AddToCartDoneRecommendationViewHolder(
        itemView: View,
        val recommendationListener: RecommendationListener
) : AbstractViewHolder<AddToCartDoneRecommendationViewModel>(itemView) {

    private val adapter: AddToCartRecommendationProductAdapter by lazy {
        AddToCartRecommendationProductAdapter(fact)
    }

    private val fact: RecommendationProductTypeFactory by lazy {
        RecommendationProductTypeFactory(recommendationListener)
    }

    override fun bind(element: AddToCartDoneRecommendationViewModel) {
        with(itemView) {
            title_recom.text = element.recommendationWidget.title
            product_recom.layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
            )
            for (ls in element.recommendationWidget.recommendationItemList) {
                adapter.addElement(RecommendationProductViewModel(ls))
            }
            product_recom.adapter = adapter
            adapter.notifyDataSetChanged()
            visible()
        }
    }

    fun updateWishlist(position: Int, isAddWishlist: Boolean) {
        if (adapter.data[position] is RecommendationProductViewModel) {
            adapter.data[position].recommendationItem.isWishlist = isAddWishlist
            adapter.notifyItemChanged(position)
        }
    }

    companion object {
        val LAYOUT_RES = R.layout.add_to_cart_done_recommendation_layout
    }
}