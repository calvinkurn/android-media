package com.tokopedia.saldodetails.view.fragment.new

object TransactionTitle{
    const val ALL_TRANSACTION = "Semua"
    const val SALDO_REFUND = "Saldo Refund"
    const val SALDO_SALES = "Penjualan"
    const val SALDO_INCOME = "Saldo Penghasilan"
}


sealed class TransactionType(val title : String,val type : Int)
object AllTransaction : TransactionType(TransactionTitle.ALL_TRANSACTION, 2)
object RefundTransaction : TransactionType(TransactionTitle.SALDO_REFUND, 0)
object SalesTransaction : TransactionType(TransactionTitle.SALDO_SALES, 1)
object IncomeTransaction : TransactionType(TransactionTitle.SALDO_INCOME, 1)

object TransactionTypeMapper{

    fun getTransactionListType(title : String?) : TransactionType?{
        return when(title){
            TransactionTitle.SALDO_REFUND -> RefundTransaction
            TransactionTitle.SALDO_SALES -> SalesTransaction
            TransactionTitle.SALDO_INCOME -> IncomeTransaction
            TransactionTitle.ALL_TRANSACTION -> AllTransaction
            else -> null
        }
    }



}