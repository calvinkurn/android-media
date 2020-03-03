package com.tokopedia.sellerhome.settings.view.uimodel

import com.tokopedia.sellerhome.settings.view.typefactory.OtherSettingTypeFactory
import com.tokopedia.sellerhome.settings.view.uimodel.base.LoadableUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiType
import com.tokopedia.sellerhome.settings.view.uimodel.state.BaseUiModelState

class ShopInfoUiModel(var shopName: String,
                      var shopAvatarUrl: String,
                      var shopBadgeUrl: String,
                      var followerCount: Int) : SettingUiModel, LoadableUiModel {

    constructor() : this("", "", "", 0)

    override fun type(typeFactory: OtherSettingTypeFactory): Int =
            typeFactory.type(this)

    override val onClickApplink: String?
        get() = null

    override val settingUiType: SettingUiType
        get() = SettingUiType.SHOP_INFO

    override var uiState: BaseUiModelState = BaseUiModelState.Loading
}