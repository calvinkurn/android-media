package com.tokopedia.updateinactivephone.features.submitnewphone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.ERROR_FAILED_UPLOAD_IMAGE
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.ERROR_UNKNOWN
import com.tokopedia.updateinactivephone.domain.data.ImageUploadDataModel
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneSubmitDataModel
import com.tokopedia.updateinactivephone.domain.data.PhoneValidationDataModel
import com.tokopedia.updateinactivephone.domain.data.SubmitExpeditedInactivePhoneDataModel
import com.tokopedia.updateinactivephone.domain.usecase.ImageUploadUseCase
import com.tokopedia.updateinactivephone.domain.usecase.PhoneValidationUseCase
import com.tokopedia.updateinactivephone.domain.usecase.SubmitDataUseCase
import com.tokopedia.updateinactivephone.domain.usecase.SubmitExpeditedInactivePhoneUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InactivePhoneDataUploadViewModel @Inject constructor(
        private val phoneValidationUseCase: PhoneValidationUseCase,
        private val imageUploadUseCase: ImageUploadUseCase,
        private val submitDataUseCase: SubmitDataUseCase,
        private val submitExpeditedInactivePhoneUseCase: SubmitExpeditedInactivePhoneUseCase,
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    private val _phoneValidation = MutableLiveData<Result<PhoneValidationDataModel>>()
    val phoneValidation: LiveData<Result<PhoneValidationDataModel>>
        get() = _phoneValidation

    private val _imageUpload = MutableLiveData<Result<ImageUploadDataModel>>()
    val imageUpload: LiveData<Result<ImageUploadDataModel>>
        get() = _imageUpload

    private val _submitData = MutableLiveData<Result<InactivePhoneSubmitDataModel>>()
    val submitData: LiveData<Result<InactivePhoneSubmitDataModel>>
        get() = _submitData

    private val _submitDataExpedited = MutableLiveData<Result<SubmitExpeditedInactivePhoneDataModel>>()
    val submitDataExpedited: LiveData<Result<SubmitExpeditedInactivePhoneDataModel>>
        get() = _submitDataExpedited

    fun userValidation(phone: String, email: String, index: Int) {
        launchCatchError(coroutineContext, {
            phoneValidationUseCase.setParam(phone, email, index)
            phoneValidationUseCase.execute(onSuccess = {
                if (it.validation.isSuccess) {
                    _phoneValidation.postValue(Success(it))
                } else {
                    _phoneValidation.postValue(Fail(Throwable(it.validation.error)))
                }
            }, onError = {
                _phoneValidation.postValue(Fail(it))
            })
        }, {
            _phoneValidation.postValue(Fail(it))
        })
    }

    fun uploadImage(email: String, oldMsisdn: String, userIndex: Int, filePath: String, source: String) {
        launchCatchError(coroutineContext, {
            imageUploadUseCase.setParam(email, oldMsisdn, userIndex, filePath)
            imageUploadUseCase.execute(onSuccess = {
                if (it.status == ImageUploadUseCase.STATUS_OK && it.data.pictureObject.isNotEmpty()) {
                    it.source = source
                    _imageUpload.postValue(Success(it))
                } else {
                    if (it.errors.isNotEmpty()) {
                        _imageUpload.postValue(Fail(Throwable(it.errors[0])))
                    } else {
                        _imageUpload.postValue(Fail(Throwable(ERROR_FAILED_UPLOAD_IMAGE)))
                    }
                }
            }, onError = {
                _imageUpload.postValue(Fail(it))
            })
        }, {
            _imageUpload.postValue(Fail(it))
        })
    }

    fun submitForm(email: String, oldPhone: String,  newPhone: String, userIndex: Int, idCardObj: String, selfieObj: String) {
        launchCatchError(coroutineContext, {
            submitDataUseCase.setParam(email, oldPhone, newPhone, userIndex, idCardObj, selfieObj)
            submitDataUseCase.execute(onSuccess = {
                if (it.status.isSuccess) {
                    _submitData.postValue(Success(it))
                } else {
                    _submitData.postValue(Fail(Throwable(it.status.errorMessage)))
                }
            }, onError = {
                _submitData.postValue(Fail(it))
            })
        }, {
            _submitData.postValue(Fail(it))
        })
    }

    fun submitNewPhoneNumber(msisdn: String, email: String) {
        launchCatchError(coroutineContext, {
            val response = submitExpeditedInactivePhoneUseCase(mapOf(
                SubmitExpeditedInactivePhoneUseCase.PARAM_MSISDN to msisdn,
                SubmitExpeditedInactivePhoneUseCase.PARAM_EMAIL to email
            ))

            withContext(dispatcher.main) {
                if (response.isSuccess) {
                    _submitDataExpedited.postValue(Success(response))
                } else {
                    if (response.errorMessage.isNotEmpty()) {
                        _submitDataExpedited.postValue(Fail(Throwable(response.errorMessage)))
                    } else {
                        _submitDataExpedited.postValue(Fail(Throwable(ERROR_UNKNOWN)))
                    }
                }
            }
        }, {
            _submitDataExpedited.postValue(Fail(Throwable(it)))
        })
    }

    public override fun onCleared() {
        super.onCleared()
        submitDataUseCase.cancelJob()
        phoneValidationUseCase.cancelJob()
    }
}