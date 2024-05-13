package com.tokopedia.search.result.domain.usecase.searchproduct.query

import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery(
    AceSearchProductQuery.QUERY_NAME,
    AceSearchProductQuery.ACE_SEARCH_PRODUCT_QUERY
)
object AceSearchProductQuery {

    const val QUERY_NAME = "AceSearchProduct"
    const val ACE_SEARCH_PRODUCT_QUERY = """
        query SearchProduct(${'$'}params: String!) {
            ace_search_product_v4(params: ${'$'}params) {
                header {
                    totalData
                    totalDataText
                    responseCode
                    errorMessage
                    additionalParams
                    keywordProcess
                    componentId
                    meta {
                        productListType
                        isPostProcessing
                        showButtonAtc
                    }
                }
                data {
                    isQuerySafe
                    autocompleteApplink
                    backendFilters
                    backendFiltersToggle
                    keywordIntention
                    redirection {
                        redirectApplink
                    }
                    ticker {
                        text
                        query
                        typeId
                        componentId
                        trackingOption
                    }
                    banner {
                        position
                        text
                        applink
                        imageUrl
                        componentId
                        trackingOption
                    }
                    related {
                        relatedKeyword
                        position
                        trackingOption
                        otherRelated {
                            keyword
                            url
                            applink
                            componentId
                            product {
                                id
                                name
                                price
                                imageUrl
                                url
                                applink
                                priceStr
                                wishlist
                                ratingAverage
                                componentId
                                warehouseIdDefault
                                labelGroups {
                                    title
                                    position
                                    type
                                    url
                                }
                                shop {
                                    city
                                }
                                badges {
                                    imageUrl
                                    title
                                    show
                                }
                                freeOngkir {
                                    isActive
                                    imgUrl
                                }
                                ads {
                                    id
                                    productClickUrl
                                    productWishlistUrl
                                    productViewUrl
                                    creativeID
                                    logExtra
                                }
                            }
                        }
                    }
                    suggestion {
                        suggestion
                        query
                        text
                        componentId
                        trackingOption
                    }
                    products {
                        id
                        name
                        ads {
                            id
                            productClickUrl
                            productWishlistUrl
                            productViewUrl
                            tag
                            creativeID
                            logExtra
                        }
                        shop {
                            id
                            name
                            city
                            url
                            isOfficial
                            isPowerBadge
                        }
                        freeOngkir {
                            isActive
                            imgUrl
                        }
                        imageUrl
                        imageUrl300
                        imageUrl700
                        price
                        priceInt
                        priceRange
                        categoryId
                        categoryName
                        categoryBreadcrumb
                        ratingAverage
                        priceInt
                        originalPrice
                        discountPercentage
                        warehouseIdDefault
                        boosterList
                        source_engine
                        minOrder
                        url
                        labelGroups {
                            title
                            position
                            type
                            url
                        }
                        labelGroupVariant {
                            title
                            type
                            type_variant
                            hex_color
                        }
                        badges {
                            title
                            imageUrl
                            show
                        }
                        wishlist
                        applink
                        customVideoURL
                        parentId
                        isPortrait
                    }
                    violation {
                        headerText
                        descriptionText
                        imageURL
                        ctaApplink
                        buttonText
                        buttonType
                    }
                }
            }
        }
    """
}
