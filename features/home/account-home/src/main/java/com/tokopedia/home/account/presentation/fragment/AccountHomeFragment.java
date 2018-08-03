package com.tokopedia.home.account.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.util.Log;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.abstraction.base.view.listener.NotificationListener;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.home.account.presentation.AccountHomeRouter;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.di.AccountHomeInjectionImpl;
import com.tokopedia.home.account.di.component.AccountHomeComponent;
import com.tokopedia.home.account.presentation.AccountHome;
import com.tokopedia.home.account.presentation.activity.GeneralSettingActivity;
import com.tokopedia.home.account.presentation.adapter.AccountFragmentItem;
import com.tokopedia.home.account.presentation.adapter.AccountHomePagerAdapter;
import com.tokopedia.home.account.presentation.presenter.AccountHomePresenter;
import com.tokopedia.home.account.presentation.viewmodel.base.AccountViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import q.rorbin.badgeview.QBadgeView;

/**
 * @author okasurya on 7/16/18.
 */
public class AccountHomeFragment extends TkpdBaseV4Fragment implements
        AccountHome.View, NotificationListener {

    @Inject
    AccountHomePresenter presenter;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AccountHomePagerAdapter adapter;

    private Toolbar toolbar;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_home, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getContext() != null) {
            GraphqlClient.init(getContext());

            presenter.getAccount(GraphqlHelper.loadRawString(getContext().getResources(), R.raw.query_account_home));
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void renderData(AccountViewModel accountViewModel) {
        if(getContext() != null) {
            List<AccountFragmentItem> fragmentItems = new ArrayList<>();
            AccountFragmentItem item = new AccountFragmentItem();
            item.setFragment(BuyerAccountFragment.newInstance(accountViewModel.getBuyerViewModel()));
            item.setTitle(getContext().getString(R.string.label_account_buyer));
            fragmentItems.add(item);

            if (accountViewModel.isSeller()) {
                item = new AccountFragmentItem();
                item.setFragment(SellerAccountFragment.newInstance(accountViewModel.getSellerViewModel()));
                item.setTitle(getContext().getString(R.string.label_account_seller));
                fragmentItems.add(item);
            } else {
                item = new AccountFragmentItem();
                item.setFragment(SellerEmptyAccountFragment.newInstance());
                item.setTitle(getContext().getString(R.string.label_account_seller));
                fragmentItems.add(item);
            }

            adapter.setItems(fragmentItems);
        }
    }

    private void initInjector() {
        AccountHomeComponent component =
            ((AccountHomeRouter) getActivity().getApplicationContext())
            .getAccountHomeInjection()
            .getAccountHomeComponent(
                ((BaseMainApplication) getActivity().getApplicationContext()).getBaseAppComponent()
            );

        component.inject(this);
        presenter.attachView(this);
    }

    private void initView(View view) {
        setToolbar(view);
        tabLayout = view.findViewById(R.id.tab_home_account);
        viewPager = view.findViewById(R.id.pager_home_account);
        setAdapter();
    }

    private ImageButton menuNotification;

    private void setToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.toolbar_title);
        title.setText(getString(R.string.title_account));
        menuNotification = toolbar.findViewById(R.id.action_notification);
        ImageButton menuSettings = toolbar.findViewById(R.id.action_settings);

        menuSettings.setOnClickListener(v -> startActivity(GeneralSettingActivity.createIntent(getActivity())));
        menuNotification.setOnClickListener(v -> RouteManager.route(getActivity(), ApplinkConst.NOTIFICATION));

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
    }

    private QBadgeView badgeView;

    @Override
    public void onNotifyBadgeNotification(int number) {
        if (menuNotification == null)
            return;
        if (badgeView == null)
            badgeView = new QBadgeView(getActivity());

        badgeView.bindTarget(menuNotification);
        badgeView.setBadgeGravity(Gravity.END | Gravity.TOP);
        badgeView.setBadgeNumber(number);
    }
}
