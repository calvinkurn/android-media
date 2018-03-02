package com.tokopedia.posapp.database.manager;

import com.tokopedia.posapp.database.manager.base.PosDbOperation;
import com.tokopedia.posapp.database.model.EtalaseDb;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.shop.domain.model.EtalaseDomain;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 9/22/17.
 */

public class EtalaseDbManager extends PosDbOperation<EtalaseDomain, EtalaseDb> {

    @Override
    protected EtalaseDb mapToDb(EtalaseDomain data) {
        if(data != null) {
            EtalaseDb etalaseDb = new EtalaseDb();
            etalaseDb.setEtalaseId(data.getEtalaseId());
            etalaseDb.setEtalaseName(data.getEtalaseName());
            etalaseDb.setEtalaseAlias(data.getEtalaseAlias());
            etalaseDb.setUseAce(data.getUseAce());
            return etalaseDb;
        }
        return null;
    }

    @Override
    protected List<EtalaseDb> mapToDb(List<EtalaseDomain> data) {
        List<EtalaseDb> etalaseDbs = new ArrayList<>();
        for(EtalaseDomain etalaseDomain: data) {
            EtalaseDb etalaseDb = mapToDb(etalaseDomain);
            if(etalaseDb != null) etalaseDbs.add(etalaseDb);
        }
        return etalaseDbs;
    }

    @Override
    protected EtalaseDomain mapToDomain(EtalaseDb db) {
        if(db != null) {
            EtalaseDomain etalaseDomain = new EtalaseDomain();
            etalaseDomain.setEtalaseId(db.getEtalaseId());
            etalaseDomain.setEtalaseName(db.getEtalaseName());
            etalaseDomain.setEtalaseAlias(db.getEtalaseAlias());
            etalaseDomain.setUseAce(db.getUseAce());
            return etalaseDomain;
        }
        return null;
    }

    @Override
    protected List<EtalaseDomain> mapToDomain(List<EtalaseDb> db) {
        List<EtalaseDomain> etalaseDomains = new ArrayList<>();

        if(db != null) {
            for (EtalaseDb etalaseDb : db) {
                EtalaseDomain etalaseDomain = mapToDomain(etalaseDb);
                if (etalaseDomain != null) etalaseDomains.add(etalaseDomain);
            }
        }

        return etalaseDomains;
    }

    @Override
    protected Class<EtalaseDb> getDbClass() {
        return EtalaseDb.class;
    }

    @Override
    public Observable<DataStatus> delete(EtalaseDomain domain) {
        return null;
    }
}
