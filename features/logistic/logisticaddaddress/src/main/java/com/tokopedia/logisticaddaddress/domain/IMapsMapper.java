package com.tokopedia.logisticaddaddress.domain;

import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.Data;
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.viewmodel.AutoCompleteViewModel;
import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.CoordinateModel;
import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.viewmodel.CoordinateViewModel;

/**
 * Created by Fajar Ulin Nuha on 30/10/18.
 */
public interface IMapsMapper {

    AutoCompleteViewModel convertAutoCompleteModel(Data autoCompleteData, String query);

    CoordinateViewModel convertAutoCompleteLocationId(CoordinateModel coordinateModel);

}
