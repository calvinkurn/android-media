package com.tokopedia.home.beranda.presentation.view.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.design.countdown.CountDownView;
import com.tokopedia.design.keyboard.KeyboardHelper;
import com.tokopedia.digital.common.analytic.DigitalEventTracking;
import com.tokopedia.gamification.floating.view.fragment.FloatingEggButtonFragment;
import com.tokopedia.home.IHomeRouter;
import com.tokopedia.home.R;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData;
import com.tokopedia.home.beranda.di.BerandaComponent;
import com.tokopedia.home.beranda.di.DaggerBerandaComponent;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.listener.ActivityStateListener;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.listener.HomeEggListener;
import com.tokopedia.home.beranda.listener.HomeFeedsListener;
import com.tokopedia.home.beranda.listener.HomeInspirationListener;
import com.tokopedia.home.beranda.listener.HomeTabFeedListener;
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.home.beranda.presentation.view.HomeContract;
import com.tokopedia.home.beranda.presentation.view.SectionContainer;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeFeedPagerAdapter;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter;
import com.tokopedia.home.beranda.presentation.view.adapter.LinearLayoutManagerWithSmoothScroller;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeAdapterFactory;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.CashBackData;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils;
import com.tokopedia.home.beranda.presentation.view.customview.CollapsingTabLayout;
import com.tokopedia.home.beranda.presentation.view.viewmodel.FeedTabModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.home.constant.BerandaUrl;
import com.tokopedia.home.constant.ConstantKey;
import com.tokopedia.home.util.ServerTimeOffsetUtil;
import com.tokopedia.home.widget.FloatingTextButton;
import com.tokopedia.home.widget.ToggleableSwipeRefreshLayout;
import com.tokopedia.loyalty.view.activity.PromoListActivity;
import com.tokopedia.loyalty.view.activity.TokoPointWebviewActivity;
import com.tokopedia.navigation_common.listener.AllNotificationListener;
import com.tokopedia.navigation_common.listener.FragmentListener;
import com.tokopedia.navigation_common.listener.ShowCaseListener;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.searchbar.MainToolbar;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.tokocash.TokoCashRouter;
import com.tokopedia.tokocash.pendingcashback.domain.PendingCashback;
import com.tokopedia.tokopoints.ApplinkConstant;
import com.tokopedia.tokopoints.notification.TokoPointsNotificationManager;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.trackingoptimizer.TrackingQueue;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by errysuprayogi on 11/27/17.
 */
public class HomeFragment extends BaseDaggerFragment implements HomeContract.View,
        SwipeRefreshLayout.OnRefreshListener, HomeCategoryListener,
        CountDownView.CountDownListener, AllNotificationListener, FragmentListener,
        HomeEggListener, HomeTabFeedListener, HomeInspirationListener, HomeFeedsListener {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final String BERANDA_TRACE = "gl_beranda";
    private static final String MAINAPP_SHOW_REACT_OFFICIAL_STORE = "mainapp_react_show_os";
    private static final String TOKOPOINTS_NOTIFICATION_TYPE = "drawer";
    private static final int REQUEST_CODE_DIGITAL_CATEGORY_LIST = 222;
    private static final int REQUEST_CODE_DIGITAL_PRODUCT_DETAIL = 220;
    private static final int DEFAULT_FEED_PAGER_OFFSCREEN_LIMIT = 10;
    String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private ActivityStateListener activityStateListener;

    public static final long ONE_SECOND = 1000l;
    @Inject
    HomePresenter presenter;

    private UserSessionInterface userSession;
    private View fragmentRootView;
    private RecyclerView recyclerView;
    private TabLayout tabLayout;
    private CoordinatorLayout root;
    private SectionContainer tabContainer;
    private ToggleableSwipeRefreshLayout refreshLayout;
    private HomeRecycleAdapter adapter;
    private RemoteConfig firebaseRemoteConfig;
    private PerformanceMonitoring performanceMonitoring;
    private SnackbarRetry messageSnackbar;
    private String[] tabSectionTitle;
    private LinearLayoutManager layoutManager;
    private FloatingTextButton floatingTextButton;
    private boolean showRecomendation;
    private boolean mShowTokopointNative;
    private RecyclerView.OnScrollListener onEggScrollListener;
    private ViewPager homeFeedsViewPager;
    private CollapsingTabLayout homeFeedsTabLayout;
    private AppBarLayout appBarLayout;
    private HomeFeedPagerAdapter homeFeedPagerAdapter;
    private int lastOffset;
    private int fragmentHeight;
    private int actionBarHeight;

    private TrackingQueue trackingQueue;

    private MainToolbar mainToolbar;

    private long serverTimeOffset = 0;

    public static final String SCROLL_RECOMMEND_LIST = "recommend_list";

    private boolean scrollToRecommendList = false;
    private boolean isTraceStopped = false;
    private boolean isFeedLoaded = false;

    public static HomeFragment newInstance(boolean scrollToRecommendList) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putBoolean(SCROLL_RECOMMEND_LIST, scrollToRecommendList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performanceMonitoring = PerformanceMonitoring.start(BERANDA_TRACE);
        userSession = new UserSession(getActivity());
        trackingQueue = new TrackingQueue(getActivity());
    }

    @Override
    protected String getScreenName() {
        return ConstantKey.Analytics.AppScreen.UnifyTracking.SCREEN_UNIFY_HOME_BERANDA;
    }

    @Override
    protected void initInjector() {
        if (getActivity() != null) {
            BerandaComponent component = DaggerBerandaComponent.builder().baseAppComponent(((BaseMainApplication)
                    getActivity().getApplication()).getBaseAppComponent()).build();
            component.inject(this);
            component.inject(presenter);
        }
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public void reInitInjector(BerandaComponent component) {
        component.inject(this);
        component.inject(presenter);
        presenter.attachView(this);
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public HomePresenter getPresenter() {
        return presenter;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public void setPresenter(HomePresenter presenter) {
        this.presenter = presenter;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public void clearAll() {
        adapter.clearItems();
        adapter.notifyDataSetChanged();
    }

    private void fetchRemoteConfig() {
        firebaseRemoteConfig = new FirebaseRemoteConfigImpl(getActivity());
        showRecomendation = firebaseRemoteConfig.getBoolean(ConstantKey.RemoteConfigKey.APP_SHOW_RECOMENDATION_BUTTON, false);
        mShowTokopointNative = firebaseRemoteConfig.getBoolean(ConstantKey.RemoteConfigKey.APP_SHOW_TOKOPOINT_NATIVE, true);
    }

    @Override
    public void showRecomendationButton() {
        if (showRecomendation) {
            floatingTextButton.setVisibility(View.VISIBLE);
            HomePageTracking.eventImpressionJumpRecomendation(getActivity());
        } else {
            floatingTextButton.setVisibility(View.GONE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mainToolbar = view.findViewById(R.id.toolbar);
        recyclerView = view.findViewById(R.id.list);
        refreshLayout = view.findViewById(R.id.home_swipe_refresh_layout);
        tabLayout = view.findViewById(R.id.tabs);
        tabContainer = view.findViewById(R.id.tab_container);
        floatingTextButton = view.findViewById(R.id.recom_action_button);
        root = view.findViewById(R.id.root);
        homeFeedsViewPager = view.findViewById(R.id.view_pager_home_feeds);
        homeFeedsTabLayout = view.findViewById(R.id.tab_layout_home_feeds);
        appBarLayout = view.findViewById(R.id.app_bar_layout);

        if (getArguments() != null) {
            scrollToRecommendList = getArguments().getBoolean(SCROLL_RECOMMEND_LIST);
        }

        initEggDragListener();

        presenter.attachView(this);
        fetchTokopointsNotification(TOKOPOINTS_NOTIFICATION_TYPE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentRootView = view;
        initResources();
        disableExpandFeedSection();
    }

    private void initResources() {
        TypedValue typedValue = new TypedValue();
        if (getActivity() != null && getActivity().getTheme() != null &&
                getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
        }
    }

    private void disableExpandFeedSection() {
        if (fragmentHeight > 0) {
            setMargins(mainToolbar, 0, 0, 0, fragmentHeight - actionBarHeight);
            return;
        }

        if (fragmentRootView != null) {
            fragmentRootView.post(new Runnable() {
                @Override
                public void run() {
                    fragmentHeight = fragmentRootView.getMeasuredHeight();
                    setMargins(mainToolbar, 0, 0, 0, fragmentHeight - actionBarHeight);
                }
            });
        }
    }

    private void enableExpandFeedSection() {
        setMargins(mainToolbar, 0, 0, 0, 0);
    }

    private void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    private void initEggDragListener() {
        FloatingEggButtonFragment floatingEggButtonFragment = getFloatingEggButtonFragment();
        if (floatingEggButtonFragment != null) {
            floatingEggButtonFragment.setOnDragListener(new FloatingEggButtonFragment.OnDragListener() {
                @Override
                public void onDragStart() {
                    refreshLayout.setCanChildScrollUp(true);
                }

                @Override
                public void onDragEnd() {
                    if (isAppBarFullyExpanded(lastOffset)) {
                        refreshLayout.setCanChildScrollUp(false);
                    }
                }
            });
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.onFirstLaunch();
        initTabNavigation();
        initAdapter();
        initRefreshLayout();
        initAppBarScrollListener();
        initEggTokenScrollListener();
        registerBroadcastReceiverTokoCash();
        fetchRemoteConfig();
        floatingTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollToRecommendList();
                HomePageTracking.eventClickJumpRecomendation(getActivity());
            }
        });

        KeyboardHelper.setKeyboardVisibilityChangedListener(root, new KeyboardHelper.OnKeyboardVisibilityChangedListener() {
            @Override
            public void onKeyboardShown() {
                floatingTextButton.forceHide();
            }

            @Override
            public void onKeyboardHide() {
                floatingTextButton.resetState();
            }
        });
    }

    private void initHomeFeedsViewPager(List<FeedTabModel> feedTabModelList) {
        enableExpandFeedSection();
        homeFeedsTabLayout.setVisibility(View.VISIBLE);
        homeFeedsViewPager.setVisibility(View.VISIBLE);
        if (homeFeedPagerAdapter == null) {
            homeFeedPagerAdapter = new HomeFeedPagerAdapter(
                    this,
                    this,
                    getChildFragmentManager(),
                    feedTabModelList,
                    trackingQueue);
        } else {
            homeFeedPagerAdapter.updateData(feedTabModelList);
        }
        homeFeedsViewPager.setOffscreenPageLimit(DEFAULT_FEED_PAGER_OFFSCREEN_LIMIT);
        homeFeedsViewPager.setAdapter(homeFeedPagerAdapter);
        homeFeedsTabLayout.setup(homeFeedsViewPager, convertToTabItemDataList(feedTabModelList));
        homeFeedsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FeedTabModel selectedFeedTabModel =
                        feedTabModelList.get(tab.getPosition());
                HomePageTracking.eventClickOnHomePageRecommendationTab(
                        trackingQueue,
                        selectedFeedTabModel
                );
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                homeFeedPagerAdapter.getHomeFeedFragmentList().get(tab.getPosition()).scrollToTop();
                homeFeedsTabLayout.resetCollapseState();
            }
        });
    }

    private List<CollapsingTabLayout.TabItemData> convertToTabItemDataList(List<FeedTabModel> feedTabModelList) {
        List<CollapsingTabLayout.TabItemData> tabItemDataList = new ArrayList<>();
        for (FeedTabModel feedTabModel : feedTabModelList) {
            tabItemDataList.add(new CollapsingTabLayout.TabItemData(feedTabModel.getName(), feedTabModel.getImageUrl()));
        }
        return tabItemDataList;
    }

    private void scrollToRecommendList() {
        appBarLayout.setExpanded(false, true);
        homeFeedsTabLayout.resetCollapseState();
        scrollToRecommendList = false;
    }

    private void initTabNavigation() {
        tabSectionTitle = getResources().getStringArray(R.array.section_title);
        TypedArray icons = getResources().obtainTypedArray(R.array.section_icon);
        for (int i = 0; i < tabSectionTitle.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setIcon(icons.getResourceId(i, R.drawable.ic_beli));
            tabLayout.addTab(tab);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof ShowCaseListener) { // show on boarding and notify mainparent
            ((ShowCaseListener) getActivity()).onReadytoShowBoarding(buildShowCase());
        }
        presenter.onResume();
        if (activityStateListener != null) {
            activityStateListener.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        trackingQueue.sendAll();
        if (activityStateListener != null) {
            activityStateListener.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        presenter.detachView();
        recyclerView.setAdapter(null);
        adapter = null;
        recyclerView.setLayoutManager(null);
        layoutManager = null;
        presenter = null;
        unRegisterBroadcastReceiverTokoCash();
    }

    private void initRefreshLayout() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (presenter != null) {
                    presenter.getHomeData();
                    presenter.getHeaderData(true);
                }
            }
        });
        refreshLayout.setOnRefreshListener(this);
    }

    private void loadEggData() {
        FloatingEggButtonFragment floatingEggButtonFragment = getFloatingEggButtonFragment();
        if (floatingEggButtonFragment != null) {
            floatingEggButtonFragment.loadEggData();
        }
    }

    private void initAppBarScrollListener() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {

                if (offset == lastOffset) {
                    return;
                }

                if (isAppBarFullyExpanded(offset)) {
                    refreshLayout.setCanChildScrollUp(false);
                } else {
                    refreshLayout.setCanChildScrollUp(true);
                }

                hideEggFragmentOnScrolling();

                if (isAppBarFullyCollapsed(offset)) {
                    floatingTextButton.setVisibility(View.INVISIBLE);
                } else if (showRecomendation) {
                    floatingTextButton.setVisibility(View.VISIBLE);
                }

                if (isAppBarScrollDown(offset) && !floatingTextButton.isAnimationStart()) {
                    floatingTextButton.hide();
                } else if (isAppBarScrollUp(offset) && !floatingTextButton.isAnimationStart()) {
                    floatingTextButton.show();
                }
                lastOffset = offset;
            }
        });
    }

    private boolean isAppBarScrollUp(int offset) {
        return offset > lastOffset;
    }

    private boolean isAppBarScrollDown(int offset) {
        return offset < lastOffset;
    }

    private boolean isAppBarFullyExpanded(int offset) {
        return offset == 0;
    }

    private boolean isAppBarFullyCollapsed(int offset) {
        return Math.abs(offset) >= appBarLayout.getTotalScrollRange();
    }

    private void hideEggFragmentOnScrolling() {
        FloatingEggButtonFragment floatingEggButtonFragment = getFloatingEggButtonFragment();
        if (floatingEggButtonFragment != null) {
            floatingEggButtonFragment.hideOnScrolling();
        }
    }

    private void initEggTokenScrollListener() {
        onEggScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy == 0) {
                    return;
                }
                FloatingEggButtonFragment floatingEggButtonFragment = getFloatingEggButtonFragment();
                if (floatingEggButtonFragment != null) {
                    floatingEggButtonFragment.hideOnScrolling();
                }
            }
        };

        recyclerView.removeOnScrollListener(onEggScrollListener);
        recyclerView.addOnScrollListener(onEggScrollListener);
    }

    private FloatingEggButtonFragment getFloatingEggButtonFragment() {
        // https://stackoverflow.com/questions/28672883/java-lang-illegalstateexception-fragment-not-attached-to-activity
        if (getActivity() != null && isAdded() && getChildFragmentManager() != null) {
            return (FloatingEggButtonFragment) getChildFragmentManager().findFragmentById(R.id.floating_egg_fragment);
        }
        return null;
    }

    private boolean isErrorMessageShown() {
        return messageSnackbar != null && messageSnackbar.isShown();
    }

    private void initAdapter() {
        layoutManager = new LinearLayoutManagerWithSmoothScroller(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.getItemAnimator().setChangeDuration(0);
        HomeAdapterFactory adapterFactory = new HomeAdapterFactory(
                getChildFragmentManager(),
                this,
                this,
                this,
                this
        );
        adapter = new HomeRecycleAdapter(adapterFactory, new ArrayList<Visitable>());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSectionItemClicked(String actionLink) {
        onActionLinkClicked(actionLink);
    }

    private void onGoToSell() {
        if (isUserLoggedIn()) {
            String shopId = getUserShopId();
            if (!shopId.equals("0")) {
                onGoToShop(shopId);
            } else {
                onGoToCreateShop();
            }
        } else {
            onGoToLogin();
        }
    }

    private void onGoToLogin() {
        Intent intent = ((IHomeRouter) getActivity().getApplication()).getLoginIntent(getContext());
        Intent intentHome = ((IHomeRouter) getActivity().getApplication()).getHomeIntent(getContext());
        intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getActivity().startActivities(new Intent[]{intentHome, intent});
        getActivity().finish();
    }

    private void onGoToCreateShop() {
        Intent intent = ((IHomeRouter) getActivity().getApplication()).getIntentCreateShop(getContext());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    private void onGoToShop(String shopId) {
        Intent intent = ((IHomeRouter) getActivity().getApplication()).getShopPageIntent(getActivity(), shopId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }


    @Override
    public void onDigitalMoreClicked(int pos) {
        AnalyticTracker tracker = HomePageTracking.getTracker(getActivity());
        if (tracker != null) {
            tracker.sendEventTracking(
                    DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                    DigitalEventTracking.Category.DIGITAL_HOMEPAGE,
                    DigitalEventTracking.Action.CLICK_SEE_ALL_PRODUCTS,
                    ""
            );
        }
    }

    @Override
    public void openShop() {
        onGoToSell();
    }

    @Override
    public void actionAppLinkWalletHeader(String appLinkBalance) {
        if ((getActivity()).getApplication() instanceof IHomeRouter) {
            ((IHomeRouter) (getActivity()).getApplication())
                    .goToTokoCash(appLinkBalance,
                            getActivity());
        }
    }

    @Override
    public void onRequestPendingCashBack() {
        presenter.getTokocashPendingBalance();
    }

    @Override
    public void actionInfoPendingCashBackTokocash(CashBackData cashBackData,
                                                  String appLinkActionButton) {
        BottomSheetView bottomSheetDialogTokoCash = new BottomSheetView(getActivity());
        bottomSheetDialogTokoCash.setListener(new BottomSheetView.ActionListener() {
            @Override
            public void clickOnTextLink(String url) {

            }

            @Override
            public void clickOnButton(String url, String appLink) {
                if (TextUtils.isEmpty(appLink)) {
                    if (getActivity() != null) {
                        if ((getActivity()).getApplication() instanceof IHomeRouter) {
                            ((IHomeRouter) (getActivity()).getApplication())
                                    .goToWallet(getActivity(), url);
                        }
                    }
                } else {
                    if ((getActivity()).getApplication() instanceof IHomeRouter) {
                        ((IHomeRouter) (getActivity()).getApplication())
                                .goToTokoCash(appLink,
                                        getActivity());
                    }
                }

            }
        });
        bottomSheetDialogTokoCash.renderBottomSheet(new BottomSheetView
                .BottomSheetField.BottomSheetFieldBuilder()
                .setTitle(getString(R.string.toko_cash_pending_title))
                .setBody(String.format(getString(R.string.toko_cash_pending_body),
                        cashBackData.getAmountText()))
                .setImg(R.drawable.ic_box)
                .setUrlButton("",
                        appLinkActionButton,
                        getString(R.string.toko_cash_pending_proceed_button))
                .build());
        bottomSheetDialogTokoCash.show();
    }

    @Override
    public void actionTokoPointClicked(String tokoPointUrl, String pageTitle) {
        if (mShowTokopointNative) {
            openApplink(ApplinkConstant.HOMEPAGE);
        } else {
            if (TextUtils.isEmpty(pageTitle))
                startActivity(TokoPointWebviewActivity.getIntent(getActivity(), tokoPointUrl));
            else
                startActivity(TokoPointWebviewActivity.getIntentWithTitle(getActivity(), tokoPointUrl, pageTitle));
        }

        AnalyticsTrackerUtil.sendEvent(getContext(),
                AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                AnalyticsTrackerUtil.CategoryKeys.HOMEPAGE,
                AnalyticsTrackerUtil.ActionKeys.CLICK_POINT,
                AnalyticsTrackerUtil.EventKeys.TOKOPOINTS_LABEL);
    }

    @Override
    public void onPromoClick(int position, BannerSlidesModel slidesModel, String attribution) {
        if (getActivity() != null
                && getActivity().getApplicationContext() instanceof IHomeRouter
                && ((IHomeRouter) getActivity().getApplicationContext()).isSupportApplink(slidesModel.getApplink())) {
            openApplink(slidesModel.getApplink(), attribution);
        } else {
            openWebViewURL(slidesModel.getRedirectUrl(), getContext());
        }
        presenter.onBannerClicked(slidesModel);
    }

    @Override
    public void onPromoAllClick() {
        HomePageTracking.eventClickViewAllPromo(getActivity());
        HomeTrackingUtils.homeViewAllPromotions(getActivity(), "PromoListActivity");

        boolean remoteConfigEnable;
        FirebaseRemoteConfigImpl remoteConfig = new FirebaseRemoteConfigImpl(getActivity());
        remoteConfigEnable = remoteConfig.getBoolean(
                ConstantKey.RemoteConfigKey.MAINAPP_NATIVE_PROMO_LIST
        );
        if (getActivity() != null && remoteConfigEnable) {
            getActivity().startActivity(PromoListActivity.newInstance(
                    getActivity(),
                    PromoListActivity.DEFAULT_AUTO_SELECTED_MENU_ID,
                    PromoListActivity.DEFAULT_AUTO_SELECTED_CATEGORY_ID
            ));
        } else {
            if (getActivity() != null
                    && getActivity().getApplicationContext() instanceof IHomeRouter) {
                Intent intent = ((IHomeRouter) (getActivity()).getApplication())
                        .getBannerWebViewOnAllPromoClickFromHomeIntent(
                                getActivity(),
                                BerandaUrl.PROMO_URL + BerandaUrl.FLAG_APP,
                                getString(R.string.title_activity_promo));
                getActivity().startActivity(intent);
            }
        }
    }

    @Override
    public void onCloseTicker(int pos) {
        adapter.getItems().remove(pos);
        adapter.notifyItemRemoved(pos);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_DIGITAL_PRODUCT_DETAIL:
                if (data != null && data.hasExtra(EXTRA_MESSAGE)) {
                    String message = data.getStringExtra(EXTRA_MESSAGE);
                    if (!TextUtils.isEmpty(message)) {
                        NetworkErrorHelper.showSnackbar(getActivity(), message);
                    }
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        resetFeedState();
        removeNetworkError();
        if (presenter != null) {
            presenter.getHomeData();
            presenter.getHeaderData(false);
        }
        loadEggData();
        fetchTokopointsNotification(TOKOPOINTS_NOTIFICATION_TYPE);
    }

    private void resetFeedState() {
        isFeedLoaded = false;
        homeFeedsTabLayout.setVisibility(View.GONE);
        homeFeedsViewPager.setVisibility(View.GONE);
        homeFeedsViewPager.setAdapter(null);
        homeFeedsTabLayout.setup(homeFeedsViewPager, new ArrayList<>());
        disableExpandFeedSection();
    }

    @Override
    public void onCountDownFinished() {
        if (presenter != null) {
            presenter.updateHomeData();
        }
    }

    @Override
    public boolean isLoading() {
        return refreshLayout.isRefreshing();
    }

    @Override
    public void showLoading() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        refreshLayout.setRefreshing(false);
        if (performanceMonitoring != null && !isTraceStopped) {
            performanceMonitoring.stopTrace();
            isTraceStopped = true;
        }
    }

    @Override
    public void setItems(List<Visitable> items) {
        this.serverTimeOffset = 0;

        if (items.get(0) instanceof HeaderViewModel) {
            HeaderViewModel dataHeader = (HeaderViewModel) items.get(0);
            updateHeaderItem(dataHeader);
        }
        adapter.setItems(items);
    }

    @Override
    public void updateListOnResume(List<Visitable> visitables) {
        this.serverTimeOffset = 0;

        adapter.updateItems(visitables);
    }

    @Override
    public void updateHeaderItem(HeaderViewModel headerViewModel) {
        if (adapter.getItemCount() > 0 && adapter.getItem(0) instanceof HeaderViewModel) {
            adapter.getItems().set(0, headerViewModel);
            adapter.notifyItemChanged(0);
        }
    }

    @Override
    public void showNetworkError(String message) {
        if (isAdded() && getActivity() != null) {
            if (adapter.getItemCount() > 0) {
                if (messageSnackbar == null) {
                    messageSnackbar = NetworkErrorHelper.createSnackbarWithAction(
                            getActivity(), getString(R.string.msg_network_error),
                            () -> onRefresh()
                    );
                }
                messageSnackbar.showRetrySnackbar();
            } else {
                NetworkErrorHelper.showEmptyState(getActivity(), root, message,
                        new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                onRefresh();
                            }
                        });
            }
        }
    }

    @Override
    public void showNetworkError() {
        if (isAdded() && getActivity() != null) {
            if (adapter.getItemCount() > 0) {
                if (messageSnackbar == null) {
                    messageSnackbar = NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            onRefresh();
                        }
                    });
                }
                messageSnackbar.showRetrySnackbar();
            } else {
                NetworkErrorHelper.showEmptyState(getActivity(), root, getString(R.string.msg_network_error),
                        new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                onRefresh();
                            }
                        });
            }
        }
    }

    @Override
    public void onDynamicChannelClicked(String actionLink, String trackingAttribution) {
        onActionLinkClicked(actionLink, trackingAttribution);
    }

    private void onActionLinkClicked(String actionLink) {
        onActionLinkClicked(actionLink, "");
    }

    private void onActionLinkClicked(String actionLink, String trackingAttribution) {
        if (TextUtils.isEmpty(actionLink)) {
            return;
        }

        if (getActivity() != null
                && getActivity().getApplicationContext() instanceof IHomeRouter
                && ((IHomeRouter) getActivity().getApplicationContext()).isSupportApplink(actionLink)) {
            openApplink(actionLink, trackingAttribution);
        } else {
            openWebViewURL(actionLink, getContext());
        }
    }

    private void openApplink(String applink) {
        if (!TextUtils.isEmpty(applink)) {
            ((IHomeRouter) getActivity().getApplicationContext())
                    .goToApplinkActivity(getActivity(), applink);
        }
    }

    private void openApplink(String applink, String trackingAttribution) {
        if (!TextUtils.isEmpty(applink)) {
            applink = appendTrackerAttributionIfNeeded(applink, trackingAttribution);
            ((IHomeRouter) getActivity().getApplicationContext())
                    .goToApplinkActivity(getActivity(), applink);
        }
    }

    private String appendTrackerAttributionIfNeeded(String applink, String trackingAttribution) {
        if (TextUtils.isEmpty(trackingAttribution)) {
            return applink;
        }

        try {
            trackingAttribution = URLEncoder.encode(trackingAttribution, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            trackingAttribution = trackingAttribution.replaceAll(" ", "%20");
        }

        if (applink.contains("?") || applink.contains("%3F") || applink.contains("%3f")) {
            return applink + "&tracker_attribution=" + trackingAttribution;
        } else {
            return applink + "?tracker_attribution=" + trackingAttribution;
        }
    }

    private static boolean isDiscoveryPage(String applink) {
        return !TextUtils.isEmpty(applink) &&
                applink.contains("tokopedia://discovery");
    }

    @Override
    public void removeNetworkError() {
        NetworkErrorHelper.removeEmptyState(root);
        if (messageSnackbar != null && messageSnackbar.isShown()) {
            messageSnackbar.hideRetrySnackbar();
            messageSnackbar = null;
        }
    }

    public void openWebViewURL(String url, Context context) {
        if (!TextUtils.isEmpty(url) && context != null) {
            ((IHomeRouter) getActivity().getApplication())
                    .actionOpenGeneralWebView(
                            getActivity(),
                            url);
        }
    }

    public void openWebViewURL(String url) {
        openWebViewURL(url, getActivity());
    }

    @Override
    public void onRefreshTokoPointButtonClicked() {
        presenter.onRefreshTokoPoint();
    }

    @Override
    public void onRefreshTokoCashButtonClicked() {
        presenter.onRefreshTokoCash();
    }

    @Override
    public void onSixGridItemClicked(String actionLink, String trackingAttribution) {
        onActionLinkClicked(actionLink, trackingAttribution);
    }

    @Override
    public void onThreeGridItemClicked(String actionLink, String trackingAttribution) {
        onActionLinkClicked(actionLink, trackingAttribution);
    }

    @Override
    public void onPromoScrolled(BannerSlidesModel bannerSlidesModel) {
        if (getUserVisibleHint()) {
            presenter.hitBannerImpression(bannerSlidesModel);
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        trackScreen(isVisibleToUser);
        restartBanner(isVisibleToUser);
    }

    @Override
    public void onStart() {
        super.onStart();
        HomePageTracking.sendScreen(getActivity(), getScreenName());
        sendScreen();
    }

    private void restartBanner(boolean isVisibleToUser) {
        if ((isVisibleToUser && getView() != null) && adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void trackScreen(boolean isVisibleToUser) {
        if (isVisibleToUser && isAdded() && getActivity() != null) {
            HomePageTracking.sendScreen(getActivity(), getScreenName());
            sendScreen();
        }
    }

    private void sendScreen() {
        if (getActivity() != null && getActivity().getApplication() instanceof IHomeRouter) {
            ((IHomeRouter) getActivity().getApplication()).sendIndexScreen(getActivity(), getScreenName());
        }
    }

    @Override
    public boolean isMainViewVisible() {
        return getUserVisibleHint();
    }

    @Override
    public boolean isHomeFragment() {
        if (getActivity() == null) {
            return false;
        }
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
        return (fragment instanceof HomeFragment);
    }

    @Override
    public void onPromoDragStart() {
        refreshLayout.setCanChildScrollUp(true);
    }

    @Override
    public void onPromoDragEnd() {
        if (isAppBarFullyExpanded(lastOffset)) {
            refreshLayout.setCanChildScrollUp(false);
        }
    }

    @Override
    public void setActivityStateListener(ActivityStateListener activityStateListener) {
        this.activityStateListener = activityStateListener;
    }

    @Override
    public void onScrollToTop() {
        if (appBarLayout != null) {
            appBarLayout.setExpanded(true);
            homeFeedsTabLayout.resetCollapseState();
        }
    }

    /**
     * Tokocash & Tokopoint
     */
    @Override
    public Observable<HomeHeaderWalletAction> getTokocashBalance() {
        if (getActivity() != null && getActivity().getApplication() instanceof IHomeRouter) {
            return ((IHomeRouter) getActivity().getApplication()).getWalletBalanceHomeHeader();
        }
        return null;
    }

    @Override
    public Observable<PendingCashback> getTokocashPendingCashback() {
        if (getActivity() != null && getActivity().getApplication() instanceof TokoCashRouter) {
            return ((TokoCashRouter) getActivity().getApplication()).getPendingCashbackUseCase();
        }
        return null;
    }

    @Override
    public Observable<TokopointHomeDrawerData> getTokopoint() {
        if (getActivity() != null && getActivity().getApplication() instanceof IHomeRouter) {
            return ((IHomeRouter) getActivity().getApplication()).getTokopointUseCaseForHome();
        }
        return null;
    }

    protected void registerBroadcastReceiverTokoCash() {
        if (getActivity() == null)
            return;

        if (getActivity().getApplication() instanceof IHomeRouter) {
            IHomeRouter homeRouter = (IHomeRouter) getActivity().getApplication();
            getActivity().registerReceiver(
                    tokoCashBroadcaseReceiver,
                    new IntentFilter(homeRouter.getExtraBroadcastReceiverWallet())
            );
        }
    }

    protected void unRegisterBroadcastReceiverTokoCash() {
        if (getActivity() == null)
            return;

        getActivity().unregisterReceiver(tokoCashBroadcaseReceiver);
    }

    private BroadcastReceiver tokoCashBroadcaseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if (extras != null && getActivity().getApplication() instanceof IHomeRouter) {
                IHomeRouter homeRouter = (IHomeRouter) getActivity().getApplication();
                String data = extras.getString(homeRouter.getExtraBroadcastReceiverWallet());
                if (data != null && !data.isEmpty())
                    presenter.getHeaderData(false); // update header data
            }
        }
    };

    public void startShopInfo(String shopId) {
        if (getActivity() != null
                && getActivity().getApplication() != null
                && getActivity().getApplication() instanceof IHomeRouter) {
            IHomeRouter homeRouter = (IHomeRouter) getActivity().getApplication();
            startActivity(homeRouter.getShopPageIntent(getActivity(), shopId));
        }
    }

    @Override
    public void startDeeplinkShopInfo(String url) {
        Context context = getContext();
        if (context != null) {
            Uri uri = Uri.parse(url);
            List<String> pathSegmentList = uri.getPathSegments();
            if (pathSegmentList.size() > 1) {
                String shopDomain = pathSegmentList.get(pathSegmentList.size() - 2);
                String productKey = pathSegmentList.get(pathSegmentList.size() - 1);
                Intent intent = RouteManager.getIntent(context,ApplinkConstInternalMarketplace.PRODUCT_DETAIL_DOMAIN,
                                shopDomain, productKey);
                if (intent != null) {
                    startActivity(intent);
                } else {
                    RouteManager.route(context, url);
                }
            } else {
                RouteManager.route(context, url);
            }
        }
    }

    @Override
    public void showPopupIntroOvo(String applinkActivation) {
        if (RouteManager.isSupportApplink(getActivity(), applinkActivation)) {
            Intent intentBalanceWalet = RouteManager.getIntent(getActivity(), applinkActivation);
            getContext().startActivity(intentBalanceWalet);
            Activity activity = (Activity) getContext();
            activity.overridePendingTransition(R.anim.anim_slide_up_in, R.anim.anim_page_stay);
        }
    }

    @Override
    public void onRetryLoadFeeds() {
        adapter.removeRetry();
        adapter.showLoading();
        presenter.getFeedTabData();
    }

    @Override
    public void onTabFeedLoadError(Throwable e) {
        adapter.hideLoading();
        adapter.showRetry();
    }

    @Override
    public void onTabFeedLoadSuccess(List<FeedTabModel> feedTabModelList) {
        adapter.hideLoading();
        initHomeFeedsViewPager(feedTabModelList);
    }

    @Override
    public void onHomeDataLoadSuccess() {
        if (!isFeedLoaded) {
            presenter.getFeedTabData();
            adapter.showLoading();
            isFeedLoaded = true;
        }
    }

    private ArrayList<ShowCaseObject> buildShowCase() {
        if (mainToolbar == null)
            return null;
        ArrayList<ShowCaseObject> list = new ArrayList<>();
        list.add(new ShowCaseObject(mainToolbar.getBtnNotification(),
                getString(R.string.sc_notif_title),
                getString(R.string.sc_notif_desc)));
        list.add(new ShowCaseObject(mainToolbar.getBtnWishlist(),
                getString(R.string.sc_wishlist_title),
                getString(R.string.sc_wishlist_desc)));
        return list;
    }

    private boolean isUserLoggedIn() {
        return userSession.isLoggedIn();
    }

    private String getUserShopId() {
        return userSession.getShopId();
    }

    @Override
    public void onServerTimeReceived(long serverTimeUnix) {
        if (serverTimeOffset == 0) {
            long serverTimemillis = serverTimeUnix * ONE_SECOND;
            this.serverTimeOffset = ServerTimeOffsetUtil.getServerTimeOffset(serverTimemillis);
        }
    }

    @Override
    public long getServerTimeOffset() {
        return this.serverTimeOffset;
    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        setUserVisibleHint(!hidden);
    }

    private void fetchTokopointsNotification(String type) {
        TokoPointsNotificationManager.fetchNotification(getActivity(), type, getChildFragmentManager());
    }

    @Override
    public void hideEggOnScroll() {
        hideEggFragmentOnScrolling();
    }

    @Override
    public void onFeedContentScrolled(int dy, int totalScrollY) {
        homeFeedsTabLayout.adjustTabCollapseOnScrolled(dy, totalScrollY);
    }

    @Override
    public void onFeedContentScrollStateChanged(int newState) {
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            homeFeedsTabLayout.scrollActiveTabToLeftScreen();
        } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            homeFeedsTabLayout.snapCollapsingTab();
        }
    }

    @Override
    public void onGoToProductDetailFromInspiration(String productId, String imageSource, String name, String price) {
        goToProductDetail(productId, imageSource, name, price);
    }

    private void goToProductDetail(String productId, String imageSourceSingle, String name, String price) {
        getActivity().startActivity(getProductIntent(productId));
    }

    private Intent getProductIntent(String productId) {
        if (getContext() != null) {
            return RouteManager.getIntent(getContext(),ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
        } else {
            return null;
        }
    }

    @Override
    public void onNotificationChanged(int notificationCount, int inboxCount) {
        if (mainToolbar != null) {
            mainToolbar.setNotificationNumber(notificationCount);
            mainToolbar.setInboxNumber(inboxCount);
        }
    }
}
