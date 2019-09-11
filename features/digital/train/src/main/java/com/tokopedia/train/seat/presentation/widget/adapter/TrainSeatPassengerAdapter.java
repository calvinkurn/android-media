package com.tokopedia.train.seat.presentation.widget.adapter;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerViewModel;

import java.util.List;

public class TrainSeatPassengerAdapter extends RecyclerView.Adapter<TrainSeatPassengerAdapter.ViewHolder> {
    private List<TrainSeatPassengerViewModel> passengerViewModels;

    public TrainSeatPassengerAdapter(List<TrainSeatPassengerViewModel> passengerViewModels) {
        this.passengerViewModels = passengerViewModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_train_seat_passenger, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(passengerViewModels.get(position));
    }

    public void setPassenger(List<TrainSeatPassengerViewModel> passengerViewModels) {
        this.passengerViewModels = passengerViewModels;
    }

    @Override
    public int getItemCount() {
        return passengerViewModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView passengerNumberTextView, nameTextView, seatTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            passengerNumberTextView = itemView.findViewById(R.id.tv_passenger_number);
            nameTextView = itemView.findViewById(R.id.tv_name);
            seatTextView = itemView.findViewById(R.id.tv_seat);
        }

        public void bind(TrainSeatPassengerViewModel viewModel) {
            passengerNumberTextView.setText("P" + viewModel.getPassengerNumber());
            nameTextView.setText(viewModel.getName());
            seatTextView.setText(String.format("%s - %s%s", viewModel.getSeatViewModel().getWagonCode(), viewModel.getSeatViewModel().getRow(), viewModel.getSeatViewModel().getColumn()));
        }
    }
}
