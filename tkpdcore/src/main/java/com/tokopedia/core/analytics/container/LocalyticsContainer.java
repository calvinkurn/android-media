package com.tokopedia.core.analytics.container;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.localytics.android.Customer;
import com.localytics.android.Localytics;
import com.localytics.android.LocalyticsActivityLifecycleCallbacks;
import com.localytics.android.MessagingListener;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.model.CustomerWrapper;
import com.tokopedia.core.analytics.model.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author by Nanda J.A on 6/14/2015.
 *         Modified by Alvarisi
 */
public class LocalyticsContainer implements ILocalyticsContainer {

    private Context context;
    private CompositeSubscription subscription;

    private LocalyticsContainer(Context context) {
        this.context = context;
        this.subscription = new CompositeSubscription();
    }

    public static LocalyticsContainer newLocalyticsContainer(Context context) {
        return new LocalyticsContainer(context);
    }

    private class TagEventModel {
        String eventName;
        public Map<String, String> attributes;
        long customerValueIncrease;

        TagEventModel(String eventName) {
            this.eventName = eventName;
        }

        TagEventModel(String eventName, Map<String, String> attributes, long customerValueIncrease) {
            this.eventName = eventName;
            this.attributes = attributes;
            this.customerValueIncrease = customerValueIncrease;
        }
    }

    private class SetProfileModel {
        String attrName;
        Object attributeValue;
        Localytics.ProfileScope scope;

        SetProfileModel(String attrName, Object attributeValue, Localytics.ProfileScope scope) {
            this.attrName = attrName;
            this.attributeValue = attributeValue;
            this.scope = scope;
        }

    }

    private void doRegister(String senderId) {
        Localytics.registerPush(senderId);
    }

    private void setPushDisabled(boolean state) {
        Localytics.setNotificationsDisabled(state);
    }

    @Override
    public void register(Application appl, String senderId) {
        appl.registerActivityLifecycleCallbacks(new LocalyticsActivityLifecycleCallbacks(context));
        doRegister(senderId);
        setPushDisabled(false);
    }

    @Override
    public void register(Application appl, String senderId, MessagingListener msgListener) {
        appl.registerActivityLifecycleCallbacks(new LocalyticsActivityLifecycleCallbacks(context));
        Localytics.setMessagingListener(msgListener);
        doRegister(senderId);
        setPushDisabled(false);
    }

    @Override
    public LocalyticsContainer tagScreen(String screenName) {
        tagScreenTask(screenName);
        return this;
    }

    @Override
    public void tagEvent(String eventName) {
        TagEventModel params = new TagEventModel(eventName);
        tagEventTask(params);
    }

    @Override
    public LocalyticsContainer tagAddedToCart(final String productName, final String productID, final String productCategory, final long price, final Map<String, String> values) {
        Observable<Boolean> observable = Observable.just(true)
                .map(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean) {
                        Localytics.tagAddedToCart(productName, productID, productCategory, price, values);
                        return null;
                    }
                });
        executeTagEvent(observable, null);
        return this;
    }

    @Override
    public LocalyticsContainer tagEvent(String eventName, Map<String, String> attributes) {
        TagEventModel params = new TagEventModel(eventName, attributes, 0L);
        tagEventTask(params);
        return this;
    }

    @Override
    public LocalyticsContainer tageEventandInApp(String screenName) {
        tagEvent(screenName);
        triggerInAppMessage(screenName);
        return this;
    }

    @Override
    public LocalyticsContainer tagEvent(String eventName, Map<String, String> attributes, long customerValueIncrease) {
        TagEventModel params = new TagEventModel(eventName, attributes, customerValueIncrease);
        tagEventTask(params);
        return this;
    }

    @Override
    public void tagPurchasedEvent(Product product) {
        Observable<Boolean> observable = Observable.just(product)
                .map(new Func1<Product, Boolean>() {
                    @Override
                    public Boolean call(Product purchasedProduct) {
                        Localytics.tagPurchased(purchasedProduct.getName(), purchasedProduct.getId(), purchasedProduct.getType(), Long.parseLong(purchasedProduct.getPrice()), purchasedProduct.getAttr());
                        return null;
                    }
                });
        executeTagEvent(observable, null);
    }

    @Override
    public void incrementProfileAttribute(String attrName, long incrementVal, Localytics.ProfileScope scope) {
        Localytics.incrementProfileAttribute(attrName, incrementVal, scope);
    }

    @Override
    public void decrementProfileAttribute(String attrName, long decrementVal, Localytics.ProfileScope scope) {
        Localytics.decrementProfileAttribute(attrName, decrementVal, scope);
    }

    @Override
    public void tagUserAttributes(final String userId, final String userFullName, final String email) {
        Observable<String> observable = Observable.just(true)
                .map(new Func1<Boolean, String>() {
                    @Override
                    public String call(Boolean aBoolean) {
                        Localytics.setCustomerId(userId);
                        Localytics.setCustomerEmail(email);
                        Localytics.setCustomerFullName(userFullName);
                        return userId + " " + email + " " + userFullName;
                    }
                });
        executeTagEvent(observable, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String profileData) {
                CommonUtils.dumper("localytics sends profile " + profileData);
            }
        });
    }

    @Override
    public void sendNotificationCallback(Intent intent) {
        Observable<Boolean> observable = Observable.just(intent)
                .map(new Func1<Intent, Boolean>() {
                    @Override
                    public Boolean call(Intent cbIntent) {
                        Intent newIntent = createLocalyticsIntent(cbIntent);
                        Localytics.handlePushNotificationOpened(newIntent);
                        return null;
                    }
                });
        executeTagEvent(observable, null);
    }

    @Override
    public void sendReceiveNotification(final Bundle data) {
        Observable<Boolean> observable = Observable.just(data)
                .map(new Func1<Bundle, Boolean>() {
                    @Override
                    public Boolean call(Bundle data) {
                        Localytics.tagPushReceivedEvent(data);
                        return null;
                    }
                });
        executeTagEvent(observable, null);
    }

    @Override
    public void tagEventWithAttribute(String eventName, String custId, String[] identifier, String[] profileAttr) {
        tagEvent(eventName);
        Localytics.setCustomerId(custId);
        Localytics.setIdentifier(identifier[0], identifier[1]);
        setProfileAttribute(profileAttr[0], profileAttr[1]);
    }

    @Override
    public void setProfileAttribute(String attributeName, long attributeValue, Localytics.ProfileScope scope) {
        SetProfileModel params = new SetProfileModel(attributeName, attributeValue, scope);
        setProfileAttributeTask(params);
    }

    @Override
    public void setProfileAttribute(String attributeName, long attributeValue) {
        SetProfileModel params = new SetProfileModel(attributeName, attributeValue, Localytics.ProfileScope.APPLICATION);
        setProfileAttributeTask(params);
    }

    @Override
    public void setProfileAttribute(String attributeName, long[] attributeValue, Localytics.ProfileScope scope) {
        SetProfileModel params = new SetProfileModel(attributeName, attributeValue, scope);
        setProfileAttributeTask(params);
    }

    @Override
    public void setProfileAttribute(String attributeName, long[] attributeValue) {
        SetProfileModel params = new SetProfileModel(attributeName, attributeValue, Localytics.ProfileScope.APPLICATION);
        setProfileAttributeTask(params);
    }

    @Override
    public void setProfileAttribute(String attributeName, String attributeValue, Localytics.ProfileScope scope) {
        SetProfileModel params = new SetProfileModel(attributeName, attributeValue, scope);
        setProfileAttributeTask(params);
    }

    @Override
    public void setProfileAttribute(String attributeName, String attributeValue) {
        SetProfileModel params = new SetProfileModel(attributeName, attributeValue, Localytics.ProfileScope.APPLICATION);
        setProfileAttributeTask(params);
    }

    @Override
    public void setProfileAttribute(String attributeName, String[] attributeValue, Localytics.ProfileScope scope) {
        SetProfileModel params = new SetProfileModel(attributeName, attributeValue, scope);
        setProfileAttributeTask(params);
    }

    @Override
    public void setProfileAttribute(String attributeName, String[] attributeValue) {
        SetProfileModel params = new SetProfileModel(attributeName, attributeValue, Localytics.ProfileScope.APPLICATION);
        setProfileAttributeTask(params);
    }

    @Override
    public void setProfileAttribute(String attributeName, Date attributeValue, Localytics.ProfileScope scope) {
        SetProfileModel params = new SetProfileModel(attributeName, attributeValue, scope);
        setProfileAttributeTask(params);
    }

    @Override
    public void setProfileAttribute(String attributeName, Date attributeValue) {
        SetProfileModel params = new SetProfileModel(attributeName, attributeValue, Localytics.ProfileScope.APPLICATION);
        setProfileAttributeTask(params);
    }

    @Override
    public void setProfileAttribute(String attributeName, Date[] attributeValue, Localytics.ProfileScope scope) {
        SetProfileModel params = new SetProfileModel(attributeName, attributeValue, scope);
        setProfileAttributeTask(params);
    }

    @Override
    public void setProfileAttribute(String attributeName, Date[] attributeValue) {
        SetProfileModel params = new SetProfileModel(attributeName, attributeValue, Localytics.ProfileScope.APPLICATION);
        setProfileAttributeTask(params);
    }

    @Override
    public void triggerInAppMessage(String triggerName) {
        Localytics.triggerInAppMessage(triggerName, null);
    }

    @Override
    public void handleTestMode(Intent intent) {
        Localytics.handleTestMode(intent);
    }

    @Override
    public void setDebugging(boolean state) {
        Localytics.setLoggingEnabled(state);
    }

    @Override
    public void deleteProfileAttribute(String attributeName) {
        Localytics.deleteProfileAttribute(attributeName, Localytics.ProfileScope.APPLICATION);
    }

    private void tagScreenTask(String screenName) {
        Observable<String> observable = Observable.just(screenName)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String sName) {
                        Localytics.openSession();
                        Localytics.tagScreen(sName);
                        Localytics.upload();
                        return sName;
                    }
                });

        executeTagEvent(observable, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String screenName) {
                CommonUtils.dumper("LOCALYTICS TAGGED SCREEN " + screenName);
            }
        });
    }

    private void tagEventTask(TagEventModel model) {
        Observable<String> observable = Observable.just(model)
                .map(new Func1<TagEventModel, String>() {
                    @Override
                    public String call(TagEventModel eventModel) {
                        if (eventModel.attributes != null && eventModel.customerValueIncrease != 0) {
                            CommonUtils.dumper("Localytics : tag event with clv " + eventModel.eventName);
                            Localytics.tagEvent(eventModel.eventName, eventModel.attributes, eventModel.customerValueIncrease);
                        } else if (eventModel.attributes != null) {
                            CommonUtils.dumper("Localytics : tag event with attribute " + eventModel.eventName);
                            Localytics.tagEvent(eventModel.eventName, eventModel.attributes);
                        } else {
                            CommonUtils.dumper("Localytics : tag event " + eventModel.eventName);
                            Localytics.tagEvent(eventModel.eventName);
                        }
                        return null;
                    }
                });
        executeTagEvent(observable, null);
    }

    private void setProfileAttributeTask(SetProfileModel model) {

        Observable<String> observable = Observable.just(model)
                .map(new Func1<SetProfileModel, String>() {
                    @Override
                    public String call(SetProfileModel pModel) {
                        if (pModel.attributeValue instanceof String) {
                            Localytics.setProfileAttribute(pModel.attrName, (String) pModel.attributeValue, pModel.scope);
                        } else if (pModel.attributeValue instanceof String[]) {
                            Localytics.setProfileAttribute(pModel.attrName, (String[]) pModel.attributeValue, pModel.scope);
                        } else if (pModel.attributeValue instanceof Date) {
                            Localytics.setProfileAttribute(pModel.attrName, (Date) pModel.attributeValue, pModel.scope);
                        } else if (pModel.attributeValue instanceof Date[]) {
                            Localytics.setProfileAttribute(pModel.attrName, (Date[]) pModel.attributeValue, pModel.scope);
                        } else if (pModel.attributeValue instanceof Long) {
                            Localytics.setProfileAttribute(pModel.attrName, (long) pModel.attributeValue, pModel.scope);
                        } else if (pModel.attributeValue instanceof Long[]) {
                            Localytics.setProfileAttribute(pModel.attrName, (long[]) pModel.attributeValue, pModel.scope);
                        }
                        return pModel.attrName;
                    }
                });

        executeTagEvent(observable, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String attrName) {
                CommonUtils.dumper("LOCALYTICS SET PROFILE " + attrName);
            }
        });
    }

    @Override
    public void sendEventRegister(CustomerWrapper customerWrapper) {
        Observable<Boolean> observable = Observable.just(customerWrapper)
                .map(new Func1<CustomerWrapper, Boolean>() {
                    @Override
                    public Boolean call(CustomerWrapper wrapper) {
                        Localytics.tagCustomerRegistered(new Customer
                                .Builder()
                                .setCustomerId(wrapper.getCustomerId())
                                .setFullName(wrapper.getFullName())
                                .setEmailAddress(wrapper.getEmailAddress())
                                .build(), wrapper.getMethod(), wrapper.getAttr());
                        return null;
                    }
                });
        executeTagEvent(observable, null);
    }

    @Override
    public void sendEventLoggedOut(Map<String, String> attributes) {
        Observable<Boolean> observable = Observable.just(attributes)
                .map(new Func1<Map<String, String>, Boolean>() {
                    @Override
                    public Boolean call(Map<String, String> attr) {
                        Localytics.tagCustomerLoggedOut(attr);
                        return null;
                    }
                });
        executeTagEvent(observable, null);
    }

    @Override
    public void sendEventSearchProduct(final String queryText, final String contentType, final Long resultCount, final Map<String, String> attributes) {
        Observable<Boolean> observable = Observable.just(true)
                .map(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean) {
                        Localytics.tagSearched(queryText, contentType, resultCount, attributes);
                        return null;
                    }
                });
        executeTagEvent(observable, null);
    }

    @Override
    public void sendEventProductView(final String contentName, final String contentId, final String contentType, final Map<String, String> attributes) {
        Observable<Boolean> observable = Observable.just(true)
                .map(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean) {
                        Localytics.tagContentViewed(contentName, contentId, contentType, attributes);
                        return null;
                    }
                });
        executeTagEvent(observable, null);
    }

    private void executeTagEvent(Observable observable, Subscriber subscriber) {
        this.subscription.add(observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber != null ? subscriber : new Subscriber() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                }));
    }

    @SuppressWarnings("unchecked")
    private Intent createLocalyticsIntent(Intent intent) {
        Bundle data = intent.getExtras();
        if (data != null && data.containsKey(AppEventTracking.LOCA.NOTIFICATION_BUNDLE)) {
            HashMap<String, String> map = new HashMap<>();
            map = (HashMap<String, String>) data.getSerializable(AppEventTracking.LOCA.NOTIFICATION_BUNDLE);
            try {
                JSONObject jsonObject = new JSONObject();
                if (map != null) {
                    for (String key : map.keySet()) {
                        jsonObject.put(key, map.get(key));
                    }
                }
                data.putString(AppEventTracking.LOCA.NOTIFICATION_BUNDLE, jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        intent.putExtras(data);
        return intent;
    }

    @Override
    public void sendLoginSuccessfull(CustomerWrapper customerWrapper) {
        Observable<Boolean> observable = Observable.just(customerWrapper)
                .map(new Func1<CustomerWrapper, Boolean>() {
                    @Override
                    public Boolean call(CustomerWrapper wrapper) {

                        Localytics.tagCustomerLoggedIn(new Customer
                                .Builder()
                                .setCustomerId(wrapper.getCustomerId())
                                .setFullName(wrapper.getFullName())
                                .setEmailAddress(wrapper.getEmailAddress())
                                .build(), wrapper.getMethod(), wrapper.getAttr());
                        return true;
                    }
                });
        executeTagEvent(observable, null);
    }

    @Override
    public void sendEventRegister(final Customer customer, final String methodName, final Map<String, String> attributes) {
        Observable<Boolean> observable = Observable.just(true)
                .map(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean) {
                        Localytics.tagCustomerRegistered(customer, methodName, attributes);
                        return null;
                    }
                });
        executeTagEvent(observable, null);
    }

    @Override
    public void sendEventLogin(final Customer customer, final String methodName, final Map<String, String> attributes) {
        Observable<Boolean> observable = Observable.just(true)
                .map(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean) {
                        Localytics.tagCustomerLoggedIn(customer, methodName, attributes);
                        return null;
                    }
                });
        executeTagEvent(observable, null);
    }

    @Override
    public void setNotificationsDisabled(boolean value) {
        Localytics.setNotificationsDisabled(value);
    }
}