package com.tokopedia.home_wishlist.view.viewholder

import android.view.View
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.datamodel.RecommendationItemDataModel
import com.tokopedia.home_wishlist.view.listener.WishlistListener
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener

class RecommendationItemViewHolder(view: View) : SmartAbstractViewHolder<RecommendationItemDataModel>(view){

    private val parentPositionDefault: Int = -1
    private val productCardView: ProductCardGridView by lazy { view.findViewById<ProductCardGridView>(R.id.product_item) }

    override fun bind(element: RecommendationItemDataModel, listener: SmartListener) {
        productCardView.run {
            setProductModel(element.recommendationItem.toProductCardModel())

            setImageProductViewHintListener(element.recommendationItem, object: ViewHintListener{
                override fun onViewHint() {
                    (listener as WishlistListener).onProductImpression(element, adapterPosition)
                }
            })

            setOnClickListener {
                (listener as WishlistListener).onProductClick(element, parentPositionDefault, adapterPosition)
            }
        }
    }

    companion object{
        val LAYOUT = R.layout.layout_recommendation_item
    }
}