package com.tokopedia.settingbank.domain.mapper

import com.tkpd.library.utils.network.MessageErrorException
import com.tokopedia.abstraction.common.data.model.response.ResponseV4ErrorException
import com.tokopedia.core.network.ErrorMessageException
import com.tokopedia.settingbank.domain.pojo.BankAccount
import com.tokopedia.settingbank.domain.pojo.BankAccountListPojo
import com.tokopedia.settingbank.domain.pojo.GetListBankAccountData
import com.tokopedia.settingbank.view.viewmodel.BankAccountListViewModel
import com.tokopedia.settingbank.view.viewmodel.BankAccountViewModel
import retrofit2.Response
import rx.functions.Func1

/**
 * @author by nisie on 6/8/18.
 */
class GetBankListMapper : Func1<Response<BankAccountListPojo>,
        BankAccountListViewModel> {


    override fun call(response: Response<BankAccountListPojo>): BankAccountListViewModel {
        var messageError: String

        if (response.isSuccessful) {

            val pojo: BankAccountListPojo = response.body().copy()

            if (pojo.data != null
                    && pojo.message_error?.isEmpty()!!) {

                return mapToViewModel(pojo)

            } else if (pojo.message_error?.isNotEmpty()!!) {
                throw ResponseV4ErrorException(response.body().message_error)
            } else {
                throw MessageErrorException("")
            }

        } else {
            messageError = response.code().toString()
            throw RuntimeException(messageError)
        }

    }

    private fun mapToViewModel(pojo: BankAccountListPojo): BankAccountListViewModel {
        return BankAccountListViewModel(
                mapToList(pojo.data)
        )
    }

    private fun mapToList(data: GetListBankAccountData?): ArrayList<BankAccountViewModel>? {
        val accountBankList: ArrayList<BankAccountViewModel> = ArrayList()

        for (bankAccount in data?.list!!) {
            accountBankList.add(mapToBankAccountViewModel(bankAccount))
        }
        return accountBankList
    }

    private fun mapToBankAccountViewModel(bankAccount: BankAccount): BankAccountViewModel {
        val IS_TRUE = 1
        return BankAccountViewModel(
                bankAccount.bank_account_id,
                bankAccount.bank_branch,
                bankAccount.bank_account_name,
                bankAccount.bank_account_number,
                bankAccount.is_verified_account == IS_TRUE,
                bankAccount.bank_account_id,
                bankAccount.bank_name,
                bankAccount.is_default_bank == IS_TRUE,
                bankAccount.bank_logo
        )
    }


}