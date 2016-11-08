package com.tokopedia.core.addtocart.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.tokopedia.core.addtocart.interactor.AddToCartNetInteractor;
import com.tokopedia.core.addtocart.interactor.AddToCartNetInteractorImpl;
import com.tokopedia.core.addtocart.model.OrderData;
import com.tokopedia.core.addtocart.utils.NetParamUtil;

import rx.schedulers.Schedulers;

/**
 * Created by Angga.Prasetiyo on 21/03/2016.
 */
public class ATCIntentService extends IntentService {
    private static final String TAG = ATCIntentService.class.getSimpleName();

    public static final String EXTRA_ACTION = "EXTRA_ACTION";
    public static final String EXTRA_ORDER_DATA = "EXTRA_ORDER_DATA";
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static final String EXTRA_RECEIVER = "EXTRA_RECEIVER";
    public static final int RESULT_ADD_TO_CART_RUNNING = 1;
    public static final int RESULT_ADD_TO_CART_ERROR = 2;
    public static final int RESULT_ADD_TO_CART_SUCCESS = 3;
    public static final int RESULT_ADD_TO_CART_TIMEOUT = 4;
    public static final int RESULT_ADD_TO_CART_NO_CONNECTION = 5;

    public static final int ACTION_ADD_TO_CART = 1;

    private AddToCartNetInteractor interactor;


    public ATCIntentService() {
        super(ATCIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (interactor == null) interactor = new AddToCartNetInteractorImpl();
        switch (intent.getIntExtra(EXTRA_ACTION, 0)) {
            case ACTION_ADD_TO_CART:
                addToCart(intent);
                break;
        }
    }

    private void addToCart(Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RECEIVER);
        final OrderData orderData = intent.getParcelableExtra(EXTRA_ORDER_DATA);
        receiver.send(RESULT_ADD_TO_CART_RUNNING, null);
        interactor.addToCart(getBaseContext(), Schedulers.immediate(),
                NetParamUtil.paramAddToCart(orderData), new AddToCartNetInteractor.OnAddToCart() {
                    @Override
                    public void onSuccess(String message) {
                        Bundle bundle = new Bundle();
                        bundle.putString(EXTRA_MESSAGE, message);
                        receiver.send(RESULT_ADD_TO_CART_SUCCESS, bundle);
                        stopSelf();
                    }

                    @Override
                    public void onFailure(String message) {
                        Bundle bundle = new Bundle();
                        bundle.putString(EXTRA_MESSAGE, message);
                        receiver.send(RESULT_ADD_TO_CART_ERROR, bundle);
                        stopSelf();
                    }

                    @Override
                    public void onTimeout() {
                        receiver.send(RESULT_ADD_TO_CART_TIMEOUT, null);
                        stopSelf();
                    }

                    @Override
                    public void onNoConnection() {
                        receiver.send(RESULT_ADD_TO_CART_NO_CONNECTION, null);
                        stopSelf();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (interactor != null) interactor.unSubscribeObservable();
        interactor = null;
    }
}
