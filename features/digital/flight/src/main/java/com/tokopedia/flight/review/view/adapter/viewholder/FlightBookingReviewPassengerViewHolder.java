package com.tokopedia.flight.review.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.constant.FlightBookingPassenger;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.cancellation.constant.FlightCancellationStatus;
import com.tokopedia.flight.review.view.model.FlightDetailPassenger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/10/17.
 */

public class FlightBookingReviewPassengerViewHolder extends AbstractViewHolder<FlightDetailPassenger> {
    @LayoutRes
    public static int LAYOUT = R.layout.item_flight_review_passenger;
    private Context context;
    private View containerView;
    private ReviewPassengerDetailAdapter reviewPassengerDetailAdapter;
    private TextView passengerNumber;
    private TextView passengerName;
    private TextView passengerCategory;
    private AppCompatTextView passengerCancellationStatus;
    private RecyclerView recyclerViewPassengerDetail;

    @Override
    public void bind(FlightDetailPassenger flightDetailPassenger) {

        if (shouldShowCancellationStatus(flightDetailPassenger.getPassengerStatus())) {
            passengerCancellationStatus.setVisibility(View.VISIBLE);
        }

        passengerNumber.setText(String.format("%d.", getAdapterPosition() + 1));
        passengerName.setText(flightDetailPassenger.getPassengerName());
        passengerCategory.setText(getPassengerType(flightDetailPassenger.getPassengerType()));
        if (flightDetailPassenger.getInfoPassengerList().size() > 0) {
            recyclerViewPassengerDetail.setVisibility(View.VISIBLE);
            reviewPassengerDetailAdapter.addData(flightDetailPassenger.getInfoPassengerList());
        } else {
            recyclerViewPassengerDetail.setVisibility(View.GONE);
        }
    }

    private String getPassengerType(int flightDetailPassenger) {
        switch (flightDetailPassenger) {
            case FlightBookingPassenger.ADULT:
                return getString(R.string.flight_label_adult_review);
            case FlightBookingPassenger.CHILDREN:
                return getString(R.string.flight_label_child_review);
            case FlightBookingPassenger.INFANT:
                return getString(R.string.flight_label_infant_review);
            default:
                return "";
        }
    }

    public FlightBookingReviewPassengerViewHolder(View layoutView) {
        super(layoutView);

        context = layoutView.getContext();

        containerView = layoutView;
        passengerNumber = (TextView) layoutView.findViewById(R.id.passenger_number);
        passengerName = (TextView) layoutView.findViewById(R.id.passenger_name);
        passengerCategory = (TextView) layoutView.findViewById(R.id.passenger_category);
        passengerCancellationStatus = layoutView.findViewById(R.id.txt_passenger_cancellation_status);
        recyclerViewPassengerDetail = (RecyclerView) layoutView.findViewById(R.id.recycler_view_passenger_detail);

        recyclerViewPassengerDetail.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        reviewPassengerDetailAdapter = new ReviewPassengerDetailAdapter();
        recyclerViewPassengerDetail.setAdapter(reviewPassengerDetailAdapter);
    }

    private boolean shouldShowCancellationStatus(int status) {
        switch (status) {
            case FlightCancellationStatus.REQUESTED:
                passengerCancellationStatus.setText(String.format(getString(
                        R.string.flight_cancellation_passenger_status), "sedang dalam pengajuan."));
                passengerCancellationStatus.setTextAppearance(context, R.style.CardProcessStatusStyle);
                passengerCancellationStatus.setBackground(context.getResources().getDrawable(R.drawable.bg_card_process));
                return true;
            case FlightCancellationStatus.PENDING:
                passengerCancellationStatus.setText(String.format(getString(
                        R.string.flight_cancellation_passenger_status), "sedang menunggu persetujuan."));
                passengerCancellationStatus.setTextAppearance(context, R.style.CardProcessStatusStyle);
                passengerCancellationStatus.setBackground(context.getResources().getDrawable(R.drawable.bg_card_process));
                return true;
            case FlightCancellationStatus.REFUNDED:
                passengerCancellationStatus.setText(String.format(getString(
                        R.string.flight_cancellation_passenger_status), "telah disetujui."));
                passengerCancellationStatus.setTextAppearance(context, R.style.CardSuccessStatusStyle);
                passengerCancellationStatus.setBackground(context.getResources().getDrawable(R.drawable.bg_card_process));
                return true;
            case FlightCancellationStatus.ABORTED:
                return false;
            default:
                return false;
        }
    }

    private class ReviewPassengerDetailAdapter extends RecyclerView.Adapter<PassengerDetailViewHolder> {
        List<SimpleViewModel> infoList;

        public ReviewPassengerDetailAdapter() {
            infoList = new ArrayList<>();
        }

        @Override
        public PassengerDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight_detail_passenger_info, parent, false);
            return new PassengerDetailViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PassengerDetailViewHolder holder, int position) {
            holder.bindData(infoList.get(position));
        }

        @Override
        public int getItemCount() {
            return infoList.size();
        }

        public void addData(List<SimpleViewModel> infos) {
            infoList.clear();
            infoList.addAll(infos);
            notifyDataSetChanged();
        }
    }

    private class PassengerDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView titleInfo;
        private TextView descInfo;

        public PassengerDetailViewHolder(View itemView) {
            super(itemView);
            titleInfo = (TextView) itemView.findViewById(R.id.title_info);
            descInfo = (TextView) itemView.findViewById(R.id.desc_info);
        }

        public void bindData(SimpleViewModel info) {
            titleInfo.setText(info.getDescription());
            descInfo.setText(info.getLabel().trim());
        }
    }
}
