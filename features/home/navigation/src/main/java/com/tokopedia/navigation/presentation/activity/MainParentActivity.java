package com.tokopedia.navigation.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseAppCompatActivity;
import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.BottomNavigation;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.home.account.presentation.fragment.AccountHomeFragment;
import com.tokopedia.navigation.GlobalNavRouter;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.domain.model.Notification;
import com.tokopedia.navigation.presentation.di.DaggerGlobalNavComponent;
import com.tokopedia.navigation.presentation.di.GlobalNavModule;
import com.tokopedia.navigation.presentation.fragment.InboxFragment;
import com.tokopedia.navigation.presentation.presenter.MainParentPresenter;
import com.tokopedia.navigation.presentation.view.MainParentView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by meta on 19/06/18.
 */
public class MainParentActivity extends BaseAppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, HasComponent, MainParentView {

    public static int HOME_MENU = 0;
    public static int FEED_MENU = 1;
    public static int INBOX_MENU = 2;
    public static int CART_MENU = 3;
    public static int ACCOUNT_MENU = 4;

    private BottomNavigation bottomNavigation;
    private TouchViewPager viewPager;

    @Inject
    UserSession userSession;

    @Inject
    MainParentPresenter presenter;

    private boolean isUserFirstTimeLogin = false;

    public static Intent start(Context context) {
        return new Intent(context, MainParentActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GraphqlClient.init(this);
        this.initInjector();
        presenter.setView(this);
        setContentView(R.layout.activity_main_parent);

        bottomNavigation = findViewById(R.id.bottomnav);
        viewPager = findViewById(R.id.container);

        FragmentAdapter adapterViewPager = new FragmentAdapter(getSupportFragmentManager(), createFragments());
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
        viewPager.setAllowPageSwitching(false);

        if (savedInstanceState == null) {
            onNavigationItemSelected(bottomNavigation.getMenu().findItem(R.id.menu_home));
        }
    }

    private void initInjector() {
        DaggerGlobalNavComponent.builder()
                .baseAppComponent(getApplicationComponent())
                .globalNavModule(new GlobalNavModule())
                .build()
                .inject(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.menu_home) {
            viewPager.setCurrentItem(HOME_MENU, false);
        } else if (i == R.id.menu_feed) {
            viewPager.setCurrentItem(FEED_MENU, false);
        } else if (i == R.id.menu_inbox) {
            if (isUserLogin())
                viewPager.setCurrentItem(INBOX_MENU, false);
        } else if (i == R.id.menu_cart) {
            if (isUserLogin())
                viewPager.setCurrentItem(CART_MENU, false);
        } else if (i == R.id.menu_account) {
            if (isUserLogin())
                viewPager.setCurrentItem(ACCOUNT_MENU, false);
        }
        return true;
    }

    private boolean isUserLogin() {
        if (!userSession.isLoggedIn())
            RouteManager.route(this, ApplinkConst.LOGIN);
        return userSession.isLoggedIn();
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public void setUserSession(UserSession userSession){
        this.userSession = userSession;
    }

    @Override
    public BaseAppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
        if (userSession.isLoggedIn() && isUserFirstTimeLogin) {
            reloadPage();
        }
        isUserFirstTimeLogin = !userSession.isLoggedIn();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    private void reloadPage() {
        FragmentAdapter adapterViewPager = new FragmentAdapter(getSupportFragmentManager(), createFragments());
        viewPager.setAdapter(adapterViewPager);
        bottomNavigation.getMenu().getItem(HOME_MENU).setChecked(true);
    }

    List<Fragment> createFragments() {
        List<Fragment> fragmentList = new ArrayList<>();
        if (MainParentActivity.this.getApplication() instanceof GlobalNavRouter) {
            fragmentList.add(((GlobalNavRouter) MainParentActivity.this.getApplication()).getHomeFragment());
            fragmentList.add(((GlobalNavRouter) MainParentActivity.this.getApplication()).getFeedPlusFragment());
            fragmentList.add(InboxFragment.newInstance());
            fragmentList.add(((GlobalNavRouter) MainParentActivity.this.getApplication()).getCartFragment());
            fragmentList.add(AccountHomeFragment.newInstance());
        }
        return fragmentList;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public Fragment getFragment(int index){ return ((FragmentAdapter)viewPager.getAdapter()).getItem(index); }

    @Override
    public void renderNotification(Notification notification) {
        bottomNavigation.setNotification(notification.getTotalInbox(), INBOX_MENU);
        bottomNavigation.setNotification(notification.getTotalCart(), CART_MENU);
    }

    @Override
    public void onStartLoading() { }

    @Override
    public void onError(String message) { }

    @Override
    public void onHideLoading() { }

    @Override
    public Context getContext() {
        return this;
    }

    public class FragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public BaseAppComponent getApplicationComponent() {
        return ((BaseMainApplication) getApplication()).getBaseAppComponent();
    }
}
