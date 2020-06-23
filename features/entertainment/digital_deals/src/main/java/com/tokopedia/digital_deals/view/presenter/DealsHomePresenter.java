package com.tokopedia.digital_deals.view.presenter;


import android.content.Intent;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.domain.getusecase.GetAllBrandsUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetCategoryDetailRequestUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetDealsListRequestUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetInitialLocationUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetLocationListRequestUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetNearestLocationUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetNextDealPageUseCase;
import com.tokopedia.digital_deals.domain.postusecase.PostNsqEventUseCase;
import com.tokopedia.digital_deals.view.TopDealsCacheHandler;
import com.tokopedia.digital_deals.view.activity.AllBrandsActivity;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.activity.DealsSearchActivity;
import com.tokopedia.digital_deals.view.contractor.DealsContract;
import com.tokopedia.digital_deals.view.customview.WrapContentHeightViewPager;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.CategoriesModel;
import com.tokopedia.digital_deals.view.model.CategoryItem;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.model.nsqevents.NsqMessage;
import com.tokopedia.digital_deals.view.model.nsqevents.NsqServiceModel;
import com.tokopedia.digital_deals.view.model.response.AllBrandsResponse;
import com.tokopedia.digital_deals.view.model.response.DealsResponse;
import com.tokopedia.digital_deals.view.model.response.LocationResponse;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class DealsHomePresenter extends BaseDaggerPresenter<DealsContract.View>
        implements DealsContract.Presenter {

    private int currentPage, totalPages;
    private final int PAGE_SIZE = 20;
    private boolean isLoading;
    private boolean isLastPage;
    private volatile boolean isDealsLoaded = false;
    private volatile boolean isBrandsLoaded = false;

    public final static String TAG = "url";
    private final String CAROUSEL = "carousel";
    private final String TOP = "top";
    private GetNearestLocationUseCase getNearestLocationUseCase;
    private GetDealsListRequestUseCase getDealsListRequestUseCase;
    private GetInitialLocationUseCase getInitialLocationUseCase;
    private GetAllBrandsUseCase getAllBrandsUseCase;
    private GetNextDealPageUseCase getNextDealPageUseCase;
    private ArrayList<CategoryItem> categoryItems;
    private PostNsqEventUseCase postNsqEventUseCase;
    private ArrayList<CategoryItem> curatedItems;
    private List<Brand> brands;
    private List<CategoriesModel> categoriesModels;
    private WrapContentHeightViewPager mTouchViewPager;
    private RequestParams searchNextParams = RequestParams.create();
    private Subscription subscription;
    RequestParams params = RequestParams.create();
    private UserSessionInterface userSession;
    private DealsAnalytics dealsAnalytics;
    private boolean isTopLocations = true;

    private GetLocationListRequestUseCase getSearchLocationListRequestUseCase;
    private GetCategoryDetailRequestUseCase getCategoryDetailRequestUseCase;
    private List<Location> mTopLocations;


    @Inject
    public DealsHomePresenter(GetDealsListRequestUseCase getDealsListRequestUseCase, GetAllBrandsUseCase getAllBrandsUseCase, GetNextDealPageUseCase getNextDealPageUseCase, GetLocationListRequestUseCase getSearchLocationListRequestUseCase, GetCategoryDetailRequestUseCase getCategoryDetailRequestUseCase, GetInitialLocationUseCase getInitialLocationUseCase, PostNsqEventUseCase postNsqEventUseCase, GetNearestLocationUseCase getNearestLocationUseCase, DealsAnalytics dealsAnalytics) {
        this.getDealsListRequestUseCase = getDealsListRequestUseCase;
        this.getAllBrandsUseCase = getAllBrandsUseCase;
        this.getNextDealPageUseCase = getNextDealPageUseCase;
        this.getSearchLocationListRequestUseCase = getSearchLocationListRequestUseCase;
        this.getCategoryDetailRequestUseCase = getCategoryDetailRequestUseCase;
        this.getInitialLocationUseCase = getInitialLocationUseCase;
        this.postNsqEventUseCase = postNsqEventUseCase;
        this.getNearestLocationUseCase = getNearestLocationUseCase;
        this.dealsAnalytics = dealsAnalytics;
    }

    @Override
    public void initialize() {
        userSession = new UserSession(getView().getActivity());
    }

    @Override
    public void onDestroy() {
        getAllBrandsUseCase.unsubscribe();
        getDealsListRequestUseCase.unsubscribe();
        getNextDealPageUseCase.unsubscribe();
        if (getSearchLocationListRequestUseCase != null) {
            getSearchLocationListRequestUseCase.unsubscribe();
        }
        if (getCategoryDetailRequestUseCase != null) {
            getCategoryDetailRequestUseCase.unsubscribe();
        }
        if (getInitialLocationUseCase != null) {
            getInitialLocationUseCase.unsubscribe();
        }
        if (postNsqEventUseCase != null) {
            postNsqEventUseCase.unsubscribe();
        }
        if (getNearestLocationUseCase != null) {
            getNearestLocationUseCase.unsubscribe();
        }
        stopBannerSlide();
    }

    @Override
    public void startBannerSlide(WrapContentHeightViewPager viewPager) {
        this.mTouchViewPager = viewPager;
        currentPage = viewPager.getCurrentItem();
        try {
            totalPages = viewPager.getAdapter().getCount();
        } catch (Exception e) {
            e.printStackTrace();
            totalPages = viewPager.getChildCount();
        }

        stopBannerSlide();
        subscription = Observable.interval(5000, 5000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (currentPage + 1 < totalPages)
                            ++currentPage;
                        else if (currentPage + 1 >= totalPages) {
                            currentPage = 0;
                        }
                        mTouchViewPager.setCurrentItem(currentPage, true);
                        sendEvent();
                    }
                });


    }

    private void sendEvent() {
        if (getView() == null)
            return;
        CategoryItem carousel = getCarouselOrTop(categoryItems, CAROUSEL);
        ProductItem productItem = null;
        if (carousel != null) {
            productItem = carousel.getItems().get(currentPage);
        } else {
            return;
        }
        if (productItem == null)
            return;
    }

    @Override
    public void onBannerSlide(int page) {
        sendEvent();
        currentPage = page;
    }

    @Override
    public boolean onOptionMenuClick(int id) {
        if (id == com.tokopedia.digital_deals.R.id.search_input_view || id == com.tokopedia.digital_deals.R.id.action_menu_search) {
            dealsAnalytics.sendSearchClickedEvent(getView().getSearchInputText());
            if (userSession.isLoggedIn()) {
                sendNSQEvent(userSession.getUserId(), "search");
            }
            Intent searchIntent = new Intent(getView().getActivity(), DealsSearchActivity.class);
            TopDealsCacheHandler.init().setTopDeals(getCarouselOrTop(categoryItems, TOP).getItems());
            searchIntent.putParcelableArrayListExtra(AllBrandsActivity.EXTRA_LIST, (ArrayList<? extends Parcelable>) categoriesModels);
            getView().navigateToActivityRequest(searchIntent, DealsHomeActivity.REQUEST_CODE_DEALSSEARCHACTIVITY);
        } else if (id == com.tokopedia.digital_deals.R.id.tv_location_name || id == com.tokopedia.digital_deals.R.id.toolbar_title) {
            getView().startLocationFragment();
        } else if (id == com.tokopedia.digital_deals.R.id.action_menu_favourite) {

        } else if (id == com.tokopedia.digital_deals.R.id.action_promo) {
            dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_PROMO,
                    "");
            getView().startGeneralWebView(DealsUrl.WebUrl.PROMOURL);
        } else if (id == com.tokopedia.digital_deals.R.id.action_booked_history) {
            dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_DAFTAR_TRANSAKSI,
                    "");
            if (userSession.isLoggedIn()) {
                getView().startOrderListActivity();
            } else {
                Intent intent = RouteManager.getIntent(getView().getActivity(), ApplinkConst.LOGIN);
                getView().navigateToActivityRequest(intent, getView().getRequestCode());
            }
        } else if (id == com.tokopedia.digital_deals.R.id.action_faq) {
            dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_BANTUAN,
                    "");
            getView().startGeneralWebView(DealsUrl.WebUrl.FAQURL);
        } else if (id == com.tokopedia.digital_deals.R.id.tv_see_all_brands) {
            dealsAnalytics.sendAllBrandsClickEvent(DealsAnalytics.SEE_ALL_BRANDS_HOME);
            Intent brandIntent = new Intent(getView().getActivity(), AllBrandsActivity.class);
            brandIntent.putParcelableArrayListExtra(AllBrandsActivity.EXTRA_LIST, (ArrayList<? extends Parcelable>) categoriesModels);
            getView().navigateToActivity(brandIntent);
        }
        else {
            getView().getActivity().onBackPressed();
        }
        return true;
    }

    @Override
    public void onRecyclerViewScrolled(LinearLayoutManager layoutManager) {
        checkIfToLoad(layoutManager);
    }

    public void getDealsList(boolean showProgressBar) {
        if (getView() == null) {
            return;
        }
        if (showProgressBar) {
            getView().showProgressBar();
            params.putAll(getView().getParams().getParameters());
            getDealsListRequestUseCase.setRequestParams(params);
        }
        getDealsListRequestUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
                if(isDealsLoaded){
                    getView().renderCarousels(getCarouselOrTop(categoryItems, CAROUSEL));
                }
            }

            @Override
            public void onError(Throwable e) {
                if (getView() == null) {
                    return;
                }
                Timber.d("enter error");
                e.printStackTrace();
                getView().hideProgressBar();
                NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getDealsList(true);
                    }
                });
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {

                if (getView() == null) {
                    return;
                }

                Type token = new TypeToken<DataResponse<DealsResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);

                if (restResponse == null || restResponse.isError()) {
                    getView().hideProgressBar();
                    NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            getDealsList(true);
                        }
                    });
                    return;
                }

                DataResponse data = restResponse.getData();

                DealsResponse dealsResponse = (DealsResponse) data.getData();

                processSearchResponse(dealsResponse);
                isDealsLoaded = true;
                getView().hideProgressBar();
                getView().renderTopDeals(getCarouselOrTop(categoryItems, TOP));

                getView().renderCategoryList(getCategories(dealsResponse.getCategoryItems()), categoriesModels);
                getView().renderCuratedDealsList(getCuratedDeals(dealsResponse.getCategoryItems(), TOP));
            }
        });
    }


    public void getBrandsHome() {
        if (getView() == null) {
            return;
        }
        RequestParams params = RequestParams.create();
        params.putAll(getView().getBrandParams().getParameters());
        getAllBrandsUseCase.setRequestParams(params);
        getAllBrandsUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
                Timber.d("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                if (getView() == null) {
                    return;
                }
                Timber.d("enter error");
                e.printStackTrace();
                getView().hideProgressBar();
                NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getBrandsHome();
                    }
                });
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                if (getView() == null) {
                    return;
                }
                Type token2 = new TypeToken<DataResponse<AllBrandsResponse>>() {
                }.getType();

                RestResponse restResponse2 = typeRestResponseMap.get(token2);
                DataResponse data2 = restResponse2.getData();
                if (data2 != null) {
                    AllBrandsResponse brandsResponse = (AllBrandsResponse) data2.getData();
                    brands = brandsResponse.getBrands();
                    getView().renderBrandList(brands);
                }
                isBrandsLoaded = true;
                showHideViews();
                Timber.d("enter onNext");
            }
        });
    }

    private void loadMoreItems() {
        isLoading = true;

        getNextDealPageUseCase.setRequestParams(searchNextParams);
        getDealsListRequestUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                isLoading = false;

            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                if (getView() == null) {
                    return;
                }
                isLoading = false;
                Type token = new TypeToken<DataResponse<DealsResponse>>() {
                }.getType();


                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse data = restResponse.getData();
                DealsResponse dealsResponse = (DealsResponse) data.getData();
                processSearchResponse(dealsResponse);
                getView().addDealsToCards(getCarouselOrTop(categoryItems, TOP));
                checkIfToLoad(getView().getLayoutManager());
            }
        });

    }

    private void showHideViews() {
        if (isBrandsLoaded && isDealsLoaded) {
            getView().showFavouriteButton();
            getView().hideProgressBar();
            getView().showViews();
        }
    }

    public void onClickBanner(ProductItem productItem) {
        if (!TextUtils.isEmpty(productItem.getSeoUrl())) {
            RouteManager.route(getView().getActivity(), productItem.getSeoUrl());
        }
    }


    private void checkIfToLoad(LinearLayoutManager layoutManager) {
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE) {
                loadMoreItems();
            } else {
                getView().addFooter();
            }
        }
    }

    private CategoryItem getCarouselOrTop(List<CategoryItem> categoryList, String carouselOrTop) {

        if (categoryList.get(1).getName().equalsIgnoreCase(carouselOrTop)) {
            return categoryList.get(1);
        } else {
            return categoryList.get(0);
        }
    }

    private List<CategoryItem> getCuratedDeals(List<CategoryItem> categoryItems, String carouselOrTop) {
        curatedItems = new ArrayList<>();
        for (CategoryItem categoryItem : categoryItems) {
            if (categoryItem.getIsCard() == 1 && !categoryItem.getName().equalsIgnoreCase(carouselOrTop)) {
                curatedItems.add(categoryItem);
            }
        }
        return curatedItems;
    }

    private List<CategoryItem> getCategories(List<CategoryItem> listItems) {
        applyFilterOnCategories(listItems);
        List<CategoryItem> categoryList = null;
        categoriesModels = new ArrayList<>();
        if (listItems != null && listItems.size() > 2) {
            categoryList = new ArrayList<>();
            for (int i = 0; i < listItems.size() -1; i++) {
                if (listItems.get(i).getIsCard() != 1 && !listItems.get(i).getTitle().equalsIgnoreCase(CAROUSEL)) {
                    categoryList.add(listItems.get(i));
                    CategoriesModel categoriesModel = new CategoriesModel();
                    categoriesModel.setName(listItems.get(i).getName());
                    categoriesModel.setTitle(listItems.get(i).getTitle());
                    categoriesModel.setCategoryUrl(listItems.get(i).getCategoryUrl());
                    categoriesModel.setPosition(i);
                    categoriesModel.setCategoryId(listItems.get(i).getCategoryId());
                    categoriesModel.setItems(listItems.get(i).getItems());
                    categoriesModels.add(categoriesModel);
                }
            }
        }


        CategoriesModel categoriesModel = new CategoriesModel();
        categoriesModel.setCategoryUrl("");
        categoriesModel.setTitle(getView().getActivity().getResources().getString(com.tokopedia.digital_deals.R.string.all_brands));
        categoriesModel.setName(getView().getActivity().getResources().getString(com.tokopedia.digital_deals.R.string.all_brands));
        categoriesModel.setPosition(0);
        categoriesModels.add(0, categoriesModel);
        return categoryList;
    }


    public void applyFilterOnCategories(List<CategoryItem> categoryRespons) {
        Map<Integer, Integer> sortOrder = new HashMap<>();
        for (CategoryItem categoryItem : categoryRespons) {
            sortOrder.put(categoryItem.getCategoryId(), categoryItem.getPriority());
            if (sortOrder.size() == categoryRespons.size()) {
                Collections.sort(categoryRespons, new CategoryItemComparator(sortOrder));
            }
        }
    }

    public void sendCategoryClickEvent(String name, int position) {
        dealsAnalytics.sendCategoryClickEventHome(name,  position);
    }

    public void sendSeeAllTrendingDealsEvent() {
        dealsAnalytics.sendSeeAllTrendingDeals();
    }

    private class CategoryItemComparator implements Comparator<CategoryItem> {
        private Map<Integer, Integer> sortOrder;

        public CategoryItemComparator(Map<Integer, Integer> sortOrder) {
            this.sortOrder = sortOrder;
        }

        @Override
        public int compare(CategoryItem i1, CategoryItem i2) {
            Integer id1 = sortOrder.get(i1.getCategoryId());
            if (id1 == null) {
                throw new IllegalArgumentException("Bad id encountered: " +
                        i1.getCategoryId());
            }
            Integer id2 = sortOrder.get(i2.getCategoryId());
            if (id2 == null) {
                throw new IllegalArgumentException("Bad id encountered: " +
                        i2.getCategoryId());
            }
            return id2.compareTo(id1);
        }
    }

    private void processSearchResponse(DealsResponse dealEntity) {
        JsonObject layout = dealEntity.getHome().getLayout();
        List<CategoryItem> dealsCategoryDomains = new ArrayList<>();
        CategoryItem dealsCategoryDomain;
        if (layout != null) {
            for (Map.Entry<String, JsonElement> entry : layout.entrySet()) {
                JsonObject object = entry.getValue().getAsJsonObject();
                dealsCategoryDomain = new Gson().fromJson(object, CategoryItem.class);
                dealsCategoryDomains.add(dealsCategoryDomain);
            }
        }
        dealEntity.setCategoryItems(dealsCategoryDomains);

        categoryItems = Utils.getSingletonInstance()
                .convertIntoCategoryListViewModel(dealsCategoryDomains);
    }

    private void stopBannerSlide() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public void sendEventView(String action, String label) {
        dealsAnalytics.sendEventDealsDigitalView(action, label);
    }

    public void getLocations() {
        RequestParams params = RequestParams.create();
        params.putInt("id", Utils.LOCATION_ID);
        getInitialLocationUseCase.setRequestParams(params);
        getInitialLocationUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                if (getView() == null) {
                    return;
                }
                Type token = new TypeToken<DataResponse<LocationResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                LocationResponse locationResponse = (LocationResponse) dataResponse.getData();
                if (locationResponse != null && locationResponse.getLocations() != null) {
                    getView().updateInitialLocation(locationResponse.getLocations());
                }
            }
        });
    }

    public void sendScreenNameEvent(String screenName) {
        dealsAnalytics.sendScreenNameEvent(screenName);
    }

    public void sendNSQEvent(String userId, String action) {
        NsqServiceModel nsqServiceModel = new NsqServiceModel();
        nsqServiceModel.setService(Utils.NSQ_SERVICE);
        NsqMessage nsqMessage = new NsqMessage();
        nsqMessage.setUserId(Integer.parseInt(userId));
        nsqMessage.setUseCase(Utils.NSQ_USE_CASE);
        nsqMessage.setAction(action);
        nsqServiceModel.setMessage(nsqMessage);
        postNsqEventUseCase.setRequestModel(nsqServiceModel);
        postNsqEventUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Timber.d(e);
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
            }
        });
    }


    public void getNearestLocation(String coordinates) {
        if (getView() == null) {
            return;
        }
        RequestParams params = RequestParams.create();
        params.putString(Utils.LOCATION_COORDINATES, coordinates);
        getNearestLocationUseCase.setRequestParams(params);
        getNearestLocationUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<LocationResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                LocationResponse locationResponse = (LocationResponse) dataResponse.getData();
                if (locationResponse != null && locationResponse.getLocations() != null) {
                    getView().updateInitialLocation(locationResponse.getLocations());
                } else {
                    getView().showErrorMessage();
                    getView().setDefaultLocation();
                }
            }
        });
    }
}
