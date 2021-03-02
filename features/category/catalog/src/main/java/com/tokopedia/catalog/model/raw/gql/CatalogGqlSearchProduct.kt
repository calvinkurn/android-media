package com.tokopedia.catalog.model.raw.gql

const val CATALOG_GQL_SEARCH_PRODUCT: String =  """
    query SearchProduct(${'$'}params: String!) {
        ace_search_product_v4(params: ${'$'}params) {
            header {
                totalData
                totalDataText
                defaultView
                responseCode
                errorMessage
                additionalParams
                keywordProcess
            }
            data {
                isQuerySafe
                autocompleteApplink
                redirection {
                    redirectApplink
                }
                ticker {
                    text
                    query
                    typeId
                }
                related {
                    relatedKeyword
                    position
                    otherRelated {
                        keyword
                        url
                        applink
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
                            }
                        }
                    }
                }
                suggestion {
                    suggestion
                    query
                    text
                }
                products {
                    id
                    name
                    ads {
                        id
                        productClickUrl
                        productWishlistUrl
                        productViewUrl
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
                }
            }
        }
    }
"""