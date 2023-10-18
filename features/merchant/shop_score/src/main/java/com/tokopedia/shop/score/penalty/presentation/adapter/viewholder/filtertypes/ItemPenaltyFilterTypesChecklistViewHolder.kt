package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.filtertypes

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemPenaltyFilterTypeChecklistBinding
import com.tokopedia.shop.score.penalty.presentation.model.filtertypes.ItemPenaltyFilterTypesChecklistUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemPenaltyFilterTypesChecklistViewHolder(view: View, private val listener: Listener) :
    AbstractViewHolder<ItemPenaltyFilterTypesChecklistUiModel>(view) {

    private val binding: ItemPenaltyFilterTypeChecklistBinding? by viewBinding()

    override fun bind(element: ItemPenaltyFilterTypesChecklistUiModel) {
        binding?.tvPenaltyFilterTypeChecklist?.text = element.title
        binding?.cbPenaltyFilterTypeChecklist?.run {
            setOnCheckedChangeListener(null)
            isChecked = element.isSelected
            skipAnimation()
        }
        setupRootClickListener()
        setupCheckboxClickListener(element)
    }

    private fun setupRootClickListener() {
        binding?.root?.setOnClickListener {
            val isChecked = binding?.cbPenaltyFilterTypeChecklist?.isChecked == true
            binding?.cbPenaltyFilterTypeChecklist?.isChecked = !isChecked
            binding?.cbPenaltyFilterTypeChecklist?.callOnClick()
        }
    }

    private fun setupCheckboxClickListener(element: ItemPenaltyFilterTypesChecklistUiModel) {
        binding?.cbPenaltyFilterTypeChecklist?.setOnClickListener {
            val isChecked = binding?.cbPenaltyFilterTypeChecklist?.isChecked == true
            element.isSelected = isChecked
            listener.onChecklistClicked(element.filterId, isChecked)
        }
    }

    interface Listener {
        fun onChecklistClicked(filterId: Int, isSelected: Boolean)
    }

    companion object {
        val LAYOUT = R.layout.item_penalty_filter_type_checklist
    }

}
