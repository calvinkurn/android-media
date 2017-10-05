package com.tokopedia.posapp.database.manager;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.posapp.domain.model.DataStatus;
import com.tokopedia.posapp.database.manager.base.PosDbOperation;
import com.tokopedia.posapp.database.model.BinInstallmentDb;
import com.tokopedia.posapp.database.model.BinInstallmentDb_Table;
import com.tokopedia.posapp.domain.model.bank.BinDomain;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 9/19/17.
 */

public class BinInstallmentDbManager extends PosDbOperation<BinDomain, BinInstallmentDb>{
    @Override
    protected BinInstallmentDb mapToDb(BinDomain data) {
        if(data != null) {
            BinInstallmentDb binDb = new BinInstallmentDb();
            binDb.setBin(data.getBin());
            binDb.setBankId(data.getBankId());
            return binDb;
        }
        return null;
    }

    @Override
    protected List<BinInstallmentDb> mapToDb(List<BinDomain> data) {
        List<BinInstallmentDb> binDbs = new ArrayList<>();
        for(BinDomain binDomain: data) {
            BinInstallmentDb binDb = mapToDb(binDomain);
            if(binDb != null) binDbs.add(binDb);
        }
        return binDbs;
    }

    @Override
    protected BinDomain mapToDomain(BinInstallmentDb db) {
        if(db != null) {
            BinDomain binDomain = new BinDomain();
            binDomain.setBin(db.getBin());
            binDomain.setBankId(db.getBankId());
            return binDomain;
        }
        return null;
    }

    @Override
    protected List<BinDomain> mapToDomain(List<BinInstallmentDb> db) {
        List<BinDomain> binDomains = new ArrayList<>();
        for(BinInstallmentDb binDb : db) {
            BinDomain binDomain = mapToDomain(binDb);
            if(binDomain != null) binDomains.add(binDomain);
        }
        return binDomains;
    }

    @Override
    protected Class<BinInstallmentDb> getDbClass() {
        return BinInstallmentDb.class;
    }

    @Override
    public Observable<DataStatus> delete(BinDomain domain) {
        return executeDelete(getDbClass(), ConditionGroup.clause().and(BinInstallmentDb_Table.bankId.eq(domain.getBankId())));
    }
}
