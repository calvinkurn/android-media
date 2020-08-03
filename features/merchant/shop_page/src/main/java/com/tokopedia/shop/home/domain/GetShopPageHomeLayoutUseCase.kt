package com.tokopedia.shop.home.domain

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.*
import com.tokopedia.shop.home.GqlQueryConstant.GQL_GET_SHOP_PAGE_HOME_LAYOUT
import com.tokopedia.shop.home.data.model.ShopLayoutWidget
import com.tokopedia.usecase.coroutines.UseCase
import java.io.File
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Named

class GetShopPageHomeLayoutUseCase @Inject constructor(
        @Named(GQL_GET_SHOP_PAGE_HOME_LAYOUT)
        val gqlQuery: String,
        private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<ShopLayoutWidget>() {

    companion object {
        private const val KEY_SHOP_ID = "shopId"
        private const val KEY_STATUS = "status"
        private const val KEY_LAYOUT_ID = "layoutId"

        @JvmStatic
        fun createParams(
                shopId: String = "",
                status: String = "",
                layoutId: String = ""
        ) = mapOf<String, Any>(
                KEY_SHOP_ID to shopId,
                KEY_STATUS to status,
                KEY_LAYOUT_ID to layoutId
        )
    }

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): ShopLayoutWidget {
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.CLOUD_THEN_CACHE).build())

        val gqlRequest = GraphqlRequest(gqlQuery, ShopLayoutWidget.Response::class.java, params)
        gqlUseCase.addRequest(gqlRequest)
        val gqlResponse = gqlUseCase.executeOnBackground()
//        val gqlResponse = createMockGraphQlSuccessResponse()
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<ShopLayoutWidget.Response>(ShopLayoutWidget.Response::class.java)
                    .shopLayoutWidget
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }


    private fun createMockGraphQlSuccessResponse(): GraphqlResponse {
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val jsonObject: JsonObject = CommonUtils.fromJson(
                asdasd,
                JsonObject::class.java
        )
        val data = jsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
        val objectType = ShopLayoutWidget.Response::class.java
        val obj: Any =  Gson().fromJson(data, objectType)
        result[objectType] = obj
        return GraphqlResponse(result, errors, false)
    }


    private fun String.getJsonFromFile(): String {
        val uri = ClassLoader.getSystemClassLoader().getResource(this)
        val file = File(uri.path)
        return String(file.readBytes())
    }

    val asdasd ="""
       {
	"data": {
		"shopPageGetLayout": {
			"layoutID": 123,
			"layoutName": "Basic Layout",
			"masterLayoutID": 1,
			"merchantTierID": 1,
			"status": 1,
			"maxWidgets": 3,
			"publishDate": "2020-02-14 17:00:00",
			"widgets": [{
					"widgetID": 0,
					"widgetMasterID": 0,
					"layoutOrder": 0,
					"name": "play",
					"type": "dynamic",
					"header": {
						"title": "Yuk Tonton Sekarang",
						"ctaText": "lihat semua",
						"ctaLink": "https://www.tokopedia.com/official-store",
						"cover": "https://ecs7.tokopedia.net/img/cache/1190/attachment/2020/3/10/71731933/71731933_3a69ee2d-dcb9-48c8-9434-4fa5dbe0c480.jpg",
						"ratio": "",
						"isATC": 0
					},
					"data": []
				},
				{
					"widgetID": 0,
					"layoutOrder": 1,
					"name": "promo_campaign",
					"type": "campaign",
					"header": {
						"title": "Sneaker Sage Vol. II",
						"ctaText": "Lihat Semua",
						"ctaLink": "",
						"isActive": 1
					},
					"data": [{
						"campaignID": 1726,
						"name": "Sneaker Sage Vol. II",
						"description": "",
						"startDate": "2020-06-12 15:00:00",
						"endDate": "2020-07-10 16:30:00 +0000 +0000",
						"statusCampaign": "upcoming",
						"timeDescription": "Berakhir dalam",
						"timeCounter": "164",
						"totalNotify": 200,
						"totalNotifyWording": "orang tertarik campaign ini",
						"banners": [{
								"imageID": "1",
								"imageURL": "https://ecs7.tokopedia.net/img/attachment/2019/10/29/5605351/5605351_a6d29f7a-ab54-494c-acdb-42b2aa9f3151",
								"bannerType": "upcoming"
							},
							{
								"imageID": "2",
								"imageURL": "https://ecs7.tokopedia.net/img/attachment/2019/10/29/5605351/5605351_a6d29f7a-ab54-494c-acdb-42b2aa9f3151",
								"bannerType": "ongoing"
							}, {
								"imageID": "3",
								"imageURL": "https://ecs7.tokopedia.net/img/attachment/2019/10/29/5605351/5605351_a6d29f7a-ab54-494c-acdb-42b2aa9f3151",
								"bannerType": "finished"
							}
						],
						"products": [{
							"id": 1,
							"name": "Sneaker",
							"url": "https://ecs7.tokopedia.net/i",
							"urlApps": "apps:",
							"urlMobile": "m.tokopedia",
							"imageURL": "https://ecs7.tokopedia.net/i",
							"price": "240000",
							"countSold": 2,
							"stock": 6,
							"status": "ready",
							"discountedPrice": "Rp 100.000",
							"discountPercentage": 44,
							"posistion": "1",
							"stockWording": {
								"title": "Tersedia"
							}
						}]
					}]
				},
				{
					"widgetID": 1,
					"layoutOrder": 2,
					"name": "slider_banner",
					"type": "display",
					"header": {
						"title": "Anniversary Deals",
						"ratio": "2:1"
					},
					"data": [{
						"imageUrl": "https://tokopedia.com/sss/bbb.jpg",
						"desktopImageUrl": "https://tokopedia.com/sss/bbb.jpg",
						"appLink": "tokopedia://shop/2400899/etalase/21735836",
						"webLink": "https://m.tokopedia.com/etalase/21735836",
						"videoUrl": ""
					}, {
						"imageUrl": "https://tokopedia.com/sss/bbb.jpg",
						"appLink": "tokopedia://shop/2400899/etalase/21735836",
						"webLink": "https://m.tokopedia.com/etalase/21735836",
						"videoUrl": ""
					}]
				}
			]
		}
	}
}
    """.trimIndent()

    fun clearCache() {
        gqlUseCase.clearCache()
    }

}