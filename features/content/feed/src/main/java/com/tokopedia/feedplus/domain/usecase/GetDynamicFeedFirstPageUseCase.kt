package com.tokopedia.feedplus.domain.usecase

import android.text.TextUtils

import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedplus.domain.model.DynamicFeedFirstPageDomainModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistUseCase
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
            private val getWhitelistInterestUseCase: GetWhitelistUseCase)
    : UseCase<DynamicFeedFirstPageDomainModel>() {

    override fun createObservable(requestParams: RequestParams)
            : Observable<DynamicFeedFirstPageDomainModel> {
        return Observable.zip(
                getDynamicFeed(requestParams),
                getInterestWhitelist(requestParams)
        ) { dynamicFeedDomainModel, isInterestWhitelist ->
            DynamicFeedFirstPageDomainModel(
                    dynamicFeedDomainModel,
                    isInterestWhitelist
            )
        }
    }

    private fun getDynamicFeed(requestParams: RequestParams): Observable<DynamicFeedDomainModel> {
        return getDynamicFeedUseCase.createObservable(requestParams).subscribeOn(Schedulers.io())
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
        fun createRequestParams(userId: String, cursor: String = "",
                                source: GetDynamicFeedUseCase.FeedV2Source,
                                firstPageCursor: String = "",
                                isLogin: Boolean = true): RequestParams {
            val requestParams = GetDynamicFeedUseCase.createRequestParams(
                    userId= userId, cursor =  cursor,
                    source = source, firstPageCursor = firstPageCursor
            )
            requestParams.putBoolean(PARAM_IS_LOGIN, isLogin)
            return requestParams
        }
    }
}
