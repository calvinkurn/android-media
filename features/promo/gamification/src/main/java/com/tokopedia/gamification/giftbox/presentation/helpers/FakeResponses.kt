package com.tokopedia.gamification.giftbox.presentation.helpers

object FakeResponses {

    object GamiCrackResponse {
        val COUPONS_ONLY = "{\n" +
                "  \"crackResult\": {\n" +
                "    \"resultStatus\": {\n" +
                "      \"code\": \"200\",\n" +
                "      \"message\": [\n" +
                "        \"success\"\n" +
                "      ],\n" +
                "      \"status\": \"\"\n" +
                "    },\n" +
                "    \"imageUrl\": \"\",\n" +
                "    \"benefitText\": [\n" +
                "      \"Yeay kamu dapat\",\n" +
                "      \"Kupon Cashback & 500 OVO Points\"\n" +
                "    ],\n" +
                "    \"benefits\": [\n" +
                "      {\n" +
                "        \"text\": \"Kupon Cashback\",\n" +
                "        \"color\": \"#FFFFFF\",\n" +
                "        \"imageUrl\": \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/icon-coupon.png\",\n" +
                "        \"benefitType\": \"coupon\",\n" +
                "        \"isBigPrize\": false,\n" +
                "        \"isAutoApply\": true,\n" +
                "        \"autoApplyMsg\": \"Kupon Cashback udah terpasang ya. Ayo beli sekarang!\",\n" +
                "        \"dummyCode\": \"qweqw\",\n" +
                "        \"referenceID\": \"123\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"text\": \"Kupon Cashback\",\n" +
                "        \"color\": \"#FFFFFF\",\n" +
                "        \"imageUrl\": \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/icon-coupon.png\",\n" +
                "        \"benefitType\": \"coupon\",\n" +
                "        \"isBigPrize\": false,\n" +
                "        \"dummyCode\": \"asdas\",\n" +
                "        \"referenceID\": \"222\",\n" +
                "        \"isAutoApply\": false,\n" +
                "        \"autoApplyMsg\": \"\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"actionButton\": [\n" +
                "      {\n" +
                "        \"text\": \"Pakai TokoPoints\",\n" +
                "        \"url\": \"https://tokopedia.com/tokopoints\",\n" +
                "        \"applink\": \"tokopedia://webview?url=https%3A%2F%2Fm.tokopedia.com%2Ftokopoints%2Fmobile&title=TokoPoints\",\n" +
                "        \"type\": \"redirect\",\n" +
                "        \"backgroundColor\": \"orange\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}"

        val COUPONS_WITH_POINTS = "{\n" +
                "  \"crackResult\": {\n" +
                "    \"resultStatus\": {\n" +
                "      \"code\": \"200\",\n" +
                "      \"message\": [\n" +
                "        \"success\"\n" +
                "      ],\n" +
                "      \"status\": \"\"\n" +
                "    },\n" +
                "    \"imageUrl\": \"\",\n" +
                "    \"benefitText\": [\n" +
                "      \"Yeay kamu dapat\",\n" +
                "      \"Kupon Cashback & 500 OVO Points\"\n" +
                "    ],\n" +
                "    \"benefits\": [\n" +
                "      {\n" +
                "        \"text\": \"Kupon Cashback\",\n" +
                "        \"color\": \"#FFFFFF\",\n" +
                "        \"imageUrl\": \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/icon-coupon.png\",\n" +
                "        \"benefitType\": \"coupon\",\n" +
                "        \"isBigPrize\": false,\n" +
                "        \"isAutoApply\": true,\n" +
                "        \"autoApplyMsg\": \"Kupon Cashback udah terpasang ya. Ayo beli sekarang!\",\n" +
                "        \"dummyCode\": \"qweqw\",\n" +
                "        \"referenceID\": \"123\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"text\": \"Kupon Cashback\",\n" +
                "        \"color\": \"#FFFFFF\",\n" +
                "        \"imageUrl\": \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/icon-coupon.png\",\n" +
                "        \"benefitType\": \"coupon\",\n" +
                "        \"isBigPrize\": false,\n" +
                "        \"dummyCode\": \"asdas\",\n" +
                "        \"referenceID\": \"222\",\n" +
                "        \"isAutoApply\": false,\n" +
                "        \"autoApplyMsg\": \"\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"text\": \"500 OVO Points\",\n" +
                "        \"color\": \"#FFFFFF\",\n" +
                "        \"imageUrl\": \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/icon-ovo.png\",\n" +
                "        \"benefitType\": \"ovopoints\",\n" +
                "        \"isBigPrize\": false,\n" +
                "        \"dummyCode\": \"\",\n" +
                "        \"referenceID\": \"0\",\n" +
                "        \"isAutoApply\": false,\n" +
                "        \"autoApplyMsg\": \"\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"actionButton\": [\n" +
                "      {\n" +
                "        \"text\": \"Pakai TokoPoints\",\n" +
                "        \"url\": \"https://tokopedia.com/tokopoints\",\n" +
                "        \"applink\": \"tokopedia://webview?url=https%3A%2F%2Fm.tokopedia.com%2Ftokopoints%2Fmobile&title=TokoPoints\",\n" +
                "        \"type\": \"redirect\",\n" +
                "        \"backgroundColor\": \"orange\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}"

        val POINTS_ONLY = "{\n" +
                "  \"crackResult\": {\n" +
                "    \"resultStatus\": {\n" +
                "      \"code\": \"200\",\n" +
                "      \"message\": [\n" +
                "        \"success\"\n" +
                "      ],\n" +
                "      \"status\": \"\"\n" +
                "    },\n" +
                "    \"imageUrl\": \"\",\n" +
                "    \"benefitText\": [\n" +
                "      \"Yeay kamu dapat\",\n" +
                "      \"Kupon Cashback & 500 OVO Points\"\n" +
                "    ],\n" +
                "    \"benefits\": [\n" +
                "      {\n" +
                "        \"text\": \"500 OVO Points\",\n" +
                "        \"color\": \"#FFFFFF\",\n" +
                "        \"imageUrl\": \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/icon-ovo.png\",\n" +
                "        \"benefitType\": \"ovopoints\",\n" +
                "        \"isBigPrize\": false,\n" +
                "        \"dummyCode\": \"\",\n" +
                "        \"referenceID\": \"0\",\n" +
                "        \"isAutoApply\": false,\n" +
                "        \"autoApplyMsg\": \"\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"actionButton\": [\n" +
                "      {\n" +
                "        \"text\": \"Pakai TokoPoints\",\n" +
                "        \"url\": \"https://tokopedia.com/tokopoints\",\n" +
                "        \"applink\": \"tokopedia://webview?url=https%3A%2F%2Fm.tokopedia.com%2Ftokopoints%2Fmobile&title=TokoPoints\",\n" +
                "        \"type\": \"redirect\",\n" +
                "        \"backgroundColor\": \"orange\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}"

        val ERROR = "{\n" +
                "  \"crackResult\": {\n" +
                "    \"resultStatus\": {\n" +
                "      \"code\": \"XXXXX\",\n" +
                "      \"message\": [\n" +
                "        \"Maaf terjadi kendala dalam sistem, silahkan coba beberapa saat lagi\"\n" +
                "      ],\n" +
                "      \"status\": \"Internal server error\"\n" +
                "    },\n" +
                "    \"imageUrl\": \"\",\n" +
                "    \"benefitText\": [],\n" +
                "    \"benefits\": [],\n" +
                "    \"actionButton\": []\n" +
                "  }\n" +
                "}"
    }

    object GamiLuckyHomeResponse {
        val ACTIVE = "{\n" +
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
                "      \"backgroundImgURL\": \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/kejutan-bg.png\",\n" +
                "      \"imageV2URLs\": [\n" +
                "        \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/keejutan-box.png\",\n" +
                "        \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/kejutan-box.gif\"\n" +
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

        val EMPTY = "{\n" +
                "  \"gamiLuckyHome\": {\n" +
                "    \"resultStatus\": {\n" +
                "      \"code\": \"200\",\n" +
                "      \"message\": [\n" +
                "        \"Success\"\n" +
                "      ],\n" +
                "      \"reason\": \"\"\n" +
                "    },\n" +
                "    \"tokensUser\": {\n" +
                "      \"state\": \"empty\",\n" +
                "      \"campaignSlug\": \"\",\n" +
                "      \"title\": \"\",\n" +
                "      \"text\": \"Kamu udah ambil hadiahnya.\\nCoba lagi besok ya!\",\n" +
                "      \"desc\": \"\"\n" +
                "    },\n" +
                "    \"liveFeedChannel\": 1,\n" +
                "    \"tokenAsset\": {\n" +
                "      \"backgroundImgURL\": \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/kejutan-bg.png\",\n" +
                "      \"imageURL\": \"\",\n" +
                "      \"imageV2URLs\": [\n" +
                "        \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/keejutan-box.png\",\n" +
                "        \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/kejutan-box.gif\"\n" +
                "      ]\n" +
                "    },\n" +
                "    \"actionButton\": [],\n" +
                "    \"reminder\": {\n" +
                "      \"text\": \"Besok bisa dapet hadiah lagi lho. Mau diingetin?\",\n" +
                "      \"enableText\": \"Ingatkan Besok\",\n" +
                "      \"disableText\": \"Pengingat Nyala\"\n" +
                "    },\n" +
                "    \"prizeList\": []\n" +
                "  }\n" +
                "}"

        val ERROR = "{\n" +
                "  \"gamiLuckyHome\": {\n" +
                "    \"resultStatus\": {\n" +
                "      \"code\": \"50001\",\n" +
                "      \"message\": [\n" +
                "        \"Maaf terjadi kendala dalam sistem, silahkan coba beberapa saat lagi\"\n" +
                "      ],\n" +
                "      \"reason\": \"Internal server error\"\n" +
                "    }\n" +
                "  }\n" +
                "}"
        val QR_INACTIVE = "{\n" +
                "  \"gamiLuckyHome\": {\n" +
                "    \"resultStatus\": {\n" +
                "      \"code\": \"200\",\n" +
                "      \"message\": [\n" +
                "        \"Success\"\n" +
                "      ],\n" +
                "      \"reason\": \"\"\n" +
                "    },\n" +
                "    \"tokensUser\": {\n" +
                "      \"state\": \"inactive\",\n" +
                "      \"title\": \"\",\n" +
                "      \"text\": \"Kejutan Ramadan Ekstra lagi siapkan hadiah buatmu!\",\n" +
                "      \"desc\": \"Kamu bisa mulai scan QR untuk dapatkan \\n hadiahnya pada <b>6 Mei 2020.</b> \"\n" +
                "    },\n" +
                "    \"tokenAsset\": {\n" +
                "      \"backgroundImgURL\": \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/kejutan-bg.png\",\n" +
                "      \"imageV2URLs\": [\n" +
                "        \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/kejutan-inactive.png\"\n" +
                "      ]\n" +
                "    },\n" +
                "    \"actionButton\": [\n" +
                "      {\n" +
                "        \"text\": \"Pelajari Selengkapnya\",\n" +
                "        \"type\": \"redirect\",\n" +
                "        \"url\": \"https://www.tokopedia.com/pelajari\",\n" +
                "        \"applink\": \"tokopedia://giftbox/pelajari\",\n" +
                "        \"backgroundColor\": \"orange\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}"

        val QR_EXPIRED = "{\n" +
                "  \"gamiLuckyHome\": {\n" +
                "    \"resultStatus\": {\n" +
                "      \"code\": \"200\",\n" +
                "      \"message\": [\n" +
                "        \"Success\"\n" +
                "      ],\n" +
                "      \"reason\": \"\"\n" +
                "    },\n" +
                "    \"tokensUser\": {\n" +
                "      \"state\": \"expired\",\n" +
                "      \"title\": \"\",\n" +
                "      \"text\": \"Terima kasih sudah ikutan serunya scan QR Kejutan Ramadan Ekstra!\",\n" +
                "      \"desc\": \"Walaupun telah berakhir, tapi kamu tetap bisa seseruan dengan belanja di Tokopedia!\"\n" +
                "    },\n" +
                "    \"tokenAsset\": {\n" +
                "      \"backgroundImgURL\": \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/kejutan-bg.png\",\n" +
                "      \"imageV2URLs\": [\n" +
                "        \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/kejutan-expired.png\"\n" +
                "      ]\n" +
                "    },\n" +
                "    \"actionButton\": [\n" +
                "      {\n" +
                "        \"text\": \"Coba Serunya Belanja\",\n" +
                "        \"type\": \"redirect\",\n" +
                "        \"url\": \"https://www.tokopedia.com/\",\n" +
                "        \"applink\": \"tokopedia://homepage\",\n" +
                "        \"backgroundColor\": \"white\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}"
    }
}