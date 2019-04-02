package com.tokopedia.settingbank.addeditaccount.domain.usecase

import com.tokopedia.settingbank.addeditaccount.domain.mapper.EditBankMapper
import com.tokopedia.settingbank.banklist.data.SettingBankApi
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * @author by nisie on 6/22/18.
 */

class EditBankUseCase(val api: SettingBankApi,
                      val mapper: EditBankMapper) : UseCase<Boolean>() {

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {

        return api.editBankAccount(requestParams.parameters).map(mapper)
    }

}