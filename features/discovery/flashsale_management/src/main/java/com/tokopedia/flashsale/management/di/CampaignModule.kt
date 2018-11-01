package com.tokopedia.flashsale.management.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.domain.GraphqlUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
@CampaignScope
class CampaignModule {

    @CampaignScope
    @Provides
    fun provideGraphqlUseCase() = GraphqlUseCase()

    @CampaignScope
    @Named(FlashSaleConstant.NAMED_GQL_RAW_ELIGIBLE_SELLER_PRODUCT)
    @Provides
    fun provideGqlRawString(@ApplicationContext context: Context) =
        GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_campaign_label)

    @Provides
    fun provideGraphqlMultiUseCase() = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

}