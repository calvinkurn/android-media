package com.tokopedia.digital.common.data.source;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.var.TkpdCache;
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
 * Created by Rizky on 19/01/18.
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
                .first(new Func1<List<CategoryEntity>, Boolean>() {
                    @Override
                    public Boolean call(List<CategoryEntity> categoryEntities) {
                        return categoryEntities != null && !categoryEntities.isEmpty();
                    }
                })
                .map(categoryMapper);
    }

    private Observable<List<CategoryEntity>> getDataFromCloud() {
        return digitalEndpointService.getApi().getCategoryList()
                .map(getFuncTransformCategoryEntityList())
                .doOnNext(new Action1<List<CategoryEntity>>() {
                    @Override
                    public void call(List<CategoryEntity> categoryEntityList) {
                        deleteCache(categoryEntityList);
                        if (categoryEntityList != null) {
                            globalCacheManager.setKey(KEY_CATEGORY_LIST);
                            globalCacheManager.setValue(
                                    CacheUtil.convertListModelToString(categoryEntityList,
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
            globalCacheManager.delete(TkpdCache.Key.DIGITAL_CATEGORY_DETAIL + "/" + categoryEntity.getId());
        }
    }

    private Func1<Response<TkpdDigitalResponse>, List<CategoryEntity>> getFuncTransformCategoryEntityList() {
        return new Func1<Response<TkpdDigitalResponse>, List<CategoryEntity>>() {
            @Override
            public List<CategoryEntity> call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                return tkpdDigitalResponseResponse.body().convertDataList(CategoryEntity[].class);
            }
        };
    }

}
