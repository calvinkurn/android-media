package com.tokopedia.updateinactivephone.viewmodel

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.core.app.MainApplication
import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.core.util.SessionHandler
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants
import com.tokopedia.updateinactivephone.data.model.request.ChangePhoneNumberRequestModel
import com.tokopedia.updateinactivephone.data.model.request.UploadHostModel
import com.tokopedia.updateinactivephone.data.model.request.UploadImageModel
import com.tokopedia.updateinactivephone.data.model.response.GqlValidateUserDataResponse
import com.tokopedia.updateinactivephone.data.model.response.ValidateUserDataResponse
import com.tokopedia.updateinactivephone.usecase.*
import com.tokopedia.updateinactivephone.viewmodel.ChangeInactivePhoneViewModel.Companion.PHONE_MATCHER
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import org.json.JSONException
import org.json.JSONObject
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

class ChangeInactiveFormRequestViewModel @Inject constructor(
        private val getValidationUserDataUsecase: GetValidationUserDataUseCase,
        private val getUploadHostUseCase: GetUploadHostUseCase,
        private val uploadImageUseCase: UploadImageUseCase,
        private val submitImageUseCase: SubmitImgUseCase,
        dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    private var photoIdImagePath: String? = null
    private var accountImagePath: String? = null

    private val mutableValidateUserDataResponse = MutableLiveData<Result<ValidateUserDataResponse>>()
    val validateUserDataResponse: LiveData<Result<ValidateUserDataResponse>>
        get() = mutableValidateUserDataResponse

    private val mutableUploadHostModel = MutableLiveData<Result<UploadHostModel>>()
    val uploadHostModel: LiveData<Result<UploadHostModel>>
        get() = mutableUploadHostModel

    private val mutableUploadImageIdModel = MutableLiveData<Result<UploadImageModel>>()
    val uploadImageIdModel: LiveData<Result<UploadImageModel>>
        get() = mutableUploadImageIdModel

    private val mutableUploadImageAccountModel = MutableLiveData<Result<UploadImageModel>>()
    val uploadImageAccountModel: LiveData<Result<UploadImageModel>>
        get() = mutableUploadImageAccountModel

    private val mutableSubmitImage = MutableLiveData<Result<GraphqlResponse>>()
    val submitImageLiveData: LiveData<Result<GraphqlResponse>>
        get() = mutableSubmitImage

    private val changePhoneNumberRequestModel = ChangePhoneNumberRequestModel()

    fun setPhotoIdImagePath(imagePath: String) {
        this.photoIdImagePath = imagePath
    }

    fun setAccountPhotoImagePath(imagePath: String) {
        this.accountImagePath = imagePath
    }

    fun uploadPhotoIdImage(email: String, phone: String, userId: String) {
        if (!TextUtils.isEmpty(photoIdImagePath) && !TextUtils.isEmpty(accountImagePath)) {
            launchCatchError(block = {
                val requestParams = getUploadChangePhoneNumberRequestParam(email, phone, userId)
                val uploadHostModel = getUploadHost(getUploadHostParam(requestParams))
                changePhoneNumberRequestModel.uploadHostModel = uploadHostModel
                changePhoneNumberRequestModel.isSuccess = uploadHostModel.isSuccess

                if (changePhoneNumberRequestModel.uploadHostModel?.isSuccess == false &&
                        changePhoneNumberRequestModel.uploadHostModel?.errorMessage != null) {
                    throw RuntimeException(changePhoneNumberRequestModel.uploadHostModel?.errorMessage)

                } else if (changePhoneNumberRequestModel.uploadHostModel?.isSuccess == false) {
                    throw RuntimeException(java.lang.RuntimeException())

                } else if (changePhoneNumberRequestModel.uploadHostModel?.isSuccess == true) {
                    mutableUploadHostModel.value = Success(uploadHostModel)

                    launchCatchError(block = {
                        val uploadImageModel = uploadImage(getUploadIdImageParam(requestParams, changePhoneNumberRequestModel))
                        changePhoneNumberRequestModel.uploadIdImageModel = uploadImageModel
                        changePhoneNumberRequestModel.isSuccess = uploadImageModel.isSuccess

                        if (changePhoneNumberRequestModel.uploadIdImageModel?.isSuccess == false &&
                                changePhoneNumberRequestModel.uploadIdImageModel?.errorMessage != null) {
                            throw RuntimeException(changePhoneNumberRequestModel.uploadIdImageModel?.errorMessage)

                        } else if (changePhoneNumberRequestModel.uploadIdImageModel?.isSuccess == false &&
                                changePhoneNumberRequestModel.uploadIdImageModel?.responseCode != 200) {
                            throw RuntimeException(changePhoneNumberRequestModel.uploadIdImageModel?.responseCode.toString())

                        } else if (changePhoneNumberRequestModel.uploadIdImageModel?.isSuccess == true) {
                            mutableUploadImageIdModel.value = Success(uploadImageModel)

                            launchCatchError(block = {
                                val uploadImageAccountModel = uploadImage(getUploadBookBankImageParam(requestParams, changePhoneNumberRequestModel))
                                changePhoneNumberRequestModel.uploadBankBookImageModel = uploadImageModel
                                changePhoneNumberRequestModel.isSuccess = uploadImageModel.isSuccess

                                if (changePhoneNumberRequestModel.uploadBankBookImageModel?.isSuccess == false &&
                                        changePhoneNumberRequestModel.uploadBankBookImageModel?.errorMessage != null){
                                    throw RuntimeException(changePhoneNumberRequestModel.uploadBankBookImageModel?.errorMessage)

                                } else if (changePhoneNumberRequestModel.uploadBankBookImageModel?.isSuccess == false &&
                                        changePhoneNumberRequestModel.uploadBankBookImageModel?.responseCode != 200) {
                                    throw RuntimeException(changePhoneNumberRequestModel.uploadBankBookImageModel?.responseCode.toString())

                                } else if (changePhoneNumberRequestModel.uploadBankBookImageModel?.isSuccess == true) {
                                    mutableUploadImageAccountModel.value = Success(uploadImageAccountModel)

                                    val submitImageParams = getSubmitImageParam(requestParams, changePhoneNumberRequestModel)
                                    submitImageUseCase.submitImage(onSuccessSubmitImage(),
                                            onErrorSubmitImage(),
                                            submitImageParams)
                                }
                            }) {
                                it.printStackTrace()
                                mutableSubmitImage.value = Fail(it)
                            }
                        }

                    }) {
                        it.printStackTrace()
                        mutableSubmitImage.value = Fail(it)
                    }
                }

            }) {
                it.printStackTrace()
                mutableSubmitImage.value = Fail(it)
            }
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
        val p: Pattern = Pattern.compile(PHONE_MATCHER)
        val m: Matcher
        m = p.matcher(phoneNumber)
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

        if (!TextUtils.isEmpty(accountImagePath)) {
            setParamUploadBankBookImage(params)
        }
        return params
    }

    private fun setParamUploadBankBookImage(params: RequestParams) {
        params.putString(UpdateInactivePhoneConstants.Constants.PARAM_BANK_BOOK_IMAGE_PATH, accountImagePath)
    }

    private fun setParamUploadIdImage(params: RequestParams) {
        params.putString(UpdateInactivePhoneConstants.Constants.PARAM_KTP_IMAGE_PATH, photoIdImagePath)
    }

    private suspend fun getUploadHost(requestParams: RequestParams): UploadHostModel {
        return getUploadHostUseCase.getUploadHost(requestParams)
    }

    private fun getUploadHostParam(requestParams: RequestParams): RequestParams {
        val params = RequestParams.create()
        params.putString(GetUploadHostUseCase.PARAM_NEW_ADD, requestParams.getString(GetUploadHostUseCase.PARAM_NEW_ADD,
                GetUploadHostUseCase.DEFAULT_NEW_ADD))
        return params
    }

    private suspend fun uploadImage(requestParams: RequestParams): UploadImageModel {
        return uploadImageUseCase.uploadImage(requestParams)
    }

    private fun getUploadBookBankImageParam(requestParams: RequestParams, changePhoneNumberRequestModel: ChangePhoneNumberRequestModel?): RequestParams {
        val params = RequestParams.create()

        params.putString(UpdateInactivePhoneConstants.Constants.USERID,
                requestParams.getString(UpdateInactivePhoneConstants.Constants.USERID,
                        SessionHandler.getTempLoginSession(MainApplication.getAppContext())))
        params.putString(UpdateInactivePhoneConstants.Constants.PARAM_DEVICE_ID,
                requestParams.getString(UpdateInactivePhoneConstants.Constants.PARAM_DEVICE_ID,
                        GCMHandler.getRegistrationId(MainApplication.getAppContext())))

        params.putString(UpdateInactivePhoneConstants.Constants.PARAM_FILE_TO_UPLOAD,
                requestParams.getString(UpdateInactivePhoneConstants.Constants.PARAM_BANK_BOOK_IMAGE_PATH, ""))
        params.putInt(UpdateInactivePhoneConstants.Constants.SERVER_ID, requestParams.getInt(UpdateInactivePhoneConstants.Constants.SERVER_ID, 49))
        params.putInt(UpdateInactivePhoneConstants.Constants.RESOLUTION, requestParams.getInt(UpdateInactivePhoneConstants.Constants.RESOLUTION, 215))
        params.putString(UpdateInactivePhoneConstants.Constants.TOKEN, requestParams.getString(UpdateInactivePhoneConstants.Constants.TOKEN, ""))

        if (changePhoneNumberRequestModel?.uploadHostModel != null &&
                changePhoneNumberRequestModel.uploadHostModel?.uploadHostData != null &&
                !TextUtils.isEmpty(changePhoneNumberRequestModel.uploadHostModel?.uploadHostData?.generatedHost?.uploadHost)) {
            params.putString(UpdateInactivePhoneConstants.Constants.IMAGE_UPLOAD_URL, changePhoneNumberRequestModel.uploadHostModel?.uploadHostData?.generatedHost?.uploadHost)
        }

        return params
    }


    private fun getUploadIdImageParam(requestParams: RequestParams, changePhoneNumberRequestModel: ChangePhoneNumberRequestModel?): RequestParams {
        val params = RequestParams.create()

        params.putString(UpdateInactivePhoneConstants.Constants.USERID,
                requestParams.getString(UpdateInactivePhoneConstants.Constants.USERID,
                        SessionHandler.getTempLoginSession(MainApplication.getAppContext())))
        params.putString(UpdateInactivePhoneConstants.Constants.PARAM_DEVICE_ID,
                requestParams.getString(UpdateInactivePhoneConstants.Constants.PARAM_DEVICE_ID,
                        GCMHandler.getRegistrationId(MainApplication.getAppContext())))
        params.putString(UpdateInactivePhoneConstants.Constants.PARAM_FILE_TO_UPLOAD,
                requestParams.getString(UpdateInactivePhoneConstants.Constants.PARAM_KTP_IMAGE_PATH, ""))
        params.putInt(UpdateInactivePhoneConstants.Constants.SERVER_ID, requestParams.getInt(UpdateInactivePhoneConstants.Constants.SERVER_ID, 49))
        params.putInt(UpdateInactivePhoneConstants.Constants.RESOLUTION, requestParams.getInt(UpdateInactivePhoneConstants.Constants.RESOLUTION, 215))
        params.putString(UpdateInactivePhoneConstants.Constants.TOKEN, requestParams.getString(UpdateInactivePhoneConstants.Constants.TOKEN, ""))

        if (changePhoneNumberRequestModel?.uploadHostModel != null &&
                changePhoneNumberRequestModel.uploadHostModel?.uploadHostData != null &&
                !TextUtils.isEmpty(changePhoneNumberRequestModel.uploadHostModel?.uploadHostData?.generatedHost?.uploadHost)) {

            params.putString(UpdateInactivePhoneConstants.Constants.IMAGE_UPLOAD_URL, changePhoneNumberRequestModel.uploadHostModel?.uploadHostData?.generatedHost?.uploadHost)
        }

        return params
    }

    private fun getSubmitImageParam(requestParams: RequestParams,
                                    changePhoneNumberRequestModel: ChangePhoneNumberRequestModel): RequestParams {
        val params = RequestParams.create()
        params.putString(SubmitImgUseCase.PARAM_FILE_UPLOADED,
                generateFileUploaded(requestParams, changePhoneNumberRequestModel))
        params.putString(UpdateInactivePhoneConstants.QueryConstants.PHONE, requestParams.getString(UpdateInactivePhoneConstants.QueryConstants.PHONE, ""))
        params.putString(UpdateInactivePhoneConstants.QueryConstants.EMAIL, requestParams.getString(UpdateInactivePhoneConstants.QueryConstants.EMAIL, ""))
        params.putInt(UpdateInactivePhoneConstants.QueryConstants.USER_ID, requestParams.getInt(UpdateInactivePhoneConstants.QueryConstants.USER_ID, 0))
        return params
    }

    private fun generateFileUploaded(requestParams: RequestParams, changePhoneNumberRequestModel: ChangePhoneNumberRequestModel): String {
        val reviewPhotos = JSONObject()

        try {
            reviewPhotos.put(UpdateInactivePhoneConstants.Constants.PARAM_KTP_IMAGE_ID,
                    changePhoneNumberRequestModel.uploadIdImageModel?.uploadImageData?.picObj)
            if (!TextUtils.isEmpty(requestParams.getString(UpdateInactivePhoneConstants.Constants.PARAM_BANK_BOOK_IMAGE_PATH, ""))) {
                reviewPhotos.put(UpdateInactivePhoneConstants.Constants.PARAM_BANKBOOK_IMAGE_ID,
                        changePhoneNumberRequestModel.uploadBankBookImageModel?.uploadImageData?.picObj)
            }
        } catch (e: JSONException) {
            throw RuntimeException(MainApplication.getAppContext().getString(R.string.default_error_upload_image))
        }

        return reviewPhotos.toString()
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

    private fun onSuccessSubmitImage(): (GraphqlResponse) -> Unit {
        return { mutableSubmitImage.value = Success(it) }
    }

    override fun onCleared() {
        super.onCleared()
        getValidationUserDataUsecase.cancelJobs()
        submitImageUseCase.cancelJobs()
    }
}