package com.tokopedia.gm.common.data.repository

import com.tokopedia.gm.common.data.source.GMCommonDataSource
import com.tokopedia.gm.common.domain.repository.GMCommonRepository
import rx.Observable

/**
 * @author hendry on 4/20/17.
 */
class GMCommonRepositoryImpl(private val gmCommonDataSource: GMCommonDataSource) :
    GMCommonRepository {
    override fun setCashback(productId: String?, cashback: Int): Observable<Boolean?> {
        return gmCommonDataSource.setCashback(productId.orEmpty(), cashback)
    }
}