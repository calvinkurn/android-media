package com.tokopedia.withdraw.saldowithdrawal.util

sealed class SaldoWithdrawal
object BuyerSaldoWithdrawal : SaldoWithdrawal()
object SellerSaldoWithdrawal : SaldoWithdrawal()