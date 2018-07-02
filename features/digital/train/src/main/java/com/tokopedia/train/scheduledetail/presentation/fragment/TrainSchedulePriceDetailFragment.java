package com.tokopedia.train.scheduledetail.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.scheduledetail.presentation.model.TrainScheduleDetailViewModel;

/**
 * Created by Rizky on 14/05/18.
 */
public class TrainSchedulePriceDetailFragment extends Fragment {

    private TextView textTrip;
    private LinearLayout containerAdultPrice;
    private TextView textAdultCountPassenger;
    private TextView textAdultPrice;
    private LinearLayout containerInfantPrice;
    private TextView textInfantCountPassenger;
    private TextView textInfantPrice;
    private TextView textTotalPrice;

    public static Fragment createInstance() {
        TrainSchedulePriceDetailFragment fragment = new TrainSchedulePriceDetailFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_train_schedule_price_detail, container, false);

        textTrip = rootview.findViewById(R.id.text_trip);
        containerAdultPrice = rootview.findViewById(R.id.container_adult_price);
        textAdultCountPassenger = rootview.findViewById(R.id.text_adult_count_passenger);
        textAdultPrice = rootview.findViewById(R.id.text_adult_price);
        containerInfantPrice = rootview.findViewById(R.id.container_infant_price);
        textInfantCountPassenger = rootview.findViewById(R.id.text_infant_count_passenger);
        textInfantPrice = rootview.findViewById(R.id.text_infant_price);
        textTotalPrice = rootview.findViewById(R.id.text_total_price);

        return rootview;
    }

    public void showPrice(TrainScheduleDetailViewModel trainScheduleDetailViewModel) {
        if (trainScheduleDetailViewModel.isOneWay()) {
            textTrip.setText(getString(R.string.train_departure_trip,
                    trainScheduleDetailViewModel.getOriginStationCode(),
                    trainScheduleDetailViewModel.getDestinationStationCode()));
        } else {
            textTrip.setText(getString(R.string.train_return_trip,
                    trainScheduleDetailViewModel.getOriginStationCode(),
                    trainScheduleDetailViewModel.getDestinationStationCode()));

        }
        if (trainScheduleDetailViewModel.getNumOfAdultPassenger() > 0) {
            textAdultCountPassenger.setText(getString(R.string.train_label_adult,
                    trainScheduleDetailViewModel.getNumOfAdultPassenger(),
                    trainScheduleDetailViewModel.getDisplayAdultFare()));
            textAdultPrice.setText(getString(R.string.train_label_currency,
                    CurrencyFormatUtil.getThousandSeparatorString(trainScheduleDetailViewModel.getTotalAdultFare(),
                            false, 0).getFormattedString()));
        } else {
            containerAdultPrice.setVisibility(View.GONE);
        }
        if (trainScheduleDetailViewModel.getNumOfInfantPassenger() > 0) {
            textInfantCountPassenger.setText(getString(R.string.train_label_infant,
                    trainScheduleDetailViewModel.getNumOfInfantPassenger(),
                    trainScheduleDetailViewModel.getDisplayInfantFare()));
            textInfantPrice.setText(getString(R.string.train_label_currency,
                    CurrencyFormatUtil.getThousandSeparatorString(trainScheduleDetailViewModel.getTotalInfantFare(),
                            false, 0).getFormattedString()));
        } else {
            containerInfantPrice.setVisibility(View.GONE);
        }
        textTotalPrice.setText(getString(R.string.train_label_currency,
                CurrencyFormatUtil.getThousandSeparatorString(trainScheduleDetailViewModel.getTotalPrice(),
                        false, 0).getFormattedString()));
    }

}