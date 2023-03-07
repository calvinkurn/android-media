package com.tokopedia.sellerorder.list.presentation.adapter.viewholders

import android.annotation.SuppressLint
import android.view.View
import android.widget.CompoundButton
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemSomListMultiSelectSectionBinding
import com.tokopedia.sellerorder.list.presentation.models.SomListMultiSelectSectionUiModel
import com.tokopedia.utils.view.binding.viewBinding

@SuppressLint("ClickableViewAccessibility")
class SomListOrderMultiSelectSectionViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<SomListMultiSelectSectionUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_som_list_multi_select_section
    }

    private val binding by viewBinding<ItemSomListMultiSelectSectionBinding>()
    private val checkboxListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        if (isChecked) {
            listener.onCheckAllOrderClicked()
        } else {
            listener.onUncheckAllOrderClicked()
        }
    }

    init {
        binding?.tvSomListBulk?.setOnClickListener { listener.onToggleMultiSelectClicked() }
        binding?.checkBoxBulkAction?.setOnCheckedChangeListener(checkboxListener)
    }

    override fun bind(element: SomListMultiSelectSectionUiModel) {
        setupCounter(element)
        setupCheckbox(element, true)
        setupToggle(element)
    }

    override fun bind(element: SomListMultiSelectSectionUiModel, payloads: MutableList<Any>) {
        setupCounter(element)
        setupCheckbox(element, false)
        setupToggle(element)
    }

    private fun setupCounter(element: SomListMultiSelectSectionUiModel) {
        binding?.run {
            tvSomListOrderCounter.text = if (element.isEnabled) {
                root.context?.resources?.getString(
                    R.string.som_list_order_counter_multi_select_enabled,
                    element.totalSelected
                ).orEmpty()
            } else {
                root.context?.resources?.getString(
                    R.string.som_list_order_counter,
                    element.totalOrder
                ).orEmpty()
            }
        }
    }

    private fun setupCheckbox(element: SomListMultiSelectSectionUiModel, skipAnimation: Boolean) {
        binding?.checkBoxBulkAction?.run {
            setOnCheckedChangeListener(null)
            val newCheckedStatus = element.totalSelected.isMoreThanZero()
            val newIndeterminateStatus = element.totalSelected.isMoreThanZero() && element.totalSelected < element.totalSelectable
            val checkStatusChanged = isChecked != newCheckedStatus
            val indeterminateStatusChanged = getIndeterminate() != newIndeterminateStatus
            if (checkStatusChanged) isChecked = newCheckedStatus
            if (indeterminateStatusChanged) setIndeterminate(newIndeterminateStatus)
            if (skipAnimation && checkStatusChanged && indeterminateStatusChanged) skipAnimation()
            showWithCondition(element.isEnabled)
            setOnCheckedChangeListener(checkboxListener)
        }
    }

    private fun setupToggle(element: SomListMultiSelectSectionUiModel) {
        binding?.tvSomListBulk?.run {
            val textResId = if (element.isEnabled) R.string.som_list_multi_select_cancel else R.string.som_list_multi_select
            text = getString(textResId).orEmpty()
        }
    }

    interface Listener {
        fun onToggleMultiSelectClicked()
        fun onCheckAllOrderClicked()
        fun onUncheckAllOrderClicked()
    }
}