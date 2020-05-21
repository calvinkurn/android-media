package com.tokopedia.withdraw.saldowithdrawal.constant

sealed class SaldoWithdrawal
object BuyerSaldoWithdrawal : SaldoWithdrawal()
object SellerSaldoWithdrawal : SaldoWithdrawal()