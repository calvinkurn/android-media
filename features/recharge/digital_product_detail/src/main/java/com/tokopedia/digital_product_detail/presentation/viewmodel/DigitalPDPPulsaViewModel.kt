package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.topupbills.data.favorite_number_perso.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.data.prefix_select.RechargeCatalogPrefixSelect
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPRepository
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.utils.generateRechargeCheckoutToken
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.config.GlobalConfig
import com.tokopedia.digital_product_detail.data.model.data.SelectedGridProduct
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomMCCMModel
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.model.denom.MenuDetailModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.*
import java.util.regex.Pattern
import javax.inject.Inject

class DigitalPDPPulsaViewModel @Inject constructor(
    val repo: DigitalPDPRepository,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    private var loadingJob: Job? = null
    var operatorData: TelcoCatalogPrefixSelect = TelcoCatalogPrefixSelect(
        RechargeCatalogPrefixSelect()
    )

    var isEligibleToBuy = false

    var selectedGridProduct = SelectedGridProduct()

    val digitalCheckoutPassData = DigitalCheckoutPassData.Builder()
        .action(DigitalCheckoutPassData.DEFAULT_ACTION)
        .instantCheckout("0")
        .utmContent(GlobalConfig.VERSION_NAME)
        .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
        .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
        .voucherCodeCopied("")
        .isFromPDP(true).build()

    private val _menuDetailData = MutableLiveData<RechargeNetworkResult<MenuDetailModel>>()
    val menuDetailData: LiveData<RechargeNetworkResult<MenuDetailModel>>
        get() = _menuDetailData

    private val _favoriteNumberData = MutableLiveData<RechargeNetworkResult<Pair<List<TopupBillsPersoFavNumberItem>, Boolean>>>()
    val favoriteNumberData: LiveData<RechargeNetworkResult<Pair<List<TopupBillsPersoFavNumberItem>, Boolean>>>
        get() = _favoriteNumberData

    private val _catalogProductInput = MutableLiveData<Result<List<DenomWidgetModel>>>()
    val catalogProductInput: LiveData<Result<List<DenomWidgetModel>>>
        get() = _catalogProductInput

    private val _catalogPrefixSelect = MutableLiveData<RechargeNetworkResult<TelcoCatalogPrefixSelect>>()
    val catalogPrefixSelect: LiveData<RechargeNetworkResult<TelcoCatalogPrefixSelect>>
        get() = _catalogPrefixSelect

    private val _observableDenomMCCMData = MutableLiveData<RechargeNetworkResult<DenomMCCMModel>>()
    val observableDenomMCCMData: LiveData<RechargeNetworkResult<DenomMCCMModel>>
        get() = _observableDenomMCCMData

    private val _addToCartResult = MutableLiveData<RechargeNetworkResult<String>>()
    val addToCartResult: LiveData<RechargeNetworkResult<String>>
        get() = _addToCartResult

    private val _clientNumberValidatorMsg = MutableLiveData<String>()
    val clientNumberValidatorMsg: LiveData<String>
        get() = _clientNumberValidatorMsg

    fun getMenuDetail(menuId: Int, isLoadFromCloud: Boolean = false) {
        _menuDetailData.postValue(RechargeNetworkResult.Loading)
        viewModelScope.launchCatchError(dispatchers.io, block = {
            val menuDetail = repo.getMenuDetail(menuId, isLoadFromCloud)
            _menuDetailData.postValue(RechargeNetworkResult.Success(menuDetail))
        }) {
            _menuDetailData.postValue(RechargeNetworkResult.Fail(it))
        }
    }

    fun getRechargeCatalogInput(menuId: Int, operator: String){
        _observableDenomMCCMData.postValue(RechargeNetworkResult.Loading)
        launchCatchError(block = {
            val denomGrid = repo.getDenomGridList(menuId, operator)
            _observableDenomMCCMData.postValue(RechargeNetworkResult.Success(denomGrid))
        }){
            _observableDenomMCCMData.postValue(RechargeNetworkResult.Fail(it))
        }
    }

    fun getFavoriteNumber(categoryIds: List<Int>, shouldRefreshInputNumber: Boolean) {
        _favoriteNumberData.postValue(RechargeNetworkResult.Loading)
        viewModelScope.launchCatchError(dispatchers.io, block = {
            val favoriteNumber = repo.getFavoriteNumber(categoryIds)
            _favoriteNumberData.postValue(RechargeNetworkResult.Success(
                favoriteNumber.persoFavoriteNumber.items to shouldRefreshInputNumber))
        }) {
            _favoriteNumberData.postValue(RechargeNetworkResult.Fail(it))
        }
    }

    fun getPrefixOperator(menuId: Int) {
        _catalogPrefixSelect.postValue(RechargeNetworkResult.Loading)
        viewModelScope.launchCatchError(dispatchers.io, block = {
            operatorData = repo.getOperatorList(menuId)
            delay(DELAY_TIME)
            _catalogPrefixSelect.postValue(RechargeNetworkResult.Success(operatorData))
        }) {
            _catalogPrefixSelect.postValue(RechargeNetworkResult.Fail(it))
        }
    }

    fun addToCart(digitalCheckoutPassData: DigitalCheckoutPassData,
                  digitalIdentifierParam: RequestBodyIdentifier,
                  digitalSubscriptionParams: DigitalSubscriptionParams,
                  userId: String
    ){
        _addToCartResult.postValue(RechargeNetworkResult.Loading)
        viewModelScope.launchCatchError(dispatchers.io, block = {
            val categoryIdAtc = repo.addToCart(digitalCheckoutPassData, digitalIdentifierParam, digitalSubscriptionParams, userId)
            _addToCartResult.postValue(RechargeNetworkResult.Success(categoryIdAtc))
        }) {
            _addToCartResult.postValue(RechargeNetworkResult.Fail(it))
        }
    }

    fun updateCheckoutPassData(denomData: DenomData, idemPotencyKeyActive: String, clientNumberWidget: String, operatorActiveId: String){
        digitalCheckoutPassData.apply {
            categoryId = denomData.categoryId
            clientNumber = clientNumberWidget
            isPromo = denomData.promoStatus
            operatorId = operatorActiveId
            productId = denomData.id
            utmCampaign = denomData.categoryId
            isSpecialProduct = denomData.isSpecialPromo
            idemPotencyKey = idemPotencyKeyActive
        }
    }

    fun updateCategoryCheckoutPassData(categoryId: String){
        digitalCheckoutPassData.categoryId = categoryId
    }

    fun validateClientNumber(clientNumber: String) {
        loadingJob?.cancel()
        loadingJob = viewModelScope.launch {
            var errorMessage = ""
            for (validation in operatorData.rechargeCatalogPrefixSelect.validations) {
                val phoneIsValid = Pattern.compile(validation.rule)
                    .matcher(clientNumber).matches()
                if (!phoneIsValid) {
                    errorMessage = validation.message
                }
            }
            isEligibleToBuy = errorMessage.isEmpty()
            delay(VALIDATOR_DELAY_TIME)
            _clientNumberValidatorMsg.postValue(errorMessage)
        }
    }

    fun onResetSelectedProduct(){
        selectedGridProduct = SelectedGridProduct()
    }

    fun getSelectedPositionId(listDenomData: List<DenomData>, id: String): Int?{
        var selectedProductPositionId : Int? = null
        listDenomData.forEachIndexed { index, denomData ->
            if (denomData.id.equals(id, false)) selectedProductPositionId = index
        }
        if (selectedProductPositionId == null) onResetSelectedProduct()
        return selectedProductPositionId
    }

    companion object {
        const val DELAY_TIME = 200L
        const val VALIDATOR_DELAY_TIME = 3000L
    }
}