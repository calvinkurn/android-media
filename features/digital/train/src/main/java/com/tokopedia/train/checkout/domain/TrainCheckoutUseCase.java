package com.tokopedia.train.checkout.domain;

import com.tokopedia.train.checkout.data.entity.TrainCheckoutEntity;
import com.tokopedia.train.checkout.presentation.model.TrainCheckoutViewModel;
import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.reviewdetail.data.entity.TrainCheckVoucherEntity;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rizky on 26/07/18.
 */
public class TrainCheckoutUseCase extends UseCase<TrainCheckoutViewModel> {

    private static final String PARAM_RESERVATION_ID = "reservationId";
    private static final String PARAM_RESERVATION_CODE = "reservationCode";
    private static final String PARAM_GALA_CODE = "galaCode";
    private static final String PARAM_CLIENT = "client";
    private static final String PARAM_VERSION = "version";

    private TrainRepository trainRepository;

    public TrainCheckoutUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Observable<TrainCheckoutViewModel> createObservable(RequestParams requestParams) {
        return trainRepository.checkout(requestParams.getParameters())
                .map(new Func1<TrainCheckoutEntity, TrainCheckoutViewModel>() {
                    @Override
                    public TrainCheckoutViewModel call(TrainCheckoutEntity trainCheckoutEntity) {
                        return new TrainCheckoutViewModel(trainCheckoutEntity.getQueryString());
                    }
                });
    }

    public RequestParams createRequestParams(String reservationId, String reservationCode,
                                             String galaCode, String client, String version) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_RESERVATION_ID, reservationId);
        requestParams.putString(PARAM_RESERVATION_CODE, reservationCode);
        requestParams.putString(PARAM_GALA_CODE, galaCode);
        requestParams.putString(PARAM_CLIENT, client);
        requestParams.putString(PARAM_VERSION, version);
        return requestParams;
    }
}