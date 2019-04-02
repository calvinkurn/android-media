package com.tokopedia.pms.bankdestination.data;

import com.tokopedia.pms.bankdestination.domain.BankListRepository;
import com.tokopedia.pms.bankdestination.view.model.BankListModel;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 7/5/18.
 */

public class BankListRepositoryImpl implements BankListRepository {

    private BankListDataSourceLocal bankListDataSourceLocal;

    public BankListRepositoryImpl(BankListDataSourceLocal bankListDataSourceLocal) {
        this.bankListDataSourceLocal = bankListDataSourceLocal;
    }

    @Override
    public Observable<List<BankListModel>> getBankList() {
        return bankListDataSourceLocal.getBankList();
    }
}
