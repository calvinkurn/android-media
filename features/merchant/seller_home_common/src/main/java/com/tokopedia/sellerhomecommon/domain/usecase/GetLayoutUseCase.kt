package com.tokopedia.sellerhomecommon.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.mapper.LayoutMapper
import com.tokopedia.sellerhomecommon.domain.model.GetLayoutResponse
import com.tokopedia.sellerhomecommon.presentation.model.BaseWidgetUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 09/06/20
 */

class GetLayoutUseCase(
    gqlRepository: GraphqlRepository,
    mapper: LayoutMapper,
    dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetLayoutResponse, List<BaseWidgetUiModel<*>>>(
    gqlRepository, mapper, dispatchers, GetLayoutResponse::class.java, QUERY, false
) {

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<BaseWidgetUiModel<*>> {
        val layout: GetLayoutResponse = Gson().fromJson(DUMMY, GetLayoutResponse::class.java)
        return mapper.mapRemoteDataToUiData(layout, false)
        val gqlRequest = GraphqlRequest(QUERY, GetLayoutResponse::class.java, params.parameters)
        val gqlResponse: GraphqlResponse =
            graphqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors: List<GraphqlError>? = gqlResponse.getError(GetLayoutResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetLayoutResponse>()
            return mapper.mapRemoteDataToUiData(data, cacheStrategy.type == CacheType.CACHE_ONLY)
        } else {
            throw MessageErrorException(errors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {
        private const val KEY_SHOP_ID = "shopID"
        private const val KEY_PAGE = "page"

        fun getRequestParams(shopId: String, pageName: String): RequestParams {
            return RequestParams.create().apply {
                putLong(KEY_SHOP_ID, shopId.toLongOrZero())
                putString(KEY_PAGE, pageName)
            }
        }

        private val DUMMY = """
            {
              "GetSellerDashboardPageLayout": {
                "widget": [
                  {
                    "ID": 10,
                    "widgetType": "section",
                    "title": "Penting hari ini",
                    "subtitle": "",
                    "comparePeriode": false,
                    "tooltip": {
                      "title": "Penting hari ini",
                      "content": "",
                      "show": true,
                      "list": [
                        {
                          "title": "Pesanan baru",
                          "description": "Jumlah pesanan yang perlu diproses."
                        },
                        {
                          "title": "Siap dikirim",
                          "description": "Jumlah pesanan yang perlu dikirim."
                        },
                        {
                          "title": "Komplain pesanan",
                          "description": "Jumlah komplain dari pembeli yang perlu diselesaikan."
                        },
                        {
                          "title": "Chat belum dibaca",
                          "description": "Jumlah chat dari pembeli yang perlu dibaca."
                        },
                        {
                          "title": "Diskusi belum dibaca",
                          "description": "Jumlah diskusi dari pembeli yang perlu dibaca."
                        },
                        {
                          "title": "Ulasan belum dibalas",
                          "description": "Jumlah ulasan dari pembeli yang belum kamu balas"
                        }
                      ]
                    },
                    "showEmpty": false,
                    "postFilter": [],
                    "url": "",
                    "applink": "",
                    "dataKey": "",
                    "ctaText": "",
                    "maxData": 0,
                    "maxDisplay": 0,
                    "emptyState": {
                      "imageUrl": "",
                      "title": "",
                      "description": "",
                      "ctaText": "",
                      "applink": ""
                    },
                    "searchTableColumnFilter": []
                  },
                  {
                    "ID": 644,
                    "widgetType": "milestone",
                    "title": "Misi berjualan Tokopedia",
                    "subtitle": "",
                    "comparePeriode": false,
                    "tooltip": {
                      "title": "",
                      "content": "Selesaikan semua langkah sebelum 30 hari",
                      "show": true,
                      "list": []
                    },
                    "showEmpty": false,
                    "postFilter": [],
                    "url": "/statistic/overview",
                    "applink": "",
                    "dataKey": "shopQuest",
                    "ctaText": "Lihat Selengkapnya",
                    "maxData": 0,
                    "maxDisplay": 0,
                    "emptyState": {
                      "imageUrl": "",
                      "title": "",
                      "description": "",
                      "ctaText": "",
                      "applink": ""
                    },
                    "searchTableColumnFilter": []
                  },
                  {
                    "ID": 11,
                    "widgetType": "card",
                    "title": "Pesanan baru",
                    "subtitle": "",
                    "comparePeriode": false,
                    "tooltip": {
                      "title": "",
                      "content": "",
                      "show": false,
                      "list": []
                    },
                    "showEmpty": true,
                    "postFilter": [
                      {
                        "name": "test",
                        "value": "test"
                      }
                    ],
                    "url": "https://seller-staging.tokopedia.com/myshop_order?status=new_order",
                    "applink": "tokopedia://seller/new-order",
                    "dataKey": "newOrder",
                    "ctaText": "",
                    "maxData": 0,
                    "maxDisplay": 0,
                    "emptyState": {
                      "imageUrl": "",
                      "title": "",
                      "description": "",
                      "ctaText": "",
                      "applink": ""
                    },
                    "searchTableColumnFilter": []
                  },
                  {
                    "ID": 12,
                    "widgetType": "card",
                    "title": "Siap dikirim",
                    "subtitle": "",
                    "comparePeriode": false,
                    "tooltip": {
                      "title": "",
                      "content": "",
                      "show": false,
                      "list": []
                    },
                    "showEmpty": true,
                    "postFilter": [],
                    "url": "https://seller-staging.tokopedia.com/myshop_order?status=confirm_shipping",
                    "applink": "tokopedia://seller/shipment",
                    "dataKey": "readyToShipOrder",
                    "ctaText": "",
                    "maxData": 0,
                    "maxDisplay": 0,
                    "emptyState": {
                      "imageUrl": "",
                      "title": "",
                      "description": "",
                      "ctaText": "",
                      "applink": ""
                    },
                    "searchTableColumnFilter": []
                  },
                  {
                    "ID": 13,
                    "widgetType": "card",
                    "title": "Komplain pesanan",
                    "subtitle": "",
                    "comparePeriode": false,
                    "tooltip": {
                      "title": "",
                      "content": "",
                      "show": false,
                      "list": []
                    },
                    "showEmpty": true,
                    "postFilter": [],
                    "url": "https://seller-staging.tokopedia.com/resolution-center/inbox/seller",
                    "applink": "",
                    "dataKey": "complaint",
                    "ctaText": "",
                    "maxData": 0,
                    "maxDisplay": 0,
                    "emptyState": {
                      "imageUrl": "",
                      "title": "",
                      "description": "",
                      "ctaText": "",
                      "applink": ""
                    },
                    "searchTableColumnFilter": []
                  },
                  {
                    "ID": 612,
                    "widgetType": "card",
                    "title": "Dibatalkan pembeli",
                    "subtitle": "",
                    "comparePeriode": false,
                    "tooltip": {
                      "title": "",
                      "content": "Test Tooltip",
                      "show": false,
                      "list": []
                    },
                    "showEmpty": false,
                    "postFilter": [],
                    "url": "/myshop_order?status=all_order&brc=1",
                    "applink": "tokopedia://seller/cancellationrequest",
                    "dataKey": "orderBuyerCancellationRequest",
                    "ctaText": "",
                    "maxData": 0,
                    "maxDisplay": 0,
                    "emptyState": {
                      "imageUrl": "",
                      "title": "",
                      "description": "",
                      "ctaText": "",
                      "applink": ""
                    },
                    "searchTableColumnFilter": []
                  },
                  {
                    "ID": 14,
                    "widgetType": "card",
                    "title": "Chat baru",
                    "subtitle": "",
                    "comparePeriode": false,
                    "tooltip": {
                      "title": "",
                      "content": "",
                      "show": false,
                      "list": []
                    },
                    "showEmpty": true,
                    "postFilter": [],
                    "url": "https://seller-staging.tokopedia.com/chat",
                    "applink": "",
                    "dataKey": "unreadChat",
                    "ctaText": "",
                    "maxData": 0,
                    "maxDisplay": 0,
                    "emptyState": {
                      "imageUrl": "",
                      "title": "",
                      "description": "",
                      "ctaText": "",
                      "applink": ""
                    },
                    "searchTableColumnFilter": []
                  },
                  {
                    "ID": 15,
                    "widgetType": "card",
                    "title": "Diskusi belum dibaca",
                    "subtitle": "",
                    "comparePeriode": false,
                    "tooltip": {
                      "title": "",
                      "content": "",
                      "show": true,
                      "list": []
                    },
                    "showEmpty": true,
                    "postFilter": [],
                    "url": "https://seller-staging.tokopedia.com/inbox-talk",
                    "applink": "tokopedia://talk?filter=unread",
                    "dataKey": "discussion",
                    "ctaText": "",
                    "maxData": 0,
                    "maxDisplay": 0,
                    "emptyState": {
                      "imageUrl": "",
                      "title": "",
                      "description": "",
                      "ctaText": "",
                      "applink": ""
                    },
                    "searchTableColumnFilter": []
                  },
                  {
                    "ID": 394,
                    "widgetType": "card",
                    "title": "Ulasan belum dibalas",
                    "subtitle": "",
                    "comparePeriode": false,
                    "tooltip": {
                      "title": "",
                      "content": "",
                      "show": false,
                      "list": []
                    },
                    "showEmpty": true,
                    "postFilter": [],
                    "url": "https://seller-staging.tokopedia.com/review/inbox",
                    "applink": "tokopedia://review?tab=inbox-ulasan",
                    "dataKey": "unrepliedReview",
                    "ctaText": "",
                    "maxData": 0,
                    "maxDisplay": 0,
                    "emptyState": {
                      "imageUrl": "",
                      "title": "",
                      "description": "",
                      "ctaText": "",
                      "applink": ""
                    },
                    "searchTableColumnFilter": []
                  },
                  {
                    "ID": 748,
                    "widgetType": "announcement",
                    "title": "",
                    "subtitle": "",
                    "comparePeriode": false,
                    "tooltip": {
                      "title": "",
                      "content": "",
                      "show": false,
                      "list": []
                    },
                    "showEmpty": false,
                    "postFilter": [],
                    "url": "",
                    "applink": "",
                    "dataKey": "anniversaryAnnoucement",
                    "ctaText": "",
                    "maxData": 0,
                    "maxDisplay": 0,
                    "emptyState": {
                      "imageUrl": "",
                      "title": "",
                      "description": "",
                      "ctaText": "",
                      "applink": ""
                    },
                    "searchTableColumnFilter": []
                  },
                  {
                    "ID": 16,
                    "widgetType": "section",
                    "title": "Ringkasan penjualan",
                    "subtitle": "Update terakhir: {NOW_DD_MMMM_YYYY_hh:mm_WIB}",
                    "comparePeriode": false,
                    "tooltip": {
                      "title": "Ringkasan penjualan",
                      "content": "",
                      "show": true,
                      "list": [
                        {
                          "title": "Pendapatan bersih baru",
                          "description": "Didapat dari pesanan yang baru masuk, di luar ongkos kirim"
                        },
                        {
                          "title": "Produk dilihat",
                          "description": "Jumlah kunjungan ke halaman produkmu"
                        },
                        {
                          "title": "Produk terjual",
                          "description": "Jumlah produk terjual di tokomu"
                        }
                      ]
                    },
                    "showEmpty": false,
                    "postFilter": [],
                    "url": "",
                    "applink": "",
                    "dataKey": "",
                    "ctaText": "",
                    "maxData": 0,
                    "maxDisplay": 0,
                    "emptyState": {
                      "imageUrl": "",
                      "title": "",
                      "description": "",
                      "ctaText": "",
                      "applink": ""
                    },
                    "searchTableColumnFilter": []
                  },
                  {
                    "ID": 591,
                    "widgetType": "multiTrendline",
                    "title": "Statistik tokomu",
                    "subtitle": "",
                    "comparePeriode": false,
                    "tooltip": {
                      "title": "",
                      "content": "Ini statistik tokomu today!",
                      "show": false,
                      "list": []
                    },
                    "showEmpty": true,
                    "postFilter": [],
                    "url": "/statistic/overview",
                    "applink": "tokopedia://gold-merchant-statistic-dashboard",
                    "dataKey": "productActivity",
                    "ctaText": "Lihat Selengkapnya",
                    "maxData": 0,
                    "maxDisplay": 0,
                    "emptyState": {
                      "imageUrl": "",
                      "title": "Belum ada data di hari ini",
                      "description": "Statistik penjualan tokomu akan ditampilkan disini jika produkmu sudah terjual",
                      "ctaText": "Yuk, Promosikan Produkmu!",
                      "applink": "sellerapp://topads"
                    },
                    "searchTableColumnFilter": []
                  },
                  {
                    "ID": 651,
                    "widgetType": "recommendation",
                    "title": "Performa Toko",
                    "subtitle": "",
                    "comparePeriode": false,
                    "tooltip": {
                      "title": "Performa Toko",
                      "content": "Rangkuman performa toko serta tips untuk meningkatkannya",
                      "show": true,
                      "list": []
                    },
                    "showEmpty": false,
                    "postFilter": [],
                    "url": "/statistic/overview",
                    "applink": "tokopedia-android-internal://marketplace/shop/performance",
                    "dataKey": "shopScoreLevelDetail",
                    "ctaText": "Lihat Selengkapnya",
                    "maxData": 0,
                    "maxDisplay": 0,
                    "emptyState": {
                      "imageUrl": "",
                      "title": "",
                      "description": "",
                      "ctaText": "",
                      "applink": ""
                    },
                    "searchTableColumnFilter": []
                  },
                  {
                    "ID": 722,
                    "widgetType": "multiTrendline",
                    "title": "Performa iklan minggu ini",
                    "subtitle": "",
                    "comparePeriode": true,
                    "tooltip": {
                      "title": "",
                      "content": "",
                      "show": false,
                      "list": []
                    },
                    "showEmpty": true,
                    "postFilter": [],
                    "url": "https://ta-staging.tokopedia.com/v2/manage/group/product",
                    "applink": "tokopedia://topads/dashboard",
                    "dataKey": "multiTrendlineTopadsSummary",
                    "ctaText": "Lihat Selengkapnya",
                    "maxData": 0,
                    "maxDisplay": 0,
                    "emptyState": {
                      "imageUrl": "",
                      "title": "Belum ada data iklan baru",
                      "description": "Iklanmu sudah tidak aktif, nih. Mulai beriklan lagi yuk!",
                      "ctaText": "Cek dashboard TopAds",
                      "applink": "tokopedia://topads/dashboard"
                    },
                    "searchTableColumnFilter": []
                  },
                  {
                    "ID": 618,
                    "widgetType": "searchTable",
                    "title": "Produk terlaris di tokomu",
                    "subtitle": "",
                    "comparePeriode": false,
                    "tooltip": {
                      "title": "Produk yang laris terjual di tokomu kemarin",
                      "content": "Produk yang laris terjual di tokomu kemarin",
                      "show": true,
                      "list": []
                    },
                    "showEmpty": true,
                    "postFilter": [],
                    "url": "",
                    "applink": "",
                    "dataKey": "topProduct",
                    "ctaText": "",
                    "maxData": 5,
                    "maxDisplay": 5,
                    "emptyState": {
                      "imageUrl": "https://ecs7.tokopedia.net/seller-dashboard/stock_product.png",
                      "title": "Belum ada produk yang terjual, nih",
                      "description": "Yuk, buat produk di tokomu makin laris dengan promosi lewat TopAds!",
                      "ctaText": "Promosikan Produkmu",
                      "applink": "sellerapp://topads"
                    },
                    "searchTableColumnFilter": []
                  },
                  {
                    "ID": 638,
                    "widgetType": "searchTable",
                    "title": "Tambahkan stok produkmu",
                    "subtitle": "",
                    "comparePeriode": false,
                    "tooltip": {
                      "title": "",
                      "content": "",
                      "show": false,
                      "list": []
                    },
                    "showEmpty": false,
                    "postFilter": [],
                    "url": "/manage-product?isEmptyStockOnly=true",
                    "applink": "tokopedia://seller/product/manage?filter=isEmptyStockOnly",
                    "dataKey": "lowStock",
                    "ctaText": "Lihat Selengkapnya",
                    "maxData": 6,
                    "maxDisplay": 2,
                    "emptyState": {
                      "imageUrl": "",
                      "title": "",
                      "description": "",
                      "ctaText": "",
                      "applink": ""
                    },
                    "searchTableColumnFilter": []
                  },
                  {
                    "ID": 595,
                    "widgetType": "section",
                    "title": "Keperluan untuk jualanmu",
                    "subtitle": "Cek semuanya untuk tingkatkan performa toko",
                    "comparePeriode": false,
                    "tooltip": {
                      "title": "",
                      "content": "",
                      "show": false,
                      "list": []
                    },
                    "showEmpty": false,
                    "postFilter": [],
                    "url": "",
                    "applink": "",
                    "dataKey": "",
                    "ctaText": "",
                    "maxData": 0,
                    "maxDisplay": 0,
                    "emptyState": {
                      "imageUrl": "",
                      "title": "",
                      "description": "",
                      "ctaText": "",
                      "applink": ""
                    },
                    "searchTableColumnFilter": []
                  },
                  {
                    "ID": 24,
                    "widgetType": "carouselImage",
                    "title": "Pengumuman",
                    "subtitle": "",
                    "comparePeriode": false,
                    "tooltip": {
                      "title": "",
                      "content": "",
                      "show": false,
                      "list": []
                    },
                    "showEmpty": true,
                    "postFilter": [],
                    "url": "",
                    "applink": "",
                    "dataKey": "home",
                    "ctaText": "",
                    "maxData": 0,
                    "maxDisplay": 0,
                    "emptyState": {
                      "imageUrl": "",
                      "title": "",
                      "description": "",
                      "ctaText": "",
                      "applink": ""
                    },
                    "searchTableColumnFilter": []
                  },
                  {
                    "ID": 733,
                    "widgetType": "post",
                    "title": "Rekomendasi",
                    "subtitle": "",
                    "comparePeriode": false,
                    "tooltip": {
                      "title": "",
                      "content": "",
                      "show": false,
                      "list": []
                    },
                    "showEmpty": false,
                    "postFilter": [],
                    "url": "/home",
                    "applink": "tokopedia://review-reminder",
                    "dataKey": "shopAdvisor",
                    "ctaText": "Lihat Selengkapnya",
                    "maxData": 6,
                    "maxDisplay": 3,
                    "emptyState": {
                      "imageUrl": "",
                      "title": "",
                      "description": "",
                      "ctaText": "",
                      "applink": ""
                    },
                    "searchTableColumnFilter": []
                  },
                  {
                    "ID": 539,
                    "widgetType": "post",
                    "title": "Bacaan terkini",
                    "subtitle": "",
                    "comparePeriode": false,
                    "tooltip": {
                      "title": "Informasi",
                      "content": "Informasi ini hanya khusus untukmu",
                      "show": true,
                      "list": []
                    },
                    "showEmpty": true,
                    "postFilter": [
                      {
                        "name": "Artikel",
                        "value": "article"
                      },
                      {
                        "name": "Info",
                        "value": "info"
                      },
                      {
                        "name": "Aktivitas",
                        "value": "activity"
                      }
                    ],
                    "url": "",
                    "applink": "tokopedia://sellerinfo",
                    "dataKey": "sellerInfo",
                    "ctaText": "Lihat selengkapnya",
                    "maxData": 3,
                    "maxDisplay": 3,
                    "emptyState": {
                      "imageUrl": "https://ecs7.tokopedia.net/seller-dashboard/seller_Info.png",
                      "title": "Belum ada informasi untukmu",
                      "description": "Tenang, kamu tetap bisa ikuti semua update mengenai Seller di Pusat Edukasi Seller, ya.",
                      "ctaText": "Ke Pusat Edukasi Seller",
                      "applink": "tokopedia://webview?url=https%3A%2F%2Fseller.tokopedia.com%2Fedu"
                    },
                    "searchTableColumnFilter": []
                  }
                ]
              }
            }
        """.trimIndent()

        private val QUERY = """
            query GetSellerDashboardLayout(${'$'}shopID: Int!, ${'$'}page: String!) {
              GetSellerDashboardPageLayout(shopID: ${'$'}shopID, page: ${'$'}page) {
                widget {
                  ID
                  widgetType
                  title
                  subtitle
                  comparePeriode
                  tooltip {
                    title
                    content
                    show
                    list {
                      title
                      description
                    }
                  }
                  showEmpty
                  postFilter {
                    name
                    value
                  }
                  url
                  applink
                  dataKey
                  ctaText
                  maxData
                  maxDisplay
                  emptyState {
                    imageUrl
                    title
                    description
                    ctaText
                    applink
                  }
                  searchTableColumnFilter{
                    name
                    value
                  }
                }
              }
            }
        """.trimIndent()
    }
}