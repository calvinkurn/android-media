package com.tokopedia.navigation_common.usecase.query

object QueryHomeWallet {
    val eligibilityQuery : String = "query getEligibility(\$partnerCode:String, \$walletCode: [String!]) {\n" +
            "walletappGetWalletEligible(partnerCode: \$partnerCode, walletCode: \$walletCode) {\n" +
            "    code \n" +
            "    message\n" +
            "    data {\n" +
            "      wallet_code #PEMUDAPOINTS\n" +
            "      is_eligible #true/false\n" +
            "    }\n" +
            "  }\n" +
            "}"
}