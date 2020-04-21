package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.data.pojo.SettingSections
import com.tokopedia.settingnotif.usersetting.util.componentTextColor

class SettingSectionViewHolder(itemView: View?) : AbstractViewHolder<SettingSections>(itemView) {

    private val icSection = itemView?.findViewById<ImageView>(R.id.ic_section)
    private val sectionTitle = itemView?.findViewById<TextView>(R.id.tv_section_title)

    private val context by lazy { itemView?.context }

    override fun bind(element: SettingSections?) {
        if (element == null) return
        sectionTitle?.text = element.title
        ImageHandler.LoadImage(icSection, element.icon)

        context?.let {
            val colorId = componentTextColor(element.isEnabled)
            sectionTitle?.setTextColor(ContextCompat.getColor(it, colorId))
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_setting_section
    }
}