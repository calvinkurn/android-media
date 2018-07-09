package com.tokopedia.digital_deals.view.presenter;


import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.data.entity.response.homeresponse.DealsResponse;
import com.tokopedia.digital_deals.domain.getusecase.GetDealDetailsUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetDealLikesUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetSearchNextUseCase;
import com.tokopedia.digital_deals.domain.model.dealdetailsdomainmodel.DealsDetailsDomain;
import com.tokopedia.digital_deals.domain.model.request.likes.GetLikesDomain;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.SearchDomainModel;
import com.tokopedia.digital_deals.view.contractor.DealDetailsContract;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.DealsDetailsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.OutletViewModel;
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
    private DealsDetailsViewModel dealsDetailsViewModel;
    public static final String HOME_DATA = "home_data";
    public final static String TAG = "url";
    private TouchViewPager mTouchViewPager;
    private boolean isLoading;
    private boolean isLastPage;
    private final int PAGE_SIZE = 20;
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
                Type token = new TypeToken<DataResponse<DealsDetailsDomain>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse data = restResponse.getData();
                DealsDetailsDomain dealEntity = (DealsDetailsDomain) data.getData();
                getView().hideProgressBar();
                getView().showShareButton();
                getView().showCollapsingHeader();
                dealsDetailsViewModel = Utils.getSingletonInstance()
                        .convertIntoDealDetailsViewModel(dealEntity);
                getView().renderDealDetails(dealsDetailsViewModel);
                searchNextParams.putString("nexturl", dealsDetailsViewModel.getRecommendationUrl());
                getRecommendedDeals();
                CommonUtils.dumper("enter onNext");
                getLikes();
            }
        });
//        getDealDetailsUseCase.execute(getView().getParams(), new Subscriber<DealsDetailsDomain>() {
//
//            @Override
//            public void onCompleted() {
//                CommonUtils.dumper("enter onCompleted");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                CommonUtils.dumper("enter error");
//                e.printStackTrace();
//                getView().hideProgressBar();
//                getView().hideCollapsingHeader();
//                NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
//                    @Override
//                    public void onRetryClicked() {
//                        getDealDetails();
//                    }
//                });
//            }
//
//            @Override
//            public void onNext(DealsDetailsDomain dealEntity) {
//                getView().hideProgressBar();
//                getView().showShareButton();
//                getView().showCollapsingHeader();
//                dealsDetailsViewModel = Utils.getSingletonInstance()
//                        .convertIntoDealDetailsViewModel(dealEntity);
//                getView().renderDealDetails(dealsDetailsViewModel);
//                searchNextParams.putString("nexturl", dealsDetailsViewModel.getRecommendationUrl());
//                getRecommendedDeals();
//                CommonUtils.dumper("enter onNext");
//                getLikes();
//            }
//        });
    }

    private void getLikes() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("deal_id", String.valueOf(dealsDetailsViewModel.getId()));
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
                Type token = new TypeToken<DataResponse<ArrayList<GetLikesDomain>>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();

                ArrayList<GetLikesDomain> getLikesDomain = (ArrayList<GetLikesDomain>) dataResponse.getData();
                if (getLikesDomain != null && getLikesDomain.size() > 0) {
                    getView().setLikes(getLikesDomain.get(0).getTotalLikes(), getLikesDomain.get(0).isLiked());
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
                Log.d("inOnErrrror", e.getMessage());
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Log.d("inOnNext", "ds");

                Type token = new TypeToken<DataResponse<SearchDomainModel>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse data = restResponse.getData();
                SearchDomainModel searchDomainModel = (SearchDomainModel) data.getData();

                isLoading = false;
                getView().removeFooter();

                getView().addDealsToCards(processSearchResponse(searchDomainModel));

                checkIfToLoad(getView().getLayoutManager());
            }
        });
    }

    List<CategoryItemsViewModel> processSearchResponse(SearchDomainModel searchDomainModel) {

        String nexturl = searchDomainModel.getPage().getUriNext();
        if (nexturl != null && !nexturl.isEmpty() && nexturl.length() > 0) {
            searchNextParams.putString("nexturl", nexturl);
            isLastPage = false;
        } else {
            isLastPage = true;
        }
        List<CategoryItemsViewModel> categoryItemsViewModels = Utils.getSingletonInstance()
                .convertIntoCategoryListItemsViewModel(searchDomainModel.getDeals());
        return categoryItemsViewModels;
    }

    @Override
    public boolean onOptionMenuClick(int id) {
        if (id == R.id.action_menu_share) {
            Utils.getSingletonInstance().shareDeal(dealsDetailsViewModel.getSeoUrl(),
                    getView().getActivity(), dealsDetailsViewModel.getDisplayName(),
                    dealsDetailsViewModel.getImageWeb());
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
    public List<OutletViewModel> getAllOutlets() {
        if (dealsDetailsViewModel != null)
            return dealsDetailsViewModel.getOutlets();
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
