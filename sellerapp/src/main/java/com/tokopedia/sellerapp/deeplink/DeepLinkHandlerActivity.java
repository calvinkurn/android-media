package com.tokopedia.sellerapp.deeplink;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.tokopedia.applink.SessionApplinkModule;
import com.tokopedia.applink.SessionApplinkModuleLoader;
import com.tokopedia.chatbot.applink.ChatbotApplinkModule;
import com.tokopedia.chatbot.applink.ChatbotApplinkModuleLoader;
import com.tokopedia.changepassword.common.applink.ChangePasswordDeeplinkModule;
import com.tokopedia.changepassword.common.applink.ChangePasswordDeeplinkModuleLoader;
import com.tokopedia.contact_us.applink.CustomerCareApplinkModule;
import com.tokopedia.contact_us.applink.CustomerCareApplinkModuleLoader;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.deeplink.CoreDeeplinkModule;
import com.tokopedia.core.deeplink.CoreDeeplinkModuleLoader;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.gm.applink.GMApplinkModule;
import com.tokopedia.gm.applink.GMApplinkModuleLoader;
import com.tokopedia.inbox.deeplink.InboxDeeplinkModule;
import com.tokopedia.inbox.deeplink.InboxDeeplinkModuleLoader;
import com.tokopedia.loginregister.common.applink.LoginRegisterApplinkModule;
import com.tokopedia.loginregister.common.applink.LoginRegisterApplinkModuleLoader;
import com.tokopedia.phoneverification.applink.PhoneVerificationApplinkModule;
import com.tokopedia.phoneverification.applink.PhoneVerificationApplinkModuleLoader;
import com.tokopedia.product.manage.item.utils.ProductAddDeeplinkModule;
import com.tokopedia.product.manage.item.utils.ProductAddDeeplinkModuleLoader;
import com.tokopedia.profile.applink.ProfileApplinkModule;
import com.tokopedia.profile.applink.ProfileApplinkModuleLoader;
import com.tokopedia.seller.applink.SellerApplinkModule;
import com.tokopedia.seller.applink.SellerApplinkModuleLoader;
import com.tokopedia.sellerapp.SplashScreenActivity;
import com.tokopedia.sellerapp.applink.SellerappAplinkModule;
import com.tokopedia.sellerapp.applink.SellerappAplinkModuleLoader;
import com.tokopedia.sellerapp.deeplink.presenter.DeepLinkAnalyticsImpl;
import com.tokopedia.settingbank.applink.SettingBankApplinkModule;
import com.tokopedia.settingbank.applink.SettingBankApplinkModuleLoader;
import com.tokopedia.shop.applink.ShopAppLinkModule;
import com.tokopedia.shop.applink.ShopAppLinkModuleLoader;
import com.tokopedia.talk.common.applink.InboxTalkApplinkModule;
import com.tokopedia.talk.common.applink.InboxTalkApplinkModuleLoader;
import com.tokopedia.tkpd.tkpdreputation.applink.ReputationApplinkModule;
import com.tokopedia.tkpd.tkpdreputation.applink.ReputationApplinkModuleLoader;
import com.tokopedia.product.detail.applink.ProductDetailApplinkModule;
import com.tokopedia.product.detail.applink.ProductDetailApplinkModuleLoader;
import com.tokopedia.topads.applink.TopAdsApplinkModule;
import com.tokopedia.topads.applink.TopAdsApplinkModuleLoader;
import com.tokopedia.topads.dashboard.data.applink.TopAdsDashboardApplinkModule;
import com.tokopedia.topads.dashboard.data.applink.TopAdsDashboardApplinkModuleLoader;
import com.tokopedia.topchat.deeplink.TopChatAppLinkModule;
import com.tokopedia.topchat.deeplink.TopChatAppLinkModuleLoader;
import com.tokopedia.tracking.applink.TrackingAppLinkModule;
import com.tokopedia.tracking.applink.TrackingAppLinkModuleLoader;
import com.tokopedia.transaction.applink.TransactionApplinkModule;
import com.tokopedia.transaction.applink.TransactionApplinkModuleLoader;
import com.tokopedia.updateinactivephone.applink.ChangeInactivePhoneApplinkModule;
import com.tokopedia.updateinactivephone.applink.ChangeInactivePhoneApplinkModuleLoader;
import com.tokopedia.useridentification.applink.UserIdentificationApplinkModule;
import com.tokopedia.useridentification.applink.UserIdentificationApplinkModuleLoader;

/**
 * @author rizkyfadillah on 26/07/17.
 */
@DeepLinkHandler({
        SellerApplinkModule.class,
        TopAdsDashboardApplinkModule.class,
        TopAdsApplinkModule.class,
        TransactionApplinkModule.class,
        GMApplinkModule.class,
        SellerappAplinkModule.class,
        InboxDeeplinkModule.class,
        ShopAppLinkModule.class,
        ProfileApplinkModule.class,
        TrackingAppLinkModule.class,
        ProductAddDeeplinkModule.class,
        TopChatAppLinkModule.class,
        CoreDeeplinkModule.class,
        CustomerCareApplinkModule.class,
        ReputationApplinkModule.class,
        SessionApplinkModule.class,
        ProductDetailApplinkModule.class,
        SettingBankApplinkModule.class,
        InboxTalkApplinkModule.class,
        LoginRegisterApplinkModule.class,
        ChangeInactivePhoneApplinkModule.class,
        PhoneVerificationApplinkModule.class,
        ChangePasswordDeeplinkModule.class,
        UserIdentificationApplinkModule.class,
        ChatbotApplinkModule.class
})

public class DeepLinkHandlerActivity extends AppCompatActivity {


    public static DeepLinkDelegate getDelegateInstance() {
        return new DeepLinkDelegate(
                new SellerApplinkModuleLoader(),
                new TopAdsDashboardApplinkModuleLoader(),
                new TopAdsApplinkModuleLoader(),
                new TransactionApplinkModuleLoader(),
                new GMApplinkModuleLoader(),
                new SellerappAplinkModuleLoader(),
                new InboxDeeplinkModuleLoader(),
                new ShopAppLinkModuleLoader(),
                new ProfileApplinkModuleLoader(),
                new TrackingAppLinkModuleLoader(),
                new ProductAddDeeplinkModuleLoader(),
                new TopChatAppLinkModuleLoader(),
                new CoreDeeplinkModuleLoader(),
                new CustomerCareApplinkModuleLoader(),
                new ReputationApplinkModuleLoader(),
                new SessionApplinkModuleLoader(),
                new ProductDetailApplinkModuleLoader(),
                new SettingBankApplinkModuleLoader(),
                new InboxTalkApplinkModuleLoader(),
                new LoginRegisterApplinkModuleLoader(),
                new ChangeInactivePhoneApplinkModuleLoader(),
                new PhoneVerificationApplinkModuleLoader(),
                new ChangePasswordDeeplinkModuleLoader(),
                new UserIdentificationApplinkModuleLoader(),
                new ChatbotApplinkModuleLoader()
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
        presenter.processUTM(this, applink);
        if (deepLinkDelegate.supportsUri(applink.toString())) {
            deepLinkDelegate.dispatchFrom(this, intent);
            if (getIntent().getExtras() != null) {
                Bundle bundle = getIntent().getExtras();
                UnifyTracking.eventPersonalizedClicked(this, bundle.getString(Constants.EXTRA_APPLINK_CATEGORY));
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
