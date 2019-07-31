package com.tokopedia.settingbank.banklist.domain.mapper

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.settingbank.banklist.domain.pojo.BankAccount
import com.tokopedia.settingbank.banklist.domain.pojo.BankAccountListPojo
import com.tokopedia.settingbank.banklist.view.viewmodel.BankAccountListViewModel
import com.tokopedia.settingbank.banklist.view.viewmodel.BankAccountViewModel
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 6/8/18.
 */
class GetBankAccountListMapper @Inject constructor(): Func1<Response<DataResponse<BankAccountListPojo>>,
        BankAccountListViewModel> {


    override fun call(response: Response<DataResponse<BankAccountListPojo>>):
            BankAccountListViewModel {
        if (response.body().header.messages.isEmpty() ||
                response.body().header.messages[0].isBlank()) {
            val pojo: BankAccountListPojo = response.body().data
            return mapToViewModel(pojo)

        } else {
            throw MessageErrorException(response.body().header.messages[0])

        }
    }

    private fun mapToViewModel(pojo: BankAccountListPojo): BankAccountListViewModel {
        return BankAccountListViewModel(
                mapToList(pojo),
                checkEnableButton(pojo),
                pojo.user_info.messages ?: ""
        )
    }

    private fun checkEnableButton(data: BankAccountListPojo?): Boolean {
        return if (data?.user_info?.is_verified != null) data.user_info.is_verified else false
    }

    private fun mapToList(data: BankAccountListPojo?): ArrayList<BankAccountViewModel>? {
        val accountBankList: ArrayList<BankAccountViewModel> = ArrayList()

        for (bankAccount in data?.bank_accounts!!) {
            accountBankList.add(mapToBankAccountViewModel(bankAccount))
        }
        return accountBankList
    }

    private fun mapToBankAccountViewModel(bankAccount: BankAccount): BankAccountViewModel {
        return BankAccountViewModel(
                bankAccount.bank_id,
                bankAccount.branch,
                bankAccount.acc_name,
                bankAccount.acc_number,
                bankAccount.acc_id,
                bankAccount.bank_name,
                bankAccount.primary,
                bankAccount.bank_image_url
        )
    }


}