package com.tokopedia.instantloan.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewTreeObserver;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
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

public class InstantLoanActivity extends BaseSimpleActivity implements HasComponent<AppComponent>, BannerContractor.View, View.OnClickListener {

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
        populateThreeTabItem();
        InstantLoanPagerAdapter instantLoanPagerAdapter = new InstantLoanPagerAdapter(getSupportFragmentManager());
        instantLoanPagerAdapter.setData(instantLoanItemList);
        viewPager.setAdapter(instantLoanPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
        setActiveTab();

        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    private void setActiveTab() {
        viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                viewPager.setCurrentItem(0);
            }
        });
    }

    private void populateThreeTabItem() {
        instantLoanItemList.add(new InstantLoanItem(getEnabledPageTitle(0), getDanaInstantFragment()));
        instantLoanItemList.add(new InstantLoanItem(getPageTitle(1), getTanpaAgunanFragment()));
        instantLoanItemList.add(new InstantLoanItem(getPageTitle(2), getDenganAngunanFragment()));
    }

    private DanaInstantFragment getDanaInstantFragment() {
        return DanaInstantFragment.createInstance();
    }

    private TanpaAgunanFragment getTanpaAgunanFragment() {
        return TanpaAgunanFragment.createInstance();
    }

    private DenganAgunanFragment getDenganAngunanFragment() {
        return DenganAgunanFragment.createInstance();
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
        tabLayout.removeOnTabSelectedListener(onTabSelectedListener);
        viewPager.removeOnPageChangeListener(onPageChangeListener);
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            viewPager.reMeasureLayout();
        }

        @Override
        public void onPageSelected(int position) {
            viewPager.reMeasureLayout();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            tab.setText(getEnabledPageTitle(tab.getPosition()));
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            tab.setText(getPageTitle(tab.getPosition()));
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };


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
            mBannerPager.setAdapter(new BannerPagerAdapter(this, banners));
            mBannerPager.setPadding(getResources().getDimensionPixelOffset(R.dimen.il_margin_large), 0, getResources().getDimensionPixelOffset(R.dimen.il_margin_large), 0);
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
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(this.getTitle());
            updateTitle("");
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
        TypedArray imgs = getResources().obtainTypedArray(R.array.values_drawable_disable);
        Drawable image = getResources().getDrawable(imgs.getResourceId(position, -1));
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" " + "\n" + getResources().getStringArray(R.array.values_title)[position]);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BASELINE);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        imgs.recycle();
        return sb;
    }

    private CharSequence getEnabledPageTitle(int position) {
        TypedArray imgs = getResources().obtainTypedArray(R.array.values_drawable_enable);
        Drawable image = getResources().getDrawable(imgs.getResourceId(position, -1));
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" " + "\n" + getResources().getStringArray(R.array.values_title)[position]);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BASELINE);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        imgs.recycle();
        return sb;
    }
}
