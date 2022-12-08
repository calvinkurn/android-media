package com.tokopedia.mvc.presentation.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.VoucherCreationQuota
import com.tokopedia.mvc.domain.entity.VoucherListParam
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
import com.tokopedia.mvc.domain.entity.enums.VoucherSort
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.usecase.GetVoucherListChildUseCase
import com.tokopedia.mvc.domain.usecase.GetVoucherListUseCase
import com.tokopedia.mvc.domain.usecase.GetVoucherQuotaUseCase
import com.tokopedia.mvc.presentation.list.helper.MvcListPageStateHelper
import com.tokopedia.mvc.domain.usecase.*
import com.tokopedia.mvc.domain.usecase.CancelVoucherUseCase.CancelStatus.Companion.DELETE
import com.tokopedia.mvc.domain.usecase.CancelVoucherUseCase.CancelStatus.Companion.STOP
import com.tokopedia.mvc.presentation.list.model.DeleteVoucherUiEffect
import com.tokopedia.mvc.presentation.list.model.FilterModel
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class MvcListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getVoucherListUseCase: GetVoucherListUseCase,
    private val getVoucherQuotaUseCase: GetVoucherQuotaUseCase,
    private val getVoucherListChildUseCase: GetVoucherListChildUseCase,
    private val cancelVoucherUseCase: CancelVoucherUseCase,
    private val getInitiateVoucherPageUseCase: GetInitiateVoucherPageUseCase,
    ) : BaseViewModel(dispatchers.main) {

    private val _voucherList = MutableLiveData<List<Voucher>>()
    val voucherList: LiveData<List<Voucher>> get() = _voucherList

    private val _voucherChilds = MutableLiveData<List<Voucher>>()
    val voucherChilds: LiveData<List<Voucher>> get() = _voucherChilds

    private val _voucherQuota = MutableLiveData<VoucherCreationQuota>()
    val voucherQuota: LiveData<VoucherCreationQuota> get() = _voucherQuota

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    val pageState = Transformations.map(voucherList) {
        MvcListPageStateHelper.getPageState(it, filter)
    }

    private val _deleteUIEffect = MutableSharedFlow<DeleteVoucherUiEffect>(replay = 1)
    val deleteUIEffect = _deleteUIEffect.asSharedFlow()

    var filter = FilterModel()

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
                    voucherName = filter.keyword,
                    type = filter.promoType.firstOrNull(),
                    status = filter.status,
                    sort = VoucherSort.VOUCHER_STATUS,
                    target = filter.target,
                    voucherType = filter.voucherType,
                    page = page,
                    perPage = pageSize,
                    targetBuyer = filter.targetBuyer,
                    source = filter.source
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

    fun getVoucherListChild(voucherId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getVoucherListChildUseCase.execute(voucherId)
                _voucherChilds.postValue(result)
            },
            onError = {
                _error.postValue(it)
            }
        )
    }

    fun getFilterCount(): Int {
        var count = 0
        filter.apply {
            if (status.isNotEmpty()) count = count.inc()
            if (voucherType.isNotEmpty()) count = count.inc()
            if (promoType.isNotEmpty()) count = count.inc()
            if (source.isNotEmpty()) count = count.inc()
            if (target.isNotEmpty()) count = count.inc()
            if (targetBuyer.isNotEmpty()) count = count.inc()
        }
        return count
    }

    fun stopVoucher(voucher: Voucher){
        val voucherStatus = voucher.status
        launchCatchError(
            dispatchers.io,
            block = {
                _deleteUIEffect.emit(DeleteVoucherUiEffect.OnProgressToDeletedVoucherList)
                val metadataParam = GetInitiateVoucherPageUseCase.Param(VoucherAction.UPDATE, PromoType.DISCOUNT, isVoucherProduct = true)
                val metadataDeferred = async { getInitiateVoucherPageUseCase.execute(metadataParam)}
                val token = metadataDeferred.await()
                val couponStatus = if(voucherStatus == VoucherStatus.NOT_STARTED) DELETE else STOP
                val idCancelVoucher = cancelVoucherUseCase.execute(voucher.id.toInt(), couponStatus, token.token)
                if(!idCancelVoucher.isZero()){
                    _deleteUIEffect.emit(DeleteVoucherUiEffect.SuccessDeletedVoucher(idCancelVoucher, voucher.name, voucherStatus))
                }
            },
            onError = {error ->
                _deleteUIEffect.emit(DeleteVoucherUiEffect.ShowToasterErrorDelete(error, voucher.name, voucherStatus))
            }
        )
    }
}
