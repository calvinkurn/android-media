package com.tokopedia.tkpd.deeplink;

/**
 * Created by Rizky on 04/01/18.
 */

public class WhitelistItem {
    public String path;
    public String applink;

    public WhitelistItem(String path, String applink) {
        this.path = path;
        this.applink = applink;
    }
}
