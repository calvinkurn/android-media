package com.tokopedia.tokopedianow.recipedetail.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetRecipe: GqlQueryInterface {

    const val PARAM_RECIPE_ID = "recipeID"
    const val PARAM_SLUG = "slug"
    const val PARAM_WAREHOUSE_ID = "warehouseID"

    private const val OPERATION_NAME = "TokonowGetRecipe"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
        query $OPERATION_NAME(
                ${'$'}${PARAM_RECIPE_ID}: String!, 
                ${'$'}${PARAM_SLUG}: String!, 
                ${'$'}${PARAM_WAREHOUSE_ID}: String) {
            $OPERATION_NAME(input: {
                ${PARAM_RECIPE_ID}:${'$'}${PARAM_RECIPE_ID}, 
                ${PARAM_SLUG}:${'$'}${PARAM_SLUG}, 
                ${PARAM_WAREHOUSE_ID}:${'$'}${PARAM_WAREHOUSE_ID}
            }) {
                header {
                  success
                  processTime
                  message
                  statusCode
                }
                data {
                  id
                  title
                  status
                  instruction
                  portion
                  duration
                  isBookmarked
                  url
                  category {
                    id
                    name
                  }
                  tags {
                    id
                    name
                  }
                  ingredients {
                    id
                    name
                    quantity
                    unit
                  }
                  images {
                    urlOriginal
                    urlThumbnail
                  }
                  videos {
                    url
                    type
                  }
                  medias {
                    url
                    type
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
                    similarProducts {
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