package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneAddedProductDataModel
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationItemDataModel
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
            free_ongkir_image.gone()
            element.productImageUr?.let {
                image_view_added_product.loadImageRounded(it, resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_8))
            }
            button_go_to_cart.setOnClickListener {
                addToCartDoneAddedProductListener.onButtonGoToCartClicked()
            }
            element.bebasOngkirUrl?.let {
                if(it.isNotEmpty()) {
                    free_ongkir_image.show()
                    free_ongkir_image?.loadImage(it)
                }
            }
        }
    }

    interface AddToCartDoneAddedProductListener {
        fun onButtonGoToCartClicked()
        fun onRecommendationItemSelected(item: AddToCartDoneRecommendationItemDataModel, position: Int)
    }
}