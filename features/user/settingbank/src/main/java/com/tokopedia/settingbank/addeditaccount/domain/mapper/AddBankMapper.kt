package com.tokopedia.settingbank.addeditaccount.domain.mapper

import com.tkpd.library.utils.network.MessageErrorException
import com.tokopedia.abstraction.common.data.model.response.ResponseV4ErrorException
import com.tokopedia.settingbank.addeditaccount.domain.pojo.AddBankAccountPojo
import retrofit2.Response
import rx.functions.Func1

/**
 * @author by nisie on 6/22/18.
 */

class AddBankMapper : Func1<Response<AddBankAccountPojo>, String> {

    override fun call(response: Response<AddBankAccountPojo>): String {
        var messageError: String

        if (response.isSuccessful) {

            val pojo: AddBankAccountPojo = response.body().copy()

            if (pojo.data != null
                    && pojo.message_error?.isEmpty()!!
                    && !pojo.message_status?.isEmpty()!!) {

                return pojo.message_status[0]

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


}