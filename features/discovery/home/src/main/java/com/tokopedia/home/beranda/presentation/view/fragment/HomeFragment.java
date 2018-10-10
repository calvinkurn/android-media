package com.tokopedia.home.beranda.presentation.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.perf.metrics.Trace;
import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.model.LoadingModel;
import com.tokopedia.core.base.presentation.EndlessRecyclerviewListener;
import com.tokopedia.core.drawer.listener.TokoCashUpdateListener;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.design.keyboard.KeyboardHelper;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.design.countdown.CountDownView;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.gamification.floating.view.fragment.FloatingEggButtonFragment;
import com.tokopedia.home.IHomeRouter;
import com.tokopedia.home.R;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData;
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
import com.tokopedia.home.beranda.presentation.view.viewmodel.InspirationViewModel;
import com.tokopedia.home.constant.ConstantKey;
import com.tokopedia.home.widget.FloatingTextButton;
import com.tokopedia.loyalty.view.activity.TokoPointWebviewActivity;
import com.tokopedia.navigation_common.listener.FragmentListener;
import com.tokopedia.navigation_common.listener.NotificationListener;
import com.tokopedia.navigation_common.listener.ShowCaseListener;
import com.tokopedia.searchbar.MainToolbar;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.tokocash.TokoCashRouter;
import com.tokopedia.tokocash.pendingcashback.domain.PendingCashback;
import com.tokopedia.tokocash.pendingcashback.receiver.TokocashPendingDataBroadcastReceiver;
import com.tokopedia.tokopoints.ApplinkConstant;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.user.session.UserSession;

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
        TokoCashUpdateListener, HomeFeedListener, CountDownView.CountDownListener,
        NotificationListener, FragmentListener {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final String MAINAPP_SHOW_REACT_OFFICIAL_STORE = "mainapp_react_show_os";

    @Inject
    HomePresenter presenter;

    @Inject
    UserSession userSession;

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
    private RecyclerView.OnScrollListener onEggScrollListener;

    private MainToolbar mainToolbar;

    public static final String SCROLL_RECOMMEND_LIST = "recommend_list";

    private boolean scrollToRecommendList = false;
    private boolean isTraceStopped = false;

    public static HomeFragment newInstance(boolean scrollToRecommendList) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putBoolean(SCROLL_RECOMMEND_LIST,scrollToRecommendList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trace = TrackingUtils.startTrace("beranda_trace");
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
        showRecomendation = firebaseRemoteConfig.getBoolean(ConstantKey.RemoteConfigKey.APP_SHOW_RECOMENDATION_BUTTON, false);
        mShowTokopointNative = firebaseRemoteConfig.getBoolean(ConstantKey.RemoteConfigKey.APP_SHOW_TOKOPOINT_NATIVE, true);
    }

    @Override
    public void showRecomendationButton() {
        if (showRecomendation && isUserLoggedIn()) {
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
        refreshLayout = view.findViewById(R.id.sw_refresh_layout);
        tabLayout = view.findViewById(R.id.tabs);
        tabContainer = view.findViewById(R.id.tab_container);
        floatingTextButton = view.findViewById(R.id.recom_action_button);
        root = view.findViewById(R.id.root);
        if(isUserLoggedIn()) {
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
                HomePageTracking.eventClickJumpRecomendation(getActivity());
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isUserLoggedIn() && showRecomendation) {
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

        if (isUserLoggedIn()) {
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
        if (isUserLoggedIn()) {
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
        if (getActivity().getApplication() instanceof DigitalModuleRouter) {
            DigitalModuleRouter digitalModuleRouter =
                    (DigitalModuleRouter) getActivity().getApplication();
            startActivityForResult(
                    digitalModuleRouter.instanceIntentDigitalCategoryList(),
                    DigitalModuleRouter.REQUEST_CODE_DIGITAL_CATEGORY_LIST
            );
        }
    }

    @Override
    public void openShop() {
        onGoToSell();
    }

    @Override
    public void actionAppLinkWalletHeader(String redirectUrlBalance, String appLinkBalance) {
        if ((getActivity()).getApplication() instanceof IHomeRouter) {
            ((IHomeRouter) (getActivity()).getApplication())
                    .goToTokoCash(appLinkBalance,
                            redirectUrlBalance,
                            getActivity());
        }
    }

    @Override
    public void onRequestPendingCashBack() {
        presenter.getTokocashPendingBalance();
    }

    @Override
    public void actionInfoPendingCashBackTokocash(CashBackData cashBackData,
                                                  String redirectUrlActionButton,
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
                                    .goToWalletFromHome(getActivity(), url);
                        }
                    }
                } else {
                    if ((getActivity()).getApplication() instanceof IHomeRouter) {
                        ((IHomeRouter) (getActivity()).getApplication())
                                .goToTokoCash(appLink,
                                        url,
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
                .setUrlButton(redirectUrlActionButton,
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
    public void onCloseTicker(int pos) {
        adapter.getItems().remove(pos);
        adapter.notifyItemRemoved(pos);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DigitalModuleRouter.REQUEST_CODE_DIGITAL_PRODUCT_DETAIL:
                if (data != null && data.hasExtra(DigitalModuleRouter.EXTRA_MESSAGE)) {
                    String message = data.getStringExtra(DigitalModuleRouter.EXTRA_MESSAGE);
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
        if (getContext() != null && isUserLoggedIn() && feedLoadMoreTriggerListener != null) {
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
        if(scrollToRecommendList) {
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
                            getActivity(), getString(com.tokopedia.core.R.string.msg_network_error),
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

    @Override
    public void onGoToProductDetailFromInspiration(String productId,
                                                   String imageSource,
                                                   String name,
                                                   String price) {
        goToProductDetail(productId, imageSource, name, price);
    }

    private void goToProductDetail(String productId, String imageSourceSingle, String name, String price) {
        if (getActivity().getApplication() instanceof IHomeRouter) {
            ((IHomeRouter) getActivity().getApplication()).goToProductDetail(
                    getActivity(),
                    productId,
                    imageSourceSingle,
                    name,
                    price
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
        if(scrollToRecommendList) {
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
    public void onReceivedTokoCashData(DrawerTokoCash tokoCashData) {
        presenter.updateHeaderTokoCashData(tokoCashData.getHomeHeaderWalletAction());
    }

    @Override
    public void onTokoCashDataError(String errorMessage) {
        Log.e(TAG, errorMessage);
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
        if(isVisible()) {
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
        ScreenTracking.screen(getScreenName());
    }

    private void restartBanner(boolean isVisibleToUser) {
        if ((isVisibleToUser && getView() != null) && adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void trackScreen(boolean isVisibleToUser) {
        if (isVisibleToUser && isAdded() && getActivity() != null) {
            ScreenTracking.screen(getScreenName());
        }
    }

    @Override
    public boolean isMainViewVisible() {
        return getUserVisibleHint();
    }

    @Override
    public void onScrollToTop() {
        if (recyclerView != null) recyclerView.scrollToPosition(0);
    }

    /**
     * Tokocash & Tokopoint
     */
    @Override
    public Observable<TokoCashData> getTokocashBalance() {
        if (getActivity() != null && getActivity().getApplication() instanceof TkpdCoreRouter) {
            return ((TkpdCoreRouter) getActivity().getApplication()).getTokoCashBalance();
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
                new IntentFilter(TokocashPendingDataBroadcastReceiver.class.getSimpleName())
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
                String data = extras.getString(TokocashPendingDataBroadcastReceiver.class.getSimpleName());
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
        if(getActivity() != null){
            if ((getActivity()).getApplication() instanceof IHomeRouter) {
                ((IHomeRouter) (getActivity()).getApplication())
                        .goToProductDetail(
                                getActivity(),
                                url);
            }
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

    private boolean isUserLoggedIn(){
        return userSession.isLoggedIn();
    }

    private String getUserShopId(){
        return userSession.getShopId();
    }
}
