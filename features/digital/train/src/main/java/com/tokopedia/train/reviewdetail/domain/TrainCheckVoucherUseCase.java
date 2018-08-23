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

    private final String PARAM_TRAIN_RESERVATION_ID = "reservationId";
    private final String PARAM_TRAIN_RESERVATION_CODE = "reservationCode";
    private final String PARAM_GALA_CODE = "galaCode";

    private TrainRepository trainRepository;

    public TrainCheckVoucherUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Observable<TrainCheckVoucherModel> createObservable(RequestParams requestParams) {
        return trainRepository.checkVoucher(requestParams.getParameters())
                .map(trainCheckVoucherEntity -> new TrainCheckVoucherModel(
                        trainCheckVoucherEntity.getGalaCode(),
                        trainCheckVoucherEntity.getSuccessMessage(),
                        trainCheckVoucherEntity.getDiscountAmount(),
                        trainCheckVoucherEntity.getCashbackAmount()));
    }

    public RequestParams createRequestParams(String reservationId, String reservationCode, String galaCode) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_TRAIN_RESERVATION_ID, reservationId);
        requestParams.putString(PARAM_TRAIN_RESERVATION_CODE, reservationCode);
        requestParams.putString(PARAM_GALA_CODE, galaCode);
        return requestParams;
    }

}