package com.tokopedia.train.reviewdetail.domain;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.reviewdetail.data.TrainPromoEntity;
import com.tokopedia.train.reviewdetail.presentation.model.TrainCheckVoucherModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rizky on 23/07/18.
 */
public class TrainCheckVoucherUseCase extends UseCase<TrainCheckVoucherModel> {

    private final String PARAM_TRAIN_RESERVATION_ID = "PARAM_TRAIN_RESERVATION_ID";
    private final String PARAM_TRAIN_RESERVATION_CODE = "PARAM_RESERVATION_CODE";
    private final String PARAM_GALA_CODE = "PARAM_GALA_CODE";

    private TrainRepository trainRepository;

    public TrainCheckVoucherUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Observable<TrainCheckVoucherModel> createObservable(RequestParams requestParams) {
        return trainRepository.checkVoucher(requestParams.getParameters())
                .map(trainPromoEntity -> new TrainCheckVoucherModel(trainPromoEntity.getPromoCode(),
                        trainPromoEntity.getFailureMessage(), trainPromoEntity.getDiscountAmount(),
                        trainPromoEntity.getCashbackAmount(),
                        trainPromoEntity.getPromoCodeStatus().equalsIgnoreCase("success")));
    }

    public RequestParams createRequestParams(String reservationId, String reservationCode, String galaCode) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_TRAIN_RESERVATION_ID, reservationId);
        requestParams.putString(PARAM_TRAIN_RESERVATION_CODE, reservationCode);
        requestParams.putString(PARAM_GALA_CODE, galaCode);
        return requestParams;
    }

}