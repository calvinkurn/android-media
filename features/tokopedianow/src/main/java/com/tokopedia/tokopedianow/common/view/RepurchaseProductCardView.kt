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
    attrs: AttributeSet? = null,
) : CardView(context, attrs) {

    private val view by lazy {
        LayoutRepurchaseProductCardBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun bind(model: TokoNowRepurchaseProductUiModel) {
        view.apply {
            val discount = model.discount.isNotEmpty()

            imageProduct.loadImage(model.imageUrl)
            labelDiscount.text = model.discount
            labelDiscount.showWithCondition(discount)
            textProductPrice.text = model.formattedPrice

            initQuantityEditor(
                isVariant = model.isVariant,
                minOrder = model.minOrder,
                maxOrder = model.maxOrder,
                orderQuantity = model.orderQuantity,
                isOos = model.isOos(),
                needToShowQuantityEditor = model.needToShowQuantityEditor
            )

            initWeight(model)
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

    private fun LayoutRepurchaseProductCardBinding.initQuantityEditor(
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

    private fun LayoutRepurchaseProductCardBinding.initWeight(
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
