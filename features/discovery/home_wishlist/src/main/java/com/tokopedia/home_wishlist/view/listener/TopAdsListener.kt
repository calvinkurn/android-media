package com.tokopedia.home_wishlist.view.listener

import com.tokopedia.home_wishlist.model.datamodel.BannerTopAdsDataModel
import com.tokopedia.smart_recycler_helper.SmartListener

/**
 * Created by Lukas on 27/07/20.
 */

interface TopAdsListener : SmartListener {
    fun onBannerTopAdsClick(item: BannerTopAdsDataModel, position: Int)
    fun onBannerTopAdsImpress(item: BannerTopAdsDataModel, position: Int)
}