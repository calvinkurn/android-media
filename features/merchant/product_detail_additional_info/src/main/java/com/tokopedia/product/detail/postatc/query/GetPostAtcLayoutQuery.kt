package com.tokopedia.product.detail.postatc.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetPostAtcLayoutQuery : GqlQueryInterface {

    private const val OPERATION_NAME = "PostATCLayout"
    private const val PARAM_PRODUCT_ID = "productID"
    private const val PARAM_CART_ID = "cartID"
    private const val PARAM_LAYOUT_ID = "layoutID"

    override fun getOperationNameList() = listOf(OPERATION_NAME)

    override fun getQuery() = """
        query $OPERATION_NAME(
            $$PARAM_PRODUCT_ID: String, 
            $$PARAM_LAYOUT_ID: String, 
            $$PARAM_CART_ID: String
        ) {
            pdpGetPostATCLayout(
                $PARAM_PRODUCT_ID: $$PARAM_PRODUCT_ID, 
                $PARAM_LAYOUT_ID: $$PARAM_LAYOUT_ID, 
                $PARAM_CART_ID: $$PARAM_CART_ID
            ) {
                name
                basicInfo {
                    shopID
                    category {
                        id
                        name
                    }
                }
                components {
                    name
                    type
                    data {
                        ... on pdpProductPostATCInfo {
                            title
                            subTitle
                            image
                            button {
                                text
                                cartID
                            }
                        }
                    }
                }
            }
        }
    """.trimIndent()

    override fun getTopOperationName() = OPERATION_NAME

    fun createParams(
        productId: String,
        cartId: String,
        layoutId: String
    ) = mapOf(
        PARAM_PRODUCT_ID to productId,
        PARAM_CART_ID to cartId,
        PARAM_LAYOUT_ID to layoutId
    )
}
