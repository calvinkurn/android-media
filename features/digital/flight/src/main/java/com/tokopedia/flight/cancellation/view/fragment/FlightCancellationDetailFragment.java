package com.tokopedia.flight.cancellation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationDetailPassengerAdapter;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationDetailPassengerAdapterTypeFactory;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationDetailContract;
import com.tokopedia.flight.cancellation.view.presenter.FlightCancellationDetailPresenter;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.detail.presenter.ExpandableOnClickListener;
import com.tokopedia.flight.detail.view.adapter.FlightDetailOrderAdapter;
import com.tokopedia.flight.detail.view.adapter.FlightDetailOrderTypeFactory;

import javax.inject.Inject;

/**
 * @author by furqan on 03/05/18.
 */

public class FlightCancellationDetailFragment extends BaseDaggerFragment
        implements FlightCancellationDetailContract.View, ExpandableOnClickListener {

    public static final String EXTRA_CANCELLATION_DETAIL_PASS_DATA = "EXTRA_CANCELLATION_DETAIL_PASS_DATA";
    private static final float JOURNEY_TITLE_FONT_SIZE = 16;

    @Inject
    FlightCancellationDetailPresenter presenter;

    private VerticalRecyclerView rvFlights;
    private VerticalRecyclerView rvPassengers;
    private View layoutExpendablePassenger;
    private AppCompatImageView imageExpendablePassenger;
    private AppCompatTextView txtCancellationStatus;
    private TextView txtCancellationDate;
    private TextView txtRealRefund;
    private TextView txtEstimateRefund;

    private FlightDetailOrderAdapter flightDetailOrderAdapter;
    private FlightCancellationDetailPassengerAdapter flightCancellationDetailPassengerAdapter;

    private FlightCancellationListViewModel flightCancellationListViewModel;
    private boolean isPassengerInfoShowed = true;

    public static FlightCancellationDetailFragment createInstance(FlightCancellationListViewModel flightCancellationListViewModel) {
        FlightCancellationDetailFragment fragment = new FlightCancellationDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CANCELLATION_DETAIL_PASS_DATA, flightCancellationListViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_cancellation_detail, container, false);

        layoutExpendablePassenger = view.findViewById(R.id.layout_expendable_passenger);
        imageExpendablePassenger = view.findViewById(R.id.image_expendable_passenger);
        rvFlights = view.findViewById(R.id.recycler_view_flight);
        rvPassengers = view.findViewById(R.id.recycler_view_data_passenger);
        txtRealRefund = view.findViewById(R.id.total_price);
        txtEstimateRefund = view.findViewById(R.id.estimate_refund);
        txtCancellationStatus = view.findViewById(R.id.cancellation_status);
        txtCancellationDate = view.findViewById(R.id.cancellation_date);

        FlightDetailOrderTypeFactory flightDetailOrderTypeFactory = new FlightDetailOrderTypeFactory(this, JOURNEY_TITLE_FONT_SIZE);
        flightDetailOrderAdapter = new FlightDetailOrderAdapter(flightDetailOrderTypeFactory);
        rvFlights.setAdapter(flightDetailOrderAdapter);

        FlightCancellationDetailPassengerAdapterTypeFactory flightCancellationDetailPassengerAdapterTypeFactory = new FlightCancellationDetailPassengerAdapterTypeFactory();
        flightCancellationDetailPassengerAdapter = new FlightCancellationDetailPassengerAdapter(flightCancellationDetailPassengerAdapterTypeFactory);
        rvPassengers.setAdapter(flightCancellationDetailPassengerAdapter);

        layoutExpendablePassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageExpendablePassenger.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate_reverse));
                togglePassengerInfo();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        flightCancellationListViewModel = getArguments().getParcelable(EXTRA_CANCELLATION_DETAIL_PASS_DATA);

        presenter.attachView(this);
        presenter.onViewCreated();
        renderView();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightCancellationComponent.class).inject(this);
    }


    @Override
    public void onCloseExpand(int position) {

    }

    private void renderView() {
        flightDetailOrderAdapter.addElement(flightCancellationListViewModel
                .getCancellations().getJourneys());
        flightDetailOrderAdapter.notifyDataSetChanged();

        flightCancellationDetailPassengerAdapter.addElement(flightCancellationListViewModel
                .getCancellations().getPassengers());
        flightCancellationDetailPassengerAdapter.notifyDataSetChanged();

        txtRealRefund.setText(flightCancellationListViewModel.getCancellations().getRealRefund());
        txtEstimateRefund.setText(flightCancellationListViewModel.getCancellations().getEstimatedRefund());

        txtCancellationDate.setText(FlightDateUtil.formatDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                FlightDateUtil.DEFAULT_VIEW_FORMAT,
                flightCancellationListViewModel.getCancellations().getCreateTime()));
        presenter.checkCancellationStatus();

    }

    @Override
    public FlightCancellationListViewModel getFlightCancellationList() {
        return flightCancellationListViewModel;
    }

    @Override
    public void renderCancellationStatus(int resId) {
        txtCancellationStatus.setText(getString(resId));
    }

    private void togglePassengerInfo() {
        if (isPassengerInfoShowed) {
            hidePassengerInfo();
        } else {
            showPassengerInfo();
        }
    }

    private void hidePassengerInfo() {
        isPassengerInfoShowed = false;
        rvPassengers.setVisibility(View.GONE);
        imageExpendablePassenger.setRotation(180);
    }

    private void showPassengerInfo() {
        isPassengerInfoShowed = true;
        rvPassengers.setVisibility(View.VISIBLE);
        imageExpendablePassenger.setRotation(0);
    }
}
