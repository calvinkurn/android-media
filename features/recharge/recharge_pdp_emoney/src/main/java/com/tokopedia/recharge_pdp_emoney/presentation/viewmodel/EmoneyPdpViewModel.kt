package com.tokopedia.recharge_pdp_emoney.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.data.prefix_select.RechargePrefix
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.product.CatalogData
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.common.topupbills.usecase.RechargeCatalogPrefixSelectUseCase
import com.tokopedia.common.topupbills.usecase.RechargeCatalogProductInputUseCase
import com.tokopedia.common.topupbills.utils.generateRechargeCheckoutToken
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by jessica on 01/04/21
 */
class EmoneyPdpViewModel @Inject constructor(private val userSession: UserSessionInterface,
                                             private val rechargeCatalogPrefixSelectUseCase: RechargeCatalogPrefixSelectUseCase,
                                             private val rechargeCatalogProductInputUseCase: RechargeCatalogProductInputUseCase)
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
                                 selectedOperatorId: String? = null): DigitalCheckoutPassData {
        val checkoutPassData = DigitalCheckoutPassData().apply {
            idemPotencyKey = userSession.userId.generateRechargeCheckoutToken()
            voucherCodeCopied = copiedPromoCode
            this.clientNumber = clientNumber
            productId = selectedProductId ?: selectedProduct.value?.id
            operatorId = selectedOperatorId ?: selectedOperator.value?.key
            isFromPDP = true
        }

        digitalCheckoutPassData = checkoutPassData
        return digitalCheckoutPassData
    }

    companion object {
        const val ERROR_GRPC_TIMEOUT = "grpc timeout"
    }
}