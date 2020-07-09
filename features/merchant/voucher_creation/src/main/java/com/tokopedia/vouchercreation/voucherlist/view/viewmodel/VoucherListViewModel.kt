package com.tokopedia.vouchercreation.voucherlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.common.domain.usecase.CancelVoucherUseCase
import com.tokopedia.vouchercreation.detail.domain.usecase.VoucherDetailUseCase
import com.tokopedia.vouchercreation.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.voucherlist.domain.model.VoucherListParam
import com.tokopedia.vouchercreation.voucherlist.domain.model.VoucherSort
import com.tokopedia.vouchercreation.voucherlist.domain.model.VoucherStatus
import com.tokopedia.vouchercreation.voucherlist.domain.usecase.GetVoucherListUseCase
import com.tokopedia.vouchercreation.voucherlist.domain.usecase.ShopBasicDataUseCase
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class VoucherListViewModel @Inject constructor(
        private val getVoucherListUseCase: GetVoucherListUseCase,
        private val cancelVoucherUseCase: CancelVoucherUseCase,
        private val shopBasicDataUseCase: ShopBasicDataUseCase,
        private val voucherDetailUseCase: VoucherDetailUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val notStartedVoucherRequestParam by lazy {
        VoucherListParam.createParam(status = VoucherStatus.NOT_STARTED)
    }

    private val ongoingVoucherRequestParam by lazy {
        VoucherListParam.createParam(status = VoucherStatus.ONGOING)
    }

    private val _keywordLiveData = MutableLiveData<String>()

    private val _cancelledVoucherLiveData = MutableLiveData<Int>()
    private val _stoppedVoucherLiveData = MutableLiveData<Int>()
    private val _successCreatedVoucherIdLiveData = MutableLiveData<Int>()

    private val _voucherList = MutableLiveData<Result<List<VoucherUiModel>>>()
    val voucherList: LiveData<Result<List<VoucherUiModel>>>
        get() = _voucherList

    private val _localVoucherListLiveData = MediatorLiveData<Result<List<VoucherUiModel>>>().apply {
        addSource(_keywordLiveData) { keyword ->
            searchVoucherByKeyword(keyword)
        }
    }
    val localVoucherListLiveData: LiveData<Result<List<VoucherUiModel>>>
        get() = _localVoucherListLiveData

    private val _fullVoucherListLiveData = MutableLiveData<MutableList<VoucherUiModel>>().apply { value = mutableListOf() }

    private val _cancelVoucherResponseLiveData = MediatorLiveData<Result<Int>>().apply {
        addSource(_cancelledVoucherLiveData) { id ->
            launchCatchError(
                    block = {
                        cancelVoucherUseCase.params = CancelVoucherUseCase.createRequestParam(id, CancelVoucherUseCase.CancelStatus.DELETE)
                        value = Success(withContext(Dispatchers.IO) {
                            cancelVoucherUseCase.executeOnBackground()
                        })
                    },
                    onError = {
                        value = Fail(it)
                    }
            )
        }
    }
    val cancelVoucherResponseLiveData: LiveData<Result<Int>>
        get() = _cancelVoucherResponseLiveData

    private val _stopVoucherResponseLiveData = MediatorLiveData<Result<Int>>().apply {
        addSource(_stoppedVoucherLiveData) { id ->
            launchCatchError(
                    block = {
                        cancelVoucherUseCase.params = CancelVoucherUseCase.createRequestParam(id, CancelVoucherUseCase.CancelStatus.STOP)
                        value = Success(withContext(Dispatchers.IO) {
                            cancelVoucherUseCase.executeOnBackground()
                        })
                    },
                    onError = {
                        value = Fail(it)
                    }
            )
        }
    }
    val stopVoucherResponseLiveData: LiveData<Result<Int>>
        get() = _stopVoucherResponseLiveData

    private val _successVoucherLiveData = MediatorLiveData<Result<VoucherUiModel>>().apply {
        addSource(_successCreatedVoucherIdLiveData) { id ->
            launchCatchError(
                    block = {
                        voucherDetailUseCase.params = VoucherDetailUseCase.createRequestParam(id)
                        value = Success(withContext(Dispatchers.IO) {
                            voucherDetailUseCase.executeOnBackground()
                        })
                    },
                    onError = {
                        value = Fail(it)
                    }
            )
        }
    }
    val successVoucherLiveData: LiveData<Result<VoucherUiModel>>
        get() = _successVoucherLiveData

    private val _shopBasicLiveData = MutableLiveData<Result<ShopBasicDataResult>>()
    val shopBasicLiveData: LiveData<Result<ShopBasicDataResult>>
        get() = _shopBasicLiveData

    fun getActiveVoucherList(isFirstTime: Boolean) {
        launchCatchError(block = {
            if (isFirstTime) {
                _shopBasicLiveData.value = Success(withContext(Dispatchers.IO) {
                    shopBasicDataUseCase.executeOnBackground()
                })
            }
            _voucherList.value = Success(withContext(Dispatchers.IO) {
                mutableListOf<VoucherUiModel>().apply {
                    getVoucherListUseCase.params = GetVoucherListUseCase.createRequestParam(ongoingVoucherRequestParam)
                    addAll(getVoucherListUseCase.executeOnBackground())
                    getVoucherListUseCase.params = GetVoucherListUseCase.createRequestParam(notStartedVoucherRequestParam)
                    addAll(getVoucherListUseCase.executeOnBackground())
                }
            })
        }, onError = {
            _voucherList.value = Fail(it)
        })
    }

    fun getVoucherListHistory(@VoucherTypeConst type: Int?,
                              targetList: List<Int>?,
                              @VoucherSort sort: String?,
                              page: Int,
                              isInverted: Boolean) {
        launchCatchError(block = {
            getVoucherListUseCase.params = GetVoucherListUseCase.createRequestParam(
                    VoucherListParam.createParam(
                            status = VoucherStatus.HISTORY,
                            type = type,
                            targetList = targetList,
                            sort = sort,
                            page = page,
                            isInverted = isInverted)
            )
            withContext(Dispatchers.IO) {
                val voucherList = getVoucherListUseCase.executeOnBackground()
                if (page == 1) {
                    _fullVoucherListLiveData.value?.clear()
                }
                _fullVoucherListLiveData.value?.addAll(voucherList)
                _voucherList.postValue(Success(voucherList))
            }
        }, onError = {
            _voucherList.value = Fail(it)
        })
    }

    fun setSearchKeyword(keyword: String) {
        _keywordLiveData.value = keyword
    }

    fun cancelVoucher(voucherId: Int,
                      isCancel: Boolean) {
        if (isCancel) {
            _cancelledVoucherLiveData.value = voucherId
        } else {
            _stoppedVoucherLiveData.value = voucherId
        }
    }

    fun getSuccessCreatedVoucher(voucherId: Int) {
        _successCreatedVoucherIdLiveData.value = voucherId
    }

    private fun searchVoucherByKeyword(keyword: String) {
        launchCatchError(block = {
            _localVoucherListLiveData.value = Success(
                    _fullVoucherListLiveData.value?.filter {
                        it.name.contains(keyword, true) } ?: listOf())
        }, onError = {
            _localVoucherListLiveData.value = Fail(it)
        })
    }
}