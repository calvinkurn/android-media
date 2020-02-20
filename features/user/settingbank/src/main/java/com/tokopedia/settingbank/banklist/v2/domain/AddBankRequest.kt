package com.tokopedia.settingbank.banklist.v2.domain

data class AddBankRequest(
        var bankId: Long,
        var bankName: String,
        var accountNo: String,
        var accountName: String,
        var isManual: Boolean
) {
    class Builder {
        private var bankId: Long = 0L
        private var bankName: String = ""
        private lateinit var accountNo: String
        private lateinit var accountName: String
        private var isManual: Boolean = false

        fun bank(bankId: Long, bankName: String?) = apply {
            this.bankId = bankId
            bankName?.let {
                this.bankName = bankName
            }
        }

        fun accountNumber(accountNo: String?) = apply {
            accountNo?.let {
                this.accountName = ""
                this.accountNo = accountNo
            } ?: run {
                this.accountName = ""
                this.accountNo = ""
            }
        }

        fun accountName(accountName: String, isManual: Boolean) = apply {
            this.accountName = accountName
            this.isManual = isManual
        }

        fun isManual(isManual: Boolean){
            this.isManual = isManual
        }


        fun build() = AddBankRequest(
                bankId, bankName, accountNo, accountName, isManual
        )
    }
}