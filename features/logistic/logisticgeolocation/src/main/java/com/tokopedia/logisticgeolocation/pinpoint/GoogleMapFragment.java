package com.tokopedia.logisticgeolocation.pinpoint;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.logisticgeolocation.data.IMapsRepository;
import com.tokopedia.logisticgeolocation.R;
import com.tokopedia.logisticgeolocation.util.RequestPermissionUtil;
import com.tokopedia.logisticgeolocation.di.DaggerGeolocationComponent;
import com.tokopedia.logisticgeolocation.di.GeolocationModule;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;


/**
 * A Refactored fragment from core
 * Removed save instance mechanism due to behaviour provided with support fragment (still need to test)
 */
public class GoogleMapFragment extends BaseDaggerFragment implements
        GeolocationContract.GeolocationView,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<LocationSettingsResult>, OnMapReadyCallback {

    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final String ARGUMENT_GEOLOCATION_DATA = "ARG_GEO_DATA";
    private static final String ARGUMENT_INITIAL_STATE_BOOL = "ARG_INIT";
    private static final String STATE_MAPVIEW_SAVE_STATE = "STATE_MAPVIEW_SAVE_STATE";

    private static final boolean HAS_LOCATION = true;
    private ITransactionAnalyticsGeoLocationPinPoint analyticsGeoLocationListener;
    private GoogleMap googleMap;
    private LocationPass locationPass;
    private SuggestionLocationAdapter adapter;
    private BottomSheetDialog dialog;
    private ActionBar actionBar;
    private boolean hasLocation;

    MapView mapView;
    Toolbar toolbar;
    AutoCompleteTextView autoComplete;
    TextView textPointer;
    View submitPointer;
    FloatingActionButton fab;
    @Inject GeolocationContract.GeolocationPresenter presenter;
    @Inject UserSession mUser;

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
        mapView = view.findViewById(R.id.mapview);
        toolbar = view.findViewById(R.id.app_bar);
        autoComplete = view.findViewById(R.id.autocomplete);
        textPointer = view.findViewById(R.id.pointer_text);
        submitPointer = view.findViewById(R.id.pointer_submit);
        fab = view.findViewById(R.id.fab);

        submitPointer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onSubmitPointer(getActivity());
                analyticsGeoLocationListener.sendAnalyticsOnSetCurrentMarkerAsCurrentPosition();
            }
        });

        presenter.setUpVariables(locationPass, hasLocation);
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
            presenter.connectGoogleApi();
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
        presenter.disconnectGoogleApi();
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
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                dimissKeyBoard();
                presenter.onCameraChange(getActivity(), cameraPosition);
            }
        });

        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long itemID) {
                presenter.onSuggestionItemClick(adapterView, position);
                analyticsGeoLocationListener.sendAnalyticsOnDropdownSuggestionItemClicked();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
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
        presenter.prepareDetailDestination(view);
    }

    @Override
    public void initAutoCompleteAdapter(CompositeSubscription compositeSubscription, IMapsRepository repository, GoogleApiClient googleApiClient, LatLngBounds latLngBounds) {
        adapter = new SuggestionLocationAdapter(getActivity(), googleApiClient, latLngBounds,
                null, mUser, compositeSubscription, repository);
    }

    @Override
    public void setAutoCompleteAdaoter() {
        autoComplete.setAdapter(adapter);
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
        fab.setVisibility(View.GONE);
    }

    @Override
    public void showDetailDestination(View view) {
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public void setManualDestination(String s) {
        dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(R.layout.dialog_extra_google_map);
        TextView destination = (TextView) dialog.findViewById(R.id.text_address_destination);
        if (destination != null) {
            destination.setText(MethodChecker.fromHtml(s).toString());
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        presenter.onGoogleApiConnected(bundle);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        presenter.onGoogleApiSuspended(cause);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        presenter.onGoogleApiFailed(connectionResult);
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        presenter.onResult(locationSettingsResult);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        initMapView();
        initMapListener();

        //This to fulfill 4th condition when no coordinate, no district, and no GPS
        //TODO remove this if annoys UX or inefficient fending off GPS Error
        presenter.initDefaultLocation();

        presenter.onMapReady();
    }

    public void prepareAutoCompleteView() {
        presenter.prepareAutoCompleteView();
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
        if (getScreenRotation() == LANDSCAPE) {
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
}
