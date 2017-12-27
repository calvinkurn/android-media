package com.tokopedia.core.common.category.data.source;

import com.tokopedia.core.common.category.data.mapper.CategoryDataToDomainMapper;
import com.tokopedia.core.common.category.data.source.db.CategoryDataManager;
import com.tokopedia.core.common.category.data.source.db.CategoryDataBase;
import com.tokopedia.core.common.category.domain.model.CategoryDomainModel;
import com.tokopedia.core.common.category.domain.model.CategoryLevelDomainModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/7/17.
 */

public class FetchCategoryDataSource {


    private final CategoryDataManager categoryDataManager;

    @Inject
    public FetchCategoryDataSource(CategoryDataManager categoryDataManager) {
        this.categoryDataManager = categoryDataManager;
    }

    public Observable<List<CategoryDomainModel>> fetchCategoryLevelOne(long categoryId) {
        return Observable.just(categoryId).map(new FetchFromParent()).map(new CategoryDataToDomainMapper());
    }

    public Observable<List<CategoryLevelDomainModel>> fetchCategoryFromSelected(long categoryId) {
        return Observable.just(categoryId).map(new FetchCategoryFromSelected());
    }

    public Observable<List<String>> fetchCategoryDisplay(long categoryId) {
        return Observable
                .just(categoryId)
                .map(new FetchCategoryFromSelected())
                .map(new GetStringListCategory());
    }

    private class FetchCategoryFromSelected implements Func1<Long, List<CategoryLevelDomainModel>> {
        @Override
        public List<CategoryLevelDomainModel> call(Long categoryId) {
            List<CategoryLevelDomainModel> categoryLevelDomainModels = new ArrayList<>();
            long currentLevelSelected = categoryId;
            do {
                CategoryLevelDomainModel categoryLevelDomain = new CategoryLevelDomainModel();
                categoryLevelDomain.setSelectedCategoryId(currentLevelSelected);

                long parentId = getParentId(currentLevelSelected);
                categoryLevelDomain.setParentCategoryId(parentId);
                List<CategoryDataBase> categoryFromParent = getCategoryFromParent(parentId);
                categoryLevelDomain.setCategoryModels(
                        CategoryDataToDomainMapper.mapDomainModels(categoryFromParent)
                );
                categoryLevelDomainModels.add(0, categoryLevelDomain);
                currentLevelSelected = parentId;
            }
            while (categoryLevelDomainModels.get(0).getParentCategoryId() != CategoryDataBase.LEVEL_ONE_PARENT);
            return categoryLevelDomainModels;
        }
    }

    private class FetchFromParent implements Func1<Long, List<CategoryDataBase>> {

        @Override
        public List<CategoryDataBase> call(Long parent) {
            return getCategoryFromParent(parent);
        }
    }

    private List<CategoryDataBase> getCategoryFromParent(long categoryId) {
        return categoryDataManager.fetchCategoryFromParent(categoryId);
    }

    private long getParentId(long categoryId) {
        CategoryDataBase selectedCategory = categoryDataManager.fetchCategoryWithId(categoryId);
        return selectedCategory.getParentId();
    }

    private class GetStringListCategory implements Func1<List<CategoryLevelDomainModel>, List<String>> {
        @Override
        public List<String> call(List<CategoryLevelDomainModel> categoryLevelDomainModels) {
            List<String> listString = new ArrayList<>();
            for (CategoryLevelDomainModel domainModel : categoryLevelDomainModels) {
                CategoryDataBase categoryDataBase = categoryDataManager
                        .fetchCategoryWithId(domainModel.getSelectedCategoryId());
                listString.add(categoryDataBase.getName());
            }
            return listString;
        }
    }
}