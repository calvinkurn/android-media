package com.tokopedia.tkpd.beranda.data.source;

import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.tkpd.beranda.data.mapper.BrandsOfficialStoreMapper;
import com.tokopedia.tkpd.beranda.domain.model.brands.BrandsOfficialStoreResponseModel;

import rx.Observable;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class BrandsOfficialStoreDataSource {

    private final MojitoApi mojitoApi;
    private final BrandsOfficialStoreMapper brandsOfficialStoreMapper;

    public BrandsOfficialStoreDataSource(MojitoApi mojitoApi, BrandsOfficialStoreMapper brandsOfficialStoreMapper) {
        this.mojitoApi = mojitoApi;
        this.brandsOfficialStoreMapper = brandsOfficialStoreMapper;
    }

    public Observable<BrandsOfficialStoreResponseModel> getBrandsOfficialStore(){
        return mojitoApi.getBrandsOfficialStore().map(brandsOfficialStoreMapper);
    }
}
