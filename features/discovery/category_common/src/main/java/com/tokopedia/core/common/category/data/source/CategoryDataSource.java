package com.tokopedia.core.common.category.data.source;

import android.support.annotation.NonNull;

import com.tokopedia.core.common.category.data.mapper.CategoryServiceToDbMapper;
import com.tokopedia.core.common.category.data.source.cloud.CategoryCloud;
import com.tokopedia.core.common.category.data.source.db.CategoryDataManager;
import com.tokopedia.core.common.category.data.source.db.CategoryDataBase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CategoryDataSource {
    private final CategoryDataManager categoryDataManager;
    private final CategoryCloud categoryCloud;

    @Inject
    public CategoryDataSource(CategoryDataManager categoryDataManager, CategoryCloud categoryCloud) {
        this.categoryDataManager = categoryDataManager;
        this.categoryCloud = categoryCloud;
    }

    public Observable<Boolean> checkCategoryAvailable() {
        return Observable
                .just(true)
                .map(new FetchFromDatabase())
                .map(new CheckDatabaseNotNull())
                .onErrorResumeNext(fetchDataFromNetwork());
    }

    @NonNull
    private Observable<Boolean> fetchDataFromNetwork() {
        return categoryCloud.fetchDataFromNetwork()
            .map(new CategoryServiceToDbMapper())
            .map(new StoreDataToDatabase());
    }

    public Observable<String> getCategoryName(long categoryId) {
        return categoryDataManager.getCategoryName(categoryId);
    }

    private class CheckDatabaseNotNull implements Func1<List<CategoryDataBase>, Boolean> {
        @Override
        public Boolean call(List<CategoryDataBase> categoryDataBases) {
            if(categoryDataBases == null || categoryDataBases.isEmpty()){
                throw new RuntimeException("");
            }
            return true;
        }
    }

    private class StoreDataToDatabase implements Func1<List<CategoryDataBase>, Boolean> {

        @Override
        public Boolean call(List<CategoryDataBase> categoryDataBases) {
            categoryDataManager.storeData(categoryDataBases);
            return true;
        }
    }

    private class FetchFromDatabase implements Func1<Boolean, List<CategoryDataBase>> {
        @Override
        public List<CategoryDataBase> call(Boolean aBoolean) {
            return categoryDataManager.fetchFromDatabase();
        }
    }
}
