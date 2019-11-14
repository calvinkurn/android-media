package com.tokopedia.kol.feature.following_list.domain.interactor

import com.tokopedia.feedcomponent.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kol.feature.following_list.data.pojo.usershopfollow.GetShopFollowingData
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by jegul on 2019-10-22
 */
class GetShopFollowingListUseCase @Inject constructor(
        @Named(QUERY_USER_SHOP_FOLLOWING) private val query: String,
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        private val dispatchers: CoroutineDispatcherProvider
) : UseCase<GetShopFollowingData>() {

    companion object {
        const val QUERY_USER_SHOP_FOLLOWING = "query_user_shop_following"

        private const val KEY_PARAMS = "params"

        private const val PARAM_USER_ID = "userID"
        private const val PARAM_PAGE = "page"
        private const val PARAM_PER_PAGE = "perPage"

        private const val PER_PAGE = 10
    }

    override suspend fun executeOnBackground(): GetShopFollowingData = withContext(dispatchers.io) {
        val response = graphqlUseCase.executeOnBackground()
        return@withContext response.getData<GetShopFollowingData>(GetShopFollowingData::class.java)
    }

    fun addRequestWithParam(userId: Int, page: Int) {
        graphqlUseCase.addRequest(
                GraphqlRequest(
                        query,
                        GetShopFollowingData::class.java,
                        mapOf(
                                KEY_PARAMS to mapOf(
                                        PARAM_USER_ID to userId,
                                        PARAM_PAGE to page,
                                        PARAM_PER_PAGE to PER_PAGE
                                )
                        )
                )
        )
    }

    fun clearRequest() = graphqlUseCase.clearRequest()
}