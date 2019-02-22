package com.tokopedia.core.common.category.presenter;

import com.tokopedia.core.common.category.domain.interactor.FetchCategoryFromSelectedUseCase;
import com.tokopedia.core.common.category.domain.interactor.FetchCategoryWithParentChildUseCase;
import com.tokopedia.core.common.category.domain.model.CategoryDomainModel;
import com.tokopedia.core.common.category.domain.model.CategoryLevelDomainModel;
import com.tokopedia.core.common.category.view.mapper.CategoryViewMapper;
import com.tokopedia.core.common.category.view.model.CategoryLevelViewModel;
import com.tokopedia.core.common.category.view.model.CategoryViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/3/17.
 */

public class CategoryPickerPresenterImpl extends CategoryPickerPresenter {
    private final FetchCategoryWithParentChildUseCase fetchCategoryChildUseCase;
    private final FetchCategoryFromSelectedUseCase fetchCategoryFromSelectedUseCase;

    public CategoryPickerPresenterImpl(
            FetchCategoryWithParentChildUseCase fetchCategoryChildUseCase,
            FetchCategoryFromSelectedUseCase fetchCategoryFromSelectedUseCase) {
        this.fetchCategoryChildUseCase = fetchCategoryChildUseCase;
        this.fetchCategoryFromSelectedUseCase = fetchCategoryFromSelectedUseCase;
    }

    @Override
    public void fetchCategoryLevelOne() {
        if (!isViewAttached()) {
            return;
        }
        getView().hideRetryEmpty();
        getView().showLoadingDialog();
        RequestParams requestParam = FetchCategoryWithParentChildUseCase.generateLevelOne();
        fetchCategoryChildUseCase.execute(requestParam, new FetchCategoryParentSubscriber());
    }

    @Override
    public void fetchCategoryChild(long categoryId) {
        if (!isViewAttached()) {
            return;
        }
        RequestParams requestParam = FetchCategoryWithParentChildUseCase.generateFromParent(categoryId);
        fetchCategoryChildUseCase.execute(requestParam, new FetchCategoryChildSubscriber(categoryId));
    }

    @Override
    public void fetchCategoryFromSelected(long initSelected) {
        if (!isViewAttached()) {
            return;
        }
        getView().hideRetryEmpty();
        RequestParams requestParam = FetchCategoryFromSelectedUseCase.generateParam(initSelected);
        fetchCategoryFromSelectedUseCase.execute(requestParam, new FetchCategoryFromSelectedSubscriber());
    }

    @Override
    public void unsubscribe() {
        fetchCategoryChildUseCase.unsubscribe();
        fetchCategoryFromSelectedUseCase.unsubscribe();
    }

    private class FetchCategoryChildSubscriber extends Subscriber<List<CategoryDomainModel>> {
        private final long categoryId;

        private FetchCategoryChildSubscriber(long categoryId) {
            this.categoryId = categoryId;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (!isViewAttached()) {
                return;
            }
        }

        @Override
        public void onNext(List<CategoryDomainModel> domainModel) {
            checkViewAttached();
            getView().renderCategory(CategoryViewMapper.mapList(domainModel), categoryId);

        }
    }

    private class FetchCategoryFromSelectedSubscriber extends Subscriber<List<CategoryLevelDomainModel>> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (!isViewAttached()) {
                return;
            }
            fetchCategoryLevelOne();
        }

        @Override
        public void onNext(List<CategoryLevelDomainModel> categoryLevelDomainModels) {
            checkViewAttached();
            List<CategoryLevelViewModel> categoryLevelViewModels = CategoryViewMapper.mapLevel(categoryLevelDomainModels);
            getView().renderCategoryFromSelected(categoryLevelViewModels);
        }
    }

    private class FetchCategoryParentSubscriber extends Subscriber<List<CategoryDomainModel>> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (!isViewAttached()) {
                return;
            }
            getView().dismissLoadingDialog();
            getView().showRetryEmpty();
        }

        @Override
        public void onNext(List<CategoryDomainModel> domainModels) {
            getView().dismissLoadingDialog();
            List<CategoryViewModel> map = CategoryViewMapper.mapList(domainModels);
            getView().renderCategory(map, FetchCategoryWithParentChildUseCase.UNSELECTED);
        }
    }
}
