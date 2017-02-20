package com.tokopedia.tkpd.home.recharge.interactor;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.database.manager.RechargeRecentDataManager;
import com.tokopedia.core.database.model.RechargeOperatorModel;
import com.tokopedia.core.database.model.category.CategoryData;
import com.tokopedia.core.database.recharge.operator.Operator;
import com.tokopedia.core.database.recharge.operator.OperatorData;
import com.tokopedia.core.database.recharge.product.Product;
import com.tokopedia.core.database.recharge.product.ProductData;
import com.tokopedia.core.database.recharge.recentNumber.RecentData;
import com.tokopedia.core.database.recharge.status.Status;
import com.tokopedia.core.network.apiservices.recharge.RechargeService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * @author ricoharisin on 7/18/16.
 */
public class RechargeInteractorImpl implements RechargeInteractor {

    private static final String TAG = "RechargeInteractorImpl";
    private final static String KEY_CATEGORY = "RECHARGE_CATEGORY";
    private final static String KEY_STATUS = "RECHARGE_STATUS";
    private final static String KEY_PRODUCT = "RECHARGE_PRODUCT";
    private final static String KEY_OPERATOR = "RECHARGE_OPERATOR";
    private final static String KEY_STATUS_CURRENT = "RECHARGE_STATUS_CURRENT";
    private final static int STATE_CATEGORY_NON_ACTIVE = 2;
    private RechargeService rechargeService;
    private static int CACHE_DURATION = 60 * 5;

    public RechargeInteractorImpl() {
        rechargeService = new RechargeService();
    }


    @Override
    public void getListProduct(final OnGetListProduct onGetListProduct, final String prefix,
                               final int categoryId, final Boolean validatePrefix) {

        Observable.zip(getOperatorByPrefix(prefix),
                getObservableListProduct(),
                new Func2<Operator, List<Product>, List<Product>>() {
                    @Override
                    public List<Product> call(final Operator operator, List<Product> products) {
                        return Observable.from(products)
                                .filter(isProductValidToOperator(categoryId, operator.getId()))
                                .toList()
                                .toBlocking()
                                .single();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Product>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        onGetListProduct.onError(e);
                    }

                    @Override
                    public void onNext(List<Product> products) {
                        onGetListProduct.onSuccess(products);
                    }
                });
    }

    @Override
    public void getListProductForOperator(final OnGetListProductForOperator onGetListProductForOperator, final int categoryId) {
        getObservableListProduct()
                .flatMap(new Func1<List<Product>, Observable<Product>>() {
                    @Override
                    public Observable<Product> call(List<Product> products) {
                        return Observable.from(products);
                    }
                })
                .filter(new Func1<Product, Boolean>() {
                    @Override
                    public Boolean call(Product product) {
                        return product.getRelationships().getCategory().getData().getId() == categoryId;
                    }
                })
                .toList()
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Product>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        onGetListProductForOperator.onErrorFetchProdcuts(e);
                    }

                    @Override
                    public void onNext(List<Product> products) {
                        onGetListProductForOperator.onSuccessFetchProducts(products);
                    }
                });
    }

    @Override
    public void getListProductDefaultOperator(
            final OnGetListProduct onGetListProduct, final int categoryId, final String operatorId) {

        getObservableListProduct()
                .flatMap(new Func1<List<Product>, Observable<Product>>() {
                    @Override
                    public Observable<Product> call(List<Product> products) {
                        return Observable.from(products);
                    }
                })
                .filter(isProductValidToOperator(categoryId, Integer.parseInt(operatorId)))
                .toList()
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Product>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        onGetListProduct.onError(e);
                    }

                    @Override
                    public void onNext(List<Product> products) {
                        onGetListProduct.onSuccess(products);
                    }
                });
    }

    @Override
    public void getDetailProductFromOperator(final OnGetDetailProduct listener, int categoryId, String operatorId) {
        getObservableListProduct()
                .flatMap(new Func1<List<Product>, Observable<Product>>() {
                    @Override
                    public Observable<Product> call(List<Product> products) {
                        return Observable.from(products);
                    }
                })
                .filter(isOperatorExist(categoryId, Integer.parseInt(operatorId)))
                .toList()
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Product>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(List<Product> products) {
                        listener.onSuccessDetailProduct(products);
                    }
                });
    }

    @Override
    public void getCategoryData(final OnGetCategory onGetCategory) {
        Observable.concat(getObservableDbCategory(), getObservableNetworkCategory())
                .first(isCategoryExist())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CategoryData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        onGetCategory.onEmpty();
                    }

                    @Override
                    public void onNext(CategoryData categoryData) {
                        if (categoryData != null) {
                            onGetCategory.onSuccess(categoryData);
                        } else {
                            onGetCategory.onEmpty();
                        }
                    }
                });
    }

    @Override
    public void getProductById(final OnGetProductById listener, String categoryId, String operatorId,
                               final String productId) {
        getObservableListProduct()
                .flatMap(new Func1<List<Product>, Observable<Product>>() {
                    @Override
                    public Observable<Product> call(List<Product> products) {
                        return Observable.from(products);
                    }
                })
                .filter(isProductExist(Integer.parseInt(categoryId), Integer.parseInt(operatorId),
                        Integer.parseInt(productId)))
                .toList()
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Product>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(List<Product> products) {
                        if (products.size() > 0) {
                            listener.onSuccessFetchProductById(products.get(0));
                        }
                    }
                });

    }

    @Override
    public void getStatus(final OnGetStatus onGetStatus) {
        Observable.concat(getObservableDbStatus(), getObservableNetworkStatus())
                .first(isStatusExist())
                .doOnNext(validateStatus())
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Status>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        onGetStatus.onEmpty();
                    }

                    @Override
                    public void onNext(Status status) {
                        if (status != null) {
                            onGetStatus.onSuccess(status);
                        } else {
                            onGetStatus.onEmpty();
                        }
                    }
                });
    }

    @Override
    public void storeRecentData(final RecentData recentData) {
        Observable.just(recentData)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(storeRecentDataToDb())
                .subscribe();
    }

    @Override
    public void getRecentData(final int categoryId, final OnGetRecentNumberListener listener) {
        Observable.just(categoryId)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Integer, List<String>>() {
                    @Override
                    public List<String> call(Integer integer) {
                        return new RechargeRecentDataManager().getListDataByCategory(categoryId);
                    }
                })
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(List<String> results) {
                        if (!results.isEmpty()) {
                            listener.onGetRecentNumberSuccess(results);
                        } else {
                            listener.onEmpty();
                        }
                    }
                });
    }

    @Override
    public void getOperatorById(final String operatorId, final OnGetOperatorByIdListener listener) {
        getObservableListOperator()
                .flatMap(new Func1<List<Operator>, Observable<Operator>>() {
                    @Override
                    public Observable<Operator> call(List<Operator> operators) {
                        return Observable.from(operators);
                    }
                })
                .filter(new Func1<Operator, Boolean>() {
                    @Override
                    public Boolean call(Operator operator) {
                        return String.valueOf(operator.getId()).equals(operatorId);
                    }
                })
                .map(convertToRechargeOperatorModel())
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RechargeOperatorModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(RechargeOperatorModel rechargeOperatorModel) {
                        if (rechargeOperatorModel != null) {
                            listener.onSuccess(rechargeOperatorModel);
                        } else {
                            listener.onEmpty();
                        }

                    }
                });
    }

    @Override
    public void getOperatorListByIds(final List<Integer> operatorIds, final OnGetListOperatorByIdsListener listener) {
        getObservableListOperator()
                .flatMap(new Func1<List<Operator>, Observable<Operator>>() {
                    @Override
                    public Observable<Operator> call(List<Operator> operators) {
                        return Observable.from(operators);
                    }
                })
                .filter(new Func1<Operator, Boolean>() {
                    @Override
                    public Boolean call(Operator operator) {
                        return operatorIds.contains(operator.getId());
                    }
                })
                .map(convertToRechargeOperatorModel())
                .toList()
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<RechargeOperatorModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(List<RechargeOperatorModel> results) {
                        if (results.size() > 0) {
                            listener.onSuccessFetchOperators(results);
                        } else {
                            listener.onEmpty();
                        }
                    }
                });
    }

    private Observable<List<Product>> getObservableListProduct() {
        return Observable.concat(getObservableDbListProduct(), getObservableNetworkListProduct())
                .first(isProductExist());
    }

    private Observable<List<Operator>> getObservableListOperator() {
        return Observable.concat(getObservableDbListOperator(), getObservableNetworkListOperator())
                .first(isOperatorExist());
    }

    private Observable<Operator> getOperatorByPrefix(final String prefix) {
        return getObservableListOperator()
                .flatMap(new Func1<List<Operator>, Observable<Operator>>() {
                    @Override
                    public Observable<Operator> call(List<Operator> operators) {
                        return Observable.from(operators);
                    }
                })
                .filter(new Func1<Operator, Boolean>() {
                    @Override
                    public Boolean call(Operator operator) {
                        return operator.getAttributes().getPrefix().contains(prefix);
                    }
                });
    }

    private Observable<CategoryData> getObservableNetworkCategory() {
        return rechargeService.getApi().getCategory()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(storeResponseCategoryToDb())
                .flatMap(new Func1<Response<CategoryData>, Observable<CategoryData>>() {
                    @Override
                    public Observable<CategoryData> call(Response<CategoryData> categoryDataResponse) {
                        Log.i("OBSERVABLE", "network enter : " + categoryDataResponse);
                        return Observable.just(categoryDataResponse.body());
                    }
                });
    }

    private Observable<CategoryData> getObservableDbCategory() {
        return Observable.just(true)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(getCategoryFromDb())
                .onErrorReturn(new Func1<Throwable, List<com.tokopedia.core.database.model.category.Category>>() {
                    @Override
                    public List<com.tokopedia.core.database.model.category.Category> call(Throwable throwable) {
                        return new ArrayList<com.tokopedia.core.database.model.category.Category>();
                    }
                })
                .flatMap(new Func1<List<com.tokopedia.core.database.model.category.Category>, Observable<CategoryData>>() {
                    @Override
                    public Observable<CategoryData> call(List<com.tokopedia.core.database.model.category.Category> categories) {
                        CategoryData categoryData = new CategoryData();
                        categoryData.setData(categories);
                        return Observable.just(categoryData);
                    }
                });
    }

    private Observable<List<Product>> getObservableNetworkListProduct() {
        return rechargeService.getApi().getProduct()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(storeResponseProductToDb())
                .flatMap(new Func1<Response<ProductData>, Observable<List<Product>>>() {
                    @Override
                    public Observable<List<Product>> call(Response<ProductData> productDataResponse) {
                        return Observable.just(productDataResponse.body().getData());
                    }
                });
    }

    private Observable<List<Product>> getObservableDbListProduct() {
        return Observable.just(true)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Boolean, List<Product>>() {
                    @Override
                    public List<Product> call(Boolean condition) {
                        GlobalCacheManager manager = new GlobalCacheManager();
                        return CacheUtil.convertStringToListModel(
                                manager.getValueString(KEY_PRODUCT),
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

    private Observable<List<Operator>> getObservableDbListOperator() {
        return Observable.just(true)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Boolean, List<Operator>>() {
                    @Override
                    public List<Operator> call(Boolean condition) {
                        GlobalCacheManager manager = new GlobalCacheManager();
                        return CacheUtil.convertStringToListModel(
                                manager.getValueString(KEY_OPERATOR),
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

    private Observable<List<Operator>> getObservableNetworkListOperator() {
        return rechargeService.getApi().getOperator()
                .doOnNext(storeResponseOperatorToDb())
                .flatMap(new Func1<Response<OperatorData>, Observable<List<Operator>>>() {
                    @Override
                    public Observable<List<Operator>> call(Response<OperatorData> operatorDataResponse) {
                        return Observable.just(operatorDataResponse.body().getData());
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Status> getObservableNetworkStatus() {
        return rechargeService.getApi().getStatus()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .doOnNext(storeResponseStatusToDb())
                .flatMap(new Func1<Response<Status>, Observable<Status>>() {
                    @Override
                    public Observable<Status> call(Response<Status> statusResponse) {
                        return Observable.just(statusResponse.body());
                    }
                });
    }

    private Observable<Status> getObservableDbStatus() {
        return Observable.just(true)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
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

    private Action1<Response<Status>> storeResponseStatusToDb() {
        return new Action1<Response<Status>>() {
            @Override
            public void call(Response<Status> statusResponse) {
                GlobalCacheManager manager = new GlobalCacheManager();
                manager.setKey(KEY_STATUS);
                manager.setValue(CacheUtil.convertModelToString(statusResponse.body(),
                        new TypeToken<Status>() {
                        }.getType()));
                manager.setCacheDuration(CACHE_DURATION);
                manager.store();
            }
        };
    }

    private Action1<Response<OperatorData>> storeResponseOperatorToDb() {
        return new Action1<Response<OperatorData>>() {
            @Override
            public void call(Response<OperatorData> operatorDataResponse) {
                OperatorData operatorData = operatorDataResponse.body();
                GlobalCacheManager manager = new GlobalCacheManager();
                if (operatorData != null && operatorData.getData() != null) {
                    manager.setKey(RechargeInteractorImpl.KEY_OPERATOR);
                    manager.setValue(CacheUtil.convertListModelToString(operatorData.getData(),
                            new TypeToken<List<Operator>>() {
                            }.getType()));
                    manager.store();
                }
            }
        };
    }

    private Action1<Response<ProductData>> storeResponseProductToDb() {
        return new Action1<Response<ProductData>>() {
            @Override
            public void call(Response<ProductData> productDataResponse) {
                ProductData productData = productDataResponse.body();
                GlobalCacheManager manager = new GlobalCacheManager();
                if (productData != null && productData.getData() != null) {
                    manager.setKey(RechargeInteractorImpl.KEY_PRODUCT);
                    manager.setValue(CacheUtil.convertListModelToString(productData.getData(),
                            new TypeToken<List<Product>>() {
                            }.getType()));
                    manager.store();
                }
            }
        };
    }

    private Func1<Boolean, List<com.tokopedia.core.database.model.category.Category>> getCategoryFromDb() {
        return new Func1<Boolean, List<com.tokopedia.core.database.model.category.Category>>() {
            @Override
            public List<com.tokopedia.core.database.model.category.Category> call(Boolean aBoolean) {
                GlobalCacheManager manager = new GlobalCacheManager();
                manager.getValueString(KEY_CATEGORY);
                return CacheUtil.convertStringToListModel(
                        manager.getValueString(KEY_CATEGORY),
                        new TypeToken<List<com.tokopedia.core.database.model.category.Category>>() {
                        }.getType());
            }
        };
    }

    private Action1<Response<CategoryData>> storeResponseCategoryToDb() {
        return new Action1<Response<CategoryData>>() {
            @Override
            public void call(Response<CategoryData> categoryDataResponse) {
                CategoryData categoryData = categoryDataResponse.body();
                GlobalCacheManager manager = new GlobalCacheManager();
                if (categoryData != null) {
                    manager.setKey(RechargeInteractorImpl.KEY_CATEGORY);
                    manager.setValue(CacheUtil.convertListModelToString(categoryData.getData(),
                            new TypeToken<List<com.tokopedia.core.database.model.category.Category>>() {
                            }.getType()));
                    manager.store();
                }
            }
        };
    }

    private Func1<List<Operator>, Boolean> isOperatorExist() {
        return new Func1<List<Operator>, Boolean>() {
            @Override
            public Boolean call(List<Operator> operators) {
                return operators != null && !operators.isEmpty();
            }
        };
    }

    private Func1<List<Product>, Boolean> isProductExist() {
        return new Func1<List<Product>, Boolean>() {
            @Override
            public Boolean call(List<Product> products) {
                return products != null && !products.isEmpty();
            }
        };
    }

    private Func1<Operator, RechargeOperatorModel> convertToRechargeOperatorModel() {
        return new Func1<Operator, RechargeOperatorModel>() {
            @Override
            public RechargeOperatorModel call(Operator operator) {
                RechargeOperatorModel rechargeModel = new RechargeOperatorModel();
                rechargeModel.image = operator.getAttributes().getImage();
                rechargeModel.maximumLength = operator.getAttributes().getMaximumLength();
                rechargeModel.minimumLength = operator.getAttributes().getMinimumLength();
                rechargeModel.name = operator.getAttributes().getName();
                rechargeModel.nominalText = operator.getAttributes().getRule().getProductText();
                rechargeModel.operatorId = operator.getId();
                rechargeModel.showPrice = operator.getAttributes().getRule().getShowPrice();
                rechargeModel.showProduct = operator.getAttributes().getRule().getShowProduct();
                rechargeModel.status = operator.getAttributes().getStatus();
                rechargeModel.weight = operator.getAttributes().getWeight();
                rechargeModel.defaultProductId = operator.getAttributes().getDefaultProductId();
                return rechargeModel;
            }
        };
    }

    private Action1<Status> validateStatus() {
        return new Action1<Status>() {
            @Override
            public void call(Status status) {
                GlobalCacheManager manager = new GlobalCacheManager();
                String currentStatusString = manager.getValueString(KEY_STATUS_CURRENT);
                String statusString = CacheUtil.convertModelToString(status,
                        new TypeToken<Status>() {
                        }.getType());
                if (currentStatusString != null && !currentStatusString.equals(statusString)) {
                    manager.delete(KEY_CATEGORY);
                    manager.delete(KEY_OPERATOR);
                    manager.delete(KEY_PRODUCT);

                    GlobalCacheManager managerStatus = new GlobalCacheManager();
                    managerStatus.setKey(KEY_STATUS_CURRENT);
                    managerStatus.setValue(statusString);
                    managerStatus.store();
                } else if (currentStatusString == null) {
                    GlobalCacheManager managerStatus = new GlobalCacheManager();
                    managerStatus.setKey(KEY_STATUS_CURRENT);
                    managerStatus.setValue(statusString);
                    managerStatus.store();
                }
            }
        };
    }

    private Func1<RecentData, Boolean> storeRecentDataToDb() {
        return new Func1<RecentData, Boolean>() {
            @Override
            public Boolean call(RecentData productData) {
                RechargeRecentDataManager dbManager = new RechargeRecentDataManager();
                if (productData != null && productData.getData() != null)
                    dbManager.bulkInsert(productData.getData());

                return true;
            }
        };
    }

    private Func1<Status, Boolean> isStatusExist() {
        return new Func1<Status, Boolean>() {
            @Override
            public Boolean call(Status status) {
                return status != null && status.getData() != null;
            }
        };
    }

    private Func1<CategoryData, Boolean> isCategoryExist() {
        return new Func1<CategoryData, Boolean>() {
            @Override
            public Boolean call(CategoryData categoryData) {
                return categoryData != null &&
                        categoryData.getData() != null &&
                        !categoryData.getData().isEmpty();
            }
        };
    }

    private Func1<Product, Boolean> isProductValidToOperator(final int categoryId, final int operatorId) {
        return new Func1<Product, Boolean>() {
            @Override
            public Boolean call(Product product) {
                return product
                        .getRelationships()
                        .getCategory()
                        .getData()
                        .getId() == categoryId
                        &&
                        product
                                .getRelationships()
                                .getOperator()
                                .getData()
                                .getId() == operatorId
                        &&
                        product
                                .getAttributes()
                                .getStatus() != STATE_CATEGORY_NON_ACTIVE;
            }
        };
    }

    private Func1<Product, Boolean> isProductExist(final int categoryId, final int operatorId, final int productId) {
        return new Func1<Product, Boolean>() {
            @Override
            public Boolean call(Product product) {
                return product
                        .getRelationships()
                        .getCategory()
                        .getData()
                        .getId() == categoryId
                        &&
                        product
                                .getRelationships()
                                .getOperator()
                                .getData()
                                .getId() == operatorId
                        &&
                        product
                                .getId() == productId;
            }
        };
    }

    private Func1<Product, Boolean> isOperatorExist(final int categoryId, final int operatorId) {
        return new Func1<Product, Boolean>() {
            @Override
            public Boolean call(Product product) {
                return product
                        .getRelationships()
                        .getCategory()
                        .getData()
                        .getId() == categoryId
                        &&
                        product
                                .getRelationships()
                                .getOperator()
                                .getData()
                                .getId() == operatorId;
            }
        };
    }
}
