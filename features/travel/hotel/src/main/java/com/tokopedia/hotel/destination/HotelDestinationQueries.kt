package com.tokopedia.hotel.destination

/**
 * @author by jessica on 03/11/20
 */

object HotelDestinationQueries {
    val GET_POPULAR_PROPERTY_QUERY = """
        query {
          propertyPopular {
            name
            destinationID
            type
            subLocation
            image
            metaDescription
            searchID
          }
        }
    """.trimIndent()

    val GET_HOTEL_RECENT_SEARCH_QUERY = """
        query propertyRecentSearch(${'$'}id: Int!){
         status
         travelRecentSearch(dataType:HOTEL, userID:${'$'}id){
           UUID
           property {
             type
             value
             ID
             location{
               district
               region
               city
               country
             }
           }
           startTime
           endTime
           lastSearch
           customer {
             adult
             child
             class
             infant
             room
           }
         }
        }
    """.trimIndent()
}