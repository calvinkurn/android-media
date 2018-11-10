package com.tokopedia.tokopoints.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.design.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.gamification.applink.ApplinkConstant;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.TokopointRouter;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.activity.MyCouponListingActivity;
import com.tokopedia.tokopoints.view.adapter.CatalogBannerPagerAdapter;
import com.tokopedia.tokopoints.view.adapter.CatalogChipAdapter;
import com.tokopedia.tokopoints.view.adapter.CatalogSortTypePagerAdapter;
import com.tokopedia.tokopoints.view.adapter.PaddingItemDecoration;
import com.tokopedia.tokopoints.view.contract.CatalogListingContract;
import com.tokopedia.tokopoints.view.model.CatalogBanner;
import com.tokopedia.tokopoints.view.model.CatalogCategory;
import com.tokopedia.tokopoints.view.model.CatalogFilterBase;
import com.tokopedia.tokopoints.view.model.LobDetails;
import com.tokopedia.tokopoints.view.model.LuckyEggEntity;
import com.tokopedia.tokopoints.view.model.TokoPointStatusPointsEntity;
import com.tokopedia.tokopoints.view.model.TokoPointStatusTierEntity;
import com.tokopedia.tokopoints.view.presenter.CatalogListingPresenter;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;
import com.tokopedia.tokopoints.view.util.TabUtil;

import java.util.ArrayList;
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
    private TextView mTextPoints, mTextMembershipValueBottom, mTextPointsBottom;
    private ImageView mImgEggBottom;
    private CatalogChipAdapter mChipAdapter;
    private CatalogSortTypePagerAdapter mViewPagerAdapter;
    private int mSelectedCategory = CommonConstant.DEFAULT_CATEGORY_TYPE;
    private int mSumToken;
    private LobDetails mLobDetails;

    @Inject
    public CatalogListingPresenter mPresenter;
    private LinearLayout bottomViewMembership;
    private ConstraintLayout containerPointDetail;
    private LinearLayout containerEgg;

    public static Fragment newInstance(Bundle extras) {
        Fragment fragment = new CatalogListingFragment();
        fragment.setArguments(extras);
        return fragment;
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
    public boolean isAddedView() {
        return getActivity() != null && isAdded();
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
    public void onSuccessPoints(String rewardStr, int rewardValue, String membership, String eggUrl) {
        mTextPoints.setText(rewardStr);
        mTextMembershipValueBottom.setText(membership);
        mTextPointsBottom.setText(CurrencyFormatUtil.convertPriceValue(rewardValue, false));
        ImageHandler.loadImageCircle2(getActivityContext(), mImgEggBottom, eggUrl);
    }

    @Override
    public void onErrorPoint(String errorMessage) {

    }

    @Override
    public void onSuccessFilter(CatalogFilterBase filters) {
        hideLoader();
        //Setting up sort types tabs
        mViewPagerAdapter = new CatalogSortTypePagerAdapter(getChildFragmentManager(), filters.getSortType(), mPresenter);

        mPagerSortType.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                AnalyticsTrackerUtil.sendEvent(getContext(),
                        AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                        "click " + filters.getSortType().get(position).getText(),
                        mPresenter.getCategoryName(filters.getCategories(), mPresenter.getSelectedCategoryId()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPagerSortType.setAdapter(mViewPagerAdapter);
        mTabSortType.setupWithViewPager(mPagerSortType);

        //setup category type
        if (filters.getCategories() != null && !filters.getCategories().isEmpty()) {
            mChipAdapter = new CatalogChipAdapter(mPresenter, filters.getCategories());
            mRecyclerViewChips.setLayoutManager(new LinearLayoutManager(getActivityContext(), LinearLayoutManager.HORIZONTAL, false));

            if (mRecyclerViewChips.getItemDecorationCount() == 0) {
                mRecyclerViewChips.addItemDecoration(new PaddingItemDecoration(getResources().getDimensionPixelSize(R.dimen.tp_margin_medium)));
            }

            mRecyclerViewChips.setAdapter(mChipAdapter);
        } else {
            mRecyclerViewChips.setVisibility(View.GONE);
        }

        mSelectedCategory = lookupForSelectedCategory(filters.getCategories());

        //To ensure get data loaded for very first time for first fragment(Providing a small to ensure fragment get displayed).
        mPagerSortType.postDelayed(() -> refreshTab(getSelectedCategoryId()), CommonConstant.TAB_SETUP_DELAY_MS);

        //excluding extra padding from tabs
        TabUtil.wrapTabIndicatorToTitle(mTabSortType,
                (int) getResources().getDimension(R.dimen.tp_margin_medium),
                (int) getResources().getDimension(R.dimen.tp_margin_regular));
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
        if (source.getId() == R.id.text_my_coupon) {
            gotoMyCoupons();
        } else if (source.getId() == R.id.text_failed_action) {
            mPresenter.getHomePageData();
            mPresenter.getPointData();
        } else if (source.getId() == R.id.text_token_title
                || source.getId() == R.id.img_token) {
            if (mSumToken <= 0) {
                StartPurchaseBottomSheet startPurchaseBottomSheet = new StartPurchaseBottomSheet();
                startPurchaseBottomSheet.setData(mLobDetails);
                startPurchaseBottomSheet.show(getChildFragmentManager(), mLobDetails.getTitle());
                AnalyticsTrackerUtil.sendEvent(source.getContext(),
                        AnalyticsTrackerUtil.EventKeys.EVENT_LUCKY_EGG,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_EGG,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_EGG_EMPTY,
                        "");
            } else {
                if (getActivity() != null) {
                    RouteManager.route(getActivity(), ApplinkConstant.GAMIFICATION);
                }

                AnalyticsTrackerUtil.sendEvent(source.getContext(),
                        AnalyticsTrackerUtil.EventKeys.EVENT_LUCKY_EGG,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_EGG,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_EGG,
                        "");
            }
        } else if (source.getId() == R.id.text_membership_label
                || source.getId() == R.id.text_membership_value_bottom) {
            openWebView(CommonConstant.WebLink.MEMBERSHIP);
        } else if (source.getId() == R.id.view_point_saya
                || source.getId() == R.id.text_my_points_value_bottom) {
            openWebView(CommonConstant.WebLink.HISTORY);
        }

    }

    private void initViews(@NonNull View view) {
        mContainerMain = view.findViewById(R.id.container_main);
        mRecyclerViewChips = view.findViewById(R.id.list_chip);
        mPagerSortType = view.findViewById(R.id.view_pager_sort_type);
        mTabSortType = view.findViewById(R.id.tabs_sort_type);
        mTextPoints = view.findViewById(R.id.text_point_value);
        bottomViewMembership = view.findViewById(R.id.bottom_view_membership);
        containerPointDetail = view.findViewById(R.id.container_point_detail);
        containerEgg = view.findViewById(R.id.container_fab_egg_token);
        mTextMembershipValueBottom = view.findViewById(R.id.text_membership_value_bottom);
        mTextPointsBottom = view.findViewById(R.id.text_my_points_value_bottom);
        mImgEggBottom = view.findViewById(R.id.img_egg_bottom);
        AppBarLayout appBarHeader = view.findViewById(R.id.app_bar_header);
        appBarHeader.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                verticalOffset = Math.abs(verticalOffset);
                if (verticalOffset >= containerPointDetail.getHeight()) {
                    slideUp();
                } else {
                    slideDown();
                }
            }
        });

        if (getArguments() != null && getArguments().getInt(CommonConstant.EXTRA_COUPON_COUNT) <= 0) {
            view.findViewById(R.id.text_my_coupon).setVisibility(View.GONE);
        }
    }

    private void slideUp() {
        if (bottomViewMembership.getVisibility() != View.VISIBLE) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) containerEgg.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, getResources().getDimensionPixelOffset(R.dimen.tp_margin_xxxlarge));
            Animation bottomUp = AnimationUtils.loadAnimation(bottomViewMembership.getContext(),
                    R.animator.tp_bottom_up);
            bottomViewMembership.startAnimation(bottomUp);
            bottomViewMembership.setVisibility(View.VISIBLE);
        }

    }

    private void slideDown() {
        if (bottomViewMembership.getVisibility() != View.GONE) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) containerEgg.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, getResources().getDimensionPixelOffset(R.dimen.tp_margin_large));
            Animation slideDown = AnimationUtils.loadAnimation(bottomViewMembership.getContext(),
                    R.animator.tp_bottom_down);
            bottomViewMembership.startAnimation(slideDown);
            bottomViewMembership.setVisibility(View.GONE);
        }
    }

    private void initListener() {
        if (getView() == null) {
            return;
        }

        getView().findViewById(R.id.text_my_coupon).setOnClickListener(this);
        getView().findViewById(R.id.text_failed_action).setOnClickListener(this);
        getView().findViewById(R.id.text_token_title).setOnClickListener(this);
        getView().findViewById(R.id.img_token).setOnClickListener(this);
        mTextMembershipValueBottom.setOnClickListener(this);
        mTextPointsBottom.setOnClickListener(this);

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
        ((TokopointRouter) getAppContext()).openTokoPoint(getContext(), url);
    }

    private int lookupForSelectedCategory(ArrayList<CatalogCategory> catalogCategories) {
        for (CatalogCategory each : catalogCategories) {
            if (each == null) {
                continue;
            }

            if (each.isSelected()) {
                return each.getId();
            }
        }

        return CommonConstant.DEFAULT_CATEGORY_TYPE;
    }

    @Override
    public void onSuccessTokenDetail(LuckyEggEntity tokenDetail, LobDetails lobDetails) {
        if (tokenDetail != null) {
            try {
                containerEgg.setVisibility(View.VISIBLE);
                TextView textCount = getView().findViewById(R.id.text_token_count);
                TextView textMessage = getView().findViewById(R.id.text_token_title);
                ImageView imgToken = getView().findViewById(R.id.img_token);
                textCount.setText(tokenDetail.getSumTokenStr());
                this.mSumToken = tokenDetail.getSumToken();
                this.mLobDetails = lobDetails;
                textMessage.setText(tokenDetail.getFloating().getTokenClaimText());
                ImageHandler.loadImageFitCenter(getContext(), imgToken, tokenDetail.getFloating().getTokenAsset().getFloatingImgUrl());

                if (mSumToken == 0) {
                    textCount.setVisibility(View.GONE);
                    textMessage.setPadding(getResources().getDimensionPixelSize(R.dimen.tp_padding_medium),
                            getResources().getDimensionPixelSize(R.dimen.dp_10),
                            0,
                            getResources().getDimensionPixelSize(R.dimen.dp_10));
                } else {
                    textCount.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //to avoid any accidental crash in order to prevent homepage error
            }
        }
    }
}
