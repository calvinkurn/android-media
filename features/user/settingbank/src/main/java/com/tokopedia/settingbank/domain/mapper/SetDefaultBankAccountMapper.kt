package com.tokopedia.settingbank.domain.mapper

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
                messageError = response.body().message_error!![0]
                throw RuntimeException(messageError)
            } else {
                throw RuntimeException("")
            }

        } else {
            messageError = response.code().toString()
            throw RuntimeException(messageError)
        }
    }


}