package com.tokopedia.train.reviewdetail.domain;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.reviewdetail.presentation.model.TrainCheckVoucherModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by Rizky on 23/07/18.
 */
public class TrainCheckVoucherUseCase extends UseCase<TrainCheckVoucherModel> {

    private TrainRepository trainRepository;

    public TrainCheckVoucherUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Observable<TrainCheckVoucherModel> createObservable(RequestParams requestParams) {
        return null;
    }

    public RequestParams createRequestParams(String reservationId, String reservationCode, String galaCode) {
        RequestParams requestParams = RequestParams.create();
        return requestParams;
    }

}