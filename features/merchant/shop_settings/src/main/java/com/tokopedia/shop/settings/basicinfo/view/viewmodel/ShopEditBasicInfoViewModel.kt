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
import com.tokopedia.shop.settings.basicinfo.data.AllowShopNameDomainChangesData
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
    private var currentShop: ShopBasicDataModel? = null

    private var _validateShopNameJob: Job? = null
    val validateShopNameJob: Job?
        get() = _validateShopNameJob

    private var _validateShopDomainJob: Job? = null
    val validateShopDomainJob: Job?
        get() = _validateShopDomainJob

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
        if(shopName == currentShop?.name) return

        _validateShopNameJob?.cancel()

        launchCatchError(block = {
            val allowChangeDomain = isDomainChangeAllowed()
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
        }.let { _validateShopNameJob = it }

        setCurrentShopName(shopName)
    }

    fun validateShopDomain(domain: String) {
        if(domain == currentShop?.domain) return

        _validateShopDomainJob?.cancel()

        launchCatchError(block = {
            val data = withContext(dispatchers.io) {
                delay(INPUT_DELAY)

                val requestParams = ValidateDomainShopNameUseCase.createRequestParam(domain)
                validateDomainShopNameUseCase.params = requestParams
                validateDomainShopNameUseCase.executeOnBackground()
            }

            if(!data.validateDomainShopName.isValid) {
                currentShopName?.let { getShopDomainSuggestion(it) }
            }

            _validateShopDomain.value = Success(data)
        }) {
            _validateShopDomain.value = Fail(it)
        }.let { _validateShopDomainJob = it }
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

    fun uploadShopImage(
        imagePath: String,
        name: String,
        domain: String,
        tagline: String,
        description: String
    ) {
        uploadShopImageUseCase.unsubscribe()

        launchCatchError(block = {
            val requestParams = UploadShopImageUseCase.createRequestParams(imagePath)
            val uploadShopImage = withContext(dispatchers.io) {
                uploadShopImageUseCase.getData(requestParams)
            }
            uploadShopImage.data?.image?.picCode?.let { picCode ->
                updateShopBasicData(name, domain, tagline, description, picCode)
            }
            _uploadShopImage.value = Success(uploadShopImage)
        }) {
            _uploadShopImage.value = Fail(it)
        }
    }

    fun updateShopBasicData(
        name: String,
        domain: String,
        tagLine: String,
        description: String,
        logoCode: String? = null
    ) {
        updateShopBasicDataUseCase.unsubscribe()

        val shopName = name.nullIfNotChanged(currentShop?.name)
        val shopDomain = domain.nullIfNotChanged(currentShop?.domain)
        val shopTagLine = tagLine.nullIfNotChanged(currentShop?.tagline)
        val shopDescription = description.nullIfNotChanged(currentShop?.description)

        val requestParams = UpdateShopBasicDataUseCase.createRequestParam(
            shopName, shopDomain, shopTagLine, shopDescription, logoCode)

        updateShopBasicData(requestParams)
    }

    fun detachView() {
        getShopBasicDataUseCase.unsubscribe()
        updateShopBasicDataUseCase.unsubscribe()
        uploadShopImageUseCase.unsubscribe()
    }

    fun cancelValidateShopName() {
        _validateShopNameJob?.cancel()
    }

    fun cancelValidateShopDomain() {
        _validateShopDomainJob?.cancel()
    }

    fun setCurrentShopData(data: ShopBasicDataModel) {
        currentShop = data
        setCurrentShopName(data.name)
    }

    private fun setCurrentShopName(shopName: String?) {
        currentShopName = shopName
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

    private fun isDomainChangeAllowed(): Boolean {
        return (_allowShopNameDomainChanges.value as? Success<AllowShopNameDomainChangesData>)
            ?.data?.isDomainAllowed ?: false
    }

    private fun String.nullIfNotChanged(previousValue: String?): String? {
        val currentValue = this
        return if(currentValue == previousValue) null else currentValue
    }
}