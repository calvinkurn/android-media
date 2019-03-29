package com.tokopedia.settingbank.addeditaccount.domain.usecase

import com.tokopedia.settingbank.addeditaccount.domain.mapper.AddBankMapper
import com.tokopedia.settingbank.addeditaccount.domain.pojo.AddBankAccountPojo
import com.tokopedia.settingbank.banklist.data.SettingBankApi
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * @author by nisie on 6/22/18.
 */

class AddBankUseCase(val api: SettingBankApi,
                     val mapper: AddBankMapper) : UseCase<AddBankAccountPojo>() {

    override fun createObservable(requestParams: RequestParams): Observable<AddBankAccountPojo> {
        return api.addBankAccount(requestParams.parameters).map(mapper)
    }

}