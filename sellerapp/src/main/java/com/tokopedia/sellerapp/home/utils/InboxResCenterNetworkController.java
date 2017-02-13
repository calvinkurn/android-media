package com.tokopedia.sellerapp.home.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.tkpd.library.utils.network.BaseNetworkController;
import com.tkpd.library.utils.network.CommonListener;
import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.apiservices.user.InboxResCenterService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.sellerapp.home.model.rescenter.ResCenterInboxData;
import com.tokopedia.sellerapp.home.model.rescenter.ResCenterInboxDataPass;
import com.tokopedia.sellerapp.home.model.rescenter.ResCenterInboxDataPassFactory;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by normansyahputa on 8/31/16.
 */

public class InboxResCenterNetworkController extends BaseNetworkController {
    private InboxResCenterService inboxResCenterService;

    public InboxResCenterNetworkController(Context context, Gson gson, InboxResCenterService inboxResCenterService) {
        super(context, gson);
        this.inboxResCenterService = inboxResCenterService;
    }

    public void getResCenterList(String userId, String deviceId, final InboxResCenterListener inboxResCenterListener){
        getResCenterList(userId, deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                inboxResCenterListener.onError(e);
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> response) {
                                if (response.isSuccessful()) {
                                    if(!response.body().isError()) {
                                        String stringData = response.body().getStringData();
                                        Log.d("STUART", "getResCenterList : onNext : "+stringData);
                                        ResCenterInboxData shopModel = gson.fromJson(stringData, ResCenterInboxData.class);
                                        inboxResCenterListener.onSuccess(shopModel);
                                    }else {
                                        throw new MessageErrorException(response.body().getErrorMessages().get(0));
                                    }
                                } else {
                                    onResponseError(response.code(), inboxResCenterListener);
                                }
                            }
                        }
                );
    }

    public Observable<Response<TkpdResponse>> getResCenterList(String userId, String deviceId){
        return getResCenterList(userId, deviceId, ResCenterInboxDataPassFactory.defaultResCenterInboxDataPass());
    }

    public Observable<Response<TkpdResponse>> getResCenterList(String userId, String deviceId,
                                                               ResCenterInboxDataPass resCenterInboxDataPass){
        return inboxResCenterService.getApi().getResCenter(AuthUtil.generateParams(userId, deviceId, paramGetInbox(resCenterInboxDataPass)));
    }

    public interface InboxResCenterListener extends CommonListener {
        void onSuccess(ResCenterInboxData resCenterInboxData);
    }


    // [START]  This code brought for demo only
    private static final String PARAM_AS = "as";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_SORT_TYPE = "sort_type";
    private static final String PARAM_STATUS = "status";
    private static final String PARAM_UNREAD = "unread";

    public static Map<String, String> paramGetInbox(@NonNull ResCenterInboxDataPass dataPass) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_AS, String.valueOf(dataPass.getRequestAs()));
        params.put(PARAM_PAGE, String.valueOf(dataPass.getRequestPage()));
        params.put(PARAM_SORT_TYPE, String.valueOf(dataPass.getSortType()));
        params.put(PARAM_STATUS, String.valueOf(dataPass.getFilterStatus()));
        params.put(PARAM_UNREAD, String.valueOf(dataPass.getReadUnreadStatus()));
        return params;
    }
    // [END] This code brought for demo only
}
