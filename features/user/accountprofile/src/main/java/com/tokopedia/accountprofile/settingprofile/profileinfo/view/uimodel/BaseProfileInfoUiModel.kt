package com.tokopedia.accountprofile.settingprofile.profileinfo.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.accountprofile.settingprofile.profileinfo.view.adapter.ProfileInfoListTypeFactory

interface BaseProfileInfoUiModel : Visitable<ProfileInfoListTypeFactory> {
    var id: String
    var title: String
}
