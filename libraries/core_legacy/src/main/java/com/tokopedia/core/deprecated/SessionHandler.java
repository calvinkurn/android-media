package com.tokopedia.core.deprecated;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.core.var.TkpdCache;

/**
 * most of the codes is no-op that need to defined at the application.
 */
public abstract class SessionHandler {
    protected Context context;

    public SessionHandler(Context context) {
        this.context = context;
    }

    public abstract String getLoginID();

    public String getAdsId(){
        final LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.ADVERTISINGID);
        String adsId = localCacheHandler.getString(TkpdCache.Key.KEY_ADVERTISINGID);
        if (adsId != null && !"".equalsIgnoreCase(adsId.trim())) {
            return adsId;
        }else{
            return null;
        }
    }

    public abstract String getRefreshToken();

}
