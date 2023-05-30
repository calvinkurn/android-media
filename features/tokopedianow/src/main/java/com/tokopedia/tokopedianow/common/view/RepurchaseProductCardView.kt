package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseProductUiModel
import com.tokopedia.tokopedianow.databinding.LayoutRepurchaseProductCardBinding
import com.tokopedia.unifycomponents.BaseCustomView

class RepurchaseProductCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : BaseCustomView(context, attrs) {

    private val view by lazy {
        LayoutRepurchaseProductCardBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun bind(model: TokoNowRepurchaseProductUiModel) {
        view.apply {
            setupProductImage(model)
            setupLabelDiscount(model)
            setupProductPrice(model)
            setupProductWeight(model)
            setupQuantityEditor(model)
        }
    }

    fun setOnQuantityChangedListener(
        onQuantityChangedListener: (Int) -> Unit
    ) {
        view.quantityEditor.onQuantityChangedListener = onQuantityChangedListener
    }

    fun setOnClickAddVariantListener(
        onClickAddVariantListener: (Int) -> Unit
    ) {
        view.quantityEditor.onClickAddVariantListener = onClickAddVariantListener
    }

    private fun LayoutRepurchaseProductCardBinding.setupProductImage(
        model: TokoNowRepurchaseProductUiModel
    ) {
        imageProduct.loadImage(model.imageUrl)
    }

    private fun LayoutRepurchaseProductCardBinding.setupLabelDiscount(
        model: TokoNowRepurchaseProductUiModel
    ) {
        val discount = model.discount.isNotEmpty()
        labelDiscount.text = model.discount
        labelDiscount.showWithCondition(discount)
    }

    private fun LayoutRepurchaseProductCardBinding.setupProductPrice(
        model: TokoNowRepurchaseProductUiModel
    ) {
        textProductPrice.text = model.formattedPrice
    }

    private fun LayoutRepurchaseProductCardBinding.setupQuantityEditor(
        model: TokoNowRepurchaseProductUiModel
    ) {
        quantityEditor.showIfWithBlock(!model.isOos() && model.needToShowQuantityEditor) {
            quantityEditor.enableQuantityEditor = false
            quantityEditor.isVariant = model.isVariant
            quantityEditor.minQuantity = model.minOrder
            quantityEditor.maxQuantity = model.maxOrder
            quantityEditor.setQuantity(model.orderQuantity)
        }
    }

    private fun LayoutRepurchaseProductCardBinding.setupProductWeight(
        model: TokoNowRepurchaseProductUiModel?
    ) {
        val labelGroup = model?.getWeightLabelGroup()
        textProductWeight.showIfWithBlock(labelGroup != null) {
            labelGroup?.let { labelGroup ->
                text = labelGroup.title
            }
        }
    }
}
