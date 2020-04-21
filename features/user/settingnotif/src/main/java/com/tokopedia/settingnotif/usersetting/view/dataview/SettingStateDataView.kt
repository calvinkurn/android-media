package com.tokopedia.settingnotif.usersetting.view.dataview

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.usersetting.data.pojo.ParentSetting

object SettingStateDataView {

    fun mapCloneSettings(list: MutableList<Visitable<*>>): List<ParentSetting> {
        return list.toMutableList() // clone container first
                // filter for parentSetting
                .filterIsInstance<ParentSetting>()
                // copy all of objects on parentSetting
                .map { it.deepCopy() }
                // convert map back into list
                .toList()
    }

}