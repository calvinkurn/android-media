package com.tokopedia.review.feature.inbox.buyerreview.data.source

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.network.CacheUtil
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.review.common.util.ReviewInboxUtil.convertMapObjectToString
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.InboxReputationMapper
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetFirstTimeInboxReputationUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetInboxReputationUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain
import com.tokopedia.review.feature.inbox.buyerreview.network.ReputationService
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable
import rx.functions.Action1

/**
 * @author by nisie on 8/14/17.
 */
class CloudInboxReputationDataSource(
    private val reputationService: ReputationService,
    private val inboxReputationMapper: InboxReputationMapper,
    private val persistentCacheManager: PersistentCacheManager,
    private val userSessionInterface: UserSessionInterface
) {
    fun getInboxReputation(requestParams: RequestParams): Observable<InboxReputationDomain> {
        return reputationService.api.getInbox(
            AuthHelper.generateParamsNetwork(
                userSessionInterface.userId,
                userSessionInterface.deviceId,
                convertMapObjectToString(requestParams.parameters)
            )
        )
            .map(inboxReputationMapper)
            .doOnNext(saveToCache(requestParams))
    }

    private fun saveToCache(requestParams: RequestParams): Action1<InboxReputationDomain> {
        return Action1 { inboxReputationDomain: InboxReputationDomain ->
            if (inboxReputationDomain.inboxReputation.isNotEmpty()
                && isRequestNotFiltered(requestParams)
            ) {
                val key: String =
                    GetFirstTimeInboxReputationUseCase.CACHE_REPUTATION + requestParams.parameters[GetInboxReputationUseCase.PARAM_TAB]
                val value = CacheUtil.convertModelToString(
                    inboxReputationDomain,
                    object : TypeToken<InboxReputationDomain?>() {}.type
                )
                val cacheDuration: Int = GetFirstTimeInboxReputationUseCase.DURATION_CACHE
                persistentCacheManager.put(
                    key,
                    value,
                    cacheDuration
                        .toLong()
                )
            }
        }
    }

    private fun isRequestNotFiltered(requestParams: RequestParams): Boolean {
        return requestParams.getBoolean(IS_SAVE_TO_CACHE, false)
    }

    companion object {
        const val IS_SAVE_TO_CACHE = "IS_SAVE_TO_CACHE"
    }
}