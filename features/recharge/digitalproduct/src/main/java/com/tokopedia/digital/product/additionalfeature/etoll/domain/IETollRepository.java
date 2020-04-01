package com.tokopedia.digital.product.additionalfeature.etoll.domain;

import com.tokopedia.digital.product.additionalfeature.etoll.data.entity.requestbody.smartcardcommand.RequestBodySmartcardCommand;
import com.tokopedia.digital.product.additionalfeature.etoll.data.entity.requestbody.smartcardinquiry.RequestBodySmartcardInquiry;
import com.tokopedia.digital.product.additionalfeature.etoll.view.model.InquiryBalanceModel;

import rx.Observable;

/**
 * Created by Rizky on 18/05/18.
 */
public interface IETollRepository {

    Observable<InquiryBalanceModel> inquiryBalance(RequestBodySmartcardInquiry requestBodySmartcardInquiry);

    Observable<InquiryBalanceModel> sendCommand(RequestBodySmartcardCommand requestBodySmartcardCommand);

}
