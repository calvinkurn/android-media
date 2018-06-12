package com.tokopedia.train.scheduledetail.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.tkpdtrain.R;

/**
 * Created by Rizky on 14/05/18.
 */
public class TrainSchedulePriceDetailFragment extends Fragment {

    public static Fragment createInstance() {
        TrainSchedulePriceDetailFragment fragment = new TrainSchedulePriceDetailFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_train_schedule_price_detail, container, false);

        return rootview;
    }

}
