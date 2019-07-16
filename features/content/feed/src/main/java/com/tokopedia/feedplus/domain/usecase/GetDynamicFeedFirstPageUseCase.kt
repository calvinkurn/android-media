package com.tokopedia.feedplus.domain.usecase

import android.text.TextUtils

import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedplus.domain.model.DynamicFeedFirstPageDomainModel
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kolcommon.data.pojo.WhitelistQuery
import com.tokopedia.kolcommon.domain.usecase.GetWhitelistUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1
import rx.schedulers.Schedulers

/**
 * @author by milhamj on 07/01/19.
 */
class GetDynamicFeedFirstPageUseCase @Inject
constructor(private val getDynamicFeedUseCase: GetDynamicFeedUseCase,
            private val getWhitelistUseCase: GetWhitelistUseCase,
            private val getWhitelistInterestUseCase: GetWhitelistUseCase)
    : UseCase<DynamicFeedFirstPageDomainModel>() {

    override fun createObservable(requestParams: RequestParams)
            : Observable<DynamicFeedFirstPageDomainModel> {
        return Observable.zip(
                getDynamicFeed(requestParams),
                getCreatePostWhitelist(requestParams),
                getInterestWhitelist(requestParams)
        ) { dynamicFeedDomainModel, whitelistDomain, isInterestWhitelist ->
            DynamicFeedFirstPageDomainModel(
                    dynamicFeedDomainModel,
                    whitelistDomain,
                    isInterestWhitelist
            )
        }
    }

    private fun getDynamicFeed(requestParams: RequestParams): Observable<DynamicFeedDomainModel> {
        return getDynamicFeedUseCase.createObservable(requestParams).subscribeOn(Schedulers.io())
    }

    private fun getCreatePostWhitelist(requestParams: RequestParams): Observable<WhitelistDomain> {
        return if (requestParams.getBoolean(PARAM_IS_LOGIN, true)) {
            getWhitelistUseCase.clearRequest()
            getWhitelistUseCase.addRequest(getWhitelistUseCase.getRequest(
                    GetWhitelistUseCase.createRequestParams(GetWhitelistUseCase.WHITELIST_ENTRY_POINT))
            )
            getWhitelistUseCase.createObservable(RequestParams.EMPTY)
                    .subscribeOn(Schedulers.io())
                    .map(mapCreatePostWhitelist())
        } else {
            Observable.just(WhitelistDomain())
        }
    }

    private fun getInterestWhitelist(requestParams: RequestParams): Observable<Boolean> {
        return if (requestParams.getBoolean(PARAM_IS_LOGIN, true)) {
            getWhitelistInterestUseCase.clearRequest()
            getWhitelistInterestUseCase.addRequest(getWhitelistInterestUseCase.getRequest(
                    GetWhitelistUseCase.createRequestParams(GetWhitelistUseCase.WHITELIST_INTEREST))
            )
            getWhitelistInterestUseCase.createObservable(RequestParams.EMPTY)
                    .subscribeOn(Schedulers.io())
                    .map(mapInterestWhitelist())
        } else {
            Observable.just(false)
        }
    }

    private fun mapCreatePostWhitelist(): Func1<GraphqlResponse, WhitelistDomain> {
        return Func1 { graphqlResponse ->
            val query = graphqlResponse.getData<WhitelistQuery>(WhitelistQuery::class.java)
            getWhitelistDomain(query)
        }
    }

    private fun getWhitelistDomain(query: WhitelistQuery?): WhitelistDomain? {
        return if (query == null) {
            null
        } else {
            WhitelistDomain().apply {
                error = query.whitelist.error ?: ""
                url = query.whitelist.url ?: ""
                isWhitelist = query.whitelist.isWhitelist
                title = query.whitelist.title ?: ""
                desc = query.whitelist.description ?: ""
                titleIdentifier = query.whitelist.titleIdentifier ?: ""
                postSuccessMessage = query.whitelist.postSuccessMessage ?: ""
                image = query.whitelist.imageUrl ?: ""
                authors = query.whitelist.authors ?: arrayListOf()
            }
        }
    }

    private fun mapInterestWhitelist(): Func1<GraphqlResponse, Boolean> {
        return Func1 { graphqlResponse ->
            val whitelistQuery = graphqlResponse.getData<WhitelistQuery>(WhitelistQuery::class.java)
            (whitelistQuery?.whitelist != null
                    && TextUtils.isEmpty(whitelistQuery.whitelist.error)
                    && whitelistQuery.whitelist.isWhitelist)
        }
    }

    companion object {
        private const val PARAM_IS_LOGIN = "isLogin"

        @JvmOverloads
        fun createRequestParams(userId: String, cursor: String = "", source: String,
                                isLogin: Boolean = true): RequestParams {
            val requestParams = GetDynamicFeedUseCase.createRequestParams(userId, cursor, source)
            requestParams.putBoolean(PARAM_IS_LOGIN, isLogin)
            return requestParams
        }
    }
}
