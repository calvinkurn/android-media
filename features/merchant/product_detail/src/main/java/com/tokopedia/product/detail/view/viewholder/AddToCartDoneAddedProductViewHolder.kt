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
import com.tokopedia.product.detail.databinding.AddToCartDoneAddedProductLayoutBinding

class AddToCartDoneAddedProductViewHolder(
        itemView: View,
        private val addToCartDoneAddedProductListener: AddToCartDoneAddedProductListener
) : AbstractViewHolder<AddToCartDoneAddedProductDataModel>(itemView) {

    companion object {
        val LAYOUT_RES = R.layout.add_to_cart_done_added_product_layout
    }

    private val binding = AddToCartDoneAddedProductLayoutBinding.bind(itemView)

    override fun bind(element: AddToCartDoneAddedProductDataModel) {
        with(binding) {
            freeOngkirImage.gone()
            element.productImageUr?.let {
                imageViewAddedProduct.loadImageRounded(it, itemView.resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_8))
            }
            buttonGoToCart.setOnClickListener {
                addToCartDoneAddedProductListener.onButtonGoToCartClicked()
            }
            element.bebasOngkirUrl?.let {
                if(it.isNotEmpty()) {
                    freeOngkirImage.show()
                    freeOngkirImage.loadImage(it)
                }
            }
        }
    }

    interface AddToCartDoneAddedProductListener {
        fun onButtonGoToCartClicked()
        fun onRecommendationItemSelected(item: AddToCartDoneRecommendationItemDataModel, position: Int)
    }
}