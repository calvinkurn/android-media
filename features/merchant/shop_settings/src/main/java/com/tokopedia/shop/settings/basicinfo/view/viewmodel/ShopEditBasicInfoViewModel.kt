package com.tokopedia.shop.settings.basicinfo.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.graphql.data.shopbasicdata.gql.ShopBasicDataMutation
import com.tokopedia.shop.common.graphql.data.shopopen.ShopDomainSuggestionData
import com.tokopedia.shop.common.graphql.data.shopopen.ValidateShopDomainNameResult
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.GetShopDomainNameSuggestionUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.ValidateDomainShopNameUseCase
import com.tokopedia.shop.settings.basicinfo.data.AllowShopNameDomainChangesData
import com.tokopedia.shop.settings.basicinfo.domain.GetAllowShopNameDomainChanges
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopEditBasicInfoFragment.Companion.INPUT_DELAY
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopEditBasicInfoFragment.Companion.UPLOADER_SOURCE_ID
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File
import java.lang.Exception
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class ShopEditBasicInfoViewModel @Inject constructor(
        private val getShopBasicDataUseCase: GetShopBasicDataUseCase,
        private val updateShopBasicDataUseCase: UpdateShopBasicDataUseCase,
        private val uploaderUseCase: UploaderUseCase,
        private val getAllowShopNameDomainChangesUseCase: GetAllowShopNameDomainChanges,
        private val getShopDomainNameSuggestionUseCase: GetShopDomainNameSuggestionUseCase,
        private val validateDomainShopNameUseCase: ValidateDomainShopNameUseCase,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val shopBasicData: LiveData<Result<ShopBasicDataModel>>
        get() = _shopBasicData
    val uploadShopImage: LiveData<Result<String>>
        get() = _uploadShopImage
    val updateShopBasicData: LiveData<Result<ShopBasicDataMutation>>
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
    private val _uploadShopImage = MutableLiveData<Result<String>>()
    private val _updateShopBasicData = MutableLiveData<Result<ShopBasicDataMutation>>()
    private val _allowShopNameDomainChanges = MutableLiveData<Result<AllowShopNameDomainChangesData>>()
    private val _validateShopName = MutableLiveData<Result<ValidateShopDomainNameResult>>()
    private val _validateShopDomain = MutableLiveData<Result<ValidateShopDomainNameResult>>()
    private val _shopDomainSuggestion = MutableLiveData<Result<ShopDomainSuggestionData>>()

    private val shopNameValidation = MutableLiveData<String>()
    private val shopDomainValidation = MutableLiveData<String>()
    private val getShopDomainNameSuggestionParams = MutableLiveData<String>()

    private var currentShopName: String? = null
    private var currentShop: ShopBasicDataModel? = null

    init {
        initShopNameValidation()
        initShopDomainValidation()
        initGetShopDomainNameSuggestion()
    }

    fun validateShopName(shopName: String) {
        shopNameValidation.value = shopName
    }

    fun validateShopDomain(shopDomain: String) {
        shopDomainValidation.value = shopDomain
    }

    fun setCurrentShopData(data: ShopBasicDataModel) {
        currentShop = data
        currentShopName = data.name
    }

    fun setShopName(shopName: String) {
        currentShopName = shopName
    }

    fun uploadShopImage(
            imagePath: String,
            name: String,
            domain: String,
            tagLine: String,
            description: String
    ) {
        launchCatchError(block = {
            val requestParams = uploaderUseCase.createParams(
                sourceId = UPLOADER_SOURCE_ID,
                filePath = File(imagePath)
            )
            withContext(dispatchers.io) {
                when (val result = uploaderUseCase(requestParams)) {
                    is UploadResult.Success -> {
                        updateShopBasicData(name, domain, tagLine, description, result.uploadId)
                        _uploadShopImage.postValue(Success(result.uploadId))
                    }
                    is UploadResult.Error -> {
                        _uploadShopImage.postValue(Fail(Throwable(result.message)))
                    }
                }
            }
        }) {
            _uploadShopImage.value = Fail(it)
        }
    }

    fun updateShopBasicData(
            name: String,
            domain: String,
            tagLine: String,
            description: String,
            imgId: String? = null
    ) {
        val shopName = name.nullIfNotChanged(currentShop?.name)
        val shopDomain = domain.nullIfNotChanged(currentShop?.domain)

        val requestParams = UpdateShopBasicDataUseCase.createRequestParam(
                shopName, shopDomain, tagLine, description, imgId)

        updateShopBasicData(requestParams)
    }

    fun getAllowShopNameDomainChanges() {
        launchCatchError(dispatchers.io, block = {
            val allowChanges = getAllowShopNameDomainChangesUseCase.executeOnBackground().data
            _allowShopNameDomainChanges.postValue(Success(allowChanges))
        }, onError = {
            _allowShopNameDomainChanges.postValue(Fail(it))
        })
    }

    fun getShopBasicData() {
        launchCatchError(block = {
            val shopBasicData = withContext(dispatchers.io) {
                getShopBasicDataUseCase.getData(RequestParams.EMPTY)
            }
            _shopBasicData.value = Success(shopBasicData)
        }) {
            _shopBasicData.value = Fail(it)
        }
    }

    private fun updateShopBasicData(requestParams: RequestParams) {
        launchCatchError(block = {
            updateShopBasicDataUseCase.setParams(requestParams)
            val updateShopBasicData = updateShopBasicDataUseCase.executeOnBackground()
            _updateShopBasicData.value = Success(updateShopBasicData)
        }) {
            _updateShopBasicData.value = Fail(it)
        }
    }

    private fun getShopDomainSuggestion(shopName: String) {
        getShopDomainNameSuggestionParams.value = shopName
    }

    private fun initShopNameValidation() = launch {
        shopNameValidation
                .asFlow()
                .debounce(INPUT_DELAY)
                .map {
                    validateDomainShopNameUseCase.params = ValidateDomainShopNameUseCase.createRequestParams(it)
                    validateDomainShopNameUseCase.executeOnBackground()
                }
                .flowOn(dispatchers.io)
                .retryWhen { cause, _ ->
                    if (cause is Exception) {
                        _validateShopName.value = Fail(cause)
                        true
                    } else {
                        false
                    }
                }.catch {
                    _validateShopName.value = Fail(it)
                }.collectLatest {
                    _validateShopName.value = Success(it)
                }
    }

    private fun initShopDomainValidation() = launch {
        shopDomainValidation
                .asFlow()
                .debounce(INPUT_DELAY)
                .map {
                    validateDomainShopNameUseCase.params = ValidateDomainShopNameUseCase.createRequestParam(it)
                    validateDomainShopNameUseCase.executeOnBackground()
                }
                .flowOn(dispatchers.io)
                .retryWhen { cause, _ ->
                    if (cause is Exception) {
                        _validateShopDomain.value = Fail(cause)
                        true
                    } else {
                        false
                    }
                }.catch {
                    _validateShopDomain.value = Fail(it)
                }.collectLatest {
                    if(!it.validateDomainShopName.isValid) {
                        currentShopName?.let { shopName ->
                            getShopDomainSuggestion(shopName)
                        }
                    }
                    _validateShopDomain.value = Success(it)
                }
    }

    private fun initGetShopDomainNameSuggestion() = launch {
        getShopDomainNameSuggestionParams
                .asFlow()
                .map {
                    getShopDomainNameSuggestionUseCase.params = GetShopDomainNameSuggestionUseCase.createRequestParams(it)
                    getShopDomainNameSuggestionUseCase.executeOnBackground()
                }
                .flowOn(dispatchers.io)
                .conflate()
                .catch {
                    _shopDomainSuggestion.value = Fail(it)
                }.collectLatest {
                    _shopDomainSuggestion.value = Success(it)
                }
    }

    private fun String.nullIfNotChanged(previousValue: String?): String? {
        val currentValue = this
        return if(currentValue == previousValue) null else currentValue
    }
}