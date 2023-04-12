package com.tokopedia.logisticcart.scheduledelivery.domain.usecase

import com.google.gson.Gson
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.logisticcart.scheduledelivery.domain.entity.request.ScheduleDeliveryParam
import com.tokopedia.logisticcart.scheduledelivery.domain.entity.response.ScheduleDeliveryRatesResponse
import com.tokopedia.logisticcart.shipping.usecase.scheduleDeliveryRatesQuery
import rx.Observable
import javax.inject.Inject

class GetScheduleDeliveryUseCase @Inject constructor(
    private val scheduler: SchedulerProvider
) {

    private var gql: GraphqlUseCase? = null

    fun execute(param: ScheduleDeliveryParam): Observable<ScheduleDeliveryRatesResponse> {
        val query = scheduleDeliveryRatesQuery()
        val gqlRequest =
            GraphqlRequest(query, ScheduleDeliveryRatesResponse::class.java, param.toMap())
        // Need to init usecase here to prevent request cleared since this usecase will be called multiple time in a very tight interval of each call.
        // Will consider this as tech debt until find a proper solution
        val gql = GraphqlUseCase()
        this.gql = gql
        gql.clearRequest()
        gql.addRequest(gqlRequest)
        return Observable.just(
            """
            {
                        "ongkirGetScheduledDeliveryRates": {
                            "data": {
                                "rates_id": 1681311106302028959,
                                "available": true,
                                "hidden": false,
                                "recommend": true,
                                "delivery_type": 1,
                                "title": "Jadwal lainnya",
                                "text": "",
                                "notice": {
                                    "title": "Kirim sesuai jadwal",
                                    "text": "Bebas atur kapan pesanan tiba sesuai kebutuhanmu. Layanan ini baru tersedia untuk pesanan Tokopedia NOW!."
                                },
                                "error": {
                                    "error_id": "",
                                    "error_message": ""
                                },
                                "delivery_services": [
                                    {
                                        "title": "Hari ini",
                                        "title_label": "12 Apr",
                                        "id": "2023-04-12",
                                        "available": true,
                                        "hidden": false,
                                        "error": {
                                            "error_id": "",
                                            "error_message": ""
                                        },
                                        "delivery_products": [
                                            {
                                                "title": "Tiba 00:00 - 02:00",
                                                "text": "",
                                                "text_eta": "Tiba hari ini, 00:00 - 02:00",
                                                "promo_text": "",
                                                "timeslot_id": 335,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":335,\"schedule_date\":\"2023-04-12T00:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 02:00 - 04:00",
                                                "text": "",
                                                "text_eta": "Tiba hari ini, 02:00 - 04:00",
                                                "promo_text": "",
                                                "timeslot_id": 336,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":336,\"schedule_date\":\"2023-04-12T02:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 04:00 - 06:00",
                                                "text": "",
                                                "text_eta": "Tiba hari ini, 04:00 - 06:00",
                                                "promo_text": "",
                                                "timeslot_id": 337,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":337,\"schedule_date\":\"2023-04-12T04:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 06:00 - 08:00",
                                                "text": "",
                                                "text_eta": "Tiba hari ini, 06:00 - 08:00",
                                                "promo_text": "",
                                                "timeslot_id": 338,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":338,\"schedule_date\":\"2023-04-12T06:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 08:00 - 10:00",
                                                "text": "",
                                                "text_eta": "Tiba hari ini, 08:00 - 10:00",
                                                "promo_text": "",
                                                "timeslot_id": 339,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":339,\"schedule_date\":\"2023-04-12T08:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 10:00 - 12:00",
                                                "text": "",
                                                "text_eta": "Tiba hari ini, 10:00 - 12:00",
                                                "promo_text": "",
                                                "timeslot_id": 340,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":340,\"schedule_date\":\"2023-04-12T10:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 12:00 - 14:00",
                                                "text": "",
                                                "text_eta": "Tiba hari ini, 12:00 - 14:00",
                                                "promo_text": "",
                                                "timeslot_id": 341,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":341,\"schedule_date\":\"2023-04-12T12:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 14:00 - 16:00",
                                                "text": "",
                                                "text_eta": "Tiba hari ini, 14:00 - 16:00",
                                                "promo_text": "",
                                                "timeslot_id": 342,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":342,\"schedule_date\":\"2023-04-12T14:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 16:00 - 18:00",
                                                "text": "",
                                                "text_eta": "Tiba hari ini, 16:00 - 18:00",
                                                "promo_text": "",
                                                "timeslot_id": 343,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":343,\"schedule_date\":\"2023-04-12T16:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 18:00 - 20:00",
                                                "text": "Sisa 1 slot",
                                                "text_eta": "Tiba hari ini, 18:00 - 20:00",
                                                "promo_text": "",
                                                "timeslot_id": 344,
                                                "available": true,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":344,\"schedule_date\":\"2023-04-12T18:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 20:00 - 22:00",
                                                "text": "",
                                                "text_eta": "Tiba hari ini, 20:00 - 22:00",
                                                "promo_text": "",
                                                "timeslot_id": 345,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":345,\"schedule_date\":\"2023-04-12T20:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 22:00 - 00:00",
                                                "text": "",
                                                "text_eta": "Tiba hari ini, 22:00 - 00:00",
                                                "promo_text": "",
                                                "timeslot_id": 346,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":346,\"schedule_date\":\"2023-04-12T22:00:00+07:00\",\"shipping_price\":17000}"
                                            }
                                        ]
                                    },
                                    {
                                        "title": "Besok",
                                        "title_label": "13 Apr",
                                        "id": "2023-04-13",
                                        "available": true,
                                        "hidden": false,
                                        "error": {
                                            "error_id": "",
                                            "error_message": ""
                                        },
                                        "delivery_products": [
                                            {
                                                "title": "Tiba 00:00 - 02:00",
                                                "text": "",
                                                "text_eta": "Tiba besok, 00:00 - 02:00",
                                                "promo_text": "",
                                                "timeslot_id": 335,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "HkwFtXKjFbDHtGZcvnDK6LoChB4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":335,\"schedule_date\":\"2023-04-13T00:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 02:00 - 04:00",
                                                "text": "",
                                                "text_eta": "Tiba besok, 02:00 - 04:00",
                                                "promo_text": "",
                                                "timeslot_id": 336,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "HkwFtXKjFbDHtGZcvnDK6LoChB4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":336,\"schedule_date\":\"2023-04-13T02:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 04:00 - 06:00",
                                                "text": "",
                                                "text_eta": "Tiba besok, 04:00 - 06:00",
                                                "promo_text": "",
                                                "timeslot_id": 337,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "HkwFtXKjFbDHtGZcvnDK6LoChB4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":337,\"schedule_date\":\"2023-04-13T04:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 06:00 - 08:00",
                                                "text": "",
                                                "text_eta": "Tiba besok, 06:00 - 08:00",
                                                "promo_text": "",
                                                "timeslot_id": 338,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "HkwFtXKjFbDHtGZcvnDK6LoChB4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":338,\"schedule_date\":\"2023-04-13T06:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 08:00 - 10:00",
                                                "text": "",
                                                "text_eta": "Tiba besok, 08:00 - 10:00",
                                                "promo_text": "",
                                                "timeslot_id": 339,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "HkwFtXKjFbDHtGZcvnDK6LoChB4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":339,\"schedule_date\":\"2023-04-13T08:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 10:00 - 12:00",
                                                "text": "",
                                                "text_eta": "Tiba besok, 10:00 - 12:00",
                                                "promo_text": "",
                                                "timeslot_id": 340,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "HkwFtXKjFbDHtGZcvnDK6LoChB4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":340,\"schedule_date\":\"2023-04-13T10:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 12:00 - 14:00",
                                                "text": "",
                                                "text_eta": "Tiba besok, 12:00 - 14:00",
                                                "promo_text": "",
                                                "timeslot_id": 341,
                                                "available": true,
                                                "hidden": false,
                                                "recommend": true,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "HkwFtXKjFbDHtGZcvnDK6LoChB4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":341,\"schedule_date\":\"2023-04-13T12:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 14:00 - 16:00",
                                                "text": "",
                                                "text_eta": "Tiba besok, 14:00 - 16:00",
                                                "promo_text": "",
                                                "timeslot_id": 342,
                                                "available": true,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":342,\"schedule_date\":\"2023-04-13T14:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 16:00 - 18:00",
                                                "text": "",
                                                "text_eta": "Tiba besok, 16:00 - 18:00",
                                                "promo_text": "",
                                                "timeslot_id": 343,
                                                "available": true,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":343,\"schedule_date\":\"2023-04-13T16:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 18:00 - 20:00",
                                                "text": "",
                                                "text_eta": "Tiba besok, 18:00 - 20:00",
                                                "promo_text": "",
                                                "timeslot_id": 344,
                                                "available": true,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":344,\"schedule_date\":\"2023-04-13T18:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 20:00 - 22:00",
                                                "text": "",
                                                "text_eta": "Tiba besok, 20:00 - 22:00",
                                                "promo_text": "",
                                                "timeslot_id": 345,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":345,\"schedule_date\":\"2023-04-13T20:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 22:00 - 00:00",
                                                "text": "",
                                                "text_eta": "Tiba besok, 22:00 - 00:00",
                                                "promo_text": "",
                                                "timeslot_id": 346,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":346,\"schedule_date\":\"2023-04-13T22:00:00+07:00\",\"shipping_price\":17000}"
                                            }
                                        ]
                                    },
                                    {
                                        "title": "Lusa",
                                        "title_label": "14 Apr",
                                        "id": "2023-04-14",
                                        "available": true,
                                        "hidden": false,
                                        "error": {
                                            "error_id": "",
                                            "error_message": ""
                                        },
                                        "delivery_products": [
                                            {
                                                "title": "Tiba 00:00 - 02:00",
                                                "text": "",
                                                "text_eta": "Tiba 14 Apr, 00:00 - 02:00",
                                                "promo_text": "",
                                                "timeslot_id": 335,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":335,\"schedule_date\":\"2023-04-14T00:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 02:00 - 04:00",
                                                "text": "",
                                                "text_eta": "Tiba 14 Apr, 02:00 - 04:00",
                                                "promo_text": "",
                                                "timeslot_id": 336,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":336,\"schedule_date\":\"2023-04-14T02:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 04:00 - 06:00",
                                                "text": "",
                                                "text_eta": "Tiba 14 Apr, 04:00 - 06:00",
                                                "promo_text": "",
                                                "timeslot_id": 337,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":337,\"schedule_date\":\"2023-04-14T04:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 06:00 - 08:00",
                                                "text": "",
                                                "text_eta": "Tiba 14 Apr, 06:00 - 08:00",
                                                "promo_text": "",
                                                "timeslot_id": 338,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":338,\"schedule_date\":\"2023-04-14T06:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 08:00 - 10:00",
                                                "text": "Sisa 3 slot",
                                                "text_eta": "Tiba 14 Apr, 08:00 - 10:00",
                                                "promo_text": "",
                                                "timeslot_id": 339,
                                                "available": true,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":339,\"schedule_date\":\"2023-04-14T08:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 10:00 - 12:00",
                                                "text": "",
                                                "text_eta": "Tiba 14 Apr, 10:00 - 12:00",
                                                "promo_text": "",
                                                "timeslot_id": 340,
                                                "available": true,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":340,\"schedule_date\":\"2023-04-14T10:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 12:00 - 14:00",
                                                "text": "",
                                                "text_eta": "Tiba 14 Apr, 12:00 - 14:00",
                                                "promo_text": "",
                                                "timeslot_id": 341,
                                                "available": true,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":341,\"schedule_date\":\"2023-04-14T12:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 14:00 - 16:00",
                                                "text": "",
                                                "text_eta": "Tiba 14 Apr, 14:00 - 16:00",
                                                "promo_text": "",
                                                "timeslot_id": 342,
                                                "available": true,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":342,\"schedule_date\":\"2023-04-14T14:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 16:00 - 18:00",
                                                "text": "",
                                                "text_eta": "Tiba 14 Apr, 16:00 - 18:00",
                                                "promo_text": "",
                                                "timeslot_id": 343,
                                                "available": true,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":343,\"schedule_date\":\"2023-04-14T16:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 18:00 - 20:00",
                                                "text": "",
                                                "text_eta": "Tiba 14 Apr, 18:00 - 20:00",
                                                "promo_text": "",
                                                "timeslot_id": 344,
                                                "available": true,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":344,\"schedule_date\":\"2023-04-14T18:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 20:00 - 22:00",
                                                "text": "",
                                                "text_eta": "Tiba 14 Apr, 20:00 - 22:00",
                                                "promo_text": "",
                                                "timeslot_id": 345,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":345,\"schedule_date\":\"2023-04-14T20:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 22:00 - 00:00",
                                                "text": "",
                                                "text_eta": "Tiba 14 Apr, 22:00 - 00:00",
                                                "promo_text": "",
                                                "timeslot_id": 346,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":346,\"schedule_date\":\"2023-04-14T22:00:00+07:00\",\"shipping_price\":17000}"
                                            }
                                        ]
                                    },
                                    {
                                        "title": "Sabtu",
                                        "title_label": "15 Apr",
                                        "id": "2023-04-15",
                                        "available": true,
                                        "hidden": false,
                                        "error": {
                                            "error_id": "",
                                            "error_message": ""
                                        },
                                        "delivery_products": [
                                            {
                                                "title": "Tiba 00:00 - 02:00",
                                                "text": "",
                                                "text_eta": "Tiba 15 Apr, 00:00 - 02:00",
                                                "promo_text": "",
                                                "timeslot_id": 335,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":335,\"schedule_date\":\"2023-04-15T00:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 02:00 - 04:00",
                                                "text": "",
                                                "text_eta": "Tiba 15 Apr, 02:00 - 04:00",
                                                "promo_text": "",
                                                "timeslot_id": 336,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":336,\"schedule_date\":\"2023-04-15T02:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 04:00 - 06:00",
                                                "text": "",
                                                "text_eta": "Tiba 15 Apr, 04:00 - 06:00",
                                                "promo_text": "",
                                                "timeslot_id": 337,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":337,\"schedule_date\":\"2023-04-15T04:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 06:00 - 08:00",
                                                "text": "",
                                                "text_eta": "Tiba 15 Apr, 06:00 - 08:00",
                                                "promo_text": "",
                                                "timeslot_id": 338,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":338,\"schedule_date\":\"2023-04-15T06:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 08:00 - 10:00",
                                                "text": "",
                                                "text_eta": "Tiba 15 Apr, 08:00 - 10:00",
                                                "promo_text": "",
                                                "timeslot_id": 339,
                                                "available": true,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":339,\"schedule_date\":\"2023-04-15T08:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 10:00 - 12:00",
                                                "text": "",
                                                "text_eta": "Tiba 15 Apr, 10:00 - 12:00",
                                                "promo_text": "",
                                                "timeslot_id": 340,
                                                "available": true,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":340,\"schedule_date\":\"2023-04-15T10:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 12:00 - 14:00",
                                                "text": "",
                                                "text_eta": "Tiba 15 Apr, 12:00 - 14:00",
                                                "promo_text": "",
                                                "timeslot_id": 341,
                                                "available": true,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":341,\"schedule_date\":\"2023-04-15T12:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 14:00 - 16:00",
                                                "text": "",
                                                "text_eta": "Tiba 15 Apr, 14:00 - 16:00",
                                                "promo_text": "",
                                                "timeslot_id": 342,
                                                "available": true,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":342,\"schedule_date\":\"2023-04-15T14:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 16:00 - 18:00",
                                                "text": "",
                                                "text_eta": "Tiba 15 Apr, 16:00 - 18:00",
                                                "promo_text": "",
                                                "timeslot_id": 343,
                                                "available": true,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":343,\"schedule_date\":\"2023-04-15T16:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 18:00 - 20:00",
                                                "text": "",
                                                "text_eta": "Tiba 15 Apr, 18:00 - 20:00",
                                                "promo_text": "",
                                                "timeslot_id": 344,
                                                "available": true,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":344,\"schedule_date\":\"2023-04-15T18:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 20:00 - 22:00",
                                                "text": "",
                                                "text_eta": "Tiba 15 Apr, 20:00 - 22:00",
                                                "promo_text": "",
                                                "timeslot_id": 345,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":345,\"schedule_date\":\"2023-04-15T20:00:00+07:00\",\"shipping_price\":17000}"
                                            },
                                            {
                                                "title": "Tiba 22:00 - 00:00",
                                                "text": "",
                                                "text_eta": "Tiba 15 Apr, 22:00 - 00:00",
                                                "promo_text": "",
                                                "timeslot_id": 346,
                                                "available": false,
                                                "hidden": false,
                                                "recommend": false,
                                                "service_id": "1000",
                                                "service_name": "instant",
                                                "shipper_id": 29,
                                                "shipper_name": "GTL",
                                                "shipper_product_id": 58,
                                                "shipper_product_name": "Instant",
                                                "final_price": 0,
                                                "real_price": 17000,
                                                "text_final_price": "Rp0",
                                                "text_real_price": "Rp17.000",
                                                "checksum": "2HdmRJYAG0yENdxC7MUcE2qScH4%3D",
                                                "insurance": {
                                                    "insurance_type": 3,
                                                    "insurance_price": 0,
                                                    "insurance_type_info": "Must insurance",
                                                    "insurance_used_type": 2,
                                                    "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, <b>gratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.</b>",
                                                    "insurance_used_default": 2
                                                },
                                                "features": {
                                                    "ontime_delivery_guarantee": {
                                                        "available": false,
                                                        "value": 0,
                                                        "text_label": "",
                                                        "text_detail": "",
                                                        "icon_url": "",
                                                        "url_detail": ""
                                                    }
                                                },
                                                "ut": "1681311106",
                                                "promo_stacking": {
                                                    "promo_code": "BONOWSELLY",
                                                    "promo_chargeable": false,
                                                    "benefit_class": "scheduled",
                                                    "is_bebas_ongkir_extra": false,
                                                    "benefit_amount": 75000,
                                                    "bo_campaign_id": 191,
                                                    "disabled": false,
                                                    "free_shipping_metadata": {
                                                        "sent_shipper_partner": false,
                                                        "benefit_class": "scheduled",
                                                        "shipping_subsidy": 75000,
                                                        "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
                                                    }
                                                },
                                                "validation_metadata": "{\"timeslot_id\":346,\"schedule_date\":\"2023-04-15T22:00:00+07:00\",\"shipping_price\":17000}"
                                            }
                                        ]
                                    }
                                ]
                            }
                        }
                    }
        """
        ).map {
            Gson().fromJson(it, ScheduleDeliveryRatesResponse::class.java)
        }
//        return gql.getExecuteObservable(null)
//            .map { graphqlResponse: GraphqlResponse ->
//                val response: ScheduleDeliveryRatesResponse =
//                    graphqlResponse.getData<ScheduleDeliveryRatesResponse>(
//                        ScheduleDeliveryRatesResponse::class.java
//                    ) ?: throw MessageErrorException(DEFAULT_ERROR_MESSAGE)
//                response
//            }
//            .subscribeOn(scheduler.io())
//            .observeOn(scheduler.ui())
    }

    fun unsubscribe() {
        gql?.unsubscribe()
    }
}
