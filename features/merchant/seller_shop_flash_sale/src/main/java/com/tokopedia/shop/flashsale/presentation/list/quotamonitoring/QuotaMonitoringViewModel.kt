package com.tokopedia.shop.flashsale.presentation.list.quotamonitoring

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.common.constant.Constant.SELLER_QUOTA_SOURCE_EXPIRING_DAY_RANGE
import com.tokopedia.shop.flashsale.common.extension.daysDifference
import com.tokopedia.shop.flashsale.common.extension.epochToDate
import com.tokopedia.shop.flashsale.domain.entity.VpsPackageAvailability
import com.tokopedia.shop.flashsale.domain.entity.VpsPackage
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignPackageListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import java.util.Date
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

    fun getPackageAvailability(packages: List<VpsPackage>): VpsPackageAvailability {
        var totalQuota = Constant.ZERO
        var totalRemainingQuota = Constant.ZERO
        var isNearExpirePackageAvailable = false
        var packageNearExpireCount = Constant.ZERO

        packages.forEach { vpsPackage ->
            totalQuota += vpsPackage.originalQuota
            totalRemainingQuota += vpsPackage.remainingQuota
            if (vpsPackage.packageEndTime.epochToDate()
                    .daysDifference(Date()) <= SELLER_QUOTA_SOURCE_EXPIRING_DAY_RANGE
            ) {
                isNearExpirePackageAvailable = true
                packageNearExpireCount++
            }
        }

        return VpsPackageAvailability(
            totalQuota = totalQuota,
            remainingQuota = totalRemainingQuota,
            isNearExpirePackageAvailable = isNearExpirePackageAvailable,
            packageNearExpire = packageNearExpireCount
        )
    }
}