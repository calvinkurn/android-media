package com.tokopedia.posapp.data.source.local;

import com.tokopedia.posapp.database.manager.EtalaseDbManager;
import com.tokopedia.posapp.domain.model.DataStatus;
import com.tokopedia.posapp.domain.model.ListDomain;
import com.tokopedia.posapp.domain.model.shop.EtalaseDomain;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 9/25/17.
 */

public class EtalaseLocalSource {
    private EtalaseDbManager etalaseDbManager;

    public EtalaseLocalSource() {
        this.etalaseDbManager = new EtalaseDbManager();
    }

    public Observable<DataStatus> storeEtalase(ListDomain<EtalaseDomain> etalaseDomain) {
        return etalaseDbManager.store(etalaseDomain.getList());
    }

    public Observable<List<EtalaseDomain>> getAllEtalase() {
        return etalaseDbManager.getAllData();
    }

    public Observable<List<EtalaseDomain>> getListEtalase(int offset, int limit) {
        return etalaseDbManager.getListData(offset, limit);
    }
}
