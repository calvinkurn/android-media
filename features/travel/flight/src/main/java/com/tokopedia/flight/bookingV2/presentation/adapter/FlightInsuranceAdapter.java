package com.tokopedia.flight.bookingV2.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.flight.bookingV2.presentation.model.FlightInsuranceModel;
import com.tokopedia.flight.bookingV2.presentation.widget.FlightInsuranceView;

import java.util.List;

public class FlightInsuranceAdapter extends RecyclerView.Adapter<FlightInsuranceAdapter.ViewHolder> {
    private List<FlightInsuranceModel> viewModels;
    private FlightInsuranceView.ActionListener actionListener;

    public FlightInsuranceAdapter(List<FlightInsuranceModel> viewModels) {
        this.viewModels = viewModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.tokopedia.flight.R.layout.item_flight_insurance, parent, false);
        return new ViewHolder(view, actionListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(viewModels.get(position));
    }

    public void setActionListener(FlightInsuranceView.ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private FlightInsuranceView flightInsuranceView;

        public ViewHolder(View itemView, FlightInsuranceView.ActionListener actionListener) {
            super(itemView);
            flightInsuranceView = itemView.findViewById(com.tokopedia.flight.R.id.iv_insurance);
            flightInsuranceView.setListener(actionListener);
        }

        public void bind(FlightInsuranceModel flightInsuranceViewModel) {
            flightInsuranceView.renderData(flightInsuranceViewModel);
        }
    }
}
