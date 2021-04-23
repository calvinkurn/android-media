package com.tokopedia.loginregister.common.view.banner.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.loginregister.common.data.DynamicBannerConstant
import com.tokopedia.loginregister.common.data.model.DynamicBannerDataModel
import com.tokopedia.loginregister.common.view.banner.domain.query.QueryDynamicBanner
import javax.inject.Inject

/**
 * @author rival
 * @created on 20/02/2020
 */

class DynamicBannerUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase
) : BaseDynamicBannerUseCase<DynamicBannerDataModel>() {

    override suspend fun executeOnBackground(): DynamicBannerDataModel {
        val request = GraphqlRequest(QueryDynamicBanner.getQuery(), DynamicBannerDataModel::class.java, params)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(request)
        return graphqlUseCase.executeOnBackground().run {
            getData<DynamicBannerDataModel>(DynamicBannerDataModel::class.java)
        }
    }

    companion object {
        fun createRequestParams(page: String): Map<String, Any> {
            return mapOf(
                    DynamicBannerConstant.Params.PAGE to page
            )
        }
    }

}