package com.tokopedia.train.reviewdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import com.tokopedia.train.common.presentation.TrainBaseActivity;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rizky on 01/07/18.
 */
public class TrainReviewDetailActivity extends TrainBaseActivity {

    private List<TrainSeatPassengerViewModel> trainSeatPassengerViewModelList;

    public static Intent createIntent(Context context,
                                      List<TrainSeatPassengerViewModel> trainSeatPassengerViewModelList) {
        Intent intent = new Intent(context, TrainReviewDetailActivity.class);
        intent.putParcelableArrayListExtra("", (ArrayList<TrainSeatPassengerViewModel>) trainSeatPassengerViewModelList);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return TrainReviewDetailFragment.newInstance(trainSeatPassengerViewModelList);
    }

}