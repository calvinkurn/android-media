package com.tokopedia.feedcomponent.domain.usecase

import android.content.Context

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase

import java.util.HashMap

import javax.inject.Inject

import rx.Subscriber

/**
 * @author by yfsx on 20/06/18.
 */
class GetWhitelistUseCase @Inject
constructor(@ApplicationContext private val context: Context) : GraphqlUseCase() {

    init {
        this.setCacheStrategy(
                GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                        .setExpiryTime(GraphqlConstant.ExpiryTimes.WEEK.`val`())
                        .setSessionIncluded(true)
                        .build()
        )
    }

    fun setCacheStrategy(isAuthorEmpty:Boolean){
        val expiryTime = if(!isAuthorEmpty)
            GraphqlConstant.ExpiryTimes.MINUTE_1.`val`()
        else
            GraphqlConstant.ExpiryTimes.WEEK.`val`()
        this.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
            .setExpiryTime(expiryTime)
            .setSessionIncluded(true)
            .build())
    }

    fun getRequest(variables: HashMap<String, Any>): GraphqlRequest {
        return GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, R.raw.query_whitelist),
                WhitelistQuery::class.java,
                variables,
                false
        )
    }

    fun execute(variables: HashMap<String, Any>,
                subscriber: Subscriber<GraphqlResponse>) {
        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, R.raw.query_whitelist),
                WhitelistQuery::class.java,
                variables,
                false
        )
        this.clearRequest()
        this.addRequest(graphqlRequest)
        this.execute(subscriber)
    }

    companion object {
        private const val PARAM_TYPE = "type"
        private const val PARAM_ID = "ID"
        val WHITELIST_CONTENT_USER = "content-user"
        const val WHITELIST_ENTRY_POINT = "entrypoint"
        const val WHITELIST_SHOP = "content-shop"
        const val WHITELIST_INTEREST = "interest"

        @JvmOverloads
        fun createRequestParams(type: String, id: String = ""): HashMap<String, Any> {
            val variables = HashMap<String, Any>()
            variables[PARAM_TYPE] = type
            variables[PARAM_ID] = id
            return variables
        }
    }
}
