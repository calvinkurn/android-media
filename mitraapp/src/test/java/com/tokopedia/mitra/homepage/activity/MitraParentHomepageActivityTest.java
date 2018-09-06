package com.tokopedia.mitra.homepage.activity;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;

import com.tokopedia.design.component.BottomNavigation;
import com.tokopedia.mitra.BaseActivityTest;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.TestMenuItem;
import com.tokopedia.mitra.TestMitraApplication;
import com.tokopedia.mitra.account.fragment.MitraAccountFragment;
import com.tokopedia.mitra.homepage.contract.MitraParentHomepageContract;
import com.tokopedia.mitra.homepage.fragment.MitraHomepageFragment;
import com.tokopedia.session.login.loginphonenumber.view.activity.LoginPhoneNumberActivity;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestMitraApplication.class, manifest = "TestAndroidManifest.xml")
public class MitraParentHomepageActivityTest extends BaseActivityTest {
    private ActivityController<MitraParentHomepageActivity> activityController;
    private MitraParentHomepageActivity activity;
    private MitraParentHomepageContract.Presenter presenter;

    @Before
    public void setUp() throws Exception {
        activityController = Robolectric.buildActivity(MitraParentHomepageActivity.class);
        activity = activityController.create().start().postCreate(null).resume().visible().get();
        presenter = activity.presenter = spy(activity.presenter);
    }

    @After
    public void tearDown() throws Exception {
        activityController.pause().stop().destroy();
    }

    @Test
    public void getNewFragment_initialLoad_inflateHomepageFragment() {
        MitraHomepageFragment fragment = new MitraHomepageFragment();
        startFragment(activity, fragment);
        Fragment fragment1 = activity.getSupportFragmentManager().findFragmentById(R.id.parent_view);
        Assert.assertEquals(fragment.getClass().getSimpleName(), fragment1.getClass().getSimpleName());
    }

    @Test
    public void activityShouldNotNull() {
        Assert.assertNotNull(activity);
    }

    @Test
    public void onBottomNavigationClick_clickBantuan_inflateHelpFragment() {
        BottomNavigation bottomNavigation = activity.findViewById(R.id.mitra_bottom_nav);
        BottomNavigationView.OnNavigationItemSelectedListener listener = bottomNavigation.getOnNavigationItemSelectedListener();
        listener.onNavigationItemSelected(new TestMenuItem() {
            @Override
            public int getItemId() {
                return R.id.menu_mitra_help;
            }
        });
        verify(presenter).onHelpMenuClicked();
    }

    @Test
    public void inflateHomepageFragment() {
        activity.inflateHomepageFragment();
        Fragment fragment1 = activity.getSupportFragmentManager().findFragmentById(R.id.parent_view);
        Assert.assertEquals(MitraHomepageFragment.class.getSimpleName(), fragment1.getClass().getSimpleName());
    }

    @Test
    public void setHomepageMenuSelected() {
        BottomNavigation bottomNavigation = activity.findViewById(R.id.mitra_bottom_nav);
        activity.setHomepageMenuSelected();

        Assert.assertEquals(R.id.menu_mitra_home, bottomNavigation.getSelectedItemId());
    }

    @Test
    public void setAccountMenuSelected() {
        BottomNavigation bottomNavigation = activity.findViewById(R.id.mitra_bottom_nav);
        activity.setAccountMenuSelected();

        Assert.assertEquals(R.id.menu_mitra_account, bottomNavigation.getSelectedItemId());
    }

    @Test
    public void navigateToLoggedInThenAccountPage() {
        activity.navigateToLoggedInThenAccountPage();
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        Intent expectedIntent = new Intent(activity, LoginPhoneNumberActivity.class);
        Intent actualIntent = shadowActivity.getNextStartedActivity();
        assertTrue(actualIntent.filterEquals(expectedIntent));
    }

    @Test
    public void inflateAccountFragment() {
        activity.inflateAccountFragment();
        Fragment fragment1 = activity.getSupportFragmentManager().findFragmentById(R.id.parent_view);
        Assert.assertEquals(MitraAccountFragment.class.getSimpleName(), fragment1.getClass().getSimpleName());
    }

    @Test
    public void onBottomNavigationClick_clickAccount_inflateAccountFragment() {
        BottomNavigation bottomNavigation = activity.findViewById(R.id.mitra_bottom_nav);
        BottomNavigationView.OnNavigationItemSelectedListener listener = bottomNavigation.getOnNavigationItemSelectedListener();
        listener.onNavigationItemSelected(new TestMenuItem() {
            @Override
            public int getItemId() {
                return R.id.menu_mitra_account;
            }
        });
        bottomNavigation.setCurrentItem(bottomNavigation.getMenuItemPosition(R.id.menu_mitra_help));
        verify(presenter).onAccountMenuClicked();
    }

    @Test
    public void onBottomNavigationClick_clickHome_inflateHomeFragment() {
        BottomNavigation bottomNavigation = activity.findViewById(R.id.mitra_bottom_nav);
        BottomNavigationView.OnNavigationItemSelectedListener listener = bottomNavigation.getOnNavigationItemSelectedListener();
        listener.onNavigationItemSelected(new TestMenuItem() {
            @Override
            public int getItemId() {
                return R.id.menu_mitra_home;
            }
        });
        Fragment fragment1 = activity.getSupportFragmentManager().findFragmentById(R.id.parent_view);
        Assert.assertEquals(MitraHomepageFragment.class.getSimpleName(), fragment1.getClass().getSimpleName());
    }
}