package com.tokopedia.digital.product.additionalfeature.etoll.domain.interactor;

import com.tokopedia.digital.product.additionalfeature.etoll.data.repository.ETollRepository;
import com.tokopedia.digital.product.additionalfeature.etoll.view.model.InquiryBalanceModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by Rizky on 18/05/18.
 */
public class SendCommandUseCase extends UseCase<InquiryBalanceModel> {

    private ETollRepository eTollRepository;

    public SendCommandUseCase(ETollRepository eTollRepository) {
        this.eTollRepository = eTollRepository;
    }

    @Override
    public Observable<InquiryBalanceModel> createObservable(RequestParams requestParams) {
        return eTollRepository.sendCommand();
    }

}
