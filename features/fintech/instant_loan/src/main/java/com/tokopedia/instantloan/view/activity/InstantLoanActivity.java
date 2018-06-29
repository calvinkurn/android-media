package com.tokopedia.instantloan.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.home.SimpleWebViewWithFilePickerActivity;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.instantloan.InstantLoanComponentInstance;
import com.tokopedia.instantloan.R;
import com.tokopedia.instantloan.ddcollector.DDCollectorManager;
import com.tokopedia.instantloan.di.component.InstantLoanComponent;
import com.tokopedia.instantloan.view.adapter.BannerPagerAdapter;
import com.tokopedia.instantloan.view.adapter.InstantLoanPagerAdapter;
import com.tokopedia.instantloan.view.contractor.BannerContractor;
import com.tokopedia.instantloan.view.fragment.DanaInstantFragment;
import com.tokopedia.instantloan.view.fragment.DenganAgunanFragment;
import com.tokopedia.instantloan.view.fragment.TanpaAgunanFragment;
import com.tokopedia.instantloan.view.model.BannerViewModel;
import com.tokopedia.instantloan.view.presenter.BannerListPresenter;
import com.tokopedia.instantloan.view.ui.HeightWrappingViewPager;
import com.tokopedia.instantloan.view.ui.InstantLoanItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class InstantLoanActivity extends BaseSimpleActivity implements HasComponent<AppComponent>,
        BannerContractor.View,
        BannerPagerAdapter.BannerClick,
        View.OnClickListener {

    @Inject
    BannerListPresenter mBannerPresenter;

    private ViewPager mBannerPager;
    private FloatingActionButton mBtnNextBanner, mBtnPreviousBanner;

    private TabLayout tabLayout;
    private HeightWrappingViewPager viewPager;

    public static Intent createIntent(Context context) {
        return new Intent(context, InstantLoanActivity.class);
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        initInjector();
        mBannerPresenter.attachView(this);
        initializeView();
        attachViewListener();
        setupToolbar();
        loadSection();
        mBannerPresenter.loadBanners();
    }

    List<InstantLoanItem> instantLoanItemList = new ArrayList<>();

    private void loadSection() {

        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(this);
        if (remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.SHOW_INSTANT_LOAN, true)) {
            populateThreeTabItem();
        } else {
            populateTwoTabItem();
        }
        InstantLoanPagerAdapter instantLoanPagerAdapter =
                new InstantLoanPagerAdapter(getSupportFragmentManager());
        instantLoanPagerAdapter.setData(instantLoanItemList);
        viewPager.setAdapter(instantLoanPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setActiveTab();
    }

    private void populateTwoTabItem() {
        instantLoanItemList.add(new InstantLoanItem(getPageTitle(0),
                getTanpaAgunanFragment(0)));
        instantLoanItemList.add(new InstantLoanItem(getPageTitle(1),
                getDenganAngunanFragment(1)));

    }

    private void populateThreeTabItem() {
        instantLoanItemList.add(new InstantLoanItem(getPageTitle(0),
                getDanaInstantFragment(0)));
        instantLoanItemList.add(new InstantLoanItem(getPageTitle(1),
                getTanpaAgunanFragment(1)));
        instantLoanItemList.add(new InstantLoanItem(getPageTitle(2),
                getDenganAngunanFragment(2)));
    }


    private void setActiveTab() {
        viewPager.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                viewPager.setCurrentItem(0);
            }
        });
    }

    private DanaInstantFragment getDanaInstantFragment(int position) {
        return DanaInstantFragment.createInstance(position);
    }

    private TanpaAgunanFragment getTanpaAgunanFragment(int position) {
        return TanpaAgunanFragment.createInstance(position);
    }

    private DenganAgunanFragment getDenganAngunanFragment(int position) {
        return DenganAgunanFragment.createInstance(position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public AppComponent getComponent() {
        return ((MainApplication) getApplication()).getAppComponent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBannerPresenter.detachView();
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_instant_loan;
    }

    @Override
    public void renderUserList(List<BannerViewModel> banners) {
        if (!banners.isEmpty()) {
            if (banners.size() > 1) {
                ((FloatingActionButton) findViewById(R.id.button_next)).show();
            }
            findViewById(R.id.container_banner).setVisibility(View.VISIBLE);
            mBannerPager = findViewById(R.id.view_pager_banner);
            mBannerPager.setOffscreenPageLimit(2);
            mBannerPager.setAdapter(new BannerPagerAdapter(this, banners, this));
            mBannerPager.setPadding(getResources().getDimensionPixelOffset(R.dimen.il_margin_banner),
                    0, getResources().getDimensionPixelOffset(R.dimen.il_margin_banner), 0);
            mBannerPager.setClipToPadding(false);
            mBannerPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.il_margin_medium));
            mBannerPager.addOnPageChangeListener(mBannerPageChangeListener);
        }
    }

    @Override
    public void nextBanner() {
        if (mBannerPager == null
                || mBannerPager.getAdapter() == null
                || mBannerPager.getCurrentItem() == mBannerPager.getAdapter().getCount()) {
            return;
        }

        mBannerPager.setCurrentItem(mBannerPager.getCurrentItem() + 1, true); //+1 for move the page to next
    }

    @Override
    public void previousBanner() {
        if (mBannerPager == null
                || mBannerPager.getAdapter() == null
                || mBannerPager.getCurrentItem() == 0) {
            return;
        }

        mBannerPager.setCurrentItem(mBannerPager.getCurrentItem() - 1, true); //-1 for move the page to prev
    }


    @Override
    public void onClick(View source) {
        if (source.getId() == R.id.button_next) {
            nextBanner();
        } else if (source.getId() == R.id.button_previous) {
            previousBanner();
        }
    }

    private void initInjector() {
        InstantLoanComponent daggerInstantLoanComponent = InstantLoanComponentInstance.get(getApplication());
        daggerInstantLoanComponent.inject(this);
    }

    private void initializeView() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = findViewById(R.id.pager);
        mBtnNextBanner = findViewById(R.id.button_next);
        mBtnPreviousBanner = findViewById(R.id.button_previous);
    }

    private void attachViewListener() {
        mBtnNextBanner.setOnClickListener(this);
        mBtnPreviousBanner.setOnClickListener(this);
    }

    private void setupToolbar() {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        DDCollectorManager.getsInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private ViewPager.OnPageChangeListener mBannerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == mBannerPager.getAdapter().getCount() - 1) {
                mBtnNextBanner.hide();
                mBtnPreviousBanner.show();
            } else if (position == 0) {
                mBtnPreviousBanner.hide();
                mBtnNextBanner.show();
            } else {
                mBtnNextBanner.show();
                mBtnPreviousBanner.show();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private CharSequence getPageTitle(int position) {
        return getResources().getStringArray(R.array.values_title)[position];
    }

    @Override
    public void onBannerClick(View view) {
        String url = (String) view.getTag();
        if (!TextUtils.isEmpty(url)) {
            openWebView(url);
        }
    }

    public void openWebView(String url) {
        Intent intent = SimpleWebViewWithFilePickerActivity.getIntentWithTitle(this, url, "Pinjaman Online");
        startActivity(intent);
    }
}

