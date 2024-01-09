package com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3

import android.text.method.LinkMovementMethod
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.notification.TrackHistory
import com.tokopedia.notifcenter.util.setupHtmlUrls
import com.tokopedia.notifcenter.view.adapter.NotifcenterWidgetHistoryType
import com.tokopedia.notifcenter.view.adapter.NotifcenterWidgetHistoryType.FEED
import com.tokopedia.notifcenter.view.adapter.NotifcenterWidgetHistoryType.ORDER
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.time.TimeHelper
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class NotifcenterTimelineHistoryViewHolder constructor(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val title: Typography? = itemView.findViewById(R.id.notifcenter_tv_history_title)
    private val description: Typography? = itemView.findViewById(R.id.notifcenter_tv_history_description)
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
        bindDescription(trackHistory)
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
        val text = trackHistory.titleHtml.ifBlank {
            trackHistory.title
        }
        title?.text = setupHtmlUrls(
            text,
            MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_GN500)
        ) {
            RouteManager.route(itemView.context, it)
        }
        title?.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun bindDescription(trackHistory: TrackHistory) {
        description?.text = setupHtmlUrls(
            trackHistory.description,
            MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_GN500)
        ) {
            RouteManager.route(itemView.context, it)
        }
        description?.showWithCondition(trackHistory.description.isNotBlank())
    }

    private fun bindTime(trackHistory: TrackHistory) {
        if (trackHistory.createTimeUnixMillis > 0) {
            time?.text = TimeHelper.getRelativeTimeFromNow(trackHistory.createTimeUnixMillis)
            time?.show()
        } else {
            time?.hide()
        }
    }

    private fun bindTopLine(
        type: NotifcenterWidgetHistoryType,
        isFirstItem: Boolean,
        lastJourney: Boolean
    ) {
        when (type) {
            ORDER -> setTopLineOrder(isFirstItem, lastJourney)
            FEED -> setTopLineFeed()
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
            FEED -> bindBottomLineFeed()
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
