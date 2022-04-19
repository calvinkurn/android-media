package com.tokopedia.updateinactivephone.features.submitnewphone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.ERROR_FAILED_UPLOAD_IMAGE
import com.tokopedia.updateinactivephone.domain.data.*
import com.tokopedia.updateinactivephone.domain.usecase.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class InactivePhoneDataUploadViewModel @Inject constructor(
    private val phoneValidationUseCase: PhoneValidationUseCase,
    private val imageUploadUseCase: ImageUploadUseCase,
    private val submitDataUseCase: SubmitDataUseCase,
    private val submitExpeditedInactivePhoneUseCase: SubmitExpeditedInactivePhoneUseCase,
    private val verifyNewPhoneUseCase: VerifyNewPhoneUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _phoneValidation = MutableLiveData<Result<PhoneValidationDataModel>>()
    val phoneValidation: LiveData<Result<PhoneValidationDataModel>>
        get() = _phoneValidation

    private val _imageUpload = MutableLiveData<Result<ImageUploadDataModel>>()
    val imageUpload: LiveData<Result<ImageUploadDataModel>>
        get() = _imageUpload

    private val _submitData = MutableLiveData<Result<InactivePhoneSubmitDataModel>>()
    val submitData: LiveData<Result<InactivePhoneSubmitDataModel>>
        get() = _submitData

    private val _submitDataExpedited = MutableLiveData<Result<SubmitExpeditedDataModel>>()
    val submitDataExpedited: LiveData<Result<SubmitExpeditedDataModel>>
        get() = _submitDataExpedited

    private val _verifyPhoneNumber = MutableLiveData<Result<VerifyNewPhoneDataModel>>()
    val verifyNewPhone: LiveData<Result<VerifyNewPhoneDataModel>>
        get() = _verifyPhoneNumber

    fun userValidation(inactivePhoneUserDataModel: InactivePhoneUserDataModel) {
        launchCatchError(coroutineContext, {
            val response = phoneValidationUseCase(inactivePhoneUserDataModel)
            _phoneValidation.value = Success(response)
        }, {
            _phoneValidation.value = Fail(it)
        })
    }

    fun uploadImage(email: String, oldMsisdn: String, userIndex: Int, filePath: String, source: String) {
        launchCatchError(coroutineContext, {
            imageUploadUseCase.setParam(email, oldMsisdn, userIndex, filePath)
            imageUploadUseCase.execute(onSuccess = {
                if (it.status == ImageUploadUseCase.STATUS_OK && it.data.pictureObject.isNotEmpty()) {
                    it.source = source
                    _imageUpload.value = Success(it)
                } else {
                    if (it.errors.isNotEmpty()) {
                        _imageUpload.value = Fail(Throwable(it.errors[0]))
                    } else {
                        _imageUpload.value = Fail(Throwable(ERROR_FAILED_UPLOAD_IMAGE))
                    }
                }
            }, onError = {
                _imageUpload.value = Fail(it)
            })
        }, {
            _imageUpload.value = Fail(it)
        })
    }

    fun submitForm(submitDataModel: SubmitDataModel) {
        launchCatchError(coroutineContext, {
            val response = submitDataUseCase(submitDataModel)
            _submitData.value = Success(response)
        }, {
            _submitData.value = Fail(it)
        })
    }

    fun submitNewPhoneNumber(inactivePhoneUserDataModel: InactivePhoneUserDataModel) {
        launchCatchError(coroutineContext, {
            val response = submitExpeditedInactivePhoneUseCase(inactivePhoneUserDataModel)
            _submitDataExpedited.value = Success(response)
        }, {
            _submitDataExpedited.value = Fail(Throwable(it))
        })
    }

    fun verifyNewPhone(inactivePhoneUserDataModel: InactivePhoneUserDataModel) {
        launchCatchError(coroutineContext, {
            val response = verifyNewPhoneUseCase(inactivePhoneUserDataModel)
            _verifyPhoneNumber.value = Success(response)
        }, {
            _verifyPhoneNumber.value = Fail(Throwable(it))
        })
    }
}