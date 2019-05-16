package com.tokopedia.topads.auto.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

import com.tokopedia.topads.auto.base.AutoAdsViewModel
import com.tokopedia.topads.auto.data.network.response.TopAdsAutoAdsResponse
import com.tokopedia.topads.auto.data.repository.AutoAdsRepository
import com.tokopedia.topads.auto.data.repository.AutoTopAdsRepositoy
import kotlinx.coroutines.experimental.GlobalScope

/**
 * Author errysuprayogi on 15,May,2019
 */
class AutoAdsWidgetViewModel(
        private val autoAdsRepository: AutoTopAdsRepositoy
) : AutoAdsViewModel(autoAdsRepository) {


    public fun getShopInfo(shopId: Int){

    }
}
