package com.tokopedia.train.seat.presentation.fragment.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatViewModel;

import java.util.ArrayList;
import java.util.List;

import com.tokopedia.tkpdtrain.R;

public class TrainSeatAdapter extends RecyclerView.Adapter<TrainSeatAdapter.ViewHolder> {
    private List<TrainSeatViewModel> seatMaps;
    private List<TrainSeatViewModel> selectedSeat;
    private ActionListener listener;

    public interface ActionListener {
        List<TrainSeatPassengerViewModel> getPassengers();

        void seatClicked(TrainSeatViewModel viewModel, int top, int left, int width, int height);
    }

    public TrainSeatAdapter() {
        seatMaps = new ArrayList<>();
        selectedSeat = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_train_seat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(seatMaps.get(position));
    }


    public void setSeats(List<TrainSeatViewModel> seatMaps) {
        this.seatMaps = seatMaps;
    }

    public void setSelecteds(List<TrainSeatViewModel> selectedSeat) {
        this.selectedSeat = selectedSeat;
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return seatMaps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout container;
        private AppCompatTextView labelTextView;
        private TrainSeatViewModel item;

        public ViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            labelTextView = itemView.findViewById(R.id.tv_label);
        }

        public void bind(TrainSeatViewModel viewModel) {
            item = viewModel;
            int index = TrainSeatAdapter.this.selectedSeat.indexOf(viewModel);
            if (index != -1) {
                if (listener != null) {
                    labelTextView.setText("P" + listener.getPassengers().get(index).getPassengerNumber());
                }
                labelTextView.setTextColor(itemView.getResources().getColor(R.color.white));
                container.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.bg_train_your_choice));
            } else {
                labelTextView.setTextColor(itemView.getResources().getColor(R.color.grey_500));
                if (viewModel.isAvailable()) {
                    container.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.bg_train_available));
                } else {
                    container.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.bg_train_filled));
                }
                labelTextView.setText(String.format("%d%s", viewModel.getRow(), viewModel.getColumn()));
            }

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = TrainSeatAdapter.this.selectedSeat.indexOf(item);
                    if (index != -1 || item.isAvailable()) {
                        if (listener != null) {
                            listener.seatClicked(item, itemView.getTop(), itemView.getLeft(), itemView.getWidth(), itemView.getHeight());
                        }
                    }
                }
            });
            if (viewModel.isEmpty()) {
                container.setVisibility(View.GONE);
            } else {
                container.setVisibility(View.VISIBLE);
            }
        }
    }
}
