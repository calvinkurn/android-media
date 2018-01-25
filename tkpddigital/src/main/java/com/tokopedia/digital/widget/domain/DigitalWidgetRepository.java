package com.tokopedia.digital.widget.domain;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.apiservice.DigitalEndpointService;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.widget.data.entity.category.CategoryEntity;
import com.tokopedia.digital.widget.data.entity.operator.OperatorEntity;
import com.tokopedia.digital.widget.data.entity.product.ProductEntity;
import com.tokopedia.digital.widget.data.entity.response.ResponseFavoriteList;
import com.tokopedia.digital.widget.data.entity.response.ResponseFavoriteNumber;
import com.tokopedia.digital.widget.data.entity.response.ResponseMetaFavoriteNumber;
import com.tokopedia.digital.widget.data.entity.status.StatusEntity;
import com.tokopedia.digital.widget.data.mapper.IFavoriteNumberMapper;
import com.tokopedia.digital.widget.model.DigitalNumberList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 7/28/17.
 * Modified by rizkyfadillah at 10/6/17.
 */

public class DigitalWidgetRepository implements IDigitalWidgetRepository {

    private final static String KEY_CATEGORY = "RECHARGE_CATEGORY";
    private final static String KEY_PRODUCT = "RECHARGE_PRODUCT";
    private final static String KEY_OPERATOR = "RECHARGE_OPERATOR";
    private final static String KEY_STATUS_CURRENT = "RECHARGE_STATUS_CURRENT";

    private final DigitalEndpointService digitalEndpointService;
    private final IFavoriteNumberMapper favoriteNumberMapper;

    public DigitalWidgetRepository(DigitalEndpointService digitalEndpointService,
                                   IFavoriteNumberMapper favoriteNumberMapper) {
        this.digitalEndpointService = digitalEndpointService;
        this.favoriteNumberMapper = favoriteNumberMapper;
    }

    @Override
    public Observable<List<CategoryEntity>> getObservableCategoryData() {
        return Observable.concat(getObservableCategoryDataDB(), getObservableCategoryDataNetwork())
                .first(new Func1<List<CategoryEntity>, Boolean>() {
                    @Override
                    public Boolean call(List<CategoryEntity> categoryEntities) {
                        return categoryEntities != null && !categoryEntities.isEmpty();
                    }
                });
    }

    private Observable<List<CategoryEntity>> getObservableCategoryDataDB() {
        return Observable.just(new GlobalCacheManager())
                .map(new Func1<GlobalCacheManager, List<CategoryEntity>>() {
                    @Override
                    public List<CategoryEntity> call(GlobalCacheManager globalCacheManager) {
                        return CacheUtil.convertStringToListModel(
                                globalCacheManager.getValueString(KEY_CATEGORY),
                                new TypeToken<List<CategoryEntity>>() {
                                }.getType());
                    }
                })
                .onErrorReturn(new Func1<Throwable, List<CategoryEntity>>() {
                    @Override
                    public List<CategoryEntity> call(Throwable throwable) {
                        return new ArrayList<>();
                    }
                });
    }

    private Observable<List<CategoryEntity>> getObservableCategoryDataNetwork() {
        return digitalEndpointService.getApi().getCategoryList()
                .map(new Func1<Response<TkpdDigitalResponse>, List<CategoryEntity>>() {
                    @Override
                    public List<CategoryEntity> call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                        return tkpdDigitalResponseResponse.body().convertDataList(CategoryEntity[].class);
                    }
                })
                .doOnNext(new Action1<List<CategoryEntity>>() {
                    @Override
                    public void call(List<CategoryEntity> categoryEntity) {
                        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
                        if (categoryEntity != null) {
                            globalCacheManager.setKey(KEY_CATEGORY);
                            globalCacheManager.setValue(
                                    CacheUtil.convertListModelToString(categoryEntity,
                                            new TypeToken<List<CategoryEntity>>() {
                                            }.getType()));
                            globalCacheManager.store();
                        }
                    }
                });
    }

    @Override
    public Observable<List<ProductEntity>> getObservableProducts() {
        return Observable.concat(getObservableProductsDB(), getObservableProductsNetwork())
                .first(new Func1<List<ProductEntity>, Boolean>() {
                    @Override
                    public Boolean call(List<ProductEntity> products) {
                        return products != null && !products.isEmpty();
                    }
                });
    }

    private Observable<List<ProductEntity>> getObservableProductsDB() {
        return Observable.just(new GlobalCacheManager())
                .map(new Func1<GlobalCacheManager, List<ProductEntity>>() {
                    @Override
                    public List<ProductEntity> call(GlobalCacheManager globalCacheManager) {
                        return CacheUtil.convertStringToListModel(
                                globalCacheManager.getValueString(KEY_PRODUCT),
                                new TypeToken<List<ProductEntity>>() {
                                }.getType());
                    }
                })
                .onErrorReturn(new Func1<Throwable, List<ProductEntity>>() {
                    @Override
                    public List<ProductEntity> call(Throwable throwable) {
                        return new ArrayList<>();
                    }
                });
    }

    private Observable<List<ProductEntity>> getObservableProductsNetwork() {
        return digitalEndpointService.getApi().getProductList()
                .map(new Func1<Response<TkpdDigitalResponse>, List<ProductEntity>>() {
                    @Override
                    public List<ProductEntity> call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                        return tkpdDigitalResponseResponse.body().convertDataList(ProductEntity[].class);
                    }
                })
                .doOnNext(new Action1<List<ProductEntity>>() {
                    @Override
                    public void call(List<ProductEntity> productEntities) {
                        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
                        if (productEntities != null) {
                            globalCacheManager.setKey(KEY_PRODUCT);
                            globalCacheManager.setValue(CacheUtil.convertListModelToString(productEntities,
                                    new TypeToken<List<ProductEntity>>() {
                                    }.getType()));
                            globalCacheManager.store();
                        }
                    }
                });
    }

    @Override
    public Observable<List<OperatorEntity>> getObservableOperators() {
        return Observable.concat(getObservableOperatorsDB(), getObservableOperatorsNetwork())
                .first(new Func1<List<OperatorEntity>, Boolean>() {
                    @Override
                    public Boolean call(List<OperatorEntity> operators) {
                        return operators != null && !operators.isEmpty();
                    }
                });
    }

    private Observable<List<OperatorEntity>> getObservableOperatorsDB() {
        return Observable.just(new GlobalCacheManager())
                .map(new Func1<GlobalCacheManager, List<OperatorEntity>>() {
                    @Override
                    public List<OperatorEntity> call(GlobalCacheManager globalCacheManager) {
                        return CacheUtil.convertStringToListModel(
                                globalCacheManager.getValueString(KEY_OPERATOR),
                                new TypeToken<List<OperatorEntity>>() {
                                }.getType());
                    }
                })
                .onErrorReturn(new Func1<Throwable, List<OperatorEntity>>() {
                    @Override
                    public List<OperatorEntity> call(Throwable throwable) {
                        return new ArrayList<>();
                    }
                });
    }

    private Observable<List<OperatorEntity>> getObservableOperatorsNetwork() {
        return digitalEndpointService.getApi().getOperatorList()
                .map(new Func1<Response<TkpdDigitalResponse>, List<OperatorEntity>>() {
                    @Override
                    public List<OperatorEntity> call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                        return tkpdDigitalResponseResponse.body().convertDataList(OperatorEntity[].class);
                    }
                })
                .doOnNext(new Action1<List<OperatorEntity>>() {
                    @Override
                    public void call(List<OperatorEntity> operatorEntities) {
                        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
                        if (operatorEntities != null) {
                            globalCacheManager.setKey(KEY_OPERATOR);
                            globalCacheManager.setValue(CacheUtil.convertListModelToString(operatorEntities,
                                    new TypeToken<List<OperatorEntity>>() {
                                    }.getType()));
                            globalCacheManager.store();
                        }
                    }
                });
    }

    public Observable<StatusEntity> getObservableStatus() {
        return getObservableStatusNetwork()
                .map(new Func1<StatusEntity, StatusEntity>() {
                    @Override
                    public StatusEntity call(StatusEntity status) {
                        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
                        String currentStatusString = globalCacheManager.getValueString(KEY_STATUS_CURRENT);
                        String statusString = CacheUtil.convertModelToString(status,
                                new TypeToken<StatusEntity>() {
                                }.getType());
                        if (currentStatusString != null && !currentStatusString.equals(statusString)) {
                            globalCacheManager.delete(KEY_CATEGORY);
                            globalCacheManager.delete(KEY_OPERATOR);
                            globalCacheManager.delete(KEY_PRODUCT);

                            saveStatusToCache(statusString);
                        } else if (currentStatusString == null) {
                            saveStatusToCache(statusString);
                        }
                        return status;
                    }
                });
    }

    private void saveStatusToCache(String statusString) {
        GlobalCacheManager managerStatus = new GlobalCacheManager();
        managerStatus.setKey(KEY_STATUS_CURRENT);
        managerStatus.setValue(statusString);
        managerStatus.store();
    }

    private Observable<StatusEntity> getObservableStatusNetwork() {
        return digitalEndpointService.getApi().getStatus()
                .map(new Func1<Response<TkpdDigitalResponse>, StatusEntity>() {
                    @Override
                    public StatusEntity call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                        return tkpdDigitalResponseResponse.body().convertDataObj(StatusEntity.class);
                    }
                });
    }

    @Override
    public Observable<DigitalNumberList> getObservableNumberList(TKPDMapParam<String, String> param) {
        return digitalEndpointService.getApi().getNumberList(param)
                .map(getFuncTransformNumberList())
                .onErrorReturn(new Func1<Throwable, DigitalNumberList>() {
                    @Override
                    public DigitalNumberList call(Throwable throwable) {
                        return new DigitalNumberList(new ArrayList<OrderClientNumber>(), null);
                    }
                });
    }

    private Func1<Response<TkpdDigitalResponse>, DigitalNumberList> getFuncTransformNumberList() {
        return new Func1<Response<TkpdDigitalResponse>, DigitalNumberList>() {
            @Override
            public DigitalNumberList call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                List<ResponseFavoriteNumber> responseFavoriteNumbers = tkpdDigitalResponseResponse
                        .body().convertDataList(ResponseFavoriteNumber[].class);

                ResponseMetaFavoriteNumber responseMetaFavoriteNumber =
                        tkpdDigitalResponseResponse.body().convertMetaObj(ResponseMetaFavoriteNumber.class);

                ResponseFavoriteList responseFavoriteList =  new ResponseFavoriteList(responseMetaFavoriteNumber,
                        responseFavoriteNumbers);

                return favoriteNumberMapper
                        .transformDigitalFavoriteNumberItemDataList(responseFavoriteList);
            }
        };
    }

}