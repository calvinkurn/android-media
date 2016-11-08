package com.tokopedia.core.network;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.http.AndroidHttpClient;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;

import java.io.File;

/**
 * Created by ricoharisin on 7/2/15.
 */
@Deprecated
public class VolleyRequestQueue {

    private static final int THREAD_POOL_SIZE = 8;

    public static RequestQueue newRequestQueue(Context context) {
        HttpStack stack;
        File cacheDir = new File(context.getCacheDir(), "volley");
        String userAgent = "volley/0";
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            userAgent = packageName + "/" + info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }

        stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));

        Network network = new BasicNetwork(stack);
        RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), network, THREAD_POOL_SIZE);
        queue.start();
        return queue;
    }
}
