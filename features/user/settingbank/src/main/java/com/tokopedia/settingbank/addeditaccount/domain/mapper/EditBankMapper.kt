package com.tokopedia.settingbank.addeditaccount.domain.mapper

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.settingbank.addeditaccount.domain.pojo.EditBankAccountPojo
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 6/22/18.
 */

class EditBankMapper @Inject constructor() : Func1<Response<DataResponse<EditBankAccountPojo>>, Boolean> {

    override fun call(response: Response<DataResponse<EditBankAccountPojo>>): Boolean {
        val body = response.body()
        if (body != null) {
            if (body.header.messages.isEmpty() ||
                    body.header.messages[0].isBlank()) {
                val pojo: EditBankAccountPojo = body.data
                return pojo.is_success ?: false
            } else {
                throw MessageErrorException(body.header.messages[0])
            }
        } else {
           throw MessageErrorException("")
        }


    }


}