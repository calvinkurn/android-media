package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.common.topupbills.data.prefix_select.RechargeValidation
import com.tokopedia.common.topupbills.data.product.CatalogOperator
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberItem
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.config.GlobalConfig
import com.tokopedia.digital_product_detail.data.model.data.DigitalAtcResult
import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogOperatorSelectGroup
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.DELAY_AUTOCOMPLETE
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.CHECKOUT_NO_PROMO
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.VALIDATOR_DELAY_TIME
import com.tokopedia.digital_product_detail.data.model.data.RechargeProduct
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPTagihanListrikRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.recharge_component.model.denom.MenuDetailModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import javax.inject.Inject

class DigitalPDPTagihanViewModel @Inject constructor(
    val repo: DigitalPDPTagihanListrikRepository,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    var validatorJob: Job? = null

    var validators: List<RechargeValidation> = emptyList()
    var isEligibleToBuy = false
    var operatorList: List<CatalogOperator> = emptyList()
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

    private val _favoriteNumberData =
        MutableLiveData<RechargeNetworkResult<List<TopupBillsPersoFavNumberItem>>>()
    val favoriteNumberData: LiveData<RechargeNetworkResult<List<TopupBillsPersoFavNumberItem>>>
        get() = _favoriteNumberData

    private val _autoCompleteData =
        MutableLiveData<RechargeNetworkResult<List<TopupBillsPersoFavNumberItem>>>()
    val autoCompleteData: LiveData<RechargeNetworkResult<List<TopupBillsPersoFavNumberItem>>>
        get() = _autoCompleteData

    private val _catalogSelectGroup =
        MutableLiveData<RechargeNetworkResult<DigitalCatalogOperatorSelectGroup>>()
    val catalogSelectGroup: LiveData<RechargeNetworkResult<DigitalCatalogOperatorSelectGroup>>
        get() = _catalogSelectGroup

    private val _tagihanProduct = MutableLiveData<RechargeNetworkResult<RechargeProduct>>()
    val tagihanProduct: LiveData<RechargeNetworkResult<RechargeProduct>>
        get() = _tagihanProduct

    private val _inquiry = MutableLiveData<RechargeNetworkResult<TopupBillsEnquiryData>>()
    val inquiry: LiveData<RechargeNetworkResult<TopupBillsEnquiryData>>
        get() = _inquiry

    private val _clientNumberValidatorMsg = MutableLiveData<Pair<String, Boolean>>()
    val clientNumberValidatorMsg: LiveData<Pair<String, Boolean>>
        get() = _clientNumberValidatorMsg

    private val _addToCartResult = MutableLiveData<RechargeNetworkResult<DigitalAtcResult>>()
    val addToCartResult: LiveData<RechargeNetworkResult<DigitalAtcResult>>
        get() = _addToCartResult

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

    fun setFavoriteNumberLoading() {
        _favoriteNumberData.value = RechargeNetworkResult.Loading
    }

    fun getFavoriteNumber(categoryIds: List<Int>, operatorIds: List<Int>) {
        viewModelScope.launchCatchError(dispatchers.main, block = {
            val favoriteNumberChips = repo.getFavoriteNumberChips(categoryIds, operatorIds)
            _favoriteNumberData.value = RechargeNetworkResult.Success(
                favoriteNumberChips.persoFavoriteNumber.items
            )
        }) {
            _favoriteNumberData.value = RechargeNetworkResult.Fail(it)
        }
    }

    fun setAutoCompleteLoading() {
        _autoCompleteData.value = RechargeNetworkResult.Loading
    }

    fun getAutoComplete(categoryIds: List<Int>, operatorIds: List<Int>) {
        viewModelScope.launchCatchError(dispatchers.main, block = {
            delay(DELAY_AUTOCOMPLETE) // temporary solution to fix race condition
            val favoriteNumberList = repo.getFavoriteNumberList(categoryIds, operatorIds)
            _autoCompleteData.value = RechargeNetworkResult.Success(
                favoriteNumberList.persoFavoriteNumber.items)
        }) {
            _autoCompleteData.value = RechargeNetworkResult.Fail(it)
        }
    }

    fun setOperatorSelectGroupLoading() {
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

    fun setTagihanProductLoading() {
        _tagihanProduct.value = RechargeNetworkResult.Loading
    }

    fun getTagihanProduct(menuID: Int, clientNumber: String, nullErrorMessage: String) {
        viewModelScope.launchCatchError(dispatchers.main, block = {
            val data = repo.getProductTagihanListrik(menuID, operatorData.id, clientNumber)
            if (data == null) {
                throw MessageErrorException(nullErrorMessage)
            } else {
                _tagihanProduct.value = RechargeNetworkResult.Success(data)
            }
        }) {
            _tagihanProduct.value = RechargeNetworkResult.Fail(it)
        }
    }

    fun setInquiryLoading() {
        _inquiry.value = RechargeNetworkResult.Loading
    }

    fun inquiry(productId: String, clientNumber: String, inputData: Map<String, String>) {
        viewModelScope.launchCatchError(dispatchers.main, block = {
            var data: TopupBillsEnquiryData
            do {
                data = repo.inquiryProduct(productId, clientNumber, inputData)

                with(data.enquiry) {
                    if (status == STATUS_PENDING && retryDuration > 0)
                        delay(TimeUnit.SECONDS.toMillis(retryDuration.toLong()))
                }
            } while (data.enquiry.status != STATUS_DONE)
            _inquiry.value = RechargeNetworkResult.Success(data)
        }) {
            _inquiry.value = RechargeNetworkResult.Fail(it)
        }
    }

    fun validateClientNumber(clientNumber: String, isShowToaster: Boolean = false) {
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
            _clientNumberValidatorMsg.value = Pair(errorMessage, isShowToaster)
        }
    }

    fun cancelValidatorJob() {
        validatorJob?.cancel()
    }

    fun setAddToCartLoading() {
        _addToCartResult.postValue(RechargeNetworkResult.Loading)
    }

    fun addToCart(
        digitalIdentifierParam: RequestBodyIdentifier,
        digitalSubscriptionParams: DigitalSubscriptionParams,
        userId: String
    ) {
        viewModelScope.launchCatchError(dispatchers.main, block = {
            val atcResult = repo.addToCart(
                digitalCheckoutPassData,
                digitalIdentifierParam,
                digitalSubscriptionParams,
                userId
            )
            _addToCartResult.postValue(RechargeNetworkResult.Success(atcResult))
        }) {
            if (it is ResponseErrorException && !it.message.isNullOrEmpty()) {
                _addToCartResult.postValue(RechargeNetworkResult.Fail(MessageErrorException(it.message)))
            } else {
                _addToCartResult.postValue(RechargeNetworkResult.Fail(it))
            }
        }
    }

    fun setOperatorDataById(operatorId: String) {
        operatorData = operatorList.firstOrNull { it.id == operatorId } ?: CatalogOperator()
    }

    fun updateCategoryCheckoutPassData(categoryId: String) {
        digitalCheckoutPassData.categoryId = categoryId
    }

    fun updateCheckoutPassData(idemPotencyKeyActive: String, clientNumberActive: String) {
        tagihanProduct.value?.let {
            if (it is RechargeNetworkResult.Success) {
                val product = it.data
                digitalCheckoutPassData.apply {
                    categoryId = product.attributes.categoryId
                    clientNumber = clientNumberActive
                    isPromo = if (product.attributes.productPromo != null) "1" else "0"
                    operatorId = product.attributes.operatorId
                    productId = product.id
                    utmCampaign = product.attributes.categoryId
                    idemPotencyKey = idemPotencyKeyActive
                }
            }
        }
    }

    fun getListInfo(): List<String> {
        return if (operatorData.id.isNotEmpty()){
            operatorData.attributes.operatorDescriptions
        } else listOf()
    }

    companion object {
        const val STATUS_DONE = "DONE"
        const val STATUS_PENDING = "PENDING"
    }

}