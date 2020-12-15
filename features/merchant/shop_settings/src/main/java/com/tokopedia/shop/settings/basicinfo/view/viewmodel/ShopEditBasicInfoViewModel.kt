package com.tokopedia.shop.settings.basicinfo.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.graphql.data.shopbasicdata.gql.ShopBasicDataMutation
import com.tokopedia.shop.common.graphql.data.shopopen.ShopDomainSuggestionData
import com.tokopedia.shop.common.graphql.data.shopopen.ValidateShopDomainNameResult
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

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

    private var currentShopName: String? = null
    private var currentShop: ShopBasicDataModel? = null
    var timeAllowShopNameDomain = 0L
    var timeValidateShopName = 0L
    var timeValidateShopDomain = 0L
    var timeUploadShopImage = 0L
    var timeUpdateShopBasicData = 0L
    var timeGetShopDomainSuggestion = 0L

    fun getAllowShopNameDomainChanges() {
        timeAllowShopNameDomain = System.currentTimeMillis()
        launchCatchError(block = {
            flow {
                emit(getAllowShopNameDomainChangesUseCase.executeOnBackground())
            }.flowOn(dispatchers.io)
                    .collectLatest {
                        _allowShopNameDomainChanges.value = Success(it.data)
                        Log.d("SHOP_SETTINGS", "allow shop name domain - ${System.currentTimeMillis() - timeAllowShopNameDomain}")
                    }
        }) {
            _allowShopNameDomainChanges.value = Fail(it)
        }
    }

    fun validateShopName(shopName: String) {
        timeValidateShopName = System.currentTimeMillis()
        if(shopName == currentShop?.name) return

        launchCatchError(block = {
            flow {
                delay(INPUT_DELAY)
                validateDomainShopNameUseCase.params = ValidateDomainShopNameUseCase.createRequestParams(shopName)
                emit(validateDomainShopNameUseCase.executeOnBackground())
            }.flowOn(dispatchers.io)
                    .conflate()
                    .collectLatest {
                         _validateShopName.value = Success(it)
                        Log.d("SHOP_SETTINGS", "validate shop name - ${System.currentTimeMillis() - timeValidateShopName}")
                    }
        }) {
            _validateShopName.value = Fail(it)
        }

        setCurrentShopName(shopName)
    }

    fun validateShopDomain(domain: String) {
        timeValidateShopDomain = System.currentTimeMillis()
        if(domain == currentShop?.domain) return

        launchCatchError(block = {
            flow {
                delay(INPUT_DELAY)
                validateDomainShopNameUseCase.params = ValidateDomainShopNameUseCase.createRequestParam(domain)
                emit(validateDomainShopNameUseCase.executeOnBackground())
            }.flowOn(dispatchers.io)
                    .conflate()
                    .collectLatest { result ->
                        if(!result.validateDomainShopName.isValid) {
                            currentShopName?.let { getShopDomainSuggestion(it) }
                        }
                        _validateShopDomain.value = Success(result)
                        Log.d("SHOP_SETTINGS", "validate shop domain - ${System.currentTimeMillis() - timeValidateShopDomain}")
                    }
        }) {
            _validateShopDomain.value = Fail(it)
        }
    }

    fun getShopBasicData() {
        launchCatchError(block = {
            flow {
                emit(getShopBasicDataUseCase.getData(RequestParams.EMPTY))
            }.flowOn(dispatchers.io)
                    .collectLatest {
                        _shopBasicData.value = Success(it)
                    }
        }) {
            _shopBasicData.value = Fail(it)
        }
    }

    fun uploadShopImage(
        imagePath: String,
        name: String,
        domain: String,
        tagLine: String,
        description: String
    ) {
        timeUploadShopImage = System.currentTimeMillis()
        launchCatchError(block = {
            flow {
                val requestParams = UploadShopImageUseCase.createRequestParams(imagePath)
                emit(uploadShopImageUseCase.getData(requestParams))
            }.flowOn(dispatchers.io)
                    .collectLatest {
                        it.data?.image?.picCode?.let { picCode ->
                            updateShopBasicData(name, domain, tagLine, description, picCode)
                        }
                        _uploadShopImage.value = Success(it)
                        Log.d("SHOP_SETTINGS", "upload shop image - ${System.currentTimeMillis() - timeUploadShopImage}")
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
        logoCode: String? = null
    ) {
        timeUpdateShopBasicData = System.currentTimeMillis()
        val shopName = name.nullIfNotChanged(currentShop?.name)
        val shopDomain = domain.nullIfNotChanged(currentShop?.domain)

        val requestParams = UpdateShopBasicDataUseCase.createRequestParam(
            shopName, shopDomain, tagLine, description, logoCode)

        updateShopBasicData(requestParams)
    }

    fun setCurrentShopData(data: ShopBasicDataModel) {
        currentShop = data
        setCurrentShopName(data.name)
    }

    private fun setCurrentShopName(shopName: String?) {
        currentShopName = shopName
    }

    private fun getShopDomainSuggestion(shopName: String) {
        timeGetShopDomainSuggestion = System.currentTimeMillis()
        launchCatchError(block = {
            flow {
                delay(INPUT_DELAY)
                getShopDomainNameSuggestionUseCase.params = GetShopDomainNameSuggestionUseCase.createRequestParams(shopName)
                emit(getShopDomainNameSuggestionUseCase.executeOnBackground())
            }.flowOn(dispatchers.io)
                    .conflate()
                    .collectLatest {
                        _shopDomainSuggestion.value = Success(it)
                        Log.d("SHOP_SETTINGS", "get shop domain suggestion - ${System.currentTimeMillis() - timeGetShopDomainSuggestion}")
                    }
        }) {
            _shopDomainSuggestion.value = Fail(it)
        }
    }

    private fun updateShopBasicData(requestParams: RequestParams) {
        launchCatchError(block = {
            flow {
                updateShopBasicDataUseCase.setParams(requestParams)
                emit(updateShopBasicDataUseCase.executeOnBackground())
            }.flowOn(dispatchers.io)
                    .collectLatest {
                        _updateShopBasicData.value = Success(it)
                        Log.d("SHOP_SETTINGS", "get shop basic data - ${System.currentTimeMillis() - timeUpdateShopBasicData}")
                    }
        }) {
            _updateShopBasicData.value = Fail(it)
        }
    }

    private fun String.nullIfNotChanged(previousValue: String?): String? {
        val currentValue = this
        return if(currentValue == previousValue) null else currentValue
    }
}