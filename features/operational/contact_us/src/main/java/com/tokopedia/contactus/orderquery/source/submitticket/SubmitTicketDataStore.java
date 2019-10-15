package com.tokopedia.contactus.orderquery.source.submitticket;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.home.source.api.ContactUsAPI;
import com.tokopedia.contactus.orderquery.data.ContactUsPass;
import com.tokopedia.contactus.orderquery.data.CreateTicketResult;
import com.tokopedia.contactus.orderquery.data.GenerateHostPass;
import com.tokopedia.contactus.orderquery.data.ImageUpload;
import com.tokopedia.contactus.orderquery.data.ImageUploadResult;
import com.tokopedia.contactus.orderquery.data.UploadImageContactUsParam;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.response.GeneratedHost;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.SessionHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by baghira on 16/05/18.
 */

public class SubmitTicketDataStore {
    ContactUsAPI contactUsAPI;
    Context context;

    private static final String TAG = SubmitTicketDataStore.class.getSimpleName();
    private static final String TOO_MANY_REQUEST = "TOO_MANY_REQUEST";
    private static final String PARAM_IMAGE_ID = "id";
    private static final String PARAM_TOKEN = "token";
    private static final String PARAM_WEB_SERVICE = "web_service";

    public SubmitTicketDataStore(ContactUsAPI contactUsAPI, @ApplicationContext Context context) {
        this.contactUsAPI = contactUsAPI;
        this.context = context;
    }

    public Observable<CreateTicketResult> getSubmitTicket(final ContactUsPass contactUsPass) {
        Observable<CreateTicketResult> observable = Observable.just(contactUsPass)
                .flatMap(new Func1<ContactUsPass, Observable<ContactUsPass>>() {
                    @Override
                    public Observable<ContactUsPass> call(final ContactUsPass contactUsPass) {

                        if (isHasPictures(contactUsPass)) {
                            return contactUsAPI
                                    .createTicketValidation(AuthUtil.generateParams(context, contactUsPass.getCreateTicketValidationParam()))
                                    .map(new Func1<Response<DataResponse<CreateTicketResult>>, ContactUsPass>() {
                                        @Override
                                        public ContactUsPass call(Response<DataResponse<CreateTicketResult>> tkpdResponse) {

                                            CreateTicketResult result = tkpdResponse.body().getData();
                                            contactUsPass.setPostKey(result.getPostKey());
                                            return contactUsPass;

                                        }
                                    });
                        } else {
                            return Observable.just(contactUsPass);
                        }
                    }
                })
                .flatMap(new Func1<ContactUsPass, Observable<ContactUsPass>>() {
                    @Override
                    public Observable<ContactUsPass> call(ContactUsPass
                                                                  contactUsPass) {
                        if (isHasPictures(contactUsPass)) {
                            return getObservableGenerateHost(context, contactUsPass);
                        } else {
                            return Observable.just(contactUsPass);
                        }
                    }
                })
                .flatMap(new Func1<ContactUsPass, Observable<ContactUsPass>>() {
                    @Override
                    public Observable<ContactUsPass> call(ContactUsPass
                                                                  contactUsPass) {
                        if (isHasPictures(contactUsPass)) {
                            return getObservableUploadingFile(context, contactUsPass);
                        } else {
                            return Observable.just(contactUsPass);
                        }
                    }
                })
                .flatMap(new Func1<ContactUsPass, Observable<CreateTicketResult>>() {
                    @Override
                    public Observable<CreateTicketResult> call(ContactUsPass
                                                                           contactUsPass) {
                    if(isHasPictures(contactUsPass)) {
                        return contactUsAPI.
                                createTicket(AuthUtil.generateParams(context, contactUsPass.getSubmitParam())).map(new Func1<Response<DataResponse<CreateTicketResult>>, CreateTicketResult>() {

                            @Override
                            public CreateTicketResult call(Response<DataResponse<CreateTicketResult>> dataResponseResponse) {
                                return dataResponseResponse.body().getData();
                            }
                        });
                    }else {
                        return contactUsAPI.createTicketValidation(AuthUtil.generateParams(context, contactUsPass.getCreateTicketValidationParam())).map(new Func1<Response<DataResponse<CreateTicketResult>>, CreateTicketResult>() {
                            @Override
                            public CreateTicketResult call(Response<DataResponse<CreateTicketResult>> dataResponseResponse) {
                                return dataResponseResponse.body().getData();
                            }
                        });
                    }

                    }
                });

        return observable;

    }

    private boolean isHasPictures(ContactUsPass pass) {
        return pass.getAttachment() != null && pass.getAttachment().size() > 0;
    }

    private Observable<ContactUsPass> getObservableGenerateHost(Context context,
                                                                final ContactUsPass contactUsPass) {
        GenerateHostPass paramGenerateHost = new GenerateHostPass();
        paramGenerateHost.setNewAdd("2");

        Bundle bundle = new Bundle();
        bundle.putBoolean(AccountsService.USING_HMAC, true);
        bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);

        AccountsService accountsService = new AccountsService(bundle);
        return accountsService.getApi()
                .generateHost(AuthUtil.generateParams(context, paramGenerateHost.getGenerateHostParam()))
                .map(new Func1<GeneratedHost, ContactUsPass>() {
                    @Override
                    public ContactUsPass call(GeneratedHost generatedHost) {
                        if (generatedHost.getMessageError() == null || generatedHost.getMessageError().isEmpty()) {
                            contactUsPass.setGeneratedHost(generatedHost);
                            return contactUsPass;
                        } else {
                            throw new RuntimeException(generatedHost.getMessageError().get(0));
                        }
                    }
                });
    }

    private Observable<ContactUsPass> getObservableUploadingFile(final Context context,
                                                                 final ContactUsPass contactUsPass) {

        return Observable.zip(Observable.just(contactUsPass), uploadFile(context, contactUsPass),
                new Func2<ContactUsPass, List<ImageUpload>, ContactUsPass>() {
                    @Override
                    public ContactUsPass call(ContactUsPass pass, List<ImageUpload> imageUploads) {
                        pass.setAttachment((ArrayList<ImageUpload>) imageUploads);
                        return pass;
                    }
                });
    }


    private Observable<List<ImageUpload>> uploadFile(final Context context, final ContactUsPass contactUsPass) {
        return Observable
                .from(contactUsPass.getAttachment())
                .flatMap(new Func1<ImageUpload, Observable<ImageUpload>>() {
                    @Override
                    public Observable<ImageUpload> call(ImageUpload imageUpload) {
                        String uploadUrl = "http://" + contactUsPass.getGeneratedHost().getUploadHost();
                        NetworkCalculator networkCalculator = new NetworkCalculator(
                                NetworkConfig.POST, context,
                                uploadUrl)
                                .setIdentity()
                                .addParam(PARAM_IMAGE_ID, imageUpload.getImageId())
                                .addParam(PARAM_WEB_SERVICE, "1")
                                .compileAllParam()
                                .finish();

                        File file;
                        try {
                            file = ImageUploadHandler.writeImageToTkpdPath(ImageUploadHandler.compressImage(imageUpload.getFileLoc()));
                        } catch (IOException e) {
                            throw new RuntimeException(context.getString(R.string.contact_us_error_upload_image));
                        }
                        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get(NetworkCalculator.USER_ID));
                        RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get(NetworkCalculator.DEVICE_ID));
                        RequestBody hash = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get(NetworkCalculator.HASH));
                        RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get(NetworkCalculator.DEVICE_TIME));
                        RequestBody fileToUpload = RequestBody.create(MediaType.parse("image/*"),
                                file);
                        RequestBody imageId = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get(PARAM_IMAGE_ID));
                        RequestBody web_service = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get(PARAM_WEB_SERVICE));

                        Log.d(TAG + "(step 2):host = ", contactUsPass.getGeneratedHost().getUploadHost());
                        Observable<ImageUploadResult> upload;
                        if (SessionHandler.isV4Login(context)) {
                            upload = RetrofitUtils.createRetrofit(uploadUrl)
                                    .create(UploadImageContactUsParam.class)
                                    .uploadImage(
                                            networkCalculator.getHeader().get(NetworkCalculator.CONTENT_MD5),// 1
                                            networkCalculator.getHeader().get(NetworkCalculator.DATE),// 2
                                            networkCalculator.getHeader().get(NetworkCalculator.AUTHORIZATION),// 3
                                            networkCalculator.getHeader().get(NetworkCalculator.X_METHOD),// 4
                                            userId,
                                            deviceId,
                                            hash,
                                            deviceTime,
                                            fileToUpload,
                                            imageId, web_service

                                    );
                        } else {
                            upload = RetrofitUtils.createRetrofit(uploadUrl)
                                    .create(UploadImageContactUsParam.class)
                                    .uploadImagePublic(
                                            networkCalculator.getHeader().get(NetworkCalculator.CONTENT_MD5),// 1
                                            networkCalculator.getHeader().get(NetworkCalculator.DATE),// 2
                                            networkCalculator.getHeader().get(NetworkCalculator.AUTHORIZATION),// 3
                                            networkCalculator.getHeader().get(NetworkCalculator.X_METHOD),// 4
                                            userId,
                                            deviceId,
                                            hash,
                                            deviceTime,
                                            fileToUpload,
                                            imageId,
                                            web_service);
                        }

                        return Observable.zip(Observable.just(imageUpload), upload, new Func2<ImageUpload, ImageUploadResult, ImageUpload>() {
                            @Override
                            public ImageUpload call(ImageUpload imageUpload, ImageUploadResult imageUploadResult) {
                                if (imageUploadResult.getData() != null) {
                                    imageUpload.setPicSrc(imageUploadResult.getData().getPicSrc());
                                    imageUpload.setPicObj(imageUploadResult.getData().getPicObj());
                                } else if (imageUploadResult.getMessageError() != null) {
                                    throw new RuntimeException(imageUploadResult.getMessageError());
                                }
                                return imageUpload;
                            }
                        });
                    }
                })
                .toList();
    }
}
