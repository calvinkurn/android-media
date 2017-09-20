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

    void navigateAppLinkWallet(Context context,
                               String appLinkScheme,
                               String alternateRedirectUrl,
                               Bundle bundlePass);

    void navigateAppLinkWallet(Activity activity,
                               int requestCode,
                               String appLinkScheme,
                               String alternateRedirectUrl,
                               Bundle bundlePass);

    void navigateAppLinkWallet(Fragment fragment,
                               int requestCode,
                               String appLinkScheme,
                               String alternateRedirectUrl,
                               Bundle bundlePass);

    void navigateAppLinkWallet(android.support.v4.app.Fragment fragmentSupport,
                               int requestCode,
                               String appLinkScheme,
                               String alternateRedirectUrl,
                               Bundle bundlePass);

}
