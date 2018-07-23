package com.tokopedia.settingbank.addeditaccount.domain.mapper

import com.tkpd.library.utils.network.MessageErrorException
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.settingbank.addeditaccount.domain.pojo.EditBankAccountPojo
import retrofit2.Response
import rx.functions.Func1

/**
 * @author by nisie on 6/22/18.
 */

class EditBankMapper : Func1<Response<DataResponse<EditBankAccountPojo>>, Boolean> {

    override fun call(response: Response<DataResponse<EditBankAccountPojo>>): Boolean {

        if (response.body().header.messages.isEmpty() ||
                response.body().header.messages[0].isBlank()) {
            val pojo: EditBankAccountPojo = response.body().data
            return pojo.is_success ?: false
        } else {
            throw MessageErrorException(response.body().header.messages[0])
        }

    }


}