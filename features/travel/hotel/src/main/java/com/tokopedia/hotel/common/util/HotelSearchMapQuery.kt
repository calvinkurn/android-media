package com.tokopedia.hotel.common.util

object HotelSearchMapQuery {
    val propertySearchInput = """
    query PropertySearch(${'$'}data:PropertySearchRequest!) {
        propertySearch(input: ${'$'}data){
        propertyList {
            id
            name
            type
            address
            roomPrice {
                price
                priceAmount
                totalPrice
                totalPriceAmount
                extraCharges {
                    netPrice
                    netPriceAmount
                    extraChargeInfo {
                        name
                        excluded
                        type
                        price
                        priceAmount
                    }
                }
                deals {
                    tagging
                    price
                    priceAmount
                }
            }
            roomAvailability
            image {
                isLogoPhoto
                urlSquare60
                mainPhoto
                urlOriginal
                urlMax300
            }
            star
            features
            review {
                reviewScore
                reviewDescription
            }
            location {
                cityName
                description
                latitude
                longitude
            }
            isDirectPayment
            safetyBadge {
                show
                title
                content
                icon {
                    dark
                    light
                }
            }
        }
        propertyDisplayInfo {
            filter {
                filterPrice {
                    minPrice
                    maxPrice
                }
                filterAccomodation {
                    ID
                    name
                    displayName
                }
                filterPreferences {
                    ID
                    name
                    displayName
                    type
                }
                filterStar {
                    stars
                }
                filterReview {
                    minReview
                    maxReview
                }
            }
            sort {
                name
                displayName
            }
        }
        filters {
            type
            name
            displayName
            options
            image {
                light
                dark
            }
        }
        quickFilter {
            name
            displayName
            values
            selected
        }
    }
    }""".trimIndent()
}