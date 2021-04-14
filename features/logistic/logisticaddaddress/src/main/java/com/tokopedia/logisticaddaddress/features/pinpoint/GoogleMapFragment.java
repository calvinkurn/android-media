package com.tokopedia.logisticaddaddress.features.pinpoint;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.logisticCommon.data.constant.LogisticConstant;
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.uimodel.PredictionResult;
import com.tokopedia.logisticCommon.data.utils.GeoLocationUtils;
import com.tokopedia.logisticCommon.util.LocationHelperKt;
import com.tokopedia.logisticaddaddress.R;
import com.tokopedia.logisticaddaddress.common.AddressConstants;
import com.tokopedia.logisticaddaddress.data.RetrofitInteractor;
import com.tokopedia.logisticaddaddress.di.DaggerGeolocationComponent;
import com.tokopedia.logisticaddaddress.di.GeolocationModule;
import com.tokopedia.logisticaddaddress.utils.LocationCache;
import com.tokopedia.logisticaddaddress.utils.RequestPermissionUtil;
import com.tokopedia.unifycomponents.BottomSheetUnify;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * A Refactored fragment from core
 * Removed save instance mechanism due to behaviour provided with support fragment (still need to test)
 */
public class GoogleMapFragment extends BaseDaggerFragment implements
        GeolocationContract.GeolocationView,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<LocationSettingsResult>, OnMapReadyCallback, LocationListener {

    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final String ARGUMENT_GEOLOCATION_DATA = "ARG_GEO_DATA";
    private static final String ARGUMENT_INITIAL_STATE_BOOL = "ARG_INIT";
    private static final String STATE_MAPVIEW_SAVE_STATE = "STATE_MAPVIEW_SAVE_STATE";

    private static final boolean HAS_LOCATION = true;
    private ITransactionAnalyticsGeoLocationPinPoint analyticsGeoLocationListener;
    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationPass locationPass;
    private SuggestionLocationAdapter adapter;
    private BottomSheetUnify bottomSheetUnify;
    private ActionBar actionBar;
    private boolean hasLocation;
    private CompositeSubscription composite = new CompositeSubscription();

    MapView mapView;
    Toolbar toolbar;
    AutoCompleteTextView autoComplete;
    TextView textPointer;
    View submitPointer;
    FloatingActionButton fab;
    @Inject
    GeolocationContract.GeolocationPresenter presenter;
    @Inject
    UserSession mUser;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            setupArguments(getArguments());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        analyticsGeoLocationListener = (ITransactionAnalyticsGeoLocationPinPoint) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_google_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.locationRequest = LocationRequest.create()
                .setInterval(DEFAULT_UPDATE_INTERVAL_IN_MILLISECONDS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        this.googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mapView = view.findViewById(R.id.mapview);
        toolbar = view.findViewById(R.id.app_bar);
        autoComplete = view.findViewById(R.id.autocomplete);
        textPointer = view.findViewById(R.id.pointer_text);
        submitPointer = view.findViewById(R.id.pointer_submit);
        fab = view.findViewById(R.id.fab);

        submitPointer.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            intent.putExtra(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass);
            if (getActivity() != null) {
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
            analyticsGeoLocationListener.sendAnalyticsOnSetCurrentMarkerAsCurrentPosition();
        });

        if (hasLocation) saveLocationToCache();

        initActionBarView();
        prepareActionBarView();
        prepareAutoCompleteView();
        prepareDetailDestination(view);

        if (!RequestPermissionUtil.checkHasPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            restartActivity();
        } else {
            final Bundle mapViewSaveState = savedInstanceState != null ? savedInstanceState.getBundle(STATE_MAPVIEW_SAVE_STATE) : null;
            mapView.onCreate(mapViewSaveState);
            mapView.getMapAsync(this);
            this.googleApiClient.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!RequestPermissionUtil.checkHasPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            restartActivity();
        } else {
            mapView.onResume();
            setOnScreenRotate();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroyView() {
        /**
         * Must be called before onDestroyView();
         * or to be exactly before Butterknife.unBind();
         */
        if (RequestPermissionUtil.checkHasPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            mapView.onDestroy();
        }
        composite.unsubscribe();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void initInjector() {
        BaseAppComponent appComponent = ((BaseMainApplication) getActivity()
                .getApplication()).getBaseAppComponent();
        DaggerGeolocationComponent.builder()
                .baseAppComponent(appComponent)
                .geolocationModule(new GeolocationModule(getActivity(), this))
                .build().inject(this);
    }

    @Override
    protected String getScreenName() {
        return getClass().getSimpleName();
    }

    @Override
    public void prepareActionBarView() {
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
        }
    }

    @Override
    public void setToolbarMap() {
        googleMap.getUiSettings().setMapToolbarEnabled(false);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void setMyLocButton() {
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void initMapListener() {
        composite.add(LocationHelperKt.rxPinPoint(googleMap)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        dimissKeyBoard();
                        LatLng temp = googleMap.getCameraPosition().target;
                        presenter.getReverseGeoCoding(
                                String.valueOf(temp.latitude), String.valueOf(temp.longitude));
                    }
                }));

        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long itemID) {
                final PredictionResult item = (PredictionResult) adapterView.getItemAtPosition(position);
                presenter.geoCode(item.getPlaceId());
                analyticsGeoLocationListener.sendAnalyticsOnDropdownSuggestionItemClicked();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager() != null) {
                    bottomSheetUnify.show(getFragmentManager(), "E-gold more info bottom sheet");
                }
                analyticsGeoLocationListener.sendAnalyticsOnGetCurrentLocationClicked();
            }
        });
    }

    @Override
    public void initMapView() {
        setToolbarMap();
        setMyLocButton();
        MapsInitializer.initialize(getActivity());
    }

    @Override
    public void moveMap(LatLng latLng) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(15)
                .bearing(0)
                .build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void checkLocationSettings(PendingResult<LocationSettingsResult> result) {
        result.setResultCallback(this);
    }

    @Override
    public void showDialogError(Status status) {
        try {
            // Show the dialog by calling startResolutionForResult(),
            // and check the result in onActivityResult().
            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void prepareDetailDestination(View view) {
        if (hasLocation) {
            if (locationPass.getManualAddress() == null) {
                hideDetailDestination();
            } else {
                showDetailDestination(view);
                setManualDestination(locationPass.getManualAddress());
            }
        } else {
            hideDetailDestination();
        }
    }

    @Override
    public void toastMessage(String s) {
        Toast.makeText(getContext(), MethodChecker.fromHtml(s), Toast.LENGTH_LONG).show();
    }

    @Override
    public void setValuePointer(String s) {
        textPointer.setText(s);
    }

    @Override
    public void setAutoCompleteBounds(LatLngBounds bounds) {
        adapter.setBounds(bounds);
    }

    @Override
    public void hideActionBar() {
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    public void showActionBar() {
        if (actionBar != null) {
            actionBar.show();
        }
    }

    @Override
    public void generateBoundsFromCamera() {
        setAutoCompleteBounds(googleMap.getProjection().getVisibleRegion().latLngBounds);
    }

    @Override
    public void hideDetailDestination() {
        fab.hide();
    }

    @Override
    public void showDetailDestination(View view) {
        fab.show();
    }

    @Override
    public void setManualDestination(String s) {
        View child = View.inflate(getContext(), R.layout.dialog_extra_google_map, null);
        TextView destination = child.findViewById(R.id.text_address_destination);
        if (destination != null) {
            destination.setText(MethodChecker.fromHtml(s).toString());
        }
        bottomSheetUnify = new BottomSheetUnify();
        bottomSheetUnify.setChild(child);
    }

    @Override
    public void setLoading(boolean active) {
        if (active) {
            textPointer.setText(getString(R.string.wait_generate_address));
        }
    }

    @Override
    public void setNewLocationPass(LocationPass locationPass) {
        this.locationPass = locationPass;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // no op
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // no op
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // no op
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                moveMap(getLastLocation());
                requestLocationUpdate();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                showDialogError(status);
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                break;
            default:
                break;
        }
    }

    @SuppressLint("MissingPermission")
    private LatLng getLastLocation() {
        try {
            if (isServiceConnected()) {
                Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                LocationCache.saveLocation(getContext(), location);
                return new LatLng(location.getLatitude(), location.getLongitude());
            } else {
                return DEFAULT_LATLNG_JAKARTA;
            }
        } catch (Exception e) {
            return DEFAULT_LATLNG_JAKARTA;
        }
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdate() {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    private boolean isServiceConnected() {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();

        int resultCode = availability.isGooglePlayServicesAvailable(getContext());

        if (ConnectionResult.SUCCESS == resultCode) {
            Timber.d("Google play services available");
            return true;
        } else {
            Timber.d("Google play services unavailable");
            return false;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        moveMap(GeoLocationUtils.generateLatLng(location.getLatitude(), location.getLongitude()));
        LocationCache.saveLocation(getContext(), location);
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        initMapView();
        initMapListener();

        LatLng defLatLng = new LatLng(AddressConstants.DEFAULT_LAT, AddressConstants.DEFAULT_LONG);
        moveMap(defLatLng);
        if (!hasLocation) {
            LocationSettingsRequest.Builder locationSettingsRequest = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            locationSettingsRequest.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi
                            .checkLocationSettings(googleApiClient, locationSettingsRequest.build());

            checkLocationSettings(result);
        } else {
            moveMap(GeoLocationUtils.generateLatLng(locationPass.getLatitude(), locationPass.getLongitude()));
        }
    }

    public void prepareAutoCompleteView() {
        RetrofitInteractor interactor = presenter.getInteractor();
        LatLngBounds bounds = GeoLocationUtils.generateBoundary(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
        adapter = new SuggestionLocationAdapter(getActivity(), googleApiClient, bounds,
                null, mUser, interactor.getCompositeSubscription(), interactor.getMapRepository());
        autoComplete.setAdapter(adapter);
    }

    private void dimissKeyBoard() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
    }

    private void setupArguments(Bundle arguments) {
        locationPass = arguments.getParcelable(ARGUMENT_GEOLOCATION_DATA);
        hasLocation = arguments.getBoolean(ARGUMENT_INITIAL_STATE_BOOL);
    }

    private void restartActivity() {
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }

    private void setOnScreenRotate() {
        if (getScreenRotation() == Configuration.ORIENTATION_LANDSCAPE) {
            hideActionBar();
        } else {
            showActionBar();
        }
    }

    private int getScreenRotation() {
        return getActivity().getResources().getConfiguration().orientation;
    }

    // shoud be with contract ?
    private void initActionBarView() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    private void saveLocationToCache() {
        Location location = new Location(LocationManager.NETWORK_PROVIDER);
        location.setLatitude(Double.parseDouble(locationPass.getLatitude()));
        location.setLongitude(Double.parseDouble(locationPass.getLongitude()));
        LocationCache.saveLocation(getContext(), location);
    }
}
