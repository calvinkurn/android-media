package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.data.prefix_select.RechargeValidation
import com.tokopedia.common.topupbills.data.product.CatalogOperator
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.config.GlobalConfig
import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogOperatorSelectGroup
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.CHECKOUT_NO_PROMO
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.DELAY_MULTI_TAB
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.VALIDATOR_DELAY_TIME
import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPTokenListrikRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.model.denom.MenuDetailModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

class DigitalPDPTokenListrikViewModel @Inject constructor(
    val repo: DigitalPDPTokenListrikRepository,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    var validatorJob: Job? = null
    var catalogProductJob: Job? = null
    var validators: List<RechargeValidation> = listOf()
    var isEligibleToBuy = false
    var selectedGridProduct = SelectedProduct()
    var operatorList: List<CatalogOperator> = listOf()
    var operatorData: CatalogOperator = CatalogOperator()
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

    private val _favoriteNumberData = MutableLiveData<RechargeNetworkResult<List<TopupBillsPersoFavNumberItem>>>()
    val favoriteNumberData: LiveData<RechargeNetworkResult<List<TopupBillsPersoFavNumberItem>>>
        get() = _favoriteNumberData

    private val _observableDenomData = MutableLiveData<RechargeNetworkResult<DenomWidgetModel>>()
    val observableDenomData: LiveData<RechargeNetworkResult<DenomWidgetModel>>
        get() = _observableDenomData

    private val _addToCartResult = MutableLiveData<RechargeNetworkResult<String>>()
    val addToCartResult: LiveData<RechargeNetworkResult<String>>
        get() = _addToCartResult

    private val _clientNumberValidatorMsg = MutableLiveData<String>()
    val clientNumberValidatorMsg: LiveData<String>
        get() = _clientNumberValidatorMsg

    private val _catalogSelectGroup =
        MutableLiveData<RechargeNetworkResult<DigitalCatalogOperatorSelectGroup>>()
    val catalogSelectGroup: LiveData<RechargeNetworkResult<DigitalCatalogOperatorSelectGroup>>
        get() = _catalogSelectGroup

    fun setMenuDetailLoading(){
        _menuDetailData.value = RechargeNetworkResult.Loading
    }

    fun getMenuDetail(menuId: Int, isLoadFromCloud: Boolean = false) {
        viewModelScope.launchCatchError(dispatchers.main, block = {
            val menuDetail = repo.getMenuDetail(menuId, isLoadFromCloud)
            _menuDetailData.value = RechargeNetworkResult.Success(menuDetail)
        }) {
            _menuDetailData.value = RechargeNetworkResult.Fail(it)
        }
    }

    fun setRechargeCatalogInputMultiTabLoading(){
        _observableDenomData.value = RechargeNetworkResult.Loading
    }

    fun getRechargeCatalogInputMultiTab(menuId: Int, operator: String, clientNumber: String){
        catalogProductJob = viewModelScope.launch {
            launchCatchError(block = {
                delay(DELAY_MULTI_TAB)
                val denomGrid = repo.getProductTokenListrikDenomGrid(menuId, operator, clientNumber)
                _observableDenomData.value = RechargeNetworkResult.Success(denomGrid)
            }){
                if (it !is CancellationException)
                    _observableDenomData.value = RechargeNetworkResult.Fail(it)
            }
        }
    }

    fun setFavoriteNumberLoading(){
        _favoriteNumberData.value = RechargeNetworkResult.Loading
    }

    fun getFavoriteNumber(categoryIds: List<Int>) {
        viewModelScope.launchCatchError(dispatchers.main, block = {
            val favoriteNumber = repo.getFavoriteNumber(categoryIds)
            _favoriteNumberData.value = RechargeNetworkResult.Success(
                favoriteNumber.persoFavoriteNumber.items)
        }) {
            _favoriteNumberData.value = RechargeNetworkResult.Fail(it)
        }
    }

    fun setOperatorSelectGroupLoading(){
        _catalogSelectGroup.value = RechargeNetworkResult.Loading
    }

    fun getOperatorSelectGroup(menuId: Int) {
        viewModelScope.launchCatchError(dispatchers.main, block = {
            val data = repo.getOperatorSelectGroup(menuId)
            operatorList = data.response.operatorGroups?.firstOrNull()?.operators ?: listOf()
            if (!operatorList.isNullOrEmpty() && operatorData.id.isNullOrEmpty()) {
                operatorData = operatorList.get(0)
            }
            validators = data.response.validations ?: listOf()
            _catalogSelectGroup.value = RechargeNetworkResult.Success(data)
        }) {
            _catalogSelectGroup.value = RechargeNetworkResult.Fail(it)
        }
    }

    fun cancelCatalogProductJob() {
        catalogProductJob?.cancel()
    }

    fun cancelValidatorJob() {
        validatorJob?.cancel()
    }


    fun setAddToCartLoading() {
        _addToCartResult.value = RechargeNetworkResult.Loading
    }

    fun addToCart(digitalIdentifierParam: RequestBodyIdentifier,
                  digitalSubscriptionParams: DigitalSubscriptionParams,
                  userId: String
    ){
        viewModelScope.launchCatchError(dispatchers.main, block = {
            val categoryIdAtc = repo.addToCart(digitalCheckoutPassData, digitalIdentifierParam, digitalSubscriptionParams, userId)
            _addToCartResult.value = RechargeNetworkResult.Success(categoryIdAtc)
        }) {
            if (it is ResponseErrorException && !it.message.isNullOrEmpty()) {
                _addToCartResult.value = RechargeNetworkResult.Fail(MessageErrorException(it.message))
            } else {
                _addToCartResult.value = RechargeNetworkResult.Fail(it)
            }
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

    fun updateCategoryCheckoutPassData(categoryId: String){
        digitalCheckoutPassData.categoryId = categoryId
    }

    fun validateClientNumber(clientNumber: String) {
        validatorJob = viewModelScope.launch {
                var errorMessage = ""
                for (validation in validators) {
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

    fun getSelectedPositionId(listDenomData: List<DenomData>): Int?{
        var selectedProductPositionId : Int? = null
        listDenomData.forEachIndexed { index, denomData ->
            if (denomData.id.equals(selectedGridProduct.denomData.id, false)
                && selectedGridProduct.denomData.id.isNotEmpty()) selectedProductPositionId = index
        }
        return selectedProductPositionId
    }

    fun isAutoSelectedProduct(layoutType: DenomWidgetEnum): Boolean = (selectedGridProduct.denomData.id.isNotEmpty()
            && selectedGridProduct.position >= 0
            && selectedGridProduct.denomWidgetEnum == layoutType
            && isEligibleToBuy)

    fun onResetSelectedProduct(){
        selectedGridProduct = SelectedProduct()
    }
}