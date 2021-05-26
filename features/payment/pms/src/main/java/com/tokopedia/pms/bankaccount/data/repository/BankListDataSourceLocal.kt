package com.tokopedia.pms.bankaccount.data.repository

import com.tokopedia.pms.bankaccount.data.model.BankListModel
import java.util.*
import javax.inject.Inject

/**
 * Created by zulfikarrahman on 7/5/18.
 */
class BankListDataSourceLocal @Inject constructor() {
    val bankList: List<BankListModel>
        get() {
            val listOfBank = listOf("BCA", "MANDIRI", "BNI", "BRI", "CIMB")
            val listOfBankCode = listOf("1", "2", "3", "4", "5")
            val bankListModels: MutableList<BankListModel> = ArrayList()
            for (i in listOfBank.indices) {
                val bankListModel =
                    BankListModel()
                bankListModel.bankName = listOfBank[i]
                bankListModel.id = listOfBankCode[i]
                bankListModels.add(bankListModel)
            }
            return bankListModels
        }
}