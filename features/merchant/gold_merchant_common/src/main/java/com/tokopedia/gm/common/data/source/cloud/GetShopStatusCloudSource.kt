package com.tokopedia.gm.common.data.source.cloud

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.gm.common.data.source.cloud.api.GMCommonApi
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel
import rx.Observable
import javax.inject.Inject

/**
 * @author by milhamj on 12/06/19.
 */
class GetShopStatusCloudSource @Inject constructor(private val gmCommonApi: GMCommonApi) {
    fun getShopStatus(shopId: String): Observable<ShopStatusModel> {
        return gmCommonApi.getShopStatus(shopId).map {
            if (!it.isSuccessful) {
                throw RuntimeException(it.code().toString())
            } else {
                if (it.body().header.messages != null
                        && it.body().header.messages.isNotEmpty()
                        && it.body().header.messages.first().isNotBlank()) {
                    throw MessageErrorException(it.body().header.messages.first())
                } else if (it.body().data == null){
                    throw RuntimeException()
                } else {
                    it.body().data
                }
            }
        }
    }
}