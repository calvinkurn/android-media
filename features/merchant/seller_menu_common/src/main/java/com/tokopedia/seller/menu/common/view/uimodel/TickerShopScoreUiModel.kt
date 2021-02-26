package com.tokopedia.seller.menu.common.view.uimodel

import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiType

class TickerShopScoreUiModel(val tickerTitle: String = "", val descTitle: String = ""): SettingUiModel {

    override val onClickApplink: String?
        get() = null

    override val settingUiType: SettingUiType
        get() = SettingUiType.TICKER_SHOP_SCORE

    override fun type(typeFactory: OtherMenuTypeFactory): Int = typeFactory.type(this)

}