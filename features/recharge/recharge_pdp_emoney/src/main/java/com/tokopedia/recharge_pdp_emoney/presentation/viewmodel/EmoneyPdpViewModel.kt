package com.tokopedia.recharge_pdp_emoney.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.data.prefix_select.RechargePrefix
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.product.CatalogData
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.common.topupbills.usecase.RechargeCatalogPrefixSelectUseCase
import com.tokopedia.common.topupbills.usecase.RechargeCatalogProductInputUseCase
import com.tokopedia.common.topupbills.utils.generateRechargeCheckoutToken
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * @author by jessica on 01/04/21
 */
class EmoneyPdpViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                             private val userSession: UserSessionInterface,
                                             private val rechargeCatalogPrefixSelectUseCase: RechargeCatalogPrefixSelectUseCase,
                                             private val rechargeCatalogProductInputUseCase: RechargeCatalogProductInputUseCase)
    : BaseViewModel(dispatcher) {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

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

    fun setErrorMessage(e: Throwable) {
        val errorMsg: String = when {
            e is UnknownHostException || e is ConnectException -> ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
            e is SocketTimeoutException -> ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
            e.message.isNullOrEmpty() -> ErrorNetMessage.MESSAGE_ERROR_DEFAULT
            (e.message ?: "").contains("grpc timeout", true) -> ""
            else -> e.message ?: ""
        }
        _errorMessage.value = errorMsg
    }

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
            } else {
                setErrorMessage(MessageErrorException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT))
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
        val checkoutPassData = DigitalCheckoutPassData()
        checkoutPassData.idemPotencyKey = userSession.userId.generateRechargeCheckoutToken()
        checkoutPassData.voucherCodeCopied = copiedPromoCode
        checkoutPassData.clientNumber = clientNumber
        checkoutPassData.productId = selectedProductId ?: selectedProduct.value?.id
        checkoutPassData.operatorId = selectedOperatorId ?: selectedOperator.value?.key
        checkoutPassData.isFromPDP = true
        digitalCheckoutPassData = checkoutPassData
        return digitalCheckoutPassData
    }
}