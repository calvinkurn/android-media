package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance

import com.tokopedia.home.beranda.presentation.view.adapter.factory.balancewidget.BalanceWidgetTypeFactory
import com.tokopedia.home.beranda.presentation.view.adapter.factory.balancewidget.BalanceWidgetVisitable

/**
 * Created by dhaba
 */
class BalanceWidgetFailedModel : BalanceWidgetVisitable {
    override fun type(typeFactory: BalanceWidgetTypeFactory): Int {
        return typeFactory.type(this)
    }
}