package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseProductUiModel
import com.tokopedia.tokopedianow.databinding.LayoutRepurchaseProductCardBinding

class RepurchaseProductCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CardView(context, attrs) {

    private val view by lazy {
        LayoutRepurchaseProductCardBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun bind(model: TokoNowRepurchaseProductUiModel) {
        view.apply {
            setupProductImage(model)
            setupLabelDiscount(model)
            setupProductPrice(model)
            setupProductWeight(model)

            setupQuantityEditor(
                isVariant = model.isVariant,
                minOrder = model.minOrder,
                maxOrder = model.maxOrder,
                orderQuantity = model.orderQuantity,
                isOos = model.isOos(),
                needToShowQuantityEditor = model.needToShowQuantityEditor
            )
        }
    }

    fun setOnClickQuantityEditorListener(
        onClickListener: (Int) -> Unit
    ) {
        view.quantityEditor.onClickListener = onClickListener
    }

    fun setOnClickQuantityEditorVariantListener(
        onClickVariantListener: (Int) -> Unit
    ) {
        view.quantityEditor.onClickVariantListener = onClickVariantListener
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
        isVariant: Boolean,
        minOrder: Int,
        maxOrder: Int,
        orderQuantity: Int,
        isOos: Boolean,
        needToShowQuantityEditor: Boolean
    ) {
        quantityEditor.showIfWithBlock(!isOos && needToShowQuantityEditor) {
            quantityEditor.isVariant = isVariant
            quantityEditor.minQuantity = minOrder
            quantityEditor.maxQuantity = maxOrder
            quantityEditor.setQuantity(
                quantity = orderQuantity
            )
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
