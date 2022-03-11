package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_product_detail.data.model.data.InputMultiTabDenomModel
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPTelcoRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.data.prefix_select.RechargeCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.config.GlobalConfig
import com.tokopedia.digital_product_detail.data.model.data.DigitalAtcResult
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.DELAY_AUTOCOMPLETE
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.CHECKOUT_NO_PROMO
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.DELAY_CLIENT_NUMBER_TRANSITION
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.DELAY_MULTI_TAB
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.DELAY_PREFIX_TIME
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.VALIDATOR_DELAY_TIME
import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.data.model.data.TelcoFilterTagComponent
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.MenuDetailModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationWidgetModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

class DigitalPDPDataPlanViewModel @Inject constructor(
    val repo: DigitalPDPTelcoRepository,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    val filterDataParams = ArrayList<HashMap<String, Any>>()
    var filterData = emptyList<TelcoFilterTagComponent>()
    var validatorJob: Job? = null
    var catalogProductJob: Job? = null
    var recommendationJob: Job? = null
    var clientNumberThrottleJob: Job? = null
    var operatorData: TelcoCatalogPrefixSelect = TelcoCatalogPrefixSelect(RechargeCatalogPrefixSelect())
    var isEligibleToBuy = false
    var selectedFullProduct = SelectedProduct()
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

    private val _favoriteNumberData =
        MutableLiveData<RechargeNetworkResult<List<TopupBillsPersoFavNumberItem>>>()
    val favoriteNumberData: LiveData<RechargeNetworkResult<List<TopupBillsPersoFavNumberItem>>>
        get() = _favoriteNumberData

    private val _autoCompleteData =
        MutableLiveData<RechargeNetworkResult<List<TopupBillsPersoFavNumberItem>>>()
    val autoCompleteData: LiveData<RechargeNetworkResult<List<TopupBillsPersoFavNumberItem>>>
        get() = _autoCompleteData

    private val _catalogPrefixSelect =
        MutableLiveData<RechargeNetworkResult<TelcoCatalogPrefixSelect>>()
    val catalogPrefixSelect: LiveData<RechargeNetworkResult<TelcoCatalogPrefixSelect>>
        get() = _catalogPrefixSelect

    private val _observableDenomMCCMData = MutableLiveData<RechargeNetworkResult<InputMultiTabDenomModel>>()
    val observableDenomMCCMData: LiveData<RechargeNetworkResult<InputMultiTabDenomModel>>
        get() = _observableDenomMCCMData

    private val _addToCartResult = MutableLiveData<RechargeNetworkResult<DigitalAtcResult>>()
    val addToCartResult: LiveData<RechargeNetworkResult<DigitalAtcResult>>
        get() = _addToCartResult

    private val _clientNumberValidatorMsg = MutableLiveData<String>()
    val clientNumberValidatorMsg: LiveData<String>
        get() = _clientNumberValidatorMsg

    private val _recommendationData = MutableLiveData<RechargeNetworkResult<RecommendationWidgetModel>>()
    val recommendationData: LiveData<RechargeNetworkResult<RecommendationWidgetModel>>
        get() = _recommendationData

    fun setMenuDetailLoading(){
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

    fun setRechargeCatalogInputMultiTabLoading(){
        _observableDenomMCCMData.value = RechargeNetworkResult.Loading
    }

    fun getRechargeCatalogInputMultiTab(
        menuId: Int,
        operator: String,
        clientNumber: String,
        isFilterRefreshed: Boolean = true
    ) {
        catalogProductJob = viewModelScope.launchCatchError(dispatchers.main, block = {
            delay(DELAY_MULTI_TAB)
            val denomFull = repo.getProductInputMultiTabDenomFull(menuId, operator, clientNumber,
                filterDataParams, isFilterRefreshed
            )
            if (isFilterRefreshed) {
                setFilterDataParam(denomFull.filterTagComponents)
            }
            _observableDenomMCCMData.value = RechargeNetworkResult.Success(denomFull)
        }) {
            if (it !is CancellationException)
                _observableDenomMCCMData.value = RechargeNetworkResult.Fail(it)
        }
    }

    fun setFavoriteNumberLoading(){
        _favoriteNumberData.value = RechargeNetworkResult.Loading
    }

    fun getFavoriteNumber(categoryIds: List<Int>) {
        viewModelScope.launchCatchError(dispatchers.main, block = {
            val favoriteNumber = repo.getFavoriteNumberChips(categoryIds)
            _favoriteNumberData.value =
                RechargeNetworkResult.Success(favoriteNumber.persoFavoriteNumber.items)
        }) {
            _favoriteNumberData.value = RechargeNetworkResult.Fail(it)
        }
    }

    fun setAutoCompleteLoading() {
        _autoCompleteData.value = RechargeNetworkResult.Loading
    }

    fun getAutoComplete(categoryIds: List<Int>) {
        viewModelScope.launchCatchError(dispatchers.main, block = {
            delay(DELAY_AUTOCOMPLETE) // temporary solution to fix race condition
            val favoriteNumberList = repo.getFavoriteNumberList(categoryIds)
            _autoCompleteData.value = RechargeNetworkResult.Success(
                favoriteNumberList.persoFavoriteNumber.items)
        }) {
            _autoCompleteData.value = RechargeNetworkResult.Fail(it)
        }
    }

    fun setPrefixOperatorLoading() {
        _catalogPrefixSelect.value = RechargeNetworkResult.Loading
    }

    fun getPrefixOperator(menuId: Int) {
        viewModelScope.launchCatchError(dispatchers.main, block = {
            operatorData = repo.getOperatorList(menuId)
            delay(DELAY_PREFIX_TIME)
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
        userId: String
    ) {
        viewModelScope.launchCatchError(dispatchers.main, block = {
            val categoryIdAtc = repo.addToCart(
                digitalCheckoutPassData,
                digitalIdentifierParam,
                digitalSubscriptionParams,
                userId
            )
            _addToCartResult.value = RechargeNetworkResult.Success(categoryIdAtc)
        }) {
            if (it is ResponseErrorException && !it.message.isNullOrEmpty()) {
                _addToCartResult.value = RechargeNetworkResult.Fail(MessageErrorException(it.message))
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
            val recommendations = repo.getRecommendations(clientNumbers, dgCategoryIds, true)
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

    fun setAutoSelectedDenom(listDenomData: List<DenomData>, productId: String){
        var denomData: DenomData? = null
        listDenomData.forEachIndexed{ index, activeDenomData ->
            if (productId.equals(activeDenomData.id)) denomData = activeDenomData
        }

        denomData?.let {
            selectedFullProduct = SelectedProduct(it, DenomWidgetEnum.FULL_TYPE, 0)
        }
    }

    fun getSelectedPositionId(listDenomData: List<DenomData>): Int? {
        var selectedProductPositionId: Int? = null
        listDenomData.forEachIndexed { index, denomData ->
            if (denomData.id.equals(selectedFullProduct.denomData.id, false)
                && selectedFullProduct.denomData.id.isNotEmpty()
            ) selectedProductPositionId = index
        }
        return selectedProductPositionId
    }

    fun isAutoSelectedProduct(layoutType: DenomWidgetEnum): Boolean =
        (selectedFullProduct.denomData.id.isNotEmpty()
                && selectedFullProduct.position >= 0
                && selectedFullProduct.denomWidgetEnum == layoutType
                && isEligibleToBuy)

    fun onResetSelectedProduct() {
        selectedFullProduct = SelectedProduct()
    }

    fun updateFilterData(filterTagComponents: List<TelcoFilterTagComponent>) {
        filterData = filterTagComponents
        updateFilterData()
    }

    fun updateFilterData() {
        setFilterDataParam(filterData)
    }

    fun resetFilter(){
        filterData.forEach {
            it.filterTagDataCollections.forEach {
                it.isSelected = false
            }
        }
        setFilterDataParam(filterData)
    }

    fun isEmptyDenomMCCM(listDenomData: List<DenomData>, listMCCMData: List<DenomData>): Boolean {
        return listDenomData.isEmpty() && listMCCMData.isEmpty()
    }

    private fun setFilterDataParam(filterTagComponents: List<TelcoFilterTagComponent>) {
        filterData = filterTagComponents
        filterData.forEach {
            val paramName = it.paramName
            val arrayListIdFilter = arrayListOf<String>()
            it.filterTagDataCollections.forEach {
                if (it.isSelected) {
                    arrayListIdFilter.add(it.key)
                }
            }
            updateFilterParam(paramName, arrayListIdFilter)
        }
    }

    private fun addFilterParam(paramName: String, listKey: ArrayList<String>) {
        val valueItem = HashMap<String, Any>()
        valueItem[FILTER_PARAM_NAME] = paramName
        valueItem[FILTER_VALUE] = listKey
        filterDataParams.add(valueItem)
    }

    private fun updateFilterParam(paramName: String, listKey: ArrayList<String>) {
        removeFilterParam(paramName)
        addFilterParam(paramName, listKey)
    }

    private fun removeFilterParam(paramName: String) {
        val iterator = filterDataParams.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.containsValue(paramName)) {
                iterator.remove()
            }
        }
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

    companion object {
        const val FILTER_PARAM_NAME = "param_name"
        const val FILTER_VALUE = "value"
    }
}