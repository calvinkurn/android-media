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

        if (response.body().header.messages.isEmpty() ||
                response.body().header.messages[0].isBlank()) {
            val pojo: AddBankAccountPojo = response.body().data
            return pojo
        } else {
            throw MessageErrorException(response.body().header.messages[0])
        }
    }

}