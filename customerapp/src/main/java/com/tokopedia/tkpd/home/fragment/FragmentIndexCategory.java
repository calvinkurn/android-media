package com.tokopedia.tkpd.home.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.google.gson.Gson;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.customView.WrapContentViewPager;
import com.tokopedia.core.database.model.category.CategoryData;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.home.TopPicksWebView;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.network.entity.home.Banner;
import com.tokopedia.core.network.entity.home.Brand;
import com.tokopedia.core.network.entity.home.Brands;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.core.network.entity.homeMenu.CategoryItemModel;
import com.tokopedia.core.network.entity.homeMenu.CategoryMenuModel;
import com.tokopedia.core.network.entity.topPicks.Item;
import com.tokopedia.core.network.entity.topPicks.Toppick;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.NonScrollLinearLayoutManager;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.discovery.activity.BrowseProductActivity;
import com.tokopedia.tkpd.BuildConfig;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.HomeCatMenuView;
import com.tokopedia.tkpd.home.OnGetBrandsListener;
import com.tokopedia.tkpd.home.TopPicksView;
import com.tokopedia.tkpd.home.adapter.BrandsRecyclerViewAdapter;
import com.tokopedia.tkpd.home.adapter.RecyclerViewCategoryMenuAdapter;
import com.tokopedia.tkpd.home.adapter.SectionListCategoryAdapter;
import com.tokopedia.tkpd.home.adapter.TickerAdapter;
import com.tokopedia.tkpd.home.adapter.TopPicksAdapter;
import com.tokopedia.tkpd.home.adapter.TopPicksItemAdapter;
import com.tokopedia.tkpd.home.banner.ConvenientBanner;
import com.tokopedia.tkpd.home.banner.holder.BannerHolderView;
import com.tokopedia.tkpd.home.banner.holder.BannerViewHolderCreator;
import com.tokopedia.tkpd.home.facade.FacadePromo;
import com.tokopedia.tkpd.home.presenter.BrandsPresenter;
import com.tokopedia.tkpd.home.presenter.BrandsPresenterImpl;
import com.tokopedia.tkpd.home.presenter.Category;
import com.tokopedia.tkpd.home.presenter.CategoryImpl;
import com.tokopedia.tkpd.home.presenter.CategoryView;
import com.tokopedia.tkpd.home.presenter.HomeCatMenuPresenter;
import com.tokopedia.tkpd.home.presenter.HomeCatMenuPresenterImpl;
import com.tokopedia.tkpd.home.presenter.TopPicksPresenter;
import com.tokopedia.tkpd.home.presenter.TopPicksPresenterImpl;
import com.tokopedia.tkpd.home.recharge.adapter.RechargeViewPagerAdapter;
import com.tokopedia.tkpd.home.recharge.presenter.RechargeCategoryPresenter;
import com.tokopedia.tkpd.home.recharge.presenter.RechargeCategoryPresenterImpl;
import com.tokopedia.tkpd.home.recharge.view.RechargeCategoryView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nisie on 1/07/15.
 * modified by mady add feature Recharge and change home menu
 * modified by alifa add Top Picks, ticker enhancement
 */
public class
FragmentIndexCategory extends TkpdBaseV4Fragment implements
        CategoryView,
        RechargeCategoryView,
        SectionListCategoryAdapter.OnCategoryClickedListener,
        SectionListCategoryAdapter.OnGimmicClickedListener, HomeCatMenuView, TopPicksView,
        TopPicksItemAdapter.OnTitleClickedListener, TopPicksItemAdapter.OnItemClickedListener,
        TopPicksAdapter.OnClickViewAll, TickerAdapter.OnTickerClosed {

    private static final long SLIDE_DELAY = 5000;
    private static final long TICKER_DELAY = 5000;
    public static final String TAG = FragmentIndexCategory.class.getSimpleName();
    private static final String TOP_PICKS_URL = "https://www.tokopedia.com/toppicks/";

    private ViewHolder holder;
    private Model model;
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

    private BroadcastReceiver stopBannerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (holder.bannerPager != null) {
                holder.bannerPager.stopTurning();
            }
        }
    };

    private class ViewHolder {
        private View MainView;
        private View banner;
        private ConvenientBanner bannerPager;
        private RelativeLayout bannerContainer;
        TabLayout tabLayoutRecharge;
        WrapContentViewPager viewpagerRecharge;
        RecyclerView tickerContainer;
        NestedScrollView wrapperScrollview;
        RecyclerView categoriesRecylerview;
        RecyclerView topPicksRecyclerView;
        RecyclerView brandsRecyclerView;
        RelativeLayout rlBrands;
        public LinearLayout wrapperLinearLayout;

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
                    holder.wrapperScrollview.smoothScrollTo(0, 0);
                }

                @Override
                public void onError() {
                    holder.tickerContainer.setVisibility(View.GONE);
                }
            });
        }
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
            }
        };
    }

    private void setBanner(List<FacadePromo.PromoItem> promoList) {
        if (!promoList.isEmpty()) {
            holder.banner = getActivity().getLayoutInflater().inflate(R.layout.home_banner, holder.bannerContainer);
            holder.bannerPager = (ConvenientBanner) holder.banner.findViewById(R.id.bannerViewPager);
            holder.bannerPager.setPages(getFragmentManager(), new BannerViewHolderCreator<BannerHolderView>() {
                @Override
                public BannerHolderView createHolder() {
                    return new BannerHolderView();
                }
            }, promoList)
                    .setPageIndicator(new int[]{R.drawable.indicator, R.drawable.indicator_focus});
            holder.bannerPager.getViewPager().setPageMargin(getResources()
                    .getDimensionPixelOffset(R.dimen.viewpager_margin));
            holder.bannerPager.setOnPromoLinkClickListener(onPromoLinkClicked());
            holder.bannerPager.getViewPager().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                        holder.bannerPager.stopTurning();
                    }
                    return false;
                }
            });
            holder.bannerPager.startTurning(SLIDE_DELAY);
        } else {
            ((ViewGroup) holder.bannerContainer.getParent()).removeView(holder.banner);
        }
    }

    private View.OnClickListener onPromoLinkClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BannerWebView.class);
                intent.putExtra("url", "https://www.tokopedia.com/promo/?flag_app=1");
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
        model = new Model();
        tickerAdapter = TickerAdapter.createInstance(getActivity(), this);
        rechargeCategoryPresenter = new RechargeCategoryPresenterImpl(getActivity(), this);
        homeCatMenuPresenter = new HomeCatMenuPresenterImpl(this);
        topPicksPresenter = new TopPicksPresenterImpl(this);
        brandsPresenter = new BrandsPresenterImpl(new OnGetBrandsListener() {
            @Override
            public void onSuccess(Brands brands) {
                holder.rlBrands.setVisibility(View.VISIBLE);
                brandsRecyclerViewAdapter.setDataList(brands);
                brandsRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                holder.rlBrands.setVisibility(View.GONE);
            }
        });
        rechargeCategoryPresenter.fecthDataRechargeCategory();
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
        holder.wrapperScrollview.smoothScrollTo(0, 0);
        rechargeCategoryPresenter.fetchStatusDigitalProductData();
        if (SessionHandler.isV4Login(getActivity())) {
            rechargeCategoryPresenter.fetchLastOrder();
        }
        super.onResume();
        getActivity().registerReceiver(stopBannerReceiver, new IntentFilter(BANNER_RECEIVER_INTENT));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(stopBannerReceiver);
    }

    @Override
    public void onStop() {
        if (isTickerRotating()) {
            stopSlideTicker();
        }
        super.onStop();
    }

    private void stopSlideTicker() {
        tickerHandler.removeCallbacks(tickerIncrementPage);
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        holder.MainView = inflater.inflate(R.layout.fragment_category, container, false);
        holder.wrapperLinearLayout = (LinearLayout) holder.MainView.findViewById(R.id.wrapperLinearLayout);
        holder.bannerContainer = (RelativeLayout) holder.MainView.findViewById(R.id.banner_container);
        holder.tabLayoutRecharge = (TabLayout) holder.MainView.findViewById(R.id.tablayout_recharge);
        holder.viewpagerRecharge = (WrapContentViewPager) holder.MainView.findViewById(R.id.viewpager_pulsa);
        ((LinearLayout) holder.tabLayoutRecharge.getParent()).setVisibility(View.GONE);
        holder.tickerContainer = (RecyclerView) holder.MainView.findViewById(R.id.announcement_ticker);
        holder.wrapperScrollview = (NestedScrollView) holder.MainView.findViewById(R.id.category_scrollview);
        holder.rlBrands = (RelativeLayout) holder.MainView.findViewById(R.id.rl_title_layout);
        initCategoryRecyclerView();
        initTopPicks();
        initBrands();

    }

    private void initCategoryRecyclerView() {
        holder.categoriesRecylerview = (RecyclerView) holder.MainView.findViewById(R.id.my_recycler_view);

        holder.categoriesRecylerview.setHasFixedSize(true);
        holder.categoriesRecylerview.setNestedScrollingEnabled(false);

        recyclerViewCategoryMenuAdapter = new RecyclerViewCategoryMenuAdapter(getContext());

        recyclerViewCategoryMenuAdapter.setHomeMenuWidth(getHomeMenuWidth());

        recyclerViewCategoryMenuAdapter.setOnCategoryClickedListener(this);
        recyclerViewCategoryMenuAdapter.setOnGimmicClickedListener(this);


        holder.categoriesRecylerview.setLayoutManager(
                new NonScrollLinearLayoutManager(getActivity(),
                        LinearLayoutManager.VERTICAL,
                        false)
        );
        holder.categoriesRecylerview.setAdapter(recyclerViewCategoryMenuAdapter);
    }

    @Override
    public void onCategoryClicked(CategoryItemModel categoryItemModel, int position) {
        if (categoryItemModel != null && categoryItemModel.getRedirectValue() != null){
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

            navigateToGimmicWebview(resultGenerateUrl, categoryItemModel.getRedirectValue());
        }
    }

    private void navigateToGimmicWebview(String url, String label) {
        if (!url.equals("")) {
            Intent intent = new Intent(getActivity(), BannerWebView.class);
            intent.putExtra("url", url);
            startActivity(intent);

            UnifyTracking.eventHomeGimmick(label);
        }
    }

    private void navigateToNextActivity(String depID, String title) {
        BrowseProductActivity.moveTo(
                getActivity(),
                depID,
                TopAdsApi.SRC_DIRECTORY,
                BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY,
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
     */
    private void initBrands() {
        holder.brandsRecyclerView = (RecyclerView) holder.MainView.findViewById(R.id.rv_brands_list);
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
        holder.brandsRecyclerView.setLayoutManager(
                new NonScrollLinearLayoutManager(getActivity(),
                        LinearLayoutManager.HORIZONTAL,
                        false)
        );
        holder.brandsRecyclerView.setAdapter(brandsRecyclerViewAdapter);
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
        if (url != "") {
            startActivity(TopPicksWebView.newInstance(getActivity(), url));
        }
    }


    /* TOP PICKS */


    @Override
    public void onSuccessFetchBanners(Banner banner) {
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
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                homeCatMenuPresenter.fetchHomeCategoryMenu(true);
                topPicksPresenter.fetchTopPicks();
                brandsPresenter.fetchBrands();
            }
        }).showRetrySnackbar();
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
            ScreenTracking.screen(getScreenName());
            TrackingUtils.sendMoEngageOpenHomeEvent();
            sendAppsFlyerData();
            holder.wrapperScrollview.smoothScrollTo(0, 0);
        } else {
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
    }

    //region recharge
    @Override
    public void renderDataRechargeCategory(CategoryData rechargeCategory) {
        if (rechargeCategory.getData().size() == 0) {
            return;
        }
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
        holder.viewpagerRecharge.setOffscreenPageLimit(rechargeCategory.getData().size() + 2);
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
        ((LinearLayout) holder.tabLayoutRecharge.getParent()).setVisibility(View.GONE);
    }

    @Override
    public void renderErrorNetwork() {

    }

    @Override
    public void hideRechargeWidget() {
        ((LinearLayout) holder.tabLayoutRecharge.getParent()).setVisibility(View.GONE);
    }

    private void addChildTablayout(CategoryData rechargeCategory, List<Integer> newRechargePositions) {
        for (int i = 0; i < rechargeCategory.getData().size(); i++) {
            com.tokopedia.core.database.model.category.Category category = rechargeCategory.getData().get(i);
            TabLayout.Tab tab = holder.tabLayoutRecharge.newTab();
            tab.setText(category.getAttributes().getName());
            holder.tabLayoutRecharge.addTab(tab);
            if (category.getAttributes().getIsNew()) {
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
        int widthOfHomeMenuView = (width / 5);
        return widthOfHomeMenuView;
    }

    @Override
    public void onItemClicked() {
        holder.tickerContainer.setVisibility(View.GONE);
        category.closeTicker();
    }


}