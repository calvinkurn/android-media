package com.tokopedia.product.manage.common.constant

object GetProductV3QueryConstant {
    private val BASE_QUERY = """
            query getProductV3(%1s) {
                getProductV3(%2s) {
                    %3s
                  }
                }
        """.trimIndent()

    private val GET_PRODUCT_USE_CASE_FIRST_PARAM = """
        ${'$'}productID: String!,${'$'}options: OptionV3!
    """.trimIndent()

    private val GET_PRODUCT_USE_CASE_SECOND_PARAM = """
        productID:${'$'}productID, options:${'$'}options
    """.trimIndent()

    private val GET_PRODUCT_USE_CASE_REQUEST = """
                    productID
                    productName
                   	status
                    stock
                   	priceCurrency
                    price
                    lastUpdatePrice
                    minOrder
                    maxOrder
                    description
                    weightUnit
                    weight
                    condition
                    mustInsurance
                    isKreasiLokal
                    mustInsurance
                    alias
                    sku
                    gtin
                    url
                    brand{
                      brandID
                      name
                      brandStatus
                      isActive
                    }
                    catalog{
                      catalogID
                      name
                      url
                    }
                    category{
                      id
                      name
                      title
                      isAdult
                      breadcrumbURL
                      detail{
                        id
                        name
                        breadcrumbURL
                        isAdult
                      }
                    }
                    menus
                    pictures{
                      picID
                      description
                      filePath
                      fileName
                      width
                      height
                      urlOriginal
                      urlThumbnail
                      url300
                      status
                    }
                    position{
                      position
                      isSwap
                    }
                    preorder{
                      duration
                      timeUnit
                      isActive
                    }
                    shop {
                      id
                    }
                    wholesale{
                      minQty
                      price
                    }
                    campaign{
                      campaignID
                      campaignType
                      campaignTypeName
                      percentageAmount
                      originalPrice
                      discountedPrice
                      originalStock
                      isActive
                    }
                    video{
                      source
                      url
                    }
                    cashback {
                      percentage
                    }
                    lock{
                      full
                      partial{
                        price
                        status
                        stock
                        wholesale
                        name
                      }
                    }
                    stats{
                      countView
                      countReview
                      countTalk
                      rating
                    }
                    txStats{
                      itemSold
                      txSuccess
                      txReject
                    }
                    variant{
                      products{
                        productID
                        status
                        combination
                        isPrimary
                        price
                        sku
                        stock
                        pictures {
                          picID
                          description
                          filePath
                          fileName
                          width
                          height
                          isFromIG
                          urlOriginal
                          urlThumbnail
                          url300
                          status
                        }
                      }
                      selections{
                        variantID
                        variantName
                        unitName
                        unitID
                        unitName
                        identifier
                        options{
                          unitValueID
                          value
                          hexCode
                        }
                      }
                      sizecharts{
                        picID
                        description
                        filePath
                        fileName
                        width
                        height
                        urlOriginal
                        urlThumbnail
                        url300
                        status
                      }
                    }
        """.trimIndent()

    fun getProductUseCaseQuery() = String.format(
            BASE_QUERY,
            GET_PRODUCT_USE_CASE_FIRST_PARAM,
            GET_PRODUCT_USE_CASE_SECOND_PARAM,
            GET_PRODUCT_USE_CASE_REQUEST
    )

}