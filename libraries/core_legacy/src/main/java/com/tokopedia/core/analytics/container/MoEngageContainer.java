package com.tokopedia.core.analytics.container;

import android.app.Application;
import android.text.TextUtils;

import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;
import com.moengage.core.MoEngage;
import com.tkpd.library.utils.legacy.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.model.CustomerWrapper;

import org.json.JSONObject;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Herdi_WORK on 21.02.17.
 */
@Deprecated
public class MoEngageContainer implements IMoengageContainer {
    private static final String DATE_FORMAT_1 = "yyyy-MM-dd";

    private Application context;
    private CompositeSubscription subscription;

    private MoEngageContainer(Application ctx) {
        context = ctx;
        subscription = new CompositeSubscription();
    }

    public static MoEngageContainer getMoEngageContainer(Application context) {
        return new MoEngageContainer(context);
    }

    @Override
    public void isExistingUser(final boolean bol) {
        Single<Boolean> isExistingUser = Single.just(bol);

        executor(isExistingUser, new SingleSubscriber<Boolean>() {
            @Override
            public void onSuccess(Boolean value) {
                CommonUtils.dumper("MoEngage check is existing user " + value);
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

        /*
          Mandatory to set small/Large notification icon while initialising sdk
          */
        MoEngage moEngage =
                new MoEngage.Builder(context,
                        context.getResources().getString(R.string.key_moengage))
                        .setNotificationSmallIcon(R.drawable.ic_status_bar_notif_customerapp)
                        .setNotificationLargeIcon(R.drawable.ic_big_notif_customerapp)
                        .build();
        MoEngage.initialise(moEngage);

    }

    @Override
    public void setUserProfile(CustomerWrapper customerWrapper) {
        Single<CustomerWrapper> isExistingUser = Single.just(customerWrapper);

        executor(isExistingUser, new SingleSubscriber<CustomerWrapper>() {
            @Override
            public void onSuccess(CustomerWrapper value) {

                CommonUtils.dumper("MoEngage check user " + value.getCustomerId());

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
        CommonUtils.dumper("MoEngage check user " + fullName);
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
                CommonUtils.dumper("MoEDispatcher " + value.toString() + " " + source);
                MoEHelper helper = MoEHelper.getInstance(context);

                if (checkNull(value.getFullName()))
                    helper.setFullName(value.getFullName());

                if (checkNull(value.getFirstName()))
                    helper.setFirstName(value.getFirstName());

                if (checkNull(value.getCustomerId()))
                    helper.setUniqueId(value.getCustomerId());

                if (checkNull(value.getEmailAddress()))
                    helper.setEmail(value.getEmailAddress());

                if (checkNull(value.getPhoneNumber()))
                    helper.setNumber(value.getPhoneNumber());

                if (!TextUtils.isEmpty(value.getDateOfBirth())) {
                    helper.setBirthDate(value.getDateOfBirth());
                }

                if (checkNull(value.isGoldMerchant()))
                    helper.setUserAttribute(AppEventTracking.MOENGAGE.IS_GOLD_MERCHANT, String.valueOf(value.isGoldMerchant()));

                if (checkNull(value.getShopId()))
                    helper.setUserAttribute(AppEventTracking.MOENGAGE.SHOP_ID, value.getShopId());

                if (checkNull(value.getShopName()))
                    helper.setUserAttribute(AppEventTracking.MOENGAGE.SHOP_NAME, value.getShopName());

                if (checkNull(value.getTotalItemSold()))
                    helper.setUserAttribute(AppEventTracking.MOENGAGE.TOTAL_SOLD_ITEM, value.getTotalItemSold());

                if (checkNull(value.getTopAdsAmt()))
                    helper.setUserAttribute(AppEventTracking.MOENGAGE.TOPADS_AMT, value.getTopAdsAmt());

                if (checkNull(value.isHasPurchasedMarketplace()))
                    helper.setUserAttribute(AppEventTracking.MOENGAGE.HAS_PURCHASED_MARKETPLACE, value.isHasPurchasedMarketplace());

                if (checkNull(value.getLastTransactionDate()))
                    helper.setUserAttribute(AppEventTracking.MOENGAGE.LAST_TRANSACT_DATE, value.getLastTransactionDate());

                if (checkNull(value.getShopScore()))
                    helper.setUserAttribute(AppEventTracking.MOENGAGE.SHOP_SCORE, value.getShopScore());

                if (checkNull(value.getGender()))
                    helper.setGender(value.getGender().equals("1") ? "male" : "female");
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

    @Override
    public void setPushPreference(boolean status) {
        Observable.just(status)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .map(pushStatus -> {
                MoEHelper.getInstance(context).setUserAttribute("push_preference", pushStatus);
                return true;
            })
            .subscribe(new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(Boolean aBoolean) {
                    // no-op
                }
            });
    }

    private boolean checkNull(Object o){
        if( o instanceof String)
            return !TextUtils.isEmpty((String)o);
        else if (o instanceof Boolean)
            return o != null;
        else
            return o != null;
    }

    @Override
    public void logoutEvent() {
        if(context != null) {
            MoEHelper.getInstance(context.getApplicationContext()).logoutUser();
        }
    }

    private void executor(Single single, SingleSubscriber subscriber) {
        subscription.add(single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber));
    }
}
