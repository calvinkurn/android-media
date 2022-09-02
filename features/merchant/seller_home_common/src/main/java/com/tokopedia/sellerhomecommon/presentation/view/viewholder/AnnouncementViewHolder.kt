package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.util.TypedValue
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcAnnouncementWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.model.AnnouncementWidgetUiModel
import com.tokopedia.sellerhomecommon.utils.toggleWidgetHeight

/**
 * Created By @ilhamsuaib on 09/11/20
 */

class AnnouncementViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<AnnouncementWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_announcement_widget
    }

    private val binding by lazy {
        ShcAnnouncementWidgetBinding.bind(itemView)
    }

    override fun bind(element: AnnouncementWidgetUiModel) {
        if (!listener.getIsShouldRemoveWidget()) {
            itemView.toggleWidgetHeight(true)
        }
        val data = element.data
        when {
            data == null -> showLoadingState()
            data.error.isNotBlank() -> {
                //remove widget if state is error
                if (listener.getIsShouldRemoveWidget()) {
                    listener.removeWidget(adapterPosition, element)
                } else {
                    listener.onRemoveWidget(adapterPosition)
                    itemView.toggleWidgetHeight(false)
                }
            }
            else -> showSuccessState(element)
        }
    }

    private fun showSuccessState(element: AnnouncementWidgetUiModel) {
        with(binding) {
            shcAnnouncementLoadingState.gone()
            shcAnnouncementSuccessState.visible()

            val selectableItemBg = TypedValue()
            root.context.theme.resolveAttribute(
                android.R.attr.selectableItemBackground,
                selectableItemBg, true
            )
            shcAnnouncementSuccessState.setBackgroundResource(selectableItemBg.resourceId)

            tvShcAnnouncementTitle.text = element.data?.title
            tvShcAnnouncementSubTitle.text = element.data?.subtitle.orEmpty().parseAsHtml()
            icuShcAnnouncement.setImage(IconUnify.CHEVRON_RIGHT)

            imgShcAnnouncement.loadImage(element.data?.imgUrl.orEmpty())

            cardShcAnnouncement.setOnClickListener {
                setOnCtaClick(element)
            }

            cardShcAnnouncement.addOnImpressionListener(element.impressHolder) {
                listener.sendAnnouncementImpressionEvent(element)
            }

            listener.showAnnouncementWidgetCoachMark(element.dataKey, itemView)

            setupDismissalView(element)
        }
    }

    private fun setupDismissalView(element: AnnouncementWidgetUiModel) {
        binding.run {
            if (element.isDismissible) {
                viewShcAnnouncementDismissal.visible()
                tvShcAnnouncementDismissYes.setOnClickListener {
                    tvShcAnnouncementDismissYes.gone()
                    tvShcAnnouncementDismissNo.gone()
                    tvShcAnnouncementDismiss.text = root.context.getText(
                        R.string.shc_thanks_for_your_insight
                    )
                    listener.setOnAnnouncementWidgetYesClicked(element)
                }
                tvShcAnnouncementDismissNo.setOnClickListener {
                    listener.setOnAnnouncementWidgetNoClicked(element)
                }
            } else {
                viewShcAnnouncementDismissal.gone()
            }
        }
    }

    private fun setOnCtaClick(element: AnnouncementWidgetUiModel) {
        if (RouteManager.route(itemView.context, element.data?.appLink.orEmpty())) {
            listener.sendAnnouncementClickEvent(element)
        }
    }

    private fun showLoadingState() {
        with(binding) {
            shcAnnouncementSuccessState.gone()
            shcAnnouncementLoadingState.visible()
        }
    }

    interface Listener : BaseViewHolderListener {
        fun sendAnnouncementImpressionEvent(element: AnnouncementWidgetUiModel) {}

        fun sendAnnouncementClickEvent(element: AnnouncementWidgetUiModel) {}

        fun showAnnouncementWidgetCoachMark(dataKey: String, view: View) {}

        fun setOnAnnouncementWidgetYesClicked(element: AnnouncementWidgetUiModel) {}

        fun setOnAnnouncementWidgetNoClicked(element: AnnouncementWidgetUiModel) {}
    }
}