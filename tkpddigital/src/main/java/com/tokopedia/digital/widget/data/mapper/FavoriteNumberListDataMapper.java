package com.tokopedia.digital.widget.data.mapper;

import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.widget.data.entity.response.ResponseFavoriteList;
import com.tokopedia.digital.widget.data.entity.response.ResponseFavoriteNumber;
import com.tokopedia.digital.widget.model.DigitalNumberList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rizkyfadillah on 10/2/2017.
 */

public class FavoriteNumberListDataMapper implements IFavoriteNumberMapper {

    @Override
    public DigitalNumberList transformDigitalFavoriteNumberItemDataList(ResponseFavoriteList responseFavoriteNumbers) {
        DigitalNumberList digitalNumberList;
        if (!responseFavoriteNumbers.getResponseNumberList().isEmpty()) {
            List<OrderClientNumber> orderClientNumbers = new ArrayList<>();
            for (ResponseFavoriteNumber responseFavoriteNumber : responseFavoriteNumbers.getResponseNumberList()) {
                orderClientNumbers.add(new OrderClientNumber.Builder()
                        .name(responseFavoriteNumber.getAttributes().getLabel())
                        .clientNumber(responseFavoriteNumber.getAttributes().getClientNumber())
                        .lastUpdated(responseFavoriteNumber.getAttributes().getLastUpdated())
                        .lastProduct(responseFavoriteNumber.getAttributes().getLastProduct())
                        .categoryId(responseFavoriteNumber.getRelationships().getCategory().getData().getId())
                        .operatorId(responseFavoriteNumber.getRelationships().getOperator().getData().getId())
                        .productId(responseFavoriteNumber.getAttributes().getLastProduct())
                        .build());
            }
            int defaulIndex = responseFavoriteNumbers.getResponseMeta().getDefaultIndex();
            OrderClientNumber defaultOrder = orderClientNumbers.get(defaulIndex);
            digitalNumberList = new DigitalNumberList(orderClientNumbers, defaultOrder);
        } else {
            digitalNumberList = new DigitalNumberList(new ArrayList<OrderClientNumber>(), null);
        }
        return digitalNumberList;
    }
}
