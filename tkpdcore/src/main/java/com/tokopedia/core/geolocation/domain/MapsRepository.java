package com.tokopedia.core.geolocation.domain;



import com.tokopedia.core.geolocation.model.Data;
import com.tokopedia.core.geolocation.model.MapsResponse;
import com.tokopedia.core.network.apiservices.maps.MapService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 9/15/17. Tokopedia
 */

public class MapsRepository implements IMapsRepository {

    @Override
    public Observable<Data> getAutoCompleteList(MapService service, TKPDMapParam<String, String> params) {
        return service.getApi().getRecommendedPlaces(params)
                .map(new Func1<Response<TkpdResponse>, Data>() {
            @Override
            public Data call(Response<TkpdResponse> response) {
                return response.body().convertDataObj(Data.class);
            }
        });
    }
}
