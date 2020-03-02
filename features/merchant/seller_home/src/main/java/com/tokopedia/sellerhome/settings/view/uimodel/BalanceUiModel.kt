package com.tokopedia.sellerhome.settings.view.uimodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.sellerhome.settings.view.typefactory.OtherSettingTypeFactory
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiType

class BalanceUiModel(val balanceTitle: String?): SettingUiModel {

    private val _balanceLiveData = MutableLiveData<String>()
    val balanceLiveData : LiveData<String>
        get() = _balanceLiveData

    override fun type(typeFactory: OtherSettingTypeFactory): Int {
        return typeFactory.type(this)
    }

    override val onClickApplink: String?
        get() = null

    override val settingUiType: SettingUiType
        get() = SettingUiType.BALANCE

}