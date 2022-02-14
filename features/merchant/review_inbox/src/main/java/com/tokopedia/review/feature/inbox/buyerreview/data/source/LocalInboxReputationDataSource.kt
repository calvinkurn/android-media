package com.tokopedia.review.feature.inbox.buyerreview.data.source

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.network.CacheUtil
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetFirstTimeInboxReputationUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetInboxReputationUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain
import com.tokopedia.usecase.RequestParams
import rx.Observable

/**
 * @author by nisie on 9/20/17.
 */
class LocalInboxReputationDataSource(private val persistentCacheManager: PersistentCacheManager) {
    fun getInboxReputationFromCache(requestParams: RequestParams): Observable<InboxReputationDomain> {
        return Observable.just(
            GetFirstTimeInboxReputationUseCase.CACHE_REPUTATION +
                    requestParams.parameters[GetInboxReputationUseCase.PARAM_TAB]
        )
            .map { key: String ->
                if (getCache(key) != null) return@map CacheUtil.convertStringToModel(
                    getCache(key),
                    object : TypeToken<InboxReputationDomain?>() {}.type
                ) else throw RuntimeException("NO CACHE")
            }
    }

    private fun getCache(key: String): String? {
        return persistentCacheManager.getString(key)
    }
}