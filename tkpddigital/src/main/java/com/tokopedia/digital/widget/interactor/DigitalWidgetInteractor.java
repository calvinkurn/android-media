package com.tokopedia.digital.widget.interactor;

import android.support.v4.util.Pair;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.widget.domain.DigitalWidgetRepository;
import com.tokopedia.digital.widget.model.mapper.OperatorMapper;
import com.tokopedia.digital.widget.model.mapper.ProductMapper;
import com.tokopedia.digital.widget.model.operator.Operator;
import com.tokopedia.digital.widget.model.product.Product;
import com.tokopedia.digital.widget.model.DigitalNumberList;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 7/21/17.
 * Modified by rizkyfadillah at 10/6/17.
 */

public class DigitalWidgetInteractor implements IDigitalWidgetInteractor {

    private final static int STATE_CATEGORY_NON_ACTIVE = 2;

    private CompositeSubscription compositeSubscription;

    private DigitalWidgetRepository digitalWidgetRepository;

    private ProductMapper productMapper;
    private OperatorMapper operatorMapper;
    private ThreadExecutor threadExecutor;
    private PostExecutionThread postExecutionThread;
    private boolean useCache;

    public DigitalWidgetInteractor(CompositeSubscription compositeSubscription,
                                   DigitalWidgetRepository digitalWidgetRepository,
                                   ProductMapper productMapper,
                                   OperatorMapper operatorMapper,
                                   JobExecutor jobExecutor,
                                   PostExecutionThread postExecutionThread,
                                   boolean useCache) {
        this.compositeSubscription = compositeSubscription;
        this.digitalWidgetRepository = digitalWidgetRepository;
        this.operatorMapper = operatorMapper;
        this.productMapper = productMapper;
        this.threadExecutor = jobExecutor;
        this.postExecutionThread = postExecutionThread;
        this.useCache = useCache;
    }

    @Override
    public void getOperatorAndProductsFromPrefix(Subscriber<Pair<Operator, List<Product>>> subscriber,
                                                 final int categoryId, String prefix, Boolean validatePrefix) {
        compositeSubscription.add(
                Observable.zip(
                        getOperatorByPrefix(prefix),
                        digitalWidgetRepository.getObservableProducts(useCache)
                                .map(productMapper)
                                .doOnNext(
                                        new Action1<List<Product>>() {
                                            @Override
                                            public void call(List<Product> products) {
                                                if (products.size() == 0)
                                                    throw new RuntimeException("kosong");
                                            }
                                        }
                                ),
                        new Func2<List<Operator>, List<Product>, Pair<Operator, List<Product>>>() {
                            @Override
                            public Pair<Operator, List<Product>> call(List<Operator> operators, List<Product> products) {
                                List<Product> filteredProducts = Observable.from(products)
                                        .filter(isProductValidToOperator(categoryId, operators.get(0).getId()))
                                        .toList()
                                        .toBlocking()
                                        .single();

                                return Pair.create(operators.get(0), filteredProducts);
                            }
                        })
                        .unsubscribeOn(Schedulers.from(threadExecutor))
                        .subscribeOn(Schedulers.from(threadExecutor))
                        .observeOn(postExecutionThread.getScheduler())
                        .subscribe(subscriber));
    }

    @Override
    public void getOperatorAndProductsByOperatorId(Subscriber<Pair<Operator, List<Product>>> subscriber,
                                                   final int categoryId, String operatorId) {
        compositeSubscription.add(
                Observable.zip(
                        getOperatorById(operatorId),
                        digitalWidgetRepository.getObservableProducts(useCache)
                                .map(productMapper)
                                .doOnNext(
                                        new Action1<List<Product>>() {
                                            @Override
                                            public void call(List<Product> products) {
                                                if (products.size() == 0)
                                                    throw new RuntimeException("kosong");
                                            }
                                        }
                                ),
                        new Func2<List<Operator>, List<Product>, Pair<Operator, List<Product>>>() {
                            @Override
                            public Pair<Operator, List<Product>> call(List<Operator> operators, List<Product> products) {
                                List<Product> filteredProducts = Observable.from(products)
                                        .filter(isProductValidToOperator(categoryId, operators.get(0).getId()))
                                        .toList()
                                        .toBlocking()
                                        .single();

                                return Pair.create(operators.get(0), filteredProducts);
                            }
                        })
                        .unsubscribeOn(Schedulers.from(threadExecutor))
                        .subscribeOn(Schedulers.from(threadExecutor))
                        .observeOn(postExecutionThread.getScheduler())
                        .subscribe(subscriber));
    }

    @Override
    public void getProductsByOperatorId(Subscriber<List<Product>> subscriber, int categoryId, String operatorId) {
        compositeSubscription.add(
                digitalWidgetRepository.getObservableProducts(useCache)
                        .map(productMapper)
                        .flatMap(new Func1<List<Product>, Observable<Product>>() {
                            @Override
                            public Observable<Product> call(List<Product> products) {
                                return Observable.from(products);
                            }
                        })
                        .filter(isProductValidToOperator(categoryId, Integer.parseInt(operatorId)))
                        .toList()
                        .unsubscribeOn(Schedulers.from(threadExecutor))
                        .subscribeOn(Schedulers.from(threadExecutor))
                        .observeOn(postExecutionThread.getScheduler())
                        .subscribe(subscriber));
    }

    private Observable<List<Operator>> getOperatorByPrefix(final String prefix) {
        return digitalWidgetRepository.getObservableOperators(useCache)
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

    private Observable<List<Operator>> getOperatorById(final String operatorId) {
        return digitalWidgetRepository.getObservableOperators(useCache)
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
    public void getOperatorsByCategoryId(Subscriber<List<Operator>> subscriber, final int categoryId) {
        compositeSubscription.add(Observable.zip(
                digitalWidgetRepository.getObservableOperators(useCache).map(operatorMapper),
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
                .unsubscribeOn(Schedulers.from(threadExecutor))
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.getScheduler())
                .subscribe(subscriber));
    }

    private Observable<List<Integer>> getIdOperators(final int categoryId) {
        return digitalWidgetRepository.getObservableProducts(useCache)
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
    public void getOperatorById(Subscriber<Operator> subscriber, final String operatorId) {
        compositeSubscription.add(
                digitalWidgetRepository.getObservableOperators(useCache)
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
                        .unsubscribeOn(Schedulers.from(threadExecutor))
                        .subscribeOn(Schedulers.from(threadExecutor))
                        .observeOn(postExecutionThread.getScheduler())
                        .subscribe(subscriber));
    }

    @Override
    public void getProductById(Subscriber<Product> subscriber, String categoryId, String operatorId,
                               String productId) {
        compositeSubscription.add(
                digitalWidgetRepository.getObservableProducts(useCache)
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
                        .unsubscribeOn(Schedulers.from(threadExecutor))
                        .subscribeOn(Schedulers.from(threadExecutor))
                        .observeOn(postExecutionThread.getScheduler())
                        .subscribe(subscriber));
    }

    @Override
    public void getNumberList(Subscriber<DigitalNumberList> subscriber,
                              TKPDMapParam<String, String> param) {
        if (SessionHandler.isV4Login(MainApplication.getAppContext())) {
            compositeSubscription.add(
                    digitalWidgetRepository.getObservableNumberList(param)
                            .subscribeOn(Schedulers.from(threadExecutor))
                            .unsubscribeOn(Schedulers.from(threadExecutor))
                            .observeOn(postExecutionThread.getScheduler())
                            .subscribe(subscriber));
        }
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
}