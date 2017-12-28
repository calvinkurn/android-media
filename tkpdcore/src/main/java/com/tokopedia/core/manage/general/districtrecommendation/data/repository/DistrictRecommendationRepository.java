package com.tokopedia.core.manage.general.districtrecommendation.data.repository;

import com.tokopedia.core.manage.general.districtrecommendation.data.DistrictRecommendationEntityMapper;
import com.tokopedia.core.manage.general.districtrecommendation.data.entity.AddressResponseEntity;
import com.tokopedia.core.manage.general.districtrecommendation.data.source.DistrictRecommendationDataStore;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.AddressResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

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
