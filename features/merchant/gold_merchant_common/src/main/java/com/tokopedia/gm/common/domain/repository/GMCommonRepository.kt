package com.tokopedia.gm.common.domain.repository

import rx.Observable

/**
 * Created by sebastianuskh on 3/8/17.
 */
interface GMCommonRepository {
    fun setCashback(productId: String?, cashback: Int): Observable<Boolean?>
}