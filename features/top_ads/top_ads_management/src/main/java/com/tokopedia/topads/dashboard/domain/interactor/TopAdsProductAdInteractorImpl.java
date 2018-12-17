package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.topads.common.data.source.local.TopAdsCacheDataSourceImpl;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.data.model.data.ProductAdBulkAction;
import com.tokopedia.topads.dashboard.data.model.request.DataRequest;
import com.tokopedia.topads.dashboard.data.model.request.SearchAdRequest;
import com.tokopedia.product.manage.item.common.data.source.cloud.DataResponse;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.TopAdsManagementService;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zulfikarrahman on 12/30/16.
 * Created by Nathaniel on 12/29/2016.
 */

public class TopAdsProductAdInteractorImpl implements TopAdsProductAdInteractor {

    private CompositeSubscription compositeSubscription;
    private TopAdsManagementService topAdsManagementService;
    private TopAdsCacheDataSourceImpl topAdsCacheDataSourceImpl;

    public TopAdsProductAdInteractorImpl(TopAdsManagementService topAdsManagementService, TopAdsCacheDataSourceImpl topAdsCacheDataSourceImpl) {
        this.compositeSubscription = new CompositeSubscription();
        this.topAdsManagementService = topAdsManagementService;
        this.topAdsCacheDataSourceImpl = topAdsCacheDataSourceImpl;
    }

    @Override
    public void searchAd(SearchAdRequest searchAdRequest, final ListenerInteractor<PageDataResponse<List<ProductAd>>> listener) {
        Observable<Response<PageDataResponse<List<ProductAd>>>> observable = topAdsManagementService.getApi().getProductAd(searchAdRequest.getParams());
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Response<PageDataResponse<List<ProductAd>>>, Observable<PageDataResponse<List<ProductAd>>>>() {
                    @Override
                    public Observable<PageDataResponse<List<ProductAd>>> call(Response<PageDataResponse<List<ProductAd>>> response) {
                        return Observable.just(response.body());
                    }
                })
                .subscribe(new SubscribeOnNext<PageDataResponse<List<ProductAd>>>(listener), new SubscribeOnError(listener)));
    }

    @Override
    public void bulkAction(DataRequest<ProductAdBulkAction> bulkActionDataRequest, ListenerInteractor<ProductAdBulkAction> listener) {
        Observable<Response<DataResponse<ProductAdBulkAction>>> observable = topAdsManagementService.getApi().bulkActionProductAd(bulkActionDataRequest);
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Response<DataResponse<ProductAdBulkAction>>, Observable<ProductAdBulkAction>>() {
                    @Override
                    public Observable<ProductAdBulkAction> call(Response<DataResponse<ProductAdBulkAction>> response) {
                        return Observable.just(response.body().getData());
                    }
                })
                .subscribe(new SubscribeOnNext<ProductAdBulkAction>(listener), new SubscribeOnError(listener)));
    }

    @Override
    public void unSubscribe() {
        compositeSubscription.unsubscribe();
    }
}