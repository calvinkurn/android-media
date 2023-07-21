package com.tokopedia.shop.campaign.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.campaign.domain.entity.ExclusiveLaunchVoucher
import com.tokopedia.shop.campaign.domain.entity.RedeemPromoVoucherResult
import com.tokopedia.shop.campaign.domain.usecase.GetPromoVoucherListUseCase
import com.tokopedia.shop.campaign.domain.usecase.RedeemPromoVoucherUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class ExclusiveLaunchVoucherListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getPromoVoucherListUseCase: GetPromoVoucherListUseCase,
    private val redeemPromoVoucherUseCase: RedeemPromoVoucherUseCase
) : BaseViewModel(dispatchers.main) {

    private val _vouchers = MutableLiveData<Result<List<ExclusiveLaunchVoucher>>>()
    val vouchers: LiveData<Result<List<ExclusiveLaunchVoucher>>>
        get() = _vouchers

    private val _redeemResult = MutableLiveData<Result<RedeemPromoVoucherResult>>()
    val redeemResult: LiveData<Result<RedeemPromoVoucherResult>>
        get() = _redeemResult

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

    fun claimPromoVoucher(voucherId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = RedeemPromoVoucherUseCase.Param(catalogId = voucherId, isGift = 0)
                val redeemResult = redeemPromoVoucherUseCase.execute(param)
                _redeemResult.postValue(Success(redeemResult))
            },
            onError = { throwable ->
                _redeemResult.postValue(Fail(throwable))
            }
        )
    }
}


