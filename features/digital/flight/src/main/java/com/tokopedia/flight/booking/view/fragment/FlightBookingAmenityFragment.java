package com.tokopedia.flight.booking.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.adapter.FlightAmenityAdapterTypeFactory;
import com.tokopedia.flight.booking.view.adapter.viewholder.FlightBookingAmenityViewHolder;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/7/17.
 */

public class FlightBookingAmenityFragment extends BaseListFragment<FlightBookingAmenityViewModel, FlightAmenityAdapterTypeFactory>
        implements FlightBookingAmenityViewHolder.ListenerCheckedLuggage {

    public static final String EXTRA_SELECTED_AMENITIES = "EXTRA_SELECTED_AMENITIES";
    public static final String EXTRA_LIST_AMENITIES = "EXTRA_LIST_AMENITIES";
    private ArrayList<FlightBookingAmenityViewModel> flightBookingAmenityViewModels;
    private FlightBookingAmenityMetaViewModel selectedAmenity;

    public static FlightBookingAmenityFragment createInstance(ArrayList<FlightBookingAmenityMetaViewModel> flightBookingAmenityMetaViewModels,
                                                              FlightBookingAmenityMetaViewModel selectedAmenity) {
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
    public void onItemClicked(FlightBookingAmenityViewModel flightBookingLuggageViewModel) {
        List<FlightBookingAmenityViewModel> viewModels = new ArrayList<>();
        viewModels.add(flightBookingLuggageViewModel);
        selectedAmenity.setAmenities(viewModels);
        getAdapter().notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_luggage, container, false);
        Button button = (Button) view.findViewById(R.id.button_save);
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
    public boolean isItemChecked(FlightBookingAmenityViewModel selectedItem) {
        return selectedAmenity.getAmenities().contains(selectedItem);
    }

    @Override
    public void resetItemCheck() {
        selectedAmenity.setAmenities(new ArrayList<FlightBookingAmenityViewModel>());
        getAdapter().notifyDataSetChanged();
    }
}
