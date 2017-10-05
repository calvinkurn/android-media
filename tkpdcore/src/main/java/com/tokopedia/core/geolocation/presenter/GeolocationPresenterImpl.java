package com.tokopedia.core.geolocation.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.fragment.GoogleMapFragment;
import com.tokopedia.core.geolocation.listener.GeolocationView;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;

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
        viewListener
                .inflateFragment(GoogleMapFragment.newInstance(generateGeolocation(bundle, uri)),
                        GoogleMapFragment.class.getSimpleName());
    }

    private LocationPass generateGeolocation(Bundle bundle, @SuppressWarnings("unused") Uri uri) {
        LocationPass locationPass = null;
        if (bundle != null) {
            locationPass = bundle.getParcelable(GeolocationActivity.EXTRA_EXISTING_LOCATION);
        }
        return locationPass;
    }
}
