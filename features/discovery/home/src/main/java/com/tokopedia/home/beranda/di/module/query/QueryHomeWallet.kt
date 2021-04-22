package com.tokopedia.home.beranda.di.module.query

object QueryHomeWallet {
    val pendingCashBackQuery : String = "query pendingCashback {\n" +
            "  goalPendingBalance {\n" +
            "    balance\n" +
            "    balance_text\n" +
            "    cash_balance\n" +
            "    cash_balance_text\n" +
            "    point_balance\n" +
            "    point_balance_text\n" +
            "    wallet_type\n" +
            "    phone_number\n" +
            "    errors {\n" +
            "      title\n" +
            "      message\n" +
            "    }\n" +
            "  }\n" +
            "}\n"


    val tokopointsQuery: String = "query(\$apiVersion:String){\n" +
            "    tokopointsDrawer(apiVersion: \$apiVersion){\n" +
            "        iconImageURL\n" +
            "        redirectURL\n" +
            "        redirectAppLink\n" +
            "        sectionContent{\n" +
            "            type\n" +
            "            textAttributes{\n" +
            "                text\n" +
            "                color\n" +
            "                isBold\n" +
            "            }\n" +
            "            tagAttributes{\n" +
            "                text\n" +
            "                backgroundColor\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}"

    val tokopointsListQuery: String = "query tokopointsDrawerList(\$apiVersion:String){\n" +
            "    tokopointsDrawerList(apiVersion: \$apiVersion){\n" +
            "        offFlag\n" +
            "        drawerList{" +
            "           type\n" +
            "           iconImageURL\n" +
            "           redirectURL\n" +
            "           redirectAppLink\n" +
            "           sectionContent{\n" +
            "               type\n" +
            "               textAttributes{\n" +
            "                    text\n" +
            "                 color\n" +
            "                 isBold\n" +
            "               }\n" +
            "               tagAttributes{\n" +
            "                   text\n" +
            "                   backgroundColor\n" +
            "               }\n" +
            "           }\n" +
            "        }\n" +
            "    }\n" +
            "}"

    val walletBalanceQuery : String = "{\n" +
            "  wallet(isGetTopup:true) {\n" +
            "    linked\n" +
            "    balance\n" +
            "    rawBalance\n" +
            "    text\n" +
            "    total_balance\n" +
            "    raw_total_balance\n" +
            "    hold_balance\n" +
            "    raw_hold_balance\n" +
            "    redirect_url\n" +
            "    applinks\n" +
            "    ab_tags {\n" +
            "      tag\n" +
            "    }\n" +
            "    action {\n" +
            "      text\n" +
            "      redirect_url\n" +
            "      applinks\n" +
            "      visibility\n" +
            "    }\n" +
            "    point_balance\n" +
            "    raw_point_balance\n" +
            "    cash_balance\n" +
            "    raw_cash_balance\n" +
            "    wallet_type\n" +
            "    help_applink\n" +
            "    tnc_applink\n" +
            "    show_announcement\n" +
            "    is_show_topup\n" +
            "    topup_applink\n" +
            "    topup_limit\n" +
            "  }\n" +
            "}"
}