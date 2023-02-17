package com.tokopedia.mvc.presentation.list.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.mvc.domain.entity.ShareComponentMetaData
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.VoucherCreationMetadataWithRemoteTickerMessage
import com.tokopedia.mvc.domain.entity.VoucherCreationQuota
import com.tokopedia.mvc.domain.entity.VoucherListParam
import com.tokopedia.mvc.domain.entity.enums.UpdateVoucherAction
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
import com.tokopedia.mvc.domain.entity.enums.VoucherServiceType
import com.tokopedia.mvc.domain.entity.enums.VoucherSort
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.usecase.CancelVoucherUseCase
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.mvc.domain.usecase.GetVoucherListChildUseCase
import com.tokopedia.mvc.domain.usecase.GetVoucherListUseCase
import com.tokopedia.mvc.domain.usecase.GetVoucherQuotaUseCase
import com.tokopedia.mvc.domain.usecase.MerchantPromotionGetMVDataByIDUseCase
import com.tokopedia.mvc.domain.usecase.ProductListUseCase
import com.tokopedia.mvc.domain.usecase.ShopBasicDataUseCase
import com.tokopedia.mvc.presentation.list.helper.MvcListPageStateHelper
import com.tokopedia.mvc.presentation.list.model.DeleteVoucherUiEffect
import com.tokopedia.mvc.presentation.list.model.FilterModel
import com.tokopedia.mvc.util.constant.NumberConstant
import com.tokopedia.mvc.util.constant.TickerConstant
import com.tokopedia.mvc.util.extension.firstTickerMessage
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.mvc.R
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
    private val merchantPromotionGetMVDataByIDUseCase: MerchantPromotionGetMVDataByIDUseCase,
    private val shopBasicDataUseCase: ShopBasicDataUseCase,
    private val getProductsUseCase: ProductListUseCase,
    private val getTargetedTickerUseCase: GetTargetedTickerUseCase
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

    private val _voucherCreationMetadata = MutableLiveData<Result<VoucherCreationMetadataWithRemoteTickerMessage>>()
    val voucherCreationMetadata: LiveData<Result<VoucherCreationMetadataWithRemoteTickerMessage>> get() = _voucherCreationMetadata

    private val _generateShareComponentMetaData =
        MutableLiveData<Result<ShareComponentMetaData>>()
    val generateShareComponentMetaData: LiveData<Result<ShareComponentMetaData>>
        get() = _generateShareComponentMetaData

    val pageState = Transformations.map(voucherList) {
        MvcListPageStateHelper.getPageState(it, filter, page)
    }

    private val _deleteUIEffect = MutableSharedFlow<DeleteVoucherUiEffect>(replay = 1)
    val deleteUIEffect = _deleteUIEffect.asSharedFlow()

    var filter = FilterModel()
    var page: Int = 0

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

    fun setFilterType(type: VoucherServiceType, isEnabling: Boolean) {
        filter.voucherType.apply {
            if (isEnabling) {
                add(type)
            } else {
                removeAll {
                    it == type
                }
            }
        }
    }

    @StringRes
    fun getSelectedStatusText(): Int {
        return when (filter.status.sortedBy { it.id }) {
            listOf(VoucherStatus.NOT_STARTED) -> R.string.smvc_bottomsheet_filter_voucher_notstarted
            listOf(VoucherStatus.ONGOING) -> R.string.smvc_bottomsheet_filter_voucher_ongoing
            listOf(VoucherStatus.NOT_STARTED, VoucherStatus.ONGOING) -> R.string.smvc_bottomsheet_filter_voucher_active
            listOf(VoucherStatus.ENDED, VoucherStatus.STOPPED) -> R.string.smvc_bottomsheet_filter_voucher_finished
            else -> R.string.smvc_bottomsheet_filter_voucher_all
        }
    }

    fun getVoucherList(page: Int, pageSize: Int) {
        this.page = page
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

    fun getVoucherListChild(voucherId: Long, parentId: Long) {
        val cleanedParentId = parentId.takeIf { it.isMoreThanZero() } ?: voucherId
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getVoucherListChildUseCase.execute(
                    cleanedParentId,
                    arrayListOf(
                        VoucherStatus.NOT_STARTED,
                        VoucherStatus.ONGOING
                    )
                )
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

    fun stopVoucher(voucher: Voucher) {
        val voucherStatus = voucher.status
        launchCatchError(
            dispatchers.io,
            block = {
                _deleteUIEffect.emit(DeleteVoucherUiEffect.OnProgressToDeletedVoucherList)
                val detailVoucherParam = MerchantPromotionGetMVDataByIDUseCase.Param(voucher.id)
                val detailVoucherDeffer =
                    async { merchantPromotionGetMVDataByIDUseCase.execute(detailVoucherParam) }
                val detailVoucher = detailVoucherDeffer.await()
                val metadataParam = GetInitiateVoucherPageUseCase.Param(
                    VoucherAction.UPDATE,
                    detailVoucher.voucherType,
                    detailVoucher.isVoucherProduct
                )
                val metadataDeferred =
                    async { getInitiateVoucherPageUseCase.execute(metadataParam) }
                val token = metadataDeferred.await()
                val couponStatus =
                    if (voucherStatus == VoucherStatus.NOT_STARTED) UpdateVoucherAction.DELETE else UpdateVoucherAction.STOP
                val idCancelVoucher =
                    cancelVoucherUseCase.execute(voucher.id.toInt(), couponStatus, token.token)
                if (idCancelVoucher.updateStatusVoucherData.voucherId.isNotEmpty()) {
                    _deleteUIEffect.emit(
                        DeleteVoucherUiEffect.SuccessDeletedVoucher(
                            idCancelVoucher.updateStatusVoucherData.voucherId.toIntOrZero(),
                            voucher.name,
                            voucherStatus
                        )
                    )
                }
            },
            onError = { error ->
                _deleteUIEffect.emit(
                    DeleteVoucherUiEffect.ShowToasterErrorDelete(
                        error,
                        voucher.name,
                        voucherStatus
                    )
                )
            }
        )
    }

    fun generateShareComponentMetaData(voucher: Voucher) {
        launchCatchError(
            dispatchers.io,
            block = {
                val parentProductIds = voucher.productIds.map { it.parentProductId }
                val shopDataDeferred = async { shopBasicDataUseCase.execute() }

                val productListParam = ProductListUseCase.Param(
                    searchKeyword = "",
                    warehouseId = 0,
                    categoryIds = emptyList(),
                    showcaseIds = emptyList(),
                    page = NumberConstant.FIRST_PAGE,
                    pageSize = parentProductIds.size,
                    sortId = "DEFAULT",
                    sortDirection = "DESC",
                    productIdInclude = parentProductIds
                )

                val productsDeferred = async { getProductsUseCase.execute(productListParam) }

                val shopData = shopDataDeferred.await()
                val products = productsDeferred.await()

                val topSellingProductImageUrls = products.products
                    .sortedByDescending { it.txStats.sold }
                    .take(THREE_TOP_SELLING_PRODUCT)
                    .map { it.picture }

                val metadata = ShareComponentMetaData(
                    voucher,
                    shopData,
                    topSellingProductImageUrls
                )
                _generateShareComponentMetaData.postValue(Success(metadata))
            },
            onError = { error ->
                _generateShareComponentMetaData.postValue(Fail(error))
            }
        )
    }

    fun getVoucherCreationMetadata() {
        launchCatchError(
            dispatchers.io,
            block = {
                val voucherCreationMetadataDeferred = async { getInitiateVoucherPageUseCase.execute() }


                val tickerWordingParam = GetTargetedTickerUseCase.Param(TickerConstant.REMOTE_TICKER_KEY_VOUCHER_LIST_PAGE)
                val tickerWordingDeferred = async { getTargetedTickerUseCase.execute(tickerWordingParam) }

                val voucherCreationMetadata = voucherCreationMetadataDeferred.await()

                val tickerWordings = tickerWordingDeferred.await()
                val tickerWording = tickerWordings.getTargetedTicker.list.firstTickerMessage()

                val data = VoucherCreationMetadataWithRemoteTickerMessage(voucherCreationMetadata, tickerWording)
                _voucherCreationMetadata.postValue(Success(data))
            },
            onError = { error ->
                _voucherCreationMetadata.postValue(Fail(error))
            }
        )
    }

    companion object {
        private const val THREE_TOP_SELLING_PRODUCT = 3
    }
}
