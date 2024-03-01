package com.tokopedia.home_account.account_settings.presentation.viewmodel.base

import com.tokopedia.home_account.account_settings.presentation.viewmodel.SettingItemUIModel

class SwitchSettingItemUIModel : SettingItemUIModel {
    var isUseOnClick = false
    private var labelType = ""
    fun labelType(): String {
        return labelType
    }

    fun setLabelType(labelType: String) {
        this.labelType = labelType
    }

    constructor(id: Int, title: String?) : super(id, title!!)
    constructor(id: Int, title: String?, subtitle: String?, isUseOnClick: Boolean) : super(
        id,
        title!!,
        subtitle
    ) {
        this.isUseOnClick = isUseOnClick
    }

    constructor(
        id: Int, title: String?, subtitle: String?, isUseOnClick: Boolean, labelType: String
    ) : super(id, title!!, subtitle) {
        this.isUseOnClick = isUseOnClick
        this.labelType = labelType
    }
}