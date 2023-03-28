package com.tokopedia.shopdiscount.manage_product_discount.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shopdiscount.bulk.data.response.GetSlashPriceBenefitResponse
import com.tokopedia.shopdiscount.bulk.domain.usecase.GetSlashPriceBenefitUseCase
import com.tokopedia.shopdiscount.info.data.uimodel.ShopDiscountSellerInfoUiModel
import com.tokopedia.shopdiscount.info.util.ShopDiscountSellerInfoMapper
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.NO_ERROR
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.START_DATE_ERROR
import com.tokopedia.shopdiscount.utils.constant.DateConstant.FIVE_MINUTES
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.ERROR_PRICE_MAX
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.ERROR_PRICE_MIN
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.ERROR_R2_ABUSIVE
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.ERROR_START_DATE
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.NONE
import com.tokopedia.shopdiscount.utils.extension.minutesToMillis
import com.tokopedia.shopdiscount.utils.extension.toCalendar
import com.tokopedia.shopdiscount.utils.extension.unixToMs
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import kotlin.math.round

class ShopDiscountManageProductViewModel @Inject constructor(
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

    val updatedDiscountPercentageData: LiveData<Int>
        get() = _updatedDiscountPercentageData
    private val _updatedDiscountPercentageData = MutableLiveData<Int>()

    val updatedDiscountPriceData: LiveData<Int>
        get() = _updatedDiscountPriceData
    private val _updatedDiscountPriceData = MutableLiveData<Int>()

    val inputValidation: LiveData<Int>
        get() = _inputValidation
    private val _inputValidation = MutableLiveData<Int>()

    val discountPeriodDataBasedOnBenefitLiveData: LiveData<Pair<Date, Date>>
        get() = _discountPeriodDataBasedOnBenefitLiveData
    private val _discountPeriodDataBasedOnBenefitLiveData = MutableLiveData<Pair<Date, Date>>()

    private var productData: ShopDiscountSetupProductUiModel.SetupProductData =
        ShopDiscountSetupProductUiModel.SetupProductData()

    private val defaultStartDate: Date by lazy {
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

    fun updateProductDiscountPeriodData(startDate: Date, endDate: Date) {
        productData.slashPriceInfo.apply {
            this.startDate = startDate
            this.endDate = endDate
        }
        if (productData.productStatus.errorType == START_DATE_ERROR) {
            if (isStartDateError(productData)) {
                productData.productStatus.errorType = START_DATE_ERROR
            } else {
                productData.productStatus.errorType = NO_ERROR
            }
        }
        _updatedDiscountPeriodData.postValue(productData)
    }

    private fun isStartDateError(
        setupProductUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Boolean {
        return checkProductStartDateError(setupProductUiModel)
    }

    private fun checkProductStartDateError(
        setupProductUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Boolean {
        return (setupProductUiModel.slashPriceInfo.startDate.time - Date().time) < FIVE_MINUTES.minutesToMillis()
    }

    private fun getVpsPackageDefaultEndDate(slashPriceBenefitData: ShopDiscountSellerInfoUiModel): Date {
        val vpsPackageData = slashPriceBenefitData.listSlashPriceBenefitData.firstOrNull {
            it.packageId.toIntOrZero() != -1
        }
        return Date(vpsPackageData?.expiredAtUnix?.unixToMs().orZero())
    }

    private fun getMembershipDefaultEndDate(): Date {
        val endDateCalendar = defaultStartDate.toCalendar()
        endDateCalendar.add(Calendar.YEAR, ONE_YEAR)
        return endDateCalendar.time
    }

    fun updateDiscountPrice(discountPrice: Int) {
        val originalPrice = productData.mappedResultData.minOriginalPrice.orZero()
        val discountedPrice = (originalPrice - discountPrice).toDouble()
        var discountPercentage = round((discountedPrice) / originalPrice * 100f).toInt()
        updateProductDataDiscountPrice(discountPrice, discountPercentage)
        discountPercentage = when {
            discountPercentage < 0 -> {
                0
            }
            discountPercentage > 99 -> {
                99
            }
            else -> {
                discountPercentage
            }
        }
        _updatedDiscountPercentageData.postValue(discountPercentage)
    }

    fun updateDiscountPercent(discountPercent: Int) {
        val originalPrice = productData.mappedResultData.minOriginalPrice.orZero()
        val discountPrice = ((100 - discountPercent).toDouble() / 100 * originalPrice).toInt()
        updateProductDataDiscountPrice(discountPrice, discountPercent)
        _updatedDiscountPriceData.postValue(discountPrice)
    }

    fun updateMaxOrderData(maxOrderValue: Int) {
        productData.listProductWarehouse.forEach {
            it.maxOrder = maxOrderValue.toString()
        }
    }

    private fun updateProductDataDiscountPrice(discountPrice: Int, discountPercent: Int) {
        productData.slashPriceInfo.discountedPrice = discountPrice
        productData.slashPriceInfo.discountPercentage = discountPercent
        productData.listProductWarehouse.forEach {
            it.discountedPrice = discountPrice
            it.discountedPercentage = discountPercent
        }
    }

    fun validateInput() {
        productData.let {
            val minDiscountPrice = getMinDiscountPrice()
            val maxDiscountPrice = getMaxDiscountPrice()
            val discountedPrice = it.slashPriceInfo.discountedPrice
            val averageSoldPrice = it.listProductWarehouse.firstOrNull()?.avgSoldPrice.orZero()
            val errorValidation = when {
                discountedPrice > maxDiscountPrice -> {
                    ERROR_PRICE_MAX
                }
                discountedPrice < minDiscountPrice -> {
                    ERROR_PRICE_MIN
                }
                discountedPrice > averageSoldPrice && averageSoldPrice.isMoreThanZero() -> {
                    ERROR_R2_ABUSIVE
                }
                productData.productStatus.errorType == START_DATE_ERROR -> {
                    ERROR_START_DATE
                }
                else -> {
                    NONE
                }
            }
            _inputValidation.postValue(errorValidation)
        }
    }

    fun getMinDiscountPrice(): Int {
        val originalPrice = productData.mappedResultData.minOriginalPrice.orZero()
        val minDiscountPrice = (originalPrice.toDouble() * 0.01).toInt()
        return if (minDiscountPrice < 100) {
            100
        } else {
            minDiscountPrice
        }
    }

    fun getMaxDiscountPrice(): Int {
        val originalPrice = productData.mappedResultData.maxOriginalPrice.orZero()
        return (originalPrice.toDouble() * 0.99).toInt()
    }

    fun getProductData(): ShopDiscountSetupProductUiModel.SetupProductData {
        return productData
    }

    fun getDiscountPeriodDataBasedOnBenefit(slashPriceBenefitData: ShopDiscountSellerInfoUiModel) {
        val startDate = defaultStartDate
        val endDate = if (slashPriceBenefitData.isUseVps) {
            getVpsPackageDefaultEndDate(slashPriceBenefitData)
        } else {
            getMembershipDefaultEndDate()
        }
        _discountPeriodDataBasedOnBenefitLiveData.postValue(Pair(startDate, endDate))
    }
}
