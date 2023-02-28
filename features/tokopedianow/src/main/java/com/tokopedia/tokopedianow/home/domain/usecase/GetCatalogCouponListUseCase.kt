package com.tokopedia.tokopedianow.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopedianow.home.domain.model.GetCatalogCouponListResponse
import com.tokopedia.tokopedianow.home.domain.query.TokopointsCatalogWithCouponList
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetCatalogCouponListUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<GetCatalogCouponListResponse>(graphqlRepository) {

    companion object {
        private const val SUCCESS_CODE = "200"
        private const val CATEGORY_SLUG = "categorySlug"
        private const val CATALOG_SLUGS = "catalogSlugs"
    }

    init {
        setGraphqlQuery(TokopointsCatalogWithCouponList())
        setTypeClass(GetCatalogCouponListResponse::class.java)
    }

    suspend fun execute(
        categorySlug: String = String.EMPTY,
        catalogSlugs: List<String>
    ): GetCatalogCouponListResponse.TokopointsCatalogWithCouponList {
        setRequestParams(RequestParams.create().apply {
            putString(CATEGORY_SLUG, categorySlug)
            putObject(CATALOG_SLUGS, catalogSlugs)
        }.parameters)

        val response = executeOnBackground().tokopointsCatalogWithCouponList
        if(response.resultStatus.code == SUCCESS_CODE) {
            return response
        } else {
            throw MessageErrorException(response.resultStatus.message.firstOrNull().orEmpty())
        }
    }
}
