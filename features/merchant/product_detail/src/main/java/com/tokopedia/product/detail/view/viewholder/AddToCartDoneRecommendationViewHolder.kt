package com.tokopedia.product.detail.view.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationDataModel
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationProductDataModel
import com.tokopedia.product.detail.view.adapter.AddToCartRecommendationProductAdapter
import com.tokopedia.product.detail.view.adapter.RecommendationProductTypeFactory
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import kotlinx.android.synthetic.main.add_to_cart_done_recommendation_layout.view.*

class AddToCartDoneRecommendationViewHolder(
        itemView: View,
        private val recommendationListener: RecommendationListener
) : AbstractViewHolder<AddToCartDoneRecommendationDataModel>(itemView) {

    companion object {
        val LAYOUT_RES = R.layout.add_to_cart_done_recommendation_layout
    }

    private val adapter: AddToCartRecommendationProductAdapter by lazy {
        AddToCartRecommendationProductAdapter(fact)
    }

    private val fact: RecommendationProductTypeFactory by lazy {
        RecommendationProductTypeFactory(recommendationListener)
    }

    override fun bind(element: AddToCartDoneRecommendationDataModel) {
        with(itemView) {
            title_recom.text = element.recommendationWidget.title
            product_recom.layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
            )
            for (ls in element.recommendationWidget.recommendationItemList) {
                adapter.addElement(AddToCartDoneRecommendationProductDataModel(
                        ls,
                        adapterPosition
                ))
            }
            product_recom.adapter = adapter
            visible()
        }
    }

    fun updateWishlist(position: Int, isAddWishlist: Boolean) {
        if (adapter.data[position] is AddToCartDoneRecommendationProductDataModel) {
            adapter.data[position].recommendationItem.isWishlist = isAddWishlist
            adapter.notifyItemChanged(position)
        }
    }

}