package com.tokopedia.flight.detail.view.adapter;

import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.detail.presenter.ExpandableOnClickListener;
import com.tokopedia.flight.detail.view.model.FlightDetailOrderJourney;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteInfoModelMapper;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailRouteViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 12/13/17.
 */

public class FlightDetailOrderViewHolder extends AbstractViewHolder<FlightDetailOrderJourney> {
    @LayoutRes
    public static final int LAYOUT = com.tokopedia.flight.R.layout.item_flight_detail_order;
    private TextView flightCounter;
    private View journeyView;
    private TextView titleJourney;
    private AppCompatImageView imageJourney;
    private RecyclerView recyclerViewFlightJourney;
    private TextView cekSyaratText;
    private View separatorLine;
    private FlightDetailAdapter flightDetailAdapter;
    private FlightDetailOrderJourney flightOrderJourney;
    private FlightDetailRouteInfoModelMapper flightDetailRouteInfoViewModelMapper;

    private ExpandableOnClickListener expandableOnClickListener;

    private boolean isFlightInfoShowed = true;

    public FlightDetailOrderViewHolder(final View layoutView, ExpandableOnClickListener expandableOnClickListener, float titleFontSize) {
        super(layoutView);
        this.expandableOnClickListener = expandableOnClickListener;
        flightDetailRouteInfoViewModelMapper = new FlightDetailRouteInfoModelMapper();

        flightCounter = layoutView.findViewById(com.tokopedia.flight.R.id.counter_flight);
        journeyView = layoutView.findViewById(com.tokopedia.flight.R.id.layout_expendable_flight);
        titleJourney = layoutView.findViewById(com.tokopedia.flight.R.id.title_expendable_passenger);
        imageJourney = layoutView.findViewById(com.tokopedia.flight.R.id.image_expendable_journey);
        recyclerViewFlightJourney = layoutView.findViewById(com.tokopedia.flight.R.id.recycler_view_flight_detail_journey);
        cekSyaratText = layoutView.findViewById(com.tokopedia.flight.R.id.text_view_flight_cek_syarat);
        separatorLine = layoutView.findViewById(com.tokopedia.flight.R.id.flight_detail_order_separator_line);
        FlightDetailRouteTypeFactory detailRouteTypeFactory = new FlightDetailAdapterTypeFactory(
                new FlightDetailAdapterTypeFactory.OnFlightDetailListener() {
                    @Override
                    public int getItemCount() {
                        if (flightOrderJourney != null) {
                            return flightOrderJourney.getRouteViewModels().size();
                        }
                        return 0;
                    }
                }, true);
        flightDetailAdapter = new FlightDetailAdapter(detailRouteTypeFactory, new ArrayList<Visitable>());
        recyclerViewFlightJourney.setAdapter(flightDetailAdapter);

        journeyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageJourney.startAnimation(AnimationUtils.loadAnimation(layoutView.getContext(), R.anim.flight_rotate_reverse));
                toggleFlightInfo();
            }
        });

        flightCounter.setTextSize(titleFontSize);

    }

    @Override
    public void bind(FlightDetailOrderJourney flightOrderJourney) {
        this.flightOrderJourney = flightOrderJourney;
        flightCounter.setText(itemView.getContext().getString(com.tokopedia.flight.R.string.flight_label_detail_counter, getAdapterPosition() + 1));
        titleJourney.setText(itemView.getContext().getString(com.tokopedia.flight.R.string.flight_label_detail_format,
                flightOrderJourney.getDepartureCity(), flightOrderJourney.getDepartureAiportId(), flightOrderJourney.getArrivalCity(), flightOrderJourney.getArrivalAirportId()));
        List<Visitable> visitables = new ArrayList<>();
        visitables.addAll(transform(this.flightOrderJourney.getRouteViewModels()));
        flightDetailAdapter.addElement(visitables);
        flightDetailAdapter.notifyDataSetChanged();
    }

    private void toggleFlightInfo() {
        if (isFlightInfoShowed) {
            hideFlightInfo();
        } else {
            showFlightInfo();
        }
    }

    private void hideFlightInfo() {
        isFlightInfoShowed = false;
        recyclerViewFlightJourney.setVisibility(View.GONE);
        cekSyaratText.setVisibility(View.GONE);
        separatorLine.setVisibility(View.GONE);
        imageJourney.setRotation(180);
    }

    private void showFlightInfo() {
        isFlightInfoShowed = true;
        recyclerViewFlightJourney.setVisibility(View.VISIBLE);
        cekSyaratText.setVisibility(View.VISIBLE);
        separatorLine.setVisibility(View.VISIBLE);
        imageJourney.setRotation(0);
        expandableOnClickListener.onCloseExpand(getAdapterPosition());
    }

    private List<FlightDetailRouteModel> transform(List<FlightOrderDetailRouteViewModel> routeList) {
        List<FlightDetailRouteModel> routeViewModelList = new ArrayList<>();

        for (FlightOrderDetailRouteViewModel item : routeList) {
            FlightDetailRouteModel viewModel = new FlightDetailRouteModel();
            viewModel.setPnr(item.getPnr());
            viewModel.setAirlineName(item.getAirlineName());
            viewModel.setAirlineCode(item.getAirlineCode());
            viewModel.setAirlineLogo(item.getAirlineLogo());
            viewModel.setFlightNumber(item.getFlightNumber());
            viewModel.setDepartureTimestamp(item.getDepartureTimestamp());
            viewModel.setDepartureAirportCity(item.getDepartureAirportCity());
            viewModel.setDepartureAirportCode(item.getDepartureAirportCode());
            viewModel.setDepartureAirportName(item.getDepartureAirportName());
            viewModel.setDepartureTerminal(item.getDepartureTerminal());
            viewModel.setRefundable(item.isRefundable());
            viewModel.setDuration(item.getDuration());
            viewModel.setArrivalTimestamp(item.getArrivalTimestamp());
            viewModel.setArrivalAirportCity(item.getArrivalAirportCity());
            viewModel.setArrivalAirportCode(item.getArrivalAirportCode());
            viewModel.setArrivalAirportName(item.getArrivalAirportName());
            viewModel.setArrivalTerminal(item.getArrivalTerminal());
            viewModel.setLayover(item.getLayover());
            viewModel.setStopOver(item.getStopOver());
            viewModel.setInfos(flightDetailRouteInfoViewModelMapper.transformOrderInfo(item.getInfos()));
            viewModel.setAmenities(flightDetailRouteInfoViewModelMapper.transformOrderAmenities(item.getAmenities()));
            viewModel.setStopOverDetail(item.getStopOverDetail());

            routeViewModelList.add(viewModel);
        }

        return routeViewModelList;
    }
}
