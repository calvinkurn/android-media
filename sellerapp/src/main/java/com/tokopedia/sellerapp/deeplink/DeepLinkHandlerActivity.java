package com.tokopedia.sellerapp.deeplink;

import static com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.OPEN_SHOP;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.DeeplinkMapper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.sellerapp.SplashScreenActivity;
import com.tokopedia.sellerapp.deeplink.presenter.DeepLinkAnalyticsImpl;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;
import java.util.Map;

public class DeepLinkHandlerActivity extends AppCompatActivity {

    private static final String TOKOPEDIA_DOMAIN = "tokopedia";
    private static final String URL_QUERY_PARAM = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                processApplink(presenter);
            }

        }
        finish();
    }

    private void processApplink(DeepLinkAnalyticsImpl presenter) {
        Uri applink = getIntent().getData();
        if (applink == null) {
            return;
        }

        presenter.processUTM(this, applink);

        String applinkString = applink.toString();
        logWebViewApplink(applink);

        //map applink to internal if any
        String mappedDeeplink = DeeplinkMapper.getRegisteredNavigation(this, applinkString);
        if (TextUtils.isEmpty(mappedDeeplink)) {
            routeToApplink(applinkString);
        } else {
            routeToApplink(mappedDeeplink);
        }
    }

    private void routeToApplink(String applinkString) {
        Intent intent = RouteManager.getIntent(this, applinkString);
        startActivity(intent);
        this.finish();
    }

    public static Intent moveToCreateShop(Context context) {
        if (context == null)
            return null;

        Intent intent = RouteManager.getIntent(context, OPEN_SHOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    private void logWebViewApplink(Uri uri) {
        if (uri.toString().contains(ApplinkConst.SellerApp.WEBVIEW)) {
            Uri urlToLoad = getUrlToLoad(uri);
            if (urlToLoad != null) {
                String domain = urlToLoad.getHost();
                if (domain != null) {
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
        if (host == null) {
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
