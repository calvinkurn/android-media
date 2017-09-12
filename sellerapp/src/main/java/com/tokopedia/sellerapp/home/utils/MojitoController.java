package com.tokopedia.sellerapp.home.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.tkpd.library.utils.network.BaseNetworkController;
import com.tkpd.library.utils.network.CommonListener;
import com.tokopedia.core.common.ticker.api.TickerApiSeller;
import com.tokopedia.core.common.ticker.model.Ticker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.sellerapp.SellerMainApplication;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by normansyahputa on 5/23/17.
 */

public class MojitoController extends BaseNetworkController {
    private static final String TAG = "MojitoController";
    private TickerApiSeller tickerApiSeller;

    public MojitoController(Context context, TickerApiSeller tickerApiSeller, Gson gson) {
        super(context, gson);
        this.tickerApiSeller = tickerApiSeller;
    }

    public Observable<Response<Ticker>> getTicker() {

        return tickerApiSeller.getTickers(
                SessionHandler.getLoginID(SellerMainApplication.getAppContext()),
                TickerApiSeller.size,
                TickerApiSeller.FILTER_SELLERAPP_ANDROID_DEVICE);
    }

    public void getTicker(final ListenerGetTicker listenerGetTicker) {
        if (listenerGetTicker == null)
            return;

        getTicker().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<Ticker>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getData : onError : " + e.toString());
                        listenerGetTicker.onError(e);
                    }

                    @Override
                    public void onNext(Response<Ticker> tickerResponse) {
                        if (tickerResponse.isSuccessful()) {
                            Ticker ticker = tickerResponse.body();
                            Ticker.Data data = null;
                            if (ticker != null) {
                                data = ticker.getData();
                            }
                            if (data != null) {
                                listenerGetTicker.onSuccess(data.getTickers());
                            } else {
                                listenerGetTicker.onError(new Throwable());
                            }
                        } else {
                            listenerGetTicker.onError(new Throwable());
                        }
                    }
                });
    }

    public interface ListenerGetTicker extends CommonListener {
        void onSuccess(Ticker.Tickers[] tickers);
    }
}
