package com.tokopedia.train.checkout.domain;

import com.tokopedia.train.checkout.presentation.model.TrainCheckoutViewModel;
import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

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
                .map(trainCheckoutEntity -> new TrainCheckoutViewModel(
                        trainCheckoutEntity.getRedirectURL(),
                        trainCheckoutEntity.getCallbackURLSuccess(),
                        trainCheckoutEntity.getCallbackURLFailed(),
                        trainCheckoutEntity.getQueryString(),
                        trainCheckoutEntity.getParameter().getTransactionId()));
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