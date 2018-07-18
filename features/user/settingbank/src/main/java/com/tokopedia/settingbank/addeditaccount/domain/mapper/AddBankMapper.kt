package com.tokopedia.settingbank.addeditaccount.domain.mapper

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.settingbank.addeditaccount.domain.pojo.AddBankAccountPojo
import retrofit2.Response
import rx.functions.Func1

/**
 * @author by nisie on 6/22/18.
 */

class AddBankMapper : Func1<Response<DataResponse<AddBankAccountPojo>>, Boolean> {

    override fun call(response: Response<DataResponse<AddBankAccountPojo>>): Boolean {

        val pojo: AddBankAccountPojo = response.body().data
        return pojo.is_success ?: false
    }

}