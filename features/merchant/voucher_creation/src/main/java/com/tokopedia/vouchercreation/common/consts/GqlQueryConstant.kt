package com.tokopedia.vouchercreation.common.consts

object GqlQueryConstant {

    const val GET_INIT_VOUCHER_ELIGIBILITY_QUERY = "query InitiateVoucher(\$action: String, \$targetBuyer: Int, \$couponType: String){\n" +
            "\tgetInitiateVoucherPage(Action: \$action, TargetBuyer: \$targetBuyer ,CouponType: \$couponType){\n" +
            "\t\theader{\n" +
            "          process_time\n" +
            "          messages\n" +
            "          reason\n" +
            "          error_code\n" +
            "        }\n" +
            "        data{\n" +
            "          is_eligible\n" +
            "        }\n" +
            "    }\n" +
            "}"
}