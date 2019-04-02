package com.tokopedia.logisticgeolocation.domain;

import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.Data;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.viewmodel.AutoCompleteViewModel;
import com.tokopedia.logisticdata.data.entity.geolocation.coordinate.CoordinateModel;
import com.tokopedia.logisticdata.data.entity.geolocation.coordinate.viewmodel.CoordinateViewModel;

/**
 * Created by Fajar Ulin Nuha on 30/10/18.
 */
public interface IMapsMapper {

    AutoCompleteViewModel convertAutoCompleteModel(Data autoCompleteData, String query);

    CoordinateViewModel convertAutoCompleteLocationId(CoordinateModel coordinateModel);

}
