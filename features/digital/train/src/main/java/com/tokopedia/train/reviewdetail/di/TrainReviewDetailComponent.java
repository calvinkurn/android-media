package com.tokopedia.train.reviewdetail.di;

import com.tokopedia.train.common.di.TrainComponent;
import com.tokopedia.train.reviewdetail.presentation.fragment.TrainReviewDetailFragment;

import dagger.Component;

/**
 * Created by Rizky on 04/07/18.
 */
@TrainReviewDetailScope
@Component(dependencies = TrainComponent.class, modules = TrainReviewDetailModule.class)
public interface TrainReviewDetailComponent {

    void inject(TrainReviewDetailFragment trainReviewDetailFragment);

}