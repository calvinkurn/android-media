package com.tokopedia.posapp.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.data.mapper.GetEtalaseMapper;
import com.tokopedia.posapp.data.source.cloud.api.TomeApi;
import com.tokopedia.posapp.domain.model.shop.EtalaseDomain;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 9/25/17.
 */

public class EtalaseCloudSource {
    private TomeApi tomeApi;
    private GetEtalaseMapper getEtalaseMapper;

    public EtalaseCloudSource(TomeApi tomeApi, GetEtalaseMapper getEtalaseMapper) {
        this.tomeApi = tomeApi;
        this.getEtalaseMapper = getEtalaseMapper;
    }

    public Observable<List<EtalaseDomain>> getEtalase(RequestParams params) {
        return tomeApi.getShopEtalase(params.getParamsAllValueInString()).map(getEtalaseMapper);
    }
}
