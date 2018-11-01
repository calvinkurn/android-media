package com.tokopedia.flashsale.management.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.data.campaignlabel.DataCampaignLabel
import com.tokopedia.flashsale.management.data.campaignlist.Campaign
import com.tokopedia.flashsale.management.data.campaignlist.DataCampaignList
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
@CampaignScope
class CampaignModule {

    @CampaignScope
    @Provides
    fun provideGraphqlUseCase() = com.tokopedia.graphql.domain.GraphqlUseCase()

    @CampaignScope
    @Named(FlashSaleConstant.NAMED_GQL_RAW_ELIGIBLE_SELLER_PRODUCT)
    @Provides
    fun provideGqlRawString(@ApplicationContext context: Context) =
        GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_campaign_label)

    @Provides
    @Named(FlashSaleConstant.NAMED_REQUEST_CAMPAIGN_LABEL)
    fun provideGetCampaignLabelUseCase() =
            GraphqlUseCase<DataCampaignLabel>(GraphqlInteractor.getInstance().graphqlRepository).apply {
                setTypeClass(DataCampaignLabel::class.java)
            }

    @Provides
    @Named(FlashSaleConstant.NAMED_REQUEST_CAMPAIGN_LIST)
    fun provideGetCampaignListUseCase() =
            GraphqlUseCase<DataCampaignList>(GraphqlInteractor.getInstance().graphqlRepository).apply {
                setTypeClass(DataCampaignList::class.java)
            }

    @Provides
    @Named(FlashSaleConstant.NAMED_REQUEST_CAMPAIGN)
    fun provideGetCampaignUseCase() =
            GraphqlUseCase<Campaign>(GraphqlInteractor.getInstance().graphqlRepository).apply {
                setTypeClass(Campaign::class.java)
            }
}