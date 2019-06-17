package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.Switch
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.ChildSettingPojo

class ChildSettingViewHolder(
        itemView: View?,
        settingListener: SettingListener
) : SettingViewHolder<ChildSettingPojo>(itemView, settingListener) {

    override fun getSwitchView(itemView: View?): Switch? {
        return itemView?.findViewById(R.id.sw_setting)
    }

    override fun bind(element: ChildSettingPojo?) {
        super.bind(element)

        settingSwitch?.text = element?.name
    }

    override fun updateSiblingAndChild(element: ChildSettingPojo, checked: Boolean) {
        val childAdapterPosition = adapterPosition
        val pairParentAndIndex = settingListener.getParentSettingPojo(childAdapterPosition)
                ?: return

        val parentSettingPojo = pairParentAndIndex.first
        val parentSettingPojoIndex = pairParentAndIndex.second
        val childArrayIndex = childAdapterPosition - parentSettingPojoIndex - 1

        var allSiblingHasSameCheckedStatus = true
        parentSettingPojo.childSettings.forEachIndexed { index, childSetting ->
            if (index != childArrayIndex && childSetting.status != checked) {
                allSiblingHasSameCheckedStatus = false
            }
        }

        if (allSiblingHasSameCheckedStatus && parentSettingPojo.status != checked) {
            parentSettingPojo.status = checked
            settingListener.updateSettingView(arrayListOf(parentSettingPojoIndex))
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_child_setting
    }

}