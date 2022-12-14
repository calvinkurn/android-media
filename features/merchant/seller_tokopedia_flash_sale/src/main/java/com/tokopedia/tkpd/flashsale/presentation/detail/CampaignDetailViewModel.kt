package com.tokopedia.tkpd.flashsale.presentation.detail

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tkpd.flashsale.presentation.detail.helper.ProductCriteriaHelper
import com.tokopedia.tkpd.flashsale.common.extension.*
import com.tokopedia.tkpd.flashsale.data.mapper.FlashSaleMonitorSubmitProductSseMapper
import com.tokopedia.tkpd.flashsale.data.request.GetFlashSaleSubmittedProductListRequest
import com.tokopedia.tkpd.flashsale.data.response.SSEStatus
import com.tokopedia.tkpd.flashsale.domain.entity.*
import com.tokopedia.tkpd.flashsale.domain.entity.enums.DetailBottomSheetType
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleListPageTab
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleStatus
import com.tokopedia.tkpd.flashsale.domain.usecase.*
import com.tokopedia.tkpd.flashsale.presentation.common.constant.ValueConstant
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.ongoing.item.OngoingItem
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.ongoing.item.OngoingRejectedItem
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.item.FinishedProcessSelectionItem
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.item.OnSelectionProcessItem
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.item.WaitingForSelectionItem
import com.tokopedia.tkpd.flashsale.presentation.detail.mapper.ProductCheckingResultMapper
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.CampaignDetailBottomSheetModel
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.TimelineStepModel
import com.tokopedia.tkpd.flashsale.util.tracker.CampaignDetailPageTracker
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class CampaignDetailViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getFlashSaleDetailForSellerUseCase: GetFlashSaleDetailForSellerUseCase,
    private val getFlashSaleSubmittedProductListUseCase: GetFlashSaleSubmittedProductListUseCase,
    private val getFlashSaleSellerStatusUseCase: GetFlashSaleSellerStatusUseCase,
    private val doFlashSaleProductReserveUseCase: DoFlashSaleProductReserveUseCase,
    private val doFlashSaleProductDeleteUseCase: DoFlashSaleProductDeleteUseCase,
    private val doFlashSaleSellerRegistrationUseCase: DoFlashSaleSellerRegistrationUseCase,
    private val getFlashSaleProductSubmissionProgressUseCase: GetFlashSaleProductSubmissionProgressUseCase,
    private val flashSaleTkpdProductSubmissionMonitoringSse: FlashSaleTkpdProductSubmissionMonitoringSse,
    private val flashSaleMonitorSubmitProductSseMapper: FlashSaleMonitorSubmitProductSseMapper,
    private val doFlashSaleProductSubmitAcknowledgeUseCase: DoFlashSaleProductSubmitAcknowledgeUseCase,
    private val userSession: UserSessionInterface,
    private val sharedPreferences: SharedPreferences,
    private val tracker: CampaignDetailPageTracker
) : BaseViewModel(dispatchers.main) {

    private var _campaign = MutableLiveData<Result<FlashSale>>()
    val campaign: LiveData<Result<FlashSale>>
        get() = _campaign

    private var _submittedProduct = MutableLiveData<Result<List<DelegateAdapterItem>>>()
    val submittedProduct: LiveData<Result<List<DelegateAdapterItem>>>
        get() = _submittedProduct

    private var _selectedProducts = MutableLiveData<List<Pair<Long, Long>>>(listOf())
    val selectedProducts: LiveData<List<Pair<Long, Long>>>
        get() = _selectedProducts

    private val _productReserveResult = MutableLiveData<Pair<ProductReserveResult, String>>()
    val productReserveResult: LiveData<Pair<ProductReserveResult, String>>
        get() = _productReserveResult

    private var _productDeleteResult = MutableLiveData<ProductDeleteResult>()
    val productDeleteResult: LiveData<ProductDeleteResult>
        get() = _productDeleteResult

    private var _flashSaleRegistrationResult = MutableLiveData<FlashSaleRegistrationResult>()
    val flashSaleRegistrationResult: LiveData<FlashSaleRegistrationResult>
        get() = _flashSaleRegistrationResult

    private var _submittedProductVariant = MutableLiveData<List<ProductCheckingResult>>()
    val submittedProductVariant: LiveData<List<ProductCheckingResult>>
        get() = _submittedProductVariant

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error

    private var campaignStatus = FlashSaleStatus.NO_REGISTERED_PRODUCT
    private var tabName = FlashSaleListPageTab.UPCOMING
    private var selectedItems = mutableListOf<Pair<Long, Long>>()
    private var isOnCheckboxState = false
    private var isTriggeredFromDelete = false

    sealed class UiEffect {
        object ShowGlobalError : UiEffect()
        object ShowIneligibleAccessWarning : UiEffect()
        object OnSseOpen : UiEffect()
        data class OnProductSseSubmissionProgress(
            val flashSaleProductSubmissionSseResult: FlashSaleProductSubmissionSseResult
        ) : UiEffect()

        data class OnSuccessAcknowledgeProductSubmissionSse(
            val totalSubmittedProduct: Int
        ) : UiEffect()
    }
    private val _uiEffect = MutableSharedFlow<UiEffect>(replay = 1)
    val uiEffect = this._uiEffect.asSharedFlow()

    companion object {
        private const val PAGE_SIZE = 10
        private const val REGISTER_PERIOD_TITLE = "Periode Pendaftaran"
        private const val ADD_PRODUCT_TITLE = "Tambah Produk"
        private const val SELECTION_PROCESS_TITLE = "Proses Seleksi"
        private const val ACTIVE_PROMOTION_TITLE = "Promosi Aktif"
        private const val UPCOMING_TAB = "upcoming"
        private const val REGISTERED_TAB = "registered"
        private const val ONGOING_TAB = "ongoing"
        private const val FINISHED_TAB = "finished"
    }

    fun register(campaignId: Long) {
        launchCatchError(
            block = {
                val result = doFlashSaleSellerRegistrationUseCase.execute(campaignId)
                _flashSaleRegistrationResult.postValue(result)
            },
            onError = { error ->
                _error.postValue(error)
            }
        )
    }

    fun getCampaignDetail(campaignId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val sellerEligibilityDeferred = async { getFlashSaleSellerStatusUseCase.execute() }
                val sellerEligibility = sellerEligibilityDeferred.await()
                val isEligibleUsingFeature = sellerEligibility.isEligibleUsingFeature()

                    val result = getFlashSaleDetailForSellerUseCase.execute(
                        campaignId = campaignId
                    )
                    campaignStatus = result.status
                    tabName = result.tabName
                    _campaign.postValue(Success(result))

                if (!isEligibleUsingFeature) {
                    _uiEffect.emit(UiEffect.ShowIneligibleAccessWarning)
                }

            },
            onError = { error ->
                _campaign.postValue(Fail(error))
            }
        )
    }

    fun getSubmittedProduct(campaignId: Long, offset: Int = 0) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getFlashSaleSubmittedProductListUseCase.execute(
                    campaignId = campaignId,
                    pagination = GetFlashSaleSubmittedProductListRequest.Pagination(
                        PAGE_SIZE,
                        offset
                    )
                )
                val formattedProductList =
                    formatSubmittedProductList(campaignStatus, result.productList)
                _submittedProduct.postValue(Success(formattedProductList))
            },
            onError = { error ->
                _submittedProduct.postValue(Fail(error))
            }
        )
    }

    fun getSubmittedProductVariant(
        campaignId: Long,
        productId: Long,
        displayProductSold: Boolean,
        fallbackProductImage: String
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getFlashSaleSubmittedProductListUseCase.execute(
                    productId = productId,
                    campaignId = campaignId,
                    pagination = GetFlashSaleSubmittedProductListRequest.Pagination(
                        PAGE_SIZE,
                        Int.ZERO
                    )
                )
                _submittedProductVariant.postValue(
                    ProductCheckingResultMapper.map(result.productList, displayProductSold,
                        fallbackProductImage, getTabName()))
            },
            onError = { error ->
                _error.postValue(error)
            }
        )
    }

    fun reserveProduct(campaignId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val reservationId = userSession.shopId + Date().time.toString()
                val param = DoFlashSaleProductReserveUseCase.Param(
                    campaignId = campaignId,
                    reservationId = reservationId,
                    productData = selectedItems
                )
                val result = doFlashSaleProductReserveUseCase.execute(param)
                _productReserveResult.postValue(Pair(result, reservationId))
            },
            onError = { error ->
                _error.postValue(error)
            }
        )
    }

    fun deleteProduct(campaignId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val params = DoFlashSaleProductDeleteUseCase.Param(
                    campaignId = campaignId,
                    productIds = selectedItems.map { it.first }
                )
                val result = doFlashSaleProductDeleteUseCase.execute(params)
                _productDeleteResult.postValue(result)
            },
            onError = { error ->
                _error.postValue(error)
            }
        )
    }

    private fun formatSubmittedProductList(
        status: FlashSaleStatus,
        submittedProductList: List<SubmittedProduct>
    ): List<DelegateAdapterItem> {
        return submittedProductList.map { submittedProduct ->
            when (status) {
                FlashSaleStatus.WAITING_FOR_SELECTION -> submittedProduct.toWaitingForSelectionItem()
                FlashSaleStatus.ON_SELECTION_PROCESS -> submittedProduct.toOnSelectionProcessItem()
                FlashSaleStatus.SELECTION_FINISHED -> submittedProduct.toFinishedProcessSelectionItem()
                FlashSaleStatus.ONGOING -> submittedProduct.toOngoingAndFinishedItem()
                FlashSaleStatus.REJECTED -> submittedProduct.toOngoingRejectedItem()
                FlashSaleStatus.FINISHED -> submittedProduct.toOngoingAndFinishedItem()
                else -> submittedProduct.toWaitingForSelectionItem()
            }
        }
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

    fun getBottomSheetData(
        type: DetailBottomSheetType,
        flashSale: FlashSale
    ): CampaignDetailBottomSheetModel {
        return when (type) {
            DetailBottomSheetType.TIMELINE -> {
                CampaignDetailBottomSheetModel(
                    timelineSteps = getTimelineData(flashSale),
                    showTimeline = true
                )
            }
            DetailBottomSheetType.PRODUCT_CRITERIA -> {
                CampaignDetailBottomSheetModel(
                    productCriterias = ProductCriteriaHelper.getCriteriaData(flashSale),
                    showCriteria = true,
                    showProductCriteria = true
                )
            }
            else -> {
                CampaignDetailBottomSheetModel(
                    timelineSteps = getTimelineData(flashSale),
                    productCriterias = ProductCriteriaHelper.getCriteriaData(flashSale),
                    showTimeline = true,
                    showCriteria = true,
                    showProductCriteria = true
                )
            }
        }
    }

    private fun SubmittedProduct.toWaitingForSelectionItem(): DelegateAdapterItem {
        return WaitingForSelectionItem(
            campaignStock,
            isMultiwarehouse,
            isParentProduct,
            totalChild,
            mainStock,
            name,
            picture,
            productCriteria,
            productId,
            url,
            price,
            discount,
            discountedPrice,
            submittedProductStockStatus,
            warehouses,
            countLocation
        )
    }

    private fun SubmittedProduct.toOnSelectionProcessItem(): DelegateAdapterItem {
        return OnSelectionProcessItem(
            campaignStock,
            isMultiwarehouse,
            isParentProduct,
            totalChild,
            mainStock,
            name,
            picture,
            productCriteria,
            productId,
            url,
            price,
            discount,
            discountedPrice,
            submittedProductStockStatus,
            warehouses,
            countLocation
        )
    }

    private fun SubmittedProduct.toFinishedProcessSelectionItem(): DelegateAdapterItem {
        return FinishedProcessSelectionItem(
            campaignStock,
            isMultiwarehouse,
            isParentProduct,
            totalChild,
            mainStock,
            name,
            picture,
            productCriteria,
            productId,
            url,
            price,
            discount,
            discountedPrice,
            submittedProductStockStatus,
            warehouses,
            countLocation,
            totalSubsidy,
            statusText
        )
    }

    private fun SubmittedProduct.toOngoingAndFinishedItem(): DelegateAdapterItem {
        return OngoingItem(
            campaignStock,
            isMultiwarehouse,
            isParentProduct,
            totalChild,
            totalSubsidy,
            soldCount,
            mainStock,
            name,
            picture,
            productCriteria,
            productId,
            url,
            price,
            discount,
            discountedPrice,
            submittedProductStockStatus,
            warehouses,
            countLocation,
            statusText
        )
    }

    private fun SubmittedProduct.toOngoingRejectedItem(): DelegateAdapterItem {
        return OngoingRejectedItem(
            campaignStock,
            isMultiwarehouse,
            isParentProduct,
            totalChild,
            soldCount,
            mainStock,
            name,
            picture,
            productCriteria,
            productId,
            url,
            price,
            discount,
            discountedPrice,
            submittedProductStockStatus,
            warehouses,
            countLocation,
            statusText
        )
    }

    fun isCampaignRegisterClosed(flashSale: FlashSale): Boolean {
        val now = Date()
        val flashSaleEndDate = flashSale.submissionEndDateUnix
        return now > flashSaleEndDate
    }

    fun isFlashSalePeriodOnTheSameDate(flashSale: FlashSale): Boolean {
        val startDate = flashSale.startDateUnix.dateOnly()
        val endDate = flashSale.endDateUnix.dateOnly()
        return startDate == endDate
    }

    fun setSelectedItem(selectedItem: Pair<Long, Long>) {
        val isExist = this.selectedItems.any { it == selectedItem }
        if (isExist) {
            return
        } else {
            this.selectedItems.add(selectedItem)
            _selectedProducts.value = this.selectedItems
        }
    }

    fun removeSelectedItem(selectedItemId: Pair<Long, Long>) {
        selectedItems.remove(selectedItemId)
        _selectedProducts.value = selectedItems
    }

    fun removeAllSelectedItems() {
        selectedItems.clear()
        _selectedProducts.value = this.selectedItems
        setCheckBoxStateStatus(false)
        setDeleteStateStatus(false)
    }

    fun getCampaignStatus(): FlashSaleStatus {
        return this.campaignStatus
    }

    fun getTabName(): String {
        return when (this.tabName) {
            FlashSaleListPageTab.UPCOMING -> UPCOMING_TAB
            FlashSaleListPageTab.REGISTERED -> REGISTERED_TAB
            FlashSaleListPageTab.ONGOING -> ONGOING_TAB
            FlashSaleListPageTab.FINISHED -> FINISHED_TAB
        }
    }

    fun getAddProductButtonVisibility(): Boolean {
        return getCampaignStatus() == FlashSaleStatus.NO_REGISTERED_PRODUCT || getCampaignStatus() == FlashSaleStatus.WAITING_FOR_SELECTION
    }

    fun isOnCheckBoxState(): Boolean {
        return this.isOnCheckboxState
    }

    fun setCheckBoxStateStatus(isShown: Boolean) {
        this.isOnCheckboxState = isShown
    }

    fun isTriggeredFromDelete(): Boolean {
        return this.isTriggeredFromDelete
    }

    fun setDeleteStateStatus(isTriggered: Boolean) {
        this.isTriggeredFromDelete = isTriggered
    }

    fun isCoachMarkShown(): Boolean {
        return sharedPreferences.getBoolean(
            ValueConstant.SHARED_PREF_CAMPAIGN_DETAIL_COACH_MARK,
            false
        )
    }

    fun setSharedPrefCoachMarkAlreadyShown() {
        sharedPreferences.edit()
            .putBoolean(ValueConstant.SHARED_PREF_CAMPAIGN_DETAIL_COACH_MARK, true)
            .apply()
    }

    // BEGIN - Tracker Functions
    fun sendSeeCriteriaClickEvent(campaignId: Long) {
        tracker.sendClickSeeCriteriaEvent(campaignId.toString())
    }

    fun sendRegisterClickEvent(campaignId: Long) {
        tracker.sendClickRegisterEvent(campaignId.toString())
    }

    fun sendCheckReasonClickEvent(campaignId: Long) {
        tracker.sendClickCheckReasonEvent(campaignId.toString())
    }

    fun sendBulkChooseClickEvent(campaignId: Long) {
        tracker.sendClickBulkChooseEvent(campaignId.toString())
    }

    fun sendAddProductClickEvent(campaignId: Long) {
        tracker.sendClickAddProductEvent(campaignId.toString())
    }

    fun sendDeleteClickEvent(campaignId: Long) {
        tracker.sendClickDeleteEvent(campaignId.toString())
    }

    fun sendEditClickEvent(campaignId: Long) {
        tracker.sendClickEditEvent(campaignId.toString())
    }
    // END - Tracker Functions

    private fun SellerEligibility.isEligibleUsingFeature(): Boolean {
        val isRbacRuleActive = isDeviceAllowed

        return when {
            isRbacRuleActive && isUserAllowed -> true
            !isRbacRuleActive -> true
            else -> false
        }
    }

    fun getFlashSaleSubmissionProgress(flashSaleId: String) {
        launchCatchError(dispatchers.io,
        block = {
            val flashSaleSubmissionProgress = getFlashSaleProductSubmissionProgressResponse(
                flashSaleId
            )
            if(flashSaleSubmissionProgress.isOpenSse) {
                _uiEffect.emit(UiEffect.OnSseOpen)
            }
        }){ }
    }

    private suspend fun getFlashSaleProductSubmissionProgressResponse(campaignId: String): FlashSaleProductSubmissionProgress {
        return getFlashSaleProductSubmissionProgressUseCase.execute(GetFlashSaleProductSubmissionProgressUseCase.Param(
            campaignId,
            checkProgress = true
        ))
    }

    fun listenToOpenedSse(campaignId: String) {
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
                                UiEffect.OnProductSseSubmissionProgress(sseResult)
                            )
                            if(sseResult.status != FlashSaleProductSubmissionSseResult.Status.IN_PROGRESS){
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

    fun acknowledgeProductSubmissionSse(campaignId: String, totalSubmittedProduct: Int) {
        launchCatchError(dispatchers.io, {
            if (doAcknowledgeProductSubmission(campaignId)) {
                _uiEffect.tryEmit(
                    UiEffect.OnSuccessAcknowledgeProductSubmissionSse(
                        totalSubmittedProduct
                    )
                )
            }

        }) {}
    }

    private suspend fun doAcknowledgeProductSubmission(campaignId: String): Boolean {
        val param = DoFlashSaleProductSubmitAcknowledgeUseCase.Param(campaignId.toLongOrZero())
        return doFlashSaleProductSubmitAcknowledgeUseCase.execute(param)
    }

}
