package com.tokopedia.logisticcart.shipping.usecase

import com.google.gson.Gson
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesGqlResponse
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationConverter
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import rx.Observable
import javax.inject.Inject

class GetRatesUseCase @Inject constructor(
    private val converter: ShippingDurationConverter,
    private val scheduler: SchedulerProvider
) {

    private var gql: GraphqlUseCase? = null

    fun execute(param: RatesParam): Observable<ShippingRecommendationData> {
        val query = ratesQuery()
        val gqlRequest = GraphqlRequest(
            query,
            RatesGqlResponse::class.java,
            mapOf(
                "param" to param.toMap(),
                "metadata" to param.toMetadata()
            )
        )
        // Need to init usecase here to prevent request cleared since this usecase will be called multiple time in a very tight interval of each call.
        // Will consider this as tech debt until find a proper solution
        val gql = GraphqlUseCase()
        this.gql = gql
        gql.clearRequest()
        gql.addRequest(gqlRequest)
        return Observable.just(
            """
{
                "ratesV3": {
                "ratesv3": {
                "id": ":2270:2257:14",
                "rates_id": "1680484116532637391",
                "type": "rates",
                "error": {
                "error_id": "",
                "error_message": ""
            },
                "services": [
                {
                    "service_name": "Instan",
                    "service_id": 1000,
                    "service_order": 0,
                    "status": 200,
                    "is_promo": 0,
                    "ui_rates_hidden": false,
                    "selected_shipper_product_id": 0,
                    "range_price": {
                    "min_price": 51500,
                    "max_price": 51500
                },
                    "etd": {
                    "min_etd": 0,
                    "max_etd": 0
                },
                    "texts": {
                    "text_range_price": "Rp51.500",
                    "text_etd": "Maks. 3 jam",
                    "text_notes": "",
                    "text_service_desc": "",
                    "text_eta_summarize": "Estimasi tiba hari ini",
                    "error_code": 0
                },
                    "features": {
                    "dynamic_price": {
                    "text_label": ""
                }
                },
                    "order_priority": {
                    "is_now": false,
                    "inactive_message": "",
                    "price": 0,
                    "formatted_price": "",
                    "static_messages": {
                    "duration_message": "",
                    "checkbox_message": "",
                    "warningbox_message": "",
                    "fee_message": "",
                    "pdp_message": ""
                }
                },
                    "error": {
                    "error_id": "",
                    "error_message": ""
                },
                    "cod": {
                    "is_cod": 0,
                    "cod_text": ""
                },
                    "mvc": {
                    "is_mvc": 0,
                    "mvc_title": "",
                    "mvc_logo": "",
                    "mvc_error_message": ""
                },
                    "products": [
                    {
                        "shipper_name": "GoSend Bike",
                        "shipper_id": 10,
                        "shipper_product_id": 28,
                        "shipper_product_name": "Instant Courier",
                        "shipper_product_desc": "Dengan kurir instan, kamu bisa pakai GoSend Car dan GoSend Bike (pengiriman maks. 3 jam \u0026 berat maks. 150kg). Setelah aktif, GoSend Car dan GoSend Bike otomatis masuk pilihan kurir, tidak ditampilkan di menu ini.",
                        "is_show_map": 1,
                        "shipper_weight": 1000,
                        "status": 200,
                        "recommend": true,
                        "checksum": "IwJNu94mNBNO5YF9dLFLRPhmWBs%3D",
                        "ut": "1680484116",
                        "promo_code": "",
                        "ui_rates_hidden": false,
                        "price": {
                        "price": 51500,
                        "formatted_price": "Rp51.500"
                    },
                        "etd": {
                        "min_etd": 10800,
                        "max_etd": 10800
                    },
                        "texts": {
                        "text_price": "Rp51.500",
                        "text_etd": "Maks. 3 jam"
                    },
                        "insurance": {
                        "insurance_price": 1900,
                        "insurance_type": 2,
                        "insurance_type_info": "Optional insurance",
                        "insurance_used_type": 2,
                        "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, \u003cb\u003egratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.\u003c/b\u003e Berlaku harga asuransi 0.6% dari harga invoice.",
                        "insurance_used_default": 2
                    },
                        "error": {
                        "error_id": "",
                        "error_message": ""
                    },
                        "cod": {
                        "is_cod_available": 0,
                        "cod_price": 0,
                        "cod_text": "",
                        "formatted_price": "",
                        "tnc_text": "",
                        "tnc_link": ""
                    },
                        "features": {
                        "ontime_delivery_guarantee": {
                        "available": true,
                        "value": 10800,
                        "text_label": "Jaminan Tepat Waktu",
                        "text_detail": "Ongkir kembali jika barang tiba lebih dari 3 Jam \u003cb\u003esejak diserahkan ke kurir\u003c/b\u003e \u003ca href\u003d\"https://gw.tokopedia.com/rates/otdg_info\"\u003eS\u0026K Berlaku\u003c/a\u003e",
                        "url_detail": "https://gw.tokopedia.com/rates/otdg_info",
                        "icon_url": "https://images.tokopedia.net/img/ongkirapp/otdg-icon-v2.png",
                        "url_text": "S\u0026K Berlaku"
                    },
                        "mvc": {
                        "is_mvc": 0,
                        "mvc_logo": "",
                        "mvc_error_message": ""
                    },
                        "dynamic_price": {
                        "text_label": ""
                    }
                    },
                        "eta": {
                        "text_eta": "Estimasi tiba hari ini",
                        "error_code": 0
                    }
                    }
                    ]
                },
                {
                    "service_name": "Same Day",
                    "service_id": 1002,
                    "service_order": 0,
                    "status": 200,
                    "is_promo": 0,
                    "ui_rates_hidden": false,
                    "selected_shipper_product_id": 0,
                    "range_price": {
                    "min_price": 31000,
                    "max_price": 31000
                },
                    "etd": {
                    "min_etd": 0,
                    "max_etd": 0
                },
                    "texts": {
                    "text_range_price": "Rp31.000",
                    "text_etd": "Maks. 6 jam",
                    "text_notes": "",
                    "text_service_desc": "",
                    "text_eta_summarize": "Estimasi tiba hari ini",
                    "error_code": 0
                },
                    "features": {
                    "dynamic_price": {
                    "text_label": ""
                }
                },
                    "order_priority": {
                    "is_now": false,
                    "inactive_message": "",
                    "price": 0,
                    "formatted_price": "",
                    "static_messages": {
                    "duration_message": "",
                    "checkbox_message": "",
                    "warningbox_message": "",
                    "fee_message": "",
                    "pdp_message": ""
                }
                },
                    "error": {
                    "error_id": "",
                    "error_message": ""
                },
                    "cod": {
                    "is_cod": 0,
                    "cod_text": ""
                },
                    "mvc": {
                    "is_mvc": 0,
                    "mvc_title": "",
                    "mvc_logo": "",
                    "mvc_error_message": ""
                },
                    "products": [
                    {
                        "shipper_name": "GoSend Bike",
                        "shipper_id": 10,
                        "shipper_product_id": 20,
                        "shipper_product_name": "Same Day",
                        "shipper_product_desc": "Layanan pengiriman dengan durasi pengiriman beberapa jam (6 jam) sejak serah terima paket ke kurir dan akan sampai di hari yang sama.",
                        "is_show_map": 1,
                        "shipper_weight": 1000,
                        "status": 200,
                        "recommend": true,
                        "checksum": "%2Ba81WpV9hK%2FGv3%2FscrFne3h2Mwk%3D",
                        "ut": "1680484116",
                        "promo_code": "",
                        "ui_rates_hidden": false,
                        "price": {
                        "price": 31000,
                        "formatted_price": "Rp31.000"
                    },
                        "etd": {
                        "min_etd": 21600,
                        "max_etd": 21600
                    },
                        "texts": {
                        "text_price": "Rp31.000",
                        "text_etd": "Maks. 6 jam"
                    },
                        "insurance": {
                        "insurance_price": 1800,
                        "insurance_type": 2,
                        "insurance_type_info": "Optional insurance",
                        "insurance_used_type": 2,
                        "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, \u003cb\u003egratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.\u003c/b\u003e Berlaku harga asuransi 0.6% dari harga invoice.",
                        "insurance_used_default": 2
                    },
                        "error": {
                        "error_id": "",
                        "error_message": ""
                    },
                        "cod": {
                        "is_cod_available": 0,
                        "cod_price": 0,
                        "cod_text": "",
                        "formatted_price": "",
                        "tnc_text": "",
                        "tnc_link": ""
                    },
                        "features": {
                        "ontime_delivery_guarantee": {
                        "available": true,
                        "value": 28800,
                        "text_label": "Jaminan Tepat Waktu",
                        "text_detail": "Ongkir kembali jika barang tiba lebih dari 8 Jam \u003cb\u003esejak diserahkan ke kurir\u003c/b\u003e \u003ca href\u003d\"https://gw.tokopedia.com/rates/otdg_info\"\u003eS\u0026K Berlaku\u003c/a\u003e",
                        "url_detail": "https://gw.tokopedia.com/rates/otdg_info",
                        "icon_url": "https://images.tokopedia.net/img/ongkirapp/otdg-icon-v2.png",
                        "url_text": "S\u0026K Berlaku"
                    },
                        "mvc": {
                        "is_mvc": 0,
                        "mvc_logo": "",
                        "mvc_error_message": ""
                    },
                        "dynamic_price": {
                        "text_label": ""
                    }
                    },
                        "eta": {
                        "text_eta": "Estimasi tiba hari ini",
                        "error_code": 0
                    }
                    }
                    ]
                },
                {
                    "service_name": "Next Day",
                    "service_id": 1003,
                    "service_order": 0,
                    "status": 200,
                    "is_promo": 0,
                    "ui_rates_hidden": false,
                    "selected_shipper_product_id": 0,
                    "range_price": {
                    "min_price": 15300,
                    "max_price": 18000
                },
                    "etd": {
                    "min_etd": 0,
                    "max_etd": 0
                },
                    "texts": {
                    "text_range_price": "Rp15.300 - Rp18.000",
                    "text_etd": "1 hari",
                    "text_notes": "",
                    "text_service_desc": "",
                    "text_eta_summarize": "Estimasi tiba besok - 5 Apr",
                    "error_code": 0
                },
                    "features": {
                    "dynamic_price": {
                    "text_label": ""
                }
                },
                    "order_priority": {
                    "is_now": false,
                    "inactive_message": "",
                    "price": 0,
                    "formatted_price": "",
                    "static_messages": {
                    "duration_message": "",
                    "checkbox_message": "",
                    "warningbox_message": "",
                    "fee_message": "",
                    "pdp_message": ""
                }
                },
                    "error": {
                    "error_id": "",
                    "error_message": ""
                },
                    "cod": {
                    "is_cod": 1,
                    "cod_text": "Tersedia COD (Bayar di Tempat)"
                },
                    "mvc": {
                    "is_mvc": 0,
                    "mvc_title": "",
                    "mvc_logo": "",
                    "mvc_error_message": ""
                },
                    "products": [
                    {
                        "shipper_name": "TIKI",
                        "shipper_id": 2,
                        "shipper_product_id": 16,
                        "shipper_product_name": "Over Night Service",
                        "shipper_product_desc": "ONS (Over Night Services) Hari ini paket Anda kami kirimkan dan paket akan segera tiba keesokan harinya.",
                        "is_show_map": 0,
                        "shipper_weight": 1000,
                        "status": 200,
                        "recommend": false,
                        "checksum": "c%2Fr5QR8f4zgx0otgiPVBwVqFG5c%3D",
                        "ut": "1680484116",
                        "promo_code": "",
                        "ui_rates_hidden": false,
                        "price": {
                        "price": 18000,
                        "formatted_price": "Rp18.000"
                    },
                        "etd": {
                        "min_etd": 86400,
                        "max_etd": 86400
                    },
                        "texts": {
                        "text_price": "Rp18.000",
                        "text_etd": "1 hari"
                    },
                        "insurance": {
                        "insurance_price": 1700,
                        "insurance_type": 2,
                        "insurance_type_info": "Optional insurance",
                        "insurance_used_type": 2,
                        "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, \u003cb\u003egratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.\u003c/b\u003e Berlaku harga asuransi 0.6% dari harga invoice.",
                        "insurance_used_default": 2
                    },
                        "error": {
                        "error_id": "",
                        "error_message": ""
                    },
                        "cod": {
                        "is_cod_available": 0,
                        "cod_price": 0,
                        "cod_text": "",
                        "formatted_price": "",
                        "tnc_text": "",
                        "tnc_link": ""
                    },
                        "features": {
                        "ontime_delivery_guarantee": {
                        "available": false,
                        "value": 0,
                        "text_label": "",
                        "text_detail": "",
                        "url_detail": "",
                        "icon_url": "",
                        "url_text": ""
                    },
                        "mvc": {
                        "is_mvc": 0,
                        "mvc_logo": "",
                        "mvc_error_message": ""
                    },
                        "dynamic_price": {
                        "text_label": ""
                    }
                    },
                        "eta": {
                        "text_eta": "Estimasi tiba besok - 5 Apr",
                        "error_code": 0
                    }
                    },
                    {
                        "shipper_name": "AnterAja",
                        "shipper_id": 23,
                        "shipper_product_id": 46,
                        "shipper_product_name": "Next Day",
                        "shipper_product_desc": "Layanan pengiriman express dengan durasi pengiriman 1 hari dihitung sejak serah terima paket ke kurir.",
                        "is_show_map": 0,
                        "shipper_weight": 1000,
                        "status": 200,
                        "recommend": true,
                        "checksum": "ZdIXaFIPs2hJLaAWthkdGv%2FeNJM%3D",
                        "ut": "1680484116",
                        "promo_code": "",
                        "ui_rates_hidden": false,
                        "price": {
                        "price": 15300,
                        "formatted_price": "Rp15.300"
                    },
                        "etd": {
                        "min_etd": 0,
                        "max_etd": 0
                    },
                        "texts": {
                        "text_price": "Rp15.300",
                        "text_etd": ""
                    },
                        "insurance": {
                        "insurance_price": 1700,
                        "insurance_type": 2,
                        "insurance_type_info": "Optional insurance",
                        "insurance_used_type": 2,
                        "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, \u003cb\u003egratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.\u003c/b\u003e Berlaku harga asuransi 0.6% dari harga invoice.",
                        "insurance_used_default": 2
                    },
                        "error": {
                        "error_id": "",
                        "error_message": ""
                    },
                        "cod": {
                        "is_cod_available": 1,
                        "cod_price": 5100,
                        "cod_text": "Tersedia COD (Bayar di Tempat)",
                        "formatted_price": "Rp5.100",
                        "tnc_text": "S\u0026K Berlaku",
                        "tnc_link": "https://gw.tokopedia.com/cod_info"
                    },
                        "features": {
                        "ontime_delivery_guarantee": {
                        "available": false,
                        "value": 0,
                        "text_label": "",
                        "text_detail": "",
                        "url_detail": "",
                        "icon_url": "",
                        "url_text": ""
                    },
                        "mvc": {
                        "is_mvc": 0,
                        "mvc_logo": "",
                        "mvc_error_message": ""
                    },
                        "dynamic_price": {
                        "text_label": ""
                    }
                    },
                        "eta": {
                        "text_eta": "Estimasi tiba besok - 5 Apr",
                        "error_code": 0
                    }
                    },
                    {
                        "shipper_name": "JNE",
                        "shipper_id": 1,
                        "shipper_product_id": 6,
                        "shipper_product_name": "YES",
                        "shipper_product_desc": "JNE YES adalah paket dengan prioritas pengiriman tercepat yang ditawarkan JNE. Hanya saja perlu diperhatikan kecepatan barang diterima juga dipengaruhi oleh kecepatan penjual melakukan pengiriman barang.",
                        "is_show_map": 0,
                        "shipper_weight": 1000,
                        "status": 200,
                        "recommend": false,
                        "checksum": "ANvjXRG6c4hFH7jZLabyIPXLB64%3D",
                        "ut": "1680484116",
                        "promo_code": "",
                        "ui_rates_hidden": false,
                        "price": {
                        "price": 18000,
                        "formatted_price": "Rp18.000"
                    },
                        "etd": {
                        "min_etd": 86400,
                        "max_etd": 86400
                    },
                        "texts": {
                        "text_price": "Rp18.000",
                        "text_etd": "1 hari"
                    },
                        "insurance": {
                        "insurance_price": 1700,
                        "insurance_type": 2,
                        "insurance_type_info": "Optional insurance",
                        "insurance_used_type": 2,
                        "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, \u003cb\u003egratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.\u003c/b\u003e Berlaku harga asuransi 0.6% dari harga invoice.",
                        "insurance_used_default": 2
                    },
                        "error": {
                        "error_id": "",
                        "error_message": ""
                    },
                        "cod": {
                        "is_cod_available": 0,
                        "cod_price": 0,
                        "cod_text": "",
                        "formatted_price": "",
                        "tnc_text": "",
                        "tnc_link": ""
                    },
                        "features": {
                        "ontime_delivery_guarantee": {
                        "available": false,
                        "value": 0,
                        "text_label": "",
                        "text_detail": "",
                        "url_detail": "",
                        "icon_url": "",
                        "url_text": ""
                    },
                        "mvc": {
                        "is_mvc": 0,
                        "mvc_logo": "",
                        "mvc_error_message": ""
                    },
                        "dynamic_price": {
                        "text_label": ""
                    }
                    },
                        "eta": {
                        "text_eta": "Estimasi tiba besok - 5 Apr",
                        "error_code": 0
                    }
                    }
                    ]
                },
                {
                    "service_name": "Reguler",
                    "service_id": 1104,
                    "service_order": 0,
                    "status": 200,
                    "is_promo": 0,
                    "ui_rates_hidden": false,
                    "selected_shipper_product_id": 0,
                    "range_price": {
                    "min_price": 6500,
                    "max_price": 11500
                },
                    "etd": {
                    "min_etd": 0,
                    "max_etd": 0
                },
                    "texts": {
                    "text_range_price": "Rp6.500 - Rp11.500",
                    "text_etd": "1-2 hari",
                    "text_notes": "",
                    "text_service_desc": "",
                    "text_eta_summarize": "Estimasi tiba 5 - 7 Apr",
                    "error_code": 0
                },
                    "features": {
                    "dynamic_price": {
                    "text_label": ""
                }
                },
                    "order_priority": {
                    "is_now": false,
                    "inactive_message": "",
                    "price": 0,
                    "formatted_price": "",
                    "static_messages": {
                    "duration_message": "",
                    "checkbox_message": "",
                    "warningbox_message": "",
                    "fee_message": "",
                    "pdp_message": ""
                }
                },
                    "error": {
                    "error_id": "",
                    "error_message": ""
                },
                    "cod": {
                    "is_cod": 1,
                    "cod_text": "Tersedia COD (Bayar di Tempat)"
                },
                    "mvc": {
                    "is_mvc": 0,
                    "mvc_title": "",
                    "mvc_logo": "",
                    "mvc_error_message": ""
                },
                    "products": [
                    {
                        "shipper_name": "Ninja Xpress",
                        "shipper_id": 12,
                        "shipper_product_id": 25,
                        "shipper_product_name": "Reguler",
                        "shipper_product_desc": "Layanan pengiriman standard dengan kecepatan pengiriman tergantung dari lokasi pengiriman dan lokasi tujuan. Umumnya 2-4 hari/ 5-9 hari/ \u003e9 hari tergantung rute pengiriman.",
                        "is_show_map": 1,
                        "shipper_weight": 1000,
                        "status": 200,
                        "recommend": false,
                        "checksum": "JVV0oWQ%2BNOoNettUJEoXaMW11XA%3D",
                        "ut": "1680484116",
                        "promo_code": "",
                        "ui_rates_hidden": false,
                        "price": {
                        "price": 6500,
                        "formatted_price": "Rp6.500"
                    },
                        "etd": {
                        "min_etd": 86400,
                        "max_etd": 172800
                    },
                        "texts": {
                        "text_price": "Rp6.500",
                        "text_etd": "1-2 hari"
                    },
                        "insurance": {
                        "insurance_price": 1600,
                        "insurance_type": 2,
                        "insurance_type_info": "Optional insurance",
                        "insurance_used_type": 2,
                        "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, \u003cb\u003egratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.\u003c/b\u003e Berlaku harga asuransi 0.6% dari harga invoice.",
                        "insurance_used_default": 2
                    },
                        "error": {
                        "error_id": "",
                        "error_message": ""
                    },
                        "cod": {
                        "is_cod_available": 0,
                        "cod_price": 0,
                        "cod_text": "",
                        "formatted_price": "",
                        "tnc_text": "",
                        "tnc_link": ""
                    },
                        "features": {
                        "ontime_delivery_guarantee": {
                        "available": false,
                        "value": 0,
                        "text_label": "",
                        "text_detail": "",
                        "url_detail": "",
                        "icon_url": "",
                        "url_text": ""
                    },
                        "mvc": {
                        "is_mvc": 0,
                        "mvc_logo": "",
                        "mvc_error_message": ""
                    },
                        "dynamic_price": {
                        "text_label": ""
                    }
                    },
                        "eta": {
                        "text_eta": "Estimasi tiba 5 - 7 Apr",
                        "error_code": 0
                    }
                    },
                    {
                        "shipper_name": "TIKI",
                        "shipper_id": 2,
                        "shipper_product_id": 3,
                        "shipper_product_name": "Reguler",
                        "shipper_product_desc": "TIKI Paket Reguler adalah paket yang dapat menjangkau seluruh Indonesia hanya dalam waktu kurang dari 7 hari kerja.",
                        "is_show_map": 0,
                        "shipper_weight": 1000,
                        "status": 200,
                        "recommend": false,
                        "checksum": "qtoL1hQy6W3KkEPHbsKlPRbdjpk%3D",
                        "ut": "1680484116",
                        "promo_code": "",
                        "ui_rates_hidden": false,
                        "price": {
                        "price": 9000,
                        "formatted_price": "Rp9.000"
                    },
                        "etd": {
                        "min_etd": 172800,
                        "max_etd": 172800
                    },
                        "texts": {
                        "text_price": "Rp9.000",
                        "text_etd": "2 hari"
                    },
                        "insurance": {
                        "insurance_price": 1600,
                        "insurance_type": 2,
                        "insurance_type_info": "Optional insurance",
                        "insurance_used_type": 2,
                        "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, \u003cb\u003egratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.\u003c/b\u003e Berlaku harga asuransi 0.6% dari harga invoice.",
                        "insurance_used_default": 2
                    },
                        "error": {
                        "error_id": "",
                        "error_message": ""
                    },
                        "cod": {
                        "is_cod_available": 0,
                        "cod_price": 0,
                        "cod_text": "",
                        "formatted_price": "",
                        "tnc_text": "",
                        "tnc_link": ""
                    },
                        "features": {
                        "ontime_delivery_guarantee": {
                        "available": false,
                        "value": 0,
                        "text_label": "",
                        "text_detail": "",
                        "url_detail": "",
                        "icon_url": "",
                        "url_text": ""
                    },
                        "mvc": {
                        "is_mvc": 0,
                        "mvc_logo": "",
                        "mvc_error_message": ""
                    },
                        "dynamic_price": {
                        "text_label": ""
                    }
                    },
                        "eta": {
                        "text_eta": "Estimasi tiba 5 - 7 Apr",
                        "error_code": 0
                    }
                    },
                    {
                        "shipper_name": "JNE Reg",
                        "shipper_id": 1,
                        "shipper_product_id": 1,
                        "shipper_product_name": "Reguler",
                        "shipper_product_desc": "JNE Reguler adalah paket reguler yang ditawarkan JNE. Kecepatan pengiriman tergantung dari lokasi pengiriman dan lokasi tujuan. Untuk kota yang sama, umumnya memakan waktu 2-3 hari.",
                        "is_show_map": 0,
                        "shipper_weight": 1000,
                        "status": 200,
                        "recommend": false,
                        "checksum": "YK9wMAoP65GmFpeZSNRrrmn37sg%3D",
                        "ut": "1680484116",
                        "promo_code": "",
                        "ui_rates_hidden": false,
                        "price": {
                        "price": 9000,
                        "formatted_price": "Rp9.000"
                    },
                        "etd": {
                        "min_etd": 86400,
                        "max_etd": 172800
                    },
                        "texts": {
                        "text_price": "Rp9.000",
                        "text_etd": "1-2 hari"
                    },
                        "insurance": {
                        "insurance_price": 1600,
                        "insurance_type": 2,
                        "insurance_type_info": "Optional insurance",
                        "insurance_used_type": 2,
                        "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, \u003cb\u003egratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.\u003c/b\u003e Berlaku harga asuransi 0.6% dari harga invoice.",
                        "insurance_used_default": 2
                    },
                        "error": {
                        "error_id": "",
                        "error_message": ""
                    },
                        "cod": {
                        "is_cod_available": 0,
                        "cod_price": 0,
                        "cod_text": "",
                        "formatted_price": "",
                        "tnc_text": "",
                        "tnc_link": ""
                    },
                        "features": {
                        "ontime_delivery_guarantee": {
                        "available": false,
                        "value": 0,
                        "text_label": "",
                        "text_detail": "",
                        "url_detail": "",
                        "icon_url": "",
                        "url_text": ""
                    },
                        "mvc": {
                        "is_mvc": 0,
                        "mvc_logo": "",
                        "mvc_error_message": ""
                    },
                        "dynamic_price": {
                        "text_label": ""
                    }
                    },
                        "eta": {
                        "text_eta": "Estimasi tiba 5 - 7 Apr",
                        "error_code": 0
                    }
                    },
                    {
                        "shipper_name": "J\u0026T",
                        "shipper_id": 14,
                        "shipper_product_id": 27,
                        "shipper_product_name": "Reguler",
                        "shipper_product_desc": "J\u0026T Express adalah layanan ekspres pertama di Indonesia berbasis teknologi, memberikan kemudahan dalam satu layanan.",
                        "is_show_map": 0,
                        "shipper_weight": 1000,
                        "status": 200,
                        "recommend": false,
                        "checksum": "y%2BRUhDZMNVh4ZYU8SweiPxb%2BJGg%3D",
                        "ut": "1680484116",
                        "promo_code": "",
                        "ui_rates_hidden": false,
                        "price": {
                        "price": 8000,
                        "formatted_price": "Rp8.000"
                    },
                        "etd": {
                        "min_etd": 86400,
                        "max_etd": 172800
                    },
                        "texts": {
                        "text_price": "Rp8.000",
                        "text_etd": "1-2 hari"
                    },
                        "insurance": {
                        "insurance_price": 1600,
                        "insurance_type": 2,
                        "insurance_type_info": "Optional insurance",
                        "insurance_used_type": 2,
                        "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, \u003cb\u003egratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.\u003c/b\u003e Berlaku harga asuransi 0.6% dari harga invoice.",
                        "insurance_used_default": 2
                    },
                        "error": {
                        "error_id": "",
                        "error_message": ""
                    },
                        "cod": {
                        "is_cod_available": 0,
                        "cod_price": 0,
                        "cod_text": "",
                        "formatted_price": "",
                        "tnc_text": "",
                        "tnc_link": ""
                    },
                        "features": {
                        "ontime_delivery_guarantee": {
                        "available": false,
                        "value": 0,
                        "text_label": "",
                        "text_detail": "",
                        "url_detail": "",
                        "icon_url": "",
                        "url_text": ""
                    },
                        "mvc": {
                        "is_mvc": 0,
                        "mvc_logo": "",
                        "mvc_error_message": ""
                    },
                        "dynamic_price": {
                        "text_label": ""
                    }
                    },
                        "eta": {
                        "text_eta": "Estimasi tiba 5 - 7 Apr",
                        "error_code": 0
                    }
                    },
                    {
                        "shipper_name": "AnterAja",
                        "shipper_id": 23,
                        "shipper_product_id": 45,
                        "shipper_product_name": "Reguler",
                        "shipper_product_desc": "Layanan pengiriman standard dengan kecepatan pengiriman tergantung dari lokasi pengiriman dan lokasi tujuan. Umumnya 2-4 hari/ 5-9 hari/ \u003e9 hari tergantung rute pengiriman.",
                        "is_show_map": 0,
                        "shipper_weight": 1000,
                        "status": 200,
                        "recommend": true,
                        "checksum": "1NJkdpvAkF7dd9b0bsFQL2gVBr8%3D",
                        "ut": "1680484116",
                        "promo_code": "",
                        "ui_rates_hidden": false,
                        "price": {
                        "price": 11500,
                        "formatted_price": "Rp11.500"
                    },
                        "etd": {
                        "min_etd": 86400,
                        "max_etd": 172800
                    },
                        "texts": {
                        "text_price": "Rp11.500",
                        "text_etd": "1-2 hari"
                    },
                        "insurance": {
                        "insurance_price": 1600,
                        "insurance_type": 2,
                        "insurance_type_info": "Optional insurance",
                        "insurance_used_type": 2,
                        "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, \u003cb\u003egratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.\u003c/b\u003e Berlaku harga asuransi 0.6% dari harga invoice.",
                        "insurance_used_default": 2
                    },
                        "error": {
                        "error_id": "",
                        "error_message": ""
                    },
                        "cod": {
                        "is_cod_available": 1,
                        "cod_price": 5100,
                        "cod_text": "Tersedia COD (Bayar di Tempat)",
                        "formatted_price": "Rp5.100",
                        "tnc_text": "S\u0026K Berlaku",
                        "tnc_link": "https://gw.tokopedia.com/cod_info"
                    },
                        "features": {
                        "ontime_delivery_guarantee": {
                        "available": false,
                        "value": 0,
                        "text_label": "",
                        "text_detail": "",
                        "url_detail": "",
                        "icon_url": "",
                        "url_text": ""
                    },
                        "mvc": {
                        "is_mvc": 0,
                        "mvc_logo": "",
                        "mvc_error_message": ""
                    },
                        "dynamic_price": {
                        "text_label": ""
                    }
                    },
                        "eta": {
                        "text_eta": "Estimasi tiba 5 - 6 Apr",
                        "error_code": 0
                    }
                    },
                    {
                        "shipper_name": "Lion Parcel",
                        "shipper_id": 24,
                        "shipper_product_id": 47,
                        "shipper_product_name": "Reguler",
                        "shipper_product_desc": "Layanan pengiriman standard dengan kecepatan pengiriman tergantung dari lokasi pengiriman dan lokasi tujuan. Umumnya 2-4 hari/ 5-9 hari/ \u003e9 hari tergantung rute pengiriman.",
                        "is_show_map": 0,
                        "shipper_weight": 1000,
                        "status": 200,
                        "recommend": false,
                        "checksum": "e86uKI5bIAr5oH0nzXZtABcLOyU%3D",
                        "ut": "1680484116",
                        "promo_code": "",
                        "ui_rates_hidden": false,
                        "price": {
                        "price": 7500,
                        "formatted_price": "Rp7.500"
                    },
                        "etd": {
                        "min_etd": 0,
                        "max_etd": 0
                    },
                        "texts": {
                        "text_price": "Rp7.500",
                        "text_etd": ""
                    },
                        "insurance": {
                        "insurance_price": 1600,
                        "insurance_type": 2,
                        "insurance_type_info": "Optional insurance",
                        "insurance_used_type": 2,
                        "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, \u003cb\u003egratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.\u003c/b\u003e Berlaku harga asuransi 0.6% dari harga invoice.",
                        "insurance_used_default": 2
                    },
                        "error": {
                        "error_id": "",
                        "error_message": ""
                    },
                        "cod": {
                        "is_cod_available": 0,
                        "cod_price": 0,
                        "cod_text": "",
                        "formatted_price": "",
                        "tnc_text": "",
                        "tnc_link": ""
                    },
                        "features": {
                        "ontime_delivery_guarantee": {
                        "available": false,
                        "value": 0,
                        "text_label": "",
                        "text_detail": "",
                        "url_detail": "",
                        "icon_url": "",
                        "url_text": ""
                    },
                        "mvc": {
                        "is_mvc": 0,
                        "mvc_logo": "",
                        "mvc_error_message": ""
                    },
                        "dynamic_price": {
                        "text_label": ""
                    }
                    },
                        "eta": {
                        "text_eta": "Estimasi tiba 5 - 6 Apr",
                        "error_code": 0
                    }
                    },
                    {
                        "shipper_name": "SiCepat Reg",
                        "shipper_id": 11,
                        "shipper_product_id": 18,
                        "shipper_product_name": "Regular Package",
                        "shipper_product_desc": "Layanan Pengiriman biasa dengan lead time 1-2 hari (kota besar) dan lead time 3-4 hari (Kabupaten/transit)",
                        "is_show_map": 0,
                        "shipper_weight": 1000,
                        "status": 200,
                        "recommend": false,
                        "checksum": "HZrNkqr4xjX5nmtSLa8%2FOtge1cQ%3D",
                        "ut": "1680484116",
                        "promo_code": "",
                        "ui_rates_hidden": false,
                        "price": {
                        "price": 11500,
                        "formatted_price": "Rp11.500"
                    },
                        "etd": {
                        "min_etd": 86400,
                        "max_etd": 172800
                    },
                        "texts": {
                        "text_price": "Rp11.500",
                        "text_etd": "1-2 hari"
                    },
                        "insurance": {
                        "insurance_price": 1600,
                        "insurance_type": 2,
                        "insurance_type_info": "Optional insurance",
                        "insurance_used_type": 2,
                        "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, \u003cb\u003egratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.\u003c/b\u003e Berlaku harga asuransi 0.6% dari harga invoice.",
                        "insurance_used_default": 2
                    },
                        "error": {
                        "error_id": "",
                        "error_message": ""
                    },
                        "cod": {
                        "is_cod_available": 1,
                        "cod_price": 5100,
                        "cod_text": "Tersedia COD (Bayar di Tempat)",
                        "formatted_price": "Rp5.100",
                        "tnc_text": "S\u0026K Berlaku",
                        "tnc_link": "https://gw.tokopedia.com/cod_info"
                    },
                        "features": {
                        "ontime_delivery_guarantee": {
                        "available": false,
                        "value": 0,
                        "text_label": "",
                        "text_detail": "",
                        "url_detail": "",
                        "icon_url": "",
                        "url_text": ""
                    },
                        "mvc": {
                        "is_mvc": 0,
                        "mvc_logo": "",
                        "mvc_error_message": ""
                    },
                        "dynamic_price": {
                        "text_label": ""
                    }
                    },
                        "eta": {
                        "text_eta": "Estimasi tiba 5 - 6 Apr",
                        "error_code": 0
                    }
                    }
                    ]
                },
                {
                    "service_name": "Reguler",
                    "service_id": 1204,
                    "service_order": 0,
                    "status": 200,
                    "is_promo": 0,
                    "ui_rates_hidden": false,
                    "selected_shipper_product_id": 0,
                    "range_price": {
                    "min_price": 7000,
                    "max_price": 7000
                },
                    "etd": {
                    "min_etd": 0,
                    "max_etd": 0
                },
                    "texts": {
                    "text_range_price": "Rp7.000",
                    "text_etd": "3-5 hari",
                    "text_notes": "",
                    "text_service_desc": "",
                    "text_eta_summarize": "Estimasi tiba 5 - 6 Apr",
                    "error_code": 0
                },
                    "features": {
                    "dynamic_price": {
                    "text_label": ""
                }
                },
                    "order_priority": {
                    "is_now": false,
                    "inactive_message": "",
                    "price": 0,
                    "formatted_price": "",
                    "static_messages": {
                    "duration_message": "",
                    "checkbox_message": "",
                    "warningbox_message": "",
                    "fee_message": "",
                    "pdp_message": ""
                }
                },
                    "error": {
                    "error_id": "",
                    "error_message": ""
                },
                    "cod": {
                    "is_cod": 0,
                    "cod_text": ""
                },
                    "mvc": {
                    "is_mvc": 0,
                    "mvc_title": "",
                    "mvc_logo": "",
                    "mvc_error_message": ""
                },
                    "products": [
                    {
                        "shipper_name": "Pos Indonesia",
                        "shipper_id": 4,
                        "shipper_product_id": 10,
                        "shipper_product_name": "Pos Kilat Khusus",
                        "shipper_product_desc": "Gunakan Pos Reguler, sebagai pilihan tepat untuk pengiriman Suratpos yang mengandalkan kecepatan kiriman dan menjangkau ke seluruh pelosok Indonesia.",
                        "is_show_map": 0,
                        "shipper_weight": 1000,
                        "status": 200,
                        "recommend": true,
                        "checksum": "HOBOqt4ur90bJET4SNd%2Fef5l7zI%3D",
                        "ut": "1680484116",
                        "promo_code": "",
                        "ui_rates_hidden": false,
                        "price": {
                        "price": 7000,
                        "formatted_price": "Rp7.000"
                    },
                        "etd": {
                        "min_etd": 259200,
                        "max_etd": 432000
                    },
                        "texts": {
                        "text_price": "Rp7.000",
                        "text_etd": "3-5 hari"
                    },
                        "insurance": {
                        "insurance_price": 1600,
                        "insurance_type": 2,
                        "insurance_type_info": "Optional insurance",
                        "insurance_used_type": 2,
                        "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, \u003cb\u003egratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.\u003c/b\u003e Berlaku harga asuransi 0.6% dari harga invoice.",
                        "insurance_used_default": 2
                    },
                        "error": {
                        "error_id": "",
                        "error_message": ""
                    },
                        "cod": {
                        "is_cod_available": 0,
                        "cod_price": 0,
                        "cod_text": "",
                        "formatted_price": "",
                        "tnc_text": "",
                        "tnc_link": ""
                    },
                        "features": {
                        "ontime_delivery_guarantee": {
                        "available": false,
                        "value": 0,
                        "text_label": "",
                        "text_detail": "",
                        "url_detail": "",
                        "icon_url": "",
                        "url_text": ""
                    },
                        "mvc": {
                        "is_mvc": 0,
                        "mvc_logo": "",
                        "mvc_error_message": ""
                    },
                        "dynamic_price": {
                        "text_label": ""
                    }
                    },
                        "eta": {
                        "text_eta": "Estimasi tiba 5 - 6 Apr",
                        "error_code": 0
                    }
                    }
                    ]
                },
                {
                    "service_name": "Kargo",
                    "service_id": 1001,
                    "service_order": 0,
                    "status": 200,
                    "is_promo": 0,
                    "ui_rates_hidden": false,
                    "selected_shipper_product_id": 0,
                    "range_price": {
                    "min_price": 25000,
                    "max_price": 35000
                },
                    "etd": {
                    "min_etd": 0,
                    "max_etd": 0
                },
                    "texts": {
                    "text_range_price": "Rp25.000 - Rp35.000",
                    "text_etd": "1-3 hari",
                    "text_notes": "",
                    "text_service_desc": "Rekomendasi berat di atas 5kg",
                    "text_eta_summarize": "Estimasi tiba 6 - 9 Apr",
                    "error_code": 0
                },
                    "features": {
                    "dynamic_price": {
                    "text_label": ""
                }
                },
                    "order_priority": {
                    "is_now": false,
                    "inactive_message": "",
                    "price": 0,
                    "formatted_price": "",
                    "static_messages": {
                    "duration_message": "",
                    "checkbox_message": "",
                    "warningbox_message": "",
                    "fee_message": "",
                    "pdp_message": ""
                }
                },
                    "error": {
                    "error_id": "",
                    "error_message": ""
                },
                    "cod": {
                    "is_cod": 1,
                    "cod_text": "Tersedia COD (Bayar di Tempat)"
                },
                    "mvc": {
                    "is_mvc": 0,
                    "mvc_title": "",
                    "mvc_logo": "",
                    "mvc_error_message": ""
                },
                    "products": [
                    {
                        "shipper_name": "REX",
                        "shipper_id": 16,
                        "shipper_product_id": 32,
                        "shipper_product_name": "REX-10",
                        "shipper_product_desc": "Layanan pengiriman standard dengan kecepatan pengiriman 3-8 hari tergantung rute pengiriman. Harga lebih murah/ ekonomis dan berskala besar (bulk shipment) dengan durasi pengiriman yang lebih lama karena menggunakan jalur darat.",
                        "is_show_map": 0,
                        "shipper_weight": 1000,
                        "status": 200,
                        "recommend": true,
                        "checksum": "rHJMEa059yZijuj1sPe8D0NNev0%3D",
                        "ut": "1680484116",
                        "promo_code": "",
                        "ui_rates_hidden": false,
                        "price": {
                        "price": 25000,
                        "formatted_price": "Rp25.000"
                    },
                        "etd": {
                        "min_etd": 86400,
                        "max_etd": 259200
                    },
                        "texts": {
                        "text_price": "Rp25.000",
                        "text_etd": "1-3 hari"
                    },
                        "insurance": {
                        "insurance_price": 1700,
                        "insurance_type": 2,
                        "insurance_type_info": "Optional insurance",
                        "insurance_used_type": 2,
                        "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, \u003cb\u003egratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.\u003c/b\u003e Berlaku harga asuransi 0.6% dari harga invoice.",
                        "insurance_used_default": 2
                    },
                        "error": {
                        "error_id": "",
                        "error_message": ""
                    },
                        "cod": {
                        "is_cod_available": 0,
                        "cod_price": 0,
                        "cod_text": "",
                        "formatted_price": "",
                        "tnc_text": "",
                        "tnc_link": ""
                    },
                        "features": {
                        "ontime_delivery_guarantee": {
                        "available": false,
                        "value": 0,
                        "text_label": "",
                        "text_detail": "",
                        "url_detail": "",
                        "icon_url": "",
                        "url_text": ""
                    },
                        "mvc": {
                        "is_mvc": 0,
                        "mvc_logo": "",
                        "mvc_error_message": ""
                    },
                        "dynamic_price": {
                        "text_label": ""
                    }
                    },
                        "eta": {
                        "text_eta": "Estimasi tiba 6 - 8 Apr",
                        "error_code": 0
                    }
                    },
                    {
                        "shipper_name": "SiCepat Gokil",
                        "shipper_id": 11,
                        "shipper_product_id": 43,
                        "shipper_product_name": "Gokil",
                        "shipper_product_desc": "Layanan pengiriman standard dengan kecepatan pengiriman 3-8 hari tergantung rute pengiriman. Harga lebih murah/ ekonomis dan berskala besar (bulk shipment) dengan durasi pengiriman yang lebih lama karena menggunakan jalur darat.",
                        "is_show_map": 0,
                        "shipper_weight": 1000,
                        "status": 200,
                        "recommend": false,
                        "checksum": "2TSU4AVrX9mGt%2FIqo92xt9aymfU%3D",
                        "ut": "1680484116",
                        "promo_code": "",
                        "ui_rates_hidden": false,
                        "price": {
                        "price": 35000,
                        "formatted_price": "Rp35.000"
                    },
                        "etd": {
                        "min_etd": 172800,
                        "max_etd": 259200
                    },
                        "texts": {
                        "text_price": "Rp35.000",
                        "text_etd": "2-3 hari"
                    },
                        "insurance": {
                        "insurance_price": 1800,
                        "insurance_type": 2,
                        "insurance_type_info": "Optional insurance",
                        "insurance_used_type": 2,
                        "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, \u003cb\u003egratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.\u003c/b\u003e Berlaku harga asuransi 0.6% dari harga invoice.",
                        "insurance_used_default": 2
                    },
                        "error": {
                        "error_id": "",
                        "error_message": ""
                    },
                        "cod": {
                        "is_cod_available": 1,
                        "cod_price": 5100,
                        "cod_text": "Tersedia COD (Bayar di Tempat)",
                        "formatted_price": "Rp5.100",
                        "tnc_text": "S\u0026K Berlaku",
                        "tnc_link": "https://gw.tokopedia.com/cod_info"
                    },
                        "features": {
                        "ontime_delivery_guarantee": {
                        "available": false,
                        "value": 0,
                        "text_label": "",
                        "text_detail": "",
                        "url_detail": "",
                        "icon_url": "",
                        "url_text": ""
                    },
                        "mvc": {
                        "is_mvc": 0,
                        "mvc_logo": "",
                        "mvc_error_message": ""
                    },
                        "dynamic_price": {
                        "text_label": ""
                    }
                    },
                        "eta": {
                        "text_eta": "Estimasi tiba 6 - 9 Apr",
                        "error_code": 0
                    }
                    }
                    ]
                },
                {
                    "service_name": "Ekonomi",
                    "service_id": 1005,
                    "service_order": 0,
                    "status": 200,
                    "is_promo": 0,
                    "ui_rates_hidden": false,
                    "selected_shipper_product_id": 0,
                    "range_price": {
                    "min_price": 5000,
                    "max_price": 5000
                },
                    "etd": {
                    "min_etd": 0,
                    "max_etd": 0
                },
                    "texts": {
                    "text_range_price": "Rp5.000",
                    "text_etd": "2 hari",
                    "text_notes": "",
                    "text_service_desc": "",
                    "text_eta_summarize": "Estimasi tiba 6 - 8 Apr",
                    "error_code": 0
                },
                    "features": {
                    "dynamic_price": {
                    "text_label": ""
                }
                },
                    "order_priority": {
                    "is_now": false,
                    "inactive_message": "",
                    "price": 0,
                    "formatted_price": "",
                    "static_messages": {
                    "duration_message": "",
                    "checkbox_message": "",
                    "warningbox_message": "",
                    "fee_message": "",
                    "pdp_message": ""
                }
                },
                    "error": {
                    "error_id": "",
                    "error_message": ""
                },
                    "cod": {
                    "is_cod": 0,
                    "cod_text": ""
                },
                    "mvc": {
                    "is_mvc": 0,
                    "mvc_title": "",
                    "mvc_logo": "",
                    "mvc_error_message": ""
                },
                    "products": [
                    {
                        "shipper_name": "Wahana",
                        "shipper_id": 6,
                        "shipper_product_id": 8,
                        "shipper_product_name": "Service Normal",
                        "shipper_product_desc": "Service Normal adalah pengiriman paket yang ditawarkan Wahana ke seluruh kota besar Indonesia dengan lead time pengiriman estimasi 3-4 hari untuk kota besar propinsi dan +10 hari untuk kota kabupaten sesuai kecamatan yang dituju.",
                        "is_show_map": 0,
                        "shipper_weight": 1000,
                        "status": 200,
                        "recommend": true,
                        "checksum": "FzWlg%2FWNb5ObpP9IBYFaJ5SQA%2BU%3D",
                        "ut": "1680484116",
                        "promo_code": "",
                        "ui_rates_hidden": false,
                        "price": {
                        "price": 5000,
                        "formatted_price": "Rp5.000"
                    },
                        "etd": {
                        "min_etd": 172800,
                        "max_etd": 172800
                    },
                        "texts": {
                        "text_price": "Rp5.000",
                        "text_etd": "2 hari"
                    },
                        "insurance": {
                        "insurance_price": 1600,
                        "insurance_type": 3,
                        "insurance_type_info": "Must insurance",
                        "insurance_used_type": 2,
                        "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, \u003cb\u003egratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.\u003c/b\u003e Berlaku harga asuransi 0.6% dari harga invoice.",
                        "insurance_used_default": 2
                    },
                        "error": {
                        "error_id": "",
                        "error_message": ""
                    },
                        "cod": {
                        "is_cod_available": 0,
                        "cod_price": 0,
                        "cod_text": "",
                        "formatted_price": "",
                        "tnc_text": "",
                        "tnc_link": ""
                    },
                        "features": {
                        "ontime_delivery_guarantee": {
                        "available": false,
                        "value": 0,
                        "text_label": "",
                        "text_detail": "",
                        "url_detail": "",
                        "icon_url": "",
                        "url_text": ""
                    },
                        "mvc": {
                        "is_mvc": 0,
                        "mvc_logo": "",
                        "mvc_error_message": ""
                    },
                        "dynamic_price": {
                        "text_label": ""
                    }
                    },
                        "eta": {
                        "text_eta": "Estimasi tiba 6 - 8 Apr",
                        "error_code": 0
                    }
                    }
                    ]
                }
                ],
                "promo_stacking": {
                "is_promo": 1,
                "promo_code": "BOINTRAISLANDQA10",
                "title": "Bebas Ongkir",
                "shipper_id": 23,
                "shipper_product_id": 45,
                "shipper_name": "AnterAja",
                "shipper_desc": "2-4 hari|1104",
                "promo_detail": "",
                "benefit_desc": "",
                "point_change": 0,
                "user_point": 0,
                "promo_tnc_html": "Anda akan menggunakan promo \u003cb\u003eBOINTRAISLANDQA10\u003c/b\u003e",
                "shipper_disable_text": "Tidak dapat \u003cb\u003eubah kurir\u003c/b\u003e karena promo hanya berlaku untuk kurir yang ditentukan",
                "service_id": 1104,
                "is_applied": 0,
                "image_url": "https://images.tokopedia.net/img/img/promo/targeting/red_bo_20k.png",
                "discounted_rate": 0,
                "shipping_rate": 11500,
                "benefit_amount": 20000,
                "disabled": false,
                "hide_shipper_name": true,
                "cod": {
                "is_cod_available": 1,
                "cod_text": "Tersedia COD (Bayar di Tempat)",
                "cod_price": 5100,
                "formatted_price": "Rp5.100",
                "tnc_text": "S\u0026K Berlaku",
                "tnc_link": "https://gw.tokopedia.com/cod_info"
            },
                "eta": {
                "text_eta": "Estimasi tiba 5 - 6 Apr",
                "error_code": 0
            },
                "is_bebas_ongkir_extra": false,
                "texts": {
                "bottom_sheet": "\u003cb\u003eEstimasi tiba 5 - 6 Apr (Rp0)\u003c/b\u003e",
                "chosen_courier": "\u003cb\u003eBebas Ongkir (Rp0)\u003c/b\u003e",
                "ticker_courier": "Tersedia Bebas Ongkir",
                "bottom_sheet_description": ""
            },
                "free_shipping_metadata": {
                "sent_shipper_partner": false,
                "benefit_class": "",
                "shipping_subsidy": 20000,
                "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
            }
            },
                "promo_stackings": [
                {
                    "is_promo": 1,
                    "promo_code": "BOINTRAISLANDQA10",
                    "title": "Bebas Ongkir",
                    "shipper_id": 23,
                    "shipper_product_id": 45,
                    "shipper_name": "AnterAja",
                    "shipper_desc": "2-4 hari|1104",
                    "promo_detail": "",
                    "benefit_desc": "",
                    "point_change": 0,
                    "user_point": 0,
                    "promo_tnc_html": "Anda akan menggunakan promo \u003cb\u003eBOINTRAISLANDQA10\u003c/b\u003e",
                    "shipper_disable_text": "Tidak dapat \u003cb\u003eubah kurir\u003c/b\u003e karena promo hanya berlaku untuk kurir yang ditentukan",
                    "service_id": 1104,
                    "is_applied": 0,
                    "image_url": "https://images.tokopedia.net/img/img/promo/targeting/red_bo_20k.png",
                    "discounted_rate": 0,
                    "shipping_rate": 11500,
                    "benefit_amount": 20000,
                    "disabled": false,
                    "hide_shipper_name": true,
                    "cod": {
                    "is_cod_available": 1,
                    "cod_text": "Tersedia COD (Bayar di Tempat)",
                    "cod_price": 5100,
                    "formatted_price": "Rp5.100",
                    "tnc_text": "S\u0026K Berlaku",
                    "tnc_link": "https://gw.tokopedia.com/cod_info"
                },
                    "eta": {
                    "text_eta": "Estimasi tiba 5 - 6 Apr",
                    "error_code": 0
                },
                    "is_bebas_ongkir_extra": false,
                    "texts": {
                    "bottom_sheet": "\u003cb\u003eEstimasi tiba 5 - 6 Apr (Rp0)\u003c/b\u003e",
                    "chosen_courier": "\u003cb\u003eBebas Ongkir (Rp0)\u003c/b\u003e",
                    "ticker_courier": "Tersedia Bebas Ongkir",
                    "bottom_sheet_description": "",
                    "promo_message": "Beberapa promo tidak bisa dipakai di pengiriman ini.",
                    "title_promo_message": "Beberapa promo tidak tersedia"
                },
                    "free_shipping_metadata": {
                    "sent_shipper_partner": false,
                    "benefit_class": "",
                    "shipping_subsidy": 20000,
                    "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                },
                    "bo_campaign_id": 27
                },
                {
                    "is_promo": 1,
                    "promo_code": "WGOSAMTAR10",
                    "title": "Bebas Ongkir",
                    "shipper_id": 10,
                    "shipper_product_id": 20,
                    "shipper_name": "GoSend Bike",
                    "shipper_desc": "6-8 jam|1002",
                    "promo_detail": "",
                    "benefit_desc": "Pengiriman melebihi limit bebas ongkir, kamu cukup bayar \u003cb\u003eRp8.000\u003c/b\u003e",
                    "point_change": 0,
                    "user_point": 0,
                    "promo_tnc_html": "Anda akan menggunakan promo \u003cb\u003eWGOSAMTAR10\u003c/b\u003e",
                    "shipper_disable_text": "Tidak dapat \u003cb\u003eubah kurir\u003c/b\u003e karena promo hanya berlaku untuk kurir yang ditentukan",
                    "service_id": 1002,
                    "is_applied": 0,
                    "image_url": "",
                    "discounted_rate": 8000,
                    "shipping_rate": 31000,
                    "benefit_amount": 23000,
                    "disabled": false,
                    "hide_shipper_name": true,
                    "cod": {
                    "is_cod_available": 0,
                    "cod_text": "",
                    "cod_price": 0,
                    "formatted_price": "",
                    "tnc_text": "",
                    "tnc_link": ""
                },
                    "eta": {
                    "text_eta": "Estimasi tiba hari ini",
                    "error_code": 0
                },
                    "is_bebas_ongkir_extra": false,
                    "texts": {
                    "bottom_sheet": "\u003cb\u003eEstimasi tiba hari ini (Rp8.000\u003c/b\u003e \u003cs\u003eRp31.000\u003c/s\u003e\u003cb\u003e)\u003c/b\u003e",
                    "chosen_courier": "\u003cb\u003eBebas Ongkir (Rp8.000\u003c/b\u003e \u003cs\u003eRp31.000\u003c/s\u003e\u003cb\u003e)\u003c/b\u003e",
                    "ticker_courier": "Tersedia Bebas Ongkir",
                    "bottom_sheet_description": "",
                    "promo_message": "Beberapa promo tidak bisa dipakai di pengiriman ini.",
                    "title_promo_message": "Beberapa promo tidak tersedia"
                },
                    "free_shipping_metadata": {
                    "sent_shipper_partner": true,
                    "benefit_class": "sameday",
                    "shipping_subsidy": 23000,
                    "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                },
                    "bo_campaign_id": 26
                }
                ],
                "pre_order": {
                "header": "",
                "label": "",
                "display": false
            },
                "info": {
                "blackbox_info": {
                "text_info": ""
            }
            }
            }
            }
            }
"""
        ).map {
            val response = Gson().fromJson(it, RatesGqlResponse::class.java)
            converter.convertModel(response.ratesData)
        }
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
//        return gql.getExecuteObservable(null)
//            .map { graphqlResponse: GraphqlResponse ->
//                val response: RatesGqlResponse =
//                    graphqlResponse.getData<RatesGqlResponse>(RatesGqlResponse::class.java)
//                        ?: throw MessageErrorException(DEFAULT_ERROR_MESSAGE)
//                converter.convertModel(response.ratesData)
//            }
//            .subscribeOn(scheduler.io())
//            .observeOn(scheduler.ui())
    }

    fun unsubscribe() {
        gql?.unsubscribe()
    }
}
