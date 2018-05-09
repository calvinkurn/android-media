package com.tokopedia.checkout.domain.usecase;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.domain.mapper.ShipmentRatesDataMapper;
import com.tokopedia.logisticdata.data.repository.RatesRepository;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.logisticdata.data.entity.rates.RatesResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Irfan Khoirul on 20/02/18.
 */

public class GetRatesUseCase extends UseCase<ShipmentDetailData> {

    private static final int KILOGRAM_DIVIDER = 1000;
    private RatesRepository repository;
    private ShipmentDetailData shipmentDetailData;
    private ShipmentRatesDataMapper shipmentRatesDataMapper;

    @Inject
    public GetRatesUseCase(RatesRepository repository, ShipmentRatesDataMapper shipmentRatesDataMapper) {
        this.repository = repository;
        this.shipmentRatesDataMapper = shipmentRatesDataMapper;
    }

    public void setShipmentDetailData(ShipmentDetailData shipmentDetailData) {
        this.shipmentDetailData = shipmentDetailData;
    }

    @Override
    public Observable<ShipmentDetailData> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> mapParam = new TKPDMapParam<>();
        mapParam.putAll(requestParams.getParamsAllValueInString());
        return repository.getRates(mapParam).map(new Func1<RatesResponse, ShipmentDetailData>() {
            @Override
            public ShipmentDetailData call(RatesResponse ratesResponse) {
                return shipmentRatesDataMapper.getShipmentDetailData(shipmentDetailData, ratesResponse);
            }
        });
    }

    public RequestParams getParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(Params.SERVICE, shipmentDetailData.getShipmentCartData().getShippingServices());
        requestParams.putString(Params.NAMES, shipmentDetailData.getShipmentCartData().getShippingNames());
        double weightInKilograms = shipmentDetailData.getShipmentCartData().getWeight() / KILOGRAM_DIVIDER;
        requestParams.putString(Params.WEIGHT, String.valueOf(weightInKilograms));
        requestParams.putString(Params.TYPE, Params.VALUE_ANDROID);
        requestParams.putString(Params.FROM, Params.VALUE_CLIENT);
        requestParams.putString(Params.TOKEN, shipmentDetailData.getShipmentCartData().getToken());
        requestParams.putString(Params.UT, shipmentDetailData.getShipmentCartData().getUt());
        requestParams.putString(Params.INSURANCE, String.valueOf(shipmentDetailData.getShipmentCartData().getInsurance()));
        requestParams.putString(Params.PRODUCT_INSURANCE,
                String.valueOf(shipmentDetailData.getShipmentCartData().getProductInsurance()));
        requestParams.putString(Params.ORDER_VALUE, String.valueOf(shipmentDetailData.getShipmentCartData().getOrderValue()));
        requestParams.putString(Params.CAT_ID, shipmentDetailData.getShipmentCartData().getCategoryIds());

        StringBuilder originBuilder = new StringBuilder();
        originBuilder.append(shipmentDetailData.getShipmentCartData().getOriginDistrictId());
        if (!TextUtils.isEmpty(shipmentDetailData.getShipmentCartData().getOriginPostalCode())) {
            originBuilder.append("|")
                    .append(shipmentDetailData.getShipmentCartData().getOriginPostalCode());
        } else {
            originBuilder.append("|").append("0");
        }
        if (shipmentDetailData.getShipmentCartData().getOriginLatitude() != null) {
            originBuilder.append("|")
                    .append(shipmentDetailData.getShipmentCartData().getOriginLatitude());
        }
        if (shipmentDetailData.getShipmentCartData().getOriginLongitude() != null) {
            originBuilder.append(",")
                    .append(shipmentDetailData.getShipmentCartData().getOriginLongitude());
        }
        requestParams.putString(Params.ORIGIN, originBuilder.toString());

        StringBuilder destinationBuilder = new StringBuilder();
        destinationBuilder.append(shipmentDetailData.getShipmentCartData().getDestinationDistrictId());
        if (!TextUtils.isEmpty(shipmentDetailData.getShipmentCartData().getDestinationPostalCode())) {
            destinationBuilder.append("|")
                    .append(shipmentDetailData.getShipmentCartData().getDestinationPostalCode());
        } else {
            originBuilder.append("|").append("0");
        }
        if (shipmentDetailData.getShipmentCartData().getDestinationLatitude() != null) {
            destinationBuilder.append("|")
                    .append(shipmentDetailData.getShipmentCartData().getDestinationLatitude());
        }
        if (shipmentDetailData.getShipmentCartData().getDestinationLongitude() != null) {
            destinationBuilder.append(",")
                    .append(shipmentDetailData.getShipmentCartData().getDestinationLongitude());
        }
        requestParams.putString(Params.DESTINATION, destinationBuilder.toString());

        return requestParams;
    }

    private static final class Params {
        static final String SERVICE = "service";
        static final String NAMES = "names";
        static final String ORIGIN = "origin";
        static final String DESTINATION = "destination";
        static final String WEIGHT = "weight";
        static final String TYPE = "type";
        static final String FROM = "from";
        static final String TOKEN = "token";
        static final String UT = "ut";
        static final String INSURANCE = "insurance";
        static final String PRODUCT_INSURANCE = "product_insurance";
        static final String ORDER_VALUE = "order_value";
        static final String CAT_ID = "cat_id";
        static final String VALUE_ANDROID = "android";
        static final String VALUE_CLIENT = "client";
    }

}
