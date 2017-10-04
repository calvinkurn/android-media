package com.tokopedia.digital.widget.domain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.database.manager.RechargeRecentDataManager;
import com.tokopedia.core.database.model.category.Category;
import com.tokopedia.core.database.model.category.CategoryData;
import com.tokopedia.core.database.recharge.operator.Operator;
import com.tokopedia.core.database.recharge.operator.OperatorData;
import com.tokopedia.core.database.recharge.product.Product;
import com.tokopedia.core.database.recharge.product.ProductData;
import com.tokopedia.core.database.recharge.recentNumber.RecentData;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.core.database.recharge.status.Status;
import com.tokopedia.core.network.apiservices.recharge.RechargeService;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.utils.MapNulRemover;
import com.tokopedia.digital.apiservice.DigitalEndpointService;
import com.tokopedia.digital.widget.errorhandle.WidgetRuntimeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public DigitalWidgetRepository(RechargeService rechargeService,
                                   DigitalEndpointService digitalEndpointService) {
        this.rechargeService = rechargeService;
        this.digitalEndpointService = digitalEndpointService;
    }

    @Override
    public Observable<CategoryData> getObservableCategoryData() {
        return Observable.concat(getObservableCategoryDataDB(), getObservableCategoryDataNetwork())
                .first(new Func1<CategoryData, Boolean>() {
                    @Override
                    public Boolean call(CategoryData categoryData) {
                        return categoryData != null &&
                                categoryData.getData() != null &&
                                !categoryData.getData().isEmpty()
                                && isCategoryDataNotCorrupt(categoryData);
                    }
                });
    }

    private boolean isCategoryDataNotCorrupt(CategoryData categoryData) {
        for (Category data : categoryData.getData()) {
            if (data.getAttributes() == null || data.getAttributes().getClientNumber() == null
                    || data.getAttributes().getClientNumber().getOperatorStyle() == null)
                return false;
        }
        return true;
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
                    throw new WidgetRuntimeException("Is no need to reload widget");
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
    public Observable<List<String>> getObservableRecentData(final int categoryId) {
        List<String> recentDataList = new RechargeRecentDataManager()
                .getListDataByCategory(categoryId);
        return Observable.just(recentDataList);
    }

    @Override
    public Observable<Boolean> storeObservableRecentDataNetwork(Map<String, String> params) {
        return digitalEndpointService.getApi().getRecentNumber(MapNulRemover.removeNull(params))
                .map(new Func1<Response<TkpdDigitalResponse>, RecentData>() {
                    @Override
                    public RecentData call(Response<TkpdDigitalResponse> recentNumberResponse) {
                        return new Gson().fromJson(
                                recentNumberResponse.body().getStrResponse(), RecentData.class);
                    }
                })
                .map(new Func1<RecentData, Boolean>() {
                    @Override
                    public Boolean call(RecentData productData) {
                        RechargeRecentDataManager dbManager = new RechargeRecentDataManager();
                        if (productData != null && productData.getData() != null)
                            dbManager.bulkInsert(productData.getData());
                        return true;
                    }
                });
    }

    @Override
    public Observable<LastOrder> getObservableLastOrderNetwork(Map<String, String> params) {
        return digitalEndpointService.getApi().getLastOrder(MapNulRemover.removeNull(params))
                .map(new Func1<Response<TkpdDigitalResponse>, LastOrder>() {
                    @Override
                    public LastOrder call(Response<TkpdDigitalResponse> recentNumberResponse) {
                        if (recentNumberResponse.isSuccessful()) {
                            return new Gson().fromJson(
                                    recentNumberResponse.body().getStrResponse(), LastOrder.class);
                        } else {
                            return null;
                        }
                    }
                });
    }
}