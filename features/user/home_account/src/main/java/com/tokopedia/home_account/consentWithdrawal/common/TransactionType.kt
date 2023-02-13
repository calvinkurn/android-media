package com.tokopedia.home_account.consentWithdrawal.common

enum class TransactionType(val alias: String) {
    OPT_IN("OPT_IN"),
    OPT_OUT("OPT_OUT");

    companion object {
        fun map(alias: String): TransactionType? {
            val map = values().associateBy(TransactionType::alias)
            return map[alias]
        }
    }
}
