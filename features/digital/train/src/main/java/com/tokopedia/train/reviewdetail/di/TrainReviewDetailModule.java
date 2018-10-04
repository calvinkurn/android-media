package com.tokopedia.train.reviewdetail.di;

import com.tokopedia.train.checkout.domain.TrainCheckoutUseCase;
import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.reviewdetail.domain.TrainCheckVoucherUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rizky on 26/07/18.
 */
@Module
class TrainReviewDetailModule {

    @Provides
    TrainCheckVoucherUseCase providesTrainCheckVoucherUseCase(TrainRepository trainRepository) {
        return new TrainCheckVoucherUseCase(trainRepository);
    }

    @Provides
    TrainCheckoutUseCase providesTrainCheckoutUseCase(TrainRepository trainRepository) {
        return new TrainCheckoutUseCase(trainRepository);
    }

}