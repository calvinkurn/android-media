package com.tokopedia.profilecompletion.profileinfo.view.uimodel

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.profilecompletion.profileinfo.view.adapter.ProfileInfoListTypeFactory

data class ProfileInfoItemUiModel(
    override var id: String = "",
    override var title: String = "",
    var itemValue: String = "",
    var rightIcon: Int = IconUnify.CHEVRON_RIGHT,
): BaseProfileInfoUiModel {
    override fun type(typeFactory: ProfileInfoListTypeFactory): Int {
	return typeFactory.type(this)
    }
}