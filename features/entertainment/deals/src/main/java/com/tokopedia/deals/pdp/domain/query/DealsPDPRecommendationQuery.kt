package com.tokopedia.deals.pdp.domain.query

import com.tokopedia.deals.common.model.request.RequestParam
import com.tokopedia.deals.search.DealsSearchConstants
import com.tokopedia.gql_query_annotation.GqlQueryInterface

object DealsPDPRecommendationQuery: GqlQueryInterface {
    private const val OPERATION_NAME = "DealsPDPRecommendationQuery"
    private val QUERY = """
        query $OPERATION_NAME(${'$'}params: [MapParamData]!){
          event_search(searchParams:${'$'}params)
          		{
          		brands{
                  id
                  title
                  description
                  url
                  seo_url
                  featured_image
                  featured_thumbnail_image
                  city_name
                }
                products {
                  id
                  brand_id
                  image_app
                  display_name
                  seo_url
                  thumbnail_app
                  mrp
                  sales_price
                  display_tags
                  priority
                  min_start_date
                  max_end_date
                  sale_start_date
                  sale_end_date
                  custom_text_1
                  saving_percentage
                  web_url
                  app_url
                  brand{
                    id
                    title
                    seo_url
                    featured_thumbnail_image
                  }
                  category{
                    id
                    title
                    media_url
                    url
                  }
                }
          }
        }
    """.trimIndent()

    @JvmStatic
    fun createRequestParam(
        childCategoryIds: String?,
    ) : HashMap<String, Any> {
        val hashMap = hashMapOf<String, Any>()
        val pdpRecommendation: ArrayList<RequestParam> = arrayListOf()
        pdpRecommendation.add(RequestParam(DealsSearchConstants.MAP_CATEGORY, DealsSearchConstants.DEFAULT_CATEGORY))
        pdpRecommendation.add(RequestParam(DealsSearchConstants.MAP_TREE, DealsSearchConstants.BRAND_PRODUCT_TREE))
        if (childCategoryIds != null) {
            pdpRecommendation.add(RequestParam(DealsSearchConstants.MAP_CHILD_CATEGORY_IDS, childCategoryIds))
        }
        hashMap.put(DealsSearchConstants.SEARCH_PARAM, pdpRecommendation)
        return hashMap
    }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
    override fun getQuery(): String = QUERY
    override fun getTopOperationName(): String = OPERATION_NAME
}