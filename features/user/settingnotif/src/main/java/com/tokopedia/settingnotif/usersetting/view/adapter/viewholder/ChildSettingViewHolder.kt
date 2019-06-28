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
        val childAdapterPosition = adapterPosition
        val pairParentAndIndex = settingListener.getParentSetting(childAdapterPosition)
                ?: return

        val parentSetting = pairParentAndIndex.first
        val parentSettingIndex = pairParentAndIndex.second
        val childArrayIndex = childAdapterPosition - parentSettingIndex - 1

        var allSiblingHasSameCheckedStatus = true
        parentSetting.childSettings.forEachIndexed { index, childSetting ->
            if (index != childArrayIndex && childSetting?.status != checked) {
                allSiblingHasSameCheckedStatus = false
            }
        }

        if (allSiblingHasSameCheckedStatus && parentSetting.status != checked) {
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