package com.tokopedia.pms.bankaccount.domain

import com.tokopedia.pms.bankaccount.data.model.BankListModel

/**
 * Created by zulfikarrahman on 7/5/18.
 */
class BankListDataUseCase {

    val bankList: List<BankListModel> by lazy {
        arrayListOf(
            BankListModel("1", "BCA"),
            BankListModel("2", "MANDIRI"),
            BankListModel("3", "BNI"),
            BankListModel("4", "BRI"),
            BankListModel("5", "CIMB"),
        )
    }

    fun getBankNameFromBankId(bankId: String?): String {
        for (bankModel in bankList)
            if (bankModel.id == bankId) return bankModel.bankName ?: ""
        return bankList.getOrNull(0)?.bankName ?: ""
    }

    fun getBankIdFromBankName(bankName: String): String {
        for (bankModel in bankList)
            if (bankModel.bankName == bankName) return bankModel.id ?: ""
        return bankList.getOrNull(0)?.id ?: "1"
    }
}