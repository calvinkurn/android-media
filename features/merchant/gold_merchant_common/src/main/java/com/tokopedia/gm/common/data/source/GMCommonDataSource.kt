package com.tokopedia.gm.common.data.source

import javax.inject.Inject
import com.tokopedia.gm.common.data.source.cloud.GMCommonCloudDataSource
import com.tokopedia.gm.common.data.source.cloud.model.RequestCashbackModel
import com.tokopedia.network.mapper.DataResponseMapper
import rx.Observable

/**
 * @author sebastianuskh on 3/8/17.
 */
class GMCommonDataSource @Inject constructor(private val gmCommonCloudDataSource: GMCommonCloudDataSource) {
    fun setCashback(productId: String, cashback: Int): Observable<Boolean?> {
        return gmCommonCloudDataSource.setCashback(
            RequestCashbackModel(
                productId.toLong(),
                cashback
            )
        )
            .map(DataResponseMapper())
            .map { s -> s != null }
    }
}