package com.tokopedia.shipping_recommendation.domain.usecase;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.GetRatesCourierRecommendationData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingRecommendationData;
import com.tokopedia.shipping_recommendation.shippingduration.view.ShippingDurationConverter;
import com.tokopedia.shipping_recommendation.domain.ShippingParam;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingRecommendationData;
import com.tokopedia.shipping_recommendation.shippingduration.view.ShippingDurationConverter;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipProd;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentDetailData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingRecommendationData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShopShipment;
import com.tokopedia.shipping_recommendation.shippingduration.view.ShippingDurationConverter;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Irfan Khoirul on 07/08/18.
 */

public class GetCourierRecommendationUseCase extends GraphqlUseCase {

    private static final int KILOGRAM_DIVIDER = 1000;

    private final ShippingDurationConverter shippingDurationConverter;

    @Inject
    public GetCourierRecommendationUseCase(ShippingDurationConverter shippingDurationConverter) {
        this.shippingDurationConverter = shippingDurationConverter;
    }

    public void execute(String query,
                        int codHistory,
                        String cornerId,
                        ShippingParam shippingParam,
                        int selectedSpId,
                        int selectedServiceId,
                        List<ShopShipment> shopShipments,
                        Subscriber<ShippingRecommendationData> subscriber) {
        query = getQueryWithParams(query, codHistory, cornerId, shopShipments, shippingParam);
        executeQuery(query, selectedSpId, selectedServiceId, shopShipments, subscriber);
    }

    private void executeQuery(String query, int selectedSpId, int selectedServiceId, List<ShopShipment> shopShipments,
                              Subscriber<ShippingRecommendationData> subscriber) {
        clearRequest();

        GraphqlRequest request = new GraphqlRequest(query, GetRatesCourierRecommendationData.class, false);

        addRequest(request);
        createObservable(RequestParams.EMPTY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<GraphqlResponse, ShippingRecommendationData>() {
                    @Override
                    public ShippingRecommendationData call(GraphqlResponse graphqlResponse) {
                        GetRatesCourierRecommendationData data = graphqlResponse.getData(GetRatesCourierRecommendationData.class);
                        ShippingRecommendationData shippingRecommendationData = new ShippingRecommendationData();

                        // Check response not null
                        if (data != null && data.getRatesData() != null && data.getRatesData().getRatesDetailData() != null) {
                            // Check has service / duration list
                            if (data.getRatesData().getRatesDetailData().getServices() != null &&
                                    data.getRatesData().getRatesDetailData().getServices().size() > 0) {
                                // Check if has error
                                if (data.getRatesData().getRatesDetailData().getError() != null &&
                                        !TextUtils.isEmpty(data.getRatesData().getRatesDetailData().getError().getErrorMessage())) {
                                    shippingRecommendationData.setErrorMessage(data.getRatesData().getRatesDetailData().getError().getErrorMessage());
                                    shippingRecommendationData.setErrorId(data.getRatesData().getRatesDetailData().getError().getErrorId());
                                }

                                // Check if has info
                                String blackboxInfo = "";
                                if (data.getRatesData().getRatesDetailData().getInfo() != null &&
                                        !TextUtils.isEmpty(data.getRatesData().getRatesDetailData().getInfo().getBlackboxInfo().getTextInfo())) {
                                    blackboxInfo = data.getRatesData().getRatesDetailData().getInfo().getBlackboxInfo().getTextInfo();
                                }

                                String ratesId = data.getRatesData().getRatesDetailData().getRatesId();
                                // Has service / duration list
                                shippingRecommendationData.setShippingDurationViewModels(
                                        shippingDurationConverter.convertToViewModel(
                                                data.getRatesData().getRatesDetailData().getServices(),
                                                shopShipments, selectedSpId, ratesId, selectedServiceId, blackboxInfo));
                            }
                        }
                        return shippingRecommendationData;
                    }
                })
                .subscribe(subscriber);
    }

    private String getQueryWithParams(String query, int codHistory, String cornerId, List<ShopShipment> shopShipmentList, ShippingParam shippingParam) {
        StringBuilder queryStringBuilder = new StringBuilder(query);

        StringBuilder spidsStringBuilder = new StringBuilder();
        for (int i = 0; i < shopShipmentList.size(); i++) {
            List<ShipProd> shipProdList = shopShipmentList.get(i).getShipProds();
            if (shipProdList != null) {
                for (int j = 0; j < shipProdList.size(); j++) {
                    spidsStringBuilder.append(shipProdList.get(j).getShipProdId());
                    spidsStringBuilder.append(",");
                }
            }
        }
        spidsStringBuilder.replace(spidsStringBuilder.lastIndexOf(","), spidsStringBuilder.lastIndexOf(",") + 1, "");
        queryStringBuilder = setParam(queryStringBuilder, Param.SPIDS, spidsStringBuilder.toString());

        StringBuilder originStringBuilder = new StringBuilder();
        originStringBuilder.append(shippingParam.getOriginDistrictId());
        if (!TextUtils.isEmpty(shippingParam.getOriginPostalCode())) {
            originStringBuilder.append("|").append(shippingParam.getOriginPostalCode());
        } else {
            originStringBuilder.append("|").append("0");
        }
        if (shippingParam.getOriginLatitude() != null) {
            originStringBuilder.append("|").append(shippingParam.getOriginLatitude());
        }
        if (shippingParam.getOriginLongitude() != null) {
            originStringBuilder.append(",").append(shippingParam.getOriginLongitude());
        }
        queryStringBuilder = setParam(queryStringBuilder, Param.ORIGIN, originStringBuilder.toString());

        StringBuilder destinationStringBuilder = new StringBuilder();
        destinationStringBuilder.append(shippingParam.getDestinationDistrictId());
        if (!TextUtils.isEmpty(shippingParam.getDestinationPostalCode())) {
            destinationStringBuilder.append("|").append(shippingParam.getDestinationPostalCode());
        } else {
            originStringBuilder.append("|").append("0");
        }
        if (shippingParam.getDestinationLatitude() != null) {
            destinationStringBuilder.append("|").append(shippingParam.getDestinationLatitude());
        }
        if (shippingParam.getDestinationLongitude() != null) {
            destinationStringBuilder.append(",").append(shippingParam.getDestinationLongitude());
        }
        queryStringBuilder = setParam(queryStringBuilder, Param.DESTINATION, destinationStringBuilder.toString());

        double weightInKilograms = shippingParam.getWeightInKilograms();
        queryStringBuilder = setParam(queryStringBuilder, Param.WEIGHT, String.valueOf(weightInKilograms));

        int cornerIdInt = TextUtils.isEmpty(cornerId) ? 0 : Integer.parseInt(cornerId);
        queryStringBuilder = setParam(queryStringBuilder, Param.CORNER_ID, String.valueOf(cornerIdInt));

        queryStringBuilder = setParam(queryStringBuilder, Param.SHOP_ID, shippingParam.getShopId());
        queryStringBuilder = setParam(queryStringBuilder, Param.TYPE, Param.VALUE_ANDROID);
        queryStringBuilder = setParam(queryStringBuilder, Param.FROM, Param.VALUE_CLIENT);
        queryStringBuilder = setParam(queryStringBuilder, Param.TOKEN, shippingParam.getToken());
        queryStringBuilder = setParam(queryStringBuilder, Param.UT, shippingParam.getUt());
        queryStringBuilder = setParam(queryStringBuilder, Param.INSURANCE, String.valueOf(shippingParam.getInsurance()));
        queryStringBuilder = setParam(queryStringBuilder, Param.PRODUCT_INSURANCE, String.valueOf(shippingParam.getProductInsurance()));
        queryStringBuilder = setParam(queryStringBuilder, Param.ORDER_VALUE, String.valueOf(shippingParam.getOrderValue()));
        queryStringBuilder = setParam(queryStringBuilder, Param.CAT_ID, shippingParam.getCategoryIds());
        queryStringBuilder = setParam(queryStringBuilder, Param.LANG, Param.VALUE_LANG_ID);
        queryStringBuilder = setParam(queryStringBuilder, Param.USER_HISTORY, String.valueOf(codHistory));
        queryStringBuilder = setParam(queryStringBuilder, Param.IS_BLACKBOX, String.valueOf(shippingParam.getIsBlackbox() ? 1 : 0));
        queryStringBuilder = setParam(queryStringBuilder, Param.ADDRESS_ID, String.valueOf(shippingParam.getAddressId()));
        queryStringBuilder = setParam(queryStringBuilder, Param.PREORDER, String.valueOf(shippingParam.getIsPreorder() ? 1 : 0));

        return queryStringBuilder.toString();
    }

    private StringBuilder setParam(StringBuilder originalString, String paramName, String paramValue) {
        return originalString.replace(originalString.indexOf(paramName),
                originalString.indexOf(paramName) + paramName.length(),
                paramValue);
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
        static final String USER_HISTORY = "$user_history";
        static final String CORNER_ID = "$corner_id";
        static final String IS_BLACKBOX = "$is_blackbox";
        static final String ADDRESS_ID = "$address_id";
        static final String PREORDER = "$preorder";

        static final String VALUE_ANDROID = "android";
        static final String VALUE_CLIENT = "client";
        static final String VALUE_LANG_ID = "id";
    }

}
