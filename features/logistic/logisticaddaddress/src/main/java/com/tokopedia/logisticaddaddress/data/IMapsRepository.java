package com.tokopedia.logisticaddaddress.data;

import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.uimodel.AutoCompleteUiModel;
import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.uimodel.CoordinateUiModel;
import com.tokopedia.network.utils.TKPDMapParam;

import rx.Observable;

/**
 * Created by kris on 9/15/17. Tokopedia
 */

public interface IMapsRepository {

    Observable<AutoCompleteUiModel> getAutoCompleteList(TKPDMapParam<String, Object> params,
                                                        String query);

    Observable<CoordinateUiModel> getLatLng(TKPDMapParam<String, Object> params);

    Observable<CoordinateUiModel> getLatLngFromGeocode(TKPDMapParam<String, Object> params);
}
