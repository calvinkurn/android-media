package com.tokopedia.logisticgeolocation;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.logisticdata.data.entity.address.LocationPass;


/**
 * A simple {@link Fragment} subclass.
 */
public class GoogleMapFragment extends BaseDaggerFragment {

    private static final String ARGUMENT_GEOLOCATION_DATA = "ARG_GEO_DATA";
    private static final String ARGUMENT_INITIAL_STATE_BOOL = "ARG_INIT";
    private static final boolean HAS_LOCATION = true;

    public GoogleMapFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(LocationPass locationPass) {
        GoogleMapFragment fragment = new GoogleMapFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGUMENT_GEOLOCATION_DATA, locationPass);
        args.putBoolean(ARGUMENT_INITIAL_STATE_BOOL, HAS_LOCATION);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstanceNoLocation() {
        GoogleMapFragment fragment = new GoogleMapFragment();
        Bundle args = new Bundle();
        LocationPass emptyLocationPass = new LocationPass();
        args.putParcelable(ARGUMENT_GEOLOCATION_DATA, emptyLocationPass);
        args.putBoolean(ARGUMENT_INITIAL_STATE_BOOL, !HAS_LOCATION);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_google_map, container, false);
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return getClass().getSimpleName();
    }
}
