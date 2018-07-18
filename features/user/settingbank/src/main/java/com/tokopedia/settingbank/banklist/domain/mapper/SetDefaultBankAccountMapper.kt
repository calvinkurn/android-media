package com.tokopedia.settingbank.banklist.domain.mapper

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.settingbank.banklist.domain.pojo.SetDefaultBankAccountPojo
import retrofit2.Response
import rx.functions.Func1

/**
 * @author by nisie on 6/20/18.
 */
class SetDefaultBankAccountMapper : Func1<Response<DataResponse<SetDefaultBankAccountPojo>>,
        Boolean> {

    override fun call(response: Response<DataResponse<SetDefaultBankAccountPojo>>): Boolean {

        val pojo: SetDefaultBankAccountPojo = response.body().data
        return pojo.is_success ?: false

    }


}