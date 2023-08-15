package com.tokopedia.recharge_pdp_emoney.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.data.prefix_select.RechargePrefix
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.product.CatalogData
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.common.topupbills.usecase.RechargeCatalogPrefixSelectUseCase
import com.tokopedia.common.topupbills.usecase.RechargeCatalogProductInputUseCase
import com.tokopedia.common.topupbills.utils.generateRechargeCheckoutToken
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.usecase.GetDppoConsentUseCase
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.recharge_pdp_emoney.presentation.model.EmoneyDppoConsentModel
import com.tokopedia.recharge_pdp_emoney.utils.EmoneyPdpMapper.mapDppoConsentToEmoneyModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

/**
 * @author by jessica on 01/04/21
 */
class EmoneyPdpViewModel @Inject constructor(
    private val userSession: UserSessionInterface,
    private val rechargeCatalogPrefixSelectUseCase: RechargeCatalogPrefixSelectUseCase,
    private val rechargeCatalogProductInputUseCase: RechargeCatalogProductInputUseCase,
    private val getDppoConsentUseCase: GetDppoConsentUseCase
)
    : ViewModel() {

    private val _inputViewError = MutableLiveData<String>()
    val inputViewError: LiveData<String>
        get() = _inputViewError

    private val _catalogPrefixSelect = MutableLiveData<Result<TelcoCatalogPrefixSelect>>()
    val catalogPrefixSelect: LiveData<Result<TelcoCatalogPrefixSelect>>
        get() = _catalogPrefixSelect

    private val _selectedOperator = MutableLiveData<RechargePrefix>()
    val selectedOperator: LiveData<RechargePrefix>
        get() = _selectedOperator

    private val _selectedRecentNumber = MutableLiveData<TopupBillsRecommendation>()
    val selectedRecentNumber: LiveData<TopupBillsRecommendation>
        get() = _selectedRecentNumber

    private val _selectedProduct = MutableLiveData<CatalogProduct>()
    val selectedProduct: LiveData<CatalogProduct>
        get() = _selectedProduct

    private val _catalogData = MutableLiveData<Result<CatalogData>>()
    val catalogData: LiveData<Result<CatalogData>>
        get() = _catalogData

    private val mutableDppoConsent = MutableLiveData<Result<EmoneyDppoConsentModel>>()
    val dppoConsent: LiveData<Result<EmoneyDppoConsentModel>>
        get() = mutableDppoConsent

    var digitalCheckoutPassData = DigitalCheckoutPassData()

    fun getPrefixOperator(menuId: Int) {
        rechargeCatalogPrefixSelectUseCase.execute(
                RechargeCatalogPrefixSelectUseCase.createParams(menuId),
                {
                    //on success get prefix
                    _catalogPrefixSelect.value = Success(it)
                },
                {
                    //on fail get prefix
                    _catalogPrefixSelect.value = Fail(it)
                }
        )
    }

    fun getSelectedOperator(inputNumber: String, errorNotFoundString: String) {
        resetInputViewError()
        if (inputNumber.isEmpty()) return
        try {
            if (catalogPrefixSelect.value is Success) {
                val operatorSelected = (catalogPrefixSelect.value as Success).data.rechargeCatalogPrefixSelect.prefixes.single {
                    inputNumber.startsWith(it.value)
                }
                _selectedOperator.value = operatorSelected
            } else if (catalogPrefixSelect.value is Fail) {
                _catalogPrefixSelect.value = catalogPrefixSelect.value as Fail
            }
        } catch (e: Throwable) {
            _inputViewError.value = errorNotFoundString
        }
    }

    fun getDppoConsent() {
        viewModelScope.launchCatchError(block = {
            val data = getDppoConsentUseCase.execute(EMONEY_CATEGORY_ID)
            val eventModel = mapDppoConsentToEmoneyModel(data)
            mutableDppoConsent.postValue(Success(eventModel))
        }) {
            mutableDppoConsent.postValue(Fail(it))
        }
    }

    private fun resetInputViewError() {
        _inputViewError.value = ""
    }

    fun getProductFromOperator(menuId: Int, operatorId: String) {
        rechargeCatalogProductInputUseCase.execute(
                RechargeCatalogProductInputUseCase.createProductListParams(menuId, operatorId),
                {
                    //on success get prefix
                    _catalogData.value = Success(it.response)
                },
                {
                    //on fail get prefix
                    _catalogData.value = Fail(it)
                }
        )
    }

    fun setSelectedProduct(product: CatalogProduct) {
        _selectedProduct.value = product
    }

    fun setSelectedRecentNumber(topupBillsRecommendation: TopupBillsRecommendation) {
        _selectedRecentNumber.value = topupBillsRecommendation
    }

    fun generateCheckoutPassData(copiedPromoCode: String, clientNumber: String,
                                 selectedProductId: String? = null,
                                 selectedOperatorId: String? = null,
                                 categoryIdFromPDP: String?
    ): DigitalCheckoutPassData {
        val checkoutPassData = DigitalCheckoutPassData().apply {
            idemPotencyKey = userSession.userId.generateRechargeCheckoutToken()
            voucherCodeCopied = copiedPromoCode
            this.clientNumber = clientNumber
            productId = selectedProductId ?: selectedProduct.value?.id
            operatorId = selectedOperatorId ?: selectedOperator.value?.key
            categoryId = categoryIdFromPDP ?: EMONEY_CATEGORY_ID.toString()
            isFromPDP = true
        }

        digitalCheckoutPassData = checkoutPassData
        return digitalCheckoutPassData
    }

    companion object {
        const val ERROR_GRPC_TIMEOUT = "grpc timeout"
        const val EMONEY_CATEGORY_ID = 34
    }
}
