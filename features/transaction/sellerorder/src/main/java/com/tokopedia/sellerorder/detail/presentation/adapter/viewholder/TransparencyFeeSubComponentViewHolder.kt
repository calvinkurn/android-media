package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import androidx.core.view.updatePadding
import com.google.android.material.shape.CornerFamily
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemSubComponentIncomeDetailBinding
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.DetailTransparencyFeeAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.TransparencyFeeAttributesAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.BaseTransparencyFeeAttributes
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeSubComponentUiModel
import com.tokopedia.sellerorder.detail.presentation.widget.transparency_fee.BaseWidgetTransparencyFeeAttribute
import com.tokopedia.unifycomponents.toPx

class TransparencyFeeSubComponentViewHolder(
    view: View?,
    actionListener: DetailTransparencyFeeAdapterFactoryImpl.ActionListener
) : AbstractViewHolder<TransparencyFeeSubComponentUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_sub_component_income_detail

        private const val PADDING_VERTICAL_CONTENT = 4
    }

    private val binding = ItemSubComponentIncomeDetailBinding.bind(itemView)

    private val typeFactory = TransparencyFeeAttributesAdapterFactoryImpl(actionListener)

    override fun bind(element: TransparencyFeeSubComponentUiModel) {
        setupDividerVerticalRadius(element.isFirstIndex, element.isLastIndex)
        setupVerticalPadding(element)
        setupBottomDivider(element.isLastIndex)
        setAttributes(element.attributes)
    }

    private fun setupDividerVerticalRadius(isFirstIndex: Boolean, isLastIndex: Boolean) {
        val shapeDivider = binding.dividerVerticalSubComponentIncomeDetail
        shapeDivider.shapeAppearanceModel = shapeDivider.shapeAppearanceModel.toBuilder().apply {
            if (isFirstIndex) {
                setTopLeftCorner(
                    CornerFamily.ROUNDED,
                    TransparencyFeeComponentViewHolder.ROUNDED_RADIUS
                )
                setTopRightCorner(
                    CornerFamily.ROUNDED,
                    TransparencyFeeComponentViewHolder.ROUNDED_RADIUS
                )
            }
            if (isLastIndex) {
                setBottomLeftCorner(
                    CornerFamily.ROUNDED,
                    TransparencyFeeComponentViewHolder.ROUNDED_RADIUS
                )
                setBottomRightCorner(
                    CornerFamily.ROUNDED,
                    TransparencyFeeComponentViewHolder.ROUNDED_RADIUS
                )
            }
        }.build()
    }

    private fun setupVerticalPadding(element: TransparencyFeeSubComponentUiModel) {
        binding.cgSubComponentIncomeDetailAttributes.updatePadding(
            top = if (element.isFirstIndex) Int.ZERO else PADDING_VERTICAL_CONTENT.toPx(),
            bottom = if (element.isFirstIndex) Int.ZERO else PADDING_VERTICAL_CONTENT.toPx(),
        )
    }

    private fun setupBottomDivider(show: Boolean) {
        binding.dividerTransparencyFeeSubComponent.showWithCondition(show)
    }

    private fun setAttributes(attributes: List<BaseTransparencyFeeAttributes>) {
        setAttributesData(attributes)
    }

    private fun setAttributesData(attributes: List<BaseTransparencyFeeAttributes>) {
        binding.cgSubComponentIncomeDetailAttributes.removeAllViews()
        attributes.map { attribute ->
            val attributeView = createAttributeView(attribute)
            val attributeViewHolder = createAttributeViewHolder(attribute, attributeView)
            binding.cgSubComponentIncomeDetailAttributes.addView(attributeView)
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
