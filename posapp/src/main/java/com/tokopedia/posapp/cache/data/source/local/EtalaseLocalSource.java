package com.tokopedia.posapp.cache.data.source.local;

import com.tokopedia.posapp.database.manager.EtalaseDbManager;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.base.domain.model.ListDomain;
import com.tokopedia.posapp.shop.domain.model.EtalaseDomain;

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
