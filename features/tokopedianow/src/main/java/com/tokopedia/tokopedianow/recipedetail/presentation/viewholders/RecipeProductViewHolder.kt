package com.tokopedia.tokopedianow.recipedetail.presentation.viewholders

import android.graphics.Paint
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeProductBinding
import com.tokopedia.tokopedianow.recipedetail.analytics.ProductAnalytics
import com.tokopedia.tokopedianow.recipedetail.presentation.activity.TokoNowRecipeSimilarProductActivity
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeProductUiModel
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.view.binding.viewBinding

class RecipeProductViewHolder(
    itemView: View,
    private val listener: RecipeProductListener?,
    private val analytics: ProductAnalytics?
) : AbstractViewHolder<RecipeProductUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_tokopedianow_recipe_product
    }

    private val context by lazy { itemView.context }

    private var binding: ItemTokopedianowRecipeProductBinding? by viewBinding()

    override fun bind(product: RecipeProductUiModel) {
        addImpressionListener(product)
        renderProductInfo(product)
        renderSlashedPrice(product)
        renderDiscountLabel(product)
        renderProductButton(product)
        renderQuantityEditor(product)
        renderDeleteBtn(product)
        renderSimilarProductBtn(product)
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
                    analytics?.trackClickAddToCart(product)
                }
            }
        }
    }

    private fun renderQuantityEditor(product: RecipeProductUiModel) {
        binding?.apply {
            val stock = product.stock
            val quantity = product.quantity

            if (stock > 0 && quantity > 0) {
                btnDeleteCart.show()
                btnProductCta.hide()
                quantityEditor.show()
                quantityEditor.minValue = product.minOrder
                quantityEditor.maxValue = product.maxOrder
                quantityEditor.setValue(quantity)

                quantityEditor.setAddClickListener {
                    onQuantityChanged(product)
                    analytics?.trackClickIncreaseQuantity()
                }

                quantityEditor.setSubstractListener {
                    onQuantityChanged(product)
                    analytics?.trackClickDecreaseQuantity()
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

    private fun renderSimilarProductBtn(product: RecipeProductUiModel) {
        if(product.similarProducts.isNotEmpty()) {
            binding?.textSimilarProduct?.setOnClickListener {
                openSimilarProductBottomSheet(product)
                trackClickSimilarProductBtn()
            }

            binding?.textSimilarProduct?.show()
            binding?.imageChevron?.show()
            trackImpressionSimilarProductBtn()
        } else {
            binding?.textSimilarProduct?.hide()
            binding?.imageChevron?.hide()
        }
    }

    private fun renderDeleteBtn(product: RecipeProductUiModel) {
        binding?.btnDeleteCart?.setOnClickListener {
            listener?.deleteCartItem(product.id)
            analytics?.trackClickRemoveProduct()
        }
    }

    private fun setOnClickListener(product: RecipeProductUiModel) {
        binding?.root?.setOnClickListener {
            goToProductDetailPage(product)
            trackClickProduct(product)
        }
    }

    private fun addImpressionListener(product: RecipeProductUiModel) {
        itemView.addOnImpressionListener(product.impressHolder) {
            trackProductImpression(product)
            trackOutOfStockProduct(product)
        }
    }

    private fun trackProductImpression(product: RecipeProductUiModel) {
        analytics?.trackImpressionProduct(product)
    }

    private fun trackClickProduct(product: RecipeProductUiModel) {
        analytics?.trackClickProduct(product)
    }

    private fun trackClickSimilarProductBtn() {
        analytics?.trackClickSimilarProductBtn()
    }

    private fun trackImpressionSimilarProductBtn() {
        analytics?.trackImpressionSimilarProductBtn()
    }

    private fun trackOutOfStockProduct(product: RecipeProductUiModel) {
        if(product.stock == 0) {
            analytics?.trackImpressionOutOfStockProduct(product)
        }
    }

    private fun goToProductDetailPage(item: RecipeProductUiModel) {
        RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, item.id)
    }

    private fun openSimilarProductBottomSheet(product: RecipeProductUiModel) {
        val products = product.similarProducts
        val intent = TokoNowRecipeSimilarProductActivity.createNewIntent(context, products)
        context.startActivity(intent)
    }

    private fun onQuantityChanged(product: RecipeProductUiModel) {
        val input = binding?.quantityEditor?.getValue().orZero()
        listener?.onQuantityChanged(product.id, product.shopId, input)
    }

    private fun onEditorAction(product: RecipeProductUiModel) {
        binding?.quantityEditor?.apply {
            val input = getValue()
            addButton.isEnabled = input < product.maxOrder
            subtractButton.isEnabled = input > product.minOrder
            onQuantityChanged(product)
            closeSoftKeyboard()
        }
    }

    private fun QuantityEditorUnify.closeSoftKeyboard() {
        KeyboardHandler.DropKeyboard(itemView.context, this)
    }

    interface RecipeProductListener {
        fun deleteCartItem(productId: String)
        fun onQuantityChanged(productId: String, shopId: String, quantity: Int)
        fun addItemToCart(productId: String, shopId: String, quantity: Int)
    }
}
