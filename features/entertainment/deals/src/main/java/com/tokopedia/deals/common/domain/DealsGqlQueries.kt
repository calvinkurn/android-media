package com.tokopedia.deals.common.domain

/**
 * @author by jessica on 16/06/20
 */

object DealsGqlQueries {
    fun getEventSearchQuery() = """
        query (${'$'}params: [MapParamData]!){
         event_location_search(searchParams:${'$'}params)
          {
            locations {
              id
              name
              search_name
              coordinates
              address
              city_id
              city_name
              priority
              icon_web
              icon_app
              image_web
              image_app
              location_type {
                id
                name
                display_name
                search_radius
                outer_radius
                icon
                type_id
                status
                custom_text_1
                icon_web
                icon_app
              }
            }
            page {
              next_page
              prev_page
            }
            count
          }
        }
    """

    fun getChildCategory() = """
        {
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
                  }
    """.trimIndent()

    fun getCategoryBrandProduct() = """
        query (${'$'}params: [MapParamData]!){
          event_search(searchParams:${'$'}params)
          		{
          	brands{
                 id
          title
          description
          seo_url
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
    """.trimIndent()
}