package com.tokopedia.shop.open.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.shop.common.graphql.data.shopopen.ShopDomainSuggestionData
import com.tokopedia.shop.common.graphql.data.shopopen.ValidateShopDomainNameResult
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.GetShopDomainNameSuggestionUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.ValidateDomainShopNameUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.common.graphql.data.shopopen.SaveShipmentLocation
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.ShopOpenRevampSaveShipmentLocationUseCase
import com.tokopedia.shop.open.common.EspressoIdlingResource
import com.tokopedia.shop.open.data.model.*
import com.tokopedia.shop.open.domain.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopOpenRevampViewModel @Inject constructor(
        private val validateDomainShopNameUseCase: ValidateDomainShopNameUseCase,
        private val getDomainNameSuggestionUseCase: GetShopDomainNameSuggestionUseCase,
        private val getSurveyUseCase: ShopOpenRevampGetSurveyUseCase,
        private val sendSurveyUseCase: ShopOpenRevampSendSurveyUseCase,
        private val createShopUseCase: ShopOpenRevampCreateShopUseCase,
        private val saveShopShipmentLocationUseCase: ShopOpenRevampSaveShipmentLocationUseCase,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    companion object {
        const val SURVEY_ID  = "surveyID"
        const val SURVEY_ID_VALUE = 1
        const val QUESTIONS = "questions"
        const val QUESTION_ID = "questionID"
        const val CHOICES = "choices"
        const val DELAY_TIMER = 400L
    }
    

    private val _checkDomainNameResponse = MutableLiveData<Result<ValidateShopDomainNameResult>>()
    val checkDomainNameResponse: LiveData<Result<ValidateShopDomainNameResult>>
        get() = _checkDomainNameResponse

    private val _getSurveyDataResponse = MutableLiveData<Result<GetSurveyData>>()
    val getSurveyDataResponse: LiveData<Result<GetSurveyData>>
        get() = _getSurveyDataResponse

    private val _domainShopNameSuggestionsResponse = MutableLiveData<Result<ShopDomainSuggestionData>>()
    val domainShopNameSuggestionsResponse: LiveData<Result<ShopDomainSuggestionData>>
        get() = _domainShopNameSuggestionsResponse

    private val _saveShopShipmentLocationResponse = MutableLiveData<Result<SaveShipmentLocation>>()
    val saveShopShipmentLocationResponse: LiveData<Result<SaveShipmentLocation>>
        get() = _saveShopShipmentLocationResponse

    private val _createShopOpenResponse = MutableLiveData<Result<CreateShop>>()
    val createShopOpenResponse: LiveData<Result<CreateShop>>
        get() = _createShopOpenResponse

    private val _sendSurveyDataResponse = MutableLiveData<Result<SendSurveyData>>()
    val sendSurveyDataResponse: LiveData<Result<SendSurveyData>>
        get() = _sendSurveyDataResponse

    private val _checkShopNameResponse = MutableLiveData<Result<ValidateShopDomainNameResult>>()
    val checkShopNameResponse: LiveData<Result<ValidateShopDomainNameResult>>
        get() = _checkShopNameResponse


    private var currentShopName = ""
    private var currentShopDomain = ""

    fun checkShopName(shopName: String) {
        if (currentShopName == shopName) {
            return
        }

        currentShopName = shopName
        EspressoIdlingResource.increment()
        validateShopName(shopName)
    }

    fun validateShopName(shopName: String) {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                delay(DELAY_TIMER)

                if (currentShopName != shopName) {
                    return@withContext
                }

                validateDomainShopNameUseCase.params = ValidateDomainShopNameUseCase.createRequestParams(shopName)
                val validateShopNameResult = validateDomainShopNameUseCase.executeOnBackground()
                validateShopNameResult.let {
                    _checkShopNameResponse.postValue(Success(validateShopNameResult))
                }
            }
        }) {
            _checkShopNameResponse.value = Fail(it)
        }
    }

    fun getSurveyQuizionaireData() {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                val surveyDataResult = getSurveyUseCase.executeOnBackground()
                surveyDataResult.let {
                    _getSurveyDataResponse.postValue(Success(it))
                }
            }
        }) {
            _getSurveyDataResponse.value = Fail(it)
        }
    }

    fun getDomainShopNameSuggestions(shopName: String) {
        getDomainNameSuggestionUseCase.cancelJobs()
        launchCatchError(block = {
            withContext(dispatchers.io) {
                getDomainNameSuggestionUseCase.params = GetShopDomainNameSuggestionUseCase.createRequestParams(shopName)
                val shopSuggestionsResult = getDomainNameSuggestionUseCase.executeOnBackground()
                shopSuggestionsResult.let {
                    _domainShopNameSuggestionsResponse.postValue(Success(it))
                }
            }
        }) {
             _domainShopNameSuggestionsResponse.value = Fail(it)
        }
    }

    fun saveShippingLocation(dataParam: MutableMap<String, Any>) {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                saveShopShipmentLocationUseCase.params = ShopOpenRevampSaveShipmentLocationUseCase.createRequestParams(dataParam)
                val saveShipmentLocationData = saveShopShipmentLocationUseCase.executeOnBackground()
                saveShipmentLocationData.let {
                    _saveShopShipmentLocationResponse.postValue(Success(it))
                }
            }
        }) {
            _saveShopShipmentLocationResponse.value = Fail(it)
        }
    }

    fun getSaveShopShippingLocationData(
            shopId: Int,
            postCode: String,
            courierOrigin: Int,
            addrStreet: String,
            lat: String,
            long: String
    ): MutableMap<String, Any> {
        val shipmentPayload = mutableMapOf<String, Any>()
        val SHOP_ID = "shop_id"
        val POSTAL_CODE = "postal_code"
        val COURIER_ORIGIN = "courier_origin"
        val ADDRESS_STREET = "addr_street"
        val LATITUDE = "latitude"
        val LONGITUDE = "longitude"

        shipmentPayload.put(SHOP_ID, shopId)
        shipmentPayload.put(POSTAL_CODE, postCode)
        shipmentPayload.put(COURIER_ORIGIN, courierOrigin)
        shipmentPayload.put(ADDRESS_STREET, addrStreet)
        shipmentPayload.put(LATITUDE, lat)
        shipmentPayload.put(LONGITUDE, long)
        return shipmentPayload
    }

    fun createShop(domain: String, shopName: String) {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                createShopUseCase.params = ShopOpenRevampCreateShopUseCase.createRequestParams(domain, shopName)
                val shopRegistrationResult = createShopUseCase.executeOnBackground()
                shopRegistrationResult.let {
                    _createShopOpenResponse.postValue(Success(it))
                }
            }
        }) {
            _createShopOpenResponse.value = Fail(it)
        }
    }

    fun sendSurveyData(dataSurveyInput: Map<String, Any>) {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                sendSurveyUseCase.params = ShopOpenRevampSendSurveyUseCase.createRequestParams(dataSurveyInput)
                val sendSurveyData = sendSurveyUseCase.executeOnBackground()
                sendSurveyData.let {
                    _sendSurveyDataResponse.postValue(Success(it))
                }
            }
        }) {
            _sendSurveyDataResponse.value = Fail(it)
        }
    }

    fun getDataSurveyInput(
            dataSurveys: MutableMap<Int, MutableList<Int>>
    ): MutableMap<String, Any> {
        val surveyPayload = mutableMapOf<String, Any>()
        var questionsAndChoices = mutableMapOf<String, Any>()
        dataSurveys.toList().forEachIndexed { _, dataSurvey ->
            questionsAndChoices.put(QUESTION_ID, dataSurvey.first)
            questionsAndChoices.put(CHOICES, dataSurvey.second)
        }
        surveyPayload.put(SURVEY_ID, SURVEY_ID_VALUE)
        surveyPayload.put(QUESTIONS, questionsAndChoices)
        return surveyPayload
    }

    fun checkDomainName(domain: String) {
        if (currentShopDomain == domain) {
            return
        }

        currentShopDomain = domain
        EspressoIdlingResource.increment()
        validateDomainName(domain)
    }

    fun validateDomainName(domain: String){
        launchCatchError(block = {
            withContext(dispatchers.io) {
                delay(DELAY_TIMER)

                if (currentShopDomain != domain) {
                    return@withContext
                }

                validateDomainShopNameUseCase.params = ValidateDomainShopNameUseCase.createRequestParam(domain)
                val validateShopDomainNameResult = validateDomainShopNameUseCase.executeOnBackground()
                validateShopDomainNameResult.let {
                    _checkDomainNameResponse.postValue(Success(it))
                }
            }
        }) {
            _checkDomainNameResponse.value = Fail(it)
        }
    }

}