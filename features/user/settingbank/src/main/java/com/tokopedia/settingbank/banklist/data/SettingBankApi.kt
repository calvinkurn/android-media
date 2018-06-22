package com.tokopedia.settingbank.banklist.data

import com.tokopedia.settingbank.addeditaccount.domain.pojo.AddBankAccountPojo
import com.tokopedia.settingbank.addeditaccount.domain.pojo.EditBankAccountPojo
import com.tokopedia.settingbank.banklist.domain.pojo.BankAccountListPojo
import com.tokopedia.settingbank.banklist.domain.pojo.DeleteBankAccountPojo
import com.tokopedia.settingbank.banklist.domain.pojo.SetDefaultBankAccountPojo
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import rx.Observable

/**
 * @author by nisie on 6/8/18.
 */
interface SettingBankApi {

    @FormUrlEncoded
    @POST(SettingBankUrl.PATH_GET_BANK_ACCOUNT)
    fun getBankAccountList(@FieldMap params: HashMap<String, Any>):
            Observable<Response<BankAccountListPojo>>

    @FormUrlEncoded
    @POST(SettingBankUrl.PATH_SET_DEFAULT_BANK_ACCOUNT)
    fun setDefaultBank(@FieldMap params: HashMap<String, Any>):
            Observable<Response<SetDefaultBankAccountPojo>>

    @FormUrlEncoded
    @POST(SettingBankUrl.PATH_DELETE_BANK_ACCOUNT)
    fun deleteBank(@FieldMap params: HashMap<String, Any>):
            Observable<Response<DeleteBankAccountPojo>>

    @FormUrlEncoded
    @POST(SettingBankUrl.PATH_ADD_BANK_ACCOUNT)
    fun addBankAccount(@FieldMap params: HashMap<String, Any>):
            Observable<Response<AddBankAccountPojo>>

    @FormUrlEncoded
    @POST(SettingBankUrl.PATH_EDIT_BANK_ACCOUNT)
    fun editBankAccount(@FieldMap params: HashMap<String, Any>):
            Observable<Response<EditBankAccountPojo>>


}