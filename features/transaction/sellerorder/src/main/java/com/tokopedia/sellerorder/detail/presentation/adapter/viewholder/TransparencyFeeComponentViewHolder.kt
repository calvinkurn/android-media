package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import androidx.core.view.updatePadding
import com.google.android.material.shape.CornerFamily
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemComponentIncomeDetailBinding
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.DetailTransparencyFeeAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.TransparencyFeeAttributesAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.BaseTransparencyFeeAttributes
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeComponentUiModel
import com.tokopedia.sellerorder.detail.presentation.widget.transparency_fee.BaseWidgetTransparencyFeeAttribute
import com.tokopedia.unifycomponents.toPx

class TransparencyFeeComponentViewHolder(
    view: View?,
    actionListener: DetailTransparencyFeeAdapterFactoryImpl.ActionListener
) : AbstractViewHolder<TransparencyFeeComponentUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_component_income_detail

        const val ROUNDED_RADIUS = 4f

        private const val PADDING_VERTICAL_CONTENT = 4
    }

    private val binding = ItemComponentIncomeDetailBinding.bind(itemView)

    private val typeFactory = TransparencyFeeAttributesAdapterFactoryImpl(actionListener)

    override fun bind(element: TransparencyFeeComponentUiModel) {
        setupDividerVerticalRadius(element.isFirstIndex, element.isLastIndex)
        setupValue(element.value)
        setupVerticalPadding(element)
        setupBottomDivider(element.isLastIndex)
        setAttributes(element.attributes)
    }

    private fun setupDividerVerticalRadius(isFirstIndex: Boolean, isLastIndex: Boolean) {
        val shapeDivider = binding.dividerVerticalIncomeDetail
        shapeDivider.shapeAppearanceModel = shapeDivider.shapeAppearanceModel.toBuilder().apply {
            if (isFirstIndex) {
                setTopLeftCorner(
                    CornerFamily.ROUNDED,
                    ROUNDED_RADIUS
                )
                setTopRightCorner(
                    CornerFamily.ROUNDED,
                    ROUNDED_RADIUS
                )
            }
            if (isLastIndex) {
                setBottomLeftCorner(
                    CornerFamily.ROUNDED,
                    ROUNDED_RADIUS
                )
                setBottomRightCorner(
                    CornerFamily.ROUNDED,
                    ROUNDED_RADIUS
                )
            }
        }.build()
    }

    private fun setupValue(value: String) {
        binding.tvComponentIncomeDetailValue.text = value
    }

    private fun setupVerticalPadding(element: TransparencyFeeComponentUiModel) {
        binding.cgComponentIncomeDetailAttributes.updatePadding(
            top = if (element.isFirstIndex) Int.ZERO else PADDING_VERTICAL_CONTENT.toPx(),
            bottom = if (element.isLastIndex) Int.ZERO else PADDING_VERTICAL_CONTENT.toPx()
        )
        binding.tvComponentIncomeDetailValue.updatePadding(
            top = if (element.isFirstIndex) Int.ZERO else PADDING_VERTICAL_CONTENT.toPx(),
            bottom = if (element.isLastIndex) Int.ZERO else PADDING_VERTICAL_CONTENT.toPx()
        )
    }

    private fun setupBottomDivider(show: Boolean) {
        binding.dividerTransparencyFeeComponent.showWithCondition(show)
    }

    private fun setAttributes(attributes: List<BaseTransparencyFeeAttributes>) {
        setAttributesData(attributes)
    }

    private fun setAttributesData(attributes: List<BaseTransparencyFeeAttributes>) {
        binding.cgComponentIncomeDetailAttributes.removeAllViews()
        attributes.map { attribute ->
            val attributeView = createAttributeView(attribute)
            val attributeViewHolder = createAttributeViewHolder(attribute, attributeView)
            binding.cgComponentIncomeDetailAttributes.addView(attributeView)
            attributeViewHolder.bind(attribute)
        }
    }

    private fun createAttributeView(attribute: BaseTransparencyFeeAttributes): View {
        return LayoutInflater
            .from(itemView.context)
            .inflate(attribute.type(typeFactory), null, false)
    }

    @Suppress("UNCHECKED_CAST")
    private fun createAttributeViewHolder(
        attribute: BaseTransparencyFeeAttributes,
        attributeView: View
    ): BaseWidgetTransparencyFeeAttribute<BaseTransparencyFeeAttributes> {
        return typeFactory.createWidget(attributeView, attribute.type(typeFactory)) as BaseWidgetTransparencyFeeAttribute<BaseTransparencyFeeAttributes>
    }
}
