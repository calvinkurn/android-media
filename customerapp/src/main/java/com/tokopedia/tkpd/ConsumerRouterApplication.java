package com.tokopedia.tkpd;

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
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.OtpRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.cart.activity.CartDigitalActivity;
import com.tokopedia.digital.product.activity.DigitalProductActivity;
import com.tokopedia.otp.phoneverification.activity.RidePhoneNumberVerificationActivity;
import com.tokopedia.payment.router.IPaymentModuleRouter;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.instoped.InstopedActivity;
import com.tokopedia.seller.instoped.presenter.InstagramMediaPresenterImpl;
import com.tokopedia.seller.logout.TkpdSellerLogout;
import com.tokopedia.seller.myproduct.ManageProduct;
import com.tokopedia.seller.myproduct.presenter.AddProductPresenterImpl;
import com.tokopedia.seller.product.view.activity.ProductEditActivity;
import com.tokopedia.seller.shopsettings.etalase.activity.EtalaseShopEditor;
import com.tokopedia.session.session.activity.Login;
import com.tokopedia.tkpd.deeplink.DeepLinkDelegate;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.drawer.DrawerBuyerHelper;
import com.tokopedia.tkpd.goldmerchant.GoldMerchantRedirectActivity;
import com.tokopedia.tkpd.home.ParentIndexHome;
import com.tokopedia.tkpd.home.recharge.fragment.RechargeCategoryFragment;
import com.tokopedia.tkpd.redirect.RedirectCreateShopActivity;
import com.tokopedia.tkpdpdp.ProductInfoActivity;
import com.tokopedia.transaction.wallet.WalletActivity;

import static com.tokopedia.core.router.productdetail.ProductDetailRouter.ARG_FROM_DEEPLINK;
import static com.tokopedia.core.router.productdetail.ProductDetailRouter.ARG_PARAM_PRODUCT_PASS_DATA;
import static com.tokopedia.core.router.productdetail.ProductDetailRouter.SHARE_DATA;

/**
 * @author normansyahputa on 12/15/16.
 */

public class ConsumerRouterApplication extends MainApplication implements
        TkpdCoreRouter, SellerModuleRouter, IConsumerModuleRouter, IDigitalModuleRouter, PdpRouter,
        OtpRouter, IPaymentModuleRouter {

    public static final String COM_TOKOPEDIA_TKPD_HOME_PARENT_INDEX_HOME = "com.tokopedia.tkpd.home.ParentIndexHome";

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
        Intent intent = ProductInfoActivity.createInstance(context, productUrl);
        context.startActivity(intent);
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
        bundle.putParcelable(SHARE_DATA, shareData);
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
    public Fragment getProductDetailInstanceDeeplink(Context context,
                                                     @NonNull ProductPass productPass) {
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
                                           int adapterPosition,
                                           int requestCode) {
        Intent intent = ProductInfoActivity.createInstance(fragment.getContext(), productId,
                adapterPosition);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    public Fragment getRechargeCategoryFragment() {
        Bundle bundle = new Bundle();
        return RechargeCategoryFragment.newInstance(bundle);
    }

    @Override
    public boolean isSupportedDelegateDeepLink(String appLinks) {
        DeepLinkDelegate deepLinkDelegate = DeeplinkHandlerActivity.getDelegateInstance();
        return deepLinkDelegate.supportsUri(appLinks);
    }

    @Override
    public Intent getIntentDeepLinkHandlerActivity() {
        return new Intent(this, DeeplinkHandlerActivity.class);
    }

    @Override
    public String getBaseUrlDomainPayment() {
        return ConsumerAppBaseUrl.BASE_PAYMENT_URL_DOMAIN;
    }

    @Override
    public DrawerHelper getDrawer(AppCompatActivity activity,
                                  SessionHandler sessionHandler,
                                  LocalCacheHandler drawerCache,
                                  GlobalCacheManager globalCacheManager) {
        return DrawerBuyerHelper.createInstance(activity, sessionHandler, drawerCache, globalCacheManager);
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
    public void removeInstopedToken() {
        InstagramMediaPresenterImpl.removeToken();
    }

    @Override
    public void goToManageProduct(Context context) {
        Intent intent = new Intent(context, ManageProduct.class);
        context.startActivity(intent);
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
    public void goToWallet(Context context, Bundle bundle) {
        Intent intent = new Intent(context, WalletActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void goToMerchantRedirect(Context context) {
        Intent intent = new Intent(context, GoldMerchantRedirectActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void goToCreateMerchantRedirect(Context context) {
        Intent intent = RedirectCreateShopActivity.getCallingIntent(context);
        context.startActivity(intent);
    }

    @Override
    public void actionAppLink(Activity activity, String linkUrl) {
        Intent intent = new Intent(activity, DeeplinkHandlerActivity.class);
        intent.setData(Uri.parse(linkUrl));
        activity.startActivity(intent);
    }

    @Override
    public void onLogout(AppComponent appComponent) {
        TkpdSellerLogout.onLogOut(appComponent);
    }

    @Override
    public void goToRegister(Context context) {
        Intent intent = Login.getRegisterIntent(context);
        context.startActivity(intent);
    }

    @Override
    public Intent instanceIntentCartDigitalProduct(DigitalCheckoutPassData passData) {
        return CartDigitalActivity.newInstance(this, passData);
    }

    @Override
    public Intent instanceIntentDigitalProduct(DigitalCategoryDetailPassData passData) {
        return DigitalProductActivity.newInstance(this, passData);
    }

    @Override
    public Intent getHomeIntent(Context context) {
        return new Intent(context, ParentIndexHome.class);
    }

    @Override
    public Class<?> getHomeClass(Context context) throws ClassNotFoundException {
        return Class.forName(COM_TOKOPEDIA_TKPD_HOME_PARENT_INDEX_HOME);
    }

    @Override
    public String getSchemeAppLinkCartPayment() {
        return Constants.Applinks.CART;
    }


    @Override
    public Intent instanceIntentCartDigitalProductWithBundle(Bundle bundle) {
        return CartDigitalActivity.newInstance(this, bundle);
    }

    @Override
    public Intent getRidePhoneNumberActivityIntent(Activity activity) {
        return RidePhoneNumberVerificationActivity.getCallingIntent(activity);
    }
}
