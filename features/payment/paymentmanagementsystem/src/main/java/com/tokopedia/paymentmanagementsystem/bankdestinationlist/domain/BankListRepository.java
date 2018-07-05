package com.tokopedia.paymentmanagementsystem.bankdestinationlist.domain;

import com.tokopedia.paymentmanagementsystem.bankdestinationlist.view.model.BankListModel;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 7/5/18.
 */

public interface BankListRepository {
    Observable<List<BankListModel>> getBankList();
}
