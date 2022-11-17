package com.tokopedia.mvc.presentation.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.VoucherCreationQuota
import com.tokopedia.mvc.domain.entity.VoucherListParam
import com.tokopedia.mvc.domain.entity.VoucherSort.Companion.VOUCHER_STATUS
import com.tokopedia.mvc.domain.entity.VoucherStatus.Companion.HISTORY
import com.tokopedia.mvc.domain.entity.VoucherStatus.Companion.NOT_STARTED_AND_ONGOING
import com.tokopedia.mvc.domain.usecase.GetVoucherListUseCase
import com.tokopedia.mvc.domain.usecase.GetVoucherQuotaUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class MvcListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getVoucherListUseCase: GetVoucherListUseCase,
    private val getVoucherQuotaUseCase: GetVoucherQuotaUseCase
) : BaseViewModel(dispatchers.main) {

    private val _voucherList = MutableLiveData<List<Voucher>>()
    val voucherList: LiveData<List<Voucher>> get() = _voucherList

    private val _voucherQuota = MutableLiveData<VoucherCreationQuota>()
    val voucherQuota: LiveData<VoucherCreationQuota> get() = _voucherQuota

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    fun getVoucherList(keyword: String, page: Int, pageSize: Int) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = VoucherListParam.createParam(
                    type = null,
                    status = NOT_STARTED_AND_ONGOING,
                    sort = VOUCHER_STATUS,
                    target = null,
                    page = page,
                    perPage = pageSize,
                    voucherName = keyword
                )
                _voucherList.postValue(getVoucherListUseCase.execute(param))
            },
            onError = {
                _error.postValue(it)
            }
        )
    }

    fun getVoucherQuota() {
        launchCatchError(
            dispatchers.io,
            block = {
                _voucherQuota.postValue(getVoucherQuotaUseCase.execute())
            },
            onError = {
                _error.postValue(it)
            }
        )
    }
}
