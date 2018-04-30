package com.tokopedia.digital.common.data.source;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.digital.common.constant.DigitalCache;
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService;
import com.tokopedia.digital.widget.data.entity.category.CategoryEntity;
import com.tokopedia.digital.widget.view.model.category.Category;
import com.tokopedia.digital.widget.view.model.mapper.CategoryMapper;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author rizkyfadillah on 19/01/18.
 */

public class CategoryListDataSource {

    private final static String KEY_CATEGORY_LIST = "RECHARGE_CATEGORY_LIST";

    private DigitalEndpointService digitalEndpointService;
    private GlobalCacheManager globalCacheManager;
    private CategoryMapper categoryMapper;

    public CategoryListDataSource(DigitalEndpointService digitalEndpointService,
                                  GlobalCacheManager globalCacheManager,
                                  CategoryMapper categoryMapper) {
        this.globalCacheManager = globalCacheManager;
        this.digitalEndpointService = digitalEndpointService;
        this.categoryMapper = categoryMapper;
    }

    public Observable<List<Category>> getCategoryList() {
        return Observable.concat(getDataFromDb(), getDataFromCloud())
                .first(categoryEntities -> categoryEntities != null && !categoryEntities.isEmpty())
                .map(categoryMapper);
    }

    private Observable<List<CategoryEntity>> getDataFromCloud() {
        return digitalEndpointService.getApi().getCategoryList()
                .map(getFuncTransformCategoryEntityList())
                .doOnNext(new Action1<List<CategoryEntity>>() {
                    @Override
                    public void call(List<CategoryEntity> categoryEntities) {
                        deleteCache(categoryEntities);
                        if (categoryEntities != null) {
                            globalCacheManager.setKey(KEY_CATEGORY_LIST);
                            globalCacheManager.setValue(
                                    CacheUtil.convertListModelToString(categoryEntities,
                                            new TypeToken<List<CategoryEntity>>() {
                                            }.getType()));
                            globalCacheManager.store();
                        }
                    }
                });
    }

    private Observable<List<CategoryEntity>> getDataFromDb() {
        List<CategoryEntity> categoryEntities;

        try {
            categoryEntities = CacheUtil.convertStringToListModel(
                    globalCacheManager.getValueString(KEY_CATEGORY_LIST),
                    new TypeToken<List<CategoryEntity>>() {
                    }.getType());
        } catch (RuntimeException e) {
            categoryEntities = null;
        }

        return Observable.just(categoryEntities);
    }

    private void deleteCache(List<CategoryEntity> categoryEntityList) {
        for (CategoryEntity categoryEntity : categoryEntityList) {
            globalCacheManager.delete(DigitalCache.NEW_DIGITAL_CATEGORY_DETAIL + "/" + categoryEntity.getId());
        }
    }

    private Func1<Response<TkpdDigitalResponse>, List<CategoryEntity>> getFuncTransformCategoryEntityList() {
        return new Func1<Response<TkpdDigitalResponse>, List<CategoryEntity>>() {
            @Override
            public List<CategoryEntity> call(Response<TkpdDigitalResponse> response) {
                return response.body().convertDataList(CategoryEntity[].class);
            }
        };
    }

}
