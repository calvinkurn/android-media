package com.tokopedia.digital_deals.view.presenter;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.digital_deals.domain.getusecase.GetFavouriteDealsUseCase;
import com.tokopedia.digital_deals.view.contractor.FavouriteDealsContract;
import com.tokopedia.digital_deals.view.model.Page;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.model.response.CategoryDetailsResponse;
import com.tokopedia.digital_deals.view.model.response.FavouriteDealsResponse;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class FavouriteDealsPresenter extends BaseDaggerPresenter<FavouriteDealsContract.View> implements FavouriteDealsContract.Presenter{

    GetFavouriteDealsUseCase getFavouriteDealsUseCase;
    private List<ProductItem> productItems;
    private Page page;
    private boolean isLoading;
    private boolean isLastPage;
    private RequestParams searchNextParams = RequestParams.create();
    @Inject
    public FavouriteDealsPresenter(GetFavouriteDealsUseCase getFavouriteDealsUseCase){
        this.getFavouriteDealsUseCase=getFavouriteDealsUseCase;
    }


    public void getFavouriteDeals() {
        getView().showProgressBar();
        getFavouriteDealsUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
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
                        getFavouriteDeals();
                    }
                });
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<FavouriteDealsResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                FavouriteDealsResponse dealEntity = (FavouriteDealsResponse) dataResponse.getData();

                productItems = dealEntity.getProducts();
                page = dealEntity.getPage();
                getNextPageUrl();
                getView().renderDealsList(productItems);
                getView().hideProgressBar();
                checkIfToLoad(getView().getLayoutManager());

            }
        });
    }


    private void loadMoreItems() {
        isLoading = true;

        getFavouriteDealsUseCase.setRequestParams(searchNextParams);
        getFavouriteDealsUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                isLoading = false;
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<FavouriteDealsResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                FavouriteDealsResponse categoryDetailsResponse = (FavouriteDealsResponse) dataResponse.getData();
                isLoading = false;
                List<ProductItem> productItems = categoryDetailsResponse.getProducts();
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

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {
        getFavouriteDealsUseCase.unsubscribe();
    }

    @Override
    public void onRecyclerViewScrolled(LinearLayoutManager layoutManager) {
            checkIfToLoad(layoutManager);
    }
}
