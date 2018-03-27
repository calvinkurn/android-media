package com.tokopedia.core.geolocation.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

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
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.geolocation.adapter.SuggestionLocationAdapter;
import com.tokopedia.core.geolocation.domain.IMapsRepository;
import com.tokopedia.core.geolocation.listener.GoogleMapView;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.geolocation.presenter.GoogleMapPresenter;
import com.tokopedia.core.geolocation.presenter.GoogleMapPresenterImpl;
import com.tokopedia.core.network.apiservices.maps.MapService;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created on 1/29/16.
 */
public class GoogleMapFragment extends BasePresenterFragment<GoogleMapPresenter> implements
        GoogleMapView,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<LocationSettingsResult>, OnMapReadyCallback {

    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final boolean HAS_LOCATION = true;
    private static final boolean NO_LOCATION = false;
    private static final String TAG = GoogleMapFragment.class.getSimpleName();
    private static final String ARG_PARAM_GEOLOCATION_PASS_DATA = "ARG_PARAM_GEOLOCATION_PASS_DATA";
    private static final String STATE_MAPVIEW_SAVE_STATE = "STATE_MAPVIEW_SAVE_STATE";
    private static final String STATE_LOCATION_INITIATED = "STATE_LOCATION_INITIATED";

    @BindView(R2.id.mapview)
    MapView mapView;
    @BindView(R2.id.app_bar)
    Toolbar toolbar;
    @BindView(R2.id.autocomplete)
    AutoCompleteTextView autoComplete;
    @BindView(R2.id.pointer_text)
    TextView textPointer;
    @BindView(R2.id.pointer_submit)
    View submitPointer;
    @BindView(R2.id.fab)
    FloatingActionButton fab;

    private LocationPass locationPass;
    private boolean hasLocation;
    private GoogleMap googleMap;
    private SuggestionLocationAdapter adapter;
    private ActionBar actionBar;
    private BottomSheetDialog dialog;

    public static Fragment newInstance(LocationPass locationPass) {
        GoogleMapFragment fragment = new GoogleMapFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_GEOLOCATION_PASS_DATA, locationPass);
        args.putBoolean(STATE_LOCATION_INITIATED, HAS_LOCATION);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstanceNoLocation() {
        GoogleMapFragment fragment = new GoogleMapFragment();
        Bundle args = new Bundle();
        LocationPass emptyLocationPass = new LocationPass();
        args.putParcelable(ARG_PARAM_GEOLOCATION_PASS_DATA, emptyLocationPass);
        args.putBoolean(STATE_LOCATION_INITIATED, NO_LOCATION);
        fragment.setArguments(args);
        return fragment;
    }

    public GoogleMapFragment() {
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
    }

    @Override
    public void onSaveState(Bundle state) {
        presenter.saveStateCurrentLocation(state);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        presenter.restoreStateData(savedState);
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        this.presenter = new GoogleMapPresenterImpl(
                getActivity(),
                this,
                locationPass,
                hasLocation
        );
    }

    @Override
    protected void initialListener(Activity activity) {
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        locationPass = arguments.getParcelable(ARG_PARAM_GEOLOCATION_PASS_DATA);
        hasLocation = arguments.getBoolean(STATE_LOCATION_INITIATED);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_map_v2;
    }

    @Override
    protected void initView(View view) {
        initActionBarView();
        prepareActionBarView();
        prepareAutoCompleteView();
        prepareDetailDestination(view);
    }

    private void initActionBarView() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void prepareActionBarView() {
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
        }
    }

    @Override
    public void prepareDetailDestination(View view) {
        presenter.prepareDetailDestination(view);
    }

    public void prepareAutoCompleteView() {
        presenter.prepareAutoCompleteView();
    }

    @Override
    public void initAutoCompleteAdapter(CompositeSubscription compositeSubscription,
                                        MapService service,
                                        IMapsRepository repository,
                                        GoogleApiClient googleApiClient, LatLngBounds latLngBounds) {
        adapter = new SuggestionLocationAdapter(getActivity(), googleApiClient, latLngBounds,
                null, service, compositeSubscription, repository);
    }

    @Override
    public void setAutoCompleteAdaoter() {
        autoComplete.setAdapter(adapter);
    }


    @Override
    public void setToolbarMap() {
        googleMap.getUiSettings().setMapToolbarEnabled(false);
    }

    @Override
    public void setMyLocButton() {
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    protected void setViewListener() {
        submitPointer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onSubmitPointer(getActivity());
            }
        });
    }

    @Override
    protected void initialVar() {
    }

    @Override
    protected void setActionVar() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }

    private void dimissKeyBoard() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
    }

    @Override
    public void initMapView() {
        setToolbarMap();
        setMyLocButton();
        MapsInitializer.initialize(getActivity());
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

    private void restartActivity() {
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        final Bundle mapViewSaveState = new Bundle(outState);
        mapView.onSaveInstanceState(mapViewSaveState);
        outState.putBundle(STATE_MAPVIEW_SAVE_STATE, mapViewSaveState);
        super.onSaveInstanceState(outState);
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
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");
        presenter.onGoogleApiConnected(bundle);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "onConnectionSuspended");
        presenter.onGoogleApiSuspended(cause);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
        presenter.onGoogleApiFailed(connectionResult);
    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        Log.d(TAG, "onResult");
        presenter.onResult(locationSettingsResult);
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
    public void toastMessage(String s) {
        CommonUtils.UniversalToast(getActivity(), s);
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
    public void generateBoundsFromCamera() {
        setAutoCompleteBounds(googleMap.getProjection().getVisibleRegion().latLngBounds);
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
    public void hideDetailDestination() {
        fab.setVisibility(View.GONE);
    }

    @Override
    public void showDetailDestination(View rootView) {
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public void setManualDestination(String s) {
        dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(R.layout.layout_extra_google_map);
        TextView destination = (TextView) dialog.findViewById(R.id.text_address_destination);
        if (destination != null) {
            destination.setText(MethodChecker.fromHtml(s).toString());
        }
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
}
