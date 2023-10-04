package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import androidx.core.view.updatePadding
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemHeaderIncomeDetailSectionBinding
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.DetailTransparencyFeeAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.TransparencyFeeAttributesAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.BaseTransparencyFeeAttributes
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeHeaderUiModel
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

    private val attributesAdapter = BaseAdapter(typeFactory)

    override fun bind(element: TransparencyFeeHeaderUiModel) {
        setupValue(element.value)
        setupPadding(element)
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

    private fun setAttributes(attributes: List<BaseTransparencyFeeAttributes>) {
        if (attributes.isNotEmpty()) {
            setupRecyclerView()
            setAttributesData(attributes)
        } else {
            hideAttributes()
        }
    }

    private fun setupRecyclerView() {
        binding.rvHeaderIncomeDetailAttributes.run {
            show()
            layoutManager = FlexboxLayoutManager(context).apply {
                alignItems = AlignItems.FLEX_START
            }
            adapter = attributesAdapter
        }
    }

    private fun hideAttributes() {
        binding.rvHeaderIncomeDetailAttributes.hide()
    }

    private fun setAttributesData(attributes: List<BaseTransparencyFeeAttributes>) {
        attributesAdapter.setElement(attributes)
    }
}
