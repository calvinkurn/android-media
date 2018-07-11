package com.tokopedia.digital_deals.view.presenter;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.domain.getusecase.GetAllBrandsUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetCategoryDetailRequestUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetNextCategoryPageUseCase;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.activity.DealsSearchActivity;
import com.tokopedia.digital_deals.view.contractor.DealsCategoryDetailContract;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.Page;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.model.response.AllBrandsResponse;
import com.tokopedia.digital_deals.view.model.response.CategoryDetailsResponse;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class DealsCategoryDetailPresenter extends BaseDaggerPresenter<DealsCategoryDetailContract.View>
        implements DealsCategoryDetailContract.Presenter {

    private String PROMOURL = "https://www.tokopedia.com/promo/tiket/events/";
    private String FAQURL = "https://www.tokopedia.com/bantuan/faq-tiket-event/";
    private String TRANSATIONSURL = "https://pulsa.tokopedia.com/order-list/";
    public final static String TAG = "url";
    private boolean isLoading;
    private boolean isLastPage;
    private volatile boolean isDealsLoaded = false;
    private volatile boolean isBrandsLoaded = false;

    private GetAllBrandsUseCase getAllBrandsUseCase;
    private GetCategoryDetailRequestUseCase getCategoryDetailRequestUseCase;
    private GetNextCategoryPageUseCase getNextCategoryPageUseCase;
    private List<ProductItem> productItems;
    private List<Brand> brands;
    private Page page;
    private RequestParams searchNextParams = RequestParams.create();


    @Inject
    public DealsCategoryDetailPresenter(GetCategoryDetailRequestUseCase getCategoryDetailRequestUseCase, GetNextCategoryPageUseCase getNextCategoryPageUseCase, GetAllBrandsUseCase getAllBrandsUseCase) {
        this.getCategoryDetailRequestUseCase = getCategoryDetailRequestUseCase;
        this.getNextCategoryPageUseCase = getNextCategoryPageUseCase;
        this.getAllBrandsUseCase = getAllBrandsUseCase;
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
        if (id == R.id.action_menu_search) {
            Intent searchIntent = new Intent(getView().getActivity(), DealsSearchActivity.class);
            int size = 5;
            if (productItems.size() < size) {
                size = productItems.size();
            }
            ArrayList<ProductItem> searchItems = new ArrayList<ProductItem>();
            for (int i = 0; i < size; i++) {
                searchItems.add(productItems.get(i));
            }
            searchIntent.putParcelableArrayListExtra("TOPDEALS", searchItems);
            getView().navigateToActivityRequest(searchIntent, DealsHomeActivity.REQUEST_CODE_DEALSSEARCHACTIVITY);
        } else if (id == R.id.action_promo) {
            getView().startGeneralWebView(PROMOURL);
        } else if (id == R.id.action_booked_history) {
            getView().startGeneralWebView(TRANSATIONSURL);
        } else if (id == R.id.action_faq) {
            getView().startGeneralWebView(FAQURL);
        } else {
            getView().getActivity().onBackPressed();
        }
        return true;
    }

    @Override
    public void onRecyclerViewScrolled(LinearLayoutManager layoutManager) {
        checkIfToLoad(layoutManager);
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
                checkIfToLoad(getView().getLayoutManager());
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
                checkIfToLoad(getView().getLayoutManager());
            }
        });
    }

    private void checkIfToLoad(LinearLayoutManager layoutManager) {
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                loadMoreItems();
            } else {
                getView().addFooter();
            }
        }
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
            getView().showSearchButton();
        }
    }

}

