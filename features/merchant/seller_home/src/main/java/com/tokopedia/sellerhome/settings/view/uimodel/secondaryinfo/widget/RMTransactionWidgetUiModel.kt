package com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget

import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.sellerhome.settings.view.adapter.ShopSecondaryInfoAdapterFactory
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.RmTransactionData

class RMTransactionWidgetUiModel(
    override var state: SettingResponseState<RmTransactionData>) :
    ShopSecondaryInfoWidget<RmTransactionData> {

    override fun type(typeFactory: ShopSecondaryInfoAdapterFactory): Int =
        typeFactory.type(this)

}