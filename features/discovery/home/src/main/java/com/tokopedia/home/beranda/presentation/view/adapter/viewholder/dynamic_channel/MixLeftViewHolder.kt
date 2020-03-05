package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mixleft.model.MixLeftAdapter

/**
 * @author by yoasfs on 2020-03-05
 */
class MixLeftViewHolder (itemView: View, val homeCategoryListener: HomeCategoryListener,
                         countDownListener: CountDownView.CountDownListener,
                         private val parentRecycledViewPool: RecyclerView.RecycledViewPool)
    : DynamicChannelViewHolder(itemView, homeCategoryListener, countDownListener) {

    private lateinit var adapter: MixLeftAdapter

    private val recyclerView: RecyclerView = itemView.findViewById(R.id.rv_product)
    private val alphaView: View = itemView.findViewById(R.id.viewholder_carousel_bg_parallax_alpha_view)
    private val image: ImageView = itemView.findViewById(R.id.parallax_image)
    private val parallaxBackground: View = itemView.findViewById(R.id.parallax_background)



    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_dc_mix_left
    }

    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        adapter = MixLeftAdapter()
    }

    override fun getViewHolderClassName(): String {
        return ""
    }

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {
        
    }

}
