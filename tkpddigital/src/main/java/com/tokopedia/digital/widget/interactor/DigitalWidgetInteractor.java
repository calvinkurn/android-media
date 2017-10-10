package com.tokopedia.digital.widget.interactor;

import com.tokopedia.digital.widget.domain.DigitalWidgetRepository;
import com.tokopedia.digital.widget.model.mapper.OperatorMapper;
import com.tokopedia.digital.widget.model.mapper.ProductMapper;
import com.tokopedia.digital.widget.model.operator.Operator;
import com.tokopedia.digital.widget.model.product.Product;

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

    private ProductMapper productMapper;
    private OperatorMapper operatorMapper;

    public DigitalWidgetInteractor(CompositeSubscription compositeSubscription,
                                   DigitalWidgetRepository digitalWidgetRepository,
                                   ProductMapper productMapper,
                                   OperatorMapper operatorMapper) {
        this.compositeSubscription = compositeSubscription;
        this.digitalWidgetRepository = digitalWidgetRepository;
        this.operatorMapper = operatorMapper;
        this.productMapper = productMapper;
    }

    @Override
    public void getProductsFromPrefix(Subscriber<List<Product>> subscriber, final int categoryId, String prefix,
                                      Boolean validatePrefix) {
        compositeSubscription.add(
                Observable.zip(getOperatorByPrefix(prefix),
                        digitalWidgetRepository.getObservableProducts()
                                .map(productMapper)
                                .doOnNext(
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

                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber));
    }

    private Observable<List<Operator>> getOperatorByPrefix(final String prefix) {
        return digitalWidgetRepository.getObservableOperators()
                .map(operatorMapper)
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
    public void getOperatorsFromCategory(Subscriber<List<Operator>> subscriber, final int categoryId) {
        compositeSubscription.add(Observable.zip(
                digitalWidgetRepository.getObservableOperators().map(operatorMapper),
                getIdOperators(categoryId), new Func2<List<Operator>, List<Integer>, List<Operator>>() {
                    @Override
                    public List<Operator> call(List<Operator> operators, final List<Integer> integers) {
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
                                .toList()
                                .toBlocking()
                                .single();
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));

    }

    private Observable<List<Integer>> getIdOperators(final int categoryId) {
        return digitalWidgetRepository.getObservableProducts()
                .map(productMapper)
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
                        .map(productMapper)
                        .flatMap(new Func1<List<Product>, Observable<Product>>() {
                            @Override
                            public Observable<Product> call(List<Product> products) {
                                return Observable.from(products);
                            }
                        })
                        .filter(isProductValidToOperator(categoryId, Integer.parseInt(operatorId)))
                        .toList()
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber));
    }

    @Override
    public void getOperatorById(Subscriber<Operator> subscriber, final String operatorId) {
        compositeSubscription.add(
                digitalWidgetRepository.getObservableOperators()
                        .map(operatorMapper)
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
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber));
    }

    @Override
    public void getProductById(Subscriber<Product> subscriber, String categoryId, String operatorId,
                               String productId) {
        compositeSubscription.add(
                digitalWidgetRepository.getObservableProducts()
                        .map(productMapper)
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
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber));
    }

    @Override
    public void getRecentData(Subscriber<List<String>> subscriber, final int categoryId) {
        compositeSubscription.add(
                digitalWidgetRepository.getObservableRecentData(categoryId)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
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

    @Override
    public void onDestroy() {
        if (compositeSubscription != null)
            compositeSubscription.unsubscribe();
    }
}