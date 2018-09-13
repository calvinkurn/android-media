package com.tokopedia.logout.domain.mapper

import com.tokopedia.logout.domain.model.LogoutDomain
import com.tokopedia.logout.domain.pojo.LogoutPojo
import retrofit2.Response
import rx.functions.Func1

/**
 * @author by nisie on 5/30/18.
 */

class LogoutMapper : Func1<Response<LogoutPojo>,
        LogoutDomain> {

    override fun call(response: Response<LogoutPojo>): LogoutDomain {
        var messageError: String = ""

        if (response.isSuccessful) {

            val pojo: LogoutPojo = response.body().copy()

            if (pojo.data != null
                    && pojo.message_error?.isEmpty()!!) {

                return LogoutDomain(
                        pojo.data.is_logout)

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