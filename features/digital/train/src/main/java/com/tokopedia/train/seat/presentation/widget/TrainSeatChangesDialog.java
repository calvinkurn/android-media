package com.tokopedia.train.seat.presentation.widget;

import android.app.Activity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.design.component.Dialog;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerViewModel;
import com.tokopedia.train.seat.presentation.widget.adapter.TrainSeatPassengerDialogAdapter;

import java.util.List;

public class TrainSeatChangesDialog extends Dialog {
    private RecyclerView passengerSeatRecyclerView;
    private TextView descriptionTextView;
    private List<TrainSeatPassengerViewModel> passengers;

    public TrainSeatChangesDialog(Activity context, Type type) {
        super(context, type);
    }

    @Override
    public int layoutResId() {
        return R.layout.widget_train_seat_change_dialog;
    }

    @Override
    public void initView(View dialogView) {
        super.initView(dialogView);
        passengerSeatRecyclerView = dialogView.findViewById(R.id.rv_passenger_seats);
        descriptionTextView = dialogView.findViewById(R.id.tv_desc_dialog);
    }

    public void setPassengers(List<TrainSeatPassengerViewModel> passengers){
        TrainSeatPassengerDialogAdapter adapter = new TrainSeatPassengerDialogAdapter(passengers);
        passengerSeatRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        passengerSeatRecyclerView.setAdapter(adapter);
        descriptionTextView.setVisibility(View.GONE);
    }

}
