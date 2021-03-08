package com.tokopedia.recharge_slice.util

object RechargeSliceGqlQuery {
    val rechargeFavoriteRecommendationList = """
        query rechargeFavoriteRecommendationList(${'$'}device_id: Int!) {
        	rechargeFavoriteRecommendationList(device_id:${'$'}device_id) {
        		title
        		recommendations{
        		    productId
        			iconUrl
        			title
        			clientNumber
        			operatorName
        			appLink
        			webLink
        			position
        			categoryName
        			categoryId
        			productName
        			type
        			productPrice
        		}
        	}
        }
    """.trimIndent()
}