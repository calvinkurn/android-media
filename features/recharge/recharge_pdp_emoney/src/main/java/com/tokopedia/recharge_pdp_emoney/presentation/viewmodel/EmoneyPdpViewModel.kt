package com.tokopedia.recharge_pdp_emoney.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
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
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * @author by jessica on 01/04/21
 */
class EmoneyPdpViewModel @Inject constructor(private val dispatcher: CoroutineDispatcher,
                                             private val userSession: UserSessionInterface,
                                             private val rechargeCatalogPrefixSelectUseCase: RechargeCatalogPrefixSelectUseCase,
                                             private val rechargeCatalogProductInputUseCase: RechargeCatalogProductInputUseCase)
    : BaseViewModel(dispatcher) {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _catalogPrefixSelect = MutableLiveData<Result<TelcoCatalogPrefixSelect>>()
    val catalogPrefixSelect: LiveData<Result<TelcoCatalogPrefixSelect>>
        get() = _catalogPrefixSelect

    private val _selectedOperator = MutableLiveData<RechargePrefix>()
    val selectedOperator: LiveData<RechargePrefix>
        get() = _selectedOperator

    private val _catalogData = MutableLiveData<Result<CatalogData>>()
    val catalogData: LiveData<Result<CatalogData>>
        get() = _catalogData

    fun setErrorMessage(e: Throwable) {
        _errorMessage.postValue(e.message)
    }

    fun getPrefixOperator(menuId: Int) {
        rechargeCatalogPrefixSelectUseCase.execute(
                RechargeCatalogPrefixSelectUseCase.createParams(menuId),
                {
                    //on success get prefix
                    _catalogPrefixSelect.postValue(Success(it))
                },
                {
                    //on fail get prefix
                    _catalogPrefixSelect.postValue(Fail(it))
                }
        )
    }

    fun getSelectedOperator(inputNumber: String) {
        if (inputNumber.isEmpty()) return

        try {
            if (catalogPrefixSelect.value is Success) {
                val operatorSelected = (catalogPrefixSelect.value as Success).data.rechargeCatalogPrefixSelect.prefixes.single {
                    inputNumber.startsWith(it.value)
                }
                _selectedOperator.postValue(operatorSelected)
            }
        } catch (e: Throwable) {

        }
    }

    fun getProductFromOperator(menuId: Int, operatorId: String) {
        rechargeCatalogProductInputUseCase.execute(
                RechargeCatalogProductInputUseCase.createProductListParams(menuId, operatorId),
                {
                    //on success get prefix
                    _catalogData.postValue(Success(it.response))
                },
                {
                    //on fail get prefix
                    _catalogData.postValue(Fail(it))
                }
        )
    }

    fun generateCheckoutPassData(product: CatalogProduct, copiedPromoCode: String, clientNumber: String): DigitalCheckoutPassData {
        val checkoutPassData = DigitalCheckoutPassData()
        checkoutPassData.idemPotencyKey = userSession.userId.generateRechargeCheckoutToken()
        checkoutPassData.voucherCodeCopied = copiedPromoCode
        checkoutPassData.clientNumber = clientNumber
        checkoutPassData.operatorId = product.id
        return checkoutPassData
    }

}