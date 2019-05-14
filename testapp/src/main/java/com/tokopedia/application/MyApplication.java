package com.tokopedia.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TkpdCacheApiGeneratedDatabaseHolder;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.applink.ApplinkDelegate;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.ApplinkUnsupported;
import com.tokopedia.cacheapi.domain.interactor.CacheApiWhiteListUseCase;
import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.cacheapi.util.CacheApiLoggingUtils;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.cpm.CharacterPerMinuteInterface;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.imagepickerapp.network.DataSource;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.tkpd.BuildConfig;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Response;

/**
 * Created by hendry on 25/06/18.
 */

public class MyApplication extends BaseMainApplication
        implements AbstractionRouter,
        NetworkRouter,
        ApplinkRouter, CharacterPerMinuteInterface {

    private static final long TEN_SECOND = TimeUnit.SECONDS.toSeconds(10);
    private static final long THIRTY_SECOND = TimeUnit.SECONDS.toSeconds(30);
    private static final long ONE_MINUTE = TimeUnit.MINUTES.toSeconds(1);
    private static final long FIVE_MINUTE = TimeUnit.MINUTES.toSeconds(5);
    private static final long FIFTEEN_MINUTE = TimeUnit.MINUTES.toSeconds(15);
    private static final long ONE_HOUR = TimeUnit.HOURS.toSeconds(1);
    private static final long THREE_HOURS = TimeUnit.HOURS.toSeconds(3);
    private static final long ONE_DAY = TimeUnit.HOURS.toSeconds(24);

    public static String GM_BASE_URL = "https://goldmerchant.tokopedia.com";

    public static final String GM_FEATURED_PRODUCT_URL = "/v1/mobile/featured_product/{shop_id}";

    @Override
    public void onCreate() {
        GlobalConfig.PACKAGE_APPLICATION = GlobalConfig.PACKAGE_SELLER_APP;
        GlobalConfig.DEBUG = BuildConfig.DEBUG;
        GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
        com.tokopedia.config.GlobalConfig.PACKAGE_APPLICATION = GlobalConfig.PACKAGE_SELLER_APP;
        com.tokopedia.config.GlobalConfig.DEBUG = BuildConfig.DEBUG;
        com.tokopedia.config.GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
        FlowManager.init(new FlowConfig.Builder(this)
                .build());
        FlowManager.initModule(TkpdCacheApiGeneratedDatabaseHolder.class);
        initCacheApi();

        GraphqlClient.init(this);
        NetworkClient.init(this);
        TrackApp.initTrackApp(this);
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }

    private void initCacheApi() {
        CacheApiLoggingUtils.setLogEnabled(GlobalConfig.isAllowDebuggingTools());
        new CacheApiWhiteListUseCase().executeSync(CacheApiWhiteListUseCase.createParams(
                getWhiteList(), String.valueOf(System.currentTimeMillis())));
    }

    public static int getCurrentVersion(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<CacheApiWhiteListDomain> getWhiteList() {
        List<CacheApiWhiteListDomain> cacheApiWhiteList = new ArrayList<>();
        cacheApiWhiteList.addAll(getShopWhiteList());
        return cacheApiWhiteList;
    }

    public static final List<CacheApiWhiteListDomain> getShopWhiteList() {
        List<CacheApiWhiteListDomain> cacheApiWhiteList = new ArrayList<>();

        return cacheApiWhiteList;
    }

    @Override
    public void onForceLogout(Activity activity) {

    }

    @Override
    public void showTimezoneErrorSnackbar() {

    }

    @Override
    public void showMaintenancePage() {

    }

    @Override
    public void sendForceLogoutAnalytics(Response response) {

    }

    @Override
    public void showForceLogoutTokenDialog(String response) {

    }

    @Override
    public void showServerError(Response response) {

    }

    @Override
    public void gcmUpdate() throws IOException {

    }

    @Override
    public void refreshToken() throws IOException {

    }

    @Override
    public void saveCPM(@NonNull String cpm) {

    }

    @Override
    public String getCPM() {
        return null;
    }

    @Override
    public boolean isEnable() {
        return false;
    }

    public UserSession getSession() {
        com.tokopedia.user.session.UserSession userSession =
                new com.tokopedia.user.session.UserSession(this);
        return new UserSession(this) {
            @Override
            public String getAccessToken() {
                return userSession.getAccessToken();
            }

            @Override
            public String getFreshToken() {
                return userSession.getFreshToken();
            }

            @Override
            public String getUserId() {
                return userSession.getUserId();
            }

            @Override
            public String getDeviceId() {
                return userSession.getDeviceId();
            }

            @Override
            public boolean isLoggedIn() {
                return userSession.isLoggedIn();
            }

            @Override
            public String getShopId() {
                return userSession.getShopId();
            }

            @Override
            public boolean hasShop() {
                return userSession.hasShop();
            }

            @Override
            public String getName() {
                return userSession.getName();
            }

            @Override
            public String getProfilePicture() {
                return userSession.getProfilePicture();
            }

            @Override
            public boolean isMsisdnVerified() {
                return userSession.isMsisdnVerified();
            }

        };
        //stagingURL
        // live hendry
//        return new UserSession() {
//            @Override
//            public String getAccessToken() {
//                return "_JbQIVoGTPuYbEN_mhiCOQ";
//            }
//
//            @Override
//            public String getFreshToken() {
//                return "kWv4s501oxsSQIMPtOejldf+H7FI+QkU9RnWS/RXstU=\n" +
//                        "    ";
//            }
//
//            @Override
//            public String getUserId() {
//                return "3349950";
//            }
//
//            @Override
//            public String getDeviceId() {
//                return "cK5ThvTXr2c:APA91bEkRWZNkGbzJVeP72-W7clBwDSwSuE1hoZHNyeTqD3049eV6f-81m7Apzk8qvsgvciGtoPw88IZWdWh9rGRHNwMwod3X1SKi2ljhmnIXyHpd5eZt9qCNATQnXkcEa-ba4j6H1ta";
//            }
//
//            @Override
//            public boolean isLoggedIn() {
//                return true;
//            }
//
//            @Override
//            public String getShopId() {
//                return "2116363";
//            }
//
//            @Override
//            public boolean hasShop() {
//                return true;
//            }
//
//            @Override
//            public String getName() {
//                return "Hendry Setiadi";
//            }
//
//            @Override
//            public String getProfilePicture() {
//                return "https://imagerouter.tokopedia.com/img/300/user-1/2018/3/21/3349950/3349950_642ea251-7ef6-44f0-b10c-1222aca83737.jpg";
//            }
//
//            @Override
//            public boolean isMsisdnVerified() {
//                return true;
//            }
//
//            @Override
//            public boolean isHasPassword() {
//                return true;
//            }
//        };

        //staging
        //Franky W Ramayana
//        return new UserSession() {
//            @Override
//            public String getAccessToken() {
//                return "df5STfkBS--UfPhmE-k70w";
//            }
//
//            @Override
//            public String getFreshToken() {
//                return "kWv4s501oxsSQIMPtOejldf+H7FI+QkU9RnWS/RXstU=\n" +
//                        "    ";
//            }
//
//            @Override
//            public String getUserId() {
//                return "5480226";
//            }
//
//            @Override
//            public String getDeviceId() {
//                return "dSQBiuIVbdA:APA91bGDfIJan1cDngkupmGD5i_1nFGSoVAA5TK3UncqarinyHR9BU5Y0IXh6BaK0RPplbJEHHQNYyChoh-OhdeIHRS8zraXWSIpWqP7SKCv246jS7sDd2_gKT6hI6yU6HL4OD3nsdRW";
//            }
//
//            @Override
//            public boolean isLoggedIn() {
//                return true;
//            }
//
//            @Override
//            public String getShopId() {
//                return "479057";
//            }
//
//            @Override
//            public boolean hasShop() {
//                return true;
//            }
//
//            @Override
//            public String getName() {
//                return "Franky W Ramayana";
//            }
//
//            @Override
//            public String getProfilePicture() {
//                return "https://imagerouter-staging.tokopedia.com/img/100-square/user-1/2016/12/15/5480226/5480226_cbedd327-4c08-4d5e-adbb-883803f0bb9d.jpg";
//            }
//
//            @Override
//            public boolean isMsisdnVerified() {
//                return true;
//            }
//
//            @Override
//            public boolean isHasPassword() {
//                return true;
//            }
//        };

        //staging angel2
        // angelia.telaumbanua+2@tokopedia.com // angel2
//        return new UserSession() {
//            @Override
//            public String getAccessToken() {
//                return "RcB9tXkRQG-JYOWPy55esA";
//            }
//
//            @Override
//            public String getFreshToken() {
//                return "kWv4s501oxsSQIMPtOejldf+H7FI+QkU9RnWS/RXstU=\n" +
//                        "    ";
//            }
//
//            @Override
//            public String getUserId() {
//                return "5484666";
//            }
//
//            @Override
//            public String getDeviceId() {
//                return "dSQBiuIVbdA:APA91bGDfIJan1cDngkupmGD5i_1nFGSoVAA5TK3UncqarinyHR9BU5Y0IXh6BaK0RPplbJEHHQNYyChoh-OhdeIHRS8zraXWSIpWqP7SKCv246jS7sDd2_gKT6hI6yU6HL4OD3nsdRW";
//            }
//
//            @Override
//            public boolean isLoggedIn() {
//                return true;
//            }
//
//            @Override
//            public String getShopId() {
//                return "479497";
//            }
//
//            @Override
//            public boolean hasShop() {
//                return true;
//            }
//
//            @Override
//            public String getName() {
//                return "Angelia Agustina Telaumbanua";
//            }
//
//            @Override
//            public String getProfilePicture() {
//                return "https://imagerouter-staging.tokopedia.com/img/100-square/default_v3-usrnophoto2.png";
//            }
//
//            @Override
//            public boolean isMsisdnVerified() {
//                return true;
//            }
//
//            @Override
//            public boolean isHasPassword() {
//                return true;
//            }
//        };

        //staging
        // hana mahrifah kotex
        // hana.mahrifah+kotex@tokopedia.com // Password123
//        return new UserSession() {
//            @Override
//            public String getAccessToken() {
//                return "YSf7O01cSsmNDNh8qWKTCw";
//            }
//
//            @Override
//            public String getFreshToken() {
//                return "kWv4s501oxsSQIMPtOejldf+H7FI+QkU9RnWS/RXstU=\n" +
//                        "    ";
//            }
//
//            @Override
//            public String getUserId() {
//                return "5484712";
//            }
//
//            @Override
//            public String getDeviceId() {
//                return "dlQxbTYALO4:APA91bETmNU1kCGY0OtQg2xEdsAwb6F74ONCrKqRPDIA21cgOuLi7fP8_M6fd-ekj3rh0SfQGGMoCtX1cDQzuYmoepjpQeHvyVTDyJbDglGDRDscdYNfzL-nxjU-oMDF7reg_xlWwYJU";
//            }
//
//            @Override
//            public boolean isLoggedIn() {
//                return true;
//            }
//
//            @Override
//            public String getShopId() {
//                return "479417";
//            }
//
//            @Override
//            public boolean hasShop() {
//                return true;
//            }
//
//            @Override
//            public String getName() {
//                return "Kotex Indonesia";
//            }
//
//            @Override
//            public String getProfilePicture() {
//                return "https://imagerouter-staging.tokopedia.com/img/100-square/default_v3-usrnophoto0.png";
//            }
//
//            @Override
//            public boolean isMsisdnVerified() {
//                return true;
//            }
//
//            @Override
//            public boolean isHasPassword() {
//                return true;
//            }
//        };

        // angelia.telaumbanua+2@tokopedia.com // angel2
//        return new UserSession() {
//            @Override
//            public String getAccessToken() {
//                return "rnBxS3wYSsm7LYhtfg5jRw";
//            }
//
//            @Override
//            public String getFreshToken() {
//                return "kWv4s501oxsSQIMPtOejldf+H7FI+QkU9RnWS/RXstU=\n" +
//                        "    ";
//            }
//
//            @Override
//            public String getUserId() {
//                return "5484666";
//            }
//
//            @Override
//            public String getDeviceId() {
//                return "dlQxbTYALO4:APA91bETmNU1kCGY0OtQg2xEdsAwb6F74ONCrKqRPDIA21cgOuLi7fP8_M6fd-ekj3rh0SfQGGMoCtX1cDQzuYmoepjpQeHvyVTDyJbDglGDRDscdYNfzL-nxjU-oMDF7reg_xlWwYJU";
//            }
//
//            @Override
//            public boolean isLoggedIn() {
//                return true;
//            }
//
//            @Override
//            public String getShopId() {
//                return "479497";
//            }
//
//            @Override
//            public boolean hasShop() {
//                return true;
//            }
//
//            @Override
//            public String getName() {
//                return "Angelia Agustina Telaumbanua";
//            }
//
//            @Override
//            public String getProfilePicture() {
//                return "https://imagerouter-staging.tokopedia.com/img/100-square/default_v3-usrnophoto2.png";
//            }
//
//            @Override
//            public boolean isMsisdnVerified() {
//                return true;
//            }
//
//            @Override
//            public boolean isHasPassword() {
//                return true;
//            }
//        };

        // hana.mahrifah+bosch@tokopedia.com // Password123
//        return new UserSession() {
//            @Override
//            public String getAccessToken() {
//                return "FrhNtUwNQ5WPHf2CDUcEWQ";
//            }
//
//            @Override
//            public String getFreshToken() {
//                return "kWv4s501oxsSQIMPtOejldf+H7FI+QkU9RnWS/RXstU=\n" +
//                        "    ";
//            }
//
//            @Override
//            public String getUserId() {
//                return "5484724";
//            }
//
//            @Override
//            public String getDeviceId() {
//                return "c02CcuJ7_Mg:APA91bGtVEZaNQbz4ekDIiti0U0GfW7jiiWC-Rg86VHxQNAmR5Xd5a6-CTQSH2FM3kYAiVT5jvuMmT9iy8ie_jRtssZcBuf-498aGHN0g6T8bQD09aJ41B4_ns2WAZUvWl4hKJfI3c3w";
//            }
//
//            @Override
//            public boolean isLoggedIn() {
//                return true;
//            }
//
//            @Override
//            public String getShopId() {
//                return "479420";
//            }
//
//            @Override
//            public boolean hasShop() {
//                return true;
//            }
//
//            @Override
//            public String getName() {
//                return "Hana Bosch";
//            }
//
//            @Override
//            public String getProfilePicture() {
//                return "https://imagerouter-staging.tokopedia.com/img/100-square/default_picture_user/default_toped-12.jpg";
//            }
//
//            @Override
//            public boolean isMsisdnVerified() {
//                return true;
//            }
//
//            @Override
//            public boolean isHasPassword() {
//                return true;
//            }
//        };


        // hana.mahrifah+smesco@tokopedia.com // Password123
//        return new UserSession() {
//            @Override
//            public String getAccessToken() {
//                return "iLmoMUFNTNeonPsM2qSGMw";
//            }
//
//            @Override
//            public String getFreshToken() {
//                return "kWv4s501oxsSQIMPtOejldf+H7FI+QkU9RnWS/RXstU=\n" +
//                        "    ";
//            }
//
//            @Override
//            public String getUserId() {
//                return "5484810";
//            }
//
//            @Override
//            public String getDeviceId() {
//                return "ce5KCEx4ndM:APA91bEStuiFBeTRkwbSIU2mqcMCgl5d7Yms8dN8QwP84qnqZjIef01Om8Mj022Hx8wTe2M-mGjA1XwtQWVaM9D3wXQ4HuVGvgckhghE44XZDYXQdHov4aemRd5_TY3h6vxbCPb4fg4X";
//            }
//
//            @Override
//            public boolean isLoggedIn() {
//                return true;
//            }
//
//            @Override
//            public String getShopId() {
//                return "479444";
//            }
//
//            @Override
//            public boolean hasShop() {
//                return true;
//            }
//
//            @Override
//            public String getName() {
//                return "Hana SMESCO";
//            }
//
//            @Override
//            public String getProfilePicture() {
//                return "https://imagerouter-staging.tokopedia.com/img/100-square/default_v3-usrnophoto0.png";
//            }
//
//            @Override
//            public boolean isMsisdnVerified() {
//                return true;
//            }
//
//            @Override
//            public boolean isHasPassword() {
//                return true;
//            }
//        };

        // asti.lestari+garnier@tokopedia.com // Password123
//        return new UserSession() {
//            @Override
//            public String getAccessToken() {
//                return "rF9J9KC0QDGr-K0VEYr95A";
//            }
//
//            @Override
//            public String getFreshToken() {
//                return "kWv4s501oxsSQIMPtOejldf+H7FI+QkU9RnWS/RXstU=\n" +
//                        "    ";
//            }
//
//            @Override
//            public String getUserId() {
//                return "5510902";
//            }
//
//            @Override
//            public String getDeviceId() {
//                return "ce5KCEx4ndM:APA91bEStuiFBeTRkwbSIU2mqcMCgl5d7Yms8dN8QwP84qnqZjIef01Om8Mj022Hx8wTe2M-mGjA1XwtQWVaM9D3wXQ4HuVGvgckhghE44XZDYXQdHov4aemRd5_TY3h6vxbCPb4fg4X";
//            }
//
//            @Override
//            public boolean isLoggedIn() {
//                return true;
//            }
//
//            @Override
//            public String getShopId() {
//                return "479669";
//            }
//
//            @Override
//            public boolean hasShop() {
//                return true;
//            }
//
//            @Override
//            public String getName() {
//                return "Asti Garnier";
//            }
//
//            @Override
//            public String getProfilePicture() {
//                return "https://imagerouter-staging.tokopedia.com/img/100-square/default_picture_user/default_toped-14.jpg";
//            }
//
//            @Override
//            public boolean isMsisdnVerified() {
//                return true;
//            }
//
//            @Override
//            public boolean isHasPassword() {
//                return true;
//            }
//        };


//        return new UserSession() {
//
//            @Override
//            public String getAccessToken() {
//                return "Nj152dtARg2jFfJzSYy0rw";
//            }
//
//            @Override
//            public String getFreshToken() {
//                return "DRc+iVrnm4MN16xKSYfiNi4TFK+uxgu6ofdYdUAnf7g=\n    ";
//            }
//
//            @Override
//            public String getUserId() {
//                return "3045010";
//            }
//
//            @Override
//            public String getDeviceId() {
//                return "ddG2JJdtkXc:APA91bHf90k_C73h7MqhsVpN65t_rJ6klCV02Ks86F_OyLcD2ypH1VWoASR8nzbGPj9dWHxOtk1sP8bkY4D1J_tWiqOGNXXFrdwZ7aCXLLdGY8N9Vm0SFHrD_EvzC2ojCuNmxOJ1KmPtuwHUt8TLrM0_aEfm28p3hw";
//            }
//
//            @Override
//            public boolean isLoggedIn() {
//                return true;
//            }
//
//            @Override
//            public String getVoucherShopId() {
//                return "394715";
//            }
//
//            @Override
//            public boolean hasShop() {
//                return true;
//            }
//
//            @Override
//            public String getName() {
//                return "Cincin Seller";
//            }
//
//            @Override
//            public String getProfilePicture() {
//                return "https://imagerouter.tokopedia.com/img/300/user-1/2018/7/9/3045010/3045010_ea31fee4-2d16-4366-bf7b-cdbb534950f6.jpg";
//            }
//
//            @Override
//            public boolean isMsisdnVerified() {
//                return true;
//            }
//
//            @Override
//            public boolean isHasPassword() {
//                return true;
//            }
//        };


//        return new UserSession() {
//            @Override
//            public String getAccessToken() {
//                return "Md2aLJb1Sg23_XumuWL3RQ";
//            }
//
//            @Override
//            public String getFreshToken() {
//                return "iZAfMMeZG/KfHwEYQ4MsxOSVrxayiWvajWLcFq5bNF4=\n";
//            }
//
//            @Override
//            public String getUserId() {
//                return "1533218";
//            }
//
//            @Override
//            public String getDeviceId() {
//                return "ddG2JJdtkXc:APA91bHf90k_C73h7MqhsVpN65t_rJ6klCV02Ks86F_OyLcD2ypH1VWoASR8nzbGPj9dWHxOtk1sP8bkY4D1J_tWiqOGNXXFrdwZ7aCXLLdGY8N9Vm0SFHrD_EvzC2ojCuNmxOJ1KmPtuwHUt8TLrM0_aEfm28p3hw";
//            }
//
//            @Override
//            public boolean isLoggedIn() {
//                return true;
//            }
//
//            @Override
//            public String getVoucherShopId() {
//                return "231833";
//            }
//
//            @Override
//            public boolean hasShop() {
//                return true;
//            }
//
//            @Override
//            public String getName() {
//                return "Demo User Satu";
//            }
//
//            @Override
//            public String getProfilePicture() {
//                return "https://imagerouter.tokopedia.com/img/300/user-1/2018/6/26/1533218/1533218_f406d2cf-bca6-4162-901d-a5828f769af2.jpg";
//            }
//
//            @Override
//            public boolean isMsisdnVerified() {
//                return true;
//            }
//
//            @Override
//            public boolean isHasPassword() {
//                return true;
//            }
//        };

//        return new UserSession() {
//            @Override
//            public String getAccessToken() {
//                return "2UDIp9giQHyvZBlDnls4aw";
//            }
//
//            @Override
//            public String getFreshToken() {
//                return "AHNTmmKoFSu81EysxCFeUjMVBpOrNS96ELdcmMyzLd8=\n";
//            }
//
//            @Override
//            public String getUserId() {
//                return "17039199";
//            }
//
//            @Override
//            public String getDeviceId() {
//                return "d7B4B7zf0eU:APA91bFMMLmP_SkE2MzQDAHtIl-6jcXgZ5apUk-PeSim9mK0Zoc1f7Lt7V_PTssripsiqR3kOYy51w_yy3yUjMfTNuQH0ayjcKh-HyLSP8sLwVjY-qq6NOXihm4YBTUsnS3bdUORw0__wLfiwKFlYqMCXv6j5FVtgg";
//            }
//
//            @Override
//            public boolean isLoggedIn() {
//                return true;
//            }
//
//            @Override
//            public String getVoucherShopId() {
//                return "1933102";
//            }
//
//            @Override
//            public boolean hasShop() {
//                return true;
//            }
//
//            @Override
//            public String getName() {
//                return "Komodo";
//            }
//
//            @Override
//            public String getProfilePicture() {
//                return "https://imagerouter.tokopedia.com/img/100-square/default_picture_user/default_toped-18.jpg";
//            }
//
//            @Override
//            public boolean isMsisdnVerified() {
//                return true;
//            }
//
//            @Override
//            public boolean isHasPassword() {
//                return true;
//            }
//        };

        // staging tkpdqc47
//        return new UserSession() {
//            @Override
//            public String getAccessToken() {
//                return "obkFCLTBQYST7goeq_6kCA";
//            }
//
//            @Override
//            public String getFreshToken() {
//                return "H8vV/VGaD/845dJjqlqLdF2VImoja0lU7DGvAQrWBys=\n";
//            }
//
//            @Override
//            public String getUserId() {
//                return "3045010";
//            }
//
//            @Override
//            public String getDeviceId() {
//                return "fa7HsQApupo:APA91bGEwUHTIacCzrpglSPz57I5ZDAL6vf6ReusewsGXTuXpCJwJP3NHCkEHHe7HK-3F8kfZNxZWWjptaaHej_v65YZE_KRYDCRyfTl2g0enkcAdwsGePm-L7p4CKOowHuGinnWSIkU";
//            }
//
//            @Override
//            public boolean isLoggedIn() {
//                return true;
//            }
//
//            @Override
//            public String getShopId() {
//                return "394715";
//            }
//
//            @Override
//            public boolean hasShop() {
//                return true;
//            }
//
//            @Override
//            public String getName() {
//                return "Cincin Seller";
//            }
//
//            @Override
//            public String getProfilePicture() {
//                return "https://imagerouter-staging.tokopedia.com/img/300/user-1/2018/9/21/3045010/3045010_3e7c071a-67a4-4db2-b4d0-9d5ed674c2f7.jpg";
//            }
//
//            @Override
//            public boolean isMsisdnVerified() {
//                return true;
//            }
//
//            @Override
//            public boolean isHasPassword() {
//                return true;
//            }
//        };

        /*return new UserSession() {
            @Override
            public String getAccessToken() {
                return "tz5N0rBnQKOw65RcaIOq0Q";
            }

            @Override
            public String getFreshToken() {
                return "H8vV/VGaD/845dJjqlqLdF2VImoja0lU7DGvAQrWBys=\n";
            }

            @Override
            public String getUserId() {
                return "5512646";
            }

            @Override
            public String getDeviceId() {
                return "c02CcuJ7_Mg:APA91bGtVEZaNQbz4ekDIiti0U0GfW7jiiWC-Rg86VHxQNAmR5Xd5a6-CTQSH2FM3kYAiVT5jvuMmT9iy8ie_jRtssZcBuf-498aGHN0g6T8bQD09aJ41B4_ns2WAZUvWl4hKJfI3c3w";
            }

            @Override
            public boolean isLoggedIn() {
                return true;
            }

            @Override
            public String getShopId() {
                return "480125";
            }

            @Override
            public boolean hasShop() {
                return true;
            }

            @Override
            public String getName() {
                return "Kartika Sitorus";
            }

            @Override
            public String getProfilePicture() {
                return "https://imagerouter-staging.tokopedia.com/img/100-square/default_v3-usrnophoto2.png";
            }

            @Override
            public boolean isMsisdnVerified() {
                return true;
            }

            @Override
            public boolean isHasPassword() {
                return true;
            }
        };*/

        /*return new UserSession() {
            @Override
            public String getAccessToken() {
                return "VKuXyyhlQQ-mO0sHqD7G3A";
            }

            @Override
            public String getFreshToken() {
                return "H8vV/VGaD/845dJjqlqLdF2VImoja0lU7DGvAQrWBys=\n";
            }

            @Override
            public String getUserId() {
                return "16620763";
            }

            @Override
            public String getDeviceId() {
                return "fa263TbMbWA:APA91bEHfyDziJjFAl7BB5P-AlN6f_HYksKy32AKzHP1IpHN7yCvFCHV-dYhZPzqWgyBa4J_xmG_WH8S_s1gRM-xi3-G6S_pa_4WI70rhbdKgt0uZfka2Qnn-wunydfFCLaKNMuQTrbe";
            }

            @Override
            public boolean isLoggedIn() {
                return true;
            }

            @Override
            public String getShopId() {
                return "1990266";
            }

            @Override
            public boolean hasShop() {
                return true;
            }

            @Override
            public String getName() {
                return "Tokped Merchos";
            }

            @Override
            public String getProfilePicture() {
                return "https://imagerouter-staging.tokopedia.com/img/100-square/default_v3-usrnophoto2.png";
            }

            @Override
            public boolean isMsisdnVerified() {
                return true;
            }

            @Override
            public boolean isHasPassword() {
                return true;
            }
        };*/

        /*return new UserSession() {
            @Override
            public String getAccessToken() {
                return "0E7jbFzjRneN20rrkKYunA";
            }

            @Override
            public String getFreshToken() {
                return "H8vV/VGaD/845dJjqlqLdF2VImoja0lU7DGvAQrWBys=\n";
            }

            @Override
            public String getUserId() {
                return "7323841";
            }

            @Override
            public String getDeviceId() {
                return "dxWmNJTHr38:APA91bH79g3hj0h4rU2bI9YHTVYvwTu5YWftjMWhyClBJg8gwna0pY70S_OCm25LIHJEHfg-z0U-syw0g6G2FLoW3niMInr8DSqMBujxKKDmqaodweMMlMj0VTgUMssxzduzoRx5fxh4";
            }

            @Override
            public boolean isLoggedIn() {
                return true;
            }

            @Override
            public String getShopId() {
                return "873493";
            }

            @Override
            public boolean hasShop() {
                return true;
            }

            @Override
            public String getName() {
                return "ravennaravicka";
            }

            @Override
            public String getProfilePicture() {
                return "https://imagerouter-staging.tokopedia.com/img/100-square/default_v3-usrnophoto2.png";
            }

            @Override
            public boolean isMsisdnVerified() {
                return true;
            }

            @Override
            public boolean isHasPassword() {
                return true;
            }
        };*/

        //tokpedmerchos
        /*return new UserSession() {
            @Override
            public String getAccessToken() {
                return "1C3c5IeeTPyfx60AyeoLHA";
            }

            @Override
            public String getFreshToken() {
                return "H8vV/VGaD/845dJjqlqLdF2VImoja0lU7DGvAQrWBys=\n";
            }

            @Override
            public String getUserId() {
                return "3006127";
            }

            @Override
            public String getDeviceId() {
                return "dxWmNJTHr38:APA91bH79g3hj0h4rU2bI9YHTVYvwTu5YWftjMWhyClBJg8gwna0pY70S_OCm25LIHJEHfg-z0U-syw0g6G2FLoW3niMInr8DSqMBujxKKDmqaodweMMlMj0VTgUMssxzduzoRx5fxh4";
            }

            @Override
            public boolean isLoggedIn() {
                return true;
            }

            @Override
            public String getShopId() {
                return "1704000";
            }

            @Override
            public boolean hasShop() {
                return true;
            }

            @Override
            public String getName() {
                return "jaminakasa";
            }

            @Override
            public String getProfilePicture() {
                return "https://imagerouter-staging.tokopedia.com/img/100-square/default_v3-usrnophoto2.png";
            }

            @Override
            public boolean isMsisdnVerified() {
                return true;
            }

            @Override
            public boolean isHasPassword() {
                return true;
            }
        };*/

        //lestary live
//        return new UserSession() {
//            @Override
//            public String getAccessToken() {
//                return "O3BVhHShTSSDfNw6NBPANA";
//            }
//
//            @Override
//            public String getFreshToken() {
//                return "H8vV/VGaD/845dJjqlqLdF2VImoja0lU7DGvAQrWBys=\n";
//            }
//
//            @Override
//            public String getUserId() {
//                return "28836129";
//            }
//
//            @Override
//            public String getDeviceId() {
//                return "cx68b1CtPII:APA91bEV_bdZfq9qPB-xHn2z34ccRQ5M8y9c9pfqTbpIy1AlOrJYSFMKzm_GaszoFsYcSeZY-bTUbdccqmW8lwPQVli3B1fCjWnASz5ZePCpkh9iEjaWjaPovAZKZenowuo4GMD68hoR";
//            }
//
//            @Override
//            public boolean isLoggedIn() {
//                return true;
//            }
//
//            @Override
//            public String getShopId() {
//                return "3118855";
//            }
//
//            @Override
//            public boolean hasShop() {
//                return true;
//            }
//
//            @Override
//            public String getName() {
//                return "lestary";
//            }
//
//            @Override
//            public String getProfilePicture() {
//                return "https://imagerouter-staging.tokopedia.com/img/100-square/default_v3-usrnophoto2.png";
//            }
//
//            @Override
//            public boolean isMsisdnVerified() {
//                return true;
//            }
//
//            @Override
//            public boolean isHasPassword() {
//                return true;
//            }
//        };

        //asti.lestari+os1@tokopedia.com // Password123
//        return new UserSession() {
//            @Override
//            public String getAccessToken() {
//                return "QXroi059SCSCeCE2F_cNuA";
//            }
//
//            @Override
//            public String getFreshToken() {
//                return "H8vV/VGaD/845dJjqlqLdF2VImoja0lU7DGvAQrWBys=\n";
//            }
//
//            @Override
//            public String getUserId() {
//                return "38237591";
//            }
//
//            @Override
//            public String getDeviceId() {
//                return "cx68b1CtPII:APA91bEV_bdZfq9qPB-xHn2z34ccRQ5M8y9c9pfqTbpIy1AlOrJYSFMKzm_GaszoFsYcSeZY-bTUbdccqmW8lwPQVli3B1fCjWnASz5ZePCpkh9iEjaWjaPovAZKZenowuo4GMD68hoR";
//            }
//
//            @Override
//            public boolean isLoggedIn() {
//                return true;
//            }
//
//            @Override
//            public String getShopId() {
//                return "3829112";
//            }
//
//            @Override
//            public boolean hasShop() {
//                return true;
//            }
//
//            @Override
//            public String getName() {
//                return "lestary";
//            }
//
//            @Override
//            public String getProfilePicture() {
//                return "https://imagerouter-staging.tokopedia.com/img/100-square/default_v3-usrnophoto2.png";
//            }
//
//            @Override
//            public boolean isMsisdnVerified() {
//                return true;
//            }
//
//            @Override
//            public boolean isHasPassword() {
//                return true;
//            }
//        };
    }

    @Override
    public void init() {

    }

    @Override
    public void registerShake(String screenName, Activity activity) {

    }

    @Override
    public void unregisterShake() {

    }

    @Override
    public CacheManager getGlobalCacheManager() {
        return null;
    }


//    @Override
//    public void showForceHockeyAppDialog() {
//
//    }

    @Override
    public void logInvalidGrant(Response response) {

    }

    @Override
    public void instabugCaptureUserStep(Activity activity, MotionEvent me) {

    }

    @Override
    public boolean isAllowLogOnChuckInterceptorNotification() {
        return false;
    }


    @Override
    public FingerprintModel getFingerprintModel() {
        return DataSource.generateFingerprintModel();
    }

    @Override
    public void doRelogin(String newAccessToken) {

    }

    @Override
    public void goToApplinkActivity(Context context, String applink) {
        Toast.makeText(getApplicationContext(), "GO TO " + applink, Toast.LENGTH_LONG).show();
    }

    @Override
    public void goToApplinkActivity(Activity activity, String applink, Bundle bundle) {
        Toast.makeText(getApplicationContext(), "GO TO " + applink + " with bundle", Toast.LENGTH_LONG).show();
    }

    @Override
    public Intent getApplinkIntent(Context context, String applink) {
        Toast.makeText(getApplicationContext(), "GO TO " + applink, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.goTo" + applink + ".com"));
        return intent;
    }

    @Override
    public boolean isSupportApplink(String appLink) {
        return true;
    }

    @Override
    public ApplinkUnsupported getApplinkUnsupported(Activity activity) {
        return null;
    }

    @Override
    public ApplinkDelegate applinkDelegate() {
        return null;
    }

//    @NotNull
//    @Override
//    public Observable<com.tokopedia.transaction.common.sharedata.AddToCartResult> addToCartProduct(@NotNull com.tokopedia.transaction.common.sharedata.AddToCartRequest addToCartRequest, boolean isOneClickShipment) {
//        return Observable.just(null);
//    }
}
