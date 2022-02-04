package com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder

import android.view.View

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.databinding.ItemMvcLockedToProductProductGridCardLayoutBinding
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductGridProductUiModel
import com.tokopedia.utils.view.binding.viewBinding

open class MvcLockedToProductGridViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<MvcLockedToProductGridProductUiModel>(itemView) {
    private val viewBinding: ItemMvcLockedToProductProductGridCardLayoutBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_mvc_locked_to_product_product_grid_card_layout
    }

    interface Listener {
        fun onProductClicked(index: Int, uiModel: MvcLockedToProductGridProductUiModel)
        fun onOpenVariantBottomSheet(uiModel: MvcLockedToProductGridProductUiModel)
        fun onProductVariantQuantityZero(productId: String)
    }

    override fun bind(uiModel: MvcLockedToProductGridProductUiModel) {
        setProductCardData(uiModel)
    }

    private fun setProductCardData(uiModel: MvcLockedToProductGridProductUiModel) {
        viewBinding?.productCardView?.apply {
            setProductModel(uiModel.productCardModel)
            uiModel.productCardModel.countSoldRating
            setOnClickListener {
                listener.onProductClicked(adapterPosition, uiModel)
            }
            setAddToCartNonVariantClickListener(object: ATCNonVariantListener{
                override fun onQuantityChanged(quantity: Int) {
                    if(quantity == 0 && uiModel.isVariant){
                        listener.onProductVariantQuantityZero(uiModel.productID)
                    }
                }
            })
            setAddToCartOnClickListener {
                listener.onOpenVariantBottomSheet(uiModel)
            }
        }
    }

}