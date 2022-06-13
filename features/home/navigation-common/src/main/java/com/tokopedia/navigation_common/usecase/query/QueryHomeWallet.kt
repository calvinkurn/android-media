package com.tokopedia.navigation_common.usecase.query

object QueryHomeWallet {
    val eligibilityQuery : String = "query getEligibility(\$partnerCode:String!, \$walletCode: [String!]) {\n" +
            "walletappGetWalletEligible(partnerCode: \$partnerCode, walletCode: \$walletCode) {\n" +
            "    code \n" +
            "    message\n" +
            "    data {\n" +
            "      wallet_code\n" +
            "      is_eligible\n" +
            "    }\n" +
            "  }\n" +
            "}"

    val walletAppQuery : String = "query walletAppGetBalance(\$partnerCode: [String!]) {\n" +
            "        walletappGetBalances(partnerCode:\$partnerCode) {\n" +
            "              balances {\n" +
            "                   code\n" +
            "                   message\n" +
            "                   is_linked\n" +
            "                   masked_phone\n" +
            "                   type\n" +
            "                   balance {\n" +
            "                    wallet_code\n" +
            "                    amount\n" +
            "                    amount_fmt\n" +
            "                    active\n" +
            "                    message\n" +
            "                   }\n" +
            "                   reserve_balance {\n" +
            "                    wallet_code\n" +
            "                    wallet_code_fmt\n" +
            "                    amount\n" +
            "                    amount_fmt\n" +
            "                   }" +
            "                   redirect_url\n" +
            "                   icon_url\n" +
            "                   activation_cta\n" +
            "                   wallet_name\n" +
            "                   global_menu_text {\n" +
            "                       id\n" +
                "                   }\n" +
            "                   }\n" +
            "             }\n" +
            "    }"
}