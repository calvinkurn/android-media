package com.tokopedia.accountprofile.settingprofile.profileinfo.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.accountprofile.settingprofile.profileinfo.view.uimodel.BaseProfileInfoUiModel

class ProfileInfoAdapter(
    profileInfoListTypeFactory: ProfileInfoListTypeFactory
) : BaseListAdapter<Visitable<*>, ProfileInfoListTypeFactory>(profileInfoListTypeFactory) {

    fun setProfileInfoItem(profileInfoItemUiList: List<BaseProfileInfoUiModel>) {
        visitables?.clear()
        visitables.addAll(profileInfoItemUiList)
        notifyDataSetChanged()
    }
}
