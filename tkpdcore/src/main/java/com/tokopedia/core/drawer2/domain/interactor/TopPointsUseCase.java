package com.tokopedia.core.drawer2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashModel;
import com.tokopedia.core.drawer2.data.pojo.toppoints.TopPointsModel;
import com.tokopedia.core.drawer2.domain.TopPointsRepository;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nisie on 5/5/17.
 */

public class TopPointsUseCase extends UseCase<TopPointsModel> {

    private final TopPointsRepository repository;

    public TopPointsUseCase(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            TopPointsRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    public Observable<TopPointsModel> createObservable(final RequestParams requestParams) {
        return repository.getTopPointsFromLocal()
                .onErrorResumeNext(new Func1<Throwable, Observable<TopPointsModel>>() {
                    @Override
                    public Observable<TopPointsModel> call(Throwable throwable) {
                        return repository.getTopPointsFromNetwork(requestParams.getParameters());
                    }
                });
    }
}
