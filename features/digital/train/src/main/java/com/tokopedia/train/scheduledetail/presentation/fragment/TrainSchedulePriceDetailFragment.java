package com.tokopedia.train.scheduledetail.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.scheduledetail.presentation.model.TrainScheduleDetailViewModel;

/**
 * Created by Rizky on 14/05/18.
 */
public class TrainSchedulePriceDetailFragment extends Fragment {

    private TextView textTrip;
    private TextView textCountAdultPassenger;
    private TextView textAdultPrice;
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
        textCountAdultPassenger = rootview.findViewById(R.id.text_adult_count_passenger);
        textAdultPrice = rootview.findViewById(R.id.text_adult_price);
        textInfantCountPassenger = rootview.findViewById(R.id.text_infant_count_passenger);
        textInfantPrice = rootview.findViewById(R.id.text_infant_price);
        textTotalPrice = rootview.findViewById(R.id.text_total_price);

        return rootview;
    }

    public void showPrice(TrainScheduleDetailViewModel trainScheduleDetailViewModel) {
        if (trainScheduleDetailViewModel.getNumOfAdultPassenger() > 0) {
            textCountAdultPassenger.setText(getString(R.string.train_label_adult, "(" +
                    trainScheduleDetailViewModel.getNumOfAdultPassenger() + " x " + "Rp" +
                    trainScheduleDetailViewModel.getDisplayAdultFare() + ")"));
//            textCountAdultPassenger.setText("Tiket Dewasa (" + trainScheduleDetailViewModel.getNumOfAdultPassenger() + " x " +
//                    trainScheduleDetailViewModel.getDisplayAdultFare() + ")");
            double totalAdultPrice = trainScheduleDetailViewModel.getNumOfAdultPassenger() *
                    trainScheduleDetailViewModel.getAdultFare();
            textAdultPrice.setText(getString(R.string.train_label_currency,
                    CurrencyFormatUtil.getThousandSeparatorString(totalAdultPrice,
                            false, 0).getFormattedString()));
        }
    }

}