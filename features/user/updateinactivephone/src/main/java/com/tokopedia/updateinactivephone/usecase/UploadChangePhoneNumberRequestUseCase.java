package com.tokopedia.updateinactivephone.usecase;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.updateinactivephone.R;
import com.tokopedia.updateinactivephone.model.request.ChangePhoneNumberRequestModel;
import com.tokopedia.updateinactivephone.model.request.UploadHostModel;
import com.tokopedia.updateinactivephone.model.request.UploadImageModel;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.IMAGE_UPLOAD_URL;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.PARAM_BANKBOOK_IMAGE_ID;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.PARAM_BANK_BOOK_IMAGE_PATH;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.PARAM_DEVICE_ID;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.PARAM_FILE_TO_UPLOAD;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.PARAM_KTP_IMAGE_ID;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.PARAM_KTP_IMAGE_PATH;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.RESOLUTION;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.SERVER_ID;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.TOKEN;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.USERID;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.EMAIL;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.PHONE;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.USER_ID;
import static com.tokopedia.updateinactivephone.usecase.GetUploadHostUseCase.PARAM_NEW_ADD;

public class UploadChangePhoneNumberRequestUseCase extends UseCase<GraphqlResponse> {


    private final UploadImageUseCase uploadImageUseCase;
    private final SubmitImageUseCase submitImageUseCase;
    private GetUploadHostUseCase getUploadHostUseCase;

    public UploadChangePhoneNumberRequestUseCase(ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutionThread,
                                                 UploadImageUseCase uploadImageUseCase,
                                                 SubmitImageUseCase submitImageUseCase,
                                                 GetUploadHostUseCase getUploadHostUseCase) {
        super(threadExecutor, postExecutionThread);
        this.uploadImageUseCase = uploadImageUseCase;
        this.submitImageUseCase = submitImageUseCase;
        this.getUploadHostUseCase = getUploadHostUseCase;
    }

    @Override
    public Observable<GraphqlResponse> createObservable(RequestParams requestParams) {

        final ChangePhoneNumberRequestModel changePhoneNumberRequestModel = new ChangePhoneNumberRequestModel();

        Observable<ChangePhoneNumberRequestModel> initialObservable = Observable.just(requestParams)
                .flatMap((Func1<RequestParams, Observable<ChangePhoneNumberRequestModel>>) requestParams1 -> Observable.just(changePhoneNumberRequestModel))

                .flatMap((Func1<ChangePhoneNumberRequestModel, Observable<UploadHostModel>>) changePhoneNumberRequestModel14 -> getUploadHost(getUploadHostParam(requestParams)))

                .flatMap((Func1<UploadHostModel, Observable<ChangePhoneNumberRequestModel>>) uploadHostModel -> {
                    changePhoneNumberRequestModel.setUploadHostModel(uploadHostModel);
                    changePhoneNumberRequestModel.setSuccess(uploadHostModel.isSuccess());

                    if (!changePhoneNumberRequestModel.getUploadHostModel().isSuccess()
                            && changePhoneNumberRequestModel.getUploadHostModel().getErrorMessage() != null)
                        throw new ErrorMessageException(changePhoneNumberRequestModel.getUploadHostModel().getErrorMessage());
                    else if (!changePhoneNumberRequestModel.getUploadHostModel().isSuccess()
                            && changePhoneNumberRequestModel.getUploadHostModel().getResponseCode() != 200)
                        throw new RuntimeException(String.valueOf(changePhoneNumberRequestModel.getUploadHostModel().getResponseCode()));
                    return Observable.just(changePhoneNumberRequestModel);
                })

                .flatMap((Func1<ChangePhoneNumberRequestModel, Observable<UploadImageModel>>) changePhoneNumberRequestModel1 -> uploadImage(getUploadIdImageParam(requestParams,
                        changePhoneNumberRequestModel1)))

                .flatMap((Func1<UploadImageModel, Observable<ChangePhoneNumberRequestModel>>) uploadImageModel -> {
                    changePhoneNumberRequestModel.setUploadIdImageModel(uploadImageModel);
                    changePhoneNumberRequestModel.setSuccess(uploadImageModel.isSuccess());

                    if (!changePhoneNumberRequestModel.getUploadIdImageModel().isSuccess()
                            && changePhoneNumberRequestModel.getUploadIdImageModel().getErrorMessage() != null)
                        throw new ErrorMessageException(changePhoneNumberRequestModel.getUploadIdImageModel().getErrorMessage());
                    else if (!changePhoneNumberRequestModel.getUploadIdImageModel().isSuccess()
                            && changePhoneNumberRequestModel.getUploadIdImageModel().getResponseCode() != 200)
                        throw new RuntimeException(String.valueOf(changePhoneNumberRequestModel.getUploadIdImageModel().getResponseCode()));
                    return Observable.just(changePhoneNumberRequestModel);
                });

        if (!TextUtils.isEmpty(requestParams.getString(PARAM_BANK_BOOK_IMAGE_PATH, ""))) {
            initialObservable = initialObservable.flatMap((Func1<ChangePhoneNumberRequestModel, Observable<UploadImageModel>>) changePhoneNumberRequestModel12 -> uploadImage(getUploadBookBankImageParam(requestParams,
                    changePhoneNumberRequestModel12)))
                    .flatMap((Func1<UploadImageModel, Observable<ChangePhoneNumberRequestModel>>) uploadImageModel -> {
                        changePhoneNumberRequestModel.setUploadBankBookImageModel(uploadImageModel);
                        changePhoneNumberRequestModel.setSuccess(uploadImageModel.isSuccess());

                        if (!changePhoneNumberRequestModel.getUploadBankBookImageModel().isSuccess()
                                && changePhoneNumberRequestModel.getUploadBankBookImageModel().getErrorMessage() != null)
                            throw new ErrorMessageException(changePhoneNumberRequestModel.getUploadBankBookImageModel().getErrorMessage());
                        else if (!changePhoneNumberRequestModel.getUploadBankBookImageModel().isSuccess()
                                && changePhoneNumberRequestModel.getUploadBankBookImageModel().getResponseCode() != 200)
                            throw new RuntimeException(String.valueOf(changePhoneNumberRequestModel.getUploadBankBookImageModel().getResponseCode()));

                        return Observable.just(changePhoneNumberRequestModel);
                    });
        }

        return initialObservable.flatMap((Func1<ChangePhoneNumberRequestModel, Observable<GraphqlResponse>>) changePhoneNumberRequestModel13 ->
                submitImageUseCase.getObservable(getSubmitImageParam(requestParams,
                        changePhoneNumberRequestModel13)));


    }

    private RequestParams getSubmitImageParam(RequestParams requestParams,
                                              ChangePhoneNumberRequestModel changePhoneNumberRequestModel) {
        RequestParams params = RequestParams.create();
        params.putString(SubmitImageUseCase.PARAM_FILE_UPLOADED,
                generateFileUploaded(requestParams, changePhoneNumberRequestModel));
        params.putString(PHONE, requestParams.getString(PHONE, ""));
        params.putString(EMAIL, requestParams.getString(EMAIL, ""));
        params.putInt(USER_ID, requestParams.getInt(USER_ID, 0));
        return params;
    }

    private String generateFileUploaded(RequestParams requestParams, ChangePhoneNumberRequestModel changePhoneNumberRequestModel) {
        JSONObject reviewPhotos = new JSONObject();

        try {
            reviewPhotos.put(PARAM_KTP_IMAGE_ID,
                    changePhoneNumberRequestModel.getUploadIdImageModel().getUploadImageData().getPicObj());
            if (!TextUtils.isEmpty(requestParams.getString(PARAM_BANK_BOOK_IMAGE_PATH, ""))) {
                reviewPhotos.put(PARAM_BANKBOOK_IMAGE_ID,
                        changePhoneNumberRequestModel.getUploadBankBookImageModel().getUploadImageData().getPicObj());
            }
        } catch (JSONException e) {
            throw new ErrorMessageException(MainApplication.getAppContext().getString(R.string.default_error_upload_image));
        }

        return reviewPhotos.toString();
    }

    private Observable<UploadImageModel> uploadImage(RequestParams requestParams) {
        return uploadImageUseCase.createObservable(requestParams);
    }

    private RequestParams getUploadBookBankImageParam(RequestParams requestParams, ChangePhoneNumberRequestModel changePhoneNumberRequestModel) {
        RequestParams params = RequestParams.create();

        params.putString(USERID,
                requestParams.getString(USERID,
                        SessionHandler.getTempLoginSession(MainApplication.getAppContext())));
        params.putString(PARAM_DEVICE_ID,
                requestParams.getString(PARAM_DEVICE_ID,
                        GCMHandler.getRegistrationId(MainApplication.getAppContext())));

        params.putString(PARAM_FILE_TO_UPLOAD,
                requestParams.getString(PARAM_BANK_BOOK_IMAGE_PATH, ""));
        params.putInt(SERVER_ID, requestParams.getInt(SERVER_ID, 49));
        params.putInt(RESOLUTION, requestParams.getInt(RESOLUTION, 215));
        params.putString(TOKEN, requestParams.getString(TOKEN, ""));

        if(changePhoneNumberRequestModel != null &&
                changePhoneNumberRequestModel.getUploadHostModel() != null &&
                changePhoneNumberRequestModel.getUploadHostModel().getUploadHostData() != null &&
                changePhoneNumberRequestModel.getUploadHostModel().getUploadHostData().getGeneratedHost() != null &&
                !TextUtils.isEmpty(changePhoneNumberRequestModel.getUploadHostModel().getUploadHostData().getGeneratedHost().getUploadHost())) {

            params.putString(IMAGE_UPLOAD_URL, changePhoneNumberRequestModel.getUploadHostModel().getUploadHostData().getGeneratedHost().getUploadHost());
        }


        return params;
    }


    private RequestParams getUploadIdImageParam(RequestParams requestParams, ChangePhoneNumberRequestModel changePhoneNumberRequestModel) {
        RequestParams params = RequestParams.create();

        params.putString(USERID,
                requestParams.getString(USERID,
                        SessionHandler.getTempLoginSession(MainApplication.getAppContext())));
        params.putString(PARAM_DEVICE_ID,
                requestParams.getString(PARAM_DEVICE_ID,
                        GCMHandler.getRegistrationId(MainApplication.getAppContext())));
        params.putString(PARAM_FILE_TO_UPLOAD,
                requestParams.getString(PARAM_KTP_IMAGE_PATH, ""));
        params.putInt(SERVER_ID, requestParams.getInt(SERVER_ID, 49));
        params.putInt(RESOLUTION, requestParams.getInt(RESOLUTION, 215));
        params.putString(TOKEN, requestParams.getString(TOKEN, ""));

        if(changePhoneNumberRequestModel != null &&
                changePhoneNumberRequestModel.getUploadHostModel() != null &&
                changePhoneNumberRequestModel.getUploadHostModel().getUploadHostData() != null &&
                changePhoneNumberRequestModel.getUploadHostModel().getUploadHostData().getGeneratedHost() != null &&
                !TextUtils.isEmpty(changePhoneNumberRequestModel.getUploadHostModel().getUploadHostData().getGeneratedHost().getUploadHost())) {

            params.putString(IMAGE_UPLOAD_URL, changePhoneNumberRequestModel.getUploadHostModel().getUploadHostData().getGeneratedHost().getUploadHost());
        }

        return params;
    }

    private RequestParams getUploadHostParam(RequestParams requestParams) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_NEW_ADD, requestParams.getString(PARAM_NEW_ADD,
                GetUploadHostUseCase.DEFAULT_NEW_ADD));
        return params;
    }

    private Observable<UploadHostModel> getUploadHost(RequestParams requestParams) {
        return getUploadHostUseCase.createObservable(requestParams);
    }

    @Override
    public void unsubscribe() {
        if (uploadImageUseCase != null) {
            uploadImageUseCase.unsubscribe();
        }
        if (submitImageUseCase != null) {
            submitImageUseCase.unsubscribe();
        }
        if(getUploadHostUseCase != null) {
            getUploadHostUseCase.unsubscribe();
        }
        super.unsubscribe();
    }
}
