package com.tokopedia.logisticcart.schedule_slot.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.logisticcart.schedule_slot.uimodel.TitleSectionUiModel
import com.tokopedia.unifyprinciples.Typography

class TitleSectionViewHolder(private val view: View) : AbstractViewHolder<TitleSectionUiModel>(view) {

    val icon = view.findViewById<IconUnify>(com.tokopedia.logisticcart.R.id.icon_title)
    val title = view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.tv_title)
    val description = view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.tv_description)

    override fun bind(element: TitleSectionUiModel) {
        if (element.title.isNotEmpty()) {
            title.text = element.title
            title.visibility = View.VISIBLE
        } else {
            title.visibility = View.GONE
        }

        if (element.content.isNotEmpty()) {
            description.text = element.content
            description.visibility = View.VISIBLE
        } else {
            description.visibility = View.GONE
        }

        if (element.icon != NO_ICON) {
            icon.setImage(element.icon)
            icon.setOnClickListener {
                element.onClick.invoke()
            }
            icon.visibility = View.VISIBLE
        } else {
            icon.visibility = View.GONE
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT_RES = com.tokopedia.logisticcart.R.layout.viewholder_title_section
        private const val NO_ICON = -1
    }
}
