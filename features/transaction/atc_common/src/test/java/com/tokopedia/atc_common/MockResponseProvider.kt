package com.tokopedia.atc_common

import com.google.gson.Gson
import com.tokopedia.atc_common.data.model.response.AddToCartGqlResponse

/**
 * Created by Irfan Khoirul on 2019-09-11.
 */

object MockResponseProvider {

    fun getResponseAtcSuccess(): AddToCartGqlResponse {
        return Gson().fromJson(responseAtcSuccess, AddToCartGqlResponse::class.java)
    }

    fun getResponseAtcError(): AddToCartGqlResponse {
        return Gson().fromJson(responseAtcError, AddToCartGqlResponse::class.java)
    }

}

val responseAtcSuccess = "{\n" +
        "\"add_to_cart\":{\n" +
        "\"error_message\":[\n" +
        "\"Jumlah barang melebihi stok di toko. Kurangi pembelianmu, ya!\"\n" +
        "],\n" +
        "\"status\":\"OK\",\n" +
        "\"data\":{\n" +
        "\"success\":0,\n" +
        "\"cart_id\":\"0\",\n" +
        "\"product_id\":0,\n" +
        "\"quantity\":0,\n" +
        "\"notes\":\"\",\n" +
        "\"shop_id\":0,\n" +
        "\"customer_id\":0,\n" +
        "\"warehouse_id\":0,\n" +
        "\"tracker_attribution\":\"\",\n" +
        "\"tracker_list_name\":\"\",\n" +
        "\"uc_ut_param\":\"\",\n" +
        "\"is_trade_in\":false,\n" +
        "\"message\":[\n" +
        "\"Jumlah barang melebihi stok di toko. Kurangi pembelianmu, ya!\"\n" +
        "]\n" +
        "},\n" +
        "\"error_reporter\":{\n" +
        "\"eligible\":false,\n" +
        "\"texts\":{\n" +
        "\"submit_title\":\"Barang belum bisa dimasukkan ke keranjang\",\n" +
        "\"submit_description\":\"Ayo beri tahu kami tentang gangguan ini biar bisa segera diperbaiki!\",\n" +
        "\"submit_button\":\"Laporkan Gangguan\",\n" +
        "\"cancel_button\":\"Batal\"\n" +
        "}\n" +
        "}\n" +
        "}\n" +
        "}"

val responseAtcError = "{\n" +
        "\"add_to_cart\":{\n" +
        "\"error_message\":[\n" +
        "\"Jumlah barang tidak valid.\"\n" +
        "],\n" +
        "\"status\":\"OK\",\n" +
        "\"data\":{\n" +
        "\"success\":0,\n" +
        "\"cart_id\":\"0\",\n" +
        "\"product_id\":0,\n" +
        "\"quantity\":0,\n" +
        "\"notes\":\"\",\n" +
        "\"shop_id\":0,\n" +
        "\"customer_id\":0,\n" +
        "\"warehouse_id\":0,\n" +
        "\"tracker_attribution\":\"\",\n" +
        "\"tracker_list_name\":\"\",\n" +
        "\"uc_ut_param\":\"\",\n" +
        "\"is_trade_in\":false,\n" +
        "\"message\":[\n" +
        "\"Jumlah barang tidak valid.\"\n" +
        "]\n" +
        "},\n" +
        "\"error_reporter\":{\n" +
        "\"eligible\":false,\n" +
        "\"texts\":{\n" +
        "\"submit_title\":\"Barang belum bisa dimasukkan ke keranjang\",\n" +
        "\"submit_description\":\"Ayo beri tahu kami tentang gangguan ini biar bisa segera diperbaiki!\",\n" +
        "\"submit_button\":\"Laporkan Gangguan\",\n" +
        "\"cancel_button\":\"Batal\"\n" +
        "}\n" +
        "}\n" +
        "}\n" +
        "}"