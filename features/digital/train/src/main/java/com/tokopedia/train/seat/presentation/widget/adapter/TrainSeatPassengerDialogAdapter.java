package com.tokopedia.train.seat.presentation.widget.adapter;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerViewModel;

import java.util.List;

public class TrainSeatPassengerDialogAdapter extends RecyclerView.Adapter<TrainSeatPassengerDialogAdapter.ViewHolder> {
    private List<TrainSeatPassengerViewModel> passengers;

    public TrainSeatPassengerDialogAdapter(List<TrainSeatPassengerViewModel> passengers) {
        this.passengers = passengers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_train_seat_passenger_dialog, parent, false);
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
        private AppCompatTextView tvNameTextView;
        private AppCompatTextView tvDescriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNameTextView = itemView.findViewById(R.id.tv_name);
            tvDescriptionTextView = itemView.findViewById(R.id.tv_description);
        }

        public void bind(TrainSeatPassengerViewModel trainSeatPassengerViewModel) {
            tvNameTextView.setText(trainSeatPassengerViewModel.getName());
            tvDescriptionTextView.setText(String.format(": %s/%s%s",
                    trainSeatPassengerViewModel.getSeatViewModel().getWagonCode(),
                    trainSeatPassengerViewModel.getSeatViewModel().getRow(),
                    trainSeatPassengerViewModel.getSeatViewModel().getColumn())
            );
        }
    }
}
