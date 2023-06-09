package com.tokopedia.epharmacy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.common_epharmacy.network.response.EPharmacyMiniConsultationResult
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
import com.tokopedia.epharmacy.component.model.EPharmacyDataModel
import com.tokopedia.epharmacy.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.epharmacy.network.params.InitiateConsultationParam
import com.tokopedia.epharmacy.network.response.EPharmacyConsultationDetails
import com.tokopedia.epharmacy.network.response.EPharmacyConsultationDetailsResponse
import com.tokopedia.epharmacy.network.response.EPharmacyInitiateConsultationResponse
import com.tokopedia.epharmacy.usecase.EPharmacyGetConsultationDetailsUseCase
import com.tokopedia.epharmacy.usecase.EPharmacyInitiateConsultationUseCase
import com.tokopedia.epharmacy.utils.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class EPharmacyPrescriptionAttachmentViewModel @Inject constructor(
    private val ePharmacyPrepareProductsGroupUseCase: EPharmacyPrepareProductsGroupUseCase,
    private val ePharmacyInitiateConsultationUseCase: EPharmacyInitiateConsultationUseCase,
    private val ePharmacyGetConsultationDetailsUseCase: EPharmacyGetConsultationDetailsUseCase,
    @CoroutineBackgroundDispatcher private val dispatcherBackground: CoroutineDispatcher
) : BaseViewModel(dispatcherBackground) {

    private val _productGroupLiveData = MutableLiveData<Result<EPharmacyDataModel>>()
    val productGroupLiveDataResponse: LiveData<Result<EPharmacyDataModel>> = _productGroupLiveData

    private val _initiateConsultation = MutableLiveData<Result<EPharmacyInitiateConsultationResponse>>()
    val initiateConsultation: LiveData<Result<EPharmacyInitiateConsultationResponse>> = _initiateConsultation

    private val _buttonLiveData = MutableLiveData<EPharmacyPrepareProductsGroupResponse.PapPrimaryCTA?>()
    val buttonLiveData: LiveData<EPharmacyPrepareProductsGroupResponse.PapPrimaryCTA?> = _buttonLiveData

    private val _uploadError = MutableLiveData<EPharmacyUploadError>()
    val uploadError: LiveData<EPharmacyUploadError> = _uploadError

    private val _consultationDetails = MutableLiveData<Result<EPharmacyConsultationDetails>>()
    val consultationDetails: LiveData<Result<EPharmacyConsultationDetails>> = _consultationDetails

    var ePharmacyPrepareProductsGroupResponseData: EPharmacyPrepareProductsGroupResponse ? = null

    fun getPrepareProductGroup() {
        ePharmacyPrepareProductsGroupUseCase.cancelJobs()
        ePharmacyPrepareProductsGroupUseCase.getEPharmacyPrepareProductsGroup(
            ::onAvailablePrepareProductGroup,
            ::onFailPrepareProductGroup
        )
    }

    fun initiateConsultation(params: InitiateConsultationParam) {
        ePharmacyInitiateConsultationUseCase.cancelJobs()
        ePharmacyInitiateConsultationUseCase.initiateConsultation(
            ::onSuccessInitiateConsultation,
            ::onFailInitiateConsultation,
            params
        )
    }

    fun getConsultationDetails(consultationId: Long) {
        ePharmacyGetConsultationDetailsUseCase.cancelJobs()
        ePharmacyGetConsultationDetailsUseCase.getConsultationDetails(
            ::onSuccessGetConsultationDetails,
            ::onFailGetConsultationDetails,
            consultationId
        )
    }

    private fun onAvailablePrepareProductGroup(ePharmacyPrepareProductsGroupResponse: EPharmacyPrepareProductsGroupResponse) {
        ePharmacyPrepareProductsGroupResponseData = ePharmacyPrepareProductsGroupResponse
        ePharmacyPrepareProductsGroupResponse.let { data ->
            if (data.detailData?.groupsData?.epharmacyGroups?.isNotEmpty() == true) {
                _productGroupLiveData.postValue(Success(EPharmacyUtils.mapGroupsDataIntoDataModel(data)))
                _buttonLiveData.postValue(ePharmacyPrepareProductsGroupResponse.detailData?.groupsData?.papPrimaryCTA)
                showToastData(ePharmacyPrepareProductsGroupResponse.detailData?.groupsData?.toaster)
            } else {
                onFailPrepareProductGroup(IllegalStateException("Data Invalid"))
            }
        }
    }

    private fun onSuccessInitiateConsultation(epGroupId: String, response: EPharmacyInitiateConsultationResponse) {
        response.epharmacyGroupId = epGroupId
        response.getInitiateConsultation?.let { _ ->
            _initiateConsultation.postValue(Success(response))
        } ?: kotlin.run {
            onFailInitiateConsultation(IllegalStateException("Data Invalid"))
        }
    }

    private fun onSuccessGetConsultationDetails(response: EPharmacyConsultationDetailsResponse) {
        response.getEpharmacyConsultationDetails?.let { data ->
            _consultationDetails.value = Success(data)
        } ?: kotlin.run {
            onFailGetConsultationDetails(Throwable("Data Invalid"))
        }
    }

    private fun showToastData(toaster: EPharmacyPrepareProductsGroupResponse.EPharmacyToaster?) {
        toaster?.message?.let { message ->
            if (PRESCRIPTION_ATTACH_SUCCESS == toaster.type) {
                _uploadError.value = EPharmacyMiniConsultationToaster(false, message, toaster.ePharmacyGroupId)
            } else {
                _uploadError.value = EPharmacyMiniConsultationToaster(true, message, toaster.ePharmacyGroupId)
            }
        }
    }

    private fun onFailPrepareProductGroup(throwable: Throwable) {
        _productGroupLiveData.postValue(Fail(throwable))
    }

    private fun onFailInitiateConsultation(throwable: Throwable) {
        _initiateConsultation.postValue(Fail(throwable))
    }

    private fun onFailGetConsultationDetails(throwable: Throwable) {
        _consultationDetails.postValue(Fail(throwable))
    }

    fun getResultForCheckout(): ArrayList<EPharmacyMiniConsultationResult> {
        val result = arrayListOf<EPharmacyMiniConsultationResult>()
        ePharmacyPrepareProductsGroupResponseData?.let { response ->
            response.detailData?.groupsData?.epharmacyGroups?.forEach { group ->
                result.add(EPharmacyUtils.getMiniConsultationModel(group))
            }
        }
        return result
    }

    fun getGroupIds(): ArrayList<String> {
        return EPharmacyUtils.getGroupIds(ePharmacyPrepareProductsGroupResponseData)
    }

    fun getShopIds(): List<String> {
        return EPharmacyUtils.getShopIds(ePharmacyPrepareProductsGroupResponseData)
    }

    fun getShopIds(ePharmacyGroupId: String?): List<String> {
        return EPharmacyUtils.getShopIds(ePharmacyGroupId, ePharmacyPrepareProductsGroupResponseData)
    }

    fun getEnablers(): ArrayList<String> {
        return EPharmacyUtils.getEnablers(ePharmacyPrepareProductsGroupResponseData)
    }

    fun findGroup(ePharmacyGroupId: String?): EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup? {
        return EPharmacyUtils.findGroup(ePharmacyGroupId, ePharmacyPrepareProductsGroupResponseData)
    }
}
