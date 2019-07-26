package com.tokopedia.settingbank.banklist.domain.mapper

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.settingbank.banklist.domain.pojo.SetDefaultBankAccountPojo
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 6/20/18.
 */
class SetDefaultBankAccountMapper @Inject constructor(): Func1<Response<DataResponse<SetDefaultBankAccountPojo>>,
        Boolean> {

    override fun call(response: Response<DataResponse<SetDefaultBankAccountPojo>>): Boolean {
        if (response.body().header.messages.isEmpty() ||
                response.body().header.messages[0].isBlank()) {
            val pojo: SetDefaultBankAccountPojo = response.body().data
            return pojo.is_success ?: false
        } else {
            throw MessageErrorException(response.body().header.messages[0])
        }

    }


}