package com.tokopedia.digital.widget.data.mapper;

import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.widget.data.entity.response.ResponseFavoriteNumber;
import com.tokopedia.digital.widget.data.entity.response.ResponseNumberList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rizkyfadillah on 10/2/2017.
 */

public class FavoriteNumberListDataMapper implements IFavoriteNumberMapper {

    @Override
    public List<OrderClientNumber> transformDigitalFavoriteNumberItemDataList(List<ResponseFavoriteNumber> responseFavoriteNumbers) {
        List<OrderClientNumber> orderClientNumbers = new ArrayList<>();
        for (ResponseFavoriteNumber responseFavoriteNumber : responseFavoriteNumbers) {
            orderClientNumbers.add(new OrderClientNumber.Builder()
                    .name(responseFavoriteNumber.getAttributes().getLabel())
                    .clientNumber(responseFavoriteNumber.getAttributes().getClientNumber())
                    .lastUpdated(responseFavoriteNumber.getAttributes().getLastUpdated())
                    .lastProduct(responseFavoriteNumber.getAttributes().getLastProduct())
                    .categoryId(responseFavoriteNumber.getRelationships().getCategory().getData().getId())
                    .operatorId(responseFavoriteNumber.getRelationships().getOperator().getData().getId())
                    .build());
        }
        return orderClientNumbers;
    }
}
