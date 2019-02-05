package com.tokopedia.contactus.inboxticket.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;


import com.tokopedia.core2.R;
import com.tokopedia.core.customadapter.ImageUpload;
import com.tokopedia.contactus.createticket.model.GenerateHostPass;
import com.tokopedia.contactus.createticket.model.ImageUploadResult;
import com.tokopedia.contactus.inboxticket.listener.UploadImageTicketParam;
import com.tokopedia.contactus.inboxticket.model.InboxTicketParam;
import com.tokopedia.contactus.inboxticket.model.SendReplyResult;
import com.tokopedia.contactus.inboxticket.model.inboxticket.InboxTicket;
import com.tokopedia.contactus.inboxticket.model.inboxticketdetail.InboxTicketDetail;
import com.tokopedia.contactus.inboxticket.model.replyticket.ReplyResult;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.apiservices.user.InboxTicketService;
import com.tokopedia.core.network.apiservices.user.TicketActService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.GeneratedHost;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.util.ImageUploadHandler;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by Nisie on 4/19/16.
 */
public class InboxTicketRetrofitInteractorImpl implements InboxTicketRetrofitInteractor {
    private static final String TAG = InboxTicketRetrofitInteractorImpl.class.getSimpleName();
    private static final String DEFAULT_MSG_ERROR = "Terjadi Kesalahan, Mohon ulangi beberapa saat lagi";
    private static final String PARAM_IMAGE_ID = "id";
    private static final String PARAM_WEB_SERVICE = "web_service";
    private static final String TOO_MANY_REQUEST = "TOO_MANY_REQUEST";

    private final CompositeSubscription compositeSubscription;
    private final InboxTicketService inboxTicketService;
    private final TicketActService ticketActService;
    boolean isRequesting = false;

    public InboxTicketRetrofitInteractorImpl() {
        this.compositeSubscription = new CompositeSubscription();
        this.inboxTicketService = new InboxTicketService();
        this.ticketActService = new TicketActService();
        this.isRequesting = false;
    }

    @Override
    public void getInboxTicket(@NonNull final Context context, @NonNull final Map<String, String> params, @NonNull final GetInboxTicketListener listener) {
        setRequesting(true);
        Observable<Response<TkpdResponse>> observable = inboxTicketService.getApi()
                .getInboxTicket(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);
            }

            @Override
            public void onError(Throwable e) {
                setRequesting(false);
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoConnectionError();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout("Timeout connection," +
                            " Mohon ulangi beberapa saat lagi");
                } else {
                    listener.onError("Terjadi Kesalahan, " +
                            "Mohon ulangi beberapa saat lagi");
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess(response.body().convertDataObj(InboxTicket.class));

                    } else if (response.body().getStatus().equals(TOO_MANY_REQUEST)) {
                        listener.onError(response.body().getErrorMessageJoined());
                    } else {
                        if (response.body().isNullData())
                            listener.onNullData(response.body().getErrorMessages().toString().replace("[", "").replace("]", ""));
                        else listener.onError(response.body().getErrorMessages().toString());
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout("Timeout connection," +
                                    " Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onServerError() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }
                    }, response.code());
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void getInboxTicketDetail(@NonNull final Context context, @NonNull final Map<String, String> params, @NonNull final GetInboxTicketDetailListener listener) {
        setRequesting(true);
        Observable<Response<TkpdResponse>> observable = inboxTicketService.getApi()
                .getInboxTicketDetail(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);

            }

            @Override
            public void onError(Throwable e) {
                setRequesting(false);

                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoConnectionError();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout("Timeout connection," +
                            " Mohon ulangi beberapa saat lagi");
                } else {
                    listener.onError("Terjadi Kesalahan, " +
                            "Mohon ulangi beberapa saat lagi");
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess(response.body().convertDataObj(InboxTicketDetail.class));

                    } else if (response.body().getStatus().equals(TOO_MANY_REQUEST)) {
                        listener.onError(response.body().getErrorMessageJoined());
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().toString());
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout("Timeout connection," +
                                    " Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onServerError() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }
                    }, response.code());
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void viewMore(@NonNull final Context context, @NonNull final Map<String, String> params, @NonNull final ViewMoreListener listener) {
        setRequesting(true);
        Observable<Response<TkpdResponse>> observable = inboxTicketService.getApi()
                .getInboxTicketMore(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);
            }

            @Override
            public void onError(Throwable e) {
                setRequesting(false);

                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoConnectionError();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout("Timeout connection," +
                            " Mohon ulangi beberapa saat lagi");
                } else {
                    listener.onError("Terjadi Kesalahan, " +
                            "Mohon ulangi beberapa saat lagi");
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess(response.body().convertDataObj(InboxTicketDetail.class).getTicketReply());

                    } else if (response.body().getStatus().equals(TOO_MANY_REQUEST)) {
                        listener.onError(response.body().getErrorMessageJoined());
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().toString());
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout("Timeout connection," +
                                    " Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onServerError() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }
                    }, response.code());
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void sendReply(@NonNull final Context context, @NonNull final InboxTicketParam params, @NonNull final ReplyTicketListener listener) {
        setRequesting(true);
        Observable<Response<TkpdResponse>> observable = Observable.just(params)
                .flatMap(new Func1<InboxTicketParam, Observable<InboxTicketParam>>() {
                    @Override
                    public Observable<InboxTicketParam> call(final InboxTicketParam param) {
                        if (isHasPictures(param)) {
                            return ticketActService.getApi()
                                    .replyTicketValidation(AuthUtil.generateParams(context,
                                            param.getReplyTicketParamValidation()))
                                    .map(new Func1<Response<TkpdResponse>, InboxTicketParam>() {
                                        @Override
                                        public InboxTicketParam call(Response<TkpdResponse> tkpdResponse) {
                                            SendReplyResult result = tkpdResponse.body().convertDataObj(SendReplyResult.class);
                                            if (result.getIsSuccess() == 1) {
                                                param.setPostKey(result.getPostKey());
                                                return param;
                                            } else {
                                                String errorMessage = "";
                                                for (int i = 0; i < tkpdResponse.body().getErrorMessages().size(); i++) {
                                                    errorMessage += tkpdResponse.body().getErrorMessages().get(i);
                                                }
                                                throw new RuntimeException(errorMessage);
                                            }

                                        }
                                    });
                        } else {
                            return Observable.just(param);
                        }
                    }
                })
                .flatMap(new Func1<InboxTicketParam, Observable<InboxTicketParam>>() {
                    @Override
                    public Observable<InboxTicketParam> call(InboxTicketParam param) {
                        if (isHasPictures(param)) {
                            return getObservableGenerateHost(context, param);
                        } else {
                            return Observable.just(param);
                        }
                    }
                })
                .flatMap(new Func1<InboxTicketParam, Observable<InboxTicketParam>>() {
                    @Override
                    public Observable<InboxTicketParam> call(InboxTicketParam param) {
                        if (isHasPictures(param)) {
                            return getObservableUploadingFile(context, param);
                        } else {
                            return Observable.just(param);
                        }
                    }
                })
                .flatMap(new Func1<InboxTicketParam, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(InboxTicketParam param) {
                        if (isHasPictures(param)) {
                            return ticketActService.getApi()
                                    .replyTicketSubmit(AuthUtil.generateParams(context, param.getReplyTicketParamSubmit()));
                        } else {
                            return ticketActService.getApi()
                                    .replyTicketValidation(AuthUtil.generateParams(context, param.getReplyTicketParamValidation()));
                        }
                    }
                });


        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoConnectionError();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else if (e instanceof RuntimeException) {
                    listener.onError(e.getMessage());
                } else {
                    listener.onError("Terjadi Kesalahan, " +
                            "Mohon ulangi beberapa saat lagi");
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        if (!response.body().isError()
                                && response.body().getJsonData().getString("is_success").equals("1")) {
                            listener.onSuccess(response.body().convertDataObj(ReplyResult.class));
                        } else if (response.body().getStatus().equals(TOO_MANY_REQUEST)) {
                            listener.onError(response.body().getErrorMessageJoined());
                        } else {
                            if (response.body().isNullData()) listener.onNullData();
                            else listener.onError(response.body().getErrorMessages().get(0));
                        }
                    } catch (JSONException e) {
                        listener.onError("Gagal mengirim komentar");
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError("Network Unknown Error!");
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError("Network Timeout Error!");
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError("Network Internal Server Error!");
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError("Network Bad Request Error!");
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError("Network Forbidden Error!");
                            listener.onFailAuth();
                        }
                    }, response.code());
                }

            }

        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));

    }

    private Observable<InboxTicketParam> getObservableGenerateHost(Context context,
                                                                   final InboxTicketParam param) {
        GenerateHostPass paramGenerateHost = new GenerateHostPass();
        paramGenerateHost.setNewAdd("2");

        GenerateHostActService generateHostActService = new GenerateHostActService();
        return generateHostActService.getApi()
                .generateHost(AuthUtil.generateParams(context, paramGenerateHost.getGenerateHostParam()))
                .map(new Func1<GeneratedHost, InboxTicketParam>() {
                    @Override
                    public InboxTicketParam call(GeneratedHost generatedHost) {
                        if (generatedHost.getMessageError() == null || generatedHost.getMessageError().isEmpty()) {
                            param.setGeneratedHost(generatedHost);
                            return param;
                        } else {
                            throw new RuntimeException(generatedHost.getMessageError().get(0));
                        }
                    }
                });
    }

    private Observable<InboxTicketParam> getObservableUploadingFile(final Context context,
                                                                    final InboxTicketParam param) {

        return Observable.zip(Observable.just(param), uploadFile(context, param),
                new Func2<InboxTicketParam, List<ImageUpload>, InboxTicketParam>() {
                    @Override
                    public InboxTicketParam call(InboxTicketParam reviewPass, List<ImageUpload> imageUploads) {
                        reviewPass.setImageUploads(imageUploads);
                        return reviewPass;
                    }
                });
    }

    private Observable<List<ImageUpload>> uploadFile(final Context context, final InboxTicketParam param) {
        return Observable
                .from(param.getImageUploads())
                .flatMap(new Func1<ImageUpload, Observable<ImageUpload>>() {
                    @Override
                    public Observable<ImageUpload> call(ImageUpload imageUpload) {
                        String uploadUrl = "http://" + param.getGeneratedHost().getUploadHost();
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
                            throw new RuntimeException(context.getString(R.string.error_upload_image));
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

                        Log.d(TAG + "(step 2):host = ", param.getGeneratedHost().getUploadHost());
                        Observable<ImageUploadResult> upload = RetrofitUtils.createRetrofit(uploadUrl)
                                .create(UploadImageTicketParam.class)
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
                                        imageId,
                                        web_service
                                );


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

    private boolean isHasPictures(InboxTicketParam param) {
        return param.getImageUploads() != null && param.getImageUploads().size() > 0;
    }

    @Override
    public boolean isRequesting() {
        return isRequesting;
    }

    @Override
    public void setRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }

    @Override
    public void unsubscribe() {
        compositeSubscription.unsubscribe();
    }

}
