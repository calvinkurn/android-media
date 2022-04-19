package com.tokopedia.profile.following_list.domain.interactor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.profile.following_list.data.pojo.usershopfollow.GetShopFollowingData
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 2019-10-22
 */
class GetShopFollowingListUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        private val dispatchers: CoroutineDispatchers
) : UseCase<GetShopFollowingData>() {

    companion object {
        private const val KEY_PARAMS = "params"

        private const val PARAM_USER_ID = "userID"
        private const val PARAM_PAGE = "page"
        private const val PARAM_PER_PAGE = "perPage"

        private const val PER_PAGE = 10
    }

    //region query
    private val query by lazy {
        val params = "\$params"

        """
            query GetUserShopFollow($params: UserShopFollowParam!) {
                userShopFollow(input: $params) {
                    result {
                        userShopFollowDetail {
                            shopID
                            shopName
                            logo
                            url
                            stats {
                                totalShowcase
                                productSold
                            }
                            badge {
                                title
                                imageURL
                            }
                        }
                        haveNext
                        totalCount
                    }
                }
            }
        """.trimIndent()
    }
    //endregion

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