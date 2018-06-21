package com.tokopedia.settingbank.domain.mapper

import com.tkpd.library.utils.network.MessageErrorException
import com.tokopedia.abstraction.common.data.model.response.ResponseV4ErrorException
import com.tokopedia.settingbank.domain.pojo.SetDefaultBankAccountPojo
import retrofit2.Response
import rx.functions.Func1

/**
 * @author by nisie on 6/20/18.
 */
class SetDefaultBankAccountMapper : Func1<Response<SetDefaultBankAccountPojo>, String> {

    override fun call(response: Response<SetDefaultBankAccountPojo>): String {
        var messageError: String

        if (response.isSuccessful) {

            val pojo: SetDefaultBankAccountPojo = response.body().copy()

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