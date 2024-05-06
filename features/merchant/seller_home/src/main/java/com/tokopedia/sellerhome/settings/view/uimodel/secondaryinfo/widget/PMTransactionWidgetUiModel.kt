package com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget

import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.sellerhome.settings.view.adapter.ShopSecondaryInfoAdapterFactory
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.PMTransactionDataUiModel

class PMTransactionWidgetUiModel(
    override var state: SettingResponseState<PMTransactionDataUiModel>
) : ShopSecondaryInfoWidget<PMTransactionDataUiModel> {

    override fun type(typeFactory: ShopSecondaryInfoAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
