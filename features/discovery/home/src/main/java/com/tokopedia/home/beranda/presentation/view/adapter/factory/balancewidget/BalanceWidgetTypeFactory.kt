package com.tokopedia.home.beranda.presentation.view.adapter.factory.balancewidget

import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel

/**
 * Created by dhaba
 */
interface BalanceWidgetTypeFactory {
    fun type(dataModel: HomeBalanceModel): Int
}