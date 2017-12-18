package com.tokopedia.tkpd.beranda.presentation.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.constants.HomeFragmentBroadcastReceiverConstant;
import com.tokopedia.core.constants.TokocashPendingDataBroadcastReceiverConstant;
import com.tokopedia.core.drawer.listener.TokoCashUpdateListener;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.data.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.core.drawer2.data.viewmodel.TokoPointDrawerData;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.home.BrandsWebViewActivity;
import com.tokopedia.core.home.TopPicksWebView;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.wallet.IWalletRouter;
import com.tokopedia.core.router.wallet.WalletRouterUtil;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.digital.tokocash.model.CashBackData;
import com.tokopedia.discovery.intermediary.view.IntermediaryActivity;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.di.DaggerHomeComponent;
import com.tokopedia.tkpd.beranda.di.HomeComponent;
import com.tokopedia.tkpd.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.tkpd.beranda.domain.model.brands.BrandDataModel;
import com.tokopedia.tkpd.beranda.domain.model.category.CategoryLayoutRowModel;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksItemModel;
import com.tokopedia.tkpd.beranda.listener.HomeCategoryListener;
import com.tokopedia.tkpd.beranda.listener.HomeRecycleScrollListener;
import com.tokopedia.tkpd.beranda.listener.OnSectionChangeListener;
import com.tokopedia.tkpd.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.tkpd.beranda.presentation.view.HomeContract;
import com.tokopedia.tkpd.beranda.presentation.view.SectionContainer;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.HomeRecycleAdapter;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.LinearLayoutManagerWithSmoothScroller;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.factory.HomeAdapterFactory;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.itemdecoration.VerticalSpaceItemDecoration;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.CategoryItemViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.CategorySectionViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.LayoutSections;
import com.tokopedia.tkpd.deeplink.DeepLinkDelegate;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.home.ReactNativeActivity;
import com.tokopedia.tkpdreactnative.react.ReactConst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.core.constants.HomeFragmentBroadcastReceiverConstant.EXTRA_ACTION_RECEIVER;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomeFragment extends BaseDaggerFragment implements HomeContract.View,
        SwipeRefreshLayout.OnRefreshListener, HomeCategoryListener, OnSectionChangeListener,
        TabLayout.OnTabSelectedListener, TokoCashUpdateListener {

    @Inject
    HomePresenter presenter;

    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final String MAINAPP_SHOW_REACT_OFFICIAL_STORE = "mainapp_react_show_os";
    private RecyclerView recyclerView;
    private TabLayout tabLayout;
    private CoordinatorLayout root;
    private SectionContainer tabContainer;
    private SwipeRefreshLayout refreshLayout;
    private HomeRecycleAdapter adapter;
    private RemoteConfig firebaseRemoteConfig;
    private Trace trace;
    private SnackbarRetry messageSnackbar;
    private HomeAdapterFactory adapterFactory;
    private String[] tabSectionTitle;
    private VerticalSpaceItemDecoration spaceItemDecoration;
    private HomeFragmentBroadcastReceiver homeFragmentBroadcastReceiver;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        trace = TrackingUtils.startTrace("beranda_trace");
        super.onCreate(savedInstanceState);


        homeFragmentBroadcastReceiver = new HomeFragmentBroadcastReceiver();
        getActivity().registerReceiver(
                homeFragmentBroadcastReceiver,
                new IntentFilter(
                        HomeFragmentBroadcastReceiverConstant.INTENT_ACTION
                )
        );

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        HomeComponent component = DaggerHomeComponent.builder().appComponent(getComponent(AppComponent.class)).build();
        component.inject(this);
        component.inject(presenter);
    }

    private void fetchRemoteConfig() {
        firebaseRemoteConfig = new FirebaseRemoteConfigImpl(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.sw_refresh_layout);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabContainer = (SectionContainer) view.findViewById(R.id.tab_container);
        root = (CoordinatorLayout) view.findViewById(R.id.root);
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (trace != null)
            trace.stop();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initTabNavigation();
        initAdapter();
        initRefreshLayout();
        fetchRemoteConfig();
    }

    private void initTabNavigation() {
        tabSectionTitle = getResources().getStringArray(R.array.section_title);
        TypedArray icons = getResources().obtainTypedArray(R.array.section_icon);
        for (int i = 0; i < tabSectionTitle.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setIcon(icons.getResourceId(i, R.drawable.ic_beli));
            tabLayout.addTab(tab);
        }
        tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(homeFragmentBroadcastReceiver);
        presenter.detachView();
    }

    private void initRefreshLayout() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                presenter.getHomeData();
            }
        });
        refreshLayout.setOnRefreshListener(this);
    }

    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManagerWithSmoothScroller(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapterFactory = new HomeAdapterFactory(getFragmentManager(), this);
        adapter = new HomeRecycleAdapter(adapterFactory, new ArrayList<Visitable>());
        spaceItemDecoration = new VerticalSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.margin_card_home), true, 1);
        recyclerView.addItemDecoration(spaceItemDecoration);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new HomeRecycleScrollListener(layoutManager, this));
    }

    @Override
    public void onChange(int position) {
        if (adapter.getItemCount() > position) {
            Visitable visitable = adapter.getItem(position);
            if (visitable instanceof CategoryItemViewModel) {
                tabLayout.getTabAt(((CategoryItemViewModel) visitable).getSectionId()).select();
            } else if (visitable instanceof DigitalsViewModel) {
                tabLayout.getTabAt(((DigitalsViewModel) visitable).getSectionId()).select();
            }
        }
    }

    private void toggleSectionTab(int firstPosition) {
        if (firstPosition >= 2) {
            tabContainer.setVisibility(View.VISIBLE);
        } else {
            tabContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onScrollStateChanged(int newState, int firstPosition) {
        switch (newState) {
            case RecyclerView.SCROLL_STATE_DRAGGING:
            case RecyclerView.SCROLL_STATE_SETTLING:
                tabLayout.removeOnTabSelectedListener(this);
                break;
            case RecyclerView.SCROLL_STATE_IDLE:
                tabLayout.addOnTabSelectedListener(this);
                break;
        }
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        focusView(tabSectionTitle[tab.getPosition()]);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onSectionItemClicked(LayoutSections sections, int parentPosition, int childPosition) {
        focusView(sections.getTitle());
        tabLayout.getTabAt(childPosition).select();
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
        Intent intent = SellerRouter.getAcitivityShopCreateEdit(getContext());
        intent.putExtra(SellerRouter.ShopSettingConstant.FRAGMENT_TO_SHOW,
                SellerRouter.ShopSettingConstant.CREATE_SHOP_FRAGMENT_TAG);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    private void onGoToShop(String shopId) {
        Intent intent = new Intent(getContext(), ShopInfoActivity.class);
        intent.putExtras(ShopInfoActivity.createBundle(shopId, ""));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    private void focusView(String title) {
        if (title.equalsIgnoreCase("Jual")) {
            onGoToSell();
        } else {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                Visitable visitable = adapter.getItem(i);
                if ((visitable instanceof CategoryItemViewModel && ((CategoryItemViewModel) visitable).getTitle().startsWith(title))
                        || (visitable instanceof DigitalsViewModel && ((DigitalsViewModel) visitable).getTitle().startsWith(title))) {
                    recyclerView.smoothScrollToPosition(i);
                    break;
                }
            }
        }
    }

    @Override
    public void onMarketPlaceItemClicked(CategoryLayoutRowModel data, int parentPosition, int childPosition) {
        TrackingUtils.sendMoEngageClickMainCategoryIcon(data.getName());
        openActivity(String.valueOf(data.getCategoryId()), data.getName());
    }

    @Override
    public void onDigitalItemClicked(CategoryLayoutRowModel data, int parentPosition, int childPosition) {
        presenter.onDigitalItemClicked(data, parentPosition, childPosition);

    }

    @Override
    public void onGimickItemClicked(CategoryLayoutRowModel data, int parentPosition, int childPosition) {
        String redirectUrl = data.getUrl();
        if (redirectUrl != null && redirectUrl.length() > 0) {
            String resultGenerateUrl = URLGenerator.generateURLSessionLogin(
                    Uri.encode(redirectUrl), MainApplication.getAppContext());
            openWebViewGimicURL(resultGenerateUrl, data.getUrl(), data.getName());
        }
    }

    @Override
    public void onApplinkClicked(CategoryLayoutRowModel data, int parentPosition, int childPosition) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(data.getApplinks()));
        DeepLinkDelegate delegate = DeeplinkHandlerActivity.getDelegateInstance();
        delegate.dispatchFrom(getActivity(), intent);
    }

    @Override
    public void onTopPicksItemClicked(TopPicksItemModel data, int parentPosition, int childPosition) {
        String url = data.getUrl();
        UnifyTracking.eventHomeTopPicksItem(data.getName(), data.getName());
        switch ((DeepLinkChecker.getDeepLinkType(url))) {
            case DeepLinkChecker.BROWSE:
                DeepLinkChecker.openBrowse(url, getActivity());
                break;
            case DeepLinkChecker.HOT:
                DeepLinkChecker.openHot(url, getActivity());
                break;
            case DeepLinkChecker.CATALOG:
                DeepLinkChecker.openCatalog(url, getActivity());
                break;
            default:
                openWebViewTopPicksURL(url);
        }
    }

    @Override
    public void onTopPicksMoreClicked(String url, int pos) {
        openWebViewTopPicksURL(url);
    }

    @Override
    public void onBrandsItemClicked(BrandDataModel data, int parentPosition, int childPosition) {
        UnifyTracking.eventClickOfficialStore(AppEventTracking.EventLabel.OFFICIAL_STORE + data.getShopName());
        Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
        intent.putExtras(ShopInfoActivity.createBundle(String.valueOf(data.getShopId()), ""));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    @Override
    public void onBrandsMoreClicked(int pos) {
        if (SessionHandler.isV4Login(getContext())) {
            UnifyTracking.eventViewAllOSLogin();
        } else {
            UnifyTracking.eventViewAllOSNonLogin();
        }

        if (firebaseRemoteConfig.getBoolean(MAINAPP_SHOW_REACT_OFFICIAL_STORE)) {
            getActivity().startActivity(
                    ReactNativeActivity.createOfficialStoresReactNativeActivity(
                            getActivity(), ReactConst.Screen.OFFICIAL_STORE,
                            getString(R.string.react_native_banner_official_title)
                    )
            );
        } else {
            openWebViewBrandsURL(TkpdBaseURL.OfficialStore.URL_WEBVIEW);
        }
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
    }

    @Override
    public void openShop() {
        onGoToSell();
    }

    @Override
    public void actionAppLinkWalletHeader(String redirectUrlBalance, String appLinkBalance) {
        WalletRouterUtil.navigateWallet(
                getActivity().getApplication(),
                this,
                IWalletRouter.DEFAULT_WALLET_APPLINK_REQUEST_CODE,
                appLinkBalance,
                redirectUrlBalance,
                new Bundle()
        );
    }

    @Override
    public void onRequestPendingCashBack() {
        getActivity().sendBroadcast(new Intent(TokocashPendingDataBroadcastReceiverConstant.INTENT_ACTION));
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
                .setImg(R.drawable.group_2)
                .setUrlButton(redirectUrlActionButton,
                        appLinkActionButton,
                        getString(R.string.toko_cash_pending_proceed_button))
                .build());
        bottomSheetDialogTokoCash.show();
    }

    @Override
    public void actionTokoPointClicked(String tokoPointUrl, String pageTitle) {
        if (TextUtils.isEmpty(pageTitle))
            startActivity(BannerWebView.getCallingIntent(getActivity(), tokoPointUrl));
        else
            startActivity(BannerWebView.getCallingIntentWithTitle(getActivity(), tokoPointUrl, pageTitle));
    }

    @Override
    public void onPromoClick(BannerSlidesModel slidesModel) {
        if (getActivity() != null
                && getActivity().getApplicationContext() instanceof IDigitalModuleRouter
                && ((IDigitalModuleRouter) getActivity().getApplicationContext()).isSupportedDelegateDeepLink(slidesModel.getApplink())) {
            ((IDigitalModuleRouter) getActivity().getApplicationContext())
                    .actionNavigateByApplinksUrl(getActivity(), slidesModel.getApplink(), new Bundle());
        } else {

            String url = slidesModel.getRedirectUrl();
            try {
                UnifyTracking.eventSlideBannerClicked(url);
                Uri uri = Uri.parse(url);
                String host = uri.getHost();
                List<String> linkSegment = uri.getPathSegments();
                if (isBaseHost(host) && isShop(linkSegment)) {
                    String shopDomain = linkSegment.get(0);
                    presenter.getShopInfo(url, shopDomain);
                } else if (isBaseHost(host) && isProduct(linkSegment)) {
                    String shopDomain = linkSegment.get(0);
                    presenter.openProductPageIfValid(url, shopDomain);
                } else if (DeepLinkChecker.getDeepLinkType(url) == DeepLinkChecker.CATEGORY) {
                    DeepLinkChecker.openCategory(url, getActivity());
                } else {
                    openWebViewURL(url, getActivity());
                }
            } catch (Exception e) {
                openWebViewURL(url, getActivity());
                e.printStackTrace();
            }
        }
    }

    private boolean isBaseHost(String host) {
        return (host.contains(TkpdBaseURL.BASE_DOMAIN) || host.contains(TkpdBaseURL.MOBILE_DOMAIN));
    }

    private boolean isShop(List<String> linkSegment) {
        return linkSegment.size() == 1
                && !isReservedLink(linkSegment.get(0));
    }

    private boolean isProduct(List<String> linkSegment) {
        return linkSegment.size() == 2
                && !isReservedLink(linkSegment.get(0));
    }

    private boolean isReservedLink(String link) {
        return link.equals("pulsa")
                || link.equals("iklan")
                || link.equals("newemail.pl")
                || link.equals("search")
                || link.equals("hot")
                || link.equals("about")
                || link.equals("reset.pl")
                || link.equals("activation.pl")
                || link.equals("privacy.pl")
                || link.equals("terms.pl")
                || link.equals("p")
                || link.equals("catalog")
                || link.equals("toppicks")
                || link.equals("promo")
                || link.startsWith("invoice.pl");
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
        presenter.getHomeData();
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
    public void setItems(List<Visitable> items) {
        spaceItemDecoration.setStart(lastIndexOfInstance(items, CategorySectionViewModel.class));
        recyclerView.invalidateItemDecorations();
        adapter.setItems(items);
    }

    public int lastIndexOfInstance(List list, Class clazz) {
        for (int i = 0; i < list.size(); i++) {
            if (clazz.isInstance(list.get(i))) {
                if (i > 0)
                    return i - 1;
            }
        }
        return 0;
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
        if (adapter.getItemCount() > 0) {
            if (messageSnackbar == null) {
                messageSnackbar = NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.getHomeData();
                    }
                });
            }
            messageSnackbar.showRetrySnackbar();
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(), root, message,
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.getHomeData();
                        }
                    });
        }
    }

    @Override
    public void removeNetworkError() {
        NetworkErrorHelper.removeEmptyState(root);
        if (messageSnackbar != null && messageSnackbar.isShown()) {
            messageSnackbar.hideRetrySnackbar();
        }
    }

    private void openActivity(String depID, String title) {
        IntermediaryActivity.moveTo(
                getActivity(),
                depID,
                title
        );
        Map<String, String> values = new HashMap<>();
        values.put(getString(R.string.value_category_name), title);
        UnifyTracking.eventHomeCategory(title);
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
            Intent intent = new Intent(getActivity(), BannerWebView.class);
            intent.putExtra("url", url);
            intent.putExtra(BannerWebView.EXTRA_TITLE, title);
            startActivity(intent);
            UnifyTracking.eventHomeGimmick(label);
        }
    }

    public void openWebViewURL(String url, Context context) {
        if (url != "" && context != null) {
            Intent intent = new Intent(context, BannerWebView.class);
            intent.putExtra("url", url);
            context.startActivity(intent);
        }
    }

    @Override
    public void onReceivedTokoCashData(DrawerTokoCash tokoCashData) {
        presenter.updateHeaderTokoCashData(tokoCashData.getHomeHeaderWalletAction());
    }

    @Override
    public void onTokoCashDataError(String errorMessage) {
        Log.e(TAG, errorMessage);
    }

    public class HomeFragmentBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!HomeFragmentBroadcastReceiverConstant.INTENT_ACTION.equalsIgnoreCase(intent.getAction()))
                return;
            switch (intent.getIntExtra(EXTRA_ACTION_RECEIVER, 0)) {
                case HomeFragmentBroadcastReceiverConstant.ACTION_RECEIVER_RECEIVED_TOKOPOINT_DATA:
                    TokoPointDrawerData tokoPointDrawerData = intent.getParcelableExtra(
                            HomeFragmentBroadcastReceiverConstant.EXTRA_TOKOPOINT_DRAWER_DATA
                    );
                    if (tokoPointDrawerData == null) return;
                    presenter.updateHeaderTokoPointData(tokoPointDrawerData);
                    break;
                case HomeFragmentBroadcastReceiverConstant.ACTION_RECEIVER_RECEIVED_TOKOCASH_DATA:
                    HomeHeaderWalletAction homeHeaderWalletAction = intent.getParcelableExtra(
                            HomeFragmentBroadcastReceiverConstant.EXTRA_TOKOCASH_DRAWER_DATA
                    );
                    if (homeHeaderWalletAction == null) return;
                    presenter.updateHeaderTokoCashData(homeHeaderWalletAction);
                    break;
                case HomeFragmentBroadcastReceiverConstant.ACTION_RECEIVER_RECEIVED_TOKOCASH_PENDING_DATA:
                    CashBackData cashBackData = intent.getParcelableExtra(
                            HomeFragmentBroadcastReceiverConstant.EXTRA_TOKOCASH_PENDING_DATA
                    );
                    if (cashBackData == null) return;
                    presenter.updateHeaderTokoCashPendingData(cashBackData);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getView() != null) {
            restartBanner();
        }
    }

    private void restartBanner() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean isMainViewVisible() {
        return getUserVisibleHint();
    }

}
