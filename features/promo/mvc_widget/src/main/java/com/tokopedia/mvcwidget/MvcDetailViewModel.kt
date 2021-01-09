package com.tokopedia.mvcwidget

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.mvcwidget.usecases.CatalogMVCListUseCase
import com.tokopedia.mvcwidget.usecases.MembershipRegisterUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class MvcDetailViewModel @Inject constructor(@Named(IO) workerDispatcher: CoroutineDispatcher,
                                             val catalogMVCListUseCase: CatalogMVCListUseCase,
                                             val membershipRegisterUseCase: MembershipRegisterUseCase,
) : BaseViewModel(workerDispatcher) {

    val listLiveData = SingleLiveData<LiveDataResult<TokopointsCatalogMVCListResponse>>()
    val membershipLiveData = SingleLiveData<LiveDataResult<MembershipRegisterResponse>>()

    fun getListData(shopId: Int) {
        launchCatchError(block = {
            listLiveData.postValue(LiveDataResult.loading())
            val response = catalogMVCListUseCase.getResponse(catalogMVCListUseCase.getQueryParams(shopId))
            if (response != null) {
                listLiveData.postValue(LiveDataResult.success(response))
            } else {
                listLiveData.postValue(LiveDataResult.error(Exception("Response is null")))
            }
        }, onError = {
            listLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun registerMembership(cardId: Int, referenceId: String, name: String, source: Int) {
        launchCatchError(block = {
            membershipLiveData.postValue(LiveDataResult.loading())
            val response = membershipRegisterUseCase.getResponse(
                    membershipRegisterUseCase.getQueryParams(cardId, referenceId, name, source))
            membershipLiveData.postValue(LiveDataResult.success(response))
        }, onError = {
            membershipLiveData.postValue(LiveDataResult.error(it))
        })
    }
}