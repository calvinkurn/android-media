package com.tokopedia.digital.widget.data.mapper;

import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.widget.data.entity.response.ResponseFavoriteNumber;
import com.tokopedia.digital.widget.data.entity.response.ResponseNumberList;

import java.util.List;

/**
 * @author rizkyfadillah on 10/2/2017.
 */

public interface IFavoriteNumberMapper {

    List<OrderClientNumber> transformDigitalFavoriteNumberItemDataList(List<ResponseFavoriteNumber> responseFavoriteNumbers);

}
