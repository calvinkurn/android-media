package com.tokopedia.posapp.data.factory;

import com.tokopedia.posapp.data.mapper.GetEtalaseMapper;
import com.tokopedia.posapp.data.source.cloud.api.TomeApi;
import com.tokopedia.posapp.data.source.cloud.api.GatewayProductApi;
import com.tokopedia.posapp.data.source.cloud.EtalaseCloudSource;
import com.tokopedia.posapp.data.source.local.EtalaseLocalSource;

/**
 * Created by okasurya on 9/25/17.
 */

public class EtalaseFactory {
    private TomeApi tomeApi;
    private GetEtalaseMapper getEtalaseMapper;
    private GatewayProductApi gatewayProductApi;

    public EtalaseFactory(GatewayProductApi gatewayProductApi, TomeApi tomeApi, GetEtalaseMapper getEtalaseMapper) {
        this.gatewayProductApi = gatewayProductApi;
        this.tomeApi = tomeApi;
        this.getEtalaseMapper = getEtalaseMapper;
    }

    public EtalaseCloudSource cloud() {
        return new EtalaseCloudSource(gatewayProductApi, getEtalaseMapper);
    }

    public EtalaseLocalSource local() {
        return new EtalaseLocalSource();
    }
}
