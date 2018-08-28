package com.tokopedia.home.account.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.badge.BadgeView;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.di.component.AccountHomeComponent;
import com.tokopedia.home.account.presentation.AccountHome;
import com.tokopedia.home.account.AccountHomeRouter;
import com.tokopedia.home.account.presentation.activity.GeneralSettingActivity;
import com.tokopedia.home.account.presentation.adapter.AccountFragmentItem;
import com.tokopedia.home.account.presentation.adapter.AccountHomePagerAdapter;
import com.tokopedia.home.account.presentation.listener.BaseAccountView;
import com.tokopedia.navigation_common.listener.FragmentListener;
import com.tokopedia.navigation_common.listener.NotificationListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author okasurya on 7/16/18.
 */
public class AccountHomeFragment extends TkpdBaseV4Fragment implements
        AccountHome.View, NotificationListener, FragmentListener {

    @Inject
    AccountHome.Presenter presenter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AppBarLayout appBarLayout;
    private AccountHomePagerAdapter adapter;
    private BadgeView badgeView;
    private Toolbar toolbar;
    private ImageButton menuNotification;
    private int counterNumber = 0;

    public static Fragment newInstance() {
        return new AccountHomeFragment();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setPage();
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

    public void setPage() {
        if (getContext() != null) {
            List<AccountFragmentItem> fragmentItems = new ArrayList<>();

            AccountFragmentItem item = new AccountFragmentItem();
            item.setFragment(BuyerAccountFragment.newInstance());
            item.setTitle(getContext().getString(R.string.label_account_buyer));
            fragmentItems.add(item);

            item = new AccountFragmentItem();
            item.setFragment(SellerAccountFragment.newInstance());
            item.setTitle(getContext().getString(R.string.label_account_seller));
            fragmentItems.add(item);

            adapter.setItems(fragmentItems);
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    private void initInjector() {
        AccountHomeComponent component =
                ((AccountHomeRouter) getActivity().getApplicationContext())
                        .getAccountHomeInjection()
                        .getAccountHomeComponent(
                                ((BaseMainApplication) getActivity().getApplicationContext())
                                        .getBaseAppComponent()
                        );

        component.inject(this);
        presenter.attachView(this);
    }

    private void initView(View view) {
        setToolbar(view);
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        tabLayout = view.findViewById(R.id.tab_home_account);
        viewPager = view.findViewById(R.id.pager_home_account);
        setAdapter();
    }

    private void setToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.toolbar_title);
        title.setText(getString(R.string.title_account));
        menuNotification = toolbar.findViewById(R.id.action_notification);
        ImageButton menuSettings = toolbar.findViewById(R.id.action_settings);

        menuSettings.setOnClickListener(v -> startActivity(GeneralSettingActivity.createIntent
                (getActivity())));
        menuNotification.setOnClickListener(v -> RouteManager.route(getActivity(), ApplinkConst
                .NOTIFICATION));

        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }
        setHasOptionsMenu(true);
    }

    private void setAdapter() {
        adapter = new AccountHomePagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.label_account_buyer));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.label_account_seller));

        onNotifyBadgeNotification(counterNumber);
    }

    @Override
    public void onNotifyBadgeNotification(int number) {
        this.counterNumber = number;
        if (menuNotification == null || getActivity() == null)
            return;
        if (badgeView == null)
            badgeView = new BadgeView(getActivity());

        badgeView.bindTarget(menuNotification);
        badgeView.setBadgeGravity(Gravity.END | Gravity.TOP);
        badgeView.setBadgeNumber(number);
    }

    @Override
    public void showLoading() {
        Fragment currentFragment = adapter.getItem(viewPager.getCurrentItem());
        if (currentFragment != null && currentFragment instanceof CustomerView) {
            ((BaseAccountView) currentFragment).showLoading();
        }
    }

    @Override
    public void hideLoading() {
        Fragment currentFragment = adapter.getItem(viewPager.getCurrentItem());
        if (currentFragment != null && currentFragment instanceof CustomerView) {
            ((BaseAccountView) currentFragment).hideLoading();
        }
    }

    @Override
    public void showError(String message) {
        Fragment currentFragment = adapter.getItem(viewPager.getCurrentItem());
        if (currentFragment != null && currentFragment instanceof CustomerView) {
            ((BaseAccountView) currentFragment).showError(message);
        }
    }

    @Override
    public void showErroNoConnection() {
        showError(getString(R.string.error_no_internet_connection));
    }

    @Override
    public void onScrollToTop() {
        Fragment currentFragment = adapter.getItem(viewPager.getCurrentItem());
        if (currentFragment != null && currentFragment instanceof FragmentListener) {
            ((FragmentListener) currentFragment).onScrollToTop();
        }
        if (appBarLayout != null)
            appBarLayout.setExpanded(true);
    }
}
