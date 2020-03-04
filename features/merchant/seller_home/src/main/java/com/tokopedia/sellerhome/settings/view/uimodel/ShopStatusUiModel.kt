package com.tokopedia.sellerhome.settings.view.uimodel

import com.tokopedia.sellerhome.settings.view.typefactory.OtherSettingTypeFactory
import com.tokopedia.sellerhome.settings.view.uimodel.base.LoadableUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiType
import com.tokopedia.sellerhome.settings.view.uimodel.state.BaseUiModelState

class ShopStatusUiModel() : SettingUiModel, LoadableUiModel {

    override fun type(typeFactory: OtherSettingTypeFactory): Int =
            typeFactory.type(this)

    override var uiState: BaseUiModelState = BaseUiModelState.Loading

    override val onClickApplink: String?
        get() = null

    override val settingUiType: SettingUiType
        get() = SettingUiType.SHOP_STATUS
}