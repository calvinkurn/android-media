package com.tokopedia.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
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

        FlowManager.init(new FlowConfig.Builder(this).build());
    }

    @Override
    public void doLogoutAccount(Activity activity) {

    }

    @Override
    public void goToHelpCenter(Context context) {

    }

    @Override
    public Intent getIntentCreateShop(Context context) {
        return null;
    }

    @Override
    public Intent getManageProfileIntent(Context context) {
        return null;
    }

    @Override
    public Intent getManagePasswordIntent(Context context) {
        return null;
    }

    @Override
    public Intent getManageAddressIntent(Context context) {
        return null;
    }

    @Override
    public void goToShopEditor(Context context) {

    }

    @Override
    public void goToManageShopShipping(Context context) {

    }

    @Override
    public void goToManageShopEtalase(Context context) {

    }

    @Override
    public void goTotManageShopNotes(Context context) {

    }

    @Override
    public void goToManageShopLocation(Context context) {

    }

    @Override
    public void goToManageShopProduct(Context context) {

    }

    @Override
    public void goToManageBankAccount(Context context) {

    }

    @Override
    public void goToManageCreditCard(Context context) {

    }

    @Override
    public void goToTokoCash(String applinkUrl, String redirectUrl, Activity activity) {

    }

    @Override
    public void goToSaldo(Context context) {

    }

    @Override
    public AccountHomeInjection getAccountHomeInjection() {
        return null;
    }

    @Override
    public Fragment getFavoriteFragment() {
        return null;
    }

    @Override
    public void gotoTopAdsDashboard(Context context) {

    }

    @Override
    public void goToGMSubscribe(Context context) {

    }

    @Override
    public String getStringRemoteConfig(String key, String defaultValue) {
        return null;
    }

    @Override
    public Boolean getBooleanRemoteConfig(String key, Boolean defaultValue) {
        return false;
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
    public void setPromoPushPreference(Boolean newValue) {

    }

    @Override
    public Fragment getHomeFragment() {
        return new Fragment();
    }

    @Override
    public Fragment getFeedPlusFragment(Bundle bundle) {
        return new Fragment();
    }

    @Override
    public Fragment getCartFragment() {
        return new Fragment();
    }

    @Override
    public Intent getInboxTalkCallingIntent(Context context) {
        return null;
    }

    @Override
    public Intent getInboxTicketCallingIntent(Context context) {
        return null;
    }

    @Override
    public ApplicationUpdate getAppUpdate(Context context) {
        return mock(ApplicationUpdate.class);
    }

    @Override
    public Intent getOnBoardingIntent(Activity activity) {
        return new Intent();
    }

    @Override
    public int getCartCount(Context context) {
        return 0;
    }

    @Override
    public void setCartCount(Context context, int count) {

    }

    @Override
    public void sendAnalyticsFirstTime() {

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
    public void showForceLogoutDialog(Response response) {

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
    public UserSession getSession() {
        return mock(UserSession.class);
    }

    @Override
    public void init() {

    }

    @Override
    public void registerShake(String screenName, Activity activity) {

    }

    @Override
    public void unregisterShake() {

    }

    @Override
    public CacheManager getGlobalCacheManager() {
        return mock(CacheManager.class);
    }

    @Override
    public AnalyticTracker getAnalyticTracker() {
        return mock(AnalyticTracker.class);
    }

    @Override
    public void logInvalidGrant(Response response) {

    }

    @Override
    public FingerprintModel getFingerprintModel() {
        return mock(FingerprintModel.class);
    }

    @Override
    public void doRelogin(String newAccessToken) {

    }
}
