package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.LayoutInflater
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
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.notification.TrackHistory
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.common.NotificationAdapterListener
import com.tokopedia.unifycomponents.ContainerUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.time.TimeHelper

class WidgetNotificationViewHolder constructor(
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
    private val historyAdapter: HistoryAdapter = HistoryAdapter()

    private val height_46 = itemView?.context?.resources?.getDimension(R.dimen.notif_dp_46)
    private val height_58 = itemView?.context?.resources?.getDimension(R.dimen.notif_dp_58)
    private val margin_4 = itemView?.context?.resources?.getDimension(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_4
    )
    private val margin_8 = itemView?.context?.resources?.getDimension(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_8
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
            setRecycledViewPool(adapterListener?.getWidgetTimelineViewPool())
            layoutManager = LinearLayoutManager(itemView.context)
            adapter = historyAdapter
        }
    }

    override fun bind(element: NotificationUiModel) {
        super.bind(element)
        bindWidgetBox(element)
        bindHistoryBox(element)
        bindMessage(element)
        bindPaddingBottom(element)
        bindContainerUnify(element)
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
            lineColor = com.tokopedia.unifyprinciples.R.color.Unify_G400
        } else {
            icon = R.drawable.ic_notifcenter_on_progress_mark
            lineColor = com.tokopedia.unifyprinciples.R.color.Unify_N100
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
        ImageHandler.LoadImage(thumbnail, element.widget.image)
    }

    private fun bindWidgetTitle(element: NotificationUiModel) {
        widgetTitle?.text = element.widgetTitleHtml
    }

    private fun bindWidgetDescSingle(element: NotificationUiModel) {
        widgetDescSingle?.shouldShowWithAction(element.hasSingleLineDesc()) {
            widgetDescSingle.text = element.widgetDescHtml
        }
    }

    private fun bindWidgetDesc(element: NotificationUiModel) {
        widgetDesc?.shouldShowWithAction(element.isMultiLineWidget()) {
            widgetDesc.text = element.widgetDescHtml
        }
    }

    private fun bindWidgetCta(element: NotificationUiModel) {
        widgetCta?.shouldShowWithAction(element.widget.buttonText.isNotEmpty()) {
            widgetCta.text = element.widget.buttonText
            widgetCta.setOnClickListener {
                listener?.trackClickCtaWidget(element)
                RouteManager.route(itemView.context, element.widget.androidButtonLink)
            }
        }
    }

    private fun bindMessage(element: NotificationUiModel) {
        message?.shouldShowWithAction(element.widget.message.isNotEmpty()) {
            bindMessageMargin(element)
            val widgetMessage = if (element.noWidgetWithTrackHistory()) {
                element.shortDescHtml
            } else {
                element.widgetMessageHtml
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
        val marginTop = if (element.noWidgetWithTrackHistory()) {
            margin_4
        } else {
            margin_8
        } ?: return
        messageLp.topMargin = marginTop.toInt()
        message.layoutParams = messageLp
    }

    companion object {
        val LAYOUT = R.layout.item_notification_widget_order_history
    }
}

/**
 * Adapter for [WidgetNotificationViewHolder] only
 */
class HistoryAdapter : RecyclerView.Adapter<TimeLineViewHolder>() {

    private var histories: List<TrackHistory> = emptyList()
    private var isLastJourney = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
                TimeLineViewHolder.LAYOUT, parent, false
        )
        return TimeLineViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return histories.size
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        val isLastItem = isLastItem(position)
        val isFirstItem = isFirstItem(position)
        holder.bind(histories[position], isFirstItem, isLastItem, isLastJourney)
    }

    fun updateHistories(element: NotificationUiModel) {
        histories = element.trackHistory
        isLastJourney = element.isLastJourney
        notifyDataSetChanged()
    }

    private fun isLastItem(position: Int): Boolean {
        return histories.isNotEmpty() && position == histories.lastIndex
    }

    private fun isFirstItem(position: Int): Boolean {
        return histories.isNotEmpty() && position == 0
    }
}

/**
 * ViewHolder for [TimeLineViewHolder] only
 */
class TimeLineViewHolder constructor(
        itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val title: Typography? = itemView.findViewById(R.id.tp_timeline_title)
    private val desc: Typography? = itemView.findViewById(R.id.tp_timeline_desc)
    private val bottomLine: View? = itemView.findViewById(R.id.view_bottom_line)
    private val topLine: View? = itemView.findViewById(R.id.view_top_line)

    fun bind(
            trackHistory: TrackHistory,
            isFirstItem: Boolean,
            isLastItem: Boolean,
            isLastJourney: Boolean
    ) {
        bindTitle(trackHistory)
        bindDesc(trackHistory)
        bindTopLine(isFirstItem, isLastJourney)
        bindBottomLine(isLastItem)
    }

    private fun bindTitle(trackHistory: TrackHistory) {
        title?.text = trackHistory.title
    }

    private fun bindDesc(trackHistory: TrackHistory) {
        desc?.text = TimeHelper.getRelativeTimeFromNow(trackHistory.createTimeUnixMillis)
    }

    private fun bindTopLine(isFirstItem: Boolean, lastJourney: Boolean) {
        if (isFirstItem && !lastJourney) {
            topLine?.setBackgroundResource(com.tokopedia.unifycomponents.R.color.Unify_N100)
        } else {
            topLine?.setBackgroundResource(com.tokopedia.unifycomponents.R.color.Unify_G400)
        }
    }

    private fun bindBottomLine(isLastItem: Boolean) {
        bottomLine?.showWithCondition(!isLastItem)
    }

    companion object {
        val LAYOUT = R.layout.item_notification_timeline_order_history
    }
}