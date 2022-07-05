package com.tokopedia.home.beranda.presentation.view.adapter.factory.balancewidget

import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderDataModel

/**
 * Created by dhaba
 */
interface BalanceWidgetTypeFactory {
    fun type(dataModel: HomeHeaderDataModel): Int
}