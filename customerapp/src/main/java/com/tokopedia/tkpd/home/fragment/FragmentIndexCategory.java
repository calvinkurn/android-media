package com.tokopedia.tkpd.home.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.viewpagerindicator.CirclePageIndicator;
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
import com.tokopedia.core.shopinfo.facades.GetShopInfoRetrofit;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.NonScrollLinearLayoutManager;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.discovery.activity.BrowseProductActivity;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.HomeCatMenuView;
import com.tokopedia.tkpd.home.TopPicksView;
import com.tokopedia.tkpd.home.adapter.BrandsRecyclerViewAdapter;
import com.tokopedia.tkpd.home.adapter.RecyclerViewCategoryMenuAdapter;
import com.tokopedia.tkpd.home.adapter.SectionListCategoryAdapter;
import com.tokopedia.tkpd.home.adapter.TickerAnnouncementAdapter;
import com.tokopedia.tkpd.home.adapter.TopPicksAdapter;
import com.tokopedia.tkpd.home.adapter.TopPicksItemAdapter;
import com.tokopedia.tkpd.home.facade.FacadePromo;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nisie on 1/07/15.
 * modified by mady add feature Recharge and change home menu
 * modified by alifa add Top Picks
 */
public class FragmentIndexCategory extends TkpdBaseV4Fragment implements
        CategoryView,
        RechargeCategoryView,
        SectionListCategoryAdapter.OnCategoryClickedListener,
        SectionListCategoryAdapter.OnGimmicClickedListener, HomeCatMenuView, TopPicksView,
        TopPicksItemAdapter.OnTitleClickedListener, TopPicksItemAdapter.OnItemClickedListener, TopPicksAdapter.OnClickViewAll{

    private static final long SLIDE_DELAY = 8000;
    public static final String TAG = FragmentIndexCategory.class.getSimpleName();
    private static final String BASE_URL = "www.tokopedia.com";
    private static final String BASE_MOBILE_URL = "m.tokopedia.com";
    private static final String TOP_PICKS_URL = "https://www.tokopedia.com/toppicks/";

    private ViewHolder holder;
    private Model model;
    private PromoImagePagerAdapter pagerAdapter;
    private Runnable incrementPage;
    private Handler mHandler;
    Category category;
    private RechargeCategoryPresenter rechargeCategoryPresenter;
    TickerAnnouncementAdapter tickerAdapter;

    private HomeCatMenuPresenter homeCatMenuPresenter;
    private TopPicksPresenter topPicksPresenter;
    private RecyclerViewCategoryMenuAdapter recyclerViewCategoryMenuAdapter;
    private TopPicksAdapter topPicksAdapter;
    private BrandsRecyclerViewAdapter brandsRecyclerViewAdapter;

    private GetShopInfoRetrofit getShopInfoRetrofit;

    private class ViewHolder {
        private View MainView;
        private View banner;
        private ViewPager bannerViewPager;
        private CirclePageIndicator bannerIndicator;
        private RelativeLayout bannerContainer;
        private TextView promoLink;
        private TextView tvBrandsTitle;
        TabLayout tabLayoutRecharge;
        WrapContentViewPager viewpagerRecharge;
        RecyclerView announcementContainer;
        NestedScrollView wrapperScrollview;
        RecyclerView categoriesRecylerview;
        RecyclerView topPicksRecyclerView;
        RecyclerView brandsRecyclerView;
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

    }

    private void getAnnouncement() {
        category.fetchTickers(new Category.FetchTickersListener() {
            @Override
            public void onSuccess(final Ticker.Tickers[] tickers) {
                if (tickers.length > 0) {

                    holder.announcementContainer.setVisibility(View.VISIBLE);
                    tickerAdapter.addItem(tickers);
                    holder.wrapperScrollview.smoothScrollTo(0, 0);
                }
            }

            @Override
            public void onError() {
                holder.announcementContainer.setVisibility(View.GONE);

            }
        });
    }

    private void getPromo() {
        category.fetchBanners(onGetPromoListener());
        category.fetchSlides(onGetPromoListener());
        category.fetchBrands(new Category.OnGetBrandsListener() {

            @Override
            public void onSuccess(Brands brands) {
                CommonUtils.dumper("mohito get brands "+brands.getData().size());
                brandsRecyclerViewAdapter.setDataList(brands);
                brandsRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
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
            holder.banner = getActivity().getLayoutInflater().inflate(R.layout.banner, holder.bannerContainer);
            holder.bannerViewPager = (ViewPager) holder.banner.findViewById(R.id.view_pager);
            holder.bannerIndicator = (CirclePageIndicator) holder.banner.findViewById(R.id.indicator);
            holder.promoLink = (TextView) holder.banner.findViewById(R.id.promo_link);
            holder.bannerViewPager.setAdapter(pagerAdapter);
            holder.bannerViewPager.addOnPageChangeListener(onPromoChanged());
            holder.bannerIndicator.setFillColor(ContextCompat.getColor(getContext(), R.color.green_400));
            holder.bannerIndicator.setStrokeColor(ContextCompat.getColor(getContext(), R.color.green_500));
            holder.bannerIndicator.setViewPager(holder.bannerViewPager);
            holder.promoLink.setOnClickListener(onPromoLinkClicked());
            model.listBanner.clear();
            model.listBanner.addAll(promoList);
            pagerAdapter.notifyDataSetChanged();

            RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) holder.bannerViewPager.getLayoutParams();
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            //param.height = metrics.widthPixels / 2;
            holder.bannerViewPager.setLayoutParams(param);
            holder.wrapperScrollview.smoothScrollTo(0, 0);
            startSlide();
        } else {
            ((ViewGroup) holder.bannerContainer.getParent()).removeView(holder.banner);
        }
    }

    private ViewPager.OnPageChangeListener onPromoChanged() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                stopSlide();
                startSlide();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

    private void prepareView() {
        holder.announcementContainer.setLayoutManager(new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false)
        );
        holder.announcementContainer.setAdapter(tickerAdapter);
        holder.announcementContainer.setNestedScrollingEnabled(false);
    }

    private void initVar() {
        category = new CategoryImpl(this);
        holder = new ViewHolder();
        model = new Model();
        pagerAdapter = new PromoImagePagerAdapter(model.listBanner);
        incrementPage = runnableIncrement();
        mHandler = new Handler();
        tickerAdapter = TickerAnnouncementAdapter.createInstance(getActivity());
        rechargeCategoryPresenter = new RechargeCategoryPresenterImpl(getActivity(), this);
        homeCatMenuPresenter = new HomeCatMenuPresenterImpl(this);
        topPicksPresenter = new TopPicksPresenterImpl(this);
    }

    private void startSlide() {
        mHandler.removeCallbacks(incrementPage);
        mHandler.postDelayed(incrementPage, SLIDE_DELAY);
    }

    private Runnable runnableIncrement() {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    int currentItem = holder.bannerViewPager.getCurrentItem();
                    int maxItems = holder.bannerViewPager.getAdapter().getCount();
                    if (maxItems != 0) {
                        holder.bannerViewPager.setCurrentItem((currentItem + 1) % maxItems, true);
                    } else {
                        holder.bannerViewPager.setCurrentItem(0, true);
                    }
                    mHandler.postDelayed(incrementPage, SLIDE_DELAY);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

        };
    }

    @Override
    public void onStart() {
        startSlide();
        super.onStart();
    }

    @Override
    public void onResume() {
        LocalCacheHandler.clearCache(getActivity(), "RechargeCache");
        holder.wrapperScrollview.smoothScrollTo(0, 0);
        ((LinearLayout) holder.tabLayoutRecharge.getParent()).setVisibility(View.GONE);
        rechargeCategoryPresenter.fecthDataRechargeCategory();
        if (SessionHandler.isV4Login(getActivity())) {
            rechargeCategoryPresenter.fetchLastOrder();
        }
        super.onResume();
    }


    @Override
    public void onStop() {
        stopSlide();
        super.onStop();
    }


    private void stopSlide() {
        mHandler.removeCallbacks(incrementPage);
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        holder.MainView = inflater.inflate(R.layout.fragment_category, container, false);
        holder.wrapperLinearLayout = (LinearLayout) holder.MainView.findViewById(R.id.wrapperLinearLayout);
        holder.bannerContainer = (RelativeLayout) holder.MainView.findViewById(R.id.banner_container);
        holder.tabLayoutRecharge = (TabLayout) holder.MainView.findViewById(R.id.tablayout_recharge);
        holder.viewpagerRecharge = (WrapContentViewPager) holder.MainView.findViewById(R.id.viewpager_pulsa);
        ((LinearLayout) holder.tabLayoutRecharge.getParent()).setVisibility(View.GONE);
        holder.announcementContainer = (RecyclerView) holder.MainView.findViewById(R.id.announcement_ticker);
        holder.wrapperScrollview = (NestedScrollView) holder.MainView.findViewById(R.id.category_scrollview);
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

    private boolean isShop(List<String> linkSegment) {
        return (linkSegment.size() == 1
                && !linkSegment.get(0).equals("pulsa")
                && !linkSegment.get(0).equals("iklan")
                && !linkSegment.get(0).equals("newemail.pl")
                && !linkSegment.get(0).equals("search")
                && !linkSegment.get(0).equals("hot")
                && !linkSegment.get(0).equals("about")
                && !linkSegment.get(0).equals("reset.pl")
                && !linkSegment.get(0).equals("activation.pl")
                && !linkSegment.get(0).equals("privacy.pl")
                && !linkSegment.get(0).equals("terms.pl")
                && !linkSegment.get(0).startsWith("invoice.pl"));
    }

    public void getShopInfo(final String url, final String shopDomain) {
        getShopInfoRetrofit = new GetShopInfoRetrofit(getActivity(), "", shopDomain);
        getShopInfoRetrofit.setGetShopInfoListener(new GetShopInfoRetrofit.OnGetShopInfoListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    ShopModel shopModel = new Gson().fromJson(result,
                            com.tokopedia.core.shopinfo.models.shopmodel.ShopModel.class);
                    if (shopModel.info != null) {
                        JSONObject shop = new JSONObject(result);
                        JSONObject shopInfo = new JSONObject(shop.getString("info"));
                        Bundle bundle = ShopInfoActivity.createBundle(
                                shopInfo.getString("shop_id"),shopInfo.getString("shop_domain"));
                        Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        openWebViewURL(url);
                    }
                } catch (Exception e) {
                    openWebViewURL(url);
                }
            }

            @Override
            public void onError(String message) {
                openWebViewURL(url);
            }

            @Override
            public void onFailure() {
                openWebViewURL(url);
            }
        });
        getShopInfoRetrofit.getShopInfo();
    }

    public void openWebViewURL(String url) {
        if (url!="") {
            Intent intent = new Intent(getActivity(), BannerWebView.class);
            intent.putExtra("url", url);
            startActivity(intent);
        }
    }

    private boolean isBaseHost(String host) {
        return (host.contains(BASE_URL) || host.contains(BASE_MOBILE_URL));
    }

    private View.OnClickListener onPromoClicked(final String url) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse(url);
                    String host = uri.getHost();
                    List<String> linkSegment = uri.getPathSegments();
                    if (isBaseHost(host) && isShop(linkSegment)) {
                        String shopDomain = linkSegment.get(0);
                        getShopInfo(url,shopDomain);
                    } else {
                       openWebViewURL(url);
                    }

                } catch (Exception e) {
                    openWebViewURL(url);
                }
            }
        };
    }

    private View.OnClickListener onPromoLinkClicked(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BannerWebView.class);
                intent.putExtra("url", "https://www.tokopedia.com/promo/?flag_app=1");
                startActivity(intent);
            }
        };
    }

    @Override
    public void onCategoryClicked(CategoryItemModel categoryItemModel, int position) {
        if (categoryItemModel != null && categoryItemModel.getRedirectValue() != null)
            navigateToNextActivity(categoryItemModel.getRedirectValue(), categoryItemModel.getName());
    }

    @Override
    public void onGimmicClicked(CategoryItemModel categoryItemModel) {
        String redirectUrl = categoryItemModel.getRedirectValue();
        if (redirectUrl != null && redirectUrl.length() > 0) {
            String resultGenerateUrl = URLGenerator.generateURLSessionLogin(
                    categoryItemModel.getRedirectValue(), MainApplication.getAppContext());

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
    private void initBrands(){
        CommonUtils.dumper("mohito called init brands");
        holder.brandsRecyclerView = (RecyclerView) holder.MainView.findViewById(R.id.rv_brands_list);
        holder.tvBrandsTitle = (TextView) holder.MainView.findViewById(R.id.tv_title);
        holder.tvBrandsTitle.setText("Official Store");
        holder.brandsRecyclerView.setHasFixedSize(true);
        holder.brandsRecyclerView.setNestedScrollingEnabled(false);
        brandsRecyclerViewAdapter = new BrandsRecyclerViewAdapter(getContext(), new BrandsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(String name, Brand brand, int position) {
                CommonUtils.dumper("mohito, clicked brands "+brand.getShopAppsUrl());
            }
        });
        brandsRecyclerViewAdapter.setHomeMenuWidth(getHomeBrandsWidth());
        holder.brandsRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(),
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
        topPicksAdapter = new TopPicksAdapter(getContext(),this,this,this);
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
        if (url!="") {
            startActivity(TopPicksWebView.newInstance(getActivity(),url));
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
        LocalCacheHandler handler = new LocalCacheHandler(getActivity(), "tabSelection");
        if (handler.getInt("rechargeSelectedPosition") != null && handler.getInt("rechargeSelectedPosition")<rechargeCategory.getData().size()) {
            holder.viewpagerRecharge.setCurrentItem(handler.getInt("rechargeSelectedPosition"));
            LocalCacheHandler.clearCache(getActivity(), "tabSelection");
        } else {
            holder.viewpagerRecharge.setCurrentItem(0);
        }
        addTablayoutListener(rechargeViewPagerAdapter);

    }


    @Override
    public void failedRenderDataRechargeCategory() {
        ((LinearLayout) holder.tabLayoutRecharge.getParent()).setVisibility(View.GONE);
    }

    @Override
    public void renderErrorNetwork() {

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
                CommonUtils.dumper("GAv4 " + tab.getPosition() + " " + tab.getText());
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
            CommonUtils.dumper("LocalTag : CategoryApi");
            String screenName = AppScreen.SCREEN_HOME_CATEGORY;
            ScreenTracking.screenLoca(screenName);
            ScreenTracking.eventLoca(screenName);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    protected class PromoImagePagerAdapter extends PagerAdapter {

        List<FacadePromo.PromoItem> promoList = new ArrayList<>();

        PromoImagePagerAdapter(List<FacadePromo.PromoItem> promoList) {
            this.promoList = promoList;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.image_slider, container, false);

            ImageView promoImage = (ImageView) view.findViewById(R.id.image);
            promoImage.setOnClickListener(onPromoClicked(promoList.get(position).promoUrl));
            loadImageTemp(promoImage, promoList.get(position).imgUrl);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return promoList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


    }

    public void scrollUntilBottomBanner() {
        holder.wrapperScrollview.smoothScrollTo(0, holder.banner != null ? holder.banner.getBottom() : 0);
    }

    private void loadImageTemp(ImageView imageview, String url) {
        ImageHandler.LoadImage(
                imageview,
                url
        );

    }

    private int getHomeMenuWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int widthOfHomeMenuView = (int) (width / 2);
        return widthOfHomeMenuView;
    }

    private int getHomeBrandsWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int widthOfHomeMenuView = (width / 4);
        return widthOfHomeMenuView;
    }

}