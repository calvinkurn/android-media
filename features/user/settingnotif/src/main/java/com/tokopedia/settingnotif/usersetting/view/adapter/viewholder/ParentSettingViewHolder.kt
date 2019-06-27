package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.Switch
import android.widget.TextView
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.ParentSetting

class ParentSettingViewHolder(
        itemView: View?,
        settingListener: SettingListener
) : SettingViewHolder<ParentSetting>(itemView, settingListener) {

    private val switchTitle: TextView? = itemView?.findViewById(R.id.tv_sw_title)
    private val description: TextView? = itemView?.findViewById(R.id.tv_desc)

    override fun getSwitchView(itemView: View?): Switch? {
        return itemView?.findViewById(R.id.sw_setting)
    }

    override fun bind(element: ParentSetting?) {
        super.bind(element)
        if (element == null) return

        setSettingTitle(element)
        setSettingDesc(element)
    }

    private fun setSettingTitle(element: ParentSetting) {
        switchTitle?.text = element.name
    }

    private fun setSettingDesc(element: ParentSetting) {
        if (element.hasDescription()) {
            description?.visibility = View.VISIBLE
            description?.text = element.description
        } else {
            description?.visibility = View.GONE
        }
    }

    override fun updateSiblingAndChild(element: ParentSetting, checked: Boolean) {
        if (!element.hasChild()) return

        val childs = element.childSettings
        val childPosition = arrayListOf<Int>()
        for (index in 0 until childs.size) {
            val child = childs[index]
            if (child?.status != checked) {
                child?.status = checked
                childPosition.add(index)
            }
        }

        if (childPosition.isEmpty()) return

        val dataPosition = adapterPosition
        val adapterChildPosition = childPosition.map { index -> dataPosition + 1 + index }
        settingListener.updateSettingView(adapterChildPosition)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_parent_setting
    }
}