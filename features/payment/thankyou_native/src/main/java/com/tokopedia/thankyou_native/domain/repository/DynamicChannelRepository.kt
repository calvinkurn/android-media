package com.tokopedia.thankyou_native.domain.repository

import android.os.Bundle
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.thankyou_native.domain.model.ThankYouPageChannelData
import com.tokopedia.thankyou_native.domain.repository.QueryDynamicChannelV2.DYNAMIC_CHANNEL_V2_QUERY
import com.tokopedia.thankyou_native.domain.repository.QueryDynamicChannelV2.DYNAMIC_CHANNEL_V2_QUERY_NAME
import com.tokopedia.usecase.RequestParams
import timber.log.Timber
import javax.inject.Inject

class DynamicChannelRepository @Inject constructor(
    private val graphqlRepository: GraphqlRepository
): ThankyouPageRepository<ThankYouPageChannelData> {
    suspend fun getDynamicChannelData(params: RequestParams): ThankYouPageChannelData {
        try {
            val gqlRequest = buildRequestV2(params)
            val gqlResponse = graphqlRepository.response(
                listOf(gqlRequest), GraphqlCacheStrategy
                    .Builder(CacheType.ALWAYS_CLOUD).build())
            val errors = gqlResponse.getError(ThankYouPageChannelData::class.java)
            if (errors.isNullOrEmpty()) {
                val result: ThankYouPageChannelData = gqlResponse.getData(ThankYouPageChannelData::class.java)
                return result
            } else throw MessageErrorException(errors.joinToString { it.message })
        } catch (e: Exception) {
            Timber.e(e)
            throw e
        }
    }

    private fun buildRequestV2(params: RequestParams): GraphqlRequest {
        return GraphqlRequest(DYNAMIC_CHANNEL_V2_QUERY, ThankYouPageChannelData::class.java, params.parameters)
    }

    companion object{
        const val GROUP_IDS = "groupIDs"
        const val TOKEN = "token"
        const val NUM_OF_CHANNEL = "numOfChannel"
        const val PARAMS = "param"
        const val LOCATION = "location"
        const val CHANNEL_IDS = "channelIDs"
        const val PAGE = "page"
        const val THANK_YOU = "thank_you"

        fun buildParams(
            groupIds: String = "",
            token: String = "",
            numOfChannel: Int = 0,
            queryParams: String = "",
            locationParams: String = ""
        ) : RequestParams {
            val params = RequestParams.create()
            params.parameters.clear()
            params.putString(PARAMS, queryParams)
            params.putString(GROUP_IDS, groupIds)
            params.putString(TOKEN, token)
            params.putInt(NUM_OF_CHANNEL, numOfChannel)
            params.putString(LOCATION, locationParams)
            return params
        }

        fun buildParamsV2(
            groupIds: String = "",
            channelIds: String = "0",
            queryParams: String = "",
            locationParams: String = "",
            page: String = ""
        ) : RequestParams {
            val params = RequestParams.create()
            params.parameters.clear()
            params.putString(PARAMS, queryParams)
            params.putString(GROUP_IDS, groupIds)
            params.putString(CHANNEL_IDS, channelIds)
            params.putString(LOCATION, locationParams)
            params.putString(PAGE, page)
            return params
        }
    }

    override suspend fun getRemoteData(bundle: Bundle): ThankYouPageChannelData {
        val groupId = bundle.getString(GROUP_IDS, "")
        val params = bundle.getString(PARAMS, "")
        val location = bundle.getString(LOCATION, "")
        val channelIds = bundle.getString(CHANNEL_IDS, "")

        val requestParams = buildParamsV2(
            groupIds = groupId,
            channelIds = channelIds,
            queryParams = params,
            locationParams = location,
            page = THANK_YOU
        )

        return getDynamicChannelData(requestParams)
    }
}

@GqlQuery(DYNAMIC_CHANNEL_V2_QUERY_NAME, DYNAMIC_CHANNEL_V2_QUERY)
internal object QueryDynamicChannelV2 {
    const val DYNAMIC_CHANNEL_V2_QUERY_NAME = "DynamicChannelQueryV2"
    const val DYNAMIC_CHANNEL_V2_QUERY = "query getDynamicChannelV2(\$groupIDs: String!, \$channelIDs: String!, \$param: String!, \$location: String, \$page: String){\n" +
        "  getHomeChannelV2(groupIDs: \$groupIDs, channelIDs: \$channelIDs, param: \$param, location: \$location, page: \$page) {\n" +
        "    channels {\n" +
        "      id\n" +
        "      name\n" +
        "      type\n" +
        "      token\n" +
        "      grids {\n" +
        "        id\n" +
        "        url\n" +
        "        name\n" +
        "        shop {\n" +
        "          id\n" +
        "          url\n" +
        "          city\n" +
        "          name\n" +
        "          domain\n" +
        "          applink\n" +
        "          imageUrl\n" +
        "          reputation\n" +
        "        }\n" +
        "        price\n" +
        "        label\n" +
        "        stock\n" +
        "        creativeID\n" +
        "        logExtra\n" +
        "        param\n" +
        "        rating\n" +
        "        badges {\n" +
        "          title\n" +
        "          imageUrl\n" +
        "        }\n" +
        "        applink\n" +
        "        benefit {\n" +
        "          type\n" +
        "          value\n" +
        "        }\n" +
        "        discount\n" +
        "        imageUrl\n" +
        "        cashback\n" +
        "        isTopads\n" +
        "        minOrder\n" +
        "        maxOrder\n" +
        "        clusterID\n" +
        "        backColor\n" +
        "        textColor\n" +
        "        impression\n" +
        "        freeOngkir {\n" +
        "          isActive\n" +
        "          imageUrl\n" +
        "        }\n" +
        "        labelGroup {\n" +
        "          url\n" +
        "          type\n" +
        "          title\n" +
        "          position\n" +
        "        }\n" +
        "        attribution\n" +
        "        warehouseID\n" +
        "        countReview\n" +
        "        expiredTime\n" +
        "        slashedPrice\n" +
        "        isOutOfStock\n" +
        "        hasBuyButton\n" +
        "        campaignCode\n" +
        "        ratingAverage\n" +
        "        labelTextColor\n" +
        "        soldPercentage\n" +
        "        productClickUrl\n" +
        "        productImageUrl\n" +
        "        parentProductID\n" +
        "        labelGroupVariant {\n" +
        "          type\n" +
        "          title\n" +
        "          hexColor\n" +
        "          typeVariant\n" +
        "        }\n" +
        "        discountPercentage\n" +
        "        recommendationType\n" +
        "        categoryBreadcrumbs\n" +
        "        productViewCountFormatted\n" +
        "      }\n" +
        "      layout\n" +
        "      styleParam\n" +
        "      header {\n" +
        "        id\n" +
        "        url\n" +
        "        name\n" +
        "        applink\n" +
        "        subtitle\n" +
        "        boxColor\n" +
        "        backColor\n" +
        "        backImage\n" +
        "        textColor\n" +
        "        timerColor\n" +
        "        serverTime\n" +
        "        expiredTime\n" +
        "        desktopName\n" +
        "        timerVariant\n" +
        "      }\n" +
        "      banner {\n" +
        "        id\n" +
        "        cta {\n" +
        "          type\n" +
        "          mode\n" +
        "          text\n" +
        "          couponCode\n" +
        "        }\n" +
        "        url\n" +
        "        title\n" +
        "        applink\n" +
        "        imageUrl\n" +
        "        textColor\n" +
        "        backColor\n" +
        "        description\n" +
        "        attribution\n" +
        "        gradientColor\n" +
        "      }\n" +
        "      persona\n" +
        "      brandID\n" +
        "      groupID\n" +
        "      pageName\n" +
        "      persoType\n" +
        "      categoryID\n" +
        "      campaignID\n" +
        "      widgetParam\n" +
        "      dividerType\n" +
        "      viewAllCard {\n" +
        "        id\n" +
        "        title\n" +
        "        imageUrl\n" +
        "        contentType\n" +
        "        description\n" +
        "        gradientColor\n" +
        "      }\n" +
        "      campaignCode\n" +
        "      campaignType\n" +
        "      showPromoBadge\n" +
        "      pgCampaignType\n" +
        "      hasCloseButton\n" +
        "      contextualInfo\n" +
        "      categoryPersona\n" +
        "      galaxyAttribution\n" +
        "      isAutoRefreshAfterExpired\n" +
        "    }\n" +
        "  }\n" +
        "}"
}

