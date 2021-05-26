package com.tokopedia.pms.bankaccount.data.repository;

import com.tokopedia.pms.bankaccount.domain.BankListRepository;
import com.tokopedia.pms.bankaccount.data.model.BankListModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 7/5/18.
 */

public class BankListRepositoryImpl implements BankListRepository {

    private BankListDataSourceLocal bankListDataSourceLocal;

    public BankListRepositoryImpl(BankListDataSourceLocal bankListDataSourceLocal) {
        this.bankListDataSourceLocal = bankListDataSourceLocal;
    }

    @Override
    public List<BankListModel> getBankList() {
        return bankListDataSourceLocal.getBankList();
    }
}
