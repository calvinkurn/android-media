package com.tokopedia.deals.pdp.domain.query

import com.tokopedia.deals.pdp.domain.query.DealsPDPRecommendationQuery.DEALS_PDP_RECOMMENDATION_OPERATION_NAME
import com.tokopedia.deals.pdp.domain.query.DealsPDPRecommendationQuery.DEALS_PDP_RECOMMENDATION_QUERY
import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery(
    DEALS_PDP_RECOMMENDATION_OPERATION_NAME,
    DEALS_PDP_RECOMMENDATION_QUERY
)
object DealsPDPRecommendationQuery {
    const val DEALS_PDP_RECOMMENDATION_OPERATION_NAME = "DealsPDPRecommendationsQuery"
    const val DEALS_PDP_RECOMMENDATION_QUERY = """
        query $DEALS_PDP_RECOMMENDATION_OPERATION_NAME(${'$'}params: [MapParamData]!){
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
    """
}
