package com.tokopedia.flight.detail.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.flight.R;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteInfoViewModel;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel;
import com.tokopedia.flight.search.data.api.single.response.Amenity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailFacilityViewHolder extends AbstractViewHolder<FlightDetailRouteViewModel> {
    @LayoutRes
    public static int LAYOUT = com.tokopedia.flight.R.layout.item_flight_detail_facility;

    private final ListInfoAdapter adapterInfo;
    private final RecyclerView listInfo;
    private final RecyclerView gridAmenity;
    private final AmenityAdapter adapterAmenity;
    private final AppCompatImageView imageAirline;
    private final TextView airlineName;
    private final TextView airlineCode;
    private View separatorInfoView;
    private TextView facilityInfoTextView;
    private TextView facilitySeparatorTextView;

    public FlightDetailFacilityViewHolder(View itemView) {
        super(itemView);
        listInfo = (RecyclerView) itemView.findViewById(R.id.recycler_view_info);
        gridAmenity = (RecyclerView) itemView.findViewById(R.id.recycler_view_amenity);
        imageAirline = (AppCompatImageView) itemView.findViewById(R.id.airline_icon);
        airlineName = (TextView) itemView.findViewById(R.id.airline_name);
        airlineCode = (TextView) itemView.findViewById(R.id.airline_code);
        separatorInfoView = (View) itemView.findViewById(R.id.separator_info);
        facilityInfoTextView = (TextView) itemView.findViewById(R.id.title_info);
        facilitySeparatorTextView = (TextView) itemView.findViewById(R.id.tv_facility_separator);

        listInfo.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        adapterInfo = new ListInfoAdapter();
        listInfo.setAdapter(adapterInfo);
        adapterAmenity = new AmenityAdapter();
        gridAmenity.setAdapter(adapterAmenity);
        gridAmenity.setLayoutManager(new LinearLayoutManager(itemView.getContext(), RecyclerView.VERTICAL, false));
    }

    @Override
    public void bind(FlightDetailRouteViewModel route) {
        adapterInfo.addData(route.getInfos());
        setDefaultAmenities(route);
        airlineName.setText(route.getAirlineName());
        airlineCode.setText(String.format("%s - %s", route.getAirlineCode(), route.getFlightNumber()));
        ImageHandler.loadImageWithoutPlaceholder(imageAirline, route.getAirlineLogo(),
                ContextCompat.getDrawable(itemView.getContext(), com.tokopedia.flight.R.drawable.flight_ic_airline_default)
        );
    }

    public void setDefaultAmenities(FlightDetailRouteViewModel flightDetailRouteViewModel) {
        if (flightDetailRouteViewModel.getAmenities() != null && flightDetailRouteViewModel.getAmenities().size() > 0) {
            gridAmenity.setVisibility(View.VISIBLE);
            separatorInfoView.setVisibility(View.VISIBLE);
            facilityInfoTextView.setVisibility(View.VISIBLE);
            facilitySeparatorTextView.setVisibility(View.VISIBLE);
            adapterAmenity.addData(flightDetailRouteViewModel.getAmenities());
        } else {
            facilitySeparatorTextView.setVisibility(View.GONE);
            separatorInfoView.setVisibility(View.GONE);
            gridAmenity.setVisibility(View.GONE);
            facilityInfoTextView.setVisibility(View.GONE);
        }
    }

    private class ListInfoAdapter extends RecyclerView.Adapter<FlightDetailFacilityInfoViewHolder> {
        List<FlightDetailRouteInfoViewModel> infoList;

        public ListInfoAdapter() {
            infoList = new ArrayList<>();
        }

        @Override
        public FlightDetailFacilityInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(com.tokopedia.flight.R.layout.item_flight_detail_facility_info, parent, false);
            return new FlightDetailFacilityInfoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FlightDetailFacilityInfoViewHolder holder, int position) {
            holder.bindData(infoList.get(position));
        }

        @Override
        public int getItemCount() {
            return infoList.size();
        }

        public void addData(List<FlightDetailRouteInfoViewModel> infos) {
            infoList.clear();
            infoList.addAll(infos);
            notifyDataSetChanged();
        }
    }

    private class AmenityAdapter extends RecyclerView.Adapter<FlightDetailFacilityAmenityViewHolder> {

        List<Amenity> amenityList;

        public AmenityAdapter() {
            amenityList = new ArrayList<>();
        }

        @Override
        public FlightDetailFacilityAmenityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(com.tokopedia.flight.R.layout.item_flight_detail_facility_amenity, parent, false);
            return new FlightDetailFacilityAmenityViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FlightDetailFacilityAmenityViewHolder holder, int position) {
            holder.bindData(amenityList.get(position));
        }

        @Override
        public int getItemCount() {
            return amenityList.size();
        }

        public void addData(List<Amenity> amenities) {
            amenityList.clear();
            amenityList.addAll(amenities);
            notifyDataSetChanged();
        }
    }
}
