package com.tokopedia.qrscanner.branchio;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeepgoyal on 19/03/18.
 */

public class BranchIOAndroidDeepLink {
    @SerializedName("$android_deeplink_path")
    String androidDeeplinkPath;

    public String getAndroidDeeplinkPath() {
        return androidDeeplinkPath;
    }
}
