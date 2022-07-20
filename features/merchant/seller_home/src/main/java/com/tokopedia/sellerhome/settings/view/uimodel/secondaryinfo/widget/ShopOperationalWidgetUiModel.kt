package com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget

import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.sellerhome.settings.view.adapter.ShopSecondaryInfoAdapterFactory
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.ShopOperationalData

class ShopOperationalWidgetUiModel(override var state: SettingResponseState<ShopOperationalData>) :
    ShopSecondaryInfoWidget<ShopOperationalData> {

    override fun type(typeFactory: ShopSecondaryInfoAdapterFactory): Int =
        typeFactory.type(this)

}