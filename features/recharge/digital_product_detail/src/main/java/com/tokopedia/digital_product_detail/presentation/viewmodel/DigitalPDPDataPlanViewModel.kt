package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_product_detail.data.model.data.InputMultiTabDenomModel
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPTelcoRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.common.topupbills.data.favorite_number_perso.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.data.prefix_select.RechargeCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.config.GlobalConfig
import com.tokopedia.digital_product_detail.data.model.data.SelectedFullProduct
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.MenuDetailModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

class DigitalPDPDataPlanViewModel @Inject constructor(
    val repo: DigitalPDPTelcoRepository,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    val _filterData = ArrayList<HashMap<String, Any>>()

    private var loadingJob: Job? = null
    var operatorData: TelcoCatalogPrefixSelect = TelcoCatalogPrefixSelect(
        RechargeCatalogPrefixSelect()
    )

    var isEligibleToBuy = false

    var selectedFullProduct = SelectedFullProduct()

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

    private val _favoriteNumberData = MutableLiveData<RechargeNetworkResult<List<TopupBillsPersoFavNumberItem>>>()
    val favoriteNumberData: LiveData<RechargeNetworkResult<List<TopupBillsPersoFavNumberItem>>>
        get() = _favoriteNumberData

    private val _catalogPrefixSelect = MutableLiveData<RechargeNetworkResult<TelcoCatalogPrefixSelect>>()
    val catalogPrefixSelect: LiveData<RechargeNetworkResult<TelcoCatalogPrefixSelect>>
        get() = _catalogPrefixSelect

    private val _observableDenomMCCMData = MutableLiveData<RechargeNetworkResult<InputMultiTabDenomModel>>()
    val observableDenomMCCMData: LiveData<RechargeNetworkResult<InputMultiTabDenomModel>>
        get() = _observableDenomMCCMData

    private val _addToCartResult = MutableLiveData<RechargeNetworkResult<String>>()
    val addToCartResult: LiveData<RechargeNetworkResult<String>>
        get() = _addToCartResult

    private val _clientNumberValidatorMsg = MutableLiveData<String>()
    val clientNumberValidatorMsg: LiveData<String>
        get() = _clientNumberValidatorMsg

    fun addFilter(paramName: String, listKey: ArrayList<String>){
        val valueItem = HashMap<String, Any>()
        valueItem[FILTER_PARAM_NAME] = paramName
        valueItem[FILTER_VALUE] = listKey
        _filterData.add(valueItem)
    }

    fun updateFilter(paramName: String, listKey: ArrayList<String>){
        removeFilter(paramName)
        addFilter(paramName, listKey)
    }

    fun removeFilter(paramName: String) {
        val iterator = _filterData.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.containsValue(paramName)) {
                iterator.remove()
            }
        }
    }

    fun getMenuDetail(menuId: Int, isLoadFromCloud: Boolean = false) {
        _menuDetailData.postValue(RechargeNetworkResult.Loading)
        viewModelScope.launchCatchError(dispatchers.io, block = {
            val menuDetail = repo.getMenuDetail(menuId, isLoadFromCloud, true)
            _menuDetailData.postValue(RechargeNetworkResult.Success(menuDetail))
        }) {
            _menuDetailData.postValue(RechargeNetworkResult.Fail(it))
        }
    }

    fun getRechargeCatalogInputMultiTab(menuId: Int, operator: String, clientNumber: String){
        _observableDenomMCCMData.postValue(RechargeNetworkResult.Loading)
        launchCatchError(block = {
            val denomGrid = repo.getProductInputMultiTabDenomFull(menuId, operator, clientNumber, _filterData)
            _observableDenomMCCMData.postValue(RechargeNetworkResult.Success(denomGrid))
        }){
            _observableDenomMCCMData.postValue(RechargeNetworkResult.Fail(it))
        }
    }

    fun getFavoriteNumber(categoryIds: List<Int>) {
        _favoriteNumberData.postValue(RechargeNetworkResult.Loading)
        viewModelScope.launchCatchError(dispatchers.io, block = {
            val favoriteNumber = repo.getFavoriteNumber(categoryIds)
            _favoriteNumberData.postValue(RechargeNetworkResult.Success(
                favoriteNumber.persoFavoriteNumber.items))
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
            if (it is ResponseErrorException && !it.message.isNullOrEmpty()) {
                _addToCartResult.postValue(RechargeNetworkResult.Fail(MessageErrorException(it.message)))
            } else {
                _addToCartResult.postValue(RechargeNetworkResult.Fail(it))
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

    fun getSelectedPositionId(listDenomData: List<DenomData>): Int?{
        var selectedProductPositionId : Int? = null
        listDenomData.forEachIndexed { index, denomData ->
            if (denomData.id.equals(selectedFullProduct.denomData.id, false)
                && selectedFullProduct.denomData.id.isNotEmpty()) selectedProductPositionId = index
        }
        return selectedProductPositionId
    }

    fun isAutoSelectedProduct(layoutType: DenomWidgetEnum): Boolean = (selectedFullProduct.denomData.id.isNotEmpty()
            && selectedFullProduct.position >= 0
            && selectedFullProduct.denomWidgetEnum == layoutType
            && isEligibleToBuy)

    fun onResetSelectedProduct(){
        selectedFullProduct = SelectedFullProduct()
    }

    companion object {
        const val FILTER_PARAM_NAME = "param_name"
        const val FILTER_VALUE = "value"

        const val DELAY_TIME = 200L
        const val VALIDATOR_DELAY_TIME = 3000L
    }
}