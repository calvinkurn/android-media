package com.tokopedia.checkout.domain.usecase;

import com.tokopedia.checkout.domain.datamodel.toppay.ThanksTopPayData;
import com.tokopedia.checkout.domain.mapper.ITopPayMapper;
import com.tokopedia.transactiondata.repository.ITopPayRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 06/03/18.
 */

public class GetThanksToppayUseCase extends UseCase<ThanksTopPayData> {
    public static final String PARAM_REQUEST_AUTH_MAP_STRING = "PARAM_REQUEST_AUTH_MAP_STRING";
    public static final String PARAM_TRANSACTION_ID = "id";
    private final ITopPayRepository topPayRepository;
    private final ITopPayMapper topPayMapper;

    @Inject
    public GetThanksToppayUseCase(ITopPayRepository topPayRepository, ITopPayMapper topPayMapper) {
        this.topPayRepository = topPayRepository;
        this.topPayMapper = topPayMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<ThanksTopPayData> createObservable(RequestParams requestParams) {
        return topPayRepository.getThanksTopPay(requestParams.getParamsAllValueInString())
                .map(new Func1<com.tokopedia.transactiondata.entity.response.thankstoppaydata.ThanksTopPayData,
                        ThanksTopPayData>() {
                    @Override
                    public ThanksTopPayData call(
                            com.tokopedia.transactiondata.entity.response.thankstoppaydata.ThanksTopPayData
                                    thanksTopPayData) {
                        return topPayMapper.convertThanksTopPayData(thanksTopPayData);
                    }
                });
    }
}
