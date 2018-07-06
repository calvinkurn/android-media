package com.tokopedia.tokopoints.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.home.SimpleWebViewWithFilePickerActivity;
import com.tokopedia.design.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.activity.MyCouponListingActivity;
import com.tokopedia.tokopoints.view.adapter.CatalogBannerPagerAdapter;
import com.tokopedia.tokopoints.view.adapter.CatalogChipAdapter;
import com.tokopedia.tokopoints.view.adapter.CatalogSortTypePagerAdapter;
import com.tokopedia.tokopoints.view.adapter.PaddingItemDecoration;
import com.tokopedia.tokopoints.view.contract.CatalogListingContract;
import com.tokopedia.tokopoints.view.model.CatalogBanner;
import com.tokopedia.tokopoints.view.model.CatalogFilterBase;
import com.tokopedia.tokopoints.view.presenter.CatalogListingPresenter;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.List;

import javax.inject.Inject;

public class CatalogListingFragment extends BaseDaggerFragment implements CatalogListingContract.View, View.OnClickListener {
    private static final int CONTAINER_LOADER = 0;
    private static final int CONTAINER_DATA = 1;
    private static final int CONTAINER_ERROR = 2;
    private ViewFlipper mContainerMain;
    private ViewPager mPagerSortType;
    private TabLayout mTabSortType;
    private RecyclerView mRecyclerViewChips;
    private TextView mTextPoints;
    private CatalogChipAdapter mChipAdapter;
    private CatalogSortTypePagerAdapter mViewPagerAdapter;
    private int mSelectedCategory = 0;
    private TextView mTextFailedAction;

    @Inject
    public CatalogListingPresenter mPresenter;

    public static Fragment newInstance() {
        return new CatalogListingFragment();
    }

    @Override
    public void onErrorFilter(String errorMessage) {
        mContainerMain.setDisplayedChild(CONTAINER_ERROR);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initInjector();
        View view = inflater.inflate(R.layout.tp_fragment_catalog_listing, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        initListener();
        showLoader();
        mPresenter.getHomePageData();
        mPresenter.getPointData();
    }

    @Override
    public void onDestroy() {
        mPresenter.destroyView();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public Context getAppContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void refreshTab(int categoryId) {
        CatalogListItemFragment fragment = (CatalogListItemFragment) mViewPagerAdapter.getRegisteredFragment(mPagerSortType.getCurrentItem());
        if (fragment != null
                && fragment.isAdded()) {
            if (fragment.getPresenter() != null && fragment.getPresenter().isViewAttached()) {
                fragment.getPresenter().getCatalog(categoryId, fragment.getPresenter().getView().getCurrentSortType());
            }
        }
    }

    @Override
    public int getSelectedCategoryId() {
        return mSelectedCategory;
    }

    @Override
    public void updateSelectedCategoryId(int id) {
        this.mSelectedCategory = id;
    }

    @Override
    public void showLoader() {
        mContainerMain.setDisplayedChild(CONTAINER_LOADER);
    }

    @Override
    public void onErrorBanners(String errorMessage) {
    }

    @Override
    public void onSuccessBanners(List<CatalogBanner> banners) {
        hideLoader();

        if (banners == null || banners.isEmpty()) {
            return;
        }

        ViewPager pager = getView().findViewById(R.id.view_pager_banner);
        pager.setAdapter(new CatalogBannerPagerAdapter(getContext(), banners, mPresenter));
        //adding bottom dots(Page Indicator)
        final CirclePageIndicator pageIndicator = getView().findViewById(R.id.page_indicator);
        pageIndicator.setFillColor(ContextCompat.getColor(getContext(), R.color.tkpd_main_green));
        pageIndicator.setPageColor(ContextCompat.getColor(getContext(), R.color.white_two));
        pageIndicator.setViewPager(pager, 0);
        getView().findViewById(R.id.container_pager).setVisibility(View.VISIBLE);

    }

    @Override
    public void onSuccessPoints(String point) {
        mTextPoints.setText(point);
    }

    @Override
    public void onErrorPoint(String errorMessage) {

    }

    @Override
    public void onSuccessFilter(CatalogFilterBase filters) {
        hideLoader();
        //Setting up sort types tabs
        mViewPagerAdapter = new CatalogSortTypePagerAdapter(getChildFragmentManager(), filters.getSortType(), mPresenter);
        mPagerSortType.setAdapter(mViewPagerAdapter);
        mTabSortType.setupWithViewPager(mPagerSortType);

        //setup category type
        mChipAdapter = new CatalogChipAdapter(mPresenter, filters.getCategories());
        mRecyclerViewChips.setLayoutManager(new LinearLayoutManager(getActivityContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewChips.addItemDecoration(new PaddingItemDecoration(getResources().getDimensionPixelSize(R.dimen.tp_margin_medium)));
        mRecyclerViewChips.setAdapter(mChipAdapter);

        //To ensure get data loaded for very first time for first fragment(Providing a small to ensure fragment get displayed).
        mPagerSortType.postDelayed(() -> refreshTab(getSelectedCategoryId()), CommonConstant.TAB_SETUP_DELAY_MS);
    }


    @Override
    public void hideLoader() {
        mContainerMain.setDisplayedChild(CONTAINER_DATA);
    }

    @Override
    public void gotoMyCoupons() {
        startActivity(MyCouponListingActivity.getCallingIntent(getContext()));
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(TokoPointComponent.class).inject(this);
    }

    @Override
    public void onClick(View source) {
        if (source.getId() == R.id.text_see_membership_status) {
            openWebView(CommonConstant.WebLink.MEMBERSHIP);
        } else if (source.getId() == R.id.text_my_coupon) {
            gotoMyCoupons();
        } else if (source.getId() == R.id.text_failed_action) {
            mPresenter.getHomePageData();
            mPresenter.getPointData();
        }

    }

    private void initViews(@NonNull View view) {
        mContainerMain = view.findViewById(R.id.container_main);
        mRecyclerViewChips = view.findViewById(R.id.list_chip);
        mPagerSortType = view.findViewById(R.id.view_pager_sort_type);
        mTabSortType = view.findViewById(R.id.tabs_sort_type);
        mTextPoints = view.findViewById(R.id.text_point_value);
        mTextFailedAction = view.findViewById(R.id.text_failed_action);
    }

    private void initListener() {
        getView().findViewById(R.id.text_my_coupon).setOnClickListener(this);
        getView().findViewById(R.id.text_failed_action).setOnClickListener(this);

        mPagerSortType.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                CatalogListItemFragment fragment = (CatalogListItemFragment) mViewPagerAdapter.getRegisteredFragment(position);

                if (fragment != null
                        && fragment.isAdded()) {
                    if (fragment.getPresenter() != null && fragment.getPresenter().isViewAttached()) {
                        fragment.getPresenter().getCatalog(getSelectedCategoryId(), fragment.getPresenter().getView().getCurrentSortType());
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void openWebView(String url) {
        Intent intent = SimpleWebViewWithFilePickerActivity.getIntent(getActivityContext(), url);
        startActivity(intent);
    }
}
