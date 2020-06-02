package com.tokopedia.home.beranda.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.common_wallet.balance.data.entity.WalletBalanceResponse
import com.tokopedia.common_wallet.pendingcashback.data.ResponsePendingCashback
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.mapper.FeedTabMapper
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.HomeRecommendationMapper
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.model.TokopointsDrawerHomeData
import com.tokopedia.home.beranda.data.repository.HomeRepository
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.gql.CloseChannelMutation
import com.tokopedia.home.beranda.domain.gql.ProductrevDismissSuggestion
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedContentGqlResponse
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedTabGqlResponse
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.SetInjectCouponTimeBased
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class HomeUseCaseModule {

    val businessWidgetQuery : String = "query HomeWidget() {\n" +
            "  home_widget {\n" +
            "    widget_tab {\n" +
            "      id\n" +
            "      name\n" +
            "    }\n" +
            "    widget_header{\n" +
            "        back_color\n" +
            "    }\n" +
            "  }\n" +
            "}\n"

    val dismissSuggestedQuery : String = "query productrevDismissSuggestion{\n" +
            "  productrevDismissSuggestion\n" +
            "}"

    val suggestedReviewQuery : String = "{\n" +
            "  suggestedProductReview{\n" +
            "    title\n" +
            "    description\n" +
            "    imageUrl\n" +
            "    linkURL\n" +
            "    dismissable\n" +
            "    dismissURL\n" +
            "    orderID\n" +
            "    productID\n" +
            "  }\n" +
            "}"

    val stickyLoginQuery : String = "query get_ticker(\$page: String!) {\n" +
            "  ticker {\n" +
            "    tickers(page: \$page) {\n" +
            "      message\n" +
            "      layout\n" +
            "    }\n" +
            "  }\n" +
            "}"

    val pendingCashBackQuery : String = "query pendingCashback {\n" +
            "  goalPendingBalance {\n" +
            "    balance\n" +
            "    balance_text\n" +
            "    cash_balance\n" +
            "    cash_balance_text\n" +
            "    point_balance\n" +
            "    point_balance_text\n" +
            "    wallet_type\n" +
            "    phone_number\n" +
            "    errors {\n" +
            "      title\n" +
            "      message\n" +
            "    }\n" +
            "  }\n" +
            "}\n"

    val businessUnitDataQuery : String = "query(\$tabId:Int){\n" +
            "  home_widget {\n" +
            "    widget_grid(tabID:\$tabId) {\n" +
            "      id\n" +
            "      name\n" +
            "      image_url\n" +
            "      url\n" +
            "      applink\n" +
            "      title_1\n" +
            "      desc_1\n" +
            "      title_2\n" +
            "      desc_2\n" +
            "      tag_name\n" +
            "      tag_type\n" +
            "      price\n" +
            "      original_price\n" +
            "      price_prefix\n" +
            "      template_id\n" +
            "    }\n" +
            "  }\n" +
            "}\n" +
            "\n"

    val tokopointsQuery : String = "{\n" +
            "    tokopointsDrawer{\n" +
            "        iconImageURL\n" +
            "        redirectURL\n" +
            "        redirectAppLink\n" +
            "        sectionContent{\n" +
            "            type\n" +
            "            textAttributes{\n" +
            "                text\n" +
            "                color\n" +
            "                isBold\n" +
            "            }\n" +
            "            tagAttributes{\n" +
            "                text\n" +
            "                backgroundColor\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}"

    val walletBalanceQuery : String = "{\n" +
            "  wallet {\n" +
            "    linked\n" +
            "    balance\n" +
            "    rawBalance\n" +
            "    text\n" +
            "    total_balance\n" +
            "    raw_total_balance\n" +
            "    hold_balance\n" +
            "    raw_hold_balance\n" +
            "    redirect_url\n" +
            "    applinks\n" +
            "    ab_tags {\n" +
            "      tag\n" +
            "    }\n" +
            "    action {\n" +
            "      text\n" +
            "      redirect_url\n" +
            "      applinks\n" +
            "      visibility\n" +
            "    }\n" +
            "    point_balance\n" +
            "    raw_point_balance\n" +
            "    cash_balance\n" +
            "    raw_cash_balance\n" +
            "    wallet_type\n" +
            "    help_applink\n" +
            "    tnc_applink\n" +
            "    show_announcement\n" +
            "  }\n" +
            "}"

    val dynamicChannelQuery : String = "query getDynamicChannel(\$groupIDs: String!){\n" +
            "    dynamicHomeChannel {\n" +
            "        channels(groupIDs: \$groupIDs){\n" +
            "          id\n" +
            "          group_id\n" +
            "          galaxy_attribution\n" +
            "          persona\n" +
            "          brand_id\n" +
            "          category_persona\n" +
            "          name\n" +
            "          layout\n" +
            "          type\n" +
            "          showPromoBadge\n" +
            "          header {\n" +
            "            id\n" +
            "            name\n" +
            "            subtitle\n" +
            "            url\n" +
            "            applink\n" +
            "            serverTime\n" +
            "            expiredTime\n" +
            "            backColor\n" +
            "            backImage\n" +
            "          }\n" +
            "          hero {\n" +
            "            id\n" +
            "            name\n" +
            "            url\n" +
            "            applink\n" +
            "            imageUrl\n" +
            "            attribution\n" +
            "          }\n" +
            "          grids {\n" +
            "            id\n" +
            "            name\n" +
            "            url\n" +
            "            applink\n" +
            "            price\n" +
            "            slashedPrice\n" +
            "            discount\n" +
            "            imageUrl\n" +
            "            label\n" +
            "            soldPercentage\n" +
            "            attribution\n" +
            "            productClickUrl\n" +
            "            impression\n" +
            "            cashback\n" +
            "            freeOngkir {\n" +
            "              isActive\n" +
            "              imageUrl\n" +
            "            }\n" +
            "          }\n" +
            "          banner {\n" +
            "            id\n" +
            "            title\n" +
            "            description\n" +
            "            url\n" +
            "            back_color\n" +
            "            cta {\n" +
            "              type\n" +
            "              mode\n" +
            "              text\n" +
            "              coupon_code\n" +
            "            }\n" +
            "            applink\n" +
            "            text_color\n" +
            "            image_url\n" +
            "            attribution\n" +
            "\n" +
            "          }\n" +
            "        }\n" +
            "    }\n" +
            "}"

    val recommendationQuery : String = "{\n" +
            "  get_home_recommendation{\n" +
            "    recommendation_tabs{\n" +
            "      id\n" +
            "      name\n" +
            "      image_url\n" +
            "    }\n" +
            "  }\n" +
            "}"

    val addToCartOneClickCheckout = "mutation add_to_cart_occ(\$param: OneClickCheckoutATCParam) {\n" +
            "    add_to_cart_occ(param: \$param) {\n" +
            "        error_message\n" +
            "        status\n" +
            "        data {\n" +
            "            message\n" +
            "            success\n" +
            "            data {\n" +
            "                cart_id\n" +
            "                customer_id\n" +
            "                is_scp\n" +
            "                is_trade_in\n" +
            "                notes\n" +
            "                product_id\n" +
            "                quantity\n" +
            "                shop_id\n" +
            "                warehouse_id\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}"

    val closeChannel = "mutation closeChannel(\$channelID: Int!){\n" +
            "  close_channel(channelID: \$channelID){\n" +
            "    success\n" +
            "    message\n" +
            "  }\n" +
            "}"

    @HomeScope
    @Provides
    fun graphqlUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @HomeScope
    @Provides
    fun provideGraphqlUseCase(graphqlRepository: GraphqlRepository) =
            com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<Any>(graphqlRepository)

    @HomeScope
    @Provides
    fun homeUseCase(homeRepository: HomeRepository, homeDataMapper: HomeDataMapper) = HomeUseCase(homeRepository, homeDataMapper)

    @Provides
    fun provideSendGeolocationInfoUseCase(homeRepository: HomeRepository): SendGeolocationInfoUseCase {
        return SendGeolocationInfoUseCase(homeRepository)
    }

    @Provides
    fun provideGetHomeRecommendationUseCase(
            @ApplicationContext context: Context,
            graphqlRepository: GraphqlRepository,
            homeRecommendationMapper: HomeRecommendationMapper
    ): GetHomeRecommendationUseCase{
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.gql_home_feed)
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeFeedContentGqlResponse>(graphqlRepository)
        useCase.setGraphqlQuery(query)
        return GetHomeRecommendationUseCase(useCase, homeRecommendationMapper)
    }

    @Provides
    fun provideGetFeedTabUseCase(@ApplicationContext context: Context?,
                                 graphqlUseCase: GraphqlUseCase?,
                                 feedTabMapper: FeedTabMapper?): GetFeedTabUseCase {
        return GetFeedTabUseCase(context, graphqlUseCase, feedTabMapper)
    }

    @Provides
    @HomeScope
    fun provideStickyLoginUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): StickyLoginUseCase {
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<StickyLoginTickerPojo.TickerResponse>(graphqlRepository)
        usecase.setGraphqlQuery(stickyLoginQuery)
        return StickyLoginUseCase(usecase)
    }

    @Provides
    @HomeScope
    fun provideHomeReviewSuggestedUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): GetHomeReviewSuggestedUseCase {
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<SuggestedProductReview>(graphqlRepository)
        usecase.setGraphqlQuery(suggestedReviewQuery)
        return GetHomeReviewSuggestedUseCase(usecase)
    }

    @Provides
    @HomeScope
    fun provideDismissHomeReviewUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): DismissHomeReviewUseCase {
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<ProductrevDismissSuggestion>(graphqlRepository)
        usecase.setGraphqlQuery(dismissSuggestedQuery)
        return DismissHomeReviewUseCase(usecase)
    }

    @Provides
    @HomeScope
    fun provideHomeTokopointsDataUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): GetHomeTokopointsDataUseCase {
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<TokopointsDrawerHomeData>(graphqlRepository)
        usecase.setGraphqlQuery(tokopointsQuery)
        return GetHomeTokopointsDataUseCase(usecase)
    }

    @Provides
    @HomeScope
    fun provideGetPlayLiveDynamicDataUseCase(graphqlRepository: GraphqlRepository): GetPlayLiveDynamicUseCase {
        return GetPlayLiveDynamicUseCase(com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase(graphqlRepository))
    }

    @HomeScope
    @Provides
    fun getKeywordSearchUseCase(graphqlRepository: GraphqlRepository): GetKeywordSearchUseCase {
        return GetKeywordSearchUseCase(com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase(graphqlRepository))
    }

    @HomeScope
    @Provides
    fun getCoroutineWalletBalanceUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository, userSession: UserSessionInterface, remoteConfig: RemoteConfig, localCacheHandler: LocalCacheHandler): GetCoroutineWalletBalanceUseCase {
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<WalletBalanceResponse>(graphqlRepository)
        usecase.setGraphqlQuery(walletBalanceQuery)
        return GetCoroutineWalletBalanceUseCase(usecase, remoteConfig, userSession, localCacheHandler)
    }

    @HomeScope
    @Provides
    fun getCoroutinePendingCashbackUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): GetCoroutinePendingCashbackUseCase {
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<ResponsePendingCashback>(graphqlRepository)
        usecase.setGraphqlQuery(pendingCashBackQuery)
        return GetCoroutinePendingCashbackUseCase(usecase)
    }

    @HomeScope
    @Provides
    fun getBusinessWidgetTab(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): GetBusinessWidgetTab {
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeWidget.Data>(graphqlRepository)
        usecase.setGraphqlQuery(businessWidgetQuery)
        return GetBusinessWidgetTab(usecase)
    }

    @HomeScope
    @Provides
    fun getBusinessUnitDataTab(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): GetBusinessUnitDataUseCase {
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeWidget.Data>(graphqlRepository)
        usecase.setGraphqlQuery(businessUnitDataQuery)
        return GetBusinessUnitDataUseCase(usecase)
    }

    @HomeScope
    @Provides
    fun getRecommendationTabUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): GetRecommendationTabUseCase {
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeFeedTabGqlResponse>(graphqlRepository)
        usecase.setGraphqlQuery(recommendationQuery)
        return GetRecommendationTabUseCase(usecase)
    }

    @Provides
    @HomeScope
    fun providePopularKeywordUseCase(graphqlUseCase: com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<Any>): GetPopularKeywordUseCase {
        return GetPopularKeywordUseCase(graphqlUseCase as com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeWidget.PopularKeywordQuery>)
    }

    @Provides
    @HomeScope
    fun provideGetDynamicChannels(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository, homeDataMapper: HomeDataMapper): GetDynamicChannelsUseCase{
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeData>(graphqlRepository)
        useCase.setGraphqlQuery(dynamicChannelQuery)
        return GetDynamicChannelsUseCase(useCase, homeDataMapper)
    }

    @Provides
    @HomeScope
    fun provideAddToCartOccUseCase(@ApplicationContext context: Context, graphqlUseCase: GraphqlUseCase): AddToCartOccUseCase{
        return AddToCartOccUseCase(addToCartOneClickCheckout, graphqlUseCase, AddToCartDataMapper())
    }

    @Provides
    @HomeScope
    fun provideCloseChannelUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): CloseChannelUseCase{
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<CloseChannelMutation>(graphqlRepository)
        useCase.setGraphqlQuery(closeChannel)
        return CloseChannelUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideInjectCouponTimeBasedUseCase(graphqlUseCase: com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<SetInjectCouponTimeBased>): InjectCouponTimeBasedUseCase {
        return InjectCouponTimeBasedUseCase(graphqlUseCase)
    }
}