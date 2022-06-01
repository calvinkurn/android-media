package com.tokopedia.vouchercreation.common.consts

object GqlRequestConstant {
    private const val merchantVoucherData = "\$merchantVoucherData"

    val createCouponProductMutation = """
        mutation merchantPromotionCreateMV($merchantVoucherData: merchantVoucherData!){ 
            merchantPromotionCreateMV(merchantVoucherData:$merchantVoucherData){
                status 
                message 
                process_time
                data {
                    redirect_url
                    voucher_id
                    status
                }
              }
        }
        """.trimIndent()
}