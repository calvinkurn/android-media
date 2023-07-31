package com.tokopedia.product.detail.postatc.data.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetPostAtcLayoutQuery : GqlQueryInterface {

    private const val OPERATION_NAME = "PostATCLayout"
    private const val PARAM_PRODUCT_ID = "productID"
    private const val PARAM_CART_ID = "cartID"
    private const val PARAM_LAYOUT_ID = "layoutID"
    private const val PARAM_PAGE_SOURCE = "source"
    private const val PARAM_POST_ATC_SESSION = "postATCSession"

    override fun getOperationNameList() = listOf(OPERATION_NAME)

    override fun getQuery() = """
        query $OPERATION_NAME(
            $$PARAM_PRODUCT_ID: String,
            $$PARAM_LAYOUT_ID: String,
            $$PARAM_CART_ID: String,
            $$PARAM_PAGE_SOURCE: String,
            $$PARAM_POST_ATC_SESSION: String
        ) {
            pdpGetPostATCLayout(
                $PARAM_PRODUCT_ID: $$PARAM_PRODUCT_ID,
                $PARAM_LAYOUT_ID: $$PARAM_LAYOUT_ID,
                $PARAM_CART_ID: $$PARAM_CART_ID,
                $PARAM_PAGE_SOURCE: $$PARAM_PAGE_SOURCE,
                $PARAM_POST_ATC_SESSION: $$PARAM_POST_ATC_SESSION
            ) {
                name
                basicInfo {
                    shopID
                    category {
                        id
                        name
                    }
                    price
                    originalPrice
                    condition
                }
                postATCInfo {
                    title
                    image
                    button {
                        text
                        cartID
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
                        ... on pdpProductPostATCAddOns {
                            title
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
        layoutId: String,
        pageSource: String,
        postAtcSession: String
    ) = mapOf(
        PARAM_PRODUCT_ID to productId,
        PARAM_CART_ID to cartId,
        PARAM_LAYOUT_ID to layoutId,
        PARAM_PAGE_SOURCE to pageSource,
        PARAM_POST_ATC_SESSION to postAtcSession
    )
}
