package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneAddedProductDataModel
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationItemDataModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import kotlinx.android.synthetic.main.add_to_cart_done_added_product_layout.view.*

class AddToCartDoneAddedProductViewHolder(
        itemView: View,
        private val addToCartDoneAddedProductListener: AddToCartDoneAddedProductListener
) : AbstractViewHolder<AddToCartDoneAddedProductDataModel>(itemView) {

    companion object {
        val LAYOUT_RES = R.layout.add_to_cart_done_added_product_layout
    }

    override fun bind(element: AddToCartDoneAddedProductDataModel) {
        with(itemView) {
            element.productImageUr?.let {
                image_view_added_product.loadImageRounded(it, resources.getDimension(R.dimen.dp_8))
            }
            button_go_to_cart.setOnClickListener {
                addToCartDoneAddedProductListener.onButtonGoToCartClicked()
            }
        }
    }

    interface AddToCartDoneAddedProductListener {
        fun onButtonGoToCartClicked()
        fun onRecommendationItemSelected(item: AddToCartDoneRecommendationItemDataModel, position: Int)
    }
}