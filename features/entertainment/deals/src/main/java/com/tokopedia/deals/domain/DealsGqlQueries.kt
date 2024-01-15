package com.tokopedia.deals.domain

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
}
