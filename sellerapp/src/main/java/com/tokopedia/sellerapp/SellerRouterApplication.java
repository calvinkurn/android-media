package com.tokopedia.sellerapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.module.ActivityModule;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.view.subscriber.ProfileCompletionSubscriber;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.inboxreputation.listener.SellerFragmentReputation;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.TkpdFragmentWrapper;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.welcome.WelcomeActivity;
import com.tokopedia.digital.cart.activity.CartDigitalActivity;
import com.tokopedia.digital.product.activity.DigitalProductActivity;
import com.tokopedia.digital.product.activity.DigitalWebActivity;
import com.tokopedia.digital.tokocash.activity.ActivateTokoCashActivity;
import com.tokopedia.digital.widget.activity.DigitalCategoryListActivity;
import com.tokopedia.payment.router.IPaymentModuleRouter;
import com.tokopedia.profilecompletion.data.factory.ProfileSourceFactory;
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper;
import com.tokopedia.profilecompletion.data.repository.ProfileRepositoryImpl;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.common.logout.TkpdSellerLogout;
import com.tokopedia.seller.gmsubscribe.view.activity.GmSubscribeHomeActivity;
import com.tokopedia.seller.goldmerchant.common.di.component.DaggerGoldMerchantComponent;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.goldmerchant.common.di.module.GoldMerchantModule;
import com.tokopedia.seller.instoped.InstopedActivity;
import com.tokopedia.seller.instoped.presenter.InstagramMediaPresenterImpl;
import com.tokopedia.seller.myproduct.ManageProductSeller;
import com.tokopedia.seller.myproduct.presenter.AddProductPresenterImpl;
import com.tokopedia.seller.product.common.di.component.DaggerProductComponent;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.common.di.module.ProductModule;
import com.tokopedia.seller.product.edit.view.activity.ProductEditActivity;
import com.tokopedia.seller.reputation.view.fragment.SellerReputationFragment;
import com.tokopedia.seller.shopsettings.etalase.activity.EtalaseShopEditor;
import com.tokopedia.sellerapp.deeplink.DeepLinkHandlerActivity;
import com.tokopedia.sellerapp.deeplink.DeepLinkDelegate;
import com.tokopedia.sellerapp.drawer.DrawerSellerHelper;
import com.tokopedia.sellerapp.home.view.SellerHomeActivity;
import com.tokopedia.session.session.activity.Login;
import com.tokopedia.tkpdpdp.ProductInfoActivity;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import java.util.Map;

import static com.tokopedia.core.router.productdetail.ProductDetailRouter.ARG_FROM_DEEPLINK;
import static com.tokopedia.core.router.productdetail.ProductDetailRouter.ARG_PARAM_PRODUCT_PASS_DATA;

/**
 * Created by normansyahputa on 12/15/16.
 */

public class SellerRouterApplication extends MainApplication
        implements TkpdCoreRouter, SellerModuleRouter, SellerFragmentReputation, PdpRouter,
        IPaymentModuleRouter, IDigitalModuleRouter {
    public static final String COM_TOKOPEDIA_SELLERAPP_HOME_VIEW_SELLER_HOME_ACTIVITY = "com.tokopedia.sellerapp.home.view.SellerHomeActivity";
    public static final String COM_TOKOPEDIA_CORE_WELCOME_WELCOME_ACTIVITY = "com.tokopedia.core.welcome.WelcomeActivity";

    private DaggerProductComponent.Builder daggerProductBuilder;
    private ProductComponent productComponent;

    private DaggerGoldMerchantComponent.Builder daggerGoldMerchantBuilder;
    private GoldMerchantComponent goldMerchantComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeDagger();
    }

    private void initializeDagger() {
        daggerGoldMerchantBuilder = DaggerGoldMerchantComponent.builder().goldMerchantModule(new GoldMerchantModule());
        daggerProductBuilder = DaggerProductComponent.builder().productModule(new ProductModule());
    }

    @Override
    public ProductComponent getProductComponent(ActivityModule activityModule) {
        if (productComponent == null) {
            productComponent = daggerProductBuilder.appComponent(getApplicationComponent(activityModule)).build();
        }
        return productComponent;
    }

    public GoldMerchantComponent getGoldMerchantComponent(ActivityModule activityModule) {
        if (goldMerchantComponent == null) {
            goldMerchantComponent = daggerGoldMerchantBuilder.appComponent(getApplicationComponent(activityModule)).build();
        }
        return goldMerchantComponent;
    }

    @Override
    public void startInstopedActivity(Context context) {
        InstopedActivity.startInstopedActivity(context);
    }

    @Override
    public void startInstopedActivityForResult(Activity activity, int resultCode, int maxResult) {
        InstopedActivity.startInstopedActivityForResult(activity, resultCode, maxResult);
    }

    @Override
    public void startInstopedActivityForResult(Context context, Fragment fragment, int resultCode, int maxResult) {
        InstopedActivity.startInstopedActivityForResult(context, fragment, resultCode, maxResult);
    }

    @Override
    public void removeInstopedToken() {
        InstagramMediaPresenterImpl.removeToken();
    }

    @Override
    public void goToManageProduct(Context context) {
        Intent intent = new Intent(context, ManageProductSeller.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void goToManageEtalase(Context context) {
        Intent intent = new Intent(context, EtalaseShopEditor.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void clearEtalaseCache() {
        AddProductPresenterImpl.clearEtalaseCache(getApplicationContext());
    }

    @Override
    public Intent goToEditProduct(Context context, boolean isEdit, String productId) {
        return ProductEditActivity.createInstance(context, productId);
    }

    @Override
    public void resetAddProductCache(Context context) {
        AddProductPresenterImpl.clearEtalaseCache(context);
        AddProductPresenterImpl.clearDepartementCache(context);
    }

    @Override
    public void goToWallet(Context context, String url) {
        //no route to wallet on seller, go to default
        goToDefaultRoute(context);
    }

    @Override
    public void goToMerchantRedirect(Context context) {
        Intent intent = GmSubscribeHomeActivity.getCallingIntent(context);
        context.startActivity(intent);
    }

    @Override
    public void actionAppLink(Context context, String linkUrl) {

    }

    @Override
    public void goToCreateMerchantRedirect(Context context) {
        //no route to merchant redirect on seller, go to default
        goToDefaultRoute(context);
    }

    @Override
    public void getUserInfo(RequestParams empty, ProfileCompletionSubscriber profileSubscriber) {
        Bundle bundle = new Bundle();
        SessionHandler sessionHandler = new SessionHandler(this);
        String authKey = sessionHandler.getAccessToken(this);
        authKey = sessionHandler.getTokenType(this) + " " + authKey;
        bundle.putString(AccountsService.AUTH_KEY, authKey);

        AccountsService accountsService = new AccountsService(bundle);

        ProfileSourceFactory profileSourceFactory =
                new ProfileSourceFactory(
                        this,
                        accountsService,
                        new GetUserInfoMapper(),
                        null
                );

        GetUserInfoUseCase getUserInfoUseCase = new GetUserInfoUseCase(
                new JobExecutor(),
                new UIThread(),
                new ProfileRepositoryImpl(profileSourceFactory)
        );

        getUserInfoUseCase.execute(GetUserInfoUseCase.generateParam(), profileSubscriber);
    }

    @Override
    public Intent getHomeIntent(Context context) {
        if (SessionHandler.isV4Login(context)) {
            return new Intent(context, SellerHomeActivity.class);
        } else {
            return new Intent(context, WelcomeActivity.class);
        }
    }

    @Override
    public Class<?> getHomeClass(Context context) throws ClassNotFoundException {
        if (SessionHandler.isV4Login(context)) {
            return Class.forName(COM_TOKOPEDIA_SELLERAPP_HOME_VIEW_SELLER_HOME_ACTIVITY);
        } else {
            return Class.forName(COM_TOKOPEDIA_CORE_WELCOME_WELCOME_ACTIVITY);
        }
    }

    @Override
    public DrawerHelper getDrawer(AppCompatActivity activity,
                                  SessionHandler sessionHandler,
                                  LocalCacheHandler drawerCache,
                                  GlobalCacheManager globalCacheManager) {
        return DrawerSellerHelper.createInstance(activity, sessionHandler, drawerCache);
    }

    @Override
    public void onLogout(AppComponent appComponent) {
        TkpdSellerLogout.onLogOut(appComponent);
    }

    @Override
    public void goToRegister(Context context) {
        Intent intent = Login.getSellerRegisterIntent(context);
        context.startActivity(intent);
    }

    @Override
    public Intent getLoginIntent(Context context) {
        Intent intent = Login.getCallingIntent(context);
        return intent;
    }

    @Override
    public Intent getRegisterIntent(Context context) {
        Intent intent = Login.getSellerRegisterIntent(context);
        return intent;
    }

    @Override
    public void goToProfileCompletion(Context context) {
        Intent intent = new Intent(context, ProfileCompletionActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void goToHome(Context context) {
        Intent intent = getHomeIntent(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void gotToProductDetail(Context context) {
        Intent intent = ProductInfoActivity.createInstance(context);
        context.startActivity(intent);
    }

    @Override
    public void goToProductDetail(Context context, String productUrl) {
        DeepLinkChecker.openProduct(productUrl, context);
    }

    @Override
    public void goToProductDetail(Context context, ProductPass productPass) {
        Intent intent = ProductInfoActivity.createInstance(context, productPass);
        context.startActivity(intent);
    }

    @Override
    public void goToProductDetail(Context context, ShareData shareData) {
        Intent intent = ProductInfoActivity.createInstance(context, shareData);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProductInfoActivity.SHARE_DATA, shareData);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void goToAddProductDetail(Context context) {
        Intent intent = ProductInfoActivity.createInstance(context);
        Bundle bundle = new Bundle();
        bundle.putBoolean(ProductInfoActivity.IS_ADDING_PRODUCT, true);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public Fragment getProductDetailInstanceDeeplink(
            Context context, @NonNull ProductPass productPass) {

        Fragment fragment = Fragment.instantiate(
                context, ProductDetailRouter.PRODUCT_DETAIL_FRAGMENT);
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_PRODUCT_PASS_DATA, productPass);
        args.putBoolean(ARG_FROM_DEEPLINK, true);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void goToProductDetailForResult(Fragment fragment, String productId,
                                           int adapterPosition, int requestCode) {
        Intent intent = ProductInfoActivity.createInstance(fragment.getContext(), productId,
                adapterPosition);
        fragment.startActivityForResult(intent, requestCode);
    }

    private void goToDefaultRoute(Context context) {
        Intent intent = new Intent(context,
                SellerHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public TkpdFragmentWrapper getSellerReputationFragment(Context context) {
        return new TkpdFragmentWrapper(
                context.getString(R.string.header_review_reputation),
                SellerReputationFragment.TAG,
                SellerReputationFragment.createInstance());
    }

    @Override
    public String getSchemeAppLinkCancelPayment() {
        return Constants.Applinks.PAYMENT_BACK_TO_DEFAULT;
    }

    @Override
    public Intent instanceIntentCartDigitalProduct(DigitalCheckoutPassData passData) {
        return CartDigitalActivity.newInstance(this, passData);
    }

    @Override
    public Intent instanceIntentCartDigitalProductWithBundle(Bundle bundle) {
        return CartDigitalActivity.newInstance(this, bundle);
    }

    @Override
    public Intent instanceIntentDigitalProduct(DigitalCategoryDetailPassData passData) {
        return DigitalProductActivity.newInstance(this, passData);
    }

    @Override
    public Intent instanceIntentDigitalCategoryList() {
        return DigitalCategoryListActivity.newInstance(this);
    }

    @Override
    public Intent instanceIntentDigitalWeb(String url) {
        return DigitalWebActivity.newInstance(this, url);
    }

    @Override
    public boolean isSupportedDelegateDeepLink(String appLinks) {
        DeepLinkDelegate deepLinkDelegate = DeepLinkHandlerActivity.getDelegateInstance();
        return deepLinkDelegate.supportsUri(appLinks);
    }

    @Override
    public Intent getIntentDeepLinkHandlerActivity() {
        return new Intent(this, DeepLinkHandlerActivity.class);
    }

    @Override
    public void actionNavigateByApplinksUrl(Activity activity, String applinks, Bundle bundle) {
        DeepLinkDelegate deepLinkDelegate = DeepLinkHandlerActivity.getDelegateInstance();
        Intent intent = activity.getIntent();
        intent.putExtras(bundle);
        intent.setData(Uri.parse(applinks));
        deepLinkDelegate.dispatchFrom(activity, intent);
    }

    @Override
    public Intent instanceIntentTokoCashActivation() {
        return ActivateTokoCashActivity.newInstance(this);
    }

    @Override
    public String getBaseUrlDomainPayment() {
        return SellerAppBaseUrl.BASE_PAYMENT_URL_DOMAIN;
    }

    @Override
    public String getGeneratedOverrideRedirectUrlPayment(String originUrl) {
        return Uri.parse(originUrl).buildUpon()
                .appendQueryParameter(
                        AuthUtil.WEBVIEW_FLAG_PARAM_FLAG_APP,
                        AuthUtil.DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_FLAG_APP
                )
                .appendQueryParameter(
                        AuthUtil.WEBVIEW_FLAG_PARAM_DEVICE,
                        AuthUtil.DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_DEVICE
                )
                .appendQueryParameter(
                        AuthUtil.WEBVIEW_FLAG_PARAM_UTM_SOURCE,
                        AuthUtil.DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_UTM_SOURCE
                )
                .appendQueryParameter(
                        AuthUtil.WEBVIEW_FLAG_PARAM_APP_VERSION, GlobalConfig.VERSION_NAME
                )
                .build().toString();
    }

    @Override
    public Map<String, String> getGeneratedOverrideRedirectHeaderUrlPayment(String originUrl) {
        String urlQuery = Uri.parse(originUrl).getQuery();
        return AuthUtil.generateHeaders(
                Uri.parse(originUrl).getPath(),
                urlQuery != null ? urlQuery : "",
                "GET",
                AuthUtil.KEY.KEY_WSV4);
    }
}
