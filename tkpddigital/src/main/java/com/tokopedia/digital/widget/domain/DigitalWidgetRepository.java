package com.tokopedia.digital.widget.domain;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.database.manager.RechargeNumberListManager;
import com.tokopedia.core.database.model.RechargeNumberListModelDB;
import com.tokopedia.core.database.model.category.Category;
import com.tokopedia.core.database.model.category.CategoryData;
import com.tokopedia.core.database.recharge.operator.Operator;
import com.tokopedia.core.database.recharge.operator.OperatorData;
import com.tokopedia.core.database.recharge.product.Product;
import com.tokopedia.core.database.recharge.product.ProductData;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.core.database.recharge.recentOrder.LastOrderEntity;
import com.tokopedia.core.database.recharge.status.Status;
import com.tokopedia.core.network.apiservices.digital.DigitalEndpointService;
import com.tokopedia.core.network.apiservices.recharge.RechargeService;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.widget.data.entity.response.ResponseFavoriteNumber;
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
 */

public class DigitalWidgetRepository implements IDigitalWidgetRepository {

    private final static String KEY_CATEGORY = "RECHARGE_CATEGORY";
    private final static String KEY_STATUS = "RECHARGE_STATUS";
    private final static String KEY_PRODUCT = "RECHARGE_PRODUCT";
    private final static String KEY_OPERATOR = "RECHARGE_OPERATOR";
    private final static String KEY_STATUS_CURRENT = "RECHARGE_STATUS_CURRENT";
    private static int CACHE_DURATION = 60 * 30;

    private final RechargeService rechargeService;
    private final DigitalEndpointService digitalEndpointService;
    private final IFavoriteNumberMapper favoriteNumberMapper;

    public DigitalWidgetRepository(RechargeService rechargeService,
                                   DigitalEndpointService digitalEndpointService,
                                   IFavoriteNumberMapper favoriteNumberMapper) {
        this.rechargeService = rechargeService;
        this.digitalEndpointService = digitalEndpointService;
        this.favoriteNumberMapper = favoriteNumberMapper;
    }

    @Override
    public Observable<CategoryData> getObservableCategoryData() {
        return Observable.concat(getObservableCategoryDataDB(), getObservableCategoryDataNetwork())
                .first(new Func1<CategoryData, Boolean>() {
                    @Override
                    public Boolean call(CategoryData categoryData) {
                        return categoryData != null &&
                                categoryData.getData() != null &&
                                !categoryData.getData().isEmpty();
                    }
                });
    }

    private Observable<CategoryData> getObservableCategoryDataDB() {
        return Observable.just(new GlobalCacheManager())
                .map(new Func1<GlobalCacheManager, List<Category>>() {
                    @Override
                    public List<Category> call(GlobalCacheManager globalCacheManager) {
                        return CacheUtil.convertStringToListModel(
                                globalCacheManager.getValueString(KEY_CATEGORY),
                                new TypeToken<List<Category>>() {
                                }.getType());
                    }
                })
                .onErrorReturn(new Func1<Throwable, List<Category>>() {
                    @Override
                    public List<Category> call(Throwable throwable) {
                        return new ArrayList<>();
                    }
                })
                .flatMap(new Func1<List<Category>, Observable<CategoryData>>() {
                    @Override
                    public Observable<CategoryData> call(List<Category> categories) {
                        CategoryData categoryData = new CategoryData();
                        categoryData.setData(categories);
                        return Observable.just(categoryData);
                    }
                });
    }

    private Observable<CategoryData> getObservableCategoryDataNetwork() {
        return rechargeService.getApi().getCategory()
                .doOnNext(new Action1<Response<CategoryData>>() {
                    @Override
                    public void call(Response<CategoryData> categoryDataResponse) {
                        CategoryData categoryData = categoryDataResponse.body();
                        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
                        if (categoryData != null) {
                            globalCacheManager.setKey(KEY_CATEGORY);
                            globalCacheManager.setValue(
                                    CacheUtil.convertListModelToString(categoryData.getData(),
                                            new TypeToken<List<Category>>() {
                                            }.getType()));
                            globalCacheManager.setCacheDuration(CACHE_DURATION);
                            globalCacheManager.store();
                        }
                    }
                })
                .flatMap(new Func1<Response<CategoryData>, Observable<CategoryData>>() {
                    @Override
                    public Observable<CategoryData> call(Response<CategoryData> categoryDataResponse) {
                        return Observable.just(categoryDataResponse.body());
                    }
                });

    }

    @Override
    public Observable<List<Product>> getObservableProducts() {
        return Observable.concat(getObservableProductsDB(), getObservableProductsNetwork())
                .first(new Func1<List<Product>, Boolean>() {
                    @Override
                    public Boolean call(List<Product> products) {
                        return products != null && !products.isEmpty();
                    }
                });
    }

    private Observable<List<Product>> getObservableProductsDB() {
        return Observable.just(new GlobalCacheManager())
                .map(new Func1<GlobalCacheManager, List<Product>>() {
                    @Override
                    public List<Product> call(GlobalCacheManager globalCacheManager) {
                        return CacheUtil.convertStringToListModel(
                                globalCacheManager.getValueString(KEY_PRODUCT),
                                new TypeToken<List<Product>>() {
                                }.getType());
                    }
                })
                .onErrorReturn(new Func1<Throwable, List<Product>>() {
                    @Override
                    public List<Product> call(Throwable throwable) {
                        return new ArrayList<>();
                    }
                });
    }

    private Observable<List<Product>> getObservableProductsNetwork() {
        return rechargeService.getApi().getProduct()
                .doOnNext(new Action1<Response<ProductData>>() {
                    @Override
                    public void call(Response<ProductData> productDataResponse) {
                        ProductData productData = productDataResponse.body();
                        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
                        if (productData != null && productData.getData() != null) {
                            globalCacheManager.setKey(KEY_PRODUCT);
                            globalCacheManager.setValue(CacheUtil.convertListModelToString(productData.getData(),
                                    new TypeToken<List<Product>>() {
                                    }.getType()));
                            globalCacheManager.setCacheDuration(CACHE_DURATION);
                            globalCacheManager.store();
                        }
                    }
                })
                .flatMap(new Func1<Response<ProductData>, Observable<List<Product>>>() {
                    @Override
                    public Observable<List<Product>> call(Response<ProductData> productDataResponse) {
                        return Observable.just(productDataResponse.body().getData());
                    }
                });
    }

    @Override
    public Observable<List<Operator>> getObservableOperators() {
        return Observable.concat(getObservableOperatorsDB(), getObservableOperatorsNetwork())
                .first(new Func1<List<Operator>, Boolean>() {
                    @Override
                    public Boolean call(List<Operator> operators) {
                        return operators != null && !operators.isEmpty();
                    }
                });
    }

    private Observable<List<Operator>> getObservableOperatorsDB() {
        return Observable.just(new GlobalCacheManager())
                .map(new Func1<GlobalCacheManager, List<Operator>>() {
                    @Override
                    public List<Operator> call(GlobalCacheManager globalCacheManager) {
                        return CacheUtil.convertStringToListModel(
                                globalCacheManager.getValueString(KEY_OPERATOR),
                                new TypeToken<List<Operator>>() {
                                }.getType());
                    }
                })
                .onErrorReturn(new Func1<Throwable, List<Operator>>() {
                    @Override
                    public List<Operator> call(Throwable throwable) {
                        return new ArrayList<Operator>();
                    }
                });
    }

    private Observable<List<Operator>> getObservableOperatorsNetwork() {
        return rechargeService.getApi().getOperator()
                .doOnNext(new Action1<Response<OperatorData>>() {
                    @Override
                    public void call(Response<OperatorData> operatorDataResponse) {
                        OperatorData operatorData = operatorDataResponse.body();
                        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
                        if (operatorData != null && operatorData.getData() != null) {
                            globalCacheManager.setKey(KEY_OPERATOR);
                            globalCacheManager.setValue(CacheUtil.convertListModelToString(operatorData.getData(),
                                    new TypeToken<List<Operator>>() {
                                    }.getType()));
                            globalCacheManager.setCacheDuration(CACHE_DURATION);
                            globalCacheManager.store();
                        }
                    }
                })
                .flatMap(new Func1<Response<OperatorData>, Observable<List<Operator>>>() {
                    @Override
                    public Observable<List<Operator>> call(Response<OperatorData> operatorDataResponse) {
                        return Observable.just(operatorDataResponse.body().getData());
                    }
                });
    }

    @Override
    public Observable<Status> getObservableStatus() {
        return Observable.concat(getObservableStatusDB(), getObservableStatusNetwork())
                .first(new Func1<Status, Boolean>() {
                    @Override
                    public Boolean call(Status status) {
                        return status != null && status.getData() != null;
                    }
                })
                .doOnNext(validateStatus(true));
    }

    private Observable<Status> getObservableStatusDB() {
        return Observable.just(true)
                .map(new Func1<Boolean, Status>() {
                    @Override
                    public Status call(Boolean aBoolean) {
                        GlobalCacheManager manager = new GlobalCacheManager();
                        return CacheUtil.convertStringToModel(
                                manager.getValueString(KEY_STATUS),
                                new TypeToken<Status>() {
                                }.getType());

                    }
                })
                .onErrorReturn(new Func1<Throwable, Status>() {
                    @Override
                    public Status call(Throwable throwable) {
                        return null;
                    }
                });
    }

    private Observable<Status> getObservableStatusNetwork() {
        return rechargeService.getApi().getStatus()
                .doOnNext(new Action1<Response<Status>>() {
                    @Override
                    public void call(Response<Status> statusResponse) {
                        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
                        globalCacheManager.setKey(KEY_STATUS);
                        globalCacheManager.setValue(CacheUtil.convertModelToString(statusResponse.body(),
                                new TypeToken<Status>() {
                                }.getType()));
                        globalCacheManager.setCacheDuration(CACHE_DURATION);
                        globalCacheManager.store();
                    }
                })
                .flatMap(new Func1<Response<Status>, Observable<Status>>() {
                    @Override
                    public Observable<Status> call(Response<Status> statusResponse) {
                        return Observable.just(statusResponse.body());
                    }
                });
    }

    private Action1<Status> validateStatus(final boolean isInitialGetStatus) {
        return new Action1<Status>() {
            @Override
            public void call(Status status) {
                GlobalCacheManager globalCacheManager = new GlobalCacheManager();
                String currentStatusString = globalCacheManager.getValueString(KEY_STATUS_CURRENT);
                String statusString = CacheUtil.convertModelToString(status,
                        new TypeToken<Status>() {
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
    public Observable<Status> getObservableStatusOnResume() {
        return Observable.concat(getObservableStatusDB(), getObservableStatusNetwork())
                .first(new Func1<Status, Boolean>() {
                    @Override
                    public Boolean call(Status status) {
                        return status != null && status.getData() != null;
                    }
                })
                .doOnNext(validateStatus(false));
    }

    @Override
    public Observable<DigitalNumberList> getObservableNumberList(TKPDMapParam<String, String> param) {
//        List<OrderClientNumber> dummyNumbers = new ArrayList<>();
//        dummyNumbers.add(new OrderClientNumber.Builder()
//                .clientNumber("08188812671")
//                .name("Zidane")
//                .lastUpdated("201710041400")
//                .categoryId("1")
//                .build());
//        dummyNumbers.add(new OrderClientNumber.Builder()
//                .clientNumber("081322867354")
//                .name("Rizky Fadillah")
//                .lastUpdated("201710041200")
//                .categoryId("1")
//                .build());
//        dummyNumbers.add(new OrderClientNumber.Builder()
//                .clientNumber("085611567785")
//                .name("Ronaldo")
//                .lastUpdated("201710041300")
//                .categoryId("1")
//                .build());
//        dummyNumbers.add(new OrderClientNumber.Builder()
//                .clientNumber("08111111111")
//                .lastProduct("118")
//                .lastUpdated("201710041000")
//                .categoryId("1")
//                .operatorId("12")
//                .build());

//        Observable<List<OrderClientNumber>> observableDummyNumbers = Observable.just(dummyNumbers);

        return digitalEndpointService.getApi().getNumberList(param)
                .map(getFuncTransformNumberList())
//        return observableDummyNumbers
                .flatMap(new Func1<List<OrderClientNumber>, Observable<DigitalNumberList>>() {
                    @Override
                    public Observable<DigitalNumberList> call(final List<OrderClientNumber> orderClientNumbers) {
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
                return favoriteNumberMapper
                        .transformDigitalFavoriteNumberItemDataList(responseFavoriteNumbers);
            }
        };
    }

    @Override
    public Observable<LastOrder> getObservableLastOrderFromDBByCategoryId(int categoryId) {
        RechargeNumberListModelDB db = new RechargeNumberListManager()
                .getLastOrderById(categoryId);

        LastOrder lastOrder = new LastOrder();
        LastOrderEntity data = new LastOrderEntity();
        LastOrderEntity.AttributesBean attributesBean = new LastOrderEntity.AttributesBean();
        attributesBean.setCategory_id(db.categoryId);
        attributesBean.setClient_number(db.clientNumber);
        data.setAttributes(attributesBean);
        lastOrder.setData(data);

        return Observable.just(lastOrder);
    }

    @Override
    public Observable<Boolean> hasLastOrder(int categoryId) {
        RechargeNumberListModelDB db = new RechargeNumberListManager()
                .getLastOrderById(categoryId);

        return Observable.just(db == null);
    }

}