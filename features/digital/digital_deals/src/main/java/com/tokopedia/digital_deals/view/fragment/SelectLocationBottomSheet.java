package com.tokopedia.digital_deals.view.fragment;

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

import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.adapter.DealsLocationAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsPopularLocationAdapter;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.presenter.DealsLocationPresenter;

import org.w3c.dom.Text;

import java.util.List;

import javax.inject.Inject;

public class SelectLocationBottomSheet extends LinearLayout implements DealsLocationAdapter.ActionListener {


    private final DealsLocationAdapter.ActionListener actionListener;
    private final CloseSelectLocationBottomSheet closeBottomSheetListener;
    private BottomSheetBehavior<FrameLayout> frameLayoutBottomSheetBehavior = new BottomSheetBehavior<>();

    public SelectLocationBottomSheet(@NonNull Context context, boolean isForFirstTime, List<Location> locationList, DealsLocationAdapter.ActionListener actionListener, String selectedLocation, SelectLocationBottomSheet.CloseSelectLocationBottomSheet closeBottomSheetListener) {
        super(context);
        this.actionListener=actionListener;
        this.closeBottomSheetListener = closeBottomSheetListener;
        init(isForFirstTime, locationList, selectedLocation);
    }

    private void init(boolean isForFirstTime, List<Location> locationList, String selectedLocation) {

        View locationView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_change_location, this, true);
        RecyclerView rvSearchResults = locationView.findViewById(R.id.rv_city_results);
        RecyclerView rvLocationResults = locationView.findViewById(R.id.rv_location_results);
        ImageView crossIcon = locationView.findViewById(R.id.cross_icon_bottomsheet);
        TextView titletext = locationView.findViewById(R.id.location_bottomsheet_title);
        TextView searchInputView = locationView.findViewById(R.id.search_input_view);
        TextView popularCityTitle = locationView.findViewById(R.id.popular_city_heading);
        TextView popularLocationTitle = locationView.findViewById(R.id.popular_location_heading);

        LinearLayout linearLayout = locationView.findViewById(R.id.mainContent);
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


        rvSearchResults.setLayoutManager(new GridLayoutManager(getContext(), 3,
                GridLayoutManager.VERTICAL, false));
        rvSearchResults.setAdapter(new DealsLocationAdapter(locationList, this, selectedLocation));


        rvLocationResults.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvLocationResults.setAdapter(new DealsPopularLocationAdapter(getContext(), locationList));
    }

    @Override
    public void onLocationItemSelected(boolean locationUpdated) {
        actionListener.onLocationItemSelected(locationUpdated);
    }

    public interface CloseSelectLocationBottomSheet {
        void closeBottomsheet();
    }
}
