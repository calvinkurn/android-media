package com.tokopedia.home.beranda.presentation.view.adapter.factory.balancewidget

import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceShimmerModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceWidgetFailedModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel

/**
 * Created by dhaba
 */
interface BalanceWidgetTypeFactory {
    fun type(dataModel: HomeBalanceModel): Int
    fun type(dataModel: BalanceShimmerModel): Int
    fun type(dataModel: BalanceWidgetFailedModel): Int
}