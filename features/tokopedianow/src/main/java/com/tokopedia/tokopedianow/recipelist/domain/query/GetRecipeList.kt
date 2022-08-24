package com.tokopedia.tokopedianow.recipelist.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetRecipeList: GqlQueryInterface {

    const val PARAM_PAGE = "page"
    const val PARAM_PER_PAGE = "perPage"
    const val PARAM_WAREHOUSE_ID = "warehouseID"
    const val PARAM_TITLE = "title"
    const val PARAM_TAG_ID = "tagID"
    const val PARAM_CATEGORY_ID = "categoryID"
    const val PARAM_INGREDIENT_ID = "ingredientID"
    const val PARAM_FROM_DURATION = "fromDuration"
    const val PARAM_TO_DURATION = "toDuration"
    const val PARAM_FROM_PORTION = "fromPortion"
    const val PARAM_TO_PORTION = "toPortion"
    const val PARAM_SORT_BY = "sortBy"

    private const val OPERATION_NAME = "TokonowGetRecipes"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
        query $OPERATION_NAME(
            ${'$'}${PARAM_PAGE}: Int!, 
            ${'$'}${PARAM_PER_PAGE}: Int!,
            ${'$'}${PARAM_WAREHOUSE_ID}: String,
            ${'$'}${PARAM_TITLE}: String,
            ${'$'}${PARAM_TAG_ID}: [String!],
            ${'$'}${PARAM_CATEGORY_ID}: [String!],
            ${'$'}${PARAM_INGREDIENT_ID}: [String!],
            ${'$'}${PARAM_FROM_DURATION}: Int,
            ${'$'}${PARAM_TO_DURATION}: Int,
            ${'$'}${PARAM_FROM_PORTION}: Int,
            ${'$'}${PARAM_TO_PORTION}: Int,
            ${'$'}${PARAM_SORT_BY}: String
        ) {
            $OPERATION_NAME(input: {
                ${PARAM_PAGE}:${'$'}${PARAM_PAGE}, 
                ${PARAM_PER_PAGE}:${'$'}${PARAM_PER_PAGE},
                ${PARAM_WAREHOUSE_ID}:${'$'}${PARAM_WAREHOUSE_ID},
                ${PARAM_TITLE}:${'$'}${PARAM_TITLE},
                ${PARAM_TAG_ID}:${'$'}${PARAM_TAG_ID},
                ${PARAM_CATEGORY_ID}:${'$'}${PARAM_CATEGORY_ID},
                ${PARAM_INGREDIENT_ID}:${'$'}${PARAM_INGREDIENT_ID},
                ${PARAM_FROM_DURATION}:${'$'}${PARAM_FROM_DURATION},
                ${PARAM_TO_DURATION}:${'$'}${PARAM_TO_DURATION}
                ${PARAM_FROM_PORTION}:${'$'}${PARAM_FROM_PORTION}
                ${PARAM_TO_PORTION}:${'$'}${PARAM_TO_PORTION}
                ${PARAM_SORT_BY}:${'$'}${PARAM_SORT_BY}
            }) {
                header {
                  processTime
                  success
                  message
                  statusCode
                }
                metadata {
                  hasNext
                  total
                }
                data {
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