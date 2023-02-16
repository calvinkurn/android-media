package com.tokopedia.payment.setting.list.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.payment.setting.list.model.GQLSettingBannerResponse
import com.tokopedia.payment.setting.list.model.SettingBannerModel
import com.tokopedia.payment.setting.util.GQL_GET_SETTING_BANNER
import javax.inject.Inject

@GqlQuery("GetSettingBannerQuery", GQL_GET_SETTING_BANNER)
class GetSettingBannerUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<GQLSettingBannerResponse>(graphqlRepository)  {

    fun getSettingBanner(
        onSuccess: (SettingBannerModel) -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        try {
            this.setTypeClass(GQLSettingBannerResponse::class.java)
            this.setGraphqlQuery(GetSettingBannerQuery.GQL_QUERY)
            this.setRequestParams(
                mapOf(
                    "req" to mapOf<String, Any>(
                        "cobrand_bank_id" to 1,
                        "device_type" to "ANDROID",
                    ),
                )
            )
            this.execute(
                { result ->
                    onSuccess(result.settingBannerResponse)
                }, { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }
}
