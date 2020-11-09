package com.tokopedia.gm.common.domain.repository;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface GMCommonRepository {

    Observable<Boolean> setCashback(String string, int cashback);

}