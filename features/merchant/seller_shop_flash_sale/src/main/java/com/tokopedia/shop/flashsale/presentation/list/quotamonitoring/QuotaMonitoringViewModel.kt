package com.tokopedia.shop.flashsale.presentation.list.quotamonitoring

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flashsale.domain.entity.VpsPackage
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignPackageListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class QuotaMonitoringViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSellerCampaignPackageListUseCase: GetSellerCampaignPackageListUseCase,
) : BaseViewModel(dispatchers.main) {

    private val _vpsPackages = MutableLiveData<Result<List<VpsPackage>>>()
    val vpsPackages: LiveData<Result<List<VpsPackage>>>
        get() = _vpsPackages


    fun getVpsPackages() {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getSellerCampaignPackageListUseCase.execute()
                _vpsPackages.postValue(Success(result))
            },
            onError = { error ->
                _vpsPackages.postValue(Fail(error))
            }
        )
    }
}