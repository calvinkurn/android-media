package com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.view.adapter.NotifcenterTimelineHistoryAdapter
import com.tokopedia.notifcenter.view.adapter.NotifcenterWidgetHistoryType
import com.tokopedia.notifcenter.view.adapter.common.NotificationAdapterListener
import com.tokopedia.notifcenter.view.listener.NotificationItemListener
import com.tokopedia.unifycomponents.ContainerUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class NotifcenterWidgetFeedHistoryNotificationViewHolder constructor(
    itemView: View?,
    private val listener: NotificationItemListener?,
    private val adapterListener: NotificationAdapterListener?
) : BaseNotificationViewHolder(itemView, listener) {

    /**
     * Toggle Button
     */
    private val linearLayoutToggleHistory: LinearLayout? = itemView?.findViewById(R.id.notifcenter_ll_toggle_history)
    private val textToggleHistory: Typography? = itemView?.findViewById(R.id.notifcenter_tv_toggle_history)
    private val iconToggleHistory: IconUnify? = itemView?.findViewById(R.id.notifcenter_icon_toggle_history)

    /**
     * First indicator / Title indicator with line
     */
    private val titleCircleIndicator: ImageView? = itemView?.findViewById(R.id.notifcenter_iv_title_progress_indicator_circle)
    private val titleBottomLineIndicator: View? = itemView?.findViewById(R.id.notifcenter_bottom_line)
    private val titleGroupProgressIndicator: Group? = itemView?.findViewById(
        R.id.notifcenter_group_title_progress_indicator
    )

    /**
     * Feed History RV
     */
    private val rvFeedHistory: RecyclerView? = itemView?.findViewById(R.id.notifcenter_rv_feed_history)
    private val containerFeedHistory: ContainerUnify? = itemView?.findViewById(R.id.notifcenter_container_feed_history)
    private val historyAdapter: NotifcenterTimelineHistoryAdapter = NotifcenterTimelineHistoryAdapter(
        NotifcenterWidgetHistoryType.FEED
    )

    init {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        rvFeedHistory?.apply {
            setHasFixedSize(true)
            itemAnimator = null
            isNestedScrollingEnabled = false
            setRecycledViewPool(adapterListener?.getWidgetFeedTimelineViewPool())
            layoutManager = LinearLayoutManager(itemView.context)
            adapter = historyAdapter
        }
    }

    override fun bind(element: NotificationUiModel) {
        super.bind(element)
        bindHistoryBox(element)
        bindPaddingBottom()
        bindContainerUnify(element)
    }

    private fun bindContainerUnify(element: NotificationUiModel) {
        containerFeedHistory?.showWithCondition(
            element.isRead() &&
                element.isHistoryVisible
        )
    }

    private fun bindTrackHistory(element: NotificationUiModel) {
        historyAdapter.updateHistories(element)
    }

    private fun bindHistoryBox(element: NotificationUiModel) {
        bindTrackHistory(element)
        bindTimeLineVisibility(element)
        bindProgressIndicatorVisibility(element)
        bindProgressIndicatorColor()
        bindHistoryBtn(element)
    }

    private fun bindHistoryBtn(element: NotificationUiModel) {
        linearLayoutToggleHistory?.shouldShowWithAction(element.hasTrackHistory()) {
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
        textToggleHistory?.setText(triggerText)
    }

    private fun bindHistoryViewIconState(element: NotificationUiModel) {
        val icon = if (element.isHistoryVisible) {
            IconUnify.CHEVRON_UP
        } else {
            IconUnify.CHEVRON_DOWN
        }
        iconToggleHistory?.setImage(
            newIconId = icon
        )
    }

    private fun trackWhenExpanded(element: NotificationUiModel) {
        if (element.isHistoryVisible) {
            listener?.trackExpandTimelineHistory(element)
        }
    }

    private fun bindHistoryBtnClick(element: NotificationUiModel) {
        linearLayoutToggleHistory?.setOnClickListener {
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
        rvFeedHistory?.showWithCondition(element.isHistoryVisible)
    }

    private fun bindProgressIndicatorVisibility(element: NotificationUiModel) {
        titleGroupProgressIndicator?.showWithCondition(element.isHistoryVisible)
    }

    private fun bindProgressIndicatorColor() {
        titleCircleIndicator?.setImageResource(R.drawable.notifcenter_ic_gray_circle_history)
        titleBottomLineIndicator?.setBackgroundResource(unifyprinciplesR.color.Unify_NN200)
    }

    private fun bindPaddingBottom() {
        container?.let {
            if (linearLayoutToggleHistory?.isVisible == true) {
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
                    12.toPx()
                )
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.notifcenter_widget_feed_history_item
    }
}
