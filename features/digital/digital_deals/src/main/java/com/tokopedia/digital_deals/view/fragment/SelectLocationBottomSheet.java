package com.tokopedia.digital_deals.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsLocationAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsPopularLocationAdapter;
import com.tokopedia.digital_deals.view.contractor.DealsLocationContract;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.presenter.DealsLocationPresenter;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SelectLocationBottomSheet extends LinearLayout implements DealsLocationContract.View, DealsLocationAdapter.ActionListener {


    private RecyclerView rvSearchResults,rvLocationResults;
    private TextView titletext, searchInputView, popularCityTitle, popularLocationTitle;
    private LinearLayout linearLayout;
    private String selectedLocation;
    private final DealsLocationAdapter.ActionListener actionListener;
    private final CloseSelectLocationBottomSheet closeBottomSheetListener;
    private BottomSheetBehavior<FrameLayout> frameLayoutBottomSheetBehavior = new BottomSheetBehavior<>();
    @Inject
    DealsLocationPresenter mPresenter;
    boolean isLoading = false;
    private LinearLayoutManager layoutManager;
    private DealsPopularLocationAdapter dealsPopularLocationAdapter;

    public SelectLocationBottomSheet(@NonNull Context context, boolean isForFirstTime, List<Location> locationList, DealsLocationAdapter.ActionListener actionListener, String selectedLocation, SelectLocationBottomSheet.CloseSelectLocationBottomSheet closeBottomSheetListener) {
        super(context);
        this.actionListener=actionListener;
        this.closeBottomSheetListener = closeBottomSheetListener;
        init(isForFirstTime, selectedLocation);
        injector();
        mPresenter.attachView(this);
    }

    private void init(boolean isForFirstTime, String selectedLocation) {

        View locationView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_change_location, this, true);
        rvSearchResults = locationView.findViewById(R.id.rv_city_results);
        rvLocationResults = locationView.findViewById(R.id.rv_location_results);
        ImageView crossIcon = locationView.findViewById(R.id.cross_icon_bottomsheet);
        titletext = locationView.findViewById(R.id.location_bottomsheet_title);
        searchInputView = locationView.findViewById(R.id.search_input_view);
        popularCityTitle = locationView.findViewById(R.id.popular_city_heading);
        popularLocationTitle = locationView.findViewById(R.id.popular_location_heading);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        this.selectedLocation = selectedLocation;

        linearLayout = locationView.findViewById(R.id.mainContent);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getContext().getResources().getSystem().getDisplayMetrics().widthPixels,
                getContext().getResources().getSystem().getDisplayMetrics().heightPixels);
        linearLayout.setLayoutParams(layoutParams);

        frameLayoutBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        if (isForFirstTime) {
            titletext.setText(getContext().getResources().getString(R.string.location_bottomsheet_title));
            crossIcon.setVisibility(View.GONE);
        } else {
            titletext.setText(getContext().getResources().getString(R.string.select_location_bottomsheet_title));
            crossIcon.setVisibility(View.VISIBLE);
            crossIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeBottomSheetListener.closeBottomsheet();
                }
            });
        }
    }

    @Override
    public void onLocationItemSelected(boolean locationUpdated) {
        actionListener.onLocationItemSelected(locationUpdated);
    }

    @Override
    public Context getActivity() {
        return this.getActivity().getApplicationContext();
    }

    @Override
    public View getRootView() {
        return linearLayout;
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public void renderPopularCities(List<Location> locationList, String... searchText) {
        rvSearchResults.setLayoutManager(new GridLayoutManager(getContext(), 3,
                GridLayoutManager.VERTICAL, false));
        rvSearchResults.setAdapter(new DealsLocationAdapter(locationList, this, selectedLocation));
    }

    @Override
    public void renderPopularLocations(List<Location> locationList, String... searchText) {
        dealsPopularLocationAdapter = new DealsPopularLocationAdapter(getContext(), locationList);
        rvLocationResults.setLayoutManager(layoutManager);
        rvLocationResults.setAdapter(dealsPopularLocationAdapter);
        rvLocationResults.addOnScrollListener(rvOnScrollListener);
    }

    private RecyclerView.OnScrollListener rvOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mPresenter.onRecyclerViewScrolled(layoutManager);
        }
    };


    @Override
    public void removeFooter() {
        ((DealsPopularLocationAdapter) rvLocationResults.getAdapter()).removeFooter();
    }

    @Override
    public void addFooter() {
        ((DealsPopularLocationAdapter) rvLocationResults.getAdapter()).addFooter();

    }

    @Override
    public RequestParams getParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("type", "city");
        return requestParams;
    }

    public interface CloseSelectLocationBottomSheet {
        void closeBottomsheet();
    }

    private void injector(){
        DealsComponent component = DaggerDealsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getContext().getApplicationContext()).getBaseAppComponent())
                .build();
        component.inject(this);
    }
}
