package com.tokopedia.digital.product.domain;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.apiservice.DigitalEndpointService;
import com.tokopedia.digital.product.data.entity.response.Category;
import com.tokopedia.digital.product.data.entity.response.ResponseCategoryDetailData;
import com.tokopedia.digital.product.data.entity.response.ResponseCategoryDetailIncluded;
import com.tokopedia.digital.product.data.mapper.IProductDigitalMapper;
import com.tokopedia.digital.product.model.CategoryData;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public class DigitalCategoryRepository implements IDigitalCategoryRepository {

    private static final String KEY_CATEGORY_ID = "KEY_CATEGORY_ID";

    private final DigitalEndpointService digitalEndpointService;
    private final IProductDigitalMapper productDigitalMapper;
    private GlobalCacheManager globalCacheManager = new GlobalCacheManager();

    public DigitalCategoryRepository(DigitalEndpointService digitalEndpointService,
                                     IProductDigitalMapper productDigitalMapper) {
        this.digitalEndpointService = digitalEndpointService;
        this.productDigitalMapper = productDigitalMapper;
    }

    @Override
    public Observable<CategoryData> getCategory(
            String categoryId, TKPDMapParam<String, String> param
    ) {
        return getCategoryFromCloud(categoryId, param);
//        return Observable.concat(getCategoryFromDB(categoryId), getCategoryFromCloud(categoryId, param))
//                .first(new Func1<CategoryData, Boolean>() {
//                    @Override
//                    public Boolean call(CategoryData categoryData) {
//                        return categoryData != null;
//                    }
//                });
    }

    private Observable<CategoryData> getCategoryFromCloud(
            String categoryId, TKPDMapParam<String, String> param
    ) {
        return digitalEndpointService.getApi()
                .getCategory(categoryId, param)
                .map(getFuncTransformCategoryData());
//                .doOnNext(new Action1<CategoryData>() {
//                    @Override
//                    public void call(CategoryData categoryData) {
//                        saveCategoryToCache(categoryData);
//                    }
//                });
    }

    private void saveCategoryToCache(CategoryData categoryData) {
        globalCacheManager.setKey(KEY_CATEGORY_ID);
        globalCacheManager.setValue(
                CacheUtil.convertModelToString(categoryData, new TypeToken<Category>() {
                }.getType()));
        globalCacheManager.store();
    }

    private Observable<CategoryData> getCategoryFromDB(
            String categoryId
    ) {
        return Observable.just(categoryId)
                .map(new Func1<String, CategoryData>() {
                    @Override
                    public CategoryData call(String s) {
                        return CacheUtil.convertStringToModel(s, new TypeToken<CategoryData>() {
                        }.getType());
                    }
                });
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
