package com.tokopedia.mvc.presentation.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.VoucherCreationQuota
import com.tokopedia.mvc.domain.entity.VoucherListParam
import com.tokopedia.mvc.domain.entity.enums.VoucherSort
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.usecase.GetVoucherListUseCase
import com.tokopedia.mvc.domain.usecase.GetVoucherQuotaUseCase
import com.tokopedia.mvc.presentation.list.model.FilterModel
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

    private var filter = FilterModel()

    fun setFilterKeyword(keyword: String) {
        filter = filter.copy(
            keyword = keyword
        )
    }

    fun setFilterStatus(status: List<VoucherStatus>) {
        filter = filter.copy(
            status = status.toMutableList()
        )
    }

    fun getVoucherList(page: Int, pageSize: Int) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = VoucherListParam.createParam(
                    type = null,
                    status = filter.status,
                    sort = VoucherSort.VOUCHER_STATUS,
                    target = null,
                    page = page,
                    perPage = pageSize,
                    voucherName = filter.keyword
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
