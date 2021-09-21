package com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.sellerhome.settings.view.adapter.ShopSecondaryInfoAdapterFactory

interface ShopSecondaryInfoWidget<T : Any> : Visitable<ShopSecondaryInfoAdapterFactory> {
    var state: SettingResponseState<T>
}