package com.tokopedia.settingbank.banklist.data

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.settingbank.addeditaccount.domain.pojo.AddBankAccountPojo
import com.tokopedia.settingbank.addeditaccount.domain.pojo.EditBankAccountPojo
import com.tokopedia.settingbank.addeditaccount.domain.pojo.ValidateBankAccountPojo
import com.tokopedia.settingbank.banklist.domain.pojo.BankAccountListPojo
import com.tokopedia.settingbank.banklist.domain.pojo.DeleteBankAccountPojo
import com.tokopedia.settingbank.banklist.domain.pojo.SetDefaultBankAccountPojo
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

/**
 * @author by nisie on 6/8/18.
 */
interface SettingBankApi {

    @GET(SettingBankUrl.PATH_GET_BANK_ACCOUNT)
    fun getBankAccountList(@QueryMap params: HashMap<String, Any>):
            Observable<Response<DataResponse<BankAccountListPojo>>>

    @FormUrlEncoded
    @POST(SettingBankUrl.PATH_SET_DEFAULT_BANK_ACCOUNT)
    fun setDefaultBank(@FieldMap params: HashMap<String, Any>):
            Observable<Response<DataResponse<SetDefaultBankAccountPojo>>>

    @FormUrlEncoded
    @POST(SettingBankUrl.PATH_DELETE_BANK_ACCOUNT)
    fun deleteBank(@FieldMap params: HashMap<String, Any>):
            Observable<Response<DataResponse<DeleteBankAccountPojo>>>

}