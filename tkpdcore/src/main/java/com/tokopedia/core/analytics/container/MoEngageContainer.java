package com.tokopedia.core.analytics.container;

import android.content.Context;
import android.text.TextUtils;

import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;
import com.moengage.core.Logger;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
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
    private static final String DATE_FORMAT_1 = "yyyy-MM-dd";

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
                CommonUtils.dumper("MoEngage check is existing user "+value);
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
        MoEHelper.getInstance(context).autoIntegrate(MainApplication.getInstance());
        MoEHelper.getInstance(context).setLogLevel(Logger.VERBOSE);
        /*Single<Void> initTask = Single.create(new Single.OnSubscribe<Void>() {
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
        );*/
    }

    @Override
    public void setUserProfile(CustomerWrapper customerWrapper) {
        Single<CustomerWrapper> isExistingUser = Single.just(customerWrapper);

        executor(isExistingUser, new SingleSubscriber<CustomerWrapper>() {
            @Override
            public void onSuccess(CustomerWrapper value) {

                CommonUtils.dumper("MoEngage check user "+value.getCustomerId());

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
                MoEHelper.getInstance(context).trackEvent(eventName, value);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

    @Override
    public void sendRegistrationStartEvent(String medium) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.MEDIUM, medium);
        sendEvent(
                new PayloadBuilder()
                    .putAttrString(AppEventTracking.MOENGAGE.MEDIUM, medium)
                    .build()
                , AppEventTracking.EventMoEngage.REG_START
        );
    }

    @Override
    public void sendRegisterEvent(String fullName, String mobileNo) {
        CommonUtils.dumper("MoEngage check user "+fullName);
        sendEvent(
                new PayloadBuilder()
                    .putAttrString(AppEventTracking.MOENGAGE.NAME, fullName)
                    .putAttrString(AppEventTracking.MOENGAGE.MOBILE_NUM, mobileNo)
                    .build()
                , AppEventTracking.EventMoEngage.REG_COMPL
        );
    }

    @Override
    public void setUserData(CustomerWrapper customerWrapper, final String source) {
        Single<CustomerWrapper> isExistingUser = Single.just(customerWrapper);

        executor(isExistingUser, new SingleSubscriber<CustomerWrapper>() {
            @Override
            public void onSuccess(CustomerWrapper value) {
                CommonUtils.dumper("MoEDispatcher "+value.toString()+" "+source);
                MoEHelper helper = MoEHelper.getInstance(context);

                if(checkNull(value.getFullName()))
                helper.setFullName(value.getFullName());

                if(checkNull(value.getFirstName()))
                helper.setFirstName(value.getFirstName());

                if(checkNull(value.getCustomerId()))
                helper.setUniqueId(value.getCustomerId());

                if(checkNull(value.getEmailAddress()))
                helper.setEmail(value.getEmailAddress());

                if(checkNull(value.getPhoneNumber()))
                helper.setNumber(value.getPhoneNumber());

                if(!TextUtils.isEmpty(value.getDateOfBirth())) {
                    helper.setBirthDate(value.getDateOfBirth());
                }

                if(checkNull(value.isGoldMerchant()))
                helper.setUserAttribute(AppEventTracking.MOENGAGE.IS_GOLD_MERCHANT, String.valueOf(value.isGoldMerchant()));

                if(checkNull(value.isSeller()))
                helper.setUserAttribute(AppEventTracking.MOENGAGE.IS_SELLER, String.valueOf(value.isSeller()));

                if(checkNull(value.getShopId()))
                helper.setUserAttribute(AppEventTracking.MOENGAGE.SHOP_ID, value.getShopId());

                if(checkNull(value.getShopName()))
                helper.setUserAttribute(AppEventTracking.MOENGAGE.SHOP_NAME, value.getShopName());

                if(checkNull(value.getTotalItemSold()))
                helper.setUserAttribute(AppEventTracking.MOENGAGE.TOTAL_SOLD_ITEM, value.getTotalItemSold());

                if(checkNull(value.getRegDate()))
                helper.setUserAttribute(AppEventTracking.MOENGAGE.REG_DATE, value.getRegDate());

                if(checkNull(value.getDateShopCreated()))
                helper.setUserAttribute(AppEventTracking.MOENGAGE.DATE_SHOP_CREATED, value.getDateShopCreated());

                if(checkNull(value.getShopLocation()))
                helper.setUserAttribute(AppEventTracking.MOENGAGE.SHOP_LOCATION, value.getShopLocation());

                if(checkNull(value.getTokocashAmt()))
                helper.setUserAttribute(AppEventTracking.MOENGAGE.TOKOCASH_AMT, value.getTokocashAmt());

                if(checkNull(value.getSaldoAmt()))
                helper.setUserAttribute(AppEventTracking.MOENGAGE.SALDO_AMT, value.getSaldoAmt());

                if(checkNull(value.getTopAdsAmt()))
                helper.setUserAttribute(AppEventTracking.MOENGAGE.TOPADS_AMT, value.getTopAdsAmt());

                if(checkNull(value.isTopadsUser()))
                helper.setUserAttribute(AppEventTracking.MOENGAGE.TOPADS_USER, value.isTopadsUser());

                if(checkNull(value.isHasPurchasedTiket()))
                helper.setUserAttribute(AppEventTracking.MOENGAGE.HAS_PURCHASED_TICKET, value.isHasPurchasedTiket());

                if(checkNull(value.isHasPurchasedMarketplace()))
                helper.setUserAttribute(AppEventTracking.MOENGAGE.HAS_PURCHASED_MARKETPLACE, value.isHasPurchasedMarketplace());

                if(checkNull(value.isHasPurchasedDigital()))
                helper.setUserAttribute(AppEventTracking.MOENGAGE.HAS_PURCHASED_DIGITAL, value.isHasPurchasedDigital());

                if(checkNull(value.getLastTransactionDate()))
                helper.setUserAttribute(AppEventTracking.MOENGAGE.LAST_TRANSACT_DATE, value.getLastTransactionDate());

                if(checkNull(value.getTotalActiveProduct()))
                helper.setUserAttribute(AppEventTracking.MOENGAGE.TOTAL_ACTIVE_PRODUCT, value.getTotalActiveProduct());

                if(checkNull(value.getShopScore()))
                helper.setUserAttribute(AppEventTracking.MOENGAGE.SHOP_SCORE, value.getShopScore());

                if(checkNull(value.getGender()))
                    helper.setGender(value.getGender().equals("1") ? "male" : "female");
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

    private boolean checkNull(Object o){
        if( o instanceof String)
            return !TextUtils.isEmpty((String)o);
        else if (o instanceof Boolean)
            return o != null;
        else
            return o !=null;
    }

    @Override
    public void logoutEvent() {
        MoEHelper.getInstance(context).logoutUser();
    }

    private void executor(Single single, SingleSubscriber subscriber) {
        subscription.add(single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber));
    }
}
