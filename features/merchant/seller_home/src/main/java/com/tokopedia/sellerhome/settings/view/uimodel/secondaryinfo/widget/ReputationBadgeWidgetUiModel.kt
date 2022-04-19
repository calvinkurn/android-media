package com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget

import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.sellerhome.settings.view.adapter.ShopSecondaryInfoAdapterFactory

class ReputationBadgeWidgetUiModel(override var state: SettingResponseState<String>) :
    ShopSecondaryInfoWidget<String> {

    override fun type(typeFactory: ShopSecondaryInfoAdapterFactory): Int =
        typeFactory.type(this)

}