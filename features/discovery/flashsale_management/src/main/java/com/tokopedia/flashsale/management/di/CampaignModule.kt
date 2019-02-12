package com.tokopedia.flashsale.management.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.data.campaignlabel.DataCampaignLabel
import com.tokopedia.flashsale.management.data.campaignlist.Campaign
import com.tokopedia.flashsale.management.data.campaignlist.DataCampaignList
import com.tokopedia.flashsale.management.common.data.SellerStatus
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
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
    @Named(FlashSaleConstant.NAMED_REQUEST_SUBMISSION_PRODUCT_LIST)
    @Provides
    fun provideGqlRawStringRequestProductList(@ApplicationContext context: Context) =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_mojito_eligible_seller_product)

    @CampaignScope
    @Named(FlashSaleConstant.NAMED_REQUEST_POST_PRODUCT_LIST)
    @Provides
    fun provideGqlRawStringRequestPostProductList(@ApplicationContext context: Context) =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_mojito_post_seller_product)

    @CampaignScope
    @Named(FlashSaleConstant.NAMED_REQUEST_CATEGORY_LIST)
    @Provides
    fun provideGqlRawStringRequestCategoryList(@ApplicationContext context: Context) =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_campaign_info_category_list)

    @CampaignScope
    @Named(FlashSaleConstant.NAMED_REQUEST_TNC)
    @Provides
    fun provideGqlRawStringRequestTnc(@ApplicationContext context: Context) =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_campaign_info_tnc)

    @CampaignScope
    @Named(FlashSaleConstant.NAMED_REQUEST_SUBMIT_PRODUCT)
    @Provides
    fun provideGqlRawStringRequestSubmitProduct(@ApplicationContext context: Context) =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_submit_product)

    @CampaignScope
    @Named(FlashSaleConstant.NAMED_REQUEST_RESERVE_PRODUCT)
    @Provides
    fun provideGqlRawStringRequestReserveProduct(@ApplicationContext context: Context) =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_reserve_product)

    @CampaignScope
    @Named(FlashSaleConstant.NAMED_REQUEST_DERESERVE_PRODUCT)
    @Provides
    fun provideGqlRawStringRequestDereserveProduct(@ApplicationContext context: Context) =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_dereserve_product)

    @CampaignScope
    @Provides
    fun provideGqlRepository(): GraphqlRepository =
            GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    @Named(FlashSaleConstant.NAMED_REQUEST_CAMPAIGN_LABEL)
    fun provideGetCampaignLabelUseCase(graphqlRepository: GraphqlRepository) =
            GraphqlUseCase<DataCampaignLabel.Response>(graphqlRepository).apply {
                setTypeClass(DataCampaignLabel.Response::class.java)
            }

    @Provides
    @Named(FlashSaleConstant.NAMED_REQUEST_CAMPAIGN_LIST)
    fun provideGetCampaignListUseCase(graphqlRepository: GraphqlRepository) =
            GraphqlUseCase<DataCampaignList.Response>(graphqlRepository).apply {
                setTypeClass(DataCampaignList.Response::class.java)
            }

    @Provides
    @Named(FlashSaleConstant.NAMED_REQUEST_CAMPAIGN)
    fun provideGetCampaignUseCase(graphqlRepository: GraphqlRepository) =
            GraphqlUseCase<Campaign.Response>(graphqlRepository).apply {
                setTypeClass(Campaign.Response::class.java)
            }

    @Provides
    @Named(FlashSaleConstant.NAMED_REQUEST_SELLER_STATUS)
    fun provideGetSellerStatusUseCase(graphqlRepository: GraphqlRepository) =
            GraphqlUseCase<SellerStatus.Response>(graphqlRepository).apply {
                setTypeClass(SellerStatus.Response::class.java)
            }
}