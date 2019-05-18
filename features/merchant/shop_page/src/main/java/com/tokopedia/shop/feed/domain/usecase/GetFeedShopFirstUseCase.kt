package com.tokopedia.shop.feed.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kolcommon.data.pojo.WhitelistQuery
import com.tokopedia.kolcommon.domain.usecase.GetWhitelistUseCase
import com.tokopedia.shop.feed.domain.DynamicFeedShopDomain
import com.tokopedia.shop.feed.domain.WhitelistDomain
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable
import rx.functions.Func1
import rx.schedulers.Schedulers
import java.util.ArrayList
import javax.inject.Inject

/**
 * @author by yfsx on 08/05/19.
 */
class GetFeedShopFirstUseCase
@Inject constructor(@ApplicationContext val context: Context,
                    val getDynamicFeedUseCase: GetDynamicFeedUseCase,
                    var getWhitelistUseCase: GetWhitelistUseCase)
    : UseCase<DynamicFeedShopDomain>() {

    companion object {
        fun createRequestParams(selfUserId: String, sourceId: String): RequestParams {
            val requestParams = GetDynamicFeedUseCase.createRequestParams(
                    selfUserId, "", GetDynamicFeedUseCase.SOURCE_SHOP, sourceId)
            requestParams.putString(GetWhitelistUseCase.WHITELIST_SHOP, sourceId)
            return requestParams
        }
    }

    override fun createObservable(requestParams: RequestParams?): Observable<DynamicFeedShopDomain> {
        return Observable.zip(
                getDynamicFeedData(requestParams),
                getWhitelistData(requestParams)
        ) {feed, whitelist -> DynamicFeedShopDomain(feed, whitelist)}
    }

    private fun getDynamicFeedData(requestParams: RequestParams?): Observable<DynamicFeedDomainModel> {
        return getDynamicFeedUseCase
                .createObservable(requestParams)
                .subscribeOn(Schedulers.io())
    }

    private fun getWhitelistData(requestParams: RequestParams?): Observable<WhitelistDomain> {
        getWhitelistUseCase.clearRequest()
        getWhitelistUseCase.addRequest(getWhitelistUseCase.getRequest(
                GetWhitelistUseCase.createRequestParams(GetWhitelistUseCase.WHITELIST_ENTRY_POINT))
        )
        return getWhitelistUseCase
                .createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .map(mapCreatePostWhitelist())
    }

    private fun mapCreatePostWhitelist(): Func1<GraphqlResponse, WhitelistDomain> {
        return Func1 { graphqlResponse ->
            val query = graphqlResponse.getData<WhitelistQuery>(WhitelistQuery::class.java)
            getWhitelistDomain(query)
        }
    }

    private fun getWhitelistDomain(query: WhitelistQuery?): WhitelistDomain? {
        if (query == null)
            return null
        else {
            val domain = WhitelistDomain()
            domain.error = query.whitelist.error ?: ""
            domain.url = query.whitelist.url ?: ""
            domain.isWhitelist = (query.whitelist.isWhitelist)
            domain.title = query.whitelist.title ?: ""
            domain.desc = query.whitelist.description ?: ""
            domain.titleIdentifier = query.whitelist.titleIdentifier ?: ""
            domain.postSuccessMessage = query.whitelist.postSuccessMessage ?: ""
            domain.image = query.whitelist.imageUrl ?: query.whitelist.imageUrl
            domain.authors = query.whitelist.authors ?: ArrayList()
            return domain
        }
    }
}