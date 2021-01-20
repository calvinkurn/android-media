package com.tokopedia.logisticaddaddress.data;

import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.viewmodel.AutoCompleteViewModel;
import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.viewmodel.CoordinateViewModel;
import com.tokopedia.network.utils.TKPDMapParam;

import rx.Observable;

/**
 * Created by kris on 9/15/17. Tokopedia
 */

public interface IMapsRepository {

    Observable<AutoCompleteViewModel> getAutoCompleteList(TKPDMapParam<String, Object> params,
                                                          String query);

    Observable<CoordinateViewModel> getLatLng(TKPDMapParam<String, Object> params);

    Observable<CoordinateViewModel> getLatLngFromGeocode(TKPDMapParam<String, Object> params);
}
