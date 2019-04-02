package com.tokopedia.core.geolocation.presenter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.fragment.GoogleMapFragment;
import com.tokopedia.core.geolocation.interactor.RetrofitInteractor;
import com.tokopedia.core.geolocation.interactor.RetrofitInteractorImpl;
import com.tokopedia.core.geolocation.listener.GeolocationView;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.geolocation.model.coordinate.viewmodel.CoordinateViewModel;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.HashMap;


/**
 * Created by hangnadi on 1/29/16.
 */
public class GeolocationPresenterImpl implements GeolocationPresenter {

    private final GeolocationView viewListener;

    public GeolocationPresenterImpl(GeolocationView viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void initFragment(@NonNull Context context, Uri uri, Bundle bundle) {
        LocationPass locationPass = bundle.getParcelable(GeolocationActivity.EXTRA_EXISTING_LOCATION);
//                unBundleLocationMap(
//                        (HashMap<String, String>) bundle.getSerializable(GeolocationActivity.EXTRA_HASH_LOCATION)
//                );

        if (locationPass != null) {
            if (locationPass.getLatitude() != null && !locationPass.getLatitude().isEmpty())
                viewListener
                        .inflateFragment(GoogleMapFragment.newInstance(locationPass),
                                GoogleMapFragment.class.getSimpleName());
            else {
                RetrofitInteractor interactor = new RetrofitInteractorImpl();
                TKPDMapParam<String,String> params = new TKPDMapParam<>();
                params.put("address", locationPass.getDistrictName()
                        + ", "
                        + locationPass.getCityName());
                interactor.generateLatLngGeoCode(context,
                        AuthUtil.generateParamsNetwork(context, params),
                        latLongListener(context, locationPass));
            }

        } else {
            viewListener
                    .inflateFragment(GoogleMapFragment.newInstanceNoLocation(),
                            GoogleMapFragment.class.getSimpleName());
        }
    }

    private RetrofitInteractor.GenerateLatLongListener latLongListener(
            final Context context,
            final LocationPass locationPass
    ) {
        return new RetrofitInteractor.GenerateLatLongListener() {
            @Override
            public void onSuccess(CoordinateViewModel model) {
                locationPass
                        .setLatitude(
                                String.valueOf(model.getCoordinate().latitude)
                        );
                locationPass
                        .setLongitude(
                                String.valueOf(model.getCoordinate().longitude)
                        );
                viewListener
                        .inflateFragment(
                                GoogleMapFragment.newInstance(locationPass),
                                GoogleMapFragment.class.getSimpleName()
                        );
            }

            @Override
            public void onError(String errorMessage) {
                NetworkErrorHelper.showSnackbar((Activity) context, errorMessage);
            }
        };
    }
}
