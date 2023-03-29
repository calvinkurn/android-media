package com.tokopedia.tokopedianow.similarproduct.viewholder

import android.graphics.Paint
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeProductBinding
import com.tokopedia.tokopedianow.similarproduct.model.SimilarProductUiModel
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.view.binding.viewBinding

class SimilarProductViewHolder(
    itemView: View,
    private val listener: SimilarProductListener?
    ) : AbstractViewHolder<SimilarProductUiModel>(itemView) {

    companion object{
        private const val QUANTITY_ZERO = 0
        val LAYOUT = R.layout.item_tokopedianow_recipe_product
    }

    private var binding: ItemTokopedianowRecipeProductBinding? by viewBinding()

    override fun bind(product: SimilarProductUiModel) {
        renderProductInfo(product)
        renderSlashedPrice(product)
        renderDiscountLabel(product)
        renderProductButton(product)
        renderQuantityEditor(product)
        renderDeleteBtn(product)
        setOnClickListener(product)
        impressHolder(product)
    }

    override fun bind(product: SimilarProductUiModel?, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == true && product != null) {
            renderProductInfo(product)
            renderSlashedPrice(product)
            renderDiscountLabel(product)
            renderProductButton(product)
            renderQuantityEditor(product)
            renderDeleteBtn(product)
            setOnClickListener(product)
        }
    }

    private fun renderProductInfo(product: SimilarProductUiModel) {
        binding?.apply {
            textName.text = product.name
            textPrice.text = product.priceFmt
            textWeight.text = product.weight
            imgProduct.setImageUrl(product.imageUrl)
        }
    }

    private fun renderSlashedPrice(product: SimilarProductUiModel) {
        binding?.textSlashedPrice?.apply {
            if (product.slashedPrice.isNotEmpty()) {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text = product.slashedPrice
                show()
            } else {
                hide()
            }
        }
    }

    private fun renderDiscountLabel(product: SimilarProductUiModel) {
        binding?.labelDiscount?.apply {
            if (product.discountPercentage.isNotEmpty()) {
                text = context.getString(
                    R.string.tokopedianow_percentage_format,
                    product.discountPercentage
                )
                show()
            } else {
                hide()
            }
        }
    }

    private fun renderProductButton(product: SimilarProductUiModel) {
        binding?.btnProductCta?.apply {
            if (product.stock == QUANTITY_ZERO) {
                text = context.getString(R.string.tokopedianow_stock_empty_text)
                buttonVariant = UnifyButton.Variant.FILLED
                isEnabled = false
            } else {
                text = context.getString(R.string.tokopedianow_add_to_cart_text)
                buttonVariant = UnifyButton.Variant.GHOST
                isEnabled = true

                setOnClickListener {
                    listener?.addItemToCart(
                        productId = product.id,
                        shopId = product.shopId,
                        quantity = product.minOrder
                    )
                }
            }
        }
    }

    private fun renderQuantityEditor(product: SimilarProductUiModel) {
        binding?.apply {
            val stock = product.stock
            val quantity = product.quantity

            if (stock > QUANTITY_ZERO && quantity > QUANTITY_ZERO) {
                btnDeleteCart.show()
                btnProductCta.hide()
                quantityEditor.show()
                quantityEditor.minValue = product.minOrder
                quantityEditor.maxValue = product.maxOrder
                quantityEditor.setValue(quantity)

                quantityEditor.setAddClickListener {
                    onQuantityChanged(product)
                }

                quantityEditor.setSubstractListener {
                    onQuantityChanged(product)
                }

                quantityEditor.editText.setOnEditorActionListener { _, _, _ ->
                    onEditorAction(product)
                    true
                }
            } else {
                btnDeleteCart.hide()
                btnProductCta.show()
                quantityEditor.hide()
            }
        }
    }

    private fun renderDeleteBtn(product: SimilarProductUiModel) {
        binding?.btnDeleteCart?.setOnClickListener {
            listener?.deleteCartItem(product.id)
        }
    }

    private fun setOnClickListener(product: SimilarProductUiModel) {
        binding?.root?.setOnClickListener {
            listener?.onProductClicked(product)
        }
    }

    private fun onQuantityChanged(product: SimilarProductUiModel) {
        val input = binding?.quantityEditor?.getValue().orZero()
        listener?.onQuantityChanged(product.id, product.shopId, input)
    }

    private fun onEditorAction(product: SimilarProductUiModel) {
        binding?.quantityEditor?.apply {
            val input = getValue()
            addButton.isEnabled = input < product.maxOrder
            subtractButton.isEnabled = input > product.minOrder
            onQuantityChanged(product)
            closeSoftKeyboard()
        }
    }

    private fun impressHolder(product: SimilarProductUiModel) {
        itemView.addOnImpressionListener(product.impressHolder) {
            listener?.onProductImpressed(product)
        }
    }

    private fun QuantityEditorUnify.closeSoftKeyboard() {
        KeyboardHandler.DropKeyboard(itemView.context, this)
    }

    interface SimilarProductListener {
        fun deleteCartItem(productId: String)
        fun onQuantityChanged(productId: String, shopId: String, quantity: Int)
        fun addItemToCart(productId: String, shopId: String, quantity: Int)
        fun onProductClicked(product: SimilarProductUiModel)
        fun onProductImpressed(product: SimilarProductUiModel)
    }
}
