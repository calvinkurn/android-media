package com.tokopedia.flight.passenger.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.passenger.view.adapter.FlightAmenityAdapterTypeFactory;
import com.tokopedia.flight.passenger.view.adapter.viewholder.FlightBookingAmenityViewHolder;
import com.tokopedia.flight.passenger.view.model.FlightBookingAmenityMetaModel;
import com.tokopedia.flight.passenger.view.model.FlightBookingAmenityModel;
import com.tokopedia.unifycomponents.UnifyButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/7/17.
 */

public class FlightBookingAmenityFragment extends BaseListFragment<FlightBookingAmenityModel, FlightAmenityAdapterTypeFactory>
        implements FlightBookingAmenityViewHolder.ListenerCheckedLuggage {

    public static final String EXTRA_SELECTED_AMENITIES = "EXTRA_SELECTED_AMENITIES";
    public static final String EXTRA_LIST_AMENITIES = "EXTRA_LIST_AMENITIES";
    private ArrayList<FlightBookingAmenityModel> flightBookingAmenityViewModels;
    private FlightBookingAmenityMetaModel selectedAmenity;

    public static FlightBookingAmenityFragment createInstance(ArrayList<FlightBookingAmenityMetaModel> flightBookingAmenityMetaViewModels,
                                                              FlightBookingAmenityMetaModel selectedAmenity) {
        FlightBookingAmenityFragment flightBookingAmenityFragment = new FlightBookingAmenityFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_LIST_AMENITIES, flightBookingAmenityMetaViewModels);
        bundle.putParcelable(EXTRA_SELECTED_AMENITIES, selectedAmenity);
        flightBookingAmenityFragment.setArguments(bundle);
        return flightBookingAmenityFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        flightBookingAmenityViewModels = getArguments().getParcelableArrayList(EXTRA_LIST_AMENITIES);
        selectedAmenity = getArguments().getParcelable(EXTRA_SELECTED_AMENITIES);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onItemClicked(FlightBookingAmenityModel flightBookingLuggageViewModel) {
        List<FlightBookingAmenityModel> viewModels = new ArrayList<>();
        viewModels.add(flightBookingLuggageViewModel);
        selectedAmenity.setAmenities(viewModels);
        getAdapter().notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(com.tokopedia.flight.R.layout.fragment_booking_luggage, container, false);
        UnifyButton button = (UnifyButton) view.findViewById(com.tokopedia.flight.R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_SELECTED_AMENITIES, selectedAmenity);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void loadData(int page) {
        renderList(flightBookingAmenityViewModels);
    }

    @Override
    protected FlightAmenityAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightAmenityAdapterTypeFactory(this);
    }

    @Override
    public boolean isItemChecked(FlightBookingAmenityModel selectedItem) {
        return selectedAmenity.getAmenities().contains(selectedItem);
    }

    @Override
    public void resetItemCheck() {
        selectedAmenity.setAmenities(new ArrayList<FlightBookingAmenityModel>());
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public int getRecyclerViewResourceId() {
        return R.id.recycler_view;
    }
}
