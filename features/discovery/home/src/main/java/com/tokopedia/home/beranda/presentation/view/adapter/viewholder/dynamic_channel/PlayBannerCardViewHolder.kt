package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCarouselCardDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.PlayBannerCarousel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class PlayBannerCardViewHolder(
        val view: View,
        val listener: HomeCategoryListener
): AbstractViewHolder<PlayCarouselCardDataModel>(view), CoroutineScope {

    private val masterJob = Job()

    companion object {
        @LayoutRes val LAYOUT = R.layout.play_banner_carousel
    }



    override val coroutineContext: CoroutineContext
        get() = masterJob + Dispatchers.IO

    override fun bind(element: PlayCarouselCardDataModel?) {
        val playCarousel = view.findViewById<PlayBannerCarousel>(R.id.play_banner_carousel)
        element?.playBannerCarouselDataModel?.let { playCarousel?.setItem(it) }
    }

}
