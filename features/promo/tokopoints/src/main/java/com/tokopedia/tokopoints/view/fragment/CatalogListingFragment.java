package com.tokopedia.tokopoints.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.design.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.gamification.applink.ApplinkConstant;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.TokopointRouter;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.activity.CatalogListingActivity;
import com.tokopedia.tokopoints.view.activity.MyCouponListingActivity;
import com.tokopedia.tokopoints.view.adapter.CatalogBannerPagerAdapter;
import com.tokopedia.tokopoints.view.adapter.CatalogSortTypePagerAdapter;
import com.tokopedia.tokopoints.view.contract.CatalogListingContract;
import com.tokopedia.tokopoints.view.interfaces.onAppBarCollapseListener;
import com.tokopedia.tokopoints.view.model.CatalogBanner;
import com.tokopedia.tokopoints.view.model.CatalogFilterBase;
import com.tokopedia.tokopoints.view.model.CatalogFilterPointRange;
import com.tokopedia.tokopoints.view.model.CatalogSubCategory;
import com.tokopedia.tokopoints.view.model.LobDetails;
import com.tokopedia.tokopoints.view.model.LuckyEggEntity;
import com.tokopedia.tokopoints.view.presenter.CatalogListingPresenter;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;
import com.tokopedia.tokopoints.view.util.TabUtil;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class CatalogListingFragment extends BaseDaggerFragment implements CatalogListingContract.View, View.OnClickListener, FiltersBottomSheet.OnSaveFilterCallback {
    private static final int CONTAINER_LOADER = 0;
    private static final int CONTAINER_DATA = 1;
    private static final int CONTAINER_ERROR = 2;
    private ViewFlipper mContainerMain;
    private ViewPager mPagerSortType;
    private TabLayout mTabSortType;
    private TextView mTextPoints, mTextMembershipValueBottom, mTextPointsBottom;
    private ImageView mImgEggBottom;
    private CatalogSortTypePagerAdapter mViewPagerAdapter;
    private int mSumToken;
    private LobDetails mLobDetails;
    private TextView mTvFlashTimer, mTvFlashTimerLabel;
    private ProgressBar mProgressFlash;
    private ConstraintLayout mContainerFlashTimer;
    /*This section is exclusively for handling flash-sale timer*/
    public CountDownTimer mFlashTimer;
    private AppBarLayout mAppBarHeader;

    @Inject
    public CatalogListingPresenter mPresenter;
    private LinearLayout bottomViewMembership;
    private ConstraintLayout mContainerPointDetail;
    private LinearLayout containerEgg;
    private onAppBarCollapseListener appBarCollapseListener;
    private boolean isPointsAvailable = false;
    private FiltersBottomSheet filtersBottomSheet;
    private MenuItem menuItemFilter;

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
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tp_menu_catalog_listing, menu);
        menuItemFilter = menu.findItem(R.id.filter_menu_item);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.filter_menu_item) {
            if (filtersBottomSheet != null) {
                filtersBottomSheet.show(getChildFragmentManager(), "Filters");
            }
            return true;
        }
        return false;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        mTvFlashTimer = view.findViewById(R.id.tv_flash_time);
        mTvFlashTimerLabel = view.findViewById(R.id.tv_timer_label);
        mProgressFlash = view.findViewById(R.id.progress_timer);
        mContainerFlashTimer = view.findViewById(R.id.cl_flash_container);
        initListener();

        if (isSeeAllPage()) {
            mPresenter.getHomePageData("", "", false);
        } else {
            mPresenter.getPointData();
            mPresenter.getHomePageData(getArguments().getString(CommonConstant.ARGS_SLUG_CATEGORY),
                    getArguments().getString(CommonConstant.ARGS_SLUG_SUB_CATEGORY), true);
        }
    }

    @Override
    public void onDestroy() {
        mPresenter.destroyView();
        super.onDestroy();

        if (mFlashTimer != null) {
            mFlashTimer.cancel();
            mFlashTimer = null;
        }
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
    public void refreshTab() {
        CatalogListItemFragment fragment = (CatalogListItemFragment) mViewPagerAdapter.getRegisteredFragment(mPagerSortType.getCurrentItem());
        if (fragment != null
                && fragment.isAdded()) {
            if (fragment.getPresenter() != null && fragment.getPresenter().isViewAttached()) {
                fragment.getPresenter().setPointRange(mPresenter.getPointRangeId());
                fragment.getPresenter().getCatalog(mPresenter.getCurrentCategoryId(), mPresenter.getCurrentSubCategoryId(), true);
            }
        }
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
        mAppBarHeader.addOnOffsetChangedListener(offsetChangedListenerAppBarElevation);

    }

    @Override
    public void onSuccessPoints(String rewardStr, int rewardValue, String membership, String eggUrl) {
        if (!rewardStr.isEmpty())
            mTextPoints.setText(rewardStr);
        if (!membership.isEmpty())
            mTextMembershipValueBottom.setText(membership);
        mTextPointsBottom.setText(CurrencyFormatUtil.convertPriceValue(rewardValue, false));
        if (!eggUrl.isEmpty())
            ImageHandler.loadImageCircle2(getActivityContext(), mImgEggBottom, eggUrl);
        mContainerPointDetail.setVisibility(View.VISIBLE);
        isPointsAvailable = true;
        mAppBarHeader.addOnOffsetChangedListener(offsetChangedListenerAppBarElevation);
        mAppBarHeader.addOnOffsetChangedListener(offsetChangedListenerBottomView);
    }

    @Override
    public void onErrorPoint(String errorMessage) {

    }

    @Override
    public void onSuccessFilter(CatalogFilterBase filters) {
        hideLoader();

        //Setting up filters
        setUpFilters(filters.getPointRanges());
        //Setting up subcategories types tabs
        if (filters == null
                || filters.getCategories() == null
                || filters.getCategories().isEmpty()) {
            //To ensure get data loaded for very first time for first fragment(Providing a small to ensure fragment get displayed).
            mViewPagerAdapter = new CatalogSortTypePagerAdapter(getChildFragmentManager(), null);
            mViewPagerAdapter.setPointsAvailable(isPointsAvailable);
            //TODO please replace with
            mPresenter.setCurrentCategoryId(0);
            mPresenter.setCurrentSubCategoryId(0);
            mPagerSortType.postDelayed(() -> refreshTab(), CommonConstant.TAB_SETUP_DELAY_MS);
            mTabSortType.setVisibility(View.GONE);
        } else if (filters.getCategories().get(0) != null
                && (filters.getCategories().get(0).isHideSubCategory() || filters.getCategories().get(0).getSubCategory() == null || filters.getCategories().get(0).getSubCategory().isEmpty())) {
            mViewPagerAdapter = new CatalogSortTypePagerAdapter(getChildFragmentManager(), null);
            mViewPagerAdapter.setPointsAvailable(isPointsAvailable);
            mPagerSortType.setAdapter(mViewPagerAdapter);
            mTabSortType.setupWithViewPager(mPagerSortType);
            mTabSortType.setVisibility(View.GONE);
            if (TextUtils.isEmpty(filters.getCategories().get(0).getName())) {
                updateToolbarTitle("Semua Kupon");
            } else {
                updateToolbarTitle(filters.getCategories().get(0).getName());
            }
            mPresenter.setCurrentCategoryId(filters.getCategories().get(0).getId());
            mPresenter.setCurrentSubCategoryId(0);
            mPagerSortType.postDelayed(() -> refreshTab(), CommonConstant.TAB_SETUP_DELAY_MS);
        } else if (filters.getCategories().get(0) != null
                && filters.getCategories().get(0).getSubCategory() != null) {
            mTabSortType.setVisibility(View.VISIBLE);
            updateToolbarTitle(filters.getCategories().get(0).getName());
            mViewPagerAdapter = new CatalogSortTypePagerAdapter(getChildFragmentManager(), filters.getCategories().get(0).getSubCategory());
            mViewPagerAdapter.setPointsAvailable(isPointsAvailable);
            mPagerSortType.setAdapter(mViewPagerAdapter);
            mTabSortType.setupWithViewPager(mPagerSortType);
            mPagerSortType.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    AnalyticsTrackerUtil.sendEvent(getContext(),
                            AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                            AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                            "click " + filters.getCategories().get(0).getSubCategory().get(position).getName(),
                            filters.getCategories().get(0).getSubCategory().get(position).getName());

                    CatalogListItemFragment fragment = (CatalogListItemFragment) mViewPagerAdapter.getRegisteredFragment(position);

                    if (fragment != null
                            && fragment.isAdded()) {
                        if (fragment.getPresenter() != null && fragment.getPresenter().isViewAttached()) {
                            mPresenter.setCurrentCategoryId(filters.getCategories().get(0).getId());
                            mPresenter.setCurrentSubCategoryId(filters.getCategories().get(0).getSubCategory().get(position).getId());
                            fragment.getPresenter().setPointRange(mPresenter.getPointRangeId());
                            fragment.getPresenter().getCatalog(mPresenter.getCurrentCategoryId(), mPresenter.getCurrentSubCategoryId(), true);
                        }
                    }

                    if (filters.getCategories().get(0).getSubCategory().get(position).getTimeRemainingSeconds() > 0) {
                        startFlashTimer(filters.getCategories().get(0).getSubCategory().get(position));
                        mContainerFlashTimer.setVisibility(View.VISIBLE);
                    } else {
                        mContainerFlashTimer.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            //excluding extra padding from tabs
            TabUtil.wrapTabIndicatorToTitle(mTabSortType,
                    (int) getResources().getDimension(R.dimen.tp_margin_medium),
                    (int) getResources().getDimension(R.dimen.tp_margin_regular));

            mPagerSortType.postDelayed(() -> {
                int selectedTabIndex = getSelectedCategoryIndex(filters.getCategories().get(0).getSubCategory());
                if (selectedTabIndex == 0) { // Special handling for zeroth index
                    mPresenter.setCurrentCategoryId(filters.getCategories().get(0).getId());
                    mPresenter.setCurrentSubCategoryId(filters.getCategories().get(0).getSubCategory().get(0).getId());
                    refreshTab();

                    try {
                        if (filters.getCategories().get(0).getSubCategory().get(0).getTimeRemainingSeconds() > 0) {
                            startFlashTimer(filters.getCategories().get(0).getSubCategory().get(0));
                            mContainerFlashTimer.setVisibility(View.VISIBLE);
                        } else {
                            mContainerFlashTimer.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {

                    }
                } else {
                    mPagerSortType.setCurrentItem(selectedTabIndex, false);
                }
            }, CommonConstant.TAB_SETUP_DELAY_MS);
        }
    }

    private void setUpFilters(List<CatalogFilterPointRange> pointRanges) {
        if (pointRanges != null && pointRanges.size() != 0 && isSeeAllPage()) {
            if (menuItemFilter != null)
                menuItemFilter.setVisible(true);
            filtersBottomSheet = new FiltersBottomSheet();
            pointRanges.get(0).setSelected(true);       //set Default selected
            mPresenter.setPointRangeId(pointRanges.get(0).getId());        //set Default Id
            filtersBottomSheet.setData(pointRanges, this);
        } else {
            if (menuItemFilter != null)
                menuItemFilter.setVisible(false);
        }

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
        if (context instanceof CatalogListingActivity)
            appBarCollapseListener = (onAppBarCollapseListener) context;
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
            if (isSeeAllPage()) {
                mPresenter.getHomePageData("", "", false);
            } else {
                mPresenter.getPointData();
                mPresenter.getHomePageData(getArguments().getString(CommonConstant.ARGS_SLUG_CATEGORY),
                        getArguments().getString(CommonConstant.ARGS_SLUG_SUB_CATEGORY), true);
            }

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
                || source.getId() == R.id.bottom_view_membership) {
            ((TokopointRouter) getAppContext()).openTokopointWebview(getContext(), CommonConstant.WebLink.MEMBERSHIP, getString(R.string.tp_label_membership));
        } else if (source.getId() == R.id.view_point_saya
                || source.getId() == R.id.text_my_points_value_bottom) {
            ((TokopointRouter) getAppContext()).openTokopointWebview(getContext(), CommonConstant.WebLink.HISTORY, getString(R.string.tp_history));
        }

    }

    private void initViews(@NonNull View view) {
        mContainerMain = view.findViewById(R.id.container_main);
        mPagerSortType = view.findViewById(R.id.view_pager_sort_type);
        mTabSortType = view.findViewById(R.id.tabs_sort_type);
        mTextPoints = view.findViewById(R.id.text_point_value);
        bottomViewMembership = view.findViewById(R.id.bottom_view_membership);
        mContainerPointDetail = view.findViewById(R.id.container_point_detail);
        containerEgg = view.findViewById(R.id.container_fab_egg_token);
        mTextMembershipValueBottom = view.findViewById(R.id.text_membership_value_bottom);
        mTextPointsBottom = view.findViewById(R.id.text_my_points_value_bottom);
        mImgEggBottom = view.findViewById(R.id.img_egg_bottom);
        mAppBarHeader = view.findViewById(R.id.app_bar_header);
        if (getArguments() != null && getArguments().getInt(CommonConstant.EXTRA_COUPON_COUNT) <= 0) {
            view.findViewById(R.id.text_my_coupon).setVisibility(View.GONE);
        }
    }

    AppBarLayout.OnOffsetChangedListener offsetChangedListenerBottomView = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            verticalOffset = Math.abs(verticalOffset);
            if (verticalOffset >= mContainerPointDetail.getHeight()) {
                slideUp();
            } else {
                slideDown();
            }
        }
    };

    AppBarLayout.OnOffsetChangedListener offsetChangedListenerAppBarElevation = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (appBarCollapseListener != null) {
                verticalOffset = Math.abs(verticalOffset);
                if (verticalOffset >= appBarLayout.getTotalScrollRange()) {     //Appbar is hidden now
                    appBarCollapseListener.hideToolbarElevation();
                } else {
                    appBarCollapseListener.showToolbarElevation();
                }
            }
        }
    };

    private void slideUp() {
        if (bottomViewMembership.getVisibility() != View.VISIBLE) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) containerEgg.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, getResources().getDimensionPixelOffset(R.dimen.tp_margin_xxxlarge));
            Animation bottomUp = AnimationUtils.loadAnimation(bottomViewMembership.getContext(),
                    R.anim.tp_bottom_up);
            bottomViewMembership.startAnimation(bottomUp);
            bottomViewMembership.setVisibility(View.VISIBLE);
        }

    }

    private void slideDown() {
        if (bottomViewMembership.getVisibility() != View.GONE) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) containerEgg.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, getResources().getDimensionPixelOffset(R.dimen.tp_margin_large));
            Animation slideDown = AnimationUtils.loadAnimation(bottomViewMembership.getContext(),
                    R.anim.tp_bottom_down);
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
        bottomViewMembership.setOnClickListener(this);
        mTextPointsBottom.setOnClickListener(this);
    }

    @Override
    public void openWebView(String url) {
        ((TokopointRouter) getAppContext()).openTokoPoint(getContext(), url);
    }

    @Override
    public void onSuccessTokenDetail(LuckyEggEntity tokenDetail, LobDetails lobDetails) {
        if (tokenDetail != null) {
            try {
                if (tokenDetail.isOffFlag()) {
                    return;
                }

                containerEgg.setVisibility(View.VISIBLE);
                TextView textCount = getView().findViewById(R.id.text_token_count);
                TextView textMessage = getView().findViewById(R.id.text_token_title);
                ImageView imgToken = getView().findViewById(R.id.img_token);
                textCount.setText(tokenDetail.getSumTokenStr());
                this.mSumToken = tokenDetail.getSumToken();
                this.mLobDetails = lobDetails;
                textMessage.setText(tokenDetail.getFloating().getTokenClaimCustomText());
                ImageHandler.loadImageFitCenter(getContext(), imgToken, tokenDetail.getFloating().getTokenAsset().getFloatingImgUrl());

                if (mSumToken == 0) {
                    textCount.setVisibility(View.GONE);
                    textMessage.setPadding(getResources().getDimensionPixelSize(R.dimen.dp_30),
                            0,
                            0,
                            0);
                } else {
                    textCount.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //to avoid any accidental crash in order to prevent homepage error
            }
        }
    }

    private void startFlashTimer(CatalogSubCategory subCategory) {
        if (getArguments() == null) {
            return;
        }

        if (subCategory.getTimeRemainingSeconds() < 1) {
            mContainerFlashTimer.setVisibility(View.GONE);
            return;
        }

        if (mFlashTimer != null) {
            mFlashTimer.cancel();
            mFlashTimer = null;
        }

        if (subCategory.getTimerLabel() != null) {
            mTvFlashTimerLabel.setText(subCategory.getTimerLabel());
        }

        /*This section is exclusively for handling flash-sale timer*/
        mProgressFlash.setMax((int) CommonConstant.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S);
        mFlashTimer = new CountDownTimer(subCategory.getTimeRemainingSeconds() * 1000, 1000) {
            @Override
            public void onTick(long l) {
                subCategory.setTimeRemainingSeconds(l / 1000);
                int seconds = (int) (l / 1000) % 60;
                int minutes = (int) ((l / (1000 * 60)) % 60);
                int hours = (int) ((l / (1000 * 60 * 60)) % 24);
                mTvFlashTimer.setText(String.format(Locale.ENGLISH, "%02d : %02d : %02d", hours, minutes, seconds));
                mProgressFlash.setProgress((int) l / 1000);
            }

            @Override
            public void onFinish() {
                if (isSeeAllPage()) {
                    mPresenter.getHomePageData("", "", false);
                } else {
                    mPresenter.getPointData();
                    mPresenter.getHomePageData(getArguments().getString(CommonConstant.ARGS_SLUG_CATEGORY),
                            getArguments().getString(CommonConstant.ARGS_SLUG_SUB_CATEGORY), true);
                }
            }
        }.start();
    }

    private void updateToolbarTitle(String title) {
        if (getActivity() != null && title != null) {
            ((BaseSimpleActivity) getActivity()).updateTitle(title);
        }
    }

    private boolean isSeeAllPage() {
        if (getArguments() == null
                || StringUtils.isBlank(getArguments().getString(CommonConstant.ARGS_SLUG_CATEGORY))) {
            return true;
        }

        return false;
    }

    private int getSelectedCategoryIndex(List<CatalogSubCategory> data) {
        int counter = 0;
        for (CatalogSubCategory item : data) {
            if (item.isSelected()) {
                break;
            }
            counter++;
        }

        return counter;
    }

    @Override
    public void onSaveFilter(CatalogFilterPointRange filter, int selectedPosition) {
        if (filter != null) {
            if (menuItemFilter != null) {
                if (selectedPosition == 0) {
                    menuItemFilter.setIcon(R.drawable.ic_filter_button_unselected);
                } else {
                    menuItemFilter.setIcon(R.drawable.ic_filter_button_selected);
                }
            }
            if (mPresenter.getPointRangeId() != filter.getId()) {
                mPresenter.setPointRangeId(filter.getId());
                refreshTab();
            }
        }
    }
}
