package com.tokopedia.logisticgeolocation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsChangeAddress;

import java.util.HashMap;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class GeolocationActivity extends BaseSimpleActivity implements ITransactionAnalyticsGeoLocationPinPoint{

    public static final String EXTRA_EXISTING_LOCATION = "EXTRA_EXISTING_LOCATION";
    public static final String EXTRA_IS_FROM_MARKETPLACE_CART = "EXTRA_IS_FROM_MARKETPLACE_CART";
    public static final String EXTRA_HASH_LOCATION = "EXTRA_HASH_LOCATION";
    // todo : put screen analytics
    public static final String SCREEN_ADDRESS_GEOLOCATION = "Add Geolocation Address page";

    private Bundle mBundle;
    private CheckoutAnalyticsChangeAddress checkoutAnalyticsChangeAddress;

    // Address
    public static Intent createInstance(@NonNull Context context, LocationPass locationPass,
                                        boolean isFromMarketPlaceCart) {
        Intent intent = new Intent(context, GeolocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_EXISTING_LOCATION, locationPass);
        bundle.putBoolean(EXTRA_IS_FROM_MARKETPLACE_CART, isFromMarketPlaceCart);
        intent.putExtras(bundle);
        return intent;
    }

    // Shop open
    public static Intent createInstanceIntent(@NonNull Context context, @Nullable HashMap<String, String> locationHash) {
        Intent intent = new Intent(context, GeolocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_HASH_LOCATION, locationHash);
        intent.putExtras(bundle);
        return intent;
    }

    // Checkout
    public static Intent createInstanceFromMarketplaceCart(@NonNull Context context, @Nullable LocationPass locationPass) {
        Intent intent = new Intent(context, GeolocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_EXISTING_LOCATION, locationPass);
        bundle.putBoolean(EXTRA_IS_FROM_MARKETPLACE_CART, true);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getIntent().getExtras();
        if (getApplication() instanceof AbstractionRouter) {
            checkoutAnalyticsChangeAddress =
                    new CheckoutAnalyticsChangeAddress(
                            ((AbstractionRouter) getApplication()).getAnalyticTracker()
                    );
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendAnalyticsOnBackPressClicked();
    }

    @Override
    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    protected void inflateFragment() {
        super.inflateFragment();
    }

    @Override
    protected Fragment getNewFragment() {
        if(mBundle != null) {
            LocationPass locationPass = mBundle.getParcelable(EXTRA_EXISTING_LOCATION);
            // handle from shop open unresolved shared data
            if (locationPass == null) {
                locationPass = LocationPassMapper.unBundleLocationMap(
                        (HashMap<String, String>) mBundle.getSerializable(EXTRA_HASH_LOCATION));
            }
            if(locationPass != null && !locationPass.getLatitude().isEmpty()) {
                return GoogleMapFragment.newInstance(locationPass);
            } else {
                Toast.makeText(this, "No location Provided", Toast.LENGTH_SHORT).show();
                // todo : generate longlat geocode by network here
            }
        }
        return GoogleMapFragment.newInstanceNoLocation();
    }

    @Override
    public void sendAnalyticsOnDropdownSuggestionItemClicked() {
        if (mBundle.getBoolean(EXTRA_IS_FROM_MARKETPLACE_CART, false))
            checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickDropdownSuggestionTandaiLokasiPadaTambahAddress();
    }

    @Override
    public void sendAnalyticsOnSetCurrentMarkerAsCurrentPosition() {
        if (mBundle.getBoolean(EXTRA_IS_FROM_MARKETPLACE_CART, false))
            checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickVTandaiLokasiPadaTambahAddress();
    }

    @Override
    public void sendAnalyticsOnBackPressClicked() {
        if (mBundle.getBoolean(EXTRA_IS_FROM_MARKETPLACE_CART, false))
            checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickBackArrowTandaiLokasiPadaTambahAddress();
    }

    @Override
    public void sendAnalyticsOnGetCurrentLocationClicked() {
        if (mBundle.getBoolean(EXTRA_IS_FROM_MARKETPLACE_CART, false))
            checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickPinButtonFromTandaiLokasi();
    }

    @Override
    public void sendAnalyticsOnViewErrorSetPinPointLocation(String errorMessage) {
        if (mBundle.getBoolean(EXTRA_IS_FROM_MARKETPLACE_CART, false))
            checkoutAnalyticsChangeAddress.eventViewShippingCartChangeAddressViewValidationErrorTandaiLokasi(errorMessage);
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    void showRationaleForGPS(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(this, request, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    void showDeniedForGPS() {
        RequestPermissionUtil.onPermissionDenied(this, Manifest.permission.ACCESS_FINE_LOCATION);
        finish();
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    void showNeverAskForGPS() {
        RequestPermissionUtil.onNeverAskAgain(this, Manifest.permission.ACCESS_FINE_LOCATION);
        finish();
    }
}
