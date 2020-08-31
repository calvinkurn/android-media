package com.tokopedia.updateinactivephone.viewmodel

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants
import com.tokopedia.updateinactivephone.data.model.request.ChangePhoneNumberRequestModel
import com.tokopedia.updateinactivephone.data.model.response.GqlUpdatePhoneStatusResponse
import com.tokopedia.updateinactivephone.data.model.response.GqlValidateUserDataResponse
import com.tokopedia.updateinactivephone.data.model.response.ValidateUserDataResponse
import com.tokopedia.updateinactivephone.di.UpdateInActiveQualifier
import com.tokopedia.updateinactivephone.usecase.*
import com.tokopedia.updateinactivephone.viewmodel.ChangeInactivePhoneViewModel.Companion.PHONE_MATCHER
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import org.json.JSONException
import org.json.JSONObject
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

class ChangeInactiveFormRequestViewModel @Inject constructor(
        @UpdateInActiveQualifier private val context: Context,
        private val userSessionInterface: UserSessionInterface,
        private val getValidationUserDataUsecase: GetValidationUserDataUseCase,
        private val getUploadHostUseCase: GetUploadHostUseCase,
        private val uploadImageUseCase: UploadImageUseCase,
        private val submitImageUseCase: SubmitImageUseCase,
        dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    private var photoIdImagePath: String? = null
    private var accountImagePath: String? = null

    private val mutableValidateUserDataResponse = MutableLiveData<Result<ValidateUserDataResponse>>()
    val validateUserDataResponse: LiveData<Result<ValidateUserDataResponse>>
        get() = mutableValidateUserDataResponse

    private val mutableSubmitImage = MutableLiveData<Result<GqlUpdatePhoneStatusResponse>>()
    val submitImageLiveData: LiveData<Result<GqlUpdatePhoneStatusResponse>>
        get() = mutableSubmitImage

    private val changePhoneNumberRequestModel = ChangePhoneNumberRequestModel()
    private var requestParams: RequestParams? = null

    fun setPhotoIdImagePath(imagePath: String) {
        this.photoIdImagePath = imagePath
    }

    fun setAccountPhotoImagePath(imagePath: String) {
        this.accountImagePath = imagePath
    }

    fun requestChangePhoneNumber(email: String, phone: String, userId: String) {
        if (!TextUtils.isEmpty(photoIdImagePath) && !TextUtils.isEmpty(accountImagePath)) {
            launchCatchError(block = {
                getUploadHost(email, phone, userId)
            }) {
                it.printStackTrace()
                mutableSubmitImage.value = Fail(RuntimeException(changePhoneNumberRequestModel.uploadHostModel?.errorMessage?: context.getString(R.string.msg_network_error)))
            }
        } else {
            mutableSubmitImage.value = Fail(RuntimeException(context.getString(R.string.default_error_upload_image)))
        }
    }

    fun validateUserData(email: String, phone: String, userId: String) {
        getValidationUserDataUsecase.getValidationUserData(onSuccessValidateUserData(), onErrorValidateUserData(), phone, email, userId)
    }

    fun isValidEmail(email: String): Int {
        var isValid = 0
        if (TextUtils.isEmpty(email)) {
            isValid = R.string.email_field_empty
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = R.string.invalid_email_error
        }
        return isValid
    }

    fun isValidPhoneNumber(phoneNumber: String): Int {
        var isValid = 0
        val check: Boolean
        val pattern: Pattern = Pattern.compile(PHONE_MATCHER)
        val m: Matcher = pattern.matcher(phoneNumber)
        check = m.matches()

        if (TextUtils.isEmpty(phoneNumber)) {
            isValid = R.string.phone_field_empty
        } else if (check && phoneNumber.length < 8) {
            isValid = R.string.phone_number_invalid_min_8
        } else if (check && phoneNumber.length > 15) {
            isValid = R.string.phone_number_invalid_max_15
        } else if (!check) {
            isValid = R.string.invalid_phone_number
        }
        return isValid
    }

    private fun getUploadChangePhoneNumberRequestParam(email: String, phone: String, userId: String): RequestParams {
        val params = RequestParams.create()
        params.putString(UpdateInactivePhoneConstants.Constants.USERID, userId)
        params.putString(UpdateInactivePhoneConstants.Constants.ID, userId)
        params.putInt(UpdateInactivePhoneConstants.Constants.SERVER_ID, 49)
        params.putInt(UpdateInactivePhoneConstants.Constants.RESOLUTION, 215)
        params.putString(UpdateInactivePhoneConstants.QueryConstants.PHONE, phone)
        params.putString(UpdateInactivePhoneConstants.QueryConstants.EMAIL, email)
        params.putInt(UpdateInactivePhoneConstants.QueryConstants.USER_ID, if (userId.isEmpty()) 0 else Integer.valueOf(userId))
        params.putString(UpdateInactivePhoneConstants.Constants.TOKEN, "tokopedia-lite-inactive-phone")

        setParamUploadIdImage(params)
        setParamUploadBankBookImage(params)

        return params
    }

    private fun setParamUploadBankBookImage(params: RequestParams) {
        params.putString(UpdateInactivePhoneConstants.Constants.PARAM_BANK_BOOK_IMAGE_PATH, accountImagePath)
    }

    private fun setParamUploadIdImage(params: RequestParams) {
        params.putString(UpdateInactivePhoneConstants.Constants.PARAM_KTP_IMAGE_PATH, photoIdImagePath)
    }

    private fun getUploadHostParam(): RequestParams {
        val params = RequestParams.create()
        params.putString(GetUploadHostUseCase.PARAM_NEW_ADD, requestParams?.getString(GetUploadHostUseCase.PARAM_NEW_ADD,
                GetUploadHostUseCase.DEFAULT_NEW_ADD))
        return params
    }

    private fun getUploadBookBankImageParam(requestParams: RequestParams): RequestParams {
        val params = RequestParams.create()

        params.putString(UpdateInactivePhoneConstants.Constants.USERID,
                requestParams.getString(UpdateInactivePhoneConstants.Constants.USERID,
                        userSessionInterface.temporaryUserId))
        params.putString(UpdateInactivePhoneConstants.Constants.PARAM_FILE_TO_UPLOAD,
                requestParams.getString(UpdateInactivePhoneConstants.Constants.PARAM_BANK_BOOK_IMAGE_PATH, ""))
        if (!TextUtils.isEmpty(changePhoneNumberRequestModel.uploadHostModel?.uploadHostData?.generatedHost?.uploadHost)) {
            params.putString(UpdateInactivePhoneConstants.Constants.IMAGE_UPLOAD_URL, changePhoneNumberRequestModel.uploadHostModel?.uploadHostData?.generatedHost?.uploadHost)
        }
        return params
    }


    private fun getUploadIdImageParam(requestParams: RequestParams): RequestParams {
        val params = RequestParams.create()
        userSessionInterface.deviceId
        params.putString(UpdateInactivePhoneConstants.Constants.USERID,
                requestParams.getString(UpdateInactivePhoneConstants.Constants.USERID,
                        userSessionInterface.temporaryUserId))
        params.putString(UpdateInactivePhoneConstants.Constants.PARAM_FILE_TO_UPLOAD,
                requestParams.getString(UpdateInactivePhoneConstants.Constants.PARAM_KTP_IMAGE_PATH, ""))
        if (!TextUtils.isEmpty(changePhoneNumberRequestModel.uploadHostModel?.uploadHostData?.generatedHost?.uploadHost)) {
            params.putString(UpdateInactivePhoneConstants.Constants.IMAGE_UPLOAD_URL, changePhoneNumberRequestModel.uploadHostModel?.uploadHostData?.generatedHost?.uploadHost) }
        return params
    }

    private fun getSubmitImageParam(requestParams: RequestParams): RequestParams {
        val params = RequestParams.create()
        params.putString(UpdateInactivePhoneConstants.QueryConstants.ID_CARD_IMAGE, changePhoneNumberRequestModel.uploadIdImageModel?.uploadImageData?.picObj)
        if (!TextUtils.isEmpty(requestParams.getString(UpdateInactivePhoneConstants.Constants.PARAM_BANK_BOOK_IMAGE_PATH, ""))) {
            params.putString(UpdateInactivePhoneConstants.QueryConstants.SAVING_BOOK_IMAGE,
                    changePhoneNumberRequestModel.uploadBankBookImageModel?.uploadImageData?.picObj)
        }
        params.putString(UpdateInactivePhoneConstants.QueryConstants.SAVING_BOOK_IMAGE, changePhoneNumberRequestModel.uploadIdImageModel?.uploadImageData?.picObj)
        params.putString(UpdateInactivePhoneConstants.QueryConstants.PHONE, requestParams.getString(UpdateInactivePhoneConstants.QueryConstants.PHONE, ""))
        params.putString(UpdateInactivePhoneConstants.QueryConstants.EMAIL, requestParams.getString(UpdateInactivePhoneConstants.QueryConstants.EMAIL, ""))
        params.putInt(UpdateInactivePhoneConstants.QueryConstants.USER_ID, requestParams.getInt(UpdateInactivePhoneConstants.QueryConstants.USER_ID, 0))
        return params
    }

    private suspend fun getUploadHost(email: String, phone: String, userId: String) {
        requestParams = getUploadChangePhoneNumberRequestParam(email, phone, userId)
        val uploadHostModel = getUploadHostUseCase.getUploadHost(getUploadHostParam())
        changePhoneNumberRequestModel.uploadHostModel = uploadHostModel
        changePhoneNumberRequestModel.isSuccess = uploadHostModel.isSuccess

        when {
            (changePhoneNumberRequestModel.uploadHostModel?.isSuccess == false) -> { throw RuntimeException(changePhoneNumberRequestModel.uploadHostModel?.errorMessage) }
            (changePhoneNumberRequestModel.uploadHostModel?.isSuccess == true) -> { uploadIdImage() }
        }
    }

    private suspend fun uploadIdImage(){
        val uploadImageModel = requestParams?.let {
            getUploadIdImageParam(it)
        }?.let {
            uploadImageUseCase.uploadImage(it)
        }
        changePhoneNumberRequestModel.uploadIdImageModel = uploadImageModel
        changePhoneNumberRequestModel.isSuccess = uploadImageModel?.isSuccess ?: false

        when {
            (changePhoneNumberRequestModel.uploadIdImageModel?.isSuccess == false) -> { throw RuntimeException(changePhoneNumberRequestModel.uploadIdImageModel?.errorMessage) }
            (changePhoneNumberRequestModel.uploadIdImageModel?.isSuccess == true) -> { uploadBankBookImage() }
        }
    }

    private suspend fun uploadBankBookImage(){
        val uploadImageAccountModel = requestParams?.let {
            getUploadBookBankImageParam(it)
        }?.let {
            uploadImageUseCase.uploadImage(it)
        }
        changePhoneNumberRequestModel.uploadBankBookImageModel = uploadImageAccountModel
        changePhoneNumberRequestModel.isSuccess = uploadImageAccountModel?.isSuccess ?: false

        when {
            (changePhoneNumberRequestModel.uploadBankBookImageModel?.isSuccess == false) -> { throw RuntimeException(changePhoneNumberRequestModel.uploadBankBookImageModel?.errorMessage) }
            (changePhoneNumberRequestModel.uploadBankBookImageModel?.isSuccess == true) -> { submitImage() }
        }
    }

    private fun submitImage(){
        val submitImageParams = requestParams?.let { getSubmitImageParam(it) }
        submitImageParams?.let {
            submitImageUseCase.submitImage(onSuccessSubmitImage(), onErrorSubmitImage(), it)
        }
    }

    private fun onErrorValidateUserData(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableValidateUserDataResponse.value = Fail(it)
        }
    }

    private fun onSuccessValidateUserData(): (GqlValidateUserDataResponse) -> Unit {
        return { mutableValidateUserDataResponse.value = Success(it.validateUserDataResponse) }
    }

    private fun onErrorSubmitImage(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableSubmitImage.value = Fail(it)
        }
    }

    private fun onSuccessSubmitImage(): (GqlUpdatePhoneStatusResponse) -> Unit {
        return { mutableSubmitImage.value = Success(it) }
    }

    override fun onCleared() {
        super.onCleared()
        getValidationUserDataUsecase.cancelJobs()
        submitImageUseCase.cancelJobs()
    }
}