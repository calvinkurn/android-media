package com.tokopedia.logisticaddaddress.features.pinpoint;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.MenuItem;
import android.widget.Toast;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.authentication.AuthHelper;
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

    private Bundle mBundle;
    private boolean isFromMarketPlace = false;
    private CheckoutAnalyticsChangeAddress checkoutAnalyticsChangeAddress;
    private PermissionCheckerHelper permissionCheckerHelper = new PermissionCheckerHelper();
    @Inject RetrofitInteractor mRepository;
    @Inject UserSession mUser;

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
        if (mBundle != null) {
            isFromMarketPlace = mBundle.getBoolean(EXTRA_IS_FROM_MARKETPLACE_CART, false);
        }

        BaseAppComponent appComponent =
                ((BaseMainApplication) getApplication()).getBaseAppComponent();
        DaggerGeolocationComponent.builder().baseAppComponent(appComponent)
                .geolocationModule(new GeolocationModule(this, null))
                .build()
                .inject(this);

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
                inflateFragment();
            }
        }, getString(R.string.need_permission_location));
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

    public void inflateFragment() {
        LocationPass locationPass = null;
        Fragment fragment = null;
        mBundle = getIntent().getExtras();

        if (mBundle != null) {
            locationPass = mBundle.getParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION);
        }

        if (locationPass != null) {
            if (locationPass.getLatitude() != null && !locationPass.getLatitude().isEmpty()) {
                fragment = GoogleMapFragment.newInstance(locationPass);
            } else {
                Map<String, String> params = new HashMap<>();
                params.put("address", locationPass.getDistrictName()
                        + ", "
                        + locationPass.getCityName());
                params = AuthHelper.generateParamsNetwork(mUser.getUserId(), mUser.getDeviceId(), params);
                mRepository.generateLatLngGeoCode(
                        params,
                        latLongListener(this, locationPass)
                );
            }
        } else {
            fragment = GoogleMapFragment.newInstanceNoLocation();
        }
        if (fragment != null) getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, TAG_FRAGMENT)
                .commit();
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
            final LocationPass locationPass
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
                Fragment fragment = GoogleMapFragment.newInstance(locationPass);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment, TAG_FRAGMENT)
                        .commit();
            }

            @Override
            public void onError(String errorMessage) {
                NetworkErrorHelper.showSnackbar((Activity) context, errorMessage);
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheckerHelper.onRequestPermissionsResult(GeolocationActivity.this, requestCode, permissions, grantResults);
        }
    }
}
