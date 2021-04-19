package com.tokopedia.logisticaddaddress.domain;

import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.Data;
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.uimodel.AutoCompleteUiModel;
import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.CoordinateModel;
import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.uimodel.CoordinateUiModel;

/**
 * Created by Fajar Ulin Nuha on 30/10/18.
 */
public interface IMapsMapper {

    AutoCompleteUiModel convertAutoCompleteModel(Data autoCompleteData, String query);

    CoordinateUiModel convertAutoCompleteLocationId(CoordinateModel coordinateModel);

}
