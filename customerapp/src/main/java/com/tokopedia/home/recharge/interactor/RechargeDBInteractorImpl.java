package com.tokopedia.home.recharge.interactor;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.database.manager.RechargeOperatorManager;
import com.tokopedia.core.database.manager.RechargeProductManager;
import com.tokopedia.core.database.manager.RechargeRecentDataManager;
import com.tokopedia.core.database.model.RechargeOperatorModelDB;
import com.tokopedia.core.database.model.category.CategoryData;
import com.tokopedia.core.database.recharge.operator.OperatorData;
import com.tokopedia.core.database.recharge.product.Category;
import com.tokopedia.core.database.recharge.product.Product;
import com.tokopedia.core.database.recharge.product.ProductData;
import com.tokopedia.core.database.recharge.recentNumber.RecentData;
import com.tokopedia.core.database.recharge.status.Status;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author ricoharisin on 7/18/16.
 */
public class RechargeDBInteractorImpl implements RechargeDBInteractor {

    public final static String KEY_CATEGORY = "RECHARGE_CATEGORY";
    public final static String KEY_STATUS = "RECHARGE_STATUS";


    @Override
    public void getListProduct(final OnGetListProduct onGetListProduct, String prefix,
                               final int categoryId, final Boolean validatePrefix) {
        Observable.just(prefix)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, RechargeOperatorModelDB>() {
                    @Override
                    public RechargeOperatorModelDB call(String s) {
                        return new RechargeOperatorManager().getData(s);
                    }
                })
                .map(new Func1<RechargeOperatorModelDB, List<Product>>() {
                    @Override
                    public List<Product> call(RechargeOperatorModelDB rechargeOperatorModelDB) {
                        if (validatePrefix)
                            return new RechargeProductManager().getListData(categoryId,
                                    rechargeOperatorModelDB.operatorId);

                        return new RechargeProductManager().getListDataByCategory(categoryId);
                    }
                })
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
    public void getCategory(final OnGetCategory onGetCategory) {
        Observable.just(true)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Boolean, List<Category>>() {
                    @Override
                    public List<Category> call(Boolean aBoolean) {
                        GlobalCacheManager manager = new GlobalCacheManager();
                        return CacheUtil.convertStringToListModel(
                                manager.getValueString(KEY_CATEGORY),
                                new TypeToken<List<Category>>() {
                                }.getType());

                    }
                })
                .subscribe(new Subscriber<List<Category>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        onGetCategory.onEmpty();
                    }

                    @Override
                    public void onNext(List<Category> categories) {
                        if (categories != null) {
                            onGetCategory.onSuccess(categories);
                        } else {
                            onGetCategory.onEmpty();
                        }
                    }
                });
    }

    @Override
    public void getStatus(final OnGetStatus onGetStatus) {
        Observable.just(true)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Boolean, Status>() {
                    @Override
                    public Status call(Boolean aBoolean) {
                        GlobalCacheManager manager = new GlobalCacheManager();
                        return CacheUtil.convertStringToModel(
                                manager.getValueString(KEY_STATUS),
                                Status.class);

                    }
                })
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
    public void storeCategoryData(CategoryData categoryData) {
        Observable.just(categoryData)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<CategoryData, Boolean>() {
                    @Override
                    public Boolean call(CategoryData categoryData) {
                        GlobalCacheManager manager = new GlobalCacheManager();
                        if (categoryData != null) {
                            manager.setKey(RechargeDBInteractorImpl.KEY_CATEGORY);
                            manager.setValue(CacheUtil.convertListModelToString(categoryData.getData(),
                                    new TypeToken<List<Category>>() {
                                    }.getType()));
                            manager.store();
                        }
                        return true;
                    }
                })
                .subscribe();
    }

    @Override
    public void storeOperatorData(OperatorData operatorData) {
        Observable.just(operatorData)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<OperatorData, Boolean>() {
                    @Override
                    public Boolean call(OperatorData operatorData) {
                        RechargeOperatorManager dbManager = new RechargeOperatorManager();
                        if (operatorData != null && operatorData.getData() != null)
                            dbManager.bulkInsert(operatorData.getData());

                        return true;
                    }
                })
                .subscribe();
    }

    @Override
    public void storeProductData(ProductData productData) {
        Observable.just(productData)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<ProductData, Boolean>() {
                    @Override
                    public Boolean call(ProductData productData) {
                        RechargeProductManager dbManager = new RechargeProductManager();
                        if (productData != null && productData.getData() != null)
                            dbManager.bulkInsert(productData.getData());

                        return true;
                    }
                })
                .subscribe();
    }

    @Override
    public void storeRecentData(final RecentData recentData) {
        Observable.just(recentData)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<RecentData, Boolean>() {
                    @Override
                    public Boolean call(RecentData productData) {
                        RechargeRecentDataManager dbManager = new RechargeRecentDataManager();
                        if (productData != null && productData.getData() != null)
                            dbManager.bulkInsert(productData.getData());

                        return true;
                    }
                })
                .subscribe();
    }


    @Override
    public void storeStatus(Status status) {
        Observable.just(status)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Status, Boolean>() {
                    @Override
                    public Boolean call(Status status) {
                        GlobalCacheManager manager = new GlobalCacheManager();
                        manager.setKey(RechargeDBInteractorImpl.KEY_STATUS);
                        manager.setValue(CacheUtil.convertModelToString(status, Status.class));
                        manager.store();

                        return true;
                    }
                })
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
    public void getOperatorById(String operatorId, final OnGetOperatorByIdListener listener) {
        Observable.just(operatorId)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, RechargeOperatorModelDB>() {
                    @Override
                    public RechargeOperatorModelDB call(String operatorId) {
                        return new RechargeOperatorManager().getDataOperator(Integer.valueOf(operatorId));

                    }
                }).subscribe(new Subscriber<RechargeOperatorModelDB>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e);
            }

            @Override
            public void onNext(RechargeOperatorModelDB rechargeOperatorModelDB) {
                if (rechargeOperatorModelDB != null) {
                    listener.onSuccess(rechargeOperatorModelDB);
                } else {
                    listener.onEmpty();
                }

            }
        });

    }

}
