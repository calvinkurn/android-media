package com.tokopedia.mitra.homepage.activity;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;

import com.tokopedia.design.component.BottomNavigation;
import com.tokopedia.mitra.BaseActivityTest;
import com.tokopedia.mitra.BuildConfig;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.TestMenuItem;
import com.tokopedia.mitra.homepage.fragment.MitraAccountFragment;
import com.tokopedia.mitra.homepage.fragment.MitraHelpFragment;
import com.tokopedia.mitra.homepage.fragment.MitraHomepageFragment;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 27)
public class MitraHomepageActivityTest extends BaseActivityTest {
    private ActivityController<MitraParentHomepageActivity> activityController;
    private MitraParentHomepageActivity activity;

    @Before
    public void setUp() throws Exception {
        activityController = Robolectric.buildActivity(MitraParentHomepageActivity.class);
        activity = activityController.create().start().postCreate(null).resume().visible().get();
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
        Fragment fragment1 = activity.getSupportFragmentManager().findFragmentById(R.id.parent_view);
        Assert.assertEquals(MitraHelpFragment.class.getSimpleName(), fragment1.getClass().getSimpleName());
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
        Fragment fragment1 = activity.getSupportFragmentManager().findFragmentById(R.id.parent_view);
        Assert.assertEquals(MitraAccountFragment.class.getSimpleName(), fragment1.getClass().getSimpleName());
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