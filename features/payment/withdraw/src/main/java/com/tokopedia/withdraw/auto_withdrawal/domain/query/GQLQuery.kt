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
        "        scheduleType\n" +
        "        title\n" +
        "        desc\n" +
        "        status\n" +
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "}"

const val GQL_AUTO_WD_TNC = "query GetTNCAutoWD {\n" +
        "  GetTNCAutoWD{\n" +
        "    code\n" +
        "    message\n" +
        "    data{\n" +
        "      template\n" +
        "    }\n" +
        "  }\n" +
        "}"

const val GQL_UPSERT_AUTO_WD = "mutation UpsertAutoWDData(" +
        "   \$autoWDUserId: Int\n" +
        "   \$oldAutoWDScheduleId: Int\n" +
        "   \$scheduleType: Int\n" +
        "   \$accId: Int!\n" +
        "   \$accNo: String!\n" +
        "   \$bankId: Int!\n" +
        "   \$isUpdate: Boolean!\n" +
        "   \$validateToken: String!\n" +
        "   \$isQuit: Boolean!" +
        "   ) {\n" +
        "   UpsertAutoWDData(\n"+
        "        input: {\n"+
        "           autoWDUserId: \$autoWDUserId\n" +
        "           oldAutoWDScheduleId: \$oldAutoWDScheduleId\n" +
        "           scheduleType: \$scheduleType\n" +
        "           accId: \$accId\n" +
        "           accNo: \$accNo\n" +
        "           bankId: \$bankId\n" +
        "           isUpdate: \$isUpdate\n" +
        "           validateToken: \$validateToken\n" +
        "           isQuit: \$isQuit\n" +
        "           }\n" +
        "       ){\n" +
        "           code\n" +
        "           message\n" +
        "        }\n" +
        "}"