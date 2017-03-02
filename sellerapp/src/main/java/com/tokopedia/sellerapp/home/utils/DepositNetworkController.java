package com.tokopedia.sellerapp.home.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.tkpd.library.utils.network.BaseNetworkController;
import com.tkpd.library.utils.network.CommonListener;
import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.apiservices.transaction.DepositService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.sellerapp.home.model.deposit.DepositModel;

import java.util.HashMap;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by normansyahputa on 8/31/16.
 */

public class DepositNetworkController extends BaseNetworkController {
    private DepositService depositService;

    public DepositNetworkController(Context context, Gson gson, DepositService depositService) {
        super(context, gson);
        this.depositService = depositService;
    }

    public void getDeposit(String userId, String deviceId, final DepositListener depositListener){
        getDeposit(userId, deviceId)
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
                                depositListener.onError(e);
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> response) {
                                if (response.isSuccessful()) {
                                    if(!response.body().isError()) {
                                        String stringData = response.body().getStringData();
                                        Log.d("STUART", "getDeposit : onNext : "+stringData);
                                        DepositModel.Data depositModel = gson.fromJson(stringData, DepositModel.Data.class);
                                        depositListener.onSuccess(depositModel);
                                    }else {
                                        throw new MessageErrorException(response.body().getErrorMessages().get(0));
                                    }
                                } else {
                                    onResponseError(response.code(), depositListener);
                                }
                            }
                        }
                );
    }


    public Observable<Response<TkpdResponse>> getDeposit(String userId, String deviceId){
        return depositService.getApi().getDeposit(AuthUtil.generateParams(userId, deviceId, new HashMap<String, String>()));
    }

    public interface DepositListener extends CommonListener {
        void onSuccess(DepositModel.Data depositModel);
    }
}
