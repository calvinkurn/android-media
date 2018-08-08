package com.tokopedia.core.router.wallet;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

/**
 * @author anggaprasetiyo on 9/20/17.
 */

public interface IWalletRouter {
    int DEFAULT_WALLET_APPLINK_REQUEST_CODE = 111;

    /**
     * @param context              source origin is base context app
     * @param appLinkScheme        uri app link scheme
     * @param alternateRedirectUrl alternate uri webview if applink not found
     * @param bundlePass           bundle pass data
     */
    void navigateAppLinkWallet(Context context,
                               String appLinkScheme,
                               String alternateRedirectUrl,
                               Bundle bundlePass);

    /**
     * @param activity             source origin is support Activity
     * @param requestCode          request code for callback
     * @param appLinkScheme        uri app link scheme
     * @param alternateRedirectUrl alternate uri webview if applink not found
     * @param bundlePass           bundle pass data
     */
    void navigateAppLinkWallet(Activity activity,
                               int requestCode,
                               String appLinkScheme,
                               String alternateRedirectUrl,
                               Bundle bundlePass);

    /**
     * @param fragment             source origin is support fragment native
     * @param requestCode          request code for callback
     * @param appLinkScheme        uri app link scheme
     * @param alternateRedirectUrl alternate uri webview if applink not found
     * @param bundlePass           bundle pass data
     */
    void navigateAppLinkWallet(Fragment fragment,
                               int requestCode,
                               String appLinkScheme,
                               String alternateRedirectUrl,
                               Bundle bundlePass);

    /**
     * @param fragmentSupport      source origin is support fragment
     * @param requestCode          request code for callback
     * @param appLinkScheme        uri app link scheme
     * @param alternateRedirectUrl alternate uri webview if applink not found
     * @param bundlePass           bundle pass data
     */
    void navigateAppLinkWallet(android.support.v4.app.Fragment fragmentSupport,
                               int requestCode,
                               String appLinkScheme,
                               String alternateRedirectUrl,
                               Bundle bundlePass);

}
