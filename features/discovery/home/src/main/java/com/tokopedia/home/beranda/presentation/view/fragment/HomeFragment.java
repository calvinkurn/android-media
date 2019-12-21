package com.tokopedia.home.beranda.presentation.view.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.core.analytics.screen.IndexScreenTracking;
import com.tokopedia.core.router.wallet.IWalletRouter;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.design.countdown.CountDownView;
import com.tokopedia.design.keyboard.KeyboardHelper;
import com.tokopedia.digital.common.analytic.DigitalEventTracking;
import com.tokopedia.dynamicbanner.entity.PlayCardHome;
import com.tokopedia.gamification.floating.view.fragment.FloatingEggButtonFragment;
import com.tokopedia.home.IHomeRouter;
import com.tokopedia.home.R;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData;
import com.tokopedia.home.beranda.di.BerandaComponent;
import com.tokopedia.home.beranda.di.DaggerBerandaComponent;
import com.tokopedia.home.beranda.domain.model.HomeFlag;
import com.tokopedia.home.beranda.domain.model.SearchPlaceholder;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview;
import com.tokopedia.home.beranda.helper.Resource;
import com.tokopedia.home.beranda.helper.ViewHelper;
import com.tokopedia.home.beranda.listener.ActivityStateListener;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.listener.HomeEggListener;
import com.tokopedia.home.beranda.listener.HomeFeedsListener;
import com.tokopedia.home.beranda.listener.HomeInspirationListener;
import com.tokopedia.home.beranda.listener.HomeReviewListener;
import com.tokopedia.home.beranda.listener.HomeTabFeedListener;
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.home.beranda.presentation.view.HomeContract;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeBaseAdapter;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitableDiffUtil;
import com.tokopedia.home.beranda.presentation.view.adapter.LinearLayoutManagerWithSmoothScroller;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.CashBackData;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BannerViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ReviewViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.GeolocationPromptViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.FeedTabModel;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeAdapterFactory;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.HomeRecyclerDecoration;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeRecommendationFeedViewHolder;
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils;
import com.tokopedia.home.beranda.presentation.view.customview.NestedRecyclerView;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedViewModel;
import com.tokopedia.home.constant.BerandaUrl;
import com.tokopedia.home.constant.ConstantKey;
import com.tokopedia.home.widget.FloatingTextButton;
import com.tokopedia.home.widget.ToggleableSwipeRefreshLayout;
import com.tokopedia.iris.Iris;
import com.tokopedia.iris.IrisAnalytics;
import com.tokopedia.locationmanager.DeviceLocation;
import com.tokopedia.locationmanager.LocationDetectorHelper;
import com.tokopedia.loyalty.view.activity.PromoListActivity;
import com.tokopedia.loyalty.view.activity.TokoPointWebviewActivity;
import com.tokopedia.navigation_common.listener.AllNotificationListener;
import com.tokopedia.navigation_common.listener.FragmentListener;
import com.tokopedia.navigation_common.listener.HomePerformanceMonitoringListener;
import com.tokopedia.navigation_common.listener.RefreshNotificationListener;
import com.tokopedia.navigation_common.listener.MainParentStatusBarListener;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.searchbar.HomeMainToolbar;
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo;
import com.tokopedia.stickylogin.internal.StickyLoginConstant;
import com.tokopedia.stickylogin.view.StickyLoginView;
import com.tokopedia.tokopoints.notification.TokoPointsNotificationManager;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.trackingoptimizer.TrackingQueue;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import rx.Observable;

import static rx.schedulers.Schedulers.start;

/**
 * @author by errysuprayogi on 11/27/17.
 */
public class HomeFragment extends BaseDaggerFragment implements HomeContract.View,
        SwipeRefreshLayout.OnRefreshListener, HomeCategoryListener,
        CountDownView.CountDownListener, AllNotificationListener, FragmentListener,
        HomeEggListener, HomeTabFeedListener, HomeInspirationListener, HomeFeedsListener,
        HomeReviewListener {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final String TOKOPOINTS_NOTIFICATION_TYPE = "drawer";
    private static final int REQUEST_CODE_DIGITAL_PRODUCT_DETAIL = 220;
    private static final int DEFAULT_WALLET_APPLINK_REQUEST_CODE = 111;
    private static final int REQUEST_CODE_REVIEW = 999;
    private static final int VISITABLE_SIZE_WITH_DEFAULT_BANNER = 1;
    private static final int DEFAULT_FEED_PAGER_OFFSCREEN_LIMIT = 10;
    public static final String EXTRA_SHOP_ID = "EXTRA_SHOP_ID";
    public static final String KEY_NAVIGATION_BAR_HEIGHT = "navigation_bar_height";
    public static final String KEY_DIMEN = "dimen";
    public static final String KEY_DEF_PACKAGE = "android";
    public static final String REVIEW_CLICK_AT = "REVIEW_CLICK_AT";
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_TITLE = "core_web_view_extra_title";
    private static final long SEND_SCREEN_MIN_INTERVAL_MILLIS = 1000;
    public static Boolean HIDE_TICKER = false;
    public static Boolean HIDE_GEO = false;
    private static final String SOURCE_ACCOUNT = "account";
    private boolean shouldDisplayReview = true;
    private int reviewAdapterPosition = -1;
    private MainParentStatusBarListener mainParentStatusBarListener;

    String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private ActivityStateListener activityStateListener;

    @Inject
    HomePresenter presenter;

    @Inject
    PermissionCheckerHelper permissionCheckerHelper;

    RemoteConfig remoteConfig;

    private UserSessionInterface userSession;
    private NestedRecyclerView homeRecyclerView;
    private FrameLayout root;
    private ToggleableSwipeRefreshLayout refreshLayout;
    private HomeRecycleAdapter adapter;
    private RemoteConfig firebaseRemoteConfig;
    private SnackbarRetry messageSnackbar;
    private LinearLayoutManager layoutManager;
    private FloatingTextButton floatingTextButton;
    private StickyLoginView stickyLoginView;
    private boolean showRecomendation;
    private boolean mShowTokopointNative;
    private RecyclerView.OnScrollListener onEggScrollListener;

    private TrackingQueue trackingQueue;

    private Iris irisAnalytics;

    private HomeMainToolbar homeMainToolbar;

    public static final String SCROLL_RECOMMEND_LIST = "recommend_list";

    private boolean isFeedLoaded = false;

    private View statusBarBackground;

    private int startToTransitionOffset = 0;
    private int searchBarTransitionRange = 0;
    private Visitable feedTabVisitable;
    private boolean scrollToRecommendList;
    private long lastSendScreenTimeMillis;
    private Snackbar homeSnackbar;
    private SharedPreferences sharedPrefs;

    private int[] positionSticky = new int[2];
    private StickyLoginTickerPojo.TickerDetail tickerDetail;
    private HomePerformanceMonitoringListener homePerformanceMonitoringListener;

    private boolean isLightThemeStatusBar = true;
    public static final String KEY_IS_LIGHT_THEME_STATUS_BAR = "is_light_theme_status_bar";

    public static HomeFragment newInstance(boolean scrollToRecommendList) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putBoolean(SCROLL_RECOMMEND_LIST, scrollToRecommendList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainParentStatusBarListener = (MainParentStatusBarListener)context;
        homePerformanceMonitoringListener = castContextToHomePerformanceMonitoring(context);
        requestStatusBarDark();
    }

    private void requestStatusBarDark() {
        isLightThemeStatusBar = false;
        mainParentStatusBarListener.requestStatusBarDark();
    }

    private void requestStatusBarLight() {
        isLightThemeStatusBar = true;
        mainParentStatusBarListener.requestStatusBarLight();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSession = new UserSession(getActivity());
        trackingQueue = new TrackingQueue(getActivity());
        irisAnalytics = IrisAnalytics.Companion.getInstance(getActivity());
        remoteConfig = new FirebaseRemoteConfigImpl(getActivity());

        searchBarTransitionRange = getResources().getDimensionPixelSize(R.dimen.home_searchbar_transition_range);
        startToTransitionOffset = (getResources().getDimensionPixelSize(R.dimen.banner_background_height)) / 4;
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

    private HomePerformanceMonitoringListener castContextToHomePerformanceMonitoring(Context context) {
        if(context instanceof HomePerformanceMonitoringListener) {
            return (HomePerformanceMonitoringListener) context;
        }

        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        homeMainToolbar = view.findViewById(R.id.toolbar);
        statusBarBackground = view.findViewById(R.id.status_bar_bg);
        statusBarBackground.setBackground(new ColorDrawable(
                ContextCompat.getColor(getActivity(), R.color.green_600)
        ));

        //initial condition for status and searchbar
        setStatusBarAlpha(0f);

        calculateSearchbarView(0);

        homeRecyclerView = view.findViewById(R.id.list);
        if (homeRecyclerView.getItemDecorationCount() == 0) {
            homeRecyclerView.addItemDecoration(new HomeRecyclerDecoration(getResources().getDimensionPixelSize(R.dimen.home_recyclerview_item_spacing)));
        }

        homeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //set refresh layout to only enabled when reach 0 offset

                //because later we will disable scroll up for this parent recyclerview
                //and makes refresh layout think we can't scroll up (which actually can! we only disable
                //scroll so that feed recommendation section can scroll its content)
                if (recyclerView.computeVerticalScrollOffset() == 0) {
                    refreshLayout.setCanChildScrollUp(false);
                } else {
                    refreshLayout.setCanChildScrollUp(true);
                }

                if (recyclerView.canScrollVertically(1)) {
                    homeMainToolbar.showShadow();
                    showFeedSectionViewHolderShadow(false);
                    homeRecyclerView.setNestedCanScroll(false);
                } else {
                    //home feed now can scroll up, so hide maintoolbar shadow
                    homeMainToolbar.hideShadow();
                    showFeedSectionViewHolderShadow(true);
                    homeRecyclerView.setNestedCanScroll(true);
                }

                //calculate transparency of homeMainToolbar based on rv offset
                calculateSearchbarView(recyclerView.computeVerticalScrollOffset());

                int position = layoutManager.findLastVisibleItemPosition();
                if (position == adapter.getRecommendationFeedSectionPosition()) {
                    floatingTextButton.hide();
                } else {
                    floatingTextButton.show();
                }
            }
        });
        refreshLayout = view.findViewById(R.id.home_swipe_refresh_layout);
        floatingTextButton = view.findViewById(R.id.recom_action_button);
        stickyLoginView = view.findViewById(R.id.sticky_login_text);

        root = view.findViewById(R.id.root);

        if (getArguments() != null) {
            scrollToRecommendList = getArguments().getBoolean(SCROLL_RECOMMEND_LIST);
        }

        //status bar background compability, we show view background for android >= Kitkat
        //because in that version, status bar can't forced to dark mode, we must set background
        //to keep status bar icon visible
        statusBarBackground.getLayoutParams().height = ViewHelper.getStatusBarHeight(getActivity());
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusBarBackground.setVisibility(View.INVISIBLE);
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            statusBarBackground.setVisibility(View.VISIBLE);
        } else {
            statusBarBackground.setVisibility(View.GONE);
        }

        initEggDragListener();

        presenter.attachView(this);
        fetchTokopointsNotification(TOKOPOINTS_NOTIFICATION_TYPE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.onFirstLaunch();
        initAdapter();
        initRefreshLayout();
        subscribeHome();
        initEggTokenScrollListener();
        registerBroadcastReceiverTokoCash();
        fetchRemoteConfig();
        floatingTextButton.setOnClickListener(view -> {
            scrollToRecommendList();
            HomePageTracking.eventClickJumpRecomendation(getActivity());
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

        stickyLoginView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> updateStickyState());
        stickyLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stickyLoginView.getTracker().clickOnLogin(StickyLoginConstant.Page.HOME);
                onGoToLogin();
            }
        });
        stickyLoginView.setOnDismissListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stickyLoginView.dismiss(StickyLoginConstant.Page.HOME);
                stickyLoginView.getTracker().clickOnDismiss(StickyLoginConstant.Page.HOME);

                FloatingEggButtonFragment floatingEggButtonFragment = getFloatingEggButtonFragment();
                if (floatingEggButtonFragment != null) {
                    updateEggBottomMargin(floatingEggButtonFragment);
                }
            }
        });
    }

    private void scrollToRecommendList() {
        homeRecyclerView.smoothScrollToPosition(adapter.getRecommendationFeedSectionPosition());
        scrollToRecommendList = false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_LIGHT_THEME_STATUS_BAR, isLightThemeStatusBar);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            isLightThemeStatusBar = savedInstanceState.getBoolean(KEY_IS_LIGHT_THEME_STATUS_BAR);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sendScreen();
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
        homeRecyclerView.setAdapter(null);
        adapter = null;
        homeRecyclerView.setLayoutManager(null);
        layoutManager = null;
        presenter = null;
        unRegisterBroadcastReceiverTokoCash();
    }

    private void initRefreshLayout() {
        refreshLayout.post(() -> {
            if (presenter != null) {
                presenter.searchHint();
                presenter.getHomeData();
            }
            /**
             * set notification gimmick
             */
            homeMainToolbar.setNotificationNumber(0);
        });
        refreshLayout.setOnRefreshListener(this);
    }

    private void subscribeHome(){
        presenter.getHomeLiveData().observe(this, data -> {
            if(data != null){
                if (data.getList().size() > VISITABLE_SIZE_WITH_DEFAULT_BANNER) {
                    configureHomeFlag(data.getHomeFlag());
                    setData(new ArrayList(data.getList()), data.isCache() ? HomePresenter.FLAG_FROM_CACHE : HomePresenter.FLAG_FROM_NETWORK);
                    presenter.setCache(true);
                } else {
                    showNetworkError(com.tokopedia.network.ErrorHandler.getErrorMessage(new Throwable()));
                }
            }
        });

        presenter.getUpdateNetworkLiveData().observe(this, resource -> {
            if(resource.getStatus() == Resource.Status.SUCCESS){
                hideLoading();
                if(presenter.getHomeLiveData().getValue() != null && !presenter.getHomeLiveData().getValue().getList().isEmpty()) {
                    addImpressionToTrackingQueue(new ArrayList(presenter.getHomeLiveData().getValue().getList()));
                }
            } else if(resource.getStatus() == Resource.Status.ERROR){
                hideLoading();
                showNetworkError(com.tokopedia.network.ErrorHandler.getErrorMessage(resource.getError()));
            } else {
                showLoading();
            }
        });
    }

    private void setData(List<HomeVisitable> data, int flag){
        if(!data.isEmpty()) {
            if (adapter.getItemCount() != 0) {
                updateListOnResume(data);
            } else {
                setItems(data, flag);
            }
            if (isDataValid(data)) {
                removeNetworkError();
            } else {
                showNetworkError();
            }
        }
    }

    private boolean isDataValid(List<HomeVisitable> visitables) {
        return containsInstance(visitables, BannerViewModel.class);
    }

    private <T> boolean containsInstance(List<T> list, Class type){
        for (T a : list) {
            if (a.getClass() == type) {
                return true;
            }
        }
        return false;
    }

    private void loadEggData() {
        FloatingEggButtonFragment floatingEggButtonFragment = getFloatingEggButtonFragment();
        if (floatingEggButtonFragment != null) {
            updateEggBottomMargin(floatingEggButtonFragment);
            floatingEggButtonFragment.loadEggData();
        }
    }

    private void calculateSearchbarView(int offset) {
        int positiveOffset = offset;

        int endTransitionOffset =
                startToTransitionOffset + searchBarTransitionRange;
        int maxTransitionOffset = endTransitionOffset - startToTransitionOffset;

        //mapping alpha to be rendered per pixel for x height
        float offsetAlpha =
                (255f / maxTransitionOffset) * (positiveOffset - startToTransitionOffset);
        //2.5 is maximum
        if (offsetAlpha < 0) {
            offsetAlpha = 0;
        }

        if (offsetAlpha >= 255) {
            offsetAlpha = 255;
            homeMainToolbar.switchToDarkToolbar();
            requestStatusBarDark();
        } else {
            homeMainToolbar.switchToLightToolbar();
            requestStatusBarLight();
        }

        if (offsetAlpha >= 0 && offsetAlpha <= 255) {
            homeMainToolbar.setBackgroundAlpha(offsetAlpha);
            setStatusBarAlpha(offsetAlpha);
        }
    }

    private void setStatusBarAlpha(float alpha) {
        Drawable drawable = statusBarBackground.getBackground();
        drawable.setAlpha((int) alpha);
        statusBarBackground.setBackground(drawable);
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
                    updateEggBottomMargin(floatingEggButtonFragment);
                    floatingEggButtonFragment.hideOnScrolling();
                }
            }
        };

        homeRecyclerView.removeOnScrollListener(onEggScrollListener);
        homeRecyclerView.addOnScrollListener(onEggScrollListener);
    }

    private FloatingEggButtonFragment getFloatingEggButtonFragment() {
        // https://stackoverflow.com/questions/28672883/java-lang-illegalstateexception-fragment-not-attached-to-activity
        if (getActivity() != null && isAdded() && getChildFragmentManager() != null) {
            return (FloatingEggButtonFragment) getChildFragmentManager().findFragmentById(R.id.floating_egg_fragment);
        }
        return null;
    }

    private void initAdapter() {
        layoutManager = new LinearLayoutManagerWithSmoothScroller(getContext());
        homeRecyclerView.setLayoutManager(layoutManager);
        homeRecyclerView.getItemAnimator().setChangeDuration(0);
        HomeAdapterFactory adapterFactory = new HomeAdapterFactory(
                getChildFragmentManager(),
                this,
                this,
                this,
                this,
                this
        );
        adapter = new HomeRecycleAdapter(new HomeVisitableDiffUtil(), adapterFactory, new ArrayList<Visitable>());
        homeRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onSectionItemClicked(String actionLink) {
        onActionLinkClicked(actionLink);
    }

    @Override
    public void onSpotlightItemClicked(String actionLink) {
        onActionLinkClicked(actionLink);
    }

    @Override
    public void configureHomeFlag(HomeFlag homeFlag) {
        floatingTextButton.setVisibility(homeFlag.getFlag(HomeFlag.TYPE.HAS_RECOM_NAV_BUTTON) && showRecomendation ? View.VISIBLE : View.GONE);
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
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.LOGIN);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, SOURCE_ACCOUNT);
        Intent intentHome = RouteManager.getIntent(getActivity(), ApplinkConst.HOME);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getActivity().startActivities(new Intent[]{intentHome, intent});
        getActivity().finish();
    }

    private void onGoToCreateShop() {
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.CREATE_SHOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    private void onGoToShop(String shopId) {
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.SHOP);
        intent.putExtra(EXTRA_SHOP_ID, shopId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }


    @Override
    public void onDigitalMoreClicked(int pos) {
        Analytics tracker = HomePageTracking.getTracker();
        if (tracker != null) {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                    DigitalEventTracking.Category.DIGITAL_HOMEPAGE,
                    DigitalEventTracking.Action.CLICK_SEE_ALL_PRODUCTS,
                    ""
            ));
        }
    }

    @Override
    public void openShop() {
        onGoToSell();
    }

    @Override
    public void actionAppLinkWalletHeader(String appLinkBalance) {
        goToOvo(appLinkBalance);
    }

    private void goToOvo(String appLinkScheme) {
        Intent intent = appLinkScheme == null || appLinkScheme.isEmpty() ?
                RouteManager.getIntent(getActivity(), ApplinkConst.WEBVIEW).putExtra("EXTRA_URL", appLinkScheme)
                : RouteManager.isSupportApplink(getActivity(), appLinkScheme)
                ? RouteManager.getIntent(getActivity(), appLinkScheme).setData(Uri.parse(appLinkScheme))
                : RouteManager.getIntent(getActivity(), ApplinkConst.WEBVIEW).putExtra("EXTRA_URL", appLinkScheme);
        startActivityForResult(intent, DEFAULT_WALLET_APPLINK_REQUEST_CODE);
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
                goToOvo(appLink);
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
    public void actionTokoPointClicked(String appLink, String tokoPointUrl, String pageTitle) {
        if (mShowTokopointNative) {
            openApplink(appLink);
        } else {
            if (TextUtils.isEmpty(pageTitle))
                startActivity(TokoPointWebviewActivity.getIntent(getActivity(), tokoPointUrl));
            else
                startActivity(TokoPointWebviewActivity.getIntentWithTitle(getActivity(), tokoPointUrl, pageTitle));
        }

        HomePageTracking.sendTokopointTrackerClick();
    }

    @Override
    public void onPromoClick(int position, BannerSlidesModel slidesModel) {
        if (getActivity() != null && RouteManager.isSupportApplink(getActivity(), slidesModel.getApplink())) {
            openApplink(slidesModel.getApplink());
        } else {
            openWebViewURL(slidesModel.getRedirectUrl(), getActivity());
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
            if (getActivity() != null) {
                showBannerWebViewOnAllPromoClickFromHomeIntent(BerandaUrl.PROMO_URL + BerandaUrl.FLAG_APP, getString(R.string.title_activity_promo));
            }
        }
    }

    private void showBannerWebViewOnAllPromoClickFromHomeIntent(String url, String title) {
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.PROMO);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        startActivity(intent);
    }

    @Override
    public void onCloseTicker(int pos) {
        HIDE_TICKER = true;
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
            case REQUEST_CODE_REVIEW:
                adapter.notifyDataSetChanged();
                if (resultCode == Activity.RESULT_OK) {
                    adapter.removeReviewViewModel();
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        resetFeedState();
        removeNetworkError();
        if (presenter != null) {
            presenter.searchHint();
            presenter.getHomeData();
            presenter.getStickyContent();
        }

        if (getActivity() instanceof RefreshNotificationListener) {
            ((RefreshNotificationListener) getActivity()).onRefreshNotification();
        }
        loadEggData();
        fetchTokopointsNotification(TOKOPOINTS_NOTIFICATION_TYPE);
    }

    private void resetFeedState() {
        isFeedLoaded = false;
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
    }

    @Override
    public void setItems(List<HomeVisitable> items, int repositoryFlag) {
        if (needToPerformanceMonitoring()) setOnRecyclerViewLayoutReady();
        List<HomeVisitable> list = new ArrayList<>(items);
        if (repositoryFlag == HomePresenter.FLAG_FROM_NETWORK) {

            adapter.submitList( needToShowGeolocationComponent() ? removeReviewComponent(list) : removeGeolocationComponent(items));
            presenter.getHeaderData(false);
            presenter.getFeedTabData();
//            adapter.showLoading();

            if (adapter.hasReview() != -1 && shouldDisplayReview && !needToShowGeolocationComponent()) {
                shouldDisplayReview = false;
                presenter.getSuggestedReview();
            }

        } else {
            adapter.submitList(needToShowGeolocationComponent() ? list : removeGeolocationComponent(items));
        }
    }

    private void setOnRecyclerViewLayoutReady() {
        homeRecyclerView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //At this point the layout is complete and the
                        //dimensions of recyclerView and any child views are known.
                        //Remove listener after changed RecyclerView's height to prevent infinite loop
                        homePerformanceMonitoringListener.stopHomePerformanceMonitoring();
                        homePerformanceMonitoringListener = null;
                        homeRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    private boolean needToShowGeolocationComponent() {
        boolean firebaseShowGeolocationComponent = remoteConfig.getBoolean(RemoteConfigKey.SHOW_HOME_GEOLOCATION_COMPONENT, true);
        if (!firebaseShowGeolocationComponent) {
            return false;
        }

        boolean needToShowGeolocationComponent = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean userHasDeniedPermissionBefore = ActivityCompat
                    .shouldShowRequestPermissionRationale(getActivity(),
                            PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION);
            if (userHasDeniedPermissionBefore) {
                return false;
            }
        }

        if (getActivity() != null) {
            if (hasGeolocationPermission()) {
                needToShowGeolocationComponent = false;
            }
        }
        if(needToShowGeolocationComponent && HIDE_GEO) return false;
        return needToShowGeolocationComponent;
    }

    private List<HomeVisitable> removeReviewComponent(List<HomeVisitable> items) {
        List<HomeVisitable> local = new ArrayList<>(items);
        for (Visitable visitable : local){
            if(visitable instanceof ReviewViewModel){
                local.remove(visitable);
                break;
            }
        }
        return local;
    }

    private List<HomeVisitable> removeGeolocationComponent(List<HomeVisitable> items){
        List<HomeVisitable> local = new ArrayList<>(items);
        for (HomeVisitable visitable : local){
            if(visitable instanceof GeolocationPromptViewModel){
                local.remove(visitable);
                break;
            }
        }
        return local;
    }

    @Override
    public boolean hasGeolocationPermission() {
        if (getActivity() == null) return false;
        return permissionCheckerHelper.hasPermission(getActivity(),
                new String[]{PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION});
    }

    private void promptGeolocationPermission() {
        permissionCheckerHelper.checkPermission(this,
                PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION,
                new PermissionCheckerHelper.PermissionCheckListener() {
                    @Override
                    public void onPermissionDenied(@NotNull String permissionText) {
                        HomePageTracking.eventClickNotAllowGeolocation(getActivity());
                        adapter.removeGeolocationViewModel();
                        showNotAllowedGeolocationSnackbar();
                    }

                    @Override
                    public void onNeverAskAgain(@NotNull String permissionText) {

                    }

                    @Override
                    public void onPermissionGranted() {
                        HomePageTracking.eventClickAllowGeolocation(getActivity());
                        detectAndSendLocation();
                        adapter.removeGeolocationViewModel();
                        showAllowedGeolocationSnackbar();
                    }
                }, "");
    }

    @Override
    public void detectAndSendLocation() {
        LocationDetectorHelper locationDetectorHelper = new LocationDetectorHelper(
                permissionCheckerHelper,
                LocationServices.getFusedLocationProviderClient(getActivity()
                        .getApplicationContext()),
                getActivity().getApplicationContext());
        locationDetectorHelper.getLocation(onGetLocation(), getActivity(),
                LocationDetectorHelper.TYPE_DEFAULT_FROM_CLOUD,
                "");
    }

    private Function1<DeviceLocation, Unit> onGetLocation() {
        return (deviceLocation) -> {
            saveLocation(getActivity(), deviceLocation.getLatitude(), deviceLocation.getLongitude());
            presenter.sendGeolocationData();
            return null;
        };
    }

    public void saveLocation(Context context, double latitude, double longitude) {
        SharedPreferences.Editor editor;
        if (context != null && !TextUtils.isEmpty(ConstantKey.LocationCache.KEY_LOCATION)) {
            sharedPrefs = context.getSharedPreferences(ConstantKey.LocationCache.KEY_LOCATION, Context.MODE_PRIVATE);
            editor = sharedPrefs.edit();
        } else {
            return;
        }
        editor.putString(ConstantKey.LocationCache.KEY_LOCATION_LAT, String.valueOf(latitude));
        editor.putString(ConstantKey.LocationCache.KEY_LOCATION_LONG, String.valueOf(longitude));
        editor.apply();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionCheckerHelper.onRequestPermissionsResult(getActivity(), requestCode, permissions, grantResults);
    }

    @Override
    public void setHint(SearchPlaceholder searchPlaceholder) {
        if (searchPlaceholder.getData() != null && searchPlaceholder.getData().getPlaceholder() != null && searchPlaceholder.getData().getKeyword() != null) {
            homeMainToolbar.setHint(searchPlaceholder.getData().getPlaceholder(), searchPlaceholder.getData().getKeyword());
        }
    }

    private void updateFeedRecommendationVisitable(Visitable feedRecommendationVisitable) {
        this.feedTabVisitable = feedRecommendationVisitable;
        List<Visitable> currentVisitables = adapter.getItems();

        for (int i = 0; i < currentVisitables.size(); i++) {
            if (currentVisitables.get(i) instanceof HomeRecommendationFeedViewModel) {
                currentVisitables.set(i, feedRecommendationVisitable);
//                adapter.setElement(i, feedRecommendationVisitable);
                //set new data to false because visitable already passed to adapter
                ((HomeRecommendationFeedViewModel) feedTabVisitable).setNewData(false);
                return;
            }
        }

        //if looping not returning any home recommendation feed view model
        //then add one
//        adapter.addElement(feedRecommendationVisitable);
    }

    @Override
    public void updateListOnResume(List<HomeVisitable> visitables) {

        presenter.getHeaderData(false);

        if (!visitables.isEmpty()) {
            presenter.getFeedTabData();
        }

//        List<Visitable> itemAfterGeoloc;

        // Remove review component if Geolocation showing
//        if (needToShowGeolocationComponent()) {
//            itemAfterGeoloc = removeReviewComponent(new ArrayList<>(visitables));
//        } else {
//            itemAfterGeoloc = removeGeolocationComponent(visitables);
//        }

//        adapter.updateHomeQueryItems(itemAfterGeoloc);

        if (adapter.hasReview() != -1 && shouldDisplayReview && !needToShowGeolocationComponent()) {
            shouldDisplayReview = false;
            presenter.getSuggestedReview();
        }
    }

    @Override
    public void addImpressionToTrackingQueue(List<Visitable> visitables) {
        List<Object> combinedTracking = new ArrayList<>();
        for (Visitable visitable : visitables) {
            if(visitable instanceof HomeVisitable) {
                HomeVisitable homeVisitable = (HomeVisitable) visitable;
                if (homeVisitable.isTrackingCombined() && homeVisitable.getTrackingDataForCombination() != null) {
                    combinedTracking.addAll(homeVisitable.getTrackingDataForCombination());
                } else if (!homeVisitable.isTrackingCombined() && homeVisitable.getTrackingData() != null) {
                    HomePageTracking.eventEnhancedImpressionWidgetHomePage(trackingQueue, homeVisitable.getTrackingData());
                }
            }
        }
        if (!combinedTracking.isEmpty()) {
            HomePageTracking.eventEnhanceImpressionLegoAndCuratedHomePage(trackingQueue, combinedTracking);
        }
    }

    @Override
    public void updateHeaderItem(HeaderViewModel headerViewModel) {
        adapter.setHomeHeaderViewModel(headerViewModel);
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
                        () -> onRefresh());
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
                        () -> onRefresh());
            }
        }
    }

    @Override
    public void onDynamicChannelClicked(String actionLink) {
        onActionLinkClicked(actionLink);
    }

    private void onActionLinkClicked(String actionLink) {
        onActionLinkClicked(actionLink, "");
    }

    private void onActionLinkClicked(String actionLink, String trackingAttribution) {
        if (TextUtils.isEmpty(actionLink)) {
            return;
        }

        if (getActivity() != null
                && RouteManager.isSupportApplink(getActivity(), actionLink)) {
            openApplink(actionLink, trackingAttribution);
        } else {
            openWebViewURL(actionLink, getActivity());
        }
    }

    private void openApplink(String applink) {
        if (!TextUtils.isEmpty(applink)) {
            RouteManager.route(getActivity(), applink);
        }
    }

    private void openApplink(String applink, String trackingAttribution) {
        if (!TextUtils.isEmpty(applink)) {
            applink = appendTrackerAttributionIfNeeded(applink, trackingAttribution);
            RouteManager.route(getActivity(), applink);
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
            Intent intent = RouteManager.getIntent(context, ApplinkConst.PROMO);
            intent.putExtra(EXTRA_URL, url);
            startActivity(intent);
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
    public void onLegoBannerClicked(String actionLink, String trackingAttribution) {
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

    private void restartBanner(boolean isVisibleToUser) {
        if ((isVisibleToUser && getView() != null) && adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void trackScreen(boolean isVisibleToUser) {
        if (isVisibleToUser && isAdded() && getActivity() != null) {
            sendScreen();
        }
    }

    private void sendScreen() {
        if (getActivity() != null && System.currentTimeMillis() > lastSendScreenTimeMillis + SEND_SCREEN_MIN_INTERVAL_MILLIS) {
            lastSendScreenTimeMillis = System.currentTimeMillis();
            HomePageTracking.sendScreen(getActivity(), getScreenName(), userSession.isLoggedIn());
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
    public void setActivityStateListener(ActivityStateListener activityStateListener) {
        this.activityStateListener = activityStateListener;
    }

    @Override
    public void onScrollToTop() {
        if (homeRecyclerView != null) {
            homeRecyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public boolean isLightThemeStatusBar() {
        return isLightThemeStatusBar;
    }

    /**
     * Tokopoint
     */

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

        getActivity().registerReceiver(
                tokoCashBroadcaseReceiver,
                new IntentFilter(getActivity().getString(R.string.broadcast_wallet))
        );
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
            if (extras != null) {
                String data = extras.getString(getActivity().getString(R.string.broadcast_wallet));
                if (data != null && !data.isEmpty())
                    presenter.getHeaderData(false); // update header data
            }
        }
    };

    public void startShopInfo(String shopId) {
        if (getActivity() != null
                && getActivity().getApplication() != null) {
            RouteManager.route(getActivity(), ApplinkConst.SHOP, shopId);
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
                Intent intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL_DOMAIN, shopDomain, productKey);
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
//        adapter.showLoading();
        presenter.getFeedTabData();
    }

    @Override
    public void onTabFeedLoadError(Throwable e) {
//        adapter.hideLoading();
        adapter.showRetry();
    }

    @Override
    public void onTabFeedLoadSuccess(List<FeedTabModel> feedTabModelList) {
//        adapter.hideLoading();
        updateFeedRecommendationVisitable(mappingHomeFeedModel(feedTabModelList));
    }

    private Visitable mappingHomeFeedModel(List<FeedTabModel> feedTabModelList) {
        HomeRecommendationFeedViewModel feedViewModel = new HomeRecommendationFeedViewModel();
        feedViewModel.setFeedTabModel(feedTabModelList);
        feedViewModel.setNewData(true);
        return feedViewModel;
    }

    @Override
    public void onHomeDataLoadSuccess() {
        if (!isFeedLoaded) {
            isFeedLoaded = true;
        }
    }

    private boolean isUserLoggedIn() {
        return userSession.isLoggedIn();
    }

    private String getUserShopId() {
        return userSession.getShopId();
    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        setUserVisibleHint(!hidden);
    }

    private void fetchTokopointsNotification(String type) {
        if (getActivity() != null) {
            TokoPointsNotificationManager.fetchNotification(getActivity(), type, getChildFragmentManager());
        }
    }

    @Override
    public void hideEggOnScroll() {
        hideEggFragmentOnScrolling();
    }

    @Override
    public void onFeedContentScrolled(int dy, int totalScrollY) {

    }

    @Override
    public void onFeedContentScrollStateChanged(int newState) {

    }

    @Override
    public void onGoToProductDetailFromInspiration(String productId, String imageSource, String name, String price) {
        goToProductDetail(productId, imageSource, name, price);
    }

    private void goToProductDetail(String productId, String imageSourceSingle, String name, String price) {
        getActivity().startActivity(getProductIntent(productId));
    }

    private Intent getProductIntent(String productId) {
        if (getActivity() != null) {
            return RouteManager.getIntent(getActivity(), ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
        } else {
            return null;
        }
    }

    @Override
    public void onNotificationChanged(int notificationCount, int inboxCount) {
        if (homeMainToolbar != null) {
            homeMainToolbar.setNotificationNumber(notificationCount);
            homeMainToolbar.setInboxNumber(inboxCount);
        }
    }


    @Override
    public HomeEggListener getEggListener() {
        return this;
    }

    @Override
    public TrackingQueue getTrackingQueue() {
        return trackingQueue;
    }

    @Override
    public int getWindowHeight() {
        if (getActivity() != null) {
            int hRoot = root.getHeight();
            return hRoot;
        } else {
            return 0;
        }
    }

    @Override
    public int getHomeMainToolbarHeight() {
        int height = 0;
        if (homeMainToolbar != null) {
            height = homeMainToolbar.getHeight();
            if (!homeMainToolbar.isShadowApplied()) {
                height += getResources().getDimensionPixelSize(R.dimen.dp_8);
            }
        }
        return height;
    }

    private void showFeedSectionViewHolderShadow(Boolean show) {
        RecyclerView.ViewHolder feedViewHolder = homeRecyclerView.findViewHolderForAdapterPosition(
                adapter.getRecommendationFeedSectionPosition()
        );
        if (feedViewHolder instanceof HomeRecommendationFeedViewHolder) {
            ((HomeRecommendationFeedViewHolder) feedViewHolder).showFeedTabShadow(show);
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
                    refreshLayout.setCanChildScrollUp(false);
                }
            });
        }
    }

    @Override
    public void launchPermissionChecker() {
        promptGeolocationPermission();
    }

    @Override
    public void onCloseGeolocationView() {
        HIDE_GEO = true;
        adapter.removeGeolocationViewModel();
    }

    private Snackbar getSnackbar(String text, int duration) {
        if (homeSnackbar != null) {
            homeSnackbar.dismiss();
        }
        homeSnackbar = Snackbar.make(root, text, duration);
        return homeSnackbar;
    }

    public void showNotAllowedGeolocationSnackbar() {
        getSnackbar(getString(R.string.discovery_home_snackbar_geolocation_declined_permission),
                Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.discovery_home_snackbar_geolocation_setting), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HomePageTracking.eventClickOnAtur(getActivity());
                        goToApplicationDetailActivity();
                    }
                }).show();
    }

    public void showAllowedGeolocationSnackbar() {
        getSnackbar(getString(R.string.discovery_home_snackbar_geolocation_granted_permission),
                Snackbar.LENGTH_LONG).show();
    }

    private void goToApplicationDetailActivity() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        getActivity().startActivity(intent);
    }

    @Override
    public void onPromoDragStart() {

    }

    @Override
    public void onPromoDragEnd() {

    }

    @Override
    public void putEEToTrackingQueue(HashMap<String, Object> data) {
        if (trackingQueue!=null) {
            trackingQueue.putEETracking(data);
        }
    }

    @Override
    public void putEEToIris(@NotNull HashMap<String, Object> data) {
        if (irisAnalytics!=null) {
            irisAnalytics.saveEvent(data);
        }
    }

    @Override
    public void onGetPlayBanner(int adapterPosition) {
        presenter.getPlayBanner(adapterPosition);
    }

    @Override
    public void setPlayContentBanner(PlayCardHome playContentBanner, int adapterPosition) {
        adapter.setPlayData(playContentBanner, adapterPosition);
    }

    @Override
    public void setStickyContent(StickyLoginTickerPojo.TickerDetail tickerDetail) {
        this.tickerDetail = tickerDetail;
        updateStickyState();
    }

    @Override
    public void hideStickyLogin() {
        stickyLoginView.setVisibility(View.GONE);
    }

    private void updateStickyState() {
        if (this.tickerDetail == null) {
            hideStickyLogin();
            return;
        }

        boolean isCanShowing = remoteConfig.getBoolean(StickyLoginConstant.REMOTE_CONFIG_FOR_HOME, true);
        if (!isCanShowing) {
            hideStickyLogin();
            return;
        }

        if (isUserLoggedIn()) {
            hideStickyLogin();
            return;
        }

        stickyLoginView.setContent(this.tickerDetail);
        stickyLoginView.show(StickyLoginConstant.Page.HOME);

        if (stickyLoginView.isShowing()) {
            positionSticky = stickyLoginView.getLocation();
            stickyLoginView.getTracker().viewOnPage(StickyLoginConstant.Page.HOME);
        }

        FloatingEggButtonFragment floatingEggButtonFragment = getFloatingEggButtonFragment();
        if (floatingEggButtonFragment != null) {
            updateEggBottomMargin(floatingEggButtonFragment);
        }
    }

    @Override
    public void onReviewClick(int position, int clickReviewAt, long delay, @NotNull String applink) {
        new Handler().postDelayed(() -> {
            Intent intent = RouteManager.getIntent(getContext(), applink);
            intent.putExtra(REVIEW_CLICK_AT, clickReviewAt);
            startActivityForResult(intent, REQUEST_CODE_REVIEW);
        }, delay);
    }

    @Override
    public void onCloseClick() {
        presenter.dismissReview();
    }

    @Override
    public void onSuccessDismissReview() {
        adapter.removeReviewViewModel();
    }

    @Override
    public void onSuccessGetReviewData(SuggestedProductReview suggestedProductReview) {
        shouldDisplayReview = true;
        adapter.updateReviewItem(suggestedProductReview);
    }

    @Override
    public void onErrorGetReviewData() {
        adapter.removeReviewViewModel();
    }

    private void updateEggBottomMargin(FloatingEggButtonFragment floatingEggButtonFragment) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) floatingEggButtonFragment.getView().getLayoutParams();
        if (stickyLoginView.isShowing()) {
            params.setMargins(0, 0, 0, stickyLoginView.getHeight());

            int[] positionEgg = new int[2];
            int eggHeight = floatingEggButtonFragment.getEgg().getHeight();
            floatingEggButtonFragment.getEgg().getLocationOnScreen(positionEgg);

            if (positionEgg[1] + eggHeight > positionSticky[1]) {
                floatingEggButtonFragment.moveEgg(positionSticky[1] - eggHeight);
            }
        } else {
            params.setMargins(0, 0, 0, 0);
        }
    }

    @Override
    public int getWindowWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return width;
    }

    @Override
    public void onTokopointCheckNowClicked(@NotNull String applink) {
        if(!TextUtils.isEmpty(applink)){
            RouteManager.route(getContext(),applink);
        }
    }

    private boolean needToPerformanceMonitoring() {
        return homePerformanceMonitoringListener != null;
    }
}
