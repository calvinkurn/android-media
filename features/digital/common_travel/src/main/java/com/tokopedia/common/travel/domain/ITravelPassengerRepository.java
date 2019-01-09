package com.tokopedia.common.travel.domain;

import com.tokopedia.common.travel.data.entity.TravelPassengerEntity;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;

import java.util.List;

import rx.Observable;

/**
 * Created by nabillasabbaha on 17/12/18.
 */
public interface ITravelPassengerRepository {

    Observable<Boolean> deletePassenger(String idPassenger);

    Observable<List<TravelPassenger>> findAllTravelPassenger(List<TravelPassengerEntity> travelPassengerEntities,
                                                             boolean resetDb);

    Observable<Boolean> updatePassenger(boolean isSelected, String idPassenger);

    Observable<Boolean> updateDataTravelPassenger(String idPassenger,
                                                  TravelPassengerEntity travelPassengerEntity);
}
