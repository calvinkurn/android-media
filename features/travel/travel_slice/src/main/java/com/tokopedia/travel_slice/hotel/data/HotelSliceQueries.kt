package com.tokopedia.travel_slice.hotel.data

/**
 * @author by jessica on 25/11/20
 */

object HotelSliceQueries {
    const val SEARCH_SUGGESTION = """
        query suggestion(${'$'}data: PropertySuggestionSearchParam!) {
          propertySearchSuggestion(input: ${'$'}data) {
            data {
              searchType
              searchID
            }
          }
        }
    """
    const val GET_HOTEL_PROPERTY_QUERY = """
        query PropertySearch(${'$'}data:PropertySearchRequest!){
            propertySearch(input: ${'$'}data){
              propertyList {
                id
                name
                roomPrice {
                  totalPrice
                }
                image {
                  urlMax300
                }
                location {
                  cityName
                }
              }
            }
        }
    """

    val HOTEL_ORDER_LIST_QUERY = """
        query OrderListQuery(${'$'}orderCategory: OrderCategory, ${'$'}Page: Int!, ${'$'}PerPage: Int!) {
          orders(orderCategory:${'$'}orderCategory, Page:${'$'}Page, PerPage:${'$'}PerPage) {
            title
            id
            statusStr
            appLink
            items {
              imageUrl
            }
          }
        }
    """.trimIndent()
}