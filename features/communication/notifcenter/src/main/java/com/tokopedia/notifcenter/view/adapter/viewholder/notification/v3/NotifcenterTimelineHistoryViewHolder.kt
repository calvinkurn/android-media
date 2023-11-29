package com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.notification.TrackHistory
import com.tokopedia.notifcenter.view.adapter.NotifcenterWidgetHistoryType
import com.tokopedia.notifcenter.view.adapter.NotifcenterWidgetHistoryType.*
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.time.TimeHelper
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class NotifcenterTimelineHistoryViewHolder constructor(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val title: Typography? = itemView.findViewById(R.id.notifcenter_tv_history_title)
    private val subtitle: Typography? = itemView.findViewById(R.id.notifcenter_tv_history_subtitle)
    private val icon: ImageUnify? = itemView.findViewById(R.id.notifcenter_iv_history_circle)
    private val time: Typography? = itemView.findViewById(R.id.notifcenter_tv_history_time)
    private val topLine: View? = itemView.findViewById(R.id.notifcenter_history_top_line)
    private val bottomLine: View? = itemView.findViewById(R.id.notifcenter_history_bottom_line)

    fun bind(
        trackHistory: TrackHistory,
        type: NotifcenterWidgetHistoryType,
        isFirstItem: Boolean,
        isLastItem: Boolean,
        isLastJourney: Boolean
    ) {
        bindCircleIndicator(type)
        bindTitle(trackHistory)
        bindSubtitle(trackHistory)
        bindTime(trackHistory)
        bindTopLine(type, isFirstItem, isLastJourney)
        bindBottomLine(isLastItem, type)
    }

    private fun bindCircleIndicator(type: NotifcenterWidgetHistoryType) {
        when (type) {
            ORDER -> {
                icon?.loadImage(R.drawable.ic_notifcenter_success_mark)
            }
            FEED -> {
                icon?.loadImage(R.drawable.notifcenter_ic_gray_circle_history)
            }
        }
    }

    private fun bindTitle(trackHistory: TrackHistory) {
        title?.text = trackHistory.title
    }

    private fun bindSubtitle(trackHistory: TrackHistory) {
        subtitle?.text = trackHistory.subtitle
        subtitle?.showWithCondition(trackHistory.subtitle.isNotBlank())
    }

    private fun bindTime(trackHistory: TrackHistory) {
        time?.text = TimeHelper.getRelativeTimeFromNow(trackHistory.createTimeUnixMillis)
    }

    private fun bindTopLine(
        type: NotifcenterWidgetHistoryType,
        isFirstItem: Boolean,
        lastJourney: Boolean
    ) {
        when (type) {
            ORDER -> setTopLineOrder(isFirstItem, lastJourney)
            FEED ->  setTopLineFeed()
        }
    }

    private fun setTopLineOrder(
        isFirstItem: Boolean,
        lastJourney: Boolean
    ) {
        if (isFirstItem && !lastJourney) {
            topLine?.setBackgroundResource(unifyprinciplesR.color.Unify_NN200)
        } else {
            topLine?.setBackgroundResource(unifyprinciplesR.color.Unify_GN500)
        }
    }

    private fun setTopLineFeed() {
        topLine?.setBackgroundResource(unifyprinciplesR.color.Unify_NN200)
    }

    private fun bindBottomLine(
        isLastItem: Boolean,
        type: NotifcenterWidgetHistoryType
    ) {
        bottomLine?.showWithCondition(!isLastItem)
        when (type) {
            ORDER -> bindBottomLineOrder()
            FEED ->  bindBottomLineFeed()
        }
    }

    private fun bindBottomLineOrder() {
        bottomLine?.setBackgroundResource(unifyprinciplesR.color.Unify_GN500)
    }

    private fun bindBottomLineFeed() {
        bottomLine?.setBackgroundResource(unifyprinciplesR.color.Unify_NN200)
    }

    companion object {
        val LAYOUT = R.layout.notifcenter_timeline_history_item
    }
}
