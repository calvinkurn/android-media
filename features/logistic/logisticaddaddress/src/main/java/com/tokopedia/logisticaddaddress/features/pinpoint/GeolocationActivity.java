package com.tokopedia.logisticaddaddress.features.pinpoint;

import static com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_LOCATION_PASS;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;
import android.widget.Toast;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.logisticCommon.util.MapsAvailabilityHelper;
import com.tokopedia.logisticaddaddress.features.pinpoint.webview.PinpointWebviewActivity;
import com.tokopedia.network.authentication.AuthHelper;
import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.uimodel.CoordinateUiModel;
import com.tokopedia.logisticaddaddress.R;
import com.tokopedia.logisticaddaddress.data.RetrofitInteractor;
import com.tokopedia.logisticaddaddress.di.GeolocationModule;
import com.tokopedia.logisticaddaddress.di.DaggerGeolocationComponent;
import com.tokopedia.logisticCommon.data.constant.LogisticConstant;
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.utils.permission.PermissionCheckerHelper;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.user.session.UserSession;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class GeolocationActivity extends BaseActivity implements ITransactionAnalyticsGeoLocationPinPoint {

    private static final String TAG_FRAGMENT = "TAG_FRAGMENT";
    public static final String EXTRA_IS_FROM_MARKETPLACE_CART = "EXTRA_IS_FROM_MARKETPLACE_CART";
    public static final String SCREEN_ADDRESS_GEOLOCATION = "Add Geolocation Address page";
    private static final String permission = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int REQUEST_CODE_LITE_PINPOINT = 1986;

    private Bundle mBundle;
    private boolean isFromMarketPlace = false;
    private CheckoutAnalyticsChangeAddress checkoutAnalyticsChangeAddress;
    private PermissionCheckerHelper permissionCheckerHelper = new PermissionCheckerHelper();
    @Inject
    RetrofitInteractor mRepository;
    @Inject
    UserSession mUser;

    /**
     * Usage = DistrictRecommendationAddress // DistrictRecommendationAddress Tx // Shipment // ShopOpen // Seller
     *
     * @param locationPass          please get from common module logistic_data
     * @param isFromMarketPlaceCart true if you are from marketplace cart
     * @return intent
     */
    public static Intent createInstance(@NonNull Context context, LocationPass locationPass,
                                        boolean isFromMarketPlaceCart) {
        Intent intent = new Intent(context, GeolocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass);
        bundle.putBoolean(EXTRA_IS_FROM_MARKETPLACE_CART, isFromMarketPlaceCart);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geolocation);
        if (getApplication() instanceof AbstractionRouter) {
            checkoutAnalyticsChangeAddress =
                    new CheckoutAnalyticsChangeAddress();
        }
        mBundle = getIntent().getExtras();
        LocationPass locationPass = null;
        if (mBundle != null) {
            isFromMarketPlace = mBundle.getBoolean(EXTRA_IS_FROM_MARKETPLACE_CART, false);
            locationPass = mBundle.getParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION);
        }

        BaseAppComponent appComponent =
                ((BaseMainApplication) getApplication()).getBaseAppComponent();
        DaggerGeolocationComponent.builder().baseAppComponent(appComponent)
                .geolocationModule(new GeolocationModule(this, null))
                .build()
                .inject(this);

        checkMapsAvailability(locationPass);
    }

    private void checkMapsAvailability(LocationPass locationPass) {
        if (MapsAvailabilityHelper.INSTANCE.isMapsAvailable(this)) {
            onMapsAvailable(locationPass);
        } else {
            onMapsNotAvailable(locationPass);
        }
    }

    private void onMapsAvailable(LocationPass locationPass) {
        permissionCheckerHelper.checkPermission(this, permission, new PermissionCheckerHelper.PermissionCheckListener() {
            @Override
            public void onPermissionDenied(@NotNull String permissionText) {
                Toast.makeText(GeolocationActivity.this, R.string.permission_location_denied, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onNeverAskAgain(@NotNull String permissionText) {
                Toast.makeText(GeolocationActivity.this, R.string.permission_location_neverask, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onPermissionGranted() {
                inflateFragment(locationPass);
            }
        }, getString(R.string.need_permission_location));
    }

    private void onMapsNotAvailable(LocationPass locationPass) {
        if (locationPass != null) {
            if (locationPass.hasPinpointData()) {
                // goto pinpoint page
                goToLitePinpoint(locationPass);
            } else {
                getLatLngGeoCode(locationPass, false);
            }
        } else {
            // goto pinpoint page with current loc
            goToLitePinpoint(locationPass);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendAnalyticsOnBackPressClicked();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getScreenName() {
        return SCREEN_ADDRESS_GEOLOCATION;
    }

    public void inflateFragment(LocationPass locationPass) {
        Fragment fragment = null;

        if (locationPass != null) {
            if (locationPass.hasPinpointData()) {
                fragment = GoogleMapFragment.newInstance(locationPass);
            } else {
                getLatLngGeoCode(locationPass, true);
            }
        } else {
            fragment = GoogleMapFragment.newInstanceNoLocation();
        }
        if (fragment != null) getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, TAG_FRAGMENT)
                .commit();
    }

    private void getLatLngGeoCode(@NonNull LocationPass locationPass, boolean mapsAvailable) {
        Map<String, String> params = new HashMap<>();
        params.put("address", locationPass.getDistrictName()
                + ", "
                + locationPass.getCityName());
        params = AuthHelper.generateParamsNetwork(mUser.getUserId(), mUser.getDeviceId(), params);
        mRepository.generateLatLngGeoCode(
                params,
                latLongListener(this, locationPass, mapsAvailable)
        );
    }


    @Override
    public void sendAnalyticsOnDropdownSuggestionItemClicked() {
        if (isFromMarketPlace)
            checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickDropdownSuggestionTandaiLokasiPadaTambahAddress();
    }

    @Override
    public void sendAnalyticsOnSetCurrentMarkerAsCurrentPosition() {
        if (isFromMarketPlace)
            checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickVTandaiLokasiPadaTambahAddress();
    }

    @Override
    public void sendAnalyticsOnBackPressClicked() {
        if (isFromMarketPlace)
            checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickBackArrowTandaiLokasiPadaTambahAddress();
    }

    @Override
    public void sendAnalyticsOnGetCurrentLocationClicked() {
        if (isFromMarketPlace)
            checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickPinButtonFromTandaiLokasi();
    }

    @Override
    public void sendAnalyticsOnViewErrorSetPinPointLocation(String errorMessage) {
        if (isFromMarketPlace)
            checkoutAnalyticsChangeAddress.eventViewShippingCartChangeAddressViewValidationErrorTandaiLokasi(errorMessage);
    }

    private RetrofitInteractor.GenerateLatLongListener latLongListener(
            final Context context,
            final LocationPass locationPass,
            final boolean mapsAvailable
    ) {
        return new RetrofitInteractor.GenerateLatLongListener() {
            @Override
            public void onSuccess(CoordinateUiModel model) {
                locationPass.setLatitude(
                        String.valueOf(model.getCoordinate().latitude)
                );
                locationPass.setLongitude(
                        String.valueOf(model.getCoordinate().longitude)
                );
                if (mapsAvailable) {
                    inflateFragment(locationPass);
                } else {
                    goToLitePinpoint(locationPass);
                }
            }

            @Override
            public void onError(String errorMessage) {
                NetworkErrorHelper.showSnackbar((Activity) context, errorMessage);
            }
        };
    }

    private void goToLitePinpoint(LocationPass locationPass) {
        Intent intent;
        if (locationPass != null) {
            intent = PinpointWebviewActivity.Companion.getIntent(this, null, toDoubleOrNull(locationPass.getLatitude()), toDoubleOrNull(locationPass.getLongitude()), !locationPass.hasPinpointData(), locationPass, null, null);
        } else {
            intent = PinpointWebviewActivity.Companion.getIntent(this, null, null, null, true, null, null, null);
        }
        startActivityForResult(intent, REQUEST_CODE_LITE_PINPOINT);
    }

    private Double toDoubleOrNull(String s) {
        try {
            if (s != null) {
                return Double.parseDouble(s);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LITE_PINPOINT) {
            handleLitePinpointResult(resultCode, data);
        }
    }

    private void handleLitePinpointResult(int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (data != null) {
                LocationPass locationPass = data.getParcelableExtra(KEY_LOCATION_PASS);
                if (locationPass != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.putExtra(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass);
                    setResult(Activity.RESULT_OK, intent);
                }
                finish();
            }
        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheckerHelper.onRequestPermissionsResult(GeolocationActivity.this, requestCode, permissions, grantResults);
        }
    }
}
