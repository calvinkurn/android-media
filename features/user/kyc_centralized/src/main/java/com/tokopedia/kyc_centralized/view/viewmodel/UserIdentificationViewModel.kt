package com.tokopedia.kyc_centralized.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kyc_centralized.domain.GetUserProjectInfoUseCase
import com.tokopedia.kyc_centralized.util.DispatcherProvider
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.mediauploader.domain.UploaderUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user_identification_common.KYCConstant
import com.tokopedia.user_identification_common.domain.pojo.CheckKtpStatusPojo
import com.tokopedia.user_identification_common.domain.pojo.KycUserProjectInfoPojo
import com.tokopedia.user_identification_common.domain.pojo.RegisterIdentificationPojo
import com.tokopedia.user_identification_common.domain.pojo.UploadIdentificationPojo
import com.tokopedia.user_identification_common.domain.usecase.GetStatusKtpUseCase
import com.tokopedia.user_identification_common.domain.usecase.RegisterKycUseCase
import com.tokopedia.user_identification_common.domain.usecase.UploadUserIdentificationUseCase
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class UserIdentificationViewModel @Inject constructor (
        private val getUserProjectInfoUseCase: GetUserProjectInfoUseCase,
        private val getStatusKtpUseCase: GetStatusKtpUseCase,
        private val registerKycUseCase: RegisterKycUseCase,
        private val uploadUserIdentificationUseCase: UploadUserIdentificationUseCase,
        private val dispatcher: DispatcherProvider
): BaseViewModel(dispatcher.io()) {

    private val _userProjectInfo = MutableLiveData<Result<KycUserProjectInfoPojo>>()
    val userProjectInfo: LiveData<Result<KycUserProjectInfoPojo>>
        get() = _userProjectInfo

    private val _ktpStatus = MutableLiveData<Result<CheckKtpStatusPojo>>()
    val ktpStatus: LiveData<Result<CheckKtpStatusPojo>>
        get() = _ktpStatus

    private val _registerKyc = MutableLiveData<Result<RegisterIdentificationPojo>>()
    val registerKyc: LiveData<Result<RegisterIdentificationPojo>>
        get() = _registerKyc

    private var totalSuccessUpload = 0

    fun getUserProjectInfo(projectId: Int) {
        launchCatchError(block = {
            getUserProjectInfoUseCase.params = GetUserProjectInfoUseCase.createParam(projectId)
            val userProjectInfo = getUserProjectInfoUseCase.executeOnBackground()
            _userProjectInfo.postValue(Success(userProjectInfo))
        }, onError = {
            _userProjectInfo.postValue(Fail(it))
        })
    }

    fun getStatusKtp(img: String) {
        launchCatchError(block = {
            getStatusKtpUseCase.params = GetStatusKtpUseCase.createParam(img)
            val statusKtp = getStatusKtpUseCase.executeOnBackground()
            _ktpStatus.postValue(Success(statusKtp))
        }, onError = {
            _ktpStatus.postValue(Fail(it))
        })
    }

    fun uploadUserIdentification(kycType: Int, picObjKyc: String, projectId: Int) {
        launchCatchError(block = {
            withContext(dispatcher.io()) {
                uploadUserIdentificationUseCase.params = UploadUserIdentificationUseCase.createParam(kycType, picObjKyc, projectId)
                val uploadUserIdentification = uploadUserIdentificationUseCase.executeOnBackground()
                if(uploadUserIdentification.kycUpload.isSuccess == 1) {
                    totalSuccessUpload++
                }
                if(totalSuccessUpload == KYCConstant.IS_ALL_MUTATION_SUCCESS) {
                    registerKyc(projectId)
                }
            }
        }, onError = {
            totalSuccessUpload = 0
            val throwable = Throwable("${KYCConstant.ERROR_UPLOAD_IDENTIFICATION} - $kycType")
            _registerKyc.postValue(Fail(throwable))
        })
    }

    private fun registerKyc(projectId: Int) {
        launchCatchError(block = {
            registerKycUseCase.params = RegisterKycUseCase.createParam(projectId)
            val registerKtp = registerKycUseCase.executeOnBackground()
            _registerKyc.postValue(Success(registerKtp))
        }, onError = {
            val throwable = Throwable(KYCConstant.ERROR_REGISTER)
            _registerKyc.postValue(Fail(throwable))
        })
    }

    override fun onCleared() {
        super.onCleared()
        getUserProjectInfoUseCase.cancelJobs()
        getStatusKtpUseCase.cancelJobs()
        registerKycUseCase.cancelJobs()
    }
}