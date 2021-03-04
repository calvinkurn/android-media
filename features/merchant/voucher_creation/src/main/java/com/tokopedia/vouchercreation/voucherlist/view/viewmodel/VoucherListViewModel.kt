package com.tokopedia.vouchercreation.voucherlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
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
import com.tokopedia.vouchercreation.voucherlist.domain.usecase.GetBroadCastMetaDataUseCase
import com.tokopedia.vouchercreation.voucherlist.domain.usecase.GetVoucherListUseCase
import com.tokopedia.vouchercreation.voucherlist.domain.usecase.ShopBasicDataUseCase
import com.tokopedia.vouchercreation.voucherlist.model.remote.ChatBlastSellerMetadata
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class VoucherListViewModel @Inject constructor(
        private val getVoucherListUseCase: GetVoucherListUseCase,
        private val getNotStartedVoucherListUseCase: GetVoucherListUseCase,
        private val cancelVoucherUseCase: CancelVoucherUseCase,
        private val shopBasicDataUseCase: ShopBasicDataUseCase,
        private val voucherDetailUseCase: VoucherDetailUseCase,
        private val getBroadCastMetaDataUseCase: GetBroadCastMetaDataUseCase,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private var showBroadCastChatTicker: Boolean = true

    private val bcTickerExpirationPeriod = 3 // months

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

    private val _localVoucherListLiveData = MediatorLiveData<List<VoucherUiModel>>().apply {
        addSource(_keywordLiveData) { keyword ->
            searchVoucherByKeyword(keyword)
        }
    }
    val localVoucherListLiveData: LiveData<List<VoucherUiModel>>
        get() = _localVoucherListLiveData

    private val _fullVoucherListLiveData = MutableLiveData<MutableList<VoucherUiModel>>().apply { value = mutableListOf() }

    private val _broadCastMetaData = MutableLiveData<Result<ChatBlastSellerMetadata>>()
    val broadCastMetadata: LiveData<Result<ChatBlastSellerMetadata>> get() = _broadCastMetaData

    private val _cancelVoucherResponseLiveData = MediatorLiveData<Result<Int>>().apply {
        addSource(_cancelledVoucherLiveData) { id ->
            launchCatchError(
                    block = {
                        cancelVoucherUseCase.params = CancelVoucherUseCase.createRequestParam(id, CancelVoucherUseCase.CancelStatus.DELETE)
                        value = Success(withContext(dispatchers.io) {
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
                        value = Success(withContext(dispatchers.io) {
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
                        value = Success(withContext(dispatchers.io) {
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
                _shopBasicLiveData.value = Success(withContext(dispatchers.io) {
                    shopBasicDataUseCase.executeOnBackground()
                })
            }
            _voucherList.value = Success(withContext(dispatchers.io) {
                getVoucherListUseCase.params = GetVoucherListUseCase.createRequestParam(ongoingVoucherRequestParam)
                getNotStartedVoucherListUseCase.params = GetVoucherListUseCase.createRequestParam(notStartedVoucherRequestParam)
                val ongoingVoucherList = async { getVoucherListUseCase.executeOnBackground() }
                val notStartedVoucherList = async { getNotStartedVoucherListUseCase.executeOnBackground() }
                ongoingVoucherList.await() + notStartedVoucherList.await()
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
            withContext(dispatchers.io) {
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
        _localVoucherListLiveData.value = _fullVoucherListLiveData.value?.filter {
            it.name.contains(keyword, true)
        } ?: listOf()
    }

    fun getBroadCastMetaData() {
        launchCatchError(block = {
            _broadCastMetaData.value = Success(withContext(dispatchers.io) {
                getBroadCastMetaDataUseCase.executeOnBackground()
            })
        }, onError = {
            _broadCastMetaData.value = Fail(it)
        })
    }

    fun setShowBroadCastChatTicker(showBroadCastChatTicker: Boolean) {
        this.showBroadCastChatTicker = showBroadCastChatTicker
    }

    fun getShowBroadCastChatTicker(): Boolean {
        return showBroadCastChatTicker
    }

    fun isBroadCastChatTickerExpired(firstTimeVisitLong: Long): Boolean {
        val firstTimeVisit = Date(firstTimeVisitLong)
        val calendar = Calendar.getInstance()
        calendar.time = firstTimeVisit
        calendar.add(Calendar.MONTH, bcTickerExpirationPeriod)
        // = 0 => equal
        // < 0 => current date is before the expiration date
        // > 0 => current date is after the expiration date
        return Date() >= calendar.time
    }
}