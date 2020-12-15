package com.tokopedia.tkpd;

import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.device.info.DeviceInfo;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.tkpd.BuildConfig;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;
import com.tokopedia.utils.permission.SlicePermission;

import static com.tokopedia.utils.permission.SlicePermission.RECHARGE_SLICE_AUTHORITY;
import static com.tokopedia.utils.permission.SlicePermission.TRAVEL_SLICE_AUTHORITY;

/**
 * Created by ricoharisin on 11/11/16.
 */

public class ConsumerMainApplication extends com.tokopedia.tkpd.app.ConsumerMainApplication {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    private native byte[] bytesFromJNI();

    @Override
    public void initConfigValues() {
        GlobalConfig.PACKAGE_APPLICATION = "com.tokopedia.tkpd";
        setVersionCode();
        setVersionName();

        GlobalConfig.DEBUG = BuildConfig.DEBUG;
        GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
        com.tokopedia.config.GlobalConfig.DEBUG = BuildConfig.DEBUG;
        com.tokopedia.config.GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
        com.tokopedia.config.GlobalConfig.IS_PREINSTALL = BuildConfig.IS_PREINSTALL;
        com.tokopedia.config.GlobalConfig.PREINSTALL_NAME = BuildConfig.PREINSTALL_NAME;
        com.tokopedia.config.GlobalConfig.PREINSTALL_DESC = BuildConfig.PREINSTALL_DESC;
        com.tokopedia.config.GlobalConfig.PREINSTALL_SITE = BuildConfig.PREINSTALL_SITE;
        com.tokopedia.config.GlobalConfig.APPLICATION_ID = BuildConfig.APPLICATION_ID;
        com.tokopedia.config.GlobalConfig.ENABLE_DEBUG_TRACE = BuildConfig.ENABLE_DEBUG_TRACE;
        com.tokopedia.config.GlobalConfig.HOME_ACTIVITY_CLASS_NAME = MainParentActivity.class.getName();
        com.tokopedia.config.GlobalConfig.DEEPLINK_HANDLER_ACTIVITY_CLASS_NAME = DeeplinkHandlerActivity.class.getName();
        com.tokopedia.config.GlobalConfig.DEEPLINK_ACTIVITY_CLASS_NAME = DeepLinkActivity.class.getName();
        if (com.tokopedia.config.GlobalConfig.DEBUG) {
            com.tokopedia.config.GlobalConfig.DEVICE_ID = DeviceInfo.getAndroidId(this);
        }
        if (BuildConfig.DEBUG_TRACE_NAME != null) {
            com.tokopedia.config.GlobalConfig.DEBUG_TRACE_NAME = BuildConfig.DEBUG_TRACE_NAME.split(",");
        }
        generateConsumerAppNetworkKeys();
    }

    public String getOriginalPackageApp(){
        return new String(new char[]{
                99, 111, 109, 46, 116, 111, 107, 111, 112, 101,
                100, 105, 97, 46
        }) + new String(new char[]{
                116, 107, 112, 100
        });
    }

    @Override
    public String versionName() {
        return BuildConfig.VERSION_NAME;
    }

    @Override
    public int versionCode() {
        return BuildConfig.VERSION_CODE;
    }

    @Override
    protected byte[] getJniBytes() {
        return bytesFromJNI();
    }

    @Override
    public void generateConsumerAppNetworkKeys() {
        AuthUtil.KEY.KEY_CREDIT_CARD_VAULT = ConsumerAppNetworkKeys.CREDIT_CARD_VAULT_AUTH_KEY;
        AuthUtil.KEY.ZEUS_WHITELIST = ConsumerAppNetworkKeys.ZEUS_WHITELIST;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        SlicePermission.initPermission(this, RECHARGE_SLICE_AUTHORITY);
        SlicePermission.initPermission(this, TRAVEL_SLICE_AUTHORITY);
    }
}
