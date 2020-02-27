package com.tokopedia.updateinactivephone.usecase

import android.text.TextUtils

import com.tokopedia.core.app.MainApplication
import com.tokopedia.core.base.domain.RequestParams
import com.tokopedia.core.base.domain.UseCase
import com.tokopedia.core.base.domain.executor.PostExecutionThread
import com.tokopedia.core.base.domain.executor.ThreadExecutor
import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.core.network.ErrorMessageException
import com.tokopedia.core.util.SessionHandler
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.model.request.ChangePhoneNumberRequestModel
import com.tokopedia.updateinactivephone.model.request.UploadHostModel
import com.tokopedia.updateinactivephone.model.request.UploadImageModel

import org.json.JSONException
import org.json.JSONObject

import rx.Observable
import rx.functions.Func1

import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.IMAGE_UPLOAD_URL
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.PARAM_BANKBOOK_IMAGE_ID
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.PARAM_BANK_BOOK_IMAGE_PATH
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.PARAM_DEVICE_ID
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.PARAM_FILE_TO_UPLOAD
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.PARAM_KTP_IMAGE_ID
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.PARAM_KTP_IMAGE_PATH
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.RESOLUTION
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.SERVER_ID
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.TOKEN
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.USERID
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.Companion.EMAIL
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.Companion.PHONE
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.Companion.USER_ID
import com.tokopedia.updateinactivephone.usecase.GetUploadHostUseCase.Companion.PARAM_NEW_ADD

class UploadChangePhoneNumberRequestUseCase(threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        private val uploadImageUseCase: UploadImageUseCase?,
        private val submitImageUseCase: SubmitImageUseCase?,
        private val getUploadHostUseCase: GetUploadHostUseCase?)
    : UseCase<GraphqlResponse>(threadExecutor, postExecutionThread) {

    override fun createObservable(requestParams: RequestParams): Observable<GraphqlResponse> {

        val changePhoneNumberRequestModel = ChangePhoneNumberRequestModel()

        var initialObservable = Observable.just(requestParams)
                .flatMap({ Observable.just(changePhoneNumberRequestModel) } as Func1<RequestParams, Observable<ChangePhoneNumberRequestModel>>)

                .flatMap({ getUploadHost(getUploadHostParam(requestParams)) } as Func1<ChangePhoneNumberRequestModel, Observable<UploadHostModel>>)

                .flatMap({ uploadHostModel: UploadHostModel ->
                    changePhoneNumberRequestModel.uploadHostModel = uploadHostModel
                    changePhoneNumberRequestModel.isSuccess = uploadHostModel.isSuccess

                    if (changePhoneNumberRequestModel.uploadHostModel?.isSuccess == false && changePhoneNumberRequestModel.uploadHostModel?.errorMessage != null)
                        throw ErrorMessageException(changePhoneNumberRequestModel.uploadHostModel?.errorMessage)
                    else if (changePhoneNumberRequestModel.uploadHostModel?.isSuccess == false && changePhoneNumberRequestModel.uploadHostModel?.responseCode != 200)
                        throw RuntimeException(changePhoneNumberRequestModel.uploadHostModel?.responseCode.toString())
                    Observable.just(changePhoneNumberRequestModel)
                } as Func1<UploadHostModel, Observable<ChangePhoneNumberRequestModel>>)

                .flatMap({ changePhoneNumberRequestModel1: ChangePhoneNumberRequestModel ->
                    uploadImage(getUploadIdImageParam(requestParams,
                            changePhoneNumberRequestModel1))
                } as Func1<ChangePhoneNumberRequestModel, Observable<UploadImageModel>>)

                .flatMap({ uploadImageModel: UploadImageModel ->
                    changePhoneNumberRequestModel.uploadIdImageModel = uploadImageModel
                    changePhoneNumberRequestModel.isSuccess = uploadImageModel.isSuccess

                    if (changePhoneNumberRequestModel.uploadIdImageModel?.isSuccess == false && changePhoneNumberRequestModel.uploadIdImageModel?.errorMessage != null)
                        throw ErrorMessageException(changePhoneNumberRequestModel.uploadIdImageModel?.errorMessage)
                    else if (changePhoneNumberRequestModel.uploadIdImageModel?.isSuccess == false && changePhoneNumberRequestModel.uploadIdImageModel?.responseCode != 200)
                        throw RuntimeException(changePhoneNumberRequestModel.uploadIdImageModel?.responseCode.toString())
                    Observable.just(changePhoneNumberRequestModel)
                } as Func1<UploadImageModel, Observable<ChangePhoneNumberRequestModel>>)

        if (!TextUtils.isEmpty(requestParams.getString(PARAM_BANK_BOOK_IMAGE_PATH, ""))) {
            initialObservable = initialObservable.flatMap({ changePhoneNumberRequestModel12: ChangePhoneNumberRequestModel ->
                uploadImage(getUploadBookBankImageParam(requestParams,
                        changePhoneNumberRequestModel12))
            } as Func1<ChangePhoneNumberRequestModel, Observable<UploadImageModel>>)
                    .flatMap({ uploadImageModel: UploadImageModel ->
                        changePhoneNumberRequestModel.uploadBankBookImageModel = uploadImageModel
                        changePhoneNumberRequestModel.isSuccess = uploadImageModel.isSuccess

                        if (changePhoneNumberRequestModel.uploadBankBookImageModel?.isSuccess == false && changePhoneNumberRequestModel.uploadBankBookImageModel?.errorMessage != null)
                            throw ErrorMessageException(changePhoneNumberRequestModel.uploadBankBookImageModel?.errorMessage)
                        else if (changePhoneNumberRequestModel.uploadBankBookImageModel?.isSuccess == false && changePhoneNumberRequestModel.uploadBankBookImageModel?.responseCode != 200)
                            throw RuntimeException(changePhoneNumberRequestModel.uploadBankBookImageModel?.responseCode.toString())

                        Observable.just(changePhoneNumberRequestModel)
                    } as Func1<UploadImageModel, Observable<ChangePhoneNumberRequestModel>>)
        }

        return initialObservable.flatMap({ changePhoneNumberRequestModel13: ChangePhoneNumberRequestModel ->
            submitImageUseCase?.getObservable(getSubmitImageParam(requestParams,
                    changePhoneNumberRequestModel13))
        } as Func1<ChangePhoneNumberRequestModel, Observable<GraphqlResponse>>)

    }

    private fun getSubmitImageParam(requestParams: RequestParams,
                                    changePhoneNumberRequestModel: ChangePhoneNumberRequestModel): RequestParams {
        val params = RequestParams.create()
        params.putString(SubmitImageUseCase.PARAM_FILE_UPLOADED,
                generateFileUploaded(requestParams, changePhoneNumberRequestModel))
        params.putString(PHONE, requestParams.getString(PHONE, ""))
        params.putString(EMAIL, requestParams.getString(EMAIL, ""))
        params.putInt(USER_ID, requestParams.getInt(USER_ID, 0))
        return params
    }

    private fun generateFileUploaded(requestParams: RequestParams, changePhoneNumberRequestModel: ChangePhoneNumberRequestModel): String {
        val reviewPhotos = JSONObject()

        try {
            reviewPhotos.put(PARAM_KTP_IMAGE_ID,
                    changePhoneNumberRequestModel.uploadIdImageModel?.uploadImageData?.picObj)
            if (!TextUtils.isEmpty(requestParams.getString(PARAM_BANK_BOOK_IMAGE_PATH, ""))) {
                reviewPhotos.put(PARAM_BANKBOOK_IMAGE_ID,
                        changePhoneNumberRequestModel.uploadBankBookImageModel?.uploadImageData?.picObj)
            }
        } catch (e: JSONException) {
            throw ErrorMessageException(MainApplication.getAppContext().getString(R.string.default_error_upload_image))
        }

        return reviewPhotos.toString()
    }

    private fun uploadImage(requestParams: RequestParams): Observable<UploadImageModel> {
        return uploadImageUseCase!!.createObservable(requestParams)
    }

    private fun getUploadBookBankImageParam(requestParams: RequestParams, changePhoneNumberRequestModel: ChangePhoneNumberRequestModel?): RequestParams {
        val params = RequestParams.create()

        params.putString(USERID,
                requestParams.getString(USERID,
                        SessionHandler.getTempLoginSession(MainApplication.getAppContext())))
        params.putString(PARAM_DEVICE_ID,
                requestParams.getString(PARAM_DEVICE_ID,
                        GCMHandler.getRegistrationId(MainApplication.getAppContext())))

        params.putString(PARAM_FILE_TO_UPLOAD,
                requestParams.getString(PARAM_BANK_BOOK_IMAGE_PATH, ""))
        params.putInt(SERVER_ID, requestParams.getInt(SERVER_ID, 49))
        params.putInt(RESOLUTION, requestParams.getInt(RESOLUTION, 215))
        params.putString(TOKEN, requestParams.getString(TOKEN, ""))

        if (changePhoneNumberRequestModel?.uploadHostModel != null &&
                changePhoneNumberRequestModel.uploadHostModel?.uploadHostData != null &&
                !TextUtils.isEmpty(changePhoneNumberRequestModel.uploadHostModel?.uploadHostData?.generatedHost?.uploadHost)) {
            params.putString(IMAGE_UPLOAD_URL, changePhoneNumberRequestModel.uploadHostModel?.uploadHostData?.generatedHost?.uploadHost)
        }

        return params
    }


    private fun getUploadIdImageParam(requestParams: RequestParams, changePhoneNumberRequestModel: ChangePhoneNumberRequestModel?): RequestParams {
        val params = RequestParams.create()

        params.putString(USERID,
                requestParams.getString(USERID,
                        SessionHandler.getTempLoginSession(MainApplication.getAppContext())))
        params.putString(PARAM_DEVICE_ID,
                requestParams.getString(PARAM_DEVICE_ID,
                        GCMHandler.getRegistrationId(MainApplication.getAppContext())))
        params.putString(PARAM_FILE_TO_UPLOAD,
                requestParams.getString(PARAM_KTP_IMAGE_PATH, ""))
        params.putInt(SERVER_ID, requestParams.getInt(SERVER_ID, 49))
        params.putInt(RESOLUTION, requestParams.getInt(RESOLUTION, 215))
        params.putString(TOKEN, requestParams.getString(TOKEN, ""))

        if (changePhoneNumberRequestModel?.uploadHostModel != null &&
                changePhoneNumberRequestModel.uploadHostModel?.uploadHostData != null &&
                !TextUtils.isEmpty(changePhoneNumberRequestModel.uploadHostModel?.uploadHostData?.generatedHost?.uploadHost)) {

            params.putString(IMAGE_UPLOAD_URL, changePhoneNumberRequestModel.uploadHostModel?.uploadHostData?.generatedHost?.uploadHost)
        }

        return params
    }

    private fun getUploadHostParam(requestParams: RequestParams): RequestParams {
        val params = RequestParams.create()
        params.putString(PARAM_NEW_ADD, requestParams.getString(PARAM_NEW_ADD,
                GetUploadHostUseCase.DEFAULT_NEW_ADD))
        return params
    }

    private fun getUploadHost(requestParams: RequestParams): Observable<UploadHostModel> {
        return getUploadHostUseCase!!.createObservable(requestParams)
    }

    override fun unsubscribe() {
        uploadImageUseCase?.unsubscribe()
        submitImageUseCase?.unsubscribe()
        getUploadHostUseCase?.unsubscribe()
        super.unsubscribe()
    }
}
