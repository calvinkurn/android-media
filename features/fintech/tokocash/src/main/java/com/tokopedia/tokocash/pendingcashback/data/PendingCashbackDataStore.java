package com.tokopedia.tokocash.pendingcashback.data;

import com.tokopedia.tokocash.activation.data.entity.PendingCashbackEntity;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/1/18.
 */

public interface PendingCashbackDataStore {

    Observable<PendingCashbackEntity> getPendingCashback(HashMap<String, String> mapParam);
}
