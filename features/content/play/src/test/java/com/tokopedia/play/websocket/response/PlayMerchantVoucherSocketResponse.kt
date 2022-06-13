package com.tokopedia.play.websocket.response


/**
 * Created By : Jonathan Darwin on September 15, 2021
 */
object PlayMerchantVoucherSocketResponse {
    fun generateResponse(
        size: Int = 2,
        title: String = "Diskon 10%",
        description: String = "Min. Pembelanjaan 10rb",
        quota: Int = 1,
        expiredDate: String = "2018-12-07T23:30:00Z",
    ): String {
        var voucherList = ""
        for(i in 1..size) {
            voucherList += """
                {
                    is_quota_available: 1,
                    shop_id: 240473,
                    subtitle: "$description",
                    title: "$title $i%",
                    tnc: "",
                    used_quota: 9,
                    voucher_code: "123",
                    voucher_finish_time: "$expiredDate",
                    voucher_id: 1,
                    voucher_image: "https://ecs7.tokopedia.net/img/attachment/2018/10/4/5480066/5480066_b0036e26-6b16-4afa-a8a3-c14faf9832e0",
                    voucher_image_square: "",
                    voucher_name: "test date",
                    voucher_quota: $quota,
                    voucher_type: 3,
                    is_copyable: true,
                    is_highlighted: true
                }
            """.trimIndent()

            if(i != size) voucherList += ","
        }
        return """
            {
                "type": "MERCHANT_VOUCHERS",
                "data": [
                    $voucherList
                ]
            }
        """.trimIndent()
    }
}