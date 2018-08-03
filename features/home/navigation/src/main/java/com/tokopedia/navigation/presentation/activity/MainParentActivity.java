package com.tokopedia.navigation.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseAppCompatActivity;
import com.tokopedia.navigation_common.listener.NotificationListener;
import com.tokopedia.navigation_common.listener.ShowCaseListener;
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
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by meta on 19/06/18.
 */
public class MainParentActivity extends BaseAppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, HasComponent, MainParentView {

    private final static int EXIT_DELAY_MILLIS = 2000;
    public static int HOME_MENU = 0;
    public static int FEED_MENU = 1;
    public static int INBOX_MENU = 2;
    public static int CART_MENU = 3;
    public static int ACCOUNT_MENU = 4;

    private BottomNavigation bottomNavigation;
    private FrameLayout container;

    private Notification notification;

    private UserSession userSession;
    @Inject MainParentPresenter presenter;

    private boolean isUserFirstTimeLogin = false;
    private boolean doubleTapExit = false;

    private Fragment currentFragment;

    public static Intent start(Context context) {
        return new Intent(context, MainParentActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GraphqlClient.init(this);
        this.intiInjector();
        presenter.setView(this);
        setContentView(R.layout.activity_main_parent);

        userSession = ((AbstractionRouter) this.getApplication()).getSession();

        bottomNavigation = findViewById(R.id.bottomnav);
        container = findViewById(R.id.container);

        bottomNavigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

        if (savedInstanceState == null) {
            onNavigationItemSelected(bottomNavigation.getMenu().findItem(R.id.menu_home));
        }

//        startShowCase();
    }

    private void setBadgeNotifCounter(Fragment fragment) {
        if (fragment == null)
            return;

        if (fragment instanceof NotificationListener && notification != null) {
            ((NotificationListener) fragment).onNotifyBadgeNotification(notification.getTotalNotif());
        }
    }
    private void intiInjector() {
        DaggerGlobalNavComponent.builder()
                .globalNavModule(new GlobalNavModule())
                .build()
                .inject(this);
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        int i = item.getItemId();
        if (i == R.id.menu_home) {
            if (getApplication() instanceof  GlobalNavRouter) {
                fragment = ((GlobalNavRouter) MainParentActivity.this.getApplication()).getHomeFragment();
            }
        } else if (i == R.id.menu_feed) {
            if (getApplication() instanceof  GlobalNavRouter) {
                fragment = ((GlobalNavRouter) MainParentActivity.this.getApplication()).getFeedPlusFragment();
            }
        } else if (i == R.id.menu_inbox) {
            if (isUserLogin()) {
                fragment = InboxFragment.newInstance();
            }
        } else if (i == R.id.menu_cart) {
            if (isUserLogin())
                if (getApplication() instanceof  GlobalNavRouter) {
                    fragment = ((GlobalNavRouter) MainParentActivity.this.getApplication()).getCartFragment();
                }
        } else if (i == R.id.menu_account) {
            if (isUserLogin()) {
                fragment = AccountHomeFragment.newInstance();
            }
        }

        if (fragment != null) {
            this.currentFragment = fragment;
            selectFragment(fragment);
        }
        return true;
    }

    private void selectFragment(Fragment fragment) {
        openFragment(fragment);
        setBadgeNotifCounter(fragment);
    }

    private boolean isUserLogin() {
        if (!userSession.isLoggedIn())
            RouteManager.route(this, ApplinkConst.LOGIN);
        return userSession.isLoggedIn();
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
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (getApplication() instanceof  GlobalNavRouter) {
            this.currentFragment = ((GlobalNavRouter) MainParentActivity.this.getApplication()).getHomeFragment();
        }
        selectFragment(currentFragment);
        bottomNavigation.getMenu().getItem(HOME_MENU).setChecked(true);
    }

    @Override
    public void renderNotification(Notification notification) {
        this.notification = notification;
        bottomNavigation.setNotification(notification.getTotalInbox(), INBOX_MENU);
        bottomNavigation.setNotification(notification.getTotalCart(), CART_MENU);

        if (currentFragment != null)
            setBadgeNotifCounter(currentFragment);
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

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            doubleTapExit();
        }
    }

    public BaseAppComponent getApplicationComponent() {
        return ((BaseMainApplication) getApplication()).getBaseAppComponent();
    }

    private void doubleTapExit() {
        if (doubleTapExit) {
            this.finish();
        } else {
            doubleTapExit = true;
            Toast.makeText(this, R.string.exit_message, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleTapExit = false, EXIT_DELAY_MILLIS);
        }
    }

    private ShowCaseDialog showCaseDialog;

    private void startShowCase() {
        final String showCaseTag = MainParentActivity.class.getName() + ".bottomNavigation";
//        if (ShowCasePreference.hasShown(this, showCaseTag) || showCaseDialog != null) {
//            return;
//        }
        showCaseDialog = createShowCase();

        ArrayList<ShowCaseObject> showcases = new ArrayList<>();
        showcases.add(new ShowCaseObject(
                bottomNavigation,
                getString(R.string.title_showcase),
                getString(R.string.desc_showcase),
                ShowCaseContentPosition.UNDEFINED));

        if (currentFragment != null && currentFragment instanceof ShowCaseListener) {
            ArrayList<View> views = ((ShowCaseListener) currentFragment).viewShowCases();
            showcases.add(new ShowCaseObject(views.get(0),
                    "Notifikasi tersimpan rapi",
                    "Tak ada yang terlewat, semua notifikasi ada di sini."));
            showcases.add(new ShowCaseObject(views.get(1),
                    "Cek Wishlist sangat mudah",
                    ""));

            showCaseDialog.show(this, showCaseTag, showcases);
        }
    }

    private ShowCaseDialog createShowCase() {
        return new ShowCaseBuilder()
                .backgroundContentColorRes(R.color.black)
                .shadowColorRes(R.color.shadow)
                .titleTextColorRes(R.color.white)
                .textColorRes(R.color.grey_400)
                .textSizeRes(R.dimen.sp_12)
                .titleTextSizeRes(R.dimen.sp_16)
                .nextStringRes(R.string.next)
                .prevStringRes(R.string.previous)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .build();
    }
}
