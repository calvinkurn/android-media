package com.tokopedia.checkout.data.repository;

import com.tokopedia.logisticdata.data.entity.rates.RatesResponse;
import com.tokopedia.checkout.data.mapper.ShipmentRatesDataMapper;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.logisticdata.data.repository.RatesDataStore;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Irfan Khoirul on 20/02/18.
 */

public class RatesRepository {
    private final RatesDataStore dataStore;
    private final ShipmentRatesDataMapper shipmentRatesDataMapper;

    @Inject
    public RatesRepository(RatesDataStore dataStore, ShipmentRatesDataMapper shipmentRatesDataMapper) {
        this.dataStore = dataStore;
        this.shipmentRatesDataMapper = shipmentRatesDataMapper;
    }

    public Observable<ShipmentDetailData> getRates(final ShipmentDetailData shipmentDetailData,
                                                   Map<String, String> parameters) {
        return dataStore.getRates(parameters).map(new Func1<RatesResponse, ShipmentDetailData>() {
            @Override
            public ShipmentDetailData call(RatesResponse ratesResponse) {
                return shipmentRatesDataMapper.getShipmentDetailData(shipmentDetailData, ratesResponse);
            }
        });
    }
}
