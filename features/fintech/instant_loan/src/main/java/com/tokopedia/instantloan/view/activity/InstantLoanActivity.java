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

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.home.SimpleWebViewWithFilePickerActivity;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.instantloan.InstantLoanComponentInstance;
import com.tokopedia.instantloan.R;
import com.tokopedia.instantloan.data.model.response.BannerEntity;
import com.tokopedia.instantloan.ddcollector.DDCollectorManager;
import com.tokopedia.instantloan.di.component.InstantLoanComponent;
import com.tokopedia.instantloan.view.adapter.BannerPagerAdapter;
import com.tokopedia.instantloan.view.adapter.InstantLoanPagerAdapter;
import com.tokopedia.instantloan.view.contractor.BannerContractor;
import com.tokopedia.instantloan.view.fragment.DanaInstantFragment;
import com.tokopedia.instantloan.view.fragment.DenganAgunanFragment;
import com.tokopedia.instantloan.view.fragment.TanpaAgunanFragment;
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

    public static final String PINJAMAN_TITLE = "Pinjaman Online";
    @Inject
    BannerListPresenter mBannerPresenter;

    private ViewPager mBannerPager;
    private FloatingActionButton mBtnNextBanner, mBtnPreviousBanner;

    public static final String TAB_NAME = "tab_name";
    private static final String TAB_INSTAN = "instan";
    private static final String TAB_TANPA_AGUNAN = "tanpaagunan";
    private static final String TAB_AGUNAN = "agunan";

    private TabLayout tabLayout;
    private HeightWrappingViewPager heightWrappingViewPager;
    private int activeTabPosition = 0;
    private boolean instantLoanEnabled = false;

    public static Intent createIntent(Context context) {
        return new Intent(context, InstantLoanActivity.class);
    }


    @DeepLink({ApplinkConst.INSTANT_LOAN, ApplinkConst.INSTANT_LOAN_TAB})
    public static Intent getInstantLoanCallingIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, InstantLoanActivity.class);
        intent.putExtras(bundle);
        return intent;
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

        if (instantLoanEnabled) {
            populateThreeTabItem();
        } else {
            populateTwoTabItem();
        }
        InstantLoanPagerAdapter instantLoanPagerAdapter =
                new InstantLoanPagerAdapter(getSupportFragmentManager());
        instantLoanPagerAdapter.setData(instantLoanItemList);
        heightWrappingViewPager.setAdapter(instantLoanPagerAdapter);
        tabLayout.setupWithViewPager(heightWrappingViewPager);
        setActiveTab();
    }

    private void populateTwoTabItem() {
        instantLoanItemList.add(new InstantLoanItem(getPageTitle(1),
                getTanpaAgunanFragment(1)));
        instantLoanItemList.add(new InstantLoanItem(getPageTitle(2),
                getDenganAngunanFragment(2)));

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
        heightWrappingViewPager.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        heightWrappingViewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        heightWrappingViewPager.setCurrentItem(activeTabPosition);
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
        NetworkClient.init(this);
        super.onCreate(savedInstanceState);

        /*RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(this);
        instantLoanEnabled = remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.SHOW_INSTANT_LOAN, true);*/

        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            String tabName = bundle.getString(TAB_NAME);

            if (tabName != null) {
                switch (tabName) {
                    case TAB_INSTAN:
                        if (instantLoanEnabled) {
                            activeTabPosition = 0;
                        } else {
                            finish();
                        }

                        break;

                    case TAB_TANPA_AGUNAN:
                        if (instantLoanEnabled) {
                            activeTabPosition = 1;
                        } else {
                            activeTabPosition = 0;
                        }

                        break;
                    case TAB_AGUNAN:
                        if (instantLoanEnabled) {
                            activeTabPosition = 2;
                        } else {
                            activeTabPosition = 1;
                        }

                        break;
                    default:
                        activeTabPosition = 0;
                }
            }
        } else {
            activeTabPosition = 0;
        }

        TabLayout.Tab tab = tabLayout.getTabAt(activeTabPosition);
        if (tab != null) {
            tab.select();
        }
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
    public void renderUserList(List<BannerEntity> banners) {
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
        tabLayout = findViewById(R.id.tabs);
        heightWrappingViewPager = findViewById(R.id.pager);
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
        Intent intent = SimpleWebViewWithFilePickerActivity.getIntentWithTitle(this, url, PINJAMAN_TITLE);
        startActivity(intent);
    }
}

