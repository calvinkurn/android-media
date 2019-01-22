package com.tokopedia.core.network;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.core.util.GeneralUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.var.TkpdCache;

import java.util.List;

/**
 * Created by ricoharisin on 2/10/15.
 */

@Deprecated
public class TkpdNetworkURLHandler {

    private static final String DEFAULT_URL = "www.tokopedia.com";
    private static final String DEFAULT_TRANSPORT = "http://";
    private static final int STATE_PROD = 1;
    private static final int STATE_BETA = 2;
    private static final int STATE_DEV = 3;

    public static String generateURL(Context context, String url) {
        String urlGTM = getUrlFromGTM(context, getKeyFromUrl(url));
        Log.i("GTM TKPD","GTM URL: "+urlGTM);
        if (urlGTM != null && !urlGTM.equals("")) {
            return urlGTM;
        } else {
            if (!GlobalConfig.DEBUG) return url;

            Uri uri = Uri.parse(url);
            GeneralUtils.dumper("base: " + uri.getHost());
            GeneralUtils.dumper("path: " + uri.getPathSegments());

            String newURL = DEFAULT_TRANSPORT + getHost(context);

            List<String> path = uri.getPathSegments();
            for (int i = 0; i < path.size(); i++) {
                newURL = newURL + "/" + path.get(i);
            }

            return newURL;
        }
    }

    public static String getHost(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.NETWORK_URL_KEY);
        return cache.getString(TkpdCache.Key.HOST, DEFAULT_URL);
    }

    public static void setHost(Context context, String host) {
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.NETWORK_URL_KEY);
        cache.putString(TkpdCache.Key.HOST, host);
        cache.applyEditor();
    }

    public static boolean getProtocolHttp(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.NETWORK_URL_KEY);
        return cache.getBoolean(TkpdCache.Key.IS_HTTP, false);
    }

    public static void setProtocolHttp(Context context, boolean isHttp) {
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.NETWORK_URL_KEY);
        cache.putBoolean(TkpdCache.Key.IS_HTTP, isHttp);
        cache.applyEditor();
    }

    public static String getProxyAddress(Context context, String alt) {
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.NETWORK_URL_KEY);
        return cache.getString(TkpdCache.Key.PROXY_ADDRESS, alt);
    }

    public static void setProxyAddress(Context context, String ProxyAddress) {
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.NETWORK_URL_KEY);
        cache.putString(TkpdCache.Key.PROXY_ADDRESS, ProxyAddress);
        cache.applyEditor();
    }

    public static int getProxyPort(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.NETWORK_URL_KEY);
        return cache.getInt(TkpdCache.Key.PROXY_PORT, 0);
    }

    public static void setProxyPort(Context context, int ProxyAddress) {
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.NETWORK_URL_KEY);
        cache.putInt(TkpdCache.Key.PROXY_PORT, ProxyAddress);
        cache.applyEditor();
    }

    private static int getStateURL(Context context) {
        if (!GlobalConfig.DEBUG) {
            return STATE_PROD;
        }

        String host = getHost(context);
        if (host.equals(DEFAULT_URL)) {
            return STATE_PROD;
        } else if (host.contains("beta")) {
            return STATE_BETA;
        } else if (host.contains("dev") || host.contains("dvl")) {
            return STATE_DEV;
        }

        return STATE_PROD;
    }

    private static String getKeyFromUrl(String url) {
        Uri uri = Uri.parse(url);
        List<String> path = uri.getPathSegments();
        String lastPath = path.get(path.size()-1);
        lastPath = lastPath.replace(".pl", "");
        return lastPath;
    }

    private static String getFromCollection(String key, int state) {
        Log.i("GTM TKPD", "KEY: "+key+" STATE: "+state);
        switch (state) {
            case STATE_PROD:
                return TrackingUtils.getGtmString(CoreNetworkApplication.getAppContext(),
                        key+"_production");
            case STATE_BETA:
                return TrackingUtils.getGtmString(CoreNetworkApplication.getAppContext(),
                        key+"_staging");
            case STATE_DEV:
                return TrackingUtils.getGtmString(CoreNetworkApplication.getAppContext(),
                        key+"_dev");
            default:
                return null;
        }
    }

    private static String getUrlFromGTM(Context context, String key) {
        try {
            return getFromCollection(key, getStateURL(context));
        } catch (Exception e) {
            Log.e("Error Parsing GTM", e.toString());
            return "";
        }
    }


}
