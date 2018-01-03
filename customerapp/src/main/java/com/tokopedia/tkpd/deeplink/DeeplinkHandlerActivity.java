package com.tokopedia.tkpd.deeplink;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.tokopedia.applink.SessionApplinkModule;
import com.tokopedia.applink.SessionApplinkModuleLoader;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.deeplink.CoreDeeplinkModule;
import com.tokopedia.core.deeplink.CoreDeeplinkModuleLoader;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.digital.applink.DigitalApplinkModule;
import com.tokopedia.digital.applink.DigitalApplinkModuleLoader;
import com.tokopedia.discovery.applink.DiscoveryApplinkModule;
import com.tokopedia.discovery.applink.DiscoveryApplinkModuleLoader;
import com.tokopedia.flight.applink.FlightApplinkModule;
import com.tokopedia.flight.applink.FlightApplinkModuleLoader;
import com.tokopedia.inbox.deeplink.InboxDeeplinkModule;
import com.tokopedia.inbox.deeplink.InboxDeeplinkModuleLoader;
import com.tokopedia.ride.deeplink.RideDeeplinkModule;
import com.tokopedia.ride.deeplink.RideDeeplinkModuleLoader;
import com.tokopedia.seller.applink.SellerApplinkModule;
import com.tokopedia.seller.applink.SellerApplinkModuleLoader;
import com.tokopedia.tkpd.deeplink.presenter.DeepLinkAnalyticsImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.deeplink.FeedDeeplinkModule;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.deeplink.FeedDeeplinkModuleLoader;
import com.tokopedia.tkpd.tkpdreputation.applink.ReputationApplinkModule;
import com.tokopedia.tkpd.tkpdreputation.applink.ReputationApplinkModuleLoader;
import com.tokopedia.tkpdpdp.applink.PdpApplinkModule;
import com.tokopedia.tkpdpdp.applink.PdpApplinkModuleLoader;
import com.tokopedia.transaction.applink.TransactionApplinkModule;
import com.tokopedia.transaction.applink.TransactionApplinkModuleLoader;

import io.branch.referral.Branch;

@DeepLinkHandler({
        ConsumerDeeplinkModule.class,
        CoreDeeplinkModule.class,
        InboxDeeplinkModule.class,
        SellerApplinkModule.class,
        TransactionApplinkModule.class,
        DigitalApplinkModule.class,
        PdpApplinkModule.class,
        RideDeeplinkModule.class,
        DiscoveryApplinkModule.class,
        SessionApplinkModule.class,
        FeedDeeplinkModule.class,
        FlightApplinkModule.class,
        ReputationApplinkModule.class
})

public class DeeplinkHandlerActivity extends AppCompatActivity {

    public static DeepLinkDelegate getDelegateInstance() {
        return new DeepLinkDelegate(
                new ConsumerDeeplinkModuleLoader(),
                new CoreDeeplinkModuleLoader(),
                new InboxDeeplinkModuleLoader(),
                new SellerApplinkModuleLoader(),
                new TransactionApplinkModuleLoader(),
                new DigitalApplinkModuleLoader(),
                new PdpApplinkModuleLoader(),
                new RideDeeplinkModuleLoader(),
                new DiscoveryApplinkModuleLoader(),
                new SessionApplinkModuleLoader(),
                new FeedDeeplinkModuleLoader(),
                new FlightApplinkModuleLoader(),
                new ReputationApplinkModuleLoader()
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Branch.getInstance() != null) {
            Branch.getInstance().setRequestMetadata("$google_analytics_client_id", TrackingUtils.getClientID());
            Branch.getInstance().initSession(this);
        }
        DeepLinkDelegate deepLinkDelegate = getDelegateInstance();
        DeepLinkAnalyticsImpl presenter = new DeepLinkAnalyticsImpl();
        if (getIntent() != null) {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri applink = Uri.parse(intent.getData().toString().replaceAll("%", "%25"));
            presenter.processUTM(applink);
            if (deepLinkDelegate.supportsUri(applink.toString())) {
                Intent homeIntent = HomeRouter.getHomeActivityInterfaceRouter(this);
                homeIntent.putExtra(HomeRouter.EXTRA_APPLINK, applink.toString());
                startActivity(homeIntent);
            } else {
                Intent homeIntent = HomeRouter.getHomeActivityInterfaceRouter(this);
                homeIntent.putExtra(HomeRouter.EXTRA_APPLINK_UNSUPPORTED, true);
                startActivity(homeIntent);
            }

            if (getIntent().getExtras() != null) {
                Bundle bundle = getIntent().getExtras();
                if (bundle.getBoolean(Constants.EXTRA_PUSH_PERSONALIZATION, false)) {
                    UnifyTracking.eventPersonalizedClicked(bundle.getString(Constants.EXTRA_APPLINK_CATEGORY));
                }
//                NotificationModHandler.clearCacheIfFromNotification(bundle.getString(Constants.EXTRA_APPLINK_CATEGORY));
            }
        }
        finish();
    }


    @DeepLink(Constants.Applinks.SellerApp.SELLER_APP_HOME)
    public static Intent getCallingIntentSellerAppHome(Context context, Bundle extras) {
        Intent launchIntent = context.getPackageManager()
                .getLaunchIntentForPackage(GlobalConfig.PACKAGE_SELLER_APP);

        if (launchIntent == null) {
            launchIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(Constants.URL_MARKET + GlobalConfig.PACKAGE_SELLER_APP)
            );
        }
        return launchIntent;
    }

    @DeepLink(Constants.Applinks.BROWSER)
    public static Intent getCallingIntentOpenBrowser(Context context, Bundle extras) {
        String webUrl = extras.getString(
                Constants.ARG_NOTIFICATION_URL, TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL
        );
        Intent destination = new Intent(Intent.ACTION_VIEW);
        destination.setData(Uri.parse(webUrl));
        return destination;
    }
}
