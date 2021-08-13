package com.tokopedia.home.account.presentation.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.badge.BadgeView;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.analytics.AccountAnalytics;
import com.tokopedia.home.account.di.component.AccountHomeComponent;
import com.tokopedia.home.account.di.component.DaggerAccountHomeComponent;
import com.tokopedia.home.account.presentation.AccountHome;
import com.tokopedia.home.account.presentation.activity.AccountHomeActivity;
import com.tokopedia.home.account.presentation.activity.GeneralSettingActivity;
import com.tokopedia.navigation_common.listener.AllNotificationListener;
import com.tokopedia.navigation_common.listener.FragmentListener;

import javax.inject.Inject;

/**
 * @author okasurya on 7/16/18.
 */
public class AccountHomeFragment extends TkpdBaseV4Fragment implements
        AccountHome.View, AllNotificationListener, FragmentListener {

    @Inject
    AccountHome.Presenter presenter;
    private BadgeView badgeViewInbox;
    private BadgeView badgeViewNotification;
    private ImageButton menuNotification;
    private ImageButton menuInbox;

    private int counterNumber = 0;

    private AccountAnalytics accountAnalytics;

    public static Fragment newInstance(Bundle extras) {
        Fragment fragment = new AccountHomeFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_home, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.sendUserAttributeTracker();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) presenter.sendUserAttributeTracker();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    private void initInjector() {
        AccountHomeComponent component = DaggerAccountHomeComponent.builder().baseAppComponent(((BaseMainApplication) getActivity().getApplicationContext())
                .getBaseAppComponent()).build();
        component.inject(this);
        presenter.attachView(this);
    }

    private void initView(View view) {
        setToolbar(view);
        accountAnalytics = new AccountAnalytics(getActivity());
        onNotificationChanged(counterNumber, 0, 0);
        showBuyerPage();
    }

    private void showBuyerPage() {
        getChildFragmentManager()
            .beginTransaction()
            .add(R.id.container, BuyerAccountFragment.Companion.newInstance())
            .commit();
    }

    private void setToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_account);

        View statusBarBackground = view.findViewById(R.id.status_bar_bg);
        if (getActivity() != null) {
            statusBarBackground.getLayoutParams().height =
                    DisplayMetricUtils.getStatusBarHeight(getActivity());
        }
        TextView title = toolbar.findViewById(R.id.toolbar_title);

        title.setText(getString(R.string.title_account));
        menuNotification = toolbar.findViewById(R.id.action_notification);
        menuInbox = toolbar.findViewById(R.id.action_inbox);

        ImageButton menuSettings = toolbar.findViewById(R.id.action_settings);

        menuSettings.setOnClickListener(v -> startActivity(GeneralSettingActivity.Companion.createIntent
                (getActivity())));
        menuNotification.setOnClickListener(v -> {
            accountAnalytics.eventTrackingNotifCenter();
            accountAnalytics.eventTrackingNotification();
            RouteManager.route(getActivity(), ApplinkConst.NOTIFICATION);
        });

        menuInbox.setOnClickListener(v -> {
            accountAnalytics.eventTrackingInbox();
            RouteManager.route(getActivity(), ApplinkConst.INBOX);
        });

        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }

        ImageButton backBtn = view.findViewById(R.id.action_back);
        if(getActivity() != null && getActivity() instanceof AccountHomeActivity) {
            backBtn.setVisibility(View.VISIBLE);
            backBtn.setOnClickListener(v -> {
                getActivity().onBackPressed();
            });
        }else {
            backBtn.setVisibility(View.GONE);
        }

        //status bar background compability
        statusBarBackground.getLayoutParams().height =
                DisplayMetricUtils.getStatusBarHeight(getActivity());
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusBarBackground.setVisibility(View.INVISIBLE);
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            statusBarBackground.setVisibility(View.VISIBLE);
        } else {
            statusBarBackground.setVisibility(View.GONE);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onScrollToTop() {}

    @Override
    public boolean isLightThemeStatusBar() {
        return false;
    }

    @Override
    public void onNotificationChanged(int notificationCount, int inboxCount, int cartCount) {
        setToolbarNotificationCount(notificationCount);
        setToolbarInboxCount(inboxCount);
    }

    private boolean setToolbarNotificationCount(int notificationCount) {
        if (menuNotification == null && getActivity() == null)
            return true;
        if (badgeViewNotification == null) badgeViewNotification = new BadgeView(getActivity());

        badgeViewNotification.bindTarget(menuNotification);
        badgeViewNotification.setBadgeGravity(Gravity.END | Gravity.TOP);
        badgeViewNotification.setBadgeNumber(notificationCount);

        this.counterNumber = notificationCount;
        return false;
    }

    private void setToolbarInboxCount(int badgeNumber) {
        if (menuInbox != null) {
            if (badgeViewInbox == null) badgeViewInbox = new BadgeView(getContext());

            badgeViewInbox.bindTarget(menuInbox);
            badgeViewInbox.setBadgeGravity(Gravity.END | Gravity.TOP);
            badgeViewInbox.setBadgeNumber(badgeNumber);
        }
    }
}
