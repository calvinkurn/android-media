package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.DetailIncomeItemBinding
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.SomDetailIncomeUiModel
import com.tokopedia.utils.view.binding.viewBinding

class SomDetailIncomeViewHolder(
    private val actionListener: SomDetailAdapterFactoryImpl.ActionListener?, itemView: View?
) : AbstractViewHolder<SomDetailData>(itemView) {

    companion object {
        val LAYOUT = R.layout.detail_income_item
    }

    private val binding by viewBinding<DetailIncomeItemBinding>()

    override fun bind(element: SomDetailData) {
        val data = element.dataObject
        if (data is SomDetailIncomeUiModel) {
            setIncomeLabel(data.label)
            setIconChevronRightClickListener()
        }
    }

    private fun setIncomeLabel(label: String) {
        binding?.tvDetailIncomeLabel?.text = label
    }

    private fun setIconChevronRightClickListener() {
        binding?.detailIncomeIcon?.setOnClickListener {
            actionListener?.onDetailIncomeClicked()
        }
    }

}
