package com.tokopedia.digital.product.domain;

import com.tokopedia.digital.product.view.model.InquiryBalanceModel;

import rx.Observable;

/**
 * Created by Rizky on 18/05/18.
 */
public interface IEMoneyRepository {

    Observable<InquiryBalanceModel> inquiryBalance();

    Observable<InquiryBalanceModel> sendCommand();

}
