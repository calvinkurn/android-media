package com.tokopedia.tkpd.tkpdcontactus.orderquery.domain;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.SubmitTicketInvoiceData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by baghira on 16/05/18.
 */

public class SubmitTicketUseCase extends UseCase<Response<TkpdResponse>> {
    ISubmitTicketRepository submitTicketRepository;
    public SubmitTicketUseCase(ISubmitTicketRepository submitTicketRepository) {
        this.submitTicketRepository = submitTicketRepository;
    }

    @Override
    public Observable<Response<TkpdResponse>> createObservable(RequestParams requestParams) {
        return submitTicketRepository.getQueryTickets(requestParams.getParameters());
    }
}
