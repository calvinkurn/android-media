package com.tokopedia.product.info.data.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by yovi.putra on 16/09/22"
 * Project name: android-tokopedia-core
 **/

object GetProductDetailBottomSheetQuery : GqlQueryInterface {
    private const val OPERATION_NAME = "PdpGetDetailBottomSheet"
    private const val SHOP_ID_PARAM = "shopId"
    private const val PRODUCT_ID_PARAM = "productId"
    private const val GIFTABLE_PARAM = "isGiftable"
    private const val PARENT_ID_PARAM = "parentId"
    private const val BOTTOM_SHEET_PARAM = "bottomsheetParam"
    private const val CATALOG_ID_PARAM = "catalogId"

    fun createParams(
        productId: String,
        shopId: String,
        parentId: String,
        isGiftable: Boolean,
        catalogId: String,
        bottomSheetParam: String
    ): HashMap<String, Any> = hashMapOf(
        PRODUCT_ID_PARAM to productId,
        SHOP_ID_PARAM to shopId,
        PARENT_ID_PARAM to parentId,
        GIFTABLE_PARAM to isGiftable,
        CATALOG_ID_PARAM to catalogId,
        BOTTOM_SHEET_PARAM to bottomSheetParam
    )

    override fun getOperationNameList() = listOf(OPERATION_NAME)

    override fun getQuery() = """
            query $OPERATION_NAME(${'$'}$PRODUCT_ID_PARAM:String,${'$'}$SHOP_ID_PARAM:String,${'$'}$CATALOG_ID_PARAM:String, ${'$'}$GIFTABLE_PARAM:Boolean, ${'$'}$PARENT_ID_PARAM:String, ${'$'}$BOTTOM_SHEET_PARAM:String){
              pdpGetDetailBottomSheet(productID:${'$'}$PRODUCT_ID_PARAM, shopID:${'$'}$SHOP_ID_PARAM, catalogID:${'$'}$CATALOG_ID_PARAM, isGiftable:${'$'}$GIFTABLE_PARAM, parentID:${'$'}$PARENT_ID_PARAM, bottomsheetParam:${'$'}$BOTTOM_SHEET_PARAM){
                bottomsheetData{
                  title
                  componentName
                  isApplink
                  isShowable
                  value
                  applink
                  icon
                  row {
                    key
                    value
                  }
                }
                dataShopNotes{
                  shopNotesData{
                    shopNotesID
                    title
                    content
                    isTerms
                    position
                    url
                    updateTime
                    updateTimeUTC
                  }
                  error
                }
                discussion{
                  title
                  buttonType
                  buttonCopy
                }
                error{
                  Code
                  Message
                  DevMessage
                }
              }
            }""".trimIndent()

    override fun getTopOperationName() = OPERATION_NAME

}
