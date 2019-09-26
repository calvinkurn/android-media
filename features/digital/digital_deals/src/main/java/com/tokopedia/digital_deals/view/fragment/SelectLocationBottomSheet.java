package com.tokopedia.digital_deals.view.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.adapter.DealsLocationAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsPopularLocationAdapter;
import com.tokopedia.digital_deals.view.contractor.DealsLocationContract;
import com.tokopedia.digital_deals.view.customview.SearchInputView;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.presenter.DealsLocationPresenter;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.library.baseadapter.AdapterCallback;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

public class SelectLocationBottomSheet extends BaseDaggerFragment implements DealsLocationContract.View, DealsLocationAdapter.SelectCityListener, DealsPopularLocationAdapter.SelectPopularLocationListener, SearchInputView.Listener, SearchInputView.ResetListener, SearchInputView.FocusChangeListener {


    private RecyclerView rvSearchResults, rvLocationResults;
    private TextView titletext, popularCityTitle, popularLocationTitle;
    private SearchInputView searchInputView;
    private ImageView crossIcon;
    private RelativeLayout mainContent;
    private NestedScrollView nestedScrollView;
    private LinearLayout shimmerLayout;
    private String selectedLocation;
    private ConstraintLayout noLocationLayout;
    private SelectedLocationListener selectedLocationListener;
    @Inject
    DealsLocationPresenter mPresenter;
    boolean isLoading = false;
    private LinearLayoutManager layoutManager;
    private DealsPopularLocationAdapter dealsPopularLocationAdapter;

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
        View locationView = inflater.inflate(R.layout.fragment_change_location, container, false);
        nestedScrollView = locationView.findViewById(R.id.nested_scroll_view);
        rvSearchResults = locationView.findViewById(R.id.rv_city_results);
        rvLocationResults = locationView.findViewById(R.id.rv_location_results);
        crossIcon = locationView.findViewById(R.id.cross_icon_bottomsheet);
        titletext = locationView.findViewById(R.id.location_bottomsheet_title);
        searchInputView = locationView.findViewById(R.id.search_input_view);
        popularCityTitle = locationView.findViewById(R.id.popular_city_heading);
        noLocationLayout = locationView.findViewById(R.id.no_location);
        popularLocationTitle = locationView.findViewById(R.id.popular_location_heading);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        dealsPopularLocationAdapter = new DealsPopularLocationAdapter(getContext(), this, mAdapterCallbacks, getArguments().getParcelable(Utils.LOCATION_OBJECT), true);
        this.selectedLocation = selectedLocation;
        searchInputView.setListener(this);
        searchInputView.setFocusChangeListener(this);
        searchInputView.setResetListener(this);
        searchInputView.setSearchHint(getContext().getResources().getString(R.string.location_search_hint));
        mainContent = locationView.findViewById(R.id.mainContent);
        shimmerLayout = locationView.findViewById(R.id.shimmer_layout);


        titletext.setText(getContext().getResources().getString(R.string.select_location_bottomsheet_title));
        crossIcon.setVisibility(View.VISIBLE);
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
//        mPresenter.attachView(this);
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


    public interface SelectedLocationListener {
        void onLocationItemUpdated(boolean isLocationUpdated);
    }
}
