package com.tokopedia.vouchercreation.product.list.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.product.list.domain.model.response.ShopShowcasesByShopIdResponse
import javax.inject.Inject

class GetShowCasesByIdUseCase @Inject constructor(@ApplicationContext repository: GraphqlRepository)
    : GraphqlUseCase<ShopShowcasesByShopIdResponse>(repository) {

    companion object {

        private const val KEY_SHOP_ID = "shopId"
        private const val KEY_HIDE_NO_COUNT = "hideNoCount"
        private const val KEY_HIDE_SHOWCASE_GROUP = "hideShowcaseGroup"
        private const val KEY_IS_OWNER = "isOwner"

        @JvmStatic
        fun createParams(shopId: String,
                         hideNoCount: Boolean = false,
                         hideShowcaseGroup: Boolean = false,
                         isOwner: Boolean = false): RequestParams {
            return RequestParams.create().apply {
                putObject(KEY_SHOP_ID, shopId)
                putObject(KEY_HIDE_NO_COUNT, hideNoCount)
                putObject(KEY_HIDE_SHOWCASE_GROUP, hideShowcaseGroup)
                putObject(KEY_IS_OWNER, isOwner)
            }
        }
    }

    private val query = """
        query shopShowcasesByShopID(${'$'}shopId: String!, ${'$'}hideNoCount: Boolean, ${'$'}hideShowcaseGroup: Boolean, ${'$'}isOwner:Boolean) {
            shopShowcasesByShopID(shopId: ${'$'}shopId, hideNoCount: ${'$'}hideNoCount, hideShowcaseGroup: ${'$'}hideShowcaseGroup, isOwner: ${'$'}isOwner) {
                result {
                    id
                    name
                }
                error {
                    message
                }
            }
        }
    """.trimIndent()

    init {
        setGraphqlQuery(query)
        setTypeClass(ShopShowcasesByShopIdResponse::class.java)
    }
}