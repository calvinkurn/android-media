package com.tokopedia.epharmacy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyPrescriptionDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyProductDataModel
import com.tokopedia.epharmacy.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.epharmacy.network.response.EPharmacyDataResponse
import com.tokopedia.epharmacy.network.response.EPharmacyPrescriptionUploadResponse
import com.tokopedia.epharmacy.network.response.EPharmacyUploadPrescriptionIdsResponse
import com.tokopedia.epharmacy.network.response.PrescriptionImage
import com.tokopedia.epharmacy.usecase.*
import com.tokopedia.epharmacy.utils.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import java.lang.Exception
import java.lang.reflect.Type
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class UploadPrescriptionViewModel @Inject constructor(
    private val getEPharmacyOrderDetailUseCase: GetEPharmacyOrderDetailUseCase,
    private val getEPharmacyCheckoutDetailUseCase: GetEPharmacyCheckoutDetailUseCase,
    private val uploadPrescriptionUseCase: UploadPrescriptionUseCase,
    private val postPrescriptionIdUseCase: PostPrescriptionIdUseCase,
    @CoroutineBackgroundDispatcher private val dispatcherBackground: CoroutineDispatcher
) : BaseViewModel(dispatcherBackground) {

    private val _productDetailLiveData = MutableLiveData<Result<EPharmacyDataModel>>()
    val productDetailLiveDataResponse: LiveData<Result<EPharmacyDataModel>> = _productDetailLiveData

    private val _uploadPrescriptionIdsData = MutableLiveData<Result<Boolean>>()
    val uploadPrescriptionIdsData: LiveData<Result<Boolean>> = _uploadPrescriptionIdsData

    private val _buttonLiveData = MutableLiveData<String?>()
    val buttonLiveData: LiveData<String?> = _buttonLiveData

    private val _uploadError = MutableLiveData<EPharmacyUploadError>()
    val uploadError: LiveData<EPharmacyUploadError> = _uploadError

    private val _prescriptionImages = MutableLiveData<ArrayList<PrescriptionImage?>>()
    val prescriptionImages: LiveData<ArrayList<PrescriptionImage?>> = _prescriptionImages

    private val _successUploadPhoto = MutableLiveData<Boolean>()
    val successUploadPhoto: LiveData<Boolean> = _successUploadPhoto

    fun getEPharmacyOrderDetail(orderId: Long) {
        getEPharmacyOrderDetailUseCase.cancelJobs()
        getEPharmacyOrderDetailUseCase.getEPharmacyOrderDetail(
            ::onAvailableEPharmacyOrderDetail,
            ::onFailEPharmacyDetail,
            orderId
        )
    }

    private fun onAvailableEPharmacyOrderDetail(ePharmacyDetailResponse: EPharmacyDataResponse) {
        ePharmacyDetailResponse.let { data ->
            if (data.detailData?.formData?.ePharmacyProducts?.isEmpty() == true) {
                onFailEPharmacyDetail(IllegalStateException("Data invalid"))
            } else {
                _productDetailLiveData.postValue(Success(mapOrderResponseInDataModel(data)))
                if (data.detailData?.formData?.isReUploadEnabled == true) {
                    _buttonLiveData.postValue(EPharmacyButtonKey.RE_UPLOAD.key)
                } else {
                    _buttonLiveData.postValue(EPharmacyButtonKey.CHECK.key)
                }
            }
        }
    }

    private fun mapOrderResponseInDataModel(data: EPharmacyDataResponse): EPharmacyDataModel {
        val listOfComponents = arrayListOf<BaseEPharmacyDataModel>()
        if (data.detailData?.formData?.isReUploadEnabled == true) {
            data.detailData.formData.prescriptionImages?.removeAll(getRejectedPrescriptions(data))
        }
        val prescriptionDataModel = EPharmacyPrescriptionDataModel(
            PRESCRIPTION_COMPONENT,
            PRESCRIPTION_COMPONENT,
            data.detailData?.formData?.prescriptionImages,
            data.detailData?.formData?.isReUploadEnabled ?: false
        )
        listOfComponents.add(prescriptionDataModel)
        data.detailData?.formData?.ePharmacyProducts?.forEachIndexed { index, eProduct ->
            if (index == FIRST_INDEX) {
                eProduct?.shopId = data.detailData.formData.shopId
                eProduct?.shopName = data.detailData.formData.shopName
                eProduct?.shopLocation = data.detailData.formData.shopLocation
                eProduct?.shopType = data.detailData.formData.shopType
                eProduct?.shopLogoUrl = data.detailData.formData.shopLogoUrl
            }
            listOfComponents.add(
                EPharmacyProductDataModel(
                    PRODUCT_COMPONENT,
                    eProduct?.productId ?: eProduct.hashCode().toString(),
                    eProduct
                )
            )
        }
        return EPharmacyDataModel(listOfComponents)
    }

    private fun onFailEPharmacyDetail(throwable: Throwable) {
        _productDetailLiveData.postValue(Fail(throwable))
    }

    fun getEPharmacyCheckoutDetail(checkoutId: String, source: String) {
        getEPharmacyCheckoutDetailUseCase.cancelJobs()
        getEPharmacyCheckoutDetailUseCase.getEPharmacyCheckoutDetail(
            ::onAvailableEPharmacyCheckoutDetail,
            ::onFailEPharmacyDetail,
            checkoutId,
            source
        )
    }

    private fun onAvailableEPharmacyCheckoutDetail(ePharmacyDetailResponse: EPharmacyDataResponse) {
        ePharmacyDetailResponse.let { data ->
            if (data.detailData?.formData?.ePharmacyProducts?.isEmpty() == true) {
                onFailEPharmacyDetail(IllegalStateException("Data invalid"))
            } else {
                _productDetailLiveData.postValue(Success(mapCheckoutResponseInDataModel(data)))
                if (ePharmacyDetailResponse.detailData?.formData?.prescriptionImages.isNullOrEmpty()) {
                    _buttonLiveData.postValue(EPharmacyButtonKey.RE_UPLOAD.key)
                } else {
                    _buttonLiveData.postValue(EPharmacyButtonKey.CHECK.key)
                }
            }
        }
    }

    private fun getRejectedPrescriptions(data: EPharmacyDataResponse): ArrayList<PrescriptionImage?> {
        val rejectedPrescriptions = arrayListOf<PrescriptionImage?>()
        data.detailData?.formData?.prescriptionImages?.forEach { prescriptionImage ->
            if (prescriptionImage?.status == EPharmacyPrescriptionStatus.REJECTED.status) {
                rejectedPrescriptions.add(prescriptionImage)
            }
        }
        return rejectedPrescriptions
    }

    private fun mapCheckoutResponseInDataModel(data: EPharmacyDataResponse): EPharmacyDataModel {
        val listOfComponents = arrayListOf<BaseEPharmacyDataModel>()
        val prescriptionDataModel = EPharmacyPrescriptionDataModel(
            PRESCRIPTION_COMPONENT,
            PRESCRIPTION_COMPONENT,
            data.detailData?.formData?.prescriptionImages,
            data.detailData?.formData?.isReUploadEnabled ?: false
        )
        listOfComponents.add(prescriptionDataModel)
        data.detailData?.formData?.ePharmacyProducts?.forEachIndexed { index, eProduct ->
            eProduct?.ePharmacyProducts?.forEachIndexed { indexProduct, ePharmacyProduct ->
                if (indexProduct == FIRST_INDEX) {
                    ePharmacyProduct?.shopId = eProduct.shopId
                    ePharmacyProduct?.shopName = eProduct.shopName
                    ePharmacyProduct?.shopLocation = eProduct.shopLocation
                    ePharmacyProduct?.shopType = eProduct.shopType
                    ePharmacyProduct?.shopLogoUrl = eProduct.shopLogoUrl
                }
                if (indexProduct == (eProduct.ePharmacyProducts.size - 1) && index != (data.detailData.formData.ePharmacyProducts.size - 1)) {
                    ePharmacyProduct?.divider = true
                }
                listOfComponents.add(
                    EPharmacyProductDataModel(
                        PRODUCT_COMPONENT,
                        ePharmacyProduct?.productId ?: eProduct.hashCode().toString(),
                        ePharmacyProduct
                    )
                )
            }
        }
        return EPharmacyDataModel(listOfComponents)
    }

    fun onSuccessGetPrescriptionImages(arrayList: ArrayList<PrescriptionImage?>) {
        _prescriptionImages.postValue(arrayList)
    }

    fun addSelectedPrescriptionImages(originalPaths: List<String>) {
        _prescriptionImages.value.let { prescriptionImages ->
            originalPaths.forEach { localPath ->
                changeToLoadingState(prescriptionImages, localPath)
                checkPrescriptionImages()
                uploadImageWithPath((prescriptionImages?.size ?: 0) - 1, localPath)
            }
            _prescriptionImages.postValue(prescriptionImages)
        }
    }

    fun reUploadPrescriptionImage(uniquePositionId: Int, localPath: String) {
        _prescriptionImages.value?.let {
            changeToLoadingState(it, uniquePositionId)
            uploadImageWithPath(uniquePositionId, localPath)
            _prescriptionImages.postValue(it)
        }
    }

    private fun changeToLoadingState(arrayList: ArrayList<PrescriptionImage?>?, localPath: String) {
        arrayList?.add(
            PrescriptionImage(
                "", DEFAULT_ZERO_VALUE, "",
                EPharmacyPrescriptionStatus.SELECTED.status,
                isUploading = true,
                isUploadSuccess = false,
                isDeletable = true,
                isUploadFailed = false, localPath,
                null
            )
        )
    }

    private fun changeToLoadingState(arrayList: ArrayList<PrescriptionImage?>?, position: Int) {
        arrayList?.get(position)?.apply {
            isUploadSuccess = false
            isUploading = true
            isUploadFailed = false
        }
        _prescriptionImages.postValue(arrayList)
    }

    private fun uploadImageWithPath(uniquePositionId: Int, path: String) {
        launchCatchError(block = {
            uploadImageToServer(uniquePositionId, path)
        }, onError = {
                when (it) {
                    is UnknownHostException, is SocketTimeoutException -> uploadFailed(uniquePositionId, EPharmacyNoInternetError(true))
                    else -> imageError(uniquePositionId, it)
                }
            })
    }

    private fun imageError(uniquePositionId: Int, error: Throwable) {
        uploadFailed(uniquePositionId, EPharmacyUploadEmptyImageError(false))
        EPharmacyUtils.logException(Exception(error))
    }

    private suspend fun uploadImageToServer(uniquePositionId: Int, localFilePath: String) {
        if (localFilePath.isNotBlank()) {
            val result = convertToUploadImageResponse(
                uploadPrescriptionUseCase.executeOnBackground(
                    (uniquePositionId + 1).toLong(),
                    localFilePath
                )
            )
            _prescriptionImages.value?.get(uniquePositionId)?.apply {
                result.data?.firstOrNull()?.let { uploadResult ->
                    if (uploadResult.prescriptionId != null && uploadResult.prescriptionId != DEFAULT_ZERO_VALUE) {
                        isUploadSuccess = true
                        isUploading = false
                        prescriptionId = uploadResult.prescriptionId
                        _successUploadPhoto.postValue(true)
                    } else {
                        uploadFailed(uniquePositionId, EPharmacyUploadNoPrescriptionIdError(true))
                    }
                } ?: kotlin.run {
                    isUploadSuccess = false
                    isUploading = false
                    isUploadFailed = true
                    result.data?.firstOrNull()?.errorMsg?.let { errorMessage ->
                        if (errorMessage.isNotBlank()) {
                            uploadFailed(uniquePositionId, EPharmacyUploadBackendError(errorMessage))
                        }
                    }
                }
            }
            _prescriptionImages.postValue(_prescriptionImages.value)
        } else {
            uploadFailed(uniquePositionId, EPharmacyUploadEmptyImageError(true))
        }
        checkPrescriptionImages()
    }

    private fun checkPrescriptionImages() {
        var successImagesCount = 0
        var failedImageCount = 0
        var approvedImageCount = 0
        var uploadingImageCount = 0
        val presImageSize = _prescriptionImages.value?.size ?: 0
        _prescriptionImages.value?.forEach { presImage ->
            when {
                (presImage?.isUploadSuccess == true) -> successImagesCount += 1
                (presImage?.isUploadFailed == true) -> failedImageCount += 1
                (presImage?.isUploading == true) -> uploadingImageCount += 1
                (presImage?.status == EPharmacyPrescriptionStatus.APPROVED.status) -> approvedImageCount += 1
            }
        }
        val key = if (failedImageCount > 0 || uploadingImageCount > 0) {
            EPharmacyButtonKey.DONE_DISABLED.key
        } else {
            if ((successImagesCount == presImageSize && presImageSize != 0) ||
                (successImagesCount + approvedImageCount) == presImageSize && presImageSize != 0
            ) {
                EPharmacyButtonKey.DONE.key
            } else {
                EPharmacyButtonKey.RE_UPLOAD.key
            }
        }
        _buttonLiveData.postValue(key)
    }

    private fun uploadFailed(uniquePositionId: Int, ePharmacyUploadError: EPharmacyUploadError) {
        _prescriptionImages.value?.get(uniquePositionId)?.apply {
            isUploadSuccess = false
            isUploading = false
            isUploadFailed = true
        }
        _prescriptionImages.postValue(_prescriptionImages.value)
        _uploadError.postValue(ePharmacyUploadError)
        checkPrescriptionImages()
    }

    fun uploadPrescriptionIdsInOrder(orderId: Long) {
        getPrescriptionIds()?.let {
            postPrescriptionIdUseCase.cancelJobs()
            postPrescriptionIdUseCase.postPrescriptionIdsOrder(
                ::onUploadPrescriptionIdSuccess,
                ::onUploadPrescriptionIdFail,
                orderId,
                it
            )
        }
    }

    fun uploadPrescriptionIdsInCheckout(checkoutId: String) {
        getPrescriptionIds()?.let {
            postPrescriptionIdUseCase.cancelJobs()
            postPrescriptionIdUseCase.postPrescriptionIdsCheckout(
                ::onUploadPrescriptionIdSuccess,
                ::onUploadPrescriptionIdFail,
                checkoutId,
                it
            )
        }
    }

    private fun getPrescriptionIds(): ArrayList<Long?>? {
        val prescriptionIds = arrayListOf<Long?>()
        _prescriptionImages.value?.forEach { prescriptionImage ->
            if (prescriptionImage?.prescriptionId != DEFAULT_ZERO_VALUE) {
                prescriptionIds.add(prescriptionImage?.prescriptionId)
            } else {
                checkPrescriptionImages()
                return null
            }
        }
        return prescriptionIds
    }

    private fun onUploadPrescriptionIdSuccess(data: EPharmacyUploadPrescriptionIdsResponse) {
        if (data.confirmPrescriptionIDs?.success == true) {
            _uploadPrescriptionIdsData.postValue(Success(true))
        } else {
            _uploadPrescriptionIdsData.postValue(Fail(Throwable()))
        }
    }

    private fun onUploadPrescriptionIdFail(error: Throwable) {
        _uploadPrescriptionIdsData.postValue(Fail(error))
    }

    fun convertToUploadImageResponse(typeRestResponseMap: Map<Type, RestResponse>): EPharmacyPrescriptionUploadResponse {
        return typeRestResponseMap[EPharmacyPrescriptionUploadResponse::class.java]?.getData() as EPharmacyPrescriptionUploadResponse
    }

    fun removePrescriptionImageAt(index: Int) {
        _prescriptionImages.value?.let {
            if (index < it.size) {
                it.removeAt(index)
                _prescriptionImages.postValue(it)
                checkPrescriptionImages()
            }
        }
    }

    override fun onCleared() {
        getEPharmacyOrderDetailUseCase.cancelJobs()
        getEPharmacyCheckoutDetailUseCase.cancelJobs()
        uploadPrescriptionUseCase.cancelJobs()
        postPrescriptionIdUseCase.cancelJobs()
        super.onCleared()
    }
}
