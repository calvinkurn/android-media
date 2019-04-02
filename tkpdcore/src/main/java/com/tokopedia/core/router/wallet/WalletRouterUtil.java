package com.tokopedia.core.router.wallet;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

/**
 * @author anggaprasetiyo on 9/25/17.
 */

public class WalletRouterUtil {

    /**
     * @param originSource
     * @param requestCode
     * @param appLinkScheme : using for directing page to native wallet page
     * @param alternateRedirectUrl : using for redirect url and want to show to webview
     * @param bundlePass
     */
    public static void navigateWallet(Application application,
                                      Object originSource,
                                      int requestCode,
                                      String appLinkScheme,
                                      String alternateRedirectUrl,
                                      Bundle bundlePass) {
        if (application instanceof IWalletRouter) {
            IWalletRouter walletRouter = (IWalletRouter) application;
            if (originSource instanceof Activity) {
                walletRouter.navigateAppLinkWallet(
                        (Activity) originSource, requestCode, appLinkScheme, alternateRedirectUrl,
                        bundlePass
                );
            } else if (originSource instanceof Context) {
                walletRouter.navigateAppLinkWallet(
                        (Context) originSource, appLinkScheme, alternateRedirectUrl,
                        bundlePass
                );
            } else if (originSource instanceof Fragment) {
                walletRouter.navigateAppLinkWallet(
                        (Fragment) originSource, requestCode, appLinkScheme, alternateRedirectUrl,
                        bundlePass
                );
            } else if (originSource instanceof android.support.v4.app.Fragment) {
                walletRouter.navigateAppLinkWallet(
                        (android.support.v4.app.Fragment) originSource, requestCode,
                        appLinkScheme, alternateRedirectUrl, bundlePass
                );
            } else {
                throw new Exception("Origin source param must be instance of ACTIVITY" +
                        "or FRAGMENT or SUPPORTFRAGMENT or CONTEXT");
            }
        } else {
            throw new Exception("Main application must implement IWalletRouter");
        }
    }

    public static class Exception extends RuntimeException {
        private static final long serialVersionUID = -6279191037799996371L;

        public Exception(String message) {
            super(message);
        }
    }
}
