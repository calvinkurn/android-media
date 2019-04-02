package com.tokopedia.core.geolocation.domain;

import com.tokopedia.core.geolocation.model.autocomplete.Data;
import com.tokopedia.core.geolocation.model.autocomplete.viewmodel.AutoCompleteViewModel;
import com.tokopedia.core.geolocation.model.coordinate.CoordinateModel;
import com.tokopedia.core.geolocation.model.coordinate.viewmodel.CoordinateViewModel;

/**
 * Created by kris on 9/18/17. Tokopedia
 */

public interface IMapsMapper {

    AutoCompleteViewModel convertAutoCompleteModel(Data autoCompleteData, String query);

    CoordinateViewModel convertAutoCompleteLocationId(CoordinateModel coordinateModel);

}
