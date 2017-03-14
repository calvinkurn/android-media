package com.tokopedia.core.category.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.category.domain.CategoryRepository;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public class CheckVersionCategoryUseCase extends UseCase<Boolean> {
    private CategoryRepository categoryRepository;

    public CheckVersionCategoryUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return categoryRepository.checkVersion();
    }
}
