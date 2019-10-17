package com.tokopedia.purchase_platform.features.cart.data.repository

import com.google.gson.Gson
import com.tokopedia.purchase_platform.features.cart.data.api.CartApi
import com.tokopedia.purchase_platform.features.cart.data.model.response.CartDataListResponse
import com.tokopedia.purchase_platform.features.cart.data.model.response.DeleteCartDataResponse
import com.tokopedia.purchase_platform.features.cart.data.model.response.UpdateCartDataResponse
import rx.Observable
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

class CartRepository @Inject constructor(private val cartApi: CartApi) : ICartRepository {

    override fun getShopGroupList(param: Map<String, String>): Observable<CartDataListResponse> {
        return Observable.just(Gson().fromJson(mockCartResponse, CartDataListResponse::class.java))
//        return cartApi.getShopGroupList(param).map { cartResponseResponse ->
//            cartResponseResponse.body()?.convertDataObj(CartDataListResponse::class.java)
//        }
    }

    override fun deleteCartData(param: Map<String, String>): Observable<DeleteCartDataResponse> {
        return cartApi.postDeleteCart(param).map { cartResponseResponse ->
            cartResponseResponse.body()?.convertDataObj(DeleteCartDataResponse::class.java)
        }
    }

    override fun updateCartData(param: Map<String, String>): Observable<UpdateCartDataResponse> {
        return cartApi.postUpdateCart(param).map { cartResponseResponse ->
            cartResponseResponse.body()?.convertDataObj(UpdateCartDataResponse::class.java)
        }
    }

    val mockCartResponse = "{\n" +
            "\"errors\":[\n" +
            "],\n" +
            "\"is_coupon_active\":0,\n" +
            "\"is_one_tab_promo\":false,\n" +
            "\"max_quantity\":0,\n" +
            "\"max_char_note\":0,\n" +
            "\"messages\": {\n" +
            "     \"ErrorFieldBetween\": \"Jumlah harus diisi antara 1 - {{value}}\",\n" +
            "     \"ErrorFieldMaxChar\": \"Catatan terlalu panjang, maks. {{value}} karakter.\",\n" +
            "     \"ErrorFieldRequired\": \"Jumlah harus diisi\",\n" +
            "     \"ErrorProductAvailableStock\": \"Stok tersedia:{{value}}\",\n" +
            "     \"ErrorProductAvailableStockDetail\": \"Harap kurangi jumlah barang\",\n" +
            "     \"ErrorProductMaxQuantity\": \"Maks. pembelian barang ini {{value}} item, Kurangi pembelianmu, ya!\",\n" +
            "     \"ErrorProductMinQuantity\": \"Min. pembelian produk ini {{value}} barang. Yuk, atur ulang pembelianmu.\"\n" +
            "   },\"promo_suggestion\":{\n" +
            "\"cta\":\"\",\n" +
            "\"cta_color\":\"\",\n" +
            "\"is_visible\":0,\n" +
            "\"promo_code\":\"\",\n" +
            "\"text\":\"\"\n" +
            "},\n" +
            "\"shop_group_available\":[\n" +
            "{\n" +
            "\"user_address_id\":0,\n" +
            "\"shop\":{\n" +
            "\"shop_id\":479986,\n" +
            "\"user_id\":5512189,\n" +
            "\"admin_ids\":[\n" +
            "],\n" +
            "\"shop_name\":\"Zelda OS Testing 01\",\n" +
            "\"shop_image\":\"https://ecs7.tokopedia.net/img/default_v3-shopnophoto.png\",\n" +
            "\"shop_url\":\"https://staging.tokopedia.com/zos1\",\n" +
            "\"shop_status\":1,\n" +
            "\"is_gold\":0,\n" +
            "\"is_gold_badge\":false,\n" +
            "\"is_official\":0,\n" +
            "\"is_free_returns\":0,\n" +
            "\"gold_merchant\":{\n" +
            "\"is_gold\":0,\n" +
            "\"is_gold_badge\":false,\n" +
            "\"gold_merchant_logo_url\":\"\"\n" +
            "},\n" +
            "\"official_store\":{\n" +
            "\"is_official\":0,\n" +
            "\"os_logo_url\":\"\"\n" +
            "},\n" +
            "\"address_id\":2270,\n" +
            "\"postal_code\":\"12930\",\n" +
            "\"latitude\":\"-6.221180109754967\",\n" +
            "\"longitude\":\"106.81955066523437\",\n" +
            "\"district_id\":2270,\n" +
            "\"district_name\":\"Setiabudi\",\n" +
            "\"origin\":2270,\n" +
            "\"address_street\":\"Jalan Karet Sawah, Kecamatan Setiabudi, 12930\",\n" +
            "\"province_id\":13,\n" +
            "\"city_id\":175,\n" +
            "\"city_name\":\"Jakarta Selatan\",\n" +
            "\"province_name\":\"DKI Jakarta\",\n" +
            "\"country_name\":\"Indonesia\",\n" +
            "\"is_allow_manage\":false,\n" +
            "\"shop_domain\":\"zos1\"\n" +
            "},\n" +
            "\"cart_string\":\"479986-0-1886\",\n" +
            "\"cart_details\":[\n" +
            "{\n" +
            "\"cart_id\":33376874,\n" +
            "\"product\":{\n" +
            "\"product_id\":15262849,\n" +
            "\"product_name\":\"Test vdoang\",\n" +
            "\"product_alias\":\"test-vdoang\",\n" +
            "\"parent_id\":0,\n" +
            "\"variant\":{\n" +
            "\"parent_id\":0,\n" +
            "\"is_parent\":false,\n" +
            "\"is_variant\":false,\n" +
            "\"children_id\":1\n" +
            "},\n" +
            "\"sku\":\"\",\n" +
            "\"campaign_id\":0,\n" +
            "\"is_big_campaign\":false,\n" +
            "\"product_price_fmt\":\"Rp10.000\",\n" +
            "\"product_price\":10000,\n" +
            "\"product_original_price\":0,\n" +
            "\"product_price_original_fmt\":\"\",\n" +
            "\"is_slash_price\":false,\n" +
            "\"category_id\":636,\n" +
            "\"category\":\"Elektronik / Tool & Kit\",\n" +
            "\"catalog_id\":0,\n" +
            "\"wholesale_price\":[\n" +
            "],\n" +
            "\"product_weight_fmt\":\"1gr\",\n" +
            "\"product_condition\":1,\n" +
            "\"product_status\":3,\n" +
            "\"product_url\":\"https://staging.tokopedia.com//test-vdoang\",\n" +
            "\"product_returnable\":0,\n" +
            "\"is_freereturns\":0,\n" +
            "\"free_returns\":{\n" +
            "\"free_returns_logo\":\"https://ecs7.tokopedia.net/img/icon-frs.png\"\n" +
            "},\n" +
            "\"is_preorder\":0,\n" +
            "\"product_cashback\":\"\",\n" +
            "\"product_cashback_value\":0,\n" +
            "\"product_min_order\":1,\n" +
            "\"product_max_order\":10000,\n" +
            "\"product_rating\":0,\n" +
            "\"product_invenage_value\":2,\n" +
            "\"product_switch_invenage\":1,\n" +
            "\"product_invenage_total\":{\n" +
            "\"by_user\":{\n" +
            "\"in_cart\":2,\n" +
            "\"last_stock_less_than\":5\n" +
            "},\n" +
            "\"by_user_text\":{\n" +
            "\"in_cart\":\"sudah berada di keranjang 2 pembeli lain\",\n" +
            "\"last_stock_less_than\":\"<b>Stock hampir habis!</b> tersisa < 5\",\n" +
            "\"complete\":\"<b>Stock hampir habis!</b> tersisa < 5 dan sudah berada di keranjang 2 pembeli lain\"\n" +
            "},\n" +
            "\"is_counted_by_user\":true,\n" +
            "\"by_product\":{\n" +
            "\"in_cart\":0,\n" +
            "\"last_stock_less_than\":5\n" +
            "},\n" +
            "\"by_product_text\":{\n" +
            "\"in_cart\":\"\",\n" +
            "\"last_stock_less_than\":\"\",\n" +
            "\"complete\":\"\"\n" +
            "},\n" +
            "\"is_counted_by_product\":false\n" +
            "},\n" +
            "\"currency_rate\":1,\n" +
            "\"product_price_currency\":1,\n" +
            "\"product_image\":{\n" +
            "\"image_src\":\"https://cdn-staging.tokopedia.com/img/cache/700/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919\",\n" +
            "\"image_src_200_square\":\"https://cdn-staging.tokopedia.com/img/cache/200-square/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919\",\n" +
            "\"image_src_300\":\"https://cdn-staging.tokopedia.com/img/cache/300/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919\",\n" +
            "\"image_src_square\":\"https://cdn-staging.tokopedia.com/img/cache/500-square/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919\"\n" +
            "},\n" +
            "\"product_all_images\":\"[{\\\"file_path\\\":\\\"product-1/2019/9/26/5510908\\\",\\\"file_name\\\":\\\"5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919\\\",\\\"status\\\":2},{\\\"file_path\\\":\\\"product-1/2019/9/26/5510908\\\",\\\"file_name\\\":\\\"5510908_417e3582-9671-46b4-af26-2c61d0051444_1919_1919\\\",\\\"status\\\":1},{\\\"file_path\\\":\\\"product-1/2019/9/26/5510908\\\",\\\"file_name\\\":\\\"5510908_8854f7d1-ab75-4b9c-ba15-0a88b6ae27b1_1919_1919\\\",\\\"status\\\":1},{\\\"file_path\\\":\\\"product-1/2019/9/26/5510908\\\",\\\"file_name\\\":\\\"5510908_345508de-2bfe-4bbd-90f6-7ce86f436f41_1919_1919\\\",\\\"status\\\":1},{\\\"file_path\\\":\\\"product-1/2019/9/26/5510908\\\",\\\"file_name\\\":\\\"5510908_33a77436-9623-4ceb-93d6-7af16a035eb6_1919_1919\\\",\\\"status\\\":1}]\",\n" +
            "\"product_notes\":\"\",\n" +
            "\"product_quantity\":7,\n" +
            "\"price_changes\":{\n" +
            "\"changes_state\":0,\n" +
            "\"amount_difference\":0,\n" +
            "\"original_amount\":0,\n" +
            "\"description\":\"\"\n" +
            "},\n" +
            "\"product_weight\":1,\n" +
            "\"product_weight_unit_code\":1,\n" +
            "\"product_weight_unit_text\":\"gr\",\n" +
            "\"last_update_price\":1569480222,\n" +
            "\"is_update_price\":false,\n" +
            "\"product_preorder\":{\n" +
            "},\n" +
            "\"product_showcase\":{\n" +
            "\"name\":\"testing\",\n" +
            "\"id\":1405133\n" +
            "},\n" +
            "\"product_finsurance\":1,\n" +
            "\"product_shop_id\":480829,\n" +
            "\"is_wishlisted\":false,\n" +
            "\"product_tracker_data\":{\n" +
            "\"attribution\":\"none/other\",\n" +
            "\"tracker_list_name\":\"none/other\"\n" +
            "},\n" +
            "\"is_ppp\":false,\n" +
            "\"is_cod\":false,\n" +
            "\"warehouse_id\":96,\n" +
            "\"warehouses\":{\n" +
            "\"96\":{\n" +
            "\"warehouseID\":96,\n" +
            "\"product_price\":10000,\n" +
            "\"price_currency\":1,\n" +
            "\"price_currency_name\":\"IDR\",\n" +
            "\"product_price_idr\":10000,\n" +
            "\"last_update_price\":{\n" +
            "\"unix\":1569505422,\n" +
            "\"yyyymmddhhmmss\":\"\"\n" +
            "},\n" +
            "\"product_switch_invenage\":1,\n" +
            "\"product_invenage_value\":2\n" +
            "}\n" +
            "},\n" +
            "\"is_parent\":false,\n" +
            "\"is_campaign_error\":false,\n" +
            "\"campaign_type_name\":\"\",\n" +
            "\"hide_gimmick\":false,\n" +
            "\"is_blacklisted\":false,\n" +
            "\"categories\":[\n" +
            "{\n" +
            "\"category_id\":60,\n" +
            "\"category_name\":\"Elektronik\"\n" +
            "},\n" +
            "{\n" +
            "\"category_id\":636,\n" +
            "\"category_name\":\"Tool & Kit\"\n" +
            "}\n" +
            "],\n" +
            "\"free_shipping\":{\n" +
            "\"eligible\":false,\n" +
            "\"badge_url\":\"\"\n" +
            "}\n" +
            "},\n" +
            "\"errors\":[\n" +
            "],\n" +
            "\"messages\":[\n" +
            "],\n" +
            "\"checkbox_state\":true,\n" +
            "\"similar_product_url\":\"tokopedia://rekomendasi/15262849?ref=cart\",\n" +
            "\"similar_product\":{\n" +
            "\"text\":\"Lihat Produk Serupa\",\n" +
            "\"url\":\"tokopedia://rekomendasi/15262849?ref=cart\"\n" +
            "}\n" +
            "}\n" +
            "],\n" +
            "\"total_cart_details_error\":1,\n" +
            "\"total_cart_price\":2400000,\n" +
            "\"errors\":[],\n" +
            "\"sort_key\":33388925,\n" +
            "\"is_fulfillment_service\":false,\n" +
            "\"warehouse\":{\n" +
            "\"warehouse_id\":1886,\n" +
            "\"partner_id\":0,\n" +
            "\"shop_id\":479986,\n" +
            "\"warehouse_name\":\"Shop location\",\n" +
            "\"district_id\":2270,\n" +
            "\"district_name\":\"Setiabudi\",\n" +
            "\"city_id\":175,\n" +
            "\"city_name\":\"Jakarta Selatan\",\n" +
            "\"province_id\":13,\n" +
            "\"province_name\":\"DKI Jakarta\",\n" +
            "\"status\":1,\n" +
            "\"postal_code\":\"12930\",\n" +
            "\"is_default\":1,\n" +
            "\"latlon\":\"-6.221180109754967,106.81955066523437\",\n" +
            "\"latitude\":\"-6.221180109754967\",\n" +
            "\"longitude\":\"106.81955066523437\",\n" +
            "\"email\":\"\",\n" +
            "\"address_detail\":\"Jalan Karet Sawah, Kecamatan Setiabudi, 12930\",\n" +
            "\"country_name\":\"Indonesia\",\n" +
            "\"is_fulfillment\":false,\n" +
            "\"tkpd_preferred_logistic_spid\":[\n" +
            "]\n" +
            "},\n" +
            "\"has_promo_list\":false,\n" +
            "\"checkbox_state\":true\n" +
            "}\n" +
            "],\n" +
            "\"shop_group_with_errors\":[\n" +
            "{\n" +
            "\"user_address_id\":0,\n" +
            "\"shop\":{\n" +
            "\"shop_id\":480829,\n" +
            "\"user_id\":5510908,\n" +
            "\"admin_ids\":[\n" +
            "],\n" +
            "\"shop_name\":\"mattleeshoppe\",\n" +
            "\"shop_image\":\"https://ecs7.tokopedia.net/img/default_v3-shopnophoto.png\",\n" +
            "\"shop_url\":\"https://staging.tokopedia.com/mattleeshoppe\",\n" +
            "\"shop_status\":1,\n" +
            "\"is_gold\":0,\n" +
            "\"is_gold_badge\":false,\n" +
            "\"is_official\":0,\n" +
            "\"is_free_returns\":0,\n" +
            "\"gold_merchant\":{\n" +
            "\"is_gold\":0,\n" +
            "\"is_gold_badge\":false,\n" +
            "\"gold_merchant_logo_url\":\"\"\n" +
            "},\n" +
            "\"official_store\":{\n" +
            "\"is_official\":0,\n" +
            "\"os_logo_url\":\"\"\n" +
            "},\n" +
            "\"address_id\":1,\n" +
            "\"postal_code\":\"123456\",\n" +
            "\"latitude\":\"123456\",\n" +
            "\"longitude\":\"7890\",\n" +
            "\"district_id\":1,\n" +
            "\"district_name\":\"Kaway XVI\",\n" +
            "\"origin\":1,\n" +
            "\"address_street\":\"Alamat ini milik Dragon\",\n" +
            "\"province_id\":1,\n" +
            "\"city_id\":1,\n" +
            "\"city_name\":\"Kab. Aceh Barat\",\n" +
            "\"province_name\":\"D.I. Aceh\",\n" +
            "\"country_name\":\"Indonesia\",\n" +
            "\"is_allow_manage\":false,\n" +
            "\"shop_domain\":\"mattleeshoppe\"\n" +
            "},\n" +
            "\"cart_string\":\"480829-0-96\",\n" +
            "\"cart_details\":[\n" +
            "{\n" +
            "\"cart_id\":33376874,\n" +
            "\"product\":{\n" +
            "\"product_id\":15262849,\n" +
            "\"product_name\":\"Test vdoang\",\n" +
            "\"product_alias\":\"test-vdoang\",\n" +
            "\"parent_id\":0,\n" +
            "\"variant\":{\n" +
            "\"parent_id\":0,\n" +
            "\"is_parent\":false,\n" +
            "\"is_variant\":false,\n" +
            "\"children_id\":1\n" +
            "},\n" +
            "\"sku\":\"\",\n" +
            "\"campaign_id\":0,\n" +
            "\"is_big_campaign\":false,\n" +
            "\"product_price_fmt\":\"Rp10.000\",\n" +
            "\"product_price\":10000,\n" +
            "\"product_original_price\":0,\n" +
            "\"product_price_original_fmt\":\"\",\n" +
            "\"is_slash_price\":false,\n" +
            "\"category_id\":636,\n" +
            "\"category\":\"Elektronik / Tool & Kit\",\n" +
            "\"catalog_id\":0,\n" +
            "\"wholesale_price\":[\n" +
            "],\n" +
            "\"product_weight_fmt\":\"1gr\",\n" +
            "\"product_condition\":1,\n" +
            "\"product_status\":3,\n" +
            "\"product_url\":\"https://staging.tokopedia.com//test-vdoang\",\n" +
            "\"product_returnable\":0,\n" +
            "\"is_freereturns\":0,\n" +
            "\"free_returns\":{\n" +
            "\"free_returns_logo\":\"https://ecs7.tokopedia.net/img/icon-frs.png\"\n" +
            "},\n" +
            "\"is_preorder\":0,\n" +
            "\"product_cashback\":\"\",\n" +
            "\"product_cashback_value\":0,\n" +
            "\"product_min_order\":1,\n" +
            "\"product_max_order\":10000,\n" +
            "\"product_rating\":0,\n" +
            "\"product_invenage_value\":2,\n" +
            "\"product_switch_invenage\":1,\n" +
            "\"product_invenage_total\":{\n" +
            "\"by_user\":{\n" +
            "\"in_cart\":2,\n" +
            "\"last_stock_less_than\":5\n" +
            "},\n" +
            "\"by_user_text\":{\n" +
            "\"in_cart\":\"sudah berada di keranjang 2 pembeli lain\",\n" +
            "\"last_stock_less_than\":\"<b>Stock hampir habis!</b> tersisa < 5\",\n" +
            "\"complete\":\"<b>Stock hampir habis!</b> tersisa < 5 dan sudah berada di keranjang 2 pembeli lain\"\n" +
            "},\n" +
            "\"is_counted_by_user\":true,\n" +
            "\"by_product\":{\n" +
            "\"in_cart\":0,\n" +
            "\"last_stock_less_than\":5\n" +
            "},\n" +
            "\"by_product_text\":{\n" +
            "\"in_cart\":\"\",\n" +
            "\"last_stock_less_than\":\"\",\n" +
            "\"complete\":\"\"\n" +
            "},\n" +
            "\"is_counted_by_product\":false\n" +
            "},\n" +
            "\"currency_rate\":1,\n" +
            "\"product_price_currency\":1,\n" +
            "\"product_image\":{\n" +
            "\"image_src\":\"https://cdn-staging.tokopedia.com/img/cache/700/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919\",\n" +
            "\"image_src_200_square\":\"https://cdn-staging.tokopedia.com/img/cache/200-square/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919\",\n" +
            "\"image_src_300\":\"https://cdn-staging.tokopedia.com/img/cache/300/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919\",\n" +
            "\"image_src_square\":\"https://cdn-staging.tokopedia.com/img/cache/500-square/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919\"\n" +
            "},\n" +
            "\"product_all_images\":\"[{\\\"file_path\\\":\\\"product-1/2019/9/26/5510908\\\",\\\"file_name\\\":\\\"5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919\\\",\\\"status\\\":2},{\\\"file_path\\\":\\\"product-1/2019/9/26/5510908\\\",\\\"file_name\\\":\\\"5510908_417e3582-9671-46b4-af26-2c61d0051444_1919_1919\\\",\\\"status\\\":1},{\\\"file_path\\\":\\\"product-1/2019/9/26/5510908\\\",\\\"file_name\\\":\\\"5510908_8854f7d1-ab75-4b9c-ba15-0a88b6ae27b1_1919_1919\\\",\\\"status\\\":1},{\\\"file_path\\\":\\\"product-1/2019/9/26/5510908\\\",\\\"file_name\\\":\\\"5510908_345508de-2bfe-4bbd-90f6-7ce86f436f41_1919_1919\\\",\\\"status\\\":1},{\\\"file_path\\\":\\\"product-1/2019/9/26/5510908\\\",\\\"file_name\\\":\\\"5510908_33a77436-9623-4ceb-93d6-7af16a035eb6_1919_1919\\\",\\\"status\\\":1}]\",\n" +
            "\"product_notes\":\"\",\n" +
            "\"product_quantity\":7,\n" +
            "\"price_changes\":{\n" +
            "\"changes_state\":0,\n" +
            "\"amount_difference\":0,\n" +
            "\"original_amount\":0,\n" +
            "\"description\":\"\"\n" +
            "},\n" +
            "\"product_weight\":1,\n" +
            "\"product_weight_unit_code\":1,\n" +
            "\"product_weight_unit_text\":\"gr\",\n" +
            "\"last_update_price\":1569480222,\n" +
            "\"is_update_price\":false,\n" +
            "\"product_preorder\":{\n" +
            "},\n" +
            "\"product_showcase\":{\n" +
            "\"name\":\"testing\",\n" +
            "\"id\":1405133\n" +
            "},\n" +
            "\"product_finsurance\":1,\n" +
            "\"product_shop_id\":480829,\n" +
            "\"is_wishlisted\":false,\n" +
            "\"product_tracker_data\":{\n" +
            "\"attribution\":\"none/other\",\n" +
            "\"tracker_list_name\":\"none/other\"\n" +
            "},\n" +
            "\"is_ppp\":false,\n" +
            "\"is_cod\":false,\n" +
            "\"warehouse_id\":96,\n" +
            "\"warehouses\":{\n" +
            "\"96\":{\n" +
            "\"warehouseID\":96,\n" +
            "\"product_price\":10000,\n" +
            "\"price_currency\":1,\n" +
            "\"price_currency_name\":\"IDR\",\n" +
            "\"product_price_idr\":10000,\n" +
            "\"last_update_price\":{\n" +
            "\"unix\":1569505422,\n" +
            "\"yyyymmddhhmmss\":\"\"\n" +
            "},\n" +
            "\"product_switch_invenage\":1,\n" +
            "\"product_invenage_value\":2\n" +
            "}\n" +
            "},\n" +
            "\"is_parent\":false,\n" +
            "\"is_campaign_error\":false,\n" +
            "\"campaign_type_name\":\"\",\n" +
            "\"hide_gimmick\":false,\n" +
            "\"is_blacklisted\":false,\n" +
            "\"categories\":[\n" +
            "{\n" +
            "\"category_id\":60,\n" +
            "\"category_name\":\"Elektronik\"\n" +
            "},\n" +
            "{\n" +
            "\"category_id\":636,\n" +
            "\"category_name\":\"Tool & Kit\"\n" +
            "}\n" +
            "],\n" +
            "\"free_shipping\":{\n" +
            "\"eligible\":false,\n" +
            "\"badge_url\":\"\"\n" +
            "}\n" +
            "},\n" +
            "\"errors\":[\n" +
            "\"Stok barang ini kosong.\"\n" +
            "],\n" +
            "\"messages\":[\n" +
            "],\n" +
            "\"checkbox_state\":true,\n" +
            "\"similar_product_url\":\"tokopedia://rekomendasi/15262849?ref=cart\",\n" +
            "\"similar_product\":{\n" +
            "\"text\":\"Lihat Produk Serupa\",\n" +
            "\"url\":\"tokopedia://rekomendasi/15262849?ref=cart\"\n" +
            "}\n" +
            "}\n" +
            "],\n" +
            "\"total_cart_details_error\":1,\n" +
            "\"total_cart_price\":0,\n" +
            "\"errors\":[],\n" +
            "\"sort_key\":33376874,\n" +
            "\"is_fulfillment_service\":false,\n" +
            "\"warehouse\":{\n" +
            "\"warehouse_id\":96,\n" +
            "\"partner_id\":0,\n" +
            "\"shop_id\":480829,\n" +
            "\"warehouse_name\":\"Shop Location\",\n" +
            "\"district_id\":1,\n" +
            "\"district_name\":\"Kaway XVI\",\n" +
            "\"city_id\":1,\n" +
            "\"city_name\":\"Kab. Aceh Barat\",\n" +
            "\"province_id\":1,\n" +
            "\"province_name\":\"D.I. Aceh\",\n" +
            "\"status\":1,\n" +
            "\"postal_code\":\"123456\",\n" +
            "\"is_default\":1,\n" +
            "\"latlon\":\"123456,7890\",\n" +
            "\"latitude\":\"123456\",\n" +
            "\"longitude\":\"7890\",\n" +
            "\"email\":\"\",\n" +
            "\"address_detail\":\"Alamat ini milik Dragon\",\n" +
            "\"country_name\":\"Indonesia\",\n" +
            "\"is_fulfillment\":false,\n" +
            "\"tkpd_preferred_logistic_spid\":[\n" +
            "]\n" +
            "},\n" +
            "\"has_promo_list\":false,\n" +
            "\"checkbox_state\":true\n" +
            "}\n" +
            "],\n" +
            "\"default_promo_dialog_tab\":\"\",\n" +
            "\"donation\":{\n" +
            "\"Title\":\"\",\n" +
            "\"Nominal\":0,\n" +
            "\"Description\":\"\"\n" +
            "},\n" +
            "\"total_product_price\":2403000,\n" +
            "\"total_product_count\":2,\n" +
            "\"total_product_error\":1,\n" +
            "\"autoapply_stack\":{\n" +
            "\"global_success\":false,\n" +
            "\"success\":false,\n" +
            "\"message\":{\n" +
            "\"state\":\"\",\n" +
            "\"color\":\"\",\n" +
            "\"text\":\"\"\n" +
            "},\n" +
            "\"codes\":[],\n" +
            "\"promo_code_id\":0,\n" +
            "\"title_description\":\"\",\n" +
            "\"discount_amount\":0,\n" +
            "\"cashback_amount\":0,\n" +
            "\"cashback_wallet_amount\":0,\n" +
            "\"cashback_advocate_referral_amount\":0,\n" +
            "\"cashback_voucher_description\":\"\",\n" +
            "\"invoice_description\":\"\",\n" +
            "\"gateway_id\":\"\",\n" +
            "\"is_tokopedia_gerai\":false,\n" +
            "\"is_coupon\":0,\n" +
            "\"coupon_description\":\"\",\n" +
            "\"voucher_orders\":[],\n" +
            "\"benefit_summary_info\":{\n" +
            "\"final_benefit_text\":\"\",\n" +
            "\"final_benefit_amount_str\":\"\",\n" +
            "\"final_benefit_amount\":0,\n" +
            "\"summaries\":[]\n" +
            "},\n" +
            "\"clashing_info_detail\":{\n" +
            "\"clash_message\":\"\",\n" +
            "\"clash_reason\":\"\",\n" +
            "\"is_clashed_promos\":false,\n" +
            "\"options\":[]\n" +
            "},\n" +
            "\"tracking_details\":[],\n" +
            "\"benefit_details\":[],\n" +
            "\"ticker_info\":{\n" +
            "\"unique_id\":\"\",\n" +
            "\"status_code\":0,\n" +
            "\"message\":\"\"\n" +
            "}\n" +
            "},\n" +
            "\"global_coupon_attr\":{\n" +
            "\"description\":\"Gunakan promo Tokopedia\",\n" +
            "\"quantity_label\":\"\"\n" +
            "},\n" +
            "\"global_checkbox_state\":false,\n" +
            "\"tickers\":[\n" +
            "],\n" +
            "\"hashed_email\":\"fe51cd30c27c5c660de629bd1c58a1aa\"\n" +
            "}"
}