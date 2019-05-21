package com.tokopedia.common.travel.domain;

import com.tokopedia.common.travel.data.entity.TravelPassengerEntity;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.flight.country.database.CountryPhoneCodeTable;

import java.util.List;

import rx.Observable;

/**
 * Created by nabillasabbaha on 12/03/19.
 */
public interface IPhoneCodeRepository {

    Observable<List<CountryPhoneCodeTable>> getPhoneCodeList(String string);
}
