package com.tokopedia.digital.common.data.source;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;

import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse;
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
    private static final long DEFAULT_EXPIRED_TIME = 0;

    private DigitalEndpointService digitalEndpointService;
    private CacheManager cacheManager;
    private CategoryMapper categoryMapper;

    public CategoryListDataSource(DigitalEndpointService digitalEndpointService,
                                  CacheManager cacheManager,
                                  CategoryMapper categoryMapper) {
        this.cacheManager = cacheManager;
        this.digitalEndpointService = digitalEndpointService;
        this.categoryMapper = categoryMapper;
    }

    public Observable<List<Category>> getCategoryList() {
        return Observable.concat(getDataFromDb(), getDataFromCloud())
                .first(categoryEntities -> categoryEntities != null)
                .map(categoryMapper);
    }

    private Observable<List<CategoryEntity>> getDataFromCloud() {
        return digitalEndpointService.getApi().getCategoryList()
                .map(getFuncTransformCategoryEntityList())
                .doOnNext(categoryEntities -> {
                    deleteCache(categoryEntities);
                    if (categoryEntities != null) {
                        cacheManager.save(
                                KEY_CATEGORY_LIST,
                                CacheUtil.convertListModelToString(categoryEntities,
                                        new TypeToken<List<CategoryEntity>>() {
                                        }.getType()),
                                DEFAULT_EXPIRED_TIME
                        );
                    }
                });
    }

    private Observable<List<CategoryEntity>> getDataFromDb() {
        List<CategoryEntity> categoryEntities;

        try {
            categoryEntities = CacheUtil.convertStringToListModel(
                    cacheManager.get(KEY_CATEGORY_LIST),
                    new TypeToken<List<CategoryEntity>>() {
                    }.getType());
        } catch (RuntimeException e) {
            categoryEntities = null;
        }

        return Observable.just(categoryEntities);
    }

    private void deleteCache(List<CategoryEntity> categoryEntityList) {
        for (CategoryEntity categoryEntity : categoryEntityList) {
            cacheManager.delete(DigitalCache.NEW_DIGITAL_CATEGORY_DETAIL + "/" + categoryEntity.getId());
        }
    }

    private Func1<Response<TkpdDigitalResponse>, List<CategoryEntity>> getFuncTransformCategoryEntityList() {
        return response -> response.body().convertDataList(CategoryEntity[].class);
    }

}