package com.tokopedia.tkpd.flashsale.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.tkpd.flashsale.common.extension.*
import com.tokopedia.tkpd.flashsale.data.request.GetFlashSaleSubmittedProductListRequest
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.domain.entity.ProductDeleteResult
import com.tokopedia.tkpd.flashsale.domain.entity.ProductReserveResult
import com.tokopedia.tkpd.flashsale.domain.entity.SubmittedProduct
import com.tokopedia.tkpd.flashsale.domain.entity.enums.DetailBottomSheetType
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleStatus
import com.tokopedia.tkpd.flashsale.domain.usecase.DoFlashSaleProductDeleteUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.DoFlashSaleProductReserveUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleDetailForSellerUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleSubmittedProductListUseCase
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.ongoing.item.OngoingItem
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.ongoing.item.OngoingRejectedItem
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.item.FinishedProcessSelectionItem
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.item.OnSelectionProcessItem
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.item.WaitingForSelectionItem
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.CampaignDetailBottomSheetModel
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.ProductCriteriaModel
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.TimelineStepModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

class CampaignDetailViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getFlashSaleDetailForSellerUseCase: GetFlashSaleDetailForSellerUseCase,
    private val getFlashSaleSubmittedProductListUseCase: GetFlashSaleSubmittedProductListUseCase,
    private val doFlashSaleProductReserveUseCase: DoFlashSaleProductReserveUseCase,
    private val doFlashSaleProductDeleteUseCase: DoFlashSaleProductDeleteUseCase,
    private val userSession: UserSessionInterface
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

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error

    private var campaignStatus = FlashSaleStatus.NO_REGISTERED_PRODUCT
    private var selectedItems = mutableListOf<Pair<Long, Long>>()
    private var isOnCheckboxState = false
    private var isTriggeredFromDelete = false

    companion object {
        private const val PAGE_SIZE = 3
        private const val REGISTER_PERIOD_TITLE = "Periode Pendaftaran"
        private const val ADD_PRODUCT_TITLE = "Tambah Produk"
        private const val SELECTION_PROCESS_TITLE = "Proses Seleksi"
        private const val ACTIVE_PROMOTION_TITLE = "Promosi Aktif"
    }

    fun getCampaignDetail(campaignId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getFlashSaleDetailForSellerUseCase.execute(
                    campaignId = campaignId
                )
                campaignStatus = result.status
                _campaign.postValue(Success(result))
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
            isActive = Date() >= flashSale.submissionStartDateUnix && Date() <= flashSale.submissionEndDateUnix || Date() > flashSale.submissionEndDateUnix
        )
        val addProductTimelineData = TimelineStepModel(
            ADD_PRODUCT_TITLE,
            submissionDatePeriod,
            isEnded = Date() > flashSale.submissionEndDateUnix,
            isActive = Date() >= flashSale.submissionStartDateUnix && Date() <= flashSale.submissionEndDateUnix || Date() > flashSale.submissionEndDateUnix
        )
        val selectionProcessTimelineData = TimelineStepModel(
            SELECTION_PROCESS_TITLE,
            selectionProcessDatePeriod,
            isEnded = Date() > flashSale.reviewEndDateUnix,
            isActive = Date() >= flashSale.reviewStartDateUnix && Date() <= flashSale.reviewEndDateUnix || Date() > flashSale.reviewEndDateUnix
        )
        val activePromotionTimelineData = TimelineStepModel(
            ACTIVE_PROMOTION_TITLE,
            activePromotionDatePeriod,
            isEnded = Date() > flashSale.endDateUnix,
            isActive = Date() >= flashSale.startDateUnix && Date() <= flashSale.endDateUnix || Date() > flashSale.endDateUnix
        )
        val finishTimelineData = TimelineStepModel(
            isEnded = true,
            isActive = Date() > flashSale.endDateUnix
        )
        timelineData.add(registerPeriodTimelineData)
        timelineData.add(addProductTimelineData)
        timelineData.add(selectionProcessTimelineData)
        timelineData.add(activePromotionTimelineData)
        timelineData.add(finishTimelineData)
        return timelineData
    }

    private fun getCriteriaData(flashSale: FlashSale): MutableList<ProductCriteriaModel> {
        val productCriteriaData: MutableList<ProductCriteriaModel> = mutableListOf()
        flashSale.productCriteria.forEach { productCriteria ->
            productCriteria.categories.forEach { category ->
                productCriteriaData.add(
                    ProductCriteriaModel(
                        category.categoryName,
                        "",
                        ProductCriteriaModel.ValueRange(
                            productCriteria.minPrice.toLong(),
                            productCriteria.maxPrice.toLong()
                        ),
                        ProductCriteriaModel.ValueRange(
                            productCriteria.minFinalPrice.toLong(),
                            productCriteria.maxFinalPrice.toLong()
                        ),
                        productCriteria.minDiscount.toDouble(),
                        ProductCriteriaModel.ValueRange(
                            productCriteria.minCustomStock.toLong(),
                            productCriteria.maxCustomStock.toLong()
                        ),
                        productCriteria.minRating.toDouble(),
                        productCriteria.minProductScore.toLong(),
                        ProductCriteriaModel.ValueRange(
                            productCriteria.minQuantitySold.toLong(),
                            productCriteria.maxQuantitySold.toLong()
                        ),
                        productCriteria.minQuantitySold.toLong(),
                        productCriteria.maxSubmission.toLong(),
                        productCriteria.maxProductAppear.toLong(),
                        productCriteria.dayPeriodTimeAppear.toLong()
                    )
                )
            }
        }

        return productCriteriaData
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
                    productCriterias = getCriteriaData(flashSale),
                    showCriteria = true,
                    showProductCriteria = true
                )
            }
            else -> {
                CampaignDetailBottomSheetModel(
                    timelineSteps = getTimelineData(flashSale),
                    productCriterias = getCriteriaData(flashSale),
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
            warehouses
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
            warehouses
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
            warehouses
        )
    }

    private fun SubmittedProduct.toOngoingAndFinishedItem(): DelegateAdapterItem {
        return OngoingItem(
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
            warehouses
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
            warehouses
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
        setCheckBoxStateStatus(false)
        setDeleteStateStatus(false)
    }

    fun getCampaignStatus(): FlashSaleStatus {
        return this.campaignStatus
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
}