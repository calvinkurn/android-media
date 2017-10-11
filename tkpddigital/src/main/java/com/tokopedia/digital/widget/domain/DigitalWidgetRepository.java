package com.tokopedia.digital.widget.domain;

import android.util.Log;

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
import com.tokopedia.digital.widget.data.entity.response.ResponseFavoriteNumber;
import com.tokopedia.digital.widget.data.entity.response.ResponseMetaFavoriteNumber;
import com.tokopedia.digital.widget.data.entity.status.StatusEntity;
import com.tokopedia.digital.widget.data.mapper.IFavoriteNumberMapper;
import com.tokopedia.digital.widget.model.DigitalNumberList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private final static String KEY_STATUS = "RECHARGE_STATUS";
    private final static String KEY_PRODUCT = "RECHARGE_PRODUCT";
    private final static String KEY_OPERATOR = "RECHARGE_OPERATOR";
    private final static String KEY_STATUS_CURRENT = "RECHARGE_STATUS_CURRENT";

    private static int CACHE_DURATION = 60 * 30;

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
                            globalCacheManager.setCacheDuration(CACHE_DURATION);
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
                            globalCacheManager.setCacheDuration(CACHE_DURATION);
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
                            globalCacheManager.setCacheDuration(CACHE_DURATION);
                            globalCacheManager.store();
                        }
                    }
                });
    }

    @Override
    public Observable<StatusEntity> getObservableStatus() {
        return Observable.concat(getObservableStatusDB(), getObservableStatusNetwork())
                .first(new Func1<StatusEntity, Boolean>() {
                    @Override
                    public Boolean call(StatusEntity status) {
                        return status != null;
                    }
                })
                .doOnNext(validateStatus(true));
    }

    private Observable<StatusEntity> getObservableStatusDB() {
        return Observable.just(true)
                .map(new Func1<Boolean, StatusEntity>() {
                    @Override
                    public StatusEntity call(Boolean aBoolean) {
                        GlobalCacheManager manager = new GlobalCacheManager();
                        return CacheUtil.convertStringToModel(
                                manager.getValueString(KEY_STATUS),
                                new TypeToken<StatusEntity>() {
                                }.getType());

                    }
                })
                .onErrorReturn(new Func1<Throwable, StatusEntity>() {
                    @Override
                    public StatusEntity call(Throwable throwable) {
                        return null;
                    }
                });
    }

    private Observable<StatusEntity> getObservableStatusNetwork() {
        return digitalEndpointService.getApi().getStatus()
                .map(new Func1<Response<TkpdDigitalResponse>, StatusEntity>() {
                    @Override
                    public StatusEntity call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                        return tkpdDigitalResponseResponse.body().convertDataObj(StatusEntity.class);
                    }
                })
                .doOnNext(new Action1<StatusEntity>() {
                    @Override
                    public void call(StatusEntity statusEntity) {
                        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
                        globalCacheManager.setKey(KEY_STATUS);
                        globalCacheManager.setValue(CacheUtil.convertModelToString(statusEntity,
                                new TypeToken<StatusEntity>() {
                                }.getType()));
                        globalCacheManager.setCacheDuration(CACHE_DURATION);
                        globalCacheManager.store();
                    }
                });
    }

    private Action1<StatusEntity> validateStatus(final boolean isInitialGetStatus) {
        return new Action1<StatusEntity>() {
            @Override
            public void call(StatusEntity status) {
                GlobalCacheManager globalCacheManager = new GlobalCacheManager();
                String currentStatusString = globalCacheManager.getValueString(KEY_STATUS_CURRENT);
                String statusString = CacheUtil.convertModelToString(status,
                        new TypeToken<StatusEntity>() {
                        }.getType());
                if (currentStatusString != null && !currentStatusString.equals(statusString)) {
                    globalCacheManager.delete(KEY_CATEGORY);
                    globalCacheManager.delete(KEY_OPERATOR);
                    globalCacheManager.delete(KEY_PRODUCT);

                    GlobalCacheManager managerStatus = new GlobalCacheManager();
                    managerStatus.setKey(KEY_STATUS_CURRENT);
                    managerStatus.setValue(statusString);
                    managerStatus.store();
                } else if (currentStatusString != null && currentStatusString.equals(statusString)
                        && !isInitialGetStatus) {
                    throw new RuntimeException("Is no need to reload widget");
                } else if (currentStatusString == null) {
                    GlobalCacheManager managerStatus = new GlobalCacheManager();
                    managerStatus.setKey(KEY_STATUS_CURRENT);
                    managerStatus.setValue(statusString);
                    managerStatus.store();
                }
            }
        };
    }

    @Override
    public Observable<DigitalNumberList> getObservableNumberList(TKPDMapParam<String, String> param) {
        return digitalEndpointService.getApi().getNumberList(param)
                .map(getFuncTransformNumberList())
                .flatMap(new Func1<List<OrderClientNumber>, Observable<DigitalNumberList>>() {
                    @Override
                    public Observable<DigitalNumberList> call(final List<OrderClientNumber> orderClientNumbers) {
                        if (!orderClientNumbers.isEmpty()) {
                            final List<OrderClientNumber> originalClientNumbers = new ArrayList<>();
                            originalClientNumbers.addAll(orderClientNumbers);

                            return Observable.just(orderClientNumbers)
                                    .flatMapIterable(getSortedNumberList())
                                    .first(getLastOrder())
                                    .flatMap(new Func1<OrderClientNumber, Observable<OrderClientNumber>>() {
                                        @Override
                                        public Observable<OrderClientNumber> call(OrderClientNumber orderClientNumber) {
                                            if (orderClientNumber == null) {
                                                return Observable.just(originalClientNumbers)
                                                        .flatMapIterable(getSortedNumberList())
                                                        .first();
                                            } else {
                                                return Observable.just(orderClientNumber);
                                            }
                                        }
                                    })
                                    .map(new Func1<OrderClientNumber, DigitalNumberList>() {
                                        @Override
                                        public DigitalNumberList call(OrderClientNumber orderClientNumber) {
                                            return new DigitalNumberList(originalClientNumbers, orderClientNumber);
                                        }
                                    });
                        } else {
                            return Observable.just(new DigitalNumberList(
                                    new ArrayList<OrderClientNumber>(), null));
                        }
                    }
                });
    }

    private Func1<OrderClientNumber, Boolean> getLastOrder() {
        return new Func1<OrderClientNumber, Boolean>() {
            @Override
            public Boolean call(OrderClientNumber orderClientNumber) {
                return orderClientNumber.getLastProduct() != null;
            }
        };
    }

    private Func1<List<OrderClientNumber>, List<OrderClientNumber>> getSortedNumberList() {
        return new Func1<List<OrderClientNumber>, List<OrderClientNumber>>() {
            @Override
            public List<OrderClientNumber> call(List<OrderClientNumber> orderClientNumbers) {
                Collections.sort(orderClientNumbers, new Comparator<OrderClientNumber>() {
                    @Override
                    public int compare(OrderClientNumber o1, OrderClientNumber o2) {
                        return o2.getLastUpdated().compareTo(o1.getLastUpdated());
                    }
                });
                return orderClientNumbers;
            }
        };
    }

    private Func1<Response<TkpdDigitalResponse>, List<OrderClientNumber>> getFuncTransformNumberList() {
        return new Func1<Response<TkpdDigitalResponse>, List<OrderClientNumber>>() {
            @Override
            public List<OrderClientNumber> call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                List<ResponseFavoriteNumber> responseFavoriteNumbers = tkpdDigitalResponseResponse
                        .body().convertDataList(ResponseFavoriteNumber[].class);

                ResponseMetaFavoriteNumber responseMetaFavoriteNumber =
                        tkpdDigitalResponseResponse.body().convertMetaObj(ResponseMetaFavoriteNumber.class);

                Log.d(this.getClass().getSimpleName(), responseMetaFavoriteNumber.toString());
                return favoriteNumberMapper
                        .transformDigitalFavoriteNumberItemDataList(responseFavoriteNumbers);
            }
        };
    }

}