package com.tokopedia.tokopedianow.recipebookmark.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetRecipeBookmarksQuery: GqlQueryInterface {

    const val PARAM_WAREHOUSE_ID = "warehouseID"
    const val PARAM_PAGE = "page"
    const val PARAM_PER_PAGE = "perPage"

    private const val OPERATION_NAME = "TokonowGetRecipeBookmarks"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            query $OPERATION_NAME(
               ${'$'}$PARAM_WAREHOUSE_ID : String!,
               ${'$'}$PARAM_PAGE : Int!,
               ${'$'}$PARAM_PER_PAGE : Int!
            ) {
               $OPERATION_NAME(input: 
               {
                 $PARAM_WAREHOUSE_ID: ${'$'}$PARAM_WAREHOUSE_ID,
                 $PARAM_PAGE: ${'$'}$PARAM_PAGE,
                 $PARAM_PER_PAGE: ${'$'}$PARAM_PER_PAGE
               }) {
                	header {
                      success
                      processTime
                      message
                      statusCode
                    }
                    metadata {
                      hasNext
                      total
                    }
                    data {
                      userID
                      recipes {
                        id
                        title
                        status
                        instruction
                        portion
                        duration
                        createdTime
                        updatedTime
                        publishedTime
                        isBookmarked
                        category {
                          id
                          name
                        }
                        tags {
                          id
                          name
                        }
                        products {
                          id
                          quantity
                          detail {
                            name
                            url
                            imageUrl
                            fmtPrice
                            appUrl
                            slashedPrice
                            discountPercentage
                            parentProductID
                            minOrder
                            stock
                            categoryName
                            countReview
                            rating
                            ratingAverage
                            shopID
                            maxOrder
                            categoryID
                            redirectLink
                            labelGroups {
                              title
                              type
                              position
                              url
                            }
                            labelGroupVariants {
                              title
                              type
                              typeVariant
                              hexColor
                            }
                          }
                        }
                        ingredients {
                          id
                          name
                          quantity
                          unit
                          image {
                            urlOriginal
                            urlThumbnail
                            fileName
                            filePath
                            width
                            height
                          }
                        }
                        images {
                          urlOriginal
                          urlThumbnail
                          fileName
                          filePath
                          width
                          height
                        }
                        videos {
                          url
                          type
                        }
                        medias {
                          url
                          type
                        }
                        appUrl
                     }
                  }
               }
            }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}