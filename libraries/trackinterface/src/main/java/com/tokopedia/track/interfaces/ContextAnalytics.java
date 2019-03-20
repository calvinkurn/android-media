package com.tokopedia.track.interfaces;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.support.annotation.Nullable;

import org.json.JSONObject;

import java.util.Map;

public abstract class ContextAnalytics implements Analytics {
    protected final Application context;

    public ContextAnalytics(Context context) {
        this.context = getApplication(context);
    }

    public Application getContext() {
        return context;
    }

    private Application getApplication(Context context) {
        Application application = null;
        if (context instanceof Activity) {
            application = ((Activity) context).getApplication();
        } else if (context instanceof Service) {
            application = ((Service) context).getApplication();
        } else if (context instanceof Application) {
            application = (Application) context;
        }

        return application;
    }

    public String TAG() {
        return "TEST";
    }

    public void initialize() {

    }

    public void setMoEUserAttributesLogin(@Nullable String userId,@Nullable String name,@Nullable String email,
                                          @Nullable String phoneNumber, boolean isGoldMerchant,
                                          @Nullable String shopName,@Nullable  String shopId, boolean hasShop,
                                          @Nullable String loginMethod) {}

    /**
     * used by moenagage
     * @param medium
     */
    public void sendRegistrationStartEvent(String medium){}

    public void sendMoengageRegisterEvent(String fullName, String mobileNo){}
    public void sendAppsflyerRegisterEvent(String userId, String method){}

    public void setUserData(Map<String, Object> value, final String source){}

    public void sendTrackEvent(Map<String, Object> data, final String eventName){}

    public void logoutEvent(){}

    public void setPushPreference(boolean status) {}

    public void sendTrackEvent(String eventName, Map<String, Object> eventValue) {}

    public void getAdsID(final AFAdsIDCallback callback) {}

    public void sendDeeplinkData(Activity activity) {}

    public String getUniqueId(){return null;}

    public void updateFCMToken(String fcmToken){}

    public String getClientIDString(){return null;}

    public void pushUserId(String userId){}

    public void eventOnline(String uid) {}
    public void eventError(String screenName, String errorDesc) {}
    public void clearEnhanceEcommerce() {}
    public void pushEvent(String eventName, Map<String, Object> values){}


}
