package com.tokopedia.vouchercreation.shop.voucherlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.common.consts.GqlQueryConstant.GET_INIT_VOUCHER_ELIGIBILITY_QUERY
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.common.domain.usecase.CancelVoucherUseCase
import com.tokopedia.vouchercreation.common.domain.usecase.InitiateVoucherUseCase
import com.tokopedia.vouchercreation.shop.create.view.uimodel.initiation.InitiateVoucherUiModel
import com.tokopedia.vouchercreation.shop.detail.domain.usecase.VoucherDetailUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.*
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.GetBroadCastMetaDataUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.GetVoucherListUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.ShopBasicDataUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.model.remote.ChatBlastSellerMetadata
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel
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
        private val initiateVoucherUseCase: InitiateVoucherUseCase,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    // voucher filter properties
    @VoucherTypeConst
    var voucherType: Int? = null
    var voucherTarget: List<Int>? = null
    var isSellerCreated: Boolean? = null
    var isSubsidy: Boolean? = null
    var isVps: Boolean? = null
    var keyword: String? = null
    var targetBuyer: String? = null

    // voucher sort properties
    @VoucherSort
    var voucherSort: String = VoucherSort.FINISH_TIME
    var isInverted: Boolean = false
    var isSortApplied: Boolean = false

    var isEligibleToCreateVoucher = false
    var currentPage: Int = 1

    private var showBroadCastChatTicker: Boolean = true
    private var isFreeBroadCastIconVisible: Boolean = false
    private var isSuccessDialogDisplayed: Boolean = false

    private val bcTickerExpirationPeriod = 3 // months
    private val isUpdate = false

    private val _keywordLiveData = MutableLiveData<String>()

    private val _cancelledVoucherLiveData = MutableLiveData<Int>()
    private val _stoppedVoucherLiveData = MutableLiveData<Int>()
    private val _successCreatedVoucherIdLiveData = MutableLiveData<Int>()

    private val _voucherList = MutableLiveData<Result<List<VoucherUiModel>>>()
    val voucherList: LiveData<Result<List<VoucherUiModel>>>
        get() = _voucherList

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

    private val createVoucherEligibilityLiveData = MutableLiveData<Result<InitiateVoucherUiModel>>()
    val createVoucherEligibility: LiveData<Result<InitiateVoucherUiModel>> get() = createVoucherEligibilityLiveData

    fun getActiveVoucherList(
            isFirstTime: Boolean,
            keyword: String? = null,
            @VoucherTypeConst type: Int? = null,
            target: List<Int>? = null,
            sourceRequestParams: Pair<Int, String>,
            targetBuyer: String? = null) {
        launchCatchError(block = {
            if (isFirstTime) {
                _shopBasicLiveData.value = Success(withContext(dispatchers.io) {
                    shopBasicDataUseCase.executeOnBackground()
                })
            }
            val ongoingVoucherRequestParam = VoucherListParam.createParam(
                    status = VoucherStatus.ONGOING,
                    type = type,
                    targetList = target,
                    includeSubsidy = sourceRequestParams.first,
                    isVps = sourceRequestParams.second,
                    voucherName = keyword,
                    targetBuyer = targetBuyer
            )
            val notStartedVoucherRequestParam = VoucherListParam.createParam(
                    status = VoucherStatus.NOT_STARTED,
                    type = type,
                    targetList = target,
                    includeSubsidy = sourceRequestParams.first,
                    isVps = sourceRequestParams.second,
                    voucherName = keyword,
                    targetBuyer = targetBuyer
            )
            _voucherList.value = Success(withContext(dispatchers.io) {
                getVoucherListUseCase.params = GetVoucherListUseCase.createRequestParam(ongoingVoucherRequestParam)
                getNotStartedVoucherListUseCase.params = GetVoucherListUseCase.createRequestParam(notStartedVoucherRequestParam)
                val ongoingVoucherList = async { getVoucherListUseCase.executeOnBackground() }
                val notStartedVoucherList = async { getNotStartedVoucherListUseCase.executeOnBackground() }
                _fullVoucherListLiveData.value?.addAll(ongoingVoucherList.await() + notStartedVoucherList.await())
                ongoingVoucherList.await() + notStartedVoucherList.await()
            })
        }, onError = {
            _voucherList.value = Fail(it)
        })
    }

    fun getVoucherListHistory(@VoucherTypeConst type: Int?,
                              keyword: String? = null,
                              targetList: List<Int>?,
                              @VoucherSort sort: String?,
                              page: Int,
                              isInverted: Boolean,
                              sourceRequestParams: Pair<Int, String>,
                              targetBuyer: String?) {
        launchCatchError(block = {
            getVoucherListUseCase.params = GetVoucherListUseCase.createRequestParam(
                    VoucherListParam.createParam(
                            status = VoucherStatus.HISTORY,
                            voucherName = keyword,
                            type = type,
                            targetList = targetList,
                            sort = sort,
                            page = page,
                            isInverted = isInverted,
                            includeSubsidy = sourceRequestParams.first,
                            isVps = sourceRequestParams.second,
                            targetBuyer = targetBuyer)
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

    fun searchVoucherByKeyword(
            isActiveVoucher: Boolean,
            keyword: String?,
            voucherType: Int? = this.voucherType,
            voucherTarget: List<Int>? = this.voucherTarget,
            voucherSort: String = this.voucherSort,
            sourceRequestParams: Pair<Int, String>,
            targetBuyer: String?) {
        if (isActiveVoucher) {
            getActiveVoucherList(
                    isFirstTime = false,
                    keyword = keyword,
                    type = voucherType,
                    target = voucherTarget,
                    sourceRequestParams = sourceRequestParams,
                    targetBuyer = targetBuyer)
        } else {
            getVoucherListHistory(
                    type = voucherType,
                    targetList = voucherTarget,
                    sort = voucherSort,
                    page = currentPage,
                    isInverted = isInverted,
                    sourceRequestParams = sourceRequestParams, targetBuyer = targetBuyer
            )
        }
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

    fun getCreateVoucherEligibility() {
        launchCatchError(block = {
            initiateVoucherUseCase.query = GET_INIT_VOUCHER_ELIGIBILITY_QUERY
            initiateVoucherUseCase.params = InitiateVoucherUseCase.createRequestParam(isUpdate)
            createVoucherEligibilityLiveData.value = Success(withContext(dispatchers.io) {
                initiateVoucherUseCase.executeOnBackground()
            })
        }, onError = {
            createVoucherEligibilityLiveData.value = Fail(it)
        })
    }

    fun setShowBroadCastChatTicker(showBroadCastChatTicker: Boolean) {
        this.showBroadCastChatTicker = showBroadCastChatTicker
    }

    fun getShowBroadCastChatTicker(): Boolean {
        return showBroadCastChatTicker
    }

    fun setIsFreeBroadCastIconVisible(broadCastQuota: Int) {
        this.isFreeBroadCastIconVisible = (broadCastQuota > 0)
    }

    fun isFreeBroadCastIconVisible(): Boolean {
        return isFreeBroadCastIconVisible
    }

    fun setIsSuccessDialogDisplayed(isDisplayed: Boolean) {
        this.isSuccessDialogDisplayed = isDisplayed
    }

    fun isSuccessDialogDisplayed(): Boolean {
        return isSuccessDialogDisplayed
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

    fun getVoucherSourceRequestParams(
            isSellerCreated: Boolean? = null,
            isVps: Boolean? = null,
            isSubsidy: Boolean? = null): Pair<Int, String> {
        return when {
            // seller only
            isSellerCreated == true && isVps == false && isSubsidy == false -> {
                Pair(VoucherSubsidy.SELLER, VoucherVps.NON_VPS)
            }
            // vps only
            isSellerCreated == false && isVps == true && isSubsidy == false -> {
                Pair(VoucherSubsidy.SELLER_AND_TOKOPEDIA, VoucherVps.VPS)
            }
            // subsidy only
            isSellerCreated == false && isVps == false && isSubsidy == true -> {
                Pair(VoucherSubsidy.TOKOPEDIA, VoucherVps.NON_VPS)
            }
            // seller vps subsidy
            isSellerCreated == true && isVps == true && isSubsidy == true -> {
                Pair(VoucherSubsidy.SELLER_AND_TOKOPEDIA, VoucherVps.ALL)
            }
            // seller vps
            isSellerCreated == true && isVps == true && isSubsidy == false -> {
                Pair(VoucherSubsidy.SELLER, VoucherVps.ALL)
            }
            // seller subsidy
            isSellerCreated == true && isVps == false && isSubsidy == false -> {
                Pair(VoucherSubsidy.SELLER_AND_TOKOPEDIA, VoucherVps.NON_VPS)
            }
            // vps subsidy
            isSellerCreated == false && isVps == true && isSubsidy == false -> {
                Pair(VoucherSubsidy.TOKOPEDIA, VoucherVps.VPS)
            }
            else -> {
                // all
                Pair(VoucherSubsidy.SELLER_AND_TOKOPEDIA, VoucherVps.ALL)
            }
        }
    }
}