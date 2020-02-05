package com.tokopedia.digital_deals.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.provider.Settings;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.gms.location.LocationServices;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.adapter.DealsLocationAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsPopularLocationAdapter;
import com.tokopedia.digital_deals.view.contractor.DealsLocationContract;
import com.tokopedia.digital_deals.view.customview.SearchInputView;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.response.LocationResponse;
import com.tokopedia.digital_deals.view.presenter.DealsLocationPresenter;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.library.baseadapter.AdapterCallback;
import com.tokopedia.locationmanager.DeviceLocation;
import com.tokopedia.locationmanager.LocationDetectorHelper;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.unifycomponents.Toaster;
import com.tokopedia.unifycomponents.UnifyButton;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class SelectLocationBottomSheet extends BaseDaggerFragment implements View.OnClickListener, DealsLocationContract.View, DealsLocationAdapter.SelectCityListener, DealsPopularLocationAdapter.SelectPopularLocationListener, SearchInputView.Listener, SearchInputView.ResetListener, SearchInputView.FocusChangeListener {


    private RecyclerView rvSearchResults, rvLocationResults;
    private TextView titletext, popularCityTitle, popularLocationTitle;
    private SearchInputView searchInputView;
    private ImageView crossIcon;
    private RelativeLayout mainContent;
    private NestedScrollView nestedScrollView;
    private LinearLayout shimmerLayout;
    private String selectedLocation;
    private ConstraintLayout noLocationLayout;
    private TextView detectLocation;
    private ConstraintLayout detectLocationLayout;
    private SelectedLocationListener selectedLocationListener;
    @Inject
    DealsLocationPresenter mPresenter;
    boolean isLoading = false;
    private LinearLayoutManager layoutManager;
    private DealsPopularLocationAdapter dealsPopularLocationAdapter;
    private PermissionCheckerHelper permissionCheckerHelper;
    private CloseableBottomSheetDialog locationSettingBottomSheet;
    private boolean isDeniedFirstTime = false;
    private Location location;
    private SharedPreferences sharedPrefs;

    public static Fragment createInstance(String selectedLocation, Location location) {
        Fragment fragment = new SelectLocationBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putString("selectedLocation", selectedLocation);
        bundle.putParcelable(Utils.LOCATION_OBJECT, location);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View locationView = inflater.inflate(com.tokopedia.digital_deals.R.layout.fragment_change_location, container, false);
        nestedScrollView = locationView.findViewById(com.tokopedia.digital_deals.R.id.nested_scroll_view);
        rvSearchResults = locationView.findViewById(com.tokopedia.digital_deals.R.id.rv_city_results);
        rvLocationResults = locationView.findViewById(com.tokopedia.digital_deals.R.id.rv_location_results);
        crossIcon = locationView.findViewById(com.tokopedia.digital_deals.R.id.cross_icon_bottomsheet);
        titletext = locationView.findViewById(com.tokopedia.digital_deals.R.id.location_bottomsheet_title);
        searchInputView = locationView.findViewById(com.tokopedia.digital_deals.R.id.search_input_view);
        popularCityTitle = locationView.findViewById(com.tokopedia.digital_deals.R.id.popular_city_heading);
        noLocationLayout = locationView.findViewById(com.tokopedia.digital_deals.R.id.no_location);
        popularLocationTitle = locationView.findViewById(com.tokopedia.digital_deals.R.id.popular_location_heading);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        dealsPopularLocationAdapter = new DealsPopularLocationAdapter(getContext(), this, mAdapterCallbacks, getArguments().getParcelable(Utils.LOCATION_OBJECT), true);
        this.selectedLocation = selectedLocation;
        searchInputView.setListener(this);
        searchInputView.setFocusChangeListener(this);
        searchInputView.setResetListener(this);
        searchInputView.setSearchHint(getContext().getResources().getString(com.tokopedia.digital_deals.R.string.location_search_hint));
        mainContent = locationView.findViewById(com.tokopedia.digital_deals.R.id.mainContent);
        shimmerLayout = locationView.findViewById(com.tokopedia.digital_deals.R.id.shimmer_layout);
        detectLocation = locationView.findViewById(com.tokopedia.digital_deals.R.id.detect_current_location);
        detectLocationLayout = locationView.findViewById(com.tokopedia.digital_deals.R.id.cl_detect_current_location);
        detectLocationLayout.setOnClickListener(this);

        titletext.setText(getContext().getResources().getString(com.tokopedia.digital_deals.R.string.select_location_bottomsheet_title));
        crossIcon.setVisibility(View.VISIBLE);
        permissionCheckerHelper = new PermissionCheckerHelper();
        locationSettingBottomSheet = CloseableBottomSheetDialog.createInstanceRounded(getActivity());
        location = getArguments().getParcelable(Utils.LOCATION_OBJECT);
        crossIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHandler.hideSoftKeyboard(getActivity());
                getFragmentManager().popBackStack();
            }
        });

        renderPopularLocations();

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrolX, scrollY, oldScrollX, oldScrollY) -> {
            KeyboardHandler.hideSoftKeyboard(getActivity());
            if(v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    dealsPopularLocationAdapter.startDataLoading();
                }
            }
        });
        mPresenter.attachView(this);
        return locationView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        selectedLocationListener = (SelectedLocationListener) context;
    }

    @Override
    public View getRootView() {
        return mainContent;
    }

    @Override
    public void showProgressBar(boolean showProgressBar) {
        if (showProgressBar) {
            shimmerLayout.setVisibility(View.VISIBLE);
            mainContent.setVisibility(View.GONE);
        } else {
            shimmerLayout.setVisibility(View.GONE);
            mainContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setCurrentLocation(LocationResponse locationResponse) {
        if (locationResponse != null && locationResponse.getLocations() != null && locationResponse.getLocations().size() > 0) {
            Utils.getSingletonInstance().updateLocation(getContext(), locationResponse.getLocations().get(0));
            selectedLocationListener.onLocationItemUpdated(true);
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void setDefaultLocation() {
        Toaster.INSTANCE.showNormalWithAction(mainContent, Utils.getSingletonInstance().getLocationErrorMessage(getContext()), Snackbar.LENGTH_LONG, getContext().getResources().getString(com.tokopedia.digital_deals.R.string.location_deals_changed_toast_oke), v1 -> {
        });
        selectedLocationListener.setDefaultLocationOnHomePage();
        getFragmentManager().popBackStack();
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public void renderPopularCities(List<Location> locationList, String... searchText) {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        rvSearchResults.setLayoutManager(layoutManager);
        rvSearchResults.setAdapter(new DealsLocationAdapter(locationList, this, selectedLocation));
    }

    public void renderPopularLocations() {
        rvLocationResults.setAdapter(dealsPopularLocationAdapter);
        dealsPopularLocationAdapter.startDataLoading();
    }

    @Override
    public RequestParams getParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("type", "city");
        return requestParams;
    }

    @Override
    public void onSearchSubmitted(String text) {
        if (!TextUtils.isEmpty(text)) {
            nestedScrollView.scrollTo(0, 0);
            popularCityTitle.setVisibility(View.GONE);
            rvSearchResults.setVisibility(View.GONE);
            popularLocationTitle.setVisibility(View.GONE);
            dealsPopularLocationAdapter.setSearchText(text);
            dealsPopularLocationAdapter.setCurrentPageIndex(1);
            dealsPopularLocationAdapter.startDataLoading();
            noLocationLayout.setVisibility(View.GONE);
        } else {
            dealsPopularLocationAdapter.setSearchText("");
            dealsPopularLocationAdapter.setCurrentPageIndex(1);
            dealsPopularLocationAdapter.startDataLoading();
            popularCityTitle.setVisibility(View.VISIBLE);
            rvSearchResults.setVisibility(View.VISIBLE);
            popularLocationTitle.setVisibility(View.VISIBLE);
            noLocationLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSearchTextChanged(String text) {
        onSearchSubmitted(text);
    }

    @Override
    protected void initInjector() {
        getComponent(DealsComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {
        return "";
    }

    AdapterCallback mAdapterCallbacks = new AdapterCallback() {
        @Override
        public void onRetryPageLoad(int pageNumber) {

        }

        @Override
        public void onEmptyList(Object rawObject) {
            popularLocationTitle.setVisibility(View.VISIBLE);
            noLocationLayout.setVisibility(View.VISIBLE);
        }

        @Override
        public void onStartFirstPageLoad() {
            noLocationLayout.setVisibility(View.GONE);
        }

        @Override
        public void onFinishFirstPageLoad(int itemCount, @Nullable Object rawObject) {

        }

        @Override
        public void onStartPageLoad(int pageNumber) {

        }

        @Override
        public void onFinishPageLoad(int itemCount, int pageNumber, @Nullable Object rawObject) {

        }

        @Override
        public void onError(int pageNumber) {
            popularCityTitle.setVisibility(View.VISIBLE);
            popularLocationTitle.setVisibility(View.GONE);
            rvSearchResults.setVisibility(View.VISIBLE);
            noLocationLayout.setVisibility(View.GONE);
        }
    };

    @Override
    public void onCityItemSelected(boolean locationUpdated) {
        KeyboardHandler.hideSoftKeyboard(getActivity());
        selectedLocationListener.onLocationItemUpdated(locationUpdated);
        getFragmentManager().popBackStack();
    }

    @Override
    public void onPopularLocationSelected(boolean locationUpdated) {
        KeyboardHandler.hideSoftKeyboard(getActivity());
        getFragmentManager().popBackStack();
        selectedLocationListener.onLocationItemUpdated(locationUpdated);
    }

    @Override
    public void onFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            if (TextUtils.isEmpty(searchInputView.getSearchText())) {
                //default URl
                dealsPopularLocationAdapter.setCurrentPageIndex(0);
            }
        }
    }

    @Override
    public void onSearchReset() {
        popularCityTitle.setVisibility(View.VISIBLE);
        rvSearchResults.setVisibility(View.VISIBLE);
        noLocationLayout.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == com.tokopedia.digital_deals.R.id.cl_detect_current_location) {
            if (!isDeniedFirstTime) {
                permissionCheckerHelper.checkPermission(SelectLocationBottomSheet.this, PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION, new PermissionCheckerHelper.PermissionCheckListener() {
                    @Override
                    public void onPermissionDenied(String permissionText) {
                        isDeniedFirstTime = true;
                        selectedLocationListener.onLocationItemUpdated(true);
                    }

                    @Override
                    public void onNeverAskAgain(String permissionText) {
                    }

                    @Override
                    public void onPermissionGranted() {
                        detectAndSendLocation();
                        isDeniedFirstTime = false;
                    }
                }, getContext().getResources().getString(com.tokopedia.digital_deals.R.string.deals_use_current_location));
            } else {
                openLocationSettingBottomSheet();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionCheckerHelper.onRequestPermissionsResult(getContext(), requestCode, permissions, grantResults);
    }

    private void openLocationSettingBottomSheet() {
        View categoryView = getLayoutInflater().inflate(com.tokopedia.digital_deals.R.layout.deals_current_location_bottomsheet, null);
        ImageView crossIcon = categoryView.findViewById(com.tokopedia.digital_deals.R.id.location_cross_icon_bottomsheet);
        UnifyButton openLocationSettings = categoryView.findViewById(com.tokopedia.digital_deals.R.id.goto_location_settings);
        crossIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationSettingBottomSheet.dismiss();
            }
        });

        openLocationSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDeniedFirstTime = false;
                locationSettingBottomSheet.dismiss();
                final Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                getContext().startActivity(intent);
            }
        });
        locationSettingBottomSheet.setCustomContentView(categoryView, "", false);
        locationSettingBottomSheet.show();
        locationSettingBottomSheet.setCanceledOnTouchOutside(true);
    }


    public void detectAndSendLocation() {
        LocationDetectorHelper locationDetectorHelper = new LocationDetectorHelper(
                permissionCheckerHelper,
                LocationServices.getFusedLocationProviderClient(getActivity()
                        .getApplicationContext()),
                getActivity().getApplicationContext());
        locationDetectorHelper.getLocation(onGetLocation(), getActivity(),
                LocationDetectorHelper.TYPE_DEFAULT_FROM_CLOUD,
                "");
    }

    private Function1<DeviceLocation, Unit> onGetLocation() {
        return (deviceLocation) -> {
            saveLocation(getActivity(), deviceLocation.getLatitude(), deviceLocation.getLongitude());
            return null;
        };
    }

    public void saveLocation(Context context, double latitude, double longitude) {
        mPresenter.getNearestLocation(String.format("%s,%s", String.valueOf(latitude), String.valueOf(longitude)));
    }

    public interface SelectedLocationListener {
        void onLocationItemUpdated(boolean isLocationUpdated);
        void setDefaultLocationOnHomePage();
    }
}
