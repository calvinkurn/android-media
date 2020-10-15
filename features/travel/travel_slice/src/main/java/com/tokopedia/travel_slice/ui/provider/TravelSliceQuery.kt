package com.tokopedia.travel_slice.ui.provider

/**
 * @author by jessica on 14/10/20
 */

object TravelSliceQuery {
    const val GET_HOTEL_PROPERTY_QUERY = """
        query PropertySearch(${'$'}data:PropertySearchRequest!){
            propertySearch(input: ${'$'}data){
              propertyList {
                name
                type
                address
                roomPrice{
                  totalPrice
                }
                star
                review {
                  reviewScore
                }
                location {
                  description
                }
              }
            }
            }
    """

}