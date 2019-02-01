package com.tokopedia.logisticgeolocation.pinpoint;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.logisticcommon.LogisticCommonConstant;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.logisticdata.data.entity.geolocation.coordinate.viewmodel.CoordinateViewModel;
import com.tokopedia.logisticgeolocation.R;
import com.tokopedia.logisticgeolocation.data.RetrofitInteractor;
import com.tokopedia.logisticgeolocation.di.DaggerGeolocationComponent;
import com.tokopedia.logisticgeolocation.di.GeolocationModule;
import com.tokopedia.logisticgeolocation.util.RequestPermissionUtil;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.user.session.UserSession;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class GeolocationActivity extends BaseActivity implements ITransactionAnalyticsGeoLocationPinPoint {

    private static final String TAG_FRAGMENT = "TAG_FRAGMENT";
    public static final String EXTRA_IS_FROM_MARKETPLACE_CART = "EXTRA_IS_FROM_MARKETPLACE_CART";
    public static final String SCREEN_ADDRESS_GEOLOCATION = "Add Geolocation Address page";

    private Bundle mBundle;
    private boolean isFromMarketPlace = false;
    private CheckoutAnalyticsChangeAddress checkoutAnalyticsChangeAddress;
    @Inject RetrofitInteractor mRepository;
    @Inject UserSession mUser;

    /**
     * Usage = Address // Address Tx // Shipment // ShopOpen // Seller
     *
     * @param locationPass          please get from common module logistic_data
     * @param isFromMarketPlaceCart true if you are from marketplace cart
     * @return intent
     */
    public static Intent createInstance(@NonNull Context context, LocationPass locationPass,
                                        boolean isFromMarketPlaceCart) {
        Intent intent = new Intent(context, GeolocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(LogisticCommonConstant.EXTRA_EXISTING_LOCATION, locationPass);
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
                    new CheckoutAnalyticsChangeAddress(
                            ((AbstractionRouter) getApplication()).getAnalyticTracker()
                    );
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

        if (savedInstanceState == null) {
            GeolocationActivityPermissionsDispatcher.inflateFragmentWithCheck(this);
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

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public void inflateFragment() {
        LocationPass locationPass = null;
        Fragment fragment = null;
        mBundle = getIntent().getExtras();

        if (mBundle != null) {
            locationPass = mBundle.getParcelable(LogisticCommonConstant.EXTRA_EXISTING_LOCATION);
        }

        if (locationPass != null) {
            if (locationPass.getLatitude() != null && !locationPass.getLatitude().isEmpty()) {
                fragment = GoogleMapFragment.newInstance(locationPass);
            } else {
                Map<String, String> params = new HashMap<>();
                params.put("address", locationPass.getDistrictName()
                        + ", "
                        + locationPass.getCityName());
                params = AuthUtil
                        .generateParamsNetwork(mUser.getUserId(), mUser.getDeviceId(), params);
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

    private RetrofitInteractor.GenerateLatLongListener latLongListener(
            final Context context,
            final LocationPass locationPass
    ) {
        return new RetrofitInteractor.GenerateLatLongListener() {
            @Override
            public void onSuccess(CoordinateViewModel model) {
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
}
