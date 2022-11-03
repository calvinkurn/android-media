package com.tokopedia.epharmacy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
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
            if(data.detailData?.groupsData?.epharmacyGroups?.isEmpty() == true)
                onFailPrepareProductGroup(IllegalStateException("Data invalid"))
            else {
                _productGroupLiveData.postValue(Success(mapGroupsDataIntoDataModel(data)))
            }
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
                    if(info?.products?.isNullOrEmpty() != true){
                        val ePharmacyGroupData = EPharmacyAttachmentDataModel(
                            "${group.epharmacyGroupId},${info?.shopId  ?: ""}",GROUP_COMPONENT,
                            group.epharmacyGroupId,
                            group.consultationSource?.enablerName,
                            group.consultationSource?.partnerLogoUrl,
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
                            index == ((group.shopInfo?.size ?: 0) - 1)
                            )
                        listOfComponents.add(ePharmacyGroupData)
                    }
                }
            }
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
