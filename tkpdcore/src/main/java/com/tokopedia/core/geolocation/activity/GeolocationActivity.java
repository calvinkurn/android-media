package com.tokopedia.core.geolocation.activity;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.geolocation.fragment.GoogleMapFragment;
import com.tokopedia.core.geolocation.listener.GeolocationView;
import com.tokopedia.core.geolocation.listener.ITransactionAnalyticsGeoLocationPinPoint;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.geolocation.presenter.GeolocationPresenter;
import com.tokopedia.core.geolocation.presenter.GeolocationPresenterImpl;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core2.R;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsChangeAddress;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Created by hangnadi on 1/29/16.
 */
public class GeolocationActivity extends BasePresenterActivity<GeolocationPresenter>
        implements GeolocationView, ITransactionAnalyticsGeoLocationPinPoint {

    public static final String EXTRA_EXISTING_LOCATION = "EXTRA_EXISTING_LOCATION";
    public static final String EXTRA_IS_FROM_MARKETPLACE_CART = "EXTRA_IS_FROM_CHECKOUT_CART";
    public static final String EXTRA_HASH_LOCATION = "EXTRA_HASH_LOCATION";

    private Bundle bundleData;
    private Uri uriData;
    private CheckoutAnalyticsChangeAddress checkoutAnalyticsChangeAddress;
    private PermissionCheckerHelper permissionCheck;

    // Address -> Router
    public static Intent createInstanceFromAddress(@NonNull Context context,
                                                   @Nullable HashMap<String, String> locationPass,
                                                   boolean isFromMarketPlaceCart) {
        Intent intent = new Intent(context, GeolocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_HASH_LOCATION, locationPass);
        bundle.putBoolean(EXTRA_IS_FROM_MARKETPLACE_CART, isFromMarketPlaceCart);
        intent.putExtras(bundle);
        return intent;
    }

    // Shop Open -> Router
    public static Intent createInstanceIntent(@NonNull Context context, @Nullable LocationPass locationPass) {
        Intent intent = new Intent(context, GeolocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_EXISTING_LOCATION, locationPass);
        intent.putExtras(bundle);
        return intent;
    }

    // Shipment Fragment -> Direct dep
    public static Intent createInstanceFromMarketplaceCart(@NonNull Context context, @Nullable LocationPass locationPass) {
        Intent intent = new Intent(context, GeolocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_EXISTING_LOCATION, locationPass);
        bundle.putBoolean(EXTRA_IS_FROM_MARKETPLACE_CART, true);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_ADDRESS_GEOLOCATION;
    }

    @Override
    public void inflateFragment(Fragment fragment, String tag) {
        if (getFragmentManager().findFragmentByTag(tag) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, tag)
                    .commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gojek);
        permissionCheck = new PermissionCheckerHelper();
        permissionCheck.checkPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION,
                new PermissionCheckerHelper.PermissionCheckListener() {
                    @Override
                    public void onPermissionDenied(@NotNull String permissionText) {
                        RequestPermissionUtil.onPermissionDenied(
                                GeolocationActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        );
                        finish();
                    }

                    @Override
                    public void onNeverAskAgain(@NotNull String permissionText) {
                        RequestPermissionUtil.onNeverAskAgain(
                                GeolocationActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        );
                        finish();
                    }

                    @Override
                    public void onPermissionGranted() {
                        presenter.initFragment(GeolocationActivity.this, uriData, bundleData);
                    }
                },
                ""
        );
    }

    @Override
    public void onBackPressed() {
        sendAnalyticsOnBackPressClicked();
        super.onBackPressed();

    }

    @Override
    protected void setupURIPass(Uri data) {
        this.uriData = data;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.bundleData = extras;
    }

    @Override
    protected void initialPresenter() {
        presenter = new GeolocationPresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gojek;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void setViewListener() {
    }

    @Override
    protected void initVar() {
        if (getApplication() instanceof AbstractionRouter) {
            checkoutAnalyticsChangeAddress =
                    new CheckoutAnalyticsChangeAddress();
        }
    }

    @Override
    protected void setActionVar() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getFragmentManager().findFragmentByTag(
                GoogleMapFragment.class.getSimpleName()
        ) != null) {
            getFragmentManager()
                    .findFragmentByTag(
                            GoogleMapFragment.class.getSimpleName()
                    ).onActivityResult(requestCode, resultCode, new Intent());
        }

        //presenter.replaceFragment(this, bundleData);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionCheck.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void sendAnalyticsOnDropdownSuggestionItemClicked() {
        if (bundleData.getBoolean(EXTRA_IS_FROM_MARKETPLACE_CART, false))
            checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickDropdownSuggestionTandaiLokasiPadaTambahAddress();
    }

    @Override
    public void sendAnalyticsOnSetCurrentMarkerAsCurrentPosition() {
        if (bundleData.getBoolean(EXTRA_IS_FROM_MARKETPLACE_CART, false))
            checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickVTandaiLokasiPadaTambahAddress();
    }

    @Override
    public void sendAnalyticsOnBackPressClicked() {
        if (bundleData.getBoolean(EXTRA_IS_FROM_MARKETPLACE_CART, false))
            checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickBackArrowTandaiLokasiPadaTambahAddress();
    }

    @Override
    public void sendAnalyticsOnGetCurrentLocationClicked() {
        if (bundleData.getBoolean(EXTRA_IS_FROM_MARKETPLACE_CART, false))
            checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickPinButtonFromTandaiLokasi();
    }

    @Override
    public void sendAnalyticsOnViewErrorSetPinPointLocation(String errorMessage) {
        if (bundleData.getBoolean(EXTRA_IS_FROM_MARKETPLACE_CART, false))
            checkoutAnalyticsChangeAddress.eventViewShippingCartChangeAddressViewValidationErrorTandaiLokasi(errorMessage);
    }
}
