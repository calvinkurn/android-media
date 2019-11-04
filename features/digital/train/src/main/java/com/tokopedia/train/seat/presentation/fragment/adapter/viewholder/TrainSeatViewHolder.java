package com.tokopedia.train.seat.presentation.fragment.adapter.viewholder;

import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.seat.presentation.fragment.adapter.TrainSeatAdapterTypeFactory;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatViewModel;

public class TrainSeatViewHolder extends AbstractViewHolder<TrainSeatViewModel> {
    public static final int LAYOUT = R.layout.item_train_seat;
    private LinearLayout container;
    private AppCompatTextView labelTextView;
    private TrainSeatViewModel item;

    private TrainSeatAdapterTypeFactory.ActionListener listener;

    public TrainSeatViewHolder(View itemView, TrainSeatAdapterTypeFactory.ActionListener listener) {
        super(itemView);
        container = itemView.findViewById(R.id.container);
        labelTextView = itemView.findViewById(R.id.tv_label);
        this.listener = listener;
    }

    @Override
    public void bind(TrainSeatViewModel viewModel) {
        item = viewModel;

        setupItemView(viewModel);
        setupListener(viewModel);
        setVisibilityIfCurrentIsGap(viewModel);
    }

    private void setupListener(TrainSeatViewModel viewModel) {
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = listener.getSelectedSeatInCurrentWagon().indexOf(viewModel);
                if (index != -1 || item.isAvailable()) {
                    if (listener != null) {
                        listener.seatClicked(item, getAdapterPosition(), itemView.getHeight());
                    }
                }
            }
        });
    }

    private void setVisibilityIfCurrentIsGap(TrainSeatViewModel viewModel) {
        if (viewModel.isEmpty()) {
            container.setVisibility(View.GONE);
        } else {
            container.setVisibility(View.VISIBLE);
        }
    }

    private void setupItemView(TrainSeatViewModel viewModel) {
        int passengerNumber = findPassengerNumber(viewModel);

        if (passengerNumber != -1) {
            if (listener != null) {
                labelTextView.setText(String.format("P%d", passengerNumber));
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
    }

    private int findPassengerNumber(TrainSeatViewModel viewModel) {
        int passengerNumber = -1;
        String wagonCode = listener.getWagonCode();
        for (TrainSeatPassengerViewModel trainSeatViewModel : listener.getPassengers()) {
            if (trainSeatViewModel.getSeatViewModel().getWagonCode().equalsIgnoreCase(wagonCode) &&
                    trainSeatViewModel.getSeatViewModel().getRow().equalsIgnoreCase(String.valueOf(viewModel.getRow())) &&
                    trainSeatViewModel.getSeatViewModel().getColumn().equalsIgnoreCase(String.valueOf(viewModel.getColumn()))) {
                passengerNumber = trainSeatViewModel.getPassengerNumber();
                break;
            }
        }
        return passengerNumber;
    }
}
