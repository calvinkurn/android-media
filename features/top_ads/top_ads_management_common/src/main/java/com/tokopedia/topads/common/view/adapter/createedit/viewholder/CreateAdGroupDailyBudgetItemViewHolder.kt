package com.tokopedia.topads.common.view.adapter.createedit.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.databinding.TopadsCreateDailyBudgetItemAdGroupBinding
import com.tokopedia.topads.common.domain.model.createedit.CreateAdGroupDailyBudgetItemUiModel
import com.tokopedia.utils.text.currency.NumberTextWatcher

class CreateAdGroupDailyBudgetItemViewHolder(private val viewBinding: TopadsCreateDailyBudgetItemAdGroupBinding) : AbstractViewHolder<CreateAdGroupDailyBudgetItemUiModel>(viewBinding.root) {

    override fun bind(element: CreateAdGroupDailyBudgetItemUiModel) {
        viewBinding.adItemTitle.text = element.title
        viewBinding.editAdItemSubtitle.text = element.subtitle
        if (element.isDailyBudgetEnabled) {
            viewBinding.dailyBudgetSwitch.isChecked = true
            viewBinding.recommendationBudget.show()
            viewBinding.recommendationBudget.editText.setText(String.format("Rp %s", element.dailyBudget))
        } else {
            viewBinding.dailyBudgetSwitch.isChecked = false
            viewBinding.recommendationBudget.editText.setText(String.format("Rp %s", element.dailyBudget))
            viewBinding.recommendationBudget.hide()
        }
        setClicksOnViews(element)
        setInteractionOnViews(element)
//        element.clickListener.invoke()

    }

    private fun setInteractionOnViews(element: CreateAdGroupDailyBudgetItemUiModel) {
        viewBinding.recommendationBudget.editText.addTextChangedListener(object : NumberTextWatcher(viewBinding.recommendationBudget.editText) {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                element.onDailyBudgetChange(number)
                if (number < element.dailyBudget.toDoubleOrZero()) {
                    viewBinding.recommendationBudget.isInputError = true
                    viewBinding.recommendationBudget.setMessage(String.format("Minimum %s", "Rp32.000"))
                } else {
                    element.dailyBudget = number.toString()
                    viewBinding.recommendationBudget.isInputError = false
                    viewBinding.recommendationBudget.setMessage(String.EMPTY)
                }
            }
        })
    }

    private fun setClicksOnViews(element: CreateAdGroupDailyBudgetItemUiModel) {
        viewBinding.dailyBudgetSwitch.setOnCheckedChangeListener { _, isChecked ->
            element.onSwitchChange(isChecked)
            if (isChecked) {
                viewBinding.recommendationBudget.show()
            } else {
                viewBinding.recommendationBudget.hide()
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.topads_create_item_ad_group
    }

}
