package com.tokopedia.withdraw.auto_withdrawal.domain.query

const val GQL_GET_BANK_ACCOUNT = "query {\n" +
        "    GetBankListWD{\n" +
        "        status\n" +
        "        message\n" +
        "        data {\n" +
        "            bankID\n" +
        "            accountNo\n" +
        "            bankName\n" +
        "            bankBranch\n" +
        "            bankAccountID\n" +
        "            bankImageUrl\n" +
        "            isDefaultBank\n" +
        "            adminFee\n" +
        "            minAmount\n" +
        "            maxAmount\n" +
        "            status\n" +
        "            isVerifiedAccount\n" +
        "            accountName\n" +
        "            statusFraud\n" +
        "            copyWriting\n" +
        "            is_fraud\n" +
        "            have_rp_program\n" +
        "            have_special_offer\n" +
        "            default_bank_account\n" +
        "        }\n" +
        "    }\n" +
        "}"

const val GQL_GET_INFO_AUTO_WITHDRAWAL= "query GetInfoAutoWD {\n" +
        "  GetInfoAutoWD {\n" +
        "    code\n" +
        "    message\n" +
        "    data{\n" +
        "      title\n" +
        "      desc\n" +
        "      icon\n" +
        "    }\n" +
        "  }\n" +
        "}"

const val GQL_GET_AUTO_WD_STATUS = "query GetAutoWDStatus {\n" +
        "  GetAutoWDStatus{\n" +
        "    code\n" +
        "    message\n" +
        "    data{\n" +
        "      auto_wd_user_id\n" +
        "      user_id\n" +
        "      status\n" +
        "      is_owner\n" +
        "      is_power_wd\n" +
        "      schedule{\n" +
        "        auto_wd_schedule_id\n" +
        "        title\n" +
        "        desc\n" +
        "        status\n" +//0 never opted show banner --- if is owner -- warning page disabled
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "}"