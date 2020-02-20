package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeFeedTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

class BannerFeedViewModel(
        val id: Int,
        val name: String,
        val imageUrl: String,
        val url: String,
        val applink: String,
        val buAttribution: String,
        val creativeName: String,
        val target: String,
        val position: Int,
        val galaxyAttribution: String,
        val affinityLabel: String,
        val shopId: String,
        val categoryPersona: String
): ImpressHolder(), Visitable<HomeFeedTypeFactory> {
    override fun type(typeFactory: HomeFeedTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        val LAYOUT = R.layout.home_feed_banner
    }
}