package com.tokopedia.train.seat.presentation.fragment.adapter;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatViewModel;

import java.util.List;

public class TrainSeatPopupAdapter extends RecyclerView.Adapter<TrainSeatPopupAdapter.ViewHolder> {
    private String wagonCode;
    private List<TrainSeatPassengerViewModel> passengers;
    private TrainSeatViewModel seat;
    private ActionListener listener;

    public interface ActionListener {
        void onPassengerClicked(TrainSeatPassengerViewModel passenger, TrainSeatViewModel seat);
    }

    public TrainSeatPopupAdapter(String wagonCode, List<TrainSeatPassengerViewModel> passengers, TrainSeatViewModel seat) {
        this.wagonCode = wagonCode;
        this.passengers = passengers;
        this.seat = seat;
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_train_passenger_popup, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(passengers.get(position));
    }

    @Override
    public int getItemCount() {
        return passengers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout container;
        private AppCompatTextView tvPassengerNumber;
        private AppCompatTextView tvName;
        private AppCompatTextView tvSeat;
        private AppCompatImageView ivCheck;
        private TrainSeatPassengerViewModel element;

        public ViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            tvPassengerNumber = (AppCompatTextView) itemView.findViewById(R.id.tv_passenger_number);
            tvName = (AppCompatTextView) itemView.findViewById(R.id.tv_name);
            tvSeat = (AppCompatTextView) itemView.findViewById(R.id.tv_seat);
            ivCheck = (AppCompatImageView) itemView.findViewById(R.id.iv_check);
        }

        public void bind(TrainSeatPassengerViewModel viewModel) {
            element = viewModel;
            tvPassengerNumber.setText(String.format("P%d", viewModel.getPassengerNumber()));
            tvName.setText(viewModel.getName());
            tvSeat.setText(String.format("%s - %s%s", viewModel.getSeatViewModel().getWagonCode(), viewModel.getSeatViewModel().getRow(), viewModel.getSeatViewModel().getColumn()));
            if (wagonCode.equalsIgnoreCase(viewModel.getSeatViewModel().getWagonCode()) &&
                    seat.getColumn().equalsIgnoreCase(viewModel.getSeatViewModel().getColumn()) &&
                    viewModel.getSeatViewModel().getRow().equalsIgnoreCase(String.valueOf(seat.getRow()))) {
                ivCheck.setVisibility(View.VISIBLE);
                container.setBackgroundColor(itemView.getResources().getColor(R.color.train_pupup_passenger_selected));
            } else {
                container.setBackgroundColor(itemView.getResources().getColor(R.color.white));
                ivCheck.setVisibility(View.GONE);
            }
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onPassengerClicked(element, seat);
                    }
                }
            });
        }
    }
}
