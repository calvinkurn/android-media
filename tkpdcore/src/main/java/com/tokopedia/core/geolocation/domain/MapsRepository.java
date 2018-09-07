package com.tokopedia.core.geolocation.domain;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.core.geolocation.model.autocomplete.Data;
import com.tokopedia.core.geolocation.model.autocomplete.viewmodel.AutoCompleteViewModel;
import com.tokopedia.core.geolocation.model.coordinate.CoordinateModel;
import com.tokopedia.core.geolocation.model.coordinate.viewmodel.CoordinateViewModel;
import com.tokopedia.core.network.apiservices.maps.MapService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.Arrays;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 9/15/17. Tokopedia
 */

public class MapsRepository implements IMapsRepository {

    private IMapsMapper mapsMapper = new MapsMapper();

    public MapsRepository() {
        mapsMapper = new MapsMapper();
    }

    @Override
    public Observable<AutoCompleteViewModel> getAutoCompleteList(MapService service,
                                                                 TKPDMapParam<String, Object> params,
                                                                 final String query) {
        return service.getApi().getRecommendedPlaces(params)
                .map(new Func1<Response<TkpdResponse>, AutoCompleteViewModel>() {
            @Override
            public AutoCompleteViewModel call(Response<TkpdResponse> response) {
                handleError(response);
                return mapsMapper.convertAutoCompleteModel(
                        response.body().convertDataObj(Data.class),
                        query
                );
            }
        });
    }

    @Override
    public Observable<CoordinateViewModel> getLatLng(MapService service,
                                                     TKPDMapParam<String, Object> params) {
        return service.getApi().getLatLng(params)
                .map(new Func1<Response<TkpdResponse>, CoordinateViewModel>() {
            @Override
            public CoordinateViewModel call(Response<TkpdResponse> response) {
                handleError(response);
                return mapsMapper
                        .convertAutoCompleteLocationId(response.body()
                                .convertDataObj(CoordinateModel.class));
            }
        });
    }

    @Override
    public Observable<CoordinateViewModel> getLatLngFromGeocode(MapService service, TKPDMapParam<String, Object> params) {
        return service.getApi().getLatLngGeocode(params)
                .map(new Func1<Response<TkpdResponse>, CoordinateViewModel>() {
                    @Override
                    public CoordinateViewModel call(Response<TkpdResponse> response) {
                        handleError(response);
                        Gson gsonData = new GsonBuilder().disableHtmlEscaping()
                                .setPrettyPrinting().create();

//                        List<CoordinateModel> coordinateModels = Arrays
//                                .asList(gsonData.fromJson(
//                                        response.body().getStringData(), CoordinateModel[].class)
//                                );
                        List<CoordinateModel> coordinateModels = response.body()
                                .convertDataList(CoordinateModel[].class);
                        return mapsMapper
                                .convertAutoCompleteLocationId(coordinateModels.get(0));
                    }
                });
    }

    private void handleError(Response<TkpdResponse> response) {
        if(response.body() == null) {
            throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_NULL_DATA);
        } else if(response.body().isError() && !response.body().getErrorMessageJoined().isEmpty()) {
            throw new RuntimeException(response.body().getErrorMessageJoined());
        } else if(response.body().isError() && !response.body().getErrorMessageJoined().isEmpty()) {
            throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }
}
