package com.tokopedia.pms.bankaccount.domain

import com.tokopedia.pms.bankaccount.data.model.BankListModel
import java.util.*

/**
 * Created by zulfikarrahman on 7/5/18.
 */
class BankListDataUseCase {
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

    fun getBankNameFromBankId(bankId: String?): String {
        for (bankModel in bankList) {
            if (bankModel.id == bankId) return bankModel.bankName
        }
        return bankList.getOrNull(0)?.bankName ?: ""
    }

    fun getBankIdFromBankName(bankName: String): String {
        for (bankModel in bankList) {
            if (bankModel.bankName == bankName) return bankModel.id
        }
        return bankList.getOrNull(0)?.id ?: "1"
    }

}