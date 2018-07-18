package com.tokopedia.settingbank.banklist.domain.mapper

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.settingbank.banklist.domain.pojo.DeleteBankAccountPojo
import retrofit2.Response
import rx.functions.Func1

/**
 * @author by nisie on 6/20/18.
 */
class DeleteBankAccountMapper : Func1<Response<DataResponse<DeleteBankAccountPojo>>, Boolean> {

    override fun call(response: Response<DataResponse<DeleteBankAccountPojo>>): Boolean {

        val pojo: DeleteBankAccountPojo = response.body().data
        return pojo.is_success ?: false

    }

}