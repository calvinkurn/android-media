package com.tokopedia.sellerhomecommon.presentation.view.viewholder

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
import com.tokopedia.sellerhomecommon.common.DismissibleState
import com.tokopedia.sellerhomecommon.databinding.ShcAnnouncementWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.model.AnnouncementWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.view.customview.DismissalTimerView
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
                    listener.removeWidget(absoluteAdapterPosition, element)
                } else {
                    listener.onRemoveWidget(absoluteAdapterPosition)
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

            if (element.shouldShowDismissalTimer) {
                showTimerState(element)
                return
            }

            shcAnnouncementTimerView.gone()

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

    private fun showTimerState(element: AnnouncementWidgetUiModel) {
        with(binding) {
            hideDismissibleView()
            shcAnnouncementSuccessState.gone()
            shcAnnouncementTimerView.visible()
            shcAnnouncementTimerView.setBackgroundResource(R.drawable.shc_dashed_background)

            val title = root.context.getString(R.string.shc_info_deleted)
            shcAnnouncementTimerView.startTimer(
                title = title,
                duration = DismissalTimerView.DEFAULT_DURATION,
                listener = object : DismissalTimerView.Listener {

                    override fun onFinished() {
                        listener.removeWidget(absoluteAdapterPosition, element)
                    }

                    override fun onCancelTimer() {
                        element.shouldShowDismissalTimer = false
                        showSuccessState(element)
                        listener.setOnWidgetCancelDismissal(element)
                    }

                    override fun onTicked(millisUntilFinished: Long) {}
                }
            )
        }
    }

    private fun setupDismissalView(element: AnnouncementWidgetUiModel) {
        binding.run {
            when {
                element.isDismissible && element.dismissibleState == DismissibleState.ALWAYS -> {
                    showDismissibleView(element)
                }
                element.isDismissible && element.dismissibleState == DismissibleState.TRIGGER -> {
                    hideDismissibleView()
                    cardShcAnnouncement.setOnLongClickListener {
                        showDismissibleView(element)
                        return@setOnLongClickListener true
                    }
                }
                else -> {
                    hideDismissibleView()
                }
            }
        }
    }

    private fun hideDismissibleView() {
        with(binding) {
            shcAnnouncementContainer.gone()
            viewShcSpacer.gone()
        }
    }

    private fun showDismissibleView(element: AnnouncementWidgetUiModel) {
        binding.run {
            shcAnnouncementContainer.visible()
            viewShcAnnouncementDismissal.visible()
            viewShcSpacer.visible()
            tvShcAnnouncementDismissYes.visible()
            tvShcAnnouncementDismissNo.visible()
            tvShcAnnouncementDismiss.text = root.context.getText(
                R.string.shc_still_need_this_info
            )
            tvShcAnnouncementDismissYes.setOnClickListener {
                tvShcAnnouncementDismissYes.gone()
                tvShcAnnouncementDismissNo.gone()
                tvShcAnnouncementDismiss.text = root.context.getText(
                    R.string.shc_thanks_for_your_insight
                )
                element.dismissibleState = DismissibleState.TRIGGER
                listener.setOnAnnouncementWidgetYesClicked(element)
            }
            tvShcAnnouncementDismissNo.setOnClickListener {
                listener.setOnAnnouncementWidgetNoClicked(element)
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
            shcAnnouncementContainer.visible()
            shcAnnouncementLoadingState.visible()
            shcAnnouncementTimerView.gone()
            viewShcAnnouncementDismissal.gone()
        }
    }

    interface Listener : BaseViewHolderListener, WidgetDismissalListener {
        fun sendAnnouncementImpressionEvent(element: AnnouncementWidgetUiModel) {}

        fun sendAnnouncementClickEvent(element: AnnouncementWidgetUiModel) {}

        fun showAnnouncementWidgetCoachMark(dataKey: String, view: View) {}

        fun setOnAnnouncementWidgetYesClicked(element: AnnouncementWidgetUiModel) {}

        fun setOnAnnouncementWidgetNoClicked(element: AnnouncementWidgetUiModel) {}
    }
}
