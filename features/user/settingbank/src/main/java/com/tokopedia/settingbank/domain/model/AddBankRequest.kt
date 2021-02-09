package com.tokopedia.settingbank.domain.model

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

        fun setAccountNumber(accountNo: String?) = apply {
            accountNo?.let {
                this.accountName = ""
                this.accountNo = accountNo
            } ?: run {
                this.accountName = ""
                this.accountNo = ""
            }
        }

        fun setAccountName(accountName: String, isManual: Boolean) = apply {
            this.accountName = accountName
            this.isManual = isManual
        }

        fun isManual(isManual: Boolean) {
            this.isManual = isManual
        }

        fun getAccountNumber(): String? = if (::accountNo.isInitialized) accountNo else null

        fun getAccountName(): String? = if (::accountName.isInitialized) accountName else null

        fun isManual(): Boolean = isManual


        fun build() = AddBankRequest(
                bankId, bankName, accountNo, accountName, isManual
        )
    }
}