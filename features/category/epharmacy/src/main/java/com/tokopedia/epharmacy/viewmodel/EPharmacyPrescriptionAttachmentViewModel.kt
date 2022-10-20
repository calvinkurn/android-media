package com.tokopedia.epharmacy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyPrescriptionDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyProductDataModel
import com.tokopedia.epharmacy.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.epharmacy.network.response.EPharmacyDataResponse
import com.tokopedia.epharmacy.utils.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class EPharmacyPrescriptionAttachmentViewModel @Inject constructor(
    private val ePharmacyPrepareProductsGroupUseCase: EPharmacyPrepareProductsGroupUseCase,
    @CoroutineBackgroundDispatcher private val dispatcherBackground: CoroutineDispatcher
)  : BaseViewModel(dispatcherBackground){

    private val _productGroupLiveData = MutableLiveData<Result<EPharmacyDataModel>>()
    val productGroupLiveDataResponse: LiveData<Result<EPharmacyDataModel>> = _productGroupLiveData

    private val _buttonLiveData = MutableLiveData<String?>()
    val buttonLiveData: LiveData<String?> = _buttonLiveData

    fun getPrepareProductGroup() {
        ePharmacyPrepareProductsGroupUseCase.cancelJobs()
        ePharmacyPrepareProductsGroupUseCase.getEPharmacyPrepareProductsGroup(
            ::onAvailablePrepareProductGroup,
            ::onFailPrepareProductGroup
        )
    }

    private fun onAvailablePrepareProductGroup(ePharmacyPrepareProductsGroupResponse: EPharmacyPrepareProductsGroupResponse) {
        ePharmacyPrepareProductsGroupResponse.let { data ->
//            if(data.detailData?.formData?.ePharmacyProducts?.isEmpty() == true)
//                onFailPrepareProductGroup(IllegalStateException("Data invalid"))
//            else {
//                _productDetailLiveData.postValue(Success(mapOrderResponseInDataModel(data)))
//                if(data.detailData?.formData?.isReUploadEnabled == true){
//                    _buttonLiveData.postValue(EPharmacyButtonKey.RE_UPLOAD.key)
//                }else {
//                    _buttonLiveData.postValue(EPharmacyButtonKey.CHECK.key)
//                }
//            }
        }
    }

    private fun mapOrderResponseInDataModel(data: EPharmacyDataResponse) : EPharmacyDataModel{
        val listOfComponents = arrayListOf<BaseEPharmacyDataModel>()
        val prescriptionDataModel = EPharmacyPrescriptionDataModel(PRESCRIPTION_COMPONENT,
            PRESCRIPTION_COMPONENT, data.detailData?.formData?.prescriptionImages, data.detailData?.formData?.isReUploadEnabled ?: false)
        listOfComponents.add(prescriptionDataModel)
        data.detailData?.formData?.ePharmacyProducts?.forEachIndexed { index, eProduct ->
            if(index == FIRST_INDEX){
                eProduct?.shopId = data.detailData.formData.shopId
                eProduct?.shopName = data.detailData.formData.shopName
                eProduct?.shopLocation = data.detailData.formData.shopLocation
                eProduct?.shopType = data.detailData.formData.shopType
                eProduct?.shopLogoUrl = data.detailData.formData.shopLogoUrl
            }
            listOfComponents.add(EPharmacyProductDataModel(PRODUCT_COMPONENT, eProduct?.productId ?: eProduct.hashCode().toString(),
                eProduct))
        }
        return EPharmacyDataModel(listOfComponents)
    }

    private fun onFailPrepareProductGroup(throwable: Throwable) {
        _productGroupLiveData.postValue(Fail(throwable))
    }

    override fun onCleared() {
        ePharmacyPrepareProductsGroupUseCase.cancelJobs()
        super.onCleared()
    }
}
