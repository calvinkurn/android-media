package com.tokopedia.home.beranda.helper.glide;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;

import com.tokopedia.config.GlobalConfig;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.wandroid.traceroute.TraceRoute;
import com.wandroid.traceroute.TraceRouteCallback;
import com.wandroid.traceroute.TraceRouteResult;

import com.bumptech.glide.load.engine.GlideException;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class GlideErrorLogHelper {

    public static void logError(Context context, GlideException e, String url) {

        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
        long traceRouteMinVersion = remoteConfig.getLong(RemoteConfigKey.ENABLE_TRACEROUTE_MIN_VERSION, 999999999);
        if (GlobalConfig.VERSION_CODE < traceRouteMinVersion) {
            return;
        }

        if (!isNetworkAvailable(context)) {
            Timber.w("P2#IMAGE_TRACEROUTE#network not available");
            return;
        }

        TraceRoute.INSTANCE.setCallback(new TraceRouteCallback() {
            String traceResult = "";

            @Override
            public void onSuccess(@NotNull TraceRouteResult traceRouteResult) {
                Timber.w("P2#IMAGE_TRACEROUTE#success: url= %s message= %s traceroute= %s",
                        url,
                        e != null ? e.getMessage() : "",
                        traceResult);
            }

            @Override
            public void onUpdate(@NotNull String text) {
                traceResult += text;
            }

            @Override
            public void onFailed(int code, @NotNull String reason) {
                traceResult += String.format("code: %d reason: %s", code, reason);
                Timber.w("P2#IMAGE_TRACEROUTE#failed: url= %s message= %s traceroute= %s",
                        url,
                        e != null ? e.getMessage() : "",
                        traceResult);
            }
        });

        String host = Uri.parse(url).getHost();
        if (!TextUtils.isEmpty(host)) {
            TraceRoute.INSTANCE.traceRoute(host, true);
        }
    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } else {
            return true;
        }
    }
}
