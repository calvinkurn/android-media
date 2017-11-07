package com.tokopedia.digital.widget.data.mapper;

import com.tokopedia.digital.widget.data.entity.response.ResponseFavoriteList;
import com.tokopedia.digital.widget.model.DigitalNumberList;

/**
 * @author rizkyfadillah on 10/2/2017.
 */

public interface IFavoriteNumberMapper {

    DigitalNumberList transformDigitalFavoriteNumberItemDataList(ResponseFavoriteList responseFavoriteNumbers);

}
