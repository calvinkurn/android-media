package com.tokopedia.posapp.bank.data.source.local;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.database.model.BankDb_Table;
import com.tokopedia.posapp.bank.domain.model.BankDomain;
import com.tokopedia.posapp.bank.domain.model.BankInstallmentDomain;
import com.tokopedia.posapp.bank.domain.model.BankSavedResult;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/8/17.
 */

public class BankLocalSource {
    private BankDbManager bankDbManager;

    public BankLocalSource() {
        bankDbManager = new BankDbManager();
    }

    public Observable<BankSavedResult> storeBankToCache(BankInstallmentDomain data) {
        return Observable.just(data)
                .flatMap(new Func1<BankInstallmentDomain, Observable<BankSavedResult>>() {
                    @Override
                    public Observable<BankSavedResult> call(final BankInstallmentDomain bankInstallmentDomain) {
                        return bankDbManager.store(bankInstallmentDomain.getBankDomainList())
                                .map(new Func1<DataStatus, BankSavedResult>() {
                                    @Override
                                    public BankSavedResult call(DataStatus dataStatus) {
                                        BankSavedResult result = new BankSavedResult();
                                        result.setStatus(dataStatus.isOk());
                                        result.setMessage(dataStatus.getMessage());
                                        return result;
                                    }
                                });
                    }
                });
    }

    public Observable<List<BankDomain>> getAllBank() {
        return bankDbManager.getAllData();
    }

    public Observable<List<BankDomain>> getBanks(int offset, int limit) {
        return bankDbManager.getListData(offset, limit);
    }

    public Observable<BankDomain> getBank(String id) {
        try {
            int bankId = Integer.parseInt(id);
            return bankDbManager.getData(ConditionGroup.clause().and(BankDb_Table.bankId.eq(bankId)));
        } catch (Exception e) {
            return Observable.error(e);
        }
    }
}
