package com.tokopedia.digital_deals.view.presenter;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.domain.getusecase.GetAllBrandsUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetCategoryDetailRequestUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetLocationListRequestUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetNextCategoryPageUseCase;
import com.tokopedia.digital_deals.view.TopDealsCacheHandler;
import com.tokopedia.digital_deals.view.contractor.DealsCategoryDetailContract;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.Page;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.model.response.AllBrandsResponse;
import com.tokopedia.digital_deals.view.model.response.CategoryDetailsResponse;
import com.tokopedia.digital_deals.view.model.response.LocationResponse;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.observers.Subscribers;
import rx.schedulers.Schedulers;

;

public class DealsCategoryDetailPresenter extends BaseDaggerPresenter<DealsCategoryDetailContract.View>
        implements DealsCategoryDetailContract.Presenter {

    public final static String TAG = "url";
    public static final String FROM_DEEPLINK = "from_deeplink";
    private boolean isLoading;
    private boolean isLastPage;
    private volatile boolean isDealsLoaded = false;
    private volatile boolean isBrandsLoaded = false;

    private GetAllBrandsUseCase getAllBrandsUseCase;
    private GetLocationListRequestUseCase getLocationListRequestUseCase;
    private GetCategoryDetailRequestUseCase getCategoryDetailRequestUseCase;
    private GetNextCategoryPageUseCase getNextCategoryPageUseCase;
    private List<ProductItem> productItems;
    private List<Brand> brands;
    private Page page;
    private RequestParams searchNextParams = RequestParams.create();
    private Subscription loadSubscription;


    @Inject
    public DealsCategoryDetailPresenter(GetCategoryDetailRequestUseCase getCategoryDetailRequestUseCase, GetNextCategoryPageUseCase getNextCategoryPageUseCase, GetAllBrandsUseCase getAllBrandsUseCase, GetLocationListRequestUseCase getLocationListRequestUseCase) {
        this.getCategoryDetailRequestUseCase = getCategoryDetailRequestUseCase;
        this.getNextCategoryPageUseCase = getNextCategoryPageUseCase;
        this.getAllBrandsUseCase = getAllBrandsUseCase;
        this.getLocationListRequestUseCase = getLocationListRequestUseCase;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {
        getCategoryDetailRequestUseCase.unsubscribe();
        getNextCategoryPageUseCase.unsubscribe();
        getAllBrandsUseCase.unsubscribe();
    }

    @Override
    public boolean onOptionMenuClick(int id) {
//        if (id == R.id.action_menu_search) {
//            setTopDeals();
//            getView().checkLocationStatus();
//        } else {
//            getView().getActivity().onBackPressed();
//        }
        return true;
    }

    @Override
    public void searchSubmitted(String searchText) {
        getBrandsList(true);
        getCategoryDetails(true);
    }

    public void setTopDeals() {
        int size = 5;
        if (productItems.size() < size) {
            size = productItems.size();
        }
        ArrayList<ProductItem> searchItems = new ArrayList<ProductItem>();
        for (int i = 0; i < size; i++) {
            searchItems.add(productItems.get(i));
        }
        TopDealsCacheHandler.init().setTopDeals(searchItems);
    }

    @Override
    public void onRecyclerViewScrolled(LinearLayoutManager layoutManager) {
        if (loadSubscription == null) {
            Log.d("DealsPresenter", "subscribed");
            loadSubscription = checkToLoadAsync(layoutManager);
        } else {
            if (loadSubscription != null) {
                Log.d("DealsPresenter", "Subscription Exists");
                if (!loadSubscription.isUnsubscribed()) {
                    Log.d("DealsPresenter", "Subscription Unsubscribed");
                    loadSubscription.unsubscribe();
                }
                loadSubscription = checkToLoadAsync(layoutManager);
            }
        }
    }

    public void getBrandsList(boolean showProgressBar) {
        if (showProgressBar)
            getView().showProgressBar();
        getAllBrandsUseCase.setRequestParams(getView().getBrandParams());
        getAllBrandsUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
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
                        getBrandsList(true);
                    }
                });
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<AllBrandsResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse data = restResponse.getData();
                AllBrandsResponse dealEntity = (AllBrandsResponse) data.getData();
                isBrandsLoaded = true;
                brands = dealEntity.getBrands();
                getView().renderBrandList(brands);
                showHideViews();
                CommonUtils.dumper("enter onNext");
            }
        });
    }

    public void getCategoryDetails(boolean showProgressBar) {
        if (showProgressBar)
            getView().showProgressBar();
        getCategoryDetailRequestUseCase.setRequestParams(getView().getCategoryParams());
        getCategoryDetailRequestUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
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
                        getCategoryDetails(true);
                    }
                });
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<CategoryDetailsResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                CategoryDetailsResponse dealEntity = (CategoryDetailsResponse) dataResponse.getData();
                isDealsLoaded = true;
                productItems = dealEntity.getDealItems();
                page = dealEntity.getPage();
                getNextPageUrl();
                getView().renderCategoryList(productItems, dealEntity.getCount());
                checkToLoadAsync(getView().getLayoutManager());
                showHideViews();
            }
        });
    }


    private void loadMoreItems() {
        isLoading = true;

        getNextCategoryPageUseCase.setRequestParams(searchNextParams);
        getNextCategoryPageUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                isLoading = false;
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<CategoryDetailsResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                CategoryDetailsResponse categoryDetailsResponse = (CategoryDetailsResponse) dataResponse.getData();
                isLoading = false;
                List<ProductItem> productItems = categoryDetailsResponse.getDealItems();
                page = categoryDetailsResponse.getPage();
                getView().removeFooter();
                getNextPageUrl();
                getView().addDealsToCards(productItems);
                //checkIfToLoad(getView().getLayoutManager());
            }
        });
    }

    private Boolean checkIfToLoad(LinearLayoutManager layoutManager) {

        Log.d("DealsPresenter", Thread.currentThread().getName());
        int visibleItemCount = layoutManager.findLastVisibleItemPosition();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        Log.d("DealsPreseneterItems", "Visible Items = "
                + visibleItemCount + "Total item count = "
                + totalItemCount + "First item = " + firstVisibleItemPosition);
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition + 1) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private Subscription checkToLoadAsync(LinearLayoutManager layoutManager) {
        return Observable.fromCallable(() -> {
            return checkIfToLoad(layoutManager);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            Log.d("DealsPresenter", "LoadMore");
                            if (!isLoading) {
                                isLoading = true;
                                loadMoreItems();
                            }
                        } else
                            getView().addFooter();
                    }
                });
    }

    private void getNextPageUrl() {
        if (page != null) {

            if (!TextUtils.isEmpty(page.getUriNext())) {
                searchNextParams.putString(Utils.NEXT_URL, page.getUriNext());
                isLastPage = false;
            } else {
                isLastPage = true;
            }
        }
    }

    private void showHideViews() {
        if (isBrandsLoaded && isDealsLoaded) {
            getView().hideProgressBar();
            getView().showViews();
        }
    }

    public void getLocations() {
        getLocationListRequestUseCase.setRequestParams(RequestParams.EMPTY);
        getLocationListRequestUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
                e.printStackTrace();
                NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getLocations();
                    }
                });
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<LocationResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                LocationResponse locationResponse = (LocationResponse) dataResponse.getData();
                getView().startLocationFragment(locationResponse.getLocations());
            }
        });
    }

}

