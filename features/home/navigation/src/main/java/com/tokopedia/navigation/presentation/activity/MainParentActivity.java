package com.tokopedia.navigation.presentation.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.component.BottomNavigation;
import com.tokopedia.feedplus.view.fragment.FeedPlusFragment;
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.presentation.fragment.AccountFragment;
import com.tokopedia.navigation.presentation.fragment.CartFragment;
import com.tokopedia.navigation.presentation.fragment.InboxFragment;

/**
 * Created by meta on 19/06/18.
 */
public class MainParentActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, HasComponent {

    private BottomNavigation bottomNavigation;
    private TouchViewPager viewPager;

    private boolean isUserFirstTimeLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_parent);

        bottomNavigation = findViewById(R.id.bottomnav);
        viewPager = findViewById(R.id.container);

        FragmentAdapter adapterViewPager = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                bottomNavigation.getMenu().getItem(position).setChecked(true);
            }
        });

        bottomNavigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

        viewPager.setOnTouchListener((arg0, arg1) -> true);

        if (savedInstanceState == null) {
            onNavigationItemSelected(bottomNavigation.getMenu().findItem(R.id.menu_home));
        }

        bottomNavigation.setNotification(2000, 2);
        bottomNavigation.setNotification(1200, 3);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.menu_home) {
            viewPager.setCurrentItem(0, false);

        } else if (i == R.id.menu_feed) {
            viewPager.setCurrentItem(1, false);

        } else if (i == R.id.menu_inbox) {
            viewPager.setCurrentItem(2, false);

        } else if (i == R.id.menu_cart) {
            viewPager.setCurrentItem(3, false);

        } else if (i == R.id.menu_account) {
            viewPager.setCurrentItem(4, false);

        }
        return true;
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SessionHandler.isV4Login(this) && isUserFirstTimeLogin) {
            reloadPage();
        }

        isUserFirstTimeLogin = !SessionHandler.isV4Login(this);
    }

    private void reloadPage() {
        FragmentAdapter adapterViewPager = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        bottomNavigation.getMenu().getItem(0).setChecked(true);
    }

    public AppComponent getApplicationComponent() {
        return ((MainApplication) getApplication()).getAppComponent();
    }

    public class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HomeFragment.newInstance();
                case 1:
                    return new FeedPlusFragment();
                case 2:
                    return InboxFragment.newInstance();
                case 3:
                    return CartFragment.newInstance();
                case 4:
                    return AccountFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }
}
