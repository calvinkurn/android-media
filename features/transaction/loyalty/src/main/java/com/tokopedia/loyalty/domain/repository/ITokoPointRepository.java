package com.tokopedia.loyalty.domain.repository;


import com.tokopedia.loyalty.common.TokoPointDrawerData;

import rx.Observable;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public interface ITokoPointRepository {

    Observable<TokoPointDrawerData> getPointDrawer(String requestQuery);
}
