package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.AnnouncementWidgetUiModel
import kotlinx.android.synthetic.main.shc_announcement_widget.view.*

/**
 * Created By @ilhamsuaib on 09/11/20
 */

class AnnouncementViewHolder(
        itemView: View?,
        private val listener: Listener
) : AbstractViewHolder<AnnouncementWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_announcement_widget
    }

    override fun bind(element: AnnouncementWidgetUiModel) {
        with(itemView) {
            tvShcAnnouncementTitle.text = element.title
            tvShcAnnouncementSubTitle.text = element.subtitle
            icuShcAnnouncement.setImage(IconUnify.CHEVRON_RIGHT)

            setOnClickListener {

            }
        }
    }

    interface Listener : BaseViewHolderListener
}