package com.tokopedia.topads.common.domain.model.createedit

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.common.view.adapter.createedit.CreateEditAdGroupTypeFactory

data class CreateAdGroupDailyBudgetItemUiModel(
    var title: String = String.EMPTY,
    var subtitle: String = String.EMPTY,
    var isItemClickable: Boolean = false,
    var dailyBudget: String = Int.ZERO.toString(),
    var isDailyBudgetEnabled: Boolean = true,
    var hasDivider: Boolean = false,
    val onSwitchChange: (isSwitchOn: Boolean) -> Unit,
    val onDailyBudgetChange: (isEnable: Boolean) -> Unit,
    val clickListener: () -> Unit,
) : CreateEditItemUiModel {

    override fun type(typeFactory: CreateEditAdGroupTypeFactory): Int {
        return typeFactory.type(this)
    }
}
