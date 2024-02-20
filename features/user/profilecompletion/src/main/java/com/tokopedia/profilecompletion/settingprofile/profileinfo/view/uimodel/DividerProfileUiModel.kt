package com.tokopedia.profilecompletion.settingprofile.profileinfo.view.uimodel

import com.tokopedia.profilecompletion.settingprofile.profileinfo.view.adapter.ProfileInfoListTypeFactory

data class DividerProfileUiModel(
    override var title: String = "",
    override var id: String = ""
) : BaseProfileInfoUiModel {
    override fun type(typeFactory: ProfileInfoListTypeFactory): Int {
        return typeFactory.type(this)
    }
}
