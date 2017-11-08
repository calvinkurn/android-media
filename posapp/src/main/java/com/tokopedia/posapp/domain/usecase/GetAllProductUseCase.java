package com.tokopedia.posapp.domain.usecase;

import com.beloo.widget.chipslayoutmanager.util.log.Log;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.data.repository.ProductRepository;
import com.tokopedia.posapp.domain.model.product.ProductListDomain;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.observables.AsyncOnSubscribe;

/**
 * Created by okasurya on 11/7/17.
 */

public class GetAllProductUseCase extends UseCase<ProductListDomain> {
    private static final String ROW = "row";
    private static final String START = "start";

    private ProductRepository productRepository;
    private RequestParams requestParams;
    private boolean getNextPage = true;
    private int page = 1;

    @Inject
    public GetAllProductUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                ProductRepository productRepository) {
        super(threadExecutor, postExecutionThread);
        this.productRepository = productRepository;
    }

    @Override
    public Observable<ProductListDomain> createObservable(RequestParams params) {
        this.requestParams = params;
        page = 1;

        Observable.OnSubscribe<ProductListDomain> os = AsyncOnSubscribe.createStateless(
            new Action2<Long, Observer<Observable<? extends ProductListDomain>>>() {
                @Override
                public void call(Long aLong, final Observer<Observable<? extends ProductListDomain>> observer) {
                    Observable.defer(new Func0<Observable<ProductListDomain>>() {
                        @Override
                        public Observable<ProductListDomain> call() {
                            if (getNextPage) {
                                return productRepository.getProductList(requestParams);
                            }
                            return Observable.error(new Exception("Repeating Process Finished"));
                        }
                    })
                    .repeat()
                    .subscribe(new Subscriber<ProductListDomain>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            Log.d("o2o", "repeat error msg: " + e.getMessage());
                            observer.onCompleted();
                        }

                        @Override
                        public void onNext(ProductListDomain productListDomain) {
                            if (productListDomain.getNextUri() != null && !productListDomain.getNextUri().isEmpty()) {
                                page++;
                                requestParams.putInt(START, 1 + (requestParams.getInt(ROW, 10) * (page - 1)));
                                getNextPage = true;
                            } else {
                                getNextPage = false;
                            }
                            observer.onNext(Observable.just(productListDomain));
                            onCompleted();
                        }
                    });
                }
            }
        );

        return Observable.create(os);
    }
}
