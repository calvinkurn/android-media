package com.tokopedia.digital_deals.view.presenter;


import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.digital_deals.DealsModuleRouter;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.domain.getusecase.GetAllBrandsUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetDealsListRequestUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetNextDealPageUseCase;
import com.tokopedia.digital_deals.view.activity.AllBrandsActivity;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.activity.DealsLocationActivity;
import com.tokopedia.digital_deals.view.activity.DealsSearchActivity;
import com.tokopedia.digital_deals.view.contractor.DealsContract;
import com.tokopedia.digital_deals.view.customview.WrapContentHeightViewPager;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.CategoriesModel;
import com.tokopedia.digital_deals.view.model.CategoryItem;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.model.response.AllBrandsResponse;
import com.tokopedia.digital_deals.view.model.response.DealsResponse;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

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
    private GetDealsListRequestUseCase getDealsListRequestUseCase;
    private GetAllBrandsUseCase getAllBrandsUseCase;
    private GetNextDealPageUseCase getNextDealPageUseCase;
    private ArrayList<CategoryItem> categoryItems;
    private List<Brand> brands;
    private List<CategoriesModel> categoriesModels;
    private WrapContentHeightViewPager mTouchViewPager;
    private RequestParams searchNextParams = RequestParams.create();
    private Subscription subscription;
    private HashMap<String, Object> params = new HashMap<>();
    private UserSession userSession;
    private DealsAnalytics dealsAnalytics;


    @Inject
    public DealsHomePresenter(GetDealsListRequestUseCase getDealsListRequestUseCase, GetAllBrandsUseCase getAllBrandsUseCase, GetNextDealPageUseCase getNextDealPageUseCase, DealsAnalytics dealsAnalytics) {
        this.getDealsListRequestUseCase = getDealsListRequestUseCase;
        this.getAllBrandsUseCase = getAllBrandsUseCase;
        this.getNextDealPageUseCase = getNextDealPageUseCase;
        this.dealsAnalytics = dealsAnalytics;
    }

    @Override
    public void initialize() {
        userSession = ((AbstractionRouter) getView().getActivity().getApplication()).getSession();

    }

    @Override
    public void onDestroy() {
        getAllBrandsUseCase.unsubscribe();
        getDealsListRequestUseCase.unsubscribe();
        getNextDealPageUseCase.unsubscribe();
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

        if (!productItem.isTrack()) {
            HashMap<String, Object> bannerMap = new HashMap<>();
            bannerMap.put("id", productItem.getId());
            bannerMap.put("name", "/deals - top banner");
            bannerMap.put("position", currentPage);
            bannerMap.put("creative", productItem.getDisplayName());
            HashMap<String, Object> ecommerce = new HashMap<>();
            ecommerce.put(DealsAnalytics.EVENT_PROMO_VIEW, bannerMap);
            sendEventEcommerce(DealsAnalytics.EVENT_PROMO_VIEW
                    , DealsAnalytics.EVENT_IMPRESSION_PROMO_BANNER,
                    String.format("%s - %s", productItem.getDisplayName(), currentPage), ecommerce);
            productItem.setTrack(true);

        }
    }

    @Override
    public void onBannerSlide(int page) {
        sendEvent();
        currentPage = page;
    }

    @Override
    public boolean onOptionMenuClick(int id) {
        if (id == R.id.search_input_view) {
            Intent searchIntent = new Intent(getView().getActivity(), DealsSearchActivity.class);
            searchIntent.putParcelableArrayListExtra("TOPDEALS", (ArrayList<? extends Parcelable>) getCarouselOrTop(categoryItems, TOP).getItems());
            getView().navigateToActivityRequest(searchIntent, DealsHomeActivity.REQUEST_CODE_DEALSSEARCHACTIVITY);
        } else if (id == R.id.tv_location_name) {
            getView().navigateToActivityRequest(new Intent(getView().getActivity(), DealsLocationActivity.class), DealsHomeActivity.REQUEST_CODE_DEALSLOCATIONACTIVITY);
            getView().getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.hold);

        } else if (id == R.id.action_menu_favourite) {

        } else if (id == R.id.action_promo) {
            dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_PROMO,
                    "");
            getView().startGeneralWebView(DealsUrl.WebUrl.PROMOURL);
        } else if (id == R.id.action_booked_history) {
            dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_DAFTAR_TRANSAKSI,
                    "");
            if (userSession.isLoggedIn()) {
                getView().startOrderListActivity();
            } else {
                Intent intent = ((DealsModuleRouter) getView().getActivity().getApplication()).
                        getLoginIntent(getView().getActivity());
                getView().navigateToActivityRequest(intent, getView().getRequestCode());
            }
        } else if (id == R.id.action_faq) {
            dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_BANTUAN,
                    "");
            getView().startGeneralWebView(DealsUrl.WebUrl.FAQURL);
        } else if (id == R.id.tv_see_all_brands) {
            dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_SEE_ALL_BRANDS,
                    "");
            Intent brandIntent = new Intent(getView().getActivity(), AllBrandsActivity.class);
            brandIntent.putParcelableArrayListExtra(AllBrandsActivity.EXTRA_LIST, (ArrayList<? extends Parcelable>) categoriesModels);
            getView().navigateToActivity(brandIntent);
        } else if (id == R.id.see_all_promo) {
            dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_SEE_ALL_PROMO,
                    "");
            getView().startGeneralWebView(DealsUrl.WebUrl.PROMOURL);
        } else {
            getView().getActivity().onBackPressed();
        }
        return true;
    }

    @Override
    public void onRecyclerViewScrolled(LinearLayoutManager layoutManager) {
        checkIfToLoad(layoutManager);
    }

    public void getDealsList(boolean showProgressBar) {
        if (showProgressBar)
            getView().showProgressBar();
        params.putAll(getView().getParams().getParameters());
        params.putAll(getView().getBrandParams().getParameters());
        getDealsListRequestUseCase.setRequestParams(params);
        getDealsListRequestUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
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

                getView().renderCategoryList(getCategories(categoryItems),
                        getCarouselOrTop(categoryItems, CAROUSEL),
                        getCarouselOrTop(categoryItems, TOP));

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
                CommonUtils.dumper("enter onNext");
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

    public void onClickBanner() {
        CategoryItem carousel = getCarouselOrTop(categoryItems, CAROUSEL);
        if (carousel != null) {
            ProductItem productItem = carousel.getItems().get(currentPage);
            if (productItem.getUrl().contains("www.tokopedia.com")
                    || productItem.getUrl().contains("docs.google.com")) {
                getView().startGeneralWebView(productItem.getUrl());
            } else {
                Intent detailsIntent = new Intent(getView().getActivity(), DealDetailsActivity.class);
                detailsIntent.putExtra(DealDetailsPresenter.HOME_DATA, productItem.getSeoUrl());
                getView().navigateToActivity(detailsIntent);
            }
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

        if (categoryList.get(0).getName().equalsIgnoreCase(carouselOrTop)) {
            return categoryList.get(0);
        } else {
            return categoryList.get(1);
        }
    }

    private List<CategoryItem> getCategories(List<CategoryItem> listItems) {
        List<CategoryItem> categoryList = null;
        categoriesModels = new ArrayList<>();
        if (listItems != null && listItems.size() > 2) {
            categoryList = new ArrayList<>();
            for (int i = 2; i < listItems.size(); i++) {
                categoryList.add(listItems.get(i));
                CategoriesModel categoriesModel = new CategoriesModel();
                categoriesModel.setName(listItems.get(i).getName());
                categoriesModel.setTitle(listItems.get(i).getTitle());
                categoriesModel.setUrl(listItems.get(i).getUrl());
                categoriesModel.setPosition(i - 1);
                categoriesModel.setCategoryId(listItems.get(i).getCategoryId());
                categoriesModels.add(categoriesModel);
            }
        }


        CategoriesModel categoriesModel = new CategoriesModel();
        categoriesModel.setUrl("");
        categoriesModel.setTitle(getView().getActivity().getResources().getString(R.string.all_brands));
        categoriesModel.setName(getView().getActivity().getResources().getString(R.string.all_brands));
        categoriesModel.setPosition(0);
        categoriesModels.add(0, categoriesModel);
        return categoryList;
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
                .convertIntoCategoryListViewModel(dealEntity);
    }

    private void stopBannerSlide() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public void sendEventEcommerce(String event, String action, String label,
                       HashMap<String, Object> ecommerce){
        dealsAnalytics.sendEventEcommerce(event, action, label, ecommerce);
    }

    public void sendEventView(String action, String label) {
        dealsAnalytics.sendEventDealsDigitalView(action, label);
    }
}
