package com.tokopedia.campaignlist.common.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.campaignlist.common.data.model.response.GetSellerCampaignSellerAppMetaResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class GetSellerMetaDataUseCase @Inject constructor(
        @ApplicationContext repository: GraphqlRepository
) : GraphqlUseCase<GetSellerCampaignSellerAppMetaResponse>(repository) {

    companion object {
        private val query = """
            query getSellerCampaignSellerAppMeta() {
                getSellerCampaignSellerAppMeta() {
                    campaign_status {
                        status
                        status_text
                    }
                    campaign_type_data {
                        campaign_type_id
                        campaign_type_name
                        status
                        status_text
                    }
                }
            }
    """.trimIndent()
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(GetSellerCampaignSellerAppMetaResponse::class.java)
    }
}