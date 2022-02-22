package com.tokopedia.profilecompletion.profileinfo.view.uimodel

import com.tokopedia.profilecompletion.profileinfo.view.adapter.ProfileInfoListTypeFactory

data class DividerProfileUiModel(
    override var title: String = "",
    override var id: String = ""
): BaseProfileInfoUiModel {
    override fun type(typeFactory: ProfileInfoListTypeFactory): Int {
	return typeFactory.type(this)
    }
}