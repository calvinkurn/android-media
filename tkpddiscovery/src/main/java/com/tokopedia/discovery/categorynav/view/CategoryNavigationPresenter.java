package com.tokopedia.discovery.categorynav.view;

import android.content.Context;

import com.tokopedia.core.base.domain.DefaultSubscriber;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.discovery.categorynav.domain.interactor.GetNavigationCategoryRootUseCase;
import com.tokopedia.discovery.categorynav.domain.model.CategoryNavDomainModel;

/**
 * @author by alifa on 7/6/17.
 */

public class CategoryNavigationPresenter extends BaseDaggerPresenter<CategoryNavigationContract.View>
        implements CategoryNavigationContract.Presenter {

    private final GetNavigationCategoryRootUseCase getNavigationCategoryRootUseCase;

    public CategoryNavigationPresenter(GetNavigationCategoryRootUseCase getNavigationCategoryRootUseCase) {
        this.getNavigationCategoryRootUseCase = getNavigationCategoryRootUseCase;
    }

    @Override
    public void getRootCategory(String departementId) {
        getNavigationCategoryRootUseCase.setCategoryId(departementId);
        getNavigationCategoryRootUseCase.execute(RequestParams.EMPTY, new CategoryRootNavigationSubscriber());
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
        }

    }

}
