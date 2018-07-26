package com.tokopedia.train.checkout;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.reviewdetail.data.TrainCheckVoucherEntity;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rizky on 26/07/18.
 */
public class TrainCheckoutUseCase extends UseCase<TrainCheckoutViewModel> {

    private TrainRepository trainRepository;

    public TrainCheckoutUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Observable<TrainCheckoutViewModel> createObservable(RequestParams requestParams) {
        return trainRepository.checkVoucher(requestParams.getParameters())
                .map(new Func1<TrainCheckVoucherEntity, TrainCheckoutViewModel>() {
                    @Override
                    public TrainCheckoutViewModel call(TrainCheckVoucherEntity trainCheckVoucherEntity) {
                        return null;
                    }
                });
    }

}