package com.tokopedia.tkpd.home.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.perf.metrics.Trace;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer.listener.TokoCashUpdateListener;
import com.tokopedia.core.drawer.receiver.TokoCashBroadcastReceiver;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashModel;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerWalletAction;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.home.BrandsWebViewActivity;
import com.tokopedia.core.home.TopPicksWebView;
import com.tokopedia.core.home.customview.TokoCashHeaderView;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.entity.home.Banner;
import com.tokopedia.core.network.entity.home.Brand;
import com.tokopedia.core.network.entity.home.Brands;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.core.network.entity.homeMenu.CategoryItemModel;
import com.tokopedia.core.network.entity.homeMenu.CategoryMenuModel;
import com.tokopedia.core.network.entity.topPicks.Item;
import com.tokopedia.core.network.entity.topPicks.Toppick;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.wallet.IWalletRouter;
import com.tokopedia.core.router.wallet.WalletRouterUtil;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.NonScrollGridLayoutManager;
import com.tokopedia.core.util.NonScrollLinearLayoutManager;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.TokoCashUtil;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.widgets.DividerItemDecoration;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.digital.apiservice.DigitalEndpointService;
import com.tokopedia.digital.product.activity.DigitalProductActivity;
import com.tokopedia.digital.tokocash.model.CashBackData;
import com.tokopedia.digital.widget.compoundview.WidgetClientNumberView;
import com.tokopedia.digital.widget.data.mapper.FavoriteNumberListDataMapper;
import com.tokopedia.digital.widget.domain.DigitalWidgetRepository;
import com.tokopedia.digital.widget.model.mapper.CategoryMapper;
import com.tokopedia.digital.widget.model.mapper.StatusMapper;
import com.tokopedia.discovery.intermediary.view.IntermediaryActivity;
import com.tokopedia.tkpd.BuildConfig;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.deeplink.DeepLinkDelegate;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.home.HomeCatMenuView;
import com.tokopedia.tkpd.home.OnGetBrandsListener;
import com.tokopedia.tkpd.home.ReactNativeActivity;
import com.tokopedia.tkpd.home.TopPicksView;
import com.tokopedia.tkpd.home.adapter.BrandsRecyclerViewAdapter;
import com.tokopedia.tkpd.home.adapter.RecyclerViewCategoryMenuAdapter;
import com.tokopedia.tkpd.home.adapter.SectionListCategoryAdapter;
import com.tokopedia.tkpd.home.adapter.TickerAdapter;
import com.tokopedia.tkpd.home.adapter.TopPicksAdapter;
import com.tokopedia.tkpd.home.adapter.TopPicksItemAdapter;
import com.tokopedia.tkpd.home.customview.BannerView;
import com.tokopedia.tkpd.home.customview.DigitalWidgetView;
import com.tokopedia.tkpd.home.facade.FacadePromo;
import com.tokopedia.tkpd.home.presenter.BrandsPresenter;
import com.tokopedia.tkpd.home.presenter.BrandsPresenterImpl;
import com.tokopedia.tkpd.home.presenter.Category;
import com.tokopedia.tkpd.home.presenter.CategoryImpl;
import com.tokopedia.tkpd.home.presenter.CategoryView;
import com.tokopedia.tkpd.home.presenter.HomeCatMenuPresenter;
import com.tokopedia.tkpd.home.presenter.HomeCatMenuPresenterImpl;
import com.tokopedia.tkpd.home.presenter.TokoCashPresenter;
import com.tokopedia.tkpd.home.presenter.TokoCashPresenterImpl;
import com.tokopedia.tkpd.home.presenter.TopPicksPresenter;
import com.tokopedia.tkpd.home.presenter.TopPicksPresenterImpl;
import com.tokopedia.tkpd.home.recharge.interactor.RechargeNetworkInteractorImpl;
import com.tokopedia.tkpd.home.recharge.presenter.RechargeCategoryPresenter;
import com.tokopedia.tkpd.home.recharge.presenter.RechargeCategoryPresenterImpl;
import com.tokopedia.tkpd.home.recharge.view.RechargeCategoryView;
import com.tokopedia.tkpd.remoteconfig.RemoteConfigFetcher;
import com.tokopedia.tkpdreactnative.react.ReactConst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Nisie on 1/07/15.
 * modified by mady add feature Recharge and change home menu
 * modified by alifa add Top Picks, ticker enhancement
 */


public class FragmentIndexCategory extends TkpdBaseV4Fragment implements
        CategoryView, RechargeCategoryView, SectionListCategoryAdapter.OnCategoryClickedListener,
        SectionListCategoryAdapter.OnGimmicClickedListener, HomeCatMenuView, TopPicksView,
        TopPicksItemAdapter.OnTitleClickedListener, TopPicksItemAdapter.OnItemClickedListener,
        TopPicksAdapter.OnClickViewAll, TickerAdapter.OnTickerClosed, TokoCashUpdateListener,
        TokoCashHeaderView.ActionListener,
        SectionListCategoryAdapter.OnApplinkClickedListener {

    private static final long SLIDE_DELAY = 5000;
    private static final long TICKER_DELAY = 5000;
    public static final String TAG = FragmentIndexCategory.class.getSimpleName();
    private static final String TOP_PICKS_URL = "https://www.tokopedia.com/toppicks/";
    private static final String MAINAPP_SHOW_REACT_OFFICIAL_STORE = "mainapp_react_show_os";

    private ViewHolder holder;

    private Runnable tickerIncrementPage;
    private Handler tickerHandler;
    Category category;
    private RechargeCategoryPresenter rechargeCategoryPresenter;
    TickerAdapter tickerAdapter;
    private int currentTicker = 0;
    ArrayList<Ticker.Tickers> tickers = new ArrayList<>();
    ArrayList<Ticker.Tickers> tickerShowed = new ArrayList<>();
    public static final String BANNER_RECEIVER_INTENT = BuildConfig.APPLICATION_ID + ".BANNER_RECEIVER_INTENT";
    private HomeCatMenuPresenter homeCatMenuPresenter;
    private TopPicksPresenter topPicksPresenter;
    private RecyclerViewCategoryMenuAdapter recyclerViewCategoryMenuAdapter;
    private TopPicksAdapter topPicksAdapter;
    private BrandsRecyclerViewAdapter brandsRecyclerViewAdapter;
    private BrandsPresenter brandsPresenter;
    private TokoCashPresenter tokoCashPresenter;
    private SnackbarRetry messageSnackbar;
    private TokoCashBroadcastReceiver tokoCashBroadcastReceiver;
    private BottomSheetView bottomSheetDialogTokoCash;
    private Trace trace;
    private IntentFilter intentFilerTokoCash;

    private DrawerTokoCash tokoCashData;

    FirebaseRemoteConfig firebaseRemoteConfig;

    @Override
    public void onRequestPendingCashBack() {
        tokoCashPresenter.onRequestCashBackPending();
    }

    @Override
    public void onShowTokoCashBottomSheet() {
        bottomSheetDialogTokoCash.show();
    }

    @Override
    public void actionAppLinkWalletHeader(String redirectUrl, String appLinkScheme) {
        WalletRouterUtil.navigateWallet(
                getActivity().getApplication(),
                this,
                IWalletRouter.DEFAULT_WALLET_APPLINK_REQUEST_CODE,
                appLinkScheme,
                redirectUrl,
                new Bundle()
        );
    }

    private class ViewHolder {
        private View MainView;

        private TokoCashHeaderView tokoCashHeaderView;
        RecyclerView tickerContainer;
        NestedScrollView wrapperScrollview;
        RecyclerView categoriesRecylerview;
        RecyclerView topPicksRecyclerView;
        CardView cardBrandLayout;
        RecyclerView brandsRecyclerView;
        RelativeLayout rlBrands;
        TextView textViewAllBrands;
        LinearLayout wrapperLinearLayout;
        DigitalWidgetView digitalWidgetView;
        BannerView bannerView;

        private ViewHolder() {
        }

    }

    public FragmentIndexCategory() {

    }

    public static FragmentIndexCategory newInstance() {
        return new FragmentIndexCategory();
    }

    private class Model {
        private ArrayList<FacadePromo.PromoItem> listBanner = new ArrayList<>();

        private Model() {
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        trace = TrackingUtils.startTrace("beranda_trace");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_HOME_PRODUCT_CATEGORY;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initVar();
        category.subscribe();
        initView(inflater, container);
        prepareView();
        initData();

        return holder.MainView;
    }

    private void initData() {

        loadDummyPromos();
        rechargeCategoryPresenter.fetchDataRechargeCategory();

        getAnnouncement();
        getPromo();
        homeCatMenuPresenter.fetchHomeCategoryMenu(false);
        topPicksPresenter.fetchTopPicks();
        brandsPresenter.fetchBrands();
        fetchRemoteConfig();
    }

    private void loadDummyPromos() {
        List<FacadePromo.PromoItem> dummyPromoList = new ArrayList<>();
        dummyPromoList.add(new FacadePromo.PromoItem());
        setBanner(dummyPromoList);
    }

    private void fetchRemoteConfig() {
        RemoteConfigFetcher remoteConfigFetcher = new RemoteConfigFetcher(getActivity());
        remoteConfigFetcher.fetch(new RemoteConfigFetcher.Listener() {
            @Override
            public void onComplete(FirebaseRemoteConfig firebaseRemoteConfig) {
                FragmentIndexCategory.this.firebaseRemoteConfig = firebaseRemoteConfig;
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }


    private void getAnnouncement() {
        if (!category.isTickerClosed()) {
            category.fetchTickers(new Category.FetchTickersListener() {
                @Override
                public void onSuccess(final ArrayList<Ticker.Tickers> tickersResponse) {
                    holder.tickerContainer.setVisibility(View.VISIBLE);
                    if (tickersResponse.size() > 1) {
                        tickerShowed.clear();
                        tickerIncrementPage = runnableIncrementTicker();
                        tickerShowed.clear();
                        tickerHandler = new Handler();
                        holder.tickerContainer.setVisibility(View.VISIBLE);
                        tickers.addAll(tickersResponse);
                        tickerShowed.add(tickers.get(currentTicker));
                        tickerAdapter.addItem(tickerShowed);
                        startSlideTicker();
                    } else if (tickersResponse.size() == 1) {
                        tickerAdapter.addItem(tickersResponse);
                    }
                }

                @Override
                public void onError() {
                    holder.tickerContainer.setVisibility(View.GONE);
                }
            });
        }
    }

    private ArrayList<String> mappingListTickerMessage(ArrayList<Ticker.Tickers> tickersResponse) {
        ArrayList<String> strings = new ArrayList<>();
        for (Ticker.Tickers tickers : tickersResponse) {
            String str = tickers.getMessage2().replaceAll("<p>(.*?)</p>", "$1");
            strings.add(str);
        }
        return strings;
    }

    private void getPromo() {
        category.fetchSlides(onGetPromoListener());
    }

    private FacadePromo.GetPromoListener onGetPromoListener() {
        return new FacadePromo.GetPromoListener() {

            @Override
            public void OnSuccessBanner(List<FacadePromo.PromoItem> promoList) {
                setBanner(promoList);
            }

            @Override
            public void OnError() {
                if (holder.bannerView != null) {
                    holder.bannerView.setVisibility(View.GONE);
                }
                showGetHomeMenuNetworkError();
            }
        };
    }

    private void setBanner(List<FacadePromo.PromoItem> promoList) {
        if (!promoList.isEmpty()) {
            stopAutoScrollBanner();
            holder.bannerView.setPromoList(mappingListBannerPromo(promoList));
            holder.bannerView.buildView();
        } else {
            if (holder.bannerView != null) {
                holder.bannerView.setVisibility(View.GONE);
            }
        }
    }

    private List<BannerView.PromoItem> mappingListBannerPromo(List<FacadePromo.PromoItem> promoList) {
        List<BannerView.PromoItem> list = new ArrayList<>();
        for (FacadePromo.PromoItem item : promoList) {
            BannerView.PromoItem toItem = new BannerView.PromoItem();
            toItem.setImgUrl(item.imgUrl);
            toItem.setPromoId(item.id);
            toItem.setPromoTitle(item.title);
            toItem.setPromoUrl(item.promoUrl);
            toItem.setPromoApplink(item.appLink); // toItem.setPromoApplink(Uri.decode(item.appLink)); wait should be fix from ws or client cause ios already use it
            list.add(toItem);
        }
        return list;
    }

    private void restartAutoScrollBanner() {
        holder.bannerView.restartAutoScrollBanner();
    }

    private void stopAutoScrollBanner() {
        holder.bannerView.stopAutoScrollBanner();
    }

    private void prepareView() {
        holder.tickerContainer.setLayoutManager(new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false)
        );
        holder.tickerContainer.setAdapter(tickerAdapter);
        holder.tickerContainer.setNestedScrollingEnabled(false);
    }

    private void initVar() {
        category = new CategoryImpl(getActivity(), this);
        holder = new ViewHolder();
        tickerAdapter = TickerAdapter.createInstance(getActivity(), this);
        rechargeCategoryPresenter = new RechargeCategoryPresenterImpl(getActivity(), this,
                new RechargeNetworkInteractorImpl(
                        new DigitalWidgetRepository(
                                new DigitalEndpointService(), new FavoriteNumberListDataMapper()),
                        new CategoryMapper(),
                        new StatusMapper()));

        homeCatMenuPresenter = new HomeCatMenuPresenterImpl(this);
        topPicksPresenter = new TopPicksPresenterImpl(this);
        tokoCashPresenter = new TokoCashPresenterImpl(this);
        brandsPresenter = new BrandsPresenterImpl(new OnGetBrandsListener() {
            @Override
            public void onSuccess(Brands brands) {
                holder.cardBrandLayout.setVisibility(View.VISIBLE);
                brandsRecyclerViewAdapter.setDataList(brands);
                brandsRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                holder.cardBrandLayout.setVisibility(View.GONE);
            }
        });
        tokoCashBroadcastReceiver = new TokoCashBroadcastReceiver(this);
        intentFilerTokoCash = new IntentFilter(TokoCashBroadcastReceiver.ACTION_GET_TOKOCASH);
    }

    private void startSlideTicker() {
        tickerHandler.removeCallbacks(tickerIncrementPage);
        tickerHandler.postDelayed(tickerIncrementPage, TICKER_DELAY);
    }

    private Runnable runnableIncrementTicker() {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    tickerShowed.clear();
                    incrementTicker();
                    tickerShowed.add(tickers.get(currentTicker));
                    tickerAdapter.notifyDataSetChanged();
                    tickerHandler.postDelayed(tickerIncrementPage, TICKER_DELAY);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

        };
    }

    private void incrementTicker() {
        currentTicker++;
        if (currentTicker >= tickers.size())
            currentTicker = 0;
    }

    private boolean isTickerRotating() {
        return (tickers != null && tickers.size() > 1 && tickerHandler != null && tickerIncrementPage != null);
    }

    @Override
    public void onStart() {
        if (isTickerRotating()) {
            startSlideTicker();
        }
        if (!holder.bannerView.isAutoScrollOnProgress()) {
            restartAutoScrollBanner();
        }
        super.onStart();
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
    public void onStop() {
        if (isTickerRotating()) {
            stopSlideTicker();
        }
        if (holder.bannerView.isAutoScrollOnProgress()) {
            stopAutoScrollBanner();
        }
        super.onStop();
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

    private void stopSlideTicker() {
        tickerHandler.removeCallbacks(tickerIncrementPage);
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        holder.MainView = inflater.inflate(R.layout.fragment_category, container, false);
        holder.wrapperLinearLayout = (LinearLayout) holder.MainView.findViewById(R.id.wrapperLinearLayout);
        holder.bannerView = (BannerView) holder.MainView.findViewById(R.id.banner_container_2);
        holder.digitalWidgetView = (DigitalWidgetView) holder.MainView.findViewById(R.id.digital_widget);
        holder.tickerContainer = (RecyclerView) holder.MainView.findViewById(R.id.announcement_ticker);
        holder.wrapperScrollview = (NestedScrollView) holder.MainView.findViewById(R.id.category_scrollview);
        holder.cardBrandLayout = (CardView) holder.MainView.findViewById(R.id.card_brand_layout);
        holder.rlBrands = (RelativeLayout) holder.MainView.findViewById(R.id.rl_title_layout);
        holder.tokoCashHeaderView = (TokoCashHeaderView) holder
                .MainView.findViewById(R.id.toko_cash_header_layout);
        holder.tokoCashHeaderView.setActionListener(this);
        initCategoryRecyclerView();
        initTopPicks();
        initBrands();
        category.fetchCacheTokocash();
    }

    private void initCategoryRecyclerView() {
        holder.categoriesRecylerview = (RecyclerView) holder.MainView.findViewById(R.id.my_recycler_view);

        holder.categoriesRecylerview.setHasFixedSize(true);
        holder.categoriesRecylerview.setNestedScrollingEnabled(false);

        recyclerViewCategoryMenuAdapter = new RecyclerViewCategoryMenuAdapter(getContext());

        recyclerViewCategoryMenuAdapter.setHomeMenuWidth(getHomeMenuWidth());

        recyclerViewCategoryMenuAdapter.setOnCategoryClickedListener(this);
        recyclerViewCategoryMenuAdapter.setOnGimmicClickedListener(this);
        recyclerViewCategoryMenuAdapter.setOnApplinkClickedListener(this);

        holder.categoriesRecylerview.setLayoutManager(
                new NonScrollLinearLayoutManager(getActivity(),
                        LinearLayoutManager.VERTICAL,
                        false)
        );
        holder.categoriesRecylerview.setAdapter(recyclerViewCategoryMenuAdapter);
    }

    @Override
    public void onCategoryClicked(CategoryItemModel categoryItemModel, int position) {
        if (categoryItemModel != null && categoryItemModel.getRedirectValue() != null) {
            TrackingUtils.sendMoEngageClickMainCategoryIcon(categoryItemModel.getName());
            navigateToNextActivity(categoryItemModel.getRedirectValue(), categoryItemModel.getName());
        }

    }

    @Override
    public void onGimmicClicked(CategoryItemModel categoryItemModel) {
        String redirectUrl = categoryItemModel.getRedirectValue();
        if (redirectUrl != null && redirectUrl.length() > 0) {
            String resultGenerateUrl = URLGenerator.generateURLSessionLogin(
                    Uri.encode(redirectUrl), MainApplication.getAppContext());

            navigateToGimmicWebview(resultGenerateUrl, categoryItemModel.getRedirectValue(), categoryItemModel.getName());
        }
    }

    @Override
    public void onDigitalCategoryClicked(CategoryItemModel itemModel) {

        UnifyTracking.eventClickCategoriesIcon(itemModel.getName());

        if (itemModel.getCategoryId().equalsIgnoreCase("103") && tokoCashData != null
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
                    .isSupportedDelegateDeepLink(itemModel.getAppLinks())) {
                DigitalCategoryDetailPassData passData = new DigitalCategoryDetailPassData.Builder()
                        .appLinks(itemModel.getAppLinks())
                        .categoryId(itemModel.getCategoryId())
                        .categoryName(itemModel.getName())
                        .url(itemModel.getRedirectValue())
                        .build();
                Bundle bundle = new Bundle();
                bundle.putParcelable(DigitalProductActivity.EXTRA_CATEGORY_PASS_DATA, passData);
                Intent intent = new Intent(getActivity(), DeeplinkHandlerActivity.class);
                intent.putExtras(bundle);
                intent.setData(Uri.parse(itemModel.getAppLinks()));
                startActivity(intent);
            } else {
                onGimmicClicked(itemModel);
            }
        }

        TrackingUtils.sendMoEngageClickMainCategoryIcon(itemModel.getName());
    }

    private void openActivationTokoCashWebView(String seamlessUrl) {
        if (getContext() instanceof Activity) {
            if (((Activity) getContext()).getApplication() instanceof TkpdCoreRouter) {
                ((TkpdCoreRouter) ((Activity) getContext()).getApplication())
                        .goToWallet(getContext(), seamlessUrl);
            }
        }
    }


    private void navigateToGimmicWebview(String url, String label, String title) {
        if (!url.equals("")) {
            Intent intent = new Intent(getActivity(), BannerWebView.class);
            intent.putExtra("url", url);
            intent.putExtra(BannerWebView.EXTRA_TITLE, title);
            startActivity(intent);

            UnifyTracking.eventHomeGimmick(label);
        }
    }

    private void navigateToNextActivity(String depID, String title) {
        IntermediaryActivity.moveTo(
                getActivity(),
                depID,
                title
        );

        Map<String, String> values = new HashMap<>();
        values.put(getString(R.string.value_category_name), title);

        UnifyTracking.eventHomeCategory(title);
    }

    /**
     * TokoCash Header show from Cache
     * Created by Nabilla Sabbaha 20171113
     */
    @Override
    public void onSuccessFetchTokoCashDataFromCache(TokoCashModel tokoCashModel) {
        this.tokoCashData = TokoCashUtil.convertToViewModel(
                tokoCashModel.getTokoCashData());
        holder.tokoCashHeaderView.setVisibility(View.VISIBLE);
        holder.tokoCashHeaderView.renderData(tokoCashData, false, "");
    }

    @Override
    public void onErrorFetchTokoCashDataFromCache(String message) {

    }

    /**
     * Brands a.k.a. Official Store
     * Created by Hafizh Herdi 20173001
     * Modified by Oka 20170315 (add view all official store)
     */
    private void initBrands() {
        holder.brandsRecyclerView = (RecyclerView) holder.MainView.findViewById(R.id.rv_brands_list);
        holder.textViewAllBrands = (TextView) holder.MainView.findViewById(R.id.text_view_all_brands);
        holder.brandsRecyclerView.setHasFixedSize(true);
        holder.brandsRecyclerView.setNestedScrollingEnabled(false);
        brandsRecyclerViewAdapter = new BrandsRecyclerViewAdapter(new BrandsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(String name, Brand brand, int position) {
                UnifyTracking.eventClickOfficialStore(AppEventTracking.EventLabel.OFFICIAL_STORE + name);
                Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
                intent.putExtras(ShopInfoActivity.createBundle(String.valueOf(brand.getShopId()), ""));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
            }
        }, getBrandsMenuWidth());
        holder.brandsRecyclerView.setLayoutManager(new NonScrollGridLayoutManager(getContext(), 3,
                GridLayoutManager.VERTICAL, false));
        holder.brandsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.divider300));
        holder.brandsRecyclerView.setAdapter(brandsRecyclerViewAdapter);
        holder.textViewAllBrands.setOnClickListener(onMoreBrandsClicked());
    }

    private View.OnClickListener onMoreBrandsClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        };
    }

    private void openWebViewBrandsURL(String url) {
        if (!url.trim().equals("")) {
            startActivity(BrandsWebViewActivity.newInstance(getActivity(), url));
        }
    }

    /* TOP PICKS */
    private void initTopPicks() {
        holder.topPicksRecyclerView = (RecyclerView) holder.MainView.findViewById(R.id.recycler_view_toppicks);
        holder.topPicksRecyclerView.setHasFixedSize(true);
        holder.topPicksRecyclerView.setNestedScrollingEnabled(false);
        topPicksAdapter = new TopPicksAdapter(getContext(), this, this, this);
        topPicksAdapter.setHomeMenuWidth(getHomeMenuWidth());
        holder.topPicksRecyclerView.setLayoutManager(
                new NonScrollLinearLayoutManager(getActivity(),
                        LinearLayoutManager.VERTICAL,
                        false)
        );
        holder.topPicksRecyclerView.setAdapter(topPicksAdapter);
    }

    @Override
    public void renderTopPicks(ArrayList<Toppick> toppicks) {

        topPicksAdapter.setDataList(toppicks);
        topPicksAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClicked(String toppickName, Item topPickItem, int position) {
        String url = topPickItem.getUrl();
        UnifyTracking.eventHomeTopPicksItem(toppickName, topPickItem.getName());
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
    public void onTitleClicked(Toppick toppick) {
        openWebViewTopPicksURL(toppick.getUrl());
        UnifyTracking.eventHomeTopPicksTitle(toppick.getName());
    }

    @Override
    public void onClick(Toppick toppick) {
        openWebViewTopPicksURL(TOP_PICKS_URL);
    }

    public void openWebViewTopPicksURL(String url) {
        if (!url.isEmpty()) {
            startActivity(TopPicksWebView.newInstance(getActivity(), url));
        }
    }


    /* TOP PICKS */


    @Override
    public void onSuccessFetchBanners(Banner banner) {
    }

    @Override
    public void onReceivePendingCashBack(CashBackData cashBackData) {
        if (cashBackData.getAmount() > 0) {
            bottomSheetDialogTokoCash = new BottomSheetView(getActivity());
            bottomSheetDialogTokoCash.setListener(getActinListener());
            bottomSheetDialogTokoCash.renderBottomSheet(new BottomSheetView
                    .BottomSheetField.BottomSheetFieldBuilder()
                    .setTitle(getString(R.string.toko_cash_pending_title))
                    .setBody(String.format(getString(R.string.toko_cash_pending_body),
                            cashBackData.getAmountText()))
                    .setImg(R.drawable.group_2)
                    .setUrlButton(tokoCashData.getDrawerWalletAction().getRedirectUrlActionButton(),
                            getString(R.string.toko_cash_pending_proceed_button))
                    .build());
            holder.tokoCashHeaderView.showPendingTokoCash(cashBackData.getAmountText());
        }
    }

    private BottomSheetView.ActionListener getActinListener() {
        return new BottomSheetView.ActionListener() {
            @Override
            public void clickOnTextLink(String url) {

            }

            @Override
            public void clickOnButton(String url) {
                String seamlessUrl;
                seamlessUrl = URLGenerator.generateURLSessionLogin((Uri.encode(url)),
                        getContext());
                if (getActivity() != null) {
                    if ((getActivity()).getApplication() instanceof TkpdCoreRouter) {
                        ((TkpdCoreRouter) (getActivity()).getApplication())
                                .goToWallet(getActivity(), seamlessUrl);
                    }
                }
            }
        };
    }

    @Override
    public void showGetCatMenuLoading() {
        //TODO create loading view when fetch request
    }

    @Override
    public void dismmisGetCatMenuLoading() {
        //TODO create dismiss loading view when fetch request
    }

    @Override
    public void showGetCatMenuErrorMessage(int stringId) {
        showGetHomeMenuNetworkError();
    }

    private void showGetHomeMenuNetworkError() {
        if (messageSnackbar == null) {
            messageSnackbar = NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    rechargeCategoryPresenter.fetchDataRechargeCategory();
                    getAnnouncement();
                    getPromo();
                    homeCatMenuPresenter.fetchHomeCategoryMenu(true);
                    topPicksPresenter.fetchTopPicks();
                    brandsPresenter.fetchBrands();
                }
            });
        }
        messageSnackbar.showRetrySnackbar();
    }

    @Override
    public void showGetCatMenuUnknownHostMessage(int stringId) {
        showGetHomeMenuNetworkError();
    }

    @Override
    public void showGetCatMenuSocketTimeoutExceptionMessage(int stringId) {
        showGetHomeMenuNetworkError();
        Log.d(TAG, "showGetCatMenuSocketTimeoutExceptionMessage() called with: stringId = [" + stringId + "]");
    }

    @Override
    public void showGetCatMenuEmptyMessage(int stringId) {
        showGetHomeMenuNetworkError();
        Log.d(TAG, "showGetCatMenuEmptyMessage() called with: stringId = [" + stringId + "]");
    }

    @Override
    public void renderHomeCatMenu(ArrayList<CategoryMenuModel> menuModelArrayList) {
        Log.d(TAG, "renderHomeCatMenu() called with: menuModelArrayList = ["
                + new Gson().toJson(menuModelArrayList) + "]");
        recyclerViewCategoryMenuAdapter.setDataList(menuModelArrayList);
        recyclerViewCategoryMenuAdapter.notifyDataSetChanged();

    }

    @Override
    public void showGetCatMenuFromDbErrorMessage(int stringId) {
        Log.d(TAG, "showGetCatMenuFromDbErrorMessage() called with: stringId = [" + stringId + "]");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && getActivity() != null && isAdded()) {
            if (messageSnackbar != null) {
                messageSnackbar.resumeRetrySnackbar();
            }
            ScreenTracking.screen(getScreenName());
            TrackingUtils.sendMoEngageOpenHomeEvent();
            sendAppsFlyerData();
            if (!holder.bannerView.isAutoScrollOnProgress()) {
                restartAutoScrollBanner();
            }
        } else {
            if (messageSnackbar != null) {
                messageSnackbar.pauseRetrySnackbar();
            }
            hideKeyboard();
        }

        super.setUserVisibleHint(isVisibleToUser);
    }

    public void sendAppsFlyerData() {
        TrackingUtils.fragmentBasedAFEvent(HomeRouter.IDENTIFIER_CATEGORY_FRAGMENT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        category.unSubscribe();
        homeCatMenuPresenter.OnDestroy();
        topPicksPresenter.onDestroy();
        brandsPresenter.onDestroy();
        tokoCashPresenter.onDestroy();
        rechargeCategoryPresenter.onDestroy();
    }

    //region recharge
    //Modified by Nabilla Sabbaha 3/10/2017
    @Override
    public void renderDataRechargeCategory(List<com.tokopedia.digital.widget.model.category.Category> rechargeCategory, boolean useCache) {
        holder.wrapperScrollview.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        holder.wrapperScrollview.setFocusable(true);
        holder.wrapperScrollview.setFocusableInTouchMode(true);
        holder.wrapperScrollview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view instanceof WidgetClientNumberView) {
                    view.requestFocusFromTouch();
                } else {
                    hideKeyboard();
                    view.clearFocus();
                }
                return false;
            }
        });
        holder.digitalWidgetView.setListener(getWidgetActionListener());
        holder.digitalWidgetView.renderDataWidget(rechargeCategory, useCache, getFragmentManager());
    }

    @NonNull
    private DigitalWidgetView.ActionListener getWidgetActionListener() {
        return new DigitalWidgetView.ActionListener() {
            @Override
            public void onClickSeeAllProduct() {
                if (getActivity().getApplication() instanceof IDigitalModuleRouter) {
                    IDigitalModuleRouter digitalModuleRouter =
                            (IDigitalModuleRouter) getActivity().getApplication();
                    startActivityForResult(
                            digitalModuleRouter.instanceIntentDigitalCategoryList(),
                            IDigitalModuleRouter.REQUEST_CODE_DIGITAL_CATEGORY_LIST
                    );
                }
            }
        };
    }

    @Override
    public void failedRenderDataRechargeCategory() {
        holder.digitalWidgetView.hideDigitalWidget();
    }

    @Override
    public void renderErrorNetwork() {
        showGetHomeMenuNetworkError();
    }

    @Override
    public void renderErrorMessage() {

    }

    private void hideKeyboard() {
        if (getView() != null) {
            InputMethodManager inputManager =
                    (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                    getView().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS
            );
        }
    }

    //endregion recharge

    public void scrollUntilBottomBanner() {
        holder.wrapperScrollview.smoothScrollTo(0, holder.bannerView != null ? holder.bannerView.getBottom() : 0);
    }

    private int getHomeMenuWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width / 2;
    }

    private int getBrandsMenuWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width / 5;
    }

    @Override
    public void onItemClicked() {
        holder.tickerContainer.setVisibility(View.GONE);
        category.closeTicker();
    }

    @Override
    public void onApplinkClicked(CategoryItemModel categoryItemModel) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(categoryItemModel.getAppLinks()));
        DeepLinkDelegate delegate = DeeplinkHandlerActivity.getDelegateInstance();
        delegate.dispatchFrom(getActivity(), intent);
    }

    @Override
    public void onReceivedTokoCashData(final DrawerTokoCash tokoCashData) {
        holder.tokoCashHeaderView.setVisibility(View.VISIBLE);
        final FirebaseRemoteConfig config = RemoteConfigFetcher.initRemoteConfig(getActivity());
        if(config != null) {
            config.setDefaults(R.xml.remote_config_default);
            config.fetch().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        config.activateFetched();
                        holder.tokoCashHeaderView.renderData(tokoCashData, config
                                .getBoolean("toko_cash_top_up"), config.getString("toko_cash_label"));
                        FragmentIndexCategory.this.tokoCashData = tokoCashData;
                    }
                }
            });
        }
        holder.tokoCashHeaderView.renderData(tokoCashData, false, getActivity()
                .getString(R.string.tokocash));
        this.tokoCashData = tokoCashData;
    }

    @Override
    public void onTokoCashDataError(String errorMessage) {
    }

    @Override
    public String getUserId() {
        SessionHandler sessionHandler = new SessionHandler(getActivity());
        return sessionHandler.getLoginID();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (trace != null)
            trace.stop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        storeLastStateTabSelected();
        super.onSaveInstanceState(outState);
    }

    protected void storeLastStateTabSelected() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(
                getActivity(), TkpdCache.CACHE_RECHARGE_WIDGET_TAB_SELECTION
        );
        int position = holder.digitalWidgetView.getPosition();
        localCacheHandler.putInt(TkpdCache.Key.WIDGET_RECHARGE_TAB_LAST_SELECTED,
                position);
        localCacheHandler.applyEditor();
    }

}