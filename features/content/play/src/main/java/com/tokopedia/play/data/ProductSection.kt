package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 27/01/22
 */
data class ProductSection(
    @SerializedName("sections")
    val sectionList: List<Section> = emptyList(),

    @SerializedName("vouchers")
    val voucherList: List<Voucher> = emptyList(),

    @SerializedName("config")
    val config: Config = Config()
){
    data class Response(
        @SerializedName("playGetTagsItemSection")
        val playGetTagsItem: ProductSection = ProductSection()
    )
}
