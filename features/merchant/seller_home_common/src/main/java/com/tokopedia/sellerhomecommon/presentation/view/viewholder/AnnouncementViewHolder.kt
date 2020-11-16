package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.util.TypedValue
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
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
        val data = element.data
        when {
            data == null -> showLoadingState()
            data.error.isNotBlank() -> {
                //remove widget if state is error
                listener.removeWidget(adapterPosition, element)
            }
            else -> showSuccessState(element)
        }
    }

    private fun showSuccessState(element: AnnouncementWidgetUiModel) {
        with(itemView) {
            shcAnnouncementLoadingState.gone()
            shcAnnouncementSuccessState.visible()

            val selectableItemBg = TypedValue()
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground,
                    selectableItemBg, true)
            shcAnnouncementSuccessState.setBackgroundResource(selectableItemBg.resourceId)

            tvShcAnnouncementTitle.text = element.data?.title
            tvShcAnnouncementSubTitle.text = element.data?.subtitle
            icuShcAnnouncement.setImage(IconUnify.CHEVRON_RIGHT)

            ImageHandler.loadImageWithoutPlaceholderAndError(imgShcAnnouncement, element.data?.imgUrl.orEmpty())

            setOnClickListener {
                setOnCtaClick(element)
            }

            addOnImpressionListener(element.impressHolder) {
                listener.sendAnnouncementImpressionEvent(element)
            }
        }
    }

    private fun setOnCtaClick(element: AnnouncementWidgetUiModel) {
        if (RouteManager.route(itemView.context, element.data?.appLink.orEmpty())) {
            listener.sendAnnouncementClickEvent(element)
        }
    }

    private fun showLoadingState() {
        with(itemView) {
            shcAnnouncementSuccessState.gone()
            shcAnnouncementLoadingState.visible()
        }
    }

    interface Listener : BaseViewHolderListener {
        fun sendAnnouncementImpressionEvent(element: AnnouncementWidgetUiModel) {}

        fun sendAnnouncementClickEvent(element: AnnouncementWidgetUiModel) {}
    }
}