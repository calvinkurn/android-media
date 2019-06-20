package com.tokopedia.settingbank.addeditaccount.domain.mapper

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.settingbank.addeditaccount.domain.pojo.FormInfoPojo
import com.tokopedia.settingbank.addeditaccount.domain.pojo.ValidateBankAccountPojo
import com.tokopedia.settingbank.addeditaccount.view.viewmodel.ValidateBankViewModel
import com.tokopedia.settingbank.addeditaccount.view.viewmodel.ValidationForm
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 7/13/18.
 */

class ValidateBankMapper @Inject constructor(): Func1<Response<DataResponse<ValidateBankAccountPojo>>,
        ValidateBankViewModel> {

    override fun call(response: Response<DataResponse<ValidateBankAccountPojo>>): ValidateBankViewModel {

        if (response.body().header.messages.isEmpty() ||
                response.body().header.messages[0].isBlank()) {
            val pojo: ValidateBankAccountPojo = response.body().data
            return ValidateBankViewModel(pojo.is_valid,
                    pojo.is_data_change,
                    mapToFormInfo(pojo.form_info))
        } else {
            throw MessageErrorException(response.body().header.messages[0])
        }

    }

    private fun mapToFormInfo(form_info: List<FormInfoPojo>): ArrayList<ValidationForm> {
        val list: ArrayList<ValidationForm> = ArrayList()
        for (form in form_info) {
            list.add(ValidationForm(
                    form.param_name,
                    form.messages))
        }
        return list
    }


}