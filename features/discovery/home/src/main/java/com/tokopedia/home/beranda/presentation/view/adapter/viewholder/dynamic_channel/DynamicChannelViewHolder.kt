package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.TextView

import com.crashlytics.android.Crashlytics
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.DateHelper
import com.tokopedia.home.beranda.helper.DynamicLinkHelper
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.unifyprinciples.Typography

abstract class DynamicChannelViewHolder<T: RecyclerView.ViewHolder>(itemView: View,
                               private val listener: HomeCategoryListener,
                               private val countDownListener: CountDownView.CountDownListener) : AbstractViewHolder<DynamicChannelViewModel>(itemView) {

    protected val defaultSpanCount = 3

    private val context: Context = itemView.context

    lateinit var countDownView: CountDownView

    companion object {
        @LayoutRes
        val MASTER_LAYOUT_DC = R.layout.home_master_dynamic_channel
    }

    override fun bind(element: DynamicChannelViewModel) {
        try {
            val recyclerView: RecyclerView = itemView.findViewById(R.id.recycleList)
            val channelTitle: Typography = itemView.findViewById(R.id.channel_title)
            val seeAllButton: TextView = itemView.findViewById(R.id.see_all_button)
            val channelTitleContainer: View = itemView.findViewById(R.id.channel_title_container)
            countDownView = itemView.findViewById(R.id.count_down)

            val channel = element.channel
            val channelHeaderName = element.channel.header.name

            recyclerView.layoutManager = GridLayoutManager(itemView.context, defaultSpanCount,
                    GridLayoutManager.VERTICAL, false)
            /**
             * add decoration when recyclerview has no item decorator
             */
            if (recyclerView.itemDecorationCount == 0) recyclerView.addItemDecoration(getRecyclerViewDecorator())

            recyclerView.adapter = getItemAdapter(channel)
            /**
             * Requirement:
             * Only show channel header name when it is exist
             */
            if (!TextUtils.isEmpty(channelHeaderName)) {
                channelTitleContainer.visibility = View.VISIBLE
                channelTitle.text = channelHeaderName
            } else {
                channelTitleContainer.visibility = View.GONE
            }

            /**
             * Requirement:
             * Only show `see all` button when it is exist
             */
            if (!TextUtils.isEmpty(DynamicLinkHelper.getActionLink(channel.header))) {
                seeAllButton.visibility = View.VISIBLE
                seeAllButton.setOnClickListener {
                    listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(channel.header), channel.homeAttribution)
                    HomeTrackingUtils.homeDiscoveryWidgetViewAll(context,
                            DynamicLinkHelper.getActionLink(channel.header))
                    onSeeAllClickTracker(channel, DynamicLinkHelper.getActionLink(channel.getHeader()))
                }
            } else {
                seeAllButton.visibility = View.GONE
            }

            /**
             * Requirement:
             * Only show countDownView when expired time exist
             * Don't start countDownView when it is expired from backend (possibly caused infinite refresh)
             *  since onCountDownFinished would getting called and refresh home
             */
            if (hasExpiredTime(channel)) {
                val expiredTime = DateHelper.getExpiredTime(channel.header.expiredTime)
                if (!DateHelper.isExpired(element.serverTimeOffset, expiredTime)) {
                    countDownView.setup(element.serverTimeOffset, expiredTime, countDownListener)
                    countDownView.visibility = View.VISIBLE
                }
            } else {
                countDownView.visibility = View.GONE
            }
        } catch (e: Exception) {
            Crashlytics.log(0, getViewHolderClassName(), e.localizedMessage)
        }
    }

    protected abstract fun getItemAdapter(channel: DynamicHomeChannel.Channels): RecyclerView.Adapter<T>

    protected abstract fun getRecyclerViewDecorator(): RecyclerView.ItemDecoration

    protected abstract fun getViewHolderClassName(): String

    protected abstract fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String)

    private fun hasExpiredTime(channel: DynamicHomeChannel.Channels): Boolean {
        return channel.header.expiredTime != null && !TextUtils.isEmpty(channel.header.expiredTime)
    }
}
