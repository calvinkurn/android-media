package com.tokopedia.core.common.category.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.common.category.domain.CategoryRepository;

import rx.Observable;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class ClearCategoryCacheUseCase extends UseCase<Boolean>{
    private final CategoryRepository categoryRepository;

    public ClearCategoryCacheUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, CategoryRepository categoryRepository) {
        super(threadExecutor, postExecutionThread);
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return categoryRepository.clearCache();
    }
}
