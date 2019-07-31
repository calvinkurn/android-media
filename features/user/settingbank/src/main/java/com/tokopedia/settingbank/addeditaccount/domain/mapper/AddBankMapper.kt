package com.tokopedia.settingbank.addeditaccount.domain.mapper

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.settingbank.addeditaccount.domain.pojo.AddBankAccountPojo
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 6/22/18.
 */

class AddBankMapper @Inject constructor() : Func1<Response<DataResponse<AddBankAccountPojo>>, AddBankAccountPojo> {

    override fun call(response: Response<DataResponse<AddBankAccountPojo>>): AddBankAccountPojo {
        val body = response.body()
        if(body != null) {
            if (body.header.messages.isEmpty() || body.header.messages[0].isBlank()) {
                val pojo: AddBankAccountPojo = body.data
                return pojo
            } else {
                throw MessageErrorException(body.header.messages[0])
            }
        } else {
            throw MessageErrorException("")
        }
    }

}