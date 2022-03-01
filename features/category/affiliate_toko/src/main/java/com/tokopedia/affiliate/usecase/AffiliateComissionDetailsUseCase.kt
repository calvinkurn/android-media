package com.tokopedia.affiliate.usecase

import com.google.gson.Gson
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Commission
import com.tokopedia.affiliate.model.response.AffiliateCommissionDetailsData
import com.tokopedia.affiliate.model.response.AffiliateTrafficCommissionCardDetails
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateCommissionDetailsUseCase @Inject constructor(
    private val repository: AffiliateRepository
) {

    private fun createRequestParams(transactionID: String): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_TRANSACTION_ID] = transactionID
        return request
    }

    suspend fun affiliateCommissionDetails(transactionID: String): AffiliateCommissionDetailsData {
//        return repository.getGQLData(
//            GQL_Affiliate_Commission,
//            AffiliateCommissionDetailsData::class.java,
//            createRequestParams(transactionID)
//        )
        return Gson().fromJson("{\n" +
                "    \"getAffiliateCommissionDetail\": {\n" +
                "      \"Data\": {\n" +
                "        \"Status\": 1,\n" +
                "        \"Error\": {\n" +
                "          \"ErrorType\": 0,\n" +
                "          \"Message\": \"\",\n" +
                "          \"CtaText\": \"\",\n" +
                "          \"CtaLink\": {\n" +
                "            \"DesktopURL\": \"\",\n" +
                "            \"MobileURL\": \"\",\n" +
                "            \"AndroidURL\": \"\",\n" +
                "            \"IosURL\": \"\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"CardDetail\": {\n" +
                "        },\n" +
                "        \"DetailTitle\": \"Detail Komisi Penjualan\",\n" +
                "        \"Detail\": [\n" +
                "          {\n" +
                "            \"DetailType\": \"text\",\n" +
                "            \"TextSize\": 14,\n" +
                "            \"TextType\": \"regular\",\n" +
                "            \"TextStyle\": \"body3\",\n" +
                "            \"DetailTitle\": \"Komisi Diterima\",\n" +
                "            \"DetailTooltip\": \"\",\n" +
                "            \"DetailDescription\": \"21 Dec 2021, 12:03 WIB\",\n" +
                "            \"AdvancedTooltip\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"DetailType\": \"divider\",\n" +
                "            \"TextSize\": 0,\n" +
                "            \"TextType\": \"\",\n" +
                "            \"TextStyle\": \"\",\n" +
                "            \"DetailTitle\": \"\",\n" +
                "            \"DetailTooltip\": \"\",\n" +
                "            \"DetailDescription\": \"\",\n" +
                "            \"AdvancedTooltip\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"DetailType\": \"text\",\n" +
                "            \"TextSize\": 14,\n" +
                "            \"TextType\": \"regular\",\n" +
                "            \"TextStyle\": \"body3\",\n" +
                "            \"DetailTitle\": \"Total Kunjungan (20 Dec 2021)\",\n" +
                "            \"DetailTooltip\": \"\",\n" +
                "            \"DetailDescription\": \"1.025\",\n" +
                "            \"AdvancedTooltip\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"DetailType\": \"text\",\n" +
                "            \"TextSize\": 14,\n" +
                "            \"TextType\": \"regular\",\n" +
                "            \"TextStyle\": \"body3\",\n" +
                "            \"DetailTitle\": \"Kunjungan Terpilih\",\n" +
                "            \"DetailTooltip\": \"\",\n" +
                "            \"DetailDescription\": \"1.000\",\n" +
                "            \"AdvancedTooltip\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"DetailType\": \"text\",\n" +
                "            \"TextSize\": 14,\n" +
                "            \"TextType\": \"regular\",\n" +
                "            \"TextStyle\": \"body3\",\n" +
                "            \"DetailTitle\": \"Komisi\",\n" +
                "            \"DetailTooltip\": \"\",\n" +
                "            \"DetailDescription\": \"Rp50\",\n" +
                "            \"AdvancedTooltip\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"DetailType\": \"divider\",\n" +
                "            \"TextSize\": 0,\n" +
                "            \"TextType\": \"\",\n" +
                "            \"TextStyle\": \"\",\n" +
                "            \"DetailTitle\": \"\",\n" +
                "            \"DetailTooltip\": \"\",\n" +
                "            \"DetailDescription\": \"\",\n" +
                "            \"AdvancedTooltip\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"DetailType\": \"text\",\n" +
                "            \"TextSize\": 14,\n" +
                "            \"TextType\": \"bold\",\n" +
                "            \"TextStyle\": \"heading6\",\n" +
                "            \"DetailTitle\": \"Total Diterima\",\n" +
                "            \"DetailTooltip\": \"\",\n" +
                "            \"DetailDescription\": \"Rp50.000\",\n" +
                "            \"AdvancedTooltip\": [ {\n" +
                "                \"TooltipType\": \"text\",\n" +
                "                \"TextType\": \"bold\",\n" +
                "                \"TextStyle\": \"heading6\",\n" +
                "                \"TextSize\": 16,\n" +
                "                \"TooltipText\": \"Total Kunjungan\"\n" +
                "              },\n" +
                "              {\n" +
                "                \"TooltipType\": \"text\",\n" +
                "                \"TextType\": \"regular\",\n" +
                "                \"TextStyle\": \"body3\",\n" +
                "                \"TextSize\": 14,\n" +
                "                \"TooltipText\": \"Semua total kunjungan yang dipilih berdasarkan aturan yang berlaku di Tokopedia dan juga dieliminasi dari pelanggaran & bot.\"\n" +
                "              },\n" +
                "              {\n" +
                "                \"TooltipType\": \"divider\",\n" +
                "                \"TextType\": \"\",\n" +
                "                \"TextStyle\": \"\",\n" +
                "                \"TextSize\": 0,\n" +
                "                \"TooltipText\": \"\"\n" +
                "              },\n" +
                "              {\n" +
                "                \"TooltipType\": \"text\",\n" +
                "                \"TextType\": \"bold\",\n" +
                "                \"TextStyle\": \"heading6\",\n" +
                "                \"TextSize\": 16,\n" +
                "                \"TooltipText\": \"Komisi\"\n" +
                "              },\n" +
                "              {\n" +
                "                \"TooltipType\": \"text\",\n" +
                "                \"TextType\": \"regular\",\n" +
                "                \"TextStyle\": \"body3\",\n" +
                "                \"TextSize\": 14,\n" +
                "                \"TooltipText\": \"Nilai komisi kunjungan produk yang ditentukan oleh Tokopedia.\"\n" +
                "              },\n" +
                "              {\n" +
                "                \"TooltipType\": \"divider\",\n" +
                "                \"TextType\": \"\",\n" +
                "                \"TextStyle\": \"\",\n" +
                "                \"TextSize\": 0,\n" +
                "                \"TooltipText\": \"\"\n" +
                "              },\n" +
                "              {\n" +
                "                \"TooltipType\": \"text\",\n" +
                "                \"TextType\": \"bold\",\n" +
                "                \"TextStyle\": \"heading6\",\n" +
                "                \"TextSize\": 16,\n" +
                "                \"TooltipText\": \"Pajak Penghasilan\"\n" +
                "              },\n" +
                "              {\n" +
                "                \"TooltipType\": \"text\",\n" +
                "                \"TextType\": \"regular\",\n" +
                "                \"TextStyle\": \"body3\",\n" +
                "                \"TextSize\": 14,\n" +
                "                \"TooltipText\": \"Pajak penghasilan yang wajib dibayarkan sebesar 6% dihitung dari pendapatan bersih komisi.\"\n" +
                "              }]\n" +
                "          }\n" +
                "        ],\n" +
                "        \"CommissionType\": \"TRAFFIC\",\n" +
                "        \"PageType\": \"PDP\",\n" +
                "        \"Notes\": \"1.025 total kunjungan\",\n" +
                "        \"AdditionalQueryKey\": \"\",\n" +
                "        \"CreatedAt\": \"2022-01-06T16:26:05Z\",\n" +
                "        \"CreatedAtFormatted\": \"06 Jan 2022, 23:26 WIB\",\n" +
                "        \"UpdatedAt\": \"2022-01-06T16:26:05Z\",\n" +
                "        \"UpdatedAtFormatted\": \"06 Jan 2022, 23:26 WIB\"\n" +
                "      }\n" +
                "    }\n" +
                "  }",AffiliateCommissionDetailsData::class.java)

    }

    fun affiliateTrafficCardDetails(transactionDate: String, lastItem: String?, type: String?): AffiliateTrafficCommissionCardDetails? {
        return Gson().fromJson("{\n" +
                "   \"getAffiliateTrafficCommissionDetailCards\":{\n" +
                "      \"Data\":{\n" +
                "         \"LastID\":\"129312\",\n" +
                "         \"HasNext\":true,\n" +
                "         \"Status\":1,\n" +
                "         \"Error\":{\n" +
                "            \"ErrorType\":0,\n" +
                "            \"Message\":\"\",\n" +
                "            \"CtaText\":\"\",\n" +
                "            \"CtaLink\":{\n" +
                "               \"DesktopURL\":\"\",\n" +
                "               \"MobileURL\":\"\",\n" +
                "               \"AndroidURL\":\"\",\n" +
                "               \"IosURL\":\"\"\n" +
                "            }\n" +
                "         },\n" +
                "         \"TrafficCommissionCardDetail\":[\n" +
                "            {\n" +
                "               \"CardTitle\":\"Things you dream at night\",\n" +
                "               \"CardDescription\":\"500 Total Kunjungan\",\n" +
                "               \"Image\":{\n" +
                "                  \"DesktopURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "                  \"MobileURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "                  \"AndroidURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "                  \"IosURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\"\n" +
                "               }\n" +
                "            },\n" +
                "            {\n" +
                "               \"CardTitle\":\"Things you dream at night\",\n" +
                "               \"CardDescription\":\"500 Total Kunjungan\",\n" +
                "               \"Image\":{\n" +
                "                  \"DesktopURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "                  \"MobileURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "                  \"AndroidURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "                  \"IosURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\"\n" +
                "               }\n" +
                "            },\n" +
                "            {\n" +
                "               \"CardTitle\":\"Things you dream at night\",\n" +
                "               \"CardDescription\":\"500 Total Kunjungan\",\n" +
                "               \"Image\":{\n" +
                "                  \"DesktopURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "                  \"MobileURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "                  \"AndroidURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "                  \"IosURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\"\n" +
                "               }\n" +
                "            },\n" +
                "            {\n" +
                "               \"CardTitle\":\"Things you dream at night\",\n" +
                "               \"CardDescription\":\"500 Total Kunjungan\",\n" +
                "               \"Image\":{\n" +
                "                  \"DesktopURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "                  \"MobileURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "                  \"AndroidURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "                  \"IosURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\"\n" +
                "               }\n" +
                "            },\n" +
                "            {\n" +
                "               \"CardTitle\":\"Things you dream at night\",\n" +
                "               \"CardDescription\":\"500 Total Kunjungan\",\n" +
                "               \"Image\":{\n" +
                "                  \"DesktopURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "                  \"MobileURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "                  \"AndroidURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "                  \"IosURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\"\n" +
                "               }\n" +
                "            },\n" +
                "            {\n" +
                "               \"CardTitle\":\"Things you dream at night\",\n" +
                "               \"CardDescription\":\"500 Total Kunjungan\",\n" +
                "               \"Image\":{\n" +
                "                  \"DesktopURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "                  \"MobileURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "                  \"AndroidURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "                  \"IosURL\":\"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\"\n" +
                "               }\n" +
                "            }\n" +
                "         ]\n" +
                "      }\n" +
                "   }\n" +
                "}",AffiliateTrafficCommissionCardDetails::class.java)
    }


    companion object {
        private const val PARAM_TRANSACTION_ID = "transactionID"
    }
}