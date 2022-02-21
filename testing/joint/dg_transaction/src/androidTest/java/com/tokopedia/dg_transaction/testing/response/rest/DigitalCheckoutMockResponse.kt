package com.tokopedia.dg_transaction.testing.response.rest

import com.google.gson.reflect.TypeToken
import com.tokopedia.common_digital.cart.data.entity.response.AttributesCheckout
import com.tokopedia.common_digital.cart.data.entity.response.Parameter
import com.tokopedia.digital_checkout.data.response.ResponseCheckout
import com.tokopedia.network.data.model.response.DataResponse
import java.lang.reflect.Type

class DigitalCheckoutMockResponse: MockRestResponse<ResponseCheckout>() {

    override fun getToken(): Type =
        object : TypeToken<DataResponse<ResponseCheckout>>() {}.type

    override fun getResponse(): ResponseCheckout {
        return ResponseCheckout(
            id = "1902418153",
            type = "scrooge_parameter",
            attributes = AttributesCheckout(
                callbackUrlFailed = "https://pulsa.tokopedia.com/checkout",
                callbackUrlSuccess = "https://pulsa.tokopedia.com/checkout/back-to-app",
                parameter = Parameter().apply {
                    amount = "22500"
                    currency = "IDR"
                    customerEmail = "android.automation.seller.h5+frontendtest@tokopedia.com"
                    customerName = "Hefdy Wiguna"
                    fee = ""
                    itemsName = listOf("578 - Rp 20.000 (6032984011276619)")
                    itemsPrice = listOf(22500)
                    itemsQuantity = listOf(1)
                    language = "id-ID"
                    merchantCode = "tokopediapulsa"
                    nid = ""
                    profileCode = "PULSA_ETOLL"
                    signature = "d04db6e5a0a5ae55dfc1a423d68815d41aa625ea"
                    transactionCode = ""
                    transactionDate = "2021-10-26T03:06:08Z"
                    transactionId = "1902418153"
                    userDefinedValue =
                        "{\"user_id\":17211378,\"voucher_code\":\"\",\"device\":3,\"product_id\":2069,\"price\":22500,\"client_number\":\"6032984011276619\",\"payment_description\":\"\",\"promo_code_id\":0,\"discount_amount\":0,\"cashback_amount\":0,\"cashback_voucher_amount\":0,\"cashback_top_cash_amount\":0,\"va_code\":\"081908878591\",\"fingerprint_id\":\"\",\"hide_header_flag\":true,\"category_code\":\"34\",\"product_code\":\"man20k\",\"is_mitra\":false,\"order_type\":8,\"coda_amount\":0,\"xshop_flag\":0,\"api_key\":\"\",\"is_stacking\":false}"
                },
                queryString = "Items=%7B0+578+-+Rp+20.000+%286032984011276619%29+1+22500%7D&SubscriptionToken=&SubscriptionUUID=&ValidationType=&additional_fee=&amount=22500&currency=IDR&customer_email=android.automation.seller.h5%2Bfrontendtest%40tokopedia.com&customer_msisdn=6281908878591&customer_name=Hefdy+Wiguna&expired_on=&fee=&items%5Bname%5D=578+-+Rp+20.000+%286032984011276619%29&items%5Bprice%5D=22500&items%5Bquantity%5D=1&language=id-ID&merchant_code=tokopediapulsa&nid=&payment_identifier=&payment_metadata=%7B%22user_data%22%3A%7B%22device_id%22%3A%22ch41iuBKTQOfFW-obndScn%3AAPA91bHc57PES9OE30LJvPdyFdaHozgrr0M_BFSruJgxAffdHQBhLl9MnwZQXZt0aLTVBdoVkFNYUb1E-jLx3ClYWu1J627FsWfDP6c3a4aIlQAeMLsG9vZTx6TZ-mHd50yEPEw_ZAWQ%22%2C%22device_version%22%3A%223.148%22%2C%22device_type%22%3A%22android%22%2C%22dg_loyalty_status%22%3A%22%22%7D%2C%22order_data%22%3A%7B%22order_data%22%3A%5B%7B%22order_id%22%3A%22904275664%22%2C%22order_code%22%3A%22IVR%2F20211026%2FXXI%2FX%2F904311479%22%2C%22product_code%22%3A%22emoney%22%2C%22invoice_url%22%3A%22https%3A%2F%2Fpulsa.tokopedia.com%2Finvoice%2F%3Fpdf%3DInvoice-Recharge-17211378-2069-20211026100608-MjyXrrErjUSH.pdf%5Cu0026id%3D904275664%22%2C%22product_data%22%3A%5B%7B%22operator_id%22%3A%22578%22%2C%22product_id%22%3A%222069%22%2C%22product_name%22%3A%22Mandiri+E-Money+Rp+20.000%22%2C%22product_code%22%3A%22man20k%22%2C%22price%22%3A22500%2C%22quantity%22%3A1%2C%22campaign_id%22%3A%22%22%2C%22client_number%22%3A%226032984011276619%22%2C%22identity_number%22%3A%22%22%2C%22is_product_pre_order%22%3Afalse%2C%22product_category%22%3A%7B%22id%22%3A34%2C%22name%22%3A%22Uang+Elektronik%22%2C%22identifier%22%3A%22Top+Up+Uang+Elektronik+melalui+Tokopedia%22%2C%22parent%22%3A0%7D%7D%5D%2C%22expire_time%22%3A%220001-01-01T00%3A00%3A00Z%22%7D%5D%7D%2C%22promo_data%22%3A%7B%22voucher_code%22%3A%22%22%2C%22promo_code_id%22%3A0%2C%22payment_description%22%3A%22%22%2C%22subsidized_amount%22%3A0%2C%22total_discount%22%3A0%2C%22total_cashback%22%3A0%2C%22is_stacking%22%3Afalse%7D%2C%22config_data%22%3A%7B%22hide_header_flag%22%3Afalse%2C%22device%22%3A0%2C%22is_verified_msisdn%22%3Atrue%2C%22va_code%22%3A%22%22%2C%22thanks_data%22%3A%7B%22custom_order_url%22%3A%22https%3A%2F%2Fm.tokopedia.com%2Forder-list%2F%3Ftab%3Ddigital%5Cu0026backBtn%3Dfalse%22%2C%22custom_order_url_app%22%3A%22tokopedia%3A%2F%2Fdigital%2Forder%22%2C%22custom_home_url%22%3A%22tokopedia%3A%2F%2Fdigital%2Fsmartcard%3Fidem_potency_key%3D1%22%2C%22custom_home_url_app%22%3A%22tokopedia%3A%2F%2Fdigital%2Fsmartcard%3Fidem_potency_key%3D1%22%2C%22custom_title%22%3A%22Selamat%2C+pembayaran+Mandiri+E-Money+Rp+20.000+berhasil%21%22%2C%22custom_subtitle%22%3A%22Kamu+bisa+cek+status+transaksimu+di+Detail+Transaksi%2C+ya.%22%2C%22custom_title_order_button%22%3A%22Lihat+Daftar+Transaksi%22%2C%22custom_wtv_text%22%3A%22Transaksimu+baru+dilanjutkan+setelah+pembayaran+terverifikasi.%22%2C%22custom_title_home_button%22%3A%22Update+Saldo%22%2C%22tracking_data%22%3A%22%5B%7B%5C%22event%5C%22%3A+%5C%22transaction%5C%22%2C%5C%22eventCategory%5C%22%3A+%5C%22digital+-+thanks%5C%22%2C%5C%22eventAction%5C%22%3A+%5C%22view+purchase+attempt%5C%22%2C%5C%22eventLabel%5C%22%3A+%5C%22Uang+Elektronik+-+Mandiri+E-Money+-+Waiting+Payment%5C%22%2C%5C%22currentSite%5C%22%3A+%5C%22tokopediadigital%5C%22%2C%5C%22environment%5C%22%3A+%5C%22android%5C%22%2C%5C%22loginStatus%5C%22%3A+%5C%221%5C%22%2C%5C%22userId%5C%22%3A+%5C%2217211378%5C%22%2C%5C%22siteName%5C%22%3A+%5C%22Recharge+Site%5C%22%2C%5C%22businessUnit%5C%22%3A+%5C%22payment%5C%22%2C%5C%22payment_id%5C%22%3A+%5C%221902418153%5C%22%2C%5C%22ecommerce%5C%22%3A+%7B%5C%22currencyCode%5C%22%3A+%5C%22IDR%5C%22%2C%5C%22purchase%5C%22%3A+%7B%5C%22actionField%5C%22%3A+%7B%5C%22id%5C%22%3A+%5C%22904275664%5C%22%2C%5C%22revenue%5C%22%3A+%5C%2222500.00%5C%22%2C%5C%22tax%5C%22%3A+%5C%22%5C%22%2C%5C%22coupon%5C%22%3A+%5C%22%5C%22%7D%2C%5C%22products%5C%22%3A+%5B%7B%5C%22name%5C%22%3A+%5C%22Mandiri+E-Money+Rp+20.000%5C%22%2C%5C%22id%5C%22%3A+%5C%222069%5C%22%2C%5C%22price%5C%22%3A+%5C%2222500.00%5C%22%2C%5C%22quantity%5C%22%3A+%5C%221%5C%22%7D%5D%7D%7D%2C%5C%22categoryId%5C%22%3A+%5C%2234%5C%22%2C%5C%22categoryName%5C%22%3A+%5C%22Uang+Elektronik%5C%22%2C%5C%22operator%5C%22%3A+%5C%22Mandiri+E-Money%5C%22%2C%5C%22profileId%5C%22%3A%5C%22PULSA_ETOLL%5C%22%7D%5D%22%2C%22thank_you_page_token%22%3A%22gFbe9sxxdvqwwMzZT-hTLzGo6xUIJzThIy0IN2ZsXZ4sTpzKafniDTezPZGXtvonP9dm7CDBlnZlajt-3JUaBQ%22%2C%22hide_search_bar%22%3Afalse%2C%22hide_global_menu%22%3Afalse%2C%22hide_feature_recom%22%3Afalse%2C%22hide_pg_recom%22%3Afalse%2C%22hide_dg_recom%22%3Afalse%7D%7D%7D&pid=18889461427784454758975&profile_code=PULSA_ETOLL&signature=d04db6e5a0a5ae55dfc1a423d68815d41aa625ea&state=0&transaction_code=&transaction_date=2021-10-26T03%3A06%3A08Z&transaction_id=1902418153&user_defined_value=%7B%22user_id%22%3A17211378%2C%22voucher_code%22%3A%22%22%2C%22device%22%3A3%2C%22product_id%22%3A2069%2C%22price%22%3A22500%2C%22client_number%22%3A%226032984011276619%22%2C%22payment_description%22%3A%22%22%2C%22promo_code_id%22%3A0%2C%22discount_amount%22%3A0%2C%22cashback_amount%22%3A0%2C%22cashback_voucher_amount%22%3A0%2C%22cashback_top_cash_amount%22%3A0%2C%22va_code%22%3A%22081908878591%22%2C%22fingerprint_id%22%3A%22%22%2C%22hide_header_flag%22%3Atrue%2C%22category_code%22%3A%2234%22%2C%22product_code%22%3A%22man20k%22%2C%22is_mitra%22%3Afalse%2C%22order_type%22%3A8%2C%22coda_amount%22%3A0%2C%22xshop_flag%22%3A0%2C%22api_key%22%3A%22%22%2C%22is_stacking%22%3Afalse%7D",
                redirectUrl = "https://pay.tokopedia.com/v2/payment",
                thanksUrl = ""
            )
        )
    }
}