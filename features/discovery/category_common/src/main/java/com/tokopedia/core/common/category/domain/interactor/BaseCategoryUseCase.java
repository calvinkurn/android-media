package com.tokopedia.core.common.category.domain.interactor;

import com.tokopedia.core.common.category.domain.CategoryRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 3/8/17.
 */

public abstract class BaseCategoryUseCase<T> extends UseCase<T> {
    protected final CategoryRepository categoryRepository;

    public BaseCategoryUseCase(CategoryRepository categoryRepository) {
        super();
        this.categoryRepository = categoryRepository;
    }

    @Override
    public final Observable<T> createObservable(RequestParams requestParams) {
        return Observable.just(true)
                .flatMap(new CheckVersion())
                .flatMap(new CheckCategoryAvailable())
                .flatMap(new FetchCategoryObservable(requestParams));
    }

    private class CheckVersion implements Func1<Boolean, Observable<Boolean>> {
        @Override
        public Observable<Boolean> call(Boolean aBoolean) {
            return categoryRepository.checkVersion();
        }
    }

    private class CheckCategoryAvailable implements Func1<Boolean, Observable<Boolean>> {
        @Override
        public Observable<Boolean> call(Boolean aBoolean) {
            return categoryRepository.checkCategoryAvailable();
        }
    }

    private class FetchCategoryObservable implements Func1<Boolean, Observable<? extends T>> {
        private final RequestParams requestParams;

        public FetchCategoryObservable(RequestParams requestParams) {
            this.requestParams = requestParams;
        }

        @Override
        public Observable<T> call(Boolean aBoolean) {
            return createObservableCategory(requestParams);
        }
    }

    protected abstract Observable<T> createObservableCategory(RequestParams requestParams);
}
