package com.tokopedia.core.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.DownloadResultReceiver;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.data.DataManagerImpl;
import com.tkpd.library.utils.data.DataReceiver;
import com.tokopedia.core2.R;
import com.tokopedia.core.SplashScreen;
import com.tokopedia.core.database.model.Bank;
import com.tokopedia.core.database.model.City;
import com.tokopedia.core.database.model.District;
import com.tokopedia.core.database.model.Province;
import com.tokopedia.core.home.model.HotListModel;
import com.tokopedia.core.home.presenter.HotList;
import com.tokopedia.core.home.presenter.HotListImpl;
import com.tokopedia.core.network.apiservices.search.HotListService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.service.constant.DownloadServiceConstant;
import com.tokopedia.core.session.model.LoginFacebookViewModel;
import com.tokopedia.core.session.model.LoginGoogleModel;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.RecyclerViewItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * this is for critical data only for session
 * <p>
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 * Please do not use this class. Use UseCase instead
 */
@Deprecated
public class DownloadService extends IntentService implements DownloadServiceConstant, DataReceiver {
    public static final String TAG = "DownloadService";
    private static final String IS_LOGIN = "is_login";
    public static final String messageTAG = DownloadService.class.getSimpleName() + " ";
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    static String emailV2;
    static String passwordV2;
    static int loginType;
    static LoginGoogleModel loginGoogleModel;
    static LoginFacebookViewModel loginFacebookViewModel;
    static int loginVia;
    SessionHandler sessionHandler;
    CompositeSubscription compositeSubscription = new CompositeSubscription();
    int EmailType = 0;
    int FacebookType = 1;
    int GooglePlusType = 2;
    int WebViewType = 3;
    private AuthService service;
    private Gson gson;
    private ResultReceiver receiver;
    private LocalCacheHandler cache;

    public DownloadService() {
        super("DownloadService");
    }

    public static void setLoginGoogleModel(LoginGoogleModel googleModel){
        loginGoogleModel = googleModel;
    }

    public static void setLoginFacebookViewModel(LoginFacebookViewModel facebookViewModel){
        loginFacebookViewModel = facebookViewModel;
    }

    public static void startDownload(Context context, DownloadResultReceiver receiver, Bundle bundle, int type) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, context, DownloadService.class);
        intent.putExtras(bundle);
        boolean isNeedLogin = bundle.getBoolean(IS_NEED_LOGIN, false);
        if (receiver != null)
            intent.putExtra(DownloadService.RECEIVER, receiver);

        // set mandatory param
        intent.putExtra(TYPE, type);
        intent.putExtra(IS_NEED_LOGIN, isNeedLogin);

        /* Send optional extras to Download IntentService */
        switch (type) {
            case HOTLIST:
                int page = bundle.getInt(PAGE_KEY);
                int perpage = bundle.getInt(PER_PAGE_KEY);
                intent.putExtra(PAGE_KEY, page);
                intent.putExtra(PER_PAGE_KEY, perpage);
                break;
            default:
                throw new RuntimeException("unknown type for starting download !!!");
        }

        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, messageTAG + " Service Started!");

        receiver = intent.getParcelableExtra(RECEIVER);
        int type = intent.getIntExtra(TYPE, INVALID_TYPE);
        gson = new GsonBuilder().create();
        sessionHandler = new SessionHandler(getApplicationContext());
        boolean isNeedLogin = intent.getBooleanExtra(IS_NEED_LOGIN, false);

        switch (type) {
            case HOTLIST:
                /* Update UI: Download Service is Running */
                Bundle running = new Bundle();
                running.putInt(TYPE, type);
                receiver.send(STATUS_RUNNING, running);
                int page = intent.getIntExtra(PAGE_KEY, 1);
                Map<String, String> params = new HashMap<String, String>();
                params.put(HotList.QUERY_KEY, "");
                params.put(HotList.PAGE_KEY, page + "");
                params.put(HotList.PER_PAGE_KEY, HotList.PER_PAGE_VALUE + "");
                service = new HotListService();
                ((HotListService) service).getApi().getHotList(AuthUtil.generateParams(getApplicationContext(), params))
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber(type));
                break;

            default:
                break;
        }
    }

    @Override
    public CompositeSubscription getSubscription() {
        return RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
    }

    @Override
    public void setDistricts(List<District> districts) {
        throw new RuntimeException("Not supported yet");
    }

    @Override
    public void setCities(List<City> cities) {
        throw new RuntimeException("Not supported yet");
    }

    @Override
    public void setProvinces(List<Province> provinces) {
        // step 3- get list shipping city
        DataManagerImpl.getDataManager().getListShippingCity(this, this);
    }

    @Override
    public void setBank(List<Bank> banks) {
        throw new RuntimeException("Not supported yet");
    }

    @Override
    public void setShippingCity(List<District> districts) {
        LocalCacheHandler cacheHandler = new LocalCacheHandler(
                DownloadService.this,
                FETCH_DEPARTMENT_
        );
        cacheHandler.setExpire(SplashScreen.DAYS_IN_SECONDS);

        Bundle finish = new Bundle();
        finish.putInt(TYPE, DownloadServiceConstant.FETCH_DEPARTMENT);
        receiver.send(STATUS_FINISHED, finish);
    }

    @Override
    public void onNetworkError(String message) {
        Bundle resultData = new Bundle();
        resultData.putInt(TYPE, DownloadServiceConstant.FETCH_DEPARTMENT);
        //                                        resultData.putInt(NETWORK_ERROR_FLAG, 10101);
        resultData.putString(MESSAGE_ERROR_FLAG, message);
        receiver.send(STATUS_ERROR, resultData);
    }

    @Override
    public void onMessageError(String message) {
        Bundle resultData = new Bundle();
        resultData.putInt(TYPE, DownloadServiceConstant.FETCH_DEPARTMENT);
//                                        resultData.putInt(NETWORK_ERROR_FLAG, errorCode);
        resultData.putString(MESSAGE_ERROR_FLAG, message);
        receiver.send(STATUS_ERROR, resultData);
    }

    @Override
    public void onUnknownError(String message) {
        Bundle resultData = new Bundle();
        resultData.putInt(TYPE, DownloadServiceConstant.FETCH_DEPARTMENT);
//                                        resultData.putInt(NETWORK_ERROR_FLAG, errorCode);
        resultData.putString(MESSAGE_ERROR_FLAG, message);
        receiver.send(STATUS_ERROR, resultData);
    }

    @Override
    public void onTimeout() {
        Bundle resultData = new Bundle();
        resultData.putInt(TYPE, DownloadServiceConstant.FETCH_DEPARTMENT);
//                                        resultData.putInt(NETWORK_ERROR_FLAG, errorCode);
        resultData.putString(MESSAGE_ERROR_FLAG, "Time Out");
        receiver.send(STATUS_ERROR, resultData);
    }

    @Override
    public void onFailAuth() {
        Bundle resultData = new Bundle();
        resultData.putInt(TYPE, DownloadServiceConstant.FETCH_DEPARTMENT);
//                                        resultData.putInt(NETWORK_ERROR_FLAG, errorCode);
        resultData.putString(MESSAGE_ERROR_FLAG, "On Fail Auth");
        receiver.send(STATUS_ERROR, resultData);
    }


    private class Subscriber extends rx.Subscriber<Response<TkpdResponse>> {
        int type;
        ErrorListener listener;

        public Subscriber(int type) {
            this.type = type;
            listener = new ErrorListener(type);
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, messageTAG + e.getLocalizedMessage());
            if (e instanceof UnknownHostException) {
                listener.noConnection();
            } else if (e instanceof SocketTimeoutException) {
                listener.onTimeout();
            } else if (e instanceof IOException) {
                listener.onServerError();
            } else {
                listener.onUnknown();
            }
        }

        @Override
        public void onNext(Response<TkpdResponse> responseData) {
            if (responseData.isSuccessful()) {
                TkpdResponse response = responseData.body();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.getStringData());
                } catch (JSONException je) {
                    Log.e(TAG, messageTAG + je.getLocalizedMessage());
                }
                if (!response.isError()) {
                    switch (type) {
                        case HOTLIST:
                            // set paging
                            PagingHandler mPaging = new PagingHandler();
                            mPaging.setNewParameter(jsonObject);
                            boolean hasNext = mPaging.CheckNextPage();
                            mPaging.nextPage();
                            int nextPage = mPaging.getPage();

                            // add to real container data
                            List<RecyclerViewItem> items = (List<RecyclerViewItem>) parseJSON(type, jsonObject);
                            cache(jsonObject, nextPage - 1);

                            Bundle result = new Bundle();
                            result.putInt(TYPE, HOTLIST);
                            result.putParcelable(HOTLIST_DATA, Parcels.wrap(items));
                            result.putBoolean(HOTLIST_HAS_NEXT, hasNext);
                            result.putInt(HOTLIST_NEXT_PAGE, nextPage);
                            result.putBoolean(RETRY_FLAG, false);
                            receiver.send(STATUS_FINISHED, result);
                            break;
                    }
                } else {
                    onMessageError(response.getErrorMessages());
                }
            } else {
                new ErrorHandler(listener, responseData.code());
            }
        }

        public void onMessageError(List<String> MessageError) {
            Log.d(TAG, DownloadService.class.getSimpleName() + " onMessageError " + MessageError.toString());
            if (MessageError == null || !(MessageError.size() > 0))
                return;

            Bundle resultData = new Bundle();
            switch (type) {
                case SECURITY_QUESTION_GET:
                case REQUEST_OTP:
                case ANSWER_SECURITY_QUESTION:
                case REGISTER:
                case HOTLIST:
                    resultData.putInt(TYPE, type);
                    resultData.putString(MESSAGE_ERROR_FLAG, MessageError.toString().replace("[", "").replace("]", ""));
                    receiver.send(STATUS_ERROR, resultData);
                    break;
            }
        }

        private void storeUUID(Context context, String UUID) {
            LocalCacheHandler cache = new LocalCacheHandler(context, "LOGIN_UUID");
            String prevUUID = cache.getString("uuid", "");
            String currUUID;
            if (prevUUID.equals("")) {
                currUUID = UUID;

            } else {
                currUUID = prevUUID + "*~*" + UUID;
            }
            cache.putString("uuid", currUUID);
            cache.applyEditor();
        }

        private Object parseJSON(int type, JSONObject response) {
            switch (type) {
                case HOTLIST:
                    List<RecyclerViewItem> temps = new ArrayList<>();
                    try {
                        JSONArray listHot = new JSONArray(response.getString(HotList.LIST_KEY));
                        java.lang.reflect.Type listType = new TypeToken<List<HotListModel>>() {
                        }.getType();
                        temps = gson.fromJson(listHot.toString(), listType);
                    } catch (JSONException json) {
                        Log.e(TAG, HotListImpl.class.getSimpleName() + " is error : " + json.getLocalizedMessage());
                    }
                    return temps;
            }
            return null;
        }

        private void cache(JSONObject jsonObject, Object... data) {
            switch (type) {
                case HOTLIST:
                    // only cache first page
                    int page = (int) data[0];
                    if (page == 1) {
                        cache = new LocalCacheHandler(getApplicationContext(), HotList.CACHE_KEY);
                        cache.putString(HotList.HOT_LIST_PAGE_1, jsonObject.toString());
                        cache.putLong(HotList.EXPIRY, System.currentTimeMillis() / 1000);
                        cache.applyEditor();
                    }
                    break;
            }
        }

        /**
         * No connection still not known
         */
        public class ErrorListener implements com.tokopedia.core.network.retrofit.response.ErrorListener {
            int errorCode;
            String error;

            public ErrorListener(int errorCode) {
                this.errorCode = errorCode;
                switch (errorCode) {
                    case ResponseStatus.SC_REQUEST_TIMEOUT:
                        error = NetworkConfig.TIMEOUT_TEXT;
                        break;
                    case ResponseStatus.SC_GATEWAY_TIMEOUT:
                        error = NetworkConfig.TIMEOUT_TEXT;
                        break;
                    case ResponseStatus.SC_INTERNAL_SERVER_ERROR:
                        error = "SERVER ERROR";
                        break;
                    case ResponseStatus.SC_FORBIDDEN:
                        error = "FORBIDDEN ACCESS";
                        break;
                    case ResponseStatus.SC_BAD_GATEWAY:
                        error = "INVALID INPUT";
                        break;
                    case ResponseStatus.SC_BAD_REQUEST:
                        error = "INVALID INPUT";
                        break;
                }
            }

            public void onResponse() {
                Bundle resultData = new Bundle();
                switch (type) {
                    case SECURITY_QUESTION_GET:
                    case REQUEST_OTP:
                    case ANSWER_SECURITY_QUESTION:
                    case HOTLIST:
                    case REGISTER:
                        resultData.putInt(TYPE, type);
                        resultData.putInt(NETWORK_ERROR_FLAG, errorCode);
                        resultData.putString(MESSAGE_ERROR_FLAG, error.toString());
                        receiver.send(STATUS_ERROR, resultData);
                        break;
                }
            }

            public void noConnection() {
                error = DownloadServiceConstant.noNetworkConnection;
                onResponse();
            }

            @Override
            public void onUnknown() {
                error = getString(R.string.default_request_error_unknown);
                onResponse();
            }

            @Override
            public void onTimeout() {
                error = getString(R.string.default_request_error_timeout);
                onResponse();
            }

            @Override
            public void onServerError() {
                error = getString(R.string.default_request_error_internal_server);
                onResponse();
            }

            @Override
            public void onBadRequest() {
                error = getString(R.string.default_request_error_bad_request);
                onResponse();
            }

            @Override
            public void onForbidden() {
                error = getString(R.string.msg_connection_timeout);
                onResponse();
            }
        }
    }

}
