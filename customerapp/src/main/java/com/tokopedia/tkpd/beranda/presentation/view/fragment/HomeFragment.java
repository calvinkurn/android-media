package com.tokopedia.tkpd.beranda.presentation.view.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.perf.metrics.Trace;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.drawer.listener.TokoCashUpdateListener;
import com.tokopedia.core.drawer.receiver.TokoCashBroadcastReceiver;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerWalletAction;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.home.BrandsWebViewActivity;
import com.tokopedia.core.home.TopPicksWebView;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.core.router.wallet.IWalletRouter;
import com.tokopedia.core.router.wallet.WalletRouterUtil;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.product.activity.DigitalProductActivity;
import com.tokopedia.discovery.intermediary.view.IntermediaryActivity;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.di.DaggerHomeComponent;
import com.tokopedia.tkpd.beranda.di.HomeComponent;
import com.tokopedia.tkpd.beranda.domain.model.brands.BrandDataModel;
import com.tokopedia.tkpd.beranda.domain.model.category.CategoryLayoutRowModel;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksItemModel;
import com.tokopedia.tkpd.beranda.listener.HomeCategoryListener;
import com.tokopedia.tkpd.beranda.listener.OnSectionChangeListener;
import com.tokopedia.tkpd.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.tkpd.beranda.presentation.view.HomeContract;
import com.tokopedia.tkpd.beranda.presentation.view.SectionContainer;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.HomeRecycleAdapter;
import com.tokopedia.tkpd.beranda.listener.HomeRecycleScrollListener;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.LinearLayoutManagerWithSmoothScroller;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.itemdecoration.VerticalSpaceItemDecoration;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.factory.HomeAdapterFactory;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.CategoryItemViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.LayoutSections;
import com.tokopedia.tkpd.deeplink.DeepLinkDelegate;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.home.ReactNativeActivity;
import com.tokopedia.tkpd.remoteconfig.RemoteConfigFetcher;
import com.tokopedia.tkpdreactnative.react.ReactConst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomeFragment extends BaseDaggerFragment implements HomeContract.View,
        SwipeRefreshLayout.OnRefreshListener, HomeCategoryListener, TokoCashUpdateListener,
        OnSectionChangeListener, TabLayout.OnTabSelectedListener {

    @Inject
    HomePresenter presenter;

    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final String MAINAPP_SHOW_REACT_OFFICIAL_STORE = "mainapp_react_show_os";
    private RecyclerView recyclerView;
    private TabLayout tabLayout;
    private SectionContainer tabContainer;
    private SwipeRefreshLayout refreshLayout;
    private HomeRecycleAdapter adapter;
    private FirebaseRemoteConfig firebaseRemoteConfig;
    private DrawerTokoCash tokoCashData;
    private Trace trace;
    private TokoCashBroadcastReceiver tokoCashBroadcastReceiver;
    private IntentFilter intentFilerTokoCash;
    private SnackbarRetry messageSnackbar;
    private HomeAdapterFactory adapterFactory;
    private String[] tabSectionTitle;

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
        RemoteConfigFetcher remoteConfigFetcher = new RemoteConfigFetcher(getActivity());
        remoteConfigFetcher.fetch(new RemoteConfigFetcher.Listener() {
            @Override
            public void onComplete(FirebaseRemoteConfig remoteConfig) {
                firebaseRemoteConfig = remoteConfig;
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.sw_refresh_layout);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabContainer = (SectionContainer) view.findViewById(R.id.tab_container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
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
        initTokoCashReceiver();
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

    private void initTokoCashReceiver() {
        tokoCashBroadcastReceiver = new TokoCashBroadcastReceiver(this);
        intentFilerTokoCash = new IntentFilter(TokoCashBroadcastReceiver.ACTION_GET_TOKOCASH);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(tokoCashBroadcastReceiver, intentFilerTokoCash);
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(tokoCashBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.margin_card_home)));
        recyclerView.addOnScrollListener(new HomeRecycleScrollListener(layoutManager, this));
    }

    @Override
    public void onChange(int firstPosition) {
        toggleSectionTab(firstPosition);
        Visitable visitable = adapter.getItem(firstPosition);
        if (visitable instanceof CategoryItemViewModel) {
            tabLayout.getTabAt(((CategoryItemViewModel) visitable).getSectionId()).select();
        } else if (visitable instanceof DigitalsViewModel) {
            tabLayout.getTabAt(((DigitalsViewModel) visitable).getSectionId()).select();
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
                toggleSectionTab(firstPosition);
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

    private void focusView(String title) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) refreshLayout.getLayoutParams();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            Visitable visitable = adapter.getItem(i);
            if ((visitable instanceof CategoryItemViewModel && ((CategoryItemViewModel) visitable).getTitle().startsWith(title))
                    || (visitable instanceof DigitalsViewModel && ((DigitalsViewModel) visitable).getTitle().startsWith(title))) {
                recyclerView.smoothScrollToPosition(i);
                break;
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
        UnifyTracking.eventClickCategoriesIcon(data.getName());

        if (String.valueOf(data.getCategoryId()).equalsIgnoreCase("103") && tokoCashData != null
                && tokoCashData.getDrawerWalletAction().getTypeAction()
                != DrawerWalletAction.TYPE_ACTION_BALANCE) {
            WalletRouterUtil.navigateWallet(
                    getActivity().getApplication(),
                    this,
                    IWalletRouter.DEFAULT_WALLET_APPLINK_REQUEST_CODE,
                    tokoCashData.getDrawerWalletAction().getAppLinkActionButton(),
                    tokoCashData.getDrawerWalletAction().getRedirectUrlActionButton(),
                    new Bundle()
            );
        } else {
            if (getActivity() != null && ((TkpdCoreRouter) getActivity().getApplication())
                    .isSupportedDelegateDeepLink(data.getApplinks())) {
                DigitalCategoryDetailPassData passData = new DigitalCategoryDetailPassData.Builder()
                        .appLinks(data.getApplinks())
                        .categoryId(String.valueOf(data.getCategoryId()))
                        .categoryName(data.getName())
                        .url(data.getUrl())
                        .build();
                Bundle bundle = new Bundle();
                bundle.putParcelable(DigitalProductActivity.EXTRA_CATEGORY_PASS_DATA, passData);
                Intent intent = new Intent(getActivity(), DeeplinkHandlerActivity.class);
                intent.putExtras(bundle);
                intent.setData(Uri.parse(data.getApplinks()));
                startActivity(intent);
            } else {
                onGimickItemClicked(data, parentPosition, childPosition);
            }
        }

        TrackingUtils.sendMoEngageClickMainCategoryIcon(data.getName());
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

        if (firebaseRemoteConfig != null
                && firebaseRemoteConfig.getBoolean(MAINAPP_SHOW_REACT_OFFICIAL_STORE)) {
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
    public void onCloseTicker(int pos) {

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
    public void onReceivedTokoCashData(final DrawerTokoCash data) {
//        holder.tokoCashHeaderView.setVisibility(View.VISIBLE);
        final FirebaseRemoteConfig config = RemoteConfigFetcher.initRemoteConfig(getActivity());
        if (config != null) {
            config.setDefaults(R.xml.remote_config_default);
            config.fetch().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        config.activateFetched();
//                        holder.tokoCashHeaderView.renderData(data, config
//                                .getBoolean("toko_cash_top_up"), config.getString("toko_cash_label"));
                        tokoCashData = data;
                    }
                }
            });
        }
//        holder.tokoCashHeaderView.renderData(data, false, getActivity()
//                .getString(R.string.tokocash));
        tokoCashData = data;
    }

    @Override
    public void onTokoCashDataError(String errorMessage) {

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
        adapter.setItems(items);
    }

    @Override
    public void showNetworkError() {
        if (messageSnackbar == null) {
            messageSnackbar = NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.getHomeData();
                }
            });
        }
        messageSnackbar.showRetrySnackbar();
    }

    @Override
    public void removeNetworkError() {

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
}
