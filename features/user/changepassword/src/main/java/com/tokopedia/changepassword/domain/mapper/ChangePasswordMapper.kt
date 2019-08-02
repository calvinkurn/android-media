package com.tokopedia.changepassword.domain.mapper

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.changepassword.domain.model.ChangePasswordDomain
import com.tokopedia.changepassword.domain.pojo.ChangePasswordPojo
import retrofit2.Response
import rx.functions.Func1

/**
 * @author by nisie on 7/25/18.
 */
class ChangePasswordMapper : Func1<Response<ChangePasswordPojo>,
        ChangePasswordDomain> {

    private val IS_SUCCESS: Int = 1

    override fun call(response: Response<ChangePasswordPojo>): ChangePasswordDomain {
        var messageError: String = ""

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                val pojo: ChangePasswordPojo = body.copy()

                if (pojo.data != null
                        && pojo.message_error?.isEmpty() == true) {

                    return ChangePasswordDomain(
                            pojo.data.is_success == IS_SUCCESS)

                } else if (pojo.message_error?.isNotEmpty()!!) {
                    messageError = body.message_error!![0]
                    throw MessageErrorException(messageError)
                } else {
                    throw MessageErrorException("")
                }
            } else {
                throw MessageErrorException("")
            }

        } else {
            messageError = response.code().toString()
            throw RuntimeException(messageError)
        }

    }

}