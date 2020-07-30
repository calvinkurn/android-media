package com.tokopedia.digital.product.additionalfeature.etoll.data.repository;

import com.tokopedia.digital.product.additionalfeature.etoll.data.entity.requestbody.smartcardcommand.RequestBodySmartcardCommand;
import com.tokopedia.digital.product.additionalfeature.etoll.data.entity.requestbody.smartcardinquiry.RequestBodySmartcardInquiry;
import com.tokopedia.digital.product.additionalfeature.etoll.data.source.SmartcardCommandDataSource;
import com.tokopedia.digital.product.additionalfeature.etoll.data.source.SmartcardInquiryDataSource;
import com.tokopedia.digital.product.additionalfeature.etoll.domain.IETollRepository;
import com.tokopedia.digital.product.additionalfeature.etoll.view.model.InquiryBalanceModel;

import rx.Observable;

/**
 * Created by Rizky on 18/05/18.
 */
public class ETollRepository implements IETollRepository {

    private SmartcardInquiryDataSource smartcardInquiryDataSource;
    private SmartcardCommandDataSource smartcardCommandDataSource;

    public ETollRepository(SmartcardInquiryDataSource smartcardInquiryDataSource, SmartcardCommandDataSource smartcardCommandDataSource) {
        this.smartcardInquiryDataSource = smartcardInquiryDataSource;
        this.smartcardCommandDataSource = smartcardCommandDataSource;
    }

    @Override
    public Observable<InquiryBalanceModel> inquiryBalance(RequestBodySmartcardInquiry requestBodySmartcardInquiry) {
        return smartcardInquiryDataSource.inquiryBalance(requestBodySmartcardInquiry);
    }

    @Override
    public Observable<InquiryBalanceModel> sendCommand(RequestBodySmartcardCommand requestBodySmartcardCommand) {
        return smartcardCommandDataSource.sendCommand(requestBodySmartcardCommand);
    }

}
