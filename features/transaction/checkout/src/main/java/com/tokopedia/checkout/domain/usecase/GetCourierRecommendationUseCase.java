package com.tokopedia.checkout.domain.usecase;

import android.text.TextUtils;

import com.tokopedia.checkout.domain.datamodel.cartshipmentform.ShipProd;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.ShopShipment;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentItemData;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.GetCourierRecommendationData;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 07/08/18.
 */

public class GetCourierRecommendationUseCase {

    private static final int KILOGRAM_DIVIDER = 1000;

    private final GraphqlUseCase graphqlUseCase;

    @Inject
    public GetCourierRecommendationUseCase(GraphqlUseCase graphqlUseCase) {
        this.graphqlUseCase = graphqlUseCase;
    }

    public void execute(String query, ShipmentDetailData shipmentDetailData, Subscriber<GraphqlResponse> subscriber) {
        graphqlUseCase.clearRequest();
        query = getQueryWithParams(query, shipmentDetailData);

        GraphqlRequest request = new GraphqlRequest(query, GetCourierRecommendationData.class);

        graphqlUseCase.addRequest(request);
        graphqlUseCase.execute(subscriber);
    }

    public void unsubscribe() {
        graphqlUseCase.unsubscribe();
    }

    private String getQueryWithParams(String query, ShipmentDetailData shipmentDetailData) {
        if (shipmentDetailData.getShipmentCartData() != null &&
                shipmentDetailData.getShipmentCartData().getShopShipments() != null) {
            StringBuilder spidsBuilder = new StringBuilder();
            List<ShopShipment> shopShipmentList = shipmentDetailData.getShipmentCartData().getShopShipments();
            for (int i = 0; i < shopShipmentList.size(); i++) {
                List<ShipProd> shipProdList = shopShipmentList.get(i).getShipProds();
                if (shipProdList != null) {
                    for (int j = 0; j < shipProdList.size(); j++) {
                        spidsBuilder.append(shipProdList.get(j).getShipProdId());
                        if (i < shopShipmentList.size() - 1 && j < shipProdList.size() - 1) {
                            spidsBuilder.append(",");
                        }
                    }
                }
            }
            query = query.replace(Param.SPIDS, spidsBuilder.toString());

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
            query = query.replace(Param.ORIGIN, originBuilder.toString());

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
            query = query.replace(Param.DESTINATION, destinationBuilder.toString());

            double weightInKilograms = shipmentDetailData.getShipmentCartData().getWeight() / KILOGRAM_DIVIDER;
            query = query.replace(Param.WEIGHT, String.valueOf(weightInKilograms));

            query = query.replace(Param.SHOP_ID, shipmentDetailData.getShopId());
            query = query.replace(Param.TYPE, Param.VALUE_ANDROID);
            query = query.replace(Param.FROM, Param.VALUE_CLIENT);
            query = query.replace(Param.TOKEN, shipmentDetailData.getShipmentCartData().getToken());
            query = query.replace(Param.UT, shipmentDetailData.getShipmentCartData().getUt());
            query = query.replace(Param.INSURANCE, String.valueOf(shipmentDetailData.getShipmentCartData().getInsurance()));
            query = query.replace(Param.PRODUCT_INSURANCE, String.valueOf(shipmentDetailData.getShipmentCartData().getProductInsurance()));
            query = query.replace(Param.ORDER_VALUE, String.valueOf(shipmentDetailData.getShipmentCartData().getOrderValue()));
            query = query.replace(Param.CAT_ID, shipmentDetailData.getShipmentCartData().getCategoryIds());
            query = query.replace(Param.LANG, Param.VALUE_LANG_ID);
        }

        return query;
    }

    private static final class Param {
        static final String SPIDS = "$spids";
        static final String SHOP_ID = "$shop_id";
        static final String ORIGIN = "$origin";
        static final String DESTINATION = "$destination";
        static final String WEIGHT = "$weight";
        static final String TYPE = "$type";
        static final String FROM = "$from";
        static final String TOKEN = "$token";
        static final String UT = "$ut";
        static final String INSURANCE = "$insurance";
        static final String PRODUCT_INSURANCE = "$product_insurance";
        static final String ORDER_VALUE = "$order_value";
        static final String CAT_ID = "$cat_id";
        static final String LANG = "$lang";

        static final String VALUE_ANDROID = "android";
        static final String VALUE_CLIENT = "client";
        static final String VALUE_LANG_ID = "id";
    }

}
