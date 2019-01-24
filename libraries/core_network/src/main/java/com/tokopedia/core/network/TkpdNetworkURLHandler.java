package com.tokopedia.core.network;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.var.TkpdCache;

/**
 * Created by ricoharisin on 2/10/15.
 */
public class TkpdNetworkURLHandler {

    public static boolean getProtocolHttp(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.NETWORK_URL_KEY);
        return cache.getBoolean(TkpdCache.Key.IS_HTTP, false);
    }

}
