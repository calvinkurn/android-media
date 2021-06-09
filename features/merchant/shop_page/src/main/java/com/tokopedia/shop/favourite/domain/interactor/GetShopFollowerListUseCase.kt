package com.tokopedia.shop.favourite.domain.interactor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.favourite.data.pojo.shopfollowerlist.GetShopFollowerListData
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.withContext

import javax.inject.Inject
import javax.inject.Named

/**
 * Created by normansyahputa on 2/8/18.
 */

class GetShopFollowerListUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        private val dispatchers: CoroutineDispatchers
) : UseCase<GetShopFollowerListData>() {

    companion object {

        private const val PARAM_SHOP_ID = "shopId"
        private const val PARAM_PAGE = "page"
        private const val PARAM_PER_PAGE = "perPage"
        private const val PER_PAGE = 20
        private val shopId = "\$shopId"
        private val page = "\$page"
        private val perPage = "\$perPage"
        private val query: String = """
            query GetShopFollowerList($shopId: String!, $page: Int, $perPage: Int) {
              shopFollowerList(shopID: $shopId, page: $page, perPage: $perPage) {
                data {
                  followerID
                  followerName
                  profileURL
                  photo
                }
                haveNext
                error {
                  message
                }
              }
            }
        """.trimIndent()
    }

    override suspend fun executeOnBackground(): GetShopFollowerListData = withContext(dispatchers.io) {
        val response = graphqlUseCase.executeOnBackground()
        return@withContext response.getData<GetShopFollowerListData>(GetShopFollowerListData::class.java)
    }

    fun addRequestWithParam(shopId: String, page: Int) {
        graphqlUseCase.addRequest(
                GraphqlRequest(
                        query,
                        GetShopFollowerListData::class.java,
                        mapOf(
                                PARAM_SHOP_ID to shopId,
                                PARAM_PAGE to page,
                                PARAM_PER_PAGE to PER_PAGE
                        )
                )
        )
    }

    fun clearRequest() = graphqlUseCase.clearRequest()


}
