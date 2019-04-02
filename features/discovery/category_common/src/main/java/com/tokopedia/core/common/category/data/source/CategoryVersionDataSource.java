package com.tokopedia.core.common.category.data.source;

import com.tokopedia.core.common.category.data.exception.NeedCheckVersionCategoryException;
import com.tokopedia.core.common.category.data.source.cache.CategoryVersionCache;
import com.tokopedia.core.common.category.data.source.cloud.CategoryVersionCloud;
import com.tokopedia.core.common.category.data.source.cloud.model.CategoryVersionServiceModel;
import com.tokopedia.core.common.category.data.source.db.CategoryDataManager;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 3/8/17.
 */
public class CategoryVersionDataSource {
    private final CategoryVersionCloud categoryVersionCloud;
    private final CategoryVersionCache categoryVersionCache;
    private final CategoryDataManager categoryDataManager;

    @Inject
    public CategoryVersionDataSource(CategoryVersionCloud categoryVersionCloud, CategoryVersionCache categoryVersionCache, CategoryDataManager categoryDataManager) {
        this.categoryVersionCloud = categoryVersionCloud;
        this.categoryVersionCache = categoryVersionCache;
        this.categoryDataManager = categoryDataManager;
    }

    public Observable<Boolean> checkVersion() {
        return Observable.just(true)
                .map(new IsVersionNeedCheck())
                .onErrorResumeNext(checkCategoryVersion());
    }

    private Observable<Boolean> checkCategoryVersion() {
        return categoryVersionCloud.checkVersion()
                .map(new UpdateNextVersionCheck())
                .map(new CompareVersionWithCache());
    }

    private class IsVersionNeedCheck implements Func1<Boolean, Boolean> {

        @Override
        public Boolean call(Boolean aBoolean) {
            if (categoryVersionCache.isNeedCategoryVersionCheck()) {
                throw new NeedCheckVersionCategoryException();
            } else {
                return true;
            }
        }
    }
    private class UpdateNextVersionCheck implements Func1<CategoryVersionServiceModel, CategoryVersionServiceModel> {

        @Override
        public CategoryVersionServiceModel call(CategoryVersionServiceModel serviceModel) {
            categoryVersionCache.storeNeedCategoryVersionCheck(serviceModel.getInterval());
            return serviceModel;
        }
    }
    private class CompareVersionWithCache implements Func1<CategoryVersionServiceModel, Boolean> {

        @Override
        public Boolean call(CategoryVersionServiceModel categoryVersionServiceModel) {
            Long apiVersion = categoryVersionServiceModel.getVersion();
            Long cacheVersion = categoryVersionCache.getCategoryVersion();

            Boolean isNeedUpdate = apiVersion > cacheVersion;

            if (isNeedUpdate) {
                categoryDataManager.clearDatabase();
                categoryVersionCache.clearCategoryTimer();
                categoryVersionCache.storeVersion(apiVersion);
            }

            return isNeedUpdate;
        }
    }

    public Observable<Boolean> clearCache() {
        categoryDataManager.clearDatabase();
        categoryVersionCache.clearCategoryTimer();
        return Observable.just(true);
    }


}
