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
import com.tokopedia.core.gcm.utils.ApplinkUtils;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.BranchSdkUtils;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.digital.applink.DigitalApplinkModule;
import com.tokopedia.digital.applink.DigitalApplinkModuleLoader;
import com.tokopedia.discovery.applink.DiscoveryApplinkModule;
import com.tokopedia.discovery.applink.DiscoveryApplinkModuleLoader;
import com.tokopedia.events.deeplink.EventsDeepLinkModule;
import com.tokopedia.events.deeplink.EventsDeepLinkModuleLoader;
import com.tokopedia.flight.applink.FlightApplinkModule;
import com.tokopedia.flight.applink.FlightApplinkModuleLoader;
import com.tokopedia.home.applink.HomeApplinkModule;
import com.tokopedia.home.applink.HomeApplinkModuleLoader;
import com.tokopedia.inbox.deeplink.InboxDeeplinkModule;
import com.tokopedia.inbox.deeplink.InboxDeeplinkModuleLoader;
import com.tokopedia.loyalty.applink.LoyaltyAppLinkModule;
import com.tokopedia.loyalty.applink.LoyaltyAppLinkModuleLoader;
import com.tokopedia.ride.deeplink.RideDeeplinkModule;
import com.tokopedia.ride.deeplink.RideDeeplinkModuleLoader;
import com.tokopedia.seller.applink.SellerApplinkModule;
import com.tokopedia.seller.applink.SellerApplinkModuleLoader;
import com.tokopedia.tkpd.deeplink.presenter.DeepLinkAnalyticsImpl;
import com.tokopedia.tkpd.redirect.RedirectCreateShopActivity;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.deeplink.FeedDeeplinkModule;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.deeplink.FeedDeeplinkModuleLoader;
import com.tokopedia.tkpd.tkpdreputation.applink.ReputationApplinkModule;
import com.tokopedia.tkpd.tkpdreputation.applink.ReputationApplinkModuleLoader;
import com.tokopedia.tkpdpdp.applink.PdpApplinkModule;
import com.tokopedia.tkpdpdp.applink.PdpApplinkModuleLoader;
import com.tokopedia.tokocash.applink.TokoCashApplinkModule;
import com.tokopedia.tokocash.applink.TokoCashApplinkModuleLoader;
import com.tokopedia.transaction.applink.TransactionApplinkModule;
import com.tokopedia.transaction.applink.TransactionApplinkModuleLoader;

import org.json.JSONObject;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

@DeepLinkHandler({
        ConsumerDeeplinkModule.class,
        CoreDeeplinkModule.class,
        InboxDeeplinkModule.class,
        SellerApplinkModule.class,
        TransactionApplinkModule.class,
        DigitalApplinkModule.class,
        PdpApplinkModule.class,
        HomeApplinkModule.class,
        RideDeeplinkModule.class,
        DiscoveryApplinkModule.class,
        SessionApplinkModule.class,
        FeedDeeplinkModule.class,
        FlightApplinkModule.class,
        ReputationApplinkModule.class,
        TokoCashApplinkModule.class,
        EventsDeepLinkModule.class,
        LoyaltyAppLinkModule.class
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
                new HomeApplinkModuleLoader(),
                new RideDeeplinkModuleLoader(),
                new DiscoveryApplinkModuleLoader(),
                new SessionApplinkModuleLoader(),
                new FeedDeeplinkModuleLoader(),
                new FlightApplinkModuleLoader(),
                new ReputationApplinkModuleLoader(),
                new TokoCashApplinkModuleLoader(),
                new EventsDeepLinkModuleLoader(),
                new LoyaltyAppLinkModuleLoader()
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBranchSession();
        DeepLinkDelegate deepLinkDelegate = getDelegateInstance();
        DeepLinkAnalyticsImpl presenter = new DeepLinkAnalyticsImpl();
        if (getIntent() != null) {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri applink = Uri.parse(intent.getData().toString().replaceAll("%", "%25"));
            presenter.processUTM(applink);
            Intent homeIntent = HomeRouter.getHomeActivityInterfaceRouter(this);
            if (deepLinkDelegate.supportsUri(applink.toString())) {
                homeIntent.putExtra(HomeRouter.EXTRA_APPLINK, applink.toString());
            } else {
                homeIntent.putExtra(HomeRouter.EXTRA_APPLINK_UNSUPPORTED, true);
            }

            if (getIntent() != null && getIntent().getExtras() != null)
                homeIntent.putExtras(getIntent().getExtras());
            startActivity(homeIntent);

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

    @DeepLink({Constants.Applinks.SellerApp.TOPADS_DASHBOARD,
            Constants.Applinks.SellerApp.PRODUCT_ADD,
            Constants.Applinks.SellerApp.SALES,
            Constants.Applinks.SellerApp.TOPADS_CREDIT,
            Constants.Applinks.SellerApp.TOPADS_PRODUCT_CREATE,
            Constants.Applinks.SellerApp.GOLD_MERCHANT,
            Constants.Applinks.SellerApp.TOPADS_DASHBOARD,
            Constants.Applinks.SellerApp.TOPADS_PRODUCT_DETAIL,
            Constants.Applinks.SellerApp.TOPADS_PRODUCT_DETAIL_CONSTS,
            Constants.Applinks.SellerApp.BROWSER})
    public static Intent getIntentSellerApp(Context context, Bundle extras) {
        Intent launchIntent = context.getPackageManager()
                .getLaunchIntentForPackage(GlobalConfig.PACKAGE_SELLER_APP);

        if (launchIntent == null) {
            return RedirectCreateShopActivity.getCallingIntent(context);
        } else {
            return ApplinkUtils.getSellerAppApplinkIntent(context, extras);
        }
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

    private void initBranchSession() {
        Branch branch = Branch.getInstance();
        if (branch != null) {
            branch.setRequestMetadata("$google_analytics_client_id", TrackingUtils.getClientID());
            branch.initSession(new Branch.BranchReferralInitListener() {
                @Override
                public void onInitFinished(JSONObject referringParams, BranchError error) {
                    if (error == null) {
                        BranchSdkUtils.storeWebToAppPromoCodeIfExist(referringParams,DeeplinkHandlerActivity.this);
                    }
                }
            }, this.getIntent().getData(), this);
        }
    }
}
