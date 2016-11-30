package com.tokopedia.core.util;

import android.net.Uri;
import android.util.Log;

/**
 * Created by ricoharisin on 8/21/15.
 */
public class DeepLinkUtils {

    public static Uri generateAppUri(String rawurl) {
        Uri url = Uri.parse(rawurl);
        String base = "android-app://com.tokopedia.tkpd/"+url.getScheme()+"/"+url.getHost();
        for (int i = 0; i < url.getPathSegments().size(); i++) {
            base = base+"/"+url.getPathSegments( ).get(i);
        }
        Log.i("Deep Link Utils", "Generated App Uri: " + base);
        return Uri.parse(base);
    }
}
