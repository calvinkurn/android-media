package com.tokopedia.shop.open.shop_open_revamp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.shop.open.shop_open_revamp.data.model.*
import com.tokopedia.shop.open.shop_open_revamp.domain.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopOpenRevampViewModel @Inject constructor(
        private val validateDomainShopNameUseCase: ShopOpenRevampValidateDomainShopNameUseCase,
        private val getDomainNameSuggestionUseCase: ShopOpenRevampGetDomainNameSuggestionUseCase,
        private val getSurveyUseCase: ShopOpenRevampGetSurveyUseCase,
        private val sendSurveyUseCase: ShopOpenRevampSendSurveyUseCase,
        private val createShopUseCase: ShopOpenRevampCreateShopUseCase,
        private val saveShopShipmentLocationUseCase: ShopOpenRevampSaveShipmentLocationUseCase,
        dispatchers: CoroutineDispatcher
) : BaseViewModel(dispatchers) {

    companion object {
        const val SURVEY_ID  = "surveyID"
        const val SURVEY_ID_VALUE = 1
        const val QUESTIONS = "questions"
        const val QUESTION_ID = "questionID"
        const val CHOICES = "choices"
    }
    

    private val _checkDomainNameResponse = MutableLiveData<Result<ValidateShopDomainNameResult>>()
    val checkDomainNameResponse: LiveData<Result<ValidateShopDomainNameResult>>
        get() = _checkDomainNameResponse

    private val _getSurveyDataResponse = MutableLiveData<Result<GetSurveyData>>()
    val getSurveyDataResponse: LiveData<Result<GetSurveyData>>
        get() = _getSurveyDataResponse

    private val _domainShopNameSuggestionsResponse = MutableLiveData<Result<ShopDomainSuggestionResult>>()
    val domainShopNameSuggestionsResponse: LiveData<Result<ShopDomainSuggestionResult>>
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

        launchCatchError(block = {
            withContext(Dispatchers.IO) {
                delay(700)

                if (currentShopName != shopName) {
                    return@withContext
                }

                validateDomainShopNameUseCase.params = ShopOpenRevampValidateDomainShopNameUseCase.createRequestParams(shopName)
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
            withContext(Dispatchers.IO) {
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
            withContext(Dispatchers.IO) {
                getDomainNameSuggestionUseCase.params = ShopOpenRevampGetDomainNameSuggestionUseCase.createRequestParams(shopName)
                val shopSuggestionsResult = getDomainNameSuggestionUseCase.executeOnBackground()
                shopSuggestionsResult.let {
                    _domainShopNameSuggestionsResponse.postValue(Success(it))
                }
            }
        }) {
             _domainShopNameSuggestionsResponse.value = Fail(it)
        }
    }

    fun saveShippingLocation(
            shopId: Int,
            postCode: String,
            courierOrigin: Int,
            addrStreet: String,
            lat: String,
            long: String
    ) {
        launchCatchError(block = {
            withContext(Dispatchers.IO) {
                val dataParam = saveShopShippingLocation(
                        shopId, postCode, courierOrigin, addrStreet, lat, long)
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

    private fun saveShopShippingLocation(
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
            withContext(Dispatchers.IO) {
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

    fun sendInputSurveyData(dataSurvey: MutableMap<Int, MutableList<Int>>) {
        val dataSurveyInput = getDataSurveyInput(dataSurvey)
        sendSurveyData(dataSurveyInput)
    }

    fun sendSurveyData(dataSurveyInput: Map<String, Any>) {
        launchCatchError(block = {
            withContext(Dispatchers.IO) {
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

    private fun getDataSurveyInput(
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

        launchCatchError(block = {
            withContext(Dispatchers.IO) {
                delay(700)

                if (currentShopDomain != domain) {
                    return@withContext
                }

                validateDomainShopNameUseCase.params = ShopOpenRevampValidateDomainShopNameUseCase.createRequestParam(domain)
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