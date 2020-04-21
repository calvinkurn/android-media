package com.tokopedia.home.beranda.presentation.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry;
import com.tokopedia.analytics.performance.fpi.FpiPerformanceData;
import com.tokopedia.analytics.performance.fpi.FragmentFramePerformanceIndexMonitoring;
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalContent;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.design.countdown.CountDownView;
import com.tokopedia.design.keyboard.KeyboardHelper;
import com.tokopedia.home.R;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.analytics.HomePageTrackingV2;
import com.tokopedia.home.analytics.v2.MixTopTracking;
import com.tokopedia.home.analytics.v2.ProductHighlightTracking;
import com.tokopedia.home.beranda.di.BerandaComponent;
import com.tokopedia.home.beranda.di.DaggerBerandaComponent;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.domain.model.HomeFlag;
import com.tokopedia.home.beranda.domain.model.SearchPlaceholder;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.helper.Result;
import com.tokopedia.home.beranda.helper.ViewHelper;
import com.tokopedia.home.beranda.listener.ActivityStateListener;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.listener.HomeEggListener;
import com.tokopedia.home.beranda.listener.HomeFeedsListener;
import com.tokopedia.home.beranda.listener.HomeInspirationListener;
import com.tokopedia.home.beranda.listener.HomeReviewListener;
import com.tokopedia.home.beranda.listener.HomeTabFeedListener;
import com.tokopedia.home.beranda.presentation.view.listener.FramePerformanceIndexInterface;
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitableDiffUtil;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.CashBackData;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelDataModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomepageBannerDataModel;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeAdapterFactory;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.HomeRecyclerDecoration;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.BannerOrganicViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicChannelViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.PopularKeywordViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.RechargeRecommendationViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeRecommendationFeedViewHolder;
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils;
import com.tokopedia.home.beranda.presentation.view.customview.NestedRecyclerView;
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel;
import com.tokopedia.home.constant.BerandaUrl;
import com.tokopedia.home.constant.ConstantKey;
import com.tokopedia.home.widget.FloatingTextButton;
import com.tokopedia.home.widget.ToggleableSwipeRefreshLayout;
import com.tokopedia.iris.Iris;
import com.tokopedia.iris.IrisAnalytics;
import com.tokopedia.iris.util.IrisSession;
import com.tokopedia.locationmanager.DeviceLocation;
import com.tokopedia.locationmanager.LocationDetectorHelper;
import com.tokopedia.loyalty.view.activity.PromoListActivity;
import com.tokopedia.navigation_common.listener.AllNotificationListener;
import com.tokopedia.navigation_common.listener.FragmentListener;
import com.tokopedia.navigation_common.listener.HomePerformanceMonitoringListener;
import com.tokopedia.navigation_common.listener.MainParentStatusBarListener;
import com.tokopedia.navigation_common.listener.RefreshNotificationListener;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.promogamification.common.floating.view.fragment.FloatingEggButtonFragment;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.searchbar.HomeMainToolbar;
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo;
import com.tokopedia.stickylogin.internal.StickyLoginConstant;
import com.tokopedia.stickylogin.view.StickyLoginView;
import com.tokopedia.tokopoints.notification.TokoPointsNotificationManager;
import com.tokopedia.track.TrackApp;
import com.tokopedia.trackingoptimizer.TrackingQueue;
import com.tokopedia.unifycomponents.Toaster;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;
import com.tokopedia.weaver.WeaverFirebaseConditionCheck;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import rx.Observable;
import rx.schedulers.Schedulers;

import static com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicChannelViewHolder.TYPE_BANNER;
import static com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicChannelViewHolder.TYPE_BANNER_CAROUSEL;
import static com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicChannelViewHolder.TYPE_FOUR_GRID_LEGO;
import static com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicChannelViewHolder.TYPE_GIF_BANNER;
import static com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicChannelViewHolder.TYPE_MIX_LEFT;
import static com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicChannelViewHolder.TYPE_MIX_TOP;
import static com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicChannelViewHolder.TYPE_ORGANIC;
import static com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicChannelViewHolder.TYPE_PRODUCT_HIGHLIGHT;
import static com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicChannelViewHolder.TYPE_RECOMMENDATION_LIST;
import static com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicChannelViewHolder.TYPE_SIX_GRID_LEGO;
import static com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicChannelViewHolder.TYPE_SPRINT_LEGO;
import static com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicChannelViewHolder.TYPE_SPRINT_SALE;
import static com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicChannelViewHolder.TYPE_THREE_GRID_LEGO;


/**
 * @author by errysuprayogi on 11/27/17.
 */
@SuppressLint("SyntheticAccessor")
public class HomeFragment extends BaseDaggerFragment implements
        SwipeRefreshLayout.OnRefreshListener, HomeCategoryListener,
        CountDownView.CountDownListener, AllNotificationListener, FragmentListener,
        HomeEggListener, HomeTabFeedListener, HomeInspirationListener, HomeFeedsListener,
        HomeReviewListener, PopularKeywordViewHolder.PopularKeywordListener,
        FramePerformanceIndexInterface, RechargeRecommendationViewHolder.RechargeRecommendationListener {

    private static final String TOKOPOINTS_NOTIFICATION_TYPE = "drawer";
    private static final int SCROLL_STATE_DRAG = 0;
    private static final int REQUEST_CODE_DIGITAL_PRODUCT_DETAIL = 220;
    private static final int DEFAULT_WALLET_APPLINK_REQUEST_CODE = 111;
    private static final int REQUEST_CODE_REVIEW = 999;
    private static final int VISITABLE_SIZE_WITH_DEFAULT_BANNER = 1;
    private static final String EXTRA_SHOP_ID = "EXTRA_SHOP_ID";
    private static final String REVIEW_CLICK_AT = "rating";
    private static final String UTM_SOURCE = "utm_source";
    private static final String EXTRA_URL = "url";
    private static final String EXTRA_TITLE = "core_web_view_extra_title";
    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private static final String EXTRA_TOTAL_VIEW = "EXTRA_TOTAL_VIEW";
    private static final long SEND_SCREEN_MIN_INTERVAL_MILLIS = 1000;
    private static final String DEFAULT_UTM_SOURCE = "home_notif";
    private static final int REQUEST_CODE_PLAY_ROOM = 256;
    private static final String PERFORMANCE_PAGE_NAME_HOME = "home";
    @NonNull
    public static Boolean HIDE_TICKER = false;
    private static Boolean HIDE_GEO = false;
    private static final String SOURCE_ACCOUNT = "account";
    private MainParentStatusBarListener mainParentStatusBarListener;
    private ActivityStateListener activityStateListener;
    private BerandaComponent component;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    private HomeViewModel viewModel;

    @Inject
    PermissionCheckerHelper permissionCheckerHelper;

    private RemoteConfig remoteConfig;

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
    private boolean isShowFirstInstallSearch;
    private RecyclerView.OnScrollListener onEggScrollListener;
    private boolean scrollToRecommendList;

    private TrackingQueue trackingQueue;

    private Iris irisAnalytics;
    private IrisSession irisSession;

    private HomeMainToolbar homeMainToolbar;

    private static final String SCROLL_RECOMMEND_LIST = "recommend_list";

    private boolean isFeedLoaded = false;

    private View statusBarBackground;

    private int startToTransitionOffset = 0;
    private int searchBarTransitionRange = 0;
    private long lastSendScreenTimeMillis;
    private Snackbar homeSnackbar;
    private SharedPreferences sharedPrefs;

    private int[] positionSticky = new int[2];
    private StickyLoginTickerPojo.TickerDetail tickerDetail;
    private HomePerformanceMonitoringListener homePerformanceMonitoringListener;

    private boolean isLightThemeStatusBar = true;
    private static final String KEY_IS_LIGHT_THEME_STATUS_BAR = "is_light_theme_status_bar";
    private Map<String,RecyclerView.OnScrollListener> impressionScrollListeners = new HashMap<>();

    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 500;

    private FragmentFramePerformanceIndexMonitoring fragmentFramePerformanceIndexMonitoring = new FragmentFramePerformanceIndexMonitoring();

    @NonNull
    public static HomeFragment newInstance(boolean scrollToRecommendList) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putBoolean(SCROLL_RECOMMEND_LIST, scrollToRecommendList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
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
        irisSession = new IrisSession(getContext());
        remoteConfig = new FirebaseRemoteConfigImpl(getActivity());
        searchBarTransitionRange = getResources().getDimensionPixelSize(R.dimen.home_searchbar_transition_range);
        startToTransitionOffset = (getResources().getDimensionPixelSize(R.dimen.banner_background_height)) / 2;

        if (getPageLoadTimeCallback() != null) {
            getPageLoadTimeCallback().stopPreparePagePerformanceMonitoring();
            getPageLoadTimeCallback().startNetworkRequestPerformanceMonitoring();
        }

        initViewModel();
        setGeolocationPermission();
        needToShowGeolocationComponent();
        getStickyContent();
    }

    @VisibleForTesting
    protected void initViewModel(){
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel.class);
    }

    @Override
    protected String getScreenName() {
        return ConstantKey.Analytics.AppScreen.UnifyTracking.SCREEN_UNIFY_HOME_BERANDA;
    }

    @Override
    protected void initInjector() {
        if (getActivity() != null) {
            if(component == null){
                component = initBuilderComponent().build();
            }
            component.inject(this);
        }
    }

    protected DaggerBerandaComponent.Builder initBuilderComponent(){
        return DaggerBerandaComponent.builder().baseAppComponent(((BaseMainApplication)
                getActivity().getApplication()).getBaseAppComponent());
    }

    private void fetchRemoteConfig() {
        firebaseRemoteConfig = new FirebaseRemoteConfigImpl(getActivity());
        showRecomendation = firebaseRemoteConfig.getBoolean(ConstantKey.RemoteConfigKey.APP_SHOW_RECOMENDATION_BUTTON, false);
        mShowTokopointNative = firebaseRemoteConfig.getBoolean(ConstantKey.RemoteConfigKey.APP_SHOW_TOKOPOINT_NATIVE, true);
        isShowFirstInstallSearch = firebaseRemoteConfig.getBoolean(ConstantKey.RemoteConfigKey.REMOTE_CONFIG_KEY_FIRST_INSTALL_SEARCH, false);
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
        fragmentFramePerformanceIndexMonitoring.init(
                "home", this, new FragmentFramePerformanceIndexMonitoring.OnFrameListener() {
                    @Override
                    public void onFrameRendered(@NotNull FpiPerformanceData fpiPerformanceData) {

                    }
                }
        );
        getViewLifecycleOwner().getLifecycle().addObserver(fragmentFramePerformanceIndexMonitoring);
        homeMainToolbar = view.findViewById(R.id.toolbar);
        homeMainToolbar.setAfterInflationCallable(getAfterInflationCallable());
        statusBarBackground = view.findViewById(R.id.status_bar_bg);
        homeRecyclerView = view.findViewById(R.id.home_fragment_recycler_view);
        homeRecyclerView.setHasFixedSize(true);
        refreshLayout = view.findViewById(R.id.home_swipe_refresh_layout);
        floatingTextButton = view.findViewById(R.id.recom_action_button);
        stickyLoginView = view.findViewById(R.id.sticky_login_text);
        root = view.findViewById(R.id.root);
        if (getArguments() != null) {
            scrollToRecommendList = getArguments().getBoolean(SCROLL_RECOMMEND_LIST);
        }

        fetchRemoteConfig();
        fetchTokopointsNotification(TOKOPOINTS_NOTIFICATION_TYPE);
        setupStatusBar();
        setupHomeRecyclerView();
        initEggDragListener();

        return view;
    }

    private Callable getAfterInflationCallable(){
        Callable afterinflationCallable = new Callable() {
            @Override
            public Object call() throws Exception {
                calculateSearchbarView(0);
                observeSearchHint();
                return null;
            }
        };
        return afterinflationCallable;
    }

    private void setupHomeRecyclerView() {
        //giving recyclerview larger cache to prevent lag, we can implement this because home dc content
        //is finite
        homeRecyclerView.setItemViewCacheSize(20);
        homeRecyclerView.setItemAnimator(null);

        if (homeRecyclerView.getItemDecorationCount() == 0) {
            homeRecyclerView.addItemDecoration(new HomeRecyclerDecoration(getResources().getDimensionPixelSize(R.dimen.home_recyclerview_item_spacing)));
        }
        homeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                evaluateHomeComponentOnScroll(recyclerView);
                //calculate transparency of homeMainToolbar based on rv offset
                calculateSearchbarView(recyclerView.computeVerticalScrollOffset());
            }
        });
    }

    private void setupStatusBar() {
        statusBarBackground.setBackground(new ColorDrawable(
                ContextCompat.getColor(getActivity(), R.color.green_600)
        ));
        //status bar background compability, we show view background for android >= Kitkat
        //because in that version, status bar can't forced to dark mode, we must set background
        //to keep status bar icon visible
        statusBarBackground.getLayoutParams().height = ViewHelper.getStatusBarHeight(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusBarBackground.setVisibility(View.INVISIBLE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            statusBarBackground.setVisibility(View.VISIBLE);
        } else {
            statusBarBackground.setVisibility(View.GONE);
        }
        //initial condition for status and searchbar
        setStatusBarAlpha(0f);
    }

    private void evaluateInheritScrollForHomeRecommendation() {
        if (layoutManager.findLastCompletelyVisibleItemPosition()
                == viewModel.getRecommendationFeedSectionPosition()) {
            float vt = homeRecyclerView.getOverScroller().getCurrVelocity()/1000;
            float a = -10f;

            double distanceToInherit =
                    Math.abs(((Math.pow(vt,2))/(2*a)));

            inheritScrollVelocityToRecommendation((int)distanceToInherit*1000);
        }
    }

    private void evaluateFloatingTextButtonOnStateChanged() {
        int position = layoutManager.getItemCount();
        if (position == viewModel.getRecommendationFeedSectionPosition()) {
            floatingTextButton.hide();
        } else {
            floatingTextButton.show();
        }
    }

    private void evaluateHomeComponentOnScroll(RecyclerView recyclerView) {
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
            if(homeMainToolbar != null && homeMainToolbar.getViewHomeMainToolBar() != null) {
                homeMainToolbar.showShadow();
            }
            showFeedSectionViewHolderShadow(false);
            homeRecyclerView.setNestedCanScroll(false);
        } else {
            //home feed now can scroll up, so hide maintoolbar shadow
            if(homeMainToolbar != null && homeMainToolbar.getViewHomeMainToolBar() != null) {
                homeMainToolbar.hideShadow();
            }
            showFeedSectionViewHolderShadow(true);
            homeRecyclerView.setNestedCanScroll(true);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAdapter();
        initRefreshLayout();
        subscribeHome();
        initEggTokenScrollListener();
        registerBroadcastReceiverTokoCash();
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
        stickyLoginView.setOnClickListener(v -> {
            stickyLoginView.getTracker().clickOnLogin(StickyLoginConstant.Page.HOME);
            onGoToLogin();
        });
        stickyLoginView.setOnDismissListener(v -> {
            stickyLoginView.dismiss(StickyLoginConstant.Page.HOME);
            stickyLoginView.getTracker().clickOnDismiss(StickyLoginConstant.Page.HOME);

            FloatingEggButtonFragment floatingEggButtonFragment = getFloatingEggButtonFragment();
            if (floatingEggButtonFragment != null) {
                updateEggBottomMargin(floatingEggButtonFragment);
            }
        });
    }

    private void scrollToRecommendList() {
        homeRecyclerView.smoothScrollToPosition(viewModel.getRecommendationFeedSectionPosition());
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
        createAndCallSendScreen();
        adapter.onResume();
        viewModel.refresh(isFirstInstall());
        if (activityStateListener != null) {
            activityStateListener.onResume();
        }
    }

    private void createAndCallSendScreen(){
        WeaveInterface sendScrWeave =  new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return sendScreen();
            }
        };
        Weaver.Companion.executeWeaveCoRoutine(sendScrWeave,
                new WeaverFirebaseConditionCheck(RemoteConfigKey.ENABLE_ASYNC_HOME_SNDSCR, remoteConfig));
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.onPause();
        trackingQueue.sendAll();
        if (activityStateListener != null) {
            activityStateListener.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.onDestroy();
        homeRecyclerView.setAdapter(null);
        adapter = null;
        homeRecyclerView.setLayoutManager(null);
        layoutManager = null;
        unRegisterBroadcastReceiverTokoCash();
    }

    private void initRefreshLayout() {
        refreshLayout.post(() -> {
            viewModel.getSearchHint(isFirstInstall());
            viewModel.refreshHomeData();
            /*
             * set notification gimmick
             */
            if(homeMainToolbar != null && homeMainToolbar.getViewHomeMainToolBar() != null) {
                homeMainToolbar.setNotificationNumber(0);
            }
        });
        refreshLayout.setOnRefreshListener(this);
    }

    private void subscribeHome(){
        observeHomeData();
        observeUpdateNetworkStatusData();
        observeOneClickCheckout();
        observePopupIntroOvo();
        observeErrorEvent();
        observeSendLocation();
        observeStickyLogin();
        observeTrackingData();
        observeRequestImagePlayBanner();
    }

    private void observeErrorEvent(){
        viewModel.getErrorEventLiveData().observe(getViewLifecycleOwner(), data -> showToaster(getString(R.string.home_error_connection), Toaster.TYPE_ERROR));
    }

    private void observeHomeData(){
        viewModel.getHomeLiveData().observe(this, data -> {
            if(data != null){
                if (data.getList().size() > VISITABLE_SIZE_WITH_DEFAULT_BANNER ) {
                    configureHomeFlag(data.getHomeFlag());
                    setData(new ArrayList(data.getList()), data.isCache());
                } else if (!data.isCache()){
                    showToaster(getString(R.string.home_error_connection), Toaster.TYPE_ERROR);
                }
            }
        });
    }

    private void observeUpdateNetworkStatusData(){
        viewModel.getUpdateNetworkLiveData().observe(this, result -> {
            resetImpressionListener();
            if(result.getStatus() == Result.Status.SUCCESS){
                hideLoading();
            } else if(result.getStatus() == Result.Status.ERROR){
                hideLoading();
                showToaster(getString(R.string.home_error_connection), Toaster.TYPE_ERROR);
            } else {
                showLoading();
            }
        });
    }

    private void observeTrackingData(){
        viewModel.getTrackingLiveData().observe(this, trackingData-> {
            List<HomeVisitable> homeVisitables = trackingData.getContentIfNotHandled();
            if (homeVisitables != null) {
                List<Visitable> visitables = new ArrayList(homeVisitables);
                addImpressionToTrackingQueue(visitables);
                setupViewportImpression(visitables);
            }
        });
    }

    private void observeSearchHint(){
        if(getView() != null && !viewModel.getSearchHint().hasObservers() && homeMainToolbar != null && homeMainToolbar.getViewHomeMainToolBar() != null){
            viewModel.getSearchHint().observe(getViewLifecycleOwner(), data -> {
                setHint(data);
            });
        }
    }

    private void observeOneClickCheckout(){
        viewModel.getOneClickCheckout().observe(getViewLifecycleOwner(), event -> {
            Object data = event.peekContent();
            if(data instanceof Throwable){
                // error
                showToaster(getString(R.string.home_error_connection), Toaster.TYPE_ERROR);
            } else {
                Map dataMap = (Map) data;
               sendEETracking((HashMap<String, Object>) HomePageTrackingV2.RecommendationList.INSTANCE.getAddToCartOnDynamicListCarousel(
                        (DynamicHomeChannel.Channels) dataMap.get(HomeViewModel.CHANNEL),
                        (DynamicHomeChannel.Grid) dataMap.get(HomeViewModel.GRID),
                        (int) dataMap.get(HomeViewModel.POSITION),
                        ((AddToCartDataModel) dataMap.get(HomeViewModel.ATC)).getData().getCartId(),
                       viewModel.getUserId()
               ));
               RouteManager.route(getContext(), ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeSearchHint();
    }

    private void observeSendLocation(){
        viewModel.getSendLocationLiveData().observe(getViewLifecycleOwner(), data -> detectAndSendLocation());
    }

    private void observePopupIntroOvo(){
        viewModel.getPopupIntroOvoLiveData().observe(getViewLifecycleOwner(), data -> {
            if (RouteManager.isSupportApplink(getActivity(), data.peekContent())) {
                Intent intentBalanceWallet = RouteManager.getIntent(getActivity(), data.peekContent());
                Objects.requireNonNull(getContext()).startActivity(intentBalanceWallet);
                Activity activity = (Activity) getContext();
                activity.overridePendingTransition(R.anim.anim_slide_up_in, R.anim.anim_page_stay);
            }
        });
    }

    private void observeStickyLogin(){
        viewModel.getStickyLogin().observe(getViewLifecycleOwner(), result -> {
            if(result.getStatus() == Result.Status.SUCCESS){
                setStickyContent(result.getData());
            } else {
                hideStickyLogin();
            }
        });
    }

    @VisibleForTesting
    private void observeRequestImagePlayBanner(){
        viewModel.getRequestImageTestLiveData().observe(this, playCardViewModelEvent -> Glide.with(getContext())
                .asBitmap()
                .load(playCardViewModelEvent.peekContent().getPlayCardHome().getCoverUrl())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        viewModel.setPlayBanner(playCardViewModelEvent.peekContent());
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        viewModel.clearPlayBanner();
                    }
                }));
    }

    private void setData(List<HomeVisitable> data, boolean isCache){
        if(!data.isEmpty()) {
            if (!isCache && getPageLoadTimeCallback() != null) {
                getPageLoadTimeCallback().stopNetworkRequestPerformanceMonitoring();
            }

            if (needToPerformanceMonitoring() && getPageLoadTimeCallback() != null) {
                getPageLoadTimeCallback().stopNetworkRequestPerformanceMonitoring();
                getPageLoadTimeCallback().startRenderPerformanceMonitoring();
                setOnRecyclerViewLayoutReady(isCache);
            }

            adapter.submitList(data);
            if (isDataValid(data)) {
                removeNetworkError();
            } else {
                showToaster(getString(R.string.home_error_connection), Toaster.TYPE_ERROR);
            }
        }
    }

    private boolean isDataValid(List<HomeVisitable> visitables) {
        return containsInstance(visitables, HomepageBannerDataModel.class);
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
        int endTransitionOffset =
                startToTransitionOffset + searchBarTransitionRange;
        int maxTransitionOffset = endTransitionOffset - startToTransitionOffset;

        //mapping alpha to be rendered per pixel for x height
        float offsetAlpha =
                (255f / maxTransitionOffset) * (offset - startToTransitionOffset);
        //2.5 is maximum
        if (offsetAlpha < 0) {
            offsetAlpha = 0;
        }
        if(homeMainToolbar != null && homeMainToolbar.getViewHomeMainToolBar() != null) {
            if (offsetAlpha >= 150) {
                homeMainToolbar.switchToDarkToolbar();
                if (isLightThemeStatusBar) requestStatusBarDark();
            } else {
                homeMainToolbar.switchToLightToolbar();
                if (!isLightThemeStatusBar) requestStatusBarLight();
            }
        }

        if (offsetAlpha >= 255) {
            offsetAlpha = 255;
        }

        if(homeMainToolbar != null && homeMainToolbar.getViewHomeMainToolBar() != null) {
            if (offsetAlpha >= 0 && offsetAlpha <= 255) {
                homeMainToolbar.setBackgroundAlpha(offsetAlpha);
                setStatusBarAlpha(offsetAlpha);
            }
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
        layoutManager = new LinearLayoutManager(getContext());
        homeRecyclerView.setLayoutManager(layoutManager);
        HomeAdapterFactory adapterFactory = new HomeAdapterFactory(
                getChildFragmentManager(),
                this,
                this,
                this,
                this,
                this,
                homeRecyclerView.getRecycledViewPool(),
                this,
                this
        );
        AsyncDifferConfig<HomeVisitable> asyncDifferConfig =
                new AsyncDifferConfig.Builder<HomeVisitable>(new HomeVisitableDiffUtil())
                .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
                .build();
        adapter = new HomeRecycleAdapter(asyncDifferConfig, adapterFactory, new ArrayList<HomeVisitable>());
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

    private void configureHomeFlag(HomeFlag homeFlag) {
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
        viewModel.getTokocashPendingBalance();
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
                RouteManager.route(getActivity(), ApplinkConstInternalGlobal.WEBVIEW, tokoPointUrl);
            else
                RouteManager.route(getActivity(), ApplinkConstInternalGlobal.WEBVIEW_TITLE, pageTitle, tokoPointUrl);
        }
    }

    @Override
    public void onPageDragStateChanged(boolean isDragged) {
        if (refreshLayout != null) {
            refreshLayout.setEnabled(!isDragged);
        }
    }

    @Override
    public void onPromoClick(int position, BannerSlidesModel slidesModel) {
        // tracking handler
        if(slidesModel.getType().equals(BannerSlidesModel.TYPE_BANNER_PERSO)){
            sendEETracking((HashMap<String, Object>) HomePageTrackingV2.HomeBanner.INSTANCE.getOverlayBannerClick(slidesModel));
        }else {
            sendEETracking((HashMap<String, Object>) HomePageTrackingV2.HomeBanner.INSTANCE.getBannerClick(slidesModel));
        }
        HomeTrackingUtils.homeSlidingBannerClick(getContext(), slidesModel, position);

        if (getActivity() != null && RouteManager.isSupportApplink(getActivity(), slidesModel.getApplink())) {
            openApplink(slidesModel.getApplink());
        } else {
            openWebViewURL(slidesModel.getRedirectUrl(), getActivity());
        }
        viewModel.onBannerClicked(slidesModel);
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
    public void onCloseTicker() {
        HIDE_TICKER = true;
        viewModel.onCloseTicker();
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
                    viewModel.onRemoveSuggestedReview();
                }
                break;
            case REQUEST_CODE_PLAY_ROOM:
                if(data != null && data.hasExtra(EXTRA_TOTAL_VIEW)) viewModel.updateBannerTotalView(data.getStringExtra(EXTRA_TOTAL_VIEW));
                break;
        }
    }

    @Override
    public void onRefresh() {
        //on refresh most likely we already lay out many view, then we can reduce
        //animation to keep our performance
        adapter.resetImpressionHomeBanner();
        resetFeedState();
        removeNetworkError();
        if (viewModel != null) {
            viewModel.getSearchHint(isFirstInstall());
            viewModel.refreshHomeData();
            getStickyContent();
        }

        if (getActivity() instanceof RefreshNotificationListener) {
            ((RefreshNotificationListener) getActivity()).onRefreshNotification();
        }
        loadEggData();
        fetchTokopointsNotification(TOKOPOINTS_NOTIFICATION_TYPE);
    }

    private void onNetworkRetry() {
        //on refresh most likely we already lay out many view, then we can reduce
        //animation to keep our performance
        homeRecyclerView.setItemAnimator(null);

        resetFeedState();
        removeNetworkError();
        homeRecyclerView.setEnabled(false);
        if (viewModel != null) {
            viewModel.refresh(isFirstInstall());
            getStickyContent();
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
        if (viewModel != null) {
            viewModel.refreshHomeData();
        }
    }

    private void showLoading() {
        refreshLayout.setRefreshing(true);
    }

    private void getStickyContent(){
        boolean isShowSticky = remoteConfig.getBoolean(StickyLoginConstant.REMOTE_CONFIG_FOR_HOME, true);
        if(isShowSticky && !userSession.isLoggedIn()) viewModel.getStickyContent();
    }

    private void hideLoading() {
        refreshLayout.setRefreshing(false);
        homeRecyclerView.setEnabled(true);
    }

    private void setOnRecyclerViewLayoutReady(boolean isCache) {
        homeRecyclerView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //At this point the layout is complete and the
                        //dimensions of recyclerView and any child views are known.
                        //Remove listener after changed RecyclerView's height to prevent infinite loop
                        if (homePerformanceMonitoringListener != null) {
                            homePerformanceMonitoringListener.stopHomePerformanceMonitoring(isCache);
                        }

                        homePerformanceMonitoringListener = null;
                        homeRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    private void needToShowGeolocationComponent() {
        boolean firebaseShowGeolocationComponent = remoteConfig.getBoolean(RemoteConfigKey.SHOW_HOME_GEOLOCATION_COMPONENT, true);
        if (!firebaseShowGeolocationComponent) {
            viewModel.setNeedToShowGeolocationComponent(false);
            return;
        }

        boolean needToShowGeolocationComponent = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean userHasDeniedPermissionBefore = ActivityCompat
                    .shouldShowRequestPermissionRationale(getActivity(),
                            PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION);
            if (userHasDeniedPermissionBefore) {
                viewModel.setNeedToShowGeolocationComponent(false);
                return;
            }
        }

        if (getActivity() != null) {
            if (viewModel.hasGeolocationPermission()) {
                needToShowGeolocationComponent = false;
            }
        }
        if(needToShowGeolocationComponent && HIDE_GEO) {
            viewModel.setNeedToShowGeolocationComponent(false);
            return;
        }
        viewModel.setNeedToShowGeolocationComponent(needToShowGeolocationComponent);
    }

    private void setGeolocationPermission() {
        if (getActivity() == null) viewModel.setGeolocationPermission(false);
        else viewModel.setGeolocationPermission(permissionCheckerHelper.hasPermission(getActivity(),
                new String[]{PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION}));
    }

    private void promptGeolocationPermission() {
        permissionCheckerHelper.checkPermission(this,
                PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION,
                new PermissionCheckerHelper.PermissionCheckListener() {
                    @Override
                    public void onPermissionDenied(@NotNull String permissionText) {
                        HomePageTracking.eventClickNotAllowGeolocation(getActivity());
                        viewModel.onCloseGeolocation();
                        showNotAllowedGeolocationSnackbar();
                    }

                    @Override
                    public void onNeverAskAgain(@NotNull String permissionText) {

                    }

                    @Override
                    public void onPermissionGranted() {
                        HomePageTracking.eventClickAllowGeolocation(getActivity());
                        detectAndSendLocation();
                        viewModel.onCloseGeolocation();
                        showAllowedGeolocationSnackbar();
                    }
                }, "");
    }

    private void detectAndSendLocation() {
        Observable.just(true).map(aBoolean -> {
            LocationDetectorHelper locationDetectorHelper = new LocationDetectorHelper(
                    permissionCheckerHelper,
                    LocationServices.getFusedLocationProviderClient(getActivity()
                            .getApplicationContext()),
                    getActivity().getApplicationContext());
            locationDetectorHelper.getLocation(onGetLocation(), getActivity(),
                    LocationDetectorHelper.TYPE_DEFAULT_FROM_CLOUD,
                    "");
                return true;
        }).subscribeOn(Schedulers.io()).subscribe(aBoolean -> {
            //IGNORE
        }, throwable -> {
            //IGNORE
        });
    }

    private Function1<DeviceLocation, Unit> onGetLocation() {
        return (deviceLocation) -> {
            saveLocation(getActivity(), deviceLocation.getLatitude(), deviceLocation.getLongitude());
            viewModel.sendGeolocationData();
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

    private void saveFirstInstallTime() {
        if (getContext() != null) {
            sharedPrefs = getContext().getSharedPreferences(
                    ConstantKey.FirstInstallCache.KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE);
            sharedPrefs.edit().putLong(
                    ConstantKey.FirstInstallCache.KEY_FIRST_INSTALL_TIME_SEARCH, 0).apply();
        }
    }

    private boolean isFirstInstall() {
        if (getContext() != null &&
                !userSession.isLoggedIn() &&
                isShowFirstInstallSearch) {
            sharedPrefs = getContext().getSharedPreferences(
                    ConstantKey.FirstInstallCache.KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE);
            long firstInstallCacheValue = sharedPrefs.getLong(
                    ConstantKey.FirstInstallCache.KEY_FIRST_INSTALL_TIME_SEARCH, 0);

            if (firstInstallCacheValue == 0) return false;

            firstInstallCacheValue += (30 * 60000);

            Date now = new Date();
            Date firstInstallTime = new Date(firstInstallCacheValue);

            if (now.compareTo(firstInstallTime) <= 0) {
                return true;
            } else {
                saveFirstInstallTime();
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionCheckerHelper.onRequestPermissionsResult(getActivity(), requestCode, permissions, grantResults);
    }

    private void setHint(SearchPlaceholder searchPlaceholder) {
        if (searchPlaceholder.getData() != null && searchPlaceholder.getData().getPlaceholder() != null && searchPlaceholder.getData().getKeyword() != null) {
            homeMainToolbar.setHint(
                    searchPlaceholder.getData().getPlaceholder(),
                    searchPlaceholder.getData().getKeyword(),
                    isFirstInstall());
        }
    }

    private void addImpressionToTrackingQueue(List<Visitable> visitables) {
        if (visitables != null) {
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
    }

    @Override
    public void showNetworkError(String message) {
        if (isAdded() && getActivity() != null) {
            if (adapter.getItemCount() > 0) {
                if (messageSnackbar == null) {
                    messageSnackbar = NetworkErrorHelper.createSnackbarWithAction(
                            getActivity(), getString(R.string.msg_network_error),
                            this::onNetworkRetry
                    );
                }
                messageSnackbar.showRetrySnackbar();
            } else {
                NetworkErrorHelper.showEmptyState(getActivity(), root, message,
                        this::onRefresh);
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

    @Override
    public void updateExpiredChannel(@NotNull DynamicChannelDataModel dynamicChannelDataModel, int position) {
        viewModel.getDynamicChannelData(dynamicChannelDataModel, position);
    }

    @Override
    public void onBuyAgainOneClickCheckOutClick(@NotNull DynamicHomeChannel.Grid grid, @NotNull DynamicHomeChannel.Channels channel, int position) {
        viewModel.getOneClickCheckout(channel, grid, position);
    }

    @Override
    public void onBuyAgainCloseChannelClick(@NotNull DynamicHomeChannel.Channels channel, int position) {
        viewModel.onCloseBuyAgain(channel, position);
        TrackApp.getInstance().getGTM().sendGeneralEvent(HomePageTrackingV2.RecommendationList.INSTANCE.getCloseClickOnDynamicListCarousel(channel, viewModel.getUserId()));
    }

    private void onActionLinkClicked(String actionLink, String trackingAttribution) {
        long now = System.currentTimeMillis();
        if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
            return;
        }
        mLastClickTime = now;

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

    private void removeNetworkError() {
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

    @Override
    public void getTabBusinessWidget(int position) {
        viewModel.getBusinessUnitTabData(position);
    }

    @Override
    public void getBusinessUnit(int tabId, int position) {
        viewModel.getBusinessUnitData(tabId, position);
    }

    @Override
    public void getPlayChannel(int position) {
        viewModel.getPlayBanner(position);
    }

    @Override
    public void onRefreshTokoPointButtonClicked() {
        viewModel.onRefreshTokoPoint();
    }

    @Override
    public void onRefreshTokoCashButtonClicked() {
        viewModel.onRefreshTokoCash();
    }

    @Override
    public void onLegoBannerClicked(String actionLink, String trackingAttribution) {
        onActionLinkClicked(actionLink, trackingAttribution);
    }

    @Override
    public void onPromoScrolled(BannerSlidesModel bannerSlidesModel) {
        HomeTrackingUtils.homeSlidingBannerImpression(getContext(), bannerSlidesModel, bannerSlidesModel.getPosition());
        if (bannerSlidesModel.getType().equals(BannerSlidesModel.TYPE_BANNER_PERSO) && !bannerSlidesModel.isInvoke()) {
            putEEToTrackingQueue((HashMap<String, Object>) HomePageTrackingV2.HomeBanner.INSTANCE.getOverlayBannerImpression(bannerSlidesModel));
        } else if (!bannerSlidesModel.isInvoke()) {
            if(!bannerSlidesModel.getTopadsViewUrl().isEmpty()){
                viewModel.sendTopAds(bannerSlidesModel.getTopadsViewUrl());
            }

            HashMap dataLayer = (HashMap) HomePageTrackingV2.HomeBanner.INSTANCE.getBannerImpression(bannerSlidesModel);
            dataLayer.put(com.tokopedia.iris.util.ConstantKt.KEY_SESSION_IRIS, irisSession.getSessionId());
            putEEToTrackingQueue(dataLayer);
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
            createAndCallSendScreen();
        }
    }

    @NotNull
    private Boolean sendScreen() {
        if (getActivity() != null && System.currentTimeMillis() > lastSendScreenTimeMillis + SEND_SCREEN_MIN_INTERVAL_MILLIS) {
            lastSendScreenTimeMillis = System.currentTimeMillis();
            HomePageTracking.sendScreen(getActivity(), getScreenName(), userSession.isLoggedIn());
        }
        return true;
    }

    @Override
    public boolean isMainViewVisible() {
        return getUserVisibleHint();
    }

    @NotNull
    @Override
    public RecyclerView.RecycledViewPool getParentPool() {
        return homeRecyclerView.getRecycledViewPool();
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

    @Override
    public void sendIrisTrackerHashMap(@NotNull HashMap<String, Object> tracker) {
        putEEToIris(tracker);
    }

    @Override
    public void onPopularKeywordSectionReloadClicked(int position, @NotNull DynamicHomeChannel.Channels channel) {
        viewModel.getPopularKeywordData();
        HomePageTrackingV2.PopularKeyword.INSTANCE.sendPopularKeywordClickReload(channel);
    }

    @Override
    public void onPopularKeywordItemImpressed(@NotNull DynamicHomeChannel.Channels channel, int position, @NotNull String keyword) {
        trackingQueue.putEETracking((HashMap<String, Object>) HomePageTrackingV2.PopularKeyword.INSTANCE.getPopularKeywordImpressionItem(channel, position, keyword));
    }

    @Override
    public void onPopularKeywordItemClicked(@NotNull String applink, @NotNull DynamicHomeChannel.Channels channel, int position, @NotNull String keyword) {
        RouteManager.route(getContext(),applink);
        HomePageTrackingV2.PopularKeyword.INSTANCE.sendPopularKeywordClickItem(channel, position, keyword);
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
                    viewModel.getHeaderData(); // update header data
            }
        }
    };

    @Override
    public void onRetryLoadFeeds() {
        viewModel.getFeedTabData();
    }

    private boolean isUserLoggedIn() {
        return userSession.isLoggedIn();
    }

    private String getUserShopId() {
        return userSession.getShopId();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        setUserVisibleHint(!hidden);
        if (fragmentFramePerformanceIndexMonitoring != null) fragmentFramePerformanceIndexMonitoring.onFragmentHidden(hidden);
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
        if (homeMainToolbar != null && homeMainToolbar.getViewHomeMainToolBar() != null) {
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

    @NotNull
    @Override
    public String getUserId() {
        return viewModel.getUserId();
    }

    @Override
    public int getWindowHeight() {
        if (getActivity() != null) {
            return root.getHeight();
        } else {
            return 0;
        }
    }

    private int getWindowHeightForExtraSpace() {
        if (getActivity() != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.heightPixels;
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
                viewModel.getRecommendationFeedSectionPosition()
        );
        if (feedViewHolder instanceof HomeRecommendationFeedViewHolder) {
            ((HomeRecommendationFeedViewHolder) feedViewHolder).showFeedTabShadow(show);
        }
    }

    private void inheritScrollVelocityToRecommendation(int velocity) {
        RecyclerView.ViewHolder feedViewHolder = homeRecyclerView.findViewHolderForAdapterPosition(
                viewModel.getRecommendationFeedSectionPosition()
        );
        if (feedViewHolder instanceof HomeRecommendationFeedViewHolder) {
            ((HomeRecommendationFeedViewHolder) feedViewHolder).scrollByVelocity(velocity);
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
        viewModel.onCloseGeolocation();
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
                .setAction(getString(R.string.discovery_home_snackbar_geolocation_setting), view -> {
                    HomePageTracking.eventClickOnAtur(getActivity());
                    goToApplicationDetailActivity();
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
    public void sendEETracking(@NotNull HashMap<String, Object> data) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(data);
    }

    @Override
    public void putEEToIris(@NotNull HashMap<String, Object> data) {
        if (irisAnalytics!=null) {
            irisAnalytics.saveEvent(data);
        }
    }

    private void setStickyContent(StickyLoginTickerPojo.TickerDetail tickerDetail) {
        this.tickerDetail = tickerDetail;
        updateStickyState();
    }

    private void hideStickyLogin() {
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
            String newAppLink = Uri.parse(applink)
                    .buildUpon()
                    .appendQueryParameter(REVIEW_CLICK_AT, String.valueOf(clickReviewAt))
                    .appendQueryParameter(UTM_SOURCE, DEFAULT_UTM_SOURCE)
                    .build().toString();
            Intent intent = RouteManager.getIntent(getContext(), newAppLink);
            startActivityForResult(intent, REQUEST_CODE_REVIEW);
        }, delay);
    }

    @Override
    public void onCloseClick() {
        viewModel.dismissReview();
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
        return displayMetrics.widthPixels;
    }

    private void resetImpressionListener() {
        for (Map.Entry<String, RecyclerView.OnScrollListener> entry : impressionScrollListeners.entrySet()) {
            if (homeRecyclerView != null) {
                homeRecyclerView.removeOnScrollListener(entry.getValue());
            }
        }
        impressionScrollListeners.clear();
    }

    @Override
    public void refreshHomeData() {
        refreshLayout.setRefreshing(true);
        onNetworkRetry();
    }

    @Override
    public void onTokopointCheckNowClicked(@NotNull String applink) {
        if(!TextUtils.isEmpty(applink)){
            RouteManager.route(getContext(),applink);
        }
    }

    @Override
    public void onOpenPlayChannelList(String appLink) {
        openApplink(appLink);
    }

    @Override
    public void onOpenPlayActivity(@NotNull View root, String channelId) {
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalContent.PLAY_DETAIL, channelId);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                Pair.create(root.findViewById(R.id.exo_content_frame), getString(R.string.home_transition_video))
        );
        startActivityForResult(intent, REQUEST_CODE_PLAY_ROOM, options.toBundle());
    }

    private boolean needToPerformanceMonitoring() {
        return homePerformanceMonitoringListener != null;
    }

    private void showToaster(String message, int typeToaster){
        showToasterWithAction(message, typeToaster, "", v -> {});
    }

    private void showToasterWithAction(String message, int typeToaster, String actionText, View.OnClickListener clickListener){
        Toaster.INSTANCE.make(root, message, Snackbar.LENGTH_LONG, typeToaster, actionText, clickListener);
    }

    public void addRecyclerViewScrollImpressionListener(DynamicChannelDataModel dynamicChannelDataModel, int adapterPosition) {
        if (!impressionScrollListeners.containsKey(dynamicChannelDataModel.getChannel().getId())) {
            RecyclerView.OnScrollListener impressionScrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (layoutManager.findLastVisibleItemPosition() >= adapterPosition) {
                        sendIrisTracker(DynamicChannelViewHolder.Companion.getLayoutType(dynamicChannelDataModel.getChannel()),
                                dynamicChannelDataModel.getChannel(),
                                adapterPosition);
                        homeRecyclerView.removeOnScrollListener(this);
                    }
                }
            };
            impressionScrollListeners.put(dynamicChannelDataModel.getChannel().getId(), impressionScrollListener);
            homeRecyclerView.addOnScrollListener(impressionScrollListener);
        }
    }

    private void sendIrisTracker(int layoutType, DynamicHomeChannel.Channels channel, int position) {
        switch (layoutType) {
            case TYPE_SPRINT_SALE :
                putEEToIris(
                        HomePageTracking.getEnhanceImpressionSprintSaleHomePage(
                                channel.getId(), channel.getGrids(), position
                        )
                );
                break;
            case TYPE_ORGANIC :
                putEEToIris(
                        HomePageTracking.getIrisEnhanceImpressionDynamicSprintLegoHomePage(
                                channel.getId(), channel.getGrids(), channel.getHeader().getName()
                        )
                );
                break;
            case TYPE_SPRINT_LEGO :
                putEEToIris(
                        (HashMap<String, Object>) HomePageTrackingV2.SprintSale.INSTANCE.getSprintSaleImpression(channel, true)
                );
                break;
            case TYPE_SIX_GRID_LEGO :
                putEEToIris(
                        HomePageTracking.getEnhanceImpressionLegoBannerHomePage(
                                channel.getId(), channel.getGrids(), channel.getHeader().getName(), position
                        )
                );
                break;
            case TYPE_THREE_GRID_LEGO :
                putEEToIris(
                        HomePageTracking.getIrisEnhanceImpressionLegoThreeBannerHomePage(
                                channel.getId(), channel.getGrids(), channel.getHeader().getName(), position
                        )
                );
                break;
            case TYPE_FOUR_GRID_LEGO :
                putEEToIris(
                        (HashMap<String, Object>) HomePageTrackingV2.LegoBanner.INSTANCE.getLegoBannerFourImageImpression(
                                channel, position, true
                        )
                );
                break;
            case TYPE_GIF_BANNER :
                putEEToIris(
                        HomePageTracking.getEnhanceImpressionPromoGifBannerDC(channel));
                break;
            case TYPE_BANNER_CAROUSEL :
            case TYPE_BANNER :
                String bannerType = BannerOrganicViewHolder.TYPE_NON_CAROUSEL;
                if (layoutType == TYPE_BANNER_CAROUSEL) bannerType = BannerOrganicViewHolder.TYPE_CAROUSEL;
                putEEToIris(
                        HomePageTracking.getEnhanceImpressionProductChannelMix(
                                channel, bannerType
                        )
                );
                putEEToIris(
                        HomePageTracking.getIrisEnhanceImpressionBannerChannelMix(channel)
                );
                break;
            case TYPE_MIX_TOP:
                putEEToIris((HashMap<String, Object>) MixTopTracking.INSTANCE.getMixTopViewIris(MixTopTracking.INSTANCE.mapChannelToProductTracker(channel), channel.getHeader().getName(), channel.getId(), String.valueOf(position)));
                break;
            case TYPE_MIX_LEFT:
                putEEToIris((HashMap<String, Object>) HomePageTrackingV2.MixLeft.INSTANCE.getMixLeftProductView(channel, true));
                break;
            case TYPE_RECOMMENDATION_LIST:
                putEEToIris((HashMap<String, Object>) HomePageTrackingV2.RecommendationList.INSTANCE.getRecommendationListImpression(channel, true, viewModel.getUserId()));
                break;
            case TYPE_PRODUCT_HIGHLIGHT:
                putEEToIris((HashMap<String, Object>) ProductHighlightTracking.INSTANCE.getProductHighlightImpression(
                        channel, true
                ));
                break;
        }
    }

    private void setupViewportImpression(List<Visitable> visitables) {
        int index = 0;
        for (Visitable visitable: visitables) {
            if (visitable instanceof DynamicChannelDataModel) {
                DynamicChannelDataModel dynamicChannelDataModel = ((DynamicChannelDataModel) visitable);
                if (!dynamicChannelDataModel.isCache() && !dynamicChannelDataModel.getChannel().isInvoke()) {
                    addRecyclerViewScrollImpressionListener(dynamicChannelDataModel, index);
                }
            }
            index++;
        }
    }

    private PageLoadTimePerformanceInterface getPageLoadTimeCallback() {
        if (homePerformanceMonitoringListener != null && homePerformanceMonitoringListener.getPageLoadTimePerformanceInterface() != null) {
            return homePerformanceMonitoringListener.getPageLoadTimePerformanceInterface();
        }
        return null;
    }

    @Override
    public FragmentFramePerformanceIndexMonitoring getFramePerformanceIndexData() {
        return fragmentFramePerformanceIndexMonitoring;
    }

    @Override
    public void onContentClickListener(@NotNull String applink) {
        RouteManager.route(getContext(), applink);
    }

    @Override
    public void onDeclineClickListener(@NotNull Map<String, String> requestParams) {
        viewModel.declineRechargeRecommendationItem(requestParams);
    }
}
