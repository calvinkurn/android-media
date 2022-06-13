package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.SectionTitleUiModel
import com.tokopedia.unifyprinciples.Typography

class SectionTitleViewHolder(
        itemView: View?
) : AbstractViewHolder<SectionTitleUiModel>(itemView) {

    private val title: Typography? = itemView?.findViewById(R.id.txt_section_title)
    private val topPadding = itemView?.context?.resources?.getDimension(
            R.dimen.notif_dp_12
    ) ?: 0f

    override fun bind(element: SectionTitleUiModel?, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (payloads.first()) {
            PAYLOAD_UPDATE_PADDING -> bindTopPadding()
        }
    }

    override fun bind(element: SectionTitleUiModel) {
        bindTopPadding()
        bindTitle(element)
    }

    private fun bindTitle(element: SectionTitleUiModel) {
        title?.text = element.title
    }

    private fun bindTopPadding() {
        title?.apply {
            if (isFirstPosition()) {
                setPadding(paddingLeft, 0, paddingRight, paddingBottom)
            } else {
                setPadding(paddingLeft, topPadding.toInt(), paddingRight, paddingBottom)
            }
        }
    }

    private fun isFirstPosition(): Boolean {
        return adapterPosition == 0
    }

    companion object {
        val LAYOUT = R.layout.item_notifcenter_title
        const val PAYLOAD_UPDATE_PADDING = "payload_update_padding"
    }
}