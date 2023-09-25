package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemComponentIncomeDetailBinding
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.DetailTransparencyFeeAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.TransparencyFeeAttributesAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.BaseTransparencyFeeAttributes
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeComponentUiModel

class TransparencyFeeComponentViewHolder(
    view: View?,
    actionListener: DetailTransparencyFeeAdapterFactoryImpl.ActionListener
) : AbstractViewHolder<TransparencyFeeComponentUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_component_income_detail
    }

    private val binding = ItemComponentIncomeDetailBinding.bind(itemView)

    private val typeFactory = TransparencyFeeAttributesAdapterFactoryImpl(actionListener)

    private val attributesAdapter = BaseAdapter(typeFactory)

    override fun bind(element: TransparencyFeeComponentUiModel) {
        setupLabel(element.label)
        setupValue(element.value)
        setAttributes(element.attributes)
    }

    private fun setupLabel(label: String) {
        binding.tvComponentIncomeDetailLabel.text = label
    }

    private fun setupValue(value: String) {
        binding.tvComponentIncomeDetailValue.text = value
    }

    private fun setAttributes(attributes: List<BaseTransparencyFeeAttributes>) {
        if (attributes.isNotEmpty()) {
            setupRecyclerView()
            setAttributesData(attributes)
        } else {
            hideAttributes()
        }
    }

    private fun setupRecyclerView() {
        binding.rvComponentIncomeDetailAttributes.run {
            show()
            layoutManager = FlexboxLayoutManager(context).apply {
                alignItems = AlignItems.FLEX_START
            }
            adapter = attributesAdapter
        }
    }

    private fun hideAttributes() {
        binding.rvComponentIncomeDetailAttributes.hide()
    }

    private fun setAttributesData(attributes: List<BaseTransparencyFeeAttributes>) {
        attributesAdapter.setElement(attributes)
    }
}
