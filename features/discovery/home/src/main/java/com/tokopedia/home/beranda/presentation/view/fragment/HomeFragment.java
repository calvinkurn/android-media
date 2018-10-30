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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.perf.metrics.Trace;
import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.HomePageTracking;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.model.LoadingModel;
import com.tokopedia.core.base.presentation.EndlessRecyclerviewListener;
import com.tokopedia.core.drawer2.data.viewmodel.TokoPointDrawerData;
import com.tokopedia.core.helper.KeyboardHelper;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.home.BrandsWebViewActivity;
import com.tokopedia.core.home.SimpleWebViewWithFilePickerActivity;
import com.tokopedia.core.home.TopPicksWebView;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.router.wallet.IWalletRouter;
import com.tokopedia.core.router.wallet.WalletRouterUtil;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.RouterUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.design.countdown.CountDownView;
import com.tokopedia.digital.common.constant.DigitalEventTracking;
import com.tokopedia.gamification.floating.view.fragment.FloatingEggButtonFragment;
import com.tokopedia.home.IHomeRouter;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.di.BerandaComponent;
import com.tokopedia.home.beranda.di.DaggerBerandaComponent;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.listener.HomeFeedListener;
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.home.beranda.presentation.view.HomeContract;
import com.tokopedia.home.beranda.presentation.view.SectionContainer;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter;
import com.tokopedia.home.beranda.presentation.view.adapter.LinearLayoutManagerWithSmoothScroller;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeAdapterFactory;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.CashBackData;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TopAdsViewModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.home.beranda.presentation.view.viewmodel.InspirationViewModel;
import com.tokopedia.home.widget.FloatingTextButton;
import com.tokopedia.loyalty.LoyaltyRouter;
import com.tokopedia.loyalty.view.activity.TokoPointWebviewActivity;
import com.tokopedia.navigation_common.listener.FragmentListener;
import com.tokopedia.navigation_common.listener.NotificationListener;
import com.tokopedia.navigation_common.listener.ShowCaseListener;
import com.tokopedia.searchbar.MainToolbar;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.tokocash.TokoCashRouter;
import com.tokopedia.tokocash.pendingcashback.domain.PendingCashback;
import com.tokopedia.tokopoints.ApplinkConstant;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;

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
        SwipeRefreshLayout.OnRefreshListener, HomeCategoryListener, HomeFeedListener,
        CountDownView.CountDownListener,
        NotificationListener, FragmentListener {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final String BERANDA_TRACE = "beranda_trace";
    private static final String MAINAPP_SHOW_REACT_OFFICIAL_STORE = "mainapp_react_show_os";
    @Inject
    HomePresenter presenter;
    private RecyclerView recyclerView;
    private TabLayout tabLayout;
    private CoordinatorLayout root;
    private SectionContainer tabContainer;
    private SwipeRefreshLayout refreshLayout;
    private HomeRecycleAdapter adapter;
    private RemoteConfig firebaseRemoteConfig;
    private Trace trace;
    private SnackbarRetry messageSnackbar;
    private String[] tabSectionTitle;
    private EndlessRecyclerviewListener feedLoadMoreTriggerListener;
    private LinearLayoutManager layoutManager;
    private FloatingTextButton floatingTextButton;
    private boolean showRecomendation;
    private boolean mShowTokopointNative;
    private boolean isVisible;
    private RecyclerView.OnScrollListener onEggScrollListener;

    private MainToolbar mainToolbar;

    public static final String SCROLL_RECOMMEND_LIST = "recommend_list";

    private boolean scrollToRecommendList = false;
    private boolean isTraceStopped = false;

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
        trace = TrackingUtils.startTrace(BERANDA_TRACE);
    }

    @Override
    protected String getScreenName() {
        return AppScreen.UnifyScreenTracker.SCREEN_UNIFY_HOME_BERANDA;
    }

    @Override
    protected void initInjector() {
        BerandaComponent component = DaggerBerandaComponent.builder().baseAppComponent(((BaseMainApplication)
                getActivity().getApplication()).getBaseAppComponent()).build();
        component.inject(this);
        component.inject(presenter);
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
        showRecomendation = firebaseRemoteConfig.getBoolean(TkpdCache.RemoteConfigKey.APP_SHOW_RECOMENDATION_BUTTON, false);
        mShowTokopointNative = firebaseRemoteConfig.getBoolean(TkpdCache.RemoteConfigKey.APP_SHOW_TOKOPOINT_NATIVE, true);
    }

    @Override
    public void showRecomendationButton() {
        if (showRecomendation && SessionHandler.isV4Login(getActivity())) {
            floatingTextButton.setVisibility(View.VISIBLE);
            HomePageTracking.eventImpressionJumpRecomendation();
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
        refreshLayout = view.findViewById(R.id.sw_refresh_layout);
        tabLayout = view.findViewById(R.id.tabs);
        tabContainer = view.findViewById(R.id.tab_container);
        floatingTextButton = view.findViewById(R.id.recom_action_button);
        root = view.findViewById(R.id.root);
        if (SessionHandler.isV4Login(getActivity())) {
            scrollToRecommendList = getArguments().getBoolean(SCROLL_RECOMMEND_LIST);
        }
        presenter.attachView(this);
        presenter.setFeedListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.onFirstLaunch();
        initTabNavigation();
        initAdapter();
        initRefreshLayout();
        initFeedLoadMoreTriggerListener();
        initEggTokenScrollListener();
        registerBroadcastReceiverTokoCash();
        fetchRemoteConfig();
        floatingTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollToRecommendList();
                HomePageTracking.eventClickJumpRecomendation();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (SessionHandler.isV4Login(getActivity()) && showRecomendation) {
                    int firstVisibleItemPos = layoutManager.findLastVisibleItemPosition();
                    Visitable visitable = adapter.getItem(firstVisibleItemPos);
                    if ((visitable instanceof InspirationViewModel
                            || visitable instanceof TopAdsViewModel)
                            || visitable instanceof LoadingModel) {
                        floatingTextButton.setVisibility(View.INVISIBLE);
                    } else {
                        floatingTextButton.setVisibility(View.VISIBLE);
                    }
                } else {
                    floatingTextButton.setVisibility(View.GONE);
                }
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

        recyclerView.smoothScrollToPosition(adapter.findFirstInspirationPosition());
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
    }

    @Override
    public void onPause() {
        super.onPause();
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
        feedLoadMoreTriggerListener = null;
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

    private void initFeedLoadMoreTriggerListener() {
        feedLoadMoreTriggerListener = new EndlessRecyclerviewListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (isAllowLoadMore()) {
                    adapter.showLoading();
                    presenter.fetchNextPageFeed();
                }
            }
        };

        if (SessionHandler.isV4Login(getContext())) {
            recyclerView.addOnScrollListener(feedLoadMoreTriggerListener);
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
        if (SessionHandler.isV4Login(getContext())) {
            recyclerView.removeOnScrollListener(onEggScrollListener);
            recyclerView.addOnScrollListener(onEggScrollListener);
        }
    }

    private FloatingEggButtonFragment getFloatingEggButtonFragment() {
        // https://stackoverflow.com/questions/28672883/java-lang-illegalstateexception-fragment-not-attached-to-activity
        if (getActivity() != null && isAdded() && getChildFragmentManager() != null) {
            return (FloatingEggButtonFragment) getChildFragmentManager().findFragmentById(R.id.floating_egg_fragment);
        }
        return null;
    }

    private boolean isAllowLoadMore() {
        return presenter.hasNextPageFeed()
                && !adapter.isLoading()
                && !adapter.isRetryShown()
                && !refreshLayout.isRefreshing()
                && !isErrorMessageShown();
    }

    private boolean isErrorMessageShown() {
        return messageSnackbar != null && messageSnackbar.isShown();
    }

    private void initAdapter() {
        layoutManager = new LinearLayoutManagerWithSmoothScroller(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.getItemAnimator().setChangeDuration(0);
        HomeAdapterFactory adapterFactory = new HomeAdapterFactory(getChildFragmentManager(), this, this, this);
        adapter = new HomeRecycleAdapter(adapterFactory, new ArrayList<Visitable>());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSectionItemClicked(String actionLink) {
        onActionLinkClicked(actionLink);
    }

    private void onGoToSell() {
        if (SessionHandler.isV2Login(getContext())) {
            String shopId = SessionHandler.getShopID(getContext());
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
        Intent intent = ((TkpdCoreRouter) getActivity().getApplication()).getLoginIntent(getContext());
        Intent intentHome = ((TkpdCoreRouter) getActivity().getApplication()).getHomeIntent(getContext());
        intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getActivity().startActivities(new Intent[]{intentHome, intent});
        getActivity().finish();
    }

    private void onGoToCreateShop() {
        Intent intent = SellerRouter.getActivityShopCreateEdit(getContext());
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
        if (getActivity().getApplication() instanceof IDigitalModuleRouter) {
            IDigitalModuleRouter digitalModuleRouter =
                    (IDigitalModuleRouter) getActivity().getApplication();
            startActivityForResult(
                    digitalModuleRouter.instanceIntentDigitalCategoryList(),
                    IDigitalModuleRouter.REQUEST_CODE_DIGITAL_CATEGORY_LIST
            );
        }

        if (getActivity() != null && getActivity().getApplication() instanceof AbstractionRouter) {
            AnalyticTracker analyticTracker = ((AbstractionRouter) getActivity().getApplication()).getAnalyticTracker();
            analyticTracker.sendEventTracking(
                    DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                    DigitalEventTracking.Category.DIGITAL_HOMEPAGE,
                    DigitalEventTracking.Action.CLICK_LIHAT_SEMUA_PRODUK,
                    DigitalEventTracking.Label.DEFAULT_EMPTY_VALUE
            );
        }
    }

    @Override
    public void openShop() {
        onGoToSell();
    }

    @Override
    public void actionAppLinkWalletHeader(String appLinkBalance) {
        WalletRouterUtil.navigateWallet(
                getActivity().getApplication(),
                this,
                IWalletRouter.DEFAULT_WALLET_APPLINK_REQUEST_CODE,
                appLinkBalance,
                "",
                new Bundle()
        );
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
                    String seamlessUrl;
                    seamlessUrl = URLGenerator.generateURLSessionLogin((Uri.encode(url)),
                            getContext());
                    if (getActivity() != null) {
                        if ((getActivity()).getApplication() instanceof TkpdCoreRouter) {
                            ((TkpdCoreRouter) (getActivity()).getApplication())
                                    .goToWallet(getActivity(), seamlessUrl);
                        }
                    }
                } else {
                    WalletRouterUtil.navigateWallet(
                            getActivity().getApplication(),
                            HomeFragment.this,
                            IWalletRouter.DEFAULT_WALLET_APPLINK_REQUEST_CODE,
                            appLink, url, new Bundle()
                    );
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
            RouterUtils.getDefaultRouter().actionAppLink(getContext(), ApplinkConstant.HOMEPAGE);
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
                && getActivity().getApplicationContext() instanceof TkpdCoreRouter
                && ((TkpdCoreRouter) getActivity().getApplicationContext()).isSupportedDelegateDeepLink(slidesModel.getApplink())) {
            openApplink(slidesModel.getApplink(), attribution);
        } else {
            openWebViewURL(slidesModel.getRedirectUrl(), getContext());
        }
        presenter.onBannerClicked(slidesModel);
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
            case IDigitalModuleRouter.REQUEST_CODE_DIGITAL_PRODUCT_DETAIL:
                if (data != null && data.hasExtra(IDigitalModuleRouter.EXTRA_MESSAGE)) {
                    String message = data.getStringExtra(IDigitalModuleRouter.EXTRA_MESSAGE);
                    if (!TextUtils.isEmpty(message)) {
                        NetworkErrorHelper.showSnackbar(getActivity(), message);
                    }
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        removeNetworkError();
        if (presenter != null) {
            resetFeedState();
            presenter.getHomeData();
            presenter.getHeaderData(false);
        }
        loadEggData();
    }

    private void resetFeedState() {
        presenter.resetPageFeed();
        if (getContext() != null && SessionHandler.isV4Login(getContext()) && feedLoadMoreTriggerListener != null) {
            feedLoadMoreTriggerListener.resetState();
        }
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
        if (trace != null && !isTraceStopped) {
            trace.stop();
            isTraceStopped = true;
        }
    }

    @Override
    public void setItems(List<Visitable> items) {
        if (items.get(0) instanceof HeaderViewModel) {
            HeaderViewModel dataHeader = (HeaderViewModel) items.get(0);
            updateHeaderItem(dataHeader);
        }
        adapter.setItems(items);
        if (scrollToRecommendList) {
            presenter.fetchNextPageFeed();
        }
    }

    @Override
    public void updateListOnResume(List<Visitable> visitables) {
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
                            root, getString(com.tokopedia.core.R.string.msg_network_error),
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
                && getActivity().getApplicationContext() instanceof TkpdCoreRouter
                && ((TkpdCoreRouter) getActivity().getApplicationContext()).isSupportedDelegateDeepLink(actionLink)) {
            openApplink(actionLink, trackingAttribution);
        } else {
            openWebViewURL(actionLink, getContext());
        }
    }

    private void openApplink(String applink) {
        openApplink(applink, "");
    }

    private void openApplink(String applink, String trackingAttribution) {
        if (!TextUtils.isEmpty(applink)) {
            applink = appendTrackerAttributionIfNeeded(applink, trackingAttribution);
            ((TkpdCoreRouter) getActivity().getApplicationContext())
                    .actionApplink(getActivity(), applink);
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

    @Override
    public void onGoToProductDetailFromInspiration(String productId,
                                                   String imageSource,
                                                   String name,
                                                   String price) {
        goToProductDetail(productId, imageSource, name, price);
    }

    private void goToProductDetail(String productId, String imageSourceSingle, String name, String price) {
        if (getActivity().getApplication() instanceof PdpRouter) {
            ((PdpRouter) getActivity().getApplication()).goToProductDetail(
                    getActivity(),
                    ProductPass.Builder.aProductPass()
                            .setProductId(productId)
                            .setProductImage(imageSourceSingle)
                            .setProductName(name)
                            .setProductPrice(price)
                            .build()
            );
        }
    }

    @Override
    public void updateCursor(String currentCursor) {
        presenter.setCursor(currentCursor);
    }

    @Override
    public void onSuccessGetFeed(ArrayList<Visitable> visitables) {
        adapter.hideLoading();
        int posStart = adapter.getItemCount();
        adapter.addItems(visitables);
        adapter.notifyItemRangeInserted(posStart, visitables.size());
        if (scrollToRecommendList) {
            scrollToRecommendList();
        }

    }

    @Override
    public void onRetryClicked() {
        if (!isErrorMessageShown()) {
            adapter.removeRetry();
            adapter.showLoading();
            presenter.fetchCurrentPageFeed();
        } else {
            onRefresh();
        }
    }

    @Override
    public void onShowRetryGetFeed() {
        if (adapter != null) {
            adapter.hideLoading();
            adapter.showRetry();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateCursorNoNextPageFeed() {
        presenter.setCursorNoNextPageFeed();
    }

    private void openWebViewBrandsURL(String url) {
        if (!url.trim().equals("")) {
            startActivity(BrandsWebViewActivity.newInstance(getActivity(), url));
        }
    }

    public void openWebViewTopPicksURL(String url) {
        if (!url.isEmpty()) {
            startActivity(TopPicksWebView.newInstance(getActivity(), url));
        }
    }

    private void openWebViewGimicURL(String url, String label, String title) {
        if (!url.equals("")) {
            Intent intent = SimpleWebViewWithFilePickerActivity.getIntentWithTitle(getActivity(), url, title);
            startActivity(intent);
            UnifyTracking.eventHomeGimmick(label);
        }
    }


    public void openWebViewURL(String url, Context context) {
        if (!TextUtils.isEmpty(url) && context != null) {
            Intent intent = new Intent(context, BannerWebView.class);
            intent.putExtra("url", url);
            context.startActivity(intent);
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
    public void onPromoScrolled(BannerSlidesModel bannerSlidesModel) {
        if (getUserVisibleHint()) {
            presenter.hitBannerImpression(bannerSlidesModel);
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        trackScreen(isVisibleToUser);
        restartBanner(isVisibleToUser);
    }

    @Override
    public void onStart() {
        super.onStart();
        sendScreen();
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
        if(getActivity() != null && getActivity().getApplication() instanceof IHomeRouter) {
            ((IHomeRouter) getActivity().getApplication()).sendIndexScreen(getActivity(), getScreenName());
        }
    }

    @Override
    public boolean isMainViewVisible() {
        return getUserVisibleHint() && isVisible && isResumed();
    }

    @Override
    public void onScrollToTop() {
        if (recyclerView != null) recyclerView.scrollToPosition(0);
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
    public Observable<TokoPointDrawerData> getTokopoint() {
        if (getActivity() != null && getActivity().getApplication() instanceof LoyaltyRouter) {
            return ((LoyaltyRouter) getActivity().getApplication()).getTokopointUseCase();
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

    @Override
    public void onNotifyBadgeNotification(int number) {
        if (mainToolbar != null) {
            mainToolbar.setNotificationNumber(number);
        }
    }

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
        if (getActivity() != null) DeepLinkChecker.openProduct(url, getActivity());
    }

    @Override
    public void showPopupIntroOvo(String applinkActivation) {
        if (RouteManager.isSupportApplink(getActivity(), applinkActivation)) {
            Intent intentBalanceWalet = RouteManager.getIntent(getActivity(), applinkActivation);
            getContext().startActivity(intentBalanceWalet);
            Activity activity = (Activity) getContext();
            activity.overridePendingTransition(R.anim.digital_slide_up_in, R.anim.digital_anim_stay);
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
}
