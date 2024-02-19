package com.tokopedia.digital.home.presentation.listener

import com.tokopedia.digital.home.model.RechargeHomepageSections

interface RechargeHomepageTodoWidgetListener {
    fun onClickTodoWidget(widget: RechargeHomepageSections.Widgets, isButton: Boolean,
                          todoWidgetSectionId: String, todoWidgetSectionName: String,
                          todoWidgetSectionTemplate: String)
    fun onClickThreeButton(widget: RechargeHomepageSections.Widgets)
    fun onCloseItem(widget: RechargeHomepageSections.Widgets)
    fun onImpressThreeButton(widget: RechargeHomepageSections.Widgets)
}
