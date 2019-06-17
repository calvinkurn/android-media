package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.Switch
import android.widget.TextView
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.ParentSettingPojo

class ParentSettingViewHolder(itemView: View?) : SettingViewHolder<ParentSettingPojo>(itemView) {

    private val switchTitle: TextView? = itemView?.findViewById(R.id.tv_sw_title)
    private val description: TextView? = itemView?.findViewById(R.id.tv_desc)

    override fun getSwitchView(itemView: View?): Switch? {
        return itemView?.findViewById(R.id.sw_setting)
    }

    override fun bind(element: ParentSettingPojo?) {
        super.bind(element)
        if(element == null) return

        setSettingTitle(element)
        setSettingDesc(element)
    }

    private fun setSettingTitle(element: ParentSettingPojo) {
        switchTitle?.text = element.name
    }

    private fun setSettingDesc(element: ParentSettingPojo) {
        if (element.hasDescription()) {
            description?.visibility = View.VISIBLE
            description?.text = element.description
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_parent_setting
    }
}