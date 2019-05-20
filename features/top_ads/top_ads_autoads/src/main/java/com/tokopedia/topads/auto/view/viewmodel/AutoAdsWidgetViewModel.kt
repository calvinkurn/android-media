package com.tokopedia.topads.auto.view.viewmodel

import com.tokopedia.topads.auto.base.AutoAdsViewModel
import com.tokopedia.topads.auto.data.repository.AutoTopAdsRepositoy

/**
 * Author errysuprayogi on 15,May,2019
 */
class AutoAdsWidgetViewModel(
        private val autoAdsRepository: AutoTopAdsRepositoy
) : AutoAdsViewModel(autoAdsRepository) {


    public fun getShopInfo(shopId: Int){

    }
}
