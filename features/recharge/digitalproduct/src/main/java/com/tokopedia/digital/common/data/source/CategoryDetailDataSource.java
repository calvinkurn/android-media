package com.tokopedia.digital.common.data.source;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.common_digital.common.util.CommonDigitalGqlMutation;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.digital.common.constant.DigitalCache;
import com.tokopedia.digital.common.data.apiservice.DigitalGqlApi;
import com.tokopedia.digital.common.data.entity.response.RechargeResponseEntity;
import com.tokopedia.digital.common.data.mapper.ProductDigitalMapper;
import com.tokopedia.digital.product.view.model.ProductDigitalData;
import com.tokopedia.network.data.model.response.GraphqlResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author rizkyfadillah on 19/01/18.
 *         Modified by Vishal Gupta on 8th May, 2018
 */

public class CategoryDetailDataSource {

    private DigitalGqlApi digitalGqlApi;
    private ProductDigitalMapper productDigitalMapper;
    private Context context;

    public CategoryDetailDataSource(DigitalGqlApi digitalGqlApi,
                                    ProductDigitalMapper productDigitalMapper,
                                    Context context) {
        this.digitalGqlApi = digitalGqlApi;
        this.productDigitalMapper = productDigitalMapper;
        this.context = context;
    }

    /**
     * Fetches Category Detail Data, first this function try to fetch data from local if exists
     *
     * @param categoryId
     * @return
     */
    public Observable<ProductDigitalData> getCategory(String categoryId) {
        return Observable.concat(getCategoryDataFromLocal(categoryId), getCategoryDataFromCloud(categoryId))
                .first(categoryData -> categoryData != null);
    }

    /**
     * Fetches CategoryDetail and Favorit Number Combined, first this function try to fetch data from local if exists
     *
     * @param categoryId
     * @return
     */
    public Observable<ProductDigitalData> getCategoryDetailWithFavorit(String categoryId) {
        return Observable.concat(getCategoryAndFavDataFromLocal(categoryId), getCategoryAndFavoritFromCloud(categoryId, "", "", ""))
                .first(productDigitalData -> productDigitalData != null);
    }

    /**
     * Fetched category detail data from local cache and returns if expired or not present,
     * Cache Expiry Time is 15 minutes
     *
     * @param categoryId
     * @return
     */
    private Observable<ProductDigitalData> getCategoryDataFromLocal(String categoryId) {
        RechargeResponseEntity digitalCategoryDetailEntity;
        try {
            digitalCategoryDetailEntity = CacheUtil.convertStringToModel(
                    PersistentCacheManager.instance.getString(DigitalCache.NEW_DIGITAL_CATEGORY_DETAIL + "/" + categoryId),
                    new TypeToken<RechargeResponseEntity>() {
                    }.getType());
        } catch (RuntimeException e) {
            digitalCategoryDetailEntity = null;
        }

        ProductDigitalData categoryData = null;
        if (digitalCategoryDetailEntity != null) {
            categoryData = productDigitalMapper.transformCategoryData(digitalCategoryDetailEntity);
        }

        return Observable.just(categoryData);
    }

    /**
     * Get Category detail from network
     *
     * @param categoryId
     * @return
     */
    private Observable<ProductDigitalData> getCategoryDataFromCloud(String categoryId) {
        return digitalGqlApi.getCategory(getCategoryRequestPayload(categoryId))
                .map(response -> {
                    if(response != null && response.body() != null && response.body().size() > 0) {
                        return response.body().get(0).getData();
                    }
                    return null;
                })
                .doOnNext(saveCategoryDetailToCache(categoryId))
                .map(getFuncTransformCategoryData());
    }

    /**
     * Get category and favourite numbers data from local, cache expiry time is 15 minutes
     *
     * @param categoryId
     * @return
     */
    private Observable<ProductDigitalData> getCategoryAndFavDataFromLocal(String categoryId) {
        RechargeResponseEntity digitalCategoryDetailEntity;
        try {
            // currently, DigitalCache.NEW_DIGITAL_CATEGORY_AND_FAV is saved via CacheManager library
            digitalCategoryDetailEntity = CacheUtil.convertStringToModel(
                    PersistentCacheManager.instance.getString(DigitalCache.NEW_DIGITAL_CATEGORY_AND_FAV + "/" + categoryId),
                    new TypeToken<RechargeResponseEntity>() {
                    }.getType());
        } catch (RuntimeException e) {
            digitalCategoryDetailEntity = null;
        }

        ProductDigitalData categoryData = null;
        if (digitalCategoryDetailEntity != null) {
            categoryData = productDigitalMapper.transformCategoryData(digitalCategoryDetailEntity);
        }

        return Observable.just(categoryData);
    }

    public Observable<ProductDigitalData> getCategoryAndFavoritFromCloud(String categoryId, String operatorId, String clientNumber, String productId) {
        return digitalGqlApi.getCategoryAndFavoriteList(getCategoryAndFavRequestPayload(categoryId, operatorId, clientNumber, productId))
                .map(response -> {
                    RechargeResponseEntity newMappedObject = new RechargeResponseEntity();

                    if(response != null && response.body() != null && response.body().size() > 1) {
                        List<GraphqlResponse<RechargeResponseEntity>> responseEntity = response.body();
                        newMappedObject.setRechargeCategoryDetail(responseEntity.get(0).getData().getRechargeCategoryDetail());
                        newMappedObject.setRechargeFavoritNumberResponseEntity(responseEntity.get(1).getData().getRechargeFavoritNumberResponseEntity());
                    }

                    return newMappedObject;
                })
                .doOnNext(saveCategoryDetailAndFavToCache(categoryId))
                .map(getFuncTransformCategoryData());
    }

    private Action1<RechargeResponseEntity> saveCategoryDetailToCache(final String categoryId) {
        return digitalCategoryDetailEntity -> {
            PersistentCacheManager.instance.put(
                    DigitalCache.NEW_DIGITAL_CATEGORY_DETAIL + "/" + categoryId,
                    CacheUtil.convertModelToString(digitalCategoryDetailEntity,
                            new TypeToken<RechargeResponseEntity>() {
                            }.getType()),
                    900
            );
        };
    }

    private Action1<RechargeResponseEntity> saveCategoryDetailAndFavToCache(final String categoryId) {
        return digitalCategoryDetailEntity -> {
            PersistentCacheManager.instance.put(
                    DigitalCache.NEW_DIGITAL_CATEGORY_AND_FAV + "/" + categoryId,
                    CacheUtil.convertModelToString(digitalCategoryDetailEntity,
                            new TypeToken<RechargeResponseEntity>() {
                            }.getType()),
                    900_000
            );
        };
    }

    @NonNull
    private Func1<RechargeResponseEntity, ProductDigitalData> getFuncTransformCategoryData() {
        return digitalCategoryDetailEntity -> productDigitalMapper.transformCategoryData(digitalCategoryDetailEntity);
    }

    private String getCategoryRequestPayload(String categoryId) {
        String query = CommonDigitalGqlMutation.INSTANCE.getDigitalCategory();
        String isSeller = GlobalConfig.isSellerApp() ? "1" : "0";
        return String.format(query, categoryId, isSeller);
    }

    private String getCategoryAndFavRequestPayload(String categoryId, String operatorId, String clientNumber, String productId) {
        String query = CommonDigitalGqlMutation.INSTANCE.getDigitalCategoryFavorites();

        String isSeller = GlobalConfig.isSellerApp() ? "1" : "0";

        if (operatorId == null) {
            operatorId = "";
        }

        if (clientNumber == null) {
            clientNumber = "";
        }

        if (productId == null) {
            productId = "";
        }

        return String.format(query, categoryId, isSeller, categoryId, operatorId, productId, clientNumber);
    }

    private String loadRawString(Resources resources, int resId) {
        InputStream rawResource = resources.openRawResource(resId);
        String content = streamToString(rawResource);
        try {
            rawResource.close();
        } catch (IOException e) {
        }
        return content;
    }

    private String streamToString(InputStream in) {
        String temp;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp + "\n");
            }
        } catch (IOException e) {
        }
        return stringBuilder.toString();
    }
}