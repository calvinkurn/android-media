package com.tokopedia.sellerapp.deeplink;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.applink.DigitalApplinkModule;
import com.tokopedia.digital.applink.DigitalApplinkModuleLoader;
import com.tokopedia.gm.applink.GMApplinkModule;
import com.tokopedia.gm.applink.GMApplinkModuleLoader;
import com.tokopedia.inbox.deeplink.InboxDeeplinkModule;
import com.tokopedia.inbox.deeplink.InboxDeeplinkModuleLoader;
import com.tokopedia.seller.applink.SellerApplinkModule;
import com.tokopedia.seller.applink.SellerApplinkModuleLoader;
import com.tokopedia.sellerapp.SplashScreenActivity;
import com.tokopedia.sellerapp.applink.SellerappAplinkModule;
import com.tokopedia.sellerapp.applink.SellerappAplinkModuleLoader;
import com.tokopedia.sellerapp.deeplink.presenter.DeepLinkAnalyticsImpl;
import com.tokopedia.topads.applink.TopAdsApplinkModule;
import com.tokopedia.topads.applink.TopAdsApplinkModuleLoader;

/**
 * @author rizkyfadillah on 26/07/17.
 */
@DeepLinkHandler({
        DigitalApplinkModule.class,
        SellerApplinkModule.class,
        TopAdsApplinkModule.class,
        GMApplinkModule.class,
        SellerappAplinkModule.class,
        InboxDeeplinkModule.class
})
public class DeepLinkHandlerActivity extends AppCompatActivity {

    public static DeepLinkDelegate getDelegateInstance() {
        return new DeepLinkDelegate(
                new DigitalApplinkModuleLoader(),
                new SellerApplinkModuleLoader(),
                new TopAdsApplinkModuleLoader(),
                new GMApplinkModuleLoader(),
                new SellerappAplinkModuleLoader(),
                new InboxDeeplinkModuleLoader()
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeepLinkDelegate deepLinkDelegate = getDelegateInstance();
        DeepLinkAnalyticsImpl presenter = new DeepLinkAnalyticsImpl();
        if (getIntent() != null) {
            if (!SessionHandler.isV4Login(this) || !SessionHandler.isUserHasShop(this)) {
                if (SessionHandler.isV4Login(this)) {
                    startActivity(moveToCreateShop(this));
                } else {
                    startActivity(new Intent(this, SplashScreenActivity.class));
                }
            } else {
                processApplink(deepLinkDelegate, presenter);
            }

        }
        finish();
    }

    private void processApplink(DeepLinkDelegate deepLinkDelegate, DeepLinkAnalyticsImpl presenter) {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri applink = intent.getData();
        presenter.processUTM(applink);
        if (deepLinkDelegate.supportsUri(applink.toString())) {
            deepLinkDelegate.dispatchFrom(this, intent);
            if (getIntent().getExtras() != null) {
                Bundle bundle = getIntent().getExtras();
                UnifyTracking.eventPersonalizedClicked(bundle.getString(Constants.EXTRA_APPLINK_CATEGORY));
            }
        }
    }

    public static Intent moveToCreateShop(Context context) {
        if (context == null)
            return null;

        Intent intent = SellerRouter.getActivityShopCreateEdit(context);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @DeepLink(Constants.Applinks.SellerApp.BROWSER)
    public static Intent getCallingIntentOpenBrowser(Context context, Bundle extras) {
        String webUrl = extras.getString(
                Constants.ARG_NOTIFICATION_URL, TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL
        );
        Intent destination = new Intent(Intent.ACTION_VIEW);
        destination.setData(Uri.parse(webUrl));
        return destination;
    }
}
