package com.tokopedia.discovery.categorynav.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.discovery.categorynav.domain.CategoryNavigationRepository;
import com.tokopedia.discovery.categorynav.domain.model.Category;
import com.tokopedia.discovery.categorynav.domain.model.ChildCategory;

import java.util.List;

import rx.Observable;

/**
 * @author by alifa on 7/17/17.
 */

public class GetCategoryChildrenUseCase extends UseCase<List<Category>> {

    private final CategoryNavigationRepository repository;
    private String categoryId = "";

    public GetCategoryChildrenUseCase(ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread,
                                      CategoryNavigationRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public Observable<List<Category>> createObservable(RequestParams requestParams) {
        return repository.getCategoryChildren(categoryId);
    }
}
