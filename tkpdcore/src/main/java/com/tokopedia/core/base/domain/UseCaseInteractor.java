package com.tokopedia.core.base.domain;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;

import rx.Observable;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public abstract class UseCaseInteractor<O, P> extends UseCase<O> {


    public UseCaseInteractor(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    public UseCaseInteractor() {
        super();
    }

    @Override
    @Deprecated
    public Observable<O> createObservable(RequestParams requestParams) {
        return null;
    }

    public abstract Observable<O>  createObservable(P requestParams) ;

    @Override
    public void unsubscribe() {
        super.unsubscribe();
    }

    @Override
    public Observable<O> getExecuteObservable(RequestParams requestParams) {
        return super.getExecuteObservable(requestParams);
    }

    @Override
    public Observable<O> getExecuteObservableAsync(RequestParams requestParams) {
        return super.getExecuteObservableAsync(requestParams);
    }
}
