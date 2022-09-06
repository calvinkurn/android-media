package com.tokopedia.tokopedianow.recipedetail.presentation.viewholders

import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeProductBinding
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeProductUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.view.binding.viewBinding

class RecipeProductViewHolder(
    itemView: View,
    private val listener: RecipeProductListener?
) : AbstractViewHolder<RecipeProductUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_tokopedianow_recipe_product
    }

    private val context by lazy { itemView.context }

    private var binding: ItemTokopedianowRecipeProductBinding? by viewBinding()

    private var qtyEditorListener: TextWatcher? = null

    override fun bind(product: RecipeProductUiModel) {
        renderProductInfo(product)
        renderSlashedPrice(product)
        renderDiscountLabel(product)
        renderProductButton(product)
        renderQuantityEditor(product)
        renderDeleteBtn(product)
        setOnClickListener(product)
    }

    private fun renderProductInfo(product: RecipeProductUiModel) {
        binding?.apply {
            textName.text = product.name
            textPrice.text = product.priceFmt
            textWeight.text = product.weight
            imgProduct.loadImage(product.imageUrl)
        }
    }

    private fun renderSlashedPrice(product: RecipeProductUiModel) {
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

    private fun renderDiscountLabel(product: RecipeProductUiModel) {
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

    private fun renderProductButton(product: RecipeProductUiModel) {
        binding?.btnProductCta?.apply {
            if (product.stock == 0) {
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

    private fun renderQuantityEditor(product: RecipeProductUiModel) {
        binding?.apply {
            val stock = product.stock
            val quantity = product.quantity

            removeTextChangeListener(qtyEditorListener)
            qtyEditorListener = qtyEditorListener(product)

            if (stock > 0 && quantity > 0) {
                btnDeleteCart.show()
                btnProductCta.hide()
                quantityEditor.show()
                quantityEditor.minValue = product.minOrder
                quantityEditor.maxValue = product.maxOrder
                quantityEditor.setValue(quantity)
                addTextChangeListener(qtyEditorListener)
            } else {
                btnDeleteCart.hide()
                btnProductCta.show()
                quantityEditor.hide()
            }
        }
    }

    private fun addTextChangeListener(qtyEditorListener: TextWatcher?) {
        binding?.quantityEditor?.editText
            ?.addTextChangedListener(qtyEditorListener)
    }

    private fun removeTextChangeListener(qtyEditorListener: TextWatcher?) {
        binding?.quantityEditor?.editText
            ?.removeTextChangedListener(qtyEditorListener)
    }

    private fun renderDeleteBtn(product: RecipeProductUiModel) {
        binding?.btnDeleteCart?.setOnClickListener {
            listener?.deleteCartItem(product.id)
        }
    }

    private fun setOnClickListener(item: RecipeProductUiModel) {
        binding?.root?.setOnClickListener {
            goToProductDetailPage(item)
        }

        binding?.textSimilarProduct?.setOnClickListener {
            openSimilarProductBottomSheet()
        }
    }

    private fun goToProductDetailPage(item: RecipeProductUiModel) {
        RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, item.id)
    }

    private fun openSimilarProductBottomSheet() {
        RouteManager.route(context, ApplinkConstInternalTokopediaNow.RECIPE_SIMILAR_PRODUCT_BOTTOM_SHEET)
    }

    private fun qtyEditorListener(product: RecipeProductUiModel): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                val input = p0?.toString()

                if(input?.isNotEmpty() == true) {
                    listener?.onQuantityChanged(
                        productId = product.id,
                        shopId = product.shopId,
                        quantity = input.toIntOrZero()
                    )
                }
            }
        }
    }

    interface RecipeProductListener {
        fun deleteCartItem(productId: String)
        fun onQuantityChanged(productId: String, shopId: String, quantity: Int)
        fun addItemToCart(productId: String, shopId: String, quantity: Int)
    }
}