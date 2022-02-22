package com.tokopedia.profilecompletion.profileinfo.view.uimodel

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.profilecompletion.profileinfo.view.adapter.ProfileInfoListTypeFactory

data class ProfileInfoTitleUiModel(
    override var id: String = "",
    override var title: String = "",
    var infoIcon: Int = IconUnify.INFORMATION
): BaseProfileInfoUiModel {
    override fun type(typeFactory: ProfileInfoListTypeFactory): Int {
	return typeFactory.type(this)
    }
}