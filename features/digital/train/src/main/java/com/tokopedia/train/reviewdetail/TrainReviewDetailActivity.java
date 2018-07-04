package com.tokopedia.train.reviewdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.train.common.presentation.TrainBaseActivity;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;

/**
 * Created by Rizky on 01/07/18.
 */
public class TrainReviewDetailActivity extends TrainBaseActivity {

    public static String EXTRA_TRAIN_SOFTBOOK = "EXTRA_TRAIN_SOFTBOOK";

    private TrainSoftbook trainSoftbook;

    public static Intent createIntent(Context context,
                                      TrainSoftbook trainSoftbook) {
        Intent intent = new Intent(context, TrainReviewDetailActivity.class);
        intent.putExtra(EXTRA_TRAIN_SOFTBOOK, trainSoftbook);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        trainSoftbook = getIntent().getParcelableExtra(EXTRA_TRAIN_SOFTBOOK);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return TrainReviewDetailFragment.newInstance(trainSoftbook);
    }

}