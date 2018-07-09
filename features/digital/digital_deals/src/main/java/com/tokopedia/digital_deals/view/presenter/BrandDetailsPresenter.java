package com.tokopedia.digital_deals.view.presenter;


import android.support.v7.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.digital_deals.domain.getusecase.GetBrandDetailsUseCase;
import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDetailsDomain;
import com.tokopedia.digital_deals.view.contractor.BrandDetailsContract;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.BrandViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.PageViewModel;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;


public class BrandDetailsPresenter extends BaseDaggerPresenter<BrandDetailsContract.View>
        implements BrandDetailsContract.Presenter {

    public final static String TAG = "url";
    public final static String BRAND_DATA = "brand_data";
    private GetBrandDetailsUseCase getBrandDetailsUseCase;
    private List<CategoryItemsViewModel> categoryViewModels;
    private BrandViewModel brandViewModel;
    private boolean isLoading;
    private boolean isLastPage;
    private final int PAGE_SIZE = 20;
    private RequestParams searchNextParams;
    private PageViewModel pageViewModel;

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


    public void getBrandDetails() {

        getView().showProgressBar();
        getView().hideCollapsingHeader();
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
                        getBrandDetails();
                    }
                });
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<BrandDetailsDomain>>(){
                }.getType();

                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                BrandDetailsDomain dealEntity = (BrandDetailsDomain) dataResponse.getData();

                getView().hideProgressBar();
                getView().showCollapsingHeader();

                categoryViewModels = Utils.getSingletonInstance()
                        .convertIntoCategoryListItemsViewModel(dealEntity.getDealItems());
                if(dealEntity.getDealBrand()!=null) {
                    brandViewModel = Utils.getSingletonInstance().convertIntoBrandViewModel(dealEntity.getDealBrand());
                }
                pageViewModel = Utils.getSingletonInstance().convertIntoPageViewModel(dealEntity.getPage());

                getNextPageUrl();
                getView().renderBrandDetails(categoryViewModels, brandViewModel, dealEntity.getCount());
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
                Type token = new TypeToken<DataResponse<BrandDetailsDomain>>(){
                }.getType();

                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                BrandDetailsDomain dealEntity = (BrandDetailsDomain) dataResponse.getData();

                isLoading = false;
                ArrayList<CategoryItemsViewModel> categoryList = Utils.getSingletonInstance()
                        .convertIntoCategoryListItemsViewModel(dealEntity.getDealItems());
                pageViewModel = Utils.getSingletonInstance().convertIntoPageViewModel(dealEntity.getPage());
                getView().removeFooter();
                getNextPageUrl();
                getView().addDealsToCards(categoryList);
                checkIfToLoad(getView().getLayoutManager());
            }
        });
    }

    void getNextPageUrl() {

        String nexturl = pageViewModel.getUriNext();
        if (nexturl != null && !nexturl.isEmpty() && nexturl.length() > 0) {
            searchNextParams.putString(TAG, nexturl);
            isLastPage = false;
        } else {
            isLastPage = true;
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
