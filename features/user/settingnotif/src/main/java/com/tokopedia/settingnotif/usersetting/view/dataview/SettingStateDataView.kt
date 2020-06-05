package com.tokopedia.settingnotif.usersetting.view.dataview

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.usersetting.data.pojo.ParentSetting
import com.tokopedia.settingnotif.usersetting.data.pojo.SettingSections
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.SettingFieldTypeFactory
import com.tokopedia.settingnotif.usersetting.view.fragment.base.SettingFieldFragment

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

    fun removeBuyerNotificationSetting(items: List<Visitable<SettingFieldTypeFactory>>): List<Visitable<SettingFieldTypeFactory>> {
        // find the buyer setting section, this section also contain some setting items belong to it, those items that will be removed from the main setting items
        val buyerSettingSection = items.filterIsInstance<SettingSections>()
                .find { it.title == SettingFieldFragment.BUYING_TRANSACTION_SECTION_TITLE }

        // if buyer setting section is exists, then there are some setting items need to be removed
        return if (buyerSettingSection != null) {
            // remove buyer setting section from setting items
            items.filter { it != buyerSettingSection }
                    // remove buyer setting items from setting items
                    .filter { setting -> buyerSettingSection.listSettings.find { setting == it } == null }
        } else {
            items
        }
    }
}
