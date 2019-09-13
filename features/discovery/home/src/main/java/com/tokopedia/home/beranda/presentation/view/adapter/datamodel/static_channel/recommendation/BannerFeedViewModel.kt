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
        val position: Int
): ImpressHolder(), Visitable<HomeFeedTypeFactory> {
    private val DATA_ID = "id"
    private val DATA_NAME = "name"
    private val DATA_CREATIVE = "creative"
    private val DATA_CREATIVE_URL = "creative_url"
    private val DATA_POSITION = "position"
    private val DATA_PROMO_ID = "promo_id"
    private val DATA_PROMO_CODE = "promo_code"
    val VALUE_BANNER_NAME = "/ - banner inside recom tab - %s - "

    override fun type(typeFactory: HomeFeedTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        val LAYOUT = R.layout.home_feed_banner
    }

    fun convertBannerFeedModelToDataLayer(tabName: String): Any {
        return DataLayer.mapOf(
                DATA_ID, id.toString(),
                DATA_NAME, String.format(VALUE_BANNER_NAME, tabName),
                DATA_CREATIVE, buAttribution+"_"+creativeName,
                DATA_CREATIVE_URL, imageUrl,
                DATA_POSITION, position.toString(),
                DATA_PROMO_ID, "",
                DATA_PROMO_CODE, ""
        )
    }
}