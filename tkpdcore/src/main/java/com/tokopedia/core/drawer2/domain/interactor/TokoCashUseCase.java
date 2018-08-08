package com.tokopedia.core.drawer2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;

import rx.Observable;

/**
 * Created by nisie on 5/5/17.
 */

public class TokoCashUseCase extends UseCase<TokoCashData> {

    private Observable<TokoCashData> tokoCashData;

    public TokoCashUseCase(ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread,
                           Observable<TokoCashData> tokoCashData) {
        super(threadExecutor, postExecutionThread);
        this.tokoCashData = tokoCashData;
    }

    @Override
    public Observable<TokoCashData> createObservable(RequestParams requestParams) {
        return tokoCashData;
    }
}
