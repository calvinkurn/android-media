package com.tokopedia.deals.home.domain

/**
 * @author by jessica on 18/06/20
 */

object DealsGqlHomeQuery {

    fun getEventHomeQuery() = """
        query getEventHome(${'$'}category: String!, ${'$'}additionalParams: [MapParamData]!){
          event_home(category:${'$'}category, additionalParams:${'$'}additionalParams){
            layout{
              id
              title
              description
              seo_url
              is_card
              is_hidden
              priority
              media_url
              inactive_media_url
              count
              web_url
              app_url
              items{
                id
                brand_id
                category_id
                provider_id
                child_category_ids
                city_ids
                display_name
                title
                seo_url
                image_web
                thumbnail_web
                image_app
                thumbnail_app
                meta_title
                meta_description
                meta_keywords
                display_tags
                mrp
                sales_price
                priority
                is_featured
                is_promo
                is_food_available
                is_searchable
                is_top
                status
                min_start_date
                max_end_date
                sale_start_date
                sale_end_date
                custom_labels
                custom_text_1
                city_name
                rating
                likes
                is_liked
                saving
                saving_percentage
                location
                web_url
                app_url
                brand{
                  id
                  title
                  description
                  url
                  seo_url
                  featured_image
                  featured_thumbnail_image
                  city_name
                }
                category{
                  id
                  title
                  media_url
                  url
                }
              }
            }
           	meta_description
            meta_title
            ticker{
              devices
              message
            }
          }
        }
    """
}