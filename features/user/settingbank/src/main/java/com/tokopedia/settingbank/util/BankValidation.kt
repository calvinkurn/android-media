package com.tokopedia.settingbank.util

enum class BankAccountNumber(val abbrevation: String, val count: Int) {
    Mandiri("Mandiri", 13),
    BCA("BCA", 10),
    BRI("BRI", 15),
    OTHER("", 16)
}


fun getBankTypeFromAbbreviation(abbreviation: String): BankAccountNumber = when (abbreviation.toUpperCase()) {
    BankAccountNumber.BRI.abbrevation.toUpperCase() -> BankAccountNumber.BRI
    BankAccountNumber.BCA.abbrevation.toUpperCase() -> BankAccountNumber.BCA
    BankAccountNumber.Mandiri.abbrevation.toUpperCase() -> BankAccountNumber.Mandiri
    else -> BankAccountNumber.OTHER
}

