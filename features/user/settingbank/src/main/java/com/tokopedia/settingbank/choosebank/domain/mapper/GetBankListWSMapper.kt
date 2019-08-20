package com.tokopedia.settingbank.choosebank.domain.mapper

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.settingbank.choosebank.domain.pojo.BankAccount
import com.tokopedia.settingbank.choosebank.domain.pojo.BankListPojo
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankListViewModel
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankViewModel
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 7/2/18.
 */
class GetBankListWSMapper @Inject constructor() : Func1<Response<DataResponse<BankListPojo>>,
        BankListViewModel> {

    override fun call(response: Response<DataResponse<BankListPojo>>): BankListViewModel {
        val body = response.body()
        if(body != null) {
            if (body.header.messages.isEmpty() ||
                    body.header.messages[0].isBlank()) {
                val pojo: BankListPojo = body.data
                return mapToViewModel(pojo)
            } else {
                throw MessageErrorException(body.header.messages[0])
            }
        } else {
            throw MessageErrorException("")
        }
    }

    private fun mapToViewModel(pojo: BankListPojo): BankListViewModel {
        val listBank = ArrayList<BankViewModel>()
        for (data: BankAccount in pojo.banks) {
            listBank.add(BankViewModel(
                    data.bank_id,
                    data.bank_name,
                    false,
                    ""
            ))
        }
        return BankListViewModel(listBank, hasNextPage(pojo))
    }

    private fun hasNextPage(pojo: BankListPojo): Boolean {
        return pojo.has_next_page
    }

}