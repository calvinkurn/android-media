package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.Switch
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.ChildSetting
import com.tokopedia.settingnotif.usersetting.domain.pojo.ParentSetting

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
        val currentChildIndex = element.getIndex(currentSettingPosition, parentSettingIndex)

        val otherSiblingHasSameCheckedStatus = parentSetting.isOtherChildHasSameCheckedStatus(currentChildIndex, checked)

        if (parentSetting.needToUpdateCheckedStatus(otherSiblingHasSameCheckedStatus, checked)) {
            updateParentSettingCheckedState(parentSetting, parentSettingIndex, checked)
        }
    }

    private fun updateParentSettingCheckedState(
            parentSetting: ParentSetting,
            parentSettingIndex: Int,
            checked: Boolean
    ) {
        parentSetting.status = checked
        settingListener.updateSettingView(arrayListOf(parentSettingIndex))
    }

    override fun getUpdatedSettingIds(element: ChildSetting, checked: Boolean): List<Map<String, Any>> {
        return listOf(
                getMapSettingToChange(element, checked)
        )
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_child_setting
    }

}