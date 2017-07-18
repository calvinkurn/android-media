package com.tokopedia.discovery.categorynav.view;

import com.tokopedia.core.base.domain.DefaultSubscriber;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.discovery.categorynav.domain.interactor.GetCategoryChildrenUseCase;
import com.tokopedia.discovery.categorynav.domain.interactor.GetCategoryParentUseCase;
import com.tokopedia.discovery.categorynav.domain.model.Category;
import com.tokopedia.discovery.categorynav.domain.model.CategoryNavDomainModel;
import com.tokopedia.discovery.categorynav.domain.model.ChildCategory;

import java.util.List;

/**
 * @author by alifa on 7/6/17.
 */

public class CategoryNavigationPresenter extends BaseDaggerPresenter<CategoryNavigationContract.View>
        implements CategoryNavigationContract.Presenter {

    private final GetCategoryParentUseCase getCategoryParentUseCase;
    private final GetCategoryChildrenUseCase getCategoryChildrenUseCase;

    public CategoryNavigationPresenter(GetCategoryParentUseCase getCategoryParentUseCase,
                                       GetCategoryChildrenUseCase getCategoryChildrenUseCase) {
        this.getCategoryParentUseCase = getCategoryParentUseCase;
        this.getCategoryChildrenUseCase = getCategoryChildrenUseCase;
    }

    @Override
    public void getRootCategory(String departementId) {
        getCategoryParentUseCase.setCategoryId(departementId);
        getCategoryParentUseCase.execute(RequestParams.EMPTY, new CategoryRootNavigationSubscriber());
    }

    @Override
    public void getChildren(String departementId) {
        getCategoryChildrenUseCase.setCategoryId(departementId);
        getCategoryChildrenUseCase.execute(RequestParams.EMPTY, new CategoryChildrenSubscriber());
    }

    private class CategoryRootNavigationSubscriber extends DefaultSubscriber<CategoryNavDomainModel> {
        @Override
        public void onCompleted() {
           getView().hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            getView().emptyState();
            e.printStackTrace();
        }

        @Override
        public void onNext(CategoryNavDomainModel domainModel) {
            getView().renderRootCategory(domainModel);
            getView().hideLoading();
        }

    }

    private class CategoryChildrenSubscriber extends DefaultSubscriber<List<Category>> {
        @Override
        public void onCompleted() {
            getView().hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            getView().emptyState();
            e.printStackTrace();
        }

        @Override
        public void onNext(List<Category> children) {
            getView().renderCategoryChildren(children);
            getView().hideLoading();
        }

    }

}
