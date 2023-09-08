package com.tokopedia.topads.common.domain.model.createedit

import com.tokopedia.topads.common.view.adapter.createedit.CreateEditAdGroupTypeFactory

data class CreateAdGroupDailyBudgetItemUiModel(
    var title: String = "",
    var subtitle: String = "",
    var isItemClickable: Boolean = false,
    var dailyBudget: String = "0",
    var isDailyBudgetEnabled: Boolean = true,
    var hasDivider: Boolean = false,
    val onSwitchChange: (isSwitchOn: Boolean) -> Unit,
    val onDailyBudgetChange: (isSwitchOn: Double) -> Unit,
    val clickListener: () -> Unit,
) : CreateEditItemUiModel {

    override fun type(typeFactory: CreateEditAdGroupTypeFactory): Int {
        return typeFactory.type(this)
    }
}
