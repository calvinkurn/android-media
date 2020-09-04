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
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
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
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.SetInjectCouponTimeBased
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.play_common.domain.model.PlayToggleChannelEntity
import com.tokopedia.play_common.domain.usecases.GetPlayWidgetUseCase
import com.tokopedia.play_common.domain.usecases.PlayToggleChannelReminderUseCase
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.repository.TopAdsRepository
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class HomeUseCaseModule {

    private val businessWidgetQuery : String = "query HomeWidget() {\n" +
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

    private val dismissSuggestedQuery : String = "query productrevDismissSuggestion{\n" +
            "  productrevDismissSuggestion\n" +
            "}"

    private val suggestedReviewQuery : String = "{\n" +
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

    private val stickyLoginQuery : String = "query get_ticker(\$page: String!) {\n" +
            "  ticker {\n" +
            "    tickers(page: \$page) {\n" +
            "      message\n" +
            "      layout\n" +
            "    }\n" +
            "  }\n" +
            "}"

    private val pendingCashBackQuery : String = "query pendingCashback {\n" +
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

    private val businessUnitDataQuery : String = "query(\$tabId:Int){\n" +
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

    val tokopointsQuery: String = "query(\$apiVersion:String){\n" +
            "    tokopointsDrawer(apiVersion: \$apiVersion){\n" +
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

    private val walletBalanceQuery : String = "{\n" +
            "  wallet(isGetTopup:true) {\n" +
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
            "    is_show_topup\n" +
            "    topup_applink\n" +
            "    topup_limit\n" +
            "  }\n" +
            "}"

    private val dynamicChannelQuery : String = "query getDynamicChannel(\$groupIDs: String!, \$numOfChannel: Int!, \$token: String!){\n" +
            "    dynamicHomeChannel {\n" +
            "        channels(groupIDs: \$groupIDs, numOfChannel: \$numOfChannel, token: \$token){\n" +
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
            "          has_close_button\n" +
            "          isAutoRefreshAfterExpired\n" +
            "          token\n" +
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
            "           grids {\n" +
            "             id\n" +
            "             back_color\n" +
            "             name\n" +
            "             url\n" +
            "             applink\n" +
            "             price\n" +
            "             slashedPrice\n" +
            "             discount\n" +
            "             imageUrl\n" +
            "             label\n" +
            "             soldPercentage\n" +
            "             attribution\n" +
            "             productClickUrl\n" +
            "             impression\n" +
            "             cashback\n" +
            "             isTopads\n" +
            "             freeOngkir {\n" +
            "                isActive\n" +
            "                imageUrl\n" +
            "              }\n" +
            "              productViewCountFormatted\n" +
            "              isOutOfStock\n" +
            "              warehouseID\n" +
            "              minOrder\n" +
            "              shop{\n" +
            "                shopID\n" +
            "               }\n" +
            "              labelGroup {\n" +
            "                title\n" +
            "                position\n" +
            "                type\n" +
            "              }\n" +
            "              has_buy_button\n" +
            "              rating\n" +
            "              count_review\n" +
            "              benefit {\n" +
            "                 type\n" +
            "                 value\n" +
            "              }\n" +
            "              textColor\n" +
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

    private val homeQuery: String = "" +
            "query homeData\n" +
            "        {\n" +
            "        status\n" +
            "          ticker {\n" +
            "            meta {\n" +
            "              total_data\n" +
            "            }\n" +
            "            tickers\n" +
            "            {\n" +
            "              id\n" +
            "              title\n" +
            "              message\n" +
            "              color\n" +
            "              layout\n" +
            "              ticker_type\n" +
            "              title\n" +
            "            }\n" +
            "          }\n" +
            "          slides(device: 32) {\n" +
            "            meta { total_data }\n" +
            "            slides {\n" +
            "              id\n" +
            "              galaxy_attribution\n" +
            "              persona\n" +
            "              brand_id\n" +
            "              category_persona\n" +
            "              image_url\n" +
            "              redirect_url\n" +
            "              applink\n" +
            "              topads_view_url\n" +
            "              promo_code\n" +
            "              creative_name\n" +
            "              type\n" +
            "              category_id\n" +
            "              campaignCode\n" +
            "            }\n" +
            "          }\n" +
            "          dynamicHomeIcon {\n" +
            "            dynamicIcon {\n" +
            "              id\n" +
            "              galaxy_attribution\n" +
            "              persona\n" +
            "              brand_id\n" +
            "              category_persona\n" +
            "              name\n" +
            "              url\n" +
            "              imageUrl\n" +
            "              applinks\n" +
            "              bu_identifier\n" +
            "            }\n" +
            "          }\n" +
            "          homeFlag{\n" +
            "                event_time\n" +
            "                server_time\n" +
            "                flags(name: \"has_recom_nav_button,dynamic_icon_wrap,has_tokopoints,is_autorefresh\"){\n" +
            "                    name\n" +
            "                    is_active\n" +
            "                }\n" +
            "            }\n" +
            "        }"

    private val recommendationQuery : String = "{\n" +
            "  get_home_recommendation{\n" +
            "    recommendation_tabs{\n" +
            "      id\n" +
            "      name\n" +
            "      image_url\n" +
            "    }\n" +
            "  }\n" +
            "}"

    private val addToCartOneClickCheckout = "mutation add_to_cart_occ(\$param: OneClickCheckoutATCParam) {\n" +
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

    private val closeChannel = "mutation closeChannel(\$channelID: Int!){\n" +
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
    ): GetHomeRecommendationUseCase {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.gql_home_feed)
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeFeedContentGqlResponse>(graphqlRepository)
        useCase.setGraphqlQuery(query)
        return GetHomeRecommendationUseCase(useCase, homeRecommendationMapper)
    }

    @Provides
    @HomeScope
    fun provideStickyLoginUseCase(graphqlRepository: GraphqlRepository): StickyLoginUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<StickyLoginTickerPojo.TickerResponse>(graphqlRepository)
        useCase.setGraphqlQuery(stickyLoginQuery)
        return StickyLoginUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideHomeReviewSuggestedUseCase(graphqlRepository: GraphqlRepository): GetHomeReviewSuggestedUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<SuggestedProductReview>(graphqlRepository)
        useCase.setGraphqlQuery(suggestedReviewQuery)
        return GetHomeReviewSuggestedUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideDismissHomeReviewUseCase(graphqlRepository: GraphqlRepository): DismissHomeReviewUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<ProductrevDismissSuggestion>(graphqlRepository)
        useCase.setGraphqlQuery(dismissSuggestedQuery)
        return DismissHomeReviewUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideHomeTokopointsDataUseCase(graphqlRepository: GraphqlRepository): GetHomeTokopointsDataUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<TokopointsDrawerHomeData>(graphqlRepository)
        useCase.setGraphqlQuery(tokopointsQuery)
        return GetHomeTokopointsDataUseCase(useCase)
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
    fun getCoroutineWalletBalanceUseCase(graphqlRepository: GraphqlRepository, userSession: UserSessionInterface, remoteConfig: RemoteConfig, localCacheHandler: LocalCacheHandler): GetCoroutineWalletBalanceUseCase {
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<WalletBalanceResponse>(graphqlRepository)
        usecase.setGraphqlQuery(walletBalanceQuery)
        return GetCoroutineWalletBalanceUseCase(usecase, remoteConfig, userSession, localCacheHandler)
    }

    @HomeScope
    @Provides
    fun getCoroutinePendingCashbackUseCase(graphqlRepository: GraphqlRepository): GetCoroutinePendingCashbackUseCase {
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<ResponsePendingCashback>(graphqlRepository)
        usecase.setGraphqlQuery(pendingCashBackQuery)
        return GetCoroutinePendingCashbackUseCase(usecase)
    }

    @HomeScope
    @Provides
    fun getBusinessWidgetTab(graphqlRepository: GraphqlRepository): GetBusinessWidgetTab {
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeWidget.Data>(graphqlRepository)
        usecase.setGraphqlQuery(businessWidgetQuery)
        return GetBusinessWidgetTab(usecase)
    }

    @HomeScope
    @Provides
    fun getBusinessUnitDataTab(graphqlRepository: GraphqlRepository): GetBusinessUnitDataUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeWidget.Data>(graphqlRepository)
        useCase.setGraphqlQuery(businessUnitDataQuery)
        return GetBusinessUnitDataUseCase(useCase)
    }

    @HomeScope
    @Provides
    fun getRecommendationTabUseCase(graphqlRepository: GraphqlRepository): GetRecommendationTabUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeFeedTabGqlResponse>(graphqlRepository)
        useCase.setGraphqlQuery(recommendationQuery)
        return GetRecommendationTabUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun providePopularKeywordUseCase(graphqlUseCase: com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<Any>): GetPopularKeywordUseCase {
        return GetPopularKeywordUseCase(graphqlUseCase as com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeWidget.PopularKeywordQuery>)
    }

    @Provides
    @HomeScope
    fun provideGetDynamicChannels(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository, homeDynamicChannelDataMapper: HomeDynamicChannelDataMapper): GetDynamicChannelsUseCase{
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeChannelData>(graphqlRepository)
        useCase.setGraphqlQuery(dynamicChannelQuery)
        return GetDynamicChannelsUseCase(useCase, homeDynamicChannelDataMapper)
    }

    @Provides
    @HomeScope
    fun provideGetHomeData(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository, homeDynamicChannelDataMapper: HomeDynamicChannelDataMapper): GetHomeDataUseCase{
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeData>(graphqlRepository)
        useCase.setGraphqlQuery(homeQuery)
        return GetHomeDataUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideAddToCartOccUseCase(graphqlUseCase: GraphqlUseCase): AddToCartOccUseCase{
        return AddToCartOccUseCase(addToCartOneClickCheckout, graphqlUseCase, AddToCartDataMapper())
    }

    @Provides
    @HomeScope
    fun provideCloseChannelUseCase(graphqlRepository: GraphqlRepository): CloseChannelUseCase{
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<CloseChannelMutation>(graphqlRepository)
        useCase.setGraphqlQuery(closeChannel)
        return CloseChannelUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideGetPlayBannerV2UseCase(graphqlRepository: GraphqlRepository): GetPlayWidgetUseCase{
        return GetPlayWidgetUseCase(graphqlRepository)
    }

    @Provides
    @HomeScope
    fun providePlayToggleChannelReminderUseCase(graphqlRepository: GraphqlRepository): PlayToggleChannelReminderUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<PlayToggleChannelEntity>(graphqlRepository)
        return PlayToggleChannelReminderUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideInjectCouponTimeBasedUseCase(graphqlUseCase: com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<SetInjectCouponTimeBased>): InjectCouponTimeBasedUseCase {
        return InjectCouponTimeBasedUseCase(graphqlUseCase)
    }

    @HomeScope
    @Provides
    fun provideTopAdsImageViewUseCase(userSession: UserSessionInterface): TopAdsImageViewUseCase {
        return TopAdsImageViewUseCase(userSession.userId, TopAdsRepository())
    }
}