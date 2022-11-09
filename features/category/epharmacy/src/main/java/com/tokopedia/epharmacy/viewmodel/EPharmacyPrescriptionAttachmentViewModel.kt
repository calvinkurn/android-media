package com.tokopedia.epharmacy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.common_epharmacy.network.response.EPharmacyItemButtonData
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.*
import com.tokopedia.epharmacy.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.epharmacy.utils.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class EPharmacyPrescriptionAttachmentViewModel @Inject constructor(
    private val ePharmacyPrepareProductsGroupUseCase: EPharmacyPrepareProductsGroupUseCase,
    @CoroutineBackgroundDispatcher private val dispatcherBackground: CoroutineDispatcher
)  : BaseViewModel(dispatcherBackground){

    private val _productGroupLiveData = MutableLiveData<Result<EPharmacyDataModel>>()
    val productGroupLiveDataResponse: LiveData<Result<EPharmacyDataModel>> = _productGroupLiveData

    private val _buttonLiveData = MutableLiveData<Pair<String,String>>()
    val buttonLiveData: LiveData<Pair<String,String>> = _buttonLiveData

    fun getPrepareProductGroup() {
        ePharmacyPrepareProductsGroupUseCase.cancelJobs()
        ePharmacyPrepareProductsGroupUseCase.getEPharmacyPrepareProductsGroup(
            ::onAvailablePrepareProductGroup,
            ::onFailPrepareProductGroup
        )
    }

    private fun onAvailablePrepareProductGroup(ePharmacyPrepareProductsGroupResponse: EPharmacyPrepareProductsGroupResponse) {
        ePharmacyPrepareProductsGroupResponse.let { data ->
            if(data.detailData?.groupsData?.epharmacyGroups?.isEmpty() == true)
                onFailPrepareProductGroup(IllegalStateException("Data invalid"))
            else {
                _productGroupLiveData.postValue(Success(mapGroupsDataIntoDataModel(data)))
                mapButtonData(ePharmacyPrepareProductsGroupResponse)
            }
        }
    }

    private fun mapButtonData(ePharmacyPrepareProductsGroupResponse: EPharmacyPrepareProductsGroupResponse) {
        var rejectedCount = 0
        ePharmacyPrepareProductsGroupResponse.detailData?.groupsData?.epharmacyGroups?.forEach { group ->
            if(group?.consultationData?.consultationStatus == EPharmacyConsultationStatus.REJECTED.status
                || group?.consultationData?.consultationStatus == EPharmacyConsultationStatus.EXPIRED.status){
                rejectedCount += 1
            }
        }

        if(rejectedCount > 0){
            _buttonLiveData.postValue(Pair("Lanjut ke Pengiriman","tokopedia://checkout/"))
        }else {
            _buttonLiveData.postValue(Pair("Lanjut ke Beli Langsung","tokopedia://cart/"))
        }
    }

    private fun mapGroupsDataIntoDataModel(data: EPharmacyPrepareProductsGroupResponse) : EPharmacyDataModel{
        val listOfComponents = arrayListOf<BaseEPharmacyDataModel>()
        if(data.detailData?.groupsData?.attachmentPageTickerText?.isNotBlank() == true){
            listOfComponents.add(EPharmacyTickerDataModel(TICKER_COMPONENT,
                TICKER_COMPONENT,
                data.detailData?.groupsData?.attachmentPageTickerText, EPHARMACY_TICKER_ICON,EPHARMACY_TICKER_BACKGROUND ))
        }

        data.detailData?.groupsData?.epharmacyGroups?.forEach { group ->
            if(!group?.shopInfo.isNullOrEmpty()){
                group?.shopInfo?.forEachIndexed { index, info ->
                    if(info?.products?.isEmpty() != true){
                        listOfComponents.add(getGroupComponent(group,info,index))
                    }
                }
            }
        }
        return EPharmacyDataModel(listOfComponents)
    }

    private fun getGroupComponent(group: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup,
                                  info: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo?,
                                  index: Int): BaseEPharmacyDataModel {
        return EPharmacyAttachmentDataModel(
            "${group.epharmacyGroupId},${info?.shopId  ?: ""}",GROUP_COMPONENT,
            group.epharmacyGroupId,
            group.consultationSource?.enablerName,
            group.consultationSource?.enablerLogoUrl,
            info,
            group.consultationData?.consultationStatus,
            group.consultationData?.consultationString,
            group.consultationData?.prescription,
            group.consultationData?.partnerConsultationId,
            group.consultationData?.tokoConsultationId,
            group.prescriptionImages,
            group.prescriptionSource,
            group.consultationSource,
            false,
            prepareCtaData(group.prescriptionSource,group.consultationData?.prescription,group.prescriptionImages),
            index == ((group.shopInfo?.size ?: 0) - 1)
        )
    }

    private fun onFailPrepareProductGroup(throwable: Throwable) {
        _productGroupLiveData.postValue(Fail(throwable))
    }

    override fun onCleared() {
        ePharmacyPrepareProductsGroupUseCase.cancelJobs()
        super.onCleared()
    }

    private fun prepareCtaData(
        prescriptionSource: List<String?>?,
        prescription: List<EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationData.Prescription?>?,
        prescriptionImages: List<EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.PrescriptionImage?>?
    ) : EPharmacyItemButtonData {
        var buttonText = "Chat Dokter Buat Dapat Resep"
        var buttonSubText = ""
        var buttonIconUrl = ""
        prescriptionSource?.let {
            if(it.size > 1){
                buttonText = "Upload Resep atau Chat Dokter"
            }else {
                it.firstOrNull()?.let { source ->
                    buttonText = when(source){
                        PRESCRIPTION_SOURCE_TYPE.MINI_CONSULT.type ->{
                            "Chat Dokter Buat Dapat Resep"
                        }
                        PRESCRIPTION_SOURCE_TYPE.UPLOAD.type ->{
                            "Upload Resep"
                        }
                        else -> {
                            ""
                        }
                    }
                }
            }
        }
        if(prescriptionImages?.size ?: 0 > 1 || prescription?.size ?:0 > 1){
            buttonText = "Resep Digital Terlampir"
            buttonSubText = "Resep dari dokter"
        }
        return EPharmacyItemButtonData(buttonIconUrl,buttonText,buttonSubText,"")
    }
}
