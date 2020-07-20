package com.tokopedia.settingbank.util

enum class BankAccountNumber(val abbrevation: String, val count: Int) {
    Mandiri("Mandiri", 13),
    BCA("BCA", 10),
    BRI("BRI", 15),
    OTHER("", 16)
}

