package com.tokopedia.tokomember

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tokomember.usecase.TokomemberUsecase
import com.tokopedia.tokomember.util.IO
import com.tokopedia.tokomember.util.LiveDataResult
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class TokomemberBotttomSheetViewModel @Inject constructor(@Named(IO) workerDispatcher: CoroutineDispatcher,
                                             val tmBottomSheetUsecase: TokomemberUsecase
) : BaseViewModel(workerDispatcher) {

    val listLiveData = SingleLiveEvent<LiveDataResult<Any>>()
    var membershipCardID: String? = null
    var shopId: String = ""
    var membershipRegistrationSuccessMessage = ""

    fun getListData(shopId: String) {
        this.shopId = shopId
        launchCatchError(block = {
            listLiveData.postValue(LiveDataResult.loading())
            val response = tmBottomSheetUsecase.getTmBottomSheetData()
            //membershipRegistrationSuccessMessage = response?.data?.toasterSuccessMessage ?: ""
            if (response != null) {
               // membershipCardID = response.data?.followWidget?.membershipCardID
                listLiveData.postValue(LiveDataResult.success(response))
            } else {
               // listLiveData.postValue(LiveDataResult.error(Exception(ERROR_NULL_RESPONSE)))
            }
        }, onError = {
            listLiveData.postValue(LiveDataResult.error(it))
        })
    }
}