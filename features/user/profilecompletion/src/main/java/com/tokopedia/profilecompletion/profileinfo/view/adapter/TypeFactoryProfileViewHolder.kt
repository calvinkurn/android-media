package com.tokopedia.profilecompletion.profileinfo.view.adapter

import com.tokopedia.profilecompletion.profileinfo.view.uimodel.DividerProfileUiModel
import com.tokopedia.profilecompletion.profileinfo.view.uimodel.ProfileInfoItemUiModel
import com.tokopedia.profilecompletion.profileinfo.view.uimodel.ProfileInfoTitleUiModel

interface TypeFactoryProfileViewHolder {
    fun type(profileInfoItemUiModel: ProfileInfoItemUiModel): Int
    fun type(profileInfoTitleUiModel: ProfileInfoTitleUiModel): Int
    fun type(dividerProfileUiModel: DividerProfileUiModel): Int
}