package com.tokopedia.digital.widget.interactor;

import com.tokopedia.core.database.model.RechargeOperatorModel;
import com.tokopedia.core.database.recharge.operator.Operator;
import com.tokopedia.core.database.recharge.product.Product;
import com.tokopedia.digital.widget.domain.DigitalWidgetRepository;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 7/21/17.
 */

public class DigitalWidgetInteractor implements IDigitalWidgetInteractor {

    private final static int STATE_CATEGORY_NON_ACTIVE = 2;

    private CompositeSubscription compositeSubscription;

    private DigitalWidgetRepository digitalWidgetRepository;

    public DigitalWidgetInteractor(CompositeSubscription compositeSubscription,
                                   DigitalWidgetRepository digitalWidgetRepository) {
        this.compositeSubscription = compositeSubscription;
        this.digitalWidgetRepository = digitalWidgetRepository;
    }

    @Override
    public void getProductsFromPrefix(Subscriber<List<Product>> subscriber, final int categoryId, String prefix,
                                      Boolean validatePrefix) {
        compositeSubscription.add(
                Observable.zip(getOperatorByPrefix(prefix),
                        digitalWidgetRepository.getObservableProducts().doOnNext(
                                new Action1<List<Product>>() {
                                    @Override
                                    public void call(List<Product> products) {
                                        if (products.size() == 0)
                                            throw new RuntimeException("kosong");
                                    }
                                }
                        ), new Func2<List<Operator>, List<Product>, List<Product>>() {
                            @Override
                            public List<Product> call(List<Operator> operators, List<Product> products) {
                                return Observable.from(products)
                                        .filter(isProductValidToOperator(categoryId, operators.get(0).getId()))
                                        .toList()
                                        .toBlocking()
                                        .single();
                            }
                        })
                        .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<Product>>>() {
                            @Override
                            public Observable<? extends List<Product>> call(Throwable throwable) {
                                return Observable.just(new ArrayList<Product>());
                            }
                        })

                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber));
    }

    private Observable<List<Operator>> getOperatorByPrefix(final String prefix) {
        return digitalWidgetRepository.getObservableOperators()
                .flatMap(new Func1<List<Operator>, Observable<Operator>>() {
                    @Override
                    public Observable<Operator> call(List<Operator> operators) {
                        return Observable.from(operators);
                    }
                })
                .filter(new Func1<Operator, Boolean>() {
                    @Override
                    public Boolean call(Operator operator) {
                        String prefixTemp = prefix.substring(0, 4);
                        if (operator.getAttributes().getPrefix().contains(prefixTemp)) {
                            return true;
                        } else {
                            prefixTemp = prefix.substring(0, 3);
                            return operator.getAttributes().getPrefix().contains(prefixTemp);
                        }
                    }
                })
                .toList()
                .doOnNext(new Action1<List<Operator>>() {
                    @Override
                    public void call(List<Operator> operators) {
                        if (operators.size() == 0) {
                            throw new RuntimeException("kosong");
                        }
                    }
                });
    }

    @Override
    public void getOperatorsFromCategory(Subscriber<List<RechargeOperatorModel>> subscriber, final int categoryId) {
        compositeSubscription.add(Observable.zip(
                digitalWidgetRepository.getObservableOperators(), getIdOperators(categoryId),
                new Func2<List<Operator>, List<Integer>, List<RechargeOperatorModel>>() {
                    @Override
                    public List<RechargeOperatorModel> call(List<Operator> operators, final List<Integer> integers) {
                        return Observable.just(operators)
                                .flatMap(new Func1<List<Operator>, Observable<Operator>>() {
                                    @Override
                                    public Observable<Operator> call(List<Operator> operators) {
                                        return Observable.from(operators);
                                    }
                                })
                                .filter(new Func1<Operator, Boolean>() {
                                    @Override
                                    public Boolean call(Operator operator) {
                                        return integers.contains(operator.getId());
                                    }
                                })
                                .map(convertToRechargeOperatorModel())
                                .toList()
                                .toBlocking()
                                .single();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));

    }

    private Observable<List<Integer>> getIdOperators(final int categoryId) {
        return digitalWidgetRepository.getObservableProducts()
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
                .map(new Func1<List<Product>, List<Integer>>() {
                    @Override
                    public List<Integer> call(List<Product> products) {
                        List<Integer> operatorIds = new ArrayList<>();
                        for (Product prod : products) {
                            if (!operatorIds.contains(prod.getRelationships().getOperator().getData().getId()))
                                operatorIds.add(prod.getRelationships().getOperator().getData().getId());
                        }
                        return operatorIds;
                    }
                });
    }

    @Override
    public void getProductsFromOperator(Subscriber<List<Product>> subscriber, int categoryId, String operatorId) {
        compositeSubscription.add(
                digitalWidgetRepository.getObservableProducts()
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
                        .subscribe(subscriber));
    }

    @Override
    public void getOperatorById(Subscriber<RechargeOperatorModel> subscriber, final String operatorId) {
        compositeSubscription.add(
                digitalWidgetRepository.getObservableOperators()
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
                        .subscribe(subscriber));
    }

    @Override
    public void getProductById(Subscriber<Product> subscriber, String categoryId, String operatorId,
                               String productId) {
        compositeSubscription.add(
                digitalWidgetRepository.getObservableProducts()
                        .flatMap(new Func1<List<Product>, Observable<Product>>() {
                            @Override
                            public Observable<Product> call(List<Product> products) {
                                return Observable.from(products);
                            }
                        })
                        .filter(isProductExist(Integer.parseInt(categoryId),
                                Integer.parseInt(operatorId), Integer.parseInt(productId)))
                        .toList()
                        .map(new Func1<List<Product>, Product>() {
                            @Override
                            public Product call(List<Product> products) {
                                return products.get(0);
                            }
                        })
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber));
    }

    @Override
    public void getRecentData(Subscriber<List<String>> subscriber, final int categoryId) {
        compositeSubscription.add(
                digitalWidgetRepository.getObservableRecentData(categoryId)
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber));
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
                        product.getAttributes().getStatus() != STATE_CATEGORY_NON_ACTIVE;
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
                        product.getId() == productId
                        &&
                        product.getAttributes().getStatus() != STATE_CATEGORY_NON_ACTIVE;
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
                rechargeModel.showPrice = operator.getAttributes().getRule().isShowPrice();
                rechargeModel.showProduct = operator.getAttributes().getRule().isShowProduct();
                rechargeModel.status = operator.getAttributes().getStatus();
                rechargeModel.weight = operator.getAttributes().getWeight();
                rechargeModel.defaultProductId = operator.getAttributes().getDefaultProductId();
                rechargeModel.allowAlphanumeric = operator.getAttributes().getRule().isAllowAphanumericNumber();
                return rechargeModel;
            }
        };
    }

    @Override
    public void onDestroy() {
        if (compositeSubscription != null)
            compositeSubscription.unsubscribe();
    }
}