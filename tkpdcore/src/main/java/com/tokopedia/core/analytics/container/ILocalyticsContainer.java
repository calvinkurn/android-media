package com.tokopedia.core.analytics.container;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.localytics.android.Customer;
import com.localytics.android.Localytics;
import com.localytics.android.MessagingListener;
import com.tokopedia.core.analytics.model.CustomerWrapper;
import com.tokopedia.core.analytics.model.Product;

import java.util.Date;
import java.util.Map;

/**
 * @author by alvarisi on 10/27/16.
 */

public interface ILocalyticsContainer {
    void register(Application appl, String senderId);

    void register(Application appl, String senderId, MessagingListener msgListener);

    LocalyticsContainer tagScreen(String screenName);

    void tagEvent(String eventName);

    LocalyticsContainer tagAddedToCart(String productName, String productID,
                                       String productCategory, long price, Map<String, String> values);

    LocalyticsContainer tagEvent(String eventName, Map<String, String> attributes);

    LocalyticsContainer tageEventandInApp(String screenName);

    LocalyticsContainer tagEvent(String eventName, Map<String, String> attributes, long customerValueIncrease);

    void tagPurchasedEvent(Product product);

    void incrementProfileAttribute(String attrName, long incrementVal, Localytics.ProfileScope scope);

    void decrementProfileAttribute(String attrName, long decrementVal, Localytics.ProfileScope scope);

    void tagUserAttributes(String userId, String userFullName, String email);

    void sendNotificationCallback(Intent intent);

    void sendReceiveNotification(Bundle data);

    void tagEventWithAttribute(String eventName, String custId, String[] identifier, String[] profileAttr);

    void setProfileAttribute(String attributeName, long attributeValue, Localytics.ProfileScope scope);

    void setProfileAttribute(String attributeName, long attributeValue);

    void setProfileAttribute(String attributeName, long[] attributeValue, Localytics.ProfileScope scope);

    void setProfileAttribute(String attributeName, long[] attributeValue);

    void setProfileAttribute(String attributeName, String attributeValue, Localytics.ProfileScope scope);

    void setProfileAttribute(String attributeName, String attributeValue);

    void setProfileAttribute(String attributeName, String[] attributeValue, Localytics.ProfileScope scope);

    void setProfileAttribute(String attributeName, String[] attributeValue);

    void setProfileAttribute(String attributeName, Date attributeValue, Localytics.ProfileScope scope);

    void setProfileAttribute(String attributeName, Date attributeValue);

    void setProfileAttribute(String attributeName, Date[] attributeValue, Localytics.ProfileScope scope);

    void setProfileAttribute(String attributeName, Date[] attributeValue);

    void triggerInAppMessage(String triggerName);

    void handleTestMode(Intent intent);

    void setDebugging(boolean state);

    void deleteProfileAttribute(String attributeName);

    void sendEventRegister(CustomerWrapper customerWrapper);

    void sendEventLoggedOut(Map<String, String> attributes);

    void sendEventSearchProduct(String queryText, String contentType, Long resultCount,
                                Map<String, String> attributes);

    void sendEventProductView(String contentName, String contentId, String contentType,
                              Map<String, String> attributes);

    void sendLoginSuccessfull(CustomerWrapper customerWrapper);

    void sendEventRegister(Customer build, String label, Map<String, String> attributesLogin);

    void sendEventLogin(Customer build, String label, Map<String, String> attributesLogin);

    void setNotificationsDisabled(boolean value);
}
