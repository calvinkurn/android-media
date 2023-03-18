package com.tokopedia.dilayanitokopedia.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.dilayanitokopedia.home.domain.mapper.LocationParamMapper.mapLocation
import com.tokopedia.dilayanitokopedia.home.domain.model.GetDtHomeLayoutParam
import com.tokopedia.dilayanitokopedia.home.domain.model.GetHomeLayoutResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import javax.inject.Inject

/**
 * Dynamic Home Channel Query Docs:
 * https://tokopedia.atlassian.net/wiki/spaces/HP/pages/2043906993/HPB+Home+-+API+GQL+GraphQL+getHomeChannelV2
 */

class GetLayoutDataUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetDtHomeLayoutParam, GetHomeLayoutResponse>(dispatcher.io) {

    companion object {
        private const val QUERY = """
            query getHomeChannelV2(
                ${'$'}page: String, 
                ${'$'}param: String,
                ${'$'}groupIDs: String, 
                ${'$'}channelIDs: String, 
                ${'$'}location: String) {
                getHomeChannelV2(
                    page: ${'$'}page, 
                    param: ${'$'}param, 
                    groupIDs: ${'$'}groupIDs, 
                    channelIDs: ${'$'}channelIDs, 
                    location: ${'$'}location) {
                    channels {
                        id
                        name
                        type
                        token
                        layout
                        persona
                        brandID
                        groupID
                        pageName
                        persoType
                        categoryID
                        campaignID
                        widgetParam
                        dividerType
                        campaignCode
                        campaignType
                        showPromoBadge
                        pgCampaignType
                        hasCloseButton
                        contextualInfo
                        categoryPersona
                        galaxyAttribution
                        isAutoRefreshAfterExpired
                        grids {
                        id
                        url
                        name
                        price
                        label
                        stock
                        param
                        rating
                        applink
                        discount
                        imageUrl
                        cashback
                        isTopads
                        minOrder
                        maxOrder
                        clusterID
                        backColor
                        textColor
                        impression
                        attribution
                        warehouseID
                        countReview
                        expiredTime
                        slashedPrice
                        isOutOfStock
                        hasBuyButton
                        campaignCode
                        ratingAverage
                        labelTextColor
                        soldPercentage
                        productClickUrl
                        productImageUrl
                        parentProductID
                        discountPercentage
                        recommendationType
                        categoryBreadcrumbs
                        productViewCountFormatted
                        shop {
                          id
                          url
                          city
                          name
                          domain
                          applink
                          imageUrl
                          reputation
                        }
                        badges {
                          title
                          imageUrl
                        }
                        benefit {
                          type
                          value
                        }
                        freeOngkir {
                          isActive
                          imageUrl
                        }
                        labelGroup {
                          url
                          type
                          title
                          position
                        }
                        labelGroupVariant {
                          type
                          title
                          hexColor
                          typeVariant
                        }
                        }
                        header {
                        id
                        url
                        name
                        applink
                        subtitle
                        boxColor
                        backColor
                        backImage
                        textColor
                        timerColor
                        serverTime
                        expiredTime
                        desktopName
                        timerVariant
                        }
                        banner {
                        id
                        url
                        title
                        applink
                        imageUrl
                        textColor
                        backColor
                        description
                        attribution
                        gradientColor
                        cta {
                          type
                          mode
                          text
                          couponCode
                        }
                        }
                        viewAllCard {
                        id
                        title
                        imageUrl
                        contentType
                        description
                        gradientColor
                        }
                    }
                }
        }
        """
        private const val QUERY_NAME = "DTGetHomeChannelV2"
        private const val PARAM_VALUE_PAGE_DT = "dt"

        fun getParam(location: LocalCacheModel): GetDtHomeLayoutParam {
            return GetDtHomeLayoutParam(
                page = PARAM_VALUE_PAGE_DT,
                location = mapLocation(location)
            )
        }
    }

    override fun graphqlQuery(): String {
        return QUERY
    }

    @GqlQuery(QUERY_NAME, QUERY)
    override suspend fun execute(params: GetDtHomeLayoutParam): GetHomeLayoutResponse {
        return graphqlRepository.request(DTGetHomeChannelV2(), params)
    }
}
