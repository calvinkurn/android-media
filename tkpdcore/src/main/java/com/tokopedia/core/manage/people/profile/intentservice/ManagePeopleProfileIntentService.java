package com.tokopedia.core.manage.people.profile.intentservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core2.R;
import com.tokopedia.core.manage.people.profile.datamanager.NetworkParam;
import com.tokopedia.core.manage.people.profile.interactor.UploadImageProfile;
import com.tokopedia.core.manage.people.profile.model.PeopleProfilePass;
import com.tokopedia.core.manage.people.profile.model.Profile;
import com.tokopedia.core.manage.people.profile.model.UploadProfileImageData;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.apiservices.user.PeopleActService;
import com.tokopedia.core.network.retrofit.response.GeneratedHost;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.util.ImageUploadHandler;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by stevenfredian on 4/29/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class ManagePeopleProfileIntentService extends IntentService {

    public final static String EXTRA_TYPE = "EXTRA_TYPE";
    public final static String EXTRA_BUNDLE = "EXTRA_BUNDLE";
    public final static String EXTRA_RECEIVER = "EXTRA_RECEIVER";

    public final static int STATUS_SUCCESS = 1;
    public final static int STATUS_ERROR = 2;
    public final static int STATUS_TIME_OUT = 3;


    public final static int ACTION_EDIT_PROFILE = 771;

    public final static String PARAM_SAVE = "PARAM_SAVE";
    public static final String EXTRA_PARAM_ACTION_TYPE = "action_type";
    public static final String EXTRA_PARAM_NETWORK_ERROR_TYPE = "network_error_type";
    public static final String EXTRA_PARAM_NETWORK_ERROR_MESSAGE = "server_error_message";

    private static final String TAG = ManagePeopleProfileIntentService.class.getSimpleName();

    private ResultReceiver receiver;
    private int action;


    public ManagePeopleProfileIntentService() {
        super("ManagePeopleProfileIntentService");
    }

    public static void saveProfile(@NonNull Context context,
                                   @NonNull PeopleProfilePass param,
                                   @NonNull ManagePeopleProfileResultReceiver receiver) {
        Intent intent = new Intent(context, ManagePeopleProfileIntentService.class);
        intent.putExtra(EXTRA_TYPE, ACTION_EDIT_PROFILE);
        intent.putExtra(EXTRA_BUNDLE, param);
        intent.putExtra(EXTRA_RECEIVER, receiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            action = intent.getIntExtra(EXTRA_TYPE, 0);
            PeopleProfilePass param = intent.getParcelableExtra(EXTRA_BUNDLE);
            receiver = intent.getParcelableExtra(EXTRA_RECEIVER);

            switch (action) {
                case ACTION_EDIT_PROFILE:
                    handleSave(param);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown Action");
            }
        }
    }

    private void handleSave(PeopleProfilePass param) {
        Observable.just(param)
                .flatMap(new Func1<PeopleProfilePass, Observable<PeopleProfilePass>>() {
                    @Override
                    public Observable<PeopleProfilePass> call(PeopleProfilePass peopleProfilePass) {
                        if (peopleProfilePass.getImagePath() == null || peopleProfilePass.getImagePath().isEmpty()) {
                            peopleProfilePass.setByPass(true);
                            return Observable.just(peopleProfilePass);
                        } else {
                            GenerateHostActService generateHostActService = new GenerateHostActService();
                            Observable<GeneratedHost> generateHost = generateHostActService.getApi().generateHost(AuthUtil.generateParams(getApplicationContext(), NetworkParam.generateHost()));
                            return Observable.zip(Observable.just(peopleProfilePass), generateHost, new Func2<PeopleProfilePass, GeneratedHost, PeopleProfilePass>() {
                                @Override
                                public PeopleProfilePass call(PeopleProfilePass peopleProfilePass, GeneratedHost generatedHost) {
                                    Log.d(TAG + "-step1", String.valueOf(generatedHost));
                                    if (generatedHost.getMessageError() == null || generatedHost.getMessageError().isEmpty()) {
                                        peopleProfilePass.setServerID(String.valueOf(generatedHost.getServerId()));
                                        peopleProfilePass.setUploadHost(generatedHost.getUploadHost());
                                        return peopleProfilePass;
                                    } else {
                                        throw new RuntimeException(generatedHost.getMessageError().get(0));
                                    }
                                }
                            });
                        }
                    }
                })
                .flatMap(new Func1<PeopleProfilePass, Observable<PeopleProfilePass>>() {
                    @Override
                    public Observable<PeopleProfilePass> call(PeopleProfilePass peopleProfilePass) {
                        if (peopleProfilePass.isByPass()) {
                            return Observable.just(peopleProfilePass);
                        } else {
                            return Observable.zip(Observable.just(peopleProfilePass), uploading(peopleProfilePass), new Func2<PeopleProfilePass, UploadProfileImageData, PeopleProfilePass>() {
                                @Override
                                public PeopleProfilePass call(PeopleProfilePass peopleProfilePass, UploadProfileImageData uploadedData) {
                                    if (uploadedData != null) {
                                        if (uploadedData.getMessageError() == null) {
                                            peopleProfilePass.setFileUploaded(uploadedData.getData().getPicObj());
                                            return peopleProfilePass;
                                        } else {
                                            throw new RuntimeException(uploadedData.getMessageError().get(0));
                                        }
                                    } else {
                                        throw new RuntimeException("");
                                    }
                                }
                            });
                        }
                    }
                })
                .flatMap(new Func1<PeopleProfilePass, Observable<PeopleProfilePass>>() {
                    @Override
                    public Observable<PeopleProfilePass> call(PeopleProfilePass peopleProfilePass) {
                        if (peopleProfilePass.isByPass()) {
                            return Observable.just(peopleProfilePass);
                        } else {
                            Log.d(TAG + "-step3", peopleProfilePass.getFileUploaded());
                            PeopleActService peopleActService = new PeopleActService();
                            Observable<Response<TkpdResponse>> peopleActObservable =
                                    peopleActService
                                            .getApi()
                                            .uploadProfilePic(
                                                    AuthUtil.generateParams(
                                                            getApplicationContext(),
                                                            NetworkParam.uploadProfilePic(peopleProfilePass)
                                                    )
                                            );
                            return Observable.zip(Observable.just(peopleProfilePass), peopleActObservable, new Func2<PeopleProfilePass, Response<TkpdResponse>, PeopleProfilePass>() {
                                @Override
                                public PeopleProfilePass call(PeopleProfilePass peopleProfilePass, Response<TkpdResponse> response) {
                                    Log.d(TAG + "-step3", response.body().getStrResponse());
                                    if (response.isSuccessful()) {
                                        Profile responseData = response.body().convertDataObj(Profile.class);
                                        if (responseData.isSuccess()) {
                                            peopleProfilePass.setSuccess(true);
                                            return peopleProfilePass;
                                        } else {
                                            throw new RuntimeException(response.body().getErrorMessages().get(0));
                                        }
                                    } else {
                                        throw new RuntimeException(String.valueOf(response.code()));
                                    }
                                }
                            });
                        }
                    }
                })
                .flatMap(new Func1<PeopleProfilePass, Observable<PeopleProfilePass>>() {
                    @Override
                    public Observable<PeopleProfilePass> call(PeopleProfilePass peopleProfilePass) {
                        PeopleActService peopleActService = new PeopleActService();
                        Observable<Response<TkpdResponse>> observablePeopleActService = peopleActService.getApi()
                                .editProfile(AuthUtil.generateParams(getApplicationContext(), NetworkParam.editProfile(peopleProfilePass)));

                        return Observable.zip(Observable.just(peopleProfilePass), observablePeopleActService, new Func2<PeopleProfilePass, Response<TkpdResponse>, PeopleProfilePass>() {
                            @Override
                            public PeopleProfilePass call(PeopleProfilePass peopleProfilePass, Response<TkpdResponse> response) {
                                if (response.isSuccessful()) {
                                    Profile temp = response.body().convertDataObj(Profile.class);
                                    if (temp != null) {
                                        if (temp.isSuccess()) {
                                            peopleProfilePass.setSuccess(true);
                                        } else {
                                            throw new RuntimeException(response.body().getErrorMessages().get(0));
                                        }
                                    } else {
                                        peopleProfilePass.setSuccess(false);
                                        throw new RuntimeException(response.body().getErrorMessages().get(0));
                                    }
                                } else {
                                    throw new RuntimeException(String.valueOf(response.code()));
                                }
                                return peopleProfilePass;
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PeopleProfilePass>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        String message = null;
                        Integer statusRequest;

                        if (e instanceof IOException) {
                            statusRequest = STATUS_TIME_OUT;
                        } else {
                            statusRequest = STATUS_ERROR;
                            message = e.getMessage();
                        }

                        Log.d(TAG, "onError: " + message);
                        Bundle resultData = new Bundle();
                        resultData.putInt(EXTRA_PARAM_ACTION_TYPE, action);
                        resultData.putInt(EXTRA_PARAM_NETWORK_ERROR_TYPE, statusRequest);
                        resultData.putString(EXTRA_PARAM_NETWORK_ERROR_MESSAGE, message);

                        receiver.send(STATUS_ERROR, resultData);
                    }

                    @Override
                    public void onNext(PeopleProfilePass peopleProfilePass) {
                        Log.d(TAG, "onNext: " + peopleProfilePass);
                        if (peopleProfilePass.isSuccess()) {
                            Bundle resultData = new Bundle();
                            resultData.putInt(EXTRA_PARAM_ACTION_TYPE, action);
                            receiver.send(STATUS_SUCCESS, resultData);
                        } else {
                            throw new RuntimeException("");
                        }
                    }
                });
    }

    private Observable<UploadProfileImageData> uploading(PeopleProfilePass peopleProfilePass) {
        NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, getApplicationContext(),
                "https://" + peopleProfilePass.getUploadHost())
                .setIdentity()
                .addParam("server_id", String.valueOf(peopleProfilePass.getServerID()))
                .addParam("new_add", String.valueOf(2))
                .compileAllParam()
                .finish();

        File file = null;
        try {
            file = ImageUploadHandler.writeImageToTkpdPath(ImageUploadHandler.compressImage(peopleProfilePass.getImagePath()));
        } catch (IOException e) {
            throw new RuntimeException(getApplicationContext().getString(R.string.error_upload_image));
        }
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.USER_ID));
        RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_ID));
        RequestBody hash = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.HASH));
        RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_TIME));
        RequestBody fileToUpload = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get("server_id"));
        RequestBody serverLanguage = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get("new_add"));

        Log.d(TAG + "(step 2):host", peopleProfilePass.getUploadHost());
        return RetrofitUtils.createRetrofit(networkCalculator.getUrl())
                .create(UploadImageProfile.class)
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
                        serverId,
                        serverLanguage
                );
    }

}
