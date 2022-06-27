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
import com.tokopedia.epharmacy.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.epharmacy.network.response.*
import com.tokopedia.epharmacy.usecase.GetEPharmacyCheckoutDetailUseCase
import com.tokopedia.epharmacy.usecase.GetEPharmacyOrderDetailUseCase
import com.tokopedia.epharmacy.usecase.PostPrescriptionIdUseCase
import com.tokopedia.epharmacy.usecase.UploadPrescriptionUseCase
import com.tokopedia.epharmacy.utils.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import javax.inject.Inject

class UploadPrescriptionViewModel @Inject constructor(
    private val getEPharmacyOrderDetailUseCase: GetEPharmacyOrderDetailUseCase,
    private val getEPharmacyCheckoutDetailUseCase: GetEPharmacyCheckoutDetailUseCase,
    private val uploadPrescriptionUseCase: UploadPrescriptionUseCase,
    private val postPrescriptionIdUseCase: PostPrescriptionIdUseCase,
    @CoroutineBackgroundDispatcher private val dispatcherBackground: CoroutineDispatcher,
    @CoroutineMainDispatcher private val dispatcherMain: CoroutineDispatcher
)  : BaseViewModel(dispatcherBackground){

    @Inject
    lateinit var userSession: UserSessionInterface

    private val _productDetailLiveData = MutableLiveData<Result<EPharmacyDataModel>>()
    val productDetailLiveDataResponse: LiveData<Result<EPharmacyDataModel>> = _productDetailLiveData

    private val _uploadPrescriptionIdsData = MutableLiveData<Result<Boolean>>()
    val uploadPrescriptionIdsData: LiveData<Result<Boolean>> = _uploadPrescriptionIdsData

    private val _buttonLiveData = MutableLiveData<String?>()
    val buttonLiveData: LiveData<String?> = _buttonLiveData

    private val _prescriptionImages = MutableLiveData<ArrayList<PrescriptionImage?>>()
    val prescriptionImages: LiveData<ArrayList<PrescriptionImage?>> = _prescriptionImages

    fun getEPharmacyOrderDetail(orderId: String) {
        getEPharmacyOrderDetailUseCase.cancelJobs()
        getEPharmacyOrderDetailUseCase.getEPharmacyOrderDetail(
            ::onAvailableEPharmacyOrderDetail,
            ::onFailEPharmacyDetail,
            orderId
        )
    }

    private fun onAvailableEPharmacyOrderDetail(ePharmacyDetailResponse: EPharmacyDataResponse) {
        ePharmacyDetailResponse.let { data ->
            if(data.ePharmacyProducts?.isEmpty() == true)
                onFailEPharmacyDetail(IllegalStateException("Data invalid"))
            else {
                _productDetailLiveData.postValue(Success(mapResponseInDataModel(data)))
                _buttonLiveData.postValue(data.epharmacyButton?.key)
            }
        }
    }

    private fun mapResponseInDataModel(data: EPharmacyDataResponse) : EPharmacyDataModel{
        val listOfComponents = arrayListOf<BaseEPharmacyDataModel>()
        data.prescriptionImages?.forEach { prescriptionImage ->
            prescriptionImage?.isUploadSuccess = true
            prescriptionImage?.isDeletable = true
        }
        val prescriptionDataModel = EPharmacyPrescriptionDataModel(PRESCRIPTION_COMPONENT,
            PRESCRIPTION_COMPONENT, data.prescriptionImages)
        listOfComponents.add(prescriptionDataModel)
        data.ePharmacyProducts?.forEachIndexed { index, eProduct ->
            if(index == 0){
                eProduct?.shopId = data.shopId
                eProduct?.shopName = data.shopName
                eProduct?.shopLocation = data.shopLocation
                eProduct?.shopType = data.shopType
            }
            listOfComponents.add(EPharmacyProductDataModel(PRODUCT_COMPONENT, eProduct?.productId.toString(),
                eProduct))
        }
        return EPharmacyDataModel(listOfComponents)
    }

    private fun onFailEPharmacyDetail(throwable: Throwable) {
        _productDetailLiveData.postValue(Fail(throwable))
    }

    fun onSuccessGetPrescriptionImages(arrayList: ArrayList<PrescriptionImage?>) {
        _prescriptionImages.postValue(arrayList)
    }


    fun addSelectedPrescriptionImages(originalPaths: List<String>) {
        _prescriptionImages.value.let {
            originalPaths.forEach { localPath ->
                changeToLoadingState(it,localPath)
                uploadImageWithPath((_prescriptionImages.value?.size ?: 0) - 1,localPath)
            }
            _prescriptionImages.postValue(it)
        }
    }

    fun reUploadPrescriptionImage(uniquePositionId : Int ,localPath: String) {
        _prescriptionImages.value?.let {
            changeToLoadingState(it,uniquePositionId)
            uploadImageWithPath(uniquePositionId, localPath)
            _prescriptionImages.postValue(it)
        }
    }

    private fun changeToLoadingState(arrayList: ArrayList<PrescriptionImage?>?, localPath :String) {
        arrayList?.add(PrescriptionImage("","",DEFAULT_ZERO_VALUE,"",
            EPharmacyPrescriptionStatus.SELECTED.status,
            isUploading = true,
            isUploadSuccess = false,
            isDeletable = true,
            isUploadFailed = false, localPath,
            null
        ))
    }

    private fun changeToLoadingState(arrayList: ArrayList<PrescriptionImage?>?, position  :Int) {
        arrayList?.get(position)?.apply {
            isUploadSuccess = false
            isUploading = true
            isUploadFailed = false
        }
        _prescriptionImages.postValue(arrayList)
    }

    private fun uploadImageWithPath(uniquePositionId : Int, path: String) {
        launchCatchError(block = {
            uploadImageToServer(uniquePositionId, path)
        }, onError = {
            uploadFailed(uniquePositionId , it)
        })

    }

    private suspend fun uploadImageToServer(uniquePositionId: Int, localFilePath : String) {
        if (localFilePath.isNotBlank()){
            uploadPrescriptionUseCase.setBase64Image(uniquePositionId.toLong(),localFilePath, userSession.accessToken)
            val result = withContext(dispatcherBackground) {
                convertToUploadImageResponse(uploadPrescriptionUseCase.executeOnBackground())
            }
            _prescriptionImages.value?.get(uniquePositionId)?.apply {
                result.data?.firstOrNull()?.let { uploadResult ->
                    if(uploadResult.prescriptionId != null && uploadResult.prescriptionId != DEFAULT_ZERO_VALUE){
                        isUploadSuccess = true
                        isUploading = false
                        prescriptionId = uploadResult.prescriptionId
                    }else {
                        uploadFailed(uniquePositionId, Throwable("No Prescription Id"))
                    }
                }?: kotlin.run {
                    isUploadSuccess = false
                    isUploading = false
                    isUploadFailed = true
                }
            }
            _prescriptionImages.postValue(_prescriptionImages.value)
        }else {
            uploadFailed(uniquePositionId, Throwable("Empty Image"))
        }
        withContext(dispatcherMain){
            checkPrescriptionImages()
        }
    }

    private fun checkPrescriptionImages() {
        var successImagesCount = 0
        var failedImageCount = 0
        var rejectedImages = 0
        val presImageSize  = _prescriptionImages.value?.size ?: 0
        _prescriptionImages.value?.forEach { presImage ->
            when {
                presImage?.status == EPharmacyPrescriptionStatus.REJECTED.status -> {
                    rejectedImages += 1
                }
                presImage?.isUploadSuccess == true -> {
                    successImagesCount += 1
                }
                presImage?.isUploadFailed == true -> {
                    failedImageCount += 1
                }
            }
        }
        val key = if(failedImageCount > 0){
            EPharmacyButtonKey.DONE_DISABLED.key
        }else {
            if(successImagesCount == presImageSize
                && rejectedImages == 0 && presImageSize != 0){
                EPharmacyButtonKey.DONE.key
            }else {
                EPharmacyButtonKey.RE_UPLOAD.key
            }
        }
        _buttonLiveData.postValue(key)
    }

    private fun uploadFailed(uniquePositionId : Int, exception : Throwable){
        _prescriptionImages.value?.get(uniquePositionId)?.apply {
            isUploadSuccess = false
            isUploading = false
            isUploadFailed = true
        }
        _prescriptionImages.postValue(_prescriptionImages.value)
        checkPrescriptionImages()
    }

    fun uploadPrescriptionIds(uploadKey: String, id: String) {
        val prescriptionIds = arrayListOf<Long?>()
        _prescriptionImages.value?.forEach { prescriptionImage ->
            if(prescriptionImage?.prescriptionId != DEFAULT_ZERO_VALUE){
                prescriptionIds.add(prescriptionImage?.prescriptionId)
            }else {
                checkPrescriptionImages()
                return
            }
        }
        postPrescriptionIdUseCase.cancelJobs()
        postPrescriptionIdUseCase.postPrescriptionIds(
            ::onUploadPrescriptionIdSuccess,
            ::onUploadPrescriptionIdFail,
            uploadKey, id ,prescriptionIds
        )
    }

    private fun onUploadPrescriptionIdSuccess(data : EPharmacyUploadPrescriptionIdsResponse) {
        if(data.data?.success == true){
            _uploadPrescriptionIdsData.postValue(Success(true))
        }else {
            _uploadPrescriptionIdsData.postValue(Fail(Throwable(data.error)))
        }
    }

    private fun onUploadPrescriptionIdFail(error : Throwable) {
        _uploadPrescriptionIdsData.postValue(Fail(error))
    }

    private fun convertToUploadImageResponse(typeRestResponseMap: Map<Type, RestResponse>):  EPharmacyPrescriptionUploadResponse{
        return typeRestResponseMap[EPharmacyPrescriptionUploadResponse::class.java]?.getData() as EPharmacyPrescriptionUploadResponse
    }

    fun removePrescriptionImageAt(index : Int){
        _prescriptionImages.value?.let {
            it.removeAt(index)
            _prescriptionImages.postValue(it)
            checkPrescriptionImages()
        }
    }

    override fun onCleared() {
        getEPharmacyOrderDetailUseCase.cancelJobs()
        super.onCleared()
    }
}
