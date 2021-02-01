package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.notification.TrackHistory
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.common.NotificationAdapterListener
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.time.TimeHelper

class WidgetNotificationViewHolder constructor(
        itemView: View?,
        listener: NotificationItemListener?,
        private val adapterListener: NotificationAdapterListener?
) : BaseNotificationViewHolder(itemView, listener) {

    private val widgetBox: ConstraintLayout? = itemView?.findViewById(R.id.view_notification_desc)
    private val historyBtn: Typography? = itemView?.findViewById(R.id.tp_history)
    private val thumbnail: ImageView? = itemView?.findViewById(R.id.iv_product_thumbnail)
    private val widgetTitle: Typography? = itemView?.findViewById(R.id.tp_widget_title)
    private val widgetDesc: Typography? = itemView?.findViewById(R.id.tp_widget_desc)
    private val widgetDescSingle: Typography? = itemView?.findViewById(R.id.tp_widget_desc_single)
    private val widgetCta: Typography? = itemView?.findViewById(R.id.tp_cta)
    private val message: Typography? = itemView?.findViewById(R.id.tp_message)
    private val progressIndicator: Group? = itemView?.findViewById(
            R.id.group_progress_indicator
    )
    private val historyTimeLine: RecyclerView? = itemView?.findViewById(R.id.rv_history)
    private val historyAdapter: HistoryAdapter = HistoryAdapter()

    private val height_38 = itemView?.context?.resources?.getDimension(R.dimen.notif_dp_30)
    private val height_50 = itemView?.context?.resources?.getDimension(R.dimen.notif_dp_50)
    private val margin_4 = itemView?.context?.resources?.getDimension(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_4
    )
    private val margin_8 = itemView?.context?.resources?.getDimension(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_8
    )

    init {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        if (!enableTrackHistory) return
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
        if (enableTrackHistory) {
            bindTrackHistory(element)
            bindTimeLineVisibility(element)
            bindProgressIndicator(element)
            bindHistoryBtn(element)
        } else {
            historyTimeLine?.hide()
            progressIndicator?.hide()
            historyBtn?.hide()
        }
    }

    private fun bindHistoryBtn(element: NotificationUiModel) {
        historyBtn?.shouldShowWithAction(element.hasTrackHistory()) {
            bindHistoryBtnClick(element)
        }
    }

    private fun bindHistoryBtnClick(element: NotificationUiModel) {
        historyBtn?.setOnClickListener {
            element.toggleHistoryVisibility()
            bindTimeLineVisibility(element)
            bindProgressIndicator(element)
        }
    }

    private fun bindTimeLineVisibility(element: NotificationUiModel) {
        if (element.isHistoryVisible) {
            historyTimeLine?.show()
        } else {
            historyTimeLine?.hide()
        }
    }

    private fun bindProgressIndicator(element: NotificationUiModel) {
        if (element.isHistoryVisible) {
            progressIndicator?.show()
        } else {
            progressIndicator?.hide()
        }
    }

    private fun bindThumbnailHeight(element: NotificationUiModel) {
        val thumbLp = thumbnail?.layoutParams as? ViewGroup.MarginLayoutParams ?: return
        val height = if (element.isMultiLineWidget()) {
            height_50
        } else {
            height_38
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

        private const val enableTrackHistory = true
    }
}

/**
 * Adapter for [WidgetNotificationViewHolder] only
 */
class HistoryAdapter : RecyclerView.Adapter<TimeLineViewHolder>(), TimeLineViewHolder.Listener {

    val histories: ArrayList<TrackHistory> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
                TimeLineViewHolder.LAYOUT, parent, false
        )
        return TimeLineViewHolder(layout, this)
    }

    override fun getItemCount(): Int {
        return histories.size
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        holder.bind(histories[position])
    }

    fun updateHistories(element: NotificationUiModel) {
        if (histories.isNotEmpty()) {
            histories.clear()
        }
        histories.addAll(element.trackHistory)
        notifyDataSetChanged()
    }

    override fun isLastItem(position: Int): Boolean {
        return histories.isNotEmpty() && position == histories.lastIndex
    }

    override fun isFirstItem(position: Int): Boolean {
        return histories.isNotEmpty() && position == 0
    }
}

/**
 * ViewHolder for [TimeLineViewHolder] only
 */
class TimeLineViewHolder(
        itemView: View,
        private val listener: Listener
) : RecyclerView.ViewHolder(itemView) {

    interface Listener {
        fun isLastItem(position: Int): Boolean
        fun isFirstItem(position: Int): Boolean
    }

    private val title: Typography? = itemView.findViewById(R.id.tp_timeline_title)
    private val desc: Typography? = itemView.findViewById(R.id.tp_timeline_desc)
    private val bottomLine: View? = itemView.findViewById(R.id.view_bottom_line)
    private val topLine: View? = itemView.findViewById(R.id.view_top_line)

    fun bind(trackHistory: TrackHistory) {
        bindTitle(trackHistory)
        bindDesc(trackHistory)
        bindTopLine()
        bindBottomLine()
    }

    private fun bindTitle(trackHistory: TrackHistory) {
        title?.text = trackHistory.title
    }

    private fun bindDesc(trackHistory: TrackHistory) {
        desc?.text = TimeHelper.getRelativeTimeFromNow(trackHistory.createTimeUnixMillis)
    }

    private fun bindTopLine() {
        if (listener.isFirstItem(adapterPosition)) {
            topLine?.setBackgroundResource(com.tokopedia.unifycomponents.R.color.Unify_N100)
        } else {
            topLine?.setBackgroundResource(com.tokopedia.unifycomponents.R.color.Unify_G400)
        }
    }

    private fun bindBottomLine() {
        bottomLine?.showWithCondition(!listener.isLastItem(adapterPosition))
    }

    companion object {
        val LAYOUT = R.layout.item_notification_timeline_order_history
    }
}