package com.tokopedia.core.analytics.container;

import android.content.Context;

import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;
import com.moengage.core.Logger;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.model.CustomerWrapper;
import com.tokopedia.core.app.MainApplication;

import org.json.JSONObject;

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
    public void isExistingUser(final boolean bol) {
        Single<Boolean> isExistingUser = Single.just(bol);

        executor(isExistingUser, new SingleSubscriber<Boolean>() {
            @Override
            public void onSuccess(Boolean value) {
                CommonUtils.dumper("MoEngage check is existing user "+bol);
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
                MoEHelper.getInstance(context).setLogLevel(Logger.VERBOSE);
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

    @Override
    public void setUserProfile(CustomerWrapper customerWrapper) {
        Single<CustomerWrapper> isExistingUser = Single.just(customerWrapper);

        executor(isExistingUser, new SingleSubscriber<CustomerWrapper>() {
            @Override
            public void onSuccess(CustomerWrapper value) {
                CommonUtils.dumper("MoEngage check is existing user "+value.getFullName()+" "+value.getEmailAddress()+" "+value.getCustomerId());
                MoEHelper helper = MoEHelper.getInstance(context);
                helper.setFullName(value.getFullName());
                helper.setUniqueId(value.getCustomerId());
                helper.setEmail(value.getEmailAddress());
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

    @Override
    public void sendEvent(JSONObject data, final String eventName) {
        Single<JSONObject> isExistingUser = Single.just(data);

        executor(isExistingUser, new SingleSubscriber<JSONObject>() {
            @Override
            public void onSuccess(JSONObject value) {
                CommonUtils.dumper("MoEngage send event "+value.toString());
                MoEHelper.getInstance(context).trackEvent(eventName, value);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

    private void executor(Single single, SingleSubscriber subscriber) {
        subscription.add(single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber));
    }
}
