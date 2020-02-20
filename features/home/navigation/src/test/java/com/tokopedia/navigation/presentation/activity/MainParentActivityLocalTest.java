package com.tokopedia.navigation.presentation.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.view.MenuItem;

import com.tokopedia.DeepLinkActivity;
import com.tokopedia.ShadowBottomNavigationView;
import com.tokopedia.ShadowLocalBroadcastManager;
import com.tokopedia.ShadowTaskStackBuilder;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.design.component.BottomNavigation;
import com.tokopedia.home.HomeApp;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.presentation.presenter.MainParentPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowActivity;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@Config(application = HomeApp.class, manifest = "AndroidTestManifest.xml",
        shadows = {ShadowTaskStackBuilder.class, ShadowLocalBroadcastManager.class, ShadowBottomNavigationView.class})
@RunWith(RobolectricTestRunner.class)
public class MainParentActivityLocalTest {

    private MainParentActivity mActivity;
    private ActivityController<MainParentActivity> mActivityController;

    @Before
    public void setup() throws Exception {

        mActivity = (mActivityController = Robolectric.buildActivity(MainParentActivity.class)).get();
    }

    @Test
    public void testIntent() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ApplinkConst.HOME));

        DeepLinkActivity deepLinkActivity = Robolectric.buildActivity(DeepLinkActivity.class, intent)
                .create().get();

        ShadowActivity shadowActivity = shadowOf(deepLinkActivity);
        Intent launchedIntent = shadowActivity.peekNextStartedActivityForResult().intent;
        assertThat(launchedIntent.getComponent(),
                equalTo(new ComponentName(deepLinkActivity, MainParentActivity.class)));
    }

    @Test
    public void verify_things() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(RuntimeEnvironment.application);
        ShadowLocalBroadcastManager shadowLocalBroadcastManager = shadowOf_(broadcastManager);
        assertEquals(0, shadowLocalBroadcastManager.getSentBroadcastIntents().size());

        Intent broadcastIntent = new Intent("foo");
        broadcastManager.sendBroadcast(broadcastIntent);

        assertEquals(1, shadowLocalBroadcastManager.getSentBroadcastIntents().size());
        assertEquals(broadcastIntent, shadowLocalBroadcastManager.getSentBroadcastIntents().get(0));
    }

    @Test
    public void verify_hockeyapp_dialog_appear() throws Exception {
//        GlobalConfig.DEBUG = false;
//
//        skip_onboarding();
//
//        // punya ini
//        Intent intent = new Intent(MainParentActivity.FORCE_HOCKEYAPP);
//
//        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(RuntimeEnvironment.application.getApplicationContext());
//        ShadowLocalBroadcastManager shadowLocalBroadcastManager = shadowOf_(broadcastManager);
//        broadcastManager.sendBroadcast(intent);
//
//        assertEquals(1, shadowLocalBroadcastManager.getSentBroadcastIntents().size());
//        assertEquals(1, shadowLocalBroadcastManager.getRegisteredBroadcastReceivers().size());
    }

    @Test
    public void verify_hockeyapp_dialog_disappear() throws Exception {
//        GlobalConfig.DEBUG = true;
//
//        skip_onboarding();
//
//        // punya ini
//        Intent intent = new Intent(MainParentActivity.FORCE_HOCKEYAPP);
//
//        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(RuntimeEnvironment.application.getApplicationContext());
//        ShadowLocalBroadcastManager shadowLocalBroadcastManager = shadowOf_(broadcastManager);
//        broadcastManager.sendBroadcast(intent);
//
//        assertEquals(1, shadowLocalBroadcastManager.getSentBroadcastIntents().size());
//        assertEquals(0, shadowLocalBroadcastManager.getRegisteredBroadcastReceivers().size());
    }

    @Test
    public void skip_onboarding() throws Exception {
        assert mActivity != null;

        mActivityController.create();

        MainParentPresenter mActivityPresenter = null;
        mActivityController.get().setPresenter(mActivityPresenter = spy(mActivityController.get().getPresenter()));

        when(mActivityPresenter.isFirstTimeUser()).thenReturn(false);

        mActivityController.start().resume();

        verify(mActivityPresenter, times(1)).isFirstTimeUser();

        assertEquals(mActivityController.get().isFinishing(), false);

        // verify selected tab is home
        assertEquals(((BottomNavigation) mActivity.findViewById(R.id.bottomnav)).getCurrentItem(), 0);
    }

    @Test
    public void test_tap_all_navigation() throws Exception {
        skip_onboarding();

        // tap all navigation view
        MenuItem menuItem = mock(MenuItem.class);
        doReturn(R.id.menu_feed).when(menuItem).getItemId();
        mActivityController.get().onNavigationItemSelected(menuItem);

        BottomNavigation bottomNavigation = mActivityController.get().findViewById(R.id.bottomnav);
        MenuItem selectedMenuItem = ((ShadowBottomNavigationView) shadowOf(bottomNavigation)).getSelectedMenuItem();
        assertThat(selectedMenuItem.getItemId(), is(R.id.menu_home));

        ((ShadowBottomNavigationView) shadowOf(bottomNavigation)).selectMenuItem(1);

        assertEquals(bottomNavigation.getMenu().getItem(0).isChecked(), false);
        assertEquals(bottomNavigation.getMenu().getItem(1).isChecked(), true);

        doReturn(false).when(mActivityController.get().presenter).isUserLogin();

        ((ShadowBottomNavigationView) shadowOf(bottomNavigation)).selectMenuItem(2);
        assertTrue(mActivityController.get().fragmentList.get(1) == mActivityController.get().currentFragment);
        assertFalse(mActivityController.get().fragmentList.get(2) == mActivityController.get().currentFragment);

    }

    @Test
    public void show_onboarding() throws Exception {
        assert mActivity != null;

        mActivityController.create();

        MainParentPresenter mActivityPresenter = null;
        mActivityController.get().setPresenter(mActivityPresenter = spy(mActivityController.get().getPresenter()));

        when(mActivityPresenter.isFirstTimeUser()).thenReturn(true);

        mActivityController.start();

        verify(mActivityPresenter, times(1)).isFirstTimeUser();

        assertEquals(mActivityController.get().isFinishing(), true);
    }

    private ShadowLocalBroadcastManager shadowOf_(LocalBroadcastManager localBroadcastManager) {
        return (ShadowLocalBroadcastManager) Shadow.extract(localBroadcastManager);
    }

}
