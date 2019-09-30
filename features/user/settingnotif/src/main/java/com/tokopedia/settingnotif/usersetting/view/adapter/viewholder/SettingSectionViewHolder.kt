package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.SettingSections

class SettingSectionViewHolder(itemView: View?) : AbstractViewHolder<SettingSections>(itemView) {

    private val sectionTitle = itemView?.findViewById<TextView>(R.id.tv_section_title)

    override fun bind(element: SettingSections?) {
        sectionTitle?.text = element?.title
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_setting_section
    }
}