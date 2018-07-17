package com.tokopedia.settingbank.addeditaccount.domain.mapper

import com.tkpd.library.utils.network.MessageErrorException
import com.tokopedia.abstraction.common.data.model.response.ResponseV4ErrorException
import com.tokopedia.settingbank.addeditaccount.domain.pojo.ValidateBankAccountPojo
import com.tokopedia.settingbank.addeditaccount.view.viewmodel.ValidateBankViewModel
import retrofit2.Response
import rx.functions.Func1

/**
 * @author by nisie on 7/13/18.
 */

class ValidateBankMapper : Func1<Response<ValidateBankAccountPojo>, ValidateBankViewModel> {

    override fun call(response: Response<ValidateBankAccountPojo>): ValidateBankViewModel {
        val messageError: String

        if (response.isSuccessful) {

            val pojo: ValidateBankAccountPojo = response.body().copy()

            if (pojo.data != null
                    && pojo.messages?.isEmpty()!!
                    && pojo.data.form_info != null) {
                return ValidateBankViewModel(pojo.data.is_valid,
                        pojo.data.is_data_change,
                        pojo.data.form_info.param_name,
                        pojo.data.form_info.messages)
            } else if (!pojo.messages?.isEmpty()!!) {
                throw ResponseV4ErrorException(pojo.messages)
            } else {
                throw MessageErrorException("")
            }

        } else {
            messageError = response.code().toString()
            throw RuntimeException(messageError)
        }
    }


}