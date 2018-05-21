package com.tokopedia.digital.product.data.repository;

import com.tokopedia.digital.product.data.source.InquiryBalanceDataSource;
import com.tokopedia.digital.product.data.source.SendCommandDataSource;
import com.tokopedia.digital.product.domain.IEMoneyRepository;
import com.tokopedia.digital.product.view.model.InquiryBalanceModel;

import rx.Observable;

/**
 * Created by Rizky on 18/05/18.
 */
public class EMoneyRepository implements IEMoneyRepository {

    private InquiryBalanceDataSource inquiryBalanceDataSource;
    private SendCommandDataSource sendCommandDataSource;

    public EMoneyRepository(InquiryBalanceDataSource inquiryBalanceDataSource, SendCommandDataSource sendCommandDataSource) {
        this.inquiryBalanceDataSource = inquiryBalanceDataSource;
        this.sendCommandDataSource = sendCommandDataSource;
    }

    @Override
    public Observable<InquiryBalanceModel> inquiryBalance() {
        // return mock response here
        return inquiryBalanceDataSource.inquiryBalance();
    }

    @Override
    public Observable<InquiryBalanceModel> sendCommand() {
        return sendCommandDataSource.sendCommand();
    }

}
