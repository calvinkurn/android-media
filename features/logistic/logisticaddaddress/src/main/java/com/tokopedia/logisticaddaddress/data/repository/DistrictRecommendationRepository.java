package com.tokopedia.logisticaddaddress.data.repository;

import com.tokopedia.logisticaddaddress.data.mapper.DistrictRecommendationEntityMapper;
import com.tokopedia.logisticaddaddress.data.entity.AddressResponseEntity;
import com.tokopedia.logisticaddaddress.data.source.DistrictRecommendationDataStore;
import com.tokopedia.logisticaddaddress.domain.model.AddressResponse;
import com.tokopedia.network.utils.TKPDMapParam;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Irfan Khoirul on 17/11/17.
 */

public class DistrictRecommendationRepository {

    private final DistrictRecommendationDataStore dataStore;
    private final DistrictRecommendationEntityMapper entityMapper;

    @Inject
    public DistrictRecommendationRepository(DistrictRecommendationDataStore dataStore,
                                            DistrictRecommendationEntityMapper entityMapper) {
        this.dataStore = dataStore;
        this.entityMapper = entityMapper;
    }

    public Observable<AddressResponse> getAddresses(TKPDMapParam<String, String> parameters) {
        return dataStore.getAddresses(parameters).map(new Func1<AddressResponseEntity, AddressResponse>() {
            @Override
            public AddressResponse call(AddressResponseEntity addressResponseEntity) {
                return entityMapper.transform(addressResponseEntity);
            }
        });
    }
}
