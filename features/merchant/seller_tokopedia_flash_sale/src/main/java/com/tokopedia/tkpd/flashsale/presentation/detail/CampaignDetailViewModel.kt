package com.tokopedia.tkpd.flashsale.presentation.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tkpd.flashsale.common.extension.*
import com.tokopedia.tkpd.flashsale.data.request.GetFlashSaleSubmittedProductListRequest
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.domain.entity.SubmittedProduct
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleStatus
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleDetailForSellerUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleSubmittedProductListUseCase
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.item.FinishedProcessSelectionItem
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.item.OnProcessSelectionItem
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.item.WaitingForSelectionItem
import com.tokopedia.tkpd.flashsale.util.extension.hoursDifference
import com.tokopedia.tkpd.flashsale.util.extension.minutesDifference
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import java.util.*
import javax.inject.Inject

class CampaignDetailViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getFlashSaleDetailForSellerUseCase: GetFlashSaleDetailForSellerUseCase,
    private val getFlashSaleSubmittedProductListUseCase: GetFlashSaleSubmittedProductListUseCase
) : BaseViewModel(dispatchers.main) {

    private var _campaign = MutableLiveData<Result<FlashSale>>()
    val campaign: LiveData<Result<FlashSale>>
        get() = _campaign

    private var _submittedProduct = MutableLiveData<Result<List<DelegateAdapterItem>>>()
    val submittedProduct: LiveData<Result<List<DelegateAdapterItem>>>
        get() = _submittedProduct

    private var campaignStatus = FlashSaleStatus.NO_REGISTERED_PRODUCT

    companion object {
        private const val TWENTY_FOUR_HOURS = 24
        private const val SIXTY_MINUTES = 60
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
                        10,
                        offset
                    )
                )
                val formattedProductList = formatSubmittedProductList(campaignStatus, result.productList)
                _submittedProduct.postValue(Success(formattedProductList))
            },
            onError = { error ->
                _submittedProduct.postValue(Fail(error))
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
                FlashSaleStatus.ON_SELECTION_PROCESS -> submittedProduct.toOnProcessSelectionItem()
                FlashSaleStatus.SELECTION_FINISHED -> submittedProduct.toFinishedProcessSelectionItem()
                else -> submittedProduct.toWaitingForSelectionItem()
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

    private fun SubmittedProduct.toOnProcessSelectionItem(): DelegateAdapterItem {
        return OnProcessSelectionItem(
            campaignStock,
            isMultiwarehouse,
            isParentProduct,
            mainStock,
            name,
            picture,
            productCriteria,
            productId,
            url,
            price,
            discount,
            discountedPrice,
            warehouses
        )
    }

    private fun SubmittedProduct.toFinishedProcessSelectionItem(): DelegateAdapterItem {
        return FinishedProcessSelectionItem(
            campaignStock,
            isMultiwarehouse,
            isParentProduct,
            mainStock,
            name,
            picture,
            productCriteria,
            productId,
            url,
            price,
            discount,
            discountedPrice,
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

    fun isFlashSaleClosedMoreThan24Hours(targetDate: Date): Boolean {
        val now = Date()
        val distanceHoursToSubmissionEndDate = hoursDifference(now, targetDate)
        return distanceHoursToSubmissionEndDate > TWENTY_FOUR_HOURS
    }

    fun isFlashSaleClosedLessThan24Hour(targetDate: Date): Boolean {
        val now = Date()
        val distanceHoursToSubmissionEndDate = hoursDifference(now, targetDate)
        return distanceHoursToSubmissionEndDate in Int.ZERO..TWENTY_FOUR_HOURS
    }

    fun isFlashSaleClosedLessThan60Minutes(targetDate: Date): Boolean {
        val now = Date()
        val distanceMinutesToSubmissionEndDate = minutesDifference(now, targetDate)
        return distanceMinutesToSubmissionEndDate in Int.ZERO..SIXTY_MINUTES
    }
}