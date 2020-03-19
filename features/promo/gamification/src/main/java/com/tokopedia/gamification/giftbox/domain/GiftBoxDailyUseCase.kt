package com.tokopedia.gamification.giftbox.domain

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.data.di.GIFT_BOX_DAILY
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxEntity
import com.tokopedia.gamification.giftbox.presentation.helpers.Util
import com.tokopedia.gamification.pdp.data.GqlUseCaseWrapper
import javax.inject.Inject
import javax.inject.Named

class GiftBoxDailyUseCase @Inject constructor(@Named(GIFT_BOX_DAILY) val queryString: String, val gqlWrapper: GqlUseCaseWrapper) {

    suspend fun getResponse(map: HashMap<String, Any>): GiftBoxEntity {
        return gqlWrapper.getResponse(GiftBoxEntity::class.java, queryString, map)
    }

    fun getRequestParams(campaignSlug: String, pageName: String): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map[Params.PAGE] = pageName
        map[Params.CAMPAIGN_SLUG] = campaignSlug
        return map
    }

    object Params {
        const val CAMPAIGN_SLUG = "campaignSlug"
        const val PAGE = "page"
    }

    fun getFakeResponse(): GiftBoxEntity {
        val str = "{\n" +
                "  \"gamiLuckyHome\": {\n" +
                "    \"resultStatus\": {\n" +
                "      \"code\": \"200\",\n" +
                "      \"message\": [\n" +
                "        \"Success\"\n" +
                "      ],\n" +
                "      \"reason\": \"\"\n" +
                "    },\n" +
                "    \"tokensUser\": {\n" +
                "      \"state\": \"active\",\n" +
                "      \"campaignSlug\": \"DAILY2020\",\n" +
                "      \"title\": \"Tap kotaknya untuk dapetin hadiah\",\n" +
                "      \"text\": \"Hadiah yang bisa kamu dapatkan:\",\n" +
                "      \"desc\": \"\"\n" +
                "    },\n" +
                "    \"liveFeedChannel\": 1,\n" +
                "    \"tokenAsset\": {\n" +
                "      \"backgroundImgURL\": \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/lucky-bg.png\",\n" +
                "      \"imageV2URLs\": [\n" +
                "        \"https://ecs7.tokopedia.net/assets-tokopoints/prod/images/2019/02/emptyegg.png\",\n" +
                "        \"https://ecs7.tokopedia.net/assets-tokopoints/prod/images/2019/02/emptyegg.gif\"\n" +
                "      ]\n" +
                "    },\n" +
                "    \"actionButton\": [],\n" +
                "    \"prizeList\": [\n" +
                "      {\n" +
                "        \"isSpecial\": false,\n" +
                "        \"imageURL\": \"http://tokopedia.com/ovopoints.png\",\n" +
                "        \"text\": [\n" +
                "          \"OVO Points\"\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"isSpecial\": true,\n" +
                "        \"imageURL\": \"http://tokopedia.com/tokopoints.png\",\n" +
                "        \"text\": [\n" +
                "          \"Special Hari Ini\",\n" +
                "          \"Kupon Diskon Bimoli\",\n" +
                "          \"Jadi Rp100\"\n" +
                "        ]\n" +
                "      }\n" +
                "    ],\n" +
                "    \"reminder\": {\n" +
                "      \"text\": \"Besok bisa dapet hadiah lagi lho. Mau diingetin?\",\n" +
                "      \"enableText\": \"Ingatkan Besok\",\n" +
                "      \"disableText\": \"Pengingat Nyala\"\n" +
                "    }\n" +
                "  }\n" +
                "}"
        val type = object : TypeToken<GiftBoxEntity>() {}.type
        val entity = Gson().fromJson<GiftBoxEntity>(str, type)
        return entity
    }


}