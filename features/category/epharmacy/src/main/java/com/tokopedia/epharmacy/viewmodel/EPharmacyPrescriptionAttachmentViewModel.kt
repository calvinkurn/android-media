package com.tokopedia.epharmacy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyPrescriptionDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyProductDataModel
import com.tokopedia.epharmacy.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.epharmacy.utils.FIRST_INDEX
import com.tokopedia.epharmacy.utils.GROUP_COMPONENT
import com.tokopedia.epharmacy.utils.PRESCRIPTION_COMPONENT
import com.tokopedia.epharmacy.utils.PRODUCT_COMPONENT
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
        data.detailData?.groupsData?.epharmacyGroups?.forEach { group ->
            if(!group?.shopInfo.isNullOrEmpty()){
                group?.shopInfo?.forEachIndexed { index, info ->
                    if(info?.products?.isNullOrEmpty() != true){
                        val ePharmacyGroupData = EPharmacyAttachmentDataModel(
                            GROUP_COMPONENT,GROUP_COMPONENT,
                            info?.orderName,
                            group.consultationSource?.enablerName,
                            group.consultationSource?.partnerLogoUrl,
                            info,
                            group.cta?.text,
                            group.cta?.appLink,
                            group.cta?.icon,
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
