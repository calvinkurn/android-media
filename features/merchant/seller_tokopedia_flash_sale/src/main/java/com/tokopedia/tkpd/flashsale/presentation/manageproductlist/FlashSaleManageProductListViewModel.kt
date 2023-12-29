package com.tokopedia.tkpd.flashsale.presentation.manageproductlist

import android.content.SharedPreferences
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.entity.RemoteTicker
import com.tokopedia.campaign.usecase.GetTargetedTickerUseCase
import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.campaign.utils.constant.TickerConstant
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tkpd.flashsale.data.mapper.FlashSaleMonitorSubmitProductSseMapper
import com.tokopedia.tkpd.flashsale.data.request.DoFlashSaleProductSubmissionRequest
import com.tokopedia.tkpd.flashsale.data.response.SSEStatus
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleProductSubmissionProgress
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleProductSubmissionSseResult.Status.IN_PROGRESS
import com.tokopedia.tkpd.flashsale.domain.entity.ProductDeleteResult
import com.tokopedia.tkpd.flashsale.domain.entity.ProductSubmissionResult
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.domain.usecase.DoFlashSaleProductDeleteUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.DoFlashSaleProductSubmissionUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.DoFlashSaleProductSubmitAcknowledgeUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.FlashSaleTkpdProductSubmissionMonitoringSse
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleDetailForSellerUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleProductSubmissionProgressUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleReservedProductListUseCase
import com.tokopedia.tkpd.flashsale.presentation.common.constant.ValueConstant.ONE_SECOND_DELAY
import com.tokopedia.tkpd.flashsale.presentation.common.constant.ValueConstant.SHARED_PREF_MANAGE_PRODUCT_LIST_COACH_MARK
import com.tokopedia.tkpd.flashsale.presentation.detail.helper.ProductCriteriaHelper
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.CampaignDetailBottomSheetModel
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.TimelineStepModel
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.item.FlashSaleManageProductListItem
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.uimodel.FlashSaleManageProductListUiEffect
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.uimodel.FlashSaleManageProductListUiEvent
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.uimodel.FlashSaleManageProductListUiState
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class FlashSaleManageProductListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getFlashSaleReservedProductListUseCase: GetFlashSaleReservedProductListUseCase,
    private val getFlashSaleDetailForSellerUseCase: GetFlashSaleDetailForSellerUseCase,
    private val doFlashSaleProductDeleteUseCase: DoFlashSaleProductDeleteUseCase,
    private val doFlashSaleProductSubmissionUseCase: DoFlashSaleProductSubmissionUseCase,
    private val flashSaleTkpdProductSubmissionMonitoringSse: FlashSaleTkpdProductSubmissionMonitoringSse,
    private val sharedPreferences: SharedPreferences,
    private val getTargetedTickerUseCase: GetTargetedTickerUseCase,
    private val getFlashSaleProductSubmissionProgressUseCase: GetFlashSaleProductSubmissionProgressUseCase,
    private val flashSaleMonitorSubmitProductSseMapper: FlashSaleMonitorSubmitProductSseMapper,
    private val doFlashSaleProductSubmitAcknowledgeUseCase: DoFlashSaleProductSubmitAcknowledgeUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val REGISTER_PERIOD_TITLE = "Periode Pendaftaran"
        private const val ADD_PRODUCT_TITLE = "Tambah Produk"
        private const val SELECTION_PROCESS_TITLE = "Proses Seleksi"
        private const val ACTIVE_PROMOTION_TITLE = "Promosi Aktif"
    }

    private val _uiState = MutableStateFlow(FlashSaleManageProductListUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<FlashSaleManageProductListUiEffect>(replay = 1)
    val uiEffect = _uiEffect.asSharedFlow()

    private val currentState: FlashSaleManageProductListUiState
        get() = _uiState.value

    fun processEvent(event: FlashSaleManageProductListUiEvent) {
        when (event) {
            is FlashSaleManageProductListUiEvent.GetReservedProductList -> {
                getReservedProductList(event.reservationId, event.page)
            }
            is FlashSaleManageProductListUiEvent.GetCampaignDetailBottomSheet -> {
                getCampaignDetailBottomSheetData(event.campaignId)
            }
            is FlashSaleManageProductListUiEvent.DeleteProductFromReserved -> {
                deleteProductFromReservation(
                    event.productData, event.reservationId, event.campaignId
                )
            }
            is FlashSaleManageProductListUiEvent.CheckShouldEnableButtonSubmit -> {
                checkShouldEnableButtonSubmit()
            }
            is FlashSaleManageProductListUiEvent.LoadNextReservedProduct -> {
                loadNextReservedProductList(event.reservationId, event.page)
            }
            is FlashSaleManageProductListUiEvent.SubmitDiscountedProduct -> {
                submitDiscountedProduct(event.reservationId, event.campaignId)
            }
            is FlashSaleManageProductListUiEvent.UpdateProductData -> {
                updateProductData(event.productData)
            }
            is FlashSaleManageProductListUiEvent.GetTickerData -> {
                getTickerData(event.rollenceValueList)
            }
        }
    }

    private fun updateProductData(productData: ReservedProduct.Product) {
        launchCatchError(dispatchers.io, {
            val updatedProductListData = currentState.listDelegateItem
                .filterIsInstance<FlashSaleManageProductListItem>()
                .indexOfFirst {
                    it.product.productId == productData.productId
                }.let { idx ->
                    if(idx >= Int.ZERO) {
                        currentState.listDelegateItem.toMutableList().apply {
                            set(
                                idx, FlashSaleManageProductListItem(
                                    productData
                                )
                            )
                        }
                    } else {
                        currentState.listDelegateItem
                    }
                }
            updateUiState {
                it.copy(
                    listDelegateItem = updatedProductListData
                )
            }
        }) { }
    }

    private fun submitDiscountedProduct(reservationId: String, campaignId: String) {
        launchCatchError(dispatchers.io, {
            val result = doSubmitDiscountedProduct(reservationId, campaignId)
            if (result.useSse) {
                monitorProductSubmitSse(result.sseKey)
            } else {
                delay(ONE_SECOND_DELAY)
                _uiEffect.emit(FlashSaleManageProductListUiEffect.OnProductSubmitted(result))
            }
        }) { error ->
            _uiEffect.tryEmit(
                FlashSaleManageProductListUiEffect.ShowErrorSubmitDiscountedProduct(error)
            )
        }
    }

    fun listenToExistingSse(campaignId: String) {
        launchCatchError(dispatchers.io, {
            monitorProductSubmitSse(campaignId)
        }) {

        }
    }

    private suspend fun monitorProductSubmitSse(sseKey: String) {
        viewModelScope.launch {
            flashSaleTkpdProductSubmissionMonitoringSse.connect(sseKey)
            flashSaleTkpdProductSubmissionMonitoringSse.listen().collect {
                when (it) {
                    is SSEStatus.Success -> {
                        val sseMsg = it.sseResponse.message
                        val flashSaleProductSubmissionSseResult =
                            flashSaleMonitorSubmitProductSseMapper.map(sseMsg)
                        flashSaleProductSubmissionSseResult?.let { sseResult ->
                            _uiEffect.emit(
                                FlashSaleManageProductListUiEffect.OnProductSseSubmissionProgress(
                                    sseResult
                                )
                            )
                            if (sseResult.status != IN_PROGRESS) {
                                closeSse()
                            }
                        }
                    }
                    is SSEStatus.Close -> {

                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        closeSse()
    }

    private fun closeSse() {
        flashSaleTkpdProductSubmissionMonitoringSse.closeSSE()
    }

    private suspend fun doSubmitDiscountedProduct(
        reservationId: String,
        campaignId: String
    ): ProductSubmissionResult {
        val productList =
            currentState.listDelegateItem.filterIsInstance<FlashSaleManageProductListItem>()
        val mappedListProductSubmission =
            mutableListOf<DoFlashSaleProductSubmissionRequest.ProductData>().apply {
                productList.onEach {
                    addAll(it.product.toFlashSaleProductSubmissionProductData())
                }
            }
        val requestParam = DoFlashSaleProductSubmissionUseCase.Param(
            campaignId.toLongOrZero(),
            mappedListProductSubmission,
            reservationId
        )
        return doFlashSaleProductSubmissionUseCase.execute(requestParam)
    }

    private fun loadNextReservedProductList(reservationId: String, page: Int) {
        launchCatchError(dispatchers.io, {
            val data = getReservedProductListData(reservationId, page)
            updateUiState {
                it.copy(
                    listDelegateItem = currentState.listDelegateItem.toMutableList().apply {
                        addAll(data.toFlashSaleManageProductListItem())
                    },
                    totalProduct = data.totalProduct
                )
            }
        }) { error ->
            _uiEffect.tryEmit(
                FlashSaleManageProductListUiEffect.ShowErrorLoadNextReservedProductList(error)
            )
        }
    }

    private fun checkShouldEnableButtonSubmit() {
        launchCatchError(dispatchers.io, {
            val listCurrentItem =
                currentState.listDelegateItem.filterIsInstance<FlashSaleManageProductListItem>()
            val isEnableSubmitButton = if (listCurrentItem.isEmpty()) {
                false
            } else {
                listCurrentItem.all {
                    it.product.isDiscounted()
                }
            }
            _uiEffect.emit(
                FlashSaleManageProductListUiEffect.ConfigSubmitButton(
                    isEnableSubmitButton
                )
            )
        }) {}
    }

    private fun deleteProductFromReservation(
        productData: ReservedProduct.Product,
        reservationId: String,
        campaignId: String
    ) {
        launchCatchError(dispatchers.io, {
            _uiEffect.emit(FlashSaleManageProductListUiEffect.ClearProductList)
            updateUiState {
                it.copy(
                    isLoading = true
                )
            }
            val data = deleteReservedProduct(productData, reservationId, campaignId)
            if (data.isSuccess) {
                _uiEffect.emit(FlashSaleManageProductListUiEffect.ShowToasterSuccessDelete)
                val deletedProduct =
                    currentState.listDelegateItem.filterIsInstance<FlashSaleManageProductListItem>()
                        .firstOrNull {
                            it.product.productId == productData.productId
                        }
                val updatedData = currentState.listDelegateItem.toMutableList().apply {
                    deletedProduct?.let {
                        remove(it)
                    }
                }
                updateUiState {
                    it.copy(
                        isLoading = false,
                        listDelegateItem = updatedData,
                        totalProduct = currentState.totalProduct - Int.ONE
                    )
                }
                checkIfShouldCloseManageProductListPage()
            } else {
                throw MessageErrorException(data.errorMessage)
            }
        }) { error ->
            _uiEffect.emit(FlashSaleManageProductListUiEffect.ShowToasterErrorDelete(error))
            updateUiState {
                it.copy(
                    isLoading = false,
                )
            }
        }
    }

    private suspend fun checkIfShouldCloseManageProductListPage() {
        if (currentState.totalProduct.isZero()) {
            _uiEffect.emit(FlashSaleManageProductListUiEffect.CloseManageProductListPage)
        }
    }

    private suspend fun deleteReservedProduct(
        productData: ReservedProduct.Product,
        reservationId: String,
        campaignId: String
    ): ProductDeleteResult {
        return doFlashSaleProductDeleteUseCase.execute(
            DoFlashSaleProductDeleteUseCase.Param(
                campaignId.toLongOrZero(),
                listOf(productData.productId),
                reservationId
            )
        )
    }

    private fun getTickerData(rollenceValueList: List<String>) {
        launchCatchError(dispatchers.io, {
            val targetParams: List<GetTargetedTickerUseCase.Param.Target> = listOf(
                GetTargetedTickerUseCase.Param.Target(
                    type = GetTargetedTickerUseCase.KEY_TYPE_ROLLENCE_NAME,
                    values = rollenceValueList
                )
            )
            val tickerParams = GetTargetedTickerUseCase.Param(
                page = TickerConstant.REMOTE_TICKER_KEY_FLASH_SALE_TOKOPEDIA_MANAGE_PRODUCT,
                targets = targetParams
            )
            val data = getTickerListData(tickerParams)
            updateUiState {
                it.copy(
                    showTicker = data.isNotEmpty(),
                    tickerList = data
                )
            }
        }) {
        }
    }

    private fun getCampaignDetailBottomSheetData(campaignId: String) {
        launchCatchError(dispatchers.io, {
            val data = getCampaignDetailData(campaignId)
            _uiEffect.emit(
                FlashSaleManageProductListUiEffect.AddIconCampaignDetailBottomSheet(
                    CampaignDetailBottomSheetModel(
                        timelineSteps = getTimelineData(data),
                        productCriterias = ProductCriteriaHelper.getCriteriaData(data),
                        showTimeline = true,
                        showCriteria = true,
                        showProductCriteria = true
                    )
                )
            )
        }) {
        }
    }

    private suspend fun getCampaignDetailData(campaignId: String): FlashSale {
        return getFlashSaleDetailForSellerUseCase.execute(campaignId.toLongOrZero())
    }

    private suspend fun getTickerListData(param: GetTargetedTickerUseCase.Param): List<RemoteTicker> {
        return getTargetedTickerUseCase.execute(param = param)
    }

    private fun getTimelineData(flashSale: FlashSale): MutableList<TimelineStepModel> {
        val timelineData: MutableList<TimelineStepModel> = mutableListOf()
        val submissionDatePeriod =
            "${flashSale.submissionStartDateUnix.formatTo(DateConstant.DATE_ONLY)}-${
                flashSale.submissionEndDateUnix.formatTo(DateConstant.DATE_YEAR_PRECISION)
            }"
        val selectionProcessDatePeriod =
            "${flashSale.reviewStartDateUnix.formatTo(DateConstant.DATE_ONLY)}-${
                flashSale.reviewEndDateUnix.formatTo(DateConstant.DATE_YEAR_PRECISION)
            }"
        val activePromotionDatePeriod =
            "${flashSale.startDateUnix.formatTo(DateConstant.DATE_ONLY)}-${
                flashSale.endDateUnix.formatTo(DateConstant.DATE_YEAR_PRECISION)
            }"
        val registerPeriodTimelineData = TimelineStepModel(
            REGISTER_PERIOD_TITLE,
            submissionDatePeriod,
            isEnded = Date() > flashSale.submissionEndDateUnix,
            isActive = Date() >= flashSale.submissionStartDateUnix && Date() <= flashSale.submissionEndDateUnix || Date() > flashSale.submissionEndDateUnix,
            icon = IconUnify.CLIPBOARD
        )
        val addProductTimelineData = TimelineStepModel(
            ADD_PRODUCT_TITLE,
            submissionDatePeriod,
            isEnded = Date() > flashSale.submissionEndDateUnix,
            isActive = Date() >= flashSale.submissionStartDateUnix && Date() <= flashSale.submissionEndDateUnix || Date() > flashSale.submissionEndDateUnix,
            icon = IconUnify.PRODUCT_ADD
        )
        val selectionProcessTimelineData = TimelineStepModel(
            SELECTION_PROCESS_TITLE,
            selectionProcessDatePeriod,
            isEnded = Date() > flashSale.reviewEndDateUnix,
            isActive = Date() >= flashSale.reviewStartDateUnix && Date() <= flashSale.reviewEndDateUnix || Date() > flashSale.reviewEndDateUnix,
            icon = IconUnify.PRODUCT_VERIFIED
        )
        val activePromotionTimelineData = TimelineStepModel(
            ACTIVE_PROMOTION_TITLE,
            activePromotionDatePeriod,
            isEnded = Date() > flashSale.endDateUnix,
            isActive = Date() >= flashSale.startDateUnix && Date() <= flashSale.endDateUnix || Date() > flashSale.endDateUnix,
            icon = IconUnify.FLASH_ON
        )
        val finishTimelineData = TimelineStepModel(
            isEnded = true,
            isActive = Date() > flashSale.endDateUnix,
            icon = IconUnify.CHECK_BIG
        )
        timelineData.add(registerPeriodTimelineData)
        timelineData.add(addProductTimelineData)
        timelineData.add(selectionProcessTimelineData)
        timelineData.add(activePromotionTimelineData)
        timelineData.add(finishTimelineData)
        for (i in 0 until timelineData.indexOfLast { it.isActive }) timelineData[i].isEnded = true
        return timelineData
    }

    private fun getReservedProductList(reservationId: String, page: Int) {
        launchCatchError(dispatchers.io, {
            updateUiState {
                it.copy(
                    isLoading = true,
                    currentPage = page
                )
            }
            delay(ONE_SECOND_DELAY)
            val data = getReservedProductListData(reservationId, page)
            checkShowCoachMarkOnFirstProductItem()
            showSubmitButton()
            updateUiState {
                it.copy(
                    isLoading = false,
                    listDelegateItem = data.toFlashSaleManageProductListItem(),
                    totalProduct = data.totalProduct
                )
            }
        }) { error ->
            updateUiState {
                it.copy(
                    isLoading = false
                )
            }
            _uiEffect.tryEmit(
                FlashSaleManageProductListUiEffect.ShowErrorGetReservedProductList(
                    error
                )
            )
        }
    }

    private suspend fun showSubmitButton() {
        _uiEffect.emit(FlashSaleManageProductListUiEffect.ShowSubmitButton)
    }

    private suspend fun checkShowCoachMarkOnFirstProductItem() {
        if (!sharedPreferences.getBoolean(SHARED_PREF_MANAGE_PRODUCT_LIST_COACH_MARK, false))
            _uiEffect.emit(FlashSaleManageProductListUiEffect.ShowCoachMarkOnFirstProductItem)
    }

    private fun updateUiState(newState: (FlashSaleManageProductListUiState) -> FlashSaleManageProductListUiState) {
        _uiState.update {
            newState.invoke(it)
        }
    }

    private suspend fun getReservedProductListData(
        reservationId: String,
        page: Int
    ): ReservedProduct {
        val param = GetFlashSaleReservedProductListUseCase.Param(
            reservationId,
            page
        )
        return getFlashSaleReservedProductListUseCase.execute(param)
    }

    private fun ReservedProduct.toFlashSaleManageProductListItem(): List<FlashSaleManageProductListItem> {
        return products.map {
            FlashSaleManageProductListItem(it)
        }
    }

    private fun ReservedProduct.Product.toFlashSaleProductSubmissionProductData(): List<DoFlashSaleProductSubmissionRequest.ProductData> {
        return if (isParentProduct) {
            childProducts.map {
                DoFlashSaleProductSubmissionRequest.ProductData(
                    productCriteria.criteriaId,
                    it.productId,
                    it.warehouses.filteredWarehouse().map { warehouse ->
                        DoFlashSaleProductSubmissionRequest.ProductData.Warehouse(
                            warehouse.warehouseId,
                            warehouse.discountSetup.price,
                            warehouse.discountSetup.stock
                        )
                    }
                )
            }
        } else {
            listOf(DoFlashSaleProductSubmissionRequest.ProductData(
                productCriteria.criteriaId,
                productId,
                warehouses.filteredWarehouse().map {
                    DoFlashSaleProductSubmissionRequest.ProductData.Warehouse(
                        it.warehouseId,
                        it.discountSetup.price,
                        it.discountSetup.stock
                    )
                }
            ))
        }
    }

    fun setSharedPrefCoachMarkAlreadyShown() {
        sharedPreferences.edit().putBoolean(SHARED_PREF_MANAGE_PRODUCT_LIST_COACH_MARK, true)
            .apply()
    }

    fun acknowledgeProductSubmissionSse(campaignId: String, totalSubmittedProduct: Int) {
        launchCatchError(dispatchers.io, {
            if (doAcknowledgeProductSubmission(campaignId)) {
                _uiEffect.tryEmit(
                    FlashSaleManageProductListUiEffect.OnSuccessAcknowledgeProductSubmissionSse(
                        totalSubmittedProduct
                    )
                )
            }
        }) { }
    }

    private suspend fun doAcknowledgeProductSubmission(campaignId: String): Boolean {
        val param = DoFlashSaleProductSubmitAcknowledgeUseCase.Param(campaignId.toLongOrZero())
        return doFlashSaleProductSubmitAcknowledgeUseCase.execute(param)
    }

    fun getFlashSaleSubmissionProgress(flashSaleId: String) {
        launchCatchError(dispatchers.io,
            block = {
                val flashSaleSubmissionProgress = getFlashSaleProductSubmissionProgressResponse(
                    flashSaleId
                )
                if (flashSaleSubmissionProgress.isOpenSse) {
                    _uiEffect.emit(FlashSaleManageProductListUiEffect.OnSseOpen)
                }
            }) { }
    }

    private suspend fun getFlashSaleProductSubmissionProgressResponse(campaignId: String): FlashSaleProductSubmissionProgress {
        return getFlashSaleProductSubmissionProgressUseCase.execute(
            GetFlashSaleProductSubmissionProgressUseCase.Param(
                campaignId,
                checkProgress = true
            )
        )
    }
}
