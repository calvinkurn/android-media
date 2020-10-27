package com.tokopedia.updateinactivephone.revamp.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.ERROR_FAILED_UPLOAD_IMAGE
import com.tokopedia.updateinactivephone.revamp.domain.data.ImageUploadDataModel
import com.tokopedia.updateinactivephone.revamp.domain.data.InactivePhoneSubmitDataModel
import com.tokopedia.updateinactivephone.revamp.domain.data.PhoneValidationDataModel
import com.tokopedia.updateinactivephone.revamp.domain.data.UploadHostDataModel
import com.tokopedia.updateinactivephone.revamp.domain.usecase.GetUploadHostUseCase
import com.tokopedia.updateinactivephone.revamp.domain.usecase.ImageUploadUseCase
import com.tokopedia.updateinactivephone.revamp.domain.usecase.PhoneValidationUseCase
import com.tokopedia.updateinactivephone.revamp.domain.usecase.SubmitDataUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class InactivePhoneDataUploadViewModel @Inject constructor(
        private val phoneValidationUseCase: PhoneValidationUseCase,
        private val imageUploadUseCase: ImageUploadUseCase,
        private val getUploadHostUseCase: GetUploadHostUseCase,
        private val submitDataUseCase: SubmitDataUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _phoneValidation = MutableLiveData<Result<PhoneValidationDataModel>>()
    val phoneValidation: LiveData<Result<PhoneValidationDataModel>>
        get() = _phoneValidation

    private val _uploadHost = MutableLiveData<Result<UploadHostDataModel>>()
    val uploadHost: LiveData<Result<UploadHostDataModel>>
        get() = _uploadHost

    private val _imageUpload = MutableLiveData<Result<ImageUploadDataModel>>()
    val imageUpload: LiveData<Result<ImageUploadDataModel>>
        get() = _imageUpload

    private val _submitData = MutableLiveData<Result<InactivePhoneSubmitDataModel>>()
    val submitData: LiveData<Result<InactivePhoneSubmitDataModel>>
        get() = _submitData

    fun userValidation(phone: String, index: Int) {
        launchCatchError(coroutineContext, {
            phoneValidationUseCase.setParam(phone, index)
            phoneValidationUseCase.execute(onSuccess = {
                _phoneValidation.postValue(Success(it))
            }, onError = {
                _phoneValidation.postValue(Fail(it))
            })
        }, {
            _phoneValidation.postValue(Fail(it))
        })
    }

    fun getUploadHost() {
        launchCatchError(coroutineContext, {
            getUploadHostUseCase.execute(onSuccess = {
                if (it.data.generatedHost.uploadHost.isNotEmpty()) {
                    _uploadHost.postValue(Success(it))
                } else {
                    _uploadHost.postValue(Fail(Throwable("")))
                }
            }, onError = {
                _uploadHost.postValue(Fail(it))
            })
        }, {
            _uploadHost.postValue(Fail(it))
        })
    }

    fun uploadImage(url: String, userId: String, filePath: String, source: String) {
        launchCatchError(coroutineContext, {
            imageUploadUseCase.setParam(url, userId, filePath)
            imageUploadUseCase.execute(onSuccess = {
                if (it.picObj.isNotEmpty()) {
                    it.source = source
                    _imageUpload.postValue(Success(it))
                } else {
                    _imageUpload.postValue(Fail(Throwable(ERROR_FAILED_UPLOAD_IMAGE)))
                }
            }, onError = {
                _imageUpload.postValue(Fail(it))
            })
        }, {
            _imageUpload.postValue(Fail(it))
        })
    }

    fun submitForm(email: String, newPhone: String, userIndex: Int, idCardObj: String, selfieObj: String) {
        launchCatchError(coroutineContext, {
            submitDataUseCase.setParam(email, newPhone, userIndex, idCardObj, selfieObj)
            submitDataUseCase.execute(onSuccess = {
                _submitData.postValue(Success(it))
            }, onError = {
                _submitData.postValue(Fail(it))
            })
        }, {
            _submitData.postValue(Fail(it))
        })
    }

    override fun onCleared() {
        super.onCleared()
        submitDataUseCase.cancelJob()
        phoneValidationUseCase.cancelJob()
    }

    companion object {

    }
}