package com.tokopedia.sellerapp.deeplink;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.tokopedia.applink.AppUtil;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.DeeplinkMapper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.homecredit.applink.HomeCreditAppLinkModule;
import com.tokopedia.homecredit.applink.HomeCreditAppLinkModuleLoader;
import com.tokopedia.sellerapp.SplashScreenActivity;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.sellerapp.deeplink.presenter.DeepLinkAnalyticsImpl;
import com.tokopedia.topads.applink.TopAdsApplinkModule;
import com.tokopedia.topads.applink.TopAdsApplinkModuleLoader;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.webview.WebViewApplinkModule;
import com.tokopedia.webview.WebViewApplinkModuleLoader;

import java.util.HashMap;
import java.util.Map;

import static com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.OPEN_SHOP;

/**
 * @author rizkyfadillah on 26/07/17.
 */
@DeepLinkHandler({
        TopAdsApplinkModule.class,
        WebViewApplinkModule.class,
        HomeCreditAppLinkModule.class
})
/* **
 * Navigation will via RouteManager -> manifest instead.
 * Put new Deeplink directly into the target activity
 */
@Deprecated
public class DeepLinkHandlerActivity extends AppCompatActivity {

    private static final String TOKOPEDIA_DOMAIN = "tokopedia";
    private static final String URL_QUERY_PARAM = "url";


    public static DeepLinkDelegate getDelegateInstance() {
        return new DeepLinkDelegate(
                new TopAdsApplinkModuleLoader(),
                new WebViewApplinkModuleLoader(),
                new HomeCreditAppLinkModuleLoader()
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeepLinkDelegate deepLinkDelegate = getDelegateInstance();
        DeepLinkAnalyticsImpl presenter = new DeepLinkAnalyticsImpl();
        if (getIntent() != null) {
            UserSessionInterface userSession = new UserSession(this);
            if (!userSession.isLoggedIn() || !userSession.hasShop()) {
                if (userSession.isLoggedIn()) {
                    startActivity(moveToCreateShop(this));
                } else {
                    startActivity(new Intent(this, SplashScreenActivity.class));
                }
            } else {
                processApplink(deepLinkDelegate, presenter);
            }

        }
        finish();
    }

    private void processApplink(DeepLinkDelegate deepLinkDelegate, DeepLinkAnalyticsImpl presenter) {
        Uri applink = getIntent().getData();
        presenter.processUTM(this, applink);

        if (applink == null) {
            return;
        }

        String applinkString = applink.toString();
        logWebViewApplink(applink);

        //map applink to internal if any
        String mappedDeeplink = DeeplinkMapper.getRegisteredNavigation(this, applinkString);
        if (TextUtils.isEmpty(mappedDeeplink)) {
            routeToApplink(deepLinkDelegate, applinkString);
        } else {
            routeToApplink(deepLinkDelegate, mappedDeeplink);
        }
    }

    private void routeToApplink(DeepLinkDelegate deepLinkDelegate, String applinkString) {
        if (deepLinkDelegate.supportsUri(applinkString)) {
            getIntent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            deepLinkDelegate.dispatchFrom(this, getIntent());
            if (getIntent().getExtras() != null) {
                Bundle bundle = getIntent().getExtras();
                eventPersonalizedClicked(bundle.getString(Constants.EXTRA_APPLINK_CATEGORY));
            }
            AppUtil.logAirBnbUsage(applinkString);
        } else {
            Intent intent = RouteManager.getIntent(this, applinkString);
            startActivity(intent);
            this.finish();
        }
    }


    public void eventPersonalizedClicked(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.Event.OPEN_PUSH_NOTIFICATION,
                AppEventTracking.Category.PUSH_NOTIFICATION,
                AppEventTracking.Action.OPEN,
                label);
    }

    public static Intent moveToCreateShop(Context context) {
        if (context == null)
            return null;

        Intent intent = RouteManager.getIntent(context,OPEN_SHOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    private void logWebViewApplink(Uri uri) {
        if(uri.toString().contains(ApplinkConst.SellerApp.WEBVIEW)) {
            Uri urlToLoad = getUrlToLoad(uri);
            if(urlToLoad != null) {
                String domain = urlToLoad.getHost();
                if(domain != null) {
                    if (!getBaseDomain(domain).equalsIgnoreCase(TOKOPEDIA_DOMAIN)) {
                        Map<String, String> messageMap = new HashMap<>();
                        messageMap.put("type", "applink");
                        messageMap.put("domain", domain);
                        messageMap.put("url", String.valueOf(uri));
                        ServerLogger.log(Priority.P1, "WEBVIEW_OPENED", messageMap);
                    }
                }
            }
        }
    }

    private String getBaseDomain(String host) {
        if(host == null) {
            return "";
        }
        String[] split = host.split("\\.");
        if (split.length > 2) {
            return split[1];
        } else {
            return split[0];
        }
    }

    private Uri getUrlToLoad(Uri url) {
        try {
            return Uri.parse(url.getQueryParameter(URL_QUERY_PARAM));
        } catch (Exception e) {
            return null;
        }
    }

}
