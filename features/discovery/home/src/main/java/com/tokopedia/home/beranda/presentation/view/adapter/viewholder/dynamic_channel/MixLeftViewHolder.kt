package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.listener.HomeCategoryListener

/**
 * @author by yoasfs on 2020-03-05
 */
class MixLeftViewHolder (itemView: View, val homeCategoryListener: HomeCategoryListener,
                         countDownListener: CountDownView.CountDownListener,
                         private val parentRecycledViewPool: RecyclerView.RecycledViewPool)
    : DynamicChannelViewHolder(itemView, homeCategoryListener, countDownListener) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_dc_mix_left
    }

    override fun setupContent(channel: DynamicHomeChannel.Channels) {
    }

    override fun getViewHolderClassName(): String {
        return ""
    }

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {
    }

}
