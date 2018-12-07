package com.tokopedia.chatbot.attachinvoice.data.repository;


import com.tokopedia.chatbot.attachinvoice.data.mapper.TkpdResponseToInvoicesDataModelMapper;
import com.tokopedia.chatbot.attachinvoice.data.model.GetInvoicePostRequest;
import com.tokopedia.chatbot.attachinvoice.domain.model.Invoice;
import com.tokopedia.chatbot.attachinvoice.domain.usecase.AttachInvoicesUseCase;
import com.tokopedia.chatbot.data.network.ChatBotApi;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Hendri on 21/03/18.
 */

public class AttachInvoicesRepository {
    private final TkpdResponseToInvoicesDataModelMapper mapper;
    private final ChatBotApi api;

    @Inject
    public AttachInvoicesRepository(ChatBotApi api, TkpdResponseToInvoicesDataModelMapper mapper) {
        this.mapper = mapper;
        this.api = api;
    }

    public Observable<List<Invoice>> getUserInvoices(Map<String, String> params) {
        //TODO NISIE
        return api.getTXOrderList(params, buildRequestBody(params)).map(mapper);
    }

    private GetInvoicePostRequest buildRequestBody(Map<String, String> params) {
        int messageId = Integer.parseInt(params.get(AttachInvoicesUseCase.Companion.getMESSAGE_ID_KEY()));
        int userId = Integer.parseInt(params.get(AttachInvoicesUseCase.Companion.getUSER_ID_KEY()));
        int page = Integer.parseInt(params.get(AttachInvoicesUseCase.Companion.getPAGE_KEY()));
        GetInvoicePostRequest body = new GetInvoicePostRequest(messageId, userId, false, page,
                AttachInvoicesUseCase.Companion.getDEFAULT_LIMIT());
        params.remove(AttachInvoicesUseCase.Companion.getMESSAGE_ID_KEY());
        params.remove(AttachInvoicesUseCase.Companion.getUSER_ID_KEY());
        params.remove(AttachInvoicesUseCase.Companion.getPAGE_KEY());
        return body;
    }
}
