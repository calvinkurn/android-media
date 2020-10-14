package com.tokopedia.topads.product.view.presenter;

import com.tokopedia.topads.common.AutoAdsUseCase;
import com.tokopedia.topads.common.data.internal.AutoAdsStatus;
import com.tokopedia.topads.common.data.response.TopAdsAutoAdsData;
import com.tokopedia.topads.common.domain.interactor.TopAdsDatePickerInteractor;
import com.tokopedia.topads.common.model.request.DataRequest;
import com.tokopedia.topads.common.view.presenter.TopAdsBaseListPresenter;
import com.tokopedia.topads.dashboard.constant.SortTopAdsOption;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.data.model.data.ProductAdAction;
import com.tokopedia.topads.dashboard.data.model.data.ProductAdBulkAction;
import com.tokopedia.topads.dashboard.data.model.request.SearchAdRequest;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.group.domain.usecase.TopAdsSearchGroupAdUseCase;
import com.tokopedia.topads.product.domain.usecase.TopAdsGetProductAdUseCase;
import com.tokopedia.topads.product.domain.usecase.TopAdsToggleStatusUseCase;
import com.tokopedia.topads.product.view.listener.TopAdsProductAdListView;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsAddSourceTaggingUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by hadi.putra on 04/05/18.
 */

public class TopAdsProductAdListPresenter extends TopAdsBaseListPresenter<TopAdsProductAdListView> {

    private final TopAdsGetProductAdUseCase topAdsGetProductAdUseCase;
    private final TopAdsToggleStatusUseCase topAdsToggleStatusUseCase;
    private final TopAdsSearchGroupAdUseCase topAdsSearchGroupAdUseCase;
    private final AutoAdsUseCase autoAdsUseCase;

    private final BehaviorSubject<String> searchGroupName = BehaviorSubject.create();
    private final Subscription subscriptionSearchGroupName;
    private Subscription subscriptionLoadAds;

    @Inject
    public TopAdsProductAdListPresenter(TopAdsDatePickerInteractor topAdsDatePickerInteractor,
                                        TopAdsAddSourceTaggingUseCase topAdsAddSourceTaggingUseCase,
                                        TopAdsGetProductAdUseCase topAdsGetProductAdUseCase,
                                        TopAdsToggleStatusUseCase topAdsToggleStatusUseCase,
                                        TopAdsSearchGroupAdUseCase topAdsSearchGroupAdUseCase,
                                        AutoAdsUseCase autoAdsUseCase,
                                        UserSessionInterface userSession) {
        super(topAdsDatePickerInteractor, topAdsAddSourceTaggingUseCase, userSession);
        this.topAdsGetProductAdUseCase = topAdsGetProductAdUseCase;
        this.topAdsToggleStatusUseCase = topAdsToggleStatusUseCase;
        this.topAdsSearchGroupAdUseCase = topAdsSearchGroupAdUseCase;
        this.autoAdsUseCase = autoAdsUseCase;

        subscriptionSearchGroupName = searchGroupName
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubscriberDebounceSearchGroupName());
    }

    private Subscriber<String> getSubscriberDebounceSearchGroupName() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String keyword) {
                topAdsSearchGroupAdUseCase.execute(TopAdsSearchGroupAdUseCase
                                .createRequestParams(keyword, userSession.getShopId())
                        , getSubscriberSearchGroupName());
            }
        };
    }

    private Subscriber<List<GroupAd>> getSubscriberSearchGroupName() {
        return new Subscriber<List<GroupAd>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                getView().onGetGroupAdListError();
            }

            @Override
            public void onNext(List<GroupAd> groupAds) {
                getView().onGetGroupAdList(groupAds);
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsGetProductAdUseCase.unsubscribe();
        subscriptionSearchGroupName.unsubscribe();
        subscriptionLoadAds.unsubscribe();
        topAdsSearchGroupAdUseCase.unsubscribe();
        autoAdsUseCase.unsubscribe();
    }

    private boolean autoAdsIsActive(int status) {
        return (status == AutoAdsStatus.STATUS_ACTIVE
                || status == AutoAdsStatus.STATUS_IN_PROGRESS_ACTIVE
                || status == AutoAdsStatus.STATUS_IN_PROGRESS_AUTOMANAGE
                || status == AutoAdsStatus.STATUS_IN_PROGRESS_INACTIVE
                || status == AutoAdsStatus.STATUS_NOT_DELIVERED);
    }

    public void searchAd(Date startDate, Date endDate, String keyword, int status, long groupId,
                         final int page, @SortTopAdsOption String sortId) {
        SearchAdRequest searchAdRequest = new SearchAdRequest();
        searchAdRequest.setShopId(getShopId());
        searchAdRequest.setStartDate(startDate);
        searchAdRequest.setEndDate(endDate);
        searchAdRequest.setKeyword(keyword);
        searchAdRequest.setStatus(status);
        searchAdRequest.setGroup(groupId);
        searchAdRequest.setPage(page);
        searchAdRequest.setSort(sortId);

        subscriptionLoadAds = Observable.zip(topAdsGetProductAdUseCase.getExecuteObservable(
                TopAdsGetProductAdUseCase.createRequestParams(searchAdRequest)),
                autoAdsUseCase.getExecuteObservable(RequestParams.EMPTY),
                new Func2<PageDataResponse<List<ProductAd>>, TopAdsAutoAdsData, PageDataResponse<List<ProductAd>>>() {
                    @Override
                    public PageDataResponse<List<ProductAd>> call(PageDataResponse<List<ProductAd>> listPageDataResponse,
                                                                  TopAdsAutoAdsData topAdsAutoAdsData) {
                        for (ProductAd ads : listPageDataResponse.getData()) {
                            ads.setAutoAds(autoAdsIsActive(topAdsAutoAdsData.getStatus()));
                        }
                        listPageDataResponse.setAutoAds(autoAdsIsActive(topAdsAutoAdsData.getStatus()));
                        return listPageDataResponse;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PageDataResponse<List<ProductAd>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().showListError(e);
                        }
                    }

                    @Override
                    public void onNext(PageDataResponse<List<ProductAd>> listPageDataResponse) {
                        if (isViewAttached()) {
                            boolean hasNextData = listPageDataResponse.getPage().getPerPage() * page < listPageDataResponse.getPage().getTotal();
                            getView().onSearchLoaded(listPageDataResponse.getData(), hasNextData);
                            if (listPageDataResponse.isAutoAds()) {
                                getView().onAutoAdsActive();
                            } else {
                                getView().onAutoAdsInactive();
                            }
                        }
                    }
                });
    }

    public void setAdActive(List<String> ids) {
        DataRequest<ProductAdBulkAction> dataRequest = generateActionRequest(generateProductAds(ids),
                TopAdsNetworkConstant.ACTION_BULK_ON_AD);
        topAdsToggleStatusUseCase.execute(TopAdsToggleStatusUseCase.createRequestParams(dataRequest),
                getBulkSubscriber());
    }

    public void setAdInactive(List<String> ids) {
        DataRequest<ProductAdBulkAction> dataRequest = generateActionRequest(generateProductAds(ids),
                TopAdsNetworkConstant.ACTION_BULK_OFF_AD);
        topAdsToggleStatusUseCase.execute(TopAdsToggleStatusUseCase.createRequestParams(dataRequest),
                getBulkSubscriber());
    }

    public void deleteAds(List<String> ids) {
        DataRequest<ProductAdBulkAction> dataRequest = generateActionRequest(generateProductAds(ids),
                TopAdsNetworkConstant.ACTION_BULK_DELETE_AD);
        topAdsToggleStatusUseCase.execute(TopAdsToggleStatusUseCase.createRequestParams(dataRequest),
                getBulkSubscriber());
    }


    private DataRequest<ProductAdBulkAction> generateActionRequest(List<ProductAdAction> productAdActions, String bulkAction) {
        ProductAdBulkAction dataRequestProductAd = new ProductAdBulkAction();
        dataRequestProductAd.setAction(bulkAction);
        dataRequestProductAd.setShopId(getShopId());
        dataRequestProductAd.setAds(productAdActions);
        return new DataRequest<>(dataRequestProductAd);
    }

    private Subscriber<ProductAdBulkAction> getBulkSubscriber() {
        return new Subscriber<ProductAdBulkAction>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onBulkActionError(e);
                }
            }

            @Override
            public void onNext(ProductAdBulkAction productAdBulkAction) {
                if (isViewAttached()) {
                    getView().onBulkActionSuccess(productAdBulkAction);
                }
            }
        };
    }

    private List<ProductAdAction> generateProductAds(List<String> ids) {
        List<ProductAdAction> dataRequestProductAds = new ArrayList<>();
        for (String adId : ids) {
            ProductAdAction adAction = new ProductAdAction();
            adAction.setId(adId);
            dataRequestProductAds.add(adAction);
        }
        return dataRequestProductAds;
    }

    private List<ProductAdAction> generateProductAds(List<String> ids, String groupId) {
        List<ProductAdAction> dataRequestProductAds = new ArrayList<>();
        for (String adId : ids) {
            ProductAdAction adAction = new ProductAdAction();
            adAction.setId(adId);
            adAction.setGroupId(groupId);
            dataRequestProductAds.add(adAction);
        }
        return dataRequestProductAds;
    }

    public void searchGroupName(String query) {
        searchGroupName.onNext(query);
    }

    public void moveAdsToExistingGroup(List<String> ids, String selectedGroupAdId) {
        DataRequest<ProductAdBulkAction> dataRequest = generateActionRequest(generateProductAds(ids, selectedGroupAdId),
                TopAdsNetworkConstant.ACTION_BULK_MOVE_AD);
        topAdsToggleStatusUseCase.execute(TopAdsToggleStatusUseCase.createRequestParams(dataRequest),
                getBulkSubscriber());
    }
}
