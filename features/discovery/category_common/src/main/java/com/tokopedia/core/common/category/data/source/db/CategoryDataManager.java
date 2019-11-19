package com.tokopedia.core.common.category.data.source.db;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;


/**
 * @author sebastianuskh on 4/3/17.
 */
public class CategoryDataManager {
    private CategoryDao categoryDao;

    @Inject
    public CategoryDataManager(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public void clearDatabase() {
        categoryDao.clearTables();
    }

    public List<CategoryDataBase> fetchCategoryFromParent(long categoryId) {
        return categoryDao.getCategoryListByParent(categoryId);
    }

    public List<CategoryDataBase> fetchFromDatabase() {
        return categoryDao.getAllCategories();
    }

    public Observable<String> getCategoryName(long categoryId){
        return Observable.fromCallable(() -> categoryDao.getCategoryName(categoryId));
    }

    public void storeData(List<CategoryDataBase> categoryDataBases) {
        Observable.fromCallable(() -> {
            categoryDao.insertMultiple(categoryDataBases);
            return true;
        }).subscribeOn(Schedulers.io()).subscribe();

    }

    public CategoryDataBase fetchCategoryWithId(long categoryId) {
        CategoryDataBase category = categoryDao.getSingleCategory(categoryId);
        if (category != null) {
            return category;
        } else {
            throw new RuntimeException("No category found");
        }
    }
}
