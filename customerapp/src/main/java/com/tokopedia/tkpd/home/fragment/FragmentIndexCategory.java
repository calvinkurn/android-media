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
import android.support.design.widget.TabLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.customView.RechargeEditText;
import com.tokopedia.core.customView.WrapContentViewPager;
import com.tokopedia.core.database.model.category.CategoryData;
import com.tokopedia.core.drawer.listener.TokoCashUpdateListener;
import com.tokopedia.core.drawer.receiver.TokoCashBroadcastReceiver;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCashAction;
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
import com.tokopedia.core.react.ReactConst;
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
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.widgets.DividerItemDecoration;
import com.tokopedia.digital.product.activity.DigitalProductActivity;
import com.tokopedia.digital.tokocash.model.CashBackData;
import com.tokopedia.discovery.intermediary.view.IntermediaryActivity;
import com.tokopedia.tkpd.BuildConfig;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.deeplink.DeepLinkDelegate;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.home.HomeCatMenuView;
import com.tokopedia.tkpd.home.OnGetBrandsListener;
import com.tokopedia.tkpd.home.ReactNativeOfficialStoresActivity;
import com.tokopedia.tkpd.home.TopPicksView;
import com.tokopedia.tkpd.home.adapter.BannerPagerAdapter;
import com.tokopedia.tkpd.home.adapter.BrandsRecyclerViewAdapter;
import com.tokopedia.tkpd.home.adapter.RecyclerViewCategoryMenuAdapter;
import com.tokopedia.tkpd.home.adapter.SectionListCategoryAdapter;
import com.tokopedia.tkpd.home.adapter.TickerAdapter;
import com.tokopedia.tkpd.home.adapter.TopPicksAdapter;
import com.tokopedia.tkpd.home.adapter.TopPicksItemAdapter;
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
import com.tokopedia.tkpd.home.recharge.adapter.RechargeViewPagerAdapter;
import com.tokopedia.tkpd.home.recharge.presenter.RechargeCategoryPresenter;
import com.tokopedia.tkpd.home.recharge.presenter.RechargeCategoryPresenterImpl;
import com.tokopedia.tkpd.home.recharge.view.RechargeCategoryView;
import com.tokopedia.tkpd.home.tokocash.BottomSheetTokoCash;
import com.tokopedia.tkpd.remoteconfig.RemoteConfigFetcher;

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
    private BottomSheetTokoCash bottomSheetDialogTokoCash;

    private DrawerTokoCash tokoCashData;
    private BannerPagerAdapter bannerPagerAdapter;
    private int currentPosition;
    private ArrayList<ImageView> indicatorItems = new ArrayList<>();
    private Runnable runnableScrollBanner;
    private Handler bannerHandler;

    RemoteConfigFetcher remoteConfigFetcher;
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
        private View banner;
        private RecyclerView bannerPager;
        private ViewGroup bannerIndicator;
        private RelativeLayout bannerContainer;
        public View bannerSeeAll;
        private TokoCashHeaderView tokoCashHeaderView;
        TabLayout tabLayoutRecharge;
        WrapContentViewPager viewpagerRecharge;
        RecyclerView tickerContainer;
        NestedScrollView wrapperScrollview;
        RecyclerView categoriesRecylerview;
        RecyclerView topPicksRecyclerView;
        CardView cardBrandLayout;
        RecyclerView brandsRecyclerView;
        RelativeLayout rlBrands;
        TextView textViewAllBrands;
        LinearLayout wrapperLinearLayout;
        TextView seeAllProduct;
        CardView containerRecharge;

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
        super.onCreate(savedInstanceState);
        LocalCacheHandler.clearCache(getActivity(), TkpdCache.CACHE_RECHARGE_WIDGET_TAB_SELECTION);
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
        getAnnouncement();
        getPromo();
        homeCatMenuPresenter.fetchHomeCategoryMenu(false);
        topPicksPresenter.fetchTopPicks();
        brandsPresenter.fetchBrands();
        fetchRemoteConfig();
    }

    private void fetchRemoteConfig() {
        remoteConfigFetcher = new RemoteConfigFetcher(getActivity());
        remoteConfigFetcher.fetch(new RemoteConfigFetcher.Listener() {
            @Override
            public void onComplete(FirebaseRemoteConfig firebaseRemoteConfig) {
                FragmentIndexCategory.this.firebaseRemoteConfig = firebaseRemoteConfig;
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                FragmentIndexCategory.this.firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
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
                        tickerIncrementPage = runnableIncrementTicker();
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
        category.fetchBanners(onGetPromoListener());
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
                if (holder.MainView.getParent() != null && holder.bannerContainer != null)
                    ((ViewGroup) holder.MainView.getParent()).removeView(holder.bannerContainer);
                showGetHomeMenuNetworkError();
            }
        };
    }

    private void setBanner(List<FacadePromo.PromoItem> promoList) {
        if (!promoList.isEmpty()) {
            bannerPagerAdapter = new BannerPagerAdapter(promoList);
            holder.banner = getActivity().getLayoutInflater().inflate(R.layout.home_banner, holder.bannerContainer);
            holder.bannerPager = (RecyclerView) holder.banner.findViewById(R.id.viewpager_banner_category);
            holder.bannerIndicator = (ViewGroup) holder.banner.findViewById(R.id.indicator_banner_container);
            holder.bannerSeeAll = holder.banner.findViewById(R.id.promo_link);
            holder.bannerSeeAll.setOnClickListener(onPromoLinkClicked());

            holder.bannerPager.setHasFixedSize(true);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.bannerPager.setLayoutManager(layoutManager);
            holder.bannerPager.setAdapter(bannerPagerAdapter);

            for (int count = 0; count < promoList.size(); count++) {
                ImageView pointView = new ImageView(getContext());
                pointView.setPadding(5, 0, 5, 0);
                if (count == 0) {
                    pointView.setImageResource(R.drawable.indicator_focus);
                } else {
                    pointView.setImageResource(R.drawable.indicator);
                }
                indicatorItems.add(pointView);
                holder.bannerIndicator.addView(pointView);
            }

            holder.bannerPager.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int firstCompleteVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                    currentPosition = firstCompleteVisibleItemPosition;
                    for (int i = 0; i < indicatorItems.size(); i++) {
                        if (firstCompleteVisibleItemPosition != i) {
                            indicatorItems.get(i).setImageResource(R.drawable.indicator);
                        } else {
                            indicatorItems.get(i).setImageResource(R.drawable.indicator_focus);
                        }
                    }
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING && recyclerView.isInTouchMode()) {
                        stopAutoScrollBanner();
                    }
                }

            });

            if (promoList.size() == 1) {
                holder.bannerIndicator.setVisibility(View.GONE);
            }

            PagerSnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(holder.bannerPager);

            bannerHandler = new Handler();
            runnableScrollBanner = new Runnable() {
                @Override
                public void run() {
                    if (holder.bannerPager != null) {
                        if (currentPosition == holder.bannerPager.getAdapter().getItemCount() - 1) {
                            currentPosition = -1;
                        }
                        holder.bannerPager.smoothScrollToPosition(currentPosition + 1);
                        bannerHandler.postDelayed(this, SLIDE_DELAY);
                    }
                }
            };

            startAutoScrollBanner();
        } else {
            ((ViewGroup) holder.bannerContainer.getParent()).removeView(holder.banner);
        }
    }

    private void startAutoScrollBanner() {
        if (bannerHandler != null && runnableScrollBanner != null) {
            bannerHandler.postDelayed(runnableScrollBanner, SLIDE_DELAY);
        }
    }

    private void stopAutoScrollBanner() {
        if (bannerHandler != null && runnableScrollBanner != null) {
            bannerHandler.removeCallbacks(runnableScrollBanner);
        }
    }

    private View.OnClickListener onPromoLinkClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BannerWebView.class);
                intent.putExtra(BannerWebView.EXTRA_TITLE, getString(R.string.title_activity_promo));
                intent.putExtra(BannerWebView.EXTRA_URL, TkpdBaseURL.URL_PROMO +
                        TkpdBaseURL.FLAG_APP);
                startActivity(intent);
            }
        };
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
        rechargeCategoryPresenter = new RechargeCategoryPresenterImpl(getActivity(), this);
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
        rechargeCategoryPresenter.fecthDataRechargeCategory();
        tokoCashBroadcastReceiver = new TokoCashBroadcastReceiver(this);
        getActivity().registerReceiver(tokoCashBroadcastReceiver, new IntentFilter(
                TokoCashBroadcastReceiver.ACTION_GET_TOKOCASH
        ));
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
        super.onStart();
    }

    @Override
    public void onResume() {
        LocalCacheHandler.clearCache(getActivity(), "RechargeCache");
        rechargeCategoryPresenter.fetchStatusDigitalProductData();
        if (SessionHandler.isV4Login(getActivity())) {
            rechargeCategoryPresenter.fetchLastOrder();
        }
        super.onResume();
    }

    @Override
    public void onStop() {
        if (isTickerRotating()) {
            stopSlideTicker();
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
        holder.bannerContainer = (RelativeLayout) holder.MainView.findViewById(R.id.banner_container);
        holder.containerRecharge = (CardView) holder.MainView.findViewById(R.id.container_recharge);
        holder.tabLayoutRecharge = (TabLayout) holder.MainView.findViewById(R.id.tablayout_recharge);
        holder.viewpagerRecharge = (WrapContentViewPager) holder.MainView.findViewById(R.id.viewpager_pulsa);
        ((LinearLayout) holder.tabLayoutRecharge.getParent()).setVisibility(View.GONE);
        holder.tickerContainer = (RecyclerView) holder.MainView.findViewById(R.id.announcement_ticker);
        holder.wrapperScrollview = (NestedScrollView) holder.MainView.findViewById(R.id.category_scrollview);
        holder.cardBrandLayout = (CardView) holder.MainView.findViewById(R.id.card_brand_layout);
        holder.rlBrands = (RelativeLayout) holder.MainView.findViewById(R.id.rl_title_layout);
        holder.tokoCashHeaderView = (TokoCashHeaderView) holder
                .MainView.findViewById(R.id.toko_cash_header_layout);
        holder.tokoCashHeaderView.setActionListener(this);
        holder.seeAllProduct = (TextView) holder.MainView.findViewById(R.id.see_all_product);
        holder.seeAllProduct.setOnClickListener(getClickListenerShowAllDigitalProducts());
        initCategoryRecyclerView();
        initTopPicks();
        initBrands();

    }

    @NonNull
    private View.OnClickListener getClickListenerShowAllDigitalProducts() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            DeepLinkDelegate deepLinkDelegate = DeeplinkHandlerActivity.getDelegateInstance();
            if (deepLinkDelegate.supportsUri(itemModel.getAppLinks())) {
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

        TrackingUtils.eventLoca("event : " + getString(R.string.event_click_category), values);

        UnifyTracking.eventHomeCategory(title);
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
                            ReactNativeOfficialStoresActivity.createReactNativeActivity(
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
        //TODO Uncomment Later
        if (cashBackData.getAmount() > 0) {
            bottomSheetDialogTokoCash = new BottomSheetTokoCash(getActivity());
            bottomSheetDialogTokoCash.setCashBackText(cashBackData.getAmountText());
            bottomSheetDialogTokoCash.setActivationUrl(tokoCashData.getDrawerWalletAction().getRedirectUrlActionButton());
            holder.tokoCashHeaderView.showPendingTokoCash(cashBackData.getAmountText());
        }
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
                    rechargeCategoryPresenter.fecthDataRechargeCategory();
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
        setLocalyticFlow();
        if (isVisibleToUser && getActivity() != null && isAdded()) {
            if (messageSnackbar != null) {
                messageSnackbar.resumeRetrySnackbar();
            }
            ScreenTracking.screen(getScreenName());
            TrackingUtils.sendMoEngageOpenHomeEvent();
            sendAppsFlyerData();
            stopAutoScrollBanner();
            startAutoScrollBanner();
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
        stopAutoScrollBanner();
        getActivity().unregisterReceiver(tokoCashBroadcastReceiver);
    }

    //region recharge
    @Override
    public void renderDataRechargeCategory(CategoryData rechargeCategory) {
        holder.wrapperScrollview.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        holder.wrapperScrollview.setFocusable(true);
        holder.wrapperScrollview.setFocusableInTouchMode(true);
        holder.wrapperScrollview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view instanceof RechargeEditText) {
                    view.requestFocusFromTouch();
                } else {
                    hideKeyboard();
                    view.clearFocus();
                }
                return false;
            }
        });

        if (rechargeCategory.getData().size() == 0) {
            return;
        }
        holder.containerRecharge.setVisibility(View.VISIBLE);
        ((LinearLayout) holder.tabLayoutRecharge.getParent()).setVisibility(View.VISIBLE);

        List<Integer> newRechargePositions = new ArrayList<>();

        holder.tabLayoutRecharge.removeAllTabs();
        addChildTablayout(rechargeCategory, newRechargePositions);
        getPositionFlagNewRecharge(newRechargePositions);


        if (rechargeCategory.getData().size() == 1)
            holder.tabLayoutRecharge.setTabMode(TabLayout.MODE_SCROLLABLE);
        else {
            holder.tabLayoutRecharge.setTabGravity(TabLayout.GRAVITY_FILL);
            holder.tabLayoutRecharge.setTabMode(TabLayout.MODE_FIXED);
        }

        final RechargeViewPagerAdapter rechargeViewPagerAdapter = new RechargeViewPagerAdapter(
                getChildFragmentManager(), rechargeCategory.getData()
        );
        holder.viewpagerRecharge.setAdapter(rechargeViewPagerAdapter);
        holder.viewpagerRecharge.getAdapter().notifyDataSetChanged();
        LocalCacheHandler handler = new LocalCacheHandler(
                getActivity(), TkpdCache.CACHE_RECHARGE_WIDGET_TAB_SELECTION
        );
        addTablayoutListener(rechargeViewPagerAdapter);
        holder.viewpagerRecharge.setOffscreenPageLimit(rechargeCategory.getData().size());
        final int positionTab = handler.getInt(TkpdCache.Key.WIDGET_RECHARGE_TAB_LAST_SELECTED);
        if (positionTab != -1 && positionTab < rechargeCategory.getData().size()) {
            holder.viewpagerRecharge.postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.viewpagerRecharge.setCurrentItem(positionTab);
                }
            }, 300);
            holder.tabLayoutRecharge.getTabAt(positionTab).select();
        } else {
            holder.viewpagerRecharge.setCurrentItem(0);
        }


    }


    @Override
    public void failedRenderDataRechargeCategory() {
        holder.containerRecharge.setVisibility(View.GONE);
        ((LinearLayout) holder.tabLayoutRecharge.getParent()).setVisibility(View.GONE);
    }

    @Override
    public void renderErrorNetwork() {

    }

    @Override
    public void hideRechargeWidget() {
        holder.containerRecharge.setVisibility(View.GONE);
        ((LinearLayout) holder.tabLayoutRecharge.getParent()).setVisibility(View.GONE);
    }

    private void addChildTablayout(CategoryData rechargeCategory, List<Integer> newRechargePositions) {
        for (int i = 0; i < rechargeCategory.getData().size(); i++) {
            com.tokopedia.core.database.model.category.Category category = rechargeCategory.getData().get(i);
            TabLayout.Tab tab = holder.tabLayoutRecharge.newTab();
            tab.setText(category.getAttributes().getName());
            holder.tabLayoutRecharge.addTab(tab);
            if (category.getAttributes().isNew()) {
                newRechargePositions.add(i);

            }
        }
    }

    private void getPositionFlagNewRecharge(List<Integer> newRechargePositions) {
        for (int positionRecharge : newRechargePositions) {
            TextView tv = (TextView) (((LinearLayout) ((LinearLayout)
                    holder.tabLayoutRecharge.getChildAt(0))
                    .getChildAt(positionRecharge)).getChildAt(1));
            if (tv != null) tv.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    ResourcesCompat.getDrawable(getResources(), R.drawable.recharge_circle, null)
                    , null
            );
        }
    }

    private void addTablayoutListener(final RechargeViewPagerAdapter rechargeViewPagerAdapter) {
        holder.viewpagerRecharge.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(holder.tabLayoutRecharge)
        );

        holder.tabLayoutRecharge.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                holder.viewpagerRecharge.setCurrentItem(tab.getPosition(), false);
                rechargeViewPagerAdapter.notifyDataSetChanged();
                if (tab.getText() != null) {
                    UnifyTracking.eventHomeRechargeTab(tab.getText().toString());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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

    private void setLocalyticFlow() {
        try {
            String screenName = AppScreen.SCREEN_HOME_CATEGORY;
            ScreenTracking.screenLoca(screenName);
            ScreenTracking.eventLoca(screenName);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void scrollUntilBottomBanner() {
        holder.wrapperScrollview.smoothScrollTo(0, holder.banner != null ? holder.banner.getBottom() : 0);
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

    public void onReceivedTokoCashData(final DrawerTokoCash tokoCashData) {
        holder.tokoCashHeaderView.setVisibility(View.VISIBLE);
        final FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();
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
        holder.tokoCashHeaderView.renderData(tokoCashData, false, getActivity()
                .getString(R.string.tokocash));
        this.tokoCashData = tokoCashData;
    }

    @Override
    public void onTokoCashDataError(String errorMessage) {

    }

    private String getTokoCashActionRedirectUrl(DrawerTokoCashAction tokoCashData) {
        if (tokoCashData == null) return "";
        else return tokoCashData.getRedirectUrl();
    }

    @Override
    public String getUserId() {
        SessionHandler sessionHandler = new SessionHandler(getActivity());
        return sessionHandler.getLoginID();
    }
}