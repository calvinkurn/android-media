package com.tokopedia.common_digital.common.util

object GqlQuery {
    val rechargeFavoriteRecommendationList = """
        query rechargeFavoriteRecommendationList(${'$'}device_id: Int!) {
        	rechargeFavoriteRecommendationList(device_id:${'$'}device_id) {
        		title
        		recommendations{
        			iconUrl
        			title
        			clientNumber
        			appLink
        			webLink
        			position
        			categoryName
        			categoryId
        			productName
        			type
        		}
        	}
        }
    """.trimIndent()
}