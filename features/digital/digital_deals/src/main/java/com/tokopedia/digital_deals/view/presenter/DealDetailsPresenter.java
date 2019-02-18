package com.tokopedia.digital_deals.view.presenter;


import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.domain.getusecase.GetDealDetailsUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetDealLikesUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetSearchNextUseCase;
import com.tokopedia.digital_deals.view.contractor.DealDetailsContract;
import com.tokopedia.digital_deals.view.model.Outlet;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse;
import com.tokopedia.digital_deals.view.model.response.GetLikesResponse;
import com.tokopedia.digital_deals.view.model.response.SearchResponse;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;


public class DealDetailsPresenter extends BaseDaggerPresenter<DealDetailsContract.View>
        implements DealDetailsContract.Presenter {

    private int currentPage, totalPages;
    private GetDealDetailsUseCase getDealDetailsUseCase;
    private GetSearchNextUseCase getSearchNextUseCase;
    private GetDealLikesUseCase getDealLikesUseCase;
    private DealsDetailsResponse dealsDetailsResponse;
    public static final String HOME_DATA = "home_data";
    public final static String TAG = "url";
    public final static boolean DEFAULT_PARAM_ENABLE = true;
    public final static String PARAM_DEAL_PASSDATA = "PARAM_DEAL_PASSDATA";
    private TouchViewPager mTouchViewPager;
    private boolean isLoading;
    private boolean isLastPage;
    private RequestParams searchNextParams = RequestParams.create();


    @Inject
    public DealDetailsPresenter(GetDealDetailsUseCase getDealDetailsUseCase, GetSearchNextUseCase getSearchNextUseCase, GetDealLikesUseCase getDealLikesUseCase) {
        this.getDealDetailsUseCase = getDealDetailsUseCase;
        this.getSearchNextUseCase = getSearchNextUseCase;
        this.getDealLikesUseCase = getDealLikesUseCase;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {
        getDealDetailsUseCase.unsubscribe();
        getSearchNextUseCase.unsubscribe();
    }

    public void getDealDetails() {

        getView().showProgressBar();
        getView().hideCollapsingHeader();
        getDealDetailsUseCase.setRequestParams(getView().getParams());
        getDealDetailsUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
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
                        getDealDetails();
                    }
                });
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse data = restResponse.getData();
                dealsDetailsResponse = (DealsDetailsResponse) data.getData();
                if (dealsDetailsResponse != null )
                    Utils.getSingletonInstance().sortOutletsWithLocation(dealsDetailsResponse.getOutlets(), Utils.getSingletonInstance().getLocation(getView().getActivity()));
                getView().hideProgressBar();
                getView().showShareButton();
                getView().showCollapsingHeader();
                getView().renderDealDetails(dealsDetailsResponse);
                searchNextParams.putString(Utils.NEXT_URL, dealsDetailsResponse.getRecommendationUrl());
                if (getView().isRecommendationEnableFromArguments()){
                    getRecommendedDeals();
                }else {
                    getView().hideRecomendationDealsView();
                }
                CommonUtils.dumper("enter onNext");

                if (!getView().isEnableBuyFromArguments()){
                    getView().hideCheckoutView();
                }

                if (!getView().isEnableLikeFromArguments()){
                    getView().hideLikeButtonView();
                } else {
                    getLikes();
                }

                if (!getView().isEnableShareFromArguments()){
                    getView().hideShareButton();
                }
            }
        });
    }

    private void getLikes() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("deal_id", String.valueOf(dealsDetailsResponse.getId()));
        getDealLikesUseCase.setRequestParams(requestParams);
        getDealLikesUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<ArrayList<GetLikesResponse>>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();

                ArrayList<GetLikesResponse> getLikesResponse = (ArrayList<GetLikesResponse>) dataResponse.getData();
                if (getLikesResponse != null && getLikesResponse.size() > 0) {
                    getView().setLikes(getLikesResponse.get(0).getTotalLikes(), getLikesResponse.get(0).isLiked());
                }

            }
        });
    }

    private void getRecommendedDeals() {

        isLoading = true;
        getSearchNextUseCase.setRequestParams(searchNextParams);
        getSearchNextUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {

                Type token = new TypeToken<DataResponse<SearchResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse data = restResponse.getData();
                SearchResponse searchResponse = (SearchResponse) data.getData();

                isLoading = false;
                getView().removeFooter();

                getView().addDealsToCards(processSearchResponse(searchResponse));

                checkIfToLoad(getView().getLayoutManager());
            }
        });
    }

    private List<ProductItem> processSearchResponse(SearchResponse searchResponse) {

        String nexturl = searchResponse.getPage().getUriNext();
        if (!TextUtils.isEmpty(nexturl)) {
            searchNextParams.putString(Utils.NEXT_URL, nexturl);
            isLastPage = false;
        } else {
            isLastPage = true;
        }
        return searchResponse.getDeals();
    }

    @Override
    public boolean onOptionMenuClick(int id) {
        if (id == R.id.action_menu_share) {
            Utils.getSingletonInstance().shareDeal(dealsDetailsResponse.getSeoUrl(),
                    getView().getActivity(), dealsDetailsResponse.getDisplayName(),
                    dealsDetailsResponse.getImageWeb(), dealsDetailsResponse.getDesktopUrl());
        } else {
            getView().getActivity().onBackPressed();
        }
        return true;
    }

    @Override
    public void onBannerSlide(int page) {
        currentPage = page;
    }

    @Override
    public void startBannerSlide(TouchViewPager viewPager) {
        this.mTouchViewPager = viewPager;
        currentPage = viewPager.getCurrentItem();
        try {
            totalPages = viewPager.getAdapter().getCount();
        } catch (Exception e) {
            e.printStackTrace();
            totalPages = viewPager.getChildCount();
        }
        Observable.interval(5000, 5000, TimeUnit.MILLISECONDS)
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
                    }
                });
    }

    @Override
    public List<Outlet> getAllOutlets() {
        if (dealsDetailsResponse != null)
            return dealsDetailsResponse.getOutlets();
        else
            return null;
    }

    public void onRecyclerViewScrolled(LinearLayoutManager layoutManager) {
        checkIfToLoad(layoutManager);
    }

    private void checkIfToLoad(LinearLayoutManager layoutManager) {
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                getRecommendedDeals();
            } else {
                getView().addFooter();
            }
        }
    }


}
