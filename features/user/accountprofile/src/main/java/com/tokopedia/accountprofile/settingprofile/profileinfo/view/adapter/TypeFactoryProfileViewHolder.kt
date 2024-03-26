package com.tokopedia.accountprofile.settingprofile.profileinfo.view.adapter

import com.tokopedia.accountprofile.settingprofile.profileinfo.view.uimodel.DividerProfileUiModel
import com.tokopedia.accountprofile.settingprofile.profileinfo.view.uimodel.ProfileInfoItemUiModel
import com.tokopedia.accountprofile.settingprofile.profileinfo.view.uimodel.ProfileInfoTitleUiModel

interface TypeFactoryProfileViewHolder {
    fun type(profileInfoItemUiModel: ProfileInfoItemUiModel): Int
    fun type(profileInfoTitleUiModel: ProfileInfoTitleUiModel): Int
    fun type(dividerProfileUiModel: DividerProfileUiModel): Int
}
