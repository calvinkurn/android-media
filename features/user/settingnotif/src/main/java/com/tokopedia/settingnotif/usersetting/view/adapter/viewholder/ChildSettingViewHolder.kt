package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.Switch
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.ChildSetting

class ChildSettingViewHolder(
        itemView: View?,
        settingListener: SettingListener
) : SettingViewHolder<ChildSetting>(itemView, settingListener) {

    override fun getSwitchView(itemView: View?): Switch? {
        return itemView?.findViewById(R.id.sw_setting)
    }

    override fun bind(element: ChildSetting?) {
        super.bind(element)

        settingSwitch?.text = element?.name
    }

    override fun updateSiblingAndChild(element: ChildSetting, checked: Boolean) {
        val currentSettingPosition = adapterPosition
        val parentSettingAndItsIndex = settingListener.getParentSetting(currentSettingPosition)
                ?: return

        val parentSetting = parentSettingAndItsIndex.first
        val parentSettingIndex = parentSettingAndItsIndex.second
        val childSettings = parentSetting.childSettings
        val currentChildSettingIndex = currentSettingPosition - parentSettingIndex - 1

        var allSiblingHasSameCheckedStatus = true
        childSettings.forEachIndexed { index, childSetting ->
            childSetting?.let {
                if (index != currentChildSettingIndex && !childSetting.hasSameCheckedStatusWith(checked)) {
                    allSiblingHasSameCheckedStatus = false
                }
            }
        }

        if (allSiblingHasSameCheckedStatus && !parentSetting.hasSameCheckedStatusWith(checked)) {
            parentSetting.status = checked
            settingListener.updateSettingView(arrayListOf(parentSettingIndex))
        }
    }

    override fun getUpdatedSettingIds(element: ChildSetting, checked: Boolean): Map<String, Boolean> {
        return HashMap<String, Boolean>().apply {
            put(element.key, checked)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_child_setting
    }

}