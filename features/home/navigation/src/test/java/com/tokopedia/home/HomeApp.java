package com.tokopedia.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.home.account.AccountHomeRouter;
import com.tokopedia.home.account.analytics.data.model.UserAttributeData;
import com.tokopedia.home.account.di.AccountHomeInjection;
import com.tokopedia.navigation.GlobalNavRouter;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;

import org.robolectric.annotation.internal.Instrument;

import java.io.IOException;

import okhttp3.Response;

import static org.mockito.Mockito.mock;

@Instrument
public class HomeApp extends BaseMainApplication implements AccountHomeRouter, GlobalNavRouter, NetworkRouter, AbstractionRouter {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void doLogoutAccount(Context activity) {

    }

    @Override
    public Intent getManageAddressIntent(Context context) {
        return null;
    }

    @Override
    public void goToManageShopShipping(Context context) {

    }

    @Override
    public void goToManageShopProduct(Context context) {

    }

    @Override
    public void goToSaldo(Context context) {

    }

    @Override
    public AccountHomeInjection getAccountHomeInjection() {
        return null;
    }

    @Override
    public String getStringRemoteConfig(String key, String defaultValue) {
        return null;
    }

    @Override
    public boolean getBooleanRemoteConfig(String key, boolean defaultValue) {
        return false;
    }

    @Override
    public void sendOpenHomeEvent() {

    }

    @Override
    public Intent getTrainOrderListIntent(Context context) {
        return null;
    }

    @Override
    public void sendAnalyticsUserAttribute(UserAttributeData userAttributeData) {

    }

    @Override
    public void goToCreateMerchantRedirect(Context context) {

    }

    @Override
    public boolean isEnableInterestPick() {
        return false;
    }

    @Override
    public Fragment getHomeFragment(boolean scroll) {
        return null;
    }

    @Override
    public Fragment getFeedPlusFragment(Bundle bundle) {
        return new Fragment();
    }

    @Override
    public Fragment getCartFragment(Bundle bundle) {
        return null;
    }

    @Override
    public Fragment getOfficialStoreFragment(Bundle bundle) {
        return null;
    }

    @Override
    public ApplicationUpdate getAppUpdate(Context context) {
        return mock(ApplicationUpdate.class);
    }

    @Override
    public int getCartCount(Context context) {
        return 0;
    }

    @Override
    public void setCartCount(Context context, int count) {

    }

    @Override
    public Intent getHomeIntent(Context context) {
        return null;
    }

    @Override
    public void onForceLogout(Activity activity) {

    }

    @Override
    public void showTimezoneErrorSnackbar() {

    }

    @Override
    public void showMaintenancePage() {

    }

    @Override
    public void sendForceLogoutAnalytics(Response response, boolean b, boolean b1) {

    }

    @Override
    public void showForceLogoutTokenDialog(String response) {

    }

    @Override
    public void showServerError(Response response) {

    }

    @Override
    public void gcmUpdate() throws IOException {

    }

    @Override
    public void refreshToken() throws IOException {

    }

    @Override
    public CacheManager getGlobalCacheManager() {
        return mock(CacheManager.class);
    }

    @Override
    public void logInvalidGrant(Response response) {

    }

    @Override
    public boolean isAllowLogOnChuckInterceptorNotification() {
        return false;
    }

    @Override
    public void onNewIntent(Context context, Intent intent) {

    }

    @Override
    public FingerprintModel getFingerprintModel() {
        return mock(FingerprintModel.class);
    }

    @Override
    public void doRelogin(String newAccessToken) {

    }

    @Override
    public void sendAnalyticsAnomalyResponse(String s, String s1, String s2, String s3, String s4) {

    }
}
