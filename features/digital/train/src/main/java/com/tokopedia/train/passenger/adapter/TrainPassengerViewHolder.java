package com.tokopedia.train.passenger.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.passenger.viewmodel.TrainPassengerViewModel;

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
    private AppCompatTextView passengerIdentityNumberTv;

    public TrainPassengerViewHolder(View itemView, TrainBookingPassengerAdapterListener listener) {
        super(itemView);
        this.listener = listener;
        initView(itemView);
    }

    private void initView(View itemView) {
        headerLabel = itemView.findViewById(R.id.label_header);
        passengerDetailLayout = itemView.findViewById(R.id.passenger_detail_layout);
        passengerNameTv = itemView.findViewById(R.id.tv_passenger_name);
        passengerIdentityNumberTv = itemView.findViewById(R.id.tv_passenger_identity_number);
    }

    @Override
    public void bind(TrainPassengerViewModel trainPassengerViewModel) {
        headerLabel.setTitle(trainPassengerViewModel.getHeaderTitle());
        headerLabel.setContentColorValue(itemView.getResources().getColor(R.color.colorPrimary));
        if (trainPassengerViewModel.getName() != null) {
            passengerDetailLayout.setVisibility(View.VISIBLE);
            headerLabel.setContent("Ubah");
            passengerNameTv.setText(trainPassengerViewModel.getSalutationTitle() + " " + trainPassengerViewModel.getName());

            if (trainPassengerViewModel.getIdentityNumber() != null) {
                passengerIdentityNumberTv.setVisibility(View.VISIBLE);
                passengerIdentityNumberTv.setText(String.format(
                        getString(R.string.train_passenger_label_identity_number), trainPassengerViewModel.getIdentityNumber()));
            } else {
                passengerIdentityNumberTv.setVisibility(View.GONE);
            }

        } else {
            passengerDetailLayout.setVisibility(View.GONE);
            headerLabel.setContent("Isi Data");
        }

        headerLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onChangePassengerData(trainPassengerViewModel);
            }
        });
    }
}
