package com.tokopedia.profilecompletion.settingprofile.profileinfo.view.uimodel

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.profilecompletion.settingprofile.profileinfo.view.adapter.ProfileInfoListTypeFactory

data class ProfileInfoItemUiModel(
    override var id: String = "",
    override var title: String = "",
    var itemValue: String = "",
    var rightIcon: Int = IconUnify.CHEVRON_RIGHT,
    var isEnable: Boolean = true,
    var placeholder: String = "",
    var showVerifiedTag: Boolean = false,
    var action: () -> Unit = {},
) : BaseProfileInfoUiModel {
    override fun type(typeFactory: ProfileInfoListTypeFactory): Int {
        return typeFactory.type(this)
    }
}
