package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.topupbills.data.prefix_select.RechargeCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.favoritepdp.domain.model.AutoCompleteModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteChipModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.MenuDetailModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.PrefillModel
import com.tokopedia.common.topupbills.favoritepdp.util.FavoriteNumberType
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.atc.data.response.ErrorAtc
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.DigitalAtcErrorException
import com.tokopedia.config.GlobalConfig
import com.tokopedia.digital_product_detail.data.model.data.DigitalAtcResult
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.CHECKOUT_NO_PROMO
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.DELAY_CLIENT_NUMBER_TRANSITION
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.DELAY_MULTI_TAB
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.DELAY_PREFIX_TIME
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.VALIDATOR_DELAY_TIME
import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPTelcoRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomMCCMModel
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationWidgetModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

class DigitalPDPPulsaViewModel @Inject constructor(
    val repo: DigitalPDPTelcoRepository,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    var validatorJob: Job? = null
    var catalogProductJob: Job? = null
    var recommendationJob: Job? = null
    var clientNumberThrottleJob: Job? = null
    var operatorData: TelcoCatalogPrefixSelect =
        TelcoCatalogPrefixSelect(RechargeCatalogPrefixSelect())
    var isEligibleToBuy = false
    var selectedGridProduct = SelectedProduct()
    var recomCheckoutUrl = ""

    val digitalCheckoutPassData = DigitalCheckoutPassData.Builder()
        .action(DigitalCheckoutPassData.DEFAULT_ACTION)
        .instantCheckout(CHECKOUT_NO_PROMO)
        .utmContent(GlobalConfig.VERSION_NAME)
        .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
        .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
        .voucherCodeCopied("")
        .isFromPDP(true).build()

    private val _menuDetailData = MutableLiveData<RechargeNetworkResult<MenuDetailModel>>()
    val menuDetailData: LiveData<RechargeNetworkResult<MenuDetailModel>>
        get() = _menuDetailData

    private val _favoriteChipsData =
        MutableLiveData<RechargeNetworkResult<List<FavoriteChipModel>>>()
    val favoriteChipsData: LiveData<RechargeNetworkResult<List<FavoriteChipModel>>>
        get() = _favoriteChipsData

    private val _autoCompleteData =
        MutableLiveData<RechargeNetworkResult<List<AutoCompleteModel>>>()
    val autoCompleteData: LiveData<RechargeNetworkResult<List<AutoCompleteModel>>>
        get() = _autoCompleteData

    private val _prefillData =
        MutableLiveData<RechargeNetworkResult<PrefillModel>>()
    val prefillData: LiveData<RechargeNetworkResult<PrefillModel>>
        get() = _prefillData

    private val _catalogPrefixSelect =
        MutableLiveData<RechargeNetworkResult<TelcoCatalogPrefixSelect>>()
    val catalogPrefixSelect: LiveData<RechargeNetworkResult<TelcoCatalogPrefixSelect>>
        get() = _catalogPrefixSelect

    private val _observableDenomMCCMData = MutableLiveData<RechargeNetworkResult<DenomMCCMModel>>()
    val observableDenomMCCMData: LiveData<RechargeNetworkResult<DenomMCCMModel>>
        get() = _observableDenomMCCMData

    private val _addToCartResult = MutableLiveData<RechargeNetworkResult<DigitalAtcResult>>()
    val addToCartResult: LiveData<RechargeNetworkResult<DigitalAtcResult>>
        get() = _addToCartResult

    private val _errorAtc = MutableLiveData<ErrorAtc>()
    val errorAtc: LiveData<ErrorAtc>
        get() = _errorAtc

    private val _clientNumberValidatorMsg = MutableLiveData<String>()
    val clientNumberValidatorMsg: LiveData<String>
        get() = _clientNumberValidatorMsg

    private val _recommendationData =
        MutableLiveData<RechargeNetworkResult<RecommendationWidgetModel>>()
    val recommendationData: LiveData<RechargeNetworkResult<RecommendationWidgetModel>>
        get() = _recommendationData

    fun setMenuDetailLoading() {
        _menuDetailData.value = RechargeNetworkResult.Loading
    }

    fun getMenuDetail(menuId: Int) {
        viewModelScope.launchCatchError(dispatchers.main, block = {
            val menuDetail = repo.getMenuDetail(menuId)
            _menuDetailData.value = RechargeNetworkResult.Success(menuDetail)
        }) {
            _menuDetailData.value = RechargeNetworkResult.Fail(it)
        }
    }

    fun setRechargeCatalogInputMultiTabLoading() {
        _observableDenomMCCMData.value = RechargeNetworkResult.Loading
    }

    fun getRechargeCatalogInputMultiTab(menuId: Int, operator: String, clientNumber: String) {
        catalogProductJob = viewModelScope.launchCatchError(dispatchers.main, block = {
            delay(DELAY_MULTI_TAB)
            val denomGrid = repo.getProductInputMultiTabDenomGrid(menuId, operator, clientNumber)
            _observableDenomMCCMData.value = RechargeNetworkResult.Success(denomGrid)
        }) {
            if (it !is CancellationException)
                _observableDenomMCCMData.value = RechargeNetworkResult.Fail(it)
        }
    }

    fun setFavoriteNumberLoading() {
        _favoriteChipsData.value = RechargeNetworkResult.Loading
    }

    fun getFavoriteNumbers(categoryIds: List<Int>, favoriteNumberTypes: List<FavoriteNumberType>) {
        viewModelScope.launchCatchError(dispatchers.main, block = {
            val data = repo.getFavoriteNumbers(favoriteNumberTypes, categoryIds)
            for (type in favoriteNumberTypes) {
                when (type) {
                    FavoriteNumberType.PREFILL -> {
                        _prefillData.value = RechargeNetworkResult.Success(data.prefill)
                    }
                    FavoriteNumberType.CHIP -> {
                        _favoriteChipsData.value = RechargeNetworkResult.Success(data.favoriteChips)
                    }
                    FavoriteNumberType.LIST -> {
                        _autoCompleteData.value = RechargeNetworkResult.Success(data.autoCompletes)
                    }
                }
            }
        }) {
            // this section is not reachable due to no fail scenario
        }
    }

    fun setPrefixOperatorLoading() {
        _catalogPrefixSelect.value = RechargeNetworkResult.Loading
    }

    fun getPrefixOperator(menuId: Int) {
        viewModelScope.launchCatchError(dispatchers.main, block = {
            delay(DELAY_PREFIX_TIME)
            operatorData = repo.getOperatorList(menuId)
            _catalogPrefixSelect.value = RechargeNetworkResult.Success(operatorData)
        }) {
            _catalogPrefixSelect.value = RechargeNetworkResult.Fail(it)
        }
    }

    fun cancelCatalogProductJob() {
        catalogProductJob?.cancel()
    }

    fun cancelRecommendationJob() {
        recommendationJob?.cancel()
    }

    fun cancelValidatorJob() {
        validatorJob?.cancel()
    }

    fun setAddToCartLoading() {
        _addToCartResult.value = RechargeNetworkResult.Loading
    }

    fun addToCart(
        digitalIdentifierParam: RequestBodyIdentifier,
        digitalSubscriptionParams: DigitalSubscriptionParams,
        userId: String,
        isUseGql: Boolean
    ) {
        viewModelScope.launchCatchError(dispatchers.main, block = {
            val categoryIdAtc = repo.addToCart(
                digitalCheckoutPassData,
                digitalIdentifierParam,
                digitalSubscriptionParams,
                userId,
                isUseGql
            )
            if (categoryIdAtc.errorAtc == null){
                _addToCartResult.value = RechargeNetworkResult.Success(categoryIdAtc)
            }else{
                _errorAtc.value = categoryIdAtc.errorAtc
            }
        }) {
            if (it is ResponseErrorException && !it.message.isNullOrEmpty()) {
                _addToCartResult.value = RechargeNetworkResult.Fail(MessageErrorException(it.message))
            } else if (it is DigitalAtcErrorException ){
                _errorAtc.value = it.getError()
            } else {
                _addToCartResult.value = RechargeNetworkResult.Fail(it)
            }
        }
    }

    fun setRecommendationLoading() {
        _recommendationData.value = RechargeNetworkResult.Loading
    }

    fun getRecommendations(clientNumbers: List<String>, dgCategoryIds: List<Int>) {
        recommendationJob = viewModelScope.launchCatchError(dispatchers.main, block = {
            delay(DELAY_MULTI_TAB)
            val recommendations = repo.getRecommendations(
                clientNumbers,
                dgCategoryIds,
                emptyList(),
                DigitalPDPConstant.RECOMMENDATION_GQL_CHANNEL_NAME_PULSA,
                true,
            )
            _recommendationData.value = RechargeNetworkResult.Success(recommendations)
        }) {
            _recommendationData.value = RechargeNetworkResult.Fail(it)
        }
    }

    fun updateCheckoutPassData(
        denomData: DenomData,
        idemPotencyKeyActive: String,
        clientNumberWidget: String,
        operatorActiveId: String
    ) {
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

    fun updateCheckoutPassData(recom: RecommendationCardWidgetModel, idemPotencyKeyActive: String) {
        digitalCheckoutPassData.apply {
            categoryId = recom.categoryId
            clientNumber = recom.clientNumber
            isPromo = CHECKOUT_NO_PROMO
            operatorId = recom.operatorId
            productId = recom.productId
            utmCampaign = recom.categoryId
            isSpecialProduct = false
            idemPotencyKey = idemPotencyKeyActive
        }
    }

    fun updateCategoryCheckoutPassData(categoryId: String) {
        digitalCheckoutPassData.categoryId = categoryId
    }

    fun validateClientNumber(clientNumber: String) {
        validatorJob = viewModelScope.launch {
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
            _clientNumberValidatorMsg.value = errorMessage
        }
    }

    fun setAutoSelectedDenom(listDenomData: List<DenomData>, productId: String) {
        var denomData: DenomData? = null
        listDenomData.forEachIndexed { index, activeDenomData ->
            if (productId.equals(activeDenomData.id)) denomData = activeDenomData
        }

        denomData?.let {
            selectedGridProduct = SelectedProduct(it, DenomWidgetEnum.GRID_TYPE, 0)
        }
    }

    fun getSelectedPositionId(listDenomData: List<DenomData>): Int? {
        var selectedProductPositionId: Int? = null
        listDenomData.forEachIndexed { index, denomData ->
            if (denomData.id.equals(selectedGridProduct.denomData.id, false)
                && selectedGridProduct.denomData.id.isNotEmpty()
            ) selectedProductPositionId = index
        }
        return selectedProductPositionId
    }

    fun updateSelectedPositionId(position: Int?) {
        position?.let { selectedGridProduct.position = it }
    }

    fun isAutoSelectedProduct(layoutType: DenomWidgetEnum): Boolean =
        (selectedGridProduct.denomData.id.isNotEmpty()
                && selectedGridProduct.position >= 0
                && selectedGridProduct.denomWidgetEnum == layoutType
                && isEligibleToBuy)

    fun onResetSelectedProduct() {
        selectedGridProduct = SelectedProduct()
    }

    fun isEmptyDenomMCCM(listDenomData: List<DenomData>, listMCCMData: List<DenomData>): Boolean {
        return listDenomData.isEmpty() && listMCCMData.isEmpty()
    }

    fun runThrottleJob(
        skipMs: Long = DELAY_CLIENT_NUMBER_TRANSITION,
        destinationFunction: () -> Unit
    ) {
        if (clientNumberThrottleJob?.isCompleted != false) {
            clientNumberThrottleJob = viewModelScope.launch {
                destinationFunction()
                delay(skipMs)
            }
        }
    }
}