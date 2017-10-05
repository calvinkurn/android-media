package com.tokopedia.core.drawer2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashModel;
import com.tokopedia.core.drawer2.domain.TokoCashRepository;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nisie on 5/5/17.
 */

public class TokoCashUseCase extends UseCase<TokoCashModel> {

    private final TokoCashRepository tokoCashRepository;

    public TokoCashUseCase(ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread,
                           TokoCashRepository tokoCashRepository) {
        super(threadExecutor, postExecutionThread);
        this.tokoCashRepository = tokoCashRepository;
    }

    @Override
    public Observable<TokoCashModel> createObservable(final RequestParams requestParams) {
        return tokoCashRepository.getTokoCashFromLocal()
                .onErrorResumeNext(new Func1<Throwable, Observable<TokoCashModel>>() {
                    @Override
                    public Observable<TokoCashModel> call(Throwable throwable) {
                        return tokoCashRepository.getTokoCashFromNetwork(requestParams.getParameters());
                    }
                });
    }
}
