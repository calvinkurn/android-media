package com.tokopedia.discovery.catalog.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.core.network.apiservices.search.CatalogService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.catalog.database.CatalogDetailDBModel;
import com.tokopedia.discovery.catalog.database.CatalogDetailDatabaseClient;
import com.tokopedia.discovery.catalog.model.CatalogDetailData;
import com.tokopedia.discovery.catalog.model.CatalogDetailListData;
import com.tokopedia.discovery.catalog.model.CatalogListWrapperData;
import com.tokopedia.discovery.catalog.model.CatalogWrapperData;
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
    private Context context;

    public CatalogDataInteractor(Context context) {
        this.context = context;
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
                        return getFromCache(catalogWrapperData);
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
        long expiredTime = System.currentTimeMillis() + 1000 * 60 * 60;
        CatalogDetailDatabaseClient
                .getInstance(context)
                .getCatalogDetailDatabase()
                .catalogDetailDao()
                .insert(new CatalogDetailDBModel(catalogId, stringData, expiredTime));
    }

    private CatalogWrapperData getFromCache(CatalogWrapperData data) {

        CatalogDetailDBModel dbModel = CatalogDetailDatabaseClient
                .getInstance(context)
                .getCatalogDetailDatabase()
                .catalogDetailDao()
                .getCatalogDetailDataById(data.getCatalogId());

        CatalogDetailData catalogDetailData = null;

        if (dbModel != null) {
             catalogDetailData = dbModel.getExpiredTime() < System.currentTimeMillis() ? null
                    : new Gson().fromJson(dbModel.getDetailCatalogData(), CatalogDetailData.class);
        }

        data.setCatalogDetailData(catalogDetailData);

        return data;
    }
}
