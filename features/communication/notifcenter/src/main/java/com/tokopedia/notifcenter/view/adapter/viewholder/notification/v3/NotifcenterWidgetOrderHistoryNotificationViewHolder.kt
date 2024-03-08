package com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImageFitCenter
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.view.adapter.NotifcenterTimelineHistoryAdapter
import com.tokopedia.notifcenter.view.adapter.NotifcenterWidgetHistoryType
import com.tokopedia.notifcenter.view.listener.NotificationItemListener
import com.tokopedia.notifcenter.view.adapter.common.NotificationAdapterListener
import com.tokopedia.unifycomponents.ContainerUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class NotifcenterWidgetOrderHistoryNotificationViewHolder constructor(
    itemView: View?,
    private val listener: NotificationItemListener?,
    private val adapterListener: NotificationAdapterListener?
) : BaseNotificationViewHolder(itemView, listener) {

    private val widgetBox: ConstraintLayout? = itemView?.findViewById(R.id.view_notification_desc)
    private val toggleHistoryBtn: LinearLayout? = itemView?.findViewById(R.id.toggle_history_btn)
    private val historyViewTextState: Typography? = itemView?.findViewById(R.id.tp_history)
    private val historyViewIconState: IconUnify? = itemView?.findViewById(R.id.tp_history_state)
    private val thumbnail: ImageView? = itemView?.findViewById(R.id.iv_product_thumbnail)
    private val widgetTitle: Typography? = itemView?.findViewById(R.id.tp_widget_title)
    private val widgetDesc: Typography? = itemView?.findViewById(R.id.tp_widget_desc)
    private val widgetDescSingle: Typography? = itemView?.findViewById(R.id.tp_widget_desc_single)
    private val widgetCta: Typography? = itemView?.findViewById(R.id.tp_cta)
    private val message: Typography? = itemView?.findViewById(R.id.tp_message)
    private val parentIndicatorMark: ImageView? = itemView?.findViewById(R.id.iv_mark_seen)
    private val parentIndicatorLine: View? = itemView?.findViewById(R.id.view_bottom_line)
    private val parentProgressIndicator: Group? = itemView?.findViewById(
            R.id.group_progress_indicator
    )
    private val historyTimeLine: RecyclerView? = itemView?.findViewById(R.id.rv_history)
    private val containerUnify: ContainerUnify? = itemView?.findViewById(R.id.unify_container)
    private val historyAdapter: NotifcenterTimelineHistoryAdapter =
        NotifcenterTimelineHistoryAdapter(NotifcenterWidgetHistoryType.ORDER)

    private val height_46 = itemView?.context?.resources?.getDimension(R.dimen.notif_dp_46)
    private val height_58 = itemView?.context?.resources?.getDimension(R.dimen.notif_dp_58)
    private val margin_4 = itemView?.context?.resources?.getDimension(
        unifyprinciplesR.dimen.unify_space_4
    )
    private val margin_8 = itemView?.context?.resources?.getDimension(
        unifyprinciplesR.dimen.unify_space_8
    )
    private val padding_12 = itemView?.context?.resources?.getDimension(
            R.dimen.notif_dp_12
    )

    init {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        historyTimeLine?.apply {
            setHasFixedSize(true)
            itemAnimator = null
            isNestedScrollingEnabled = false
            setRecycledViewPool(adapterListener?.getWidgetOrderTimelineViewPool())
            layoutManager = LinearLayoutManager(itemView.context)
            adapter = historyAdapter
        }
    }

    override fun bind(element: NotificationUiModel) {
        super.bind(element)
        bindWidgetBox(element)
        bindHistoryBox(element)
        bindMessage(element)
        bindIconMarkSeenAnchor(element)
        bindPaddingBottom(element)
        bindContainerUnify(element)
    }

    private fun bindIconMarkSeenAnchor(element: NotificationUiModel) {
        val viewAnchor = if (widgetBox?.isVisible == true) {
            widgetBox
        } else {
            message
        }
        viewAnchor ?: return
        val lp = parentIndicatorMark?.layoutParams as? ConstraintLayout.LayoutParams
        lp?.let {
            lp.topToTop = viewAnchor.id
            lp.bottomToBottom = viewAnchor.id
        }
        parentIndicatorMark?.layoutParams = lp
    }

    private fun bindContainerUnify(element: NotificationUiModel) {
        containerUnify?.showWithCondition(element.isRead() && element.isHistoryVisible)
    }

    private fun bindTrackHistory(element: NotificationUiModel) {
        historyAdapter.updateHistories(element)
    }

    private fun bindWidgetBox(element: NotificationUiModel) {
        widgetBox?.shouldShowWithAction(element.hasWidget()) {
            bindThumbnailHeight(element)
            bindThumbnail(element)
            bindWidgetTitle(element)
            bindWidgetDescSingle(element)
            bindWidgetDesc(element)
            bindWidgetCta(element)
        }
    }

    private fun bindHistoryBox(element: NotificationUiModel) {
        bindTrackHistory(element)
        bindTimeLineVisibility(element)
        bindProgressIndicatorVisibility(element)
        bindProgressIndicatorColor(element)
        bindHistoryBtn(element)
    }

    private fun bindHistoryBtn(element: NotificationUiModel) {
        toggleHistoryBtn?.shouldShowWithAction(element.hasTrackHistory()) {
            bindHistoryViewTextState(element)
            bindHistoryViewIconState(element)
            bindHistoryBtnClick(element)
        }
    }

    private fun bindHistoryViewTextState(element: NotificationUiModel) {
        val triggerText = if (element.isHistoryVisible) {
            R.string.cta_widget_notifcenter_close_previous
        } else {
            R.string.cta_widget_notifcenter_see_previous
        }
        historyViewTextState?.setText(triggerText)
    }

    private fun bindHistoryViewIconState(element: NotificationUiModel) {
        val icon = if (element.isHistoryVisible) {
            IconUnify.CHEVRON_UP
        } else {
            IconUnify.CHEVRON_DOWN
        }
        historyViewIconState?.setImage(
                newIconId = icon
        )
    }

    private fun trackWhenExpanded(element: NotificationUiModel) {
        if (element.isHistoryVisible) {
            listener?.trackExpandTimelineHistory(element)
        }
    }

    private fun bindHistoryBtnClick(element: NotificationUiModel) {
        toggleHistoryBtn?.setOnClickListener {
            element.toggleHistoryVisibility()
            bindTimeLineVisibility(element)
            bindProgressIndicatorVisibility(element)
            bindHistoryViewTextState(element)
            bindHistoryViewIconState(element)
            bindContainerUnify(element)
            trackWhenExpanded(element)
        }
    }

    private fun bindTimeLineVisibility(element: NotificationUiModel) {
        if (element.isHistoryVisible) {
            historyTimeLine?.show()
        } else {
            historyTimeLine?.hide()
        }
    }

    private fun bindProgressIndicatorVisibility(element: NotificationUiModel) {
        if (element.isHistoryVisible) {
            parentProgressIndicator?.show()
        } else {
            parentProgressIndicator?.hide()
        }
    }

    private fun bindProgressIndicatorColor(element: NotificationUiModel) {
        @DrawableRes val icon: Int
        @ColorRes val lineColor: Int
        if (element.isLastJourney) {
            icon = R.drawable.ic_notifcenter_success_mark
            lineColor = unifyprinciplesR.color.Unify_GN500
        } else {
            icon = R.drawable.ic_notifcenter_on_progress_mark
            lineColor = unifyprinciplesR.color.Unify_NN200
        }
        parentIndicatorMark?.setImageResource(icon)
        parentIndicatorLine?.setBackgroundResource(lineColor)
    }

    private fun bindThumbnailHeight(element: NotificationUiModel) {
        val thumbLp = thumbnail?.layoutParams as? ViewGroup.MarginLayoutParams ?: return
        val height = if (element.isMultiLineWidget()) {
            height_58
        } else {
            height_46
        } ?: return
        thumbLp.height = height.toInt()
        thumbnail.layoutParams = thumbLp
    }

    private fun bindThumbnail(element: NotificationUiModel) {
        thumbnail?.loadImageFitCenter(element.widget.image)
    }

    private fun bindWidgetTitle(element: NotificationUiModel) {
        widgetTitle?.text = element.widgetTitleHtml
    }

    private fun bindWidgetDescSingle(element: NotificationUiModel) {
        widgetDescSingle?.shouldShowWithAction(element.hasSingleLineDesc()) {
            widgetDescSingle.text = element.widgetDescHtml
            bindWidgetDescSingleMargin(element)
        }
    }

    private fun bindWidgetDescSingleMargin(element: NotificationUiModel) {
        val lp = widgetDescSingle?.layoutParams
                as? ViewGroup.MarginLayoutParams ?: return
        if (element.widget.hasCta()) {
            lp.rightMargin = 0
        } else {
            lp.rightMargin = padding_12?.toInt() ?: 0
        }
        widgetDescSingle.layoutParams = lp
    }

    private fun bindWidgetDesc(element: NotificationUiModel) {
        widgetDesc?.shouldShowWithAction(element.isMultiLineWidget()) {
            widgetDesc.text = element.widgetDescHtml
        }
    }

    private fun bindWidgetCta(element: NotificationUiModel) {
        widgetCta?.shouldShowWithAction(element.widget.hasCta()) {
            widgetCta.text = element.widget.buttonText
            widgetCta.setOnClickListener {
                listener?.trackClickCtaWidget(element)
                RouteManager.route(itemView.context, element.widget.androidButtonLink)
            }
        }
    }

    private fun bindMessage(element: NotificationUiModel) {
        val noWidgetWithTrackHistory = element.noWidgetOrderWithTrackHistory() &&
                element.shortDescHtml.isNotEmpty()
        message?.shouldShowWithAction(
            element.hasWidgetMsg() || noWidgetWithTrackHistory
        ) {
            bindMessageMargin(element)
            val widgetMessage = if (element.hasWidgetMsg()) {
                element.widgetMessageHtml
            } else {
                element.shortDescHtml
            }
            message.text = widgetMessage
        }
    }

    private fun bindPaddingBottom(element: NotificationUiModel) {
        container?.let {
            if (toggleHistoryBtn?.isVisible == true) {
                container.setPadding(
                        it.paddingLeft,
                        it.paddingTop,
                        it.paddingRight,
                        0
                )
            } else {
                container.setPadding(
                        it.paddingLeft,
                        it.paddingTop,
                        it.paddingRight,
                        padding_12?.toInt() ?: 0
                )
            }
        }
    }

    private fun bindMessageMargin(element: NotificationUiModel) {
        val messageLp = message?.layoutParams as? ViewGroup.MarginLayoutParams ?: return
        val marginTop = if (element.noWidgetOrderWithTrackHistory()) {
            margin_4
        } else {
            margin_8
        } ?: return
        messageLp.topMargin = marginTop.toInt()
        message.layoutParams = messageLp
    }

    companion object {
        val LAYOUT = R.layout.notifcenter_widget_order_history_item
    }
}
