package com.tokopedia.shop.score.performance.domain.usecase

import com.tokopedia.shop.score.performance.domain.model.ShoLevelTooltipParam
import com.tokopedia.usecase.RequestParams

class GetShopLevelTooltipUseCase {

    companion object {
        //need adjust type input
        const val SHOP_LEVEL_INPUT = "input"
        const val SHOP_ID = "shopID"
        val SHOP_LEVEL_TOOLTIP = """
            query shopLevel(${'$'}input: ShopLevelRequest!){
              shopLevel(input: ${'$'}input){
                result {
                  shopID
                  period
                  nextUpdate
                  shopLevel
                  itemSold
                  niv
                }
                error {
                  message
                }
              }
            }
        """.trimIndent()

        @JvmStatic
        fun createParams(shopID: Int, shoScoreTooltipParam: ShoLevelTooltipParam): RequestParams = RequestParams.create().apply {
            putInt(SHOP_ID, shopID)
            putObject(SHOP_LEVEL_INPUT, shoScoreTooltipParam)
        }
    }

    var requestParams: RequestParams = RequestParams.EMPTY



}