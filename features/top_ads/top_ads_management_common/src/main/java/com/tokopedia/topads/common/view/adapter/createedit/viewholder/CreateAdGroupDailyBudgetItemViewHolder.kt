package com.tokopedia.topads.common.view.adapter.createedit.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.MAXIMUM_DAILY_BUDGET
import com.tokopedia.topads.common.databinding.TopadsCreateDailyBudgetItemAdGroupBinding
import com.tokopedia.topads.common.domain.model.createedit.CreateAdGroupDailyBudgetItemUiModel
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.NumberTextWatcher

class CreateAdGroupDailyBudgetItemViewHolder(private val viewBinding: TopadsCreateDailyBudgetItemAdGroupBinding) : AbstractViewHolder<CreateAdGroupDailyBudgetItemUiModel>(viewBinding.root) {

    var minBudget = Int.ZERO.toString()
    val maxBudget = MAXIMUM_DAILY_BUDGET
    override fun bind(element: CreateAdGroupDailyBudgetItemUiModel) {
        viewBinding.adItemTitle.text = element.title
        viewBinding.editAdItemSubtitle.text = element.subtitle
        setInitialValues(element.dailyBudget)
        val dailyBudget = CurrencyFormatHelper.convertToRupiah(element.dailyBudget)
        if (element.isDailyBudgetEnabled) {
            viewBinding.dailyBudgetSwitch.isChecked = true
            viewBinding.recommendationBudget.show()
            viewBinding.recommendationBudget.editText.setText(String.format("Rp %s", dailyBudget))
        } else {
            viewBinding.dailyBudgetSwitch.isChecked = false
            viewBinding.recommendationBudget.editText.setText(String.format("Rp %s", dailyBudget))
            viewBinding.recommendationBudget.hide()
        }
        setClicksOnViews(element)
        setInteractionOnViews(element)
    }

    private fun setInitialValues(dailyBudget: String) {
        minBudget = dailyBudget
    }

    private fun setInteractionOnViews(element: CreateAdGroupDailyBudgetItemUiModel) {
        viewBinding.recommendationBudget.editText.addTextChangedListener(object : NumberTextWatcher(viewBinding.recommendationBudget.editText) {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                if (number < minBudget.toDoubleOrZero()) {
                    viewBinding.recommendationBudget.isInputError = true
                    viewBinding.recommendationBudget.setMessage(String.format(getString(R.string.topads_ads_minimum_budget), CurrencyFormatHelper.convertToRupiah(minBudget)))
                    element.onDailyBudgetChange(false)
                } else if (number > MAXIMUM_DAILY_BUDGET.toDoubleOrZero()) {
                    viewBinding.recommendationBudget.isInputError = true
                    viewBinding.recommendationBudget.setMessage(String.format(getString(R.string.topads_ads_maximum_budget), CurrencyFormatHelper.convertToRupiah(maxBudget)))
                    element.onDailyBudgetChange(false)
                } else {
                    element.dailyBudget = number.toString()
                    viewBinding.recommendationBudget.isInputError = false
                    viewBinding.recommendationBudget.setMessage(String.EMPTY)
                    element.onDailyBudgetChange(true)
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
