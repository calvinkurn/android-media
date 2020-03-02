package com.tokopedia.tkpd.timber;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.logger.utils.DataLogConfig;
import com.tokopedia.logger.utils.TimberReportingTree;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.user.session.UserSession;

import timber.log.Timber;

/**
 * Wrap for timber library
 * Initialize this in application level
 * TimberWrapper.init(application);
 */
public class TimberWrapper {

    public static final String[] LOGENTRIES_TOKEN = new String[]{
            "08fcd148-14aa-4d89-ac67-4f70fefd2f37",
            "60664ea7-4d61-4df1-b39c-365dc647aced"
    };

    private static final String ANDROID_CUSTOMER_APP_LOG_CONFIG = "android_customer_app_log_config";
    
    public static void init(@NonNull Application application){
        initByConfig(application, new FirebaseRemoteConfigImpl(application));
    }

    public static void initByConfig(@NonNull Application application, @NonNull RemoteConfig remoteConfig){
        Timber.uprootAll();
//        boolean isDebug = GlobalConfig.DEBUG;
//        if (isDebug) {
//            Timber.plant(new TimberDebugTree());
//        } else {
//            String logConfigString = remoteConfig.getString(ANDROID_CUSTOMER_APP_LOG_CONFIG);
            String logConfigString = "{\"enabled\":true,\"appVersionMin\":0,\"tags\":[\"P1#DFM#100#offline\",\"P1#DFM_DEFERRED#10#online\",\"P1#NULL_CHECKER#1#online\",\"P1#REQUEST_ERROR_GQL#1#online\",\"P1#IMAGE_UPLOADER#100#online\",\"P1#IRIS#100#offline\",\"P1#IRIS_COLLECT#0#online\",\"P1#CACHE_API#1#online\",\"P2#FINGERPRINT#1#online\",\"P2#TOKEN_REFRESH#1#online\",\"P2#PLAY_SERVICE_ERROR#1#offline\",\"P2#IMAGE_TRACEROUTE#10#offline\",\"P2#PRODUCT_UPLOAD#1#online\",\"P2#PDP_CACHE#1#online\",\"P2#PDP_OPEN_DEEPLINK_ERROR#10#online\",\"P2#ACTION_SLICE_RECHARGE_IMPRESSION#100#online\",\"P2#ACTION_SLICE_CLICK_RECHARGE#100#online\"],\"priority\":[false,false,false]}";
            if (!TextUtils.isEmpty(logConfigString)) {
                DataLogConfig dataLogConfig = new Gson().fromJson(logConfigString, DataLogConfig.class);
                if(dataLogConfig != null && dataLogConfig.isEnabled() && GlobalConfig.VERSION_CODE >= dataLogConfig.getAppVersionMin() && dataLogConfig.getTags() != null) {
                    UserSession userSession = new UserSession(application);
                    TimberReportingTree timberReportingTree = new TimberReportingTree(dataLogConfig.getTags());
                    timberReportingTree.setUserId(userSession.getUserId());
                    timberReportingTree.setVersionName(GlobalConfig.VERSION_NAME);
                    timberReportingTree.setVersionCode(GlobalConfig.VERSION_CODE);
                    Timber.plant(timberReportingTree);
                }
            }
//        }
    }
}
