package com.tokopedia.logisticgeolocation.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.logisticdata.data.apiservice.MapsApi;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.Data;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.viewmodel.AutoCompleteViewModel;
import com.tokopedia.logisticdata.data.entity.geolocation.coordinate.CoordinateModel;
import com.tokopedia.logisticdata.data.entity.geolocation.coordinate.viewmodel.CoordinateViewModel;
import com.tokopedia.logisticgeolocation.di.GeolocationScope;
import com.tokopedia.logisticgeolocation.domain.IMapsMapper;
import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.network.utils.TKPDMapParam;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by Fajar Ulin Nuha on 30/10/18.
 */
@GeolocationScope
public class MapsRepository implements IMapsRepository {

    private IMapsMapper mapsMapper;
    private MapsApi mapsApi;

    @Inject
    public MapsRepository(MapsApi mapsApi, IMapsMapper mapsMapper) {
        this.mapsMapper = mapsMapper;
        this.mapsApi = mapsApi;
    }

    @Override
    public Observable<AutoCompleteViewModel> getAutoCompleteList(TKPDMapParam<String, Object> params,
                                                                 final String query) {
        return mapsApi.getRecommendedPlaces(params)
                .map(response -> {
                    handleError(response);
                    return mapsMapper.convertAutoCompleteModel(
                            response.body().convertDataObj(Data.class),
                            query
                    );
                });
    }

    @Override
    public Observable<CoordinateViewModel> getLatLng(TKPDMapParam<String, Object> params) {
        return mapsApi.getLatLng(params)
                .map(response -> {
                    handleError(response);
                    return mapsMapper
                            .convertAutoCompleteLocationId(response.body()
                                    .convertDataObj(CoordinateModel.class));
                });
    }

    @Override
    public Observable<CoordinateViewModel> getLatLngFromGeocode(TKPDMapParam<String, Object> params) {
        return mapsApi.getLatLngGeocode(params)
                .map(response -> {
                    handleError(response);
                    List<CoordinateModel> coordinateModels = response.body()
                            .convertDataList(CoordinateModel[].class);
                    return mapsMapper
                            .convertAutoCompleteLocationId(coordinateModels.get(0));
                });
    }

    private void handleError(Response<TokopediaWsV4Response> response) {
        if(response.body() == null) {
            throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_NULL_DATA);
        } else if(response.body().isError() && !response.body().getErrorMessageJoined().isEmpty()) {
            throw new RuntimeException(response.body().getErrorMessageJoined());
        } else if(response.body().isError() && !response.body().getErrorMessageJoined().isEmpty()) {
            throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }
}

