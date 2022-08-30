package com.tokopedia.tokofood.feature.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.common.domain.param.TokoFoodLocationParamMapper.mapLocation

object TokoFoodHomeDynamicChannelQuery: GqlQueryInterface {

    const val PARAM_LOCATION = "location"

    private const val OPERATION_NAME = "getDynamicHomeChannel"
    private val QUERY = """
       query $OPERATION_NAME(
         ${'$'}location: String
       ) {
         dynamicHomeChannel {
           channels(
             type:"tokofood", 
             location: ${'$'}location
           ) {
             id
             widgetParam
             group_id
             galaxy_attribution
             persona
             brand_id
             category_persona
             name
             layout
             type
             campaignID
             showPromoBadge
             categoryID
             perso_type
             pageName
             campaignCode
             has_close_button
             header {
               id
               name
               subtitle
               url
               applink
               serverTime
               expiredTime
               backColor
               backImage
               textColor
             }
             hero {
               id
               name
               url
               applink
               imageUrl
               attribution
             }
             grids {
               id
               name
               url
               applink
               price
               slashedPrice
               discount
               imageUrl
               label
               soldPercentage
               attribution
               productClickUrl
               impression
               cashback
               isTopads
               freeOngkir {
                 isActive
                 imageUrl
               }
               recommendationType
               productViewCountFormatted
               isOutOfStock
               warehouseID
               parentProductID
               minOrder
               maxOrder
               shop {
                 shopID
               }
               labelGroup {
                 title
                 position
                 type
               }
               param
             }
             banner {
               id
               title
               description
               url
               back_color
               cta {
                 type
                 mode
                 text
                 coupon_code
               }
               applink
               text_color
               image_url
               attribution
               gradient_color
             }
             token
           }
         }
       }
    """.trimIndent()

    @JvmStatic
    fun createRequestParams(localCacheModel: LocalCacheModel?) = HashMap<String, Any>().apply {
        put(PARAM_LOCATION, mapLocation(localCacheModel))
    }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
    override fun getQuery(): String = QUERY
    override fun getTopOperationName(): String = OPERATION_NAME
}