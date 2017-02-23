package com.tokopedia.core.analytics.container;

import android.content.Context;

import com.moe.pushlibrary.MoEHelper;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.MainApplication;

import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Herdi_WORK on 21.02.17.
 */

public class MoEngageContainer implements IMoengageContainer {

    private Context context;
    private CompositeSubscription subscription;

    private MoEngageContainer(Context ctx) {
        context = ctx;
        subscription = new CompositeSubscription();
    }

    public static MoEngageContainer getMoEngageContainer(Context context) {
        return new MoEngageContainer(context);
    }

    @Override
    public void isExistingUser(boolean bol) {
        Single<Boolean> isExistingUser = Single.just(bol);

        executor(isExistingUser, new SingleSubscriber<Boolean>() {
            @Override
            public void onSuccess(Boolean value) {
                CommonUtils.dumper("MoEngage check is existing user");
                MoEHelper.getInstance(context).setExistingUser(value);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

    @Override
    public void initialize() {
        Single<Void> initTask = Single.create(new Single.OnSubscribe<Void>() {
            @Override
            public void call(SingleSubscriber<? super Void> singleSubscriber) {
                MoEHelper.getInstance(context).autoIntegrate(MainApplication.getInstance());
            }
        });

        executor(initTask, new SingleSubscriber<Void>() {
                    @Override
                    public void onSuccess(Void value) {
                        CommonUtils.dumper("MoEngage Successs");
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }
                }
        );
    }

    private void executor(Single single, SingleSubscriber subscriber) {
        subscription.add(single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber));
    }
}
