package com.tokopedia.digital_deals.view.presenter;


import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.digital_deals.domain.getusecase.GetBrandDetailsUseCase;
import com.tokopedia.digital_deals.domain.postusecase.PostNsqEventUseCase;
import com.tokopedia.digital_deals.view.contractor.BrandDetailsContract;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.Page;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.model.nsqevents.NsqMessage;
import com.tokopedia.digital_deals.view.model.nsqevents.NsqServiceModel;
import com.tokopedia.digital_deals.view.model.response.BrandDetailsResponse;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import timber.log.Timber;


public class BrandDetailsPresenter extends BaseDaggerPresenter<BrandDetailsContract.View>
        implements BrandDetailsContract.Presenter {

    public final static String TAG = "url";
    public final static String BRAND_DATA = "brand_data";
    private GetBrandDetailsUseCase getBrandDetailsUseCase;
    private PostNsqEventUseCase postNsqEventUseCase;
    private List<ProductItem> categoryViewModels;
    private Brand brand;
    private boolean isLoading;
    private boolean isLastPage;
    private RequestParams searchNextParams;
    private Page page;

    @Inject
    public BrandDetailsPresenter(GetBrandDetailsUseCase getBrandDetailsUseCase, PostNsqEventUseCase postNsqEventUseCase) {
        this.getBrandDetailsUseCase = getBrandDetailsUseCase;
        this.postNsqEventUseCase = postNsqEventUseCase;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {
        getBrandDetailsUseCase.unsubscribe();
        postNsqEventUseCase.unsubscribe();
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
                Timber.d("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Timber.d("enter error");
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
                Timber.d("enter onNext");
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
                Timber.d("enter onCompleted");
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
                Log.d("Naveen", "NSQ Event Sent brand Detail");
            }
        });
    }
}
