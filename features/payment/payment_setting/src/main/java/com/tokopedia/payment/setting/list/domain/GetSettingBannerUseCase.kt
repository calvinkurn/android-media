package com.tokopedia.payment.setting.list.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.payment.setting.list.model.GQLSettingBannerResponse
import com.tokopedia.payment.setting.list.model.SettingBannerModel
import com.tokopedia.payment.setting.util.GQL_GET_SETTING_BANNER
import com.tokopedia.usecase.RequestParams
import java.util.HashMap
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
            this.setRequestParams(getRequestParams())
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

    private fun getRequestParams(): HashMap<String, Any> {
        val requestParam = RequestParams.create()
        requestParam.putObject(
            REQ_PARAM, mapOf<String, Any>(
                COBRAND_BANK_ID_PARAM to COBRAND_BANK_ID_VALUE_1,
                DEVICE_TYPE_PARAM to DEVICE_TYPE_ANDROID,
            ),
        )

        return requestParam.parameters
    }

    companion object {
        const val REQ_PARAM = "req"
        const val COBRAND_BANK_ID_PARAM = "cobrand_bank_id"
        const val COBRAND_BANK_ID_VALUE_1 = 1
        const val DEVICE_TYPE_PARAM = "device_type"
        const val DEVICE_TYPE_ANDROID = "ANDROID"
    }
}
