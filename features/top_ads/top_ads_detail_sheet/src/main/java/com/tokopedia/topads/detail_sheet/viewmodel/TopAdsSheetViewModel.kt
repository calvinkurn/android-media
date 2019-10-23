package com.tokopedia.topads.detail_sheet.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topads.detail_sheet.data.model.GroupProduct
import com.tokopedia.topads.detail_sheet.data.source.TopAdsSheetRepository
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/**
 * Author errysuprayogi on 22,October,2019
 */
class TopAdsSheetViewModel @Inject constructor(val topAdsSheetRepository: TopAdsSheetRepository,
                                               val dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher) {

    val groupProduct = MutableLiveData<Result<GroupProduct>>()

    fun getGroupProduct(adId: String, shopId: String, startDate: String, endDate: String) {
//        launchCatchError(block = {
//            val data = withContext(Dispatchers.IO){
//                topAdsSheetRepository.getGroupProduct(adId, shopId, startDate, endDate)
//            }
//            groupProduct.postValue(data)
//        }){
//            it.printStackTrace()
//        }
    }
}
