package com.tokopedia.shopdiscount.manage_product_discount.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shopdiscount.bulk.data.response.GetSlashPriceBenefitResponse
import com.tokopedia.shopdiscount.bulk.domain.entity.DiscountSettings
import com.tokopedia.shopdiscount.bulk.domain.entity.DiscountType
import com.tokopedia.shopdiscount.bulk.domain.usecase.GetSlashPriceBenefitUseCase
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.shopdiscount.info.data.uimodel.ShopDiscountSellerInfoUiModel
import com.tokopedia.shopdiscount.info.util.ShopDiscountSellerInfoMapper
import com.tokopedia.shopdiscount.manage_product_discount.data.uimodel.ShopDiscountManageProductVariantItemUiModel
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.ERROR_PRICE_MAX
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.ERROR_PRICE_MIN
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.NONE
import com.tokopedia.shopdiscount.utils.constant.SlashPriceStatusId
import com.tokopedia.shopdiscount.utils.extension.toCalendar
import com.tokopedia.shopdiscount.utils.extension.unixToMs
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import java.util.*
import javax.inject.Inject
import kotlin.math.round

class ShopDiscountManageProductVariantDiscountViewModel @Inject constructor(
    private val dispatcherProvider: CoroutineDispatchers,
    private val getSlashPriceBenefitUseCase: GetSlashPriceBenefitUseCase
) : BaseViewModel(dispatcherProvider.main) {

    companion object {
        private const val START_TIME_OFFSET_IN_MINUTES = 10
        private const val ONE_YEAR = 1
    }

    val slashPriceBenefitLiveData: LiveData<Result<ShopDiscountSellerInfoUiModel>>
        get() = _sellerInfoBenefitLiveData
    private val _sellerInfoBenefitLiveData =
        MutableLiveData<Result<ShopDiscountSellerInfoUiModel>>()

    val updatedDiscountPeriodData: LiveData<ShopDiscountSetupProductUiModel.SetupProductData>
        get() = _updatedDiscountPeriodData
    private val _updatedDiscountPeriodData =
        MutableLiveData<ShopDiscountSetupProductUiModel.SetupProductData>()

    val updatedDiscountPercentageData: LiveData<ShopDiscountSetupProductUiModel.SetupProductData>
        get() = _updatedDiscountPercentageData
    private val _updatedDiscountPercentageData =
        MutableLiveData<ShopDiscountSetupProductUiModel.SetupProductData>()

    val updatedDiscountPriceData: LiveData<Int>
        get() = _updatedDiscountPriceData
    private val _updatedDiscountPriceData = MutableLiveData<Int>()

    val inputValidation: LiveData<Int>
        get() = _inputValidation
    private val _inputValidation = MutableLiveData<Int>()

    val isEnableSubmitButton: LiveData<Boolean>
        get() = _isEnableSubmitButton
    private val _isEnableSubmitButton = MutableLiveData<Boolean>()

    val isEnableBulkApplyVariant: LiveData<Int>
        get() = _isEnableBulkApplyVariant
    private val _isEnableBulkApplyVariant = MutableLiveData<Int>()

    val onToggleVariantStateUpdated: LiveData<ShopDiscountSetupProductUiModel.SetupProductData>
        get() = _onToggleVariantStateUpdated
    private val _onToggleVariantStateUpdated =
        MutableLiveData<ShopDiscountSetupProductUiModel.SetupProductData>()

    val bulkApplyListVariantItemUiModel: LiveData<List<ShopDiscountManageProductVariantItemUiModel>>
        get() = _bulkApplyListVariantItemUiModel
    private val _bulkApplyListVariantItemUiModel =
        MutableLiveData<List<ShopDiscountManageProductVariantItemUiModel>>()

    private var productData: ShopDiscountSetupProductUiModel.SetupProductData =
        ShopDiscountSetupProductUiModel.SetupProductData()

    val defaultStartDate: Date by lazy {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, START_TIME_OFFSET_IN_MINUTES)
        calendar.time
    }

    fun getSlashPriceBenefit() {
        launchCatchError(dispatcherProvider.io, block = {
            val response = getSlashPriceBenefitData()
            val mappedUiModel =
                ShopDiscountSellerInfoMapper.mapToShopDiscountSellerInfoBenefitUiModel(response)
            _sellerInfoBenefitLiveData.postValue(Success(mappedUiModel))
        }) {
            _sellerInfoBenefitLiveData.postValue(Fail(it))
        }
    }

    private suspend fun getSlashPriceBenefitData(): GetSlashPriceBenefitResponse {
        getSlashPriceBenefitUseCase.setParams()
        return getSlashPriceBenefitUseCase.executeOnBackground()
    }

    fun setProductData(productData: ShopDiscountSetupProductUiModel.SetupProductData) {
        this.productData = productData
    }

    fun updateProductDiscountPeriodDataBasedOnBenefit(
        startDate: Date,
        endDate: Date
    ) {
        productData.listProductVariant.forEach {
            it.slashPriceInfo.apply {
                this.startDate = startDate
                this.endDate = endDate
            }
        }
    }

    fun getVpsPackageDefaultEndDate(slashPriceBenefitData: ShopDiscountSellerInfoUiModel): Date {
        val vpsPackageData = slashPriceBenefitData.listSlashPriceBenefitData.firstOrNull {
            it.packageId.toIntOrZero() != -1
        }
        return Date(vpsPackageData?.expiredAtUnix?.unixToMs().orZero())
    }

    fun getMembershipDefaultEndDate(): Date {
        val endDateCalendar = defaultStartDate.toCalendar()
        endDateCalendar.add(Calendar.YEAR, ONE_YEAR)
        return endDateCalendar.time
    }

    fun getMinDiscountPrice(): Int {
        val originalPrice = productData.mappedResultData.minOriginalPrice.orZero()
        return (originalPrice.toDouble() * 0.01).toInt()
    }

    fun getMaxDiscountPrice(): Int {
        val originalPrice = productData.mappedResultData.minOriginalPrice.orZero()
        return (originalPrice.toDouble() * 0.99).toInt()
    }

    fun getProductData(): ShopDiscountSetupProductUiModel.SetupProductData {
        return productData
    }

    fun checkShouldEnableSubmitButton(listVariantItemUiModel: List<ShopDiscountManageProductVariantItemUiModel>) {
        val isNoVariantEnabled = listVariantItemUiModel.none {
            it.isEnabled
        }
        val isEnabledButtonSubmit: Boolean = if (isNoVariantEnabled) {
            false
        } else {
            listVariantItemUiModel.filter {
                it.isEnabled
            }.all {
                it.errorType == NONE && !it.discountedPrice.isZero()
            }
        }
        _isEnableSubmitButton.postValue(isEnabledButtonSubmit)
    }

    fun updateProductDataBasedOnVariantUiModel(listVariantUiModel: List<ShopDiscountManageProductVariantItemUiModel>) {
        productData.listProductVariant.forEach { variantProductData ->
            getMatchedVariantUiModel(
                variantProductData.productId,
                listVariantUiModel
            )?.let { matchedVariantUiModel ->
                variantProductData.variantStatus.isVariantEnabled = matchedVariantUiModel.isEnabled
                if (matchedVariantUiModel.isEnabled) {
                    variantProductData.slashPriceInfo.apply {
                        startDate = matchedVariantUiModel.startDate
                        endDate = matchedVariantUiModel.endDate
                        discountPercentage = matchedVariantUiModel.discountedPercentage
                        discountedPrice = matchedVariantUiModel.discountedPrice
                    }
                    variantProductData.listProductWarehouse.forEach {
                        it.discountedPrice = matchedVariantUiModel.discountedPrice
                        it.discountedPercentage = matchedVariantUiModel.discountedPercentage
                        it.maxOrder = matchedVariantUiModel.maxOrder
                    }
                }
            }
        }
    }

    private fun getMatchedVariantUiModel(
        variantId: String,
        listVariantUiModel: List<ShopDiscountManageProductVariantItemUiModel>
    ): ShopDiscountManageProductVariantItemUiModel? {
        return listVariantUiModel.firstOrNull {
            it.variantId == variantId
        }
    }

    fun checkShouldEnableBulkApplyVariant(
        listVariantItemUiModel: List<ShopDiscountManageProductVariantItemUiModel>
    ) {
        val totalEnabledVariant = listVariantItemUiModel.count {
            it.isEnabled
        }
        _isEnableBulkApplyVariant.postValue(totalEnabledVariant)
    }

    fun applyBulkUpdate(
        listVariantItemUiModel: List<ShopDiscountManageProductVariantItemUiModel>,
        bulkApplyDiscountResult: DiscountSettings,
        slashPriceStatus: Int
    ) {
        launchCatchError(dispatcherProvider.io, {
            listVariantItemUiModel.filter { it.isEnabled }.forEach { variantProductData ->
                val minOriginalPrice = variantProductData.variantMinOriginalPrice
                updateProductData(
                    variantProductData,
                    bulkApplyDiscountResult,
                    minOriginalPrice,
                    slashPriceStatus
                )
            }
            _bulkApplyListVariantItemUiModel.postValue(listVariantItemUiModel)
        }) {}
    }

    private fun updateProductData(
        productData: ShopDiscountManageProductVariantItemUiModel,
        bulkApplyDiscountResult: DiscountSettings,
        minOriginalPrice: Int,
        slashPriceStatus: Int
    ) {
        productData.apply {
            val discountedPrice: Int
            val discountedPercentage: Int
            when (bulkApplyDiscountResult.discountType) {
                DiscountType.RUPIAH -> {
                    val diffPrice = minOriginalPrice - bulkApplyDiscountResult.discountAmount
                    discountedPrice = this.variantMinOriginalPrice - diffPrice
                    discountedPercentage =
                        round(diffPrice.toDouble() / this.variantMinOriginalPrice.toDouble() * 100f).toInt()
                }
                DiscountType.PERCENTAGE -> {
                    discountedPercentage = bulkApplyDiscountResult.discountAmount
                    discountedPrice =
                        (100 - discountedPercentage) * this.variantMinOriginalPrice / 100
                }
            }
            when (slashPriceStatus) {
                SlashPriceStatusId.CREATE, SlashPriceStatusId.SCHEDULED -> {
                    this.startDate = bulkApplyDiscountResult.startDate ?: Date()
                }
            }
            this.endDate = bulkApplyDiscountResult.endDate ?: Date()
            this.discountedPrice = discountedPrice
            this.discountedPercentage = discountedPercentage
            this.maxOrder = bulkApplyDiscountResult.maxPurchaseQuantity.toString()
        }
    }

}