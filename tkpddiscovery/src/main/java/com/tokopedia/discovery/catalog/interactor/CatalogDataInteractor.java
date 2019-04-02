package com.tokopedia.discovery.catalog.interactor;

import android.support.annotation.NonNull;

import com.tokopedia.discovery.catalog.model.CatalogDetailData;
import com.tokopedia.discovery.catalog.model.CatalogDetailListData;
import com.tokopedia.discovery.catalog.model.CatalogListWrapperData;
import com.tokopedia.discovery.catalog.model.CatalogWrapperData;
import com.tokopedia.core.database.DbFlowOperation;
import com.tokopedia.discovery.catalog.database.manager.DetailCatalogCacheManager;
import com.tokopedia.core.database.model.CatalogDetailModelDB;
import com.tokopedia.core.network.apiservices.search.CatalogService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.catalog.network.apiservices.CatalogAWSService;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 10/17/16.
 */

public class CatalogDataInteractor implements ICataloDataInteractor {
    private final CompositeSubscription compositeSubscription;

    public CatalogDataInteractor() {
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getDetailCatalogData(TKPDMapParam<String, String> param,
                                     Subscriber<CatalogDetailData> subscriber) {
        CatalogWrapperData catalogWrapperData = new CatalogWrapperData();
        catalogWrapperData.setParam(param);
        compositeSubscription.add(Observable.just(catalogWrapperData)
                .map(new Func1<CatalogWrapperData, CatalogWrapperData>() {
                    @Override
                    public CatalogWrapperData call(CatalogWrapperData catalogWrapperData) {
                        return getFromChace(catalogWrapperData);
                    }
                })
                .flatMap(new Func1<CatalogWrapperData, Observable<CatalogWrapperData>>() {
                    @Override
                    public Observable<CatalogWrapperData> call(final CatalogWrapperData catalogWrapperData) {
                        return catalogWrapperData.getCatalogDetailData() == null
                                ? getFromNetwork(catalogWrapperData)
                                : Observable.just(catalogWrapperData);
                    }
                })
                .map(new Func1<CatalogWrapperData, CatalogDetailData>() {
                    @Override
                    public CatalogDetailData call(CatalogWrapperData catalogWrapperData) {
                        return catalogWrapperData.getCatalogDetailData();
                    }
                })
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));

    }

    @Override
    public void getDetailCatalogListData(CatalogListWrapperData catalogListWrapperData,
                                         Subscriber<CatalogDetailListData> subscriber) {
        compositeSubscription.add(Observable.just(catalogListWrapperData)
                .flatMap(new Func1<CatalogListWrapperData, Observable<CatalogListWrapperData>>() {
                    @Override
                    public Observable<CatalogListWrapperData> call(CatalogListWrapperData catalogListWrapperData) {
                        CatalogAWSService service = new CatalogAWSService();
                        Observable<Response<CatalogDetailListData>> observableNetwork = service
                                .getApi()
                                .getCatalogDetailList(catalogListWrapperData.getParams());

                        return Observable.zip(Observable.just(
                                catalogListWrapperData), observableNetwork, transformWrapCatalogListData()
                        );
                    }
                }).map(new Func1<CatalogListWrapperData, CatalogDetailListData>() {
            @Override
            public CatalogDetailListData call(CatalogListWrapperData catalogListWrapperData) {
                return catalogListWrapperData.getData();
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void unsubscribe() {
        compositeSubscription.unsubscribe();
    }

    @NonNull
    private Func2<CatalogListWrapperData, Response<CatalogDetailListData>, CatalogListWrapperData>
    transformWrapCatalogListData() {
        return new Func2<CatalogListWrapperData, Response<CatalogDetailListData>,
                CatalogListWrapperData>() {
            @Override
            public CatalogListWrapperData call(CatalogListWrapperData catalogListWrapperData,
                                               Response<CatalogDetailListData> catalogDetailListDataResponse) {
                catalogListWrapperData.setData(catalogDetailListDataResponse.body());
                return catalogListWrapperData;
            }
        };
    }


    private Observable<CatalogWrapperData> getFromNetwork(CatalogWrapperData catalogWrapperData) {
        CatalogService service = new CatalogService();
        Observable<Response<TkpdResponse>> observableNetwork = service.getApi()
                .getCatalogDetail(catalogWrapperData.getParam());
        return Observable.zip(Observable.just(
                catalogWrapperData),
                observableNetwork,
                transformWrapperCatalogData()
        );
    }

    @NonNull
    private Func2<CatalogWrapperData, Response<TkpdResponse>, CatalogWrapperData> transformWrapperCatalogData() {
        return new Func2<CatalogWrapperData, Response<TkpdResponse>, CatalogWrapperData>() {
            @Override
            public CatalogWrapperData call(CatalogWrapperData catalogWrapperData,
                                           Response<TkpdResponse> tkpdResponseResponse) {
                saveToCache(catalogWrapperData.getCatalogId(), tkpdResponseResponse.body().getStringData());
                catalogWrapperData.setCatalogDetailData(
                        tkpdResponseResponse.body().convertDataObj(CatalogDetailData.class)
                );
                return catalogWrapperData;
            }
        };
    }

    private void saveToCache(String catalogId, String stringData) {
        DbFlowOperation<CatalogDetailModelDB> dbFlowOperation = new DetailCatalogCacheManager();
        dbFlowOperation.store(new CatalogDetailModelDB.Builder()
                .detailCatalogId(catalogId)
                .detailCatalogData(stringData)
                .build());
    }

    private CatalogWrapperData getFromChace(CatalogWrapperData data) {
        DbFlowOperation<CatalogDetailModelDB> dbFlowOperation = new DetailCatalogCacheManager();
        data.setCatalogDetailData(dbFlowOperation.getConvertObjData(data.getCatalogId(),
                CatalogDetailData.class));
        return data;
    }

}
