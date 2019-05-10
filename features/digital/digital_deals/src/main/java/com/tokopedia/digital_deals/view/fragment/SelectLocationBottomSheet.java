package com.tokopedia.digital_deals.view.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.adapter.DealsLocationAdapter;
import com.tokopedia.digital_deals.view.model.Location;

import java.util.List;

public class SelectLocationBottomSheet extends FrameLayout implements DealsLocationAdapter.ActionListener {


    private final DealsLocationAdapter.ActionListener actionListener;
    private final CloseSelectLocationBottomSheet closeBottomSheetListener;

    public SelectLocationBottomSheet(@NonNull Context context, boolean isForFirstTime, List<Location> locationList, DealsLocationAdapter.ActionListener actionListener, String selectedLocation, SelectLocationBottomSheet.CloseSelectLocationBottomSheet closeBottomSheetListener) {
        super(context);
        this.actionListener=actionListener;
        this.closeBottomSheetListener = closeBottomSheetListener;
        init(isForFirstTime, locationList, selectedLocation);
    }

    private void init(boolean isForFirstTime, List<Location> locationList, String selectedLocation) {

        View locationView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_change_location, this, true);
        RecyclerView rvSearchResults = locationView.findViewById(R.id.rv_search_results);
        ImageView crossIcon = locationView.findViewById(R.id.cross_icon_bottomsheet);
        TextView titletext = locationView.findViewById(R.id.location_bottomsheet_title);

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
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setAlignItems(AlignItems.CENTER);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        rvSearchResults.setLayoutManager(layoutManager);
        rvSearchResults.setAdapter(new DealsLocationAdapter(locationList, this, selectedLocation));
    }

    @Override
    public void onLocationItemSelected(boolean locationUpdated) {
        actionListener.onLocationItemSelected(locationUpdated);
    }

    public interface CloseSelectLocationBottomSheet {
        void closeBottomsheet();
    }
}
