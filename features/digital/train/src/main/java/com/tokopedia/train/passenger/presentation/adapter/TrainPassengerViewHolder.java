package com.tokopedia.train.passenger.presentation.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.RelativeLayout;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.passenger.data.TrainBookingPassenger;
import com.tokopedia.train.passenger.presentation.viewmodel.TrainPassengerViewModel;

/**
 * Created by nabillasabbaha on 25/06/18.
 */
public class TrainPassengerViewHolder extends AbstractViewHolder<TrainPassengerViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_train_passenger;

    private TrainBookingPassengerAdapterListener listener;
    private LabelView headerLabel;
    private AppCompatTextView passengerPaxTypeTv;
    private RelativeLayout itemPassengerLayout;

    public TrainPassengerViewHolder(View itemView, TrainBookingPassengerAdapterListener listener) {
        super(itemView);
        this.listener = listener;
        initView(itemView);
    }

    private void initView(View itemView) {
        headerLabel = itemView.findViewById(R.id.label_header);
        passengerPaxTypeTv = itemView.findViewById(R.id.tv_passenger_pax_type);
        itemPassengerLayout = itemView.findViewById(R.id.item_passenger_layout);
    }

    @Override
    public void bind(TrainPassengerViewModel trainPassengerViewModel) {
        headerLabel.setContentColorValue(itemView.getResources().getColor(R.color.green_400));
        if (trainPassengerViewModel.getName() != null) {
            passengerPaxTypeTv.setVisibility(View.VISIBLE);
            passengerPaxTypeTv.setText(trainPassengerViewModel.getPaxType() == TrainBookingPassenger.ADULT ?
                    getString(R.string.kai_homepage_adult_passenger) : getString(R.string.kai_homepage_infant_passenger));
            headerLabel.setTitle(trainPassengerViewModel.getSalutationTitle() + " " + trainPassengerViewModel.getName());
            headerLabel.setContent(getString(R.string.train_btn_change_passenger_data));
            headerLabel.setPadding(0,16, 0, 0);
        } else {
            passengerPaxTypeTv.setVisibility(View.GONE);
            headerLabel.setTitle(trainPassengerViewModel.getHeaderTitle());
            headerLabel.setContent(getString(R.string.train_btn_fill_passenger_data));
        }

        itemPassengerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onChangePassengerData(trainPassengerViewModel);
            }
        });
    }
}
