package com.tokopedia.digital.common.data.source;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.constant.DigitalCache;
import com.tokopedia.digital.common.constant.DigitalCategoryConstant;
import com.tokopedia.digital.common.constant.DigitalUrl;
import com.tokopedia.digital.common.data.apiservice.DigitalGqlApiService;
import com.tokopedia.digital.common.data.entity.response.RechargeCategoryDetailEntity;
import com.tokopedia.digital.common.data.mapper.ProductDigitalMapper;
import com.tokopedia.digital.product.view.model.CategoryData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author rizkyfadillah on 19/01/18.
 */

public class CategoryDetailDataSource {

    private DigitalGqlApiService digitalEndpointService;
    private GlobalCacheManager globalCacheManager;
    private ProductDigitalMapper productDigitalMapper;
    private Context context;

    public CategoryDetailDataSource(DigitalGqlApiService digitalEndpointService,
                                    GlobalCacheManager globalCacheManager,
                                    ProductDigitalMapper productDigitalMapper) {
        this.digitalEndpointService = digitalEndpointService;
        this.globalCacheManager = globalCacheManager;
        this.productDigitalMapper = productDigitalMapper;
        context = MainApplication.getAppContext();
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
        RechargeCategoryDetailEntity digitalCategoryDetailEntity;
        try {
            digitalCategoryDetailEntity = CacheUtil.convertStringToModel(
                    globalCacheManager.getValueString(DigitalCache.NEW_DIGITAL_CATEGORY_DETAIL + "/" + categoryId),
                    new TypeToken<RechargeCategoryDetailEntity>() {
                    }.getType());
        } catch (RuntimeException e) {
            digitalCategoryDetailEntity = null;
        }

        CategoryData categoryData = null;
        if (digitalCategoryDetailEntity != null && digitalCategoryDetailEntity.getRechargeCategoryDetail() != null) {
            categoryData = productDigitalMapper.transformCategoryData(digitalCategoryDetailEntity.getRechargeCategoryDetail());
        }

        return Observable.just(categoryData);
    }

    private Observable<CategoryData> getDataFromCloud(String categoryId, TKPDMapParam<String, String> param) {
        return digitalEndpointService.getApi().getCategory(String.format(getRequestPayload(), categoryId))
                .map(new Func1<Response<GraphqlResponse<RechargeCategoryDetailEntity>>, RechargeCategoryDetailEntity>() {
                    @Override
                    public RechargeCategoryDetailEntity call(Response<GraphqlResponse<RechargeCategoryDetailEntity>> response) {
                        return response.body().getData();
                    }
                })
                //.doOnNext(saveToCache(categoryId))
                .map(getFuncTransformCategoryData());
    }

    private Action1<RechargeCategoryDetailEntity> saveToCache(final String categoryId) {
        return new Action1<RechargeCategoryDetailEntity>() {
            @Override
            public void call(RechargeCategoryDetailEntity digitalCategoryDetailEntity) {
                globalCacheManager.setKey(DigitalCache.NEW_DIGITAL_CATEGORY_DETAIL + "/" + categoryId);
                globalCacheManager.setValue(CacheUtil.convertModelToString(digitalCategoryDetailEntity,
                        new TypeToken<RechargeCategoryDetailEntity>() {
                        }.getType()));
                globalCacheManager.setCacheDuration(600); // 10 minutes
                globalCacheManager.store();
            }
        };
    }

    @NonNull
    private Func1<RechargeCategoryDetailEntity, CategoryData> getFuncTransformCategoryData() {
        return new Func1<RechargeCategoryDetailEntity, CategoryData>() {
            @Override
            public CategoryData call(RechargeCategoryDetailEntity digitalCategoryDetailEntity) {
                return productDigitalMapper.transformCategoryData(digitalCategoryDetailEntity.getRechargeCategoryDetail());
            }
        };
    }

    public Observable<String> getHelpUrl(String categoryId) {
        String result;
        switch (categoryId) {
            case DigitalCategoryConstant.PULSA:
                result = DigitalUrl.HelpUrl.PULSA;
                break;
            case DigitalCategoryConstant.PAKET_DATA:
                result = DigitalUrl.HelpUrl.PAKET_DATA;
                break;
            case DigitalCategoryConstant.PLN:
                result = DigitalUrl.HelpUrl.PLN;
                break;
            case DigitalCategoryConstant.BPJS:
                result = DigitalUrl.HelpUrl.BPJS;
                break;
            case DigitalCategoryConstant.PDAM:
                result = DigitalUrl.HelpUrl.PDAM;
                break;
            case DigitalCategoryConstant.GAME:
                result = DigitalUrl.HelpUrl.GAME;
                break;
            case DigitalCategoryConstant.CREDIT:
                result = DigitalUrl.HelpUrl.CREDIT;
                break;
            case DigitalCategoryConstant.TV:
                result = DigitalUrl.HelpUrl.TV;
                break;
            case DigitalCategoryConstant.POSTPAID:
                result = DigitalUrl.HelpUrl.POSTPAID;
                break;
            case DigitalCategoryConstant.TELKOM:
                result = DigitalUrl.HelpUrl.TELKOM;
                break;
            case DigitalCategoryConstant.STREAMING:
                result = DigitalUrl.HelpUrl.STREAMING;
                break;
            case DigitalCategoryConstant.PGN:
                result = DigitalUrl.HelpUrl.PGN;
                break;
            case DigitalCategoryConstant.ROAMING:
                result = DigitalUrl.HelpUrl.ROAMING;
                break;
            case DigitalCategoryConstant.TAX:
                result = DigitalUrl.HelpUrl.TAX;
                break;
            case DigitalCategoryConstant.GIFT_CARD:
                result = DigitalUrl.HelpUrl.GIFT_CARD;
                break;
            case DigitalCategoryConstant.RETRIBUTION:
                result = DigitalUrl.HelpUrl.RETRIBUTION;
                break;
            case DigitalCategoryConstant.MTIX:
                result = DigitalUrl.HelpUrl.MTIX;
                break;
            case DigitalCategoryConstant.CREDIT_CARD:
                result = DigitalUrl.HelpUrl.CREDIT_CARD;
                break;
            default:
                result = "";
        }
        return Observable.just(result);
    }

    private String getRequestPayload() {
        return loadRawString(context.getResources(), R.raw.digital_category_query);
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