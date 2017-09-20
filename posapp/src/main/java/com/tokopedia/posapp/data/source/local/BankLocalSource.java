package com.tokopedia.posapp.data.source.local;

import android.content.Intent;

import com.google.gson.annotations.Expose;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.posapp.database.manager.BankDbManager;
import com.tokopedia.posapp.database.manager.BankDbManager2;
import com.tokopedia.posapp.database.manager.BinDbManager;
import com.tokopedia.posapp.database.manager.BinInstallmentDbManager;
import com.tokopedia.posapp.database.manager.DbManager;
import com.tokopedia.posapp.database.manager.base.DbStatus;
import com.tokopedia.posapp.database.model.BankDb_Table;
import com.tokopedia.posapp.database.model.BinInstallmentDb;
import com.tokopedia.posapp.domain.model.bank.BankDomain;
import com.tokopedia.posapp.domain.model.bank.BankInstallmentDomain;
import com.tokopedia.posapp.domain.model.bank.BinDomain;
import com.tokopedia.posapp.domain.model.result.BankSavedResult;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/8/17.
 */

public class BankLocalSource {
    private BankDbManager bankDbManager;

    private BinDbManager binDbManager;

    private BinInstallmentDbManager binInstallmentDbManager;

    BankDbManager2 bankDbManager2;

    public BankLocalSource(BankDbManager bankDbManager) {
        this.bankDbManager = bankDbManager;
        bankDbManager2 = new BankDbManager2();
    }

    public Observable<BankSavedResult> storeBankToCache(BankInstallmentDomain data) {
        return Observable.just(data)
                .flatMap(new Func1<BankInstallmentDomain, Observable<BankSavedResult>>() {
                    @Override
                    public Observable<BankSavedResult> call(final BankInstallmentDomain bankInstallmentDomain) {
                        return bankDbManager2.store(bankInstallmentDomain.getBankDomainList())
                                .map(new Func1<DbStatus, BankSavedResult>() {
                                    @Override
                                    public BankSavedResult call(DbStatus dbStatus) {
                                        BankSavedResult result = new BankSavedResult();
                                        result.setStatus(dbStatus.isOk());
                                        result.setMessage(dbStatus.getMessage());
                                        return result;
                                    }
                                });
                    }
                });
    }

    public Observable<List<BankDomain>> getAllBank() {
        return bankDbManager2.getAllData();
    }

    public Observable<List<BankDomain>> getBanks(int offset, int limit) {
        return bankDbManager2.getListData(offset, limit);
    }

    public Observable<BankDomain> getBank(String id) {
        try {
            int bankId = Integer.parseInt(id);
            return bankDbManager2.getData(ConditionGroup.clause().and(BankDb_Table.bankId.eq(bankId)));
        } catch (Exception e) {
            return Observable.error(e);
        }
    }
}
