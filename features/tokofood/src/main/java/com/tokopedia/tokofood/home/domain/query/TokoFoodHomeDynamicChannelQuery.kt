package com.tokopedia.tokofood.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import java.lang.StringBuilder

object TokoFoodHomeDynamicChannelQuery: GqlQueryInterface {

    private const val PARAM_LOCATION = "location"
    private const val PARAM_USER_LAT = "user_lat"
    private const val PARAM_USER_LONG = "user_long"
    private const val PARAM_USER_CITY_ID = "user_cityId"
    private const val PARAM_USER_DISTRICT_ID = "user_districtId"
    private const val PARAM_USER_POSTAL_CODE = "user_postCode"
    private const val PARAM_USER_ADDRESS_ID = "user_addressId"
    private const val PARAM_WAREHOUSE_IDS = "warehouse_ids"

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

    private fun mapLocation(localCacheModel: LocalCacheModel?): String {
        val stringBuilder = StringBuilder()
        localCacheModel?.run {
            val locationParamsMap = mutableMapOf<String, String>()

            locationParamsMap[PARAM_USER_LAT] = lat
            locationParamsMap[PARAM_USER_LONG] = long
            locationParamsMap[PARAM_USER_CITY_ID] = city_id
            locationParamsMap[PARAM_USER_DISTRICT_ID] = district_id
            locationParamsMap[PARAM_USER_POSTAL_CODE] = postal_code
            locationParamsMap[PARAM_USER_ADDRESS_ID] = address_id
            locationParamsMap[PARAM_WAREHOUSE_IDS] = warehouse_id

            for((key, value) in locationParamsMap) {
                if(stringBuilder.isNotBlank()) {
                    stringBuilder.append("&")
                }
                stringBuilder.append("$key=$value")
            }
        }
        return stringBuilder.toString()
    }
}