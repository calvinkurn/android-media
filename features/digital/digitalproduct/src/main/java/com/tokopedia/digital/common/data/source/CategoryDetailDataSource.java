package com.tokopedia.digital.common.data.source;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.constant.DigitalCache;
import com.tokopedia.digital.common.constant.DigitalCategoryConstant;
import com.tokopedia.digital.common.constant.DigitalUrl;
import com.tokopedia.digital.common.data.apiservice.DigitalGqlApiService;
import com.tokopedia.digital.common.data.entity.response.RechargeResponseEntity;
import com.tokopedia.digital.common.data.mapper.ProductDigitalMapper;
import com.tokopedia.digital.product.view.model.ProductDigitalData;

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
 *         Modified by Vishal Gupta on 8th May, 2018
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

    /**
     * Fetches Category Detail Data, first this function try to fetch data from local if exists
     *
     * @param categoryId
     * @return
     */
    public Observable<ProductDigitalData> getCategory(String categoryId) {
        return Observable.concat(getCategoryDataFromLocal(categoryId), getCategoryDataFromCloud(categoryId))
                .first(new Func1<ProductDigitalData, Boolean>() {
                    @Override
                    public Boolean call(ProductDigitalData categoryData) {
                        return categoryData != null;
                    }
                });
    }

    /**
     * Fetches CategoryDetail and Favorit Number Combined, first this function try to fetch data from local if exists
     *
     * @param categoryId
     * @return
     */
    public Observable<ProductDigitalData> getCategoryDetailWithFavorit(String categoryId) {
        return Observable.concat(getCategoryAndFavDataFromLocal(categoryId), getCategoryAndFavoritFromCloud(categoryId, "", "", ""))
                .first(new Func1<ProductDigitalData, Boolean>() {
                    @Override
                    public Boolean call(ProductDigitalData productDigitalData) {
                        return productDigitalData != null;
                    }
                });
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
                    globalCacheManager.getValueString(DigitalCache.NEW_DIGITAL_CATEGORY_DETAIL + "/" + categoryId),
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
        return digitalEndpointService.getApi().getCategory(String.format(getCategoryRequestPayload(), categoryId))
                .map(new Func1<Response<GraphqlResponse<RechargeResponseEntity>>, RechargeResponseEntity>() {
                    @Override
                    public RechargeResponseEntity call(Response<GraphqlResponse<RechargeResponseEntity>> response) {
                        return response.body().getData();
                    }
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
            digitalCategoryDetailEntity = CacheUtil.convertStringToModel(
                    globalCacheManager.getValueString(DigitalCache.NEW_DIGITAL_CATEGORY_AND_FAV + "/" + categoryId),
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
        return digitalEndpointService.getApi().getCategoryAndFavoriteList(getCategoryAndFavRequestPayload(categoryId, operatorId, clientNumber, productId))
                .map(new Func1<Response<GraphqlResponse<RechargeResponseEntity>>, RechargeResponseEntity>() {
                    @Override
                    public RechargeResponseEntity call(Response<GraphqlResponse<RechargeResponseEntity>> response) {
                        return response.body().getData();
                    }
                })
                .doOnNext(saveCategoryDetailAndFavToCache(categoryId))
                .map(getFuncTransformCategoryData());
    }

    private Action1<RechargeResponseEntity> saveCategoryDetailToCache(final String categoryId) {
        return new Action1<RechargeResponseEntity>() {
            @Override
            public void call(RechargeResponseEntity digitalCategoryDetailEntity) {
                globalCacheManager.setKey(DigitalCache.NEW_DIGITAL_CATEGORY_DETAIL + "/" + categoryId);
                globalCacheManager.setValue(CacheUtil.convertModelToString(digitalCategoryDetailEntity,
                        new TypeToken<RechargeResponseEntity>() {
                        }.getType()));
                globalCacheManager.setCacheDuration(900); // 15 minutes
                globalCacheManager.store();
            }
        };
    }

    private Action1<RechargeResponseEntity> saveCategoryDetailAndFavToCache(final String categoryId) {
        return new Action1<RechargeResponseEntity>() {
            @Override
            public void call(RechargeResponseEntity digitalCategoryDetailEntity) {
                globalCacheManager.setKey(DigitalCache.NEW_DIGITAL_CATEGORY_AND_FAV + "/" + categoryId);
                globalCacheManager.setValue(CacheUtil.convertModelToString(digitalCategoryDetailEntity,
                        new TypeToken<RechargeResponseEntity>() {
                        }.getType()));
                globalCacheManager.setCacheDuration(900); // 15 //fetch category detail if user is not logged in
                globalCacheManager.store();
            }
        };
    }

    @NonNull
    private Func1<RechargeResponseEntity, ProductDigitalData> getFuncTransformCategoryData() {
        return new Func1<RechargeResponseEntity, ProductDigitalData>() {
            @Override
            public ProductDigitalData call(RechargeResponseEntity digitalCategoryDetailEntity) {
                return productDigitalMapper.transformCategoryData(digitalCategoryDetailEntity);
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

    private String getCategoryRequestPayload(String categoryId) {
        String query = loadRawString(context.getResources(), R.raw.digital_category_query);
        String isSeller = GlobalConfig.isSellerApp() ? "1" : "0";
        return String.format(query, categoryId, isSeller);
    }

    private String getCategoryAndFavRequestPayload(String categoryId, String operatorId, String clientNumber, String productId) {

        String query = loadRawString(context.getResources(), R.raw.digital_category_favourites_query);

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