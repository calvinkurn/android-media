package com.tokopedia.core.geolocation.domain;


import com.tokopedia.core.geolocation.model.Data;
import com.tokopedia.core.geolocation.model.MapsResponse;
import com.tokopedia.core.network.apiservices.maps.MapService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by kris on 9/15/17. Tokopedia
 */

public interface IMapsRepository {

    Observable<Data> getAutoCompleteList(MapService service,
                                         TKPDMapParam<String, String> params);

}
