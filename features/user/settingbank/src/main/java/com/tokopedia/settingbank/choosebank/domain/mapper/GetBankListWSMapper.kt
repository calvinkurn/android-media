package com.tokopedia.settingbank.choosebank.domain.mapper

import com.tkpd.library.utils.network.MessageErrorException
import com.tokopedia.abstraction.common.data.model.response.ResponseV4ErrorException
import com.tokopedia.settingbank.choosebank.domain.pojo.BankAccount
import com.tokopedia.settingbank.choosebank.domain.pojo.BankListPojo
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankListViewModel
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankViewModel
import retrofit2.Response
import rx.functions.Func1

/**
 * @author by nisie on 7/2/18.
 */
class GetBankListWSMapper : Func1<Response<BankListPojo>,
        BankListViewModel> {

    override fun call(response: Response<BankListPojo>): BankListViewModel {
        var messageError: String

        if (response.isSuccessful) {

            val pojo: BankListPojo = response.body().copy()

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

    private fun mapToViewModel(pojo: BankListPojo): BankListViewModel {
        val listBank = ArrayList<BankViewModel>()
        for (data: BankAccount in pojo.data!!.list) {
            listBank.add(BankViewModel(
                    data.bank_id,
                    data.bank_name,
                    false
            ))
        }
        return BankListViewModel(listBank,
                !pojo.data.paging!!.uri_next.isNullOrBlank()
                        && !pojo.data.paging.uri_next.equals("0"))
    }

}