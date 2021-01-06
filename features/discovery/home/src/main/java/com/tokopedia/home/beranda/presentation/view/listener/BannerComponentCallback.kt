package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.model.ChannelGrid

class BannerComponentCallback (val context: Context?,
                               val homeCategoryListener: HomeCategoryListener): BannerComponentListener {
    override fun onBannerClickListener(position: Int, channelGrid: ChannelGrid) {

    }

    override fun isMainViewVisible(): Boolean {
        return homeCategoryListener.isMainViewVisible
    }

    override fun onPromoScrolled(channelGrid: ChannelGrid) {

    }

    override fun onPageDragStateChanged(isDrag: Boolean) {
        homeCategoryListener.onPageDragStateChanged(isDrag)
    }

    override fun onPromoAllClick(applink: String) {

    }
}