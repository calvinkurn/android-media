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
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.GetShopDomainNameSuggestionUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.ValidateDomainShopNameUseCase
import com.tokopedia.shop.settings.basicinfo.data.AllowShopNameDomainChangesData
import com.tokopedia.shop.settings.basicinfo.data.UploadShopEditImageModel
import com.tokopedia.shop.settings.basicinfo.domain.GetAllowShopNameDomainChanges
import com.tokopedia.shop.settings.basicinfo.domain.UploadShopImageUseCase
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopEditBasicInfoFragment.Companion.INPUT_DELAY
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class ShopEditBasicInfoViewModel @Inject constructor(
        private val getShopBasicDataUseCase: GetShopBasicDataUseCase,
        private val updateShopBasicDataUseCase: UpdateShopBasicDataUseCase,
        private val uploadShopImageUseCase: UploadShopImageUseCase,
        private val getAllowShopNameDomainChangesUseCase: GetAllowShopNameDomainChanges,
        private val getShopDomainNameSuggestionUseCase: GetShopDomainNameSuggestionUseCase,
        private val validateDomainShopNameUseCase: ValidateDomainShopNameUseCase,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val shopBasicData: LiveData<Result<ShopBasicDataModel>>
        get() = _shopBasicData
    val uploadShopImage: LiveData<Result<UploadShopEditImageModel>>
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
    private val _uploadShopImage = MutableLiveData<Result<UploadShopEditImageModel>>()
    private val _updateShopBasicData = MutableLiveData<Result<ShopBasicDataMutation>>()
    private val _allowShopNameDomainChanges = MutableLiveData<Result<AllowShopNameDomainChangesData>>()
    private val _validateShopName = MutableLiveData<Result<ValidateShopDomainNameResult>>()
    private val _validateShopDomain = MutableLiveData<Result<ValidateShopDomainNameResult>>()
    private val _shopDomainSuggestion = MutableLiveData<Result<ShopDomainSuggestionData>>()

    private val shopNameValidation = MutableLiveData<String>()
    private val shopDomainValidation = MutableLiveData<String>()
    private val uploadShopImageParams = MutableLiveData<RequestParams>()
    private val getShopDomainNameSuggestionParams = MutableLiveData<RequestParams>()
    private val getShopBasicDataParams = MutableLiveData<RequestParams>()
    private val updateShopBasicDataParams = MutableLiveData<RequestParams>()

    private var currentShopName: String? = null
    private var currentShop: ShopBasicDataModel? = null

    private var name: String = ""
    private var domain: String = ""
    private var tagLine: String = ""
    private var description: String = ""

    init {
        initShopNameValidation()
        initShopDomainValidation()
        initUploadShopImage()
        initGetShopDomainNameSuggestion()
        initGetShopBasicData()
        initUpdateShopBasicData()
    }

    fun validateShopName(shopName: String) {
        if(shopName == currentShop?.name) return
        shopNameValidation.value = shopName
        currentShopName = shopName
    }

    fun validateShopDomain(shopDomain: String) {
        if(shopDomain == currentShop?.domain) return
        shopDomainValidation.value = shopDomain
    }

    fun getShopBasicData() = launch {
        getShopBasicDataParams.value = RequestParams.EMPTY
    }

    fun uploadShopImage(
            imagePath: String,
            name: String,
            domain: String,
            tagLine: String,
            description: String
    ) {
        uploadShopImageParams.value = UploadShopImageUseCase.createRequestParams(imagePath)
        this.name = name
        this.domain = domain
        this.tagLine = tagLine
        this.description = description
    }

    fun updateShopBasicData(
            name: String,
            domain: String,
            tagLine: String,
            description: String,
            logoCode: String? = null
    ) {
        val shopName = name.nullIfNotChanged(currentShop?.name)
        val shopDomain = domain.nullIfNotChanged(currentShop?.domain)

        val requestParams = UpdateShopBasicDataUseCase.createRequestParam(
                shopName, shopDomain, tagLine, description, logoCode)

        updateShopBasicData(requestParams)
    }

    fun setCurrentShopData(data: ShopBasicDataModel) {
        currentShop = data
        currentShopName = data.name
    }

    fun getAllowShopNameDomainChanges() {
        launchCatchError(dispatchers.io, block = {
            _allowShopNameDomainChanges.postValue(Success(getAllowShopNameDomainChangesAsync().await()))
        }, onError = {
            _allowShopNameDomainChanges.postValue(Fail(it))
        })
    }

    private fun getShopDomainSuggestion(shopName: String) {
        getShopDomainNameSuggestionParams.value = GetShopDomainNameSuggestionUseCase.createRequestParams(shopName)
    }

    private fun updateShopBasicData(requestParams: RequestParams) {
        updateShopBasicDataParams.value = requestParams
    }

    private fun initGetShopBasicData() = launch {
        getShopBasicDataParams
                .asFlow()
                .map {
                    getShopBasicDataUseCase.getData(it)
                }
                .flowOn(dispatchers.io)
                .catch {
                    _shopBasicData.value = Fail(it)
                }
                .collectLatest {
                    _shopBasicData.value = Success(it)
                }
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
                .catch {
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
                .catch {
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

    private fun initUploadShopImage() = launch {
        uploadShopImageParams
                .asFlow()
                .map {
                    uploadShopImageUseCase.getData(it)
                }.flowOn(dispatchers.io)
                .catch {
                    _uploadShopImage.value = Fail(it)
                }.collectLatest {
                    it.data?.image?.picCode?.let { picCode ->
                        updateShopBasicData(name, domain, tagLine, description, picCode)
                    }
                    _uploadShopImage.value = Success(it)
                }
    }

    private fun initGetShopDomainNameSuggestion() = launch {
        getShopDomainNameSuggestionParams
                .asFlow()
                .map {
                    getShopDomainNameSuggestionUseCase.params = it
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

    private fun initUpdateShopBasicData() = launch {
        updateShopBasicDataParams
                .asFlow()
                .map {
                    updateShopBasicDataUseCase.setParams(it)
                    updateShopBasicDataUseCase.executeOnBackground()
                }.flowOn(dispatchers.io)
                .catch {
                    _updateShopBasicData.value = Fail(it)
                }
                .collectLatest {
                    _updateShopBasicData.value = Success(it)
                }
    }

    private fun getAllowShopNameDomainChangesAsync(): Deferred<AllowShopNameDomainChangesData> {
        return async(start = CoroutineStart.LAZY, context = dispatchers.io) {
            var allowChanges = AllowShopNameDomainChangesData()
            try {
                allowChanges = getAllowShopNameDomainChangesUseCase.executeOnBackground().data
            } catch (t: Throwable) {
                _allowShopNameDomainChanges.postValue(Fail(t))
            }
            allowChanges
        }
    }

    private fun String.nullIfNotChanged(previousValue: String?): String? {
        val currentValue = this
        return if(currentValue == previousValue) null else currentValue
    }
}