package com.tokopedia.epharmacy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyPrescriptionDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyProductDataModel
import com.tokopedia.epharmacy.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.epharmacy.network.response.EPharmacyDataResponse
import com.tokopedia.epharmacy.network.response.EpharmacyButton
import com.tokopedia.epharmacy.usecase.GetEPharmacyOrderDetailUseCase
import com.tokopedia.epharmacy.utils.PRESCRIPTION_COMPONENT
import com.tokopedia.epharmacy.utils.PRODUCT_COMPONENT
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class UploadPrescriptionViewModel @Inject constructor(
    private val getEPharmacyOrderDetailUseCase: GetEPharmacyOrderDetailUseCase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher
)  : BaseViewModel(dispatcher){

    private val _productDetailLiveData = MutableLiveData<Result<EPharmacyDataModel>>()
    val productDetailLiveDataResponse: LiveData<Result<EPharmacyDataModel>> = _productDetailLiveData
    private val _buttonLiveData = MutableLiveData<Result<List<EpharmacyButton?>>>()
    val buttonLiveData: LiveData<Result<List<EpharmacyButton?>>> = _buttonLiveData

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
            listOfComponents.add(EPharmacyProductDataModel(PRODUCT_COMPONENT, eProduct?.productId.toString(),
                eProduct))
        }
        return EPharmacyDataModel(listOfComponents)
    }

    private fun onFailEPharmacyDetail(throwable: Throwable) {
        _productDetailLiveData.postValue(Fail(throwable))
    }

    override fun onCleared() {
        getEPharmacyOrderDetailUseCase.cancelJobs()
        super.onCleared()
    }
}
