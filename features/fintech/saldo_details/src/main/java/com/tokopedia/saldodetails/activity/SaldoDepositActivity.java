package com.tokopedia.saldodetails.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.adapter.SaldoDetailPagerAdapter;
import com.tokopedia.saldodetails.di.SaldoDetailsComponent;
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance;
import com.tokopedia.saldodetails.presenter.SaldoDetailsPresenter;
import com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment;
import com.tokopedia.saldodetails.view.ui.SaldoTabItem;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

@DeepLink(ApplinkConst.DEPOSIT)
public class SaldoDepositActivity extends BaseSimpleActivity implements
        HasComponent<SaldoDetailsComponent> {

    private static final int REQUEST_CODE_LOGIN = 1001;
    private static final String TAG = "DEPOSIT_FRAGMENT";
    private TabLayout saldoTabLayout;
    private ViewPager saldoViewPager;
    private SaldoDetailPagerAdapter saldoDetailPagerAdapter;
    private View saldoTabViewSeparator;
    ArrayList<SaldoTabItem> saldoTabItems = new ArrayList<>();

    @Inject
    UserSession userSession;
    private boolean isSeller;
    private SaldoTabItem sellerSaldoTabItem;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SaldoDetailsPresenter.REQUEST_WITHDRAW_CODE && resultCode == Activity.RESULT_OK) {
            if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
                finish();
            } else {
                ((SaldoDepositFragment) getSupportFragmentManager().findFragmentByTag(TAG)).refresh();
            }
        }
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (resultCode == RESULT_OK) {
                inflateFragment();
                loadSection();
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkUserLoginStatus();
    }

    private void checkUserLoginStatus() {
        if (userSession != null && !userSession.isLoggedIn()) {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN);
        }
    }

    private void initInjector() {
        SaldoDetailsComponentInstance.getComponent(getApplication()).inject(this);
    }

    @DeepLink(ApplinkConst.DEPOSIT)
    public static Intent createInstance(Context context) {
        // TODO: 25/1/19 check for tab flag
        return new Intent(context, SaldoDepositActivity.class);
    }

    @Override
    public SaldoDetailsComponent getComponent() {
        return SaldoDetailsComponentInstance.getComponent(getApplication());
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_saldo_deposit;
    }

    @Override
    protected Fragment getNewFragment() {

        /*if (userSession.isLoggedIn()) {
            return SaldoDepositFragment.createInstance();
        } else {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN);
            return null;
        }*/

        return null;

    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        initInjector();
        initializeView();
        setUpToolbar();
        loadSection();
    }

    private void setUpToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_icon_back_black);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(this.getTitle());
            updateTitle("");
        }

    }

    private void loadSection() {
        isSeller = !TextUtils.isEmpty(userSession.getShopId());
        if (isSeller) {
            loadTwoTabItem();
        } else {
            loadOneTabItem();
        }
        saldoDetailPagerAdapter = new SaldoDetailPagerAdapter(getSupportFragmentManager());
        saldoDetailPagerAdapter.setItems(saldoTabItems);
        saldoViewPager.setAdapter(saldoDetailPagerAdapter);
        saldoTabLayout.setupWithViewPager(saldoViewPager);

        saldoViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (sellerSaldoTabItem != null &&
                        sellerSaldoTabItem.getFragment() != null &&
                        sellerSaldoTabItem.getFragment().isVisible()) {
                    sellerSaldoTabItem.getFragment().setUserVisibleHint(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void loadTwoTabItem() {
        saldoTabItems.clear();

        SaldoTabItem buyerSaldoTabItem = new SaldoTabItem();
        buyerSaldoTabItem.setTitle(getString(R.string.saldo_buyer_tab_title));
        buyerSaldoTabItem.setFragment(SaldoDepositFragment.createInstance(false, isSeller));

        saldoTabItems.add(buyerSaldoTabItem);

        sellerSaldoTabItem = new SaldoTabItem();
        sellerSaldoTabItem.setTitle(getString(R.string.saldo_seller_tab_title));
        sellerSaldoTabItem.setFragment(SaldoDepositFragment.createInstance(true, isSeller));

        saldoTabItems.add(sellerSaldoTabItem);

        saldoTabLayout.setVisibility(View.VISIBLE);
        saldoTabViewSeparator.setVisibility(View.VISIBLE);

    }

    private void loadOneTabItem() {
        saldoTabItems.clear();
        SaldoTabItem saldoTabItem = new SaldoTabItem();
        saldoTabItem.setTitle("");
        saldoTabItem.setFragment(SaldoDepositFragment.createInstance(false, isSeller));
        saldoTabItems.add(saldoTabItem);
        saldoTabLayout.setVisibility(View.GONE);
        saldoTabViewSeparator.setVisibility(View.GONE);
    }

    private void initializeView() {
        saldoTabLayout = findViewById(R.id.saldo_tab_layout);
        saldoViewPager = findViewById(R.id.saldo_view_pager);
        saldoTabViewSeparator = findViewById(R.id.saldo_tab_view_separator);
        TextView saldoHelp = findViewById(R.id.toolbar_saldo_help);

        saldoHelp.setOnClickListener(v -> {
            /*RouteManager.route(this, String.format("%s?url=%s", ApplinkConst.WEBVIEW,
                    SaldoDetailsConstants.SALDO_HELP_URL));*/
            Toast.makeText(SaldoDepositActivity.this, "Go to help page", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    protected void setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public TabLayout getSaldoTabLayout() {
        return saldoTabLayout;
    }

    public View getBuyerTabView() {
        return Objects.requireNonNull(saldoTabLayout.getTabAt(0)).getCustomView();
    }
}
