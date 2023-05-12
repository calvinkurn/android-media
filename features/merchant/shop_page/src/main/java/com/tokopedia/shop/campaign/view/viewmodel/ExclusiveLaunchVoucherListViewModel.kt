package com.tokopedia.shop.campaign.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.campaign.domain.entity.ExclusiveLaunchVoucher
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class ExclusiveLaunchVoucherListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
) : BaseViewModel(dispatchers.main) {

    private val _vouchers = MutableLiveData<Result<List<ExclusiveLaunchVoucher>>>()
    val vouchers: LiveData<Result<List<ExclusiveLaunchVoucher>>>
        get() = _vouchers

    fun getVouchers() {
        launchCatchError(dispatchers.io,
            block = {
                val vouchers = mutableListOf<ExclusiveLaunchVoucher>()
                repeat(30) {
                    vouchers.add(ExclusiveLaunchVoucher(it.toLong(), 20, 100_000, 300_000, 5, false))
                }
                _vouchers.postValue(Success(vouchers))
            },
            onError = { throwable ->
                _vouchers.postValue(Fail(throwable))
            }
        )
    }
}


