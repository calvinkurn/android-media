package com.tokopedia.shop.campaign.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.campaign.domain.entity.ExclusiveLaunchVoucher
import com.tokopedia.shop.campaign.domain.usecase.GetMerchantVoucherListUseCase
import com.tokopedia.shop.campaign.domain.usecase.GetPromoVoucherListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.async
import javax.inject.Inject

class ExclusiveLaunchVoucherListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getMerchantVoucherListUseCase: GetMerchantVoucherListUseCase,
    private val getPromoVoucherListUseCase: GetPromoVoucherListUseCase
) : BaseViewModel(dispatchers.main) {

    private val _vouchers = MutableLiveData<Result<List<ExclusiveLaunchVoucher>>>()
    val vouchers: LiveData<Result<List<ExclusiveLaunchVoucher>>>
        get() = _vouchers

    fun getExclusiveLaunchVouchers() {
        launchCatchError(
            dispatchers.io,
            block = {
                val merchantVouchers = async { getMerchantVoucherListUseCase.execute() }

                val param = GetPromoVoucherListUseCase.Param(categorySlug = "", categorySlugs = listOf("FMCG523"))
                val promoVouchers = async { getPromoVoucherListUseCase.execute(param) }

                val exclusiveLaunchVouchers = merchantVouchers.await() + promoVouchers.await()
                _vouchers.postValue(Success(exclusiveLaunchVouchers))
            },
            onError = { throwable ->
                _vouchers.postValue(Fail(throwable))
            }
        )
    }

}


