package com.tokopedia.home.beranda.presentation.view.viewmodel

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
        val position: Int
): ImpressHolder(), Visitable<HomeFeedTypeFactory> {
    val DATA_ID = "id"
    val DATA_NAME = "name"
    val DATA_CREATIVE = "creative"
    val DATA_CREATIVE_URL = "creative_url"
    val DATA_POSITION = "position"
    val DATA_PROMO_ID = "promo_id"
    val DATA_PROMO_CODE = "promo_code"
    override fun type(typeFactory: HomeFeedTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        val LAYOUT = R.layout.home_feed_banner
    }

    fun convertBannerFeedModelToDataLayer(): Any {
        return DataLayer.mapOf(
                DATA_ID, id,
                DATA_NAME, name,
                DATA_CREATIVE, buAttribution+"_"+creativeName,
                DATA_CREATIVE_URL, imageUrl,
                DATA_POSITION, position,
                DATA_PROMO_ID, "",
                DATA_PROMO_CODE, ""
        )
    }
}