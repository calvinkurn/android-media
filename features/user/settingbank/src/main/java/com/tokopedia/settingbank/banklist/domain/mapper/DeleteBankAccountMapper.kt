package com.tokopedia.settingbank.banklist.domain.mapper

import com.tkpd.library.utils.network.MessageErrorException
import com.tokopedia.abstraction.common.data.model.response.ResponseV4ErrorException
import com.tokopedia.settingbank.banklist.domain.pojo.DeleteBankAccountPojo
import retrofit2.Response
import rx.functions.Func1

/**
 * @author by nisie on 6/20/18.
 */
class DeleteBankAccountMapper : Func1<Response<DeleteBankAccountPojo>, Boolean> {

    override fun call(response: Response<DeleteBankAccountPojo>): Boolean {

        val pojo: DeleteBankAccountPojo = response.body().copy()
        return pojo.is_success ?: false

    }

}