package com.tokopedia.train.passenger.presentation.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

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
    private LinearLayout passengerDetailLayout;
    private AppCompatTextView passengerNameTv;
    private AppCompatTextView passengerPaxTypeTv;

    public TrainPassengerViewHolder(View itemView, TrainBookingPassengerAdapterListener listener) {
        super(itemView);
        this.listener = listener;
        initView(itemView);
    }

    private void initView(View itemView) {
        headerLabel = itemView.findViewById(R.id.label_header);
        passengerDetailLayout = itemView.findViewById(R.id.passenger_detail_layout);
        passengerNameTv = itemView.findViewById(R.id.tv_passenger_name);
        passengerPaxTypeTv = itemView.findViewById(R.id.tv_passenger_pax_type);
    }

    @Override
    public void bind(TrainPassengerViewModel trainPassengerViewModel) {
        headerLabel.setTitle(trainPassengerViewModel.getHeaderTitle());
        headerLabel.setContentColorValue(itemView.getResources().getColor(R.color.green_400));
        if (trainPassengerViewModel.getName() != null) {
            passengerDetailLayout.setVisibility(View.VISIBLE);
            passengerPaxTypeTv.setVisibility(View.VISIBLE);
            headerLabel.setContent(getString(R.string.train_btn_change_passenger_data));
            passengerNameTv.setText(trainPassengerViewModel.getSalutationTitle() + " " + trainPassengerViewModel.getName());
            passengerPaxTypeTv.setText(trainPassengerViewModel.getPaxType() == TrainBookingPassenger.ADULT ?
                    getString(R.string.kai_homepage_adult_passenger) : getString(R.string.kai_homepage_adult_infant));

        } else {
            passengerDetailLayout.setVisibility(View.GONE);
            passengerPaxTypeTv.setVisibility(View.GONE);
            headerLabel.setContent(getString(R.string.train_btn_fill_passenger_data));
        }

        headerLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onChangePassengerData(trainPassengerViewModel);
            }
        });
    }
}
