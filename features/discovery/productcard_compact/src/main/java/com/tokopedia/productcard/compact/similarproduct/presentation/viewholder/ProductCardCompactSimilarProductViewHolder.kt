package com.tokopedia.productcard.compact.similarproduct.presentation.viewholder

import android.graphics.Paint
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.compact.similarproduct.presentation.uimodel.ProductCardCompactSimilarProductUiModel
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.productcard.compact.R
import com.tokopedia.productcard.compact.databinding.ItemProductCardCompactSimilarProductBinding

class ProductCardCompactSimilarProductViewHolder(
    itemView: View,
    private val listener: SimilarProductListener?
    ) : AbstractViewHolder<ProductCardCompactSimilarProductUiModel>(itemView) {

    companion object{
        private const val QUANTITY_ZERO = 0
        val LAYOUT = R.layout.item_product_card_compact_similar_product
    }

    private var binding: ItemProductCardCompactSimilarProductBinding? by viewBinding()

    override fun bind(product: ProductCardCompactSimilarProductUiModel) {
        renderProductInfo(product)
        renderSlashedPrice(product)
        renderDiscountLabel(product)
        renderProductButton(product)
        renderQuantityEditor(product)
        renderDeleteBtn(product)
        setOnClickListener(product)
        impressHolder(product)
    }

    override fun bind(product: ProductCardCompactSimilarProductUiModel?, payloads: MutableList<Any>) {
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

    private fun renderProductInfo(product: ProductCardCompactSimilarProductUiModel) {
        binding?.apply {
            textName.text = product.name
            textPrice.text = product.priceFmt
            textWeight.text = product.weight
            imgProduct.setImageUrl(product.imageUrl)
        }
    }

    private fun renderSlashedPrice(product: ProductCardCompactSimilarProductUiModel) {
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

    private fun renderDiscountLabel(product: ProductCardCompactSimilarProductUiModel) {
        binding?.labelDiscount?.apply {
            if (product.discountPercentage.isNotEmpty()) {
                text = context.getString(
                    R.string.product_card_compact_similar_product_bottom_sheet_percentage_format,
                    product.discountPercentage
                )
                show()
            } else {
                hide()
            }
        }
    }

    private fun renderProductButton(product: ProductCardCompactSimilarProductUiModel) {
        binding?.btnProductCta?.apply {
            if (product.stock == QUANTITY_ZERO) {
                text = context.getString(R.string.product_card_compact_similar_product_bottom_sheet_stock_empty_text)
                buttonVariant = UnifyButton.Variant.FILLED
                isEnabled = false
            } else {
                text = context.getString(R.string.product_card_compact_add_to_cart)
                buttonVariant = UnifyButton.Variant.GHOST
                isEnabled = true

                setOnClickListener {
                    listener?.onCartQuantityChanged(
                        productId = product.id,
                        shopId = product.shopId,
                        quantity = product.minOrder
                    )
                }
            }
        }
    }

    private fun renderQuantityEditor(product: ProductCardCompactSimilarProductUiModel) {
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

    private fun renderDeleteBtn(product: ProductCardCompactSimilarProductUiModel) {
        binding?.btnDeleteCart?.setOnClickListener {
            listener?.onCartQuantityChanged(product.id, product.shopId, QUANTITY_ZERO)
        }
    }

    private fun setOnClickListener(product: ProductCardCompactSimilarProductUiModel) {
        binding?.root?.setOnClickListener {
            listener?.onProductClicked(product)
        }
    }

    private fun onQuantityChanged(product: ProductCardCompactSimilarProductUiModel) {
        val input = binding?.quantityEditor?.getValue().orZero()
        listener?.onCartQuantityChanged(product.id, product.shopId, input)
    }

    private fun onEditorAction(product: ProductCardCompactSimilarProductUiModel) {
        binding?.quantityEditor?.apply {
            val input = getValue()
            addButton.isEnabled = input < product.maxOrder
            subtractButton.isEnabled = input > product.minOrder
            onQuantityChanged(product)
            closeSoftKeyboard()
        }
    }

    private fun impressHolder(product: ProductCardCompactSimilarProductUiModel) {
        itemView.addOnImpressionListener(product.impressHolder) {
            listener?.onProductImpressed(product)
        }
    }

    private fun QuantityEditorUnify.closeSoftKeyboard() {
        KeyboardHandler.DropKeyboard(itemView.context, this)
    }

    interface SimilarProductListener {
        fun onCartQuantityChanged(productId: String, shopId: String, quantity: Int)
        fun onProductClicked(product: ProductCardCompactSimilarProductUiModel)
        fun onProductImpressed(product: ProductCardCompactSimilarProductUiModel)
    }
}
