package com.tokopedia.sellerapp.deeplink;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.tokopedia.applink.DeeplinkMapper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.SessionApplinkModule;
import com.tokopedia.applink.SessionApplinkModuleLoader;
import com.tokopedia.changepassword.common.applink.ChangePasswordDeeplinkModule;
import com.tokopedia.changepassword.common.applink.ChangePasswordDeeplinkModuleLoader;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.deeplink.CoreDeeplinkModule;
import com.tokopedia.core.deeplink.CoreDeeplinkModuleLoader;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.developer_options.presentation.applink.RNDevOptionsApplinkModule;
import com.tokopedia.developer_options.presentation.applink.RNDevOptionsApplinkModuleLoader;
import com.tokopedia.flashsale.management.applink.FlashsaleDeeplinkModule;
import com.tokopedia.flashsale.management.applink.FlashsaleDeeplinkModuleLoader;
import com.tokopedia.gm.applink.GMApplinkModule;
import com.tokopedia.gm.applink.GMApplinkModuleLoader;
import com.tokopedia.inbox.deeplink.InboxDeeplinkModule;
import com.tokopedia.inbox.deeplink.InboxDeeplinkModuleLoader;
import com.tokopedia.loginregister.common.applink.LoginRegisterApplinkModule;
import com.tokopedia.loginregister.common.applink.LoginRegisterApplinkModuleLoader;
import com.tokopedia.phoneverification.applink.PhoneVerificationApplinkModule;
import com.tokopedia.phoneverification.applink.PhoneVerificationApplinkModuleLoader;
import com.tokopedia.product.detail.applink.ProductDetailApplinkModule;
import com.tokopedia.product.detail.applink.ProductDetailApplinkModuleLoader;
import com.tokopedia.seller.applink.SellerApplinkModule;
import com.tokopedia.seller.applink.SellerApplinkModuleLoader;
import com.tokopedia.sellerapp.SplashScreenActivity;
import com.tokopedia.sellerapp.applink.SellerappAplinkModule;
import com.tokopedia.sellerapp.applink.SellerappAplinkModuleLoader;
import com.tokopedia.sellerapp.deeplink.presenter.DeepLinkAnalyticsImpl;
import com.tokopedia.shop.applink.ShopAppLinkModule;
import com.tokopedia.shop.applink.ShopAppLinkModuleLoader;
import com.tokopedia.talk.common.applink.InboxTalkApplinkModule;
import com.tokopedia.talk.common.applink.InboxTalkApplinkModuleLoader;
import com.tokopedia.tkpd.tkpdreputation.applink.ReputationApplinkModule;
import com.tokopedia.tkpd.tkpdreputation.applink.ReputationApplinkModuleLoader;
import com.tokopedia.topads.applink.TopAdsApplinkModule;
import com.tokopedia.topads.applink.TopAdsApplinkModuleLoader;
import com.tokopedia.topchat.deeplink.TopChatAppLinkModule;
import com.tokopedia.topchat.deeplink.TopChatAppLinkModuleLoader;
import com.tokopedia.track.TrackApp;
import com.tokopedia.tracking.applink.TrackingAppLinkModule;
import com.tokopedia.tracking.applink.TrackingAppLinkModuleLoader;
import com.tokopedia.transaction.applink.TransactionApplinkModule;
import com.tokopedia.transaction.applink.TransactionApplinkModuleLoader;
import com.tokopedia.updateinactivephone.applink.ChangeInactivePhoneApplinkModule;
import com.tokopedia.updateinactivephone.applink.ChangeInactivePhoneApplinkModuleLoader;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.useridentification.applink.UserIdentificationApplinkModule;
import com.tokopedia.useridentification.applink.UserIdentificationApplinkModuleLoader;

import static com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.OPEN_SHOP;

/**
 * @author rizkyfadillah on 26/07/17.
 */
@DeepLinkHandler({
        SellerApplinkModule.class,
        TopAdsApplinkModule.class,
        TransactionApplinkModule.class,
        GMApplinkModule.class,
        SellerappAplinkModule.class,
        InboxDeeplinkModule.class,
        ShopAppLinkModule.class,
        TrackingAppLinkModule.class,
        TopChatAppLinkModule.class,
        CoreDeeplinkModule.class,
        ReputationApplinkModule.class,
        SessionApplinkModule.class,
        ProductDetailApplinkModule.class,
        InboxTalkApplinkModule.class,
        LoginRegisterApplinkModule.class,
        ChangeInactivePhoneApplinkModule.class,
        PhoneVerificationApplinkModule.class,
        ChangePasswordDeeplinkModule.class,
        UserIdentificationApplinkModule.class,
        FlashsaleDeeplinkModule.class,
        RNDevOptionsApplinkModule.class
})
/* **
 * Navigation will via RouteManager -> manifest instead.
 * Put new Deeplink directly into the target activity
 */
@Deprecated
public class DeepLinkHandlerActivity extends AppCompatActivity {


    public static DeepLinkDelegate getDelegateInstance() {
        return new DeepLinkDelegate(
                new SellerApplinkModuleLoader(),
                new TopAdsApplinkModuleLoader(),
                new TransactionApplinkModuleLoader(),
                new GMApplinkModuleLoader(),
                new SellerappAplinkModuleLoader(),
                new InboxDeeplinkModuleLoader(),
                new ShopAppLinkModuleLoader(),
                new TrackingAppLinkModuleLoader(),
                new TopChatAppLinkModuleLoader(),
                new CoreDeeplinkModuleLoader(),
                new ReputationApplinkModuleLoader(),
                new SessionApplinkModuleLoader(),
                new ProductDetailApplinkModuleLoader(),
                new InboxTalkApplinkModuleLoader(),
                new LoginRegisterApplinkModuleLoader(),
                new ChangeInactivePhoneApplinkModuleLoader(),
                new PhoneVerificationApplinkModuleLoader(),
                new ChangePasswordDeeplinkModuleLoader(),
                new UserIdentificationApplinkModuleLoader(),
                new FlashsaleDeeplinkModuleLoader(),
                new RNDevOptionsApplinkModuleLoader()
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
        Uri applink = getIntent().getData();
        presenter.processUTM(this, applink);

        if (applink == null) {
            return;
        }

        String applinkString = applink.toString();

        //map applink to internal if any
        String mappedDeeplink = DeeplinkMapper.getRegisteredNavigation(this, applinkString);
        if (TextUtils.isEmpty(mappedDeeplink)) {
            routeToApplink(deepLinkDelegate, applinkString);
        } else {
            routeToApplink(deepLinkDelegate, mappedDeeplink);
        }
    }

    private void routeToApplink(DeepLinkDelegate deepLinkDelegate, String applinkString) {
        if (deepLinkDelegate.supportsUri(applinkString)) {
            getIntent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            deepLinkDelegate.dispatchFrom(this, getIntent());
            if (getIntent().getExtras() != null) {
                Bundle bundle = getIntent().getExtras();
                eventPersonalizedClicked(bundle.getString(Constants.EXTRA_APPLINK_CATEGORY));
            }
        } else {
            Intent intent = RouteManager.getIntent(this, applinkString);
            startActivity(intent);
            this.finish();
        }
    }


    public void eventPersonalizedClicked(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.Event.OPEN_PUSH_NOTIFICATION,
                AppEventTracking.Category.PUSH_NOTIFICATION,
                AppEventTracking.Action.OPEN,
                label);
    }

    public static Intent moveToCreateShop(Context context) {
        if (context == null)
            return null;

        Intent intent = RouteManager.getIntent(context,OPEN_SHOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @DeepLink(Constants.Applinks.SellerApp.BROWSER)
    public static Intent getCallingIntentOpenBrowser(Context context, Bundle extras) {
        String webUrl = extras.getString(
                Constants.ARG_NOTIFICATION_URL, TokopediaUrl.Companion.getInstance().getWEB()
        );
        Intent destination = new Intent(Intent.ACTION_VIEW);
        destination.setData(Uri.parse(webUrl));
        return destination;
    }
}
