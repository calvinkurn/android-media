package com.tokopedia.digital.home.presentation.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.digital.home.presentation.adapter.RechargeHomepageTodoWidgetAdapterTypeFactory

interface RechargeHomepageTodoWidgetCloseProcessListener {
    fun onCloseWidget(element: Visitable<RechargeHomepageTodoWidgetAdapterTypeFactory>)
}
