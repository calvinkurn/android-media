package com.tokopedia.core.geolocation.domain;


import com.tokopedia.core.geolocation.model.autocomplete.Data;
import com.tokopedia.core.geolocation.model.autocomplete.viewmodel.AutoCompleteViewModel;
import com.tokopedia.core.geolocation.model.coordinate.CoordinateModel;
import com.tokopedia.core.geolocation.model.coordinate.viewmodel.CoordinateViewModel;
import com.tokopedia.core.network.apiservices.maps.MapService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;

/**
 * Created by kris on 9/15/17. Tokopedia
 */

public interface IMapsRepository {

    Observable<AutoCompleteViewModel> getAutoCompleteList(MapService service,
                                                          TKPDMapParam<String, Object> params,
                                                          String query);

    Observable<CoordinateViewModel> getLatLng(MapService service,
                                              TKPDMapParam<String, Object> params);

}
