package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import androidx.core.view.updatePadding
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemHeaderIncomeDetailSectionBinding
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.DetailTransparencyFeeAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.TransparencyFeeAttributesAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.BaseTransparencyFeeAttributes
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeHeaderUiModel
import com.tokopedia.sellerorder.detail.presentation.widget.transparency_fee.BaseWidgetTransparencyFeeAttribute
import com.tokopedia.unifycomponents.toPx

class TransparencyFeeHeaderViewHolder(
    view: View?,
    actionListener: DetailTransparencyFeeAdapterFactoryImpl.ActionListener
) : AbstractViewHolder<TransparencyFeeHeaderUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_header_income_detail_section

        private const val PADDING_VERTICAL = 12
    }

    private val binding = ItemHeaderIncomeDetailSectionBinding.bind(itemView)

    private val typeFactory = TransparencyFeeAttributesAdapterFactoryImpl(actionListener)

    override fun bind(element: TransparencyFeeHeaderUiModel) {
        setupValue(element.value)
        setupPadding(element)
        setupDivider(element.hasComponents)
        setAttributes(element.attributes)
    }

    private fun setupValue(value: String) {
        binding.tvHeaderIncomeDetailValue.text = value
    }

    private fun setupPadding(element: TransparencyFeeHeaderUiModel) {
        binding.root.updatePadding(
            top = if (element.isFirstHeader) Int.ZERO else PADDING_VERTICAL.toPx()
        )
    }

    private fun setupDivider(hasComponents: Boolean) {
        binding.dividerTransparencyFeeHeader.showWithCondition(!hasComponents)
    }

    private fun setAttributes(attributes: List<BaseTransparencyFeeAttributes>) {
        setAttributesData(attributes)
    }

    private fun setAttributesData(attributes: List<BaseTransparencyFeeAttributes>) {
        binding.cgHeaderIncomeDetailAttributes.removeAllViews()
        attributes.map { attribute ->
            val attributeView = createAttributeView(attribute)
            val attributeViewHolder = createAttributeViewHolder(attribute, attributeView)
            binding.cgHeaderIncomeDetailAttributes.addView(attributeView)
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
