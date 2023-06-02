package com.tokopedia.shop.campaign.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.campaign.domain.entity.ExclusiveLaunchVoucher
import com.tokopedia.shop.campaign.domain.usecase.GetPromoVoucherListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class ExclusiveLaunchVoucherListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getPromoVoucherListUseCase: GetPromoVoucherListUseCase
) : BaseViewModel(dispatchers.main) {

    private val _vouchers = MutableLiveData<Result<List<ExclusiveLaunchVoucher>>>()
    val vouchers: LiveData<Result<List<ExclusiveLaunchVoucher>>>
        get() = _vouchers

    fun getPromoVouchers(voucherSlugs: List<String>) {
        launchCatchError(
            dispatchers.io,
            block = {
                val vouchers = getPromoVoucherListUseCase.execute(voucherSlugs)
                _vouchers.postValue(Success(vouchers))
            },
            onError = { throwable ->
                _vouchers.postValue(Fail(throwable))
            }
        )
    }

    fun refreshVoucherClaimStatus(
        currentVouchers: List<ExclusiveLaunchVoucher>,
        voucherSlugs: List<String>
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val latestVoucherStatus = getPromoVoucherListUseCase.execute(voucherSlugs)
                val updatedVouchers = updateVoucherStatus(currentVouchers, latestVoucherStatus)
                _vouchers.postValue(Success(updatedVouchers))
            },
            onError = { throwable ->
                _vouchers.postValue(Fail(throwable))
            }
        )
    }
    private fun updateVoucherStatus(
        currentVouchers: List<ExclusiveLaunchVoucher>,
        latestVoucherStatus: List<ExclusiveLaunchVoucher>
    ): List<ExclusiveLaunchVoucher> {
        return currentVouchers.map { currentVoucher ->
            val latestVoucher = latestVoucherStatus.find { latestVoucher -> currentVoucher.id == latestVoucher.id }

            if (latestVoucher != null) {
                //If found matching voucher, return latest voucher status from remote instead
                latestVoucher
            } else {
                currentVoucher
            }
        }
    }

}


