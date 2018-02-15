package com.tokopedia.digital.common.data.source;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService;
import com.tokopedia.digital.common.data.entity.response.ResponseCategoryDetailData;
import com.tokopedia.digital.common.data.entity.response.ResponseCategoryDetailIncluded;
import com.tokopedia.digital.common.data.mapper.ProductDigitalMapper;
import com.tokopedia.digital.product.view.model.CategoryData;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author rizkyfadillah on 19/01/18.
 */

public class CategoryDetailDataSource {

    private DigitalEndpointService digitalEndpointService;
    private GlobalCacheManager globalCacheManager;
    private ProductDigitalMapper productDigitalMapper;

    public CategoryDetailDataSource(DigitalEndpointService digitalEndpointService,
                                    GlobalCacheManager globalCacheManager,
                                    ProductDigitalMapper productDigitalMapper) {
        this.digitalEndpointService = digitalEndpointService;
        this.globalCacheManager = globalCacheManager;
        this.productDigitalMapper = productDigitalMapper;
    }

    public Observable<CategoryData> getCategory(String categoryId, TKPDMapParam<String, String> param) {
        return Observable.concat(getDataFromLocal(categoryId), getDataFromCloud(categoryId, param))
                .first(new Func1<CategoryData, Boolean>() {
                    @Override
                    public Boolean call(CategoryData categoryData) {
                        return categoryData != null;
                    }
                });
    }

    private Observable<CategoryData> getDataFromLocal(String categoryId) {
        CategoryData categoryData;
        try {
            categoryData = CacheUtil.convertStringToModel(
                    globalCacheManager.getValueString(TkpdCache.Key.DIGITAL_CATEGORY_DETAIL + "/" + categoryId),
                    new TypeToken<CategoryData>() {
                    }.getType());
        } catch (RuntimeException e) {
            categoryData = null;
        }

        return Observable.just(categoryData);
    }

    private Observable<CategoryData> getDataFromCloud(String categoryId, TKPDMapParam<String, String> param) {
        return digitalEndpointService.getApi().getCategory(categoryId, param)
                .map(getFuncTransformCategoryData())
                .doOnNext(saveToCache(categoryId));
    }

    private Action1<CategoryData> saveToCache(final String categoryId) {
        return new Action1<CategoryData>() {
            @Override
            public void call(CategoryData categoryData) {
                globalCacheManager.setKey(TkpdCache.Key.DIGITAL_CATEGORY_DETAIL + "/" + categoryId);
                globalCacheManager.setValue(CacheUtil.convertModelToString(categoryData,
                        new TypeToken<CategoryData>() {
                        }.getType()));
                globalCacheManager.setCacheDuration(600); // 10 minutes
                globalCacheManager.store();
            }
        };
    }

    @NonNull
    private Func1<Response<TkpdDigitalResponse>, CategoryData> getFuncTransformCategoryData() {
        return new Func1<Response<TkpdDigitalResponse>, CategoryData>() {
            @Override
            public CategoryData call(
                    Response<TkpdDigitalResponse> response
            ) {
                return productDigitalMapper.transformCategoryData(
                        response.body().convertDataObj(ResponseCategoryDetailData.class),
                        response.body().convertIncludedList(ResponseCategoryDetailIncluded[].class)
                );
            }
        };
    }

}
