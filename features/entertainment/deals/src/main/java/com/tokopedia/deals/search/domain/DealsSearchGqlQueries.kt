package com.tokopedia.deals.search.domain

/**
 * @author by jessica on 16/06/20
 */

object DealsSearchGqlQueries {

    fun getEventSearchQuery() = """
        query (${'$'}params: [MapParamData]!){
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

    fun getSearchInitialLoadQuery() = """
        query (${'$'}product: TravelColletiveCategory!,${'$'}params: [MapParamData]!) {
          TravelCollectiveRecentSearches(product:${'$'}product){
            items {
              product
              title
              subtitle
              prefix
              prefixStyling
              value
              webURL
              appURL
              imageURL
            }
            meta {
              title
              webURL
              appURL
            }
          }
          event_child_category(categoryName: "deal", categoryID: 15, verticalID: 2)
          {
            categories{
                id,
                parent_id,
                name,
                title,
                description,
                meta_title,
                meta_description,
                url,
                media_url,
                seo_url,
                priority,
                status,
                message,
                code,
                message_error,
                web_url,
                app_url,
                is_card,
              	is_hidden,
                inactiveMediaUrl,
            }
          }
          event_search(searchParams:${'$'}params)
          		{
                brands{
                    id
                    title
                    seo_url
                    featured_thumbnail_image
                  }
                products {
                    id
                    brand_id
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

    fun getSearchLoadModeQuery() = """
        query (${'$'}params: [MapParamData]!){
          event_search(searchParams:${'$'}params)
          		{
                products {
                  id
                  brand_id
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