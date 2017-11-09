package com.tokopedia.posapp.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.data.mapper.GetEtalaseMapper;
import com.tokopedia.posapp.data.source.cloud.api.GatewayProductApi;
import com.tokopedia.posapp.domain.model.shop.EtalaseDomain;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 10/17/17.
 */

public class EtalaseCloudSource {
    private static final String SHOP_ID = "shop_id";

    private GatewayProductApi gatewayProductApi;
    private GetEtalaseMapper getEtalaseMapper;

    public EtalaseCloudSource(GatewayProductApi gatewayProductApi, GetEtalaseMapper getEtalaseMapper) {
        this.gatewayProductApi = gatewayProductApi;
        this.getEtalaseMapper = getEtalaseMapper;
    }

    public Observable<List<EtalaseDomain>> getEtalase(RequestParams params) {
        return gatewayProductApi.getEtalase(params.getParamsAllValueInString()).map(getEtalaseMapper);
    }
}
