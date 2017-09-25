package com.tokopedia.posapp.database.manager;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.posapp.domain.model.DataStatus;
import com.tokopedia.posapp.database.manager.base.PosDbOperation;
import com.tokopedia.posapp.database.model.BinDb;
import com.tokopedia.posapp.database.model.BinDb_Table;
import com.tokopedia.posapp.domain.model.bank.BinDomain;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 9/19/17.
 */

public class BinDbManager extends PosDbOperation<BinDomain, BinDb> {
    @Override
    protected BinDb mapToDb(BinDomain data) {
        if(data != null) {
            BinDb binDb = new BinDb();
            binDb.setBin(data.getBin());
            binDb.setBankId(data.getBankId());
            return binDb;
        }
        return null;
    }

    @Override
    protected List<BinDb> mapToDb(List<BinDomain> data) {
        List<BinDb> binDbs = new ArrayList<>();
        for(BinDomain binDomain: data) {
            BinDb binDb = mapToDb(binDomain);
            if(binDb != null) binDbs.add(binDb);
        }
        return binDbs;
    }

    @Override
    protected BinDomain mapToDomain(BinDb db) {
        if(db != null) {
            BinDomain binDomain = new BinDomain();
            binDomain.setBin(db.getBin());
            binDomain.setBankId(db.getBankId());
            return binDomain;
        }
        return null;
    }

    @Override
    protected List<BinDomain> mapToDomain(List<BinDb> db) {
        List<BinDomain> binDomains = new ArrayList<>();
        for(BinDb binDb : db) {
            BinDomain binDomain = mapToDomain(binDb);
            if(binDomain != null) binDomains.add(binDomain);
        }
        return binDomains;
    }

    @Override
    protected Class<BinDb> getDbClass() {
        return BinDb.class;
    }

    @Override
    public Observable<DataStatus> delete(BinDomain domain) {
        return executeDelete(getDbClass(), ConditionGroup.clause().and(BinDb_Table.bankId.eq(domain.getBankId())));
    }
}
