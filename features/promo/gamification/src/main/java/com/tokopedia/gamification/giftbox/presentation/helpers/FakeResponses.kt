package com.tokopedia.gamification.giftbox.presentation.helpers

object FakeResponses {

    object GamiCrackResponse {
        val COUPONS_ONLY = "{\n" +
                "  \"   gamiCrack\": {\n" +
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
                "        \"referenceID\": \"2463\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"text\": \"Kupon Cashback\",\n" +
                "        \"color\": \"#FFFFFF\",\n" +
                "        \"imageUrl\": \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/icon-coupon.png\",\n" +
                "        \"benefitType\": \"coupon\",\n" +
                "        \"isBigPrize\": false,\n" +
                "        \"dummyCode\": \"asdas\",\n" +
                "        \"referenceID\": \"2463\",\n" +
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
                "  \"gamiCrack\": {\n" +
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
                "        \"imageURL\": \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/icon-coupon.png\",\n" +
                "        \"benefitType\": \"coupon\",\n" +
                "        \"isBigPrize\": false,\n" +
                "        \"isAutoApply\": true,\n" +
                "        \"autoApplyMsg\": \"Kupon Cashback udah terpasang ya. Ayo beli sekarang!\",\n" +
                "        \"dummyCode\": \"qweqw\",\n" +
                "        \"referenceID\": \"2463\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"text\": \"Kupon Cashback\",\n" +
                "        \"color\": \"#FFFFFF\",\n" +
                "        \"imageURL\": \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/icon-coupon.png\",\n" +
                "        \"benefitType\": \"coupon\",\n" +
                "        \"isBigPrize\": false,\n" +
                "        \"dummyCode\": \"asdas\",\n" +
                "        \"referenceID\": \"2463\",\n" +
                "        \"isAutoApply\": false,\n" +
                "        \"autoApplyMsg\": \"\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"text\": \"500 OVO Points\",\n" +
                "        \"color\": \"#FFFFFF\",\n" +
                "        \"imageURL\": \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/icon-ovo.png\",\n" +
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
                "  \"gamiCrack\": {\n" +
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
                "        \"imageURL\": \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/icon-ovo.png\",\n" +
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
                "  \"gamiCrack\": {\n" +
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
                "        \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/kejutan-box3x.png\",\n" +
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
                "        \"https://ecs7.tokopedia.net/img/blog/promo/2020/03/kejutan-box3x.png\",\n" +
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

    object RemindMeCheckResponse {
        val SUCCESS = "{\"gameRemindMeCheck\": {\n" +
                "      \"resultStatus\": {\n" +
                "        \"code\": \"200\",\n" +
                "        \"message\": [\n" +
                "          \"Success\"\n" +
                "        ],\n" +
                "        \"reason\": \"OK\"\n" +
                "      },\n" +
                "      \"isRemindMe\": true\n" +
                "    }\n" +
                "  }"

        val ERROR = "{\"gameRemindMeCheck\":{\"resultStatus\":{\"code\":\"403\",\"message\":[\"User ID not found\"],\"reason\":\"User ID not found\"},\"isRemindMe\":false}}"

    }

    object TapTapHome{
        val RESPONSE = "{\n" +
                "  \"gamiTapEggHome\": {\n" +
                "    \"backButton\": {\n" +
                "      \"isShow\": true,\n" +
                "      \"imageURL\": \"https://ecs7.tokopedia.net/img/blog/promo/2019/03/confirm.png\",\n" +
                "      \"title\": \"Selesaikan Permainan?\",\n" +
                "      \"text\": \"Sisa telur kamu akan hangus apabila kamu menyelesaikan permainan.\",\n" +
                "      \"yesText\": \"Ya, Selesaikan\",\n" +
                "      \"cancelText\": \"Lanjut Main\"\n" +
                "    },\n" +
                "    \"tokensUser\": {\n" +
                "      \"state\": \"crackunlimited\",\n" +
                "      \"tokenUserIDstr\": \"0\",\n" +
                "      \"campaignID\": 2164,\n" +
                "      \"title\": \"Pecahkan Lucky Egg sebanyak-banyaknya\"\n" +
                "    },\n" +
                "    \"timeRemaining\": {\n" +
                "      \"isShow\": true,\n" +
                "      \"unixFetch\": 1585025376,\n" +
                "      \"seconds\": 30,\n" +
                "      \"backgroundColor\": \"#60562B\",\n" +
                "      \"fontColor\": \"#EEEEAE\",\n" +
                "      \"borderColor\": \"#C5C593\"\n" +
                "    },\n" +
                "    \"actionButton\": [],\n" +
                "    \"tokenAsset\": {\n" +
                "      \"backgroundImgURL\": \"https://ecs7.tokopedia.net/assets-tokopoints/prod/images/2019/01/Desktop-BG-main-special%403x.png\",\n" +
                "      \"seamlessImgURL\": \"https://ecs7.tokopedia.net/assets-tokopoints/prod/images/2019/01/Desktop-BG-seamless-special%403x.png\",\n" +
                "      \"imageURL\": \"\",\n" +
                "      \"glowShadowImgURL\": \"https://ecs7.tokopedia.net/img/blog/promo/2019/03/shadow.png\",\n" +
                "      \"glowImgURL\": \"https://ecs7.tokopedia.net/img/blog/promo/2019/03/glow.png\",\n" +
                "      \"imageV2URLs\": [\n" +
                "        \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/sp1.png\",\n" +
                "        \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/sp2.png\",\n" +
                "        \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/sp3.png\",\n" +
                "        \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/sp4.png\",\n" +
                "        \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/sp5.png\",\n" +
                "        \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/sp6.png\",\n" +
                "        \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/sp7.png\",\n" +
                "        \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/sp8.png\",\n" +
                "        \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/sp9.png\",\n" +
                "        \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/sp10.png\",\n" +
                "        \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/spkiri.png\",\n" +
                "        \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/spkanan.png\"\n" +
                "      ]\n" +
                "    },\n" +
                "    \"rewardButton\": [\n" +
                "      {\n" +
                "        \"text\": \"Cek Hadiah\",\n" +
                "        \"type\": \"redirect\",\n" +
                "        \"url\": \"https://www.tokopedia.com/tokopoints\",\n" +
                "        \"applink\": \"tokopedia://tokopoints\",\n" +
                "        \"isDisable\": false,\n" +
                "        \"backgroundColor\": \"green\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"text\": \"Keluar\",\n" +
                "        \"type\": \"redirect\",\n" +
                "        \"url\": \"https://www.tokopedia.com\",\n" +
                "        \"applink\": \"tokopedia://home\",\n" +
                "        \"isDisable\": false,\n" +
                "        \"backgroundColor\": \"outline\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}"
    }

    object TAP_TAP_CRACK {
        val REWARD_POINTS = "{\n" +
                "  \"crackResult\": {\n" +
                "    \"resultStatus\": {\n" +
                "      \"code\": \"200\",\n" +
                "      \"message\": [\n" +
                "        \"success\"\n" +
                "      ],\n" +
                "      \"status\": \"\"\n" +
                "    },\n" +
                "    \"imageUrl\": \"https://ecs7.tokopedia.net/assets/images/gamification/benefit/loyalty.png\",\n" +
                "    \"benefitType\": \"loyalty_reward_point\",\n" +
                "    \"benefits\": [\n" +
                "      {\n" +
                "        \"text\": \"+10 Points\",\n" +
                "        \"color\": \"#FFDC00\",\n" +
                "        \"size\": \"large\",\n" +
                "        \"benefitType\": \"reward_point\",\n" +
                "        \"templateText\": \"\",\n" +
                "        \"animationType\": \"\",\n" +
                "        \"valueBefore\": 0,\n" +
                "        \"valueAfter\": 10,\n" +
                "        \"tierInformation\": \"Classic 1\",\n" +
                "        \"multiplier\": \"\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"ctaButton\": {\n" +
                "      \"title\": \"Cek Tokopoints Anda\",\n" +
                "      \"url\": \"https://staging.tokopedia.com/tokopoints\",\n" +
                "      \"applink\": \"tokopedia://webview?url=https%3A%2F%2Fm.tokopedia.com%2Ftokopoints%2Fmobile&title=TokoPoints\",\n" +
                "      \"type\": \"redirect\"\n" +
                "    },\n" +
                "    \"returnButton\": {\n" +
                "      \"title\": \"Pecahkan Lucky Egg Lain\",\n" +
                "      \"url\": \"\",\n" +
                "      \"applink\": \"\",\n" +
                "      \"type\": \"dismiss\"\n" +
                "    }\n" +
                "  }\n" +
                "}"
    }
}