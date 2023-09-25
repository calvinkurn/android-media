package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemHeaderIncomeDetailSectionBinding
import com.tokopedia.sellerorder.databinding.ItemSummaryIncomeDetailSectionBinding
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.DetailTransparencyFeeAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.TransparencyFeeAttributesAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.BaseTransparencyFeeAttributes
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeHeaderUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeSummaryUiModel

class TransparencyFeeSummaryViewHolder(
    view: View?,
    actionListener: DetailTransparencyFeeAdapterFactoryImpl.ActionListener
) : AbstractViewHolder<TransparencyFeeSummaryUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_header_income_detail_section
    }

    private val binding = ItemSummaryIncomeDetailSectionBinding.bind(itemView)

    private val typeFactory = TransparencyFeeAttributesAdapterFactoryImpl(actionListener)

    private val attributesAdapter = BaseAdapter(typeFactory)

    override fun bind(element: TransparencyFeeSummaryUiModel) {
        setupLabel(element.label)
        setupValue(element.value)
        setAttributes(element.attributes)
        setupNote(element.note)
    }

    private fun setupLabel(label: String) {
        binding.tvSummaryIncomeDetailLabel.text = label
    }

    private fun setupValue(value: String) {
        binding.tvSummaryIncomeDetailValue.text = value
    }

    private fun setupNote(note: String) {
        binding.tvSummaryIncomeDetailNote.run {
            if (note.isNotBlank()) {
                show()
                text = note
            } else {
                hide()
            }
        }
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
        binding.rvSummaryIncomeDetailAttributes.run {
            show()
            layoutManager = FlexboxLayoutManager(context).apply {
                alignItems = AlignItems.FLEX_START
            }
            adapter = attributesAdapter
        }
    }

    private fun hideAttributes() {
        binding.rvSummaryIncomeDetailAttributes.hide()
    }

    private fun setAttributesData(attributes: List<BaseTransparencyFeeAttributes>) {
        attributesAdapter.setElement(attributes)
    }
}
