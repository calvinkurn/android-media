package com.tokopedia.settingbank.addeditaccount.domain.mapper

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.settingbank.addeditaccount.domain.pojo.ValidateBankAccountPojo
import com.tokopedia.settingbank.addeditaccount.view.viewmodel.ValidateBankViewModel
import retrofit2.Response
import rx.functions.Func1

/**
 * @author by nisie on 7/13/18.
 */

class ValidateBankMapper : Func1<Response<DataResponse<ValidateBankAccountPojo>>,
        ValidateBankViewModel> {

    override fun call(response: Response<DataResponse<ValidateBankAccountPojo>>): ValidateBankViewModel {

        val pojo: ValidateBankAccountPojo = response.body().data

        return ValidateBankViewModel(pojo.is_valid,
                pojo.is_data_change,
                pojo.form_info?.param_name,
                pojo.form_info?.messages)

    }


}