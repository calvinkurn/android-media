package com.tokopedia.shop.settings.basicinfo.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.graphql.data.shopopen.ShopDomainSuggestionData
import com.tokopedia.shop.common.graphql.data.shopopen.ValidateShopDomainNameResult
import com.tokopedia.shop.settings.common.coroutine.CoroutineDispatchers
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.GetShopDomainNameSuggestionUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.ValidateDomainShopNameUseCase
import com.tokopedia.shop.settings.basicinfo.data.AllowShopNameDomainChanges.*
import com.tokopedia.shop.settings.basicinfo.data.UploadShopEditImageModel
import com.tokopedia.shop.settings.basicinfo.domain.GetAllowShopNameDomainChanges
import com.tokopedia.shop.settings.basicinfo.domain.UploadShopImageUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopEditBasicInfoViewModel @Inject constructor(
    private val getShopBasicDataUseCase: GetShopBasicDataUseCase,
    private val updateShopBasicDataUseCase: UpdateShopBasicDataUseCase,
    private val uploadShopImageUseCase: UploadShopImageUseCase,
    private val getAllowShopNameDomainChangesUseCase: GetAllowShopNameDomainChanges,
    private val getShopDomainNameSuggestionUseCase: GetShopDomainNameSuggestionUseCase,
    private val validateDomainShopNameUseCase: ValidateDomainShopNameUseCase,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    companion object {
        private const val INPUT_DELAY = 500L
    }

    val shopBasicData: LiveData<Result<ShopBasicDataModel>>
        get() = _shopBasicData
    val uploadShopImage: LiveData<Result<UploadShopEditImageModel>>
        get() = _uploadShopImage
    val updateShopBasicData: LiveData<Result<String>>
        get() = _updateShopBasicData
    val allowShopNameDomainChanges: LiveData<Result<AllowShopNameDomainChangesData>>
        get() = _allowShopNameDomainChanges
    val validateShopName: LiveData<Result<ValidateShopDomainNameResult>>
        get() = _validateShopName
    val validateShopDomain: LiveData<Result<ValidateShopDomainNameResult>>
        get() = _validateShopDomain
    val shopDomainSuggestion: LiveData<Result<ShopDomainSuggestionData>>
        get() = _shopDomainSuggestion

    private val _shopBasicData = MutableLiveData<Result<ShopBasicDataModel>>()
    private val _uploadShopImage = MutableLiveData<Result<UploadShopEditImageModel>>()
    private val _updateShopBasicData = MutableLiveData<Result<String>>()
    private val _allowShopNameDomainChanges = MutableLiveData<Result<AllowShopNameDomainChangesData>>()
    private val _validateShopName = MutableLiveData<Result<ValidateShopDomainNameResult>>()
    private val _validateShopDomain = MutableLiveData<Result<ValidateShopDomainNameResult>>()
    private val _shopDomainSuggestion = MutableLiveData<Result<ShopDomainSuggestionData>>()

    private var currentShopName: String? = null
    private var currentDomainName: String? = null

    private var validateShopNameJob: Job? = null
    private var validateShopDomainJob: Job? = null

    fun getAllowShopNameDomainChanges() {
        launchCatchError(block = {
            val data = withContext(dispatchers.io) {
                val allowShopNameDomainChanges = getAllowShopNameDomainChangesUseCase.executeOnBackground()
                allowShopNameDomainChanges.data
            }
            _allowShopNameDomainChanges.value = Success(data)
        }) {
            _allowShopNameDomainChanges.value = Fail(it)
        }
    }

    fun validateShopName(shopName: String) {
        if(shopName == currentShopName) return

        validateShopNameJob?.cancel()

        launchCatchError(block = {
            val allowChangeDomain = true
            val data = withContext(dispatchers.io) {
                delay(INPUT_DELAY)

                val requestParams = ValidateDomainShopNameUseCase.createRequestParams(shopName)
                validateDomainShopNameUseCase.params = requestParams
                validateDomainShopNameUseCase.executeOnBackground()
            }

            if(data.validateDomainShopName.isValid && allowChangeDomain) {
                getShopDomainSuggestion(shopName)
            }

            _validateShopName.value = Success(data)
        }) {
            _validateShopName.value = Fail(it)
        }.let { validateShopNameJob = it }

        setCurrentShopName(shopName)
    }

    fun validateShopDomain(domainName: String) {
        if(domainName == currentDomainName) return

        validateShopDomainJob?.cancel()

        launchCatchError(block = {
            val data = withContext(dispatchers.io) {
                delay(INPUT_DELAY)

                val requestParams = ValidateDomainShopNameUseCase.createRequestParam(domainName)
                validateDomainShopNameUseCase.params = requestParams
                validateDomainShopNameUseCase.executeOnBackground()
            }

            if(!data.validateDomainShopName.isValid) {
                currentShopName?.let { getShopDomainSuggestion(it) }
            }

            _validateShopDomain.value = Success(data)
        }) {
            _validateShopDomain.value = Fail(it)
        }.let { validateShopDomainJob = it }

        setCurrentDomainName(domainName)
    }

    fun getShopBasicData() {
        getShopBasicDataUseCase.unsubscribe()

        launchCatchError(block = {
            val shopBasicData = withContext(dispatchers.io) {
                getShopBasicDataUseCase.getData(RequestParams.EMPTY)
            }
            _shopBasicData.value = Success(shopBasicData)
        }) {
            _shopBasicData.value = Fail(it)
        }
    }

    fun uploadShopImage(imagePath: String, tagline: String, description: String) {
        uploadShopImageUseCase.unsubscribe()

        launchCatchError(block = {
            val requestParams = UploadShopImageUseCase.createRequestParams(imagePath)
            val uploadShopImage = withContext(dispatchers.io) {
                uploadShopImageUseCase.getData(requestParams)
            }
            uploadShopImage.data?.image?.picCode?.let { picCode ->
                updateShopBasicData(tagline, description, picCode)
            }
        }) {
            _uploadShopImage.value = Fail(it)
        }
    }

    fun updateShopBasicData(tagline: String, description: String) {
        updateShopBasicDataUseCase.unsubscribe()

        val requestParams = UpdateShopBasicDataUseCase.createRequestParams(tagline,
            description, null, null, null)
        updateShopBasicData(requestParams)
    }

    fun detachView() {
        getShopBasicDataUseCase.unsubscribe()
        updateShopBasicDataUseCase.unsubscribe()
        uploadShopImageUseCase.unsubscribe()
    }

    fun cancelValidateShopName() {
        validateShopNameJob?.cancel()
    }

    fun cancelValidateShopDomain() {
        validateShopDomainJob?.cancel()
    }

    private fun getShopDomainSuggestion(shopName: String) {
        launchCatchError(block = {
            val data = withContext(dispatchers.io) {
                delay(INPUT_DELAY)

                val requestParams = GetShopDomainNameSuggestionUseCase.createRequestParams(shopName)
                getShopDomainNameSuggestionUseCase.params = requestParams
                getShopDomainNameSuggestionUseCase.executeOnBackground()
            }
            _shopDomainSuggestion.value = Success(data)
        }) {
            _shopDomainSuggestion.value = Fail(it)
        }
    }

    private fun updateShopBasicData(requestParams: RequestParams) {
        launchCatchError(block = {
            val updateShopBasicData = withContext(dispatchers.io) {
                updateShopBasicDataUseCase.getData(requestParams)
            }
            _updateShopBasicData.value = Success(updateShopBasicData)
        }) {
            _updateShopBasicData.value = Fail(it)
        }
    }

    fun setCurrentShopName(shopName: String) {
        currentShopName = shopName
    }

    fun setCurrentDomainName(domainName: String) {
        currentDomainName = domainName
    }

    private fun updateShopBasicData(tagline: String, description: String, logoCode: String?) {
        updateShopBasicDataUseCase.unsubscribe()

        val requestParams = UpdateShopBasicDataUseCase.createRequestParams(tagline,
            description, logoCode, null, null)
        updateShopBasicData(requestParams)
    }

    private fun isDomainChangeAllowed(): Boolean {
        val data = (_allowShopNameDomainChanges.value as? Success<AllowShopNameDomainChangesData>)?.data
        return data?.isDomainAllowed ?: false
    }
}