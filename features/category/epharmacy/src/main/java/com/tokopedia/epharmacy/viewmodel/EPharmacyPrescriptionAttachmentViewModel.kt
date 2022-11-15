package com.tokopedia.epharmacy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyTickerDataModel
import com.tokopedia.epharmacy.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.epharmacy.network.response.PrescriptionStatusCount
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

    private val _uploadError = MutableLiveData<EPharmacyUploadError>()
    val uploadError: LiveData<EPharmacyUploadError> = _uploadError

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
        val groupStatuses = arrayListOf<Int>()
        ePharmacyPrepareProductsGroupResponse.detailData?.groupsData?.epharmacyGroups?.forEach { group ->
            groupStatuses.add(group?.consultationData?.consultationStatus?:0)
        }
        val statusCount = EPharmacyMapper.getPrescriptionCount(groupStatuses)
        renderButtonOnResult(statusCount,true)
    }

    private fun renderButtonOnResult(statusCount : PrescriptionStatusCount, isApi: Boolean = false) {
        if(statusCount.approved > 0 || statusCount.active > 0){
            _buttonLiveData.postValue(Pair("Lanjut ke Pengiriman","tokopedia://checkout/"))
        }else if(statusCount.rejected > 0){
            _buttonLiveData.postValue(Pair("Lanjut ke Pengiriman",""))
            if(!isApi)
                _uploadError.postValue(EPharmacyNoDigitalPrescriptionError(true))
        }
    }

    fun validateButtonData(ePharmacyAttachmentUiUpdater : EPharmacyAttachmentUiUpdater){
        val groupStatuses = arrayListOf<Int>()
        ePharmacyAttachmentUiUpdater.mapOfData.forEach {
            if(it.value is EPharmacyAttachmentDataModel){
                (it.value as EPharmacyAttachmentDataModel).let { model ->
                    groupStatuses.add(model.consultationStatus ?: 0)
                }
            }
        }
        val statusCount = EPharmacyMapper.getPrescriptionCount(groupStatuses)
        renderButtonOnResult(statusCount)
    }

    private fun mapGroupsDataIntoDataModel(data: EPharmacyPrepareProductsGroupResponse) : EPharmacyDataModel{
        val listOfComponents = arrayListOf<BaseEPharmacyDataModel>()
        if(data.detailData?.groupsData?.attachmentPageTickerText?.isNotBlank() == true){
            listOfComponents.add(EPharmacyTickerDataModel(TICKER_COMPONENT,
                TICKER_COMPONENT,
                data.detailData?.groupsData?.attachmentPageTickerText, EPHARMACY_TICKER_ICON,EPHARMACY_TICKER_BACKGROUND ))
        }

        data.detailData?.groupsData?.epharmacyGroups?.forEachIndexed { indexGroup , group ->
            if(!group?.shopInfo.isNullOrEmpty()){
                group?.shopInfo?.forEachIndexed { index, info ->
                    if(info?.products?.isEmpty() != true){
                        listOfComponents.add(getGroupComponent(group,info,index,EPharmacyMapper.isLastIndex(group.shopInfo,index),
                        EPharmacyMapper.isLastIndex(data.detailData?.groupsData?.epharmacyGroups,indexGroup)))
                    }
                }
            }
        }
        return EPharmacyDataModel(listOfComponents)
    }

    private fun getGroupComponent(group: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup,
                                  info: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo?,
                                  index: Int, isLastShopOfGroup : Boolean, isLastGroup : Boolean): BaseEPharmacyDataModel {
        return EPharmacyMapper.mapGroupsToAttachmentComponents(group,info,index,isLastShopOfGroup,isLastGroup)
    }

    private fun onFailPrepareProductGroup(throwable: Throwable) {
        _productGroupLiveData.postValue(Fail(throwable))
    }

    override fun onCleared() {
        ePharmacyPrepareProductsGroupUseCase.cancelJobs()
        super.onCleared()
    }
}
