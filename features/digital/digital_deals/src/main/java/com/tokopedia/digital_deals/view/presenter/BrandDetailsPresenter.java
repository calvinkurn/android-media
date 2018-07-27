package com.tokopedia.digital_deals.view.presenter;


import android.support.v7.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.digital_deals.domain.getusecase.GetBrandDetailsUseCase;
import com.tokopedia.digital_deals.view.contractor.BrandDetailsContract;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.Page;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.model.response.BrandDetailsResponse;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

;


public class BrandDetailsPresenter extends BaseDaggerPresenter<BrandDetailsContract.View>
        implements BrandDetailsContract.Presenter {

    public final static String TAG = "url";
    public final static String BRAND_DATA = "brand_data";
    private GetBrandDetailsUseCase getBrandDetailsUseCase;
    private List<ProductItem> categoryViewModels;
    private Brand brand;
    private boolean isLoading;
    private boolean isLastPage;
    private RequestParams searchNextParams;
    private Page page;

    @Inject
    public BrandDetailsPresenter(GetBrandDetailsUseCase getBrandDetailsUseCase) {
        this.getBrandDetailsUseCase = getBrandDetailsUseCase;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {
        getBrandDetailsUseCase.unsubscribe();
    }


    public void getBrandDetails(boolean showProgress) {

        if (showProgress) {
            getView().showProgressBar();
            getView().hideCollapsingHeader();
        }
        searchNextParams = getView().getParams();
        getBrandDetailsUseCase.setRequestParams(getView().getParams());
        getBrandDetailsUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
                e.printStackTrace();
                getView().hideProgressBar();
                getView().hideCollapsingHeader();
                NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getBrandDetails(showProgress);
                    }
                });
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<BrandDetailsResponse>>() {
                }.getType();

                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                BrandDetailsResponse dealEntity = (BrandDetailsResponse) dataResponse.getData();

                getView().hideProgressBar();
                getView().showCollapsingHeader();

                if (dealEntity.getDealItems() != null) {
                    categoryViewModels = dealEntity.getDealItems();
                }
                if (dealEntity.getDealBrand() != null) {
                    brand = dealEntity.getDealBrand();
                }
                if (dealEntity.getPage() != null) {
                    page = dealEntity.getPage();
                }

                getNextPageUrl();
                getView().renderBrandDetails(categoryViewModels, brand, dealEntity.getCount());
                CommonUtils.dumper("enter onNext");
            }
        });
    }

    private void loadMoreItems() {
        isLoading = true;
        searchNextParams.putBoolean("search_next", true);
        getBrandDetailsUseCase.setRequestParams(searchNextParams);
        getBrandDetailsUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                isLoading = false;
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<BrandDetailsResponse>>() {
                }.getType();

                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                BrandDetailsResponse dealEntity = (BrandDetailsResponse) dataResponse.getData();

                isLoading = false;
                List<ProductItem> categoryList = dealEntity.getDealItems();
                page = dealEntity.getPage();
                getView().removeFooter();
                getNextPageUrl();
                getView().addDealsToCards(categoryList);
                checkIfToLoad(getView().getLayoutManager());
            }
        });
    }

    private void getNextPageUrl() {
        if (page != null) {
            String nexturl = page.getUriNext();
            if (nexturl != null && !nexturl.isEmpty() && nexturl.length() > 0) {
                searchNextParams.putString(TAG, nexturl);
                isLastPage = false;
            } else {
                isLastPage = true;
            }
        }
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

    public void onRecyclerViewScrolled(LinearLayoutManager layoutManager) {
        checkIfToLoad(layoutManager);
    }
}
