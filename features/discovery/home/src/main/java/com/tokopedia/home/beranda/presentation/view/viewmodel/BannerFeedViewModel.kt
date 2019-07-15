package com.tokopedia.home.beranda.presentation.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeFeedTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

class BannerFeedViewModel(
        val id: Int,
        val name: String,
        val imageUrl: String,
        val url: String,
        val applink: String,
        val buAttribution: String,
        val creativeName: String,
        val target: String
): ImpressHolder(), Visitable<HomeFeedTypeFactory> {
    override fun type(typeFactory: HomeFeedTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        val LAYOUT = R.layout.home_feed_banner
    }
}