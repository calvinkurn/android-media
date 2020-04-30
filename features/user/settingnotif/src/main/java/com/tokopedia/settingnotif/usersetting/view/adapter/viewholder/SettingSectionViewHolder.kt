package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.SettingSections

class SettingSectionViewHolder(itemView: View?) : AbstractViewHolder<SettingSections>(itemView) {

    private val icSection = itemView?.findViewById<ImageView>(R.id.ic_section)
    private val sectionTitle = itemView?.findViewById<TextView>(R.id.tv_section_title)

    override fun bind(element: SettingSections?) {
        sectionTitle?.text = element?.title
        ImageHandler.LoadImage(icSection, element?.icon)
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_setting_section
    }
}