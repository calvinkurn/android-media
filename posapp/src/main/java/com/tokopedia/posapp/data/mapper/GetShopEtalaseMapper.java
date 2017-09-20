package com.tokopedia.posapp.data.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.posapp.data.pojo.base.ListResponse;
import com.tokopedia.posapp.data.pojo.ShopEtalaseResponse;
import com.tokopedia.posapp.domain.model.shop.ShopEtalaseDomain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/19/17.
 */

public class GetShopEtalaseMapper implements Func1<Response<TkpdResponse>, List<ShopEtalaseDomain>> {
    private Gson gson;

    public GetShopEtalaseMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public List<ShopEtalaseDomain> call(Response<TkpdResponse> response) {
        ListResponse<ShopEtalaseResponse> etalaseResponse = gson.fromJson(
                response.body().getStringData(),
                new TypeToken<ListResponse<ShopEtalaseResponse>>(){}.getType()
        );

        return mapToDomain(etalaseResponse);
    }

    private List<ShopEtalaseDomain> mapToDomain(ListResponse<ShopEtalaseResponse> etalaseResponse) {
        List<ShopEtalaseDomain> domains = new ArrayList<>();
        for(ShopEtalaseResponse response: etalaseResponse.getList()) {
            ShopEtalaseDomain shopEtalaseDomain = new ShopEtalaseDomain();
            shopEtalaseDomain.setEtalaseId(response.getMenuId());
            shopEtalaseDomain.setEtalaseName(response.getMenuName());
            shopEtalaseDomain.setEtalaseAlias(response.getMenuAlias());
            shopEtalaseDomain.setUseAce(response.getUseAce());
            domains.add(shopEtalaseDomain);
        }

        return domains;
    }
}
