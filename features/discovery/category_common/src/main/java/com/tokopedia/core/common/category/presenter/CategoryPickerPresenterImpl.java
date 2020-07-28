package com.tokopedia.core.common.category.presenter;

import com.tokopedia.core.common.category.data.mapper.CategoryPickerMapper;
import com.tokopedia.core.common.category.domain.interactor.GetCategoryLiteTreeUseCase;
import com.tokopedia.core.common.category.domain.model.CategoriesResponse;
import com.tokopedia.core.common.category.domain.model.CategoryModel;
import com.tokopedia.core.common.category.domain.model.CategorySelectedModel;
import com.tokopedia.core.common.category.view.mapper.CategoryViewMapper;
import com.tokopedia.core.common.category.view.model.CategoryLevelViewModel;
import com.tokopedia.core.common.category.view.model.CategoryViewModel;

import java.util.List;

import rx.Subscriber;

/**
 * @author saidfaisal on 28/7/20.
 */

public class CategoryPickerPresenterImpl extends CategoryPickerPresenter {
    private final GetCategoryLiteTreeUseCase getCategoryLiteTreeUseCase;

    public static final int UNSELECTED = -2;
    private GetCategoryLiteTreeSubscriber subscriber;

    public CategoryPickerPresenterImpl(
            GetCategoryLiteTreeUseCase getCategoryLiteTreeUseCase) {
        this.getCategoryLiteTreeUseCase = getCategoryLiteTreeUseCase;
    }

    @Override
    public void getCategoryLiteTree() {
        if (!isViewAttached()) {
            return;
        }
        getView().hideRetryEmpty();
        getView().showLoadingDialog();

        subscriber = new GetCategoryLiteTreeSubscriber();
        getCategoryLiteTreeUseCase.execute(GetCategoryLiteTreeUseCase.Companion.createRequestParams(), subscriber);
    }

    @Override
    public void unsubscribe() {
        getCategoryLiteTreeUseCase.unsubscribe();
    }

    @Override
    public void getCategoryChild(long categoryId) {
        checkViewAttached();
        List<CategoryModel> categoriesModel = CategoryPickerMapper.INSTANCE.findCategoryChildInCategoriesWithCategoryId(categoryId, getCategorySelectedModels());
        getView().renderCategory(CategoryViewMapper.mapCategoryModelsToCategoryViewModels(categoriesModel), categoryId);
    }

    @Override
    public void getCategoryFromSelected(long initSelected) {
        checkViewAttached();
        List<CategorySelectedModel> categorySelectedModels = CategoryPickerMapper.INSTANCE.findCategoryWithCategoryId(initSelected, getCategorySelectedModels());
        List<CategoryLevelViewModel> categoryLevelViewModels = CategoryViewMapper.mapLevel(categorySelectedModels);
        getView().renderCategoryFromSelected(categoryLevelViewModels);
    }

    private CategoriesResponse getCategoriesResponse() {
        return subscriber.categoriesResponse;
    }

    private List<CategorySelectedModel> getCategorySelectedModels() {
        return CategoryPickerMapper.INSTANCE.mapCategoryResponseToCategorySelectedModels(getCategoriesResponse());
    }

    private class GetCategoryLiteTreeSubscriber extends Subscriber<CategoriesResponse> {

        public CategoriesResponse categoriesResponse;

        @Override
        public void onCompleted() { }

        @Override
        public void onError(Throwable e) {
            if (!isViewAttached()) {
                return;
            }
            getView().dismissLoadingDialog();
            getView().showRetryEmpty();
        }

        @Override
        public void onNext(CategoriesResponse categoriesResponse) {
            getView().dismissLoadingDialog();

            this.categoriesResponse = categoriesResponse;
            List<CategoryModel> categoriesModel = CategoryPickerMapper.INSTANCE.mapCategoryResponseToCategoryModels(categoriesResponse);
            List<CategoryViewModel> categoriesViewModel = CategoryViewMapper.mapCategoryModelsToCategoryViewModels(categoriesModel);
            getView().renderCategory(categoriesViewModel, UNSELECTED);
        }
    }
}

