package com.tokopedia.shop.open.shop_open_revamp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.shop.open.shop_open_revamp.data.model.*
import com.tokopedia.shop.open.shop_open_revamp.domain.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.*
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

    val checkDomainAndShopNameResponse = MutableLiveData<Result<ValidateShopDomainNameResult>>()
    val checkShopNameResponse = MutableLiveData<Result<ValidateShopDomainNameResult>>()
    val domainShopNameSuggestionsResponse = MutableLiveData<Result<ShopDomainSuggestionResult>>()
    val getSurveyDataResponse = MutableLiveData<Result<GetSurveyData>>()
    val sendSurveyDataResponse = MutableLiveData<Result<SendSurveyData>>()
    val createShopOpenResponse = MutableLiveData<Result<CreateShop>>()
    val saveShopShipmentLocationResponse = MutableLiveData<Result<SaveShipmentLocation>>()

    fun cancelUseCaseCheckDomainShopName() {
        validateDomainShopNameUseCase.cancelJobs()
        getDomainNameSuggestionUseCase.cancelJobs()
    }

    fun checkDomainAndShopName(domain: String, shopName: String) {
        validateDomainShopNameUseCase.cancelJobs()
        launchCatchError(block = {
            coroutineScope {
                launch(Dispatchers.IO) {
                    val domainShopNameValidationData = getValidationDomainShopName(domain, shopName)
                    domainShopNameValidationData.let {
                        checkDomainAndShopNameResponse.postValue(Success(it))
                    }
                }
            }
        }) {
            checkDomainAndShopNameResponse.value = Fail(it)
        }
    }

    fun checkShopName(shopName: String) {
        validateDomainShopNameUseCase.cancelJobs()
        launchCatchError(block = {
            coroutineScope {
                launch(Dispatchers.IO) {
                    val shopNameValidationData = getValidationShopName(shopName)
                    shopNameValidationData.let {
                        checkShopNameResponse.postValue(Success(it))
                    }
                }
            }
        }) {
            checkShopNameResponse.value = Fail(it)
        }
    }

    fun getDomainShopNameSuggestions(shopName: String) {
        getDomainNameSuggestionUseCase.cancelJobs()
        launchCatchError(block = {
            coroutineScope {
                launch(Dispatchers.IO) {
                    val domainShopNameValidationData = getDomainShopNameSuggestion(shopName)
                    domainShopNameValidationData.let {
                        domainShopNameSuggestionsResponse.postValue(Success(it))
                    }
                }
            }
        }) {
            checkDomainAndShopNameResponse.value = Fail(it)
        }
    }

    fun getSurveyQuizionaireData() {
        launchCatchError(block = {
            coroutineScope {
                launch(Dispatchers.IO) {
                    val surveyData = getSurveyQuizionaireDataResult()
                    surveyData.let {
                        getSurveyDataResponse.postValue(Success(it))
                    }
                }
            }
        }) {
            getSurveyDataResponse.value = Fail(it)
        }
    }

    fun createShop(domain: String, shopName: String) {
        launchCatchError(block = {
            coroutineScope {
                launch(Dispatchers.IO) {
                    val createShopData = submitShop(domain, shopName)
                    createShopData.let {
                        createShopOpenResponse.postValue(Success(it))
                    }
                }
            }
        }) {
            createShopOpenResponse.value = Fail(it)
        }
    }

    fun sendInputSurveyData(dataSurvey: MutableMap<Int, MutableList<Int>>) {
        val dataSurveyInput = getDataSurveyInput(dataSurvey)
        sendSurveyData(dataSurveyInput)
    }

    fun sendSurveyData(dataSurveyInput: Map<String, Any>) {
        launchCatchError(block = {
            coroutineScope {
                launch(Dispatchers.IO) {
                    val sendSurveyData = sendSurvey(dataSurveyInput)
                    sendSurveyDataResponse.postValue(Success(sendSurveyData))
                }
            }
        }) {
            sendSurveyDataResponse.value = Fail(it)
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
            coroutineScope {
                launch(Dispatchers.IO) {
                    val dataParam = saveShopShippingLocation(
                            shopId, postCode, courierOrigin, addrStreet, lat, long)
                    val saveShipmentLocation = saveShopShippingLocation(dataParam)
                    saveShopShipmentLocationResponse.postValue(Success(saveShipmentLocation))
                }
            }
        }) {
            saveShopShipmentLocationResponse.value = Fail(it)
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

    private fun getDataSurveyInput(
            dataSurveys: MutableMap<Int, MutableList<Int>>
    ): MutableMap<String, Any> {
        val surveyPayload = mutableMapOf<String, Any>()
        var questionsAndChoices = mutableMapOf<String, Any>()
        dataSurveys.toList().forEachIndexed { index, dataSurvey ->
            questionsAndChoices.put(QUESTION_ID, dataSurvey.first)
            questionsAndChoices.put(CHOICES, dataSurvey.second)
        }
        surveyPayload.put(SURVEY_ID, SURVEY_ID_VALUE)
        surveyPayload.put(QUESTIONS, questionsAndChoices)
        return surveyPayload
    }

    private suspend fun saveShopShippingLocation(dataParam: Map<String, Any>): SaveShipmentLocation {
        var saveShipmentLocation = SaveShipmentLocation()
        try {
            saveShopShipmentLocationUseCase.params = ShopOpenRevampSaveShipmentLocationUseCase.createRequestParams(dataParam)
            saveShipmentLocation = saveShopShipmentLocationUseCase.executeOnBackground()
        } catch (t: Throwable) {
        }
        return saveShipmentLocation
    }

    private suspend fun sendSurvey(dataSurveys: Map<String, Any>): SendSurveyData {
        var sendSurveyData = SendSurveyData()
        try {
            sendSurveyUseCase.params = ShopOpenRevampSendSurveyUseCase.createRequestParams(dataSurveys)
            sendSurveyData = sendSurveyUseCase.executeOnBackground()
        } catch (t: Throwable) {
        }
        return sendSurveyData
    }

    private suspend fun submitShop(domain: String, shopName: String): CreateShop {
        var shopRegistrationResult = CreateShop()
        try {
            createShopUseCase.params = ShopOpenRevampCreateShopUseCase.createRequestParams(domain, shopName)
            shopRegistrationResult = createShopUseCase.executeOnBackground()
        } catch (t: Throwable) {
        }
        return shopRegistrationResult
    }

    private suspend fun getSurveyQuizionaireDataResult(): GetSurveyData {
        var surveyDataResult = GetSurveyData()
        try {
            surveyDataResult = getSurveyUseCase.executeOnBackground()
        } catch (t: Throwable) {
        }
        return surveyDataResult
    }

    private suspend fun getValidationDomainShopName(domain: String, shopName: String): ValidateShopDomainNameResult {
        var validateShopDomainNameResult = ValidateShopDomainNameResult()
        try {
            validateDomainShopNameUseCase.params = ShopOpenRevampValidateDomainShopNameUseCase.createRequestParams(domain, shopName)
            validateShopDomainNameResult = validateDomainShopNameUseCase.executeOnBackground()
        } catch (t: Throwable) {
        }
        return validateShopDomainNameResult
    }

    private suspend fun getValidationShopName(shopName: String): ValidateShopDomainNameResult {
        var validateShopNameResult = ValidateShopDomainNameResult()
        try {
            validateDomainShopNameUseCase.params = ShopOpenRevampValidateDomainShopNameUseCase.createRequestParams(shopName)
            validateShopNameResult = validateDomainShopNameUseCase.executeOnBackground()
        } catch (t: Throwable) {
        }
        return validateShopNameResult
    }

    private suspend fun getDomainShopNameSuggestion(shopName: String): ShopDomainSuggestionResult {
        var shopSuggestionsResult = ShopDomainSuggestionResult()
        try {
            getDomainNameSuggestionUseCase.params = ShopOpenRevampGetDomainNameSuggestionUseCase.createRequestParams(shopName)
            shopSuggestionsResult = getDomainNameSuggestionUseCase.executeOnBackground()
        } catch (t: Throwable) {
        }
        return shopSuggestionsResult
    }

}