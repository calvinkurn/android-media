package com.tokopedia.mvcwidget

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.mvcwidget.usecases.CatalogMVCListUseCase
import com.tokopedia.mvcwidget.usecases.FollowShopUseCase
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import com.tokopedia.mvcwidget.usecases.MembershipRegisterUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

const val ERROR_MSG = "Oops, ada sedikit gangguan. Coba daftar lagi, ya."
const val ERROR_NULL_RESPONSE = "Response is null"

class MvcDetailViewModel @Inject constructor(@Named(IO) workerDispatcher: CoroutineDispatcher,
                                             val catalogMVCListUseCase: CatalogMVCListUseCase,
                                             val membershipRegisterUseCase: MembershipRegisterUseCase,
                                             val mvcSummaryUseCase: MVCSummaryUseCase,
                                             val followUseCase: FollowShopUseCase
) : BaseViewModel(workerDispatcher) {

    val listLiveData = SingleLiveEvent<LiveDataResult<TokopointsCatalogMVCListResponse>>()
    val membershipLiveData = SingleLiveEvent<LiveDataResult<String>>()
    val mvcSummatLiveData = SingleLiveEvent<LiveDataResult<TokopointsCatalogMVCSummaryResponse>>()
    val followLiveData = SingleLiveEvent<LiveDataResult<String>>()
    var membershipCardID: String? = null
    var shopId: String = ""
    var membershipRegistrationSuccessMessage = ""

    fun getListData(shopId: String) {
        this.shopId = shopId
        launchCatchError(block = {
            listLiveData.postValue(LiveDataResult.loading())
            val response = catalogMVCListUseCase.getResponse(catalogMVCListUseCase.getQueryParams(shopId))
            membershipRegistrationSuccessMessage = response?.data?.toasterSuccessMessage ?: ""
            if (response != null) {
                membershipCardID = response.data?.followWidget?.membershipCardID
                listLiveData.postValue(LiveDataResult.success(response))
            } else {
                listLiveData.postValue(LiveDataResult.error(Exception(ERROR_NULL_RESPONSE)))
            }
        }, onError = {
            listLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun registerMembership() {

        launchCatchError(block = {
            membershipLiveData.postValue(LiveDataResult.loading())
            val response = membershipRegisterUseCase.getResponse(
                    membershipRegisterUseCase.getQueryParams(membershipCardID))
            if (response?.data?.resultStatus?.code == "200") {
                membershipLiveData.postValue(LiveDataResult.success(membershipRegistrationSuccessMessage))
                getMvcSummary()
                getListData(shopId)
            } else {
                membershipLiveData.postValue(LiveDataResult.error(Exception(ERROR_MSG)))
            }
        }, onError = {
            membershipLiveData.postValue(LiveDataResult.error(Exception(ERROR_MSG)))
        })
    }

    fun getMvcSummary(){
        launchCatchError(block = {
            val result = mvcSummaryUseCase.getResponse(mvcSummaryUseCase.getQueryParams(shopId))
            if(result?.data?.resultStatus?.code == "200"){
                mvcSummatLiveData.postValue(LiveDataResult.success(result))
            }else{
                mvcSummatLiveData.postValue(LiveDataResult.error(Exception(ERROR_MSG)))
            }
        },onError = {
            mvcSummatLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun followShop() {
        launchCatchError(block = {
            followLiveData.postValue(LiveDataResult.loading())
            val response = followUseCase.getResponse(followUseCase.getQueryParams(shopId))
            val success = response?.followShop?.success
            if (success == true) {
                followLiveData.postValue(LiveDataResult.success(membershipRegistrationSuccessMessage))
                getListData(shopId)
            } else {
                followLiveData.postValue(LiveDataResult.error(Exception(ERROR_MSG)))
            }
        }, onError = {
            followLiveData.postValue(LiveDataResult.error(Exception(ERROR_MSG)))
        })
    }
}