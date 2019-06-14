package com.tokopedia.home.beranda.presentation.view.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.core.analytics.screen.IndexScreenTracking;
import com.tokopedia.core.router.wallet.IWalletRouter;
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
import com.tokopedia.home.beranda.helper.ViewHelper;
import com.tokopedia.home.beranda.listener.ActivityStateListener;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.listener.HomeEggListener;
import com.tokopedia.home.beranda.listener.HomeFeedsListener;
import com.tokopedia.home.beranda.listener.HomeInspirationListener;
import com.tokopedia.home.beranda.listener.HomeTabFeedListener;
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.home.beranda.presentation.view.HomeContract;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter;
import com.tokopedia.home.beranda.presentation.view.adapter.LinearLayoutManagerWithSmoothScroller;
import com.tokopedia.home.beranda.presentation.view.adapter.TrackedVisitable;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeAdapterFactory;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.HomeRecyclerDecoration;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.HomeRecommendationFeedViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.CashBackData;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HomeRecommendationFeedViewModel;
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils;
import com.tokopedia.home.beranda.presentation.view.customview.NestedRecyclerView;
import com.tokopedia.home.beranda.presentation.view.viewmodel.FeedTabModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.home.constant.BerandaUrl;
import com.tokopedia.home.constant.ConstantKey;
import com.tokopedia.home.widget.FloatingTextButton;
import com.tokopedia.home.widget.ToggleableSwipeRefreshLayout;
import com.tokopedia.loyalty.view.activity.PromoListActivity;
import com.tokopedia.loyalty.view.activity.TokoPointWebviewActivity;
import com.tokopedia.navigation_common.listener.AllNotificationListener;
import com.tokopedia.navigation_common.listener.FragmentListener;
import com.tokopedia.navigation_common.listener.ShowCaseListener;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.searchbar.HomeMainToolbar;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.tokocash.TokoCashRouter;
import com.tokopedia.tokocash.pendingcashback.domain.PendingCashback;
import com.tokopedia.tokopoints.notification.TokoPointsNotificationManager;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
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
    private static final String TOKOPOINTS_NOTIFICATION_TYPE = "drawer";
    private static final int REQUEST_CODE_DIGITAL_PRODUCT_DETAIL = 220;
    private static final int DEFAULT_FEED_PAGER_OFFSCREEN_LIMIT = 10;
    public static final String EXTRA_SHOP_ID = "EXTRA_SHOP_ID";
    public static final String KEY_NAVIGATION_BAR_HEIGHT = "navigation_bar_height";
    public static final String KEY_DIMEN = "dimen";
    public static final String KEY_DEF_PACKAGE = "android";
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_TITLE = "core_web_view_extra_title";

    String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private ActivityStateListener activityStateListener;

    @Inject
    HomePresenter presenter;

    private UserSessionInterface userSession;
    private NestedRecyclerView homeRecyclerView;
    private FrameLayout root;
    private ToggleableSwipeRefreshLayout refreshLayout;
    private HomeRecycleAdapter adapter;
    private RemoteConfig firebaseRemoteConfig;
    private PerformanceMonitoring performanceMonitoring;
    private SnackbarRetry messageSnackbar;
    private LinearLayoutManager layoutManager;
    private FloatingTextButton floatingTextButton;
    private boolean showRecomendation;
    private boolean mShowTokopointNative;
    private RecyclerView.OnScrollListener onEggScrollListener;

    private TrackingQueue trackingQueue;

    private HomeMainToolbar homeMainToolbar;

    public static final String SCROLL_RECOMMEND_LIST = "recommend_list";

    private boolean isTraceStopped = false;
    private boolean isFeedLoaded = false;

    private View statusBarBackground;

    private int startToTransitionOffset = 0;
    private int searchBarTransitionRange = 0;
    private Visitable feedTabVisitable;
    private boolean scrollToRecommendList;

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

        searchBarTransitionRange =
                getResources().getDimensionPixelSize(R.dimen.home_searchbar_transition_range);
        startToTransitionOffset =
                (getResources().getDimensionPixelSize(R.dimen.banner_background_height))/4;
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
        } else {
            floatingTextButton.setVisibility(View.GONE);
        }
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
            homeRecyclerView.addItemDecoration(new HomeRecyclerDecoration(
                    getResources().getDimensionPixelSize(R.dimen.home_recyclerview_item_spacing)
            ));
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

                if (recyclerView.canScrollVertically(1)){
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

    private void scrollToRecommendList() {
        homeRecyclerView.smoothScrollToPosition(adapter.getRecommendationFeedSectionPosition());
        scrollToRecommendList = false;
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
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (getActivity() instanceof ShowCaseListener) { // show on boarding and notify mainparent
                    ((ShowCaseListener) getActivity()).onReadytoShowBoarding(buildShowCase());
                }

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
        } else {
            homeMainToolbar.switchToLightToolbar();
        }

        if (offsetAlpha >= 0 && offsetAlpha <= 255) {
            homeMainToolbar.setBackgroundAlpha(offsetAlpha);
            setStatusBarAlpha(offsetAlpha);
        }
    }

    private void setStatusBarAlpha(float alpha) {
        Drawable drawable = statusBarBackground.getBackground();
        drawable.setAlpha((int)alpha);
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
                this
        );
        adapter = new HomeRecycleAdapter(adapterFactory, new ArrayList<Visitable>());
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
        Analytics tracker = HomePageTracking.getTracker(getActivity());
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

    private void goToOvo(String appLinkScheme){
        Intent intent = appLinkScheme == null || appLinkScheme.isEmpty() ?
                RouteManager.getIntent(getActivity(), ApplinkConst.WEBVIEW).putExtra("EXTRA_URL", appLinkScheme)
                : RouteManager.isSupportApplink(getActivity(), appLinkScheme)
                ? RouteManager.getIntent(getActivity(), appLinkScheme).setData(Uri.parse(appLinkScheme))
                : RouteManager.getIntent(getActivity(), ApplinkConst.WEBVIEW).putExtra("EXTRA_URL", appLinkScheme);
        startActivityForResult(intent, IWalletRouter.DEFAULT_WALLET_APPLINK_REQUEST_CODE);
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

        AnalyticsTrackerUtil.sendEvent(getActivity(),
                AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                AnalyticsTrackerUtil.CategoryKeys.HOMEPAGE,
                AnalyticsTrackerUtil.ActionKeys.CLICK_POINT,
                AnalyticsTrackerUtil.EventKeys.TOKOPOINTS_LABEL);
    }

    @Override
    public void onPromoClick(int position, BannerSlidesModel slidesModel, String attribution) {
        if (getActivity() != null && RouteManager.isSupportApplink(getActivity(), slidesModel.getApplink())) {
            openApplink(slidesModel.getApplink(), attribution);
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

    private void showBannerWebViewOnAllPromoClickFromHomeIntent(String url, String title){
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.PROMO);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        startActivity(intent);
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
        if (performanceMonitoring != null && !isTraceStopped) {
            performanceMonitoring.stopTrace();
            isTraceStopped = true;
        }
    }

    @Override
    public void setItems(List<Visitable> items, int repositoryFlag) {
        if (repositoryFlag == HomePresenter.HomeDataSubscriber.FLAG_FROM_NETWORK) {
            adapter.setItems(items);
            presenter.getFeedTabData();
            adapter.showLoading();
        } else {
            adapter.setItems(items);
        }
    }

    private void updateFeedRecommendationVisitable(Visitable feedRecommendationVisitable){
        this.feedTabVisitable = feedRecommendationVisitable;
        List<Visitable> currentVisitables = adapter.getItems();

        for (int i = 0 ; i<currentVisitables.size() ; i++) {
            if (currentVisitables.get(i) instanceof HomeRecommendationFeedViewModel) {
                currentVisitables.set(i, feedRecommendationVisitable);
                adapter.setElement(i, feedRecommendationVisitable);
                //set new data to false because visitable already passed to adapter
                ((HomeRecommendationFeedViewModel) feedTabVisitable).setNewData(false);
                return;
            }
        }

        //if looping not returning any home recommendation feed view model
        //then add one
        adapter.addElement(feedRecommendationVisitable);
    }

    @Override
    public void updateListOnResume(List<Visitable> visitables) {
        if (feedTabVisitable != null) {
            visitables.add(feedTabVisitable);
        }
        presenter.getFeedTabData();
        adapter.updateItems(visitables);
    }

    @Override
    public void addImpressionToTrackingQueue(List<TrackedVisitable> visitables) {
        List<Object> combinedTracking = new ArrayList<>();
        for (TrackedVisitable visitable : visitables) {
            if (visitable.isTrackingCombined() && visitable.getTrackingDataForCombination() != null) {
                combinedTracking.addAll(visitable.getTrackingDataForCombination());
            } else if (!visitable.isTrackingCombined() && visitable.getTrackingData() != null) {
                HomePageTracking.eventEnhancedImpressionWidgetHomePage(trackingQueue, visitable.getTrackingData());
            }
        }
        if (!combinedTracking.isEmpty()) {
            HomePageTracking.eventEnhanceImpressionLegoAndCuratedHomePage(trackingQueue, combinedTracking);
        }
    }

    @Override
    public void updateHeaderItem(HeaderViewModel headerViewModel) {
        if (adapter.getItemCount() > 1 && adapter.getItem(1) instanceof HeaderViewModel) {
            adapter.getItems().set(1, headerViewModel);
            adapter.notifyItemChanged(1);
        } else if (adapter.getItemCount() > 2 && adapter.getItem(2) instanceof HeaderViewModel) {
            adapter.getItems().set(2, headerViewModel);
            adapter.notifyItemChanged(2);
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
        if (getActivity() != null) {
            IndexScreenTracking.sendScreen(getActivity(), this::getScreenName);
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
                Intent intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL_DOMAIN,
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

    private ArrayList<ShowCaseObject> buildShowCase() {
        if (homeMainToolbar == null)
            return null;
        ArrayList<ShowCaseObject> list = new ArrayList<>();
        int statusBarHeight = ViewHelper.getStatusBarHeight(getActivity());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            statusBarHeight = 0;
        }
        list.add(new ShowCaseObject(homeMainToolbar.getBtnNotification(),
                getString(R.string.sc_notif_title),
                getString(R.string.sc_notif_desc))
        .withCustomTarget(new int[]{
                homeMainToolbar.getBtnNotification().getLeft(),
                homeMainToolbar.getBtnNotification().getTop()
                + statusBarHeight,
                homeMainToolbar.getBtnNotification().getRight(),
                homeMainToolbar.getBtnNotification().getBottom()
                + statusBarHeight
        }));
        list.add(new ShowCaseObject(homeMainToolbar.getBtnWishlist(),
                getString(R.string.sc_wishlist_title),
                getString(R.string.sc_wishlist_desc))
                .withCustomTarget(new int[]{
                homeMainToolbar.getBtnWishlist().getLeft(),
                homeMainToolbar.getBtnWishlist().getTop()
                        + statusBarHeight,
                homeMainToolbar.getBtnWishlist().getRight(),
                homeMainToolbar.getBtnWishlist().getBottom()
                        + statusBarHeight
        }));
        return list;
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
        if(getActivity() != null) {
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
            return RouteManager.getIntent(getActivity(),ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
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
    public void onTokopointCheckNowClicked(String applink) {
        if (TextUtils.isEmpty(applink)) {
            return;
        }

        if (getActivity() != null
                && RouteManager.isSupportApplink(getActivity(), applink)) {
            openApplink(applink);
        } else {
            openWebViewURL(applink, getActivity());
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
                height+=getResources().getDimensionPixelSize(R.dimen.dp_8);
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
    public void onPromoDragStart() {

    }

    @Override
    public void onPromoDragEnd() {

    }

    @Override
    public void onDynamicIconScrollStart() {

    }

    @Override
    public void onDynamicIconScrollEnd() {

    }
}
