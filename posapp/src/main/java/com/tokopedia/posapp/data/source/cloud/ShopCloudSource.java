package com.tokopedia.posapp.data.source.cloud;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.posapp.data.mapper.GetShopEtalaseMapper;
import com.tokopedia.posapp.data.mapper.GetShopProductMapper;
import com.tokopedia.posapp.data.mapper.GetShopMapper;
import com.tokopedia.posapp.data.pojo.ListResponse;
import com.tokopedia.posapp.data.pojo.ShopEtalaseResponse;
import com.tokopedia.posapp.data.source.cloud.api.AceApi;
import com.tokopedia.posapp.data.source.cloud.api.ShopApi;
import com.tokopedia.posapp.data.source.cloud.api.TomeApi;
import com.tokopedia.posapp.domain.model.shop.ShopEtalaseDomain;
import com.tokopedia.posapp.domain.model.shop.ShopDomain;
import com.tokopedia.posapp.domain.model.shop.ShopProductListDomain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/3/17.
 */

public class ShopCloudSource {
    private ShopApi shopApi;
    private AceApi aceApi;
    private TomeApi tomeApi;
    private GetShopMapper getShopMapper;
    private GetShopProductMapper getShopProductMapper;
    private GetShopEtalaseMapper getShopEtalaseMapper;

    public ShopCloudSource(ShopApi shopApi,
                           AceApi aceApi,
                           TomeApi tomeApi,
                           GetShopMapper getShopMapper,
                           GetShopProductMapper getShopProductMapper,
                           GetShopEtalaseMapper getShopEtalaseMapper) {
        this.shopApi = shopApi;
        this.aceApi = aceApi;
        this.tomeApi = tomeApi;
        this.getShopMapper = getShopMapper;
        this.getShopProductMapper = getShopProductMapper;
        this.getShopEtalaseMapper = getShopEtalaseMapper;
    }

    public Observable<ShopDomain> getShop(RequestParams params) {
        return shopApi.getInfo(params.getParamsAllValueInString()).map(getShopMapper);
    }

    public Observable<ShopProductListDomain> getShopProduct(RequestParams params) {
        return aceApi.getShopProduct(params.getParamsAllValueInString()).map(getShopProductMapper);
    }

    public Observable<List<ShopEtalaseDomain>> getShopEtalase(RequestParams params) {
        return tomeApi.getShopEtalase(params.getParamsAllValueInString()).map(getShopEtalaseMapper);
    }
}
