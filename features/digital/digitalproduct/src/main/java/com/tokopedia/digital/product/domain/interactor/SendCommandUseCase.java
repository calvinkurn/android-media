package com.tokopedia.digital.product.domain.interactor;

import com.tokopedia.digital.product.data.repository.EMoneyRepository;
import com.tokopedia.digital.product.view.model.InquiryBalanceModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by Rizky on 18/05/18.
 */
public class SendCommandUseCase extends UseCase<InquiryBalanceModel> {

    private EMoneyRepository eMoneyRepository;

    public SendCommandUseCase(EMoneyRepository eMoneyRepository) {
        this.eMoneyRepository = eMoneyRepository;
    }

    @Override
    public Observable<InquiryBalanceModel> createObservable(RequestParams requestParams) {
        return eMoneyRepository.sendCommand();
    }

}
