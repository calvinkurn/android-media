package com.tokopedia.settingnotif.usersetting.data.pojo

import com.google.gson.annotations.SerializedName
import com.tokopedia.settingnotif.usersetting.util.dataClone
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.SettingFieldTypeFactory

open class ParentSetting(
        name: String = "",
        icon: String = "",
        key: String = "",
        status: Boolean = false,
        @SerializedName("description") var description: String = "",
        @SerializedName("list_settings") var childSettings: List<ChildSetting?> = emptyList()
) : BaseSetting(name, icon, key, status) {

    override fun type(typeFactory: SettingFieldTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun deepCopy() = dataClone<ParentSetting>(this)

    fun hasDescription(): Boolean = description.isNotEmpty()

    fun hasChild(): Boolean = childSettings.isNotEmpty()

    fun checkedStateIsFalse(): Boolean = !status

    fun needToUpdateCheckedStatus(allChildHasSameCheckedStatus: Boolean, childSettingStatus: Boolean): Boolean {
        return (allChildHasSameCheckedStatus && !hasSameCheckedStatusWith(childSettingStatus)) ||
                (childSettingStatus && checkedStateIsFalse())
    }

    fun isOtherChildHasSameCheckedStatus(currentChildIndex: Int, currentChildCheckedStatus: Boolean): Boolean {
        var same = true
        childSettings.forEachIndexed { otherChildIndex, childSetting ->
            childSetting?.let {
                if (otherChildIndex != currentChildIndex && !childSetting.hasSameCheckedStatusWith(currentChildCheckedStatus)) {
                    same = false
                }
            }
        }
        return same
    }

}