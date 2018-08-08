package com.tokopedia.navigation.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseAppCompatActivity;
import com.tokopedia.navigation.GlobalNavAnalytics;
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
import com.tokopedia.showcase.ShowCasePreference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by meta on 19/06/18.
 */
public class MainParentActivity extends BaseAppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, HasComponent,
        MainParentView, ShowCaseListener {

    private final static int EXIT_DELAY_MILLIS = 2000;
    public static int HOME_MENU = 0;
    public static int FEED_MENU = 1;
    public static int INBOX_MENU = 2;
    public static int CART_MENU = 3;
    public static int ACCOUNT_MENU = 4;

    private BottomNavigation bottomNavigation;
    private ShowCaseDialog showCaseDialog;

    private List<Fragment> fragmentList;
    private List<String> titles;

    private Notification notification;

    private Fragment currentFragment;

    @Inject
    UserSession userSession;

    @Inject
    MainParentPresenter presenter;

    @Inject
    GlobalNavAnalytics globalNavAnalytics;

    private boolean isUserFirstTimeLogin = false;
    private boolean doubleTapExit = false;

    @DeepLink({ApplinkConst.HOME, ApplinkConst.HOME_CATEGORY})
    public static Intent getApplinkIntent(Context context, Bundle bundle) {
        return start(context);
    }

    @DeepLink({ApplinkConst.HOME_FEED, ApplinkConst.FEED})
    public static Intent getApplinkFeedIntent(Context context, Bundle bundle) {
        // TODO: 8/7/18 oka add bundle for change tab
        Intent intent = start(context);
        intent.putExtra("TAB_POSITION", FEED_MENU);
        return intent;
    }

    public static Intent start(Context context) {
        return new Intent(context, MainParentActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GraphqlClient.init(this); // initialize graphql
        this.initInjector(); // initialize di
        presenter.setView(this);
        setContentView(R.layout.activity_main_parent);

        bottomNavigation = findViewById(R.id.bottomnav);
        FrameLayout container = findViewById(R.id.container);

        bottomNavigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

        titles = titles();
        fragmentList = fragments();

        if (savedInstanceState == null) {
            onNavigationItemSelected(bottomNavigation.getMenu().findItem(R.id.menu_home));
            this.currentFragment = fragmentList.get(0);
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
        Fragment fragment;
        int i = item.getItemId();
        int position = 0;
        if (i == R.id.menu_home) {
            position = HOME_MENU;
        } else if (i == R.id.menu_feed) {
            position = FEED_MENU;
        } else if (i == R.id.menu_inbox) {
            position = INBOX_MENU;
        } else if (i == R.id.menu_cart) {
            position = CART_MENU;
        } else if (i == R.id.menu_account) {
            position = ACCOUNT_MENU;
        }

        globalNavAnalytics.eventBottomNavigation(titles.get(position)); // push analytics

        if (position == INBOX_MENU || position == CART_MENU || position == ACCOUNT_MENU)
            if (!isUserLogin())
                return false;

        fragment = fragmentList.get(position);
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

    private void openFragment(Fragment fragment) {
        String backStateName =  fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        Fragment currentFrag = manager.findFragmentByTag(backStateName);
        if (currentFrag != null && manager.getFragments().size() > 0) {
            for (int i = 0; i < manager.getFragments().size(); i++){
                Fragment frag = manager.getFragments().get(i);
                if (frag.getClass().getName().equalsIgnoreCase(fragment.getClass().getName())) {
                    ft.show(frag); // only show fragment what you want to show
                } else {
                    ft.hide(frag); // hide all fragment
                }
            }
        } else {
            ft.add(R.id.container, fragment, backStateName); // add fragment if there re not registered on fragmentManager
        }
        ft.commit();
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
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (getApplication() instanceof  GlobalNavRouter) {
            this.currentFragment = ((GlobalNavRouter) MainParentActivity.this.getApplication()).getHomeFragment();
        }
        selectFragment(currentFragment);
        bottomNavigation.getMenu().getItem(HOME_MENU).setChecked(true);
    }

    private List<Fragment> fragments() {
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

    private List<String> titles() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.home));
        titles.add(getString(R.string.feed));
        titles.add(getString(R.string.inbox));
        titles.add(getString(R.string.keranjang));
        titles.add(getString(R.string.akun));
        return titles;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public Fragment getFragment(int index){ return getSupportFragmentManager().findFragmentById(R.id.container); }

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

    public BaseAppComponent getApplicationComponent() {
        return ((BaseMainApplication) getApplication()).getBaseAppComponent();
    }
    @Override
    public void onBackPressed() {
        doubleTapExit();
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

    /**
     * Notification
     */
    private void setBadgeNotifCounter(Fragment fragment) {
        if (fragment == null)
            return;

        if (fragment instanceof NotificationListener && notification != null) {
            ((NotificationListener) fragment).onNotifyBadgeNotification(notification.getTotalNotif());
            invalidateOptionsMenu();
        }
    }

    /**
     * Show Case on boarding
     */
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

    @Override
    public void onReadytoShowBoarding(ArrayList<ShowCaseObject> showCaseObjects) {
        final String showCaseTag = MainParentActivity.class.getName() + ".bottomNavigation";
        if (ShowCasePreference.hasShown(this, showCaseTag) || showCaseDialog != null
                || showCaseObjects == null) {
            return;
        }

        showCaseDialog = createShowCase();

        ArrayList<ShowCaseObject> showcases = new ArrayList<>();
        showcases.add(new ShowCaseObject(
                bottomNavigation,
                getString(R.string.title_showcase),
                getString(R.string.desc_showcase),
                ShowCaseContentPosition.UNDEFINED));
        showcases.addAll(showCaseObjects);

        showCaseDialog.show(this, showCaseTag, showcases);
    }
}
