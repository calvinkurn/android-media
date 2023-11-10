package com.tokopedia.digital.home.presentation.listener

import com.tokopedia.digital.home.model.RechargeHomepageSections

interface RechargeHomepageTodoWidgetListener {
    fun onClickTodoWidget(widget: RechargeHomepageSections.Widgets, isButton: Boolean)
    fun onClickThreeButton(optionButtons: List<RechargeHomepageSections.OptionButton>)
}
