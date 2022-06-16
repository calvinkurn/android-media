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
import com.tokopedia.epharmacy.network.response.EPharmacyDataResponse
import com.tokopedia.epharmacy.network.response.EPharmacyPrescriptionUploadResponse
import com.tokopedia.epharmacy.network.response.EpharmacyButton
import com.tokopedia.epharmacy.network.response.PrescriptionImage
import com.tokopedia.epharmacy.usecase.GetEPharmacyOrderDetailUseCase
import com.tokopedia.epharmacy.usecase.UploadPrescriptionUseCase
import com.tokopedia.epharmacy.utils.EPharmacyPrescriptionStatus
import com.tokopedia.epharmacy.utils.GALLERY_IMAGE_VIEW_TYPE
import com.tokopedia.epharmacy.utils.PRESCRIPTION_COMPONENT
import com.tokopedia.epharmacy.utils.PRODUCT_COMPONENT
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
    @CoroutineBackgroundDispatcher private val dispatcher: CoroutineDispatcher
)  : BaseViewModel(dispatcher){

    private val _productDetailLiveData = MutableLiveData<Result<EPharmacyDataModel>>()
    val productDetailLiveDataResponse: LiveData<Result<EPharmacyDataModel>> = _productDetailLiveData

    private val _buttonLiveData = MutableLiveData<Result<List<EpharmacyButton?>>>()
    val buttonLiveData: LiveData<Result<List<EpharmacyButton?>>> = _buttonLiveData

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
                    _buttonLiveData.postValue(Success(data.epharmacyButton))
                }
            }
        }
    }

    private fun mapResponseInDataModel(data: EPharmacyDataResponse) : EPharmacyDataModel{
        val listOfComponents = arrayListOf<BaseEPharmacyDataModel>()
        val prescriptionDataModel = EPharmacyPrescriptionDataModel(PRESCRIPTION_COMPONENT,
            PRESCRIPTION_COMPONENT, data.prescriptionImages)
        listOfComponents.add(prescriptionDataModel)
        data.ePharmacyProducts?.forEach { eProduct ->
            eProduct?.shopId = data.shopId
            eProduct?.shopName = data.shopName
            eProduct?.shopLocation = data.shopLocation
            eProduct?.shopType = data.shopType

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
            originalPaths.forEach { path ->
                it?.add(PrescriptionImage("","","","",
                    EPharmacyPrescriptionStatus.SELECTED.status,
                    true,
                    false,
                    true,
                    false, GALLERY_IMAGE_VIEW_TYPE,path
                ))
                // Fire corresponding coroutines for upload
                uploadImageWithPath((_prescriptionImages.value?.size ?: 0 + 1),path)
            }

            _prescriptionImages.postValue(it)
        }
    }

    fun uploadImageWithPath(uniquePositionId : Int ,path: String) {
        // coroutine use case
        // await callback
        // save in pres images and post value

        launchCatchError(block = {
            val base64Image = getBase64OfPrescriptionImage(path)
            uploadImageToServer(uniquePositionId,base64Image)
        }, onError = {

        })

    }

    private suspend fun getBase64OfPrescriptionImage(localPath: String): String {
        val prescriptionImageBitmap: Bitmap = BitmapFactory.decodeFile(localPath)
        val prescriptionByteArrayOutputStream = ByteArrayOutputStream()
        prescriptionImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, prescriptionByteArrayOutputStream)
        val byteArrayImage = prescriptionByteArrayOutputStream.toByteArray()
        prescriptionImageBitmap.recycle()
        return Base64.encodeToString(byteArrayImage, Base64.DEFAULT)
    }

    private suspend fun uploadImageToServer(uniquePositionId : Int, base64Image: String) {
        launchCatchError(block = {
            uploadPrescriptionUseCase.setBase64Image(uniquePositionId.toString(),base64Image)
            val result = withContext(dispatcher) {
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
        }, onError = {
            _prescriptionImages.value?.get(uniquePositionId)?.apply {
                isUploadSuccess = false
                isUploading = false
                isUploadFailed = true
            }
            _prescriptionImages.postValue(_prescriptionImages.value)
        })
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
