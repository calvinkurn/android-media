package com.tokopedia.epharmacy.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
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
import com.tokopedia.epharmacy.usecase.GetEPharmacyOrderDetailUseCase
import com.tokopedia.epharmacy.usecase.PostPrescriptionIdUseCase
import com.tokopedia.epharmacy.usecase.UploadPrescriptionUseCase
import com.tokopedia.epharmacy.utils.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.lang.reflect.Type
import javax.inject.Inject

class UploadPrescriptionViewModel @Inject constructor(
    private val getEPharmacyOrderDetailUseCase: GetEPharmacyOrderDetailUseCase,
    private val uploadPrescriptionUseCase: UploadPrescriptionUseCase,
    private val postPrescriptionIdUseCase: PostPrescriptionIdUseCase,
    @CoroutineBackgroundDispatcher private val dispatcherBackground: CoroutineDispatcher,
    @CoroutineMainDispatcher private val dispatcherMain: CoroutineDispatcher
)  : BaseViewModel(dispatcherBackground){

    private val _productDetailLiveData = MutableLiveData<Result<EPharmacyDataModel>>()
    val productDetailLiveDataResponse: LiveData<Result<EPharmacyDataModel>> = _productDetailLiveData

    private val _uploadPrescriptionIdsData = MutableLiveData<Result<Boolean>>()
    val uploadPrescriptionIdsData: LiveData<Result<Boolean>> = _uploadPrescriptionIdsData

    private val _buttonLiveData = MutableLiveData<List<EpharmacyButton?>>()
    val buttonLiveData: LiveData<List<EpharmacyButton?>> = _buttonLiveData

    private val _prescriptionImages = MutableLiveData<ArrayList<PrescriptionImage?>>()
    val prescriptionImages: LiveData<ArrayList<PrescriptionImage?>> = _prescriptionImages

    fun getEPharmacyDetail(orderId: String) {
        getEPharmacyOrderDetailUseCase.cancelJobs()
        getEPharmacyOrderDetailUseCase.getEPharmacyDetail(
            ::onAvailableEPharmacyDetail,
            ::onFailEPharmacyDetail,
            orderId
        )
    }

    private fun onAvailableEPharmacyDetail(ePharmacyDetailResponse: EPharmacyDataResponse) {
        ePharmacyDetailResponse.let { data ->
            if(data.ePharmacyProducts?.isEmpty() == true)
                onFailEPharmacyDetail(IllegalStateException("Data invalid"))
            else {
                _productDetailLiveData.postValue(Success(mapResponseInDataModel(data)))
                if(!data.epharmacyButton.isNullOrEmpty()){
                    _buttonLiveData.postValue(data.epharmacyButton)
                }
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
            if(index == 0 && eProduct?.shopId.isNullOrBlank()){
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
        arrayList?.add(PrescriptionImage("","","","",
            EPharmacyPrescriptionStatus.SELECTED.status,
            isUploading = true,
            isUploadSuccess = false,
            isDeletable = true,
            isUploadFailed = false, GALLERY_IMAGE_VIEW_TYPE, localPath
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

    private fun getBase64OfPrescriptionImage(localFilePath: String): String {
        return try {
            val prescriptionImageBitmap: Bitmap = BitmapFactory.decodeFile(localFilePath)
            val prescriptionByteArrayOutputStream = ByteArrayOutputStream()
            prescriptionImageBitmap.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                prescriptionByteArrayOutputStream
            )
            val byteArrayImage = prescriptionByteArrayOutputStream.toByteArray()
            prescriptionImageBitmap.recycle()
            "data:image/jpeg;base64,${Base64.encodeToString(byteArrayImage, Base64.DEFAULT)}"
        }catch (e : Exception){
            ""
        }
    }

    private suspend fun uploadImageToServer(uniquePositionId: Int, localFilePath : String) {
        val base64Image = getBase64OfPrescriptionImage(localFilePath)
        if (base64Image.isNotBlank()){
            uploadPrescriptionUseCase.setBase64Image(uniquePositionId.toString(),base64Image)
            val result = withContext(dispatcherBackground) {
                convertToYoutubeResponse(uploadPrescriptionUseCase.executeOnBackground())
            }
            _prescriptionImages.value?.get(uniquePositionId)?.apply {
                result.data?.firstOrNull()?.let {
                    isUploadSuccess = true
                    isUploading = false
                    prescriptionId = it.prescriptionId
                }?: kotlin.run {
                    isUploadSuccess = false
                    isUploading = false
                    isUploadFailed = true
                }
            }
            _prescriptionImages.postValue(_prescriptionImages.value)
        }else {

        }
        withContext(dispatcherMain){
            checkPrescriptionImages()
        }
    }

    private fun checkPrescriptionImages() {
        var successImagesCount = 0
        var failedImageCount = 0
        _prescriptionImages.value?.forEach { presImage ->
            if(presImage?.status == EPharmacyPrescriptionStatus.APPROVED.status
                || presImage?.isUploadSuccess == true) {
                successImagesCount += 1
            }else if(presImage?.isUploadFailed == true){
                failedImageCount += 1
            }
        }
        _buttonLiveData.value.let {
            it?.firstOrNull()?.apply {
                if(failedImageCount > 0){
                    text = DONE_TEXT
                    type = EPharmacyButtonType.TERTIARY.type
                }else {
                    text = DONE_TEXT
                    type = EPharmacyButtonType.SECONDARY.type
                }
            }
            _buttonLiveData.postValue(it)
        }
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
        val prescriptionIds = arrayListOf<String>()
        _prescriptionImages.value?.forEach { prescriptionImage ->
            if(!prescriptionImage?.prescriptionId.isNullOrBlank()){
                prescriptionIds.add(prescriptionImage?.prescriptionId ?: "")
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

    private fun convertToYoutubeResponse(typeRestResponseMap: Map<Type, RestResponse>):  EPharmacyPrescriptionUploadResponse{
        return typeRestResponseMap[EPharmacyPrescriptionUploadResponse::class.java]?.getData() as EPharmacyPrescriptionUploadResponse
    }

    fun removePrescriptionImageAt(index : Int){
        _prescriptionImages.value?.let {
            it.removeAt(index)
            _prescriptionImages.postValue(it)
        }
    }

    override fun onCleared() {
        getEPharmacyOrderDetailUseCase.cancelJobs()
        super.onCleared()
    }
}
