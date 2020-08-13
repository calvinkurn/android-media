package com.tokopedia.shop.settings.basicinfo.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.settings.common.coroutine.CoroutineDispatchers
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopBasicDataUseCase
import com.tokopedia.shop.settings.basicinfo.data.AllowShopNameDomainChanges.*
import com.tokopedia.shop.settings.basicinfo.data.UploadShopEditImageModel
import com.tokopedia.shop.settings.basicinfo.domain.GetAllowShopNameDomainChanges
import com.tokopedia.shop.settings.basicinfo.domain.UploadShopImageUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopEditBasicInfoViewModel @Inject constructor(
    private val getShopBasicDataUseCase: GetShopBasicDataUseCase,
    private val updateShopBasicDataUseCase: UpdateShopBasicDataUseCase,
    private val uploadShopImageUseCase: UploadShopImageUseCase,
    private val getAllowShopNameDomainChanges: GetAllowShopNameDomainChanges,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val shopBasicData: LiveData<Result<ShopBasicDataModel>>
        get() = _shopBasicData
    val uploadShopImage: LiveData<Result<UploadShopEditImageModel>>
        get() = _uploadShopImage
    val updateShopBasicData: LiveData<Result<String>>
        get() = _updateShopBasicData
    val allowShopNameDomainChanges: LiveData<Result<AllowShopNameDomainChangesData>>
        get() = _allowShopNameDomainChanges

    private val _shopBasicData = MutableLiveData<Result<ShopBasicDataModel>>()
    private val _uploadShopImage = MutableLiveData<Result<UploadShopEditImageModel>>()
    private val _updateShopBasicData = MutableLiveData<Result<String>>()
    private val _allowShopNameDomainChanges = MutableLiveData<Result<AllowShopNameDomainChangesData>>()

    fun getAllowShopNameDomainChanges() {
        launchCatchError(block = {
            val data = withContext(dispatchers.io) {
                val allowShopNameDomainChanges = getAllowShopNameDomainChanges.executeOnBackground()
                allowShopNameDomainChanges.data.copy(isDomainAllowed = false, reasonDomainNotAllowed = "Udah pernah ganti domain")
            }
            _allowShopNameDomainChanges.value = Success(data)
        }) {
            _allowShopNameDomainChanges.value = Fail(it)
        }
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

    private fun updateShopBasicData(tagline: String, description: String, logoCode: String?) {
        updateShopBasicDataUseCase.unsubscribe()

        val requestParams = UpdateShopBasicDataUseCase.createRequestParams(tagline,
            description, logoCode, null, null)
        updateShopBasicData(requestParams)
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

    fun detachView() {
        getShopBasicDataUseCase.unsubscribe()
        updateShopBasicDataUseCase.unsubscribe()
        uploadShopImageUseCase.unsubscribe()
    }
}